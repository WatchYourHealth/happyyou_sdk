package com.wyh.happyyousdk.model.response.qc.allProducts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductImages {
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("small")
    @Expose
    private String small;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }
}
