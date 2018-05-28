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
import com.google.firebase.auth.FirebaseAuth;


//This activity handles all registration login for creating a user

public class PassActivity extends AppCompatActivity {

    Button btnSign;
    EditText emailET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);


        btnSign = (Button)findViewById(R.id.pass_rw);
        emailET = (EditText)findViewById(R.id.pass_res);

    }

    public void sendEmail(View view){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = emailET.getText().toString();

            try {
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent goToMain = new Intent(PassActivity.this, MainActivity.class);
                                    startActivity(goToMain);
                                }else{
                                    Toast.makeText(PassActivity.this, "This email appears to be invalid",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



            } catch (Exception e) {
                Toast.makeText(PassActivity.this, "This email appears to be invalid",
                        Toast.LENGTH_SHORT).show();

            }

    }


}




