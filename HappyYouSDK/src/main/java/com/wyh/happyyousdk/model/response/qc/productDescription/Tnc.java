package com.wyh.happyyousdk.model.response.qc.productDescription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tnc {
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("content")
    @Expose
    private String content;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
