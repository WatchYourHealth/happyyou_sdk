package com.wyh.happyyousdk.model.request.hraSection.mentalWellness;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateDailyMHCRequest {

    @SerializedName("UUID")
    @Expose
    private String uuid;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("ProgramID")
    @Expose
    private String programID;
    @SerializedName("MHCID")
    @Expose
    private String mhcid;
    @SerializedName("MHCValue")
    @Expose
    private String mHCValue;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("ModifiedOn")
    @Expose
    private String modifiedOn;

    public UpdateDailyMHCRequest(String uuid, String date, String programID, String mhcid, String mHCValue, String status, String isActive, String modifiedOn) {
        this.uuid = uuid;
        this.date = date;
        this.programID = programID;
        this.mhcid = mhcid;
        this.mHCValue = mHCValue;
        this.status = status;
        this.isActive = isActive;
        this.modifiedOn = modifiedOn;
    }
}
