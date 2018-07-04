package com.example.d8.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
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

    String USERID = Information.authUser.getUserId();
    String USERRECEIPTFILENAME = USERID+Information.RECEIPTSLOCALFILENAME;

    int mYear;
    int mMonth;
    int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt_form);

        companyName = (EditText)findViewById(R.id.company_name);
        receiptDate = (EditText)findViewById(R.id.receipt_date);
        totalCost = (EditText)findViewById(R.id.add_receipt_total_cost);
        listView = (ListView)findViewById(R.id.add_item_listview);

        String cDateInString = getCurrentDate();

        receiptDate.setText(cDateInString);

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

                itemPrice = (EditText)findViewById(R.id.add_receipt_item_price);
                double itemprice = Double.parseDouble(itemPrice.getText().toString());
                String itemdesc = "";

                Receipt.Item item = new Receipt().new Item(itemname,itemdesc,itemprice);
                newItems.add(item);
                loadItemObjToListview(newItems);

                double totalCostInDouble = 0.0;
                for(int i=0; i<newItems.size(); i++){
                    totalCostInDouble+=newItems.get(i).getItemPrice();
                }
                totalCost.setText(Double.toString(totalCostInDouble));
                itemName.setText("");
                itemPrice.setText("");
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

                        JSONArray itemsJsonArray = new JSONArray();
                        if(!newItems.isEmpty()){
                            //itemsJsonArray.put(itemJsonObject);
                            for(int i=0; i<newItems.size();i++){
                                JSONObject itemJsonObject = new JSONObject();
                                itemJsonObject.put("itemName", newItems.get(i).getItemName());
                                itemJsonObject.put("itemDesc","");
                                itemJsonObject.put("itemPrice", Double.toString(newItems.get(i).getItemPrice()));
                                itemsJsonArray.put(itemJsonObject);
                            }
                        }

                        Log.i("itemsJsonArray", itemsJsonArray.toString());

                        jsonObject.put("items",itemsJsonArray);

                        //Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                        String jsonString = jsonObject.toString();

                        Log.i("JAAAAASON", jsonString);
                        Log.e("USERNAME", username);

                        //test addReceiptToLocal and parseJsonToReceiptOBJ in Information class:

                        Receipt receipt = DataController.parseJsonToReceiptOBJ(jsonString);
                        //Log.i("ITEMaaaa",receipt.getItems().get(0).getItemName());


                        Log.i("RECEIPTUSERNAME", receipt.getName());


                        DataController.addReceiptToLocal(USERID, receipt,AddReceiptFormActivity.this);

                        DataController.addReceiptToDB(receipt,"http://myvmlab.senecacollege.ca:6207/addReceipt.php",AddReceiptFormActivity.this);
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
                        startActivity(homeIntent);

                    }catch(JSONException e){
                        Log.e("EXCEPTION1:", e.toString());
                    }
                }
            }
        });

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

}
