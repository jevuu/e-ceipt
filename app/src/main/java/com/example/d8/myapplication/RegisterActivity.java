package com.example.d8.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;


//This activity handles all registration work for creating a user
public class RegisterActivity extends AppCompatActivity {

    Button btnVerify;
    Button btnSignIn;
    EditText emailEms;
    EditText emailEmsConf;
    EditText emailPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


     //   btnSignIn = (Button)findViewById(R.id.reg_btn_signIn);
        btnVerify = (Button)findViewById(R.id.reg_btn_verify);
        emailEms = (EditText)findViewById(R.id.reg_ems);
        emailPass = (EditText)findViewById(R.id.reg_passw);
        emailEmsConf = (EditText)findViewById(R.id.reg_emsConf);

    }
    //Send an email to a user assuming it passes validation
    public void verifyEmail(View view){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String emailAddress = emailEms.getText().toString();
        String emailAddressConf = emailEmsConf.getText().toString();
        String Pass = emailPass.getText().toString();

        if(emailAddress.equals(emailAddressConf) && (!Pass.isEmpty() || !emailAddress.isEmpty() || !emailAddressConf.isEmpty())){
            try {
                btnVerify.setText("Please Wait...");
                btnVerify.setEnabled(false);

                auth.createUserWithEmailAndPassword(emailAddress, Pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Registered, please check your email to verify",
                                            Toast.LENGTH_SHORT).show();


                                    authUser a = new authUser();
                                    a.createUser();


                                    Intent goToMain = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(goToMain);


                                } else {
                                    try {
                                        throw task.getException();
                                    }catch(FirebaseAuthException e){
                                        Toast.makeText(RegisterActivity.this, "Unable to Register, please verify your information" + e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }catch(Exception e) {

                                        Toast.makeText(RegisterActivity.this, "Unable to Register, please verify your information" + e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    btnVerify.setEnabled(true);
                                    btnVerify.setText(R.string.reg_verify);

                                }

                            }
                        });

            }catch (Exception e) {
                Toast.makeText(RegisterActivity.this, "Something Went Wrong, Please try again",
                        Toast.LENGTH_SHORT).show();
                btnVerify.setEnabled(true);
                btnVerify.setText(R.string.reg_verify);

            }
        }else{
            Toast.makeText(RegisterActivity.this, "Please verify your information",
                    Toast.LENGTH_SHORT).show();
            btnVerify.setEnabled(true);
            btnVerify.setText(R.string.reg_verify);
        }



    }

}
