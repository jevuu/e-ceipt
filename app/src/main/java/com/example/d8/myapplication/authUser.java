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
class authUser extends User{
    //==========Connection Classes=============//
    FirebaseAuth mAuth; //Firebase Connection
    FirebaseUser mUser; //The active user
    GoogleSignInClient mGoogleSignInClient; //Google connection

    BackgroundWorker e;
    //=============================================//
    Uri photoUrl; //Only applicable to auth user

    private boolean loggedIn = false;
    authUser() {
        this.mAuth = FirebaseAuth.getInstance();


    }
    void contactSql(Context ct){
        e = new BackgroundWorker(ct);
        e.execute("register","http://myvmlab.senecacollege.ca:6207/register.php", getUserId(), getNickName(), getEmail());
        System.out.println("D");


    }

    //Returns true when a user's sessions is still active(they can timeout for instance)
    //Possible security risk?
    boolean isLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Information.user.setUserName(currentUser.getUid());
        loggedIn = (currentUser != null);
        return loggedIn;

    }


    void MUser(){
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    //Creates a User Object
    //This also updates a firebase profile with matching data for the display name
  void createUser() {
      MUser();

      if (mUser != null) {
          String name = mUser.getEmail();
          String[] n = name.split("@");

          setNickName(name = n[0]);
          setUserId(getNickName());
          setEmail(mUser.getEmail());
          photoUrl = mUser.getPhotoUrl();


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


          System.out.println("Account is as follows:" + getNickName() + " " + getEmail() + " " + photoUrl);
      }

  }






}










