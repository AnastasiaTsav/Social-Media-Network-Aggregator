package com.example.smnaggregator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    //views
    EditText mEmail, mPassword;
    Button mRegisterBtn;

    //progress bar to display while registering user
    ProgressDialog progressDialog;

    //Declare an instance of FirebaseAuth//
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Account");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init buttons
        mEmail = findViewById(R.id.emailText);
        mPassword = findViewById(R.id.passwordInput);
        mRegisterBtn = findViewById(R.id.finalRegisterBtn);

        //In the onCreate() method, initialize the FirebaseAuth instance

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        //handle register btn click
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input email, password
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                //validate
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    //set error and focus to email editText
                    mEmail.setError("Invalid Email");
                    mEmail.setFocusable(true);
                }
                else if(password.length() < 6){
                    //set error and focus to password editText
                    mPassword.setError("Password length at least 6 characters");
                    mPassword.setFocusable(true);
                }
                else{
                    registerUser(email,password); //register the user
                }


            }
        });

    }

    private void registerUser(String email, String password){
        //email&password pattern is valid, show progress dialog and start registering user

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go to the previous activity
        return super.onSupportNavigateUp();
    }
}