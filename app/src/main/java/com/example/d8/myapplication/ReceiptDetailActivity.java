package com.example.d8.myapplication;

import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiptDetailActivity extends AppCompatActivity {
    ListView listView;
    TextView date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detail);

        String index = getIntent().getStringExtra("RECEIPTINDEX");

        listView = (ListView)findViewById(R.id.item_list_view);
        date = (TextView)findViewById(R.id.receipt_detail_date) ;
        date.setText(Information.receipts.get(Integer.parseInt(index)).getDate());

        //Toast.makeText(getApplicationContext(), index, Toast.LENGTH_LONG).show();
        loadItemListView(Integer.parseInt(index));
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
}
