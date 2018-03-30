package com.example.d8.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

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
        String login_URL = "http://pandazooka.net/prj566/login.php";
        if(type.equals("login")){
            try {
                String userName = params[1];
                //String password = params[2];    //There is no password property in the SQL table. This data isn't passed.

                URL url = new URL(login_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String postData = URLEncoder.encode("userName", "UTF-8")+"="+URLEncoder.encode(userName,"UTF-8");
                                //+"&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bWriter.write(postData);
                bWriter.flush();
                bWriter.close();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bReader.readLine()) != null){
                    result += line;
                }
                bReader.close();
                is.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
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
        aDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... strings){
        super.onProgressUpdate(strings);
    }
}
