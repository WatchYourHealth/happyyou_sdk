package com.wyh.happyyousdk.heartAge;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;
import static com.wyh.happyyousdk.utils.CommonUtils.getDateInFormat;
import static com.wyh.happyyousdk.utils.CommonUtils.hideKeyboard;
import static com.wyh.happyyousdk.utils.Constants.HEART_AGE_STATUS_COMPLETED;
import static com.wyh.happyyousdk.utils.Constants.HRA_STATUS_IN_PROGRESS;
import static com.wyh.happyyousdk.utils.Constants.Rewards;
import static com.wyh.happyyousdk.utils.Constants.RewardsBounce;
import static com.wyh.happyyousdk.utils.Constants.TokenStamp;
import static com.wyh.happyyousdk.utils.Constants.TokenStampBounce;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.ActivityHeartAgeQuestionsBinding;
import com.wyh.happyyousdk.utils.Master;
import com.wyh.happyyousdk.model.PopUpShowModel;
import com.wyh.happyyousdk.model.RewardsModel;
import com.wyh.happyyousdk.model.request.heartAge.FetchHeartAgeQuestionsRequest;
import com.wyh.happyyousdk.model.request.heartAge.GetHeartAgeAnalysisRequest;
import com.wyh.happyyousdk.model.request.heartAge.HeartAgeConversationIdReq;
import com.wyh.happyyousdk.model.request.heartAge.SaveHeartAgeRequest;
import com.wyh.happyyousdk.model.response.AssignRewardsResponse;
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeAnalysisResponse;
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeConversationIdResponse;
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeQuestions;
import com.wyh.happyyousdk.login.MobileNumberActivity;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.login.RefreshTokenRequest;
import com.wyh.happyyousdk.model.response.heartAge.HeartAgeQuestionsResponse;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.model.response.ira.IRAResponse;
import com.wyh.happyyousdk.model.response.login.RefreshTokenResponse;
import com.wyh.happyyousdk.model.response.playwin.QuizathonRewardData;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.rewards.FeedbackPopupDialogBox;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeartAgeQuestionsActivity extends AppCompatActivity {

    ActivityHeartAgeQuestionsBinding binding;
    Context context;
    String genderfrompref = "";
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    String dob, gender = "";
    String[] feetArray = new String[8];
    String[] inchesArray = new String[12];
    boolean isPressedOnPrevious = false;
    boolean isFirst = true;
    boolean IsHRACompleted = false;

    Handler handler;

    //    List<HeartAgeQuestionsResponse> heartAgeQuestions = new ArrayList<>();
    List<HeartAgeQuestions> heartAgeQuestionsResponseList = new ArrayList<>();

    private AssignRewardsResponse.SpinRewardsData spinTheWheelRewardsModel = null;
    private QuizathonRewardData quizathonRewardData = null;

    int answeredCount = 0;
    boolean isnonchangeprevious = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_heart_age_questions);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        binding.tvAgeNote.setVisibility(View.VISIBLE);
        binding.btnOk.setVisibility(View.VISIBLE);
        genderfrompref = SharedPref.getUserGender();
//
//        heartAgeQuestionsResponseList = new Gson().fromJson("[{\"QuestionId\":\"ask_cholesterol_level\",\"Question\":\"Do you know your cholesterol level?\",\"QuestionType\":\"singleselectbutton\",\"Options\":[\"Yes\",\"No\"],\"Answer\":\"\"},{\"QuestionId\":\"ask_age\",\"Question\":\"Please Enter your\",\"QuestionType\":\"number\",\"Options\":[],\"Answer\":\"\"},{\"QuestionId\":\"ask_gender\",\"Question\":\"Please Select your Gender\",\"QuestionType\":\"singleselectbutton\",\"Options\":[\"Male\",\"Female\"],\"Answer\":\"\"},{\"QuestionId\":\"ask_height\",\"Question\":\"Please Enter your Height\",\"QuestionType\":\"number\",\"Options\":[],\"Answer\":\"\"},{\"QuestionId\":\"ask_weight\",\"Question\":\"Please Enter your weight\",\"QuestionType\":\"decimal\",\"Options\":[],\"Answer\":\"\"},{\"QuestionId\":\"ask_diabetic\",\"Question\":\"Are you Diabetic?\",\"QuestionType\":\"singleselectbutton\",\"Options\":[\"Yes\",\"No\"],\"Answer\":\"\"},{\"QuestionId\":\"ask_smoked\",\"Question\":\"Have you ever smoked?\",\"QuestionType\":\"singleselectbutton\",\"Options\":[\"Yes\",\"No\"],\"Answer\":\"\"},{\"QuestionId\":\"ask_medication_high_bp\",\"Question\":\"Are you under Medication for High Blood Pressure?\",\"QuestionType\":\"singleselectbutton\",\"Options\":[\"Yes\",\"No\"],\"Answer\":\"\"},{\"QuestionId\":\"ask_bp\",\"Question\":\"Do you know your blood pressure numbers?\",\"QuestionType\":\"singleselectbutton\",\"Options\":[\"Yes\",\"No\"],\"Answer\":\"\"},{\"QuestionId\":\"ask_sbp\",\"Question\":\"What is your Systolic Blood Pressure Number?\",\"QuestionType\":\"number\",\"Options\":[],\"Answer\":\"\"},{\"QuestionId\":\"ask_total_cholesterol\",\"Question\":\"What is your Total Cholesterol Number?\",\"QuestionType\":\"number\",\"Options\":[],\"Answer\":\"\"},{\"QuestionId\":\"ask_hdl\",\"Question\":\"What is your HDL Number?\",\"QuestionType\":\"number\",\"Options\":[],\"Answer\":\"\"}]",
//                new TypeToken<List<HeartAgeQuestionsResponse>>() {
//                }.getType());
        for (int i = 1; i <= 8; i++) {
            feetArray[i - 1] = String.valueOf(i);
        }
        for (int i = 0; i < 12; i++) {
            inchesArray[i] = String.valueOf(i);
        }

        binding.numberFeet.setMinValue(0);
        binding.numberFeet.setMaxValue(feetArray.length - 1);
        binding.numberFeet.setDisplayedValues(feetArray);
        binding.numberInches.setMinValue(0);
        binding.numberInches.setMaxValue(inchesArray.length - 1);
        binding.numberInches.setDisplayedValues(inchesArray);
        //binding.numberWaist.setMinValue(0);
        //binding.numberWaist.setMaxValue(waistArray.length - 1);
        //binding.numberWaist.setDisplayedValues(waistArray);

        getHeartAgeQuestions();

        GetAnalysisResponse getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);

        if (getAnalysisResponse != null && getAnalysisResponse.getAnalysisData() != null) {
            binding.edtWeight.setText(Math.round(getAnalysisResponse.getAnalysisData().getIdealBMIWeight().getWeight()) + "");
            String feet = getAnalysisResponse.getAnalysisData().getHeights().split("\\.")[0];
            String inches = getAnalysisResponse.getAnalysisData().getHeights().split("\\.")[1];
            binding.numberFeet.setValue(Integer.parseInt(feet) - 1);
            binding.numberInches.setValue(Integer.parseInt(inches));
        }

        handler = new Handler();

        initViewTags();

        Calendar dobCalendar = Calendar.getInstance();
        dobCalendar.add(Calendar.YEAR, -30);
        dob = getDateInFormat(dobCalendar.getTime(), "dd/MM/yyyy");


        int day;
        int month1;
        int year1;
        if (!SharedPref.getDOB().isEmpty() && SharedPref.getDOB() != null) {
            String split = SharedPref.getDOB().split(" ")[0];
            String[] splitDob;
            if (split.contains("/")) {
                splitDob = split.split("/");
                day = Integer.valueOf(splitDob[0]);
                month1 = Integer.valueOf(splitDob[1]);
                year1 = Integer.valueOf(splitDob[2]);
            } else {
                splitDob = split.split("-");
                day = Integer.valueOf(splitDob[2]);
                month1 = Integer.valueOf(splitDob[1]);
                year1 = Integer.valueOf(splitDob[0]);
            }
            if (CommonUtils.getAge(year1, month1, day) < 30) {
                binding.edtAge.setText("30");
            } else {
                binding.edtAge.setText(CommonUtils.getAge(year1, month1, day) + "");
            }
            month1--;
        } else {
            year1 = dobCalendar.get(Calendar.YEAR);
            day = dobCalendar.get(Calendar.MONTH);
            month1 = dobCalendar.get(Calendar.DAY_OF_MONTH);
        }
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

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        //Gender Selection
        binding.tvMale.setOnClickListener(view -> {
            isPressedOnPrevious = false;
            binding.tvMale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
            binding.tvFemale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvOthers.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));


            gender = "Male";
            genderfrompref = "MALE";
            startHandler();
        });

        binding.tvFemale.setOnClickListener(view -> {
            isPressedOnPrevious = false;
            binding.tvMale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvFemale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
            binding.tvOthers.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));


            gender = "Female";
            genderfrompref = "FEMALE";
            startHandler();
        });

        binding.tvOthers.setOnClickListener(view -> {
            isPressedOnPrevious = false;
            binding.tvMale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvFemale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvOthers.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));

            gender = "Others";
            genderfrompref = "OTHERS";
            startHandler();
        });

       /* setQuestion();
        setProgress();*/


        binding.edtAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               /* if (charSequence.length() >= 2) {
                    CommonUtils.hideKeyboard(HeartAgeQuestionsActivity.this);
                }*/
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.edtSBP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    // Your action on done
                    checkAndForward();
                    return true;
                }
                return false;
            }
        });


        binding.edtWeight.setText(SharedPref.getWeight());


        binding.edtDBP.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    // Your action on done
                    checkAndForward();

                    return true;
                }
                return false;
            }
        });
        binding.edtAge.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    // Your action on done
                    checkAndForward();


                    return true;
                }
                return false;
            }
        });

        binding.edtWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               /* if (charSequence.length() >= 3) {
                    CommonUtils.hideKeyboard(HeartAgeQuestionsActivity.this);
                }*/
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.edtWeight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    // Your action on done
                    checkAndForward();

                    return true;
                }
                return false;
            }
        });

        binding.edtTotalCholesterol.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    // Your action on done
                    checkAndForward();

                    return true;
                }
                return false;
            }
        });
        binding.edtHdlCholesterol.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    //Your action on done
                    checkAndForward();

                    return true;
                }
                return false;
            }
        });


        //Yes, No, and Don't know
        binding.tvYes.setOnClickListener(view -> {
            if (isnonchangeprevious) {
                binding.tvYes.setTag(false);
                binding.tvDoNotKnow.setTag(true);
                binding.tvNo.setTag(true);
            }
            binding.tvYes.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
            binding.tvNo.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvYes.setTag(true);
            binding.tvNo.setTag(false);

            //optionClicked(binding.tvYes);
            binding.ivMiddleBearDoNotKnow.setVisibility(View.GONE);
            binding.ivMiddleBear.setVisibility(View.VISIBLE);
            binding.ivMiddleBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_yes));
            startHandler();
            /*if (!(boolean) binding.tvYes.getTag()) {

            }*/
            if ((boolean) binding.tvNo.getTag())
                optionClickedRed(binding.tvNo);
           /* if ((boolean) binding.tvDoNotKnow.getTag())
                optionClicked(binding.tvDoNotKnow);*/
        });

        binding.tvNo.setOnClickListener(view -> {
            if (isnonchangeprevious) {
                binding.tvYes.setTag(true);
                binding.tvDoNotKnow.setTag(true);
                binding.tvNo.setTag(false);
            }
            binding.tvYes.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
            binding.tvNo.setBackground(ContextCompat.getDrawable(context, R.drawable.light_red_border_rc_bg_8dp));
            binding.tvYes.setTag(false);
            binding.tvNo.setTag(true);
            // optionClickedRed(binding.tvNo);
            binding.ivMiddleBearDoNotKnow.setVisibility(View.GONE);
            binding.ivMiddleBear.setVisibility(View.VISIBLE);
            binding.ivMiddleBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_no));
            startHandler();
            /*if (!(boolean) binding.tvNo.getTag()) {

            }*/
            if ((boolean) binding.tvYes.getTag())
                optionClicked(binding.tvYes);
        });

        binding.llBack.setOnClickListener(view -> {
            if (answeredCount == 0)
                finish();
            else if (answeredCount == 1) {
                isPressedOnPrevious = true;
                answeredCount--;
                setProgress();
                setQuestion();
                setAnswerView("back");
                binding.tvBack.setText("Back");
            } else {
//                if (answeredCount == 10 && heartAgeQuestionsResponseList.get(0).getAnswer().equalsIgnoreCase("Yes") && heartAgeQuestionsResponseList.get(8).getAnswer().equalsIgnoreCase("No")) {
//                    answeredCount = answeredCount - 2;
//
//                } else {
                answeredCount--;
//                }
                isPressedOnPrevious = true;
                isnonchangeprevious = true;
                checkForYesselectedormaleselected();
                setProgress();
                setQuestion();
                setAnswerView("back");

            }

        });


        binding.btnOk.setOnClickListener(view -> {
            switch (answeredCount) {
                case 0:
                    if (TextUtils.isEmpty(binding.edtAge.getText())) {
                        Toast.makeText(context, "Please enter your age", Toast.LENGTH_SHORT).show();
                        binding.edtAge.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    if (Double.parseDouble(binding.edtAge.getText().toString()) < 30 || Double.parseDouble(binding.edtAge.getText().toString()) > 74) {
                        Toast.makeText(context, "Enter a Value Between 30-74", Toast.LENGTH_SHORT).show();
                        binding.edtAge.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtAge.getText().toString());
                    answeredCount++;
                    initViewTags();
                    generateHeartAgeConversationID(heartAgeQuestionsResponseList, "0");
                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    break;

                case 1:
                    if (!gender.equals("")) {
                        heartAgeQuestionsResponseList.get(answeredCount).setAnswer(gender);

                        answeredCount++;
                        generateHeartAgeConversationID(heartAgeQuestionsResponseList, "1");
                        Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    } else {
                        Toast.makeText(context, "Select your gender", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case 2:
                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(feetArray[binding.numberFeet.getValue()] + "." + inchesArray[binding.numberInches.getValue()]);
                    answeredCount++;
                    generateHeartAgeConversationID(heartAgeQuestionsResponseList, "2");
                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    break;
                case 3:
                    if (TextUtils.isEmpty(binding.edtWeight.getText())) {
                        Toast.makeText(context, "Please enter your weight", Toast.LENGTH_SHORT).show();
                        binding.edtWeight.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    if (Double.parseDouble(binding.edtWeight.getText().toString()) < 25 || Double.parseDouble(binding.edtWeight.getText().toString()) > 200) {
                        Toast.makeText(context, "Please enter valid weight", Toast.LENGTH_SHORT).show();
                        binding.edtWeight.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtWeight.getText().toString().replace(".", ""));
                    answeredCount++;
                    generateHeartAgeConversationID(heartAgeQuestionsResponseList, "3");
                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    break;
                case 4:
                    if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                        String answer;
                        if ((boolean) binding.tvYes.getTag())
                            answer = "Yes";
                        else
                            answer = "No";
                        heartAgeQuestionsResponseList.get(answeredCount).setAnswer(answer);
                        answeredCount++;
                        resetYesNo();
                        generateHeartAgeConversationID(heartAgeQuestionsResponseList, "4");
                        Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    } else {
                        Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                        String answer;
                        if ((boolean) binding.tvYes.getTag())
                            answer = "Yes";
                        else
                            answer = "No";
                        heartAgeQuestionsResponseList.get(answeredCount).setAnswer(answer);
                        answeredCount++;
                        resetYesNo();
                        generateHeartAgeConversationID(heartAgeQuestionsResponseList, "5");
                        Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    } else {
                        Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                        String answer;
                        if ((boolean) binding.tvYes.getTag())
                            answer = "Yes";
                        else
                            answer = "No";
                        heartAgeQuestionsResponseList.get(answeredCount).setAnswer(answer);
                        answeredCount++;
                        resetYesNo();
                        generateHeartAgeConversationID(heartAgeQuestionsResponseList, "6");
                        Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    } else {
                        Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                    }
                    break;


                case 7:
                    if (TextUtils.isEmpty(binding.edtSBP.getText())) {
                        Toast.makeText(context, "Enter a Value Between 90-200", Toast.LENGTH_SHORT).show();
                        binding.edtSBP.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    if (Double.parseDouble(binding.edtSBP.getText().toString()) < 90 || Double.parseDouble(binding.edtSBP.getText().toString()) > 200) {
                        Toast.makeText(context, "Enter a Value Between 90-200", Toast.LENGTH_SHORT).show();
                        binding.edtSBP.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtSBP.getText().toString());
                    answeredCount++;
                    generateHeartAgeConversationID(heartAgeQuestionsResponseList, "7");
                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    break;


                case 8:
                    if (TextUtils.isEmpty(binding.edtDBP.getText())) {
                        Toast.makeText(context, "Enter a Value Between 60-90", Toast.LENGTH_SHORT).show();
                        binding.edtDBP.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    if (Double.parseDouble(binding.edtDBP.getText().toString()) < 60 || Double.parseDouble(binding.edtDBP.getText().toString()) > 90) {
                        Toast.makeText(context, "Enter a Value Between 60-90", Toast.LENGTH_SHORT).show();
                        binding.edtDBP.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtDBP.getText().toString());
                    answeredCount++;
                    generateHeartAgeConversationID(heartAgeQuestionsResponseList, "8");
                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    break;
                case 9:
                    if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                        String answer;
                        if ((boolean) binding.tvYes.getTag())
                            answer = "Yes";
                        else
                            answer = "No";
                        heartAgeQuestionsResponseList.get(answeredCount).setAnswer(answer);
                        if (answer.equalsIgnoreCase("No")) {
                            generateHeartAgeConversationID(heartAgeQuestionsResponseList, "9");
                        } else {
                            answeredCount++;
                            resetYesNo();
                            generateHeartAgeConversationID(heartAgeQuestionsResponseList, "9");
                        }
                    } else {
                        Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("AnswerJSON 2", new Gson().toJson(heartAgeQuestionsResponseList));
                    break;

                case 10:
                    if (TextUtils.isEmpty(binding.edtTotalCholesterol.getText())) {
                        Toast.makeText(context, "Please enter your Cholesterol", Toast.LENGTH_SHORT).show();
                        binding.edtTotalCholesterol.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    if (Double.parseDouble(binding.edtTotalCholesterol.getText().toString()) < 100 || Double.parseDouble(binding.edtTotalCholesterol.getText().toString()) > 405) {
                        Toast.makeText(context, "Enter A Value between 100-405", Toast.LENGTH_SHORT).show();
                        binding.edtWeight.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtTotalCholesterol.getText().toString());
                    answeredCount++;
                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    generateHeartAgeConversationID(heartAgeQuestionsResponseList, "10");
                    break;

                case 11:
                    if (TextUtils.isEmpty(binding.edtHdlCholesterol.getText())) {
                        Toast.makeText(context, "Please enter your HDL Cholesterol", Toast.LENGTH_SHORT).show();
                        binding.edtHdlCholesterol.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    if (Double.parseDouble(binding.edtHdlCholesterol.getText().toString()) < 10 || Double.parseDouble(binding.edtHdlCholesterol.getText().toString()) > 100) {
                        Toast.makeText(context, "Enter A Value Between 10-100", Toast.LENGTH_SHORT).show();
                        binding.edtHdlCholesterol.requestFocus();
                        CommonUtils.showKeyboard(this);
                        break;
                    }
                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtHdlCholesterol.getText().toString());
                    generateHeartAgeConversationID(heartAgeQuestionsResponseList, "11");
                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    break;
            }
        });
    }

    private void checkForYesselectedormaleselected() {
        binding.tvMale.setTag(false);
        binding.tvFemale.setTag(false);
        binding.tvOthers.setTag(false);


    }

    private void checkAndForward() {
        switch (answeredCount) {
            case 0:
                if (TextUtils.isEmpty(binding.edtAge.getText())) {
                    Toast.makeText(context, "Please enter your age", Toast.LENGTH_SHORT).show();
                    binding.edtAge.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                if (Double.parseDouble(binding.edtAge.getText().toString()) < 30 || Double.parseDouble(binding.edtAge.getText().toString()) > 74) {
                    Toast.makeText(context, "Enter a Value Between 30-74", Toast.LENGTH_SHORT).show();
                    binding.edtAge.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtAge.getText().toString());
                //heartAgeQuestionsResponseList.get(answeredCount).getAnswer();
                answeredCount++;
                generateHeartAgeConversationID(heartAgeQuestionsResponseList, "0");
                Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                break;
                   /* if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                        String answer;
                        if ((boolean) binding.tvYes.getTag())
                            answer = "Yes";
                        else
                            answer = "No";
                        heartAgeQuestionsResponseList.get(answeredCount).setAnswer(answer);
                        answeredCount++;
                        setProgress();
                        setQuestion();
                        setAnswerView();
                        resetYesNo();
                        binding.tvBack.setText("Previous");
                        Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                    } else {
                        Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                    }
                    break;*/
            case 1:
                if (!gender.equals("")) {
                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(gender);
                    answeredCount++;
                    generateHeartAgeConversationID(heartAgeQuestionsResponseList, "1");

                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                } else {
                    Toast.makeText(context, "Select your gender", Toast.LENGTH_SHORT).show();
                }
                break;

            case 2:
                heartAgeQuestionsResponseList.get(answeredCount).setAnswer(
                        feetArray[binding.numberFeet.getValue()] + "." + inchesArray[binding.numberInches.getValue()]);
                answeredCount++;
                generateHeartAgeConversationID(heartAgeQuestionsResponseList, "2");

                Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                break;
            case 3:
                if (TextUtils.isEmpty(binding.edtWeight.getText())) {
                    Toast.makeText(context, "Please enter your weight", Toast.LENGTH_SHORT).show();
                    binding.edtWeight.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                if (Double.parseDouble(binding.edtWeight.getText().toString()) < 25 || Double.parseDouble(binding.edtWeight.getText().toString()) > 200) {
                    Toast.makeText(context, "Please enter valid weight", Toast.LENGTH_SHORT).show();
                    binding.edtWeight.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtWeight.getText().toString());
                answeredCount++;
                generateHeartAgeConversationID(heartAgeQuestionsResponseList, "3");
                SharedPref.putWeight(binding.edtWeight.getText().toString());

                Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                break;
            case 4:
                if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                    String answer;
                    if ((boolean) binding.tvYes.getTag())
                        answer = "Yes";
                    else
                        answer = "No";
                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(answer);
                    answeredCount++;
                    generateHeartAgeConversationID(heartAgeQuestionsResponseList, "4");
                    resetYesNo();
                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                } else {
                    Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                    String answer;
                    if ((boolean) binding.tvYes.getTag())
                        answer = "Yes";
                    else
                        answer = "No";
                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(answer);
                    answeredCount++;
                    generateHeartAgeConversationID(heartAgeQuestionsResponseList, "5");
                    resetYesNo();
                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                } else {
                    Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                }
                break;
            case 6:
                if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                    String answer;
                    if ((boolean) binding.tvYes.getTag())
                        answer = "Yes";
                    else
                        answer = "No";
                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(answer);
                    answeredCount++;
                    generateHeartAgeConversationID(heartAgeQuestionsResponseList, "6");

                    resetYesNo();
                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                } else {
                    Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                }
                break;


            case 7:
                if (TextUtils.isEmpty(binding.edtSBP.getText())) {
                    Toast.makeText(context, "Enter a Value Between 90-200", Toast.LENGTH_SHORT).show();
                    binding.edtSBP.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                if (Double.parseDouble(binding.edtSBP.getText().toString()) < 90 || Double.parseDouble(binding.edtSBP.getText().toString()) > 200) {
                    Toast.makeText(context, "Enter a Value Between 90-200", Toast.LENGTH_SHORT).show();
                    binding.edtSBP.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtSBP.getText().toString());
                answeredCount++;
                generateHeartAgeConversationID(heartAgeQuestionsResponseList, "7");


                Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                break;


            case 8:
                if (TextUtils.isEmpty(binding.edtDBP.getText())) {
                    Toast.makeText(context, "Enter a Value Between 60-90", Toast.LENGTH_SHORT).show();
                    binding.edtDBP.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                if (Double.parseDouble(binding.edtDBP.getText().toString()) < 60 || Double.parseDouble(binding.edtDBP.getText().toString()) > 90) {
                    Toast.makeText(context, "Enter a Value Between 60-90", Toast.LENGTH_SHORT).show();
                    binding.edtDBP.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtDBP.getText().toString());
                answeredCount++;
                generateHeartAgeConversationID(heartAgeQuestionsResponseList, "8");


                Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                break;
            case 9:
                if ((boolean) binding.tvYes.getTag() || (boolean) binding.tvNo.getTag()) {
                    String answer;
                    if ((boolean) binding.tvYes.getTag())
                        answer = "Yes";
                    else
                        answer = "No";

                    heartAgeQuestionsResponseList.get(answeredCount).setAnswer(answer);
                    if (answer.equalsIgnoreCase("no")) {
                        generateHeartAgeConversationID(heartAgeQuestionsResponseList, "11");
                    } else {
                        answeredCount++;
                        generateHeartAgeConversationID(heartAgeQuestionsResponseList, "9");
                    }

                    Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                } else {
                    Toast.makeText(context, "Please select one option", Toast.LENGTH_SHORT).show();
                }
                break;


            case 10:
                if (TextUtils.isEmpty(binding.edtTotalCholesterol.getText())) {
                    Toast.makeText(context, "Please enter your Cholesterol", Toast.LENGTH_SHORT).show();
                    binding.edtTotalCholesterol.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                if (Double.parseDouble(binding.edtTotalCholesterol.getText().toString()) < 100 || Double.parseDouble(binding.edtTotalCholesterol.getText().toString()) > 405) {
                    Toast.makeText(context, "Enter A Value between 100-405", Toast.LENGTH_SHORT).show();
                    binding.edtWeight.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtTotalCholesterol.getText().toString());
                answeredCount++;
                generateHeartAgeConversationID(heartAgeQuestionsResponseList, "10");
                Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                break;

            case 11:
                if (TextUtils.isEmpty(binding.edtHdlCholesterol.getText())) {
                    Toast.makeText(context, "Please enter your HDL Cholesterol", Toast.LENGTH_SHORT).show();
                    binding.edtHdlCholesterol.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                if (Double.parseDouble(binding.edtHdlCholesterol.getText().toString()) < 10 || Double.parseDouble(binding.edtHdlCholesterol.getText().toString()) > 100) {
                    Toast.makeText(context, "Enter A Value Between 10-100", Toast.LENGTH_SHORT).show();
                    binding.edtHdlCholesterol.requestFocus();
                    CommonUtils.showKeyboard(this);
                    break;
                }
                heartAgeQuestionsResponseList.get(answeredCount).setAnswer(binding.edtHdlCholesterol.getText().toString());
                generateHeartAgeConversationID(heartAgeQuestionsResponseList, "11");
                Log.d("AnswerJSON", new Gson().toJson(heartAgeQuestionsResponseList));
                break;
        }
    }

    private void startHandler() {
        if (isPressedOnPrevious) {
            isPressedOnPrevious = false;
        } else {
            handler.postDelayed(() -> checkAndForward(), 500);
        }
    }

    private void generateHeartAgeConversationID(List<HeartAgeQuestions> heartAgeQuestionsResponseList, String caseNumber) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        HeartAgeConversationIdReq conversationIdReq = new HeartAgeConversationIdReq(getString(R.string.heartage_i_id), "1.0.0.0");
        Call<HeartAgeConversationIdResponse> call = apiInterfaceWyh.createHeartAgeConversationID(SharedPref.getAuthToken(), conversationIdReq);
        Log.d("ConversationIdReqRequest", new Gson().toJson(conversationIdReq));

        call.enqueue(new Callback<HeartAgeConversationIdResponse>() {
            @Override
            public void onResponse(Call<HeartAgeConversationIdResponse> call, Response<HeartAgeConversationIdResponse> response) {
                Log.d("ConversationIdReqResponse", new Gson().toJson(response.body()));

                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    SharedPref.putHeartAgeConversationID(response.body().getData().getConversationId());
                    if (!caseNumber.equals("11")) {
                        setProgress();
                        setQuestion();
                        setAnswerView("noBack");
                        IsHRACompleted = false;
                    } else {
                        IsHRACompleted = true;
                    }
                    saveHeartAge(heartAgeQuestionsResponseList, caseNumber, IsHRACompleted);
                } else {
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HeartAgeConversationIdResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveHeartAge(List<HeartAgeQuestions> heartAgeQuestionsResponseList, String caseNumber, boolean IsAssessmentCompleted) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        SaveHeartAgeRequest request;
        if (IsAssessmentCompleted) {
            request = new SaveHeartAgeRequest(getString(R.string.heartage_i_id), SharedPref.getHeartAgeConversationID(),
                    "1.0.0.0", HEART_AGE_STATUS_COMPLETED, new Gson().toJson(heartAgeQuestionsResponseList), CommonUtils.todayDateInFormat("yyyy-MM-dd"),
                    CommonUtils.todayDateInFormat("yyyy-MM-dd"), IsAssessmentCompleted);
        } else {
            request = new SaveHeartAgeRequest(getString(R.string.heartage_i_id), SharedPref.getHeartAgeConversationID(),
                    "1.0.0.0", HRA_STATUS_IN_PROGRESS, new Gson().toJson(heartAgeQuestionsResponseList), CommonUtils.todayDateInFormat("yyyy-MM-dd"),
                    CommonUtils.todayDateInFormat("yyyy-MM-dd"), IsAssessmentCompleted);
        }

        Log.d("SaveRequest", new Gson().toJson(request));
        Call<CommonSuccessResponse> call = apiInterfaceWyh.saveHeartAgeAnswers(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {
                    refreshAuthToken("saveHeartAge");
                }
                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    //Savegender();
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_save_answers_failed));
                    //Toast.makeText(HeartAgeQuestionsActivity.this, "Heart age saved successfully", Toast.LENGTH_SHORT).show();


                    spinTheWheelRewardsModel = response.body().getSpinTheWheelRewardsModel();
                    quizathonRewardData = response.body().getQuizathonRewardData();


                    if (caseNumber.equals("11")) {
                        getHeartAnalysis(response.body().getRewards());
                    }

                    /*if(response.body().getFeedbackDetails() != null && response.body().getFeedbackDetails().getFeedbackModel() != null && response.body().getFeedbackDetails().getStarConfig() != null ){
                        FeedbackPopupDialogBox instance = FeedbackPopupDialogBox.Companion.getInstance();
                        instance.showPopUpFeedback(HeartAgeQuestionsActivity.this, response.body().getFeedbackDetails());
                    }*/

                    //finish();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_save_answers_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_save_answers_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void Savegender() {
        if (gender.equals("Male")) {
            SharedPref.putUserGender("MALE");

        } else if (genderfrompref.equals("Female")) {
            SharedPref.putUserGender("FEMALE");


        } else if (genderfrompref.equals("Others")) {
            SharedPref.putUserGender("OTHERS");

        } else {
            //donothing
        }

    }

    private void getHeartAnalysis(RewardsModel rewards) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetHeartAgeAnalysisRequest request = new GetHeartAgeAnalysisRequest(getString(R.string.heartage_i_id), SharedPref.getHeartAgeConversationID());
        Call<HeartAgeAnalysisResponse> call = apiInterfaceWyh.getHeartAgeAnalysis(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<HeartAgeAnalysisResponse>() {
            @Override
            public void onResponse(Call<HeartAgeAnalysisResponse> call, Response<HeartAgeAnalysisResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 401) {
                    refreshAuthToken("analysis");
                }
                if (response.body() != null && response.code() == 200) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_get_analysis_success));

                    if (rewards != null && rewards.getReward() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(Rewards, rewards.getReward()));

                    }
                    if (rewards != null && rewards.getBonusRewards() != null) {
                        NewDashboardHelper.Companion.getPopUpShowModels().add(new PopUpShowModel(RewardsBounce, rewards.getBonusRewards()));
                    }


                    HeartAgeAnalysisActivity.feedbackResponseData = response.body().getFeedbackDetails();
                    SharedPref.putHeartAgeAnalysis(new Gson().toJson(response.body()));
                    Intent intent = new Intent(context, HeartAgeAnalysisActivity.class);
                    intent.putExtra("quizrewards", true);
                    if (spinTheWheelRewardsModel != null) {
                        intent.putExtra("spinrewards", spinTheWheelRewardsModel);
                    }

                    intent.putExtra("popups", new Gson().toJson(NewDashboardHelper.Companion.getPopUpShowModels()));
                    startActivity(intent);
                    finish();
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_get_analysis_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HeartAgeAnalysisResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_get_analysis_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshAuthToken(String saveHeartAge) {
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
                    if (saveHeartAge != null) {
                        if (saveHeartAge.equals("analysis"))
                            getHeartAnalysis(new RewardsModel());
                        else
                            saveHeartAge(heartAgeQuestionsResponseList, "", false);
                    } /*else
                        getHRAAnswers();*/
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


    private void getHeartAgeQuestions() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        FetchHeartAgeQuestionsRequest request = new FetchHeartAgeQuestionsRequest(getResources().getString(R.string.heartage_i_id), "1.0.0.0");
        Call<HeartAgeQuestionsResponse> call = apiInterfaceWyh.getHeartAgeQuestions(SharedPref.getAuthToken(), request);
        call.enqueue(new Callback<HeartAgeQuestionsResponse>() {
            @Override
            public void onResponse(Call<HeartAgeQuestionsResponse> call, Response<HeartAgeQuestionsResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_get_answers_new_success));

                    //String heartAgeJson = ;
                    Type type = new TypeToken<List<HeartAgeQuestions>>() {
                    }.getType();

                    SharedPref.putHeartAgeConversationID(response.body().getData().getConversationId());

                    heartAgeQuestionsResponseList = new Gson().fromJson(response.body().getData().getAnswerJson(), type);

                    /*if(isFirst){
                        for(HeartAgeQuestions item: heartAgeQuestionsResponseList){
                            if(item.getAnswer() != null && !item.getAnswer().isEmpty()){
                                item.setAnswer("");
                            }
                        }
                    }*/
                    binding.hraProgress.setMax(heartAgeQuestionsResponseList.size());
                    setProgress();
                    setQuestion();
                    setAnswerView("noBack");
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_get_answers_new_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HeartAgeQuestionsResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.heart_age_get_answers_new_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkforgenderstore() {
        if (!genderfrompref.isEmpty()) {
            if (genderfrompref.equals("MALE")) {
                binding.tvMale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
                binding.tvFemale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                binding.tvOthers.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));

                gender = "Male";

            } else if (genderfrompref.equals("FEMALE")) {
                binding.tvMale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                binding.tvFemale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
                binding.tvOthers.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));

                gender = "Female";


            } else if (genderfrompref.equals("OTHERS")) {
                binding.tvMale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                binding.tvFemale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                binding.tvOthers.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
                gender = "Others";

            } else {
                //donothing
            }
        }
    }

    private void setAnswerView(String comingFrom) {
        switch (answeredCount) {
            case 0:
                if (!comingFrom.equals("back")) {
                    if (heartAgeQuestionsResponseList.get(answeredCount).getAnswer() != null) {
                        if (!heartAgeQuestionsResponseList.get(answeredCount).getAnswer().equals("")) {
                            binding.edtAge.setText(heartAgeQuestionsResponseList.get(answeredCount).getAnswer());
                        }
                    }
                }

                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.tvAgeNote.setVisibility(View.VISIBLE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
                binding.llCenterBear.setVisibility(View.GONE);
                binding.llAge.setVisibility(View.VISIBLE);
//                binding.edtWeight.setVisibility(View.VISIBLE);
                binding.llYesNo.setVisibility(View.GONE);
                binding.datePicker.setVisibility(View.GONE);
                binding.llGender.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);

                break;
            case 1:
                if (!comingFrom.equals("back")) {
                    if (heartAgeQuestionsResponseList.get(answeredCount).getAnswer() != null) {
                        if (!heartAgeQuestionsResponseList.get(answeredCount).getAnswer().equals("")) {
                            if (heartAgeQuestionsResponseList.get(answeredCount).getAnswer().equalsIgnoreCase("male")) {
                                gender = "male";
                                genderfrompref = "male";
                                binding.tvMale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
                                binding.tvFemale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                                binding.tvOthers.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));

                            } else if (heartAgeQuestionsResponseList.get(answeredCount).getAnswer().equalsIgnoreCase("female")) {
                                gender = "female";
                                genderfrompref = "female";
                                binding.tvMale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                                binding.tvFemale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
                                binding.tvOthers.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));

                            } else {
                                gender = "other ";
                                genderfrompref = "other";
                                binding.tvMale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                                binding.tvFemale.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
                                binding.tvOthers.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
                            }
                        }
                    }
                }

                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.tvAgeNote.setVisibility(View.GONE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
                binding.llWeight.setVisibility(View.GONE);
                binding.llAge.setVisibility(View.GONE);
                binding.llHeight.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                binding.datePicker.setVisibility(View.GONE);
                checkforgenderstore();
                binding.llGender.setVisibility(View.VISIBLE);
                break;
            case 2:
                if (!comingFrom.equals("back")) {
                    if (!heartAgeQuestionsResponseList.get(answeredCount).getAnswer().equals("")) {
                        setFeetAnswer(heartAgeQuestionsResponseList.get(answeredCount).getAnswer().split("\\.")[0]);
                        setInchesAnswer(heartAgeQuestionsResponseList.get(answeredCount).getAnswer().split("\\.")[1]);

                    }
                }

                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llHeight.setVisibility(View.VISIBLE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_4));
                binding.llCenterBear.setVisibility(View.GONE);
                binding.llWeight.setVisibility(View.GONE);
                binding.datePicker.setVisibility(View.GONE);
                binding.llGender.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                break;

            case 3:
                if (!comingFrom.equals("back")) {
                    if (!SharedPref.getWeight().equals("")) {
                        binding.edtWeight.setText(SharedPref.getWeight());
                    }
                }

                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llHeight.setVisibility(View.GONE);
                binding.llWeight.setVisibility(View.VISIBLE);
                binding.edtWeight.setVisibility(View.VISIBLE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_2));
                binding.llCenterBear.setVisibility(View.GONE);
                binding.datePicker.setVisibility(View.GONE);
                binding.llGender.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                binding.edtWeight.setHint("Weight");
                break;
            case 4:

                binding.ivTopBear.setVisibility(View.GONE);
                binding.llWeight.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.edtWeight.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.VISIBLE);
                binding.btnOk.setVisibility(View.GONE);
                binding.llGender.setVisibility(View.GONE);
                refillYesNo();
                break;

            case 5:
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llWeight.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.edtWeight.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.VISIBLE);
                binding.btnOk.setVisibility(View.GONE);
                binding.llGender.setVisibility(View.GONE);
                refillYesNo();
                break;
            case 6:
                binding.ivTopBear.setVisibility(View.GONE);
                binding.llWeight.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.edtWeight.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.VISIBLE);
                binding.btnOk.setVisibility(View.GONE);
                binding.llSBP.setVisibility(View.GONE);
                binding.llGender.setVisibility(View.GONE);
                refillYesNo();
                break;
            case 7:
                if (!comingFrom.equals("back")) {
                    if (!heartAgeQuestionsResponseList.get(answeredCount).getAnswer().equals("")) {
                        binding.edtSBP.setText(heartAgeQuestionsResponseList.get(answeredCount).getAnswer());
                    }
                }

                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.ivTopBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_3));
                binding.llYesNo.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                binding.llSBP.setVisibility(View.VISIBLE);
                binding.llDBP.setVisibility(View.GONE);
                binding.llTotalCholesterol.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);

                break;
            case 8:
                if (!comingFrom.equals("back")) {
                    if (!heartAgeQuestionsResponseList.get(answeredCount).getAnswer().equals("")) {
                        binding.edtDBP.setText(heartAgeQuestionsResponseList.get(answeredCount).getAnswer());
                    }
                }

                binding.btnOk.setVisibility(View.VISIBLE);
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llSBP.setVisibility(View.GONE);
                binding.llDBP.setVisibility(View.VISIBLE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.llTotalCholesterol.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                break;
            case 9:

                binding.ivTopBear.setVisibility(View.GONE);
                binding.llWeight.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.edtWeight.setVisibility(View.GONE);
                binding.llYesNo.setVisibility(View.VISIBLE);
                binding.btnOk.setVisibility(View.GONE);
                binding.llSBP.setVisibility(View.GONE);
                binding.llDBP.setVisibility(View.GONE);
                binding.llGender.setVisibility(View.GONE);
                binding.llTotalCholesterol.setVisibility(View.GONE);
                refillYesNo();
                break;
            case 10:
                if (!comingFrom.equals("back")) {
                    if (!heartAgeQuestionsResponseList.get(answeredCount).getAnswer().equals("")) {
                        binding.edtTotalCholesterol.setText(heartAgeQuestionsResponseList.get(answeredCount).getAnswer());
                    }
                }

                binding.llYesNo.setVisibility(View.GONE);
                binding.llSBP.setVisibility(View.GONE);
                binding.llDBP.setVisibility(View.GONE);
                binding.llCenterBear.setVisibility(View.GONE);
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llTotalCholesterol.setVisibility(View.VISIBLE);
                binding.llHdlCholesterol.setVisibility(View.GONE);
                binding.btnOk.setVisibility(View.VISIBLE);
                break;

            case 11:
                if (!comingFrom.equals("back")) {
                    if (!heartAgeQuestionsResponseList.get(answeredCount).getAnswer().equals("")) {
                        binding.edtHdlCholesterol.setText(heartAgeQuestionsResponseList.get(answeredCount).getAnswer());
                    }
                }

                binding.llTotalCholesterol.setVisibility(View.GONE);
                binding.ivTopBear.setVisibility(View.VISIBLE);
                binding.llHdlCholesterol.setVisibility(View.VISIBLE);
                binding.btnOk.setVisibility(View.VISIBLE);
                break;
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

    private void refillYesNo() {
        if (!heartAgeQuestionsResponseList.get(answeredCount).getAnswer().isEmpty()) {
            if (heartAgeQuestionsResponseList.get(answeredCount).getAnswer().equalsIgnoreCase("yes")) {
                isPressedOnPrevious = true;
                binding.tvYes.performClick();
                binding.tvYes.setTag(false);
                binding.tvNo.setTag(true);
                binding.tvDoNotKnow.setTag(true);


                binding.ivMiddleBearDoNotKnow.setVisibility(View.GONE);
                binding.ivMiddleBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_yes));
            } else if (heartAgeQuestionsResponseList.get(answeredCount).getAnswer().equalsIgnoreCase("no")) {
                isPressedOnPrevious = true;
                binding.tvNo.performClick();
                binding.tvYes.setTag(true);
                binding.tvNo.setTag(false);
                binding.tvDoNotKnow.setTag(true);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.GONE);
                binding.ivMiddleBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_hra_bear_no));
            } else {
                binding.tvDoNotKnow.performClick();
                binding.tvYes.setTag(true);
                binding.tvNo.setTag(true);
                binding.tvDoNotKnow.setTag(false);
                binding.llCenterBear.setVisibility(View.VISIBLE);
                binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
                binding.ivMiddleBear.setVisibility(View.GONE);
            }
        } else {
            binding.llCenterBear.setVisibility(View.VISIBLE);
            binding.ivMiddleBearDoNotKnow.setVisibility(View.VISIBLE);
            binding.ivMiddleBear.setVisibility(View.GONE);
        }
    }

    private void initViewTags() {
        binding.tvMale.setTag(false);
        binding.tvFemale.setTag(false);
        binding.tvOthers.setTag(false);
        binding.tvYes.setTag(false);
        binding.tvNo.setTag(false);
    }

    private void setQuestion() {
        binding.tvQuestion.setText(heartAgeQuestionsResponseList.get(answeredCount).getQuestion());
    }

    private void setProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.hraProgress.setProgress(answeredCount, true);
        } else {
            binding.hraProgress.setProgress(answeredCount);
        }
    }

    private void resetYesNo() {
        if (heartAgeQuestionsResponseList.get(answeredCount).getAnswer().isEmpty()) {
            if ((boolean) binding.tvYes.getTag())
                optionClicked(binding.tvYes);
            if ((boolean) binding.tvNo.getTag())
                optionClickedRed(binding.tvNo);
        } else {
            if ((boolean) binding.tvYes.getTag())
                optionClicked(binding.tvYes);
            if ((boolean) binding.tvNo.getTag())
                optionClickedRed(binding.tvNo);
        }
    }

    private void optionClicked(TextView textView) {
        textView.setTag(!(boolean) textView.getTag());
        if ((boolean) textView.getTag()) {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_blue_border_rc_bg_8dp));
        } else {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_border_rc_bg_8dp));
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
}