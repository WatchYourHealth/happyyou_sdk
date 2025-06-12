package com.wyh.happyyousdk.model.response.hraSection;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetEnrolledDailyGoalResponse {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public static class Datum {

        @SerializedName("UUID")
        @Expose
        private String uuid;
        @SerializedName("GoalID")
        @Expose
        private String goalID;
        @SerializedName("ProgramID")
        @Expose
        private String programID;
        @SerializedName("Steps")
        @Expose
        private String steps;
        @SerializedName("TrackedOn")
        @Expose
        private String trackedOn;
        @SerializedName("Sleep")
        @Expose
        private String sleep;
        @SerializedName("Exercise")
        @Expose
        private String exercise;
        @SerializedName("ActiveHours")
        @Expose
        private String activeHours;
        @SerializedName("CalorieConsumed")
        @Expose
        private String calorieConsumed;
        @SerializedName("WaterIntake")
        @Expose
        private String waterIntake;
        @SerializedName("HealthyHabit")
        @Expose
        private String healthyHabit;
        @SerializedName("Meditation")
        @Expose
        private Object meditation;
        @SerializedName("TwoMeals")
        @Expose
        private String twoMeals;
        @SerializedName("DinnerBeforeEight")
        @Expose
        private String dinnerBeforeEight;

        public String getTrackedOn() {
            return trackedOn;
        }

        public void setTrackedOn(String trackedOn) {
            this.trackedOn = trackedOn;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getGoalID() {
            return goalID;
        }

        public void setGoalID(String goalID) {
            this.goalID = goalID;
        }

        public String getProgramID() {
            return programID;
        }

        public void setProgramID(String programID) {
            this.programID = programID;
        }

        public String getSteps() {
            return steps;
        }

        public void setSteps(String steps) {
            this.steps = steps;
        }

        public String getSleep() {
            return sleep;
        }

        public void setSleep(String sleep) {
            this.sleep = sleep;
        }

        public String getExercise() {
            return exercise;
        }

        public void setExercise(String exercise) {
            this.exercise = exercise;
        }

        public String getActiveHours() {
            return activeHours;
        }

        public void setActiveHours(String activeHours) {
            this.activeHours = activeHours;
        }

        public String getCalorieConsumed() {
            return calorieConsumed;
        }

        public void setCalorieConsumed(String calorieConsumed) {
            this.calorieConsumed = calorieConsumed;
        }

        public String getWaterIntake() {
            return waterIntake;
        }

        public void setWaterIntake(String waterIntake) {
            this.waterIntake = waterIntake;
        }

        public String getHealthyHabit() {
            return healthyHabit;
        }

        public void setHealthyHabit(String healthyHabit) {
            this.healthyHabit = healthyHabit;
        }

        public Object getMeditation() {
            return meditation;
        }

        public void setMeditation(Object meditation) {
            this.meditation = meditation;
        }

        public String getTwoMeals() {
            return twoMeals;
        }

        public void setTwoMeals(String twoMeals) {
            this.twoMeals = twoMeals;
        }

        public String getDinnerBeforeEight() {
            return dinnerBeforeEight;
        }

        public void setDinnerBeforeEight(String dinnerBeforeEight) {
            this.dinnerBeforeEight = dinnerBeforeEight;
        }

    }


}