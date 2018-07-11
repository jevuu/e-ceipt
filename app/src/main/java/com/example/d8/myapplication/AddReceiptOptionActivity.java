package com.example.d8.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class AddReceiptOptionActivity extends AppCompatActivity {

    Button ocrOption;
    Button realReceiptOption;
    Button addFormOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt_option);

        ocrOption = (Button)findViewById(R.id.add_receipt_btn_qrcode);
        /*
        ocrOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goOCR = new Intent(getBaseContext(), BarcodeActivity.class);
                startActivity(goOCR);
            }
        });*/
        // Scan Handler
        ocrOption.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), BarcodeActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        realReceiptOption = (Button)findViewById(R.id.add_receipt_btn_scan_real_receipt);
        realReceiptOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addRecpt = new Intent(getBaseContext(), OCRTextActivity.class);
                startActivity(addRecpt);
            }
        });

        addFormOption = (Button)findViewById(R.id.add_receipt_btn_form);
        addFormOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addReceiptFormAct = new Intent(getBaseContext(), AddReceiptFormActivity.class);
                startActivity(addReceiptFormAct);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Barcode barcode = data.getParcelableExtra("barcode");
                Log.d("Barcode", barcode.displayValue);
            }
        }
    }
}
