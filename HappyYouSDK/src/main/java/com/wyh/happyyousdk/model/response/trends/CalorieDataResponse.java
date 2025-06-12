package com.wyh.happyyousdk.model.response.trends;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalorieDataResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;


    public CalorieDataResponse(String msg, Boolean success, List<Datum> data, Object freevoucher) {
        super();
        this.msg = msg;
        this.success = success;
        this.data = data;
        this.freevoucher = freevoucher;
    }

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

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public class Datum {

        @SerializedName("totalCalorie")
        @Expose
        private Integer totalCalorie;
        @SerializedName("totalFat")
        @Expose
        private Integer totalFat;
        @SerializedName("totalCarbs")
        @Expose
        private Integer totalCarbs;
        @SerializedName("totalProtien")
        @Expose
        private Integer totalProtien;
        @SerializedName("consumedCalorie")
        @Expose
        private Integer consumedCalorie;
        @SerializedName("consumedCarbs")
        @Expose
        private Integer consumedCarbs;
        @SerializedName("consumedFats")
        @Expose
        private Integer consumedFats;
        @SerializedName("consumedProtein")
        @Expose
        private Integer consumedProtein;
        @SerializedName("calorieMeterId")
        @Expose
        private Integer calorieMeterId;
        @SerializedName("uuid")
        @Expose
        private String uuid;
        @SerializedName("expired")
        @Expose
        private Boolean expired;

        /**
         * No args constructor for use in serialization
         */
        public Datum() {
        }

        /**
         * @param totalCarbs
         * @param totalFat
         * @param totalCalorie
         * @param expired
         * @param consumedFats
         * @param consumedProtein
         * @param consumedCalorie
         * @param calorieMeterId
         * @param totalProtien
         * @param uuid
         * @param consumedCarbs
         */
        public Datum(Integer totalCalorie, Integer totalFat, Integer totalCarbs, Integer totalProtien, Integer consumedCalorie, Integer consumedCarbs, Integer consumedFats, Integer consumedProtein, Integer calorieMeterId, String uuid, Boolean expired) {
            super();
            this.totalCalorie = totalCalorie;
            this.totalFat = totalFat;
            this.totalCarbs = totalCarbs;
            this.totalProtien = totalProtien;
            this.consumedCalorie = consumedCalorie;
            this.consumedCarbs = consumedCarbs;
            this.consumedFats = consumedFats;
            this.consumedProtein = consumedProtein;
            this.calorieMeterId = calorieMeterId;
            this.uuid = uuid;
            this.expired = expired;
        }

        public Integer getTotalCalorie() {
            return totalCalorie;
        }

        public void setTotalCalorie(Integer totalCalorie) {
            this.totalCalorie = totalCalorie;
        }

        public Integer getTotalFat() {
            return totalFat;
        }

        public void setTotalFat(Integer totalFat) {
            this.totalFat = totalFat;
        }

        public Integer getTotalCarbs() {
            return totalCarbs;
        }

        public void setTotalCarbs(Integer totalCarbs) {
            this.totalCarbs = totalCarbs;
        }

        public Integer getTotalProtien() {
            return totalProtien;
        }

        public void setTotalProtien(Integer totalProtien) {
            this.totalProtien = totalProtien;
        }

        public Integer getConsumedCalorie() {
            return consumedCalorie;
        }

        public void setConsumedCalorie(Integer consumedCalorie) {
            this.consumedCalorie = consumedCalorie;
        }

        public Integer getConsumedCarbs() {
            return consumedCarbs;
        }

        public void setConsumedCarbs(Integer consumedCarbs) {
            this.consumedCarbs = consumedCarbs;
        }

        public Integer getConsumedFats() {
            return consumedFats;
        }

        public void setConsumedFats(Integer consumedFats) {
            this.consumedFats = consumedFats;
        }

        public Integer getConsumedProtein() {
            return consumedProtein;
        }

        public void setConsumedProtein(Integer consumedProtein) {
            this.consumedProtein = consumedProtein;
        }

        public Integer getCalorieMeterId() {
            return calorieMeterId;
        }

        public void setCalorieMeterId(Integer calorieMeterId) {
            this.calorieMeterId = calorieMeterId;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public Boolean getExpired() {
            return expired;
        }

        public void setExpired(Boolean expired) {
            this.expired = expired;
        }

    }


}
