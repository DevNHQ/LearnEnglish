package com.devnhq.learnenglish.model;

public class TotalPoint {
    public  Double point;
    public  int count;

    public TotalPoint(Double point, int count) {
        this.point = point;
        this.count = count;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
