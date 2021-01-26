package com.example.smnaggregator;

import java.nio.charset.Charset;

public class ConstantsUtils {

    public static final String URL_ROOT_TWITTER_API = "https://api.twitter.com";
    public static final String URL_SEARCH = URL_ROOT_TWITTER_API + "/1.1/search/tweets.json?q=";
    public static final String URL_AUTHENTICATION = URL_ROOT_TWITTER_API + "/oauth2/token";

    public static final String URL_GREECE_TRENDING ="https://api.twitter.com/1.1/trends/place.json?id=23424833";

    public static final Charset UTF8  = Charset.forName("UTF-8");

}
