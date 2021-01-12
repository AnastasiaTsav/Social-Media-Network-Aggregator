package com.example.smnaggregator;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static android.content.Intent.ACTION_VIEW;
import static java.sql.DriverManager.println;

public class TrendsActivity extends AppCompatActivity {

    private static final String TAG = "RestAPI";
    private static final String REMOTE_API ="https://api.twitter.com/1.1/trends/place.json?id=23424833";

    private Button findTrendsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);
                Log.d(TAG,"onCreate: Starting download of trending hashtags");

                ListView postListView = findViewById(R.id.listview);

                PostArrayAdapter postArrayAdapter = new PostArrayAdapter(this,
                        R.layout.list_record,
                        new ArrayList<Post>(),
                        postListView);

                GetDataTask getDataTaskObject = new GetDataTask(postArrayAdapter);
                getDataTaskObject.execute(REMOTE_API);

                Log.d(TAG, "FINISHED downloading of trending hashtags");

                postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //ListView postview = (ListView) postListView.getSelectedView();
                        TextView hello = (TextView) postListView.findViewById(R.id.hahshtagURL);
                        //ListView p = (ListView) parent.getItemAtPosition(position);
                        //TextView hello = p.findViewById(R.id.hahshtagURL);
                        String hi = (String) hello.getText();
                        System.out.println(hi);


                       // Post hello = (Post) chosen.getItemAtPosition(position);
                        //String q = hello.getUrl();


                       //String tweetUrl ="https://api.twitter.com/1.1/search/tweets.json?"+ q;
                        try {
                            startActivity(new Intent(ACTION_VIEW, Uri.parse(hi)));
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }




                    }
                });
            }
        }