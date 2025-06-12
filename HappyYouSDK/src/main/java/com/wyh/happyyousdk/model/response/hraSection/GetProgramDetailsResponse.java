package com.wyh.happyyousdk.model.response.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GetProgramDetailsResponse {

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

        @SerializedName("GoalID")
        @Expose
        private Integer goalId;
        @SerializedName("Steps")
        @Expose
        private String steps;
        @SerializedName("Sleep")
        @Expose
        private String sleep;
        @SerializedName("Excercise")
        @Expose
        private String excercise;
        @SerializedName("ActiveHours")
        @Expose
        private String activeHours;
        @SerializedName("CalorieConsumedGoal")
        @Expose
        private String calorieConsumed;
        @SerializedName("CaloriesConsumed")
        @Expose
        private String calorieConsumedByUser;
        @SerializedName("Caloriesburn")
        @Expose
        private String caloriesBurn;
        @SerializedName("IsExerciseDone")
        @Expose
        private String isExerciseDone;
        @SerializedName("CalorieBurnGoal")
        @Expose
        private String calorieBurnGoal;
        @SerializedName("WaterIntake")
        @Expose
        private String waterIntake;
        @SerializedName("HealthyHabit")
        @Expose
        private String healthyHabit;
        @SerializedName("Meditation")
        @Expose
        private String meditation;
        @SerializedName("TwoMeals")
        @Expose
        private String twoMeals;
        @SerializedName("DinnerBeforeEight")
        @Expose
        private String dinnerBeforeEight;

        public Datum(Integer goalId, String steps, String sleep, String excercise, String activeHours, String calorieConsumed, String calorieConsumedByUser,
                     String caloriesBurn, String isExerciseDone, String calorieBurnGoal, String waterIntake,
                     String healthyHabit, String meditation, String twoMeals, String dinnerBeforeEight) {
            this.goalId = goalId;
            this.steps = steps;
            this.sleep = sleep;
            this.excercise = excercise;
            this.activeHours = activeHours;
            this.calorieConsumed = calorieConsumed;
            this.calorieConsumedByUser = calorieConsumedByUser;
            this.caloriesBurn = caloriesBurn;
            this.isExerciseDone = isExerciseDone;
            this.calorieBurnGoal = calorieBurnGoal;
            this.waterIntake = waterIntake;
            this.healthyHabit = healthyHabit;
            this.meditation = meditation;
            this.twoMeals = twoMeals;
            this.dinnerBeforeEight = dinnerBeforeEight;
        }

        public Integer getGoalId() {
            return goalId;
        }

        public void setId(Integer goalId) {
            this.goalId = goalId;
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

        public String getExcercise() {
            return excercise;
        }

        public void setExcercise(String excercise) {
            this.excercise = excercise;
        }

        public String getActiveHours() {
            return activeHours;
        }

        public void setActiveHours(String activeHours) {
            this.activeHours = activeHours;
        }

        public String getCalorieConsumedGoal() {
            return calorieConsumed;
        }

        public void setCalorieConsumed(String calorieConsumed) {
            this.calorieConsumed = calorieConsumed;
        }

        public String getCalorieConsumedByUser() {
            return calorieConsumedByUser;
        }

        public void setGoalId(Integer goalId) {
            this.goalId = goalId;
        }

        public String getCalorieConsumed() {
            return calorieConsumed;
        }

        public String getCalorieBurnGoal() {
            return calorieBurnGoal;
        }

        public void setCalorieBurnGoal(String calorieBurnGoal) {
            this.calorieBurnGoal = calorieBurnGoal;
        }

        public void setCalorieConsumedByUser(String calorieConsumedByUser) {
            this.calorieConsumedByUser = calorieConsumedByUser;
        }

        public String getCaloriesBurn() {
            return caloriesBurn;
        }

        public void setCaloriesBurn(String caloriesBurn) {
            this.caloriesBurn = caloriesBurn;
        }

        public String isExerciseDone() {
            return isExerciseDone;
        }

        public void setExerciseDone(String exerciseDone) {
            isExerciseDone = exerciseDone;
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

        public String getMeditation() {
            return meditation;
        }

        public void setMeditation(String meditation) {
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
