package com.wyh.happyyousdk.APIEncryption;

import com.wyh.happyyousdk.dashboard.model.GetBannerResponse;
import com.wyh.happyyousdk.model.BurnRegPointsReq;
import com.wyh.happyyousdk.model.BurnRegPointsResp;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.GetClientIDReq;
import com.wyh.happyyousdk.model.GetClientRes;
import com.wyh.happyyousdk.model.GetOTPReq;
import com.wyh.happyyousdk.model.GetPDRPRODReq;
import com.wyh.happyyousdk.model.GetPolicyDocumentReq;
import com.wyh.happyyousdk.model.GetPremiumCertificateReq;
import com.wyh.happyyousdk.model.GetUnitLinkedPolicyStatementPROReq;
import com.wyh.happyyousdk.model.MPINReq;
import com.wyh.happyyousdk.model.PolicyListReq;
import com.wyh.happyyousdk.model.PolicyListResp;
import com.wyh.happyyousdk.model.UpdateFileStatusReq;
import com.wyh.happyyousdk.model.VerifyEmailMobileOTPReq;
import com.wyh.happyyousdk.model.VerifyEmailOTPReq;
import com.wyh.happyyousdk.model.VerifyOTP;
import com.wyh.happyyousdk.model.request.ActivityTrackerRequest;
import com.wyh.happyyousdk.model.request.AddGoalOnDashboardRequest;
import com.wyh.happyyousdk.model.request.AddIndividualGoalRequest;
import com.wyh.happyyousdk.model.request.AssignRewardsRequest;
import com.wyh.happyyousdk.model.request.ChallengeCommunityRequest;
import com.wyh.happyyousdk.model.request.ChallengeRankRequest;
import com.wyh.happyyousdk.model.request.ClickEventRequest;
import com.wyh.happyyousdk.model.request.CommonRequest;
import com.wyh.happyyousdk.model.request.DeleteGoalRequest;
import com.wyh.happyyousdk.model.request.EnrollUserRequest;
import com.wyh.happyyousdk.model.request.EnrolledHistoryResponse;
import com.wyh.happyyousdk.model.request.FaceScanRegistrationRequest;
import com.wyh.happyyousdk.model.request.FacnScanInTribeRequest;
import com.wyh.happyyousdk.model.request.GetCityRequestModel;
import com.wyh.happyyousdk.model.request.GetEmailOTPReq;
import com.wyh.happyyousdk.model.request.GetMentalScoreRequest;
import com.wyh.happyyousdk.model.request.GetNudgeDetailsRequest;
import com.wyh.happyyousdk.model.request.HappyMartCategoryRequest;
import com.wyh.happyyousdk.model.request.JoinChallengeRequest;
import com.wyh.happyyousdk.model.request.KLISubRequest;
import com.wyh.happyyousdk.model.request.KLISubResponse;
import com.wyh.happyyousdk.model.request.LockRewardsRequest;
import com.wyh.happyyousdk.model.request.LogExceptionRequest;
import com.wyh.happyyousdk.model.request.LogExceptionResponse;
import com.wyh.happyyousdk.model.request.PollTranscribeRequest;
import com.wyh.happyyousdk.model.request.QCGetCategoryResponse;
import com.wyh.happyyousdk.model.request.ReferalContestRequest;
import com.wyh.happyyousdk.model.request.RespiratoryFirstJobRequest;
import com.wyh.happyyousdk.model.request.RespiratorySecondJobRequest;
import com.wyh.happyyousdk.model.request.SaveCorporateDetailsReq;
import com.wyh.happyyousdk.model.request.SaveCorporateDetailsReqV1;
import com.wyh.happyyousdk.model.request.SaveCorporateStatusReq;
import com.wyh.happyyousdk.model.request.ScratchRequest;
import com.wyh.happyyousdk.model.request.SondeGetUrlRequest;
import com.wyh.happyyousdk.model.request.SondeReminderRequest;
import com.wyh.happyyousdk.model.request.SondeTokenResponse;
import com.wyh.happyyousdk.model.request.SondeUserRegistrationRequest;
import com.wyh.happyyousdk.model.request.StartChallengeRequest;
import com.wyh.happyyousdk.model.request.StartRequest;
import com.wyh.happyyousdk.model.request.TranscribeJobRequest;
import com.wyh.happyyousdk.model.request.UpdateCoopCodeRequest;
import com.wyh.happyyousdk.model.request.UpdateRequest;
import com.wyh.happyyousdk.model.request.UserDetailsRequest;
import com.wyh.happyyousdk.model.request.challengeTribe.GetCommunityRankDetailsRequest;
import com.wyh.happyyousdk.model.request.challengeTribe.GetCommunityRankDetailsResponse;
import com.wyh.happyyousdk.model.request.challengeTribe.GetUserRanksRequest;
import com.wyh.happyyousdk.model.request.challengeTribe.ViewMoreTribeResponseModel;
import com.wyh.happyyousdk.model.request.login.ApiSessionLogRequest;
import com.wyh.happyyousdk.model.request.encrDecr.EncryptionRequest;
import com.wyh.happyyousdk.model.request.login.AddFCMTokenRequest;
import com.wyh.happyyousdk.model.request.login.VerifyOtpRequest;
import com.wyh.happyyousdk.model.request.postloginreward.SpinActivityrequest;
import com.wyh.happyyousdk.model.request.postloginreward.StartSpinActivityRequest;
import com.wyh.happyyousdk.model.request.qc.AllQCProductsRequest;
import com.wyh.happyyousdk.model.request.qc.QCProductDetailsRequest;
import com.wyh.happyyousdk.model.request.qc.placeOrder.QCPlaceOrderRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.request.quizathon.ClaimRewardRequest;
import com.wyh.happyyousdk.model.request.quizathon.QuizScratchRequest;
import com.wyh.happyyousdk.model.response.ActivityTrackerResponse;
import com.wyh.happyyousdk.model.response.AddGoalOnDashboardResponse;
import com.wyh.happyyousdk.model.response.AddIndividualGoalResponse;
import com.wyh.happyyousdk.model.response.AmahaKeyResponse;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.BadgesResponse;
import com.wyh.happyyousdk.model.response.ChallengeActivityResponse;
import com.wyh.happyyousdk.model.response.ChallengeRankResponse;
import com.wyh.happyyousdk.model.response.ClickEventResponse;
import com.wyh.happyyousdk.model.response.CommonResponseWyh;
import com.wyh.happyyousdk.model.response.CompleteActivityResponse;
import com.wyh.happyyousdk.model.response.DeleteGoalResponse;
import com.wyh.happyyousdk.model.response.DownloadFaceScanResponse;
import com.wyh.happyyousdk.model.response.EnrollUserResponse;
import com.wyh.happyyousdk.model.response.FaceScanInTribeResponse;
import com.wyh.happyyousdk.model.response.FaceScanRegistrationResponse;
import com.wyh.happyyousdk.model.response.FileShareResp;
import com.wyh.happyyousdk.model.response.GetBranchUserListResponse;
import com.wyh.happyyousdk.model.response.GetChallengesResponse;
import com.wyh.happyyousdk.model.response.GetCityListResponseModel;
import com.wyh.happyyousdk.model.response.GetEmailOTPResp;
import com.wyh.happyyousdk.model.response.GetGoalDayResponse;
import com.wyh.happyyousdk.model.response.GetHappyMartTiles;
import com.wyh.happyyousdk.model.response.GetIndividualGoalResponse;
import com.wyh.happyyousdk.model.response.GetMentalScoreResponse;
import com.wyh.happyyousdk.model.response.GetQuadrantsResponse;
import com.wyh.happyyousdk.model.response.GetRewardsClaimListResp;
import com.wyh.happyyousdk.model.response.GetUserDetailResponse;
import com.wyh.happyyousdk.model.response.GetWalkathonWinnerResponse;
import com.wyh.happyyousdk.model.response.GoalHistoryResponse;
import com.wyh.happyyousdk.model.response.HappyMartCategoryResponse;
import com.wyh.happyyousdk.model.response.JoinChallengeResponse;
import com.wyh.happyyousdk.model.response.LockRewardResponse;
import com.wyh.happyyousdk.model.response.NewDashboardResponse;
import com.wyh.happyyousdk.model.response.PollTranscribeResponse;
import com.wyh.happyyousdk.model.response.RecomendedGoalsResponse;
import com.wyh.happyyousdk.model.response.ReferalContestResponse;
import com.wyh.happyyousdk.model.response.RespiratoryFirstJobResponse;
import com.wyh.happyyousdk.model.response.RespiratorySecondJobResponse;
import com.wyh.happyyousdk.model.response.RewardListResponse;
import com.wyh.happyyousdk.model.response.ScratchResponse;
import com.wyh.happyyousdk.model.response.SondeGetUrlResponse;
import com.wyh.happyyousdk.model.response.SondeHistoryResponse;
import com.wyh.happyyousdk.model.response.SondeQuestionResponse;
import com.wyh.happyyousdk.model.response.SondeReminderResponse;
import com.wyh.happyyousdk.model.response.SondeUserRegistrationResponse;
import com.wyh.happyyousdk.model.response.StartResponse;
import com.wyh.happyyousdk.model.response.TranscribeJobResponse;
import com.wyh.happyyousdk.model.response.UpdateCoopResponse;
import com.wyh.happyyousdk.model.response.UpdateFileStatusResp;
import com.wyh.happyyousdk.model.response.UpdateResponse;
import com.wyh.happyyousdk.model.response.UserDetailResponse;
import com.wyh.happyyousdk.model.response.VerifyEmailOTPResp;
import com.wyh.happyyousdk.model.response.encrDecr.EncryptionResponse;
import com.wyh.happyyousdk.model.response.faceScan.GetFaceKeysResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonResponseModel;
import com.wyh.happyyousdk.model.response.postloginreward.CommonResponse;
import com.wyh.happyyousdk.model.response.postloginreward.EarnedRewardResponse;
import com.wyh.happyyousdk.model.response.postloginreward.GetActivityRewardsClaimListResp;
import com.wyh.happyyousdk.model.response.postloginreward.PostLogRewardResponse;
import com.wyh.happyyousdk.model.response.postloginreward.SpinActivityResponse;
import com.wyh.happyyousdk.model.response.qc.allProducts.AllQCProductsResponse;
import com.wyh.happyyousdk.model.response.qc.productDescription.QCProductDetailsResponse;
import com.wyh.happyyousdk.model.response.quizathon.GetQuizScratchListResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;

public interface APIInterface {

    @POST("Login/VerifyOTPV2")
    Call<ResponseBody> verifyOTP(@Body VerifyOtpRequest request);

    @GET("QC/QCGetCategory")
    Call<QCGetCategoryResponse> getCategory(@Header("Authorization") String token);

    @POST("QC/QCGetProductList")
    Call<AllQCProductsResponse> getAllQCProducts(@Header("Authorization") String token, @Body AllQCProductsRequest request);

    @POST("QC/QCGetProductDetails")
    Call<QCProductDetailsResponse> getProductDetails(@Header("Authorization") String token, @Body QCProductDetailsRequest request);

    @POST("QC/QCPlaceOrder")
    Call<CommonSuccessResponse> placeQcOrder(@Header("Authorization") String token, @Body QCPlaceOrderRequest request);

    //AES Encrypt
    @POST("EncryptDecrypt/AESEncrypt")
    Call<EncryptionResponse> encryptAES(@Body EncryptionRequest request);

    //AES Decrypt
    @POST("EncryptDecrypt/AESDecrypt")
    Call<EncryptionResponse> decryptAES(@Body EncryptionRequest request);

    @POST("Login/AddFCMToken")
    Call<CommonSuccessResponse> sendFCMToken(@Header("Authorization") String token,@Body AddFCMTokenRequest addFCMTokenRequest);

    @GET("dashboard/FetchDashboardBanner")
    Call<GetBannerResponse> getBanner();

    @GET("access/keys")
    Call<ArrayList<GetFaceKeysResponse>> getFaceKeys (@Header("Username") String Username,@Header("Password") String Password,@Header("Cookie") String Cookie);

    @POST("FB/GetKliSubActivities")
    Call<KLISubResponse> getKLIActivity (@Header("Authorization") String token, @Body KLISubRequest kliSubRequest);

    @GET("Challenges/GetChallenges")
    Call<GetChallengesResponse> getChallenges(@Header("Authorization") String token);

    @POST("Challenges/JoinChallenge")
    Call<JoinChallengeResponse> joinChallenge(@Header("Authorization") String token, @Body JoinChallengeRequest request);

    @POST("Challenges/GetChallengeActivities")
    Call<ChallengeActivityResponse> challengeActivity(@Header("Authorization") String token, @Body CommonRequest request);

    @POST("Challenges/StartChallengeActivity")
    Call<StartResponse> startChallenge(@Header("Authorization") String token, @Body StartRequest request);

    @Multipart
    @POST("Challenges/CompleteActivity")
    Call<CompleteActivityResponse> completeActivity(@Header("Authorization") String token, @Body RequestBody body);

    @POST("Challenges/UpdateChallengeActivity")
    Call<UpdateResponse> updateChalleneg(@Header("Authorization") String token, @Body UpdateRequest updateRequest);

    @GET("Challenges/GetUserBadges")
    Call<BadgesResponse> getBadges(@Header("Authorization") String token);

    @POST("Challenges/BadgeIsScratched")
    Call<ScratchResponse> scrathCard(@Header("Authorization") String token, @Body ScratchRequest request);

    @GET("MarketPlace/GetAmahaKey")
    Call<AmahaKeyResponse> amahaGetKey(@Header("Authorization") String token);

    @POST("dashboard/LogException")
    Call<LogExceptionResponse> sendLogs(@Header("Authorization") String token, @Body LogExceptionRequest logExceptionRequest);


    @POST("Challenges/GetCommunityRankDetails")
    Call<GetCommunityRankDetailsResponse> getCommunityRankDetails(@Header("Authorization") String token, @Body GetCommunityRankDetailsRequest request);

    @POST("Challenges/GetCommunityRankDetails")
    Call<GetCommunityRankDetailsResponse> getUserRanks(@Header("Authorization") String token, @Body GetUserRanksRequest request);

    @POST("Challenges/StartChallenge")
    Call<StartResponse> startNewChallenge(@Header("Authorization") String token, @Body StartChallengeRequest request);

    @GET("Challenges/ViewAllTribes")
    Call<ViewMoreTribeResponseModel> viewAllTribes(@Header("Authorization") String token);

    @POST("Profile/UpdateCorporatereferralCode")
    Call<UpdateCoopResponse> updateCoopCode(@Header("Authorization") String token, @Body UpdateCoopCodeRequest updateCoopCodeRequest);
    @GET("MarketPlace/GetSondeToken")
    Call<SondeTokenResponse> getSondeToken(@Header("Authorization") String token);

    @POST("MarketPlace/SondeuserRegistration")
    Call<SondeUserRegistrationResponse> sondeUserRegistration(@Header("Authorization")String token,@Body SondeUserRegistrationRequest sondeUserRegistrationRequest);

    @GET("MarketPlace/GetSondeHistory")
    Call<SondeHistoryResponse> getSondeHistory(@Header("Authorization") String token);

    @GET("MarketPlace/GetSondeQuestions")
    Call<SondeQuestionResponse> getSondeQuestion(@Header("Authorization") String token);

    @POST("MarketPlace/GetM3FilePath")
    Call<SondeGetUrlResponse> getSondeUrl(@Header("Authorization") String token,@Body SondeGetUrlRequest sondeGetUrlRequest);

    @POST("MarketPlace/GetM3FileScore")
    Call<GetMentalScoreResponse> getMentalScore(@Header("Authorization") String token, @Body GetMentalScoreRequest getMentalScoreRequest);

    @POST("MarketPlace/RespiratoryfirstfilePath")
    Call<SondeGetUrlResponse> getRespiratoryFirstFile(@Header("Authorization") String token, @Body SondeGetUrlRequest sondeGetUrlRequest);

    @POST("MarketPlace/RespiratoryfirstfilePathJob")
    Call<RespiratoryFirstJobResponse> respiratoryFirstJob(@Header("Authorization") String token, @Body RespiratoryFirstJobRequest respiratoryFirstJobRequest);


    @POST("MarketPlace/RespiratorySecondfilePath")
    Call<SondeGetUrlResponse> getRespiratorySecondFile(@Header("Authorization") String token, @Body SondeGetUrlRequest sondeGetUrlRequest);


    @POST("MarketPlace/RespiratorySecondfilePathJob")
    Call<RespiratorySecondJobResponse> respiratorySecondJob(@Header("Authorization") String token, @Body RespiratorySecondJobRequest respiratorySecondJobRequest);


    @POST("MarketPlace/AddSondeReminder")
    Call<SondeReminderResponse> setSondeReminder(@Header("Authorization") String token, @Body SondeReminderRequest sondeReminderRequest);

    @POST("dashboard/SaveClickEventHistory")
    Call<ClickEventResponse> clickEvent (@Header("Authorization") String token, @Body ClickEventRequest clickEventRequest);

    @POST("dashboard/AddContestReferal")
    Call<ReferalContestResponse> referalContest(@Header("Authorization") String token, @Body ReferalContestRequest referalContestRequest);

    @POST("MarketPlace/Createtranscribejob")
    Call<TranscribeJobResponse> createTranscribeJob(@Header("Authorization") String token, @Body TranscribeJobRequest transcribeJobRequest);

    @POST("MarketPlace/Polltranscribejob")
    Call<PollTranscribeResponse> pollTranscribeJob(@Header("Authorization") String token, @Body PollTranscribeRequest pollTranscribeRequest);

    @GET("Goals/GetDayGoal")
    Call<GetGoalDayResponse> getDayGoal(@Header("Authorization") String token);
    @GET("Goals/GetUserRecomendedGoals")
    Call<RecomendedGoalsResponse> getRecomendedGoals(@Header("Authorization") String token);

    @POST("Login/UpdateUserDetails")
    Call<UserDetailResponse> updateUserDetails(@Header("Authorization") String token, @Body UserDetailsRequest userDetailsRequest);

    @GET("MarketPlace/GetHappyMartCategories")
    Call<GetHappyMartTiles> getHappyMartTiles(@Header("Authorization") String token);

    @POST("MarketPlace/GetHappyMartSubCategories")
    Call<HappyMartCategoryResponse> getCategories (@Header("Authorization") String token,@Body HappyMartCategoryRequest happyMartCategoryRequest);

    @POST("dashboard/FaceScanRegistration")
    Call<FaceScanRegistrationResponse> faceScanRegistration (@Header("Authorization") String token, @Body FaceScanRegistrationRequest faceScanRegistrationRequest);

    @POST("dashboard/GetNudgeDetailsV2")
    Call<ResponseBody> getNudgeDetails(@Header("Authorization") String token, @Body GetNudgeDetailsRequest getNudgeDetailsRequest);

    @POST("Goals/AddUserIndividualGoal")
    Call<AddIndividualGoalResponse> addIndividualGoal (@Header("Authorization") String token, @Body AddIndividualGoalRequest addIndividualGoalRequest);

    @GET("Goals/GetUserIndividualGoalsV2")
    Call<GetIndividualGoalResponse> getIndividualGoal(@Header("Authorization") String token);

    @POST("Goals/ShowIndividualGoalOnDashboard")
    Call<AddGoalOnDashboardResponse> addGoalOnDashboard(@Header("Authorization") String token, @Body AddGoalOnDashboardRequest addGoalOnDashboardRequest);

    @GET("Dashboard/DownloadPdf")
    Call<DownloadFaceScanResponse> faceScanReport(@Header("Authorization") String token);

    @POST("Goals/DeleteIndividualGoal")
    Call<DeleteGoalResponse> deleteGoals(@Header("Authorization") String token, @Body DeleteGoalRequest deleteGoalRequest);

    @GET("Goals/IndividualGoalHistoryV2")
    Call<GoalHistoryResponse> goalsHistory(@Header("Authorization") String token);

    @POST("Community/SharefaceScanInTribe")
    Call<FaceScanInTribeResponse> shareFaceScan(@Header("Authorization") String token, @Body FacnScanInTribeRequest request);

    @POST("Trends/ActivityTracker")
    Call<ActivityTrackerResponse> ativityTracker(@Header("Authorization") String token, @Body ActivityTrackerRequest activityTrackerRequest);

    @POST("Challenges/EnrollUser")
    Call<EnrollUserResponse> enrollUser (@Header("Authorization") String token, @Body EnrollUserRequest enrollUserRequest);

    @GET("Challenges/GetEnrollDetailsV2")
    Call<EnrolledHistoryResponse> getEnrolledHistory(@Header("Authorization") String token);

    @POST("Challenges/GetChallengeRankingV3")
    Call<ChallengeRankResponse> getChallengeRanking (@Header("Authorization") String token, @Body ChallengeRankRequest challengeRankRequest);

    @GET("Challenges/GetMyBranchUserList")
    Call<GetBranchUserListResponse> getBranchUser(@Header("Authorization") String token);

    @GET("Challenges/GetWinners")
    Call<GetWalkathonWinnerResponse> getWalkathonWinner(@Header("Authorization") String token);

    @GET("dashboard/getUserDetailV4")
    Call<GetUserDetailResponse> getUserDeatils(@Header("Authorization") String token);

    @GET("dashboard/FetchNewDashboardData")
    Call<NewDashboardResponse> getDashBoardData(@Header("Authorization") String token);

    @GET("dashboard/GetRewardsList")
    Call<RewardListResponse> getRewardList(@Header("Authorization") String token);

    @GET("Login/quadrants")
    Call<GetQuadrantsResponse> getQuadrant();

    @POST("Login/Lockrewards")
    Call<LockRewardResponse> lockRewards(@Body LockRewardsRequest lockRewardsRequest);

    @POST("Login/Assignrewards")
    Call<AssignRewardsResponse> assignRewards(@Header("Authorization") String token, @Body AssignRewardsRequest assignRewardsRequest);
    //Policy Detail API
    @POST("PolicyServices/getClientByMobilePROD")
    Call<GetClientRes> getClientID(@Header("Authorization") String token, @Body GetClientIDReq mobileNumber);
    @POST("dashboard/AddUpdateMPIN")
    Call<GetClientRes> AddUpdateMPIN(@Header("Authorization") String token, @Body MPINReq mpinReq);

    @POST("PolicyServices/getPolicyDetailsPRODV1")
    Call<PolicyListResp> GetPolicyDetailsPROD(@Header("Authorization") String token, @Body PolicyListReq policyListReq);

    @POST("PolicyServices/GetPolicyOTP_V1")
    Call<GetClientRes> GetPolicyOTP(@Header("Authorization") String token, @Body GetOTPReq getOTPReq);

    @POST("PolicyServices/VerifyPolicyOTP_V1")
    Call<GetClientRes> GetPolicyOTP(@Header("Authorization") String token, @Body VerifyOTP verifyOTP);

    @POST("PolicyServices/getUnitLinkedPolicyStatementPROD")
    Call<ResponseBody> getUnitLinkedPolicyStatementPRO(@Header("Authorization") String token, @Body GetUnitLinkedPolicyStatementPROReq getUnitLinkedPolicyStatementPROReq);

    @POST("PolicyServices/getPremiumCertificatePROD")
    Call<ResponseBody> getPremiumCertificatePRO(@Header("Authorization") String token, @Body GetPremiumCertificateReq getPremiumCertificateReq);


    @POST("PolicyServices/getPolicyDocumentPROD")
    Call<ResponseBody> getPolicyDocumentPRO(@Header("Authorization") String token, @Body GetPolicyDocumentReq getPolicyDocumentReq);
     @POST("PolicyServices/getPDRPROD")
    Call<ResponseBody> getPDRPROD(@Header("Authorization") String token, @Body GetPDRPRODReq getPDRPRODReq);

    @GET("dashboard/FetchQuadrants")
    Call<PostLogRewardResponse> fetchQuadrants(@Header("Authorization") String token);
    @POST("dashboard/AssignSpinActivity")
    Call<SpinActivityResponse> assignSpinActivity(@Header("Authorization") String token,@Body SpinActivityrequest spinActivityrequest);

    @GET("dashboard/GetSpinActivityHistory")
    Call<EarnedRewardResponse> GetSpinActivityHistory(@Header("Authorization") String token);

    @POST("dashboard/StartSpinActivity")
    Call<CommonResponse> StartSpinActivity(@Header("Authorization") String token,@Body StartSpinActivityRequest activityRequest);
    @POST("HealthQuiz/StartQuizActivity")
    Call<CommonResponse> StartQuizActivity(@Header("Authorization") String token,@Body StartSpinActivityRequest activityRequest);
    @POST("dashboard/StartFeedbackActivity")
    Call<CommonResponse> StartFeedbackActivity(@Header("Authorization") String token,@Body StartSpinActivityRequest activityRequest);
    @POST("Login/SaveAppSessionLog")
    Call<CommonResponse> SaveAppSessionLog(@Header("Authorization") String token,@Body ApiSessionLogRequest apiSessionLogRequest);

    @POST("Login/GetEmailOTP")
    Call<GetEmailOTPResp> getEmailOTP(@Header("Authorization") String token,@Body GetEmailOTPReq apiSessionLogRequest);
    @POST("Login/VerifyEmailOTP")
    Call<VerifyEmailOTPResp> verifyEmailOTP(@Header("Authorization") String token, @Body VerifyEmailOTPReq apiSessionLogRequest);
    @POST("Login/SaveCorporateStatus")
    Call<GetEmailOTPResp> SaveCorporateStatus(@Header("Authorization") String token, @Body SaveCorporateStatusReq apiSessionLogRequest);
    @POST("Login/VerifyOTPV5")
    Call<ResponseBody> VerifyOTPV5(@Body VerifyOtpRequest request);

    @GET("dashboard/FetchNewDashboardData_V3")
    Call<NewDashboardResponse> getDashBoardDataV3(@Header("Authorization") String token);
    @GET("HealthQuiz/GetActiveQuizathonV2")
    Call<QuizathonResponseModel> GetActiveQuizathon(@Header("Authorization") String token);
    @POST("dashboard/ClaimRewardById")
    Call<CommonSuccessResponse> ClaimRewardById(@Header("Authorization") String token, @Body ClaimRewardRequest claimRewardRequest);
    @GET("HealthQuiz/GetQuizActivityList")
    Call<EarnedRewardResponse> GetQuizActivityList(@Header("Authorization") String token);
    @GET("HealthQuiz/GetQuizRewardList")
    Call<GetQuizScratchListResponse> getQuizRewardList(@Header("Authorization") String token);
    @POST("HealthQuiz/QuizScratchCard")
    Call<CommonSuccessResponse> QuizScratchCard(@Header("Authorization") String token, @Body QuizScratchRequest quizScratchRequest);
    @GET("dashboard/GetFilePopup")
    Call<FileShareResp> GetFilePopup(@Header("Authorization") String token);

    @POST("dashboard/UpdateFileStatus")
    Call<UpdateFileStatusResp> UpdateFileStatus(@Header("Authorization") String token, @Body UpdateFileStatusReq updateFileStatusReq);
    @POST ("HealthQuiz/BurnRegPoints")
    Call<BurnRegPointsResp>BurnRegPoints(@Header("Authorization")String token, @Body BurnRegPointsReq burnRegPointsReq);
    @GET ("dashboard/GetRewardsClaimListV1")
    Call<GetRewardsClaimListResp>GetRewardsClaimList(@Header("Authorization")String token);

    @POST ("HealthQuiz/FetchFeatureReward")
    Call<CommonSuccessResponse>FetchQuizReward(@Header("Authorization")String token, @Body ActivityRewardRequest burnRegPointsReq);

    @GET("HealthQuiz/GetFeedbackActivityList")
    Call<EarnedRewardResponse> GetFeedbackActivityList(@Header("Authorization") String token);

    @POST("Login/GetCorporateDetails")
    Call<GetEmailOTPResp> getCorporateDetails(@Header("Authorization") String token,@Body GetEmailOTPReq apiSessionLogRequest);
    @POST("Login/SaveCorporateDetails")
    Call<GetEmailOTPResp> SaveCorporateDetails(@Header("Authorization") String token,@Body SaveCorporateDetailsReq apiSessionLogRequest);
    @POST ("HealthQuiz/BurnQuizPoints")
    Call<BurnRegPointsResp>BurnQuizPoints(@Header("Authorization")String token, @Body BurnRegPointsReq burnRegPointsReq);
    @POST("Login/GetCitiesList")
    Call<GetCityListResponseModel> getCityList(@Header("Authorization") String token, @Body GetCityRequestModel getCityRequestModel);
    @GET("dashboard/GetActivityRewardsClaimListV1")
    Call<GetActivityRewardsClaimListResp> getActivityRewardsClaimList(@Header("Authorization") String token);
    @POST("Login/VerifyEmailMobileOTP")
    Call<VerifyEmailOTPResp> VerifyEmailMobileOTP(@Header("Authorization") String token, @Body VerifyEmailMobileOTPReq apiSessionLogRequest);
    @POST("Login/SaveCorporateDetailsV1")
    Call<GetEmailOTPResp> SaveCorporateDetailsV1(@Header("Authorization") String token,@Body SaveCorporateDetailsReqV1 apiSessionLogRequest);
    @Multipart
    @POST("HealthQuiz/UploadActivityImage")
    Call<CommonResponseWyh> UploadActivityImage(@Header("Authorization") String token, @Part("ActivityEvent") RequestBody activityEvent,
                                                @Part("ActivityDescription") RequestBody activityDescription,
                                                @Part("TransId") RequestBody transId,@Part MultipartBody.Part image);

}
