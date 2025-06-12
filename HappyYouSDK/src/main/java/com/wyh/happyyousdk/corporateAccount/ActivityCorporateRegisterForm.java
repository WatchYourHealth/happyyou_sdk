package com.wyh.happyyousdk.corporateAccount;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.APIEncryption.APIInterface;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.APIEncryption.RetrofitHandler;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.common.adapter.SearchListAdapter;
import com.wyh.happyyousdk.common.adapter.SearchListAdapterCity;
import com.wyh.happyyousdk.crypto.RSAEncryption;
import com.wyh.happyyousdk.databinding.ActivityCorporateRegisterFormBinding;
import com.wyh.happyyousdk.model.request.GetCityRequestModel;
import com.wyh.happyyousdk.model.request.SaveCorporateDetailsReqV1;
import com.wyh.happyyousdk.model.response.GetCityListResponseModel;
import com.wyh.happyyousdk.model.response.GetEmailOTPResp;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.MultiLineRadioGroup;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyhsdk.sharedPreferences.SharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCorporateRegisterForm extends AppCompatActivity {
    ActivityCorporateRegisterFormBinding binding;
    ProgressDialog progressDialog;
    APIInterface apiInterfaceWyh;
    Context context;
    String jsonData = "";
    String CorporateID = "";
    String CorporateName = "";
    String EmailID = "";
    private List<FormModel> formList = new ArrayList<>();
    private List<String> stateList = new ArrayList<>();
    private List<StateModel>stateListWithId = new ArrayList<>();
    private List<GetCityListResponseModel.CityDataModel> cityListWithId = new ArrayList<>();
    ArrayList<String> cityList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner spinner;
    int viewLength;
    List<GetEmailOTPResp.Data.corporateDetails> corporateDetails = new ArrayList<>();
    List<String> corporateNameList = new ArrayList<>();
    String dobStr;
    Calendar mainStartCalender;
    String  mobilNumber="";
     String otpType="";
     boolean saveData=false;
    String userName="",Dob="",Gender="";
    Calendar prevYear ;
    boolean radiobuttonStatus=false;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_corporate_register_form);
        context = this;
        SharedPref.init(context);
        SharedPreference.init(context);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        apiInterfaceWyh = RetrofitHandler.apiInterface();
        jsonData=getIntent().getStringExtra("jsonData");
        CorporateID=getIntent().getStringExtra("CorporateID");
        CorporateName=getIntent().getStringExtra("CorporateName");
        EmailID=getIntent().getStringExtra("EmailID");
        String stateData = getIntent().getStringExtra("stateData");
        String corporateDetailsString = getIntent().getStringExtra("corporateDetails");
        mainStartCalender = Calendar.getInstance();
        prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -18);
        if (!SharedPref.getSpinWheelStatus()) {
            binding.rlSpinner1.setVisibility(View.GONE);
            binding.rlspinner2.setVisibility(View.VISIBLE);
        }
        else {
            binding.rlSpinner1.setVisibility(View.VISIBLE);
            binding.rlspinner2.setVisibility(View.GONE);
        }
        if(stateData != null && !stateData.isEmpty()){
            Type type1 = new TypeToken<List<StateModel>>() {
            }.getType();
            stateListWithId = new Gson().fromJson(stateData, type1);
        }
        if(corporateDetailsString != null && !corporateDetailsString.isEmpty()){
            Type type1 = new TypeToken<List<GetEmailOTPResp.Data.corporateDetails>>() {
            }.getType();
            corporateDetails = new Gson().fromJson(corporateDetailsString, type1);
            setSpinnerData(corporateDetails);
        }
        binding.rlUpperCard.setOnClickListener(view -> {
            onBackPressed();
        });


        for (int i=0;i<stateListWithId.size();i++)
        {
            stateList.add(stateListWithId.get(i).getStateName());
        }

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allQuestionAnswered = true;
                for (FormModel model : formList) {
                    model.captureAnswerList();
                    if (model.isIsrequired()&&!model.captureAnswerList() && allQuestionAnswered ) {
                        allQuestionAnswered = false;
                    }
                }
                List<FormModel> postFormDOB = formList.stream().filter(form-> form.getQuestionType().equalsIgnoreCase("dob")).collect(Collectors.toList());
                List<FormModel> postFormName = formList.stream().filter(form-> form.getQuestionType().equalsIgnoreCase("name")).collect(Collectors.toList());
                List<FormModel> postFormGender = formList.stream().filter(form-> form.getQuestionType().equalsIgnoreCase("gender")).collect(Collectors.toList());
                List<FormModel> postFormMobile = formList.stream().filter(form-> form.getQuestionType().equalsIgnoreCase("mobilenumber")).collect(Collectors.toList());
                List<FormModel> postFormPinCode = formList.stream().filter(form-> form.getQuestionType().equalsIgnoreCase("pincode")).collect(Collectors.toList());
                if(postFormDOB != null && postFormDOB.size()>0&&postFormDOB.get(0).getAnswerList().get(0)!=null){

                    if (!postFormDOB.get(0).getAnswerList().get(0).equalsIgnoreCase(SharedPref.getDOB())){
                        Dob=postFormDOB.get(0).getAnswerList().get(0);
                        saveData=true;
                    }
                }
                if(postFormName != null && postFormName.size()>0&&postFormName.get(0).getAnswerList().get(0)!=null){
                    postFormName.get(0).getAnswerList().get(0);
                    if (!postFormName.get(0).getAnswerList().get(0).equalsIgnoreCase(SharedPref.getUserName())){
                        userName=postFormName.get(0).getAnswerList().get(0);
                        saveData=true;
                    }
                }
                if(radiobuttonStatus&&postFormGender != null && postFormGender.size()>0&&postFormGender.get(0).getAnswerList().get(0)!=null){
                    postFormGender.get(0).getAnswerList().get(0);
                    if (!postFormGender.get(0).getAnswerList().get(0).equalsIgnoreCase(SharedPref.getGender())){
                        Gender=postFormGender.get(0).getAnswerList().get(0);
                        saveData=true;
                    }
                }


                if(postFormMobile != null && postFormMobile.size()>0&&postFormMobile.get(0).getAnswerList().get(0)!=null){
                    //postFormMobile.get(0).getAnswerList().get(0);
                    if (postFormMobile.get(0).getAnswerList().get(0).length()<10){
                        Toast.makeText(context, "Please enter valid Mobile", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(postFormPinCode != null && postFormPinCode.size()>0&&postFormPinCode.get(0).getAnswerList().get(0)!=null){
                   // postFormPinCode.get(0).getAnswerList().get(0);
                    if (postFormPinCode.get(0).getAnswerList().get(0).length()<6){
                        Toast.makeText(context, "Please enter valid Pincode", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation() // Exclude fields without @Expose
                        .setPrettyPrinting() // Optional: Makes the JSON output readable
                        .create();

                String jsonString = gson.toJson(formList);
                if (!allQuestionAnswered) {
                    Toast.makeText(context, "Please fill or select all mandatory the details to continue", Toast.LENGTH_SHORT).show();
                } else {
                    SaveFormData( jsonString);
                    APILogs.INSTANCE.activityTracker("Android_PRE_CORPORATE_SUBMIT_CORPORATE_DETAIL",context);
                }
            }
        });
        binding.spinnerSpinWheel.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                CorporateID= String.valueOf(corporateDetails.get(position).getCorpId());
                CorporateName= String.valueOf(corporateDetails.get(position).getCorporateNames());
                jsonData="";
                jsonData= corporateDetails.get(position).getQuestionJson();
                SetFormData();

            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });
        binding.spinnerNonSpinWheel.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                CorporateID= String.valueOf(corporateDetails.get(position).getCorpId());
                CorporateName= String.valueOf(corporateDetails.get(position).getCorporateNames());
                jsonData="";
                jsonData= corporateDetails.get(position).getQuestionJson();
                SetFormData();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle case if needed
            }
        });
    }
    private void SaveFormData(String jsonString) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();

        //SaveCorporateDetailsReq request = new SaveCorporateDetailsReq(RSAEncryption.rsaEncrypt(EmailID),jsonString);
        if (!mobilNumber.equalsIgnoreCase(SharedPref.getDecryptMobileNo()))
        {
            otpType="both";
        }
        else {
            otpType="email";
        }
        SaveCorporateDetailsReqV1 request = new SaveCorporateDetailsReqV1(RSAEncryption.rsaEncrypt(EmailID),RSAEncryption.rsaEncrypt(mobilNumber),otpType,true,jsonString);
        Call<GetEmailOTPResp> call = apiInterfaceWyh.SaveCorporateDetailsV1(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetEmailOTPResp> call, @NonNull Response<GetEmailOTPResp> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Intent intent = new Intent(context, ActivityCorporateOTPVerification.class);
                    intent.putExtra("CorporateID",CorporateID);
                    intent.putExtra("EmailID",EmailID);
                    intent.putExtra("jsonString",jsonString);
                    intent.putExtra("mobilNumber",mobilNumber);
                    intent.putExtra("saveData",saveData);
                    intent.putExtra("userName", userName);
                    intent.putExtra("Dob", Dob);
                    intent.putExtra("Gender", Gender);
                    startActivity(intent);
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetEmailOTPResp> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getCityListView(int stateId,TextView view) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        GetCityRequestModel request;
        request = new GetCityRequestModel(stateId);
        Call<GetCityListResponseModel> call = apiInterfaceWyh.getCityList(SharedPref.getAuthToken(), request);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<GetCityListResponseModel> call, @NonNull Response<GetCityListResponseModel> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess() && !(response.body().getData().isEmpty())) {
                    cityListWithId.clear();
                    cityList.clear();
                    cityListWithId = response.body().getData();
                    for (int i = 0; i < cityListWithId.size(); i++) {
                        if(i == 0) {
                            view.setText(cityListWithId.get(i).getCityName());
                        }
                        cityList.add(cityListWithId.get(i).getCityName());
                    }
                    adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, cityList);
                    // spinner.setAdapter(adapter);
                    //   setSpinnerData(corporateDetails);

                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No Corporate Account found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCityListResponseModel> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.search_policy_failed));
                Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setSpinnerData(List<GetEmailOTPResp.Data.corporateDetails> corporateDetails)
    {
        for (int i=0;i<corporateDetails.size();i++)
        {
            corporateNameList.add(corporateDetails.get(i).getCorporateNames());
        }
        if (corporateNameList.size()>1)
        {
            binding.imSpinnerNonSpinWheel.setVisibility(View.VISIBLE);
            binding.imSpinnerSpinWheel.setVisibility(View.VISIBLE);
            binding.rlSpinner1.setClickable(true);
            binding.rlspinner2.setClickable(true);
        }
        else {
            binding.imSpinnerNonSpinWheel.setVisibility(View.INVISIBLE);
            binding.imSpinnerSpinWheel.setVisibility(View.INVISIBLE);
            binding.rlSpinner1.setClickable(false);
            binding.rlspinner2.setClickable(false);
        }
        ArrayAdapter<String> spinnerSpinWheelAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, corporateNameList);
        ArrayAdapter<String> spinnerNonSpinWheelAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, corporateNameList);
        spinnerSpinWheelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNonSpinWheelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSpinWheel.setAdapter(spinnerSpinWheelAdapter);
        binding.spinnerNonSpinWheel.setAdapter(spinnerNonSpinWheelAdapter);
    }
    @SuppressLint("RestrictedApi")
    public void SetFormData()
    {
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout layout = findViewById(R.id.formContainer);
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            if(layout.getChildCount() > 0) {
                layout.removeAllViews();
            }
            formList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                FormModel model = new FormModel(
                        obj.getInt("questionId"),
                        obj.getString("question"),
                        obj.getString("viewType"),
                        obj.getString("viewLength"),
                        obj.getBoolean("isrequired"),
                        obj.getString("keypadtype"),
                        obj.getString("questionType"),
                        obj.has("options") ? obj.getJSONArray("options") : null
                );
                formList.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (FormModel model : formList) {
            View itemView;
            switch (model.getViewType()) {
                case "EditText":
                    itemView = inflater.inflate(R.layout.item_edittext_corporate, null);
                    TextView textLabel = itemView.findViewById(R.id.textLabel);
                    TextView textCount = itemView.findViewById(R.id.textCount);
                    TextView tvRequired = itemView.findViewById(R.id.tvRequired);
                    if (model.isIsrequired())
                    {
                        tvRequired.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvRequired.setVisibility(View.GONE);
                    }
                    EditText editText = itemView.findViewById(R.id.editText);
                    textLabel.setText((model.getQuestion()));

                    if (model.getQuestionType().equalsIgnoreCase("name"))
                    {
                        editText.setText(SharedPref.getUserName());
                    }


                    switch (model.getKeypadtype().toLowerCase()) {
                        case "number":
                            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                            break;

                        case "dob":
                            // Date input type — generally used with a DatePicker
                            editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                            break;

                        case "alphanumeric":
                            // Allows both letters and numbers
                            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            break;

                        case "alpha":
                            // Allows only letters
                            editText.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;

                        default:
                            editText.setInputType(InputType.TYPE_CLASS_TEXT);
                            break;
                    }
                    layout.addView(itemView);
                    model.setView(editText);
                    final int[] viewLength = new int[1];
                    if (model.getViewLength() != null && !model.getViewLength().isEmpty()) {
                        int len = 100;
                        viewLength[0] = len;
                        try {
                            len = Integer.parseInt(model.getViewLength());

                        } catch (Exception ex) {

                        }
                        viewLength[0]= len;
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(len);
                        editText.setFilters(filterArray);
                        textCount.setText("0/" + viewLength[0]);
                    } else {
                        textCount.setVisibility(View.GONE);
                    }
                    if (model.getQuestionType().equalsIgnoreCase("mobilenumber"))
                    {
                        editText.setText(SharedPref.getDecryptMobileNo());
                        mobilNumber= editText.getText().toString();
                        textCount.setText(mobilNumber.length()+"/" + viewLength[0]);
                    }
                    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            Log.d("AuthToken", String.valueOf(hasFocus));
                            //userScrolling[0] = false;
                        }
                    });
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (textCount.getVisibility() == View.VISIBLE) {
                                if(charSequence != null) {
                                    textCount.setText((charSequence.length()) + "/" +  viewLength[0]);
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            if (editable.length() >=  viewLength[0]) {
                                textCount.setText( viewLength[0] + "/" +  viewLength[0]);
                                //Toast.makeText(context, "Only " + viewLength + " characters are allowed.", Toast.LENGTH_SHORT).show();
                                // Trim extra characters
                                editable.delete( viewLength[0], editable.length());

                            }
                            if (model.getQuestionType().equalsIgnoreCase("mobilenumber")) {
                                mobilNumber = editText.getText().toString();
                            }
                        }
                    });
                    layout.setOnTouchListener((v, event) -> {
                        hideKeyboard(v);
                        return false;
                    });
                    break;
                case "Spinner":
                    if (model.getQuestionType().equals("state") || model.getQuestionType().equals("city")) {
                        itemView = inflater.inflate(R.layout.item_edittext_corporate, null);
                        TextView spinText = itemView.findViewById(R.id.textLabel);
                        EditText custom_spinner = itemView.findViewById(R.id.editText);
                        TextView textCountSp = itemView.findViewById(R.id.textCount);
                        TextView tvRequiredSp = itemView.findViewById(R.id.tvRequired);
                        Drawable drawableEnd = ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_down);
                        custom_spinner.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableEnd, null);
                        if (model.isIsrequired())
                        {
                            tvRequiredSp.setVisibility(View.VISIBLE);
                        }
                        else {
                            tvRequiredSp.setVisibility(View.GONE);
                        }
                        textCountSp.setVisibility(View.GONE);
                        custom_spinner.setFocusable(false);
                        custom_spinner.setClickable(true);
                        spinText.setText((model.getQuestion()));
                        if (model.getQuestionType().equals("state")) {
                            adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, stateList);
                            custom_spinner.setText(stateList.get(0));
                        } else {
                            adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, model.getOptions());
                            // custom_spinner.setText(model.getOptions().get(0));
                        }
                        layout.addView(itemView);
                        model.setSupportingView(itemView);
                        model.setView(spinText);
                        model.setView(custom_spinner);
                        for (int j = 0; j < stateListWithId.size(); j++) {
                            if (Objects.equals(stateList.get(j), stateListWithId.get(0).getStateName())) {
                                for (FormModel user : formList) {
                                    if ("city".equals(user.getQuestionType())) {
                                        EditText tvCity = getCityView();
                                        if(tvCity != null) {
                                            getCityListView(stateListWithId.get(0).getStateId(),tvCity);
                                        }
                                        //custom_spinner.setText(cityList.get(0));
                                        break;
                                    }
                                }

                            }
                        }
                        custom_spinner.setOnClickListener(view -> {
                            if ("state".equals(model.getQuestion())) {
                                showSearchableDialog(context, stateListWithId, new OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(String selectedItem, int position) {
                                        if (!custom_spinner.getText().toString().equalsIgnoreCase(selectedItem)) {
                                            EditText tvCity = getCityView();
                                            if(tvCity != null){
                                                getCityListView(stateListWithId.get(position).getStateId(),tvCity);
                                            }
                                        }
                                        custom_spinner.setText(selectedItem);
                                    }
                                });
                            } else {
                                showSearchableDialogCity(context, cityListWithId, new OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(String selectedItem, int position) {
                                        //getCityList(cityListWithId.get(position).getCityId());
                                        custom_spinner.setText(selectedItem);

                                    }
                                });
                            }
                        });

                    } else {
                        itemView = inflater.inflate(R.layout.item_spinner, null);
                        TextView spinText = itemView.findViewById(R.id.textLabel);
                        spinText.setText((model.getQuestion()));
                        TextView tvRequiredsp = itemView.findViewById(R.id.tvRequired);
                        if (model.isIsrequired())
                        {
                            tvRequiredsp.setVisibility(View.VISIBLE);
                        }
                        else {
                            tvRequiredsp.setVisibility(View.GONE);
                        }
                        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, model.getOptions());
                        spinner = itemView.findViewById(R.id.spinner);
                        spinner.setAdapter(adapter);
                        layout.addView(itemView);
                        model.setView(spinner);
                        spinner.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                hideKeyboard(v);
                                return false;
                            }
                        });

                    }


                    break;
                case "RadioButton":
                    itemView = inflater.inflate(R.layout.item_radiogroup, null);
                    CommonUtils.hideKeyboard((Activity) context);
                    TextView textLabelRadio = itemView.findViewById(R.id.textLabel);
                    TextView tvRequiredRb = itemView.findViewById(R.id.tvRequired);
                    if (model.isIsrequired())
                    {
                        tvRequiredRb.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvRequiredRb.setVisibility(View.GONE);
                    }
                    textLabelRadio.setText(model.getQuestion());
                    MultiLineRadioGroup flowRadioGroup = itemView.findViewById(R.id.radioGroup);
                   // List<RadioButton> radioButtons = new ArrayList<>();
                    for (String option : model.getOptions()) {
                        RadioButton radioButton = new RadioButton(context);
                        radioButton.setText(option);
                       // radioButtons.add(radioButton);
                        flowRadioGroup.addButtons(radioButton);

                        if (model.getQuestionType().equals("gender") &&SharedPref.getGender()!=null&& !SharedPref.getGender().isEmpty()&&option.equalsIgnoreCase(SharedPref.getGender().equalsIgnoreCase("Male")?"Male":"Female")) {
                            radioButton.setChecked(true);
                            radiobuttonStatus=true;
                        }
                        else {
                            radiobuttonStatus=false;
                        }


                        radioButton.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                hideKeyboard(v);
                                return false;
                            }
                        });
                    }
                    layout.addView(itemView);
                    model.setView(flowRadioGroup);
                    String value="";
                    if (!SharedPref.getGender().isEmpty()){
                        value = SharedPref.getGender().equalsIgnoreCase("Male") ? "Male" : "Female";
                    }
                    RadioButton button = flowRadioGroup.containsButtonGet(value);
                    if(button != null){
                        flowRadioGroup.checkName(button.getText().toString());
                    }
                    itemView.setOnTouchListener((v, event) -> {
                        hideKeyboard(v);
                        return false;
                    });
                    break;
                case "CheckBox":
                    itemView = inflater.inflate(R.layout.item_checkbox_group, null);
                    TextView textLabelCheckBox = itemView.findViewById(R.id.textLabel);
                    textLabelCheckBox.setText(model.getQuestion());
                    TextView tvRequiredCB = itemView.findViewById(R.id.tvRequired);
                    if (model.isIsrequired())
                    {
                        tvRequiredCB.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvRequiredCB.setVisibility(View.GONE);
                    }
                    FlexboxLayout checkBoxGroup = itemView.findViewById(R.id.checkBoxContainer); // Using FlowLayout for better wrapping
                    List<CheckBox> checkBoxes = new ArrayList<>();
                    for (String option : model.getOptions()) {
                        CheckBox checkBox = new CheckBox(context);
                        checkBox.setText(option);
                        checkBoxes.add(checkBox);
                        checkBox.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                hideKeyboard(v);
                                return false;
                            }
                        });
                        checkBoxGroup.addView(checkBox);
                    }
                    layout.addView(itemView);
                    model.setView(checkBoxes);

                    itemView.setOnTouchListener((v, event) -> {
                        hideKeyboard(v);
                        return false;
                    });
                    break;
                case "Switch":
                    itemView = inflater.inflate(R.layout.item_switch, null);
                    TextView textLabelSwitch = itemView.findViewById(R.id.textLabel);
                    textLabelSwitch.setText(model.getQuestion());
                    Switch switchToggle = itemView.findViewById(R.id.switchToggle);
                    TextView tvRequiredSW = itemView.findViewById(R.id.tvRequired);
                    if (model.isIsrequired())
                    {
                        tvRequiredSW.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvRequiredSW.setVisibility(View.GONE);
                    }
                    switchToggle.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            hideKeyboard(v);
                            return false;
                        }
                    });
                    layout.addView(itemView);
                    model.setView(switchToggle);
                    break;
                case "DatePicker":
                    itemView = inflater.inflate(R.layout.item_datepicker, null);
                    TextView textLabelDatePicker = itemView.findViewById(R.id.textLabel);
                    textLabelDatePicker.setText(model.getQuestion());
                    DatePicker datePicker = itemView.findViewById(R.id.datePicker);
                    TextView tvRequiredDP = itemView.findViewById(R.id.tvRequired);
                    if (model.isIsrequired())
                    {
                        tvRequiredDP.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvRequiredDP.setVisibility(View.GONE);
                    }
                    datePicker.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            hideKeyboard(v);
                            return false;
                        }
                    });
                    layout.addView(itemView);
                    model.setView(datePicker);
                    break;
                case "calendar":
                    itemView = inflater.inflate(R.layout.item_edittext_corporate, null);
                    TextView textLabelCl = itemView.findViewById(R.id.textLabel);
                    TextView textCountCl = itemView.findViewById(R.id.textCount);
                    TextView tvRequiredCL = itemView.findViewById(R.id.tvRequired);
                    if (model.isIsrequired())
                    {
                        tvRequiredCL.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvRequiredCL.setVisibility(View.GONE);
                    }
                    textCountCl.setVisibility(View.GONE);
                    EditText editTextCl = itemView.findViewById(R.id.editText);
                    editTextCl.setFocusable(false);
                    editTextCl.setClickable(true);
                    textLabelCl.setText((model.getQuestion()));
                    if (model.getQuestionType().equalsIgnoreCase("dob")&&!SharedPref.getDOB().equalsIgnoreCase("")) {
                        String input = SharedPref.getDOB();
                        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                        try {
                            Date date = inputFormat.parse(input);
                            String formattedDate = outputFormat.format(date);
                            System.out.println(formattedDate);
                            editTextCl.setText(formattedDate);
                        } catch (Exception e) {
                            e.printStackTrace();
                            editTextCl.setText(SharedPref.getDOB());
                        }

                    }
                    editTextCl.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            hideKeyboard(v);
                            return false;
                        }
                    });
                    editTextCl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int year = mainStartCalender.get(Calendar.YEAR);
                            int month = mainStartCalender.get(Calendar.MONTH);
                            int day = mainStartCalender.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog pickerDialog = new DatePickerDialog(ActivityCorporateRegisterForm.this,
                                    (view1, year1, monthOfYear, dayOfMonth) -> {
                                        String newDay;
                                        if (String.valueOf(dayOfMonth).length() == 1) {
                                            newDay = "0" + dayOfMonth;
                                        } else {
                                            newDay = String.valueOf(dayOfMonth);
                                        }
                                        String newMonth;
                                        if (String.valueOf((monthOfYear + 1)).length() == 1) {
                                            newMonth = "0" + (monthOfYear + 1);
                                        } else {
                                            newMonth = String.valueOf((monthOfYear + 1));
                                        }
                                        editTextCl.setText(year1 + "-" + newMonth + "-" + newDay);
                                        dobStr = year1 + "-" + newMonth + "-" + newDay;
                                        mainStartCalender = Calendar.getInstance();
                                        mainStartCalender.set(Calendar.YEAR, year1);
                                        mainStartCalender.set(Calendar.MONTH, monthOfYear);
                                        mainStartCalender.set(Calendar.DATE, dayOfMonth);
                                    }, year, month, day);
                             pickerDialog.getDatePicker().setMaxDate(prevYear.getTimeInMillis());
                            pickerDialog.show();
                        }
                    });
                    layout.addView(itemView);
                    model.setView(editTextCl);
                    break;
            }


        }
    }
    public void showSearchableDialog(Context context, List<StateModel> itemList, OnItemSelectedListener listener) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.spinner_search_view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        EditText searchInput = dialog.findViewById(R.id.search_input);
        searchInput.setHint("Select State");
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        SearchListAdapter adapter = new SearchListAdapter(itemList, (item, position) -> {
            listener.onItemSelected(item, position);
            dialog.dismiss();
        });

        recyclerView.setAdapter(adapter);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
        dialog.show();
    }

    public void showSearchableDialogCity(Context context, List<GetCityListResponseModel.CityDataModel> itemList, OnItemSelectedListener listener) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.spinner_search_view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        EditText searchInput = dialog.findViewById(R.id.search_input);
        searchInput.setHint("Select City");
        RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        SearchListAdapterCity adapter = new SearchListAdapterCity(itemList, (item, position) -> {
            listener.onItemSelected(item, position);
            dialog.dismiss();
        });

        recyclerView.setAdapter(adapter);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
        dialog.show();
    }
    private EditText getCityView(){
        List<FormModel> streamModel = formList.stream().filter(cityview -> cityview.getQuestionType().equalsIgnoreCase("city")).collect(Collectors.toList());
        if (streamModel != null && streamModel.size() > 0) {
            Object objectGroup = streamModel.get(0).getSupportingView();
            if (objectGroup instanceof ViewGroup) {
                try {
                    ViewGroup vGroup = (ViewGroup) objectGroup;
                    EditText custom_spinner = vGroup.findViewById(R.id.editText);
                    //custom_spinner.setText("Select City");
                    return custom_spinner;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
        return null;
    }
}
class FormModel {
    @SerializedName("questionId")
    @Expose
    private int questionId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("viewType")
    @Expose
    private String viewType;
    @SerializedName("options")
    @Expose
    private List<String> options;
    private Object view;
    private String answer;

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    @SerializedName("answer")
    @Expose
    private List<String> answerList = new ArrayList<>();
    @SerializedName("isrequired")
    @Expose
    boolean isrequired;

    public boolean isIsrequired() {
        return isrequired;
    }

    public void setIsrequired(boolean isrequired) {
        this.isrequired = isrequired;
    }

    public String getKeypadtype() {
        return keypadtype;
    }

    public void setKeypadtype(String keypadtype) {
        this.keypadtype = keypadtype;
    }

    @SerializedName("keypadtype")
    @Expose
    String keypadtype;

    public String getViewLength() {
        return viewLength;
    }

    public void setViewLength(String viewLength) {
        this.viewLength = viewLength;
    }

    @SerializedName("viewLength")
    @Expose
    private String viewLength;

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    @SerializedName("questionType")
    @Expose
    String questionType;
    public Object getSupportingView() {
        return supportingView;
    }

    public void setSupportingView(Object supportingView) {
        this.supportingView = supportingView;
    }
    private Object supportingView;
    public FormModel(int questionId, String question, String viewType, String viewLength,boolean isrequired,String keypadtype,String  questionType, JSONArray optionsJson) {
        this.questionId = questionId;
        this.question = question;
        this.viewType = viewType;
        this.options = new ArrayList<>();
        this.viewLength = viewLength;
        this.isrequired = isrequired;
        this.keypadtype = keypadtype;
        this.questionType = questionType;
        if (optionsJson != null) {
            for (int i = 0; i < optionsJson.length(); i++) {
                this.options.add(optionsJson.optString(i));
            }
        }
    }

    public String getQuestion() {
        return question;
    }

    public String getViewType() {
        return viewType;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setView(Object view) {
        this.view = view;
    }

    public void captureAnswer() {
        if (view instanceof EditText) {
            answer = ((EditText) view).getText().toString();
            ((EditText) view).setText(answer);
        } else if (view instanceof Spinner) {
            answer = ((Spinner) view).getSelectedItem().toString();
        } else if (view instanceof MultiLineRadioGroup) {
            MultiLineRadioGroup group = (MultiLineRadioGroup) view;
            if (group.getCheckedRadioButtonText() != null) {
                answer = group.getCheckedRadioButtonText().toString();
            }
        } else if (view instanceof ArrayList) {

            List<String> selectedOptions = new ArrayList<>();
            ArrayList<View> viewArrayList = (ArrayList<View>) view;
            for (int i = 0; i < viewArrayList.size(); i++) {
                View child = (View) viewArrayList.get(i);
                if (child instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) child;
                    if (checkBox.isChecked()) {
                        selectedOptions.add(checkBox.getText().toString());
                    }
                }
            }
            answer = String.join(", ", selectedOptions);
        } else if (view instanceof Switch) {
            answer = ((Switch) view).isChecked() ? "On" : "Off";
        } else if (view instanceof DatePicker) {
            DatePicker datePicker = (DatePicker) view;
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; // Months are 0-based
            int year = datePicker.getYear();
            answer = day + "/" + month + "/" + year;
        }
    }

    public boolean captureAnswerList() {
        answerList = new ArrayList<>();
        if (view instanceof EditText) {
            answer = ((EditText) view).getText().toString();
            answerList.add(answer);
        } else if (view instanceof Spinner) {
            answer = ((Spinner) view).getSelectedItem().toString();
            answerList.add(answer);
        } else if (view instanceof MultiLineRadioGroup) {
            MultiLineRadioGroup group = (MultiLineRadioGroup) view;
            if (group.getCheckedRadioButtonText() != null) {
                answer = group.getCheckedRadioButtonText().toString();
                answerList.add(answer);
            }
        } else if (view instanceof ArrayList) {

            List<String> selectedOptions = new ArrayList<>();
            ArrayList<View> viewArrayList = (ArrayList<View>) view;
            for (int i = 0; i < viewArrayList.size(); i++) {
                View child = (View) viewArrayList.get(i);
                if (child instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) child;
                    if (checkBox.isChecked()) {
                        selectedOptions.add(checkBox.getText().toString());
                    }
                }
            }
            answer = String.join(", ", selectedOptions);
            answerList = selectedOptions;
        } else if (view instanceof Switch) {
            answer = ((Switch) view).isChecked() ? "true" : "false";
            answerList.add(answer);
        } else if (view instanceof DatePicker) {
            DatePicker datePicker = (DatePicker) view;
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; // Months are 0-based
            int year = datePicker.getYear();
            answer = day + "/" + month + "/" + year;
            answerList.add(answer);
        }

        return answer != null && !answer.isEmpty();
    }

}
