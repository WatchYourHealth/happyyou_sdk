package com.wyh.happyyousdk.model;

public class InfoConcernPojo {
    private String  title;
    private String blue;
    private String orange;
    private String pink;

    public InfoConcernPojo(String title, String blue, String orange, String pink) {
        this.title = title;
        this.blue = blue;
        this.orange = orange;
        this.pink = pink;
    }

    public String getBlue() {
        return blue;
    }

    public void setBlue(String blue) {
        this.blue = blue;
    }

    public String getOrange() {
        return orange;
    }

    public void setOrange(String orange) {
        this.orange = orange;
    }

    public String getPink() {
        return pink;
    }

    public void setPink(String pink) {
        this.pink = pink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
