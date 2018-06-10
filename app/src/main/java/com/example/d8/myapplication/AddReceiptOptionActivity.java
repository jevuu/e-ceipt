package com.example.d8.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddReceiptOptionActivity extends AppCompatActivity {

    Button ocrOption;
    Button realReceiptOption;
    Button addFormOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt_option);

        ocrOption = (Button)findViewById(R.id.add_receipt_btn_qrcode);
        ocrOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goOCR = new Intent(getBaseContext(), OCRActivity.class);
                startActivity(goOCR);
            }
        });

        realReceiptOption = (Button)findViewById(R.id.add_receipt_btn_scan_real_receipt);


        addFormOption = (Button)findViewById(R.id.add_receipt_btn_form);
        addFormOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addReceiptFormAct = new Intent(getBaseContext(), AddReceiptFormActivity.class);
                startActivity(addReceiptFormAct);
            }
        });
    }
}
