package com.example.smnaggregator;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.example.smnaggregator.BuildConfig.twitterkey;
import static com.example.smnaggregator.BuildConfig.twittersecret;


public class GetHashtags extends AsyncTask<String, Void, List<Hashtag>> {
    private String hashTag;
    public static final String TAG = "SpecificHashtagSearch";

    public List<Hashtag> hashtagList;
    private HashtagArrayAdapter adapter;
    public GetHashtags(HashtagArrayAdapter adapter) {
        this.adapter=adapter;
    }

    public String downloadRestData(String remoteUrl) {

        Log.d(TAG, "Downloading data....");
        StringBuilder sb = new StringBuilder();
        HttpURLConnection httpConnection = null;
        BufferedReader bufferedReader = null;
        StringBuilder response = new StringBuilder();


        try {
            URL url = new URL(remoteUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");

            String jsonString = appAuthentication();
            JSONObject jsonObjectDocument = new JSONObject(jsonString);
            String token = jsonObjectDocument.getString("token_type") + " "
                    + jsonObjectDocument.getString("access_token");
            httpConnection.setRequestProperty("Authorization", token);
            httpConnection.setRequestProperty("Content-Type",
                    "application/json; charset = UTF-8");
            httpConnection.connect();

            bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

            String line=bufferedReader.readLine();
            while (line != null){
                response.append(line).append("\n");
                line=bufferedReader.readLine();
            }
            bufferedReader.close();
            Log.d(TAG,
                    "GET response code: "
                            + String.valueOf(httpConnection
                            .getResponseCode()));
            Log.d(TAG, "JSON response: " + response.toString());

        } catch (Exception e) {
            Log.e(TAG, "GET error: " + Log.getStackTraceString(e));

        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();

            }
        }
        return response.toString();
    }


    @Override
    protected List<Hashtag> doInBackground(String... strings) {
        String url = strings[0];
        Log.d(TAG, "Doing task in background for url "+url);

        String hashtagJson = downloadRestData(url);

        JsonParserHashtags jsonParser = new JsonParserHashtags();
        return jsonParser.parseHashtagData(hashtagJson);
    }

    @Override
    protected void onPostExecute(List<Hashtag> hashtags) {
        hashtagList =  hashtags;
        Log.d(TAG, "Just got results!");

        for(Hashtag hashtag : hashtagList) {
            Log.d(TAG, hashtag.toString());
        }

        adapter.setHashtagList(hashtagList);

    }

    public List<Hashtag> getHashtagList() {
        return hashtagList;
    }

    public static String appAuthentication() {

        HttpURLConnection httpConnection = null;
        OutputStream outputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder response = null;

        try {
            URL url = new URL(ConstantsUtils.URL_AUTHENTICATION);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);

            String accessCredential = twitterkey + ":"
                    + twittersecret;
            String authorization = "Basic "
                    + Base64.encodeToString(accessCredential.getBytes(),
                    Base64.NO_WRAP);
            String param = "grant_type=client_credentials";

            httpConnection.addRequestProperty("Authorization", authorization);
            httpConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            httpConnection.connect();

            outputStream = httpConnection.getOutputStream();
            outputStream.write(param.getBytes());
            outputStream.flush();
            outputStream.close();

            bufferedReader = new BufferedReader(new InputStreamReader(
                    httpConnection.getInputStream()));
            String line;
            response = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            Log.d(TAG,
                    "POST response code: "
                            + String.valueOf(httpConnection.getResponseCode()));
            Log.d(TAG, "JSON response: " + response.toString());

        } catch (Exception e) {
            Log.e(TAG, "POST error: " + Log.getStackTraceString(e));

        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return response.toString();
    }

}

