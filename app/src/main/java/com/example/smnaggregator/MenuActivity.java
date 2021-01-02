package com.example.smnaggregator;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";

    private Button hashtagButton;
    private Button postButton;
    private Button storyButton;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        hashtagButton=findViewById(R.id.hashtagButton);
        hashtagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HashtagActivity.class));
                Log.d(TAG, "Button presses successfully");
            }
        });

    }


    public void logout(View view){
        FirebaseAuth.getInstance().signOut(); //User Disconnection
        startActivity(new Intent(getApplicationContext(), Login_Activity.class));
        finish();
    }
}