package com.example.d8.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


//this class handles some of the actions which may reuse many time
public class DataController {
    //add new receipt data to local json file
    static void addReceiptToLocal(Receipt receipt, Context ctx)throws JSONException{
        String receiptsJSON = DataController.readJsonFile(Information.RECEIPTSLOCALFILENAME, ctx);

        JSONArray receiptsJsonArray = new JSONArray(receiptsJSON);
        JSONObject jsonObject = new JSONObject();
        JSONArray itemsJsonArray = new JSONArray();
        JSONObject itemJsonObject = new JSONObject();
        //itemsJsonArray.put(itemJsonObject);

        String company = receipt.getBusinessName();
        String date = receipt.getDate();
        String username = Information.authUser.getName();
        String userId = Information.authUser.getUserId();
        String tCost = Double.toString(receipt.getTotalCost());
        String tax = "14";

        jsonObject.put("name", username);
        jsonObject.put("receiptID", "-1");
        jsonObject.put("date", date);
        jsonObject.put("totalCost", tCost);
        jsonObject.put("tax", tax);
        jsonObject.put("businessName", company);
        jsonObject.put("items",itemsJsonArray);

        String jsonString = jsonObject.toString();

        Log.i("JSONINAddReceiptForm:", jsonString);

        receiptsJsonArray.put(jsonObject);

        String jArrayString = receiptsJsonArray.toString();
        Log.i("JArrAddReceiptForm:", jArrayString);

        DataController.storeJsonToLocal(jArrayString, Information.RECEIPTSLOCALFILENAME, ctx);
    }


    static JSONObject toJsonObject(Receipt receipt, Context ctx)throws JSONException{

        JSONObject jsonObject = new JSONObject();
        JSONArray itemsJsonArray = new JSONArray();
        JSONObject itemJsonObject = new JSONObject();
        //itemsJsonArray.put(itemJsonObject);


        jsonObject.put("name", receipt.getName());
        jsonObject.put("receiptID", receipt.getReceipId());
        jsonObject.put("date", receipt.getDate());
        jsonObject.put("totalCost", receipt.getTotalCost());
        jsonObject.put("tax", receipt.getTax());
        jsonObject.put("businessName", receipt.getBusinessName());
        jsonObject.put("items",itemsJsonArray);

        //String jsonString = jsonObject.toString();

        Log.i("JSONINAddReceiptForm:", jsonObject.toString());


        return jsonObject;
    }

    //add new receipt data to remote databse
    static void addReceiptToDB(Receipt receipt){

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
            e.printStackTrace();
        }
        return null;
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
        String RECEIPTDATAFILE = "_receipts.txt";

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
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("GET");
                    con.connect();
                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
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
                    storeJsonToLocal(jsonReturn, RECEIPTDATAFILE, ctx);
                    //storeJsonToLocal(jsonReturn);

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
        String result="";
        try{
            result = getJSON.execute().get();
        }catch (Exception e){

        }

        return result;
    }

    //this method is to load json format receipts data to "Information.receipts" object
    public static void loadReceiptsObj(String json) throws JSONException {
        if(json==null || json.equals("[]"))
            return;

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

                    receipt.addItem(itemObj.getString("itemName"), itemObj.getString("itemDesc"), Double.parseDouble(itemObj.getString("itemPrice")));
                }

                Information.receipts.add(receipt);

            }
        }catch(Exception e){
            Log.e("ERRRRR:",e.toString());
        }

        if(!Information.receipts.get(0).getItems().isEmpty()){
            Log.d("RECEIPTOBJ2:",Information.receipts.get(0).getItems().get(1).getItemName());

        }
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

        Log.d("RECEIPTOBJ", "name:"+receipt.getName()+
                "id:"+receipt.getReceipId()+
                "date:"+receipt.getDate()+
                "totalCost:"+receipt.getTotalCost()+receipt.getTax());

        JSONArray itemarray = obj.getJSONArray("items");

        Log.d("ITEMARRAYL:",itemarray.toString());

        for(int j=0; j<itemarray.length();j++){
            JSONObject itemObj = itemarray.getJSONObject(j);

            receipt.addItem(itemObj.getString("itemName"), itemObj.getString("itemDesc"), Double.parseDouble(itemObj.getString("itemPrice")));
        }
        return receipt;
    }

    public static Receipt getReceipt(String receiptId)
    {
        Receipt r=null;
        for(int i=0;i<Information.receipts.size();i++)
        {
            if(Information.receipts.get(i).getReceipId().equals(receiptId)) {
                r = Information.receipts.get(i);
                break;
            }
        }
        return r;
    }
    public static boolean deleteReceipt(String receiptId)
    {
        boolean r=false;
        Receipt re=getReceipt(receiptId);
        if(re!=null) {
            Information.receipts.remove(re);
            r = true;
        }
        return r;
    }

}
