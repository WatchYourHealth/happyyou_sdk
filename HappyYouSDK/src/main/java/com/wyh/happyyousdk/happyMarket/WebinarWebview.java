package com.wyh.happyyousdk.happyMarket;

import static android.os.Build.VERSION.SDK_INT;
import static com.wyh.happyyousdk.utils.CommonUtils.downloadAndViewFile;
import static com.wyh.happyyousdk.utils.CommonUtils.downloadImage;
import static com.wyh.happyyousdk.utils.CommonUtils.formatDateFromString;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.ChallangesModule.Activities.BadgesActivity;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.rewardDialogCloseListener;
import com.wyh.happyyousdk.WebActivity;
import com.wyh.happyyousdk.WellBeingActivity;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.NudgeDialogue;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.CustomPopupStampsBinding;
;
import com.wyh.happyyousdk.databinding.LayoutNewPointsPopUpBinding;
import com.wyh.happyyousdk.databinding.ScratchCardPopUpBinding;
import com.wyh.happyyousdk.databinding.WebinarRewardPopUpBinding;
import com.wyh.happyyousdk.fileshare.PhotoViewActivityFileShare;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.UpdateFileStatusReq;
import com.wyh.happyyousdk.model.request.rewards.RewardsPopupRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.FeedbackResponseData;
import com.wyh.happyyousdk.model.response.FileShareResp;
import com.wyh.happyyousdk.model.response.UpdateFileStatusResp;
import com.wyh.happyyousdk.model.response.faceScan.FetchFaceScanVitalsData;
import com.wyh.happyyousdk.model.response.faceScan.FetchFaceScanVitalsResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.rewards.RewardsCollectiblesActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;
import com.wyh.happyyousdk.utils.dialog.QuizRewardDialog;

import java.lang.reflect.Type;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebinarWebview extends AppCompatActivity implements ScratchListener, rewardDialogCloseListener {


    private WebView webView;
    ProgressDialog prDialog;
    String url, loginUrl, fName, email, productName;
    Context context;

    public ValueCallback<Uri[]> uploadMessage;
    int REQUEST_CODE_FILE_CHOOSER = 1;
    int INPUT_FILE_REQUEST_CODE = 2;
    boolean isStamp = false, isPositiveBtn = false;
    int stampId = -1;
    String payableAmount;
    FeedbackResponseData responseNew;
    String comingFrom = "";
    String rewardMessage = "";
    String rewardType = "";

    AlertDialog alertDialogBonusRewards, alertDialogStamp, alertDialogBonusStamp;
    AssignRewardsResponse.SpinRewardsData spinRewardData;
    QuizathonRewardData quizreward;
    FileShareResp.Data data;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webinar_webview);

        context = this;
        SharedPref.init(context);

        SharedPref.init(context);

        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            url = (String) b.get("Url");
            payableAmount = (String) b.get("payableAmount");
            fName = b.getString("fullName");
            email = b.getString("email");
            productName = b.getString("productName");
            loginUrl = (String) b.get("loginUrl");
            comingFrom = b.getString("comingFrom");
        }

        Log.d("URL", url);
        webView = findViewById(R.id.webinar_webView);
        webView.loadUrl("javascript:localStorage.clear()");
        webView.clearCache(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setNeedInitialFocus(false);
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(this, "HappyYou");
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient());

        prDialog = new ProgressDialog(WebinarWebview.this);
        prDialog.setMessage("Please wait ...");
        prDialog.setCancelable(false);
        prDialog.show();

        camera_permission();

    }

    @Override
    public void onScratchComplete() {
        if (quizreward != null) {
            QuizRewardDialog.INSTANCE.QuizScratchCard(this, quizreward.getTransId(), true);
        }
    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        Log.d("AuthToken", "" + i);
        if (i >= 20) {
            {

                if (isStamp) {
                    isStamp = false;
                    scratchTokenReward();
                }
                scratchCardLayout.onFullReveal();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (alertDialogBonusRewards != null && alertDialogBonusRewards.isShowing()) {
                            alertDialogBonusRewards.dismiss();
                        }
                        if ((alertDialogBonusStamp != null && alertDialogBonusStamp.isShowing())) {
                            alertDialogBonusStamp.dismiss();
                        }
                        if ((alertDialogStamp != null && alertDialogStamp.isShowing())) {
                            alertDialogStamp.dismiss();
                        }
                    }
                }, 3000);
            }
        }
    }

    @Override
    public void onScratchStarted() {

    }

    @Override
    public void onDialogDismiss() {
        cancelDialog();
    }

    public void cancelDialog() {
        try {
            if (spinRewardData != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, spinRewardData.getRewardType(), this);
            } else if (quizreward != null) {
                NudgeDialogue.INSTANCE.spinnerCancelDialog(context, quizreward.getRewardType(), this);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d("AuthToken", request.getUrl().toString());
            String url = request.getUrl().toString();
            if (url.contains("api.whatsapp.com")) {
                Uri uri = Uri.parse(url);
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(sendIntent);
            } else if (url.contains("tel:")) {
                requestCallPermission();
            } else if (url.contains("WatchYourHealthcomIndi")) {
                String[] urlArray = url.split("#");
                String payURL = urlArray[0].replace("intent://", "upi://");
                Uri uri = Uri.parse(payURL);
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(sendIntent);
            } else if (url.contains("action=userCancel")) {
                view.loadUrl(WebinarWebview.this.url);
                return true;
            } else if (url.contains("mihpayid") && url.contains("token")) {
                NewDashboardHelper.Companion.setPaymentSuccess(true);
                finish();
            } else if (url.contains("zoomus://us05web.zoom.us")) {
                Uri uri = Uri.parse(url.replace("zoomus://", "https://"));
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(sendIntent);
                return true;
            } else if (url.contains("mailto:")) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(sendIntent);
                return true;
            } else if (url.contains("pdf")) {
                downloadPDF(url);
            } else if (url.contains("jpeg") || url.contains("jpg") || url.contains("png")) {
                webView.setInitialScale(1);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
            } else {
                view.loadUrl(url);
                return true;
            }
            return true;

        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if (url.contains("medPay") || url.contains("healthassure")) {
                location_permission();
            }

            //Log.v("Url Start", url + "\n" + uuid);
            String key = "UserID";
            String key1 = "AuthToken";
            String key2 = "redirectUrl";
            String key3 = "Username";
            String key4 = "Email";
            String key5 = "ProductName";
            String key6 = "Mobile";
            String key7 = "PayableAmount";
            String key8 = "UserId";
            String key9 = "facescannerUserUUID";
            String key10 = "transId";
            String key11 = "featureName";
            //getSupportActionBar().setTitle("Graph");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript("localStorage.setItem('" + key + "','" + SharedPref.getAesUuid() + "');", null);
                webView.evaluateJavascript("localStorage.setItem('" + key1 + "','" + SharedPref.getAuthToken() + "');", null);
                webView.evaluateJavascript("localStorage.setItem('" + key2 + "','" + loginUrl + "');", null);
                webView.evaluateJavascript("localStorage.setItem('" + key9 + "','" + SharedPref.getAesUuid() + "');", null);
                if (fName != null) {
                    webView.evaluateJavascript("localStorage.setItem('" + key3 + "','" + fName + "');", null);
                    webView.evaluateJavascript("localStorage.setItem('" + key4 + "','" + email + "');", null);
                    webView.evaluateJavascript("localStorage.setItem('" + key5 + "','" + productName + "');", null);
                    webView.evaluateJavascript("localStorage.setItem('" + key6 + "','" + SharedPref.getDecryptMobileNo() + "');", null);
                    webView.evaluateJavascript("localStorage.setItem('" + key7 + "','" + payableAmount + "');", null);
                    webView.evaluateJavascript("localStorage.setItem('" + key8 + "','" + SharedPref.getAesUuid() + "');", null);
//                  webView.evaluateJavascript("var x= localStorage.setItem('" + key8 + "'); alert(x)", null);

                }
                if(NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()){
                    webView.evaluateJavascript("localStorage.setItem('" + key10 + "','" + NewDashboardHelper.Companion.getTrasactionId() + "');", null);
                }
                if(NewDashboardHelper.Companion.getFeatureName() != null && !NewDashboardHelper.Companion.getFeatureName().isEmpty()){
                    webView.evaluateJavascript("localStorage.setItem('" + key11 + "','" + NewDashboardHelper.Companion.getFeatureName() + "');", null);
                }
            } else {
                webView.loadUrl("javascript:localStorage.setItem('" + key + "','" + SharedPref.getAesUuid() + "');");
                webView.loadUrl("javascript:localStorage.setItem('" + key1 + "','" + SharedPref.getAuthToken() + "');");
                webView.loadUrl("javascript:localStorage.setItem('" + key2 + "','" + loginUrl + "');");
                webView.evaluateJavascript("localStorage.setItem('" + key9 + "','" + SharedPref.getAesUuid() + "');", null);

                if (fName != null) {
                    webView.loadUrl("javascript:localStorage.setItem('" + key3 + "','" + fName + "');");
                    webView.loadUrl("javascript:localStorage.setItem('" + key4 + "','" + email + "');");
                    webView.loadUrl("javascript:localStorage.setItem('" + key5 + "','" + productName + "');");
                    webView.loadUrl("javascript:localStorage.setItem('" + key6 + "','" + SharedPref.getDecryptMobileNo() + "');");
                    webView.loadUrl("javascript:localStorage.setItem('" + key7 + "','" + payableAmount + "');");
                    webView.loadUrl("javascript:localStorage.setItem('" + key8 + "','" + SharedPref.getAesUuid() + "');");
                }
                if(NewDashboardHelper.Companion.getTrasactionId() != null && !NewDashboardHelper.Companion.getTrasactionId().isEmpty()){
                    webView.evaluateJavascript("localStorage.setItem('" + key10 + "','" + NewDashboardHelper.Companion.getTrasactionId() + "');", null);
                }
                if(NewDashboardHelper.Companion.getFeatureName() != null && !NewDashboardHelper.Companion.getFeatureName().isEmpty()){
                    webView.evaluateJavascript("localStorage.setItem('" + key11 + "','" + NewDashboardHelper.Companion.getFeatureName() + "');", null);
                }
            }

            //Log.v("Url", url + " , UUID: " + SharedPref.getAesUuid() + ", Token: " + SharedPref.getAuthToken());
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (prDialog != null) {
                prDialog.dismiss();
            }

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                    Log.d("console", consoleMessage.message());

                    if (consoleMessage.message().equalsIgnoreCase("Payment successful")) {
                        NewDashboardHelper.Companion.setPaymentSuccess(true);
                        finish();
                    }

                    if (consoleMessage.message().equalsIgnoreCase("payment failed")) {
                        NewDashboardHelper.Companion.setPaymentSuccess(true);
                        finish();
                    }

                    if (consoleMessage.message().contains("SpinTheWheel:")) {
                        try {
                            String spinRewardJson = consoleMessage.message().toString().replace("SpinTheWheel:", "");
                            Type type = new TypeToken<AssignRewardsResponse.SpinRewardsData>() {
                            }.getType();
                            spinRewardData = new Gson().fromJson(spinRewardJson, type);
                            getSpinRewardPopup(spinRewardData);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (consoleMessage.message().contains("Quizathon:")) {
                        try {
                            String quizRewardJson = consoleMessage.message().toString().replace("Quizathon:", "");
                            Type type = new TypeToken<QuizathonRewardData>() {
                            }.getType();
                            quizreward = new Gson().fromJson(quizRewardJson, type);
                            getQuizathonRewardPopup(quizreward);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (consoleMessage.message().contains("FileData :")) {
                        try {
                            String quizRewardJson = consoleMessage.message().toString().replace("FileData :", "");
                            Type type = new TypeToken<FileShareResp.Data>() {
                            }.getType();
                            data = new Gson().fromJson(quizRewardJson, type);
                            ShowFileShareDialogNew(data);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (consoleMessage.message().contains("rewards:")) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, consoleMessage.message().toString().split(":")[1]));
                        /*String rewardsText = consoleMessage.message().split(":")[1];
                        showRewardsPopupNew(rewardsText, WebinarWebview.this);*/
                    }

                    if (consoleMessage.message().contains("bonusRewards:")) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, consoleMessage.message().toString().split(":")[1]));
                        /*String rewardsText = consoleMessage.message().split(":")[1];
                        showRewardsPopupNew(rewardsText, WebinarWebview.this);*/
                    }


                    if (consoleMessage.message().contains("tokens:")) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, consoleMessage.message().toString().split(":")[1]));
                        /*String rewardsText = consoleMessage.message().split(":")[1];
                        showRewardsPopupNew(rewardsText, WebinarWebview.this);*/
                    }

                    if (consoleMessage.message().contains("bonusTokens:")) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, consoleMessage.message().toString().split(":")[1]));
                        /*String rewardsText = consoleMessage.message().split(":")[1];
                        showRewardsPopupNew(rewardsText, WebinarWebview.this);*/
                    }

                    if (consoleMessage.message().contains("Badges:") || consoleMessage.message().contains("badges:")) {
                        String[] message = consoleMessage.message().split(":");
                        if (message.length != 0 && message.length > 1) {
                            rewardMessage = message[1];
                            rewardType = message[0];
                        }
                        showWebinarPopup(rewardMessage, rewardType);
                    }

                    if (consoleMessage.message().contains("Voucher:") || consoleMessage.message().contains("voucher:")) {
                        String[] message = consoleMessage.message().split(":");
                        if (message.length != 0 && message.length > 1) {
                            rewardMessage = message[1];
                            rewardType = message[0];

                        }
                        showWebinarPopup(rewardMessage, rewardType);
                    }

                    if (consoleMessage.message().contains("Stamps:") || consoleMessage.message().contains("stamps:")) {
                        String[] message = consoleMessage.message().split(":");
                        if (message.length != 0 && message.length > 1) {
                            rewardMessage = message[1];
                            rewardType = message[0];

                        }
                        showWebinarPopup(rewardMessage, rewardType);
                    }

                    if (consoleMessage.message().contains("Points:") || consoleMessage.message().contains("points:")) {
                        String[] message = consoleMessage.message().split(":");
                        if (message.length != 0 && message.length > 1) {
                            rewardMessage = message[1];
                            rewardType = message[0];

                        }
                        showWebinarPopup(rewardMessage, rewardType);
                    }

                    if (consoleMessage.message().contains("No Rewards:")) {
                        String[] message = consoleMessage.message().split(":");
                        if (message.length != 0 && message.length > 1) {
                            rewardMessage = message[1];
                            rewardType = message[0];

                        }
                        showWebinarPopup(rewardMessage, rewardType);
                    }

                    if (consoleMessage.message().contains("home button clicked")) {
                        gotoHome();
                    }
                    Log.d("AuthToken", "Console " + consoleMessage.message());


                    if (consoleMessage.message().contains("exit button clicked")) {
                        if (responseNew == null || responseNew.getStarConfig() == null) {
                            finish();
                        }
                    }

                    if (consoleMessage.message().contains("toast")) {
                        String toastMessage = consoleMessage.message().split(":")[2];
                        if (toastMessage != null)
                            Toast.makeText(WebinarWebview.this, toastMessage, Toast.LENGTH_SHORT).show();

                        if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0)
                            showRewardsPopupDialogBox();
                    }

                    if (consoleMessage.message().contains("feedback:")) {
                        try {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    String str = consoleMessage.message().toString().split("feedback:")[1];

                                    if (comingFrom != null && comingFrom.equalsIgnoreCase("dashboard")) {
//                                        DashboardActivity.feedBackModel = str;
                                    } else {
                                        TrendsActivity.feedBackModel = str;

                                    }
                                }
                            }, 500);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    return super.onConsoleMessage(consoleMessage);
                }

                @Override
                public void onGeolocationPermissionsShowPrompt(String origin,
                                                               GeolocationPermissions.Callback callback) {
                    // Always grant permission since the app itself requires location
                    // permission and the user has therefore already granted it
                    callback.invoke(origin, true, false);
                }

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onPermissionRequest(final PermissionRequest request) {
                    request.grant(request.getResources());
                }

                @SuppressLint("InlinedApi")
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                                 FileChooserParams fileChooserParams) {
                    if (SDK_INT >= Build.VERSION_CODES.R) {
                        if (uploadMessage != null) {
                            uploadMessage.onReceiveValue(null);
                            uploadMessage = null;
                        }
                        uploadMessage = filePathCallback;

                        Intent fileChooserIntent = getFileChooserIntent();
                        startActivityForResult(fileChooserIntent, INPUT_FILE_REQUEST_CODE);
                        return true;
                    } else {
                        if (file_permission()) {
                            // make sure there is no existing message
                            if (uploadMessage != null) {
                                uploadMessage.onReceiveValue(null);
                                uploadMessage = null;
                            }
                            uploadMessage = filePathCallback;

                            Intent fileChooserIntent = getFileChooserIntent();
                            startActivityForResult(fileChooserIntent, INPUT_FILE_REQUEST_CODE);
                            return true;
                        } else {
                            return false;
                        }
                    }

                }
            });
        }
    }


    private void requestCallPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length != 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                startActivity(intent);
            } else {
                Toast.makeText(context, "Required telephone permission to make a call", Toast.LENGTH_LONG).show();

            }
        }

    }

    private void getSpinRewardPopup(AssignRewardsResponse.SpinRewardsData data) {
        try {
            if (data.getRewardType() != null && !data.getRewardType().isEmpty()) {
                String rewardtype = data.getRewardType();
                if (rewardtype.equalsIgnoreCase("Points")) {
                    PostSpinDialog.INSTANCE.showPostSpinnerPointsPopupCallBack(data.getRewardTitle(), context, "Trends", data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Offers")) {
                    PostSpinDialog.INSTANCE.showPostSpinnerOfferCallBack(context, data.getPartnerLogo(), data.getRewardDescription(), data.getPartnerName()
                            , data.getPartnerUrl(), "Trends", data.getExpiryInHours(), data.getRewardTitle(), data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Voucher")) {
                    PostSpinDialog.INSTANCE.showPostSpinnerVoucherCallBack(context, data.getRewardLogo(), data.getCouponCode(), data.getRewardDescription()
                            , data.getRewardTitle(), data.getExpiryInHours(), "Trends", data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Stamps")) {
                    PostSpinDialog.INSTANCE.showStampsPopupCallBack(data.getRewardHeader1(), data.getRewardHeader2(), data.getRewardTitle(), data.getRewardValue(), this, this, this);
                } else if (rewardtype.equalsIgnoreCase("Badge")) {
                    PostSpinDialog.INSTANCE.showPostSpinnerBadgePopupCallBack(context, "Trends", data.getRewardLogo(), data.getRewardTitle(), data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                }
            }
        } catch (Exception ex) {

        }
    }

    private void getQuizathonRewardPopup(QuizathonRewardData data) {
        try {
            quizreward = data;
            if (data.getRewardType() != null && !data.getRewardType().isEmpty()) {
                String rewardtype = data.getRewardType();
                if (rewardtype.equalsIgnoreCase("Points")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerPointsPopupCallBack(data.getRewardTitle(), context, "Trends", data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Offers")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerOfferCallBack(context, data.getPartnerLogo(), data.getRewardDescription(), data.getPartnerName()
                            , data.getPartnerUrl(), "Trends", data.getExpiryInHours(), data.getRewardTitle(), data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Voucher")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerVoucherCallBack(context, data.getRewardLogo(), data.getCouponCode(), data.getRewardDescription()
                            , data.getRewardTitle(), data.getExpiryInHours(), "Trends", data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Badge")) {
                    QuizRewardDialog.INSTANCE.showPostSpinnerBadgePopupCallBack(context, "Trends", data.getRewardLogo(), data.getRewardTitle(), data.getRewardDescription(),
                            data.getRewardHeader1(), data.getRewardHeader2(), this, this);
                } else if (rewardtype.equalsIgnoreCase("Stamps")) {
                    QuizRewardDialog.INSTANCE.showStampsPopupCallBack(data.getRewardHeader1(), data.getRewardHeader2(), data.getRewardTitle(), data.getRewardValue(), this, this, this);
                }
                else if (rewardtype.equalsIgnoreCase("future")) {
                    QuizRewardDialog.INSTANCE.showFutureRewardDialog(context,data.getDialogModel(),data.getClaimDate());
                }

            }
        } catch (Exception ex) {

        }
    }

    private void showWebinarPopup(String rewardText, String rewardType) {
        try {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            WebinarRewardPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.webinar_reward_pop_up, null, false);
            alertBuilder.setView(binding.getRoot());
            AlertDialog alertDialog = alertBuilder.create();


            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }

            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {

                }
            });

            if (rewardType.equalsIgnoreCase("No Rewards")) {
                binding.btnPositive.setVisibility(View.GONE);
            } else {
                binding.btnPositive.setVisibility(View.VISIBLE);
            }

            binding.tvPoints.setText(rewardText);
            binding.ivClose.setOnClickListener(view -> {
                alertDialog.dismiss();
            });

            binding.scratchView.setScratchListener(WebinarWebview.this);
            //binding.scratchView.onFullReveal();

            binding.btnNegative.setOnClickListener(view -> {
                alertDialog.dismiss();
                finish();
            });

            binding.btnPositive.setOnClickListener(view -> {
                if (rewardType.equalsIgnoreCase("badges")) {
                    alertDialog.dismiss();
                    startActivity(new Intent(this, BadgesActivity.class));
                    finish();

                } else if (rewardType.equalsIgnoreCase("voucher")) {
                    alertDialog.dismiss();
                    startActivity(new Intent(this, RewardsCollectiblesActivity.class));
                    finish();

                } else if (rewardType.equalsIgnoreCase("stamps")) {
                    alertDialog.dismiss();
                    Intent intent = new Intent(this, RewardsActivity.class);
                    intent.putExtra("currentIndex", 1);
                    startActivity(intent);
                    finish();

                } else if (rewardType.equalsIgnoreCase("points")) {
                    //level
                    alertDialog.dismiss();
                    Intent intent = new Intent(this, RewardsActivity.class);
                    intent.putExtra("currentIndex", 0);
                    startActivity(intent);
                    finish();

                } else {
                    alertDialog.dismiss();
                    finish();
                }
            });

            Rect displayRectangle = new Rect();
            Window window = getWindow();

            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                    0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoHome() {
        Intent intent = new Intent(this, NewDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public boolean file_permission() {
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(WebinarWebview.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_FILE_CHOOSER);
            return false;
        } else {
            return true;
        }
    }

    public boolean camera_permission() {
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(WebinarWebview.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1111);
            return false;
        } else {
            return true;
        }
    }

    public boolean location_permission() {
        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(WebinarWebview.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3333);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (prDialog != null) {
            prDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (prDialog != null) {
            prDialog.dismiss();
        }
    }

    private void showRewardsPopup(String rewards) {
        Context context = WebinarWebview.this;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

        ScratchCardPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.scratch_card_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvPoints.setText(points);

        binding.btnNegative.setOnClickListener(view -> {
            alertDialog.dismiss();
            finish();
        });
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, RewardsActivity.class);
            context.startActivity(intent);
            finish();
        });
        binding.scratchView.onFullReveal();
        binding.scratchView.setScratchListener(WebinarWebview.this);


        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void showRewardsPopupNew(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];

        String points = message.replaceAll("[^0-9]", "");

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 3000);

        alertDialog.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });
        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);
        binding.tvEventName.setText(title);

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.onFullReveal();
        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.scratchView.setScratchListener(WebinarWebview.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.67f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                //Log.v("Url Back Top", webView.getUrl());
                if (webView.getUrl().contains("kotak_analysis") || webView.getUrl().contains("kotak_immune_analysis") ||
                        webView.getUrl().contains("kotak_end") || webView.getUrl().contains("kotak_full_analysis")) {
                    /*finishAffinity();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);*/
                    finish();
                } else if (webView.getUrl().contains("Article") || webView.getUrl().contains("graph")
                        || webView.getUrl().contains("singlegraph")) {
                    finish();
                } else {
                    if (webView.getUrl().contains("customerId") || webView.getUrl().contains("quiz")) {
                        if (webView.canGoBack()) {
                            webView.goBack();
                        }
                    } else {
                        finish();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public void onTopBackPressed() {
        ////Log.v("Url Back Bottom", webView.getUrl());
        //Log.v("Url Back Top", webView.getUrl());
        if (webView.getUrl().contains("kotak_analysis") || webView.getUrl().contains("kotak_immune_analysis") ||
                webView.getUrl().contains("kotak_end") || webView.getUrl().contains("kotak_full_analysis")) {
            /*finishAffinity();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);*/
            finish();
        } else if (webView.getUrl().contains("Article") || webView.getUrl().contains("graph")
                || webView.getUrl().contains("singlegraph")) {
            finish();
        } else {
            if (webView.getUrl().contains("customerId") || webView.getUrl().contains("quiz")) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            } else {
                finish();
            }
        }
    }

    void downloadPDF(String url) {
        String scheme = Uri.parse(url).getScheme();
        if (scheme != null && ((scheme.equals("http") || scheme.equals("https")))) {

            Intent viewIntent = new Intent(Intent.ACTION_VIEW);
            viewIntent.setData(Uri.parse(url));
            try {
                startActivity(viewIntent);
            } catch (ActivityNotFoundException ex) {
                ex.getMessage();
            }


            try {
                Uri fileUri = Uri.parse(url);
                String fileName = "";
                if (fileUri.getPath().lastIndexOf('/') != -1) {
                    fileName = fileUri.getPath().substring(fileUri.getPath().lastIndexOf('/') + 1);
                } else {
                    fileName = "document";
                }
                System.out.println("getPath: " + fileName);

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/mibPulse/" + fileName);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Intent getFileChooserIntent() {
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        return intent;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INPUT_FILE_REQUEST_CODE) {
            if (uploadMessage == null) return;
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // For Single file upload
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            }*/

            Uri[] results = null;
            try {
                if (resultCode == RESULT_OK) {
                    String dataString = data.getDataString();
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        results = new Uri[clipData.getItemCount()];
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            uploadMessage.onReceiveValue(results);
            uploadMessage = null;
        }
        if (requestCode == 1) {
            Log.d("Authtoken", "Pay Response " + resultCode + " " + data);
        }
    }

    public boolean CheckGpsStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsStatus == true) {
            return gpsStatus;
        } else {
            return false;
        }
    }
    /*private void handleUploadMessage(final int requestCode, final int resultCode, final Intent data) {
        Uri result = null;
        try {
            if (resultCode != RESULT_OK) {
                result = null;
            } else {
                // retrieve from the private variable if the intent is null

                result = data == null ? mCapturedImageURI : data.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;

        // code for all versions except of Lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            result = null;

            try {
                if (resultCode != RESULT_OK) {
                    result = null;
                } else {
                    // retrieve from the private variable if the intent is null
                    result = data == null ? mCapturedImageURI : data.getData();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "activity :" + e, Toast.LENGTH_LONG).show();
            }

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }

    } */
    // end of code for all versions except of Lollipop

    private void showBonusRewardsPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        LayoutNewPointsPopUpBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_new_points_pop_up, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusRewards = alertBuilder.create();
        alertDialogBonusRewards.setCancelable(false);
        if (!alertDialogBonusRewards.isShowing())
            alertDialogBonusRewards.show();

        alertDialogBonusRewards.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 0);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });
        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

//        binding.tvTitle.setText(title);
        binding.tvPoints.setText(points);
        binding.tvEventName.setVisibility(View.VISIBLE);

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusRewards.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusRewards.dismiss();
        });

        binding.scratchView.setScratchListener(WebinarWebview.this);

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusRewards.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusRewards.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusRewards.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showStampsPopup(String rewards, Context context) {
        isStamp = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogStamp = alertBuilder.create();
        alertDialogStamp.setCancelable(true);
        if (!alertDialogStamp.isShowing())
            alertDialogStamp.show();

        alertDialogStamp.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });
        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }

        if (title.contains(":")) {
            title = title.split(":")[1];
        }

        /*if(isOrange){
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_orange_new));
        }else{
            binding.scratchView.setScratchDrawable(context.getDrawable(R.drawable.scratch_card_pink_new));
        }*/
//        String points = message.replaceAll("[^0-9]", "");

        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


        /*if (rewards.contains("First Login")) {
            binding.btnNegative.setVisibility(View.GONE);
            binding.btnPositive.setText("OK");
            binding.btnPositive.setOnClickListener(view -> {
                alertDialog.dismiss();
                Intent intent = new Intent(context, SyncDeviceActivity.class);
                startActivity(intent);
            });
        } else {

        }*/

//        binding.scratchView.onFullReveal();
        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogStamp.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogStamp.dismiss();
        });

        binding.scratchView.setScratchListener(WebinarWebview.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_pink_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        isStamp = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        alertDialogBonusStamp = alertBuilder.create();
        alertDialogBonusStamp.setCancelable(true);
        if (!alertDialogBonusStamp.isShowing())
            alertDialogBonusStamp.show();


        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];
        if (rewards.split(";").length == 4) {
            String id = rewards.split(";")[3];
            stampId = Integer.parseInt(id);
        }
        alertDialogBonusStamp.setOnDismissListener(dialogInterface -> {
            if (isPositiveBtn) {
                isPositiveBtn = false;
                Intent intent = new Intent(this, RewardsActivity.class);
                intent.putExtra("currentIndex", 1);
                startActivity(intent);
                finish();
            } else {
                showRewardsPopupDialogBox();
            }
        });
        if (title.contains(":")) {
            title = title.split(":")[1];
        }

        binding.tvTitle.setText("Milestone Points");
        binding.tvDescription.setText("");
        binding.tvDescription2.setText("No. of Stamps: " + points);

        binding.btnPositive.setOnClickListener(view -> {
            isPositiveBtn = true;
            alertDialogBonusStamp.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialogBonusStamp.dismiss();
        });

        binding.scratchView.setScratchListener(WebinarWebview.this);
        binding.scratchView.setScratchDrawable(getDrawable(R.drawable.scratch_card_orange_new));

        binding.btnNegative.setOnClickListener(view -> alertDialogBonusStamp.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialogBonusStamp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialogBonusStamp.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showRewardsPopupDialogBox() {
        if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 0) {
            int i = 0;
            PopUpShowModel firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            if (NewDashboardHelper.Companion.getPopUpShowModels().size() > 1 && Objects.equals(firstData.getKey(), "Rewards")) {
                i = 1;
                firstData = NewDashboardHelper.Companion.getPopUpShowModels().get(i);
            }
            switch (firstData.getKey()) {
                case "Rewards":
                    showRewardsPopupNew(firstData.getValue(), context);
                    break;
                case "RewardsBounce":
                    showBonusRewardsPopup(firstData.getValue(), context);
                    break;
                case "TokenStamp":
                    showStampsPopup(firstData.getValue(), context);
                    break;
                case "TokenStampBounce":
                    showBonusStampPopup(firstData.getValue(), context);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstData.getKey());
            }
            NewDashboardHelper.Companion.getPopUpShowModels().remove(i);
        } else
            finish();
    }

    private void scratchTokenReward() {
        RewardsPopupRequest request = new RewardsPopupRequest(stampId);
        ApiInterfaceWyh apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.scratchTokenReward(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {

                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_success));

                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {

                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.scratch_token_reward_failed));
                Toast.makeText(context, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowFileShareDialogNew(FileShareResp.Data data)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_file_share_new, null);

        // Build the dialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        if (window != null) {
            window.setDimAmount(0.9f); // Set dim amount (0.0f = no dim, 1.0f = fully dimmed)
        }
        TextView tvTitle=dialogView.findViewById(R.id.tvTitle);
//        TextView tvTitle2=dialogView.findViewById(R.id.tvTitle2);
        TextView tvSubTitle1=dialogView.findViewById(R.id.tvSubTitle1);
        TextView tvSubTitle2=dialogView.findViewById(R.id.tvSubTitle2);
        TextView tvSubTitle3=dialogView.findViewById(R.id.tvSubTitle3);
        TextView tvSubTitle4=dialogView.findViewById(R.id.tvSubTitle4);
        TextView tvSubTitle5=dialogView.findViewById(R.id.tvSubTitle5);
        TextView tvSubTitle6=dialogView.findViewById(R.id.tvSubTitle6);
        TextView tvSubTitle1Value=dialogView.findViewById(R.id.tvSubTitle1Value);
        TextView tvSubTitle2Value=dialogView.findViewById(R.id.tvSubTitle2Value);
        TextView tvSubTitle3Value=dialogView.findViewById(R.id.tvSubTitle3Value);
        TextView tvSubTitle4Value=dialogView.findViewById(R.id.tvSubTitle4Value);
        TextView tvSubTitle5Value=dialogView.findViewById(R.id.tvSubTitle5Value);
        TextView tvSubTitle6Value=dialogView.findViewById(R.id.tvSubTitle6Value);
        TextView tvDesc1=dialogView.findViewById(R.id.tvDesc1);
        TextView tvDesc2=dialogView.findViewById(R.id.tvDesc2);
        TextView tvDesc3=dialogView.findViewById(R.id.tvDesc3);
        TextView tvExpireOn=dialogView.findViewById(R.id.tvExpireOn);
        TextView tvFileName=dialogView.findViewById(R.id.tvFileName);
        RelativeLayout btnDownload=dialogView.findViewById(R.id.btnDownload);
        RelativeLayout btnView=dialogView.findViewById(R.id.btnView);
        ImageView imFileShareImage=dialogView.findViewById(R.id.imFileShareImage);
        ImageView btnClose=dialogView.findViewById(R.id.imClose);
        TextView tvNote = dialogView.findViewById(R.id.tvNote);
        btnDownload.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_FILE_DOWNLOAD",context);
            saveFileShare(data.getCategoryName(),"Download");
            if (data.getFilePath()!=null&!data.getFilePath().isEmpty())
            {
                downloadImage(context,data.getFilePath(),data.getFileName());
            }
            dialog.dismiss();
        });
        btnClose.setOnClickListener(v -> { dialog.dismiss();
            APILogs.INSTANCE.activityTracker("Android_FILE_CANCEL",context);
        });
        btnView.setOnClickListener(v -> {
            APILogs.INSTANCE.activityTracker("Android_FILE_VIEW",context);
            saveFileShare(data.getCategoryName(),"View");
            if (data.getFilePath()!=null&!data.getFilePath().isEmpty())
            {
                if (data.getFilePath().endsWith(".pdf")||data.getFilePath().endsWith(".doc")||data.getFilePath().endsWith(".docx"))
                {
                    downloadAndViewFile(context,data.getFilePath());
                }
                else
                {
                    viewImage(data.getFilePath());
                }
            }
            //dialog.dismiss();
        });
        Glide.with(context)
                .load(data.getTileIcon())
                .error(R.drawable.ic_file_share)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imFileShareImage);

        tvTitle.setText(data.getCategoryName());
        tvSubTitle1.setText(data.getEventId().split(";")[0]);
        tvSubTitle2.setText(data.getDateString().split(";")[0]);
        tvSubTitle3.setText(data.getTimeString().split(";")[0]);
        tvSubTitle4.setText(data.getPlace().split(";")[0]);
        tvSubTitle5.setText("End Date");
        tvSubTitle6.setText("End Time");
        tvSubTitle1Value.setText(data.getEventId().split(";")[1]);
        tvSubTitle2Value.setText(data.getDateString().split(";")[1]);
        tvSubTitle3Value.setText(data.getTimeString().split(";")[1]);
        tvSubTitle4Value.setText(data.getPlace().split(";")[1]);
        tvSubTitle5Value.setText(formatDateFromString("yyyy-MM-dd'T'HH:mm","dd-MM-yyyy",data.getExpiryDate()));
        tvSubTitle6Value.setText(formatDateFromString("yyyy-MM-dd'T'HH:mm","hh:mm a",data.getExpiryDate()));
        tvDesc1.setText(data.getDescription1().replace("\\n", "\n"));
        tvDesc2.setText(data.getDescription2().replace("\\n", "\n"));
        tvDesc3.setText(data.getDescription3().replace("\\n", "\n"));
        tvExpireOn.setVisibility(View.GONE);
        tvExpireOn.setText("Expire On "+formatDateFromString("yyyy-MM-dd'T'HH:mm","dd/MM/yyyy",data.getExpiryDate()));
        tvFileName.setText(data.getFileName());
        tvNote.setVisibility(View.GONE);
        tvNote.setText(data.getNote());
        dialog.show();
    }
    private void saveFileShare(String corporateCat,String UserAction) {
        UpdateFileStatusReq request = new UpdateFileStatusReq(corporateCat,UserAction);
        APIInterface apiInterfaceWyh ;
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        Call<UpdateFileStatusResp> call = apiInterfaceWyh.UpdateFileStatus(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<UpdateFileStatusResp> call, @NonNull Response<UpdateFileStatusResp> response) {

            }

            @Override
            public void onFailure(@NonNull Call<UpdateFileStatusResp> call, @NonNull Throwable t) {

            }
        });
    }
    private void viewImage(String url) {
        if (url.endsWith(".pdf")) {
            // Handle PDF files
            Uri path = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No Application Available to View PDF", Toast.LENGTH_SHORT).show();
            }
        } else if (url.endsWith(".doc") || url.endsWith(".docx")) {
            // Handle DOC and DOCX files
            Uri path = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/msword");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No Application Available to View Word Document", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle image files
            Intent i = new Intent(context, PhotoViewActivityFileShare.class);
            i.putExtra("url", url);
            context.startActivity(i);
        }
    }
}