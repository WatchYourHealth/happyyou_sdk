package com.wyh.happyyousdk.model.request.ice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddEmergencyDetailsReq {


    @SerializedName("primaryContactDetails")
    @Expose
    private PrimaryContactDetails primaryContactDetails;
    @SerializedName("secondaryContactDetails")
    @Expose
    private SecondaryContactDetails secondaryContactDetails;

    public AddEmergencyDetailsReq(PrimaryContactDetails primaryContactDetails, SecondaryContactDetails secondaryContactDetails) {
        this.primaryContactDetails = primaryContactDetails;
        this.secondaryContactDetails = secondaryContactDetails;
    }

    public PrimaryContactDetails getPrimaryContactDetails() {
        return primaryContactDetails;
    }

    public void setPrimaryContactDetails(PrimaryContactDetails primaryContactDetails) {
        this.primaryContactDetails = primaryContactDetails;
    }

    public SecondaryContactDetails getSecondaryContactDetails() {
        return secondaryContactDetails;
    }

    public void setSecondaryContactDetails(SecondaryContactDetails secondaryContactDetails) {
        this.secondaryContactDetails = secondaryContactDetails;
    }
    public static class PrimaryContactDetails {

        @SerializedName("FirstName")
        @Expose
        private String firstName;
        @SerializedName("Mobile")
        @Expose
        private String mobile;
        @SerializedName("Relationship")
        @Expose
        private String relationship;

        public PrimaryContactDetails(String firstName, String mobile, String relationship) {
            this.firstName = firstName;
            this.mobile = mobile;
            this.relationship = relationship;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

    }

    public static class SecondaryContactDetails {

        @SerializedName("FirstName")
        @Expose
        private String firstName;
        @SerializedName("Mobile")
        @Expose
        private String mobile;
        @SerializedName("Relationship")
        @Expose
        private String relationship;
        public SecondaryContactDetails(String firstName, String mobile, String relationship) {
            this.firstName = firstName;
            this.mobile = mobile;
            this.relationship = relationship;
        }
        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

    }
}
