package com.example.d8.myapplication;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.GoogleAuthProvider;


import java.util.concurrent.Executor;

//Represents the authorized user(the active user) within the App.
<<<<<<< HEAD
class authUser extends User {
    //Attributes
=======
//Last Modification: 6/7/2018 Alistair

class authUser extends User{
    //==========Connection Classes=============//
>>>>>>> master
    FirebaseAuth mAuth; //Firebase Connection

    GoogleSignInClient mGoogleSignInClient; //Google connection

<<<<<<< HEAD
=======

    String firebaseUID = "NOT SET"; //Should be checked for

    BackgroundWorker e;
    //=============================================//
    Uri photoUrl; //Only applicable to auth user
>>>>>>> master


    authUser() {
        this.mAuth = FirebaseAuth.getInstance();

<<<<<<< HEAD
    }
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
=======
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
>>>>>>> master

            System.out.println("Account is as follows:" + getUserId() + " " + getEmail() + " " + photoUrl);
        }

    }
<<<<<<< HEAD
    //Returns true when a user's sessions is still active(they can timeout for instance)
    //Possible security risk?
    boolean isLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        loggedIn = (currentUser != null);
        return loggedIn;
=======
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
>>>>>>> master

    }





}










