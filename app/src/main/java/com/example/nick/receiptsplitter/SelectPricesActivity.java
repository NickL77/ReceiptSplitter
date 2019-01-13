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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.graphics.Bitmap.Config.ARGB_8888;


public class SelectPricesActivity  extends AppCompatActivity {

    Bitmap photo, mutableBitMap, croppedBitMap;
    Uri photoUri;
    ImageView imageView;
    ImageButton addButton, finishButton;
    TextView OCRval;
    int rectHeight = 100, rectWidth = 200, rectX = 100, rectY = 300;
    int fingerX = 0, fingerY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_prices);

        imageView = (ImageView)findViewById(R.id.imageView);
        addButton = (ImageButton)findViewById(R.id.imageButton);
        finishButton = (ImageButton)findViewById(R.id.finishButton);
        OCRval = (TextView)findViewById(R.id.OCRval);




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
                croppedBitMap = Bitmap.createBitmap(mutableBitMap, rectX, rectY, rectWidth, rectHeight);

                imageView.setImageBitmap(croppedBitMap);

                //begin OCR
                //String result = runOCR(croppedBitMap);
                //Log.e("User",result);

                OCRval.setTextColor(Color.GREEN);
                OCRval.setText("test");


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
                        paint.setColor(Color.rgb(51,255,51));
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

    private String runOCR(Bitmap image){

        String OCRresult = null;
//

//
        //mTess.setImage(image);
//        OCRresult = mTess.getUTF8Text();

        return OCRresult;
    }


}
