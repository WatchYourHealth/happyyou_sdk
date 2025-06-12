package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllocateVoucherData {
    @SerializedName("voucherLogo")
    @Expose
    private String voucherLogo;
    @SerializedName("voucherName")
    @Expose
    private String voucherName;
    @SerializedName("voucherCode")
    @Expose
    private String voucherCode;
    @SerializedName("voucherDesc")
    @Expose
    private String voucherDesc;
    @SerializedName("voucherValue")
    @Expose
    private String voucherValue;

    public String getVoucherLogo() {
        return voucherLogo;
    }

    public void setVoucherLogo(String voucherLogo) {
        this.voucherLogo = voucherLogo;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherDesc() {
        return voucherDesc;
    }

    public void setVoucherDesc(String voucherDesc) {
        this.voucherDesc = voucherDesc;
    }

    public String getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(String voucherValue) {
        this.voucherValue = voucherValue;
    }
}
