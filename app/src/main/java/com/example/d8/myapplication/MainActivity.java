package com.example.d8.myapplication;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


//Last modification: Alistair
//Firebase Support Added

public class MainActivity extends AppCompatActivity {
    //Attributes
    EditText userET, passET;
    authUser aUser;
    Button btnSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Firebase
        aUser = new authUser();

        //Hides the Actionbar
        ActionBar ag = getSupportActionBar();
        if(ag != null) {
            ag.hide();
        }

        userET = (EditText)findViewById(R.id.main_uid);
        passET = (EditText)findViewById(R.id.main_pwd);
        btnSign = (Button)findViewById(R.id.main_btn_login);
    }


    //The initial function after onCreate()
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in
        if(aUser.isLoggedIn()) {
            Toast.makeText(this, "User Already Logged in? Invalid Exit.",
                    Toast.LENGTH_SHORT).show();
         FirebaseAuth.getInstance().signOut();
        }
    }

    //Opens the Registration Activity
    public void onRegister(View view){
    Intent goToReg = new Intent(this, RegisterActivity.class);
    startActivity(goToReg);

    }
    //Opens the Registration Activity
    public void onReady(OnCompleteListener<AuthResult> view){
        Intent goToReg = new Intent(this, HomeActivity.class);
        startActivity(goToReg);

   }
    //Opens the Password Reset  Activity
    public void onPassW(View view){
        Intent goToPws = new Intent(this, PassActivity.class);
        startActivity(goToPws);
    }

    public void onLogin(View view) {

        //Checks if GMS services are available(for emulators) for safety
        try {
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if (status != ConnectionResult.SUCCESS) {
                throw new Exception("Install Google Play Services ASAP");
            }
        } catch (Exception e) {
            System.exit(-1);
        }

        //Get user data
        String userString = userET.getText().toString();
        String passString = passET.getText().toString();
        String type = "login";


        //===========Ensure data exists before sending to firebase==========
        if (userString.isEmpty() || passString.isEmpty()) {
            Toast.makeText(MainActivity.this, "Username/Password cannot be empty!",
                    Toast.LENGTH_SHORT).show();
        } else {

            btnSign.setText("Please Wait...");
            btnSign.setClickable(false);

            //Firebase Authentication
            aUser.mAuth.signInWithEmailAndPassword(userString, passString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(MainActivity.this, "Authentication Passed",
                                Toast.LENGTH_SHORT).show();
                        btnSign.setText(getString(R.string.main_login));
                        btnSign.setClickable(true);

                        //Go to home screen
                        onReady(this);


                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, "Authentication Failed.",
                                Toast.LENGTH_SHORT).show();
                        btnSign.setText(getString(R.string.main_login));
                        btnSign.setClickable(true);

                    }
                }
            });
            //My.SQL VM Connection
            BackgroundWorker bgWorker = new BackgroundWorker(this);
            bgWorker.execute(type, userString, passString);


        }
        //===================================//

    }

}
