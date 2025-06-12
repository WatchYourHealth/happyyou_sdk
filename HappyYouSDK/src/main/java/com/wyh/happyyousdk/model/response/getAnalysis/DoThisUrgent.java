package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoThisUrgent {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cls")
    @Expose
    private String cls;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("activeOn")
    @Expose
    private Object activeOn;
    @SerializedName("blogURL")
    @Expose
    private String blogURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getActiveOn() {
        return activeOn;
    }

    public void setActiveOn(Object activeOn) {
        this.activeOn = activeOn;
    }

    public String getBlogURL() {
        return blogURL;
    }

    public void setBlogURL(String blogURL) {
        this.blogURL = blogURL;
    }

}
