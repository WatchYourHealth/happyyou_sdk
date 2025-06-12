package com.wyh.happyyousdk.trends;

import java.util.List;

public class MarkerDataClass {
    private List<String> userName;
    private String name;
    private float[] points;
    private float point;

    public MarkerDataClass() {
    }

    public MarkerDataClass(List<String> userName, float[] points) {
        this.userName = userName;
        this.points = points;
    }

    public MarkerDataClass(String name, float point) {
        this.name = name;
        this.point = point;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public List<String> getUserName() {
        return userName;
    }

    public void setUserName(List<String> userName) {
        this.userName = userName;
    }

    public float[] getPoints() {
        return points;
    }

    public void setPoints(float[] points) {
        this.points = points;
    }
}
