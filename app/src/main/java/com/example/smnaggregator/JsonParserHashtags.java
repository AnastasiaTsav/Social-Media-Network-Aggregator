package com.example.smnaggregator;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JsonParserHashtags {
    public static final String TAG = "JsonParserHashtags";
    private static final String TEXT = "text";
    private static final String ENTITIES = "entities";

    public List<Hashtag> parseHashtagData(String hashtagJsonData) {

        List<Hashtag> hashtagList = new ArrayList<>();
        String url = "1";
        try {
            JSONObject root = new JSONObject(hashtagJsonData);
            JSONArray jsonHashtagArray = root.getJSONArray("statuses");

            for (int i = 0; i < jsonHashtagArray.length(); i++) {
                JSONObject tweetsObject = jsonHashtagArray.getJSONObject(i);

                String text = tweetsObject.getString(TEXT);

                String entities = tweetsObject.getString(ENTITIES);
                JSONObject entity = new JSONObject(entities);
                JSONArray urlsArray = entity.getJSONArray("urls");
                for (int j=0; j < urlsArray.length();j++){
                    JSONObject urlObj = urlsArray.getJSONObject(j);
                    url = urlObj.getString("expanded_url");
                }

                Hashtag hashtag = new Hashtag();
                hashtag.setText(text);
                hashtag.setUrl(url);
                hashtagList.add(hashtag);

            }

        } catch (JSONException ex) {
            Log.e(TAG, "Error in json parsing", ex);
        }

        return hashtagList;

    }

}
