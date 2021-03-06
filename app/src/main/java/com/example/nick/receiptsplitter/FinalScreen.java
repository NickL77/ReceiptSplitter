package com.example.nick.receiptsplitter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class FinalScreen extends AppCompatActivity {

    Receipt receipt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_screen);

        Bundle extras = this.getIntent().getExtras();

        Log.e("USER1", "" + extras.containsKey("receipt"));

        receipt1 = (Receipt)extras.getSerializable("r");

        //Log.e("USER2", "" + (receipt1== null));

        receipt1.getAllPayments();

        int updateScreen = receipt1.people.size();

        TextView person1 = (TextView) findViewById(R.id.person1);
        TextView person2 = (TextView) findViewById(R.id.person2);
        TextView person3 = (TextView) findViewById(R.id.person3);
        TextView person4 = (TextView) findViewById(R.id.person4);
        TextView person5 = (TextView) findViewById(R.id.person5);
        TextView person6 = (TextView) findViewById(R.id.person6);
        TextView person7 = (TextView) findViewById(R.id.person7);
        TextView person8 = (TextView) findViewById(R.id.person8);
        TextView person9 = (TextView) findViewById(R.id.person9);
        TextView person10 = (TextView) findViewById(R.id.person10);

        TextView payment1 = (TextView) findViewById(R.id.payment1);
        TextView payment2 = (TextView) findViewById(R.id.payment2);
        TextView payment3 = (TextView) findViewById(R.id.payment3);
        TextView payment4 = (TextView) findViewById(R.id.payment4);
        TextView payment5 = (TextView) findViewById(R.id.payment5);
        TextView payment6 = (TextView) findViewById(R.id.payment6);
        TextView payment7 = (TextView) findViewById(R.id.payment7);
        TextView payment8 = (TextView) findViewById(R.id.payment8);
        TextView payment9 = (TextView) findViewById(R.id.payment9);
        TextView payment10 = (TextView) findViewById(R.id.payment10);


        if(updateScreen > 0){
            person1.setText(receipt1.people.get(0).name);
            payment1.setText(Double.toString(receipt1.people.get(0).payment));
            updateScreen -= 1;
        }

        if(updateScreen > 0){
            person2.setText(receipt1.people.get(1).name);
            payment2.setText(Double.toString(receipt1.people.get(1).payment));
            updateScreen -= 1;
        }

        if(updateScreen > 0){
            person3.setText(receipt1.people.get(2).name);
            payment3.setText(Double.toString(receipt1.people.get(2).payment));
            updateScreen -= 1;
        }

        if(updateScreen > 0){
            person4.setText(receipt1.people.get(3).name);
            payment4.setText(Double.toString(receipt1.people.get(3).payment));
            updateScreen -= 1;
        }

        if(updateScreen > 0){
            person5.setText(receipt1.people.get(4).name);
            payment5.setText(Double.toString(receipt1.people.get(4).payment));
            updateScreen -= 1;
        }

        if(updateScreen > 0){
            person6.setText(receipt1.people.get(5).name);
            payment6.setText(Double.toString(receipt1.people.get(5).payment));
            updateScreen -= 1;
        }

        if(updateScreen > 0){
            person7.setText(receipt1.people.get(6).name);
            payment7.setText(Double.toString(receipt1.people.get(6).payment));
            updateScreen -= 1;
        }

        if(updateScreen > 0){
            person8.setText(receipt1.people.get(7).name);
            payment8.setText(Double.toString(receipt1.people.get(7).payment));
            updateScreen -= 1;
        }

        if(updateScreen > 0){
            person9.setText(receipt1.people.get(8).name);
            payment9.setText(Double.toString(receipt1.people.get(8).payment));
            updateScreen -= 1;
        }

        if(updateScreen > 0){
            person10.setText(receipt1.people.get(9).name);
            payment10.setText(Double.toString(receipt1.people.get(9).payment));
            updateScreen -= 1;
        }

    }

}
