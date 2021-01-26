package com.example.smnaggregator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    private static final String REMOTE_API = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_tweet);

        //receive the query from the searchIntent
        Bundle extras = getIntent().getExtras();
        String hashTag = extras.getString("hashtag");

        //  GetHashtags getHashtags = new GetHashtags(hashTag);
        // getHashtags.execute();

        Log.d(TAG, "onCreate: Starting download of trending hashtags");

        ListView postListView = findViewById(R.id.searchHashtagList);

        PostArrayAdapter postArrayAdapter = new PostArrayAdapter(this,
                R.layout.activity_chosen_tweet,
                new ArrayList<>(),
                postListView);

        String remoteUrl = "https://api.twitter.com/1.1/search/tweets.json?id=23424833&lang=el&q=%23" + hashTag + "&include_entities=true";
        GetDataTask getDataTaskObject = new GetDataTask(postArrayAdapter);
        getDataTaskObject.execute(remoteUrl);


    }

}