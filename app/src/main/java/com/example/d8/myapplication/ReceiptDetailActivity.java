package com.example.d8.myapplication;

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
    ImageButton deleteImgBtn;
    String USERID = Information.authUser.getUserId();
    String USERRECEIPTFILENAME = USERID+Information.RECEIPTSLOCALFILENAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detail);

        String index = getIntent().getStringExtra("RECEIPTINDEX");
        Log.i("INDEX", index);

        Receipt receipt = Information.receipts.get(Integer.parseInt(index));

        listView = (ListView)findViewById(R.id.item_list_view);
        date = (TextView)findViewById(R.id.receipt_detail_date) ;
        date.setText(Information.receipts.get(Integer.parseInt(index)).getDate());

        deleteImgBtn = (ImageButton)findViewById(R.id.buttonDelete);
        deleteImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    //Append Json string into given file
                    //                   DataController.appendJsonToLocal(ja.toString(),Information.RECEIPTSLOCALFILENAME,v.getContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Start MenuActivity to get refreshed
                Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                startActivity(intent);
                //Just finished this activity, and will go back to tracked previous activity.(No refresh)
                //finish();
            }
        });


        //Toast.makeText(getApplicationContext(), index, Toast.LENGTH_LONG).show();
//        loadItemListView(Integer.parseInt(index));
        loadItemObjToListview(receipt.getItems());

        total_cost = (TextView)findViewById(R.id.total_cost);

        double totalCostInDouble = 0.0;
        for(int i=0; i<receipt.getItems().size(); i++){
            if(receipt.getItems().get(i).getItemPrice()==-1){
                totalCostInDouble+=0.00;
            }else{
                totalCostInDouble+=receipt.getItems().get(i).getItemPrice();
            }

        }
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
