package com.wyh.happyyousdk.dashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.playwin.DialogModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonModel;
import com.wyh.happyyousdk.model.response.quizathon.SaveQuizathonAnsModel;

import java.util.ArrayList;
import java.util.List;

public class GetBannerResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("success")
    @Expose
    private Boolean success;


    @SerializedName("data")
    @Expose
    private ArrayList<Data> data;

    @SerializedName("freevoucher")
    @Expose
    private String freevoucher;

    @SerializedName("isGoogleFit")
    @Expose
    private Boolean isGoogleFit;

    public ArrayList<Data> getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public Boolean getSuccess() {
        return success;
    }


    public String getFreevoucher() {
        return freevoucher;
    }

    public Boolean getIsGoogleFit() {
        return isGoogleFit;
    }

    public class Data {

        @SerializedName("bannerId")
        @Expose
        private String bannerId;
        @SerializedName("imageUrl")
        @Expose
        private String imageUrl;

        @SerializedName("disclaimer")
        @Expose
        private String disclaimer;

        @SerializedName("disclaimerImageUrl")
        @Expose
        private String disclaimerImageUrl;

        @SerializedName("redirectionUrl")
        @Expose
        private String redirectionUrl;

        @SerializedName("redirectionKey")
        @Expose
        private String redirectionKey;

        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("openInChrome")
        @Expose
        private Boolean openInChrome;

        @SerializedName("sequence")
        @Expose
        private int sequence;

        @SerializedName("happyMartKey")
        @Expose
        private String happyMartKey;

        @SerializedName("happyMartValue")
        @Expose
        private String happyMartValue;

        @SerializedName("jsonContent")
        @Expose
        private String bannerQuizModel;
        @SerializedName("isCompleted")
        @Expose
        private Boolean isCompleted;
        @SerializedName("rewardDate")
        @Expose
        private String rewardDate;
        @SerializedName("registrationData")
        @Expose
        private String registrationData;
        @SerializedName("isRegistered")
        @Expose
        private boolean isRegistered;
        @SerializedName("registrationID")
        @Expose
        private String registrationID;
        @SerializedName("dialogTitle")
        @Expose
        private String dialogTitle;
        @SerializedName("quizathonRegistered")
        @Expose
        private boolean quizathonRegistered=false;
        @SerializedName("quizathonQuestion")
        @Expose
        private String quizathonQuestion;
        @SerializedName("quizathonStarted")
        @Expose
        private boolean quizathonStarted;
        @SerializedName("quizathonRegistrationQuestion")
        @Expose
        private String quizathonRegistrationQuestion;
        @SerializedName("regTitle")
        @Expose
        private String regTitle;
        @SerializedName("regIcon")
        @Expose
        private String regIcon;
        @SerializedName("isQuizCompleted")
        @Expose
        private boolean isQuizCompleted;
        @SerializedName("quizathonId")
        @Expose
        private String quizathonId;
        @SerializedName("quizathonTitle")
        @Expose
        private String quizathonTitle;
        @SerializedName("quizathonRewardDate")
        @Expose
        private String quizathonRewardDate;
        @SerializedName("isRegistrationAllowed")
        @Expose
        private boolean isRegistrationAllowed ;
        @SerializedName("isRegistrationRequired")
        @Expose
        private boolean isRegistrationRequired ;
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
        @SerializedName("activityRedirectionKey")
        @Expose
        private String activityRedirectionKey;
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
        @SerializedName("quizathonTransId")
        @Expose
        private Integer quizathonTransId;
        @SerializedName("burnRegTitle")
        @Expose
        private String burnRegTitle;
        @SerializedName("burnRegIcon")
        @Expose
        private String burnRegIcon;
        @SerializedName("showConsent")
        @Expose
        private Boolean showConsent =false;
        @SerializedName("burnRegMessage")
        @Expose
        private String burnRegMessage;
        @SerializedName("quizBurnPoints")
        @Expose
        private String quizBurnPoints;
        @SerializedName("dialogDetail")
        @Expose
        private DialogModel dialogDetail;
        @SerializedName("isRetakeAvailable")
        @Expose
        private boolean isRetakeAvailable;
        @SerializedName("retakePointstoBurn")
        @Expose
        private int retakePointstoBurn;
        @SerializedName("retakeId")
        @Expose
        private int retakeId;
        @SerializedName("activityImageUploaded")
        @Expose
        private boolean activityImageUploaded;
        @SerializedName("messageNote")
        @Expose
        private String messageNote;

        public Boolean getShowConsent() {
            return showConsent;
        }

        public void setShowConsent(Boolean showConsent) {
            this.showConsent = showConsent;
        }

        public void setBannerId(String bannerId) {
            this.bannerId = bannerId;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setDisclaimer(String disclaimer) {
            this.disclaimer = disclaimer;
        }

        public void setDisclaimerImageUrl(String disclaimerImageUrl) {
            this.disclaimerImageUrl = disclaimerImageUrl;
        }

        public void setRedirectionUrl(String redirectionUrl) {
            this.redirectionUrl = redirectionUrl;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setOpenInChrome(Boolean openInChrome) {
            this.openInChrome = openInChrome;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public void setHappyMartKey(String happyMartKey) {
            this.happyMartKey = happyMartKey;
        }

        public void setHappyMartValue(String happyMartValue) {
            this.happyMartValue = happyMartValue;
        }

        public boolean isQuizathonRegistered() {
            return quizathonRegistered;
        }

        public boolean isQuizathonStarted() {
            return quizathonStarted;
        }

        public String getQuizBurnPoints() {
            return quizBurnPoints;
        }

        public void setQuizBurnPoints(String quizBurnPoints) {
            this.quizBurnPoints = quizBurnPoints;
        }

        public DialogModel getDialogDetail() {
            return dialogDetail;
        }

        public void setDialogDetail(DialogModel dialogDetail) {
            this.dialogDetail = dialogDetail;
        }

        public boolean isRetakeAvailable() {
            return isRetakeAvailable;
        }

        public void setRetakeAvailable(boolean retakeAvailable) {
            isRetakeAvailable = retakeAvailable;
        }

        public int getRetakePointstoBurn() {
            return retakePointstoBurn;
        }

        public void setRetakePointstoBurn(int retakePointstoBurn) {
            this.retakePointstoBurn = retakePointstoBurn;
        }

        public int getRetakeId() {
            return retakeId;
        }

        public void setRetakeId(int retakeId) {
            this.retakeId = retakeId;
        }

        public boolean isActivityImageUploaded() {
            return activityImageUploaded;
        }

        public void setActivityImageUploaded(boolean activityImageUploaded) {
            this.activityImageUploaded = activityImageUploaded;
        }

        public void setModel(SaveQuizathonAnsModel model) {
            this.model = model;
        }

        public String getNextGameRedirectTo() {
            return nextGameRedirectTo;
        }

        public void setNextGameRedirectTo(String nextGameRedirectTo) {
            this.nextGameRedirectTo = nextGameRedirectTo;
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

        private SaveQuizathonAnsModel model;

        public Boolean getShowActivity() {
            return showActivity;
        }

        public void setShowActivity(Boolean showActivity) {
            this.showActivity = showActivity;
        }

        public Integer getQuizathonTransId() {
            return quizathonTransId;
        }

        public void setQuizathonTransId(Integer quizathonTransId) {
            this.quizathonTransId = quizathonTransId;
        }

        public SaveQuizathonAnsModel getModel() {
            model = new SaveQuizathonAnsModel();

            model.setActivityTransId("" + getQuizathonTransId());
            model.setHowToDo(getHowToDo());
            model.setWhatToDo(getWhatToDo());
            model.setWhyToDo(getWhyToDo());
            model.setRewardName(getRewardName());
            model.setRewardType(getRewardType());
            model.setRewardDescription(null);
            model.setActivity(getActivity());
            model.setExpireOn(getExpireOn());
            model.setRedirectionKey(getActivityRedirectionKey());
            model.setStarted(getStarted());

            return model;
        }

        public QuizathonModel getQuizathonModel(){
            QuizathonModel quizathonModel=new QuizathonModel();
            quizathonModel.setTransId(getQuizathonTransId());
            quizathonModel.setRewardIcon(getRewardIcon());
            quizathonModel.setModel(getModel());
            quizathonModel.setShowActivity(getShowActivity());
            quizathonModel.setAnswerJson(getQuizathonQuestion());
            quizathonModel.setActivity(getActivity());
            quizathonModel.setExpireOn(getExpireOn());
            quizathonModel.setQuizTitle(getQuizathonTitle());
            quizathonModel.setQuizCategory("");
            quizathonModel.setWhyToDo(getWhyToDo());
            quizathonModel.setWhatToDo(getWhatToDo());
            quizathonModel.setHowToDo(getHowToDo());
            quizathonModel.setHeader1(getHeader1());
            quizathonModel.setHeader2(getHeader2());
            quizathonModel.setShowConsent(getShowConsent());
            quizathonModel.setDialog_model(getDialogDetail());
            quizathonModel.setRewardGiven(getRewardGiven());
            quizathonModel.setUserRegistered(getQuizathonRegistered());
            quizathonModel.setStarted(getQuizathonStarted());
            quizathonModel.setRewardType(getRewardType());
            quizathonModel.setMessageNote(getRewardType());
            quizathonModel.setRetakeId(""+getRetakeId());
            quizathonModel.setBurnRegIcon(getBurnRegIcon());
            quizathonModel.setBurnRegMessage(getBurnRegMessage());
            quizathonModel.setBurnRegMessage(getBurnRegMessage());
            quizathonModel.setIsRegistrationRequired(isRegistrationAllowed());

            return quizathonModel;
        }

        public String getActivityRedirectionKey() {
            return activityRedirectionKey;
        }

        public void setActivityRedirectionKey(String activityRedirectionKey) {
            this.activityRedirectionKey = activityRedirectionKey;
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

        public String getQuizRegistrationDate() {
            return quizRegistrationDate;
        }

        public void setQuizRegistrationDate(String quizRegistrationDate) {
            this.quizRegistrationDate = quizRegistrationDate;
        }

        @SerializedName("quizRegistrationDate")
        @Expose
        private String quizRegistrationDate;

        @SerializedName("showNextGameButton")
        @Expose
        private boolean showNextGameButton;

        public boolean isShowNextGameButton() {
            return showNextGameButton;
        }

        public void setShowNextGameButton(boolean showNextGameButton) {
            this.showNextGameButton = showNextGameButton;
        }

        public String getRedirectTo() {
            return nextGameRedirectTo;
        }

        public void setRedirectTo(String redirectTo) {
            this.nextGameRedirectTo = redirectTo;
        }

        @SerializedName("nextGameRedirectTo")
        @Expose
        private String nextGameRedirectTo;

        public String getQuizathonTitle() {
            return quizathonTitle;
        }

        public void setQuizathonTitle(String quizathonTitle) {
            this.quizathonTitle = quizathonTitle;
        }

        public boolean isQuizCompleted() {
            return isQuizCompleted;
        }

        public void setQuizCompleted(boolean quizCompleted) {
            isQuizCompleted = quizCompleted;
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

        public boolean getQuizathonRegistered() {
            return quizathonRegistered;
        }

        public void setQuizathonRegistered(boolean quizathonRegistered) {
            this.quizathonRegistered = quizathonRegistered;
        }

        public String getQuizathonQuestion() {
            return quizathonQuestion;
        }

        public void setQuizathonQuestion(String quizathonQuestion) {
            this.quizathonQuestion = quizathonQuestion;
        }

        public boolean getQuizathonStarted() {
            return quizathonStarted;
        }

        public void setQuizathonStarted(boolean quizathonStarted) {
            this.quizathonStarted = quizathonStarted;
        }

        public String getQuizathonRegistrationQuestion() {
            return quizathonRegistrationQuestion;
        }

        public void setQuizathonRegistrationQuestion(String quizathonRegistrationQuestion) {
            this.quizathonRegistrationQuestion = quizathonRegistrationQuestion;
        }

        public String getQuizathonId() {
            return quizathonId;
        }

        public void setQuizathonId(String quizathonId) {
            this.quizathonId = quizathonId;
        }

        public String getQuizathonRewardDate() {
            return quizathonRewardDate;
        }

        public void setQuizathonRewardDate(String quizathonRewardDate) {
            this.quizathonRewardDate = quizathonRewardDate;
        }

        public String getDialogTitle() {
            return dialogTitle;
        }

        public void setDialogTitle(String dialogTitle) {
            this.dialogTitle = dialogTitle;
        }

        public String getRegistrationID() {
            return registrationID;
        }

        public void setRegistrationID(String registrationID) {
            this.registrationID = registrationID;
        }

        public String getRegistrationData() {
            return registrationData;
        }

        public void setRegistrationData(String registrationData) {
            this.registrationData = registrationData;
        }

        public boolean isRegistered() {
            return isRegistered;
        }

        public void setRegistered(boolean registered) {
            isRegistered = registered;
        }

        public String getBannerId() {
            return bannerId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getDisclaimer() {
            return disclaimer;
        }

        public String getDisclaimerImageUrl() {
            return disclaimerImageUrl;
        }

        public String getRedirectionUrl() {
            return redirectionUrl;
        }

        public String getRedirectionKey() {
            return redirectionKey;
        }

        public String getTitle() {
            return title;
        }

        public Boolean getOpenInChrome() {
            return openInChrome;
        }

        public int getSequence() {
            return sequence;
        }

        public String getHappyMartKey() {
            return happyMartKey;
        }

        public String getHappyMartValue() {
            return happyMartValue;
        }

        public String getBannerQuizModel() {
            return bannerQuizModel;
        }

        public void setBannerQuizModel(String jsonContent) {
            this.bannerQuizModel = bannerQuizModel;
        }

        public Boolean getCompleted() {
            return isCompleted;
        }

        public void setCompleted(Boolean completed) {
            isCompleted = completed;
        }

        public String getRewardDate() {
            return rewardDate;
        }

        public void setRewardDate(String rewardDate) {
            this.rewardDate = rewardDate;
        }
    }
}
