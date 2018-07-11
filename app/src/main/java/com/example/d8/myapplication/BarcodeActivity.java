package com.example.d8.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeActivity extends Activity {
    String TAG = "BarcodeActivity";

    SurfaceView surfaceView;
    CameraSource cameraSource;
    SurfaceHolder holder;
    BarcodeDetector barcodeDetector;
    Boolean QRFound = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_barcode);
        surfaceView = (SurfaceView) findViewById(R.id.preview);
        holder = surfaceView.getHolder();
        createCameraSource();
    }

    private void createCameraSource() {
        Context context = getApplicationContext();
        barcodeDetector = new BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the text recognizer to detect small pieces of text.
        cameraSource =
                new CameraSource.Builder(context, barcodeDetector)
                        .setFacing(CameraSource.CAMERA_FACING_BACK)
                        .setRequestedPreviewSize(1280, 1024)
                        .setRequestedFps(2.0f)
                        .setAutoFocusEnabled(true)
                        .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(BarcodeActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(surfaceView.getHolder());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() > 0 && QRFound == false) {
                    Log.d("Barcode", "Found QR code!");
                    //Barcode bcode = barcodes.valueAt(0);
                    //Log.d("Barcode", bcode.displayValue);

                    //int receiptID = Integer.parseInt(bcode.displayValue);
                    //DataController.getReceiptById(receiptID, "http://myvmlab.senecacollege.ca:6207/getOneReceipt.php", )

                    Intent returnReceipt = new Intent();
                    returnReceipt.putExtra("barcode", barcodes.valueAt(0));
                    setResult(RESULT_OK, returnReceipt);

                    QRFound = true; //Prevents this function from being called twice
                    finish();
                    Log.d("Barcode", "Finished!");
                }
            }
        });
    }
}
