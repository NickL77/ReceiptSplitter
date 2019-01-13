package com.example.nick.receiptsplitter;

import android.content.ContentResolver;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.googlecode.tesseract.android.TessBaseAPI;

import android.Manifest;
import android.graphics.BitmapFactory;
import android.support.constraint.solver.widgets.Rectangle;
import android.widget.Button;
import android.content.Intent;
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
    Person p1 = new Person("p1", 255, 110, 108);
    Person p2 = new Person("p2", 255, 210, 119);
    Person p3 = new Person("p3", 153, 237, 127);
    Person p4 = new Person("p4", 111, 230, 241);
    Person p5 = new Person("p5", 125, 134, 253);
    Person p6 = new Person("p6", 239, 135, 239);
    Person currPerson = p1;

    Uri photoUri;
    TextView OCRval;
    ImageView imageView;
    ImageButton checkmarkButton;
    Bitmap photo, mutableBitMap, croppedBitMap;
    Button addButton1, addButton2, addButton3, addButton4, addButton5, addButton6, finishButton;

    TessBaseAPI mTess;

    String datapath = "";
    int fingerX = 0, fingerY = 0;
    int currR = 0, currG = 0, currB = 0;
    int rectHeight = 150, rectWidth = 400, rectX = 100, rectY = 300;
    int origHeight = 150, origWidth = 400;

    ArrayList<Rect> Boxes = new ArrayList<>();
    ArrayList<Item> Items = new ArrayList<>();

    private ScaleGestureDetector zoomDetector;
    private double zoomFactor = 1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highlighting);

        initTesseract();

        zoomDetector = new ScaleGestureDetector(this, new ScaleListener());

        imageView = findViewById(R.id.imageView);
        addButton1 = findViewById(R.id.btnCircle1);
        addButton2 = findViewById(R.id.btnCircle2);
        addButton3 = findViewById(R.id.btnCircle3);
        addButton4 = findViewById(R.id.btnCircle4);
        addButton5 = findViewById(R.id.btnCircle5);
        addButton6 = findViewById(R.id.btnCircle6);
        checkmarkButton = findViewById(R.id.btnCheckmark);
        finishButton = findViewById(R.id.btnFinish);

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

        addButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receipt.addPerson(p1);
                currPerson = p1;
                currR = p1.rgb1;
                currG = p1.rgb2;
                currB = p1.rgb3;
                drawOnPic();
            }
        });
        addButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receipt.addPerson(p2);
                currPerson = p2;
                currR = p2.rgb1;
                currG = p2.rgb2;
                currB = p2.rgb3;
                drawOnPic();
            }
        });
        addButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receipt.addPerson(p3);
                currPerson = p3;
                currR = p3.rgb1;
                currG = p3.rgb2;
                currB = p3.rgb3;
                drawOnPic();
            }
        });
        addButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receipt.addPerson(p4);
                currPerson = p4;
                currR = p4.rgb1;
                currG = p4.rgb2;
                currB = p4.rgb3;
                drawOnPic();
            }
        });
        addButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receipt.addPerson(p5);
                currPerson = p5;
                currR = p5.rgb1;
                currG = p5.rgb2;
                currB = p5.rgb3;
                drawOnPic();
            }
        });
        addButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receipt.addPerson(p6);
                currPerson = p6;
                currR = p6.rgb1;
                currG = p6.rgb2;
                currB = p6.rgb3;
                drawOnPic();
            }
        });

        addButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receipt.addPerson(p6);
                currPerson = p6;
                currR = p6.rgb1;
                currG = p6.rgb2;
                currB = p6.rgb3;
                drawOnPic();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switch2final = new Intent(this, FinalScreen.class);
                //switch2select.putExtra("photo", photoURI.toString());
                startActivity(switch2final);
            }
        });



        checkmarkButton.setOnClickListener(new View.OnClickListener() {
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

                Log.e("ITEMS", "START OF NEW ITERATION");
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

                zoomDetector.onTouchEvent(event);
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                        rectX = fingerX * 2;
                        rectY = fingerY * 2;
                        drawOnPic();

                }
                return true;
            }
        });

    }

    public void drawOnPic(){
        mutableBitMap = photo.copy(ARGB_8888, true);

        Canvas canvas = new Canvas(mutableBitMap);

        Paint paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.rgb(currR, currG, currB));
        paint.setStyle(Paint.Style.STROKE);

        Rect rectangle = new Rect(rectX, rectY + rectHeight, rectX + rectWidth, rectY);

        canvas.drawRect(rectangle, paint);

        imageView.setImageBitmap(mutableBitMap);
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            zoomFactor *= scaleGestureDetector.getScaleFactor();
            zoomFactor = Math.min(5,Math.max(zoomFactor, 0.2));//limit scalefactor to be in range 0.1 to 10

            Log.e("zoomFactor", String.valueOf(zoomFactor));


            int centerX = rectX+rectWidth/2;
            int centerY = rectY+rectHeight/2;

            //scale the rectangle
            rectWidth = (int) (origHeight * zoomFactor);
            rectHeight = (int) (origWidth * zoomFactor);


            //reposition rectX and rectY based on scaling and center coordinate
            rectX = centerX - rectWidth/2;
            rectY = centerY - rectHeight/2;

            drawOnPic();

            return true;
        }
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
                if (2*overlapArea > 0.5 * (currArea + pastArea)){
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
