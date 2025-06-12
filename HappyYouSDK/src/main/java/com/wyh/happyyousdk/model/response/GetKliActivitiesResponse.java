package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;

import java.util.List;

public class GetKliActivitiesResponse extends CommonSuccessResponse {
    @SerializedName("data")
    @Expose
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("ActivityId")
        @Expose
        private Integer activityId;
        @SerializedName("ActivityName")
        @Expose
        private String activityName;
        @SerializedName("ActivityLogo")
        @Expose
        private String activityLogo;
        @SerializedName("ActivityDesc")
        @Expose
        private String activityDesc;
        @SerializedName("ActivityStartDate")
        @Expose
        private String activityStartDate;
        @SerializedName("ActivityEndDate")
        @Expose
        private String activityEndDate;

        @SerializedName("IsCoupon")
        @Expose
        private Boolean IsCoupon;



        public Integer getActivityId() {
            return activityId;
        }

        public void setActivityId(Integer activityId) {
            this.activityId = activityId;
        }

        public String getActivityName() {
            return activityName;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public String getActivityLogo() {
            return activityLogo;
        }

        public void setActivityLogo(String activityLogo) {
            this.activityLogo = activityLogo;
        }

        public String getActivityDesc() {
            return activityDesc;
        }

        public void setActivityDesc(String activityDesc) {
            this.activityDesc = activityDesc;
        }

        public String getActivityStartDate() {
            return activityStartDate;
        }

        public void setActivityStartDate(String activityStartDate) {
            this.activityStartDate = activityStartDate;
        }

        public String getActivityEndDate() {
            return activityEndDate;
        }

        public void setActivityEndDate(String activityEndDate) {
            this.activityEndDate = activityEndDate;
        }

        public Boolean getCoupon() {
            return IsCoupon;
        }
    }
}
