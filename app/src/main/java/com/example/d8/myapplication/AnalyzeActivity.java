package com.example.d8.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class AnalyzeActivity extends AppCompatActivity {

    //Declare and initialize variables
    TextView periodTotal;
    TextView mostExpensiveTransaction;
    TextView mostExpensiveItem;
    ListView analyzeListView;
    Spinner analyzeDaySpinner;

    int receiptCount;
    double accumulativeTotal;
    double greatestTotal;
    double priciestItem;
    double currentReceiptTotal;
    double currentItemPrice;
    int currentReceiptCatID;

    Calendar cal    = Calendar.getInstance();
    int dayOfWeek   = cal.get(Calendar.DAY_OF_WEEK);
    int dayOfMonth  = cal.get(Calendar.DAY_OF_MONTH);
    int dayOfYear   = cal.get(Calendar.DAY_OF_YEAR);

    static ArrayList<Receipt> receiptList = Information.receipts;
    ArrayList<Receipt.Item> itemList;
    ArrayList<String> categoryList      = Information.categories;
    ArrayList<Double> valueList         = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        //Assign TextViews and spinner
        periodTotal                 = (TextView) findViewById(R.id.periodTotal);
        mostExpensiveTransaction    = (TextView) findViewById(R.id.mostExpensiveTransaction);
        mostExpensiveItem           = (TextView) findViewById(R.id.mostExpensiveItem);
        analyzeListView             = (ListView) findViewById(R.id.analyze_list_view);
        analyzeDaySpinner           = (Spinner) findViewById(R.id.analyze_day_spinner);

        //Build the day selection spinner and display the screen
        ArrayAdapter<CharSequence> analyzeAdapter = ArrayAdapter.createFromResource(this, R.array.analyzeDays, android.R.layout.simple_spinner_item);
        analyzeDaySpinner.setAdapter(analyzeAdapter);
        analyzeDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String item = parent.getItemAtPosition(i).toString();
                receiptList = loadReceiptObjToListviewByDays(item);
                displayAnalysis();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //displayAnalysis();
    }

    //Calculates and displays the analysis
    public void displayAnalysis(){
        //Initialize variables
        receiptCount            = 0;
        accumulativeTotal       = 0.0;
        greatestTotal           = 0.0;
        priciestItem            = 0.0;
        currentReceiptTotal     = 0.0;
        currentItemPrice        = 0.0;
        currentReceiptCatID     = 0;
        valueList.clear();

        //Set length of valueList to categoryList while also initializing all values to 0
        for (String cat : categoryList) {
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
        periodTotal.setText("$" + String.format("%.2f", accumulativeTotal));
        mostExpensiveTransaction.setText("$" + String.format("%.2f", greatestTotal));
        mostExpensiveItem.setText("$" + String.format("%.2f", priciestItem));

        AnalyzeListviewAdapter adapter = new AnalyzeListviewAdapter(this, categoryList, valueList);
        analyzeListView.setAdapter(adapter);
    }

    //Function returns a list of Receipts up to the number days selected.
    ArrayList<Receipt> loadReceiptObjToListviewByDays(String daysSelect) {
        ArrayList<Receipt> receipts = new ArrayList<Receipt>();
        if(daysSelect.equals("All time")) {
            receipts = Information.receipts;
        }else if(daysSelect.equals("This week")) {
            Log.d("dayOfWeek", String.valueOf(dayOfWeek));
            receipts = DataController.getReceiptsInDays(dayOfWeek - 1);
        }else if(daysSelect.equals("This month")){
            Log.d("dayOfMonth", String.valueOf(dayOfMonth));
            receipts = DataController.getReceiptsInDays(dayOfMonth - 1);
        }else if(daysSelect.equals("This year")){
            Log.d("dayOfYear", String.valueOf(dayOfYear));
            receipts = DataController.getReceiptsInDays(dayOfYear - 1);
        }else{
            String daysNumString = daysSelect.replaceAll("\\D+", "");
            Log.d("Days string: ", daysNumString);
            Integer daysNum = Integer.parseInt(daysNumString);
            Log.d("Days int   :", String.valueOf(daysNum));
            receipts = DataController.getReceiptsInDays(daysNum);
        }
        return receipts;
    }
}
