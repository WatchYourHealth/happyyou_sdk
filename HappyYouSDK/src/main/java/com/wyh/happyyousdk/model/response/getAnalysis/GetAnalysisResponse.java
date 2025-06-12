package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.FreeVoucher;
import com.wyh.happyyousdk.model.RewardsModel;

public class GetAnalysisResponse extends CommonSuccessResponse {

    @SerializedName("data")
    @Expose
    private GetAnalysisData analysisData;



    public GetAnalysisData getAnalysisData() {
        return analysisData;
    }

    public void setAnalysisData(GetAnalysisData analysisData) {
        this.analysisData = analysisData;
    }


}
