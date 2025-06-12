package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class KLISubResponse {


    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("data")
    @Expose
    private ArrayList<Data> data;


    public String getMsg() {
        return msg;
    }

    public String getSuccess() {
        return success;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public class Data{

        @SerializedName("ActivityId")
        @Expose
        private String ActivityId;

        @SerializedName("ActivityName")
        @Expose
        private String ActivityName;

        @SerializedName("ActivityLogo")
        @Expose
        private String ActivityLogo;

        @SerializedName("ActivityDesc")
        @Expose
        private String ActivityDesc;

        @SerializedName("RedirectUrl")
        @Expose
        private String RedirectUrl;

        @SerializedName("DisclaimerImage")
        @Expose
        private String DisclaimerImage;

        @SerializedName("Disclaimer")
        @Expose
        private String Disclaimer;


        public String getActivityId() {
            return ActivityId;
        }

        public String getActivityName() {
            return ActivityName;
        }

        public String getActivityLogo() {
            return ActivityLogo;
        }

        public String getActivityDesc() {
            return ActivityDesc;
        }

        public String getRedirectUrl() {
            return RedirectUrl;
        }

        public String getDisclaimerImage() {
            return DisclaimerImage;
        }

        public String getDisclaimer() {
            return Disclaimer;
        }
    }
}
