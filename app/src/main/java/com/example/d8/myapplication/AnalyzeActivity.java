package com.example.d8.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyCharacterMap;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalyzeActivity extends AppCompatActivity {

    TextView averageTotal;
    TextView mostExpensiveTransaction;
    TextView mostExpensiveItem;
    ListView analyzeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        //Assign TextViews
        averageTotal                = (TextView)findViewById(R.id.averageTotal);
        mostExpensiveTransaction    = (TextView)findViewById(R.id.mostExpensiveTransaction);
        mostExpensiveItem           = (TextView)findViewById(R.id.mostExpensiveItem);
        analyzeListView             = (ListView)findViewById(R.id.analyze_list_view);

        ArrayList<Receipt> receiptList      = Information.receipts;
        ArrayList<Receipt.Item> itemList;
        ArrayList<String> categoryList      = Information.categories;
        ArrayList<Double> valueList         = new ArrayList<>();
        //Set length of valueList to categoryList while also initializing all values to 0
        for(String cat:categoryList){
            valueList.add(0.0);
        }

        int receiptCount            = 0;
        double accumulativeTotal    = 0.0;
        double greatestTotal        = 0.0;
        double priciestItem         = 0.0;
        double currentReceiptTotal;
        double currentItemPrice;
        int currentReceiptCatID;

        //Calculate values
        for(Receipt receipt:receiptList){
            receiptCount ++;
            currentReceiptTotal = receipt.getTotalCost();
            currentReceiptCatID   = categoryList.indexOf(receipt.getCategory());
            accumulativeTotal  += currentReceiptTotal;

            if(greatestTotal < currentReceiptTotal){
                greatestTotal = currentReceiptTotal;
            }

            valueList.set(currentReceiptCatID, valueList.get(currentReceiptCatID) + currentReceiptTotal);

            itemList = receipt.getItems();

            for(Receipt.Item item:itemList){
                currentItemPrice = item.getItemPrice();

                if(priciestItem < currentItemPrice){
                    priciestItem = currentItemPrice;
                }
            }
        }
        double average = accumulativeTotal/receiptCount;

        //Display values
        averageTotal.setText("$" + String.format("%.2f", average));
        mostExpensiveTransaction.setText("$" + String.format("%.2f", greatestTotal));
        mostExpensiveItem.setText("$" + String.format("%.2f", priciestItem));

        AnalyzeListviewAdapter adapter = new AnalyzeListviewAdapter(this, categoryList, valueList);
        analyzeListView.setAdapter(adapter);
    }


}
