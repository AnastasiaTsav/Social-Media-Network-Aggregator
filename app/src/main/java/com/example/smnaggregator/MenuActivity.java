package com.example.smnaggregator;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;

public class MenuActivity extends AppCompatActivity {
    private Button hashtagButton;
    private Button postButton;
    private Button storyButton;
    OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        hashtagButton=findViewById(R.id.hashtagButton);
        hashtagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    public void logout(View view){
        FirebaseAuth.getInstance().signOut(); //User Disconnection
        startActivity(new Intent(getApplicationContext(), Login_Activity.class));
        finish();
    }
}