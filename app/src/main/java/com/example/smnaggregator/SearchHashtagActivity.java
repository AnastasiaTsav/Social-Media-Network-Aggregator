package com.example.smnaggregator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.common.util.Strings;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static com.example.smnaggregator.BuildConfig.twitterkey;
import static com.example.smnaggregator.BuildConfig.twittersecret;

public class SearchHashtagActivity extends AppCompatActivity {

    private static final String TAG = "SearchHashtagActivity";
    private static final String REMOTE_API ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_tweet);

        //receive the query from the searchIntent
        Bundle extras = getIntent().getExtras();
        String hashTag = extras.getString("hashtag");

        GetHashtags getHashtags = new GetHashtags(hashTag);
        getHashtags.execute();



    }

}