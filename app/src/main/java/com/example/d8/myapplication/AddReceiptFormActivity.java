package com.example.d8.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.IDNA;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.InputType;
import android.text.TextUtils;
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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddReceiptFormActivity extends AppCompatActivity {
    EditText companyName;
    EditText receiptDate;
    EditText totalCost;
    Button receiptSubmitButton;
    Button addItemBtn;
    EditText itemName;
    EditText itemPrice;
    ArrayList<Receipt.Item>newItems = new ArrayList<Receipt.Item>();
    ListView listView;
    String category = "No category";
    String temp = "";

    ArrayList<String> resourceCate = new ArrayList<String>();

    String USERID = Information.authUser.getUserId();
    String USERRECEIPTFILENAME = USERID+Information.RECEIPTSLOCALFILENAME;

    int mYear;
    int mMonth;
    int mDay;

    boolean ocrFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt_form);

        companyName = (EditText)findViewById(R.id.company_name);
        receiptDate = (EditText)findViewById(R.id.receipt_date);
        totalCost = (EditText)findViewById(R.id.add_receipt_total_cost);
        listView = (ListView)findViewById(R.id.add_item_listview);

        String cDateInString = getCurrentDate();

        initCustomSpinner();

        receiptDate.setText(cDateInString);

        //This accepts an OCR scanned receipt and pre-loads it into the form.
        Receipt ocrScn = (Receipt)(getIntent().getSerializableExtra("ocrScan"));
      if(ocrScn != null) {
          ocrFlag = true;
          for (int i = 0; i < ocrScn.getItems().size(); i++) {

              System.out.println(ocrScn.getItembyId(i).getItemName() + "--" + ocrScn.getItembyId(i).getItemPrice());
              newItems.add(ocrScn.getItembyId(i));

          }
          loadItemObjToListview(newItems);
          totalCost.setText(String.valueOf(ocrScn.getTotalCost()));

      }
        receiptDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(AddReceiptFormActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        addItemBtn = (Button)findViewById(R.id.add_receipt_add_item_btn);

        addItemBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                itemName = (EditText)findViewById(R.id.add_receipt_item_name);
                String itemname = itemName.getText().toString();
                if(TextUtils.isEmpty(itemname)){
                    itemname = "N/A";
                }

                itemPrice = (EditText)findViewById(R.id.add_receipt_item_price);
                String itempriceStr = itemPrice.getText().toString();
                double itemprice = 0.0;
                if( TextUtils.isEmpty(itempriceStr)){
                    Log.i("ITEMPRICE IS: ", "NULL");
                    itemprice = -1;
                }else{
                    itemprice = Double.parseDouble(itempriceStr);
                }

                String itemdesc = "";

                Receipt.Item item = new Receipt().new Item(itemname,itemdesc,itemprice, "-1");
                newItems.add(item);
                loadItemObjToListview(newItems);

                if(!ocrFlag) {
                    double totalCostInDouble = 0.0;
                    for (int i = 0; i < newItems.size(); i++) {
                        if (newItems.get(i).getItemPrice() == -1) {
                            totalCostInDouble += 0.00;
                        } else {
                            totalCostInDouble += newItems.get(i).getItemPrice();
                        }

                    }
                    String.format("%.2f", totalCostInDouble);
                    totalCost.setText(String.format("%.2f", totalCostInDouble));
                }
                itemName.setText("");
                itemPrice.setText("");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("positionZZZZ: ", Integer.toString(position));

                AlertDialog alertDialog = new AlertDialog.Builder(AddReceiptFormActivity.this).create();
                alertDialog.setTitle("Delete item");
                alertDialog.setMessage("Do you want to delete this item?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                newItems.remove(position);
                                loadItemObjToListview(newItems);

                                double totalCostInDouble = 0.0;
                                for(int i=0; i<newItems.size(); i++){
                                    if(newItems.get(i).getItemPrice()==-1){
                                        totalCostInDouble+=0.00;
                                    }else{
                                        totalCostInDouble+=newItems.get(i).getItemPrice();
                                    }

                                }
                                if(!ocrFlag)
                                totalCost.setText(Double.toString(totalCostInDouble));

                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
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
                    Toast.makeText(getApplicationContext(),"Please enter a Business Name",Toast.LENGTH_LONG).show();
                }else if(tCost.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter the total cost!",Toast.LENGTH_LONG).show();
                }else{
                    //all input are validated!
                    String receiptsJSON = DataController.readJsonFile(USERRECEIPTFILENAME, AddReceiptFormActivity.this);
                    try{
                        JSONArray receiptsJsonArray = new JSONArray(receiptsJSON);
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("name", username);
                        jsonObject.put("receiptID", "-1");
                        jsonObject.put("date", date);
                        jsonObject.put("totalCost", tCost);
                        jsonObject.put("tax", tax);
                        jsonObject.put("businessName", company);
                        jsonObject.put("categoryName", category);

                        JSONArray itemsJsonArray = new JSONArray();
                        if(!newItems.isEmpty()){
                            //itemsJsonArray.put(itemJsonObject);
                            for(int i=0; i<newItems.size();i++){
                                JSONObject itemJsonObject = new JSONObject();
                                itemJsonObject.put("itemName", newItems.get(i).getItemName());
                                itemJsonObject.put("itemDesc","");
                                itemJsonObject.put("itemPrice", Double.toString(newItems.get(i).getItemPrice()));
                                itemJsonObject.put("itemID", newItems.get(i).getItemID());
                                itemsJsonArray.put(itemJsonObject);
                            }
                        }

                        Log.i("itemsJsonArray", itemsJsonArray.toString());

                        jsonObject.put("items",itemsJsonArray);

                        //Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                        String jsonString = jsonObject.toString();

                        Log.i("AddReceiptFormResult", jsonString);
                        Log.e("USERNAME", username);

                        //test addReceiptToLocal and parseJsonToReceiptOBJ in Information class:

                        Receipt receipt = DataController.parseJsonToReceiptOBJ(jsonString);
                        //Log.i("ITEMaaaa",receipt.getItems().get(0).getItemName());


                        Log.i("RECEIPTUSERNAME", receipt.getName());


                        DataController.addReceiptToLocal(USERID, receipt,AddReceiptFormActivity.this);

                        DataController.addReceiptToDB(receipt,"http://myvmlab.senecacollege.ca:6207/addReceipt.php",AddReceiptFormActivity.this);

                        DataController.SyncronizeData("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php", AddReceiptFormActivity.this);
//                        Log.i("JSONINAddReceiptForm:", jsonString);
//
//                        receiptsJsonArray.put(jsonObject);
//
//                        String jArrayString = receiptsJsonArray.toString();
//                        Log.i("JArrAddReceiptForm:", jArrayString);
//
//                        DataController.storeJsonToLocal(jArrayString, Information.RECEIPTSLOCALFILENAME, AddReceiptFormActivity.this);
//

                        //sendToDB(jsonString, "http://myvmlab.senecacollege.ca:6207/addReceipt.php");
                        Intent homeIntent = new Intent(AddReceiptFormActivity.this, MenuActivity.class);
                        homeIntent.putExtra("finish", true);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homeIntent);
                        finish();

                    }catch(JSONException e){
                        Log.e("EXCEPTION1:", e.toString());
                    }
                }
            }
        });

        try {
            //Set main layout background
            SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            int themeDayNight=settings.getInt("Theme_DayNight", AppCompatDelegate.MODE_NIGHT_NO);//Default day

            LinearLayout li=(LinearLayout)findViewById(R.id.add_receipt_form_bg);
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

    void sendToDB(String jsonString, final String urlWebService){

        class SendDB extends AsyncTask<Void, Void, String>{
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }


            @Override
            protected String doInBackground(Void... voids) {
                try{
                    URL url = new URL(urlWebService);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(jsonString.getBytes().length);

                    //make some HTTP header nicety
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    //conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    conn.connect();

                    OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(jsonString.getBytes());
                    //clean up
                    os.flush();

//                    DataOutputStream printout = new DataOutputStream(conn.getOutputStream());
//                    printout.writeBytes(URLEncoder.encode(jsonString,"UTF-8"));
//                    printout.flush ();
//                    printout.close ();
                    Log.i("DOINBACK", "Here!");

                    //do somehting with response
                    InputStream is = conn.getInputStream();
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while((line = bReader.readLine()) != null){
                        result += line;
                    }
                    bReader.close();
                    is.close();
                    conn.disconnect();

                    Log.i("RESULT:", result);

                }catch(Exception e){
                    Log.e("EXCEPTION2:", e.toString());
                }
//                try{
//                    URL url = new URL(urlWebService);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setReadTimeout(10000);
//                    conn.setConnectTimeout(15000);
//                    conn.setRequestMethod("POST");
//                    conn.setDoInput(true);
//                    conn.setDoOutput(true);
//
//
//                    DataOutputStream printout = new DataOutputStream(conn.getOutputStream());
//                    printout.writeBytes(URLEncoder.encode(jsonString,"UTF-8"));
//                    printout.flush ();
//                    printout.close ();
//
//                }catch(Exception e){
//                    Log.e("EXCEPTION:", e.toString());
//                }

                return null;
            }
        }

        new SendDB().execute();
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
        Spinner spinnerCate = (Spinner) findViewById(R.id.add_receipt_form_categories_spinner);

        resourceCate.clear();
        for(int i=0; i<Information.categories.size(); i++){
            resourceCate.add(Information.categories.get(i));
        }
        resourceCate.set(0,"Select a category");
        resourceCate.add("  + New category +");


        CustomSpinnerAdapter customSpinnerAdapterDay=new CustomSpinnerAdapter(AddReceiptFormActivity.this,resourceCate);
        spinnerCate.setAdapter(customSpinnerAdapterDay);

        spinnerCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    category = "No category";
                }else if(position == resourceCate.size()-1){
//                    Toast.makeText(getBaseContext(), "Add New Category", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddReceiptFormActivity.this);
                    builder.setTitle("New category");
                    builder.setMessage("Enter new category name:");

// Set up the input
                    final EditText input = new EditText(AddReceiptFormActivity.this);
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

                            CustomSpinnerAdapter customSpinnerAdapterDay=new CustomSpinnerAdapter(AddReceiptFormActivity.this,resourceCate);
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
                    Toast.makeText(getBaseContext(), "selected " + item, Toast.LENGTH_LONG).show();
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
            TextView txt = new TextView(AddReceiptFormActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.LEFT);
            txt.setText(asr.get(position));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(AddReceiptFormActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            //txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
            txt.setText(asr.get(i));
            return  txt;
        }
    }

}
