package com.example.smnaggregator;

public class Hashtag {
    private String id;
    private String text;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        return "Hashtag{" +
                " Id=" + id +
                ", text='" + text + '\'' +
                "url"+ url+
                '}';
    }

}





