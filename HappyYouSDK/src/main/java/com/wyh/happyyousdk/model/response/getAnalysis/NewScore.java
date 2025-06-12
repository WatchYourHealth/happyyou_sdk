package com.wyh.happyyousdk.model.response.getAnalysis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewScore {
    @SerializedName("CRN")
    @Expose
    private String crn;
    @SerializedName("Sections")
    @Expose
    private List<Section> sections = null;
    @SerializedName("positiveFactors")
    @Expose
    private List<String> positiveFactors = null;
    @SerializedName("nagetiveFactor")
    @Expose
    private List<String> nagetiveFactor = null;
    @SerializedName("RRpositiveFactors")
    @Expose
    private List<String> rRpositiveFactors = null;
    @SerializedName("RRnegativeFactor")
    @Expose
    private List<String> rRnegativeFactor = null;
    @SerializedName("healthAnalysis")
    @Expose
    private List<String> healthAnalysis = null;
    @SerializedName("BMI")
    @Expose
    private double bmi;
    @SerializedName("finalscore")
    @Expose
    private int finalscore;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Age")
    @Expose
    private int age;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("Height")
    @Expose
    private double height;
    @SerializedName("Heights")
    @Expose
    private String heights;
    @SerializedName("Weight")
    @Expose
    private Double weight;
    @SerializedName("TargetWeight")
    @Expose
    private double targetWeight;
    @SerializedName("HappinessIndex")
    @Expose
    private List<HappinessIndexNewScore> happinessIndex = null;
    @SerializedName("HappinessScore")
    @Expose
    private int happinessScore;
    @SerializedName("Tags")
    @Expose
    private String tags;
    @SerializedName("Concern")
    @Expose
    private String concern;

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<String> getPositiveFactors() {
        return positiveFactors;
    }

    public void setPositiveFactors(List<String> positiveFactors) {
        this.positiveFactors = positiveFactors;
    }

    public List<String> getNagetiveFactor() {
        return nagetiveFactor;
    }

    public void setNagetiveFactor(List<String> nagetiveFactor) {
        this.nagetiveFactor = nagetiveFactor;
    }

    public List<String> getRRpositiveFactors() {
        return rRpositiveFactors;
    }

    public void setRRpositiveFactors(List<String> rRpositiveFactors) {
        this.rRpositiveFactors = rRpositiveFactors;
    }

    public List<String> getRRnegativeFactor() {
        return rRnegativeFactor;
    }

    public void setRRnegativeFactor(List<String> rRnegativeFactor) {
        this.rRnegativeFactor = rRnegativeFactor;
    }

    public List<String> getHealthAnalysis() {
        return healthAnalysis;
    }

    public void setHealthAnalysis(List<String> healthAnalysis) {
        this.healthAnalysis = healthAnalysis;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public int getFinalscore() {
        return finalscore;
    }

    public void setFinalscore(int finalscore) {
        this.finalscore = finalscore;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getHeights() {
        return heights;
    }

    public void setHeights(String heights) {
        this.heights = heights;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public List<HappinessIndexNewScore> getHappinessIndex() {
        return happinessIndex;
    }

    public void setHappinessIndex(List<HappinessIndexNewScore> happinessIndex) {
        this.happinessIndex = happinessIndex;
    }

    public int getHappinessScore() {
        return happinessScore;
    }

    public void setHappinessScore(int happinessScore) {
        this.happinessScore = happinessScore;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getConcern() {
        return concern;
    }

    public void setConcern(String concern) {
        this.concern = concern;
    }
}
