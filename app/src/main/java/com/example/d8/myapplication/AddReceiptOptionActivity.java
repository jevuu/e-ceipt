package com.example.d8.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
//                Intent goOCR = new Intent(getBaseContext(), OCRActivity.class);
//                startActivity(goOCR);
                IntentIntegrator integrator = new IntentIntegrator(AddReceiptOptionActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan a QR code");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
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

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String resultStr = result.getContents();
                Intent intent = new Intent(AddReceiptOptionActivity.this,AddReceiptByQR.class);
                intent.putExtra("RECEIPTID_", resultStr);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
