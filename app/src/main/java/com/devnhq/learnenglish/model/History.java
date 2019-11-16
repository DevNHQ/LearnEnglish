package com.devnhq.learnenglish.model;

public class History {
    public String id;
    public String correctAnswer;
    public  String wrongAnswer;
    public  String point;
    public  String topic;
    public  String date;
    public  String url;

    public History(String id, String correctAnswer, String wrongAnswer, String point, String topic, String date, String url) {
        this.id = id;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer = wrongAnswer;
        this.point = point;
        this.topic = topic;
        this.date = date;
        this.url = url;
    }

    public History() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(String wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
