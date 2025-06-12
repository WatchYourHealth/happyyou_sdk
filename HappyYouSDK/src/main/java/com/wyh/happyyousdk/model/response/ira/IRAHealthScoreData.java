package com.wyh.happyyousdk.model.response.ira;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IRAHealthScoreData implements Serializable {

    @SerializedName("haid")
    @Expose
    private String haid;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("conversationID")
    @Expose
    private String conversationID;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gendeR")
    @Expose
    private String gendeR;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("waist")
    @Expose
    private String waist;
    @SerializedName("bloodGroup")
    @Expose
    private String bloodGroup;
    @SerializedName("activityLevel")
    @Expose
    private String activityLevel;
    @SerializedName("dietRoutine")
    @Expose
    private String dietRoutine;
    @SerializedName("consume")
    @Expose
    private String consume;
    @SerializedName("junkFood")
    @Expose
    private String junkFood;
    @SerializedName("areYouA")
    @Expose
    private String areYouA;
    @SerializedName("doYouFast")
    @Expose
    private String doYouFast;
    @SerializedName("playSports")
    @Expose
    private String playSports;
    @SerializedName("glassOfWater")
    @Expose
    private String glassOfWater;
    @SerializedName("sleepHours")
    @Expose
    private Object sleepHours;
    @SerializedName("soundSleeper")
    @Expose
    private Object soundSleeper;
    @SerializedName("sufferFrom")
    @Expose
    private Object sufferFrom;
    @SerializedName("addictedToTV")
    @Expose
    private Object addictedToTV;
    @SerializedName("stress")
    @Expose
    private Object stress;
    @SerializedName("communting")
    @Expose
    private Object communting;
    @SerializedName("screenTime")
    @Expose
    private Object screenTime;
    @SerializedName("healthCheckUp")
    @Expose
    private Object healthCheckUp;
    @SerializedName("appreciation")
    @Expose
    private Object appreciation;
    @SerializedName("familyTime")
    @Expose
    private Object familyTime;
    @SerializedName("meditate")
    @Expose
    private Object meditate;
    @SerializedName("familyHistory")
    @Expose
    private Object familyHistory;
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * No args constructor for use in serialization
     *
     */
    public IRAHealthScoreData() {
    }

    /**
     *
     * @param addictedToTV
     * @param conversationID
     * @param screenTime
     * @param areYouA
     * @param consume
     * @param familyTime
     * @param uuid
     * @param createdAt
     * @param bloodGroup
     * @param familyHistory
     * @param activityLevel
     * @param height
     * @param sufferFrom
     * @param soundSleeper
     * @param stress
     * @param haid
     * @param gendeR
     * @param glassOfWater
     * @param weight
     * @param appreciation
     * @param sleepHours
     * @param meditate
     * @param healthCheckUp
     * @param doYouFast
     * @param dob
     * @param junkFood
     * @param playSports
     * @param communting
     * @param waist
     * @param dietRoutine
     * @param status
     */
    public IRAHealthScoreData(String haid, String uuid, String conversationID, String createdAt, String dob, String gendeR, String weight, String height, String waist, String bloodGroup, String activityLevel, String dietRoutine, String consume, String junkFood, String areYouA, String doYouFast, String playSports, String glassOfWater, Object sleepHours, Object soundSleeper, Object sufferFrom, Object addictedToTV, Object stress, Object communting, Object screenTime, Object healthCheckUp, Object appreciation, Object familyTime, Object meditate, Object familyHistory, String status) {
        super();
        this.haid = haid;
        this.uuid = uuid;
        this.conversationID = conversationID;
        this.createdAt = createdAt;
        this.dob = dob;
        this.gendeR = gendeR;
        this.weight = weight;
        this.height = height;
        this.waist = waist;
        this.bloodGroup = bloodGroup;
        this.activityLevel = activityLevel;
        this.dietRoutine = dietRoutine;
        this.consume = consume;
        this.junkFood = junkFood;
        this.areYouA = areYouA;
        this.doYouFast = doYouFast;
        this.playSports = playSports;
        this.glassOfWater = glassOfWater;
        this.sleepHours = sleepHours;
        this.soundSleeper = soundSleeper;
        this.sufferFrom = sufferFrom;
        this.addictedToTV = addictedToTV;
        this.stress = stress;
        this.communting = communting;
        this.screenTime = screenTime;
        this.healthCheckUp = healthCheckUp;
        this.appreciation = appreciation;
        this.familyTime = familyTime;
        this.meditate = meditate;
        this.familyHistory = familyHistory;
        this.status = status;
    }

    public String getHaid() {
        return haid;
    }

    public void setHaid(String haid) {
        this.haid = haid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getConversationID() {
        return conversationID;
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGendeR() {
        return gendeR;
    }

    public void setGendeR(String gendeR) {
        this.gendeR = gendeR;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getDietRoutine() {
        return dietRoutine;
    }

    public void setDietRoutine(String dietRoutine) {
        this.dietRoutine = dietRoutine;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getJunkFood() {
        return junkFood;
    }

    public void setJunkFood(String junkFood) {
        this.junkFood = junkFood;
    }

    public String getAreYouA() {
        return areYouA;
    }

    public void setAreYouA(String areYouA) {
        this.areYouA = areYouA;
    }

    public String getDoYouFast() {
        return doYouFast;
    }

    public void setDoYouFast(String doYouFast) {
        this.doYouFast = doYouFast;
    }

    public String getPlaySports() {
        return playSports;
    }

    public void setPlaySports(String playSports) {
        this.playSports = playSports;
    }

    public String getGlassOfWater() {
        return glassOfWater;
    }

    public void setGlassOfWater(String glassOfWater) {
        this.glassOfWater = glassOfWater;
    }

    public Object getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(Object sleepHours) {
        this.sleepHours = sleepHours;
    }

    public Object getSoundSleeper() {
        return soundSleeper;
    }

    public void setSoundSleeper(Object soundSleeper) {
        this.soundSleeper = soundSleeper;
    }

    public Object getSufferFrom() {
        return sufferFrom;
    }

    public void setSufferFrom(Object sufferFrom) {
        this.sufferFrom = sufferFrom;
    }

    public Object getAddictedToTV() {
        return addictedToTV;
    }

    public void setAddictedToTV(Object addictedToTV) {
        this.addictedToTV = addictedToTV;
    }

    public Object getStress() {
        return stress;
    }

    public void setStress(Object stress) {
        this.stress = stress;
    }

    public Object getCommunting() {
        return communting;
    }

    public void setCommunting(Object communting) {
        this.communting = communting;
    }

    public Object getScreenTime() {
        return screenTime;
    }

    public void setScreenTime(Object screenTime) {
        this.screenTime = screenTime;
    }

    public Object getHealthCheckUp() {
        return healthCheckUp;
    }

    public void setHealthCheckUp(Object healthCheckUp) {
        this.healthCheckUp = healthCheckUp;
    }

    public Object getAppreciation() {
        return appreciation;
    }

    public void setAppreciation(Object appreciation) {
        this.appreciation = appreciation;
    }

    public Object getFamilyTime() {
        return familyTime;
    }

    public void setFamilyTime(Object familyTime) {
        this.familyTime = familyTime;
    }

    public Object getMeditate() {
        return meditate;
    }

    public void setMeditate(Object meditate) {
        this.meditate = meditate;
    }

    public Object getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(Object familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
