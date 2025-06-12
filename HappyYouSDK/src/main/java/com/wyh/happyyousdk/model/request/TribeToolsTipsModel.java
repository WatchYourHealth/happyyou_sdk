package com.wyh.happyyousdk.model.request;

public class TribeToolsTipsModel {
    String name;
    String pre;

    public TribeToolsTipsModel(String name, String pre) {
        this.name = name;
        this.pre = pre;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPre() {
        return pre;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }
}
