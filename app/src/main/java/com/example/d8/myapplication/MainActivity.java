package com.example.d8.myapplication;

import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.MalformedInputException;

import static android.content.ContentValues.TAG;


//Last modification: Alistair 6/7/2018
//Firebase Support Added

public class MainActivity extends AppCompatActivity {
    //Attributes
    EditText userET, passET;
    authUser aUser;
    Button btnSign;
    Button btnGms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String readUserLocalJson = readJsonFile();
        Log.d("UserLocalJson", readUserLocalJson);
        String isLogin = "false";
        try{
            JSONObject userJsonObj = new JSONObject(readUserLocalJson);
            isLogin = userJsonObj.getString("isLogin");
            Log.d("ISLOGIN", isLogin);

            if(Boolean.parseBoolean(isLogin)){
                Information.user.setName(userJsonObj.getString("name"));
                Information.user.setFirebaseUID(userJsonObj.getString("firebaseUID"));
                Intent homeIntent = new Intent(this, HomeActivity.class);
                startActivity(homeIntent);
            }
        }catch (Exception e){
            Log.e("USERJSONOBJERRPR", e.toString());
        }



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
        btnGms = (Button)findViewById(R.id.sign_in_google);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        aUser.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    }


    //The initial function after onCreate()
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in
        FirebaseAuth.getInstance().signOut();

            Toast.makeText(this, "User Already Logged in? Invalid Exit.",
                    Toast.LENGTH_SHORT).show();
         FirebaseAuth.getInstance().signOut();

         aUser.mGoogleSignInClient.signOut().addOnCompleteListener(this,
                 new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         Toast.makeText(MainActivity.this, "User Already Logged in? Invalid Exit.",
                                 Toast.LENGTH_SHORT).show();
                     }
                 });

    }

    @Override
    public void onDestroy(){
    super.onDestroy();
        FirebaseAuth.getInstance().signOut();

    }

    //Opens the Google Sign In applicaiton
    public void onGoogle(View view){
        Intent signInIntent = aUser.mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 9001);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {

                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        aUser.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = aUser.mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Google Auth Passed",
                                    Toast.LENGTH_SHORT).show();

                            onReady(this, "a");

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Google Auth Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    //Opens the Registration Activity
    public void onRegister(View view){
    Intent goToReg = new Intent(this, RegisterActivity.class);
    startActivity(goToReg);

    }
    //Opens the Home Activity
    public void onReady(OnCompleteListener<AuthResult> view, String execType){
        //Excute VM connections
        BackgroundWorker bgWorker = new BackgroundWorker(MainActivity.this);
        bgWorker.execute(execType, aUser.getUserId(), aUser.getNickName());
        Information.authUser = aUser;


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
                    aUser.MUser();

                    if (task.isSuccessful() && aUser.mUser.isEmailVerified()) {
                        // Sign in success, update UI with the signed-in user's information



                        btnSign.setText(getString(R.string.main_login));
                        btnSign.setClickable(true);



                        //Store user info in Information.user object
                        if(aUser.isLoggedIn()){
                            String firebaseUserId = aUser.mAuth.getCurrentUser().getUid().toString();
                            String userEmail = aUser.mAuth.getCurrentUser().getEmail();
                            String displayName = aUser.mAuth.getCurrentUser().getDisplayName();

                            Log.d("CURRENTUSEREMAILzzzz", userEmail);
                            Log.d("CURRENTUSERIDzzzz", firebaseUserId);
                            Log.d("DISPLAYNAME", displayName);
                            Log.d("USEREMAIL", userEmail);

                            Information.user.setFirebaseUID(firebaseUserId);
                            Information.user.setName(displayName);
                            Information.user.setEmail(userEmail);
                            Information.user.setLogin(true);
                            try{
                                storeUserInfoToLocal(Information.user);
                            }catch (Exception e){

                            }

                            Toast.makeText(MainActivity.this, "Authentication Passed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        onReady(this, type);

                    } else if(task.isSuccessful() && aUser.mUser.isEmailVerified() == false) {
                        //============Fail States for Sign In=================

                        Toast.makeText(MainActivity.this, "Please verify your email to continue, an email has been resent",
                                Toast.LENGTH_SHORT).show();
                        btnSign.setText(getString(R.string.main_login));
                        btnSign.setClickable(true);
                        //Send Validation Email
                        aUser.mUser.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Email sent!");
                                        }else{

                                            Log.d(TAG, "Failed Email Verification!");
                                        }
                                    }
                                });

                    }else {
                          Toast.makeText(MainActivity.this, "Username/Password Incorrect",
                                  Toast.LENGTH_SHORT).show();
                          btnSign.setText(getString(R.string.main_login));
                          btnSign.setClickable(true);
                      }
                    }
                 //===================================//

            });



        }
        //===================================//

    }

    private void storeUserInfoToLocal(User user) throws JSONException {
        //String username = Information.user.getUserName();
        String filename = "_user"+".txt";

        try {
            //generate user in json format
            JSONObject userJson = new JSONObject();

            userJson.put("name",user.getName());
            userJson.put("firebaseUID",user.getFirebaseUID());
            userJson.put("email", user.getEmail());
            userJson.put("isLogin",user.isLogin());

            Log.d("USERJSON", userJson.toString());
            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(userJson.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("STOREERROR:", e.toString());
        }
    }

    private String readJsonFile(){
        //String username = Information.user.getUserName();
        String filename = "_user"+".txt";
        String json = "";
        try{
            FileInputStream inputStream = openFileInput(filename);
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
        return "null";
    }

}
