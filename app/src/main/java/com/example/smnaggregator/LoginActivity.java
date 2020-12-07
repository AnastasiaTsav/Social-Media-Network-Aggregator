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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static String EMAIL;

    private EditText loginEmail;
    private EditText loginPassword;
    private EditText confPassword;
    private Button loginButton;
    private Button fbLoginBtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = (EditText)findViewById(R.id.emailText);
        loginPassword =(EditText)findViewById(R.id.PasswwordInput);
        confPassword =(EditText)findViewById(R.id.ConfirmPassword);

        mAuth = FirebaseAuth.getInstance();

        //Check if the user is already logged in -> send them to an other window of the app
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            finish();
        }

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

                //register the user in firebase//
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Sign in success, sending them on another activity
                            Log.d(TAG, "create user with email: success");
                            Toast.makeText(LoginActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        }else{
                            //If sign in fails, display a message to the user
                            Log.w(TAG, "createUserWithEmail: failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });





    Log.d(TAG, "onCreate ends");
    }



}