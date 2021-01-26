package com.example.smnaggregator;

import android.os.AsyncTask;
import android.os.Bundle;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static android.content.Intent.getIntent;
import static com.example.smnaggregator.BuildConfig.twitterAccessToken;
import static com.example.smnaggregator.BuildConfig.twitterAccessTokenSecret;
import static com.example.smnaggregator.BuildConfig.twitterkey;
import static com.example.smnaggregator.BuildConfig.twittersecret;


public class GetHashtags extends AsyncTask<String, Void, Void> {
    static final int count = 100;
    static long sinceId = 0;
    static long numberOfTweets = 0;
    private String hashTag;

    public GetHashtags(String hastTag) {
        this.hashTag = hashTag;
    }

    @Override
    protected Void doInBackground(String... strings) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(twitterkey)
                .setOAuthConsumerSecret(twittersecret)
                .setOAuthAccessToken(twitterAccessToken)
                .setOAuthAccessTokenSecret(twitterAccessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        //get latest tweets as of now
        //At this point store sinceId in database
        Query queryMax = new Query();
        queryMax.setCount(count);
        getTweets(queryMax, twitter, "maxId");
        queryMax = null;

        //get tweets that may have occurred while processing above data
        //Fetch sinceId from database and get tweets, also at this point store the sinceId
        do{
            Query querySince = new Query(hashTag);
            querySince.setCount(count);
            querySince.setSinceId(sinceId);
            getTweets(querySince, twitter, "sinceId");
            querySince = null;
        }while(checkIfSinceTweetsAreAvaliable(twitter,hashTag));
        return null;
    }

    private static boolean checkIfSinceTweetsAreAvaliable(Twitter twitter, String hashTag) {
        Query query = new Query(hashTag);
        query.setCount(count);
        query.setSinceId(sinceId);
        try {
            QueryResult result = twitter.search(query);
            if(result.getTweets()==null || result.getTweets().isEmpty()){
                query = null;
                return false;
            }
        } catch (TwitterException te) {
            System.out.println("Couldn't connect: " + te);
            System.exit(-1);
        }catch (Exception e) {
            System.out.println("Something went wrong: " + e);
            System.exit(-1);
        }
        return true;
    }

    private static void getTweets(Query query, Twitter twitter, String mode) {
        boolean getTweets=true;
        long maxId = 0;
        long whileCount=0;

        while (getTweets){
            try {
                QueryResult result = twitter.search(query);
                if(result.getTweets()==null || result.getTweets().isEmpty()){
                    getTweets=false;
                }else{
                    System.out.println("***********************************************");
                    System.out.println("Gathered " + result.getTweets().size() + " tweets");
                    int forCount=0;
                    for (twitter4j.Status status: result.getTweets()) {
                        if(whileCount == 0 && forCount == 0){
                            sinceId = status.getId();//Store sinceId in database
                            System.out.println("sinceId= "+sinceId);
                        }
                        System.out.println("Id= "+status.getId());
                        System.out.println("@" + status.getUser().getScreenName() + " : "+status.getUser().getName()+"--------"+status.getText());
                        if(forCount == result.getTweets().size()-1){
                            maxId = status.getId();
                            System.out.println("maxId= "+maxId);
                        }
                        System.out.println("");
                        forCount++;
                    }
                    numberOfTweets=numberOfTweets+result.getTweets().size();
                    query.setMaxId(maxId-1);
                }
            }catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
                System.exit(-1);
            }catch (Exception e) {
                System.out.println("Something went wrong: " + e);
                System.exit(-1);
            }
            whileCount++;
        }
        System.out.println("Total tweets count======="+numberOfTweets);
    }


}
