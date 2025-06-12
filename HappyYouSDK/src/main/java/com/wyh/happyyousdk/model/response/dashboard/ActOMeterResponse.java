package com.wyh.happyyousdk.model.response.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActOMeterResponse {
    @SerializedName("user_StepsGoal")
    @Expose
    private int userStepsGoal;
    @SerializedName("user_TodayWaterIntakeGoal")
    @Expose
    private int userTodayWaterIntakeGoal;
    @SerializedName("user_CalorieGoal")
    @Expose
    private int userCalorieGoal;
    @SerializedName("user_TodaySteps")
    @Expose
    private int userTodaySteps;
    @SerializedName("user_WaterGoal")
    @Expose
    private int userWaterGoal;
    @SerializedName("user_CalorieBurned")
    @Expose
    private int userCalorieBurned = 0;

    @SerializedName("user_CalorieIntake")
    @Expose
    private int user_CalorieIntake;

    @SerializedName("user_CalorieIntakeGoal")
    @Expose
    private int user_CalorieIntakeGoal;

    @SerializedName("zenzoneMinutes")
    @Expose
    private int zenzoneMinutes;

    public int getUserStepsGoal() {
        return userStepsGoal;
    }

    public void setUserStepsGoal(int userStepsGoal) {
        this.userStepsGoal = userStepsGoal;
    }

    public int getUserTodayWaterIntakeGoal() {
        return userTodayWaterIntakeGoal;
    }

    public void setUserTodayWaterIntakeGoal(int userTodayWaterIntakeGoal) {
        this.userTodayWaterIntakeGoal = userTodayWaterIntakeGoal;
    }

    public int getUserCalorieGoal() {
        return userCalorieGoal;
    }

    public void setUserCalorieGoal(int userCalorieGoal) {
        this.userCalorieGoal = userCalorieGoal;
    }

    public int getUserTodaySteps() {
        return userTodaySteps;
    }

    public void setUserTodaySteps(int userTodaySteps) {
        this.userTodaySteps = userTodaySteps;
    }

    public int getUserWaterGoal() {
        return userWaterGoal;
    }

    public void setUserWaterGoal(int userWaterGoal) {
        this.userWaterGoal = userWaterGoal;
    }

    public int getUserCalorieBurned() {
        return userCalorieBurned;
    }

    public void setUserCalorieBurned(int userCalorieBurned) {
        this.userCalorieBurned = userCalorieBurned;
    }

    public int getUser_CalorieIntake() {
        return user_CalorieIntake;
    }

    public int getUser_CalorieIntakeGoal() {
        return user_CalorieIntakeGoal;
    }

    public int getZenzoneMinutes() {
        return zenzoneMinutes;
    }
}
