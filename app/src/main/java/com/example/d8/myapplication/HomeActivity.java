package com.example.d8.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.IDNA;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

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
    String username = Information.authUser.getUserId();
    String userFirebaseUID = Information.authUser.getFirebaseUID();
    String email = Information.authUser.getEmail();
    Button btn_add ;
    //String RECEIPTDATAFILE = "_receipts.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.i("SignInByUser: ", username);
        Log.i("UserFirebaseUID: ", userFirebaseUID);
        Log.i("UserEmail: ", email);

        listView = (ListView)findViewById(R.id.receipts_list_view);

        //DataController.SyncronizeData("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php", this);

        initCustomSpinner();

        try{
            String json = DataController.readJsonFile(Information.RECEIPTSLOCALFILENAME, this);
            Log.i("JSONHOME", json);
//            if(Information.receipts.isEmpty()){
//                DataController.loadReceiptsObj(json);
//                loadReceiptObjToListView();
//            }else{
//
//            }
            if(!Information.receipts.isEmpty()){
                Information.receipts.clear();
            }
            DataController.loadReceiptsObj(json);
            loadReceiptObjToListView();

        }catch(JSONException e){
            Log.e("JSONERROR", e.toString());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(),""+position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getBaseContext(),ReceiptDetailActivity.class);
                intent.putExtra("RECEIPTINDEX", Integer.toString(position));
                startActivity(intent);
            }
        });

        btn_add = (Button)findViewById(R.id.add_btn);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addOption = new Intent(getBaseContext(),AddReceiptOptionActivity.class);
                startActivity(addOption);
            }
        });

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


    public void onAddOption(View view) {
        Intent goOption = new Intent(this, AddReceiptOptionActivity.class);
        startActivity(goOption);
    }

    public void onBarcode(View view){
        Intent goBarcode = new Intent(this, BarcodeActivity.class);
        startActivity(goBarcode);
    }

    //Called when Barcode scanner picks up a result
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 0){
            if (resultCode == CommonStatusCodes.SUCCESS){
                if (data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Toast.makeText(getBaseContext(), "Barcode value: " + barcode.displayValue, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getBaseContext(), "No barcode found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Load the receipts data to listview(from json string to listview)
    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] receipts = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            receipts[i] = String.format("%-35s%-12s%20s",obj.getString("businessName"), obj.getString("date"), obj.getString("totalCost"));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, receipts);
        listView.setAdapter(arrayAdapter);
    }

    //Load the receipts data to listview(from object to listview)
    void loadReceiptObjToListView(){
        if(!Information.receipts.isEmpty()){
            String[] receipts = new String[Information.receipts.size()];
            Log.i("RECEIPTSSIZE", Integer.toString(Information.receipts.size()));

            for(int i=0; i<Information.receipts.size(); i++){
                receipts[i] = String.format("%-35s%-12s%20s",Information.receipts.get(i).getBusinessName(), Information.receipts.get(i).getDate(), Information.receipts.get(i).getTotalCost());
            }
            Log.i("RECEIPTSLOADING:", receipts.toString());

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, receipts);
            listView.setAdapter(arrayAdapter);
        }
    }
}
