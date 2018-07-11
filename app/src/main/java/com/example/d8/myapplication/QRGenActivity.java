package com.example.d8.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRGenActivity extends AppCompatActivity {

    int receiptID;      //Holds the receipt ID that will be passed in
    String QRString;    //Holds the receipt ID as a string
    ImageView QRView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Get the receiptID passed in from ReceiptDetailActivity and convert to string
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            receiptID = extras.getInt("receiptID");
            QRString = String.valueOf(receiptID);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgen);

        QRView = (ImageView) findViewById(R.id.QRView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Build the QR code
        //String text="TEXT"; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(QRString, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            QRView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

}
