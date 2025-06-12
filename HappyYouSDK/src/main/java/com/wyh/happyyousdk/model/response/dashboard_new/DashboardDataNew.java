package com.wyh.happyyousdk.model.response.dashboard_new;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.dashboard.model.DashboardBottomCardDataResponse;
import com.wyh.happyyousdk.dashboard.model.GetBannerResponse;
import com.wyh.happyyousdk.model.request.addFamily.AddFamilyRequest;
import com.wyh.happyyousdk.model.response.AddOnDetails;
import com.wyh.happyyousdk.model.response.FetchTemperatureResponse;
import com.wyh.happyyousdk.model.response.NudgeRedirection;
import com.wyh.happyyousdk.model.response.dashboard.DashboardData;
import com.wyh.happyyousdk.model.response.ice.FetchEmergencyDetailsResp;

import java.util.ArrayList;
import java.util.List;

public class DashboardDataNew {
    @SerializedName("getClientFamilyDetails")
    @Expose
    private List<AddFamilyRequest> getClientFamilyDetails;
    @SerializedName("fetchDashboardTilesV2")
    @Expose
    private DashboardBottomCardDataResponse.CardData fetchDashboardTilesV2;
    @SerializedName("fetchWeather")
    @Expose
    private FetchTemperatureResponse.Data fetchWeather;
    @SerializedName("fetchdashboarddata")
    @Expose
    private DashboardData fetchdashboarddata;
    @SerializedName("fetchDashboardBanner")
    @Expose
    private ArrayList<GetBannerResponse.Data> fetchDashboardBanner;

    @SerializedName("emergencydetails")
    @Expose
    private ArrayList<FetchEmergencyDetailsResp.Datum> emergencydetails;

    @SerializedName("nudgeRedirection")
    @Expose
    public NudgeRedirection nudgeRedirection;

    @SerializedName("addOnDetails")
    @Expose
    public AddOnDetails addOnDetails;

    @SerializedName("showAdminRewardsEvents")
    @Expose
    private List<ShowAdminRewardsEventsData> showAdminRewardsEvents;

    public List<AddFamilyRequest> getGetClientFamilyDetails() {
        return getClientFamilyDetails;
    }

    public void setGetClientFamilyDetails(List<AddFamilyRequest> getClientFamilyDetails) {
        this.getClientFamilyDetails = getClientFamilyDetails;
    }

    public DashboardBottomCardDataResponse.CardData getFetchDashboardTilesV2() {
        return fetchDashboardTilesV2;
    }

    public void setFetchDashboardTilesV2(DashboardBottomCardDataResponse.CardData fetchDashboardTilesV2) {
        this.fetchDashboardTilesV2 = fetchDashboardTilesV2;
    }

    public FetchTemperatureResponse.Data getFetchWeather() {
        return fetchWeather;
    }

    public void setFetchWeather(FetchTemperatureResponse.Data fetchWeather) {
        this.fetchWeather = fetchWeather;
    }

    public DashboardData getFetchdashboarddata() {
        return fetchdashboarddata;
    }

    public void setFetchdashboarddata(DashboardData fetchdashboarddata) {
        this.fetchdashboarddata = fetchdashboarddata;
    }

    public ArrayList<GetBannerResponse.Data> getFetchDashboardBanner() {
        return fetchDashboardBanner;
    }

    public void setFetchDashboardBanner(ArrayList<GetBannerResponse.Data> fetchDashboardBanner) {
        this.fetchDashboardBanner = fetchDashboardBanner;
    }

    public ArrayList<FetchEmergencyDetailsResp.Datum> getEmergencydetails() {
        return emergencydetails;
    }

    public NudgeRedirection getNudgeRedirection() {
        return nudgeRedirection;
    }

    public AddOnDetails getAddOnDetails() {
        return addOnDetails;
    }

    public List<ShowAdminRewardsEventsData> getShowAdminRewardsEvents() {
        return showAdminRewardsEvents;
    }

    public void setShowAdminRewardsEvents(List<ShowAdminRewardsEventsData> showAdminRewardsEvents) {
        this.showAdminRewardsEvents = showAdminRewardsEvents;
    }
}


