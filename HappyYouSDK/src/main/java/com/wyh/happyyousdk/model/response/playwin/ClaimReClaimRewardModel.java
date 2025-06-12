package com.wyh.happyyousdk.model.response.playwin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.postloginreward.RewardItem;

import java.io.Serializable;

public class ClaimReClaimRewardModel implements Serializable {

    @SerializedName("rewardType")
    @Expose
    private String rewardType;
    @SerializedName("rewardTitle")
    @Expose
    private String rewardTitle;
    @SerializedName("rewardTitleIcon")
    @Expose
    private String rewardTitleIcon;
    @SerializedName("rewardDescription")
    @Expose
    private String rewardDescription;
    @SerializedName("redeemMessage")
    @Expose
    private String redeemMessage;
    @SerializedName("rewardValue")
    @Expose
    private Integer rewardValue;
    @SerializedName("hyCode")
    @Expose
    private String hyCode;
    @SerializedName("couponCode")
    @Expose
    private String couponCode;
    @SerializedName("partnerName")
    @Expose
    private String partnerName;
    @SerializedName("expiryInHours")
    @Expose
    private String expiryInHours;
    @SerializedName("transId")
    @Expose
    private String transId;
    @SerializedName("isClaim")
    @Expose
    private boolean isClaim;
    @SerializedName("isReclaim")
    @Expose
    private boolean isReclaim;
    @SerializedName("reclaimBurnValue")
    @Expose
    private int reclaimBurnValue;
    @SerializedName("expireOn")
    @Expose
    private String expireOn;
    @SerializedName("quizTitle")
    @Expose
    private String quizTitle;


    // String fields for textual or date-related values
    @SerializedName("quizType")
    @Expose
    private String quizType;
    @SerializedName("quizGroup")
    @Expose
    private String quizGroup;
    @SerializedName("registrationDate")
    @Expose
    private String registrationDate;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("registrationQuestions")
    @Expose
    private String registrationQuestions;
    @SerializedName("regTitle")
    @Expose
    private String regTitle;
    @SerializedName("regIcon")
    @Expose
    private String regIcon;
    @SerializedName("questionJson")
    @Expose
    private String questionJson;
    @SerializedName("answerJson")
    @Expose
    private String answerJson;
    @SerializedName("messageNote")
    @Expose
    private String messageNote;
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
    @SerializedName("nextGameRedirectTo")
    @Expose
    private String nextGameRedirectTo;
    @SerializedName("retakeId")
    @Expose
    private String retakeId;
    @SerializedName("dialogDetail")
    @Expose
    private String dialogDetail;
    @SerializedName("quizRegistrationDate")
    @Expose
    private String quizRegistrationDate;
    @SerializedName("redirectTo")
    @Expose
    private String redirectTo;
    @SerializedName("burnRegTitle")
    @Expose
    private String burnRegTitle;
    @SerializedName("burnRegIcon")
    @Expose
    private String burnRegIcon;
    @SerializedName("burnRegMessage")
    @Expose
    private String burnRegMessage;
    @SerializedName("claimReclaimList")
    @Expose
    private String claimReclaimList;
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
    @SerializedName("completedOn")
    @Expose
    private String completedOn;
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
    @SerializedName("imageStatus")
    @Expose
    private String imageStatus;

    // Numeric fields - using int or double as appropriate
    @SerializedName("regId")
    @Expose
    private int regId;
    @SerializedName("quizId")
    @Expose
    private int quizId;
    @SerializedName("userScore")
    @Expose
    private int userScore;
    @SerializedName("totalScore")
    @Expose
    private int totalScore;
    @SerializedName("registrationPointBurn")
    @Expose
    private int registrationPointBurn;

    @SerializedName("retakePointstoBurn")
    @Expose
    private int retakePointstoBurn;

    // Boolean fields for flags
    @SerializedName("isRegistrationRequired")
    @Expose
    private boolean isRegistrationRequired;
    @SerializedName("isUserRegistered")
    @Expose
    private boolean isUserRegistered;
    @SerializedName("isQuizTaken")
    @Expose
    private boolean isQuizTaken;
    @SerializedName("isQuizCompleted")
    @Expose
    private boolean isQuizCompleted;
    @SerializedName("isQuizStarted")
    @Expose
    private boolean isQuizStarted;
    @SerializedName("showNextGameButton")
    @Expose
    private boolean showNextGameButton;
    @SerializedName("isRetake")
    @Expose
    private boolean isRetake;
    @SerializedName("isRetakeAvailable")
    @Expose
    private boolean isRetakeAvailable;
    @SerializedName("hasSufficientBalance")
    @Expose
    private boolean hasSufficientBalance;
    @SerializedName("isScratched")
    @Expose
    private boolean isScratched;
    @SerializedName("isRegistrationAllowed")
    @Expose
    private boolean isRegistrationAllowed;
    @SerializedName("showActivity")
    @Expose
    private boolean showActivity;
    @SerializedName("isStarted")
    @Expose
    private boolean isStarted;
    @SerializedName("isRewardGiven")
    @Expose
    private boolean isRewardGiven;
    @SerializedName("activityImageUploaded")
    @Expose
    private boolean activityImageUploaded = false;


    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public String getQuizGroup() {
        return quizGroup;
    }

    public void setQuizGroup(String quizGroup) {
        this.quizGroup = quizGroup;
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

    public String getRegistrationQuestions() {
        return registrationQuestions;
    }

    public void setRegistrationQuestions(String registrationQuestions) {
        this.registrationQuestions = registrationQuestions;
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

    public String getQuestionJson() {
        return questionJson;
    }

    public void setQuestionJson(String questionJson) {
        this.questionJson = questionJson;
    }

    public String getAnswerJson() {
        return answerJson;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
    }

    public String getMessageNote() {
        return messageNote;
    }

    public void setMessageNote(String messageNote) {
        this.messageNote = messageNote;
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

    public String getNextGameRedirectTo() {
        return nextGameRedirectTo;
    }

    public void setNextGameRedirectTo(String nextGameRedirectTo) {
        this.nextGameRedirectTo = nextGameRedirectTo;
    }

    public String getRetakeId() {
        return retakeId;
    }

    public void setRetakeId(String retakeId) {
        this.retakeId = retakeId;
    }

    public String getDialogDetail() {
        return dialogDetail;
    }

    public void setDialogDetail(String dialogDetail) {
        this.dialogDetail = dialogDetail;
    }

    public String getQuizRegistrationDate() {
        return quizRegistrationDate;
    }

    public void setQuizRegistrationDate(String quizRegistrationDate) {
        this.quizRegistrationDate = quizRegistrationDate;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
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

    public String getClaimReclaimList() {
        return claimReclaimList;
    }

    public void setClaimReclaimList(String claimReclaimList) {
        this.claimReclaimList = claimReclaimList;
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

    public String getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(String completedOn) {
        this.completedOn = completedOn;
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

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public int getRegId() {
        return regId;
    }

    public void setRegId(int regId) {
        this.regId = regId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getRegistrationPointBurn() {
        return registrationPointBurn;
    }

    public void setRegistrationPointBurn(int registrationPointBurn) {
        this.registrationPointBurn = registrationPointBurn;
    }

    public int getRetakePointstoBurn() {
        return retakePointstoBurn;
    }

    public void setRetakePointstoBurn(int retakePointstoBurn) {
        this.retakePointstoBurn = retakePointstoBurn;
    }

    public boolean isRegistrationRequired() {
        return isRegistrationRequired;
    }

    public void setRegistrationRequired(boolean registrationRequired) {
        isRegistrationRequired = registrationRequired;
    }

    public boolean isUserRegistered() {
        return isUserRegistered;
    }

    public void setUserRegistered(boolean userRegistered) {
        isUserRegistered = userRegistered;
    }

    public boolean isQuizTaken() {
        return isQuizTaken;
    }

    public void setQuizTaken(boolean quizTaken) {
        isQuizTaken = quizTaken;
    }

    public boolean isQuizCompleted() {
        return isQuizCompleted;
    }

    public void setQuizCompleted(boolean quizCompleted) {
        isQuizCompleted = quizCompleted;
    }

    public boolean isQuizStarted() {
        return isQuizStarted;
    }

    public void setQuizStarted(boolean quizStarted) {
        isQuizStarted = quizStarted;
    }

    public boolean isShowNextGameButton() {
        return showNextGameButton;
    }

    public void setShowNextGameButton(boolean showNextGameButton) {
        this.showNextGameButton = showNextGameButton;
    }

    public boolean isRetake() {
        return isRetake;
    }

    public void setRetake(boolean retake) {
        isRetake = retake;
    }

    public boolean isRetakeAvailable() {
        return isRetakeAvailable;
    }

    public void setRetakeAvailable(boolean retakeAvailable) {
        isRetakeAvailable = retakeAvailable;
    }

    public boolean isHasSufficientBalance() {
        return hasSufficientBalance;
    }

    public void setHasSufficientBalance(boolean hasSufficientBalance) {
        this.hasSufficientBalance = hasSufficientBalance;
    }

    public boolean isScratched() {
        return isScratched;
    }

    public void setScratched(boolean scratched) {
        isScratched = scratched;
    }

    public boolean isRegistrationAllowed() {
        return isRegistrationAllowed;
    }

    public void setRegistrationAllowed(boolean registrationAllowed) {
        isRegistrationAllowed = registrationAllowed;
    }

    public boolean isShowActivity() {
        return showActivity;
    }

    public void setShowActivity(boolean showActivity) {
        this.showActivity = showActivity;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isRewardGiven() {
        return isRewardGiven;
    }

    public void setRewardGiven(boolean rewardGiven) {
        isRewardGiven = rewardGiven;
    }

    public boolean isActivityImageUploaded() {
        return activityImageUploaded;
    }

    public void setActivityImageUploaded(boolean activityImageUploaded) {
        this.activityImageUploaded = activityImageUploaded;
    }

    public String getQuizDesc() {
        return quizDesc;
    }

    public void setQuizDesc(String quizDesc) {
        this.quizDesc = quizDesc;
    }

    @SerializedName("quizDesc")
    @Expose
    private String quizDesc;

    public String getQuizLogo() {
        return quizLogo;
    }

    public void setQuizLogo(String quizLogo) {
        this.quizLogo = quizLogo;
    }

    @SerializedName("quizLogo")
    @Expose
    private String quizLogo;

    public String getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(String expireOn) {
        this.expireOn = expireOn;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public int getReclaimBurnValue() {
        return reclaimBurnValue;
    }

    public void setReclaimBurnValue(int reclaimBurnValue) {
        this.reclaimBurnValue = reclaimBurnValue;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardTitle() {
        return rewardTitle;
    }

    public void setRewardTitle(String rewardTitle) {
        this.rewardTitle = rewardTitle;
    }

    public String getRewardTitleIcon() {
        return rewardTitleIcon;
    }

    public void setRewardTitleIcon(String rewardTitleIcon) {
        this.rewardTitleIcon = rewardTitleIcon;
    }

    public String getRewardDescription() {
        return rewardDescription;
    }

    public void setRewardDescription(String rewardDescription) {
        this.rewardDescription = rewardDescription;
    }

    public String getRedeemMessage() {
        return redeemMessage;
    }

    public void setRedeemMessage(String redeemMessage) {
        this.redeemMessage = redeemMessage;
    }

    public Integer getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(Integer rewardValue) {
        this.rewardValue = rewardValue;
    }

    public String getHyCode() {
        return hyCode;
    }

    public void setHyCode(String hyCode) {
        this.hyCode = hyCode;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getExpiryInHours() {
        return expiryInHours;
    }

    public void setExpiryInHours(String expiryInHours) {
        this.expiryInHours = expiryInHours;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public boolean isClaim() {
        return isClaim;
    }

    public void setClaim(boolean claim) {
        isClaim = claim;
    }

    public boolean isReclaim() {
        return isReclaim;
    }

    public void setReclaim(boolean reclaim) {
        isReclaim = reclaim;
    }

    public RewardItem getRewardItem() {
        RewardItem rewardItem = new RewardItem();

        rewardItem.setActivityTransId(Integer.parseInt(getTransId()));
        rewardItem.setRewardType(getRewardType());
        rewardItem.setRewardDescription(getRewardDescription());
        rewardItem.setRewardName(getRewardName());
        rewardItem.setActivity(getActivity());
        rewardItem.setIsStarted(isStarted());
        rewardItem.setExpireOn(getExpireOn());
        rewardItem.setWhatToDo(getWhatToDo());
        rewardItem.setHowToDo(getHowToDo());
        rewardItem.setWhyToDo(getWhyToDo());
        rewardItem.setRedirectionKey(getRedirectionKey());
        rewardItem.setActivityTitle(getRewardTitle());
        rewardItem.setActivityDecription(getRewardDescription());
        rewardItem.setStartedDate(getStartDate());
        rewardItem.setCompletedOn(getCompletedOn());
        rewardItem.setRewardGiven(isRewardGiven());
        rewardItem.setWithoutActivity(isShowActivity());
        rewardItem.setCompleted(isQuizCompleted());

        return rewardItem;
    }
}
