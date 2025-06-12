package com.wyh.happyyousdk.model.response.faceScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceScanKeyData {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("internalId")
    @Expose
    private String internalId;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("companyId")
    @Expose
    private int companyId;
    @SerializedName("creationDateTime")
    @Expose
    private String creationDateTime;
    @SerializedName("activationDateTime")
    @Expose
    private String activationDateTime;
    @SerializedName("expirationDateTime")
    @Expose
    private String expirationDateTime;
    @SerializedName("accessLimit")
    @Expose
    private String accessLimit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getActivationDateTime() {
        return activationDateTime;
    }

    public void setActivationDateTime(String activationDateTime) {
        this.activationDateTime = activationDateTime;
    }

    public String getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(String expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public String getAccessLimit() {
        return accessLimit;
    }

    public void setAccessLimit(String accessLimit) {
        this.accessLimit = accessLimit;
    }
}
