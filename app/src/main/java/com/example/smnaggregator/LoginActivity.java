package com.example.smnaggregator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    private EditText loginEmail;
    private EditText loginPassword;
    private EditText confPassword;
    private Button loginButton;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = (EditText)findViewById(R.id.emailText);
        loginPassword =(EditText)findViewById(R.id.PasswwordInput);
        confPassword =(EditText)findViewById(R.id.ConfirmPassword);

        mAuth = FirebaseAuth.getInstance();

        loginButton=(Button)findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=loginEmail.getText().toString().trim();
                String password=loginPassword.getText().toString().trim();
                String confirm=confPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    loginEmail.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    loginPassword.setError("Password is Required");
                    return;
                }

                if(TextUtils.isEmpty(confirm)){
                    confPassword.setError("This field is Required");
                    return;
                }
                if(password.length()<6){
                    loginPassword.setError("The Password must be at least 6 digits");
                    return;
                }

                if (!(password.equals(confirm))){
                    Toast.makeText(LoginActivity.this, "Passwords must be matched", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


    }

}