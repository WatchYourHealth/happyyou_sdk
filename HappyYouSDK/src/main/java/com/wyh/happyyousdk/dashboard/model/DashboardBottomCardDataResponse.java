package com.wyh.happyyousdk.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.RewardsModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardBottomCardDataResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private CardData data;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("rewards")
    @Expose
    private RewardsModel rewards;

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

    public CardData getData() {
        return data;
    }

    public void setData(CardData data) {
        this.data = data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }

    public RewardsModel getRewards() {
        return rewards;
    }

    public void setRewards(RewardsModel rewards) {
        this.rewards = rewards;
    }

    public static class Datum {

        @SerializedName("TileId")
        @Expose
        private Integer tileId;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Description")
        @Expose
        private String description;
        @SerializedName("IsPinned")
        @Expose
        private Boolean isPinned;
        @SerializedName("ImagePath")
        @Expose
        private Object imagePath;
        @SerializedName("CreatedOn")
        @Expose
        private String createdOn;

        public Datum(Integer tileId, String name, String description, Boolean isPinned, Object imagePath, String createdOn) {
            this.tileId = tileId;
            this.name = name;
            this.description = description;
            this.isPinned = isPinned;
            this.imagePath = imagePath;
            this.createdOn = createdOn;
        }

        public Integer getTileId() {
            return tileId;
        }

        public void setTileId(Integer tileId) {
            this.tileId = tileId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean getIsPinned() {
            return isPinned;
        }

        public void setIsPinned(Boolean isPinned) {
            this.isPinned = isPinned;
        }

        public Object getImagePath() {
            return imagePath;
        }

        public void setImagePath(Object imagePath) {
            this.imagePath = imagePath;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

    }

    public static class CardData{
        @SerializedName("tilesData")
        @Expose
        private ArrayList<tilesData> tilesData;

        @SerializedName("showCard")
        @Expose
        private Boolean showCard;

        @SerializedName("referedBy")
        @Expose
        private String referedBy;
        @SerializedName("isReferrer")
        @Expose
        private Boolean isReferrer;
        @SerializedName("referralCount")
        @Expose
        private int referralCount;


        public Boolean getReferrer() {
            return isReferrer;
        }

        public void setReferrer(Boolean referrer) {
            isReferrer = referrer;
        }

        public int getReferralCount() {
            return referralCount;
        }

        public void setReferralCount(int referralCount) {
            this.referralCount = referralCount;
        }

        public ArrayList<DashboardBottomCardDataResponse.tilesData> getTilesData() {
            return tilesData;
        }

        public Boolean getShowCard() {
            return showCard;
        }

        public String getReferedBy() {
            return referedBy;
        }
    }


    public static class tilesData{

        @SerializedName("TileId")
        @Expose
        private Integer tileId;

        @SerializedName("Name")
        @Expose
        private String name;

        @SerializedName("Description")
        @Expose
        private String description;

        @SerializedName("IsPinned")
        @Expose
        private Boolean isPinned;

        @SerializedName("ImagePath")
        @Expose
        private String imagePath;

        @SerializedName("CreatedOn")
        @Expose
        private String createdOn;

        private String mobileNumber;
        private boolean isKgiItem = false;
        private int iconImage;


        public tilesData(Integer tileId, String name, String description, Boolean isPinned, String imagePath, String createdOn, boolean isKgi, int iconImage) {
            this.tileId = tileId;
            this.name = name;
            this.description = description;
            this.isPinned = isPinned;
            this.imagePath = imagePath;
            this.createdOn = createdOn;
            this.isKgiItem = isKgi;
            this.iconImage = iconImage;
        }
        public tilesData(Integer tileId, String name, String description, Boolean isPinned, String imagePath, String createdOn, String mobileNumber) {
            this.tileId = tileId;
            this.name = name;
            this.description = description;
            this.isPinned = isPinned;
            this.imagePath = imagePath;
            this.createdOn = createdOn;
            this.mobileNumber = mobileNumber;
        }

        public tilesData(Integer tileId, String name, String description, Boolean isPinned, String imagePath, String createdOn) {
            this.tileId = tileId;
            this.name = name;
            this.description = description;
            this.isPinned = isPinned;
            this.imagePath = imagePath;
            this.createdOn = createdOn;
        }

        public Integer getTileId() {
            return tileId;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Boolean getIsPinned() {
            return isPinned;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getCreatedOn() {
            return createdOn;
        }


        public void setTileId(Integer tileId) {
            this.tileId = tileId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setPinned(Boolean pinned) {
            isPinned = pinned;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public Boolean getPinned() {
            return isPinned;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public boolean isKgiItem() {
            return isKgiItem;
        }

        public void setKgiItem(boolean kgiItem) {
            isKgiItem = kgiItem;
        }

        public int getIconImage() {
            return iconImage;
        }

        public void setIconImage(int iconImage) {
            this.iconImage = iconImage;
        }
    }

}
