package com.wyh.happyyousdk.addFamily;

import com.wyh.happyyousdk.model.request.ice.AddEmergencyDetailsReq;

public class AddFamilyPojo {

    AddFamilyData primaryFamily;
    AddFamilyData SecondaryFamily;


    public AddFamilyPojo(AddFamilyData primaryFamily, AddFamilyData secondaryFamily) {
        this.primaryFamily = primaryFamily;
        SecondaryFamily = secondaryFamily;
    }

    public AddFamilyData getPrimaryFamily() {
        return primaryFamily;
    }

    public void setPrimaryFamily(AddFamilyData primaryFamily) {
        this.primaryFamily = primaryFamily;
    }

    public AddFamilyData getSecondaryFamily() {
        return SecondaryFamily;
    }

    public void setSecondaryFamily(AddFamilyData secondaryFamily) {
        SecondaryFamily = secondaryFamily;
    }

    static class AddFamilyData {

        String name;
        String contactNumber;
        String relation;


        public AddFamilyData(String name, String contactNumber, String relation) {
            this.name = name;
            this.contactNumber = contactNumber;
            this.relation = relation;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }
    }

}
