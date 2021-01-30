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

import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountActivity";

    private EditText createEmail;
    private EditText createPassword;
    private EditText confPassword;
    private Button createButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        createEmail = findViewById(R.id.emailText);
        createPassword = findViewById(R.id.PasswwordInput);
        confPassword = findViewById(R.id.ConfirmPassword);
        //initialize firebase Auth
        mAuth = FirebaseAuth.getInstance();


        createButton = (Button) findViewById(R.id.create_btn);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = createEmail.getText().toString().trim();
                String password = createPassword.getText().toString().trim();
                String confirm = confPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    createEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    createPassword.setError("Password is Required");
                    return;
                }

                if (TextUtils.isEmpty(confirm)) {
                    confPassword.setError("This field is Required");
                    return;
                }
                if (password.length() < 6) {
                    createPassword.setError("The Password must be at least 6 digits");
                    return;
                }

                if (!(password.equals(confirm))) {
                    Toast.makeText(CreateAccountActivity.this, "Passwords must be matched", Toast.LENGTH_SHORT).show();
                    return;
                }

                //register the user in firebase//
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, sending them on another activity
                            Log.d(TAG, "create user with email: success");
                            Toast.makeText(CreateAccountActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        } else {
                            //If sign in fails, display a message to the user
                            Log.w(TAG, "createUserWithEmail: failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI();

        }
    }

    private void updateUI(){
        Toast.makeText(CreateAccountActivity.this, "You're logged in ", Toast.LENGTH_SHORT).show();

        Intent accountIntent = new Intent(CreateAccountActivity.this, MenuActivity.class);
        startActivity(accountIntent);
        finish();

    }
}




