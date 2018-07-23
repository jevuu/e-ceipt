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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
//Last Modifcation: 7/22/2018
public class OCRTextActivity extends AppCompatActivity implements View.OnClickListener {
    Bitmap ocrAble;
    String imagePath = "";
    Button ocr_fwd;
    Button ocr_bak;
    Button ocr_del;
    Button ocr_scn;
    Button ocr_fin;
    ProgressBar pb;

    TextView ocr_instLow;
    TextView ocr_itemName;
    TextView ocr_itemPrice;


    ArrayList<String> itemsRaw;
    Receipt nx = new Receipt();
    static final int REQUEST_IMAGE_CAPTURE = 1;


    //Iterator for items list
    int itemItr = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocrtext);

        //Set button listners, disable until photo is taken
        ocr_bak = (Button) findViewById(R.id.ocr_backItem);
        ocr_bak.setOnClickListener(this);
        ocr_bak.setEnabled(false);
        ocr_del = (Button) findViewById(R.id.ocr_deleteItem);
        ocr_del.setOnClickListener(this);
        ocr_del.setEnabled(false);
        ocr_fwd = (Button) findViewById(R.id.ocr_fwdItem);
        ocr_fwd.setOnClickListener(this);
        ocr_fwd.setEnabled(false);
        ocr_fin = (Button) findViewById(R.id.ocr_finish);
        ocr_fin.setOnClickListener(this);
        ocr_fin.setEnabled(false);
        ocr_fin.setVisibility(View.GONE);

        ocr_scn = (Button) findViewById(R.id.ocr_executePhoto);
        ocr_scn.setOnClickListener(this);

        nx.setUserId(Information.authUser.getFirebaseUID());
        nx.setTax(0.00);

        pb = (ProgressBar) findViewById(R.id.progressBar);

        ocr_instLow = (TextView) findViewById(R.id.ocr_instruc2);
        itemsRaw = new ArrayList<>();

        ocr_itemName = (TextView) findViewById(R.id.ocr_itemName);
        ocr_itemPrice = (TextView) findViewById(R.id.ocr_itemPrice);
    }

    //Button override, executes whats needed
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ocr_backItem:

                updateReceipt();
                if(itemItr > -1){
                itemItr--;
                }
                if(itemItr <= -1 ){
                    itemItr = -1; //Protective
                    ocr_itemName.setTextColor(getResources().getColor(R.color.colorEceiptOrange));
                    ocr_itemPrice.setTextColor(getResources().getColor(R.color.colorEceiptOrange));
                }
                displayItem();
                System.out.println(itemItr);
                updatePb();

                break;
            case R.id.ocr_deleteItem:
                deleteItem();
                updatePb();
                if(itemItr <= -1 ){
                    itemItr = -1; //Protective
                    ocr_itemName.setTextColor(getResources().getColor(R.color.colorEceiptOrange));
                    ocr_itemPrice.setTextColor(getResources().getColor(R.color.colorEceiptOrange));
                }
                displayItem();
                  break;
            case R.id.ocr_fwdItem:

                updateReceipt();
                if(itemItr >= -1 && itemItr < nx.getItems().size() - 1){
                    itemItr++;
                }
                if(itemItr <= -1 ){
                    itemItr = -1; //Protective

                    //Makes text orange
                    ocr_itemName.setTextColor(getResources().getColor(R.color.colorEceiptOrange));
                    ocr_itemPrice.setTextColor(getResources().getColor(R.color.colorEceiptOrange));
                }else{
                    //Makes text white
                    ocr_itemName.setTextColor(getResources().getColor(R.color.colorEceiptWhite));
                    ocr_itemPrice.setTextColor(getResources().getColor(R.color.colorEceiptWhite));
                }

                updatePb();
                displayItem();
                System.out.println(itemItr);
                break;
            case R.id.ocr_finish:


                //When signing out, this prevents the user 'backing' into the app.
                //finish() destroys the home activity as well.
                Intent myIntent = new Intent(this, AddReceiptFormActivity.class);
                myIntent.putExtra("ocrScan", nx);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);
                finish();
                break;
            case R.id.ocr_executePhoto:
               takePhoto();
               ocr_scn.setEnabled(false);
               break;
            default:
                break;
        }
    }

    //Deletes the item from the receipt
    private void deleteItem() {
        if(itemItr != -1){
         nx.getItems().remove(itemItr);
            Toast.makeText(this, "Item Deleted",
                    Toast.LENGTH_SHORT).show();
            itemItr = -1;

        }else{
            Toast.makeText(this, "You cannot delete the total!",
                    Toast.LENGTH_SHORT).show();
        }


    }

    //This function takes the text from each textview and sets it as the receipt's values
    private void updateReceipt() {

        if(itemItr == -1){
            nx.setTotalCost(Double.parseDouble(String.valueOf(ocr_itemPrice.getText())));
        }else {

            System.out.print("\n\nIndex Access: " + itemItr + " Size of arr: " + itemsRaw.size()+ "\n\n");
            System.out.print("\n\nIndex Access: " + itemItr + " Size of NX:  " + nx.getItems().size() + "\n\n");
            nx.getItembyId(itemItr).itemName = String.valueOf(ocr_itemName.getText());
            nx.getItembyId(itemItr).itemPrice = Double.parseDouble(String.valueOf(ocr_itemPrice.getText()));
        }
    }

    //Updates the progress bar
    //Checks for older Android APIs
    private void updatePb() {
        pb.setMax(nx.getItems().size());
        int i = nx.getItems().size();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(i != 0) {
                System.out.print("\n\nMAX PB: " + nx.getItems().size() +"ItemITR " + itemItr + "\n\n");
                pb.setProgress(itemItr + 1, true);
            }
        }else{
            if(i != 0) {
                pb.setProgress(itemItr + 1/ i);
            }else {
                pb.setProgress(0);
            }
        }
    }

    //This function sets the item display box to the itemItr's position with get() if available
    private void displayItem() {

        if(itemItr == -1){
            ocr_itemPrice.setText(String.valueOf(nx.getTotalCost()));
            ocr_itemName.setText("Total");
        }else{
            try{
                ocr_itemPrice.setText(String.valueOf(nx.getItembyId(itemItr).itemPrice));
                ocr_itemName.setText(nx.getItembyId(itemItr).itemName);
            }catch(Exception e){
                //...//
            }

        }
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
               try {
                   ocrAble = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

               }catch(Exception e){
                   Toast.makeText(this,"You dont appear to have enough memory! Try Restarting the App!", Toast.LENGTH_LONG).show();
               }
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
            System.out.println("Comparing this block: " + tBlock.getValue() + "\n========");
            boolean matched = false;

            //Check against every other text block
           for(int ii = 0; ii < 4; ii++) {
               for (int i = 0; i < textBlocks.size(); i++) {
                   //Dont self check
                   if (i != index) {
                       //Pass the target block and the inspector block if they are similar on the y-axis
                       TextBlock tBlocks = textBlocks.valueAt(i);
                       if (checkBlockTolerance(tBlock, tBlocks)) {
                           System.out.println("These Blocks Matched!--->" + tBlock.getValue() + "\n<--- || --->\n" + tBlocks.getValue() + "\n====END======");
                           //Pass the Lines to be matched where possible
                           checkLineTolerance(toLines(tBlock), toLines(tBlocks));
                       }
                   }
               }
           }

            List<? extends Text> inspected =  toLines(tBlock);
                for(int j = 0; j < inspected.size(); j++){
                 String t =  inspected.get(j).getValue();
                     if(t.matches("\\d+\\.\\d+") ) {
                         itemsRaw.add(t);
                         System.out.println("Item Added for Word + Number! " + t);

                     }
                }

        }
        sweepItems();
        parseItems(nx);
        renderItems();


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
                    double tVal = Double.parseDouble(match.group());
                    //Extract the number/decimal and add the item to the receipt

                    t = t.trim().replaceAll("\\d+\\.\\d+","");
                    t = t.replaceAll("\\$", "");
                    t = t.replaceAll("( +)"," ");


                        if (distance(t.toLowerCase(), "total") <= 3 || t.toLowerCase().contains("total")) {
                            nx.setTotalCost(tVal);
                        }else if(distance(t.toLowerCase(), "tax") <= 2 || t.toLowerCase().contains("tax")){
                            nx.setTax(tVal);
//<<<<<<< HEAD
//                        }
//
//                    }else {
//                        nx.addItem(t, "", tVal,"-1");
//=======
                        }else {
                        nx.addItem(t, "", tVal, "-1");
//>>>>>>> d26349acaa677ee84a080729b32f3eed8a781aee
                    }
                }catch(Exception e){
                    System.out.println("\n\n**Error in parseItems!\n\n");
                }
            }else{
                t = t.trim().replaceAll("\\d+\\.\\d+","");
                t = t.replaceAll("\\$", "");
                t = t.replaceAll("( +)"," ");

                nx.addItem(t, "", 0.00, "-1");
            }
        }






    }

    //This function displays the selection buttons and renders the 1st item to the item list
    //TODO
    private void renderItems() {
        ocr_instLow.setText("By Clicking Finish, all items will be added in their current state. You can also remove/add items at the next step if you like.");
        ocr_fin.setVisibility(View.VISIBLE);
        ocr_scn.setVisibility(View.GONE);
        ocr_fin.setEnabled(true);
        ocr_fwd.setEnabled(true);
        ocr_bak.setEnabled(true);
        ocr_del.setEnabled(true);

        ocr_itemName.setText("Total");
        ocr_itemName.setTextColor(getResources().getColor(R.color.colorEceiptOrange));
        ocr_itemPrice.setText(String.valueOf(nx.getTotalCost()));
        pb.setIndeterminate(false);
        itemItr = -1;

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
            inspected.remove(0);
            target.remove(0);
            System.out.println("This is a raw item: " + item);
            itemsRaw.add(item);
        }else{
            int iActual = -1;
            int jActual = -1;

            //For each item in inspected...
            for(int i = 0; i < inspected.size(); i++){
                int inspY = inspected.get(i).getBoundingBox().centerY();
                yTolerance = 3000;

                //Check against each item in target...
                for(int j = 0; j < target.size(); j++){
                  int targY = target.get(j).getBoundingBox().centerY();
                  int dif = Math.abs(inspY - targY);
                  if(yTolerance > dif && dif < 85){
                      yTolerance = dif;
                      System.out.println(yTolerance + " is now closest");
                      iActual = i;
                      jActual = j;
                      item = inspected.get(i).getValue() + " " + target.get(j).getValue();
                  }
                    System.out.println("Lines -->"+ inspected.get(i).getValue() + " <--||--> " + target.get(j).getValue());
                }
                if(iActual != -1 && jActual != -1) {
                    System.out.println("***Closest Item was " + item + "****");
                    itemsRaw.add(item);
                    System.out.println("***Indexes are: " +iActual + "--" + jActual + "****");
                    inspected.remove(iActual);
                    target.remove(jActual);
                    System.out.println("Done!");
                     iActual = -1;
                     jActual = -1;
                }else{
                    System.out.println("Lines Failed to match accurately!");
                }

            }

        }



        System.out.println("\n**=====*Finished Line Tolerance!*=====**\n");
    }

    //Determines if the textblocks are relatively the same line
    public boolean checkBlockTolerance(TextBlock a, TextBlock b){
        int yTolerance = a.getBoundingBox().centerY() - b.getBoundingBox().centerY();
        if(yTolerance <= 250 && yTolerance >= -250) {
         //   System.out.println("Block Match PASSED! Y was: " + yTolerance + " for " + a.getValue() + "\n==\n" + b.getValue());
            return true;
        }else{
            //System.out.println("Block Match Failed! Y was: " + yTolerance + " for " + a.getValue() + "\n==\n" + b.getValue());
            return false;
        }

    }

    //Returns the Lines of a given textblock
    public List<? extends Text> toLines(TextBlock e){
        return e.getComponents();
    }

    //=========================================================//


}
