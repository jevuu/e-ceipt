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
import android.graphics.Rect;
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
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Initial Class for Textual Activity Reading
//This class handles the primary bulk of text OCR
//Last Modifcation: 7/10/2018
public class OCRTextActivity extends AppCompatActivity {
  //  TextView txt_add;
    Bitmap ocrAble;
   // ImageView imgrecv;
    String imagePath = "";
 //   TextView txt_ocr;
    ArrayList<String> itemsRaw;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrtext);
      //  txt_add = (TextView) findViewById(R.id.edtErr);
     //   txt_ocr = (TextView) findViewById(R.id.ocr_recvText);
       //imgrecv = (ImageView) findViewById(R.id.ocr_pic);
        itemsRaw = new ArrayList<>();
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
               if(ocrAble != null) {
                   ocrAble.recycle();
               }
                 ocrAble = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                ocrAble = ocrAble.createBitmap(ocrAble,0,0, ocrAble.getWidth(), ocrAble.getHeight(),matrix, true);
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

        for (int index = 0; index < textBlocks.size(); index++) {
            //extract scanned text blocks here
            TextBlock tBlock = textBlocks.valueAt(index);
            System.out.println("Comparing this block: " + tBlock.getValue() + "========");
            boolean matched = false;
            //Check against every other text block
            for(int i = 0; i < textBlocks.size() && !matched; i++){
                //Dont self check
                if(i != index) {
                    //Pass the target block and the inspector block if they are similar on the y-axis
                    TextBlock tBlocks = textBlocks.valueAt(i);
                  if(checkBlockTolerance(tBlock, tBlocks)){
                      System.out.println("These Blocks Matched!--->" + tBlock.getValue() + "\n<--- || --->\n" + tBlocks.getValue() + "\n====END======");
                      //Pass the Lines to be matched where possible
                      matched = true;
                      checkLineTolerance(toLines(tBlock),toLines(tBlocks));
                      textBlocks.removeAt(index);
                      textBlocks.removeAt(i);

                  }

                }

            }
            if(!matched){
                List<? extends Text> inspected =  toLines(tBlock);
                for(int j = 0; j < inspected.size(); j++){

                 String t =  inspected.get(j).getValue();
                     if(t.matches("^\\D+\\d+.*") ) {
                         itemsRaw.add(t);
                         System.out.println("Item Added for Word + Number! " + t);

                     }
                }


            }

        }

        for(String t: itemsRaw){
            System.out.println(t);
        }

        sweepItems();
        Receipt nx = new Receipt();
        parseItems(nx);
        System.out.print("\n\nDone!\n\n");

    }

    //Todo
    //This function takes the raw items and attempts to turn them into objects. These objects are then verified or in simple mode tossed(trys only to find the total)
    private void parseItems(Receipt nx) {


        Pattern current = Pattern.compile("\\d+\\.\\d+");
        Matcher match;

        //Extract Item value from item and add both as an item
        for(String t: itemsRaw){
            match = current.matcher(t);
            if(match.find()){
                try {
                    double tx = Double.parseDouble(match.group());
                    t = t.trim().replaceAll("\\d+\\.\\d+","");
                    t = t.replaceAll("\\$", "");
                    t = t.replaceAll("( +)"," ");


                    nx.addItem(t,"",tx);
                }catch(Exception e){
                    System.out.println("\n\n**Error in parseItems!\n\n");
                }
            }
        }
        



        //Find total

        //Place into receipt


    }
    //This function implements the Levenshtein(LEV-EN-SHTEEN) distance in determining key values such as Total, SubTotal, HST, CREDIT-CARD3
    //@Param 1: The string for distance
    //@Param 2: The target to match
    //Overall a distance value of 2 is plausible to be a near match.
    public static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    //This function enforces text rules upon the raw items
    //Rule 1: Items must not be duplicated where the number and word are the same. Both must also be present to not be tossed
    //Ex: a  1
    //    1  a
    //Are considered the same and will be tossed
    //Rule 2: $x.00 are considered values that will be added as an item(or totals)
    //Rule 3: Total,SubTotal,HST are considered KEY items and must have a number attached to them if possible
    private void sweepItems() {
        System.out.println("==Original List==");
        for(String t : itemsRaw){
            System.out.println(t);
        }
        for(int i = 0; i <itemsRaw.size(); i++){
            if(itemsRaw.get(i).matches("^[0-9].*")){
                System.out.println("Protected (Has Starting Digit): " + itemsRaw.get(i));

            }else if(itemsRaw.get(i).matches("^.*([0-9])+.*$")){
                System.out.println("Protected (Contains Number): " + itemsRaw.get(i));

            }else{
                System.out.println("Killed: " + itemsRaw.get(i));
                itemsRaw.remove(i);
            }
        }
        System.out.println("==New List==");
        for(String t : itemsRaw){
            System.out.println(t);
        }


    }

    //This function analyses line by line of textblocks for valid data.
    //The closest y axis match is considered to be the same line.
    //When it finds a valid line, it adds it to the rawItems list for text enforcement
    //Bug: Items are duplicated between each textblock
    private void checkLineTolerance(List<? extends Text> inspected, List<? extends Text> target) {
        System.out.println("Checking Line Tolerance");
        String item = "";
        int yTolerance = 3000;
        if(inspected.size() == 1 && target.size() == 1){
            item = inspected.get(0).getValue() + " " + target.get(0).getValue();
            System.out.println("This is a raw item: " + item);
            itemsRaw.add(item);
        }else{
            //For each item in inspected...
            for(int i = 0; i < inspected.size(); i++){
                int inspY = inspected.get(i).getBoundingBox().centerY();
                yTolerance = 3000;
                //Check against each item in target...
                for(int j = 0; j < target.size(); j++){
                  int targY = target.get(j).getBoundingBox().centerY();
                  int dif = Math.abs(inspY - targY);
                  if(yTolerance > dif && dif < 120){
                      yTolerance = dif;
                      System.out.println(yTolerance + " is now closest");
                      item = inspected.get(i).getValue() + " " + target.get(j).getValue();
                  }
                    System.out.println(yTolerance + "< J Lines -->"+ inspected.get(i).getValue() + " <--||--> " + target.get(j).getValue());

                }

                System.out.println("***Closest Item was " + item + "****");
                itemsRaw.add(item);
            }

        }

        System.out.println("\n***Finished Line Tolerance!***\n");
    }

    //Determines if the textblocks are relatively the same line
    public boolean checkBlockTolerance(TextBlock a, TextBlock b){
        int yTolerance = a.getBoundingBox().centerY() - b.getBoundingBox().centerY();
        if(yTolerance <= 150 && yTolerance >= -150) {
           // System.out.println(yTolerance + "-->" + a.getValue() + "<--Good-->" + b.getValue());

            return true;
        }else{
            System.out.println("Block Match Failed!");
            return false;
        }

    }

    //Returns the Lines of a given textblock
    public List<? extends Text> toLines(TextBlock e){
        return e.getComponents();
    }

    //=========================================================//


}
