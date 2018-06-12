package com.example.d8.myapplication;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SyncronizeData extends AsyncTask<String, Void, String>{

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

    //in this method we are fetching the json string
    @Override
    protected String doInBackground(String... params) {

        try {
            //creating a URL
            URL url = new URL(params[0]);
            Log.i("URL", url.toString());

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
                //appending it to string builder
                sb.append(json + "\n");
            }

            String jsonReturn = sb.toString().trim();
            try {
                //loadIntoListView(s);
                //DataController.storeJsonToLocal(jsonReturn, );
            } catch (Exception e) {
                Log.e("ERROR:", e.toString());
            }

            //finally returning the read string
            return sb.toString().trim();
        } catch (Exception e) {
            return null;
        }

    }

    private void storeJsonToLocal(String json, Context ctx) throws JSONException {
        //String username = Information.user.getUserName();
        String filename = "_receipts"+".txt";

        try {
            FileOutputStream outputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.e("StoreError:",e.toString());
        }
    }

}