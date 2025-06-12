package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddOnScratchCard {


    @SerializedName("vendorName")
    @Expose
    public String vendorName;

    @SerializedName("freebieID")
    @Expose
    private int freebieID;


    @SerializedName("vendorLogo")
    @Expose
    public String vendorLogo;


    @SerializedName("voucherTitle")
    @Expose
    public String voucherTitle;


    @SerializedName("voucherDescription")
    @Expose
    public String voucherDescription;


    @SerializedName("voucherCode")
    @Expose
    public String voucherCode;


    @SerializedName("voucherValue")
    @Expose
    public String voucherValue;

    @SerializedName("isScratched")
    @Expose
    public boolean isScratched;


    public String getVendorName() {
        return vendorName;
    }

    public String getVendorLogo() {
        return vendorLogo;
    }

    public String getVoucherTitle() {
        return voucherTitle;
    }

    public String getVoucherDescription() {
        return voucherDescription;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getVoucherValue() {
        return voucherValue;
    }

    public int getFreebieID() {
        return freebieID;
    }

    public void setFreebieID(int freebieID) {
        this.freebieID = freebieID;
    }

    public boolean isScratched() {
        return isScratched;
    }

    public void setScratched(boolean scratched) {
        isScratched = scratched;
    }
}
