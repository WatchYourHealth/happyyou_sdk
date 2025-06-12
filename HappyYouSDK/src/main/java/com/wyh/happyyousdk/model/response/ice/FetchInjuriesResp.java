package com.wyh.happyyousdk.model.response.ice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchInjuriesResp {

        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }
    public class Datum {


        @SerializedName("InjuryName")
        @Expose
        private String injuryName;
        @SerializedName("InjuryLogo")
        @Expose
        private String injuryLogo;
        @SerializedName("ColorCode")
        @Expose
        private String colorCode;
        @SerializedName("infosource")
        @Expose
        private String infoSource;

        public String getInjuryName() {
            return injuryName;
        }

        public void setInjuryName(String injuryName) {
            this.injuryName = injuryName;
        }

        public String getInjuryLogo() {
            return injuryLogo;
        }

        public void setInjuryLogo(String injuryLogo) {
            this.injuryLogo = injuryLogo;
        }

        public String getColorCode() {
            return colorCode;
        }

        public void setColorCode(String colorCode) {
            this.colorCode = colorCode;
        }

        public String getInfoSource() {
            return infoSource;
        }

        public void setInfoSource(String infoSource) {
            this.infoSource = infoSource;
        }
    }
}
