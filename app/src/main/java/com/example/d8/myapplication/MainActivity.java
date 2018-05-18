package com.example.d8.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Last modification: Alistair
//Added a hide actionbar
public class MainActivity extends AppCompatActivity {
    //Attributes
    EditText userET, passET;
   firebaseController firebase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hides the Actionbar
        ActionBar ag = getSupportActionBar();
        if(ag != null) {
            ag.hide();
        }






        userET = (EditText)findViewById(R.id.main_uid);
        passET = (EditText)findViewById(R.id.main_pwd);
    }


    //The initial function after onCreate()
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in
     //    firebase.currentUser = mAuth.getCurrentUser();
        Toast.makeText(this, "User Already Logged in? Invalid Exit.",
                Toast.LENGTH_SHORT).show();
    }

    public void onRegister(View view){

    Intent goToReg = new Intent(this, RegisterActivity.class);
    startActivity(goToReg);


    }
    public void onLogin(View view) {



        String userString = userET.getText().toString();
        String passString = ""; //passET.getText().toString();
        String type = "login";

        BackgroundWorker bgWorker = new BackgroundWorker(this);

        bgWorker.execute(type, userString, passString);
    }
}
