package com.wyh.happyyousdk.network;

import com.wyh.happyyousdk.dashboard.adapter.PinUnpinTileRequest;
import com.wyh.happyyousdk.dashboard.model.DashboardBottomCardData;
import com.wyh.happyyousdk.dashboard.model.DashboardBottomCardDataResponse;
import com.wyh.happyyousdk.dashboard.model.SaveBannerQuizRequestModel;
import com.wyh.happyyousdk.model.AlternativeNumberRequestModel;
import com.wyh.happyyousdk.model.request.AddUserFeedbackRequest;
import com.wyh.happyyousdk.model.request.AddVideoMinuteRequest;
import com.wyh.happyyousdk.model.request.DashboardRequest;
import com.wyh.happyyousdk.model.request.GetQuizathonDetailsRequest;
import com.wyh.happyyousdk.model.request.KgiUpdateDetailsRequest;
import com.wyh.happyyousdk.model.request.NewMyDiaryRequest;
import com.wyh.happyyousdk.model.request.FeedbackQuestionModelRequest;
import com.wyh.happyyousdk.model.request.SaveQuizRegistration;
import com.wyh.happyyousdk.model.request.SaveRegistrationAnswer;
import com.wyh.happyyousdk.model.request.SearchRequest;
import com.wyh.happyyousdk.dashboard.model.GetActoMeterTribeModel;
import com.wyh.happyyousdk.diary.model.DiaryEventListResponse;
import com.wyh.happyyousdk.model.request.AllocateVoucherRequest;
import com.wyh.happyyousdk.model.request.GetUserTypemodel;
import com.wyh.happyyousdk.model.request.Qrrequestmodel;
import com.wyh.happyyousdk.model.request.SearchResponse;
import com.wyh.happyyousdk.model.request.absorb.AddBookmarkRequest;
import com.wyh.happyyousdk.model.request.absorb.AddUserInterestRequest;
import com.wyh.happyyousdk.model.request.absorb.FetchBlogRequest;
import com.wyh.happyyousdk.model.request.absorb.GetDashboardDataRequest;
import com.wyh.happyyousdk.model.request.absorb.GetQuickReadRequest;
import com.wyh.happyyousdk.model.request.absorb.ShareBlogRequest;
import com.wyh.happyyousdk.model.request.absorb.VideoBookmarkRequest;
import com.wyh.happyyousdk.model.request.absorb.WebinarIdRequest;
import com.wyh.happyyousdk.model.request.addFamily.AddFamilyRequest;
import com.wyh.happyyousdk.model.request.diary.ImageDeleteRequest;
import com.wyh.happyyousdk.model.request.ehr.ConvertToBitlyRequest;
import com.wyh.happyyousdk.model.request.kgi_policy.GetPolicyDetailsRequest;
import com.wyh.happyyousdk.model.request.policy.AttachUserPolicyRequest;
import com.wyh.happyyousdk.model.request.policy.GetPolicyRequest;
import com.wyh.happyyousdk.model.request.profile.NickNameRequest;
import com.wyh.happyyousdk.model.request.quizathon.ActivityRewardRequest;
import com.wyh.happyyousdk.model.request.quizathon.RetakeQuestionRequest;
import com.wyh.happyyousdk.model.request.quizathon.SaveFeedbackAnswerRequest;
import com.wyh.happyyousdk.model.request.quizathon.SaveQuizathonAnswerRequest;
import com.wyh.happyyousdk.model.request.rewards.ActivityProgressRequest;
import com.wyh.happyyousdk.model.request.rewards.GetRedeemableAmountRequest;
import com.wyh.happyyousdk.model.request.rewards.MobileValidationRequest;
import com.wyh.happyyousdk.model.request.riddle.RiddleSaveRequest;
import com.wyh.happyyousdk.model.response.AddFamilyResponse;
import com.wyh.happyyousdk.model.response.AddReadingBlogDurationResponse;
import com.wyh.happyyousdk.model.response.AddVideoMinuteResponse;
import com.wyh.happyyousdk.model.response.AllocateVoucherResponse;
import com.wyh.happyyousdk.model.response.FunZoneHistoryResponse;
import com.wyh.happyyousdk.model.response.GetKliActivitiesResponse;
import com.wyh.happyyousdk.model.response.GetQuizathonDetailsResponse;
import com.wyh.happyyousdk.model.response.NewMyDiaryResponse;
import com.wyh.happyyousdk.model.response.QrResponse;
import com.wyh.happyyousdk.model.response.SaveQrResponse;
import com.wyh.happyyousdk.model.response.absorb.AddBookmarkResponse;
import com.wyh.happyyousdk.model.response.absorb.FetchBlogResponse;
import com.wyh.happyyousdk.model.response.absorb.GetDashboardDataResponse;
import com.wyh.happyyousdk.model.response.absorb.GetQuickReadResponse;
import com.wyh.happyyousdk.model.response.absorb.WebinarDetailsResponse;
import com.wyh.happyyousdk.model.request.MultipleGraphDataRequest;
import com.wyh.happyyousdk.model.response.MultipleGraphDataResponse;
import com.wyh.happyyousdk.model.response.SingleGraphDataResponse;
import com.wyh.happyyousdk.model.AddRemoveWidgetsRequest;
import com.wyh.happyyousdk.model.AddRemoveWidgetsResponse;
import com.wyh.happyyousdk.model.GetUserWidgetsResponse;
import com.wyh.happyyousdk.model.request.FetchTemperatureRequest;
import com.wyh.happyyousdk.model.response.FetchTemperatureResponse;
import com.wyh.happyyousdk.diary.model.DiaryListResponse;
import com.wyh.happyyousdk.model.request.diary.AddDiaryRequest;
import com.wyh.happyyousdk.model.request.diary.DeleteDiaryRequest;
import com.wyh.happyyousdk.model.request.diary.UpdateDairyRequest;
import com.wyh.happyyousdk.model.response.addmember.GetFamilyRelation;
import com.wyh.happyyousdk.model.response.dashboard.UsertypeDashboardData;
import com.wyh.happyyousdk.model.response.dashboard_new.DashboardResponseNew;
import com.wyh.happyyousdk.model.response.diary.AddDiaryResponse;
import com.wyh.happyyousdk.model.response.diary.DeleteDiaryResponse;
import com.wyh.happyyousdk.model.response.diary.DiaryDetailsResponse;
import com.wyh.happyyousdk.model.request.ehr.AddHealthRecordRequest;
import com.wyh.happyyousdk.model.request.ehr.DeleteIdRequest;
import com.wyh.happyyousdk.model.request.ehr.HealthRecordIdRequest;
import com.wyh.happyyousdk.model.response.ehr.AllHealthRecordDetailsList;
import com.wyh.happyyousdk.model.response.ehr.ConvertToBitlyResponse;
import com.wyh.happyyousdk.model.response.ehr.FetchDiagnosticTypeResponse;
import com.wyh.happyyousdk.model.response.ehr.FetchHealthRecordByIDResponse;
import com.wyh.happyyousdk.model.response.ehr.HealthRecordTypeResponse;
import com.wyh.happyyousdk.model.response.ehr.UploadFileResponse;
import com.wyh.happyyousdk.model.CHCustomerRegistrationModel;
import com.wyh.happyyousdk.model.HACustomerRegistrationRequest;
import com.wyh.happyyousdk.model.HACustomerRegistrationResponse;
import com.wyh.happyyousdk.model.MyOrderResponse;
import com.wyh.happyyousdk.model.request.heartAge.FetchHeartAgeQuestionsRequest;
import com.wyh.happyyousdk.model.request.heartAge.GetHeartAgeAnalysisRequest;
import com.wyh.happyyousdk.model.request.heartAge.HeartAgeConversationIdReq;
import com.wyh.happyyousdk.model.request.heartAge.SaveHeartAgeRequest;
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeAnalysisResponse;
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeConversationIdResponse;
import com.wyh.happyyousdk.model.request.ira.ConversationIdRequest;
import com.wyh.happyyousdk.model.request.ira.ConversationRequest;
import com.wyh.happyyousdk.model.request.ira.IRARequest;
import com.wyh.happyyousdk.model.request.ira.SaveIraRequest;
import com.wyh.happyyousdk.model.response.ira.IRAHealthScoreResponse;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.AddReferralRequest;
import com.wyh.happyyousdk.model.request.ConversationIdReq;
import com.wyh.happyyousdk.model.request.FetchRewardsRequest;
import com.wyh.happyyousdk.model.request.IntegrationIdRequest;
import com.wyh.happyyousdk.model.request.UuidRequest;
import com.wyh.happyyousdk.model.request.VoucherIdRequest;
import com.wyh.happyyousdk.model.request.absorb.AddBlogDurationRequest;
import com.wyh.happyyousdk.model.request.dass21.FetchDass21QuestionsRequest;
import com.wyh.happyyousdk.model.request.dass21.SaveDass21Request;
import com.wyh.happyyousdk.model.request.earnAndGrab.BurnTokenRequest;
import com.wyh.happyyousdk.model.request.earnAndGrab.StartActivityRequest;
import com.wyh.happyyousdk.model.request.encrDecr.EncryptionRequest;
import com.wyh.happyyousdk.model.request.faceScan.AddFaceScanVitalsRequest;
import com.wyh.happyyousdk.model.request.hra.GetAnalysisRequest;
import com.wyh.happyyousdk.model.request.hra.GetHraRequest;
import com.wyh.happyyousdk.model.request.hra.SaveHraRequest;
import com.wyh.happyyousdk.model.request.hraSection.newHRA.GetHRAAnswersRequest;
import com.wyh.happyyousdk.model.request.hraSection.newHRA.UpdateHRARequest;
import com.wyh.happyyousdk.model.request.ice.AddEmergencyDetailsReq;
import com.wyh.happyyousdk.model.request.ice.InjuryTypeReq;
import com.wyh.happyyousdk.model.request.login.AddFCMTokenRequest;
import com.wyh.happyyousdk.model.request.login.GetOtpRequest;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.request.login.VerifyOtpRequest;
import com.wyh.happyyousdk.model.request.qc.AllQCProductsRequest;
import com.wyh.happyyousdk.model.request.qc.QCProductDetailsRequest;
import com.wyh.happyyousdk.model.request.qc.placeOrder.QCPlaceOrderRequest;
import com.wyh.happyyousdk.model.request.registration.RegistrationRequest;
import com.wyh.happyyousdk.model.request.rewards.GetRewardsDashboardRequest;
import com.wyh.happyyousdk.model.request.rewards.RedeemRewardsRequest;
import com.wyh.happyyousdk.model.request.unwind.UnwindShareRequest;
import com.wyh.happyyousdk.model.response.CommonResponseWyh;
import com.wyh.happyyousdk.model.response.ConversationIdResponse;
import com.wyh.happyyousdk.model.response.FetchRewardsResponse;
import com.wyh.happyyousdk.model.response.ScratchAndWinResponse;
import com.wyh.happyyousdk.model.response.dashboard.DashboardResponse;
import com.wyh.happyyousdk.model.response.dass21.DassAnalysisResponse;
import com.wyh.happyyousdk.model.response.dass21.FetchDass21QuestionsResponse;
import com.wyh.happyyousdk.model.response.encrDecr.EncryptionResponse;
import com.wyh.happyyousdk.model.response.faceScan.FaceScanKeyData;
import com.wyh.happyyousdk.model.response.faceScan.FaceScanKeyResponse;
import com.wyh.happyyousdk.model.response.faceScan.FetchFaceScanVitalsResponse;
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeQuestionsResponse;
import com.wyh.happyyousdk.model.response.hra.GetHraAnswersResponse;
import com.wyh.happyyousdk.model.response.hra.SaveAnswersResponse;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.hraSection.newHRA.GetHRAAnswersResponse;
import com.wyh.happyyousdk.model.response.hraSection.newHRA.HealthScoreResponseNew;
import com.wyh.happyyousdk.model.response.ice.AddEmergencyDetailsResp;
import com.wyh.happyyousdk.model.response.ice.FetchCPRDetailsResp;
import com.wyh.happyyousdk.model.response.ice.FetchEmergencyDetailsResp;
import com.wyh.happyyousdk.model.response.ice.FetchInjuriesResp;
import com.wyh.happyyousdk.model.response.ice.FetchInjuryDetailsResp;
import com.wyh.happyyousdk.model.response.kgi_policy.GetPolicyDetailsResponse;
import com.wyh.happyyousdk.model.response.login.GetOtpResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.login.VerifyOtpResponse;
import com.wyh.happyyousdk.model.response.playwin.RetakeResponseModel;
import com.wyh.happyyousdk.model.response.playwin.SaveQuizRegistrationResponse;
import com.wyh.happyyousdk.model.response.qc.QCOrderResponse;
import com.wyh.happyyousdk.model.response.qc.allProducts.AllQCProductsResponse;
import com.wyh.happyyousdk.model.response.qc.productDescription.QCProductDetailsResponse;
import com.wyh.happyyousdk.model.response.notification.NotificationResponse;
import com.wyh.happyyousdk.model.PolicyDetailsRequest;
import com.wyh.happyyousdk.model.PolicyDetailsResponse;
import com.wyh.happyyousdk.model.response.ProfileDetailsResponse;
import com.wyh.happyyousdk.model.request.GetQuestionRequest;
import com.wyh.happyyousdk.model.request.SaveQuizRequest;
import com.wyh.happyyousdk.model.response.GetQuizResponseModel;
import com.wyh.happyyousdk.model.response.SaveQuizResponseModel;
import com.wyh.happyyousdk.model.request.AddReminderRequest;
import com.wyh.happyyousdk.model.request.DeleteReminderRequest;
import com.wyh.happyyousdk.model.request.UpdateReminderSettingRequest;
import com.wyh.happyyousdk.model.response.FetchRemindersByUUIDResponse;
import com.wyh.happyyousdk.model.request.rewards.AppFeedbackRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsHistoryRequest;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.request.rewards.StartRewardsActivityRequest;
import com.wyh.happyyousdk.model.response.quiz.TriviaHistoryResponse;
import com.wyh.happyyousdk.model.response.quizathon.SaveQuizathonAnsResponse;
import com.wyh.happyyousdk.model.response.rewards.ActivityProgressResponse;
import com.wyh.happyyousdk.model.response.rewards.AllCollectiblesResponse;
import com.wyh.happyyousdk.model.response.rewards.AllVouchersResponse;
import com.wyh.happyyousdk.model.response.rewards.EandBDashboardResponse;
import com.wyh.happyyousdk.model.response.rewards.MobileValidationResponse;
import com.wyh.happyyousdk.model.response.rewards.PendingActivitiesResponse;
import com.wyh.happyyousdk.model.response.rewards.RedeemableAmountResponse;
import com.wyh.happyyousdk.model.response.rewards.RewardsHistoryResponse;
import com.wyh.happyyousdk.model.response.rewards.LevelDashboardResponse;
import com.wyh.happyyousdk.model.request.trends.AddReminderDataRequest;
import com.wyh.happyyousdk.model.request.trends.AddWeightRequest;
import com.wyh.happyyousdk.model.request.trends.FetchGraphRequest;
import com.wyh.happyyousdk.model.request.trends.GetCalorieDataRequest;
import com.wyh.happyyousdk.model.response.trends.CalorieBurnedResponse;
import com.wyh.happyyousdk.model.response.trends.CalorieDataResponse;
import com.wyh.happyyousdk.model.response.trends.FetchGraphResponse;
import com.wyh.happyyousdk.model.response.AffirmationResponse;
import com.wyh.happyyousdk.model.response.JokeOfTheDayResponse;
import com.wyh.happyyousdk.model.response.UnwindDashboardDataResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterfaceWyh {

    //Encryption
    @POST("EncryptDecrypt/RSAEncrypt")
    Call<EncryptionResponse> encryptRSA(@Body EncryptionRequest request);

    @POST("EncryptDecrypt/RSADecrypt")
    Call<EncryptionResponse> decryptRSA(@Body EncryptionRequest request);

    //AES Decrypt
    @POST("EncryptDecrypt/AESDecrypt")
    Call<EncryptionResponse> decryptAES(@Body EncryptionRequest request);

    //AES Encrypt
    @POST("EncryptDecrypt/AESEncrypt")
    Call<EncryptionResponse> encryptAES(@Body EncryptionRequest request);

    //Register User
    @POST("Login/RegisterUserV4")
    Call<VerifyOtpResponse> registerUserV4(@Body RegistrationRequest request);
    @POST("Login/RegisterKmamcUserV4")
    Call<VerifyOtpResponse> registerKMAMCUser(@Body RegistrationRequest request);

    //Verify OTP
    @POST("Login/VerifyOTPV2")
    Call<VerifyOtpResponse> verifyOTP(@Body VerifyOtpRequest request);

    //Refresh Auth Token
    @POST("Login/RefreshTokenV3")
    Call<RefreshTokenResponse> refreshToken(@Header("Authorization") String token, @Body RefreshTokenRequest request);

    @POST("Rewards/AddReferralDetails")
    Call<CommonSuccessResponse> addReferral(@Header("Authorization") String token, @Body AddReferralRequest request);

    //DASS-21 Start

    @POST("psychometrictest/GetQuestions")
    Call<FetchDass21QuestionsResponse> getDass21Questions(@Header("Authorization") String token, @Body FetchDass21QuestionsRequest request);

    @POST("psychometrictest/SaveAnswerNew")
    Call<CommonSuccessResponse> saveDass21(@Header("Authorization") String token, @Body SaveDass21Request request);

    @POST("Dashboard/DashboardSearch")
    Call<SearchResponse> dashboardSearch(@Header("Authorization") String Token, @Body SearchRequest body);

    @POST("psychometrictest/GetAnalysis")
    Call<DassAnalysisResponse> getDassAnalysis(@Header("Authorization") String token, @Body IntegrationIdRequest request);


    //Dashboard Start
    @GET("dashboard/fetchdashboarddata")
    Call<DashboardResponse> getDashboardDetails(@Header("Authorization") String token);

    //Dashboard New API
    @POST("dashboard/FetchAllDashboardData")
    Call<DashboardResponseNew> getDashboardDetailsNew(@Header("Authorization") String token, @Body DashboardRequest request);


    @POST("dashboard/GetUserType")
    Call<UsertypeDashboardData> getUserType(@Header("Authorization") String token, @Body GetUserTypemodel request);


    //Dashboard End

    //HRA Start
    @POST("HRA/GetHRAAnswers")
    Call<GetHraAnswersResponse> getHraAnswers(@Header("Authorization") String token, @Body GetHraRequest request);

    @POST("HRA/CreateFreshConversationNew")
    Call<ConversationIdResponse> createConversationI(@Header("Authorization") String token, @Body ConversationIdReq request);

    @POST("HRA/SaveHRAAnswers")
    Call<SaveAnswersResponse> saveHRA(@Header("Authorization") String token, @Body SaveHraRequest request);

    @POST("HRA/GetAnalysis")
    Call<GetAnalysisResponse> getAnalysis(@Header("Authorization") String token, @Body GetAnalysisRequest request);

    @POST("dashboard/FetchWeather")
    Call<FetchTemperatureResponse> fetchTemperatureData(@Header("Authorization") String token, @Body FetchTemperatureRequest request);

    @POST("HRA/GetHRAAnswersNew")
    Call<GetHRAAnswersResponse> getHRAAnswers(@Body GetHRAAnswersRequest request);

    //Update HRA
    @POST("HRA/SaveAnswerNew")
    Call<CommonResponseWyh> updateHRA(@Body UpdateHRARequest request);

    //Get Health score new
    @POST("HRA/GetHealthScoreNew")
    Call<HealthScoreResponseNew> getHealthScoreNew(@Body UuidRequest request);


    //HRA End

    //IRA
    @POST("HRA/GetHRAAnswers")
    Call<GetHRAAnswersResponse> getIRAQuestions(@Header("Authorization") String token, @Body IRARequest req);

    //GET Conversation ID
    @POST("HRA/CreateFreshConversationNew")
    Call<ConversationIdResponse> getConversationId(@Header("Authorization") String token, @Body ConversationIdRequest req);

    //FetchInjuries
    @GET("ICE/FetchInjuries")
    Call<FetchInjuriesResp> fetchInjury(@Header("Authorization") String token);

    //FetchCPRDetails
    @GET("ICE/FetchCPRDetails")
    Call<FetchCPRDetailsResp> fetchCPRDetails(@Header("Authorization") String token);

    //FetchCPRDetails
    @GET("ICE/FetchEmergencyDetails")
    Call<FetchEmergencyDetailsResp> fetchEmergencyDetails(@Header("Authorization") String token);

    //FetchCPRDetails
    @POST("ICE/FetchInjuriyDetails")
    Call<FetchInjuryDetailsResp> fetchInjuriyDetails(@Header("Authorization") String token, @Body InjuryTypeReq request);

    @POST("ICE/AddEmergencyDetails")
    Call<AddEmergencyDetailsResp> addEmergencyContact(@Header("Authorization") String token, @Body AddEmergencyDetailsReq request);

    //save IRA answers
    @POST("HRA/SaveIRAAnswers")
    Call<SaveAnswersResponse> saveIRAAnswer(@Header("Authorization") String token, @Body SaveIraRequest req);

    @POST("HRA/FetchIRAAnswersScore")
    Call<IRAHealthScoreResponse> getIRAHealthScore(@Header("Authorization") String token, @Body ConversationRequest req);

    //Fetch Graph
    @POST("Trends/FetchGraph")
    Call<FetchGraphResponse> fetchGraph(@Header("Authorization") String Token, @Body FetchGraphRequest body);

    //Ehr
    @GET("EHR/fetchAllHealthRecord")
    Call<AllHealthRecordDetailsList> fetchAllHealthRecords(@Header("Authorization") String Token);

    //Ehr Health record Types
    @GET("EHR/FetchHealthRecordTypes")
    Call<HealthRecordTypeResponse> fetchHealthRecordTypes(@Header("Authorization") String Token);

    @Multipart
    @POST("EHR/UploadFiles")
    Call<UploadFileResponse> ehrFileUploadResponse(@Header("Authorization") String Token, @Part List<MultipartBody.Part> files);

    @POST("EHR/DeleteHealthRecord")
    Call<CommonSuccessResponse> deleteHealthRecord(@Header("Authorization") String Token, @Body DeleteIdRequest body);

    @POST("EHR/AddHealthRecord")
    Call<CommonSuccessResponse> addHealthRecord(@Header("Authorization") String Token, @Body AddHealthRecordRequest body);

    @POST("EHR/UpdateHealthRecord")
    Call<CommonSuccessResponse> updateHealthRecord(@Header("Authorization") String Token, @Body FetchHealthRecordByIDResponse.Data body);

    @POST("EHR/UpdateHealthRecord")
    Call<CommonSuccessResponse> deleteHealthRecord(@Header("Authorization") String Token, @Body UploadFileResponse.Datum body);

    //Ehr
    @POST("EHR/FetchHealthRecordByType")
    Call<AllHealthRecordDetailsList> fetchEhrHealthRecordsByType(@Header("Authorization") String Token, @Body HealthRecordIdRequest body);

    //Add Water And Meditation
    @POST("Reminder/logReminder")
    Call<CommonSuccessResponse> addActivityData(@Header("Authorization") String Token, @Body AddReminderDataRequest body);

    //Absorb
    @POST("Absorb/Dashboard")
    Call<GetDashboardDataResponse> getAbsorbDashboard(@Header("Authorization") String Token, @Body GetDashboardDataRequest body);

    //Quick read
    @POST("Absorb/AllItems")
    Call<GetQuickReadResponse> getQuickReadDashboard(@Header("Authorization") String Token, @Body GetQuickReadRequest body);

    @POST("Absorb/FetchBlogs")
    Call<FetchBlogResponse> fetchBlogAPI(@Header("Authorization") String Token, @Body FetchBlogRequest body);

    @POST("Absorb/FecthWebinarDetails")
    Call<WebinarDetailsResponse> fetchWebinarDetails(@Header("Authorization") String Token, @Body WebinarIdRequest body);

    @POST("Webinar/AddUserInterest")
    Call<CommonSuccessResponse> addUserInterest(@Header("Authorization") String Token, @Body AddUserInterestRequest body);

    @GET(" ")
    Call<AffirmationResponse> getAffirmations();

    @GET("Programming,Miscellaneous,Pun?blacklistFlags=nsfw,religious,political,racist,sexist,explicit&format=json")
    Call<JokeOfTheDayResponse> getJokeOfTheDay();

    @POST("Absorb/BlogReadingDuration")
    Call<AddReadingBlogDurationResponse> blogReadingDuration(@Header("Authorization") String Token, @Body AddBlogDurationRequest body);

    @POST("DietTracker/GetDashboardData")
    Call<CalorieDataResponse> getCalorieConsumedDashboardData(@Header("Authorization") String Token, @Body GetCalorieDataRequest body);

    //Rewards Start

    @POST("Rewards/GetRewardsDashBoardData")
    Call<LevelDashboardResponse> getRewardsDashboardData(@Header("Authorization") String Token, @Body GetRewardsDashboardRequest request);

    @GET("EnG/EnGUserDashboard")
    Call<EandBDashboardResponse> getEandBDashboardData(@Header("Authorization") String Token);

    @POST("EnG/PendingEvents")
    Call<PendingActivitiesResponse> getAllActivities(@Header("Authorization") String Token);

    @POST("EnG/AvailableVoucher")
    Call<AllVouchersResponse> getAllAvailableVouchers(@Header("Authorization") String Token);

    @POST("EnG/StartActivity")
    Call<CommonSuccessResponse> startEAndGActivity(@Header("Authorization") String Token, @Body StartActivityRequest request);

    @POST("Rewards/StartActivity")
    Call<CommonSuccessResponse> startRewardsActivity(@Header("Authorization") String Token, @Body StartRewardsActivityRequest request);

    @POST("Rewards/StartSnWActivity")
    Call<ScratchAndWinResponse> startScratchAndWin(@Header("Authorization") String Token, @Body StartRewardsActivityRequest request);

    @POST("EnG/BurnTokens")
    Call<CommonSuccessResponse> burnStamps(@Header("Authorization") String Token, @Body BurnTokenRequest request);

    @Multipart
    @POST("EnG/UploadActivityFiles")
    Call<CommonSuccessResponse> uploadEAndGFiles(@Header("Authorization") String Token, @Part List<MultipartBody.Part> files,
                                                 @Part("ActivityId") int activityId);

    @Multipart
    @POST("Rewards/EarnRewards")
    Call<CommonSuccessResponse> uploadLevelFiles(@Header("Authorization") String Token, @Part List<MultipartBody.Part> files,
                                                 @Part("eventId") int eventId, @Part("eventType") String eventType, @Part("eventCategory") String eventCategory,
                                                 @Part("eventName") String eventName, @Part("yesNo") String yesNo, @Part("journalContent") String journalContent);

    @Multipart
    @POST("Rewards/EarnRewards")
    Call<CommonSuccessResponse> earnRewards(@Header("Authorization") String Token, @Part("eventCategory") String eventCategory,
                                            @Part("eventName") String eventName);

    @POST("Rewards/RedeemRewards")
    Call<CommonSuccessResponse> updateUserRewards(@Header("Authorization") String token, @Body RedeemRewardsRequest request);

    @POST("FB/ScratchCoupon")
    Call<CommonSuccessResponse> updateScratchStatus(@Header("Authorization") String token, @Body VoucherIdRequest request);

    @POST("Rewards/GetRewardsHistoryData")
    Call<RewardsHistoryResponse> getRewardsHistory(@Header("Authorization") String token, @Body RewardsHistoryRequest request);

    @GET("Rewards/GetUserCollectibels")
    Call<AllCollectiblesResponse> getAllCollectibles(@Header("Authorization") String token);

    @POST("Rewards/FetchRewards")
    Call<FetchRewardsResponse> fetchRewards(@Header("Authorization") String token, @Body FetchRewardsRequest request);

    @POST("Rewards/GetActivityProgress")
    Call<ActivityProgressResponse> getActivityProgress(@Header("Authorization") String token, @Body ActivityProgressRequest request);
    @POST("dashboard/SaveBannerRegistrationV1")
    Call<CommonSuccessResponse> SaveBannerRegistration(@Header("Authorization") String token, @Body SaveRegistrationAnswer request);

    @POST("Rewards/GetRedeemableAmount")
    Call<RedeemableAmountResponse> getRedeemableAmount(@Header("Authorization") String token, @Body GetRedeemableAmountRequest request);

    @POST("HealthQuiz/SaveQuizRegistration")
    Call<SaveQuizRegistrationResponse> SaveQuizRegistration(@Header("Authorization") String token, @Body SaveQuizRegistration request);

    //Rewards End


    //QC Start

    @POST("QC/QCGetProductList")
    Call<AllQCProductsResponse> getAllQCProducts(@Header("Authorization") String token, @Body AllQCProductsRequest request);

    @POST("QC/QCGetProductDetails")
    Call<QCProductDetailsResponse> getProductDetails(@Header("Authorization") String token, @Body QCProductDetailsRequest request);

    @POST("QC/QCPlaceOrder")
    Call<CommonSuccessResponse> placeQcOrder(@Header("Authorization") String token, @Body QCPlaceOrderRequest request);

    //QC End

    //FaceScan Start
    @POST("dashboard/addFaceScanVitals")
    Call<CommonSuccessResponse> addFaceScanVitals(@Header("Authorization") String token, @Body AddFaceScanVitalsRequest request);

    @GET("dashboard/GetSocketKeys")
    Call<FaceScanKeyResponse> getFaceScanKey(@Header("Authorization") String token);

    @GET("access/keys")
    Call<List<FaceScanKeyData>> getFaceScanKeyDirect(@Header("Authorization") String token);

    @Headers("Authorization: Basic d3loLXVzZXI6VHFLcXdlY3NjZGZlY3Nsa2ZyU214UzVh")
    @GET("access/keys")
    Call<List<FaceScanKeyData>> getFaceScanKeyDirectNoHeader(@HeaderMap Map<String, String> headermap);

    @GET("dashboard/getFaceScanVitals")
    Call<FetchFaceScanVitalsResponse> getFaceScanVitals(@Header("Authorization") String token);

    //FaceScan End

    @POST("Reminder/FetchReminderByUUID")
    Call<FetchRemindersByUUIDResponse> getRemindersByUUID(@Header("Authorization") String Token, @Body UuidRequest body);

    @POST("Trends/FetchUserGraph")
    Call<SingleGraphDataResponse> fetchIndividualGraph(@Header("Authorization") String Token, @Body MultipleGraphDataRequest body);

    //Fetch Graph
    @POST("Trends/FetchGraphCommunityWise")
    Call<MultipleGraphDataResponse> fetchGraphMultiple(@Header("Authorization") String Token, @Body MultipleGraphDataRequest body);

    //Add FCM Token
    @POST("Login/AddFCMToken")
    Call<CommonSuccessResponse> addFCMToken(@Header("Authorization") String token, @Body AddFCMTokenRequest req);


    //Search Policy Details
    @POST("Profile/SearchPolicy")
    Call<PolicyDetailsResponse> searchPolicies(@Header("Authorization") String token, @Body PolicyDetailsRequest request);


    //AddQrdata
    @POST("Profile/SaveQRToken")
    Call<SaveQrResponse> saveQRToken(@Header("Authorization") String token, @Body Qrrequestmodel request);

    @GET("Profile/FetchQRToken")
    Call<QrResponse> fetchQRToken(@Header("Authorization") String token);

    //Unwind
    @GET("unwind/Dashboard")
    Call<UnwindDashboardDataResponse> getUnwindDashboardData(@Header("Authorization") String token);

    @POST("unwind/Share")
    Call<CommonSuccessResponse> unwindShareTrack(@Header("Authorization") String token, @Body UnwindShareRequest request);

    @GET("Profile/GetProfileDetails")
    Call<ProfileDetailsResponse> getUserDetails(@Header("Authorization") String token);

    //Diary List
    @GET("UserJournal/fetchAllUserJournals")
    Call<DiaryListResponse> fetchAllUserJournals(@Header("Authorization") String token);

    @POST("UserJournal/GetAllUserJournalsMyDiary")
    Call<NewMyDiaryResponse> fetchAllUserJournalsMyDiary(@Header("Authorization") String token, @Body NewMyDiaryRequest newMyDiaryRequest);

    //Add Diary
    @POST("UserJournal/Add")
    Call<AddDiaryResponse> addDiary(@Header("Authorization") String token, @Body AddDiaryRequest request);

    //delete Diary
    @POST("UserJournal/Delete")
    Call<DeleteDiaryResponse> deleteDiary(@Header("Authorization") String token, @Body DeleteDiaryRequest request);

    //update Diary
    @POST("UserJournal/Update")
    Call<DeleteDiaryResponse> updateDiary(@Header("Authorization") String token, @Body UpdateDairyRequest request);

    //View Diary
    @POST("UserJournal/FetchJournalDetails")
    Call<DiaryDetailsResponse> viewDiary(@Header("Authorization") String token, @Body DeleteDiaryRequest request);

    //getQuestion
    @POST("HealthQuiz/GetQuestionsV3")
    Call<GetQuizResponseModel> getQuestions(@Header("Authorization") String Token, @Body GetQuestionRequest request);

    @POST("HealthQuiz/GetQuestionsV2")
    Call<GetQuizResponseModel> getQuestionsV2(@Header("Authorization") String Token, @Body GetQuestionRequest request);

    @POST("HealthQuiz/SaveAnswerNewV1")
    Call<SaveQuizResponseModel> saveQuestions(@Header("Authorization") String Token, @Body SaveQuizRequest request);
    @POST("HealthQuiz/SaveQuizAnswer")
    Call<SaveQuizathonAnsResponse> saveQuizathonAnswer(@Header("Authorization") String Token, @Body SaveQuizathonAnswerRequest request);
    @POST("dashboard/SaveUserfeedbackV1")
    Call<SaveQuizathonAnsResponse> SaveUserfeedback(@Header("Authorization") String Token, @Body SaveFeedbackAnswerRequest request);

    @POST("HeartAge/GetAnswersNew")
    Call<HeartAgeQuestionsResponse> getHeartAgeQuestions(@Header("Authorization") String token, @Body FetchHeartAgeQuestionsRequest request);

    @POST("HeartAge/SaveAnswers")
    Call<CommonSuccessResponse> saveHeartAgeAnswers(@Header("Authorization") String token, @Body SaveHeartAgeRequest request);

    @POST("HeartAge/CreateFreshConversation")
    Call<HeartAgeConversationIdResponse> createHeartAgeConversationID(@Header("Authorization") String token, @Body HeartAgeConversationIdReq request);

    @POST("HeartAge/GetAnalysis")
    Call<HeartAgeAnalysisResponse> getHeartAgeAnalysis(@Header("Authorization") String token, @Body GetHeartAgeAnalysisRequest request);

    @GET("Profile/NotificationDrawer")
    Call<NotificationResponse> getNotificationData(@Header("Authorization") String token);

    @POST("Reminder/CreateReminder")
    Call<CommonSuccessResponse> setReminderAPI(@Header("Authorization") String Token, @Body AddReminderRequest body);

    @POST("Reminder/UpdateReminderSettings")
    Call<CommonSuccessResponse> updateReminderAPI(@Header("Authorization") String Token, @Body List<UpdateReminderSettingRequest> body);

    @POST("Reminder/DeleteReminder")
    Call<CommonSuccessResponse> deleteReminder(@Header("Authorization") String token, @Body DeleteReminderRequest request);

    @POST("MarketPlace/HACustomerRegistration")
    Call<HACustomerRegistrationResponse> getHACustomerRegistration(@Header("Authorization") String token, @Body HACustomerRegistrationRequest request);

    @POST("Blogs/AddBookmark")
    Call<AddBookmarkResponse> addBookMark(@Header("Authorization") String token, @Body AddBookmarkRequest request);

    @POST("MarketPlace/CHCustomerRegistration")
    Call<CHCustomerRegistrationModel> cHCustomerRegistration(@Header("Authorization") String token, @Body HACustomerRegistrationRequest request);

    @GET("Widgets/GetUserWidgets")
    Call<GetUserWidgetsResponse> getUserWidget(@Header("Authorization") String token);

    @POST("Widgets/AddRemoveWidget")
    Call<AddRemoveWidgetsResponse> addRemoveWidget(@Header("Authorization") String token, @Body List<AddRemoveWidgetsRequest> request);

    @GET("MarketPlace/GetAllOrders")
    Call<MyOrderResponse> getAllOrders(@Header("Authorization") String token);

    @POST("Trends/AddWeight")
    Call<CommonSuccessResponse> addWeight(@Header("Authorization") String token, @Body AddWeightRequest request);

    @POST("Profile/AppFeedback")
    Call<CommonSuccessResponse> appFeedback(@Header("Authorization") String token, @Body AppFeedbackRequest request);

    @POST("Rewards/RewardsPopup")
    Call<CommonSuccessResponse> rewardsPopup(@Header("Authorization") String token, @Body RewardsPopupRequest request);

    @GET("QC/QCOrderList")
    Call<QCOrderResponse> getQCOrderList(@Header("Authorization") String token);

    @POST("Rewards/TopUpFeedback")
    Call<CommonSuccessResponse> topUpFeedback(@Header("Authorization") String token, @Body AppFeedbackRequest request);

    @POST("EnG/ScratchTokenReward")
    Call<CommonSuccessResponse> scratchTokenReward(@Header("Authorization") String token, @Body RewardsPopupRequest activityId);

    @POST("EHR/fetchHealthRecordByID")
    Call<FetchHealthRecordByIDResponse> fetchHealthRecordByID(@Header("Authorization") String token, @Body AllHealthRecordDetailsList.Datum activityId);

    @POST("dashboard/FetchDashboardTilesV2")
    Call<DashboardBottomCardDataResponse> fetchDashboardTiles(@Header("Authorization") String token, @Body DashboardBottomCardData body);

    @POST("dashboard/PinUnpinTile")
    Call<CommonSuccessResponse> pinUnpinTile(@Header("Authorization") String token, @Body PinUnpinTileRequest body);

    @GET("UserJournal/FetchPendingJournalUploadEvents")
    Call<DiaryEventListResponse> getDiaryEventList(@Header("Authorization") String token);

    @GET("EHR/FetchDiagnosticType")
    Call<FetchDiagnosticTypeResponse> fetchDiagnosticType(@Header("Authorization") String token);


    @POST("Login/ConvertToBitly")
    Call<ConvertToBitlyResponse> getConvertToBitly(@Header("Authorization") String Token, @Body List<ConvertToBitlyRequest> body);


    @POST("Profile/UpdateNickName")
    Call<CommonSuccessResponse> updateNickName(@Header("Authorization") String Token, @Body NickNameRequest body);

    @POST("Blogs/Bookmark")
    Call<CommonSuccessResponse> addBookmarkVideo(@Header("Authorization") String Token, @Body VideoBookmarkRequest body);

    @POST("UserJournal/DeleteJournalFile")
    Call<CommonSuccessResponse> imageDeleteDiary(@Header("Authorization") String Token, @Body ImageDeleteRequest body);

    @POST("Reminder/SetReminder")
    Call<CommonSuccessResponse> setAllReminderAPI(@Header("Authorization") String Token, @Body List<AddReminderRequest> body);

    @GET("unwind/FunZoneHistory")
    Call<FunZoneHistoryResponse> getFunZoneHistory(@Header("Authorization") String token);

    @POST("unwind/SaveUserResponse")
    Call<CommonSuccessResponse> unwindSave(@Header("Authorization") String auth, @Body RiddleSaveRequest request);

    @POST("Profile/DeleteProfileImage")
    Call<CommonSuccessResponse> deleteProfileImage(@Header("Authorization") String auth);

    @POST("HRA/CheckHraDraft")
    Call<ConversationIdResponse> checkHraDraft(@Header("Authorization") String token, @Body ConversationIdReq request);


    @POST("Absorb/UpdateBlogReadingDuration")
    Call<CommonSuccessResponse> updateBlogReadingDuration(@Header("Authorization") String Token, @Body AddBlogDurationRequest body);

    @POST("dashboard/AddClientFamilyDetails")
    Call<CommonSuccessResponse> addClientFamilyDetails(@Header("Authorization") String Token, @Body List<AddFamilyRequest> body);

    @POST("dashboard/DeletePolicyDetails")
    Call<CommonSuccessResponse> deletePolicyDetails(@Header("Authorization") String Token, @Body AddFamilyRequest body);

    @GET("dashboard/GetClientFamilyDetails")
    Call<AddFamilyResponse> getClientFamilyDetails(@Header("Authorization") String Token);

    @POST("dashboard/GetuserPolicy")
    Call<PolicyDetailsResponse> getPolicy(@Header("Authorization") String Token, @Body GetPolicyRequest body);

    @POST("dashboard/AttachUserPolicy")
    Call<CommonSuccessResponse> attachUserPolicy(@Header("Authorization") String Token, @Body AttachUserPolicyRequest body);
    @POST("dashboard/SaveQuizBannerHistory")
    Call<SaveQuizResponseModel> saveQuizBannerHistory(@Header("Authorization") String Token, @Body SaveBannerQuizRequestModel body);

    @GET("Profile/GetBookmarkedHealthTvs")
    Call<ProfileDetailsResponse> getProfileBookmark(@Header("Authorization") String token);

    @GET("FB/GetKliActivities")
    Call<GetKliActivitiesResponse> getKliActivities(@Header("Authorization") String token);

    @POST("FB/AllocateVoucher")
    Call<AllocateVoucherResponse> allocateVoucher(@Header("Authorization") String Token, @Body AllocateVoucherRequest body);


    @POST("Absorb/AddSearchHistory")
    Call<CommonSuccessResponse> addSearchHistory(@Header("Authorization") String Token, @Body SearchRequest body);

    @POST("Absorb/AddVideoSeenDuration")
    Call<AddVideoMinuteResponse> addVideoMinute(@Header("Authorization") String token, @Body AddVideoMinuteRequest addVideoMinuteRequest);

    @GET("dashboard/GetActoMeterTribe")
    Call<GetActoMeterTribeModel> getActoMeterTribe(@Header("Authorization") String token, @Query("commId") String id);


    //kgi
    @POST("Login/RegisterUser")
    Call<VerifyOtpResponse> kgiRegisterUser(@Body RegistrationRequest request);


    @POST("Login/GetPolicyDetailsV2")
    Call<GetPolicyDetailsResponse> kgiPolicyDetails(@Body GetPolicyDetailsRequest request);

    @POST("Login/UpdateUserDetails")
    Call<CommonSuccessResponse> kgiUpdateDetails(@Header("Authorization") String token,@Body KgiUpdateDetailsRequest request);

    @POST("Login/AddOrUpdateAlternateNumber")
    Call<CommonSuccessResponse> addOrUpdateAlternateNumber(@Header("Authorization") String token, @Body AlternativeNumberRequestModel request);

    @POST("dashboard/AddUserFeedback")
    Call<CommonSuccessResponse> addUserFeedback(@Header("Authorization") String token, @Body AddUserFeedbackRequest request);

    @POST("dashboard/AddCustomUserFeedback")
    Call<CommonSuccessResponse> customFeedback(@Header("Authorization") String token, @Body FeedbackQuestionModelRequest request);

    @GET("dashboard/GetLoginRewards")
    Call<CommonSuccessResponse> getLoginRewards(@Header("Authorization") String token);

    @GET("HealthQuiz/GetTriviaHistory")
    Call<TriviaHistoryResponse> GetTriviaHistory (@Header("Authorization") String token);


    @POST("HealthQuiz/GetRetakeQuestion")
    Call<RetakeResponseModel> GetRetakeQuestion (@Header("Authorization") String token, @Body RetakeQuestionRequest retakeQuestionRequest);

    @POST("HealthQuiz/GetQuizathonDetails")
    Call<GetQuizathonDetailsResponse> getQuizthonDetails (@Header("Authorization") String token, @Body GetQuizathonDetailsRequest getQuizathonDetailsRequest);


    @POST ("HealthQuiz/FetchFeatureReward")
    Call<CommonSuccessResponse>FetchQuizReward(@Header("Authorization")String token, @Body ActivityRewardRequest burnRegPointsReq);
    @POST("HealthQuiz/SaveStreakQuizAnswer")
    Call<SaveQuizathonAnsResponse> SaveStreakQuizAnswer(@Header("Authorization") String Token, @Body SaveQuizathonAnswerRequest request);
    @GET("Login/GetFamilyRelation")
    Call<GetFamilyRelation> getFamilyRelation(@Header("Authorization") String token);

    @POST("Community/ValidateMobile")
    Call<MobileValidationResponse> validateMobileNumbers(@Header("Authorization") String token, @Body List<MobileValidationRequest> request);
}