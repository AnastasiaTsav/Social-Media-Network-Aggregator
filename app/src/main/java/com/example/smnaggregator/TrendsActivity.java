package com.example.smnaggregator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static android.content.Intent.ACTION_VIEW;

public class TrendsActivity extends AppCompatActivity {

    private static final String TAG = "RestAPI";
    private static final String REMOTE_API ="https://api.twitter.com/1.1/trends/place.json?id=23424833";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);
                Log.d(TAG,"onCreate: Starting download of trending hashtags");

                ListView postListView = findViewById(R.id.listview);

                PostArrayAdapter postArrayAdapter = new PostArrayAdapter(this,
                        R.layout.list_record,
                        new ArrayList<>(),
                        postListView);

                GetDataTask getDataTaskObject = new GetDataTask(postArrayAdapter);
                getDataTaskObject.execute(REMOTE_API);

                Log.d(TAG, "FINISHED downloading of trending hashtags");

                postListView.setOnItemClickListener((parent, view, position, id) -> {
                    //get from the list item the field with the url and
                    //and get the text from this to search the tweets for a specific
                    //hashtag within the twitterApp
                    TextView clickedView = view.findViewById(R.id.hahshtagURL);
                    String clickedUrl = (String) clickedView.getText();


                    try {
                        startActivity(new Intent(ACTION_VIEW, Uri.parse(clickedUrl)));
                        Log.d(TAG,"Connection with the twitter app succeed");
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Log.e(TAG,"Connection with twitter App failed");
                    }




                });
            }
        }