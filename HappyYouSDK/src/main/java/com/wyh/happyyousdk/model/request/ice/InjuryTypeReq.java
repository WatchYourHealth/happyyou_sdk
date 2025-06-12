package com.wyh.happyyousdk.model.request.ice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InjuryTypeReq {

        @SerializedName("InjuryName")
        @Expose
        private String injuryName;

    public InjuryTypeReq(String injuryName) {
        this.injuryName = injuryName;
    }

    public String getInjuryName() {
            return injuryName;
        }

        public void setInjuryName(String injuryName) {
            this.injuryName = injuryName;
        }


}
