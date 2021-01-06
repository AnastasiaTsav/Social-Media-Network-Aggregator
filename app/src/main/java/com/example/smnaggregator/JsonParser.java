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
    private static final String PROMOTED_CONTENT_LITERAL = "pc";
    private static final String QUERY_LITERAL = "query";
    private static final String TWEET_VOLUME_LITERAL = "tv";

    public List<Post> parsePostData(String postJsonData) {
        List<Post> postList = new ArrayList<>();

        try {
            JSONArray jsonPostArray = new JSONArray(postJsonData);

            for (int i = 0; i < jsonPostArray.length(); i++) {
                JSONObject postJsonObject = jsonPostArray.getJSONObject(i);
                String name = postJsonObject.getString(NAME_LITERAL);
                String url = postJsonObject.getString(URL_LITERAL);
                String promotedContent = postJsonObject.getString(PROMOTED_CONTENT_LITERAL);
                String query = postJsonObject.getString(QUERY_LITERAL);
                String tweetVolume = postJsonObject.getString(TWEET_VOLUME_LITERAL);

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
