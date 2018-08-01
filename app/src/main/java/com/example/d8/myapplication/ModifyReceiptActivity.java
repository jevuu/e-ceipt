package com.example.d8.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
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

    ArrayList<String> resourceCate = new ArrayList<String>();
//    ArrayList<Receipt.Item>receiptItems = new ArrayList<Receipt.Item>();

    String category = "No category";
    String temp = "";

    String USERID = Information.authUser.getUserId();
    String USERRECEIPTFILENAME = USERID+Information.RECEIPTSLOCALFILENAME;
    Receipt receipt;

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

        receipt  = DataController.getReceiptByIdThroughInfomationClass(index);



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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(ModifyReceiptActivity.this).create();
                alertDialog.setTitle("Delete item");
                alertDialog.setMessage("Do you want to delete this item?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String itemId = receipt.getItems().get(position).getItemID();
                                Log.i("ITEMREMOVEID", itemId);

                                receipt.getItems().remove(position);
                                loadItemObjToListview(receipt.getItems());

                                double totalCostInDouble = 0.0;
                                for(int i=0; i<receipt.getItems().size(); i++){
                                    if(receipt.getItems().get(i).getItemPrice()==-1){
                                        totalCostInDouble+=0.00;
                                    }else{
                                        totalCostInDouble+=receipt.getItems().get(i).getItemPrice();
                                    }

                                }
                                totalCost.setText(Double.toString(totalCostInDouble));

                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();

                return true;
            }
        });


        updateItemBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    String itemname = itemName.getText().toString();
                    double itemprice = Double.parseDouble(itemPrice.getText().toString());
                    if (itemname != null && !itemname.isEmpty()) {
                        Receipt.Item _item=null;
                        if (itemIndex >= 0) {
                            //Modify item
                            _item = receipt.getItems().get(itemIndex);
                            //Update Total Cost
                            receipt.setTotalCost(receipt.getTotalCost()-_item.itemPrice+itemprice);
                            _item.itemName = itemname;
                            _item.itemPrice = itemprice;
                        }
                        else
                        {
                            //Update Total Cost
                            receipt.setTotalCost(receipt.getTotalCost()+itemprice);

                            //Add item
                            _item=receipt.new Item();
                            _item.itemName = itemname;
                            _item.itemPrice = itemprice;
                            _item.itemDesc = "";
                            _item.itemID = "-1";
                            receipt.getItems().add(_item);
                        }
                        loadItemObjToListview(receipt.getItems());
                        totalCost.setText(Double.toString(receipt.getTotalCost()));

                        itemName.setText("");
                        itemPrice.setText("");
                        itemIndex = -1;
                    }
                }
                catch(Exception ex)
                {

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
                    Toast.makeText(getApplicationContext(),"Please enter a Business name",Toast.LENGTH_LONG).show();
                }else if(tCost.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter the total cost",Toast.LENGTH_LONG).show();
                }else{
                    //all input are validated!
                    try{

                        receipt.setBusinessName(company);
                        receipt.setDate(date);
                        receipt.setTotalCost(Double.parseDouble(tCost));
                        receipt.setCategory(category);

                        Information.receipts.set(Integer.parseInt(index),receipt);

                        String result = DataController.toJsonObject(receipt, ModifyReceiptActivity.this).toString();
                        Log.i("MODIFYSTR", result);

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
                            DataController.modifyReceipt(receipt,"http://myvmlab.senecacollege.ca:6207/editReceipt.php", ModifyReceiptActivity.this);
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

        initCustomSpinner();

        try {
            //Set main layout background
            SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            int themeDayNight=settings.getInt("Theme_DayNight", AppCompatDelegate.MODE_NIGHT_NO);//Default day

            LinearLayout li=(LinearLayout)findViewById(R.id.modify_body);
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

    private void initCustomSpinner() {
        //Set spinner for day number select
        Spinner spinnerCate = (Spinner) findViewById(R.id.modify_cate_spinner);

        resourceCate.clear();
        for(int i=0; i<Information.categories.size(); i++){
            resourceCate.add(Information.categories.get(i));
        }
        resourceCate.set(0,"Select a category");
        resourceCate.add("  New category");

        int receiptCateInIndex = 0;
        for(int i=0; i<resourceCate.size(); i++){
            Log.i("resourceCate", resourceCate.get(i));

            if(resourceCate.get(i).toUpperCase().equals(receipt.getCategory().toUpperCase())){
                receiptCateInIndex = i;
            }
        }


        CustomSpinnerAdapter customSpinnerAdapterDay=new CustomSpinnerAdapter(ModifyReceiptActivity.this,resourceCate);
        spinnerCate.setAdapter(customSpinnerAdapterDay);

        spinnerCate.setSelection(receiptCateInIndex);

        spinnerCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    category = "No category";
                }else if(position == resourceCate.size()-1){
//                    Toast.makeText(getBaseContext(), "Add New Category", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyReceiptActivity.this);
                    builder.setTitle("New category");
                    builder.setMessage("Enter new category name:");

// Set up the input
                    final EditText input = new EditText(ModifyReceiptActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

// Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            temp = input.getText().toString();
                            temp = temp.substring(0,1).toUpperCase()+temp.substring(1).toLowerCase();
                            temp = temp.trim();
                            resourceCate.remove(resourceCate.size()-1);
                            resourceCate.add("  " + temp);
                            resourceCate.add("  New category");

                            CustomSpinnerAdapter customSpinnerAdapterDay=new CustomSpinnerAdapter(ModifyReceiptActivity.this,resourceCate);
                            spinnerCate.setAdapter(customSpinnerAdapterDay);
                            spinnerCate.setSelection(resourceCate.size()-2);

                            category = temp.trim();

                            Log.i("NEWCATEGORY:",temp);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }else{
                    String item = parent.getItemAtPosition(position).toString();
                    Log.i("ITEMSAaaaaa", item);
                    //Toast.makeText(getBaseContext(), "selected " + item, Toast.LENGTH_LONG).show();
                    category = item.trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context,ArrayList<String> asr) {
            this.asr=asr;
            activity = context;
        }

        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(ModifyReceiptActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.LEFT);
            txt.setText(asr.get(position));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(ModifyReceiptActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            txt.setText(asr.get(i));
            return  txt;
        }
    }
}
