package com.example.d8.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ModifyReceiptActivity extends AppCompatActivity {
    EditText companyName;
    EditText receiptDate;
    EditText totalCost;
    Button receiptSubmitButton;
    Button updateItemBtn;
    EditText itemName;
    EditText itemPrice;
    ListView listView;

    String USERID = Information.authUser.getUserId();
    String USERRECEIPTFILENAME = USERID+Information.RECEIPTSLOCALFILENAME;


    int mYear;
    int mMonth;
    int mDay;
    int itemIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_receipt);

        String index = getIntent().getStringExtra("RECEIPTINDEX");
        Log.i("INDEX", index);

        Receipt receipt = Information.receipts.get(Integer.parseInt(index));


        companyName = (EditText)findViewById(R.id.company_name);
        receiptDate = (EditText)findViewById(R.id.receipt_date);
        totalCost = (EditText)findViewById(R.id.add_receipt_total_cost);
        listView = (ListView)findViewById(R.id.receipt_items_listview);
        updateItemBtn =(Button)findViewById(R.id.modify_receipt_update_item_btn);
        itemName = (EditText)findViewById(R.id.modify_receipt_item_name);
        itemPrice = (EditText)findViewById(R.id.modify_receipt_item_price);

        companyName.setText(receipt.getBusinessName());
        receiptDate.setText(receipt.getDate());
        totalCost.setText(Double.toString(receipt.getTotalCost()));
        loadItemObjToListview(receipt.getItems());
        //String cDateInString = getCurrentDate();

        //receiptDate.setText(cDateInString);

        receiptDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(ModifyReceiptActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "yyyy-MM-dd"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.CANADA);
                        receiptDate.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemIndex =position;
                Receipt.Item item= receipt.getItems().get(position);
                itemName.setText(item.itemName);
                itemPrice.setText(Double.toString(item.itemPrice));
            }
        });


        updateItemBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(itemIndex>=0) {
                    String itemname = itemName.getText().toString();
                    double itemprice = Double.parseDouble(itemPrice.getText().toString());

                    Receipt.Item _item = receipt.getItems().get(itemIndex);

                    _item.itemName = itemname;
                    _item.itemPrice = itemprice;
                    loadItemObjToListview(receipt.getItems());

                    itemName.setText("");
                    itemPrice.setText("");
                    itemIndex = -1;
                }
            }
        });


        receiptSubmitButton = (Button)findViewById(R.id.receipt_submit_btn);

        receiptSubmitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String company = companyName.getText().toString();
                String date = receiptDate.getText().toString();
                String username = Information.authUser.getUserId();
                String UID = Information.authUser.getFirebaseUID();
                String tCost = totalCost.getText().toString();
                String tax = "14";

                //validate text input empty
                if(company.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter your name!",Toast.LENGTH_LONG).show();
                }else if(tCost.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter receipt's total cost!",Toast.LENGTH_LONG).show();
                }else{
                    //all input are validated!
                    try{

                        receipt.setBusinessName(company);
                        receipt.setDate(date);
                        receipt.setTotalCost(Double.parseDouble(tCost));

                        Information.receipts.set(Integer.parseInt(index),receipt);

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

                        //sendToDB(jsonString, "http://myvmlab.senecacollege.ca:6207/addReceipt.php");
                        Intent homeIntent = new Intent(ModifyReceiptActivity.this, MenuActivity.class);
                        startActivity(homeIntent);

                    }catch(Exception e){
                        Log.e("EXCEPTION1:", e.toString());
                    }
                }
            }
        });

    }

    String getCurrentDate(){
        //get current date and time
        Calendar calender = Calendar.getInstance();
        int cDay = calender.get(Calendar.DAY_OF_MONTH);
        int cMonth = calender.get(Calendar.MONTH) + 1;
        int cYear = calender.get(Calendar.YEAR);
        int cHour = calender.get(Calendar.HOUR);
        int cMinute = calender.get(Calendar.MINUTE);
        int cSecond = calender.get(Calendar.SECOND);



        //String cDateTime = ""+cDay+"/"+cMonth+"/"+cYear+" "+cHour+":"+cMinute+":"+cSecond;
        //String cDateInString = ""+cYear+"-"+cMonth+"-"+cDay;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        String cDateInString = sdf.format(calender.getTime()).toString();


        return cDateInString;
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
