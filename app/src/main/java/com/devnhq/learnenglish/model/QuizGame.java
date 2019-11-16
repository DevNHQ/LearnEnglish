package com.devnhq.learnenglish.model;

public class QuizGame {
    public  String url;
    public  String title;
    public  String sound;

    public QuizGame(String url, String title, String sound) {
        this.url = url;
        this.title = title;
        this.sound = sound;
    }

    public QuizGame() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
