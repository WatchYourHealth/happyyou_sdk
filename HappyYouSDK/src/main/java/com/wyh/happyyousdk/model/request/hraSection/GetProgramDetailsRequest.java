package com.wyh.happyyousdk.model.request.hraSection;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProgramDetailsRequest {

    @SerializedName("ProgramID")
    @Expose
    private String programID;

    @SerializedName("UUID")
    @Expose
    private String uuid;

    @SerializedName("Date")
    @Expose
    private String date;

    public GetProgramDetailsRequest() {
    }

    public GetProgramDetailsRequest(String programID,String uuid,String date) {
        super();
        this.programID = programID;
        this.uuid=uuid;
        this.date=date;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProgramID() {
        return programID;
    }

    public void setProgramID(String programID) {
        this.programID = programID;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String programID) {
        this.date = date;
    }

}
