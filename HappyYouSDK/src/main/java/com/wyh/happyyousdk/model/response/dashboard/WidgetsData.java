package com.wyh.happyyousdk.model.response.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WidgetsData {
    @SerializedName("activeHours")
    @Expose
    private CalorieCount activeHours;
    @SerializedName("calorieCount")
    @Expose
    private CalorieCount calorieCount;
    @SerializedName("happyFootprints")
    @Expose
    private CalorieCount happyFootprints;
    @SerializedName("pillowTime")
    @Expose
    private CalorieCount pillowTime;
    @SerializedName("h2O")
    @Expose
    private CalorieCount h2O;
    @SerializedName("zenZone")
    @Expose
    private CalorieCount zenZone;
    @SerializedName("tribe")
    @Expose
    private CalorieCount tribe;
    @SerializedName("healthhack")
    @Expose
    private CalorieCount healthHacks;

    public CalorieCount getActiveHours() {
        return activeHours;
    }

    public void setActiveHours(CalorieCount activeHours) {
        this.activeHours = activeHours;
    }

    public CalorieCount getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(CalorieCount calorieCount) {
        this.calorieCount = calorieCount;
    }

    public CalorieCount getHappyFootprints() {
        return happyFootprints;
    }

    public void setHappyFootprints(CalorieCount happyFootprints) {
        this.happyFootprints = happyFootprints;
    }

    public CalorieCount getPillowTime() {
        return pillowTime;
    }

    public void setPillowTime(CalorieCount pillowTime) {
        this.pillowTime = pillowTime;
    }

    public CalorieCount getH2O() {
        return h2O;
    }

    public void setH2O(CalorieCount h2O) {
        this.h2O = h2O;
    }

    public CalorieCount getZenZone() {
        return zenZone;
    }

    public void setZenZone(CalorieCount zenZone) {
        this.zenZone = zenZone;
    }

    public CalorieCount getTribe() {
        return tribe;
    }

    public void setTribe(CalorieCount tribe) {
        this.tribe = tribe;
    }

    public CalorieCount getHealthHacks() {
        return healthHacks;
    }

    public void setHealthHacks(CalorieCount healthHacks) {
        this.healthHacks = healthHacks;
    }

    public static class ZenZone {

        @SerializedName("count")
        @Expose
        private Double count;
        @SerializedName("goal")
        @Expose
        private Integer goal;

        public Double getCount() {
            return count;
        }

        public void setCount(Double count) {
            this.count = count;
        }

        public Integer getGoal() {
            return goal;
        }

        public void setGoal(Integer goal) {
            this.goal = goal;
        }

    }

    public static class PillowTime {

        @SerializedName("count")
        @Expose
        private Double count;
        @SerializedName("goal")
        @Expose
        private Integer goal;

        public Double getCount() {
            return count;
        }

        public void setCount(Double count) {
            this.count = count;
        }

        public Integer getGoal() {
            return goal;
        }

        public void setGoal(Integer goal) {
            this.goal = goal;
        }

    }

    public static class HappyFootprints {

        @SerializedName("count")
        @Expose
        private Double count;
        @SerializedName("goal")
        @Expose
        private Integer goal;

        public Double getCount() {
            return count;
        }

        public void setCount(Double count) {
            this.count = count;
        }

        public Integer getGoal() {
            return goal;
        }

        public void setGoal(Integer goal) {
            this.goal = goal;
        }

    }

    public static class H2o {

        @SerializedName("count")
        @Expose
        private Double count;
        @SerializedName("goal")
        @Expose
        private Integer goal;

        public Double getCount() {
            return count;
        }

        public void setCount(Double count) {
            this.count = count;
        }

        public Integer getGoal() {
            return goal;
        }

        public void setGoal(Integer goal) {
            this.goal = goal;
        }

    }

    public static class CalorieCount {

        @SerializedName("protein")
        @Expose
        private Integer protein;
        @SerializedName("carbs")
        @Expose
        private Integer carbs;
        @SerializedName("fat")
        @Expose
        private Integer fat;
        @SerializedName("count")
        @Expose
        private Double count;
        @SerializedName("goal")
        @Expose
        private Integer goal;
        @SerializedName("burnedCalorie")
        @Expose
        private Integer burnedCalorie;

        public Integer getProtein() {
            return protein;
        }

        public void setProtein(Integer protein) {
            this.protein = protein;
        }

        public Integer getCarbs() {
            return carbs;
        }

        public void setCarbs(Integer carbs) {
            this.carbs = carbs;
        }

        public Integer getFat() {
            return fat;
        }

        public void setFat(Integer fat) {
            this.fat = fat;
        }

        public Double getCount() {
            return count;
        }

        public void setCount(Double count) {
            this.count = count;
        }

        public Integer getGoal() {
            return goal;
        }

        public void setGoal(Integer goal) {
            this.goal = goal;
        }

        public Integer getBurnedCalorie() {
            return burnedCalorie;
        }

        public void setBurnedCalorie(Integer burnedCalorie) {
            this.burnedCalorie = burnedCalorie;
        }
    }

    public static class ActiveHours {

        @SerializedName("count")
        @Expose
        private Double count;
        @SerializedName("goal")
        @Expose
        private Integer goal;

        public Double getCount() {
            return count;
        }

        public void setCount(Double count) {
            this.count = count;
        }

        public Integer getGoal() {
            return goal;
        }

        public void setGoal(Integer goal) {
            this.goal = goal;
        }



    }
}
