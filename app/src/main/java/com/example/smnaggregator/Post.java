package com.example.smnaggregator;

public class Post {
    private String name;
    private String url;
    private String promotedContent;
    private String query;
    private String tweetVolume;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPromotedContent() {
        return promotedContent;
    }

    public void setPromotedContent(String promotedContent) {
        this.promotedContent = promotedContent;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    public String getTweetVolume() {
        return tweetVolume;
    }
    public void setTweetVolume(String tweetVolume) {
        this.tweetVolume = tweetVolume;
    }

    @Override
    public String toString() {
        return "Post{" +
                "name=" + name +
                ",  Url=" + url  + '\'' +
                ", Promoted Content=" + promotedContent + '\'' +
                ", Query=" + query + '\'' +
                ", Tweet Volume="+ tweetVolume + '\''+
                '}';
    }
}

