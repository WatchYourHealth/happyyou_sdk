package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnGTokensModel {

    @SerializedName("tokens")
    @Expose
    private String tokens;
    @SerializedName("bonusTokens")
    @Expose
    private String bonusTokens;

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }

    public String getBonusTokens() {
        return bonusTokens;
    }

    public void setBonusTokens(String bonusTokens) {
        this.bonusTokens = bonusTokens;
    }
}
