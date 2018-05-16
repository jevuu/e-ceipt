package com.example.d8.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText userET, passET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userET = (EditText)findViewById(R.id.main_uid);
        passET = (EditText)findViewById(R.id.main_pwd);
    }

    public void onLogin(View view) {
        String userString = userET.getText().toString();
        String passString = ""; //passET.getText().toString();
        String type = "login";

        BackgroundWorker bgWorker = new BackgroundWorker(this);

        bgWorker.execute(type, userString, passString);
    }
}
