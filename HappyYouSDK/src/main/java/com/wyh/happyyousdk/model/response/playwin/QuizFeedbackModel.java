package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.quizathon.SaveQuizathonAnsModel;

import java.io.Serializable;

public class QuizFeedbackModel implements Serializable {

    @SerializedName("feedbackId")
    @Expose
    private String feedbackId;
    @SerializedName("feedbackTitle")
    @Expose
    private String feedbackTitle;
    @SerializedName("feedbackDesc")
    @Expose
    private String feedbackDesc;
    @SerializedName("feedbackStartDate")
    @Expose
    private String feedbackStartDate;
    @SerializedName("feedbackLogo")
    @Expose
    private String feedbackLogo;
    @SerializedName("feedbackEndDate")
    @Expose
    private String feedbackEndDate;
    @SerializedName("feedbackQuestionJson")
    @Expose
    private String feedbackQuestionJson;
    @SerializedName("feedbackAnswerJson")
    @Expose
    private String feedbackAnswerJson;
    @SerializedName("isFeedbackTaken")
    @Expose
    private Boolean isFeedbackTaken;
    @SerializedName("feedbackCardBG")
    @Expose
    private String feedbackCardBG;
    @SerializedName("feedbackButtonText")
    @Expose
    private String feedbackButtonText;
    @SerializedName("feedbackButtonColor")
    @Expose
    private String feedbackButtonColor;
    @SerializedName("showActivity")
    @Expose
    private boolean showActivity = false;

    @SerializedName("activity")
    @Expose
    String activity;
    @SerializedName("whatToDo")
    @Expose
    String whatToDo;
    @SerializedName("howToDo")
    @Expose
    String howToDo;
    @SerializedName("whyToDo")
    @Expose
    String whyToDo;
    @SerializedName("redirectionKey")
    @Expose
    String redirectionKey;
    @SerializedName("expireOn")
    @Expose
    String expireOn;
    @SerializedName("isStarted")
    @Expose
    boolean isStarted;
    @SerializedName("completedOn")
    @Expose
    String completedOn;
    @SerializedName("isRewardGiven")
    @Expose
    String isRewardGiven;
    @SerializedName("rewardType")
    @Expose
    String rewardType;
    @SerializedName("rewardName")
    @Expose
    String rewardName;
    @SerializedName("rewardIcon")
    @Expose
    String rewardIcon;
    @SerializedName("header1")
    @Expose
    String header1;
    @SerializedName("header2")
    @Expose
    String header2;
    @SerializedName("transId")
    @Expose
    String transId;

    private SaveQuizathonAnsModel model;

    public SaveQuizathonAnsModel getModel() {
        model = new SaveQuizathonAnsModel();

        model.setActivityTransId("" + getTransId());
        model.setHowToDo(getHowToDo());
        model.setWhatToDo(getWhatToDo());
        model.setWhyToDo(getWhyToDo());
        model.setRewardName(getRewardName());
        model.setRewardType(getRewardType());
        model.setRewardDescription(null);
        model.setActivity(getActivity());
        model.setExpireOn(getExpireOn());
        model.setRedirectionKey(getRedirectionKey());
        model.setStarted(getIsStarted());

        return model;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getWhatToDo() {
        return whatToDo;
    }

    public void setWhatToDo(String whatToDo) {
        this.whatToDo = whatToDo;
    }

    public String getHowToDo() {
        return howToDo;
    }

    public void setHowToDo(String howToDo) {
        this.howToDo = howToDo;
    }

    public String getWhyToDo() {
        return whyToDo;
    }

    public void setWhyToDo(String whyToDo) {
        this.whyToDo = whyToDo;
    }

    public String getRedirectionKey() {
        return redirectionKey;
    }

    public void setRedirectionKey(String redirectionKey) {
        this.redirectionKey = redirectionKey;
    }

    public String getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(String expireOn) {
        this.expireOn = expireOn;
    }

    public boolean getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }

    public String getIsRewardGiven() {
        return isRewardGiven;
    }

    public void setIsRewardGiven(String isRewardGiven) {
        this.isRewardGiven = isRewardGiven;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardIcon() {
        return rewardIcon;
    }

    public void setRewardIcon(String rewardIcon) {
        this.rewardIcon = rewardIcon;
    }

    public String getHeader1() {
        return header1;
    }

    public void setHeader1(String header1) {
        this.header1 = header1;
    }

    public String getHeader2() {
        return header2;
    }

    public void setHeader2(String header2) {
        this.header2 = header2;
    }

    public boolean isShowActivity() {
        return showActivity;
    }

    public void setShowActivity(boolean showActivity) {
        this.showActivity = showActivity;
    }

    public String getFeedbackAnswerJson() {
        return feedbackAnswerJson;
    }

    public void setFeedbackAnswerJson(String feedbackAnswerJson) {
        this.feedbackAnswerJson = feedbackAnswerJson;
    }

    public Boolean getFeedbackTaken() {
        return isFeedbackTaken;
    }

    public void setFeedbackTaken(Boolean feedbackTaken) {
        isFeedbackTaken = feedbackTaken;
    }

    public String getFeedbackCardBG() {
        return feedbackCardBG;
    }

    public void setFeedbackCardBG(String feedbackCardBG) {
        this.feedbackCardBG = feedbackCardBG;
    }

    public String getFeedbackButtonText() {
        return feedbackButtonText;
    }

    public void setFeedbackButtonText(String feedbackButtonText) {
        this.feedbackButtonText = feedbackButtonText;
    }

    public String getFeedbackButtonColor() {
        return feedbackButtonColor;
    }

    public void setFeedbackButtonColor(String feedbackButtonColor) {
        this.feedbackButtonColor = feedbackButtonColor;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedbackTitle() {
        return feedbackTitle;
    }

    public void setFeedbackTitle(String feedbackTitle) {
        this.feedbackTitle = feedbackTitle;
    }

    public String getFeedbackDesc() {
        return feedbackDesc;
    }

    public void setFeedbackDesc(String feedbackDesc) {
        this.feedbackDesc = feedbackDesc;
    }

    public String getFeedbackStartDate() {
        return feedbackStartDate;
    }

    public void setFeedbackStartDate(String feedbackStartDate) {
        this.feedbackStartDate = feedbackStartDate;
    }

    public String getFeedbackLogo() {
        return feedbackLogo;
    }

    public void setFeedbackLogo(String feedbackLogo) {
        this.feedbackLogo = feedbackLogo;
    }

    public String getFeedbackEndDate() {
        return feedbackEndDate;
    }

    public void setFeedbackEndDate(String feedbackEndDate) {
        this.feedbackEndDate = feedbackEndDate;
    }

    public String getFeedbackQuestionJson() {
        return feedbackQuestionJson;
    }

    public void setFeedbackQuestionJson(String feedbackQuestionJson) {
        this.feedbackQuestionJson = feedbackQuestionJson;
    }

    public Boolean getIsFeedbackTaken() {
        return isFeedbackTaken;
    }

    public void setIsFeedbackTaken(Boolean isFeedbackTaken) {
        this.isFeedbackTaken = isFeedbackTaken;
    }

}
