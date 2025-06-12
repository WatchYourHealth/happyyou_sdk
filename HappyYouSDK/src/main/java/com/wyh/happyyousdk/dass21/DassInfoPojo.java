package com.wyh.happyyousdk.dass21;

public class DassInfoPojo {
    private String  title;
    private String  normal;
    private String  mild;
    private String  moderate;
    private String  severe;
    private String  extremely;

    public DassInfoPojo(String title, String normal, String mild, String moderate, String severe, String extremely) {
        this.title = title;
        this.normal = normal;
        this.mild = mild;
        this.moderate = moderate;
        this.severe = severe;
        this.extremely = extremely;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getMild() {
        return mild;
    }

    public void setMild(String mild) {
        this.mild = mild;
    }

    public String getModerate() {
        return moderate;
    }

    public void setModerate(String moderate) {
        this.moderate = moderate;
    }

    public String getSevere() {
        return severe;
    }

    public void setSevere(String severe) {
        this.severe = severe;
    }

    public String getExtremely() {
        return extremely;
    }

    public void setExtremely(String extremely) {
        this.extremely = extremely;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
