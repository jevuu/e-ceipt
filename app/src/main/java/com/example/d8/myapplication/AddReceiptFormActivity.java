package com.example.d8.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddReceiptFormActivity extends AppCompatActivity {
    EditText companyName;
    EditText receiptDate;
    Button receiptSubmitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt_form);

        companyName = (EditText)findViewById(R.id.company_name);
        receiptDate = (EditText)findViewById(R.id.receipt_date);

        receiptSubmitButton = (Button)findViewById(R.id.receipt_submit_btn);

        receiptSubmitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String company = companyName.getText().toString();
                String date = receiptDate.getText().toString();
                String username = "freddy";
                String userId = "007";
                String totalCost = "100";
                String tax = "14";



                try{
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("name", username);
                    //jsonObject.put("receiptID", "232");
                    //jsonObject.put("date", date);
                    //jsonObject.put("totalCost", totalCost);
                    jsonObject.put("tax", tax);
                    jsonObject.put("businessName", company);



                    //Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                    String jsonString = jsonObject.toString();

                    Log.i("JSONINAddReceiptForm:", jsonString);

                    sendToDB(jsonString, "http://myvmlab.senecacollege.ca:6207/addReceipt.php");

                }catch(JSONException e){
                    Log.e("EXCEPTION1:", e.toString());
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

}
