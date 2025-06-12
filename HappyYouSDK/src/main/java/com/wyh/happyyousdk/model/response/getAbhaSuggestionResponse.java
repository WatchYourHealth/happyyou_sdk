package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class getAbhaSuggestionResponse {

    @SerializedName("msg")
    String msg;

    @SerializedName("success")
    boolean success;

    @SerializedName("isPoolingReq")
    boolean isPoolingReq;

    @SerializedName("data")
    Data data;

    @SerializedName("freevoucher")
    String freevoucher;

    @SerializedName("enGTokens")
    EnGTokens enGTokens;

    @SerializedName("rewards")
    Rewards rewards;

    @SerializedName("userkey")
    String userkey;

    @SerializedName("isGoogleFit")
    boolean isGoogleFit;


    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {
        return success;
    }

    public void setIsPoolingReq(boolean isPoolingReq) {
        this.isPoolingReq = isPoolingReq;
    }
    public boolean getIsPoolingReq() {
        return isPoolingReq;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

    public void setFreevoucher(String freevoucher) {
        this.freevoucher = freevoucher;
    }
    public String getFreevoucher() {
        return freevoucher;
    }

    public void setEnGTokens(EnGTokens enGTokens) {
        this.enGTokens = enGTokens;
    }
    public EnGTokens getEnGTokens() {
        return enGTokens;
    }

    public void setRewards(Rewards rewards) {
        this.rewards = rewards;
    }
    public Rewards getRewards() {
        return rewards;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }
    public String getUserkey() {
        return userkey;
    }

    public void setIsGoogleFit(boolean isGoogleFit) {
        this.isGoogleFit = isGoogleFit;
    }
    public boolean getIsGoogleFit() {
        return isGoogleFit;
    }
    public class Data {

        @SerializedName("txnId")
        String txnId;

        @SerializedName("abhaAddressList")
        List<String> abhaAddressList;


        public void setTxnId(String txnId) {
            this.txnId = txnId;
        }
        public String getTxnId() {
            return txnId;
        }

        public void setAbhaAddressList(List<String> abhaAddressList) {
            this.abhaAddressList = abhaAddressList;
        }
        public List<String> getAbhaAddressList() {
            return abhaAddressList;
        }

    }
    public class Rewards {

        @SerializedName("reward")
        String reward;

        @SerializedName("bonusRewards")
        String bonusRewards;


        public void setReward(String reward) {
            this.reward = reward;
        }
        public String getReward() {
            return reward;
        }

        public void setBonusRewards(String bonusRewards) {
            this.bonusRewards = bonusRewards;
        }
        public String getBonusRewards() {
            return bonusRewards;
        }

    }
    public class EnGTokens {

        @SerializedName("tokens")
        String tokens;

        @SerializedName("bonusTokens")
        String bonusTokens;


        public void setTokens(String tokens) {
            this.tokens = tokens;
        }
        public String getTokens() {
            return tokens;
        }

        public void setBonusTokens(String bonusTokens) {
            this.bonusTokens = bonusTokens;
        }
        public String getBonusTokens() {
            return bonusTokens;
        }

    }
}
