package com.wyh.happyyousdk.model.response.hraSection.newHRA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthScoreResponseNew {
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("BodyProfile")
    @Expose
    private int bodyProfile;
    @SerializedName("LifeStyle")
    @Expose
    private int lifeStyle;
    @SerializedName("Activity")
    @Expose
    private int activity;
    @SerializedName("Diet")
    @Expose
    private int diet;
    @SerializedName("Stress")
    @Expose
    private int stress;
    @SerializedName("HealthScore")
    @Expose
    private int healthScore;
    @SerializedName("BMI")
    @Expose
    private double bmi;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("HeightUnit")
    @Expose
    private String heightUnit;
    @SerializedName("Age")
    @Expose
    private int age;
    @SerializedName("BiologicalAge")
    @Expose
    private int biologicalAge;
    @SerializedName("Height")
    @Expose
    private double height;
    @SerializedName("Weight")
    @Expose
    private double weight;
    @SerializedName("BodyProfileCount")
    @Expose
    private int bodyProfileCount;
    @SerializedName("DietCount")
    @Expose
    private int dietCount;
    @SerializedName("LifeStyleCount")
    @Expose
    private int lifestyleCount;
    @SerializedName("ActivityCount")
    @Expose
    private int activityCount;
    @SerializedName("StressCount")
    @Expose
    private int stressCount;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getBodyProfile() {
        return bodyProfile;
    }

    public void setBodyProfile(int bodyProfile) {
        this.bodyProfile = bodyProfile;
    }

    public int getLifeStyle() {
        return lifeStyle;
    }

    public void setLifeStyle(int lifeStyle) {
        this.lifeStyle = lifeStyle;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getDiet() {
        return diet;
    }

    public void setDiet(int diet) {
        this.diet = diet;
    }

    public int getStress() {
        return stress;
    }

    public void setStress(int stress) {
        this.stress = stress;
    }

    public int getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(int healthScore) {
        this.healthScore = healthScore;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(String heightUnit) {
        this.heightUnit = heightUnit;
    }

    public int getBiologicalAge() {
        return biologicalAge;
    }

    public void setBiologicalAge(int biologicalAge) {
        this.biologicalAge = biologicalAge;
    }

    public int getBodyProfileCount() {
        return bodyProfileCount;
    }

    public void setBodyProfileCount(int bodyProfileCount) {
        this.bodyProfileCount = bodyProfileCount;
    }

    public int getDietCount() {
        return dietCount;
    }

    public void setDietCount(int dietCount) {
        this.dietCount = dietCount;
    }

    public int getLifestyleCount() {
        return lifestyleCount;
    }

    public void setLifestyleCount(int lifestyleCount) {
        this.lifestyleCount = lifestyleCount;
    }

    public int getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    public int getStressCount() {
        return stressCount;
    }

    public void setStressCount(int stressCount) {
        this.stressCount = stressCount;
    }
}
