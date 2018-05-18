package com.example.d8.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Last modification: Alistair
//Added a hide actionbar
public class MainActivity extends AppCompatActivity {
    //Attributes
    EditText userET, passET;
    authUser aUser;
    private FirebaseAuth eAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aUser = new authUser();
        //Hides the Actionbar
        ActionBar ag = getSupportActionBar();
        if(ag != null) {
            ag.hide();
        }


        eAuth = FirebaseAuth.getInstance();
        userET = (EditText)findViewById(R.id.main_uid);
        passET = (EditText)findViewById(R.id.main_pwd);
    }


    //The initial function after onCreate()
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in
        if(aUser.isLoggedIn())
        Toast.makeText(this, "User Already Logged in? Invalid Exit.",
                Toast.LENGTH_SHORT).show();
    }

    public void onRegister(View view){

    Intent goToReg = new Intent(this, RegisterActivity.class);
    startActivity(goToReg);


    }
    public void onLogin(View view) {

        try {
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if (status == ConnectionResult.SUCCESS) {
                //Success! Do what you want
                System.out.println("GOOD!");
            } else {
                throw new Exception("Install Google Play Services ASAP");
            }
        }catch(Exception e){
            System.exit(-1);
        }

        String userString = userET.getText().toString();
        String passString = passET.getText().toString();
        String type = "login";


        eAuth.signInWithEmailAndPassword(userString, passString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(MainActivity.this, "Authentication OK!.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });


        BackgroundWorker bgWorker = new BackgroundWorker(this);

        bgWorker.execute(type, userString, passString);
    }
}
