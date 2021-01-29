package com.example.smnaggregator;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";

    private Button hashtagButton;
    private Button postButton;
    private Button storyButton;
    private SearchView searchBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth = FirebaseAuth.getInstance();

        hashtagButton=findViewById(R.id.hashtagButton);
        hashtagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HashtagActivity.class));
                Log.d(TAG, "HashTag button presses successfully");
            }
        });

        postButton = findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MakePostActivity.class));
                Log.d(TAG, "Post button presses successfully");
            }
        });

        storyButton = findViewById(R.id.storyButton);
        storyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MakeStoryActivity.class));
                Log.d(TAG,"Story button presses successfully");
            }
        });

        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //send the query in the search activity
               Intent searchIntent = new Intent(MenuActivity.this,SearchHashtagActivity.class);
               searchIntent.putExtra("hashtag", query);
               startActivity(searchIntent);

               return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut(); //User Disconnection
        startActivity(new Intent(getApplicationContext(), Login_Activity.class));
        finish();
    }
}