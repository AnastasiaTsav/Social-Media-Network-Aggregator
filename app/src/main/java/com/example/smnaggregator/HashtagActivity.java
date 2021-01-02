package com.example.smnaggregator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.Serializable;

public class HashtagActivity extends AppCompatActivity implements Serializable {

    private static final String TAG = "HashTagActivity";

    private static final String twitterkey = BuildConfig.twitterkey;
    private static final String twittersecret = BuildConfig.twittersecret;

    private TwitterLoginButton twitterLoginButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //This code must be entering before the setContentView to make the twitter login work...
        TwitterAuthConfig mTwitterAuthConfig = new TwitterAuthConfig(twitterkey, twittersecret);

        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(mTwitterAuthConfig)
                .build();
        Twitter.initialize(twitterConfig);

        setContentView(R.layout.activity_hashtag);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        twitterLoginButton = findViewById(R.id.twitterLoginBtn);


        mAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null){
                startActivity(new Intent(HashtagActivity.this, TrendsActivity.class));
            }
        };

        UpdateTwitterButton();

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(HashtagActivity.this, "Signed in to twitter successful", Toast.LENGTH_LONG).show();
                signInToFirebaseWithTwitterSession(result.data);
                Log.d(TAG, "Signed in twitter successful");
                updateUI();
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }


            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(HashtagActivity.this, "Login failed. No internet or No Twitter app found on your phone", Toast.LENGTH_LONG).show();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                UpdateTwitterButton();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void updateUI() {
        Toast.makeText(HashtagActivity.this, "You're logged in", Toast.LENGTH_LONG).show();
        //Sending user to new screen after successful login
        Intent mainActivity = new Intent(HashtagActivity.this, TrendsActivity.class);
        startActivity(mainActivity);
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mAuth.removeAuthStateListener(mAuthListener);
    }



    private void UpdateTwitterButton(){
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null){
            twitterLoginButton.setVisibility(View.VISIBLE);
        }
        else{
            //if user is already signed with twitter go directly to the trendingActivity class
            //to see the trending hashtags
            twitterLoginButton.setVisibility(View.GONE);
            Intent trendingActivity = new Intent(HashtagActivity.this, TrendsActivity.class);
            startActivity(trendingActivity);
        }
    }


    private void signInToFirebaseWithTwitterSession(TwitterSession session){
        AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    Toast.makeText(HashtagActivity.this, "Signed in firebase twitter successful", Toast.LENGTH_LONG).show();
                    if (!task.isSuccessful()){
                        Toast.makeText(HashtagActivity.this, "Auth firebase twitter failed", Toast.LENGTH_LONG).show();
                    }
                });
    }


}












