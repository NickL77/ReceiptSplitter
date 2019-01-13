package com.example.nick.receiptsplitter;

import android.Manifest;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.googlecode.tesseract.android.TessBaseAPI;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.graphics.Bitmap.Config.ARGB_8888;


public class SelectPricesActivity  extends AppCompatActivity {

    Receipt receipt = new Receipt(16.50,0,false,0.06,0);

    Person p1 = new Person("p1", 135, 206, 250);
    Person p2 = new Person("p2", 186, 85, 211);
    Person p3 = new Person("p3", 124, 252, 0);
    Person p4 = new Person("p4", 255, 127, 80);

    Person currPerson = p1;

    Bitmap photo, mutableBitMap, croppedBitMap;
    Uri photoUri;
    ImageView imageView;
    ImageButton addButton, finishButton;
    TextView OCRval;
    int rectHeight = 150, rectWidth = 400, rectX = 100, rectY = 300;
    int fingerX = 0, fingerY = 0;
    String datapath = "";
    public TessBaseAPI mTess;

    ArrayList<Rect> Boxes = new ArrayList<Rect>();
    ArrayList<Item> Items = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_prices);

        initTesseract();

        receipt.addPerson(p1);
        receipt.addPerson(p2);
        receipt.addPerson(p3);
        receipt.addPerson(p4);

        imageView = (ImageView)findViewById(R.id.imageView);
        addButton = (ImageButton)findViewById(R.id.imageButton);
        finishButton = (ImageButton)findViewById(R.id.finishButton);

        // access image based on URI sent by main activity
        Bundle extras = getIntent().getExtras();

        if (extras != null && extras.containsKey("photo")) {
            photoUri = Uri.parse(extras.getString("photo"));
        }

        this.getContentResolver().notifyChange(photoUri, null);
        ContentResolver cr = this.getContentResolver();
        try
        {
            photo = android.provider.MediaStore.Images.Media.getBitmap(cr, photoUri);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "Failed to load", e);
        }

        imageView.setImageBitmap(photo);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mutableBitMap = photo.copy(ARGB_8888, true);

                Canvas canvas = new Canvas(mutableBitMap);

                Paint paint = new Paint();
                paint.setStrokeWidth(10);
                paint.setColor(Color.rgb(51,255,51));
                paint.setStyle(Paint.Style.STROKE);

                Rect rectangle = new Rect(rectX, rectY + rectHeight, rectX + rectWidth, rectY);

                canvas.drawRect(rectangle, paint);

                imageView.setImageBitmap(mutableBitMap);

            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //overlap case: simply add currPerson to the item it overlaps with
                Item checkO = checkOverlap();
                if (checkO != null){
                    checkO.addSplitter(currPerson);
                }
                //non overlap, create new item, add to list, add box to list
                else {
                    croppedBitMap = Bitmap.createBitmap(mutableBitMap, rectX, rectY, rectWidth, rectHeight);
                    String result = runOCR(croppedBitMap);

                    double tess2d = Tess2Double(result);

                    if (tess2d == -1){
                        Toast.makeText(getApplicationContext(), "Error reading value. \n Please Try again.",
                                Toast.LENGTH_LONG).show();

                    }
                    else {
                        Item newItem = new Item(tess2d, currPerson);
                        receipt.addItem(newItem);

                        Items.add(newItem);
                        Boxes.add(new Rect(rectX, rectY + rectHeight, rectX + rectHeight, rectY));

                        Toast.makeText(getApplicationContext(), Double.toString(tess2d),
                                Toast.LENGTH_LONG).show();
                    }
                }

                for (Item i: Items){
                    Log.e("ITEMS", "" + i.price);
                }

            }
        });


        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int fingerX = (int) event.getX();
                int fingerY = (int) event.getY();
                int eventaction = event.getAction();

                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        mutableBitMap = photo.copy(ARGB_8888, true);

                        Canvas canvas = new Canvas(mutableBitMap);

                        Paint paint = new Paint();
                        paint.setStrokeWidth(10);
                        paint.setColor(Color.rgb(p1.rgb1,p1.rgb2,p1.rgb3));
                        paint.setStyle(Paint.Style.STROKE);

                        rectX = fingerX * 2;
                        rectY = fingerY * 2;

                        Log.e("rectX", String.valueOf(rectX));

                        Rect rectangle = new Rect(rectX, rectY + rectHeight, rectX + rectWidth, rectY);

                        canvas.drawRect(rectangle, paint);

                        imageView.setImageBitmap(mutableBitMap);

                }
                return true;
            }
        });

    }

    public Item checkOverlap(){
        for (int i = 0; i < Boxes.size(); i++){
            Rect b = Boxes.get(i);
            int xLeft = Math.max(b.left, rectX);
            int xRight = Math.min(b.right, rectX + rectWidth);
            int yTop = Math.min(b.top, rectY + rectHeight);
            int yBottom = Math.max(b.bottom, rectY);
            if (yTop > yBottom && xRight > xLeft){
                int overlapArea = (xRight - xLeft) * (yTop - yBottom);
                int currArea = (b.top - b.bottom) * (b.right - b.left);
                int pastArea = rectHeight * rectWidth;
                if (2*overlapArea > 0.7 * (currArea + pastArea)){
                    return Items.get(i);
                }
            }
        }
        return null;
    }

    public double Tess2Double(String str){
        String s = "";
        for (int i = 0; i < str.length(); i++){
            if (Character.isDigit(str.charAt(i))){
                s += str.charAt(i);
            }
            else if (str.charAt(i) == '.' || str.charAt(i) == '-'){
                s += ".";
            }
        }
        if (s == ""){
            return -1;
        }
        return Double.parseDouble(s);
    }

    public void initTesseract(){
        datapath = getFilesDir()+ "/tesseract/";
        mTess = new TessBaseAPI();

        checkFile(new File(datapath + "tessdata/")); //check if file exists; if it doesn't, copy it over

        mTess.init(datapath, "eng");
    }

    private void checkFile(File directory) {
        if (!directory.exists()&& directory.mkdirs()){
            copyAssets();
        }
        if(directory.exists()) {
            String assetPath = datapath+ "/tessdata/eng.traineddata";
            File data = new File(assetPath);
            if (!data.exists()) {
                copyAssets();
            }
        }
    }

    private void copyAssets() {
        try {
            String filepath = datapath + "/tessdata/eng.traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/eng.traineddata");//get from assets
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();
            File file = new File(filepath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String runOCR(Bitmap image){

        String OCRresult = null;

        mTess.setImage(image);
        OCRresult = mTess.getUTF8Text();

        return OCRresult;
    }


}
