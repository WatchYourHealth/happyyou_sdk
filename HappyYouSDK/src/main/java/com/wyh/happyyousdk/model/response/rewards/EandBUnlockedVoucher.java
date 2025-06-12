package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EandBUnlockedVoucher {
    @SerializedName("VendorName")
    @Expose
    private String vendorName;
    @SerializedName("VoucherTitle")
    @Expose
    private String voucherTitle;
    @SerializedName("Tokens")
    @Expose
    private Integer tokens;
    @SerializedName("VendorLogo")
    @Expose
    private String vendorLogo;

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVoucherTitle() {
        return voucherTitle;
    }

    public void setVoucherTitle(String voucherTitle) {
        this.voucherTitle = voucherTitle;
    }

    public Integer getTokens() {
        return tokens;
    }

    public void setTokens(Integer tokens) {
        this.tokens = tokens;
    }

    public String getVendorLogo() {
        return vendorLogo;
    }

    public void setVendorLogo(String vendorLogo) {
        this.vendorLogo = vendorLogo;
    }
}
