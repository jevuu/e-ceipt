package com.example.d8.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONException;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class AddReceiptOptionActivity extends AppCompatActivity {

    String USERID = Information.authUser.getUserId();

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
                Intent QRScan = new Intent(getBaseContext(), BarcodeActivity.class);
                startActivityForResult(QRScan, REQUEST_CODE);
                //startActivity(QRScan);
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

    //Called when QR scanner picks up a code and returns to this activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Barcode", "onActivityResult called!");
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {

                //Get Barcode from parcel and extract the receciptID
                Barcode barcode = data.getParcelableExtra("barcode");
                Log.d("Barcode", barcode.displayValue);
                int receiptID = Integer.parseInt(barcode.displayValue);

                //Get the receipt from database then copy by using addReceipt functions in DataController
                try {
                    Receipt sourceReceipt = DataController.getReceiptById(receiptID, "http://myvmlab.senecacollege.ca:6207/getOneReceipt.php", this);
                    DataController.addReceiptToLocal(USERID, sourceReceipt, this);
                    DataController.addReceiptToDB(sourceReceipt, "http://myvmlab.senecacollege.ca:6207/addReceipt.php", this);
                    Intent refresh = new Intent(getBaseContext(), MenuActivity.class);
                    startActivity(refresh);
                } catch (JSONException e){
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
