package com.devnhq.learnenglish.model;


public class Exam  {
    public String a;
    public String b;
    public String c;
    public String d;
    public String questions;
    public String result;
    public String explain ;
    public Exam() {
    }

    public Exam(String a, String b, String c, String d, String questions, String result, String explain) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.questions = questions;
        this.result = result;
        this.explain = explain;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
