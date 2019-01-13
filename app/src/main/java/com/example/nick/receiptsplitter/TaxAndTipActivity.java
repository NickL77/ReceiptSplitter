package com.example.nick.receiptsplitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TaxAndTipActivity extends AppCompatActivity {

    double tax = -1, tip = -1, total = -1;

    Button cont;
    TextView subtotalDisp, totalDisp;
    EditText taxInput, tipInput;
    Receipt receipt1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subtotal_and_tax);

        cont = (Button) findViewById(R.id.btnCalculate);

        subtotalDisp = (TextView) findViewById(R.id.subtotalDisplay);
        totalDisp = (TextView) findViewById(R.id.totalDisplay);

        taxInput = (EditText) findViewById(R.id.taxInput);
        tipInput = (EditText) findViewById(R.id.tipInput);

        tipInput.addTextChangedListener(textWatcher);
        taxInput.addTextChangedListener(textWatcher);

        Bundle extras = getIntent().getExtras();
        receipt1 = (Receipt)extras.getSerializable("receipt");

        receipt1.calcSubtotal();
        subtotalDisp.setText(Double.toString(receipt1.subtotal));

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receipt1.setTotal(Double.parseDouble(totalDisp.getText().toString()));

                Intent finalActivity = new Intent(TaxAndTipActivity.this, FinalScreen.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("receipt", receipt1);
                finalActivity.putExtras(bundle);
                startActivity(finalActivity);
            }
        });


    }

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //here, after we introduced something in the EditText we get the string from it
            String taxString = taxInput.getText().toString();
            String tipString = tipInput.getText().toString();
            if (taxString.length() > 0 && tipString.length() > 0) {
                tax = Double.parseDouble(taxString);
                tip = Double.parseDouble(tipString);
                total = receipt1.subtotal + tip + tax;
                totalDisp.setText(Double.toString(total));
            }
        }
    };
}