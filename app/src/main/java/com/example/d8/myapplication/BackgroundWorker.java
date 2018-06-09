package com.example.d8.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

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
        String login_URL = "http://myvmlab.senecacollege.ca:6207/login.php";
        if(type.equals("login")){

            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;

            try {
                    //constants
                    URL url = new URL(login_URL);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userName", params[1]);
                    /*
                    jsonObject.put("comment", "OK");
                    jsonObject.put("category", "pro");
                    jsonObject.put("day", "19");
                    jsonObject.put("month", "8");
                    jsonObject.put("year", "2015");
                    jsonObject.put("hour", "16");
                    jsonObject.put("minute", "41");
                    jsonObject.put("day_of_week", "3");
                    jsonObject.put("week", "34");
                    jsonObject.put("rate_number", "1");
                    */
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

                /*
                String userName = params[1];
                //String password = params[2];    //There is no password property in the SQL table. This data isn't passed.


                URL url = new URL(login_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                httpURLConnection.setDoInput(true);
                //httpURLConnection.setFixedLengthStreamingMode(queryString.getBytes().length);
                //Uri.Builder builder = new Uri.Builder().appendQueryParameter("userName", userName);
                //String query = builder.build().getEncodedQuery();

                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String postData = URLEncoder.encode("userName", "UTF-8")+"="+URLEncoder.encode(userName,"UTF-8");
                                //+"&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bWriter.write(postData);
                bWriter.flush();
                bWriter.close();
                os.close();

                //httpURLConnection.connect();
                */
                //InputStream
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
<<<<<<< HEAD
        aDialog.show();
=======
        System.out.println(result);
>>>>>>> master

    }

    @Override
    protected void onProgressUpdate(String... strings){
        super.onProgressUpdate(strings);
    }
}
