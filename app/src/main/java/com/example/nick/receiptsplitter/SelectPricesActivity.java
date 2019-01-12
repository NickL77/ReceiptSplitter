package com.example.nick.receiptsplitter;

import android.Manifest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class SelectPricesActivity  extends AppCompatActivity {


    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_prices);

        imageView = (ImageView)findViewById(R.id.imageView);

        Intent intent = getIntent();
        byte[] byteArrayExtra = getIntent().getByteArrayExtra("photo");

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayExtra, 0, byteArrayExtra.length);

        imageView.setImageBitmap(bitmap);
    }

}
