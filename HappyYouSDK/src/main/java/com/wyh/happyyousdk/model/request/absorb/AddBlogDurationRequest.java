package com.wyh.happyyousdk.model.request.absorb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddBlogDurationRequest {
    @SerializedName("articleCode")
    @Expose
    private String articleCode;
    @SerializedName("readingmins")
    @Expose
    private int readingmins;
    @SerializedName("blogId")
    @Expose
    private int blogId;
    @SerializedName("userId")
    @Expose
    private String userId;

    public AddBlogDurationRequest(String articleCode, int readingmins, int blogId, String userId) {
        this.articleCode = articleCode;
        this.readingmins = readingmins;
        this.blogId = blogId;
        this.userId = userId;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public int getReadingmins() {
        return readingmins;
    }

    public void setReadingmins(int readingmins) {
        this.readingmins = readingmins;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
