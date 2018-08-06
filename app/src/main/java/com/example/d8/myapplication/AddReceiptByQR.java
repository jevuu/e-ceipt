package com.example.d8.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddReceiptByQR extends AppCompatActivity {
    ListView listView;
    TextView date;
    TextView total_cost;
    TextView businessName;

    Button submitBtn;
    String USERID = Information.authUser.getUserId();
    String USERRECEIPTFILENAME = USERID+Information.RECEIPTSLOCALFILENAME;
    Receipt receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt_by_qr);

        String receiptIdStr = getIntent().getStringExtra("RECEIPTID_");

        //if app is opened from link, that means there's no EXTRA name "RECEIPTID_"
        if(receiptIdStr==null){
            Log.i("receiptIdStr", "NULL");

            //get data from incoming intent
            Intent intent = getIntent();
            String action = intent.getAction();
            Uri data = intent.getData();
            String dataStr = data.toString();
            String rId = dataStr.substring("http://www.eceipt.ca/".length());
            Log.i("INTENTDATA", rId);

            receiptIdStr = rId;
        }

        //String receiptIdStr = "270";




            try{
                receipt = DataController.getReceiptById(Integer.parseInt(receiptIdStr),"http://myvmlab.senecacollege.ca:6207/getOneReceipt.php",AddReceiptByQR.this);

                if(receipt.getReceipId()==null){
                    Log.i("RECEIPTIDINOPTION", "Receipt Null");
                    AlertDialog alertDialog = new AlertDialog.Builder(AddReceiptByQR.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Can't find the receipt!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent goToAdd = new Intent(AddReceiptByQR.this, MenuActivity.class);
                                    startActivity(goToAdd);

                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            listView = (ListView)findViewById(R.id.add_receipt_item_list_view);
            date = (TextView)findViewById(R.id.add_receipt_receipt_detail_date) ;
            date.setText(receipt.getDate());

            businessName = (TextView)findViewById(R.id.business_name_in_add_by_qr);
            businessName.setText(receipt.getBusinessName());


            total_cost = (TextView)findViewById(R.id.add_receipt_total_cost);

            double totalCostInDouble = 0.0;

            totalCostInDouble = receipt.getTotalCost();
            total_cost.setText(Double.toString(totalCostInDouble));

            loadItemObjToListview(receipt.getItems());

            submitBtn = (Button)findViewById(R.id.add_receipt_qr_btn);
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        DataController.addReceiptToLocal(USERID, receipt,AddReceiptByQR.this);
                        DataController.addReceiptToDB(receipt,"http://myvmlab.senecacollege.ca:6207/addReceipt.php", AddReceiptByQR.this);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getBaseContext(),MenuActivity.class);
                    startActivity(intent);
                }
            });


        try {
            //Set main layout background
            SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            int themeDayNight=settings.getInt("Theme_DayNight", AppCompatDelegate.MODE_NIGHT_NO);//Default day

            LinearLayout li=(LinearLayout)findViewById(R.id.add_receipt_qr_item_list);
            if(themeDayNight== AppCompatDelegate.MODE_NIGHT_YES){
                li.setBackgroundColor(Color.BLACK);
            }else {
                li.setBackgroundColor(Color.WHITE);
            }

        }
        catch(Exception ex)
        {

        }


    }

    void loadItemObjToListview(ArrayList<Receipt.Item> items ){
        if(!items.isEmpty()){
            ItemListViewAdapter adapter = new ItemListViewAdapter(this,items);
            listView.setAdapter(adapter);
        }else{
            String[] emptyString = {};
            ArrayAdapter<String> arrayAdapterEmpty = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emptyString);
            listView.setAdapter(arrayAdapterEmpty);
            Log.i("NORECEIPT!","true");
        }
    }
}
