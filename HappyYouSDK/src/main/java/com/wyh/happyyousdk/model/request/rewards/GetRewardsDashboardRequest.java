package com.wyh.happyyousdk.model.request.rewards;

public class GetRewardsDashboardRequest {
    private String tag;

    public GetRewardsDashboardRequest(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
