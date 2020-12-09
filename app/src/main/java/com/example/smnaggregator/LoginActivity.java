package com.example.smnaggregator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";


    private EditText loginEmail;
    private EditText loginPassword;
    private EditText confPassword;
    private Button loginButton;
    private Button fbLoginBtn;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    //USER INFORMATION
    private TextView textViewUser;
    private ImageView mLogo;

    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = (EditText) findViewById(R.id.emailText);
        loginPassword = (EditText) findViewById(R.id.PasswwordInput);
        confPassword = (EditText) findViewById(R.id.ConfirmPassword);
        mLogo = (ImageView) findViewById(R.id.userLogo);
        textViewUser = (TextView) findViewById(R.id.userInfo);
        //initialize firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Check if the user is already logged in -> send them to an other window of the app
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            finish();
        }


        loginButton = (Button) findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();
                String confirm = confPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    loginEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    loginPassword.setError("Password is Required");
                    return;
                }

                if (TextUtils.isEmpty(confirm)) {
                    confPassword.setError("This field is Required");
                    return;
                }
                if (password.length() < 6) {
                    loginPassword.setError("The Password must be at least 6 digits");
                    return;
                }

                if (!(password.equals(confirm))) {
                    Toast.makeText(LoginActivity.this, "Passwords must be matched", Toast.LENGTH_SHORT).show();
                    return;
                }

                //register the user in firebase//
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, sending them on another activity
                            Log.d(TAG, "create user with email: success");
                            Toast.makeText(LoginActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        } else {
                            //If sign in fails, display a message to the user
                            Log.w(TAG, "createUserWithEmail: failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        // Initialize Facebook Login button
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
        Toast.makeText(LoginActivity.this, "You're logged in ", Toast.LENGTH_SHORT).show();

        Intent accountIntent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(accountIntent);
        finish();

    }


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
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}




