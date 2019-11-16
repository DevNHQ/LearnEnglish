package com.devnhq.learnenglish.model;

import java.util.Comparator;

public class RateApp {
    public String id;
    public String name;
    public String url;
    public String comment;
    public String star;
    public String date;
    public String sortdate;
    public RateApp() {
    }
    public RateApp(String id, String name, String url, String comment, String star, String date, String sortdate) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.comment = comment;
        this.star = star;
        this.date = date;
        this.sortdate = sortdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getSortdate() {
        return sortdate;
    }

    public void setSortdate(String sortdate) {
        this.sortdate = sortdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public  static final Comparator<RateApp> BY_Rate_ALPHABETICAL = new Comparator<RateApp>() {
        @Override
        public int compare(RateApp o1, RateApp o2) {
            return (int) (Long.parseLong(o2.sortdate) - Long.parseLong(o1.sortdate));
        }
    };
}
