package com.example.d8.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    ListView listView;
    String username = "johnDoe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = (ListView)findViewById(R.id.receipts_list_view);

        //getData("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php");
        getJSON("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php");
        //new SyncronizeData().execute("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php");
        initCustomSpinner();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(),""+position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(),ReceiptDetailActivity.class);
                intent.putExtra("RECEIPTINDEX", Integer.toString(position));
                startActivity(intent);
            }
        });
        try{
            String json = readJsonFile();
            if(Information.receipts.isEmpty()){
                loadReceiptsObj(json);
            }
            //loadIntoListView(json);
            loadReceiptObjToListView();
        }catch(JSONException e){
            Log.e("JSON ERROR", e.toString());
        }

        //Log.d("RECEIPTOBJ:",Information.receipts.size().);
        Log.d("RECEIPTOBJ3:",Information.receipts.get(1).getReceipId());

    }


    private void initCustomSpinner() {
        //Set spinner for day number select
        Spinner spinnerDay = (Spinner) findViewById(R.id.spinner_daysnum_select);
        ArrayList<String> resourceDay = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.home_spinner_days_select)));
        CustomSpinnerAdapter customSpinnerAdapterDay=new CustomSpinnerAdapter(HomeActivity.this,resourceDay);
        spinnerDay.setAdapter(customSpinnerAdapterDay);

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Set spinner for category select
        Spinner spinnerCate = (Spinner) findViewById(R.id.spinner_category_select);
        ArrayList<String> resourceCate = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.home_spinner_category)));
        CustomSpinnerAdapter customSpinnerAdapterCate=new CustomSpinnerAdapter(HomeActivity.this,resourceCate);
        spinnerCate.setAdapter(customSpinnerAdapterCate);

        spinnerCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter{
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
            TextView txt = new TextView(HomeActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.LEFT);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(HomeActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            //txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }
    }

    private void getData(final String urlWebService) {

        try{
            URL url = new URL(urlWebService);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
        }catch(Exception e){

        }
    }









    //code from "https://www.simplifiedcoding.net/android-json-parsing-tutorial/"
    //this method is actually fetching the json string
    private void getJSON(final String urlWebService) {
        /*
        * As fetching the json string is a network operation
        * And we cannot perform a network operation in main thread
        * so we need an AsyncTask
        * The constrains defined here are
        * Void -> We are not passing anything
        * Void -> Nothing at progress update as well
        * String -> After completion it should return a string and it will be the json string
        * */
        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                try{
                    //loadIntoListView(s);
                    storeJsonToLocal(s);
                }catch(Exception e){
                    Log.e("ERROR:", e.toString());
                }

                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "dhfjshjfs", Toast.LENGTH_SHORT).show();
                //Log.i("Json:" , s.toString());
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {

                try {
                    //creating a URL
                    Log.i("HHHHHHHH","wwwww");
                    URL url = new URL(urlWebService);


                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("GET");
                    con.connect();
                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    Log.i("HHHHHHHH","aaa");
                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        Log.d("HHHHHHHH", json);
                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    Log.i("FAIL222",e.toString());
                    return null;
                }

            }
        }


        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


    private void storeJsonToLocal(String json) throws JSONException{
        //String username = Information.user.getUserName();
        String filename = "_receipts"+".txt";

        try {
            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readJsonFile(){
        //String username = Information.user.getUserName();
        String filename = "_receipts"+".txt";
        String json = "";
        try{
            FileInputStream inputStream = openFileInput(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer);
            //Toast.makeText(getApplicationContext(),json,Toast.LENGTH_LONG).show();
            return json;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] receipts = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            //receipts[i] = obj.getString("receiptID")+"   "+obj.getString("date")+"  "+obj.getString("totalCost");
            receipts[i] = String.format("%-35s%-12s%20s",obj.getString("businessName"), obj.getString("date"), obj.getString("totalCost"));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, receipts);
        listView.setAdapter(arrayAdapter);
    }

    private void loadReceiptsObj(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        Receipt receipt;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            receipt =  new Receipt();
            Log.d("RECEIPTname:", obj.getString("name"));
            receipt.setName(obj.getString("name"));
            Log.d("RECEIPTid:", obj.getString("receiptID"));
            receipt.setReceipId(obj.getString("receiptID"));
            Log.d("RECEIPTdate:", obj.getString("date"));
            receipt.setDate(obj.getString("date"));
            Log.d("RECEIPTtotalcost:", obj.getString("totalCost"));
            receipt.setTotalCost(Double.parseDouble(obj.getString("totalCost")));
            Log.d("RECEIPTtax:", obj.getString("tax"));
            receipt.setTax(Double.parseDouble(obj.getString("tax")));
            Log.d("RECEIPTtax:", obj.getString("businessName"));
            receipt.setBusinessName(obj.getString("businessName"));

            Log.d("RECEIPTOBJ", "name:"+receipt.getName()+
                    "id:"+receipt.getReceipId()+
                    "date:"+receipt.getDate()+
                    "totalCost:"+receipt.getTotalCost()+receipt.getTax());


            JSONArray itemarray = obj.getJSONArray("items");
            //List<Receipt.Item> itemList = new ArrayList<Receipt.Item>();
            //Receipt.Item item = new Receipt().new Item();

            for(int j=0; j<itemarray.length();j++){
                JSONObject itemObj = itemarray.getJSONObject(i);
                receipt.addItem(itemObj.getString("itemName"), itemObj.getString("itemDesc"), Double.parseDouble(itemObj.getString("itemPrice")));
            }

            Information.receipts.add(receipt);

        }
        if(!Information.receipts.get(0).getItems().isEmpty()){
            Log.d("RECEIPTOBJ2:",Information.receipts.get(0).getItems().get(0).getItemName());

        }
    }

    void loadReceiptObjToListView(){
        String[] receipts = new String[Information.receipts.size()];

        for(int i=0; i<Information.receipts.size(); i++){
            receipts[i] = String.format("%-35s%-12s%20s",Information.receipts.get(i).getBusinessName(), Information.receipts.get(i).getDate(), Information.receipts.get(i).getTotalCost());
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, receipts);
        listView.setAdapter(arrayAdapter);
    }
}
