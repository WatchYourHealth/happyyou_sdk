package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAnalysisData {
    @SerializedName("worryAbout")
    @Expose
    private List<WorryAbout> worryAbout = null;
    @SerializedName("doThisUrgent")
    @Expose
    private List<DoThisUrgent> doThisUrgent = null;
    @SerializedName("biologicalAge")
    @Expose
    private int biologicalAge;
    @SerializedName("sleepAnalysis")
    @Expose
    private SleepAnalysis sleepAnalysis;
    @SerializedName("wtHRatio")
    @Expose
    private WtHRatio wtHRatio;
    @SerializedName("idealBMIWeight")
    @Expose
    private IdealBMIWeight idealBMIWeight;
    @SerializedName("avgBMI")
    @Expose
    private AvgBMI avgBMI;
    @SerializedName("bmr")
    @Expose
    private int bmr;
    @SerializedName("rmr")
    @Expose
    private int rmr;
    @SerializedName("waterWidget")
    @Expose
    private WaterWidget waterWidget;
    @SerializedName("blueLightSyndrome")
    @Expose
    private BlueLightSyndrome blueLightSyndrome;
    @SerializedName("bodyFatPercentage")
    @Expose
    private BodyFatPercentage bodyFatPercentage;
    @SerializedName("activeDTU")
    @Expose
    private Object activeDTU;
    @SerializedName("activeProgram")
    @Expose
    private ActiveProgram activeProgram;
    @SerializedName("activeChallenge")
    @Expose
    private Object activeChallenge;
    @SerializedName("challengeTaken")
    @Expose
    private int challengeTaken;
    @SerializedName("bookLab")
    @Expose
    private List<Object> bookLab = null;
    @SerializedName("heights")
    @Expose
    private String heights;
    @SerializedName("score")
    @Expose
    private int score;
    @SerializedName("latestscore")
    @Expose
    private int latestscore;
    @SerializedName("bodyProfile")
    @Expose
    private int bodyProfile;
    @SerializedName("lifeStyle")
    @Expose
    private int lifeStyle;
    @SerializedName("diet")
    @Expose
    private int diet;
    @SerializedName("stressScore")
    @Expose
    private int stressScore;
    @SerializedName("happinessScore")
    @Expose
    private int happinessScore;
    @SerializedName("healthScoreGoal")
    @Expose
    private int healthScoreGoal;
    @SerializedName("hrAtaken")
    @Expose
    private String hrAtaken;
    @SerializedName("incDec")
    @Expose
    private String incDec;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("analysisInput")
    @Expose
    private AnalysisInput analysisInput;
    @SerializedName("analyticPositive")
    @Expose
    private List<String> analyticPositive = null;
    @SerializedName("analyticNegative")
    @Expose
    private List<String> analyticNegative = null;
    @SerializedName("happinessIndex")
    @Expose
    private List<HappinessIndex> happinessIndex = null;
    @SerializedName("newScore")
    @Expose
    private NewScore newScore;

    public List<WorryAbout> getWorryAbout() {
        return worryAbout;
    }

    public void setWorryAbout(List<WorryAbout> worryAbout) {
        this.worryAbout = worryAbout;
    }

    public List<DoThisUrgent> getDoThisUrgent() {
        return doThisUrgent;
    }

    public void setDoThisUrgent(List<DoThisUrgent> doThisUrgent) {
        this.doThisUrgent = doThisUrgent;
    }

    public int getBiologicalAge() {
        return biologicalAge;
    }

    public void setBiologicalAge(int biologicalAge) {
        this.biologicalAge = biologicalAge;
    }

    public SleepAnalysis getSleepAnalysis() {
        return sleepAnalysis;
    }

    public void setSleepAnalysis(SleepAnalysis sleepAnalysis) {
        this.sleepAnalysis = sleepAnalysis;
    }

    public WtHRatio getWtHRatio() {
        return wtHRatio;
    }

    public void setWtHRatio(WtHRatio wtHRatio) {
        this.wtHRatio = wtHRatio;
    }

    public IdealBMIWeight getIdealBMIWeight() {
        return idealBMIWeight;
    }

    public void setIdealBMIWeight(IdealBMIWeight idealBMIWeight) {
        this.idealBMIWeight = idealBMIWeight;
    }

    public AvgBMI getAvgBMI() {
        return avgBMI;
    }

    public void setAvgBMI(AvgBMI avgBMI) {
        this.avgBMI = avgBMI;
    }

    public int getBmr() {
        return bmr;
    }

    public void setBmr(int bmr) {
        this.bmr = bmr;
    }

    public int getRmr() {
        return rmr;
    }

    public void setRmr(int rmr) {
        this.rmr = rmr;
    }

    public WaterWidget getWaterWidget() {
        return waterWidget;
    }

    public void setWaterWidget(WaterWidget waterWidget) {
        this.waterWidget = waterWidget;
    }

    public BlueLightSyndrome getBlueLightSyndrome() {
        return blueLightSyndrome;
    }

    public void setBlueLightSyndrome(BlueLightSyndrome blueLightSyndrome) {
        this.blueLightSyndrome = blueLightSyndrome;
    }

    public BodyFatPercentage getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(BodyFatPercentage bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public Object getActiveDTU() {
        return activeDTU;
    }

    public void setActiveDTU(Object activeDTU) {
        this.activeDTU = activeDTU;
    }

    public ActiveProgram getActiveProgram() {
        return activeProgram;
    }

    public void setActiveProgram(ActiveProgram activeProgram) {
        this.activeProgram = activeProgram;
    }

    public Object getActiveChallenge() {
        return activeChallenge;
    }

    public void setActiveChallenge(Object activeChallenge) {
        this.activeChallenge = activeChallenge;
    }

    public int getChallengeTaken() {
        return challengeTaken;
    }

    public void setChallengeTaken(int challengeTaken) {
        this.challengeTaken = challengeTaken;
    }

    public List<Object> getBookLab() {
        return bookLab;
    }

    public void setBookLab(List<Object> bookLab) {
        this.bookLab = bookLab;
    }

    public String getHeights() {
        return heights;
    }

    public void setHeights(String heights) {
        this.heights = heights;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLatestscore() {
        return latestscore;
    }

    public void setLatestscore(int latestscore) {
        this.latestscore = latestscore;
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

    public int getDiet() {
        return diet;
    }

    public void setDiet(int diet) {
        this.diet = diet;
    }

    public int getStressScore() {
        return stressScore;
    }

    public void setStressScore(int stressScore) {
        this.stressScore = stressScore;
    }

    public int getHappinessScore() {
        return happinessScore;
    }

    public void setHappinessScore(int happinessScore) {
        this.happinessScore = happinessScore;
    }

    public int getHealthScoreGoal() {
        return healthScoreGoal;
    }

    public void setHealthScoreGoal(int healthScoreGoal) {
        this.healthScoreGoal = healthScoreGoal;
    }

    public String getHrAtaken() {
        return hrAtaken;
    }

    public void setHrAtaken(String hrAtaken) {
        this.hrAtaken = hrAtaken;
    }

    public String getIncDec() {
        return incDec;
    }

    public void setIncDec(String incDec) {
        this.incDec = incDec;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AnalysisInput getAnalysisInput() {
        return analysisInput;
    }

    public void setAnalysisInput(AnalysisInput analysisInput) {
        this.analysisInput = analysisInput;
    }

    public List<String> getAnalyticPositive() {
        return analyticPositive;
    }

    public void setAnalyticPositive(List<String> analyticPositive) {
        this.analyticPositive = analyticPositive;
    }

    public List<String> getAnalyticNegative() {
        return analyticNegative;
    }

    public void setAnalyticNegative(List<String> analyticNegative) {
        this.analyticNegative = analyticNegative;
    }

    public List<HappinessIndex> getHappinessIndex() {
        return happinessIndex;
    }

    public void setHappinessIndex(List<HappinessIndex> happinessIndex) {
        this.happinessIndex = happinessIndex;
    }

    public NewScore getNewScore() {
        return newScore;
    }

    public void setNewScore(NewScore newScore) {
        this.newScore = newScore;
    }
}
