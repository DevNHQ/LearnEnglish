package com.devnhq.learnenglish.model;

public class Topic {
    public String topicsdetail;
    public String id;
    public String title;
    public String url;

    public Topic(String topicsdetail, String id, String title, String url) {
        this.topicsdetail = topicsdetail;
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public Topic() {
    }

    public String getTopicsdetail() {
        return topicsdetail;
    }

    public void setTopicsdetail(String topicsdetail) {
        this.topicsdetail = topicsdetail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
