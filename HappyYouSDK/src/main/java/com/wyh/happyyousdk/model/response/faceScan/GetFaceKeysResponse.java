package com.wyh.happyyousdk.model.response.faceScan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetFaceKeysResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("internalId")
    @Expose
    private String internalId;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("companyId")
    @Expose
    private String companyId;

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


    public String getId() {
        return id;
    }

    public String getInternalId() {
        return internalId;
    }

    public String getValue() {
        return value;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public String getActivationDateTime() {
        return activationDateTime;
    }

    public String getExpirationDateTime() {
        return expirationDateTime;
    }

    public String getAccessLimit() {
        return accessLimit;
    }
}
