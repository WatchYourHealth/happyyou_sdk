package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EandBAvailableVoucher {
    @SerializedName("VendorName")
    @Expose
    private String vendorName;
    @SerializedName("VoucherTitle")
    @Expose
    private String voucherTitle;
    @SerializedName("Tokens")
    @Expose
    private Integer tokens;
    @SerializedName("VoucherId")
    @Expose
    private Integer voucherId;
    @SerializedName("VoucherValue")
    @Expose
    private Integer voucherValue;
    @SerializedName("VendorLogo")
    @Expose
    private String vendorLogo;
    @SerializedName("VoucherDescription")
    @Expose
    private String voucherDescription;

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

    public Integer getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(Integer voucherValue) {
        this.voucherValue = voucherValue;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public void setVoucherDescription(String voucherDescription) {
        this.voucherDescription = voucherDescription;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }
}
