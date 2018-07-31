package com.example.d8.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


//this class handles some of the actions which may reuse many time
public class DataController {
    //add new receipt data to local json file
    static void addReceiptToLocal(String USERID, Receipt receipt, Context ctx)throws JSONException{
        String userReceiptFileName = USERID+Information.RECEIPTSLOCALFILENAME;
        String receiptsJSON = DataController.readJsonFile(userReceiptFileName, ctx);

        JSONArray receiptsJsonArray = new JSONArray(receiptsJSON);
        JSONObject jsonObject = new JSONObject();


        String username = receipt.getName();
        String company = receipt.getBusinessName();
        String date = receipt.getDate();
        String userId = Information.authUser.getUserId();
        String tCost = Double.toString(receipt.getTotalCost());
        String tax = "14";

        jsonObject.put("name", username);
        jsonObject.put("receiptID", "-1");
        jsonObject.put("date", date);
        jsonObject.put("totalCost", tCost);
        jsonObject.put("tax", tax);
        jsonObject.put("businessName", company);

        JSONArray itemsJsonArray = new JSONArray();
        if(!receipt.getItems().isEmpty()){
            //itemsJsonArray.put(itemJsonObject);
            for(int i=0; i<receipt.getItems().size();i++){
                JSONObject itemJsonObject = new JSONObject();
                itemJsonObject.put("itemName", receipt.getItems().get(i).getItemName());
                itemJsonObject.put("itemDesc","");
                itemJsonObject.put("itemPrice", Double.toString(receipt.getItems().get(i).getItemPrice()));
                itemJsonObject.put("itemID", receipt.getItems().get(i).getItemID());
                itemsJsonArray.put(itemJsonObject);
            }
        }

        jsonObject.put("items",itemsJsonArray);

        String jsonString = jsonObject.toString();

        receiptsJsonArray.put(jsonObject);

        String jArrayString = receiptsJsonArray.toString();
        Log.i("addReceiptToLocalRES:", jArrayString);

        DataController.storeJsonToLocal(jArrayString, userReceiptFileName, ctx);
    }

    //add new receipt data to remote databse
    public static String addReceiptToDB(Receipt receipt, final String urlWebService, Context ctx){

        class AddReceiptToDB extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    ////////////////////////////////////////////////////////////////////////////
                    //Post version
                    //creating a URL
                    URL url = new URL(urlWebService);
                    HttpURLConnection conn = null;

                    String username = Information.authUser.getName();
                    String userId = Information.authUser.getUserId();
//                    String userId = "2";
//                    String username = "Freddy";

                    //open connection
                    conn = (HttpURLConnection) url.openConnection();

                    //set the request method to post
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    //parse data from receipt obj
                    String date = receipt.getDate();
                    String totalCost = Double.toString(receipt.getTotalCost());
                    String tax = Double.toString(receipt.getTax());
                    String businessName = receipt.getBusinessName();

                    //Data in Json object
                    //JSONArray receiptsJsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();

                    String userID = Information.authUser.getFirebaseUID();
                    //jsonObject.put("name", username);
                    jsonObject.put("userID", userID);
                    //jsonObject.put("receiptID", "-1");
                    jsonObject.put("date", date);
                    jsonObject.put("totalCost", totalCost);
                    jsonObject.put("tax", tax);
                    jsonObject.put("businessName", businessName);
                    jsonObject.put("categoryName", receipt.getCategory());
                    //jsonObject.put("items",itemsJsonArray);

                    JSONArray itemsJsonArray = new JSONArray();
                    if(!receipt.getItems().isEmpty()){
                        //itemsJsonArray.put(itemJsonObject);
                        for(int i=0; i<receipt.getItems().size();i++){
                            JSONObject itemJsonObject = new JSONObject();
                            itemJsonObject.put("itemName", receipt.getItems().get(i).getItemName());
                            itemJsonObject.put("itemDesc","");
                            itemJsonObject.put("itemPrice", Double.toString(receipt.getItems().get(i).getItemPrice()));
                            itemsJsonArray.put(itemJsonObject);
                        }
                    }

                    jsonObject.put("items",itemsJsonArray);

                    //Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                    String message = jsonObject.toString();

                    Log.i("NEWRECEIPTPOST",message);

                    //Output the stream to the server
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(message);
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        Log.d("JSONARRAY", json);
                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    String jsonReturn = sb.toString().trim();

                    Log.i("ADDRECEIPTDBTURN", jsonReturn);
                    //storeJsonToLocal(jsonReturn, RECEIPTDATAFILE, ctx);
                    //storeJsonToLocal(jsonReturn);

                    //finally returning the read string
                    return sb.toString().trim();

                    ////////////////////////////////////////////////////////////////////////////

                } catch (Exception e) {
                    Log.i("FAIL222",e.toString());
                    return null;
                }
            }

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
        }
        //creating asynctask object and executing it
        AddReceiptToDB addReceiptToDB = new AddReceiptToDB();
        String result="";
        try{
            result = addReceiptToDB.execute().get();
            Log.i("RESULTDBBB", result);
        }catch (Exception e){

        }

        return result;

        }

    //transfer receipt object to json format data
    static JSONObject toJsonObject(Receipt receipt, Context ctx)throws JSONException{

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("name", receipt.getName());
        jsonObject.put("receiptID", receipt.getReceipId());
        jsonObject.put("date", receipt.getDate());
        jsonObject.put("totalCost", receipt.getTotalCost());
        jsonObject.put("tax", receipt.getTax());
        jsonObject.put("businessName", receipt.getBusinessName());
        jsonObject.put("categoryName", receipt.getCategory());
        //jsonObject.put("items",itemsJsonArray);

        JSONArray itemsJsonArray = new JSONArray();
        if(!receipt.getItems().isEmpty()){
            //itemsJsonArray.put(itemJsonObject);
            for(int i=0; i<receipt.getItems().size();i++){
                JSONObject itemJsonObject = new JSONObject();
                itemJsonObject.put("itemName", receipt.getItems().get(i).getItemName());
                itemJsonObject.put("itemDesc","");
                itemJsonObject.put("itemPrice", Double.toString(receipt.getItems().get(i).getItemPrice()));
                itemJsonObject.put("itemID", receipt.getItems().get(i).getItemID());
                itemsJsonArray.put(itemJsonObject);
            }
        }

        Log.i("itemsJsonArray", itemsJsonArray.toString());

        //jsonObject.put("items",itemsJsonArray);


        jsonObject.put("items",itemsJsonArray);
        //String jsonString = jsonObject.toString();

        Log.i("JSONINAddReceiptForm:", jsonObject.toString());


        return jsonObject;
    }

    //Store json string to mobile local file
    public static void storeJsonToLocal(String json, String filename, Context ctx) throws JSONException {
        //String username = Information.user.getUserName();
        //String filename = "_receipts"+".txt";

        try {
            FileOutputStream outputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("STOREERROR:", e.toString());
        }
    }

    //read json from local file
    public static String readJsonFile(String filename, Context ctx){
        //String username = Information.user.getUserName();
        //String filename = "_receipts"+".txt";
        String json = "";
        try{
            FileInputStream inputStream = ctx.openFileInput(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer);
            //Toast.makeText(getApplicationContext(),json,Toast.LENGTH_LONG).show();
            return json;
        }catch (Exception e){
            Log.e("FILEREADFAIL", e.toString());
            return "null";
        }
        //return null;
    }

    //code tutorial from "https://www.simplifiedcoding.net/android-json-parsing-tutorial/"
    //this method is to syncronize remote DB data to mobile local storage.
    public static String SyncronizeData(final String urlWebService, Context ctx) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */

        //The local file which store receipts data
        String USERID = Information.authUser.getUserId();
        String USERRECEIPTFILENAME = USERID+Information.RECEIPTSLOCALFILENAME;

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

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);

            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {

                try {
                    //Post version
                    //creating a URL
                    URL url = new URL(urlWebService);
                    HttpURLConnection conn = null;

                    String username = Information.authUser.getName();
                    String userFirebaseUId = Information.authUser.getFirebaseUID();
//                    String userId = "2";
//                    String username = "Freddy";


                    //open connection
                    conn = (HttpURLConnection) url.openConnection();

                    //set the request method to post
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    //prepare data to post
                    //List<NameValuePair> params = new ArrayList<NameValuePair>();
                    List<AbstractMap.SimpleEntry> params = new ArrayList<AbstractMap.SimpleEntry>();

                    JSONObject newJson = new JSONObject();
                    newJson.put("userName", username);
                    newJson.put("userID", userFirebaseUId);

                    String message = newJson.toString();

                    //Output the stream to the server
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(message);
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        Log.d("JSONARRAY", json);
                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    String jsonReturn = sb.toString().trim();

                    Log.i("JSONRETURN", jsonReturn);
                    storeJsonToLocal(jsonReturn, USERRECEIPTFILENAME, ctx);
                    //storeJsonToLocal(jsonReturn);

                    //finally returning the read string
                    return sb.toString().trim();

//                    }catch(MalformedURLException error) {
//                        //Handles an incorrectly entered URL
//                    }
//                    catch(SocketTimeoutException error) {
//                        //Handles URL access timeout.
//                    }
//                    catch (IOException error) {
//                        //Handles input and output errors
//                    }finally {
//                        if(conn != null) // Make sure the connection is not null.
//                            conn.disconnect();
//                    }


                } catch (Exception e) {
                    Log.i("SYNCHRONIZEFAIL",e.toString());
                    return null;
                }
            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        String result="";
        try{
            result = getJSON.execute().get();
            Log.i("SYNCHRONIZERESULT",result);
        }catch (Exception e){

        }

        return result;
    }

    //this method is to load json format receipts data to "Information.receipts" object
    public static void loadReceiptsObj(String json) throws JSONException {
        try{
            JSONArray jsonArray = new JSONArray(json);
            Receipt receipt;

            Log.i("JSONOOOOOO",json);
            Log.i("JSONLENGHT", Integer.toString(jsonArray.length()));

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
                Log.d("RECEIPTbusiness:", obj.getString("businessName"));
                receipt.setBusinessName(obj.getString("businessName"));
                Log.d("RECEIPTcategory:", obj.getString("categoryName"));
                receipt.setCategory(obj.getString("categoryName"));

                Log.d("RECEIPTOBJ", "name:"+receipt.getName()+
                        "id:"+receipt.getReceipId()+
                        "date:"+receipt.getDate()+
                        "totalCost:"+receipt.getTotalCost()+receipt.getTax());

                JSONArray itemarray = obj.getJSONArray("items");

                Log.d("ITEMARRAYL:",itemarray.toString());

                for(int j=0; j<itemarray.length();j++){
                    JSONObject itemObj = itemarray.getJSONObject(j);

                    Log.d("ITEMNAME22222", itemObj.getString("itemName"));
                    Log.d("ITEMDECS", itemObj.getString("itemDesc"));
                    Log.d("ITEMPRICE", itemObj.getString("itemPrice"));

                    receipt.addItem(itemObj.getString("itemName"), itemObj.getString("itemDesc"), Double.parseDouble(itemObj.getString("itemPrice")), itemObj.getString("itemID"));
                }

                Information.receipts.add(receipt);

            }
        }catch(Exception e){
            Log.e("ERRRRR:",e.toString());
        }

//        if(!Information.receipts.get(0).getItems().isEmpty()){
//            Log.d("RECEIPTOBJ2:",Information.receipts.get(0).getItems().get(1).getItemName());
//
//        }
    }

    //This method is to parse json format string to an Receipt object and return Receipt object
    public static Receipt parseJsonToReceiptOBJ(String json)throws JSONException{
        JSONObject obj = new JSONObject(json);
        Receipt receipt =  new Receipt();
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
        Log.d("RECEIPTbusiness:", obj.getString("businessName"));
        receipt.setBusinessName(obj.getString("businessName"));

        receipt.setCategory(obj.getString("categoryName"));

        Log.d("RECEIPTOBJ", "name:"+receipt.getName()+
                "id:"+receipt.getReceipId()+
                "date:"+receipt.getDate()+
                "totalCost:"+receipt.getTotalCost()+receipt.getTax());

        Log.d("jjjjj",json.toString());

        JSONArray itemarray = obj.getJSONArray("items");

        Log.d("ITEMARRAYL:",itemarray.toString());

        for(int j=0; j<itemarray.length();j++){
            JSONObject itemObj = itemarray.getJSONObject(j);

            receipt.addItem(itemObj.getString("itemName"), itemObj.getString("itemDesc"), Double.parseDouble(itemObj.getString("itemPrice")),itemObj.getString("itemID"));
        }

        //Log.i("ITEMSssss",receipt.getItems().get(0).getItemName());
        return receipt;
    }

    //this metod is to get receipts in specific days, like 3 days, 7 days...
    public static ArrayList<Receipt> getReceiptsInDays(Integer days){
        ArrayList<Receipt> receipts = new ArrayList<Receipt>();
        try{
            Date currentDate = DataController.getCurrentDate();

            try{

                for(int i=0; i<Information.receipts.size(); i++){
                    Integer days_ = DataController.dateDiff(DataController.parseStringToDate(Information.receipts.get(i).getDate()),currentDate);
                    if(days_<=days){
                        Log.i("RECEIPT["+i+"]",Information.receipts.get(i).toString());
                        receipts.add(Information.receipts.get(i));
                    }
                }
            }catch(Exception e){
                Log.i("DAYSFAIL", e.toString());
            }

        }catch(Exception e){

        }

        return receipts;
    }

    //this metod is to get receipts in specific category
    public static ArrayList<Receipt> getReceiptsInCategory(String category){
        ArrayList<Receipt> receipts = new ArrayList<Receipt>();
        try{
            Date currentDate = DataController.getCurrentDate();

            try{

                for(int i=0; i<Information.receipts.size(); i++){
                    if(Information.receipts.get(i).getCategory().equals(category)){
                        Log.i("RECEIPT["+i+"]",Information.receipts.get(i).toString());
                        receipts.add(Information.receipts.get(i));
                    }
                }
            }catch(Exception e){
                Log.i("DAYSFAIL", e.toString());
            }

        }catch(Exception e){

        }

        return receipts;
    }

    //this metod is to get receipts in specific days and category
    public static ArrayList<Receipt> getReceiptsInDaysAndCategory(int days, String category){
        ArrayList<Receipt> receipts = new ArrayList<Receipt>();
        try{
            Date currentDate = DataController.getCurrentDate();

            for(int i=0; i<Information.receipts.size(); i++){
                Integer days_ = DataController.dateDiff(DataController.parseStringToDate(Information.receipts.get(i).getDate()),currentDate);
                if(days_<=days && Information.receipts.get(i).getCategory().equals(category)){
                    Log.i("RECEIPT["+i+"]",Information.receipts.get(i).toString());
                    receipts.add(Information.receipts.get(i));
                }
            }

        }catch(Exception e){

        }

        return receipts;
    }

    //method to get current date
    public static Date getCurrentDate()throws Exception{

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
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        String cDateInString = sdf.format(calender.getTime()).toString();
        Date currentDate = sdf.parse(cDateInString);

        return currentDate;
    }

    //this method is to get two date difference
    public static Integer dateDiff(Date startDate, Date endDate){
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String resultString = ""+elapsedDays;

        Integer result = Integer.parseInt(resultString);
        return result;
    }

    //parse data from String type to Date type
    public static Date parseStringToDate(String dateInString)throws Exception{


        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        //String cDateInString = sdf.format(dateInString).toString();
        Date currentDate = sdf.parse(dateInString);
        return currentDate;
    }

    //get receipt from DB by ID
    public static Receipt getReceiptById(int id, final String urlWebService, Context ctx)throws Exception{
        Receipt newReceipt = new Receipt();

        class GetReceiptById extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    ////////////////////////////////////////////////////////////////////////////
                    //Post version
                    //creating a URL
                    URL url = new URL(urlWebService);
                    HttpURLConnection conn = null;

                    String username = Information.authUser.getName();
                    String userId = Information.authUser.getUserId();

                    //open connection
                    conn = (HttpURLConnection) url.openConnection();

                    //set the request method to post
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("receiptID",id);


                    //Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                    String message = jsonObject.toString();

                    Log.i("GetReceiptByIDPost",message);

                    //Output the stream to the server
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(message);
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        Log.d("JSONARRAY", json);
                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    String jsonReturn = sb.toString().trim();

                    Log.i("GetReceiptReturn", jsonReturn);

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    Log.i("FAIL222",e.toString());
                    return null;
                }
            }

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
        }

        GetReceiptById getReceiptById = new GetReceiptById();
        String result = "";
        result = getReceiptById.execute().get();

        JSONObject jsonObject = new JSONObject(result);
        String receiptID = jsonObject.getString("receiptID");

        if(!receiptID.equals("null")){
            newReceipt = parseJsonToReceiptOBJ(result);
        }

        return newReceipt;
    }

    //delete receipt from DB
    public static void deleteReceiptFromDB(int id, final String urlWebService, Context ctx)throws Exception{
        Receipt newReceipt = new Receipt();

        class DeleteReceiptById extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    //Post version
                    //creating a URL
                    URL url = new URL(urlWebService);
                    HttpURLConnection conn = null;

                    String username = Information.authUser.getName();
                    String userId = Information.authUser.getUserId();

                    //open connection
                    conn = (HttpURLConnection) url.openConnection();

                    //set the request method to post
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("receiptID",id);


                    //Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                    String message = jsonObject.toString();

                    Log.i("deleteReceiptByIDPost",message);

                    //Output the stream to the server
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(message);
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        Log.d("JSONARRAY", json);
                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    String jsonReturn = sb.toString().trim();

                    Log.i("GetReceiptReturn", jsonReturn);

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    Log.i("FAIL222",e.toString());
                    return null;
                }
            }

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
        }

        DeleteReceiptById deleteReceiptById = new DeleteReceiptById();
        String result = "";
        result = deleteReceiptById.execute().get();
        newReceipt = parseJsonToReceiptOBJ(result);
    }

    //update receipt in DB (modify)
     public static String modifyReceipt(Receipt receipt, final String urlWebService, Context ctx){
         class ModifyReceiptToDB extends AsyncTask<Void, Void, String> {

             @Override
             protected String doInBackground(Void... voids) {
                 try {
                     ////////////////////////////////////////////////////////////////////////////
                     //Post version
                     //creating a URL
                     URL url = new URL(urlWebService);
                     HttpURLConnection conn = null;

                     String username = Information.authUser.getName();
                     String userId = Information.authUser.getUserId();
//                    String userId = "2";
//                    String username = "Freddy";

                     //open connection
                     conn = (HttpURLConnection) url.openConnection();

                     //set the request method to post
                     conn.setReadTimeout(10000);
                     conn.setConnectTimeout(15000);
                     conn.setRequestMethod("POST");
                     conn.setDoInput(true);
                     conn.setDoOutput(true);

                     //parse data from receipt obj
                     String date = receipt.getDate();
                     String totalCost = Double.toString(receipt.getTotalCost());
                     String tax = Double.toString(receipt.getTax());
                     String businessName = receipt.getBusinessName();

                     //Data in Json object
                     //JSONArray receiptsJsonArray = new JSONArray();
                     JSONObject jsonObject = new JSONObject();

                     String userID = Information.authUser.getFirebaseUID();
                     //jsonObject.put("name", username);
                     jsonObject.put("userID", userID);
                     //jsonObject.put("receiptID", "-1");
                     jsonObject.put("date", date);
                     jsonObject.put("totalCost", totalCost);
                     jsonObject.put("tax", tax);
                     jsonObject.put("businessName", businessName);
                     jsonObject.put("categoryName", receipt.getCategory());
                     jsonObject.put("receiptID", receipt.getReceipId());
                     //jsonObject.put("items",itemsJsonArray);

                     JSONArray itemsJsonArray = new JSONArray();
                     if(!receipt.getItems().isEmpty()){
                         //itemsJsonArray.put(itemJsonObject);
                         for(int i=0; i<receipt.getItems().size();i++){
                             JSONObject itemJsonObject = new JSONObject();
                             itemJsonObject.put("itemName", receipt.getItems().get(i).getItemName());
                             itemJsonObject.put("itemDesc","");
                             itemJsonObject.put("itemPrice", Double.toString(receipt.getItems().get(i).getItemPrice()));
                             itemJsonObject.put("itemID", receipt.getItems().get(i).getItemID());
                             itemsJsonArray.put(itemJsonObject);
                         }
                     }

                     jsonObject.put("items",itemsJsonArray);

                     //Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                     String message = jsonObject.toString();

                     Log.i("ModifyReceiptTPOST",message);

                     //Output the stream to the server
                     OutputStream os = conn.getOutputStream();
                     BufferedWriter writer = new BufferedWriter(
                             new OutputStreamWriter(os, "UTF-8"));
                     writer.write(message);
                     writer.flush();
                     writer.close();
                     os.close();

                     conn.connect();

                     //StringBuilder object to read the string from the service
                     StringBuilder sb = new StringBuilder();

                     //We will use a buffered reader to read the string from service
                     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                     //A simple string to read values from each line
                     String json;

                     //reading until we don't find null
                     while ((json = bufferedReader.readLine()) != null) {
                         Log.d("JSONARRAY", json);
                         //appending it to string builder
                         sb.append(json + "\n");
                     }

                     String jsonReturn = sb.toString().trim();

                     //storeJsonToLocal(jsonReturn, RECEIPTDATAFILE, ctx);
                     //storeJsonToLocal(jsonReturn);

                     //finally returning the read string
                     return sb.toString().trim();

                     ////////////////////////////////////////////////////////////////////////////

                 } catch (Exception e) {
                     Log.i("FAIL222",e.toString());
                     return null;
                 }
             }

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
         }
         //creating asynctask object and executing it
         ModifyReceiptToDB modifyReceiptToDB = new ModifyReceiptToDB();
         String result="";
         try{
             result = modifyReceiptToDB.execute().get();
             Log.i("MODIFYRESULT", result);
         }catch (Exception e){

         }

         return result;
     }

    //delete local file with receipts' information as json
    public static void deleteLocalFile(Context ctx){
        String dir = ctx.getFilesDir().getAbsolutePath();
        Log.i("FilePath", dir);
        String USERID = Information.authUser.getUserId();
        String USERRECEIPTFILENAME = USERID+Information.RECEIPTSLOCALFILENAME;

        File f0 = new File(dir, USERRECEIPTFILENAME);
        boolean d0 = f0.delete();
        Log.w("Delete Check", "File deleted: " + dir + "/myFile " + d0);
    }

    public static Receipt getReceiptByIdThroughInfomationClass(String receiptId){
        Receipt receipt = new Receipt();
        for(int i = 0; i<Information.receipts.size(); i++){
            if(Information.receipts.get(i).getReceipId().equals(receiptId)){
                receipt = Information.receipts.get(i);
            }
        }

        return receipt;
    }
}
