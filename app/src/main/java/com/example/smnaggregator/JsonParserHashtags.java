package com.example.smnaggregator;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
            JSONObject root = new JSONObject(hashtagJsonData);
            JSONArray jsonHashtagArray = root.getJSONArray("statuses");


            for (int i = 0; i < jsonHashtagArray.length(); i++) {
                JSONObject tweetsObject = jsonHashtagArray.getJSONObject(i);

                String createdAt = tweetsObject.getString(CREATED_AT);
                String id = tweetsObject.getString(ID);
                String id_str = tweetsObject.getString(ID_STR);
                String text = tweetsObject.getString(TEXT);
                String truncated = tweetsObject.getString(TRUNCATED);
                String entities = tweetsObject.getString(ENTITIES);


                Hashtag hashtag = new Hashtag();
                //hashtag.setCreatedAt(createdAt);
                hashtag.setText(text);

                hashtagList.add(hashtag);



            }

        } catch (JSONException ex) {
            Log.e(TAG, "Error in json parsing", ex);
        }

        return hashtagList;

    }

}
