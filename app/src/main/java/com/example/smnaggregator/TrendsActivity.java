package com.example.smnaggregator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TrendsActivity extends AppCompatActivity {

    private static final String TAG = "RestAPI";
    private static final String REMOTE_API ="https://api.twitter.com/1.1/trends/place.json?id=23424833";
    private Button findTrendsBtn;

    //  private static final String REMOTE_API="https://jsonplaceholder.typicode.com/posts/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);
        findTrendsBtn=findViewById(R.id.findTrendsButton);
        findTrendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onCreate: Starting download of trending hashtags");

                GetDataTask getDataTaskObject = new GetDataTask();

                getDataTaskObject.execute(REMOTE_API);

                Log.d(TAG, "FINISHED downloading of trending hashtags");
            }
        });






    }
}