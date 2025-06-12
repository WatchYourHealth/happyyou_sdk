package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LockRewardResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("data")
    @Expose
    private LockRewardData data;


    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public LockRewardData getData() {
        return data;
    }

    public class LockRewardData {

        @SerializedName("quadrantPosition")
        @Expose
        private Integer quadrantPosition;


        @SerializedName("rewardType")
        @Expose
        private String rewardType;


        @SerializedName("rewardName")
        @Expose
        private String rewardName;

        @SerializedName("rewardHeader1")
        @Expose
        private String rewardHeader1;

        @SerializedName("rewardHeader2")
        @Expose
        private String rewardHeader2;


        @SerializedName("rewardDescription")
        @Expose
        private String rewardDescription;

        public Integer getQuadrantPosition() {
            return quadrantPosition;
        }

        public String getRewardType() {
            return rewardType;
        }

        public String getRewardName() {
            return rewardName;
        }

        public String getRewardDescription() {
            return rewardDescription;
        }

        public String getRewardHeader1() {
            return rewardHeader1;
        }

        public String getRewardHeader2() {
            return rewardHeader2;
        }
    }
}
