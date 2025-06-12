package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DownloadFaceScanData {

    @SerializedName("fileName")
    @Expose
    private String fileName;

    @SerializedName("contentType")
    @Expose
    private String contentType;

    @SerializedName("fileDownloadName")
    @Expose
    private String fileDownloadName;

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileDownloadName() {
        return fileDownloadName;
    }
}
