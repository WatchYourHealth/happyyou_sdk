package com.wyh.happyyousdk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FreeVoucher {
    @SerializedName("freebieID")
    @Expose
    private int freebieID;
    @SerializedName("voucherCode")
    @Expose
    private String voucherCode;
    @SerializedName("vendorName")
    @Expose
    private String vendorName;
    @SerializedName("vendorLogo")
    @Expose
    private String vendorLogo;
    @SerializedName("voucherTitle")
    @Expose
    private String voucherTitle;
    @SerializedName("voucherDescription")
    @Expose
    private String voucherDescription;
    @SerializedName("voucherValue")
    @Expose
    private String voucherValue;
    @SerializedName("isScratched")
    @Expose
    private boolean isScratched;

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorLogo() {
        return vendorLogo;
    }

    public void setVendorLogo(String vendorLogo) {
        this.vendorLogo = vendorLogo;
    }

    public String getVoucherTitle() {
        return voucherTitle;
    }

    public void setVoucherTitle(String voucherTitle) {
        this.voucherTitle = voucherTitle;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public void setVoucherDescription(String voucherDescription) {
        this.voucherDescription = voucherDescription;
    }

    public String getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(String voucherValue) {
        this.voucherValue = voucherValue;
    }

    public boolean isScratched() {
        return isScratched;
    }

    public void setScratched(boolean scratched) {
        isScratched = scratched;
    }

    public int getFreebieID() {
        return freebieID;
    }

    public void setFreebieID(int freebieID) {
        this.freebieID = freebieID;
    }
}
