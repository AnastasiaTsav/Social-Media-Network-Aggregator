package com.example.smnaggregator;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParserHashtags {
    public static final String TAG = "JsonParserHashtags";

    private static final String CREATED_AT = "created_at";
    private static final String ID = "id";
    private static final String ID_STR = "id_str";
    private static final String TEXT = "text";
    private static final String TRUNCATED = "truncated";
    private static final String ENTITIES = "entities";

    public List<Hashtag> parseHashtagData(String hashtagJsonData) {

        List<Hashtag> hashtagList = new ArrayList<>();

        try {
            JSONArray jsonHashtagArray = new JSONArray(hashtagJsonData);
            // get the first object of the root Array//
            JSONObject hashtagJsonObject = jsonHashtagArray.getJSONObject(0);
            //get the trend property (Array) of the object//
            JSONArray tweetsArray = new JSONArray(hashtagJsonObject.getString("statuses"));
            //loop the trends Array and get its properties//
            for (int i = 0; i < tweetsArray.length(); i++) {
                JSONObject trendsObject = tweetsArray.getJSONObject(i);

                String createdAt = hashtagJsonObject.getString(CREATED_AT);
                String id = hashtagJsonObject.getString(ID);
                String id_str = hashtagJsonObject.getString(ID_STR);
                String text = hashtagJsonObject.getString(TEXT);
                String truncated = hashtagJsonObject.getString(TRUNCATED);
                String entities = hashtagJsonObject.getString(ENTITIES);


                Hashtag hashtag = new Hashtag();
                hashtag.setCreatedAt(createdAt);
                hashtag.setText(text);

                hashtagList.add(hashtag);



            }

        } catch (JSONException ex) {
            Log.e(TAG, "Error in json parsing", ex);
        }

        return hashtagList;

    }

}
