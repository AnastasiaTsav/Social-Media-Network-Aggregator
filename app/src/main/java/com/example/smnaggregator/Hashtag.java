package com.example.smnaggregator;

public class Hashtag {
    private String createdAt;
    private String id;
    private String id_str;
    private String text;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
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
                "createdAt=" + createdAt +
                ", Id=" + id +
                ", id_str='" + id_str + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

}





