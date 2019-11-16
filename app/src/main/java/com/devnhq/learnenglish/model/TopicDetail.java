package com.devnhq.learnenglish.model;

public class TopicDetail {
    public String id;
    public String example;
    public String clause;
    public String sound;
    public String title;
    public String translate;
    public String url;

    public TopicDetail() {
    }

    public TopicDetail(String id, String example, String clause, String sound, String title, String translate, String url) {
        this.id = id;
        this.example = example;
        this.clause = clause;
        this.sound = sound;
        this.title = title;
        this.translate = translate;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getClause() {
        return clause;
    }

    public void setClause(String clause) {
        this.clause = clause;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
