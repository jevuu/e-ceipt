package com.example.d8.myapplication;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.UserProfileChangeRequest;
import static android.content.ContentValues.TAG;

//Represents the authorized user(the active user) within the App.
//Last Modification: 6/7/2018 Alistair

class authUser extends User{
    //==========Connection Classes=============//
    FirebaseAuth mAuth; //Firebase Connection
    FirebaseUser mUser; //The active user
    GoogleSignInClient mGoogleSignInClient; //Google connection


    String firebaseUID = "NOT SET"; //Should be checked for

    BackgroundWorker e;
    //=============================================//
    Uri photoUrl; //Only applicable to auth user


    authUser() {
        this.mAuth = FirebaseAuth.getInstance();

    }
    //Get Firebase UID
    public String getFirebaseUID() {
        return firebaseUID;
    }
    //Set Firebase UID
    public void setFirebaseUID() {
        firebaseUID = mAuth.getCurrentUser().getUid();
    }
    //Contact My.SQL with the register command
    void contactSql_reg(Context ct){
        e = new BackgroundWorker(ct);
        e.execute("register","http://myvmlab.senecacollege.ca:6207/register.php", getUserId(), getUserId(), getEmail());

    }
    //Contact My.SQL with the register command
    void contactSql_log(Context ct){
        e = new BackgroundWorker(ct);
        e.execute("login","http://myvmlab.senecacollege.ca:6207/login.php", getUserId());

    }
    //Constructor
    void MUser(){
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    //Creates a User Object
    void createUser() {
        MUser();

        if (mUser != null) {
            String name = mUser.getEmail();
            String[] n = name.split("@");

            //Set Attributes
            setUserId(n[0]);
            setEmail(name);
            setName(getUserId());
            setFirebaseUID();

            photoUrl = mUser.getPhotoUrl();

            System.out.println("Account is as follows:" + getUserId() + " " + getEmail() + " " + photoUrl);
        }

    }
    void updateProfile(String name){

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        mUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Profile Updated!");
                        }
                    }
                });

    }
    //Sends a verifcation email
    void sendVerification(){
        mUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent!");
                        } else {

                            Log.d(TAG, "Failed Email Verification!");
                        }
                    }
                });
    }






}










