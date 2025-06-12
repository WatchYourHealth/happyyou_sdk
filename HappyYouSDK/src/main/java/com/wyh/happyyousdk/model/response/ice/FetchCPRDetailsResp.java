package com.wyh.happyyousdk.model.response.ice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchCPRDetailsResp {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

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

        @SerializedName("cprVideourl")
        @Expose
        private List<CprVideourl> cprVideourl = null;
        @SerializedName("cprTips")
        @Expose
        private List<String> cprTips;
        @SerializedName("iceBePrepareds")
        @Expose
        private List<IceBePrepared> iceBePrepared;

        public List<CprVideourl> getCprVideourl() {
            return cprVideourl;
        }

        public void setCprVideourl(List<CprVideourl> cprVideourl) {
            this.cprVideourl = cprVideourl;
        }

        public List<String> getCprTips() {
            return cprTips;
        }

        public void setCprTips(List<String> cprTips) {
            this.cprTips = cprTips;
        }

        public List<IceBePrepared> getIceBePrepared() {
            return iceBePrepared;
        }

        public void setIceBePrepared(List<IceBePrepared> iceBePrepared) {
            this.iceBePrepared = iceBePrepared;
        }
    }

    public class CprVideourl {

        @SerializedName("videoPath")
        @Expose
        private String videoPath;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("type")
        @Expose
        private String type;

        public String getVideoPath() {
            return videoPath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

    }

    public class IceBePrepared {

        @SerializedName("tipName")
        @Expose
        private String tipName;
        @SerializedName("tipDescription")
        @Expose
        private String tipDescription;
        @SerializedName("tipImagePath")
        @Expose
        private String tipImagePath;

        public String getTipName() {
            return tipName;
        }

        public void setTipName(String tipName) {
            this.tipName = tipName;
        }

        public String getTipDescription() {
            return tipDescription;
        }

        public void setTipDescription(String tipDescription) {
            this.tipDescription = tipDescription;
        }

        public String getTipImagePath() {
            return tipImagePath;
        }

        public void setTipImagePath(String tipImagePath) {
            this.tipImagePath = tipImagePath;
        }
    }
}
