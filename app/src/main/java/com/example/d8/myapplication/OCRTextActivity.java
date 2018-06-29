package com.example.d8.myapplication;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//Initial Class for Textual Activity Reading
//This class handles the primary bulk of text OCR
//Last Modifcation: 6/11/2018
public class OCRTextActivity extends AppCompatActivity {
    TextView txt_add;
    Bitmap ocrAble;
    ImageView imgrecv;
    String imagePath = "";
    TextView txt_ocr;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrtext);
        txt_add = (TextView) findViewById(R.id.edtErr);
        txt_ocr = (TextView) findViewById(R.id.ocr_recvText);
        imgrecv = (ImageView) findViewById(R.id.ocr_pic);
        takePhoto();
    }

    //==================== Get User Photo/Camera =======================//
//This function executes a valid bitmap intent from the camera.

    private void takePhoto() {
        try {
            //Checks for permissions of Camera and External Write
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ){

                //Request Permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            100);
                }

            }else {

                //Attempt to take a photo, make a file for the photo
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                 //Generate File
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "eceipt_" + timeStamp + ".jpg";

                    File storageDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);
                    imagePath = storageDir.getAbsolutePath() + "/" + imageFileName;

                    File file = new File(imagePath);
                    //Uri outputFileUri = Uri.fromFile(file);
                    Uri outputFileUri = FileProvider.getUriForFile(OCRTextActivity.this, OCRTextActivity.this.getApplicationContext().getPackageName(), file);
                   //Request Intent to return full size uri
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                    //Take the photo
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

        }catch(Exception e){
            Toast.makeText(this, "Something is wrong:  " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            txt_add.setText(e.getMessage());


        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            takePhoto();
        }else{
            Toast.makeText(this, "You Cannot take a photo by denying the permissions!!",
                    Toast.LENGTH_SHORT).show();
        }


    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    //Retuns the photo apps bitmap for use.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            File imgFile = new  File(imagePath);

            if(imgFile.exists()){
                 ocrAble = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                ocrAble = ocrAble.createBitmap(ocrAble,0,0, ocrAble.getWidth(), ocrAble.getHeight(),matrix, true);
                imgrecv.setImageBitmap(ocrAble);
                imgrecv.setScaleType(ImageView.ScaleType.FIT_XY);
                scanOCR();

            }


        }
    }

    public void scanOCR(){
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
        if(!textRecognizer.isOperational()) {
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this,"Low Storage", Toast.LENGTH_LONG).show();
            }
        }
        Frame imageFrame = new Frame.Builder()
                .setBitmap(ocrAble)
                .build();
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));

            stringBuilder.append(textBlock.getValue());
            stringBuilder.append("\n");
        }
        txt_ocr.setText(stringBuilder);

    }

    //=========================================================//


}
