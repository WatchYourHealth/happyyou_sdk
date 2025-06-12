package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaveCorporateStatusReq implements Serializable {

    public SaveCorporateStatusReq(boolean isCorporateEmployee) {
        IsCorporateEmployee = isCorporateEmployee;
    }

    public boolean isCorporateEmployee() {
        return IsCorporateEmployee;
    }

    public void setCorporateEmployee(boolean corporateEmployee) {
        IsCorporateEmployee = corporateEmployee;
    }

    @SerializedName("IsCorporateEmployee")
    @Expose
    private boolean IsCorporateEmployee;

}
