package com.wyh.happyyousdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RadioButton;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyhsdk.utils.Constants;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Random;
import java.util.UUID;

public class SharedPref {
    private static SharedPreferences mSharedPref;
    private static final boolean isSharedPrefEncrypted = false; //To enable decrypted shared preference set this key to false
    public static final String NDHMAccessToken = "nDHMAccessToken";
    public static final String NDHMUserToken = "nDHMUserToken";
    public static final String NDHMUserRefreshToken = "NDHMUserRefreshToken";
    public static final String IsCameFromLogin = "isCameFromLogin";

    //User
    public static final String IsLoggedIn = "isLoggedIn";
    public static final String UUID = "Uuid";
    public static final String AES_UUID = "Aes_UUID";
    public static final String ConversationID = "ConversationID";
    public static final String HeartAgeConversationID = "HeartAgeConversationID";
    public static final String UserName = "userName";

    public static final String ISPROFILEDETAILSTORED = "isProfileDetailedStore";

    public static final String AuthToken = "authToken";
    public static final String KgiAuthToken = "kgiAuthToken";
    public static final String KgiUuid = "KgiUuid";

    public static final String isFromKgi = "isFromKgi";
    public static final String IsModifiedAuthToken = "sjdhfgjsd";

    public static final String ABHAAuthToken = "abhaAuthToken";

    public static final String ABHAUSERAuthToken = "abhaUserAuthToken";
    public static final String MobileVerified = "MobileVerified";
    public static final String isAbhaAddressSelected = "isAbhaAddressSelected";

    public static final String ABHAADDRESS = "abhaAddress";

    public static final String TOTALPOINTS = "totalPoints";
    public static final String CONNECTION_COUNT = "connectionCount";
    public static final String STAMPS = "stamps";


    public static final String abhaPhrAddress = "abhaPhrAddress";

    public static final String CHALLENGE_COUNT = "challengeCount";

    public static final String CHALLENGE_ID = "challengeID";

    public static final String POSITION_ID = "positionID";

    public static final String WEIGHT = "weight";


    public static final String Email = "email";
    public static final String MobileNo = "mobileNo";
    public static final String KgiMobileNo = "kgiMobileNo";
    public static final String DecryptMobileNo = "decryptMobileNo";
    public static final String EncryptedMobileNo = "encryptedMobileNo";
    public static final String DOB = "dob";
    public static final String Gender = "gender";

    public static final String REGISTRATION_GENDER = "userGender";
    public static final String ProfilePicPath = "profilePicPath";
    public static final String HRAAnalysis = "HRAAnalysis";
    public static final String HeartAgeAnalysis = "HeartAgeAnalysis";
    public static final String DASSAnalysis = "DASSAnalysis";
    public static final String UserCommunities = "userCommunities";
    public static final String ReferredBy = "referredBy";

    public static final String redirectionKey = "redirectionKey";
    public static final String ReferredByID = "referredByID";
    public static final String ReferredFor = "referredFor";
    public static final String ReferredComId = "referredComId";
    public static final String ReferredComName = "referredComName";
    public static final String ReferredFromHRA = "referredFromHRA";
    public static final String UserVitals = "userVitals";
    public static final String IsScratchedFirstCard = "isScratchedFirstCard";
    public static final String UserVitalsScannedOn = "userVitalsScannedOn";

    public static final String RefferralCode = "referralCode";

    public static final String COP_RefferralCode = "coopreferralCode";

    public static final String SondeAuthToken = "sondeAuthToken";

    public static final String SondeUserIdentifier = "sondeUserIdentifier";

    public static final String SondeUserRegistered = "sondeUserRegister";

    public static final String SondeAudioURL = "sondeAudioUrl";

    public static final String WELLNESS_TOPIC = "wellnessTopic";

    public static final String SondeID = "sondeFirstJobID";

    public static final String WATER_INTAKE_ALLOWED = "waterInatekAllowed";

    public static final String showCaloriesFeedback = "ShowCaloriesFeedback";

    public static final String SHOW_FEEDBACK_POPUP = "showFeedBackPopup";

    public static final String SondeFilePath = "sondeFilePath";

    public static final String isUserNameRegistered = "isUserNameRegistered";

    public static final String MentalWellnessFilePath = "mentalWellnessFilePath";

    public static final String RESPIRATORY_COUNT = "respiratoryCount";

    public static final String DIARY_CONCERN = "diaryConcern";

    public static final String JOKE_NOTIFICATION = "jokesNotification";

    public static final String TransactionID = "TransactionID";
    public static final String UserLatestReminder = "userLatestReminder";
    public static final String SplashScreenShowedOn = "splashScreenShowedOn";
    public static final String GFitScreenShowedOn = "gFitScreenShowedOn";
    public static final String DashboardData = "dashboardData";
    public static final String BMR = "bmr";
    public static final String UserAge = "userAge";
    public static final String UserGender = "userGender";
    public static final String UserHeight = "userHeight";
    public static final String UserWeight = "userWeight";
    public static final String UserHeightFt = "UserHeightFt";

    //HRA
    public static final String HRAJson = "HRAJson";
    public static final String HealthScoreResponseNew = "HealthScoreResponseNew";
    public static final String IsBodyProfileHRADone = "isBodyProfileHRADone";
    public static final String IsActivityHRADone = "isActivityHRADone";
    public static final String IsNutritionHRADone = "isNutritionHRADone";
    public static final String IsMentalWellnessHRADone = "isMentalWellnessHRADone";
    public static final String IsLifestyleHRADone = "isLifestyleHRADone";
    public static final String IsNewHRACompleted = "isNewHRACompleted";
    public static final String NumberOfHRADone = "numberOfHRADone";
    public static final String BMIFromNewHRA = "BMIFromNewHRA";
    public static final String latestWeightValue = "latest_weight_value";
    public static final String IRAconversationId = "IRAconversationId";
    public static final String conversationId = "conversationId";

    //Connectivity
    private static final String FIT_BIT_CONNECTION = "fitBitConnection";
    public static final String TodaySteps = "todaySteps";
    public static final String TodaySleep = "todaySleep";
    public static final String EmergencyContact = "emergencyContact";

    //IRA
    public static final String IRAHealthData = "IRAHealthData";
    public static final String PolicyDetails = "PolicyDetails";
    public static final String KGIPolicyDetails = "KGIPolicyDetails";
    public static final String KGIPolicyVASType = "KGIPolicyVASType";
    public static final String CommunityMembers = "CommunityMembers";


    public static final String EhrUploadedResponse = "EhrUploadedResponse";

    public static final String PushNotificationToken = "pushNotificationToken";

    public static final String FirstTimeValuesInTopics = "firstTimeValuesInTopics";
    public static final String RiddleAns = "RiddleAnswer";
    public static final String RiddleAnswers = "RiddleAnswers";
    public static final String RiddleDate = "RiddleDate";
    public static final String WaterGoals = "WaterGoals";
    public static final String FootprintGoals = "FootprintGoals";
    public static final String UserSelectedTribes = "UserSelectedTribes";
    public static final String StepsGoals = "StepsGoals";
    public static final String IsFirstInstall = "isFirstInstall";
    public static final String CurrentLevel = "currentLevel";

    public static final String IsHappyMartIntroShown = "isHappyMartIntroShown";
    public static final String IsHappyYouIntroShown = "isHappyYouIntroShown";
    public static final String IsTribeIntroShown = "isTribeIntroShown";
    public static final String IsWinningsIntroShown = "isWinningsIntroShown";
    public static final String IsWellbeingIntroShown = "isWellbeingIntroShown";

    public static final String WellBeingIntroShownHealth = "WellBeingIntroShownHealth";
    public static final String WellBeingIntroShownImmunity = "WellBeingIntroShownImmunity";
    public static final String WellBeingIntroShownDAS = "WellBeingIntroShownDAS";
    public static final String WellBeingIntroShownVitals = "WellBeingIntroShownVitals";
    public static final String WellBeingIntroShownHeartAge = "WellBeingIntroShownHeartAge";

    public static final String FACE_SCAN_ID = "FaceScanID";

    public static final String StampHistory = "StampHistory";
    public static final String CurrentTimeStamp = "CurrentTimeStamp";
    public static final String CurrentTimeStampExpires = "CurrentTimeStampExpires";
    public static final String PointsHistory = "PointsHistory";
    public static final String UserCalorieBurned = "UserCalorieBurned";

    public static final String UserAllWidget = "UserAllWidget";
    public static final String HomeBottomCard = "HomeBottomCard";
    public static final String NewUser = "NewUser";
    public static final String NDHMProfileInfo = "NDHMProfileInfo";
    public static final String CommunityPreference = "CommunityPreference";
    public static final String AddFamilyMember = "AddFamilyMember";
    public static final String UserWaist = "UserWaist";

    public static final String isGoogleFitActive = "googleFitCheck";

    public static final String isChallengeStarted = "isChallengeStarted";
    public static final String DashboardResponseNew = "dashboardResponseNew";

    public static final String UserDetailResponseNew = "userDetailResponseNew";

    public static final String DashBoardDatResponseNew = "dashboardDataResponseNew";

    public static final String isHobbyTribe = "isHobbyTribe";
    public static final String hobbyTribeURL = "hobbyTribeURL";
    public static final String hobbyTribeLogo = "hobbyTribeLogo";

    public static final String tempUUID = "TempUUID";

    public static final String isWheelSpinned = "isWheelSpinned";
    public static final String isWheelTimesUp = "isWheelTimesUp";

    public static final String webinarStatus = "Webinar";

    public static Context mContext;

    public static String rewardTiwer = "rewardTimer";
    public static String isOldUserPopUpShown = "OldUserPopup";
    public static String isShowSpin = "showSpin";
    public static String sessionStartTime = "sessionStartTime";
    public static String sessionEndTime = "sessionEndTime";
    public static String PolicyMPin = "PolicyMPin";
    public static String hasBiometric = "hasBiometric";
    public static String policyContent = "policyContent";
    public static String clientId = "clientId";
    public static String clientsPolicyData = "clientsPolicyData";
    public static String corporateRegistered = "corporateRegistered";
    public static String corporateName = "corporateName";
    public static String corporateId = "corporateId";
    public static String corporateImage = "corporateImage";
    public static String CorporateAccountNotFound = "CorporateAccountNotFound";
    public static String marshURL = "marshURL";
    public static String policyRenewableURL = "policyRenewableURL";
    private static final String LAST_DIALOG_DATE_KEY = "lastDialogDate";

    public static void init(Context context) {
        mContext = context;
        if (mSharedPref == null) {
            try {
                if (isSharedPrefEncrypted) {
                    MasterKey masterKey = new MasterKey.Builder(context, context.getPackageName())
                            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                            .build();
                    mSharedPref = EncryptedSharedPreferences.create(context, context.getPackageName(), masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
                } else {
                    mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
                }
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void setLastLoginDate(long lastLoginDate) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putLong(LAST_DIALOG_DATE_KEY, lastLoginDate);
        editor.commit();
    }

    public static long getLastLogindate() {
        return mSharedPref.getLong(LAST_DIALOG_DATE_KEY, 0);
    }

    public static void putConversationID(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ConversationID, value);
        editor.apply();
    }

    public static String getConversationID() {
        return mSharedPref.getString(ConversationID, "");
    }

    public static void putHeartAgeConversationID(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(HeartAgeConversationID, value);
        editor.apply();
    }

    public static String getHeartAgeConversationID() {
        return mSharedPref.getString(HeartAgeConversationID, "");
    }

    public static void putUserName(String userName) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserName, userName);
        editor.apply();
    }

    public static String getUserName() {
        return mSharedPref.getString(UserName, "");
    }

    public static void putProfileDetailedValued(Boolean userName) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(ISPROFILEDETAILSTORED, userName);
        editor.apply();
    }

    public static Boolean getProfileDetailedValued() {
        return mSharedPref.getBoolean(ISPROFILEDETAILSTORED, false);
    }

    public static void setShowSpin(Boolean userName) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(isShowSpin, userName);
        editor.apply();
    }

    public static Boolean getShowSpin() {
        return mSharedPref.getBoolean(isShowSpin, false);
    }

    public static void putPushNotificationToken(String token) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PushNotificationToken, token);
        editor.apply();
    }

    public static String getPushNotificationToken() {
        return mSharedPref.getString(PushNotificationToken, "");
    }

    public static void putDashboardResponseNew(String dashboardResponseNew) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(DashboardResponseNew, dashboardResponseNew);
        editor.apply();
    }

    public static String getDashboardResponseNew() {
        return mSharedPref.getString(DashboardResponseNew, "");
    }

    public static void putUserDetailsResponse(String userDetailResponse) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserDetailResponseNew, userDetailResponse);
        editor.apply();
    }

    public static String getUserDetailResponse() {
        return mSharedPref.getString(UserDetailResponseNew, "");
    }

    public static void putDashboardResponse(String dashboardData) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(DashBoardDatResponseNew, dashboardData);
        editor.apply();
    }

    public static String getDashboardResponse() {
        return mSharedPref.getString(DashBoardDatResponseNew, "");

    }

    public static void putIsModifiedAuthToken(boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsModifiedAuthToken, value);
        editor.apply();
    }

    public static boolean getIsModifiedAuthToken() {
        return mSharedPref.getBoolean(IsModifiedAuthToken, false);
    }

    public static void putAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        String newSub = Constants.getSaltString();
        int index = 6;
        StringBuilder resString = new StringBuilder(authToken);
        resString.insert(index + 1, newSub);
        editor.putString(AuthToken, resString.toString());
        editor.apply();
        putIsModifiedAuthToken(true);
    }

    public static String getAuthToken() {
        if (!getIsModifiedAuthToken()) {
            return mSharedPref.getString(AuthToken, "");
        } else {
            String authToken = mSharedPref.getString(AuthToken, "");
            return authToken.equalsIgnoreCase("") ? authToken : (new StringBuffer(authToken)).replace(7, 15, "").toString();
        }
    }

    public static void putAbhaToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ABHAAuthToken, authToken);
        editor.apply();
    }

    public static void putPhrAddress(String address) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(abhaPhrAddress, address);
        editor.apply();
    }

    public static String getPhrAddress() {
        return mSharedPref.getString(abhaPhrAddress, "");
    }

    public static String getAbhaToken() {
        return mSharedPref.getString(ABHAAuthToken, "");
    }

    public static void putAbhaUserToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ABHAUSERAuthToken, authToken);
        editor.apply();
    }

    public static String getAbhaUserToken() {
        return mSharedPref.getString(ABHAUSERAuthToken, "");
    }

    public static void putMobileVerified(boolean status) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(MobileVerified, status);
        editor.apply();
    }

    public static boolean getMobileVerified() {
        return mSharedPref.getBoolean(MobileVerified, false);
    }

    public static void putAbhaAddressSelected(boolean status) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(isAbhaAddressSelected, status);
        editor.apply();
    }

    public static boolean putAbhaAddressSelected() {
        return mSharedPref.getBoolean(isAbhaAddressSelected, false);
    }

    public static void putAbhaAddress(String authToken) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ABHAADDRESS, authToken);
        editor.apply();
    }

    public static String getAbhaAddress() {
        return mSharedPref.getString(ABHAADDRESS, "");
    }

    public static void putKgiAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(KgiAuthToken, authToken);
        editor.apply();
    }

    public static String getKgiAuthToken() {
        return mSharedPref.getString(KgiAuthToken, "");
    }

    public static void putKgiUUID(String authToken) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(KgiUuid, authToken);
        editor.apply();
    }

    public static String getKgiUUID() {
        return mSharedPref.getString(KgiUuid, "");
    }


    public static void putChallengeID(String challengeID) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(CHALLENGE_ID, challengeID);
        editor.apply();
    }

    public static String getChallengeID() {
        return mSharedPref.getString(CHALLENGE_ID, "");
    }

    public static void positionID(String positionID) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(POSITION_ID, positionID);
        editor.apply();
    }

    public static String getPositionID() {
        return mSharedPref.getString(POSITION_ID, "");
    }


    public static void putChallengeCount(int authToken) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(CHALLENGE_COUNT, authToken);
        editor.apply();
    }

    public static int getChallengeCount() {
        return mSharedPref.getInt(CHALLENGE_COUNT, 0);
    }


    public static void putWeight(String weight) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(WEIGHT, weight);
        editor.apply();
    }

    public static String getWeight() {
        return mSharedPref.getString(WEIGHT, "");
    }


    public static void putEmail(String email) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(Email, email);
        editor.apply();
    }

    public static String getEmail() {
        return mSharedPref.getString(Email, "");
    }

    public static void putMobileNo(String mobileNo) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(MobileNo, mobileNo);
        editor.apply();
    }

    public static String getMobileNo() {
        return mSharedPref.getString(MobileNo, "");
    }

    public static void putKgiMobileNo(String mobileNo) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(KgiMobileNo, mobileNo);
        editor.apply();
    }

    public static String getKgiMobileNo() {
        return mSharedPref.getString(KgiMobileNo, "");
    }

    public static void putDecryptMobileNo(String mobileNo) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        String encryptedNo = RSAEncryption.rsaEncrypt(mobileNo);
        editor.putString(DecryptMobileNo, encryptedNo);
        editor.apply();
    }

    public static String getDecryptMobileNo() {
        if (mSharedPref.getString(DecryptMobileNo, "").isEmpty() || mSharedPref.getString(DecryptMobileNo, "").length() == 10) {
            return mSharedPref.getString(DecryptMobileNo, "");
        }
        String encryptedNo = mSharedPref.getString(DecryptMobileNo, "");
        return encryptedNo.isEmpty() ? "" : RSAEncryption.callDecryptionMethod(encryptedNo);
    }

    public static void putEncryptedMobileNo(String encryptedMobileNo) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(EncryptedMobileNo, encryptedMobileNo);
        editor.apply();
    }

    public static String getEncryptedMobileNo() {
        return mSharedPref.getString(EncryptedMobileNo, "");
    }

    public static void putDOB(String dob) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(DOB, dob);
        editor.apply();
    }

    public static String getDOB() {
        return mSharedPref.getString(DOB, "");
    }

    public static void putGender(String gender) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(Gender, gender);
        editor.apply();
    }

    public static String getGender() {
        return mSharedPref.getString(Gender, "");
    }

    public static void putRegistrationGender(String gender) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(REGISTRATION_GENDER, gender);
        editor.apply();
    }

    public static String getRegistrationGender() {
        return mSharedPref.getString(REGISTRATION_GENDER, "");
    }

    public static void putIRAHealthData(String iraHealthData) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(IRAHealthData, iraHealthData);
        editor.apply();
    }

    public static String getIRAHealthData() {
        return mSharedPref.getString(IRAHealthData, "");
    }

    public static void putShowCaloriesFeedback(Boolean showFeedback) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(showCaloriesFeedback, showFeedback);
        editor.apply();
    }

    public static Boolean getShowCaloriesFeedback() {
        return mSharedPref.getBoolean(showCaloriesFeedback, false);
    }

    public static void putPolicyDetails(String PolicyDetail) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PolicyDetails, PolicyDetail);
        editor.apply();
    }

    public static String getPolicyDetails() {
        return mSharedPref.getString(PolicyDetails, "");
    }

    public static void putKGIPolicyDetails(String PolicyDetail) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(KGIPolicyDetails, PolicyDetail);
        editor.apply();
    }

    public static String getKGIPolicyDetails() {
        return mSharedPref.getString(KGIPolicyDetails, "");
    }

    public static void putKGIPolicyVASType(String type) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(KGIPolicyVASType, type);
        editor.apply();
    }

    public static String gutKGIPolicyVASType() {
        return mSharedPref.getString(KGIPolicyVASType, "");
    }

    public static void putCommunityMembers(String communityMembers) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(CommunityMembers, communityMembers);
        editor.apply();
    }

    public static String getCommunityMembers() {
        return mSharedPref.getString(CommunityMembers, "");
    }


    public static void putUploadEhrResponse(String UploadedResponse) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(EhrUploadedResponse, UploadedResponse);
        editor.apply();
    }

    public static String getUploadEhrResponse() {
        return mSharedPref.getString(EhrUploadedResponse, "");
    }

    public static void putFirstTimeValuesInTopics(boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(FirstTimeValuesInTopics, value);
        editor.apply();
    }

    public static boolean getFirstTimeValuesInTopics() {
        return mSharedPref.getBoolean(FirstTimeValuesInTopics, false);
    }

    public static void putProfilePicPath(String profilePicPath) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ProfilePicPath, profilePicPath);
        editor.apply();
    }

    public static String getProfilePicPath() {
        return mSharedPref.getString(ProfilePicPath, "");
    }

    public static void putHRAAnalysis(String hraAnalysis) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(HRAAnalysis, hraAnalysis);
        editor.apply();
    }

    public static String getHRAAnalysis() {
        return mSharedPref.getString(HRAAnalysis, "");
    }

    public static void putHeartAgeAnalysis(String heartAgeAnalysis) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(HeartAgeAnalysis, heartAgeAnalysis);
        editor.apply();
    }

    public static String getHeartAgeAnalysis() {
        return mSharedPref.getString(HeartAgeAnalysis, "");
    }

    public static void putDASSAnalysis(String dassAnalysis) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(DASSAnalysis, dassAnalysis);
        editor.apply();
    }

    public static String getDASSAnalysis() {
        return mSharedPref.getString(DASSAnalysis, "");
    }

    public static void putWaterIntake(Boolean isWaterIntakeAllowed) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(WATER_INTAKE_ALLOWED, isWaterIntakeAllowed);
        editor.apply();
    }

    public static Boolean getWaterIntake() {
        return mSharedPref.getBoolean(WATER_INTAKE_ALLOWED, false);
    }

    public static void putFeedbackPopup(Boolean feedBack) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(SHOW_FEEDBACK_POPUP, feedBack);
        editor.apply();
    }

    public static Boolean getFeedbackPopup() {
        return mSharedPref.getBoolean(SHOW_FEEDBACK_POPUP, false);
    }

    public static void putUserCommunities(String userCommunities) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserCommunities, userCommunities);
        editor.apply();
    }

    public static String getUserCommunities() {
        return mSharedPref.getString(UserCommunities, "");
    }

    public static void putFaceScanID(String faceScanID) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(FACE_SCAN_ID, faceScanID);
        editor.apply();
    }

    public static String getFaceScanId() {
        return mSharedPref.getString(FACE_SCAN_ID, "");
    }

    public static void putReferredBy(String referredBy) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ReferredBy, referredBy);
        editor.apply();
    }

    public static String getReferredBy() {
        return mSharedPref.getString(ReferredBy, "");
    }

    public static void putRedirectionKey(String referredBy) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(redirectionKey, referredBy);
        editor.apply();
    }

    public static String getRedirectionKey() {
        return mSharedPref.getString(redirectionKey, "");
    }

    public static void putHobbyTribe(Boolean hobbyTribe) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(isHobbyTribe, hobbyTribe);
        editor.apply();
    }

    public static Boolean getHobbyTribe() {
        return mSharedPref.getBoolean(isHobbyTribe, false);
    }

    public static void putHobbyTribeUrl(String url) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(hobbyTribeURL, url);
        editor.apply();
    }

    public static String getHobbyTribeUrl() {
        return mSharedPref.getString(hobbyTribeURL, "");
    }


    public static void putHobbyTribeLogo(String url) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(hobbyTribeLogo, url);
        editor.apply();
    }

    public static String getHobbyTribelogo() {
        return mSharedPref.getString(hobbyTribeLogo, "");
    }

    public static void putWebinarStatus(boolean isWebinar) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(webinarStatus, isWebinar);
        editor.apply();
    }

    public static Boolean getWebinarStatus() {
        return mSharedPref.getBoolean(webinarStatus, false);
    }

    public static void putReferredByID(String referredByID) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ReferredByID, referredByID);
        editor.apply();
    }

    public static String getReferredByID() {
        return mSharedPref.getString(ReferredByID, "");
    }

    public static void putReferredFor(String referredFor) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ReferredFor, referredFor);
        editor.apply();
    }

    public static String getReferredFor() {
        return mSharedPref.getString(ReferredFor, "");
    }

    public static void putReferredComId(String referredComId) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ReferredComId, referredComId);
        editor.apply();
    }

    public static String getReferredComId() {
        return mSharedPref.getString(ReferredComId, "");
    }

    public static void putReferredComName(String referredComName) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ReferredComName, referredComName);
        editor.apply();
    }

    public static String getReferredComName() {
        return mSharedPref.getString(ReferredComName, "");
    }

    public static void putReferredFromHRA(String referredFromHRA) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(ReferredFromHRA, referredFromHRA);
        editor.apply();
    }

    public static String getReferredFromHRA() {
        return mSharedPref.getString(ReferredFromHRA, "");
    }

    public static void putUserVitals(String userVitals) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserVitals, userVitals);
        editor.apply();
    }

    public static String getUserVitals() {
        return mSharedPref.getString(UserVitals, "");
    }

    public static void putIsScratchedFirstCard(boolean isScratchedFirstCard) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsScratchedFirstCard, isScratchedFirstCard);
        editor.apply();
    }

    public static boolean getIsScratchedFirstCard() {
        return mSharedPref.getBoolean(IsScratchedFirstCard, false);
    }


    public static void putReferralCode(String refferralCode) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(RefferralCode, refferralCode);
        editor.apply();
    }

    public static String getReferralCode() {
        return mSharedPref.getString(RefferralCode, "");
    }

    public static void putCopReferralCode(String refferralCode) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(COP_RefferralCode, refferralCode);
        editor.apply();
    }

    public static String getCopReffralCode() {
        return mSharedPref.getString(COP_RefferralCode, "");
    }


    public static void putSondeToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(SondeAuthToken, authToken);
        editor.apply();
    }

    public static String getSondeToken() {
        return mSharedPref.getString(SondeAuthToken, "");
    }

    public static void putTransactionID(String transactionID) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(TransactionID, transactionID);
        editor.apply();
    }

    public static void putSondeUserIdentifier(String requestID) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(SondeUserIdentifier, requestID);
        editor.apply();
    }

    public static String getSondeUserIdentifier() {
        return mSharedPref.getString(SondeUserIdentifier, "");
    }

    public static void putSondeUserRegistered(boolean isRegistered) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(SondeUserRegistered, isRegistered);
        editor.apply();
    }

    public static boolean getSondeUserRegistered() {
        return mSharedPref.getBoolean(SondeUserRegistered, false);
    }

    public static void putSondeAudioUrl(String url) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(SondeAudioURL, url);
        editor.apply();
    }

    public static String getAudioUrl() {
        return mSharedPref.getString(SondeAudioURL, "");
    }

    public static void putSondeFirstJobID(String ID) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(SondeID, ID);
        editor.apply();
    }

    public static String getSondeFirstJobID() {
        return mSharedPref.getString(SondeID, "");
    }

    public static void putSondeFilePath(String filePath) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(SondeFilePath, filePath);
        editor.apply();
    }

    public static String getSondeFilePath() {
        return mSharedPref.getString(SondeFilePath, "");
    }

    public static void putIsUserNameUpdated(Boolean isRegistered) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(isUserNameRegistered, isRegistered);
        editor.apply();
    }

    public static Boolean getIsUserNameUpdated() {
        return mSharedPref.getBoolean(isUserNameRegistered, false);
    }

    public static void putWellnessTopic(String wellnessTopic) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(WELLNESS_TOPIC, wellnessTopic);
        editor.apply();
    }

    public static String getWellnessTopic() {
        return mSharedPref.getString(WELLNESS_TOPIC, "");
    }

    public static void putMentalWellnessFilePath(String filePath) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(MentalWellnessFilePath, filePath);
        editor.apply();
    }

    public static String getMentalWellnessPath() {
        return mSharedPref.getString(MentalWellnessFilePath, "");
    }

    public static void putRespiratoryCount(Integer ID) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(RESPIRATORY_COUNT, ID);
        editor.apply();
    }

    public static Integer getRespiratoryCount() {
        return mSharedPref.getInt(RESPIRATORY_COUNT, 0);
    }

    public static void putDiaryConcern(Boolean diaryConcern) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(DIARY_CONCERN, diaryConcern);
        editor.apply();
    }

    public static Boolean getDiaryConcern() {
        return mSharedPref.getBoolean(DIARY_CONCERN, false);
    }

    public static String getUnwindNotification() {
        return mSharedPref.getString(JOKE_NOTIFICATION, "");
    }

    public static void putUnwindNotification(String unwindType) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(JOKE_NOTIFICATION, unwindType);
        editor.apply();
    }


    public static String getTransactionID() {
        return mSharedPref.getString(TransactionID, "");
    }


    public static void putTotalPoints(String totalPoints) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(TOTALPOINTS, totalPoints);
        editor.apply();
    }


    public static String getTotalpoints() {
        return mSharedPref.getString(TOTALPOINTS, "0");
    }

    public static void putConnectionCount(String connectionCount) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(CONNECTION_COUNT, connectionCount);
        editor.apply();
    }

    public static String getConnectionCount() {
        return mSharedPref.getString(CONNECTION_COUNT, "");
    }

    public static void putUserVitalsScannedOn(String userVitalsScannedOn) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserVitalsScannedOn, userVitalsScannedOn);
        editor.apply();
    }

    public static String getUserVitalsScannedOn() {
        return mSharedPref.getString(UserVitalsScannedOn, "");
    }

    public static void putUserLatestReminder(String userLatestReminder) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserLatestReminder, userLatestReminder);
        editor.apply();
    }

    public static String getUserLatestReminder() {
        return mSharedPref.getString(UserLatestReminder, "");
    }

    public static void putSplashScreenShowedOn(String splashScreenShowedOn) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(SplashScreenShowedOn, splashScreenShowedOn);
        editor.apply();
    }

    public static String getSplashScreenShowedOn() {
        return mSharedPref.getString(SplashScreenShowedOn, "");
    }

    public static void putGFitScreenShowedOn(String gFitScreenShowedOn) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(GFitScreenShowedOn, gFitScreenShowedOn);
        editor.apply();
    }

    public static String getGFitScreenShowedOn() {
        return mSharedPref.getString(GFitScreenShowedOn, "");
    }

    public static void putDashboardData(String dashboardData) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(DashboardData, dashboardData);
        editor.apply();
    }

    public static String getBmr() {
        return mSharedPref.getString(BMR, "");
    }

    public static void putBmr(String bmr) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(BMR, bmr);
        editor.apply();
    }

    public static String getUserAge() {
        return mSharedPref.getString(UserAge, "");
    }

    public static void putUserAge(String userAge) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserAge, userAge);
        editor.apply();
    }

    public static String getUserGender() {
        return mSharedPref.getString(UserGender, "");
    }

    public static void putUserGender(String userGender) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserGender, userGender);
        editor.apply();
    }

    public static String getUserHeight() {
        return mSharedPref.getString(UserHeight, "");
    }

    public static void putUserHeight(String userHeight) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserHeight, userHeight);
        editor.apply();
    }

    public static String getUserWeight() {
        return mSharedPref.getString(UserWeight, "");
    }

    public static void putUserWeight(String userWeight) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserWeight, userWeight);
        editor.apply();
    }

    public static String getUserHeightFt() {
        return mSharedPref.getString(UserHeightFt, "");
    }

    public static void putUserHeightFt(String userHeightFt) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserHeightFt, userHeightFt);
        editor.apply();
    }

    public static String getDashboardData() {
        return mSharedPref.getString(DashboardData, "");
    }

    public static void putHRAJson(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(HRAJson, value);
        editor.apply();
    }

    public static String getHRAJson() {
        return mSharedPref.getString(HRAJson, "");
    }

    public static void putHealthScoreResponseNew(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(HealthScoreResponseNew, value);
        editor.apply();
    }

    public static String getHealthScoreResponseNew() {
        return mSharedPref.getString(HealthScoreResponseNew, "");
    }

    public static boolean isBodyProfileHRADone() {
        return mSharedPref.getBoolean(IsBodyProfileHRADone, false);
    }

    public static void putIsBodyProfileHRADone(boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsBodyProfileHRADone, value);
        editor.apply();
    }

    public static boolean isActivityHRADone() {
        return mSharedPref.getBoolean(IsActivityHRADone, false);
    }

    public static void putIsActivityHRADone(boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsActivityHRADone, value);
        editor.apply();
    }

    public static boolean isNutritionHRADone() {
        return mSharedPref.getBoolean(IsNutritionHRADone, false);
    }

    public static void putIsNutritionHRADone(boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsNutritionHRADone, value);
        editor.apply();
    }

    public static boolean isMentalWellnessHRADone() {
        return mSharedPref.getBoolean(IsMentalWellnessHRADone, false);
    }

    public static void putIsMentalWellnessHRADone(boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsMentalWellnessHRADone, value);
        editor.apply();
    }

    public static boolean isLifestyleHRADone() {
        return mSharedPref.getBoolean(IsLifestyleHRADone, false);
    }

    public static void putIsLifestyleHRADone(boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsLifestyleHRADone, value);
        editor.apply();
    }

    public static boolean isNewHRACompleted() {
        return mSharedPref.getBoolean(IsNewHRACompleted, false);
    }

    public static void putIsNewHRACompleted(boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsNewHRACompleted, value);
        editor.apply();
    }

    public static String getBMIFromNewHRA() {
        return mSharedPref.getString(BMIFromNewHRA, "");
    }

    public static void putBMIFromNewHRA(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(BMIFromNewHRA, value);
        editor.apply();
    }

    public static float getLatestWeightValue() {
        return mSharedPref.getFloat(latestWeightValue, 0);
    }

    public static void putLatestWeightValue(float value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putFloat(latestWeightValue, value);
        editor.apply();
    }

    public static String getConversationId() {
        return mSharedPref.getString(conversationId, "");
    }

    public static void putConversationId(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(conversationId, value);
        editor.apply();
    }

    public static String getIRAConversationId() {
        return mSharedPref.getString(IRAconversationId, "");
    }

    public static void putIRAConversationId(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(IRAconversationId, value);
        editor.apply();
    }

    public static int getNumberOfHRADone() {
        return mSharedPref.getInt(NumberOfHRADone, 0);
    }

    public static void putNumberOfHRADone(int value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(NumberOfHRADone, value);
        editor.apply();
    }

    public static void putUuid(String uuid) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UUID, uuid);
        editor.apply();
    }

    public static String getUuid() {
        return mSharedPref.getString(UUID, "");
    }

    public static void putAesUuid(String aesUuid) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(AES_UUID, aesUuid);
        editor.apply();
    }

    public static String getAesUuid() {
        return mSharedPref.getString(AES_UUID, "");
    }


    //Logout
    public static void clearSharedPref() {
        mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE).edit().clear().apply();
    }

    public static void putNDHMAccessToken(String data) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(NDHMAccessToken, data);
        editor.apply();
    }

    public static String getNDHMAccessToken() {
        return mSharedPref.getString(NDHMAccessToken, "");
    }

    public static void putNDHMUserToken(String data) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(NDHMUserToken, data);
        editor.apply();
    }

    public static String getNDHMUserToken() {
        return mSharedPref.getString(NDHMUserToken, "");
    }

    public static void putStepsGoals(int data) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(StepsGoals, data);
        editor.apply();
    }

    public static int getStepsGoals() {
        return mSharedPref.getInt(StepsGoals, 10000);
    }

    public static void putNDHMUserRefreshToken(String data) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(NDHMUserRefreshToken, data);
        editor.apply();
    }

    public static String getNDHMUserRefreshToken() {
        return mSharedPref.getString(NDHMUserRefreshToken, "");
    }

    public static void putIsCameFromLogin(boolean data) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsCameFromLogin, data);
        editor.apply();
    }


    public static boolean getIsCameFromLogin() {
        return mSharedPref.getBoolean(IsCameFromLogin, false);
    }

    public static void putIsLoggedIn(boolean data) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsLoggedIn, data);
        editor.apply();
    }


    public static boolean getIsLoggedIn() {
        return mSharedPref.getBoolean(IsLoggedIn, false);
    }

    //FitBit
    public static void putFitBitConnection(boolean connection) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(FIT_BIT_CONNECTION, connection);
        editor.apply();
    }

    public static boolean getFitBitConnection() {
        return mSharedPref.getBoolean(FIT_BIT_CONNECTION, false);
    }

    public static void putTodaySteps(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(TodaySteps, value);
        editor.apply();
    }

    public static String getTodaySteps() {
        return mSharedPref.getString(TodaySteps, "0");
    }

    public static void putTodaySleep(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(TodaySleep, value);
        editor.apply();
    }

    public static String getTodaySleep() {
        return mSharedPref.getString(TodaySleep, "0");
    }

    public static void putEmergencyContact(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(EmergencyContact, value);
        editor.apply();
    }

    public static String getEmergencyContact() {
        return mSharedPref.getString(EmergencyContact, "");
    }

    public static void putRiddleAns(String riddleAns) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(RiddleAns, riddleAns);
        editor.apply();
    }

    public static void puttRiddleAnsList(String riddleAns) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(RiddleAnswers, riddleAns);
        editor.apply();
    }

    public static void deleteRiddleAns() {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.remove(RiddleAns);
        editor.apply();
    }

    public static String getRiddleAns() {
        return mSharedPref.getString(RiddleAns, "");
    }

    public static String getRiddleAnsList() {
        return mSharedPref.getString(RiddleAnswers, "");
    }

    public static void putRiddleDate(String riddleAns) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(RiddleDate, riddleAns);
        editor.apply();
    }

    public static String getRiddleDate() {
        return mSharedPref.getString(RiddleDate, "");
    }

    public static void putWaterGoals(float waterGoals) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putFloat(WaterGoals, waterGoals);
        editor.apply();
    }

    public static float getWaterGoals() {
        return mSharedPref.getFloat(WaterGoals, 12);
    }

    public static void putFootprintGoals(int footprintGoals) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(FootprintGoals, footprintGoals);
        editor.apply();
    }

    public static int getFootprintGoals() {
        return mSharedPref.getInt(FootprintGoals, 10000);
    }

    public static void putUserSelectedTribes(int userSelectedTribes) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(UserSelectedTribes, userSelectedTribes);
        editor.apply();
    }

    public static int getUserSelectedTribes() {
        return mSharedPref.getInt(UserSelectedTribes, 0);
    }

    public static void putIsFirstInstall(boolean isFirstInstall) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsFirstInstall, isFirstInstall);
        editor.apply();
    }

    public static boolean getIsFirstInstall() {
        return mSharedPref.getBoolean(IsFirstInstall, false);
    }


    public static int getCurrentLevel() {
        return mSharedPref.getInt(CurrentLevel, 0);
    }

    public static void putCurrentLevel(int currentLevel) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(CurrentLevel, currentLevel);
        editor.apply();
    }

    public static void putIsHappyMartIntroShown(boolean isHappyMartIntroShown) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsHappyMartIntroShown, isHappyMartIntroShown);
        editor.apply();
    }

    public static boolean getIsHappyMartIntroShown() {
        return mSharedPref.getBoolean(IsHappyMartIntroShown, false);
    }

    public static void putIsHappyYouIntroShown(boolean isHappyYouIntroShown) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsHappyYouIntroShown, isHappyYouIntroShown);
        editor.apply();
    }

    public static boolean getIsHappyYouIntroShown() {
        return mSharedPref.getBoolean(IsHappyYouIntroShown, false);
    }

    public static void putIsTribeIntroShown(boolean isTribeIntroShown) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsTribeIntroShown, isTribeIntroShown);
        editor.apply();
    }

    public static boolean getIsTribeIntroShown() {
        return mSharedPref.getBoolean(IsTribeIntroShown, false);
    }

    public static void putIsWinningsIntroShown(boolean isWinningsIntroShown) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsWinningsIntroShown, isWinningsIntroShown);
        editor.apply();
    }

    public static boolean getIsWinningsIntroShown() {
        return mSharedPref.getBoolean(IsWinningsIntroShown, false);
    }

    public static void putIsWellbeingIntroShown(boolean isWellbeingIntroShown) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(IsWellbeingIntroShown, isWellbeingIntroShown);
        editor.apply();
    }

    public static boolean getIsWellbeingIntroShown() {
        return mSharedPref.getBoolean(IsWellbeingIntroShown, false);
    }

    public static void putWellBeingIntroShownHealth(boolean wellBeingIntroShownHealth) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(WellBeingIntroShownHealth, wellBeingIntroShownHealth);
        editor.apply();
    }

    public static boolean getWellBeingIntroShownHealth() {
        return mSharedPref.getBoolean(WellBeingIntroShownHealth, false);
    }

    public static void putWellBeingIntroShownImmunity(boolean wellBeingIntroShownImmunity) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(WellBeingIntroShownImmunity, wellBeingIntroShownImmunity);
        editor.apply();
    }

    public static boolean getWellBeingIntroShownImmunity() {
        return mSharedPref.getBoolean(WellBeingIntroShownImmunity, false);
    }

    public static void putWellBeingIntroShownDAS(boolean wellBeingIntroShownDAS) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(WellBeingIntroShownDAS, wellBeingIntroShownDAS);
        editor.apply();
    }

    public static boolean getWellBeingIntroShownDAS() {
        return mSharedPref.getBoolean(WellBeingIntroShownDAS, false);
    }

    public static void putWellBeingIntroShownVitals(boolean wellBeingIntroShownVitals) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(WellBeingIntroShownVitals, wellBeingIntroShownVitals);
        editor.apply();
    }

    public static boolean getWellBeingIntroShownVitals() {
        return mSharedPref.getBoolean(WellBeingIntroShownVitals, false);
    }

    public static void putWellBeingIntroShownHeartAge(boolean wellBeingIntroShownHeartAge) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(WellBeingIntroShownHeartAge, wellBeingIntroShownHeartAge);
        editor.apply();
    }

    public static boolean getWellBeingIntroShownHeartAge() {
        return mSharedPref.getBoolean(WellBeingIntroShownHeartAge, false);
    }

    public static void putUserAllWidget(String hraAnalysis) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserAllWidget, hraAnalysis);
        editor.apply();
    }

    public static String getUserAllWidget() {
        return mSharedPref.getString(UserAllWidget, "");
    }

    public static void putHomeBottomCard(String homeCard) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(HomeBottomCard, homeCard);
        editor.apply();
    }

    public static String gutHomeBottomCard() {
        return mSharedPref.getString(HomeBottomCard, "");
    }

    public static int getStampsHistory() {
        return mSharedPref.getInt(StampHistory, 0);
    }

    public static void putStampsHistory(int currentLevel) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(StampHistory, currentLevel);
        editor.apply();
    }

    public static int getPointsHistory() {
        return mSharedPref.getInt(PointsHistory, 0);
    }

    public static void putPointsHistory(int currentLevel) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(PointsHistory, currentLevel);
        editor.apply();
    }

    public static int getUserCalorieBurned() {
        return mSharedPref.getInt(UserCalorieBurned, 0);
    }

    public static void putUserCalorieBurned(int cb) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(UserCalorieBurned, cb);
        editor.apply();
    }

    public static void putNewUser(boolean newUser) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(NewUser, newUser);
        editor.apply();
    }

    public static boolean getNewUser() {
        return mSharedPref.getBoolean(NewUser, true);
    }


    public static String getNDHMProfileInfo() {
        return mSharedPref.getString(NDHMProfileInfo, "");
    }

    public static void putNDHMProfileInfo(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(NDHMProfileInfo, value);
        editor.apply();
    }


    public static int getCommunityPreference() {
        return mSharedPref.getInt(CommunityPreference, 0);
    }

    public static void putCommunityPreference(int preference) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(CommunityPreference, preference);
        editor.apply();
    }

    public static String getAddFamilyMember() {
        return mSharedPref.getString(AddFamilyMember, "");
    }

    public static void putAddFamilyMember(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(AddFamilyMember, value);
        editor.apply();
    }

    public static String getUserWaist() {
        return mSharedPref.getString(UserWaist, "");
    }

    public static void putUserWaist(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(UserWaist, value);
        editor.apply();
    }

    public static String getCurrentTimeStamp() {
        return mSharedPref.getString(CurrentTimeStamp, "");
    }

    public static void putCurrentTimeStamp(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(CurrentTimeStamp, value);
        editor.apply();
    }

    public static String getCurrentTimeStampExpires() {
        return mSharedPref.getString(CurrentTimeStampExpires, "");
    }

    public static void putCurrentTimeStampExpires(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(CurrentTimeStampExpires, value);
        editor.apply();
    }


    public static Boolean getGoogleFitStatus() {
        return mSharedPref.getBoolean(isGoogleFitActive, false);
    }

    public static void putGoogleFitStatus(Boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(isGoogleFitActive, value);
        editor.apply();
    }


    public static boolean getAnalysisStatus() {
        return mSharedPref.getBoolean(isGoogleFitActive, false);
    }

    public static void putAnalysisStatus(boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(isGoogleFitActive, value);
        editor.apply();
    }


    public static Boolean getChallengeStarted() {
        return mSharedPref.getBoolean(isChallengeStarted, false);
    }

    public static void putChallengeStarted(Boolean value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(isChallengeStarted, value);
        editor.apply();
    }

    public static String getTempUUID() {
        return mSharedPref.getString(tempUUID, "");
    }

    public static void putTempUUID(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(tempUUID, value);
        editor.apply();
    }

    public static String getSessionStartTime() {
        return mSharedPref.getString(sessionStartTime, "");
    }

    public static void putSessionStartTime(String value) {
        Log.d("HU App Session Start Time", value);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(sessionStartTime, value);
        editor.apply();
    }

    public static String getSessionEndTime() {
        return mSharedPref.getString(sessionEndTime, "");
    }

    public static void putSessionEndTime(String value) {
        Log.d("HU App Session End Time", value);
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(sessionEndTime, value);
        editor.apply();
    }

    public static Boolean getSpinWheelStatus() {
        return mSharedPref.getBoolean(isWheelSpinned, false);
    }

    public static void setSpinWheelStatus(Boolean spinWheel) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(isWheelSpinned, spinWheel);
        editor.apply();
    }

    public static void setWheelTimesUp(Boolean spinWheel) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(isWheelTimesUp, spinWheel);
        editor.apply();
    }

    public static Boolean getWheelTimesUp() {
        return mSharedPref.getBoolean(isWheelTimesUp, false);
    }

    public static void setRewardTimer(String time) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(rewardTiwer, time);
        editor.apply();
    }

    public static String getRewardTiwer() {
        return mSharedPref.getString(rewardTiwer, "");

    }


    public static void setOldUserPopup(Boolean popup) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(isOldUserPopUpShown, popup);
        editor.apply();
    }

    public static Boolean getOldUserPopup() {
        return mSharedPref.getBoolean(isOldUserPopUpShown, false);

    }

    public static void setPolicyMPin(String time) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PolicyMPin, time);
        editor.apply();
    }

    public static String getPolicyMPin() {
        return mSharedPref.getString(PolicyMPin, "");
    }

    public static void putHasBiometric(Boolean userName) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(hasBiometric, userName);
        editor.apply();
    }

    public static Boolean getHasBiometric() {
        return mSharedPref.getBoolean(hasBiometric, false);
    }

    public static void setPolicyContent(String time) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(policyContent, time);
        editor.apply();
    }

    public static String getPolicyContent() {
        return mSharedPref.getString(policyContent, "");
    }

    public static void setClientId(String time) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(clientId, time);
        editor.apply();
    }

    public static String getClientId() {
        return mSharedPref.getString(clientId, "");
    }

    public static void setClientsPolicyData(String time) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(clientsPolicyData, time);
        editor.apply();
    }

    public static String getClientsPolicyData() {
        return mSharedPref.getString(clientsPolicyData, "");
    }

    public static void setCorporateRegistered(String time) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(corporateRegistered, time);
        editor.apply();
    }

    public static String getCorporateRegistered() {
        return mSharedPref.getString(corporateRegistered, "");
    }

    public static void setCorporateName(String time) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(corporateName, time);
        editor.apply();
    }

    public static String getCorporateName() {
        return mSharedPref.getString(corporateName, "");
    }

    public static void setCorporateId(String time) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(corporateId, time);
        editor.apply();
    }

    public static String getCorporateId() {
        return mSharedPref.getString(corporateId, "");
    }

    public static void setCorporateImage(String time) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(corporateImage, time);
        editor.apply();
    }

    public static String getCorporateImage() {
        return mSharedPref.getString(corporateImage, "");
    }

    public static void putCorporateAccountNotFound(Boolean userName) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putBoolean(CorporateAccountNotFound, userName);
        editor.apply();
    }

    public static Boolean getCorporateAccountNotFound() {
        return mSharedPref.getBoolean(CorporateAccountNotFound, false);
    }

    public static void setMarshURL(String url) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(marshURL, url);
        editor.apply();
    }

    public static String getMarshURL() {
        return mSharedPref.getString(marshURL, "");
    }


    public static void setPolicyRenewableURL(String url) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(policyRenewableURL, url);
        editor.apply();
    }

    public static String getPolicyRenewableURL() {
        return mSharedPref.getString(policyRenewableURL, "");
    }

}