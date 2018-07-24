package com.example.d8.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AnalyzeActivity extends AppCompatActivity {

    //Declare TextView. I like the purple to differentiate from other variables
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

        //Declare and initialize variables
        int receiptCount            = 0;
        double accumulativeTotal    = 0.0;
        double greatestTotal        = 0.0;
        double priciestItem         = 0.0;
        double currentReceiptTotal;
        double currentItemPrice;
        int currentReceiptCatID;

        ArrayList<Receipt> receiptList      = Information.receipts;
        ArrayList<Receipt.Item> itemList;
        ArrayList<String> categoryList      = Information.categories;
        ArrayList<Double> valueList         = new ArrayList<>();
        //Set length of valueList to categoryList while also initializing all values to 0
        for(String cat:categoryList){
            valueList.add(0.0);
        }

        //Calculate values
        for(Receipt receipt:receiptList){
            receiptCount ++;
            currentReceiptTotal = receipt.getTotalCost();
            currentReceiptCatID = categoryList.indexOf(receipt.getCategory());
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
