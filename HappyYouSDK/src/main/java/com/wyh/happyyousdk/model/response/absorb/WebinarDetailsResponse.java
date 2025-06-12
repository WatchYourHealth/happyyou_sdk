package com.wyh.happyyousdk.model.response.absorb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WebinarDetailsResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

    public WebinarDetailsResponse(String msg, boolean success, Data data) {
        this.msg = msg;
        this.success = success;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("imgPath")
        @Expose
        private String imgPath;
        @SerializedName("heldOn")
        @Expose
        private String heldOn;
        @SerializedName("description")
        @Expose
        private String description;

        public Data(String title, String link, String imgPath, String heldOn, String description) {
            this.title = title;
            this.link = link;
            this.imgPath = imgPath;
            this.heldOn = heldOn;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getHeldOn() {
            return heldOn;
        }

        public void setHeldOn(String heldOn) {
            this.heldOn = heldOn;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }


}