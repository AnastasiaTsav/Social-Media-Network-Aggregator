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

public class GetDataTask extends AsyncTask<String, Void, List<Post>> {

    public static final String TAG = "GetDataTask";
    public static final String TAG2 = "TwitterUtils";

    public List<Post> postList;

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
            Log.d(TAG2,
                    "GET response code: "
                            + String.valueOf(httpConnection
                            .getResponseCode()));
            Log.d(TAG2, "JSON response: " + response.toString());

        } catch (Exception e) {
            Log.e(TAG2, "GET error: " + Log.getStackTraceString(e));

        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();

            }
        }
        return response.toString();
    }


    @Override
    protected List<Post> doInBackground(String... strings) {
        String url = strings[0];
        Log.d(TAG,"Doing task in background for url " +url);

        String postJson = downloadRestData(url);

        JsonParser jsonParser = new JsonParser();
        return jsonParser.parsePostData(postJson);
    }

    @Override
    protected void onPostExecute(List<Post> posts) {
        postList = posts;
        Log.d(TAG,"Just got results");

        for(Post post : postList){
            Log.i(TAG, post.toString());
        }
    }

    public List<Post> getPostList() {
        return postList;
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

            String accessCredential = ConstantsUtils.CONSUMER_KEY + ":"
                    + ConstantsUtils.CONSUMER_SECRET;
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
            // int statusCode = httpConnection.getResponseCode();
            // String reason =httpConnection.getResponseMessage();

            bufferedReader = new BufferedReader(new InputStreamReader(
                    httpConnection.getInputStream()));
            String line;
            response = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            Log.d(TAG2,
                    "POST response code: "
                            + String.valueOf(httpConnection.getResponseCode()));
            Log.d(TAG2, "JSON response: " + response.toString());

        } catch (Exception e) {
            Log.e(TAG2, "POST error: " + Log.getStackTraceString(e));

        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        return response.toString();
    }


}
