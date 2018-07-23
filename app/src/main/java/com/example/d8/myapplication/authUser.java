package com.example.d8.myapplication;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

//Represents the authorized user(the active user) within the App.
//Last Modification: 6/7/2018 Alistair

class authUser extends User{
    //==========Connection Classes=============//
    FirebaseAuth mAuth; //Firebase Connection
    FirebaseUser mUser; //The active user


    public boolean isPhone() {
        return isPhone;
    }

    public void setPhone(boolean phone) {
        isPhone = phone;
    }

    boolean isPhone = false;
    GoogleSignInClient mGoogleSignInClient; //Google connection
    String authError = "";

    String firebaseUID = "NOT SET"; //Should be checked for

    BackgroundWorker e;
    //=============================================//
    Uri photoUrl; //Only applicable to auth user


    authUser() {
        this.mAuth = FirebaseAuth.getInstance();

    }

    //Signs out the active user
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
         mGoogleSignInClient.signOut();


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
        e.execute("register","http://myvmlab.senecacollege.ca:6207/register.php", getFirebaseUID(), getName(), getEmail());

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
            if(!isPhone()) {
                String name = mUser.getEmail();
                String[] n = name.split("@");
                //Set Attributes
                setUserId(n[0]);
                setEmail(name);
                setName(mUser.getDisplayName());

            }else{

            String name = mUser.getPhoneNumber();
            setUserId(name);
            setEmail(mUser.getPhoneNumber());
                if(mUser.getDisplayName() == null) {
                    setName("Friendly Mobile User :)");

                }else{
                    setName(mUser.getDisplayName());
                }
            }
            setFirebaseUID();
            photoUrl = mUser.getPhotoUrl();
            System.out.println("Account is as follows:" + getUserId() + " " + getEmail() + " " + photoUrl + " " + getName());
        }

    }
    void updateProfile(@Nullable String name, @Nullable String email){

        if(name != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            mUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                System.out.println("Profile Updated!");
                                Information.authUser.setName(name);
                            }else{
                                System.out.println("PAIN IN PROFILE!");
                                Information.authUser.authError = task.getException().getMessage();
                            }
                        }
                    });

        }
        if( (email != null && !email.isEmpty()) && !email.equals(getEmail()) && !isPhone()){
            mUser.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email Updated!");
                            }else{
                                System.out.println("PAIN IN EMAIL! " + task.getException());
                                Information.authUser.authError = task.getException().getLocalizedMessage();


                            }
                        }
                    });


        }

    }
    //Sends a verification email
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










