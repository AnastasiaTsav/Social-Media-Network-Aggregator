package com.example.smnaggregator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {


    private EditText loginEmail;
    private EditText loginPassword;
    private EditText confPassword;

    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.emailText);
        loginPassword = findViewById(R.id.PasswwordInput);
        confPassword = findViewById(R.id.ConfirmPassword);


    }

}