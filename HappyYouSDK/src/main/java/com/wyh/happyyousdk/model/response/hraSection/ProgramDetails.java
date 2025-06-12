package com.wyh.happyyousdk.model.response.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProgramDetails implements Serializable {

    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("ProgramName")
    @Expose
    private String programName;
    @SerializedName("ProgramQuestion")
    @Expose
    private String programQuestion;
    @SerializedName("IsEnroll")
    @Expose
    private Integer isEnroll;
    @SerializedName("IconsLink")
    @Expose
    private String iconsLink;
    @SerializedName("string1")
    @Expose
    private String string1;
    @SerializedName("string2")
    @Expose
    private String string2;
    @SerializedName("string3")
    @Expose
    private String string3;
    @SerializedName("ValueAtEnroll")
    @Expose
    private String valueAtEnroll;
    @SerializedName("DateOfEnroll")
    @Expose
    private String dateOfEnroll;
    @SerializedName("ForDays")
    @Expose
    private String forDays;

    public String getForDays() {
        return forDays;
    }

    public void setForDays(String forDays) {
        this.forDays = forDays;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramQuestion() {
        return programQuestion;
    }

    public void setProgramQuestion(String programQuestion) {
        this.programQuestion = programQuestion;
    }

    public Integer getIsEnroll() {
        return isEnroll;
    }

    public void setIsEnroll(Integer id) {
        this.isEnroll = isEnroll;
    }
    public String getIconsLink() {
        return iconsLink;
    }

    public void setIconsLink(String iconsLink) {
        this.iconsLink = iconsLink;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getString3() {
        return string3;
    }

    public void setString3(String string3) {
        this.string3 = string3;
    }
    public String getValueAtEnroll() {
        return valueAtEnroll;
    }

    public void setValueAtEnroll(String valueAtEnroll) {
        this.valueAtEnroll = valueAtEnroll;
    }
    public String getDateOfEnroll() {
        return dateOfEnroll;
    }

    public void setDateOfEnroll(String dateOfEnroll) {
        this.dateOfEnroll = dateOfEnroll;
    }
}
