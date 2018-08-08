package com.example.d8.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.Layout;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;


//Last modification: Alistair 6/7/2018
//Firebase Support Added

public class MainActivity extends AppCompatActivity {
    //Attributes
    EditText userET, passET;
    authUser aUser;
    Button btnSign;
    Button btnGms;

    ProgressBar pb;
    Button btnPhn;
    Button btnCode;
    LinearLayout ltn;
    EditText phoneET;
    EditText codeET;


    Boolean ltnVisFlag = false; //Flag for button visibility
    FirebaseAuth mAuth;

    private String mVerificationId;
    static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    boolean mVerificationInProgress = false;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    //Shared Preferances(stored to device)
    public static final String PREFS_NAME = "prefs_authUser";
    private static final String PREF_USERNAME = "";

    private ConstraintLayout bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = findViewById(R.id.pb3);
        bg = findViewById(R.id.cln);

        GradientDrawable backgroundGradient = (GradientDrawable)bg.getBackground();


        try {
            //Set Day-Night theme
            SharedPreferences settings = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            int themeDayNight=settings.getInt("Theme_DayNight", AppCompatDelegate.MODE_NIGHT_NO);//Default day
            AppCompatDelegate.setDefaultNightMode(themeDayNight);

            //Set window's background color from saved settings.
            int bc = settings.getInt("Background_Color", Color.CYAN);//Default white color
            if(themeDayNight==AppCompatDelegate.MODE_NIGHT_YES){
                bc=getResources().getColor(R.color.colorEceiptDarkGrey);
                ConstraintLayout constrainLayout=(ConstraintLayout)findViewById(R.id.cln);
                GradientDrawable gd=(GradientDrawable)constrainLayout.getBackground();
                gd.setColors(new int[]{getResources().getColor(R.color.colorEceiptBlue),bc});
                constrainLayout.setBackground(gd);
            }


        }
        catch(Exception ex)
        {

        }

        if (!isTaskRoot()) {
            finish();
            return;
        }


        mAuth = FirebaseAuth.getInstance();

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

        btnPhn = (Button)findViewById(R.id.main_phone);
        btnCode = (Button)findViewById(R.id.main_phone_b2);
        ltn = (LinearLayout) findViewById(R.id.ltns);
        phoneET = (EditText)findViewById(R.id.main_phoneIn);
        codeET = (EditText)findViewById(R.id.main_code);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        aUser.mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Checks for stored username from the last user to login
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String remember = sharedPref.getString("pref_username", "");
        if(!remember.isEmpty()){
            userET.setText(remember);
        }

        SharedPreferences colorBg  = getSharedPreferences("Settings", MODE_PRIVATE);
        int colourBg = colorBg.getInt("bg", 0);
        if(colourBg != 0){
           backgroundGradient = (GradientDrawable)bg.getBackground();
           backgroundGradient.setColors(new int[] {getResources().getColor(R.color.colorEceiptBlue),colourBg});
        }

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        //Hides the auto keyboard
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);





        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // ...
            }
        };



    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        aUser.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = aUser.mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Phone Authentication Passed",
                                    Toast.LENGTH_SHORT).show();
                            aUser.setPhone(true);
                            aUser.createUser();

                           // aUser.contactSql_log(getBaseContext());
                            aUser.contactSql_reg(getBaseContext());
                            Information.authUser = aUser;
                            onReady(this, "a");

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                System.out.println(task.getException().getMessage());
                                Toast.makeText(MainActivity.this, "Phone Authentication Failed: " + task.getException().getMessage() ,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    //The initial function after onCreate()
    //This checks for previously logged in users with valid sessions and logs them in.

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && currentUser.isEmailVerified() ){
            aUser.createUser();
            Information.authUser = aUser;
            if(!aUser.isPhone()) {
                //Saves the username of this user to preferances for next login, clean.
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("pref_username", aUser.getEmail());
                editor.apply();

            }else{
                //Clears the Pref
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("pref_username","");
                editor.apply();

            }
            DataController.SyncronizeData("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php", this);
            Intent goToReg = new Intent(this, MenuActivity.class);
            goToReg.putExtra("finish", true);
            goToReg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(goToReg);
            finish();


        }

        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(phoneET.getText().toString());
        }


    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    private boolean validatePhoneNumber() {
        String phoneNumber = phoneET.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }

        return true;
    }
    //Reveal the Phone Auth UI
    public void onPhoneAuth(View view){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(bg);
        }


        ///Hide or display the phone auth as pressed
        if(!ltnVisFlag) {
            ltn.setVisibility(View.VISIBLE);
            ltnVisFlag = true;
        }else{
            ltn.setVisibility(View.GONE);

            ltnVisFlag = false;
        }

    }

    //Capture the phone number and send sms
    public void onSMS(View view){
        if (!validatePhoneNumber()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(bg);
        }
        btnCode.setVisibility(View.VISIBLE);
        codeET.setVisibility(View.VISIBLE);

        String t = phoneET.getText().toString();
        t = "+"+t;
        startPhoneNumberVerification(t);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        mVerificationInProgress = true;
    }


    public void onCode(View view){

        String code = codeET.getText().toString();
        if(!code.isEmpty()) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

            signInWithPhoneAuthCredential(credential);
        }else{
            Toast.makeText(MainActivity.this, "You must enter a valid security code! eg: 123456",
                    Toast.LENGTH_SHORT).show();

        }

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
                            aUser.createUser();
                            aUser.contactSql_reg(getBaseContext());

                            Information.authUser = aUser;
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
    //Fragment MenuActivity Call
    public void onReady(OnCompleteListener<AuthResult> view, String execType){
        //Excute VM connections
        aUser.contactSql_log(this);
        Information.authUser = aUser;


        if(!aUser.isPhone()) {
            //Saves the username of this user to preferances for next login, clean.
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("pref_username", aUser.getEmail());
            editor.apply();

        }
        DataController.SyncronizeData("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php", this);

        Intent goToReg = new Intent(this, MenuActivity.class);
        goToReg.putExtra("finish", true);
        goToReg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goToReg);
        finish();

    }
    //Opens the Password Reset Activity
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TransitionManager.beginDelayedTransition(bg);
            }
            userET.setVisibility(View.GONE);
            passET.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
            btnSign.setClickable(false);


            //Firebase Authentication
            aUser.mAuth.signInWithEmailAndPassword(userString, passString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    aUser.MUser();
                    aUser.createUser();

                    if (task.isSuccessful() && aUser.mUser.isEmailVerified()) {
                        // Sign in success, update UI with the signed-in user's information
                        btnSign.setText(getString(R.string.main_login));
                        btnSign.setClickable(true);

                        Toast.makeText(MainActivity.this, "Welcome to ECeipt",
                                Toast.LENGTH_SHORT).show();

                        onReady(this, type);

                    } else if(task.isSuccessful() && aUser.mUser.isEmailVerified() == false) {
                        //============Fail States for Sign In=================

                        Toast.makeText(MainActivity.this, "Please verify your email to continue, an email has been sent",
                                Toast.LENGTH_SHORT).show();
                        btnSign.setText(getString(R.string.main_login));
                        btnSign.setClickable(true);

                        //Send Validation Email
                        aUser.sendVerification();


                    }else {
                        Toast.makeText(MainActivity.this, "Username/Password Incorrect",
                                Toast.LENGTH_SHORT).show();
                        btnSign.setText(getString(R.string.main_login));
                        btnSign.setClickable(true);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(bg);
                    }
                    userET.setVisibility(View.VISIBLE);
                    passET.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);

                }
                //===================================//

            });



        }
        //===================================//

    }


}
