package com.example.smnaggregator;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public static final String TAG = "JsonParser";

    private static final String NAME_LITERAL = "name";
    private static final String URL_LITERAL = "url";
    private static final String PROMOTED_CONTENT_LITERAL = "promoted_content";
    private static final String QUERY_LITERAL = "query";
    private static final String TWEET_VOLUME_LITERAL = "tweet_volume";

    public List<Post> parsePostData(String postJsonData) {

        List<Post> postList = new ArrayList<>();


        try {

            JSONArray jsonPostArray = new JSONArray(postJsonData);
            // get the first object of the root Array//
            JSONObject postJsonObject = jsonPostArray.getJSONObject(0);
            //get the trend property (Array) of the object//
            JSONArray trendsArray = new JSONArray(postJsonObject.getString("trends"));
            //loop the trends Array and get its properties//
            for (int i = 0; i < trendsArray.length(); i++) {
                JSONObject trendsObject = trendsArray.getJSONObject(i);

                String name = trendsObject.getString(NAME_LITERAL);
                String url = trendsObject.getString(URL_LITERAL);
                String promotedContent = trendsObject.getString(PROMOTED_CONTENT_LITERAL);
                String query = trendsObject.getString(QUERY_LITERAL);
                String tweetVolume = trendsObject.getString(TWEET_VOLUME_LITERAL);

                Post post = new Post();
                post.setUrl(url);
                post.setName(name);
                post.setPromotedContent(promotedContent);
                post.setQuery(query);
                post.setTweetVolume(tweetVolume);

                postList.add(post);



            }

        } catch (JSONException ex) {
            Log.e(TAG, "Error in json parsing", ex);
        }

        return postList;

    }
}
