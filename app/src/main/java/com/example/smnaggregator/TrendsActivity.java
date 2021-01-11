package com.example.smnaggregator;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TrendsActivity extends AppCompatActivity {

    private static final String TAG = "RestAPI";
    private static final String REMOTE_API ="https://api.twitter.com/1.1/trends/place.json?id=23424833";
    private Button findTrendsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);
        /*
        findTrendsBtn=findViewById(R.id.findTrendsButton);
        findTrendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {*/
                Log.d(TAG,"onCreate: Starting download of trending hashtags");

                ListView postListView = findViewById(R.id.trendsListView);

                PostArrayAdapter postArrayAdapter = new PostArrayAdapter(this,
                        R.layout.list_record,
                        new ArrayList<Post>(),
                        postListView);

                GetDataTask getDataTaskObject = new GetDataTask();
                getDataTaskObject.execute(REMOTE_API);

                Log.d(TAG, "FINISHED downloading of trending hashtags");
            }
        }/*)*/;






  //  }
//}