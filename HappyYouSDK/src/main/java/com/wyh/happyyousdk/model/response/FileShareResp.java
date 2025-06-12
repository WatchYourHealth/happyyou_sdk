package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.request.KLISubResponse;

import java.io.Serializable;
import java.util.ArrayList;

public class FileShareResp implements Serializable {
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isPoolingReq() {
        return isPoolingReq;
    }

    public void setPoolingReq(boolean poolingReq) {
        isPoolingReq = poolingReq;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public Object getFeedbackDetails() {
        return feedbackDetails;
    }

    public void setFeedbackDetails(Object feedbackDetails) {
        this.feedbackDetails = feedbackDetails;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public EnGTokens getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokens enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Rewards getRewards() {
        return rewards;
    }

    public void setRewards(Rewards rewards) {
        this.rewards = rewards;
    }

    public Object getUserkey() {
        return userkey;
    }

    public void setUserkey(Object userkey) {
        this.userkey = userkey;
    }

    public boolean isGoogleFit() {
        return isGoogleFit;
    }

    public void setGoogleFit(boolean googleFit) {
        isGoogleFit = googleFit;
    }

    public Object getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(Object userDetail) {
        this.userDetail = userDetail;
    }

    public Object getSpinTheWheelRewardsDetail() {
        return spinTheWheelRewardsDetail;
    }

    public void setSpinTheWheelRewardsDetail(Object spinTheWheelRewardsDetail) {
        this.spinTheWheelRewardsDetail = spinTheWheelRewardsDetail;
    }

    @SerializedName("msg")
    private String msg;

    @SerializedName("success")
    private boolean success;

    @SerializedName("isPoolingReq")
    private boolean isPoolingReq;

    @SerializedName("data")
    private ArrayList<Data> data;

    @SerializedName("feedbackDetails")
    private Object feedbackDetails;

    @SerializedName("freevoucher")
    private Object freevoucher;

    @SerializedName("enGTokens")
    private EnGTokens enGTokens;

    @SerializedName("rewards")
    private Rewards rewards;

    @SerializedName("userkey")
    private Object userkey;

    @SerializedName("isGoogleFit")
    private boolean isGoogleFit;

    @SerializedName("userDetail")
    private Object userDetail;

    @SerializedName("spinTheWheelRewardsDetail")
    private Object spinTheWheelRewardsDetail;
    public static class Data {
        @SerializedName("id")
        private  String id;
        @SerializedName("fileName")
        private  String fileName;
        @SerializedName("tileIcon")
        private  String tileIcon;
        @SerializedName("categoryName")
        private  String categoryName;
        @SerializedName("filePath")
        private  String filePath;
        @SerializedName("subCategoryName")
        private  String subCategoryName;
        @SerializedName("expiryDate")
        private  String expiryDate;
        @SerializedName("timeString")
        private  String timeString;
        @SerializedName("dateString")
        private  String dateString;
        @SerializedName("place")
        private  String place;
        @SerializedName("eventId")
        private  String eventId;
        @SerializedName("description1")
        private  String description1;
        @SerializedName("description2")
        private  String description2;
        @SerializedName("description3")
        private  String description3;
        @SerializedName("description4")
        private  String description4;

        public String getDescription1() {
            return description1;
        }

        public void setDescription1(String description1) {
            this.description1 = description1;
        }

        public String getDescription2() {
            return description2;
        }

        public void setDescription2(String description2) {
            this.description2 = description2;
        }

        public String getDescription3() {
            return description3;
        }

        public void setDescription3(String description3) {
            this.description3 = description3;
        }

        public String getDescription4() {
            return description4;
        }

        public void setDescription4(String description4) {
            this.description4 = description4;
        }




        public String getTimeString() {
            return timeString;
        }

        public void setTimeString(String timeString) {
            this.timeString = timeString;
        }

        public String getDateString() {
            return dateString;
        }

        public void setDateString(String dateString) {
            this.dateString = dateString;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public boolean isExpired() {
            return isExpired;
        }

        public void setExpired(boolean expired) {
            isExpired = expired;
        }




        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        @SerializedName("note")
        private  String note;

        public boolean getIsExpired() {
            return isExpired;
        }

        public void setIsExpired(boolean isExpired) {
            this.isExpired = isExpired;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getSubCategoryName() {
            return subCategoryName;
        }

        public void setSubCategoryName(String subCategoryName) {
            this.subCategoryName = subCategoryName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getTileIcon() {
            return tileIcon;
        }

        public void setTileIcon(String tileIcon) {
            this.tileIcon = tileIcon;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @SerializedName("isExpired")
        private  boolean isExpired;
        // Getters and Setters
    }
    public static class EnGTokens {
        @SerializedName("tokens")
        private Object tokens;

        public Object getTokens() {
            return tokens;
        }

        public void setTokens(Object tokens) {
            this.tokens = tokens;
        }

        public Object getBonusTokens() {
            return bonusTokens;
        }

        public void setBonusTokens(Object bonusTokens) {
            this.bonusTokens = bonusTokens;
        }

        @SerializedName("bonusTokens")
        private Object bonusTokens;

        // Getters and Setters
    }
    public static class Rewards {
        public Object getReward() {
            return reward;
        }

        public void setReward(Object reward) {
            this.reward = reward;
        }

        public Object getBonusRewards() {
            return bonusRewards;
        }

        public void setBonusRewards(Object bonusRewards) {
            this.bonusRewards = bonusRewards;
        }

        @SerializedName("reward")
        private Object reward;

        @SerializedName("bonusRewards")
        private Object bonusRewards;

        // Getters and Setters
    }
}
