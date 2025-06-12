package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FacnScanInTribeRequest {

    @SerializedName("PdfPath")
    @Expose
    private String PdfPath;


    @SerializedName("CommunityIds")
    @Expose
    private ArrayList<CommunityIDs> CommunityIds;

    public FacnScanInTribeRequest(String pdfPath, ArrayList<CommunityIDs> communityIds) {
        PdfPath = pdfPath;
        CommunityIds = communityIds;
    }

    public String getPdfPath() {
        return PdfPath;
    }

    public ArrayList<CommunityIDs> getCommunityIds() {
        return CommunityIds;
    }


}


