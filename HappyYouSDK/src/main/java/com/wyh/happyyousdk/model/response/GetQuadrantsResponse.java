package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetQuadrantsResponse {

    @SerializedName("msg")
    @Expose
    public String msg;

    @SerializedName("success")
    @Expose
    public Boolean success;

    @SerializedName("data")
    @Expose
    public spinData data;

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public spinData getData() {
        return data;
    }


    public class spinData {

        @SerializedName("quadrants")
        @Expose
        public ArrayList<QuadrantData> quadrantData;


        @SerializedName("expiryInMinutes")
        @Expose
        public int expiryInMinutes;

        @SerializedName("description")
        @Expose
        public String description;


        public ArrayList<QuadrantData> getQuadrantData() {
            return quadrantData;
        }

        public int getExpiryInMinutes() {
            return expiryInMinutes;
        }

        public String getDescription() {
            return description;
        }
    }

    public class QuadrantData implements Serializable {
        @SerializedName("quadrantPosition")
        @Expose
        public String quadrantPosition;

        @SerializedName("rewardIcon")
        @Expose
        public String rewardIcon;

        @SerializedName("rewardName")
        @Expose
        public String rewardName;

        @SerializedName("quadrantColor")
        @Expose
        public String quadrantColor;

        @SerializedName("rewardType")
        @Expose
        public String rewardType;


        public String getQuadrantPosition() {
            return quadrantPosition;
        }

        public String getRewardIcon() {
            return rewardIcon;
        }

        public String getRewardName() {
            return rewardName;
        }

        public String getQuadrantColor() {
            return quadrantColor;
        }

        public String getRewardType() {
            return rewardType;
        }
    }



}




