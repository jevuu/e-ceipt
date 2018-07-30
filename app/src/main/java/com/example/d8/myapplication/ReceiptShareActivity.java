package com.example.d8.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class ReceiptShareActivity extends AppCompatActivity {
    Button sendEmail;
    EditText emailEditText;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_share);

        String receiptID = getIntent().getStringExtra("RECEIPTID");
        String userName = Information.authUser.name;

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(receiptID, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ((ImageView) findViewById(R.id.qr_code_image_view)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        emailEditText = (EditText)findViewById(R.id.share_email);


        sendEmail = (Button)findViewById(R.id.send_email_btn);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
                i.putExtra(Intent.EXTRA_SUBJECT, "E-Ceipt sharing");
                String name = email.substring( 0, email.indexOf("@"));
                String nameCap = name.substring(0, 1).toUpperCase() + name.substring(1);
                String link = "http://www.eceipt.ca/"+receiptID;
                String bodyContent = "Dear "+nameCap+",\n\nYour friend just share a receipt to you. Click the link below to get receipt in e-Ceipt:\n "+link+"\n\nBest regard!\n"+userName;
                i.putExtra(Intent.EXTRA_TEXT   , bodyContent);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getBaseContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
