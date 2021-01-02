package com.example.smnaggregator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    private static final String TAG="Login_Activity";
    private EditText loginEmail;
    private EditText loginPassword;
    private Button loginButton;
    private Button noAccountButton;
    private FirebaseAuth mAuth;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.login_password);
        //initialize firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Check if the user is already logged in -> send them to an other window of the app
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            finish();
        }

        loginButton=findViewById(R.id.twitterLoginBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    loginEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    loginPassword.setError("Password is Required");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Sign in success, sending them on another activity
                                    Log.d(TAG, "Login user with email: success");
                                    Toast.makeText(Login_Activity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                                } else {
                                    //If sign in fails, display a message to the user
                                    Log.w(TAG, "LoginUserWithEmail: failure", task.getException());
                                    Toast.makeText(Login_Activity.this, "Authentication failed ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




            }

        });

        noAccountButton=findViewById(R.id.no_account_button);
        noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
            }
        });

    }


}