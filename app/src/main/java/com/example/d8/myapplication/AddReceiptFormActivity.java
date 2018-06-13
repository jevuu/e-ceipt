package com.example.d8.myapplication;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddReceiptFormActivity extends AppCompatActivity {
    EditText companyName;
    EditText receiptDate;
    Button receiptSubmitButton;

    int mYear;
    int mMonth;
    int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt_form);

        companyName = (EditText)findViewById(R.id.company_name);
        receiptDate = (EditText)findViewById(R.id.receipt_date);

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
                    jsonObject.put("date", date);
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

    String getCurrentDate(){
        //get current date and time
        Date currentDate = new Date();
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


}
