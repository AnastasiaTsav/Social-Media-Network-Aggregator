package com.example.smnaggregator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.util.Strings;

import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static android.content.Intent.ACTION_VIEW;
import static com.example.smnaggregator.BuildConfig.twitterkey;
import static com.example.smnaggregator.BuildConfig.twittersecret;

public class SearchHashtagActivity extends AppCompatActivity {

    private static final String TAG = "SearchHashtagActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_tweet);

        //receive the query from the searchIntent
        Bundle extras = getIntent().getExtras();
        String hashTag = extras.getString("hashtag");

        Log.d(TAG, "onCreate: Starting download of trending hashtags");

        ListView hashtagListView = findViewById(R.id.searchHashtagList);

        HashtagArrayAdapter hashtagArrayAdapter = new HashtagArrayAdapter(this,
                R.layout.list_record2,
                new ArrayList<>(),
                hashtagListView);

        String remoteUrl = "https://api.twitter.com/1.1/search/tweets.json?id=23424833&lang=el&q=%23" + hashTag + "&include_entities=true";
       // GetDataTask getDataTaskObject = new GetDataTask(postArrayAdapter);
      //  getDataTaskObject.execute(remoteUrl);

        GetHashtags getHashtags = new GetHashtags(hashtagArrayAdapter);
        getHashtags.execute(remoteUrl);
        /*
        hashtagListView.setOnItemClickListener((parent, view, position, id) -> {
            TextView clickedView = view.findViewById(R.id.hahshtagURL);
            String clickedUrl = (String) clickedView.getText();
            try {
                startActivity(new Intent(ACTION_VIEW, Uri.parse(clickedUrl)));
                Log.d(TAG,"Connection with the twitter app succeed");
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG,"Connection with twitter App failed");
            }
        });*/

        Log.d(TAG,"Started Async Request Execution for web service data");


    }

}