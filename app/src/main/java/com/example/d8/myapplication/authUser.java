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
class authUser extends User {
    //Attributes
    FirebaseAuth mAuth; //Firebase Connection

    GoogleSignInClient mGoogleSignInClient; //Google connection


    private boolean loggedIn = false;
    authUser() {
        this.mAuth = FirebaseAuth.getInstance();

    }
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


    }
    //Returns true when a user's sessions is still active(they can timeout for instance)
    //Possible security risk?
    boolean isLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Information.user.setUserName(currentUser.getUid());
        loggedIn = (currentUser != null);
        return loggedIn;

    }





}










