package com.devnhq.learnenglish.model;

import java.io.Serializable;
import java.util.Comparator;

public class User implements Serializable {
    public  String id;
    public  String email;
    public String name;
    public String url;
    public String totalcount;
    public String totalscore;
    public String rateapp;
    public String password;

    public User(String id, String email, String name, String url, String totalcount, String totalscore, String rateapp, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.url = url;
        this.totalcount = totalcount;
        this.totalscore = totalscore;
        this.rateapp = rateapp;
        this.password = password;
    }

    public String getRateapp() {
        return rateapp;
    }

    public void setRateapp(String rateapp) {
        this.rateapp = rateapp;
    }

    public User() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(String totalcount) {
        this.totalcount = totalcount;
    }

    public String getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(String totalscore) {
        this.totalscore = totalscore;
    }
    public  static final  Comparator<User> BY_NAME_ALPHABETICAL = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            return (int) ((Double.parseDouble(o2.totalscore) *10) - (Double.parseDouble(o1.totalscore)*10));
        }
    };

}
