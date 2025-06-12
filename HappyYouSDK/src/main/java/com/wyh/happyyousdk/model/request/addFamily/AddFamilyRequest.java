package com.wyh.happyyousdk.model.request.addFamily;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddFamilyRequest {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("relationship")
    @Expose
    private String relationship;
    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("modifiedOn")
    @Expose
    private String modifiedOn;
    @SerializedName("modifiedBy")
    @Expose
    private String modifiedBy;


    public AddFamilyRequest(int id, String name, String relationship, String mobileNo) {
        this.id = id;
        this.name = name;
        this.relationship = relationship;
        this.mobileNo = mobileNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
