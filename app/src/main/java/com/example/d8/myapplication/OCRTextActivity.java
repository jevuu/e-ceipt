package com.example.d8.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//Initial Class for Textual Activity Reading
//This class handles the primary bulk of text OCR
//Last Modifcation: 6/11/2018
public class OCRTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrtext);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    //==================== Get User Photo/Camera =======================//

//This function executes a valid bitmap intent from the camera.
    private void takePhoto() {
        Intent take = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (take.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(take, REQUEST_IMAGE_CAPTURE);
        }

    }

    //Retuns the photo apps bitmap for use.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
        }
    }

    //=========================================================//


}
