package com.example.d8.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/*
    The QR generation and scanning tech is fron Zxing opensourse:
    https://github.com/journeyapps/zxing-android-embedded/blob/master/README.md
 */


public class AddReceiptOptionActivity extends AppCompatActivity {

    Button ocrOption;
    Button realReceiptOption;
    Button addFormOption;
    Receipt receipt;
    String receiptID;

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
//<<<<<<< HEAD
//
////                Intent addRecpt = new Intent(getBaseContext(), OCRTextActivity.class);
////                startActivity(addRecpt);
//
//                Intent goOCR = new Intent(getBaseContext(), OCRActivity.class);
//                startActivity(goOCR);
//
//=======
                Intent addRecpt = new Intent(getBaseContext(), OCRTextActivity.class);
                addRecpt.putExtra("finish", true);
                addRecpt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(addRecpt);
                finish();
//>>>>>>> d26349acaa677ee84a080729b32f3eed8a781aee

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

//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String resultStr = result.getContents();
                if(resultStr.matches("[0-9]+") && resultStr.length() > 0){
                    try{
                        receipt = DataController.getReceiptById(Integer.parseInt(resultStr),"http://myvmlab.senecacollege.ca:6207/getOneReceipt.php",AddReceiptOptionActivity.this);
                        if(receipt.getReceipId()==null){
                            Log.i("RECEIPTIDINOPTION", "Receipt Null");
                            AlertDialog alertDialog = new AlertDialog.Builder(AddReceiptOptionActivity.this).create();
                            alertDialog.setTitle("Error");
                            alertDialog.setMessage("Can't find the receipt!");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent goToAdd = new Intent(AddReceiptOptionActivity.this, MenuActivity.class);
                                            startActivity(goToAdd);

                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }else{
                            Log.i("RECEIPTIDINOPTION", "Receipt not Null");
                            Intent intent = new Intent(AddReceiptOptionActivity.this,AddReceiptByQR.class);
                            intent.putExtra("RECEIPTID_", resultStr);
                            startActivity(intent);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    AlertDialog alertDialog = new AlertDialog.Builder(AddReceiptOptionActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("This is not a receipt's QR code!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent goToAdd = new Intent(AddReceiptOptionActivity.this, MenuActivity.class);
                                    startActivity(goToAdd);

                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
