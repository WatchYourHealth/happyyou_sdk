package com.wyh.happyyousdk.model.response.quizathon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.playwin.DialogModel;
import com.wyh.happyyousdk.model.response.playwin.StreakModel;

import java.io.Serializable;
import java.util.List;

public class SaveQuizathonAnsModel implements Serializable {
    @SerializedName("quizStreakModels")
    @Expose
    private List<StreakModel> streakModels;
    @SerializedName("withActivity")
    @Expose
    private Boolean withActivity;
    @SerializedName("showConsent")
    @Expose
    private Boolean showConsent;
    @SerializedName("isRetakeAvailable")
    @Expose
    private Boolean isRetakeAvailable;
    @SerializedName("hasSufficientBalance")
    @Expose
    private Boolean hasSufficientBalance;
    @SerializedName("retakePointstoBurn")
    @Expose
    private Integer retakePointstoBurn;
    @SerializedName("claimDate")
    @Expose
    private String claimDate;
    @SerializedName("activityTransId")
    @Expose
    private String activityTransId;
    @SerializedName("rewardType")
    @Expose
    private String rewardType;
    @SerializedName("rewardDescription")
    @Expose
    private String rewardDescription;
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
    @SerializedName("buttonText")
    @Expose
    private String buttonText;
    @SerializedName("isScratched")
    @Expose
    private boolean isScratched;
    @SerializedName("activity")
    @Expose
    private String activity;
    @SerializedName("isStarted")
    @Expose
    private boolean isStarted;
    @SerializedName("expireOn")
    @Expose
    private String expireOn;
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
    private String  completedOn;
    @SerializedName("activityImageUploaded")
    @Expose
    private boolean  activityImageUploaded = false;
    @SerializedName("imageStatus")
    @Expose
    private String  imageStatus;
    @SerializedName("isRewardGiven")
    @Expose
    private Boolean isRewardGiven;
    @SerializedName("dialogDetail")
    @Expose
    private DialogModel dialog_model;

    public Boolean getShowNextGameButton() {
        return showNextGameButton;
    }

    public void setShowNextGameButton(Boolean showNextGameButton) {
        this.showNextGameButton = showNextGameButton;
    }

    @SerializedName("showNextGameButton")
    @Expose
    private Boolean  showNextGameButton;

    public String getRedirectTo() {
        return nextGameRedirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.nextGameRedirectTo = redirectTo;
    }

    @SerializedName("nextGameRedirectTo")
    @Expose
    private String nextGameRedirectTo;
    public String getRewardIcon() {
        return rewardIcon;
    }

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

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public boolean isScratched() {
        return isScratched;
    }

    public void setScratched(boolean scratched) {
        isScratched = scratched;
    }

    public DialogModel getDialog_model() {
        return dialog_model;
    }

    public void setDialog_model(DialogModel dialog_model) {
        this.dialog_model = dialog_model;
    }

    public Boolean getWithActivity() {
        return withActivity;
    }

    public void setWithActivity(Boolean withActivity) {
        this.withActivity = withActivity;
    }

    public Boolean getRetakeAvailable() {
        return isRetakeAvailable != null ? isRetakeAvailable:false;
    }

    public void setRetakeAvailable(Boolean retakeAvailable) {
        isRetakeAvailable = retakeAvailable;
    }

    public Boolean getHasSufficientBalance() {
        return hasSufficientBalance != null ? hasSufficientBalance :false;
    }

    public void setHasSufficientBalance(Boolean hasSufficientBalance) {
        this.hasSufficientBalance = hasSufficientBalance;
    }

    public Integer getRetakePointstoBurn() {
        return retakePointstoBurn;
    }

    public void setRetakePointstoBurn(Integer retakePointstoBurn) {
        this.retakePointstoBurn = retakePointstoBurn;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public String getActivityTransId() {
        return activityTransId;
    }

    public void setActivityTransId(String activityTransId) {
        this.activityTransId = activityTransId;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardDescription() {
        return rewardDescription;
    }

    public void setRewardDescription(String rewardDescription) {
        this.rewardDescription = rewardDescription;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public String getExpireOn() {
        return expireOn;
    }

    public void setExpireOn(String expireOn) {
        this.expireOn = expireOn;
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

    public Boolean getRewardGiven() {
        return isRewardGiven;
    }

    public void setRewardGiven(Boolean rewardGiven) {
        isRewardGiven = rewardGiven;
    }

    public Boolean getShowConsent() {
        return showConsent;
    }

    public void setShowConsent(Boolean showConsent) {
        this.showConsent = showConsent;
    }
    public List<StreakModel> getStreakModels() {
        return streakModels;
    }

    public void setStreakModels(List<StreakModel> streakModels) {
        this.streakModels = streakModels;
    }
}
