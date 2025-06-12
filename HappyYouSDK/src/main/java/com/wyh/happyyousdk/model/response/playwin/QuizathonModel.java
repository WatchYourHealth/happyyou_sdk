package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.quizathon.SaveQuizathonAnsModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizathonModel implements Serializable {
    @SerializedName("transId")
    @Expose
    private Integer transId;
    @SerializedName("regId")
    @Expose
    private Integer regId;
    @SerializedName("quizId")
    @Expose
    private String quizId;
    @SerializedName("quizTitle")
    @Expose
    private String quizTitle;
    @SerializedName("quizDesc")
    @Expose
    private String quizDesc;
    @SerializedName("quizLogo")
    @Expose
    private String quizLogo;
    @SerializedName("registrationDate")
    @Expose
    private String registrationDate;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("isRegistrationRequired")
    @Expose
    private Boolean isRegistrationRequired;
    @SerializedName("registrationQuestions")
    @Expose
    private String registrationQuestions;
    @SerializedName("isRetakeAllowed")
    @Expose
    private Boolean isRetakeAllowed;
    @SerializedName("questionJson")
    @Expose
    private String questionJson;
    @SerializedName("answerJson")
    @Expose
    private String answerJson;
    @SerializedName("scratchCardColor")
    @Expose
    private String scratchCardColor;
    @SerializedName("rewardDate")
    @Expose
    private String rewardDate;
    @SerializedName("quizBgColor")
    @Expose
    private String quizBgColor;
    @SerializedName("quizCategory")
    @Expose
    private String quizCategory;
    @SerializedName("quizButtonText")
    @Expose
    private String quizButtonText;
    @SerializedName("quizButtonColor")
    @Expose
    private String quizButtonColor;
    @SerializedName("isUserRegistered")
    @Expose
    private Boolean isUserRegistered;
    @SerializedName("isQuizTaken")
    @Expose
    private Boolean isQuizTaken;
    @SerializedName("isQuizStarted")
    @Expose
    private Boolean isQuizStarted;
    @SerializedName("isRetakeAvailable")
    @Expose
    private Boolean isRetakeAvailable;
    @SerializedName("isRetake")
    @Expose
    private Boolean isRetake;
    @SerializedName("retakeId")
    @Expose
    private String retakeId;
    @SerializedName("retakePointstoBurn")
    @Expose
    private int retakePointstoBurn;
    @SerializedName("hasSufficientBalance")
    @Expose
    private boolean hasSufficientBalance;
    @SerializedName("userScore")
    @Expose
    private String userScore;
    @SerializedName("totalScore")
    @Expose
    private String totalScore;
    @SerializedName("messageNote")
    @Expose
    private String messageNote;
    @SerializedName("regTitle")
    @Expose
    private String regTitle;
    @SerializedName("regIcon")
    @Expose
    private String regIcon;
    @SerializedName("isRegistrationAllowed")
    @Expose
    private boolean isRegistrationAllowed;
    @SerializedName("registrationPointBurn")
    @Expose
    private int registrationPointBurn;

    @SerializedName("showActivity")
    @Expose
    private Boolean showActivity;
    @SerializedName("activity")
    @Expose
    private String activity;
    @SerializedName("whatToDo")
    @Expose
    private String whatToDo;
    @SerializedName("howToDo")
    @Expose
    private String howToDo;
    @SerializedName("whyToDo")
    @Expose
    private String whyToDo;
    @SerializedName("redirectionKey")
    @Expose
    private String redirectionKey;
    @SerializedName("expireOn")
    @Expose
    private String expireOn;
    @SerializedName("isStarted")
    @Expose
    private Boolean isStarted;
    @SerializedName("completedOn")
    @Expose
    private String completedOn;
    @SerializedName("isRewardGiven")
    @Expose
    private Boolean isRewardGiven;
    @SerializedName("rewardType")
    @Expose
    private String rewardType;
    @SerializedName("rewardName")
    @Expose
    private String rewardName;
    @SerializedName("rewardIcon")
    @Expose
    private String rewardIcon;
    @SerializedName("header1")
    @Expose
    private String header1;
    @SerializedName("header2")
    @Expose
    private String header2;

    @SerializedName("quizType")
    @Expose
    private String quizType;

    @SerializedName("quizGroup")
    @Expose
    private String quizGroup;

    @SerializedName("burnRegTitle")
    @Expose
    private String burnRegTitle;

    @SerializedName("burnRegIcon")
    @Expose
    private String burnRegIcon;

    @SerializedName("burnRegMessage")
    @Expose
    private String burnRegMessage;
    @SerializedName("showConsent")
    @Expose
    private Boolean showConsent =false;
    @SerializedName("buttonText")
    @Expose
    private String buttonText;
    @SerializedName("rewardDescription")
    @Expose
    private String rewardDescription;


    @SerializedName("activityImageUploaded")
    @Expose
    private boolean  activityImageUploaded = false;
    @SerializedName("imageStatus")
    @Expose
    private String  imageStatus;

    public boolean getActivityImageUploaded() {
        return activityImageUploaded;
    }

    public void setActivityImageUploaded(boolean activityImageUploaded) {
        this.activityImageUploaded = activityImageUploaded;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public void setModel(SaveQuizathonAnsModel model) {
        this.model = model;
    }

    public String getQuizBurnPoints() {
        return quizBurnPoints;
    }

    public void setQuizBurnPoints(String quizBurnPoints) {
        this.quizBurnPoints = quizBurnPoints;
    }

    @SerializedName("quizBurnPoints")
    @Expose
    private String quizBurnPoints;

    @SerializedName("quizStreakModels")
    @Expose
    private List<StreakModel> streakModels;

    public String getQuizGroup() {
        return quizGroup;
    }

    public void setQuizGroup(String quizGroup) {
        this.quizGroup = quizGroup;
    }

    public List<StreakModel> getStreakModels() {
        return streakModels;
    }

    public void setStreakModels(List<StreakModel> streakModels) {
        this.streakModels = streakModels;
    }

    public String getBurnRegTitle() {
        return burnRegTitle;
    }

    public void setBurnRegTitle(String burnRegTitle) {
        this.burnRegTitle = burnRegTitle;
    }

    public String getBurnRegIcon() {
        return burnRegIcon;
    }

    public void setBurnRegIcon(String burnRegIcon) {
        this.burnRegIcon = burnRegIcon;
    }

    public String getBurnRegMessage() {
        return burnRegMessage;
    }

    public void setBurnRegMessage(String burnRegMessage) {
        this.burnRegMessage = burnRegMessage;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

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
        model.setStarted(getStarted());

        return model;
    }

    public Integer getTransId() {
        return transId;
    }

    public void setTransId(Integer transId) {
        this.transId = transId;
    }

    public Integer getRegId() {
        return regId;
    }

    public void setRegId(Integer regId) {
        this.regId = regId;
    }

    public Boolean getShowActivity() {
        return showActivity;
    }

    public void setShowActivity(Boolean showActivity) {
        this.showActivity = showActivity;
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

    public Boolean getStarted() {
        return isStarted;
    }

    public void setStarted(Boolean started) {
        isStarted = started;
    }

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
    }

    public Boolean getRewardGiven() {
        return isRewardGiven;
    }

    public void setRewardGiven(Boolean rewardGiven) {
        isRewardGiven = rewardGiven;
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

    public boolean isRegistrationAllowed() {
        return isRegistrationAllowed;
    }

    public void setRegistrationAllowed(boolean registrationAllowed) {
        isRegistrationAllowed = registrationAllowed;
    }

    public int getRegistrationPointBurn() {
        return registrationPointBurn;
    }

    public void setRegistrationPointBurn(int registrationPointBurn) {
        this.registrationPointBurn = registrationPointBurn;
    }

    public boolean isShowNextGameButton() {
        return showNextGameButton;
    }

    public String getNextGameRedirectTo() {
        return nextGameRedirectTo;
    }

    public void setNextGameRedirectTo(String nextGameRedirectTo) {
        this.nextGameRedirectTo = nextGameRedirectTo;
    }

    public String getQuizRegistrationDate() {
        return quizRegistrationDate;
    }

    public void setQuizRegistrationDate(String quizRegistrationDate) {
        this.quizRegistrationDate = quizRegistrationDate;
    }

    @SerializedName("quizRegistrationDate")
    @Expose
    private String quizRegistrationDate;

    public boolean getShowNextGameButton() {
        return showNextGameButton;
    }

    public void setShowNextGameButton(boolean showNextGameButton) {
        this.showNextGameButton = showNextGameButton;
    }

    @SerializedName("showNextGameButton")
    @Expose
    private boolean showNextGameButton;

    public String getRedirectTo() {
        return nextGameRedirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.nextGameRedirectTo = redirectTo;
    }

    @SerializedName("nextGameRedirectTo")
    @Expose
    private String nextGameRedirectTo;

    public Boolean getRetake() {
        return isRetake;
    }

    public void setRetake(Boolean retake) {
        isRetake = retake;
    }

    public String getRetakeId() {
        return retakeId;
    }

    public void setRetakeId(String retakeId) {
        this.retakeId = retakeId;
    }

    public String getRegTitle() {
        return regTitle;
    }

    public void setRegTitle(String regTitle) {
        this.regTitle = regTitle;
    }

    public String getRegIcon() {
        return regIcon;
    }

    public void setRegIcon(String regIcon) {
        this.regIcon = regIcon;
    }

    public Boolean getQuizStarted() {
        return isQuizStarted;
    }

    public void setQuizStarted(Boolean quizStarted) {
        isQuizStarted = quizStarted;
    }

    public String getMessageNote() {
        return messageNote;
    }

    public void setMessageNote(String messageNote) {
        this.messageNote = messageNote;
    }

    public String getUserScore() {
        return userScore;
    }

    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    @SerializedName("dialogDetail")
    @Expose
    private DialogModel dialog_model;
    @SerializedName("isQuizCompleted")
    @Expose
    private boolean isQuizCompleted;

    public boolean isQuizCompleted() {
        return isQuizCompleted;
    }

    public void setQuizCompleted(boolean quizCompleted) {
        isQuizCompleted = quizCompleted;
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public Boolean getRegistrationRequired() {
        return isRegistrationRequired;
    }

    public void setRegistrationRequired(Boolean registrationRequired) {
        isRegistrationRequired = registrationRequired;
    }

    public Boolean getRetakeAllowed() {
        return isRetakeAllowed;
    }

    public void setRetakeAllowed(Boolean retakeAllowed) {
        isRetakeAllowed = retakeAllowed;
    }

    public Boolean getUserRegistered() {
        return isUserRegistered;
    }

    public void setUserRegistered(Boolean userRegistered) {
        isUserRegistered = userRegistered;
    }

    public Boolean getQuizTaken() {
        return isQuizTaken;
    }

    public void setQuizTaken(Boolean quizTaken) {
        isQuizTaken = quizTaken;
    }

    public Boolean getRetakeAvailable() {
        return isRetakeAvailable;
    }

    public void setRetakeAvailable(Boolean retakeAvailable) {
        isRetakeAvailable = retakeAvailable;
    }

    public int getRetakePointstoBurn() {
        return retakePointstoBurn;
    }

    public void setRetakePointstoBurn(int retakePointstoBurn) {
        this.retakePointstoBurn = retakePointstoBurn;
    }

    public boolean isHasSufficientBalance() {
        return hasSufficientBalance;
    }

    public void setHasSufficientBalance(boolean hasSufficientBalance) {
        this.hasSufficientBalance = hasSufficientBalance;
    }

    public DialogModel getDialog_model() {
        return dialog_model;
    }

    public void setDialog_model(DialogModel dialog_model) {
        this.dialog_model = dialog_model;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public String getQuizDesc() {
        return quizDesc;
    }

    public void setQuizDesc(String quizDesc) {
        this.quizDesc = quizDesc;
    }

    public String getQuizLogo() {
        return quizLogo;
    }

    public void setQuizLogo(String quizLogo) {
        this.quizLogo = quizLogo;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsRegistrationRequired() {
        return isRegistrationRequired;
    }

    public void setIsRegistrationRequired(Boolean isRegistrationRequired) {
        this.isRegistrationRequired = isRegistrationRequired;
    }

    public String getRegistrationQuestions() {
        return registrationQuestions;
    }

    public void setRegistrationQuestions(String registrationQuestions) {
        this.registrationQuestions = registrationQuestions;
    }

    public Boolean getIsRetakeAllowed() {
        return isRetakeAllowed;
    }

    public void setIsRetakeAllowed(Boolean isRetakeAllowed) {
        this.isRetakeAllowed = isRetakeAllowed;
    }

    public String getQuestionJson() {
        return questionJson;
    }

    public void setQuestionJson(String questionJson) {
        this.questionJson = questionJson;
    }

    public String getScratchCardColor() {
        return scratchCardColor;
    }

    public void setScratchCardColor(String scratchCardColor) {
        this.scratchCardColor = scratchCardColor;
    }

    public String getRewardDate() {
        return rewardDate;
    }

    public void setRewardDate(String rewardDate) {
        this.rewardDate = rewardDate;
    }

    public String getQuizBgColor() {
        return quizBgColor;
    }

    public void setQuizBgColor(String quizBgColor) {
        this.quizBgColor = quizBgColor;
    }

    public String getQuizCategory() {
        return quizCategory;
    }

    public void setQuizCategory(String quizCategory) {
        this.quizCategory = quizCategory;
    }

    public String getQuizButtonText() {
        return quizButtonText;
    }

    public void setQuizButtonText(String quizButtonText) {
        this.quizButtonText = quizButtonText;
    }

    public String getQuizButtonColor() {
        return quizButtonColor;
    }

    public void setQuizButtonColor(String quizButtonColor) {
        this.quizButtonColor = quizButtonColor;
    }

    public Boolean getIsUserRegistered() {
        return isUserRegistered;
    }

    public void setIsUserRegistered(Boolean isUserRegistered) {
        this.isUserRegistered = isUserRegistered;
    }

    public Boolean getIsQuizTaken() {
        return isQuizTaken;
    }

    public void setIsQuizTaken(Boolean isQuizTaken) {
        this.isQuizTaken = isQuizTaken;
    }

    public Boolean getShowConsent() {
        return showConsent;
    }

    public void setShowConsent(Boolean showConsent) {
        this.showConsent = showConsent;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getRewardDescription() {
        return rewardDescription;
    }

    public void setRewardDescription(String rewardDescription) {
        this.rewardDescription = rewardDescription;
    }
}
