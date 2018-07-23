package com.example.d8.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceiptDetailActivity extends AppCompatActivity {
    ListView listView;
    TextView date;
    TextView total_cost;
    TextView businessName;
    TextView category;
    ImageButton deleteImgBtn;

    ImageButton backToHomeBtn;
    ImageButton shareReceiptBtn;
    ImageButton modifyImgBtn;


    String USERID = Information.authUser.getUserId();
    String USERRECEIPTFILENAME = USERID+Information.RECEIPTSLOCALFILENAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detail);

        String index = getIntent().getStringExtra("RECEIPTINDEX");
        Log.i("INDEX", index);

        Receipt receipt = Information.receipts.get(Integer.parseInt(index));

        String receiptIDStr_ = receipt.getReceipId();

        listView = (ListView)findViewById(R.id.item_list_view);
        date = (TextView)findViewById(R.id.receipt_detail_date) ;
        date.setText(Information.receipts.get(Integer.parseInt(index)).getDate());

        businessName = (TextView)findViewById(R.id.business_name_in_receipt_detail);
        businessName.setText(receipt.getBusinessName());

        category = (TextView)findViewById(R.id.category_in_receipt_detail);
        category.setText("-  "+receipt.getCategory()+"  -");

        deleteImgBtn = (ImageButton)findViewById(R.id.buttonDelete);
        deleteImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ReceiptDetailActivity.this).create();
                alertDialog.setTitle("Delete receipt");
                alertDialog.setMessage("Do you want to delete this receipt?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int receiptID = Integer.parseInt(Information.receipts.get(Integer.parseInt(index)).getReceipId());
                                Information.receipts.remove(Integer.parseInt(index));

                                JSONArray ja=new JSONArray();
                                JSONObject j1=null;
                                try {
                                    //Delete content from file.
                                    //                   DataController.deleteFileContent(Information.RECEIPTSLOCALFILENAME,v.getContext());

                                    for(Receipt r : Information.receipts)
                                    {
                                        j1=DataController.toJsonObject(r,v.getContext());
                                        ja.put(j1);
                                    }
                                    DataController.storeJsonToLocal(ja.toString(),USERRECEIPTFILENAME,v.getContext());
                                    DataController.deleteReceiptFromDB(receiptID,"http://myvmlab.senecacollege.ca:6207/deleteReceipt.php", v.getContext());
                                    //Append Json string into given file
                                    //                   DataController.appendJsonToLocal(ja.toString(),Information.RECEIPTSLOCALFILENAME,v.getContext());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                                //Start MenuActivity to get refreshed
                                Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                                startActivity(intent);
                                //Just finished this activity, and will go back to tracked previous activity.(No refresh)
                                //finish();
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();




            }
        });

        //Modify receipt
        modifyImgBtn = (ImageButton)findViewById(R.id.buttonModify);
        modifyImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start MenuActivity to get refreshed
                Intent intent = new Intent(getBaseContext(), ModifyReceiptActivity.class);
                intent.putExtra("RECEIPTINDEX",index);
                startActivity(intent);
                //Just finished this activity, and will go back to tracked previous activity.(No refresh)
                //finish();
            }
        });


        backToHomeBtn = (ImageButton)findViewById(R.id.receipt_detail_back_to_home_btn);
        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

        shareReceiptBtn = (ImageButton)findViewById(R.id.receipt_detail_share_btn);
        shareReceiptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ReceiptShareActivity.class);
                intent.putExtra("RECEIPTID", receiptIDStr_);
                startActivity(intent);

//                Intent intent = new Intent(getBaseContext(), ReceiptShareActivity.class);
//                startActivity(intent);
            }
        });



        backToHomeBtn = (ImageButton)findViewById(R.id.receipt_detail_back_to_home_btn);
        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addRecpt = new Intent(getBaseContext(), MenuActivity.class);
                addRecpt.putExtra("finish", true);
                addRecpt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(addRecpt);
                finish();
            }
        });

        shareReceiptBtn = (ImageButton)findViewById(R.id.receipt_detail_share_btn);
        shareReceiptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ReceiptShareActivity.class);
                intent.putExtra("RECEIPTID", receiptIDStr_);
                startActivity(intent);

//                Intent intent = new Intent(getBaseContext(), ReceiptShareActivity.class);
//                startActivity(intent);
            }
        });



        //Toast.makeText(getApplicationContext(), index, Toast.LENGTH_LONG).show();
//        loadItemListView(Integer.parseInt(index));
        loadItemObjToListview(receipt.getItems());

        total_cost = (TextView)findViewById(R.id.total_cost);

        double totalCostInDouble = 0.0;
//        for(int i=0; i<receipt.getItems().size(); i++){
//            if(receipt.getItems().get(i).getItemPrice()==-1){
//                totalCostInDouble+=0.00;
//            }else{
//                totalCostInDouble+=receipt.getItems().get(i).getItemPrice();
//            }
//
//        }
//        if(receipt.getItems().size()==0){
//            totalCostInDouble = receipt.getTotalCost();
//        }
        totalCostInDouble = receipt.getTotalCost();
        total_cost.setText(Double.toString(totalCostInDouble));

    }

    void loadItemListView(int index){
        if(!Information.receipts.get(index).getItems().isEmpty()){
            int itemsSize = Information.receipts.get(index).getItems().size();
            String[] items = new String[itemsSize];
            for(int i=0; i<itemsSize; i++){
                items[i] = String.format("%-35s%20s", Information.receipts.get(index).getItems().get(i).getItemName(),
                        Information.receipts.get(index).getItems().get(i).getItemPrice());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
            listView.setAdapter(arrayAdapter);
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
