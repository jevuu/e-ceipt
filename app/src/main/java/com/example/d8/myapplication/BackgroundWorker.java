package com.example.d8.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String, String, String> {

    Context context;
    AlertDialog aDialog;

    BackgroundWorker (Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_URL = params[1];
        if(type.equals("register")){

            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;

            try {
                    //constants
                    URL url = new URL(login_URL);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userID", params[2]);
                    jsonObject.put("name", params[3]);
                    jsonObject.put("email", params[4]);

                    String message = jsonObject.toString();

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout( 10000 /*milliseconds*/ );
                    conn.setConnectTimeout( 15000 /* milliseconds */ );
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(message.getBytes().length);

                    //make some HTTP header nicety
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    conn.connect();

                //setup send
                os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                //clean up
                os.flush();

                is = conn.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bReader.readLine()) != null){
                    result += line;
                }
                bReader.close();
                is.close();
                conn.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        else if(type.equals("login")){

            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;

            try {
                //constants
                URL url = new URL(login_URL);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userName", params[2]);


                String message = jsonObject.toString();

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout( 10000 /*milliseconds*/ );
                conn.setConnectTimeout( 15000 /* milliseconds */ );
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(message.getBytes().length);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                //open
                conn.connect();
                //setup send
                os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                //clean up
                os.flush();

                is = conn.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bReader.readLine()) != null){
                    result += line;
                }
                bReader.close();
                is.close();
                conn.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }




        }
        else if(type.equals("extern_photo")){
           //TODO Add Image Collection

        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        aDialog = new AlertDialog.Builder(context).create();
        aDialog.setTitle("Log In Status");

    }

    @Override
    protected void onPostExecute(String result){
        aDialog.setMessage(result);
        System.out.println(result);

    }

    @Override
    protected void onProgressUpdate(String... strings){
        super.onProgressUpdate(strings);
    }
}
