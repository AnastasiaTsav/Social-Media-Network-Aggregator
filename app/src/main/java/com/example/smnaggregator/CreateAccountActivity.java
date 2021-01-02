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

        createEmail = findViewById(R.id.emailText);
        createPassword = (EditText) findViewById(R.id.PasswwordInput);
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

        /*// Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton fbLoginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        fbLoginBtn.setReadPermissions("email", "public_profile");
        fbLoginBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

            }
        }); */

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


    /*
     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
         mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/



}




