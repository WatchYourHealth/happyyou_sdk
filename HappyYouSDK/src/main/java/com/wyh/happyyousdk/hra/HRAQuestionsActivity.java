package com.wyh.happyyousdk.hra;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.getDateInFormat;
import static com.wyh.happyyousdk.utils.CommonUtils.hideKeyboard;
import static com.wyh.happyyousdk.utils.Constants.FeedbackPOPUP;
import static com.wyh.happyyousdk.utils.Constants.HRA_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.HRA_STATUS_IN_PROGRESS;
import static com.wyh.happyyousdk.utils.Constants.HRA_STATUS_NEW;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityHraquestionsBinding;
import com.wyh.happyyousdk.databinding.ScratchCardPopUpBinding;
import com.wyh.happyyousdk.utils.Master;

import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.request.ConversationIdReq;
import com.wyh.happyyousdk.model.request.hra.GetAnalysisRequest;
import com.wyh.happyyousdk.model.request.hra.GetHraRequest;
import com.wyh.happyyousdk.model.request.hra.SaveHraRequest;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.ConversationIdResponse;
import com.wyh.happyyousdk.model.response.faceScan.FaceScanKeyResponse;
import com.wyh.happyyousdk.model.response.faceScan.GetFaceKeysResponse;
import com.wyh.happyyousdk.model.response.hra.GetHraAnswersResponse;
import com.wyh.happyyousdk.model.response.hra.HraAnswersData;
import com.wyh.happyyousdk.model.response.hra.SaveAnswersResponse;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.Constants;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HRAQuestionsActivity extends AppCompatActivity implements ScratchListener {

    ActivityHraquestionsBinding binding;
    Context context;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    List<HraAnswersData> hraAnswersData = new ArrayList<>();
    int answeredCount = 0;
    String dob, gender, bloodGroup, activityPattern, dietPattern, dietStyle;
    String genderfrompref = "";
    List<String> consumeList = new ArrayList<>();
    List<String> diseasesList = new ArrayList<>();
    String[] feetArray = new String[8];
    String[] inchesArray = new String[12];
    String[] waistArray = new String[31];
    AlertDialog alertDialog;
    String statusHRA;
    boolean isFromRetake, isFromRetakeData, isPressedOnPrevious = false, isYesOrNo = false, isDiseases = false;
    Handler handler;
    boolean isStamps = false;
    boolean isFirst = true, isprev = false;

    AssignRewardsResponse.SpinRewardsData spinRewardsData = null;
    QuizathonRewardData quizathonRewardData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hraquestions);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        isFromRetake = getIntent().getBooleanExtra("isFromRetake", false);
        isFromRetakeData = getIntent().getBooleanExtra("isFromRetakeData", false);

        handler = new Handler();


        for (int i = 1; i <= 8; i++) {
            feetArray[i - 1] = String.valueOf(i);
        }

        for (int i = 20; i <= 50; i++) {
            waistArray[i - 20] = String.valueOf(i);
        }

        for (int i = 0; i < 12; i++) {
            inchesArray[i] = String.valueOf(i);
        }

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });


        binding.numberFeet.setMinValue(0);
        binding.numberFeet.setMaxValue(feetArray.length - 1);
        binding.numberInches.setMinValue(0);
        binding.numberInches.setMaxValue(inchesArray.length - 1);
        binding.numberWaist.setMinValue(0);
        binding.numberFeet.setDisplayedValues(feetArray);
        binding.numberInches.setDisplayedValues(inchesArray);
        binding.numberWaist.setMaxValue(waistArray.length - 1);
        binding.numberWaist.setDisplayedValues(waistArray);


        String weight = SharedPref.getWeight();
        String height = SharedPref.getUserHeightFt();
        genderfrompref = SharedPref.getUserGender();

        if (!weight.isEmpty()) {
            binding.edtWeight.setText(weight);
        }


        if (!height.isEmpty()) {
            String feet = height.split("\\.")[0];
            String inches = height.split("\\.")[1];
            binding.numberFeet.setValue(Integer.parseInt(feet) - 1);
            binding.numberInches.setValue(Integer.parseInt(inches));
        }


        initViewTags();

        getHRAAnswers();

        Calendar dobCalendar = Calendar.getInstance();
        dobCalendar.add(Calendar.YEAR, -18);

        int day;
        int month1;
        int year1;
        String dayStr;
        String month1Str;
        String year1Str;
        if (!SharedPref.getDOB().isEmpty() && SharedPref.getDOB() != null) {
            String split = SharedPref.getDOB().split(" ")[0];
            dob = split;
            String[] splitDob;
            if (split.contains("/")) {
                splitDob = split.split("/");
                day = Integer.valueOf(splitDob[0]);
                month1 = Integer.valueOf(splitDob[1]);
                year1 = Integer.valueOf(splitDob[2]);
                dayStr = splitDob[0];
                month1Str = splitDob[1];
                year1Str = splitDob[2];
            } else {
                splitDob = split.split("-");
                day = Integer.valueOf(splitDob[2]);
                month1 = Integer.valueOf(splitDob[1]);
                year1 = Integer.valueOf(splitDob[0]);
                dayStr = splitDob[2];
                month1Str = splitDob[1];
                year1Str = splitDob[0];
            }
            if (dayStr.length() == 1)
                dayStr = "0" + dayStr;
            if (month1Str.length() == 1)
                month1Str = "0" + month1Str;
            dob = dayStr + "/" + month1Str + "/" + year1Str;
            month1--;
        } else {
            year1 = dobCalendar.get(Calendar.YEAR);
            day = dobCalendar.get(Calendar.MONTH);
            month1 = dobCalendar.get(Calendar.DAY_OF_MONTH);
            dob = getDateInFormat(dobCalendar.getTime(), "dd/MM/yyyy");
        }

        Log.d("dob 2", dob);
        binding.datePicker.setMaxDate(dobCalendar.getTimeInMillis());
        binding.datePicker.init(year1, month1,
                day, (datePicker, year, monthOfYear, dayOfMonth) -> {
                    monthOfYear++;
                    String month = String.valueOf(monthOfYear);
                    String date = String.valueOf(dayOfMonth);
                    if (dayOfMonth < 10)
                        date = "0" + date;
                    if (monthOfYear < 10)
                        month = "0" + monthOfYear;
                    dob = date + "/" + month + "/" + year;
                });

        binding.edtWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(HRAQuestionsActivity.this);
                    binding.btnOk.performClick();
                    return true;
                }
                return false;
            }
        });

        //Gender Selection
        binding.tvMale.setOnClickListener(view -> {
            if (!(boolean) binding.tvMale.getTag())
                optionClicked(binding.tvMale);
            if ((boolean) binding.tvFemale.getTag())
                optionClicked(binding.tvFemale);
            if ((boolean) binding.tvOthers.getTag())
                optionClicked(binding.tvOthers);
            gender = "Male";
            genderfrompref = "Male";
            startHandler();
        });

        binding.tvFemale.setOnClickListener(view -> {
            if (!(boolean) binding.tvFemale.getTag())
                optionClicked(binding.tvFemale);
            if ((boolean) binding.tvMale.getTag())
                optionClicked(binding.tvMale);
            if ((boolean) binding.tvOthers.getTag())
                optionClicked(binding.tvOthers);
            gender = "Female";
            genderfrompref = "Female";
            startHandler();
        });

        binding.tvOthers.setOnClickListener(view -> {
            if (!(boolean) binding.tvOthers.getTag())
                optionClicked(binding.tvOthers);
            if ((boolean) binding.tvMale.getTag())
                optionClicked(binding.tvMale);
            if ((boolean) binding.tvFemale.getTag())
                optionClicked(binding.tvFemale);
            gender = "Others";
            genderfrompref = "Others";
            startHandler();
        });

        //Blood Group Selection
        binding.tvAPos.setOnClickListener(view -> {
            if (!(boolean) binding.tvAPos.getTag())
                optionClicked(binding.tvAPos);
            if ((boolean) binding.tvANeg.getTag())
                optionClicked(binding.tvANeg);
            if ((boolean) binding.tvBPos.getTag())
                optionClicked(binding.tvBPos);
            if ((boolean) binding.tvBNeg.getTag())
                optionClicked(binding.tvBNeg);
            if ((boolean) binding.tvABPos.getTag())
                optionClicked(binding.tvABPos);
            if ((boolean) binding.tvABNeg.getTag())
                optionClicked(binding.tvABNeg);
            if ((boolean) binding.tvOPos.getTag())
                optionClicked(binding.tvOPos);
            if ((boolean) binding.tvONeg.getTag())
                optionClicked(binding.tvONeg);
            bloodGroup = "A+";
            startHandler();
        });

        binding.tvANeg.setOnClickListener(view -> {
            if (!(boolean) binding.tvANeg.getTag())
                optionClicked(binding.tvANeg);
            if ((boolean) binding.tvAPos.getTag())
                optionClicked(binding.tvAPos);
            if ((boolean) binding.tvBPos.getTag())
                optionClicked(binding.tvBPos);
            if ((boolean) binding.tvBNeg.getTag())
                optionClicked(binding.tvBNeg);
            if ((boolean) binding.tvABPos.getTag())
                optionClicked(binding.tvABPos);
            if ((boolean) binding.tvABNeg.getTag())
                optionClicked(binding.tvABNeg);
            if ((boolean) binding.tvOPos.getTag())
                optionClicked(binding.tvOPos);
            if ((boolean) binding.tvONeg.getTag())
                optionClicked(binding.tvONeg);
            bloodGroup = "A-";
            startHandler();
        });

        binding.tvBPos.setOnClickListener(view -> {
            if (!(boolean) binding.tvBPos.getTag())
                optionClicked(binding.tvBPos);
            if ((boolean) binding.tvAPos.getTag())
                optionClicked(binding.tvAPos);
            if ((boolean) binding.tvANeg.getTag())
                optionClicked(binding.tvANeg);
            if ((boolean) binding.tvBNeg.getTag())
                optionClicked(binding.tvBNeg);
            if ((boolean) binding.tvABPos.getTag())
                optionClicked(binding.tvABPos);
            if ((boolean) binding.tvABNeg.getTag())
                optionClicked(binding.tvABNeg);
            if ((boolean) binding.tvOPos.getTag())
                optionClicked(binding.tvOPos);
            if ((boolean) binding.tvONeg.getTag())
                optionClicked(binding.tvONeg);
            bloodGroup = "B+";
            startHandler();
        });

        binding.tvBNeg.setOnClickListener(view -> {
            if (!(boolean) binding.tvBNeg.getTag())
                optionClicked(binding.tvBNeg);
            if ((boolean) binding.tvAPos.getTag())
                optionClicked(binding.tvAPos);
            if ((boolean) binding.tvANeg.getTag())
                optionClicked(binding.tvANeg);
            if ((boolean) binding.tvBPos.getTag())
                optionClicked(binding.tvBPos);
            if ((boolean) binding.tvABPos.getTag())
                optionClicked(binding.tvABPos);
            if ((boolean) binding.tvABNeg.getTag())
                optionClicked(binding.tvABNeg);
            if ((boolean) binding.tvOPos.getTag())
                optionClicked(binding.tvOPos);
            if ((boolean) binding.tvONeg.getTag())
                optionClicked(binding.tvONeg);
            bloodGroup = "B-";
            startHandler();
        });

        binding.tvABPos.setOnClickListener(view -> {
            if (!(boolean) binding.tvABPos.getTag())
                optionClicked(binding.tvABPos);
            if ((boolean) binding.tvAPos.getTag())
                optionClicked(binding.tvAPos);
            if ((boolean) binding.tvANeg.getTag())
                optionClicked(binding.tvANeg);
            if ((boolean) binding.tvBPos.getTag())
                optionClicked(binding.tvBPos);
            if ((boolean) binding.tvBNeg.getTag())
                optionClicked(binding.tvBNeg);
            if ((boolean) binding.tvABNeg.getTag())
                optionClicked(binding.tvABNeg);
            if ((boolean) binding.tvOPos.getTag())
                optionClicked(binding.tvOPos);
            if ((boolean) binding.tvONeg.getTag())
                optionClicked(binding.tvONeg);
            bloodGroup = "AB+";
            startHandler();
        });

        binding.tvABNeg.setOnClickListener(view -> {
            if (!(boolean) binding.tvABNeg.getTag())
                optionClicked(binding.tvABNeg);
            if ((boolean) binding.tvAPos.getTag())
                optionClicked(binding.tvAPos);
            if ((boolean) binding.tvANeg.getTag())
                optionClicked(binding.tvANeg);
            if ((boolean) binding.tvBPos.getTag())
                optionClicked(binding.tvBPos);
            if ((boolean) binding.tvBNeg.getTag())
                optionClicked(binding.tvBNeg);
            if ((boolean) binding.tvABPos.getTag())
                optionClicked(binding.tvABPos);
            if ((boolean) binding.tvOPos.getTag())
                optionClicked(binding.tvOPos);
            if ((boolean) binding.tvONeg.getTag())
                optionClicked(binding.tvONeg);
            bloodGroup = "AB-";
            startHandler();
        });

        binding.tvOPos.setOnClickListener(view -> {
            if (!(boolean) binding.tvOPos.getTag())
                optionClicked(binding.tvOPos);
            if ((boolean) binding.tvAPos.getTag())
                optionClicked(binding.tvAPos);
            if ((boolean) binding.tvANeg.getTag())
                optionClicked(binding.tvANeg);
            if ((boolean) binding.tvBPos.getTag())
                optionClicked(binding.tvBPos);
            if ((boolean) binding.tvBNeg.getTag())
                optionClicked(binding.tvBNeg);
            if ((boolean) binding.tvABPos.getTag())
                optionClicked(binding.tvABPos);
            if ((boolean) binding.tvABNeg.getTag())
                optionClicked(binding.tvABNeg);
            if ((boolean) binding.tvONeg.getTag())
                optionClicked(binding.tvONeg);
            bloodGroup = "O+";
            startHandler();
        });

        binding.tvONeg.setOnClickListener(view -> {
            if (!(boolean) binding.tvONeg.getTag())
                optionClicked(binding.tvONeg);
            if ((boolean) binding.tvAPos.getTag())
                optionClicked(binding.tvAPos);
            if ((boolean) binding.tvANeg.getTag())
                optionClicked(binding.tvANeg);
            if ((boolean) binding.tvBPos.getTag())
                optionClicked(binding.tvBPos);
            if ((boolean) binding.tvBNeg.getTag())
                optionClicked(binding.tvBNeg);
            if ((boolean) binding.tvABPos.getTag())
                optionClicked(binding.tvABPos);
            if ((boolean) binding.tvABNeg.getTag())
                optionClicked(binding.tvABNeg);
            if ((boolean) binding.tvOPos.getTag())
                optionClicked(binding.tvOPos);
            bloodGroup = "O-";
            startHandler();
        });

        //Activity Pattern Start
        binding.tvInactive.setOnClickListener(view -> {
            if (!(boolean) binding.tvInactive.getTag())
                optionClicked(binding.tvInactive);
            if ((boolean) binding.tvSedentary.getTag())
                optionClicked(binding.tvSedentary);
            if ((boolean) binding.tvModerateA.getTag())
                optionClicked(binding.tvModerateA);
            if ((boolean) binding.tvHighlyA.getTag())
                optionClicked(binding.tvHighlyA);
            activityPattern = "Inactive";
            startHandler();
        });

        binding.tvSedentary.setOnClickListener(view -> {
            if (!(boolean) binding.tvSedentary.getTag())
                optionClicked(binding.tvSedentary);
            if ((boolean) binding.tvInactive.getTag())
                optionClicked(binding.tvInactive);
            if ((boolean) binding.tvModerateA.getTag())
                optionClicked(binding.tvModerateA);
            if ((boolean) binding.tvHighlyA.getTag())
                optionClicked(binding.tvHighlyA);
            activityPattern = "Sedentary";
            startHandler();
        });

        binding.tvModerateA.setOnClickListener(view -> {
            if (!(boolean) binding.tvModerateA.getTag())
                optionClicked(binding.tvModerateA);
            if ((boolean) binding.tvInactive.getTag())
                optionClicked(binding.tvInactive);
            if ((boolean) binding.tvSedentary.getTag())
                optionClicked(binding.tvSedentary);
            if ((boolean) binding.tvHighlyA.getTag())
                optionClicked(binding.tvHighlyA);
            activityPattern = "Moderately Active";
            startHandler();
        });

        binding.tvHighlyA.setOnClickListener(view -> {
            if (!(boolean) binding.tvHighlyA.getTag())
                optionClicked(binding.tvHighlyA);
            if ((boolean) binding.tvInactive.getTag())
                optionClicked(binding.tvInactive);
            if ((boolean) binding.tvSedentary.getTag())
                optionClicked(binding.tvSedentary);
            if ((boolean) binding.tvModerateA.getTag())
                optionClicked(binding.tvModerateA);
            activityPattern = "Highly Active";
            startHandler();
        });

        //Activity Pattern Start
        binding.tvUnhealthy.setOnClickListener(view -> {
            if (!(boolean) binding.tvUnhealthy.getTag())
                optionClicked(binding.tvUnhealthy);
            if ((boolean) binding.tvNotHealthy.getTag())
                optionClicked(binding.tvNotHealthy);
            if ((boolean) binding.tvHealthy.getTag())
                optionClicked(binding.tvHealthy);
            if ((boolean) binding.tvVeryHealthy.getTag())
                optionClicked(binding.tvVeryHealthy);
            dietPattern = "Unhealthy";
            startHandler();
        });

        binding.tvNotHealthy.setOnClickListener(view -> {
            if (!(boolean) binding.tvNotHealthy.getTag())
                optionClicked(binding.tvNotHealthy);
            if ((boolean) binding.tvUnhealthy.getTag())
                optionClicked(binding.tvUnhealthy);
            if ((boolean) binding.tvHealthy.getTag())
                optionClicked(binding.tvHealthy);
            if ((boolean) binding.tvVeryHealthy.getTag())
                optionClicked(binding.tvVeryHealthy);
            dietPattern = "Not Particularly Healthy";
            startHandler();
        });

        binding.tvHealthy.setOnClickListener(view -> {
            if (!(boolean) binding.tvHealthy.getTag())
                optionClicked(binding.tvHealthy);
            if ((boolean) binding.tvUnhealthy.getTag())
                optionClicked(binding.tvUnhealthy);
            if ((boolean) binding.tvNotHealthy.getTag())
                optionClicked(binding.tvNotHealthy);
            if ((boolean) binding.tvVeryHealthy.getTag())
                optionClicked(binding.tvVeryHealthy);
            dietPattern = "Reasonably Healthy";
            startHandler();
        });

        binding.tvVeryHealthy.setOnClickListener(view -> {
            if (!(boolean) binding.tvVeryHealthy.getTag())
                optionClicked(binding.tvVeryHealthy);
            if ((boolean) binding.tvUnhealthy.getTag())
                optionClicked(binding.tvUnhealthy);
            if ((boolean) binding.tvNotHealthy.getTag())
                optionClicked(binding.tvNotHealthy);
            if ((boolean) binding.tvHealthy.getTag())
                optionClicked(binding.tvHealthy);
            dietPattern = "Very Healthy";
            startHandler();
        });

        //Consume
        binding.tvAlcohol.setOnClickListener(view -> {
            optionClicked(binding.tvAlcohol);
            if ((boolean) binding.tvNone.getTag())
                optionClickedRed(binding.tvNone);
            if (consumeList.contains("None")) {
                consumeList.clear();
            }
            if (!consumeList.contains(binding.tvAlcohol.getText().toString())) {
                consumeList.add(binding.tvAlcohol.getText().toString());
            } else {
                consumeList.remove(binding.tvAlcohol.getText().toString());
            }
        });

        binding.tvTobacco.setOnClickListener(view -> {
            optionClicked(binding.tvTobacco);
            if ((boolean) binding.tvNone.getTag())
                optionClickedRed(binding.tvNone);

            if (consumeList.contains("None")) {
                consumeList.clear();
            }
            if (!consumeList.contains(binding.tvTobacco.getText().toString()))
                consumeList.add(binding.tvTobacco.getText().toString());
            else
                consumeList.remove(binding.tvTobacco.getText().toString());

        });

        binding.tvCigarette.setOnClickListener(view -> {
            optionClicked(binding.tvCigarette);
            if ((boolean) binding.tvNone.getTag())
                optionClickedRed(binding.tvNone);

            if (consumeList.contains("None")) {
                consumeList.clear();
            }
            if (!consumeList.contains(binding.tvCigarette.getText().toString()))
                consumeList.add(binding.tvCigarette.getText().toString());
            else
                consumeList.remove(binding.tvCigarette.getText().toString());
        });

        binding.tvNone.setOnClickListener(v -> {
            if (!(boolean) binding.tvNone.getTag())
                optionClickedRed(binding.tvNone);
            if ((boolean) binding.tvAlcohol.getTag())
                optionClicked(binding.tvAlcohol);
            if ((boolean) binding.tvTobacco.getTag())
                optionClicked(binding.tvTobacco);
            if ((boolean) binding.tvCigarette.getTag())
                optionClicked(binding.tvCigarette);
            consumeList.clear();
            consumeList.add("None");
            startHandler();
        });

        //Yes, No, and Don't know
        binding.tvYes.setOnClickListener(view -> {
            if (!(boolean) binding.tvYes.getTag()) {
                optionClicked(binding.tvYes);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.GONE);
                binding.ivMiddleBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_yes));
                startHandler();
            } else
                startHandler();
            if ((boolean) binding.tvNo.getTag())
                optionClickedRed(binding.tvNo);
            if ((boolean) binding.tvDoNotKnow.getTag())
                optionClicked(binding.tvDoNotKnow);
        });

        binding.tvNo.setOnClickListener(view -> {
            if (!(boolean) binding.tvNo.getTag()) {
                optionClickedRed(binding.tvNo);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.GONE);
                binding.ivMiddleBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_no));
                startHandler();
            } else
                startHandler();
            if ((boolean) binding.tvYes.getTag())
                optionClicked(binding.tvYes);
            if ((boolean) binding.tvDoNotKnow.getTag())
                optionClicked(binding.tvDoNotKnow);
        });

        binding.tvDoNotKnow.setOnClickListener(view -> {
            if (!(boolean) binding.tvDoNotKnow.getTag()) {
                optionClickedOrange(binding.tvDoNotKnow);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                startHandler();
            } else
                startHandler();
            if ((boolean) binding.tvYes.getTag())
                optionClicked(binding.tvYes);
            if ((boolean) binding.tvNo.getTag())
                optionClickedRed(binding.tvNo);
        });

        //Diet Style
        binding.tvVegetarian.setOnClickListener(view -> {
            if (!(boolean) binding.tvVegetarian.getTag())
                optionClicked(binding.tvVegetarian);
            if ((boolean) binding.tvNonVegetarian.getTag())
                optionClicked(binding.tvNonVegetarian);
            if ((boolean) binding.tvVegan.getTag())
                optionClicked(binding.tvVegan);
            if ((boolean) binding.tvEggetarian.getTag())
                optionClicked(binding.tvEggetarian);
            dietStyle = binding.tvVegetarian.getText().toString();
            startHandler();
        });

        binding.tvNonVegetarian.setOnClickListener(view -> {
            if (!(boolean) binding.tvNonVegetarian.getTag())
                optionClicked(binding.tvNonVegetarian);
            if ((boolean) binding.tvVegetarian.getTag())
                optionClicked(binding.tvVegetarian);
            if ((boolean) binding.tvVegan.getTag())
                optionClicked(binding.tvVegan);
            if ((boolean) binding.tvEggetarian.getTag())
                optionClicked(binding.tvEggetarian);
            dietStyle = binding.tvNonVegetarian.getText().toString();
            startHandler();
        });

        binding.tvVegan.setOnClickListener(view -> {
            if (!(boolean) binding.tvVegan.getTag())
                optionClicked(binding.tvVegan);
            if ((boolean) binding.tvVegetarian.getTag())
                optionClicked(binding.tvVegetarian);
            if ((boolean) binding.tvNonVegetarian.getTag())
                optionClicked(binding.tvNonVegetarian);
            if ((boolean) binding.tvEggetarian.getTag())
                optionClicked(binding.tvEggetarian);
            dietStyle = binding.tvVegan.getText().toString();
            startHandler();
        });

        binding.tvEggetarian.setOnClickListener(view -> {
            if (!(boolean) binding.tvEggetarian.getTag())
                optionClicked(binding.tvEggetarian);
            if ((boolean) binding.tvVegetarian.getTag())
                optionClicked(binding.tvVegetarian);
            if ((boolean) binding.tvNonVegetarian.getTag())
                optionClicked(binding.tvNonVegetarian);
            if ((boolean) binding.tvVegan.getTag())
                optionClicked(binding.tvVegan);
            dietStyle = binding.tvEggetarian.getText().toString();
            startHandler();
        });

        //Water Input
        binding.tvAdd.setOnClickListener(view -> {
            binding.edtWaterSleep.clearFocus();
            if (TextUtils.isEmpty(binding.edtWaterSleep.getText()) || Integer.parseInt(binding.edtWaterSleep.getText().toString()) < 1) {
                binding.edtWaterSleep.setText("1");
            } else if (Integer.parseInt(binding.edtWaterSleep.getText().toString()) >= 30) {
                binding.edtWaterSleep.setText("30");
            } else {
                binding.edtWaterSleep.setText(String.valueOf(Integer.parseInt(binding.edtWaterSleep.getText().toString()) + 1));
            }
        });

        binding.tvSubtract.setOnClickListener(view -> {
            binding.edtWaterSleep.clearFocus();
            if (TextUtils.isEmpty(binding.edtWaterSleep.getText())) {
                return;
            } else if (Integer.parseInt(binding.edtWaterSleep.getText().toString()) == 1) {
                return;
            } else {
                binding.edtWaterSleep.setText(String.valueOf(Integer.parseInt(binding.edtWaterSleep.getText().toString()) - 1));
            }
        });

        //Diseases input
        binding.tvBP.setOnClickListener(view -> {
            optionClicked(binding.tvBP);
            if ((boolean) binding.tvNoneDisease.getTag())
                optionClickedRed(binding.tvNoneDisease);

            if (diseasesList.contains("None")) {
                diseasesList.clear();
            }
            if (!diseasesList.contains(binding.tvBP.getText().toString()))
                diseasesList.add(binding.tvBP.getText().toString());
            else
                diseasesList.remove(binding.tvBP.getText().toString());
        });

        binding.tvDiabetes.setOnClickListener(view -> {
            optionClicked(binding.tvDiabetes);
            if ((boolean) binding.tvNoneDisease.getTag())
                optionClickedRed(binding.tvNoneDisease);

            if (diseasesList.contains("None")) {
                diseasesList.clear();
            }
            if (!diseasesList.contains(binding.tvDiabetes.getText().toString()))
                diseasesList.add(binding.tvDiabetes.getText().toString());
            else
                diseasesList.remove(binding.tvDiabetes.getText().toString());
        });

        binding.tvCholesterol.setOnClickListener(view -> {
            optionClicked(binding.tvCholesterol);
            if ((boolean) binding.tvNoneDisease.getTag())
                optionClickedRed(binding.tvNoneDisease);

            if (diseasesList.contains("None")) {
                diseasesList.clear();
            }
            if (!diseasesList.contains(binding.tvCholesterol.getText().toString()))
                diseasesList.add(binding.tvCholesterol.getText().toString());
            else
                diseasesList.remove(binding.tvCholesterol.getText().toString());
        });

        binding.tvThyroid.setOnClickListener(view -> {
            optionClicked(binding.tvThyroid);
            if ((boolean) binding.tvNoneDisease.getTag())
                optionClickedRed(binding.tvNoneDisease);

            if (diseasesList.contains("None")) {
                diseasesList.clear();
            }
            if (!diseasesList.contains(binding.tvThyroid.getText().toString()))
                diseasesList.add(binding.tvThyroid.getText().toString());
            else
                diseasesList.remove(binding.tvThyroid.getText().toString());
        });

        binding.tvNoneDisease.setOnClickListener(v -> {
            if (!(boolean) binding.tvNoneDisease.getTag())
                optionClickedRed(binding.tvNoneDisease);
            if ((boolean) binding.tvBP.getTag())
                optionClicked(binding.tvBP);
            if ((boolean) binding.tvDiabetes.getTag())
                optionClicked(binding.tvDiabetes);
            if ((boolean) binding.tvCholesterol.getTag())
                optionClicked(binding.tvCholesterol);
            if ((boolean) binding.tvThyroid.getTag())
                optionClicked(binding.tvThyroid);
            diseasesList.clear();
            diseasesList.add("None");
            startHandler();
        });

        binding.llBack.setOnClickListener(view -> {
            if (answeredCount == 0)
                finish();
            else if (answeredCount == 1) {
                isPressedOnPrevious = true;
                answeredCount--;
                setProgress();
                setQuestion();
                setAnswerView();
                binding.tvBack.setText("Back");
            } else {
                isPressedOnPrevious = true;
                answeredCount--;
                setProgress();
                setQuestion();
                setAnswerView();
            }
        });


        binding.btnOk.setOnClickListener(view -> {
            if (!hraAnswersData.isEmpty()) {
                switch (answeredCount) {
                    case 0:
                        hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(dob));
                        answeredCount++;
                        setProgress();
                        setQuestion();
                        setAnswerView();
                        binding.tvBack.setText("Previous");
                        Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                        statusHRA = HRA_STATUS_NEW;
                        saveData();
                        break;
                    case 1:
                        if (TextUtils.isEmpty(binding.edtWeight.getText())) {
                            Toast.makeText(context, "Please enter your weight", Toast.LENGTH_SHORT).show();
                            binding.edtWeight.requestFocus();
                            CommonUtils.showKeyboard(HRAQuestionsActivity.this);
                            break;
                        }
                        if (Double.parseDouble(binding.edtWeight.getText().toString()) < 25 || Double.parseDouble(binding.edtWeight.getText().toString()) > 200) {
                            Toast.makeText(context, "Please enter valid weight", Toast.LENGTH_SHORT).show();
                            binding.edtWeight.requestFocus();
                            CommonUtils.showKeyboard(HRAQuestionsActivity.this);
                            break;
                        }
                        hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(binding.edtWeight.getText().toString()));
                        SharedPref.putWeight(binding.edtWeight.getText().toString());
                        answeredCount++;
                        setProgress();
                        setQuestion();
                        setAnswerView();
                        Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                        statusHRA = HRA_STATUS_IN_PROGRESS;
                        saveData();
                        break;
                    case 2:
                        if ((boolean) binding.tvMale.getTag() || (boolean) binding.tvFemale.getTag() || (boolean) binding.tvOthers.getTag()) {
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(gender));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Select your gender", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3:
                        hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(
                                feetArray[binding.numberFeet.getValue()] + "." + inchesArray[binding.numberInches.getValue()]));
                        answeredCount++;
                        setProgress();
                        setQuestion();
                        setAnswerView();
                        Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                        statusHRA = HRA_STATUS_IN_PROGRESS;
                        saveData();
                        break;
                    case 4:
                        hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(waistArray[binding.numberWaist.getValue()]));
                        answeredCount++;
                        setProgress();
                        setQuestion();
                        setAnswerView();
                        Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                        statusHRA = HRA_STATUS_IN_PROGRESS;
                        saveData();
                        break;
                    case 5:
                        if (bloodGroup != null) {
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(bloodGroup));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select your blood group", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 6:
                        if (activityPattern != null) {
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(activityPattern));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please tell how active you are", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 7:
                        if (dietPattern != null) {
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(dietPattern));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please tell about your diet", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 8:
                        if (consumeList.size() > 0) {
                            StringBuilder consume = new StringBuilder();
                            for (int i = 0; i < consumeList.size(); i++) {
                                consume.append(consumeList.get(i));
                                if (i != consumeList.size() - 1) {
                                    consume.append(",");
                                }
                            }
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(consume.toString()));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select at least one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 9:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else
                                answer = "No";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetYesNo();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 10:
                        if (dietStyle != null) {
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(dietStyle));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 11:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else
                                answer = "No";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetYesNo();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 12:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else
                                answer = "No";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetYesNo();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 13:
                        if (!binding.edtWaterSleep.getText().toString().isEmpty()) {
                            if (Integer.parseInt(binding.edtWaterSleep.getText().toString()) >= 1 && Integer.parseInt(binding.edtWaterSleep.getText().toString()) <= 30) {
                                hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(binding.edtWaterSleep.getText().toString()));
                                answeredCount++;
                                setProgress();
                                setQuestion();
                                setAnswerView();
                                Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                                statusHRA = HRA_STATUS_IN_PROGRESS;
                                saveData();
                            } else {
                                Toast.makeText(context, "Please enter glasses of water in a range of 1-30", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please enter glasses of water", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 14:
                        if (!binding.edtWaterSleep.getText().toString().isEmpty()) {
                            if (Integer.parseInt(binding.edtWaterSleep.getText().toString()) > 0 && Integer.parseInt(binding.edtWaterSleep.getText().toString()) <= 20) {
                                hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(binding.edtWaterSleep.getText().toString()));
                                answeredCount++;
                                setProgress();
                                setQuestion();
                                setAnswerView();
                                Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                                statusHRA = HRA_STATUS_IN_PROGRESS;
                                saveData();
                            } else {
                                Toast.makeText(context, "Please enter hours of sleep in a range of 1-20", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please enter hours of sleep", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 15:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else
                                answer = "No";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetYesNo();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 16:
                        if (diseasesList.size() > 0) {
                            StringBuilder diseases = new StringBuilder();
                            for (int i = 0; i < diseasesList.size(); i++) {
                                diseases.append(diseasesList.get(i));
                                if (i != diseasesList.size() - 1) {
                                    diseases.append(",");
                                }
                            }
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(diseases.toString()));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetDiseases();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select at least one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 17:
                        if (diseasesList.size() > 0) {
                            StringBuilder diseases = new StringBuilder();
                            for (int i = 0; i < diseasesList.size(); i++) {
                                diseases.append(diseasesList.get(i));
                                if (i != diseasesList.size() - 1) {
                                    diseases.append(",");
                                }
                            }
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(diseases.toString()));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select at least one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 18:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else
                                answer = "No";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetYesNo();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 19:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag() || (boolean) binding.tvDoNotKnow.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else if ((boolean) binding.tvNo.getTag())
                                answer = "No";
                            else
                                answer = "Don't know";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetYesNo();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 20:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else
                                answer = "No";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetYesNo();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 21:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else
                                answer = "No";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetYesNo();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 22:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else
                                answer = "No";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetYesNo();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 23:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else
                                answer = "No";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetYesNo();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 24:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else
                                answer = "No";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            setProgress();
                            setQuestion();
                            setAnswerView();
                            resetYesNo();
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_IN_PROGRESS;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 25:
                        if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                            String answer;
                            if ((boolean) binding.tvYes.getTag())
                                answer = "Yes";
                            else
                                answer = "No";
                            hraAnswersData.get(answeredCount).setAnswer(Collections.singletonList(answer));
                            answeredCount++;
                            Log.d("AnswerJSON", new Gson().toJson(hraAnswersData));
                            statusHRA = HRA_STATUS_COMPLETED;
                            saveData();
                        } else {
                            Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
            Log.d("AnsweredCount", "From OK" + answeredCount);
        });


    }


    private void saveData() {
        Log.d("AnswerJSON statusHRA", statusHRA);
        submitHRA();
    }

    private void initViewTags() {
        binding.tvMale.setTag(false);
        binding.tvFemale.setTag(false);
        binding.tvOthers.setTag(false);
        binding.tvAPos.setTag(false);
        binding.tvANeg.setTag(false);
        binding.tvBPos.setTag(false);
        binding.tvBNeg.setTag(false);
        binding.tvABPos.setTag(false);
        binding.tvABNeg.setTag(false);
        binding.tvOPos.setTag(false);
        binding.tvONeg.setTag(false);
        binding.tvInactive.setTag(false);
        binding.tvSedentary.setTag(false);
        binding.tvModerateA.setTag(false);
        binding.tvHighlyA.setTag(false);
        binding.tvUnhealthy.setTag(false);
        binding.tvNotHealthy.setTag(false);
        binding.tvHealthy.setTag(false);
        binding.tvVeryHealthy.setTag(false);
        binding.tvAlcohol.setTag(false);
        binding.tvTobacco.setTag(false);
        binding.tvCigarette.setTag(false);
        binding.tvNone.setTag(false);
        binding.tvYes.setTag(false);
        binding.tvNo.setTag(false);
        binding.tvDoNotKnow.setTag(false);
        binding.tvVegetarian.setTag(false);
        binding.tvNonVegetarian.setTag(false);
        binding.tvVegan.setTag(false);
        binding.tvEggetarian.setTag(false);
        binding.tvBP.setTag(false);
        binding.tvDiabetes.setTag(false);
        binding.tvCholesterol.setTag(false);
        binding.tvThyroid.setTag(false);
        binding.tvNoneDisease.setTag(false);

    }

    private void startHandler() {
        if (isYesOrNo) {
            isYesOrNo = false;
        } else if (isDiseases)
            isDiseases = false;
        else {
            handler.postDelayed(() -> binding.btnOk.performClick(), 500);
        }
    }

    private void optionClicked(TextView textView) {
        textView.setTag(!(boolean) textView.getTag());
        if ((boolean) textView.getTag()) {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
//            textView.setTextColor(getResources().getColor(R.color.white));
        } else {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
//            textView.setTextColor(getResources().getColor(R.color.light_blue));
        }
    }

    private void optionClickedRed(TextView textView) {
        textView.setTag(!(boolean) textView.getTag());
        if ((boolean) textView.getTag()) {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_red_border_rc_bg_8dp));
//            textView.setTextColor(getResources().getColor(R.color.white));
        } else {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
//            textView.setTextColor(getResources().getColor(R.color.light_pink));
        }
    }

    private void optionClickedOrange(TextView textView) {
        textView.setTag(!(boolean) textView.getTag());
        if ((boolean) textView.getTag()) {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_orange_border_rc_bg_8dp));
//            textView.setTextColor(getResources().getColor(R.color.white));
        } else {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
//            textView.setTextColor(getResources().getColor(R.color.light_orange));
        }
    }

    private void setAnswerView() {
        switch (answeredCount) {
            case 0:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_orange_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_1));
                binding.datePicker.setVisibility(View.VISIBLE);
                binding.llWeight.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                break;
            case 1:
                if (hraAnswersData.get(answeredCount).getAnswer().size() > 0) {
                    binding.edtWeight.setText(hraAnswersData.get(answeredCount).getAnswer().get(0));
                }
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_blue_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_2));
                binding.datePicker.setVisibility(View.GONE);
                binding.llWeight.setVisibility(View.VISIBLE);
                binding.llGender.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                break;
            case 2:
                if (hraAnswersData.get(answeredCount).getAnswer().size() > 0) {
                    setGenderAnswer(hraAnswersData.get(answeredCount).getAnswer().get(0));
                } else {
                    checkforgenderstore();
                }
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_pink_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
                binding.llWeight.setVisibility(View.GONE);
                binding.llGender.setVisibility(View.VISIBLE);
                binding.llHeight.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 3:
                if (hraAnswersData.get(answeredCount).getAnswer().size() > 0) {
                    setFeetAnswer(hraAnswersData.get(answeredCount).getAnswer().get(0).split("\\.")[0]);
                    setInchesAnswer(hraAnswersData.get(answeredCount).getAnswer().get(0).split("\\.")[1]);
                }
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_blue_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_4));
                binding.llGender.setVisibility(View.GONE);
                binding.llHeight.setVisibility(View.VISIBLE);
                binding.llBloodGroup.setVisibility(View.GONE);
                binding.llWaist.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                break;
            case 4:
                if (hraAnswersData.get(answeredCount).getAnswer().size() > 0) {
                    setWaistAnswer(hraAnswersData.get(answeredCount).getAnswer().get(0));
                }
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_pink_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_1));
                binding.llYesNo.setVisibility(View.GONE);
                binding.llHeight.setVisibility(View.GONE);
                binding.llWaist.setVisibility(View.VISIBLE);
                binding.llBloodGroup.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                break;
            case 5:
                if (hraAnswersData.get(answeredCount).getAnswer().size() > 0) {
                    Log.d("BG", "" + hraAnswersData.get(answeredCount).getAnswer().get(0));
                    setBGAnswer(hraAnswersData.get(answeredCount).getAnswer().get(0));
                }
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_orange_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
                binding.llHeight.setVisibility(View.GONE);
                binding.llBloodGroup.setVisibility(View.VISIBLE);
                binding.llActivityPattern.setVisibility(View.GONE);
                binding.llWaist.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 6:
                if (hraAnswersData.get(answeredCount).getAnswer().size() > 0) {
                    setActiveAnswer(hraAnswersData.get(answeredCount).getAnswer().get(0));
                }
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_pink_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
                binding.llBloodGroup.setVisibility(View.GONE);
                binding.llActivityPattern.setVisibility(View.VISIBLE);
                binding.llDietPattern.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 7:
                if (hraAnswersData.get(answeredCount).getAnswer().size() > 0) {
                    setPreferAnswer(hraAnswersData.get(answeredCount).getAnswer().get(0));
                }

                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_orange_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
                binding.llActivityPattern.setVisibility(View.GONE);
                binding.llDietPattern.setVisibility(View.VISIBLE);
                binding.llConsume.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 8:
                if (hraAnswersData.get(answeredCount).getAnswer().size() > 0) {
                    setConsumeAnswer(hraAnswersData.get(answeredCount).getAnswer());
                }
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_blue_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
                binding.llDietPattern.setVisibility(View.GONE);
                binding.llConsume.setVisibility(View.VISIBLE);
                binding.llYesNo.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                break;
            case 9:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_orange_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                binding.llConsume.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.VISIBLE);
                refillYesNo();
                binding.llDietStyle.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 10:
                if (hraAnswersData.get(answeredCount).getAnswer().size() > 0) {
                    setEatAnswer(hraAnswersData.get(answeredCount).getAnswer().get(0));
                }
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_pink_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
                binding.llYesNo.setVisibility(View.GONE);
                binding.llDietStyle.setVisibility(View.VISIBLE);
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 11:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_blue_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                binding.llDietStyle.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.VISIBLE);
                refillYesNo();
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 12:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_orange_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.VISIBLE);
                refillYesNo();
                binding.llWaterSleep.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 13:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_pink_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.ivTopBearSleep.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_water));
                binding.llYesNo.setVisibility(View.GONE);
                binding.llWaterSleep.setVisibility(View.VISIBLE);
                binding.edtWaterSleep.setText(hraAnswersData.get(answeredCount).getAnswer().size() == 0 ? "" : hraAnswersData.get(answeredCount).getAnswer().get(0));
                binding.tvEditHead.setText("Glasses");
                binding.edtWaterSleep.setHint("Water");
                binding.tvWaterNote.setVisibility(View.VISIBLE);
                binding.edtWaterSleep.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                binding.btnOk.setVisibility(View.VISIBLE);
                break;
            case 14:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_pink_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.ivTopBearSleep.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.GONE);
                binding.llWaterSleep.setVisibility(View.VISIBLE);
                binding.tvEditHead.setText("Hours");
                binding.tvWaterNote.setVisibility(View.GONE);
                binding.edtWaterSleep.setText(hraAnswersData.get(answeredCount).getAnswer().size() == 0 ? "" : hraAnswersData.get(answeredCount).getAnswer().get(0));
                binding.edtWaterSleep.setHint("Sleep");
                binding.edtWaterSleep.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                binding.btnOk.setVisibility(View.VISIBLE);
                break;
            case 15:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_orange_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.ivTopBearSleep.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                binding.llWaterSleep.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.VISIBLE);
                refillYesNo();
                binding.llDiseases.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 16:
                /*if(hraAnswersData.get(answeredCount).getAnswer().size() > 0){
                    setDiagnosedAnswer(hraAnswersData.get(answeredCount).getAnswer().get(0));
                }*/
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_blue_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
                binding.llYesNo.setVisibility(View.GONE);
                binding.llDiseases.setVisibility(View.VISIBLE);
                refillDiseases();
                binding.btnOk.setVisibility(View.VISIBLE);
                break;
            case 17:
                /*if(hraAnswersData.get(answeredCount).getAnswer().size() > 0){
                    setDiagnosedAnswer(hraAnswersData.get(answeredCount).getAnswer().get(0));
                }*/
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_pink_bg));
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
                binding.llDiseases.setVisibility(View.VISIBLE);
                refillDiseases();
                binding.llYesNo.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                break;
            case 18:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_orange_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                binding.llDiseases.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.VISIBLE);
                refillYesNo();
                binding.tvDoNotKnow.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 19:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_blue_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                binding.tvDoNotKnow.setVisibility(View.VISIBLE);
                refillYesNo();
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 20:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_pink_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                binding.tvDoNotKnow.setVisibility(View.GONE);
                refillYesNo();
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 21:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_orange_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                refillYesNo();
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 22:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_blue_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                refillYesNo();
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 23:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_pink_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                refillYesNo();
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 24:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_orange_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                refillYesNo();
                binding.btnOk.setVisibility(View.GONE);
                break;
            case 25:
                binding.ivTopBg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_blue_bg));
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.VISIBLE);
                refillYesNo();
                binding.llWaist.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.GONE);
                break;

        }
    }

    private void checkforgenderstore() {
        if (!genderfrompref.isEmpty()) {
            if (genderfrompref.equals("MALE")) {
                optionClicked(binding.tvMale);
                gender = "Male";

            } else if (genderfrompref.equals("FEMALE")) {
                optionClicked(binding.tvFemale);
                gender = "Female";


            } else if (genderfrompref.equals("OTHERS")) {
                optionClicked(binding.tvOthers);
                gender = "Others";

            } else {
                //donothing
            }
        }
    }

    private void setQuestion() {
        binding.tvQuestion.setText(hraAnswersData.get(answeredCount).getQuestion());
    }

    private void setProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.hraProgress.setProgress(answeredCount, true);
        } else {
            binding.hraProgress.setProgress(answeredCount);
        }
        handler.removeCallbacksAndMessages(null);
    }

    private void resetYesNo() {
        if (hraAnswersData.get(answeredCount).getAnswer().size() == 0) {
            if ((boolean) binding.tvYes.getTag())
                optionClicked(binding.tvYes);
            if ((boolean) binding.tvNo.getTag())
                optionClickedRed(binding.tvNo);
            if ((boolean) binding.tvDoNotKnow.getTag())
                optionClickedOrange(binding.tvDoNotKnow);
            isYesOrNo = false;
        }
    }

    private void refillYesNo() {
        if (hraAnswersData.get(answeredCount).getAnswer().size() > 0) {
            isYesOrNo = true;
            if (hraAnswersData.get(answeredCount).getAnswer().get(0).equalsIgnoreCase("yes")) {
                binding.tvYes.performClick();
                binding.ivMiddleBearDoNotKnow.setVisibility(View.GONE);
                binding.ivMiddleBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_yes));
                isYesOrNo = false;
            } else if (hraAnswersData.get(answeredCount).getAnswer().get(0).equalsIgnoreCase("no")) {
                binding.tvNo.performClick();
                binding.ivMiddleBearDoNotKnow.setVisibility(View.GONE);
                binding.ivMiddleBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_no));
                isYesOrNo = false;
            } else {
                binding.tvDoNotKnow.performClick();
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
                isYesOrNo = false;
            }
        }
    }

    private void refillDiseases() {
        if (hraAnswersData.get(answeredCount).getAnswer().size() > 0) {
            diseasesList.clear();
            isDiseases = true;
            if (hraAnswersData.get(answeredCount).getAnswer().get(0).contains(binding.tvBP.getText().toString())) {
                if (!(boolean) binding.tvBP.getTag())
                    optionClicked(binding.tvBP);
                if (!diseasesList.contains(binding.tvBP.getText().toString()))
                    diseasesList.add(binding.tvBP.getText().toString());
            } else {
                if ((boolean) binding.tvBP.getTag())
                    optionClicked(binding.tvBP);
                diseasesList.remove(binding.tvBP.getText().toString());
            }
            if (hraAnswersData.get(answeredCount).getAnswer().get(0).contains(binding.tvDiabetes.getText().toString())) {
                if (!(boolean) binding.tvDiabetes.getTag())
                    optionClicked(binding.tvDiabetes);
                if (!diseasesList.contains(binding.tvDiabetes.getText().toString()))
                    diseasesList.add(binding.tvDiabetes.getText().toString());
            } else {
                if ((boolean) binding.tvDiabetes.getTag())
                    optionClicked(binding.tvDiabetes);
                diseasesList.remove(binding.tvDiabetes.getText().toString());
            }
            if (hraAnswersData.get(answeredCount).getAnswer().get(0).contains(binding.tvCholesterol.getText().toString())) {
                if (!(boolean) binding.tvCholesterol.getTag())
                    optionClicked(binding.tvCholesterol);
                if (!diseasesList.contains(binding.tvCholesterol.getText().toString()))
                    diseasesList.add(binding.tvCholesterol.getText().toString());
            } else {
                if ((boolean) binding.tvCholesterol.getTag())
                    optionClicked(binding.tvCholesterol);
                diseasesList.remove(binding.tvCholesterol.getText().toString());
            }
            if (hraAnswersData.get(answeredCount).getAnswer().get(0).contains(binding.tvThyroid.getText().toString())) {
                if (!(boolean) binding.tvThyroid.getTag())
                    optionClicked(binding.tvThyroid);
                if (!diseasesList.contains(binding.tvThyroid.getText().toString()))
                    diseasesList.add(binding.tvThyroid.getText().toString());
            } else {
                if ((boolean) binding.tvThyroid.getTag())
                    optionClicked(binding.tvThyroid);
                diseasesList.remove(binding.tvThyroid.getText().toString());
            }
            if (hraAnswersData.get(answeredCount).getAnswer().get(0).contains(binding.tvNoneDisease.getText().toString()))
                binding.tvNoneDisease.performClick();
            isDiseases = false;
        }
    }

    private void resetDiseases() {
        if (hraAnswersData.get(answeredCount).getAnswer().size() == 0) {
            if ((boolean) binding.tvBP.getTag())
                optionClicked(binding.tvBP);
            if ((boolean) binding.tvDiabetes.getTag())
                optionClicked(binding.tvDiabetes);
            if ((boolean) binding.tvCholesterol.getTag())
                optionClicked(binding.tvCholesterol);
            if ((boolean) binding.tvThyroid.getTag())
                optionClicked(binding.tvThyroid);
            if ((boolean) binding.tvNoneDisease.getTag())
                optionClickedRed(binding.tvNoneDisease);
            diseasesList.clear();
        }
    }

    private void getHRAAnswers() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetHraRequest request = new GetHraRequest(context.getString(R.string.hra_i_id), "", "");
        Log.d("req", new Gson().toJson(request));
        Log.d("req", SharedPref.getAuthToken());
        Call<GetHraAnswersResponse> call = apiInterfaceWyh.getHraAnswers(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<GetHraAnswersResponse>() {
            @Override
            public void onResponse(Call<GetHraAnswersResponse> call, Response<GetHraAnswersResponse> response) {
                try {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();

                    statusHRA = response.body().getStatus();

                    if (response.code() == 401) {
                        refreshAuthToken(null);
                    }


                    if (response.code() == 200 && response.body() != null) {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_hra_answers_success));
                        SharedPref.putConversationID(response.body().getConversationId());
                        Log.d("response ", response.body().getConversationId());
                        Log.d("response Share", SharedPref.getConversationID());
                        String hraJson = response.body().getAnswerJson();

                        Type type = new TypeToken<List<HraAnswersData>>() {
                        }.getType();
                        hraAnswersData = new Gson().fromJson(hraJson, type);
                        binding.hraProgress.setMax(hraAnswersData.size());
                        if (hraAnswersData.get(0).getAnswer().size() > 0) {
                            Log.d("dob", hraAnswersData.get(0).getAnswer().get(0));
                            setDatePickerAnswer(hraAnswersData.get(0).getAnswer().get(0));
                        }
                        if (hraAnswersData.get(4).getAnswer().size() > 0) {
                            setWaistAnswer(SharedPref.getUserWaist());
                        }
                        if (response.body().getStatus().equalsIgnoreCase(HRA_STATUS_COMPLETED) || response.body().getStatus().equalsIgnoreCase(HRA_STATUS_NEW)) {
                            generateConversationID();
                            for (HraAnswersData item : hraAnswersData) {
                                if (item.getAnswer().size() > 0) {
                                    item.getAnswer().clear();
                                }
                            }
                        }





                    /*if(response.body().getStatus().equalsIgnoreCase(HRA_STATUS_NEW)){
                        if(response.body().getConversationId().isEmpty() && SharedPref.getConversationID().isEmpty()){
                            generateConversationID();
                        }
                    }*/


                        setProgress();
                        setQuestion();
                    } else {
                        Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_hra_answers_failed));
                        Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<GetHraAnswersResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.get_hra_answers_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateConversationID() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        ConversationIdReq conversationIdReq = new ConversationIdReq(getString(R.string.hra_i_id));
        Call<ConversationIdResponse> call = apiInterfaceWyh.createConversationI(SharedPref.getAuthToken(), conversationIdReq);
        call.enqueue(new Callback<ConversationIdResponse>() {
            @Override
            public void onResponse(Call<ConversationIdResponse> call, Response<ConversationIdResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_create_fresh_conversation_success));
                    SharedPref.putConversationID(response.body().getConversationId());
                    Log.d("response genrate", response.body().getConversationId());
                    Log.d("response Share genrate", SharedPref.getConversationID());
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_create_fresh_conversation_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConversationIdResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_create_fresh_conversation_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void submitHRA() {
        spinRewardsData = null;
        quizathonRewardData = null;
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        SaveHraRequest request = new SaveHraRequest(getString(R.string.hra_i_id), SharedPref.getConversationID(),
                "1.0.0.0", statusHRA, new Gson().toJson(hraAnswersData), CommonUtils.todayDateInFormat("yyyy-MM-dd"),
                CommonUtils.todayDateInFormat("yyyy-MM-dd"));
        Log.d("SaveRequest", new Gson().toJson(request));
        Log.d("response share save", SharedPref.getConversationID());
        if (Objects.equals(statusHRA, HRA_STATUS_COMPLETED)) {
            Log.d("SaveRequest", statusHRA);
        }
        Call<SaveAnswersResponse> call = apiInterfaceWyh.saveHRA(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<SaveAnswersResponse>() {
            @Override
            public void onResponse(Call<SaveAnswersResponse> call, Response<SaveAnswersResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();

                Log.d("response", new Gson().toJson(response.body()));
                Log.d("response", new Gson().toJson(response.code()));
                if (response.code() == 401) {
                    refreshAuthToken("submitHRA");
                }
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.save_hra_answers_success));

                    SharedPref.putUserGender(genderfrompref);
                    SharedPref.putDOB(dob);
                    if (Objects.equals(statusHRA, HRA_STATUS_COMPLETED)) {
                        SharedPref.putUserWaist(waistArray[binding.numberWaist.getValue()]);
                        Toast.makeText(HRAQuestionsActivity.this, "HRA saved successfully", Toast.LENGTH_SHORT).show();
                        if (response.body().getRewards() != null && response.body().getRewards().getReward() != null) {
                            NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, response.body().getRewards().getReward()));
                        }
                        if (response.body().getSpinTheWheelRewardsModel() != null) {
                            spinRewardsData = response.body().getSpinTheWheelRewardsModel();
                        } else if (response.body().getQuizathonRewardData() != null) {
                            quizathonRewardData = response.body().getQuizathonRewardData();
                        } else {
                            spinRewardsData = null;
                            quizathonRewardData = null;
                            if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null) {
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, response.body().getRewards().getBonusRewards()));
                            }
                            if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));
                            }
                            if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null && !TextUtils.isEmpty(response.body().getEnGTokens().getBonusTokens())) {
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                            }

                            if (response.body().getFeedbackDetails() != null && response.body().getFeedbackDetails().getFeedbackModel() != null && response.body().getFeedbackDetails().getStarConfig() != null) {
                                NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(FeedbackPOPUP, ""));
                                NewDashboardHelper.Companion.setFeedbackResponseData(response.body().getFeedbackDetails());
                            }
                        }

                        getAnalysis();
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.save_hra_answers_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SaveAnswersResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.save_hra_answers_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAnalysis() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetAnalysisRequest request = new GetAnalysisRequest(getString(R.string.hra_i_id), "");
        Call<GetAnalysisResponse> call = apiInterfaceWyh.getAnalysis(SharedPref.getAuthToken(), request);
        Log.d("response HRA", new Gson().toJson(request));
        call.enqueue(new Callback<GetAnalysisResponse>() {
            @Override
            public void onResponse(Call<GetAnalysisResponse> call, Response<GetAnalysisResponse> response) {
                Log.d("response HRA", new Gson().toJson(response.body()));
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {
                    refreshAuthToken("analysis");
                }
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_get_analysis_success));
                    Log.d("response HRA", new Gson().toJson(response.body()));
                    SharedPref.putHRAAnalysis(new Gson().toJson(response.body()));
                    /*if (response.body().getRewards() != null && response.body().getRewards().getReward() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, response.body().getRewards().getReward()));
                    }
                    if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, response.body().getRewards().getBonusRewards()));
                    }
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStamp, response.body().getEnGTokens().getTokens()));
                    }
                    if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null && !TextUtils.isEmpty(response.body().getEnGTokens().getBonusTokens())) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(TokenStampBounce, response.body().getEnGTokens().getBonusTokens()));
                    }*/
//                    NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, "Test;ahVSNX 150 znxjkB"));
                    Intent intent = new Intent(context, HRAAnalysisActivity.class);
                        intent.putExtra("quizrewards", true);
                    if (spinRewardsData != null) {
                        intent.putExtra("spinrewards", spinRewardsData);
                    }

                    intent.putExtra("popups", new Gson().toJson(NewDashboardHelper.Companion.getPopUpShowModels()));
                    startActivity(intent);
                    finish();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_get_analysis_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetAnalysisResponse> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.hra_get_analysis_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRewardsPopup(String rewards) {
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
            Intent intent = new Intent(context, HRAAnalysisActivity.class);
            startActivity(intent);
            finish();
        });
        binding.btnPositive.setOnClickListener(view -> {
            alertDialog.dismiss();
            Intent intent = new Intent(context, RewardsActivity.class);
            context.startActivity(intent);
            finish();
        });
        binding.scratchView.onFullReveal();
        binding.scratchView.setScratchListener(HRAQuestionsActivity.this);

        Rect displayRectangle = new Rect();
        Window window = ((HRAQuestionsActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), (int) (displayRectangle.height() * 0.6f));
    }

    private void refreshAuthToken(String submitHRA) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        String deviceModel = Build.BRAND + " " + Build.MODEL;
        String osVersion = Build.VERSION.RELEASE + "(" + Build.VERSION.SDK_INT + ")";
        String appVersion = SDKConstants.appVersionName;
        RefreshTokenRequest request = new RefreshTokenRequest(deviceModel, osVersion, appVersion);
        Call<RefreshTokenResponse> call = apiInterfaceWyh.refreshToken(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().isSuccess() &&
                        response.body().getData().getAuthToken() != null && !response.body().getData().getAuthToken().equals("")) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_success));
                    SharedPref.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    SharedPreference.putAuthToken("Bearer " + response.body().getData().getAuthToken());
                    if (submitHRA != null) {
                        if (submitHRA.equals("analysis"))
                            getAnalysis();
                        else
                            submitHRA();
                    } else
                        getHRAAnswers();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                    /*Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                    Master.INSTANCE.logOut(context);
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.refresh_token_failed));
                /*Toast.makeText(context, getResources().getString(R.string.session_time_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MobileNumberActivity.class);
                    startActivity(intent);
                    SharedPref.clearSharedPref();
                    finishAffinity();*/
                Master.INSTANCE.logOut(context);
            }
        });
    }

    @Override
    public void onScratchComplete() {

    }

    @Override
    public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
        /*if (isStamps) {
            if (i >= 20) {
                isStamps = false;
                scratchCardLayout.onFullReveal();
            }
        } else {
            if (i >= 20) {
                scratchCardLayout.onFullReveal();
            }
        }*/

        if (i > 20) {

            if (isStamps) {
                isStamps = false;
//                scratchTokenReward();
            }


            scratchCardLayout.onFullReveal();
        }
    }

    @Override
    public void onScratchStarted() {

    }

   /* private void showStampsPopup(String rewards, Context context) {
        isStamps = true;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();

        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];


        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


//        binding.scratchView.onFullReveal();


        binding.btnPositive.setOnClickListener(view -> {
           isPositiveBtn = true;
            alertDialog.dismiss();
        });

        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });

        binding.scratchView.setScratchListener(this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void showBonusStampPopup(String rewards, Context context) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

        CustomPopupStampsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_popup_stamps, null, false);        alertBuilder.setView(binding.getRoot());
        alertBuilder.setView(binding.getRoot());
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing())
            alertDialog.show();


        String title = rewards.split(";")[0];
        String message = rewards.split(";")[1];
        String points = rewards.split(";")[2];

        binding.tvTitle.setText(title);
        binding.tvDescription.setText(message);
        binding.tvDescription2.setText("No. of Stamps: " + points);


        binding.btnPositive.setOnClickListener(view -> {
           isPositiveBtn = true;
            alertDialog.dismiss();
        });
        binding.ivClose.setOnClickListener(view -> {
            alertDialog.dismiss();
            showRewardsPopupDialogBox();
        });
        binding.scratchView.setScratchListener(HRAQuestionsActivity.this);

        binding.btnNegative.setOnClickListener(view -> alertDialog.dismiss());

        Rect displayRectangle = new Rect();
        Window window = ((DashboardActivity) context).getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.getWindow().setLayout((int) (displayRectangle.width() *
                0.7f), RelativeLayout.LayoutParams.WRAP_CONTENT);
    }*/

    private void setBGAnswer(String answer) {
        boolean c = answer.equalsIgnoreCase("B-");
        if (!isprev) {
            if (answer.equalsIgnoreCase("A+")) {
                if (!(boolean) binding.tvAPos.getTag())
                    optionClicked(binding.tvAPos);
                bloodGroup = "A+";
            } else if (answer.equalsIgnoreCase("A-")) {
                if (!(boolean) binding.tvANeg.getTag())
                    optionClicked(binding.tvANeg);
                bloodGroup = "A-";
            } else if (answer.equalsIgnoreCase("B+")) {
                if (!(boolean) binding.tvBPos.getTag())
                    optionClicked(binding.tvBPos);
                bloodGroup = "B+";
            } else if (answer.equalsIgnoreCase("B-")) {
                if (!(boolean) binding.tvBNeg.getTag())
                    optionClicked(binding.tvBNeg);
                bloodGroup = "B-";
            } else if (answer.equalsIgnoreCase("AB+")) {
                if (!(boolean) binding.tvABPos.getTag())
                    optionClicked(binding.tvABPos);
                bloodGroup = "AB+";
            } else if (answer.equalsIgnoreCase("AB-")) {
                if (!(boolean) binding.tvABNeg.getTag())
                    optionClicked(binding.tvABNeg);
                bloodGroup = "AB-";
            } else if (answer.equalsIgnoreCase("O+")) {
                if (!(boolean) binding.tvOPos.getTag())
                    optionClicked(binding.tvOPos);
                bloodGroup = "O+";
            } else if (answer.equalsIgnoreCase("O-")) {
                if (!(boolean) binding.tvONeg.getTag())
                    optionClicked(binding.tvONeg);
                bloodGroup = "O-";
            }
        }

    }

    private void setActiveAnswer(String answer) {
        if (answer.equalsIgnoreCase("Inactive")) {
            if (!(boolean) binding.tvInactive.getTag())
                optionClicked(binding.tvInactive);
        } else if (answer.equalsIgnoreCase("Sedentary")) {
            if (!(boolean) binding.tvSedentary.getTag())
                optionClicked(binding.tvSedentary);
        } else if (answer.equalsIgnoreCase("Moderately Active")) {
            if (!(boolean) binding.tvModerateA.getTag())
                optionClicked(binding.tvModerateA);
        } else if (answer.equalsIgnoreCase("Highly Active")) {
            if (!(boolean) binding.tvHighlyA.getTag())
                optionClicked(binding.tvHighlyA);
        }
    }

    private void setPreferAnswer(String answer) {
        if (answer.equalsIgnoreCase("Unhealthy")) {
            if (!(boolean) binding.tvUnhealthy.getTag())
                optionClicked(binding.tvUnhealthy);
        } else if (answer.equalsIgnoreCase("Not Particularly Healthy")) {
            if (!(boolean) binding.tvNotHealthy.getTag())
                optionClicked(binding.tvNotHealthy);
        } else if (answer.equalsIgnoreCase("Reasonably Healthy")) {
            if (!(boolean) binding.tvHealthy.getTag())
                optionClicked(binding.tvHealthy);
        } else if (answer.equalsIgnoreCase("Very Healthy")) {
            if (!(boolean) binding.tvVeryHealthy.getTag())
                optionClicked(binding.tvVeryHealthy);
        }
    }

    private void setConsumeAnswer(List<String> data) {
        consumeList.clear();
        String[] data1 = data.get(0).toString().split(",");
        Log.d("data", new Gson().toJson(data));
        for (String answer : data1) {
            Log.d("data ans", answer);
            if (answer.equalsIgnoreCase("Alcohol")) {
                if (!(boolean) binding.tvAlcohol.getTag())
                    optionClicked(binding.tvAlcohol);
                consumeList.add("Alcohol");
            } else if (answer.equalsIgnoreCase("Tobacco")) {
                if (!(boolean) binding.tvTobacco.getTag())
                    optionClicked(binding.tvTobacco);
                consumeList.add("Tobacco");
            } else if (answer.equalsIgnoreCase("Cigarette")) {
                if (!(boolean) binding.tvCigarette.getTag())
                    optionClicked(binding.tvCigarette);
                consumeList.add("Cigarette");
            } else if (answer.equalsIgnoreCase("None")) {
                if (!(boolean) binding.tvNone.getTag())
                    optionClickedRed(binding.tvNone);
                consumeList.add("None");
            }
        }
    }

    private void setEatAnswer(String answer) {
        if (answer.equalsIgnoreCase("Vegetarian")) {
            if (!(boolean) binding.tvVegetarian.getTag())
                optionClicked(binding.tvVegetarian);
        } else if (answer.equalsIgnoreCase("Non Vegetarian")) {
            if (!(boolean) binding.tvNonVegetarian.getTag())
                optionClicked(binding.tvNonVegetarian);
        } else if (answer.equalsIgnoreCase("Vegan")) {
            if (!(boolean) binding.tvVegan.getTag())
                optionClicked(binding.tvVegan);
        } else if (answer.equalsIgnoreCase("Eggetarian")) {
            if (!(boolean) binding.tvEggetarian.getTag())
                optionClicked(binding.tvEggetarian);
        }
    }

    private void setGenderAnswer(String answer) {
        if (answer.equalsIgnoreCase("Male")) {
            if (!(boolean) binding.tvMale.getTag())
                optionClicked(binding.tvMale);
            gender = "Male";
            genderfrompref = "Male";
        } else if (answer.equalsIgnoreCase("Female")) {
            if (!(boolean) binding.tvFemale.getTag())
                optionClicked(binding.tvFemale);
            gender = "Female";
            genderfrompref = "Female";
        } else if (answer.equalsIgnoreCase("Others")) {
            if (!(boolean) binding.tvOthers.getTag())
                optionClicked(binding.tvOthers);
            gender = "Others";
            genderfrompref = "Others";
        }
    }

    private void setWaistAnswer(String answer) {
        for (int i = 0; i < waistArray.length; i++) {
            if (waistArray[i].equalsIgnoreCase(answer)) {
                binding.numberWaist.setValue(i);
                break;
            }
        }
    }

    private void setFeetAnswer(String answer) {
        if (answer.equalsIgnoreCase("0")) {
            binding.numberFeet.setValue(Integer.parseInt(answer));
        } else {
            binding.numberFeet.setValue(Integer.parseInt(answer) - 1);
        }
    }

    private void setInchesAnswer(String answer) {
        if (answer.equalsIgnoreCase("0")) {
            binding.numberInches.setValue(Integer.parseInt(answer));
        } else {
            binding.numberInches.setValue(Integer.parseInt(answer));
        }
    }

    private void setDatePickerAnswer(String answer) {
        int day = Integer.parseInt(answer.split("/")[0]);
        int month = Integer.parseInt(answer.split("/")[1]);
        int year = Integer.parseInt(answer.split("/")[2]);
        Log.d("dob", answer);
        Log.d("dob", month + "");
        binding.datePicker.updateDate(year, month - 1, day);
    }


}