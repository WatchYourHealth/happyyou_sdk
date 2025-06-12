package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardsCollectible {

    @SerializedName("VendorLogo")
    @Expose
    private String vendorLogo;
    @SerializedName("VendorName")
    @Expose
    private String vendorName;
    @SerializedName("VoucherCode")
    @Expose
    private String voucherCode;
    @SerializedName("VoucherDescription")
    @Expose
    private String voucherDescription;
    @SerializedName("VoucherTitle")
    @Expose
    private String voucherTitle;
    @SerializedName("VoucherValue")
    @Expose
    private Integer voucherValue;


    private  boolean isscrached =false;
    private  int id =0;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIsscrached() {
        return isscrached;
    }

    public void setIsscrached(boolean isscrached) {
        this.isscrached = isscrached;
    }

    public String getVendorLogo() {
        return vendorLogo;
    }

    public void setVendorLogo(String vendorLogo) {
        this.vendorLogo = vendorLogo;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public void setVoucherDescription(String voucherDescription) {
        this.voucherDescription = voucherDescription;
    }

    public String getVoucherTitle() {
        return voucherTitle;
    }

    public void setVoucherTitle(String voucherTitle) {
        this.voucherTitle = voucherTitle;
    }

    public Integer getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(Integer voucherValue) {
        this.voucherValue = voucherValue;
    }
}
