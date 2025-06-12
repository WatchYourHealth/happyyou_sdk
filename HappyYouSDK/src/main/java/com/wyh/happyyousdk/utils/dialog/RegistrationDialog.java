package com.wyh.happyyousdk.utils.dialog;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.model.RedirectionModel;
import com.wyh.happyyousdk.play_and_win.PlayAndWinActivity;
import com.wyh.happyyousdk.quizathon.QuizathonScoreActivity;
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.MultiLineRadioGroup;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDialog extends Dialog {
    private Context context;
    private List<FormModel> formList = new ArrayList<>();
    RedirectionModel redirectionModel;
    String jsonData = "";
    String dialogTitle = "";
    String registrationId = "";
    String from = "";
    Intent intent;
    Boolean isQuizStarted = false;
    String iconUrl;
    String quizathonTitle = "";
    ImageView icon;
    Boolean isRegistered;
    int viewLength;
    //final boolean[] userScrolling = {false};

    public String getIconUrl() {
        return iconUrl;
    }

    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;

    }

    @Override
    public boolean isShowing() {

        return super.isShowing();

    }

    public RegistrationDialog(@NonNull Context context, String registrationId, String dialogTitle, RedirectionModel redirectionModel, String jsonData) {
        super(context);
        this.context = context;
        this.redirectionModel = redirectionModel;
        this.jsonData = jsonData;
        this.dialogTitle = dialogTitle;
        this.registrationId = registrationId;
        from = "Dashboard";

    }

    public RegistrationDialog(@NonNull Context context, String registrationId, String dialogTitle, RedirectionModel redirectionModel, String jsonData, boolean isRegistered) {
        super(context);
        this.context = context;
        this.redirectionModel = redirectionModel;
        this.jsonData = jsonData;
        this.dialogTitle = dialogTitle;
        this.registrationId = registrationId;
        this.isRegistered = isRegistered;
        from = "Dashboard";

    }


    public RegistrationDialog(@NonNull Context context, String jsonData, String dialogTitle, String registrationId, String quizathonTitle) {
        super(context);
        this.context = context;
        this.jsonData = jsonData;
        this.dialogTitle = dialogTitle;
        this.registrationId = registrationId;
        this.quizathonTitle = quizathonTitle;
        from = "Quiz";
        APILogs.INSTANCE.activityTracker("A_PlayAndWin_Quizathon_RegistrationPopUp", context);
    }

    public RegistrationDialog(@NonNull Context context, String jsonData, String dialogTitle, String registrationId, Intent intent, boolean isStarted, String quizathonTitle) {
        super(context);
        this.context = context;
        this.jsonData = jsonData;
        this.dialogTitle = dialogTitle;
        this.registrationId = registrationId;
        this.intent = intent;
        this.isQuizStarted = isStarted;
        this.quizathonTitle = quizathonTitle;
        from = "Quiz";
        APILogs.INSTANCE.activityTracker("A_PlayAndWin_Quizathon_RegistrationPopUp", context);
    }

    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_registration);
        LayoutInflater inflater = getLayoutInflater();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);


        ScrollView scvRegScroll = findViewById(R.id.scvRegScroll);


//        scvRegScroll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d("userScrolling Reg 1", "" + userScrolling[0]);
//                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
//                    userScrolling[0] = true;
//                    Log.d("userScrolling Reg 2", "" + userScrolling[0]);
//                }
//                return false; // allow scrolling to continue
//            }
//        });
//
//        scvRegScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("userScrolling Reg 3", "" + userScrolling[0]);
//                        if (userScrolling[0]) {
//                            hideKeyboard(scvRegScroll);
//                            userScrolling[0] = false;
//                            Log.d("userScrolling Reg 4", "" + userScrolling[0]);
//                        }
//                    }
//                }, 100);
//
//            }
//        });


        LinearLayout layout = findViewById(R.id.formContainer);
        icon = findViewById(R.id.ivRegStore);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnCancel = findViewById(R.id.btnCancel);
        TextView tvdialogTitle = findViewById(R.id.tvdialogTitle);
        TextView tvQuizTitle = findViewById(R.id.tvQuizTitle);
        if (dialogTitle != null && !dialogTitle.isEmpty()) {
            tvdialogTitle.setText(dialogTitle);
        } else {
            tvdialogTitle.setText("Registration");
        }
        // Sample JSON Data

        if (quizathonTitle != null && !quizathonTitle.isEmpty()) {
            tvQuizTitle.setVisibility(View.VISIBLE);
            tvQuizTitle.setText(quizathonTitle);
        } else {
            tvQuizTitle.setVisibility(View.GONE);
        }

        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                FormModel model = new FormModel(
                        obj.getInt("questionId"),
                        obj.getString("question"),
                        obj.getString("viewType"),
                        obj.getString("viewLength"),
                        obj.has("options") ? obj.getJSONArray("options") : null
                );
                formList.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//         onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Rect rect = new Rect();
//                // Get the current focus view (if any)
//                View currentFocus = getCurrentFocus();
//                if (currentFocus != null) {
//                    currentFocus.getGlobalVisibleRect(rect);
//                    // Check if the EditText is still focused or if the keyboard is showing
//                    if (!rect.contains(0, 0)) {
//                        // If the EditText is out of focus (or the user taps somewhere else)
//                        hideKeyboard(currentFocus);
//                    }
//                }
//            }
//        };

        // Dynamically inflate custom layouts for each input type
        for (FormModel model : formList) {
            View itemView;
            switch (model.getViewType()) {
                case "EditText":
                    itemView = inflater.inflate(R.layout.item_edittext, null);
                    TextView textLabel = itemView.findViewById(R.id.textLabel);
                    TextView textCount = itemView.findViewById(R.id.textCount);
                    EditText editText = itemView.findViewById(R.id.editText);
                    textLabel.setText((model.getQuestion()));
                    layout.addView(itemView);
                    model.setView(editText);
                    if (model.getViewLength() != null && !model.getViewLength().isEmpty()) {
                        int len = 100;
                        viewLength = len;
                        try {
                            len = Integer.parseInt(model.getViewLength());

                        } catch (Exception ex) {

                        }
                        viewLength = len;
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(len);
                        editText.setFilters(filterArray);
                        textCount.setText("0/" + viewLength);
                    } else {
                        textCount.setVisibility(View.GONE);
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
                                    textCount.setText((charSequence.length()) + "/" + viewLength);
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            Log.e("viewLength", viewLength + "");
                            if (editable.length() >= viewLength) {
                                textCount.setText(viewLength + "/" + viewLength);
                                //Toast.makeText(context, "Only " + viewLength + " characters are allowed.", Toast.LENGTH_SHORT).show();
                                // Trim extra characters
                                editable.delete(viewLength, editable.length());
                            }
                        }
                    });
                    layout.setOnTouchListener((v, event) -> {
                        hideKeyboard(v);
                        return false;
                    });
                    break;
                case "Spinner":
                    itemView = inflater.inflate(R.layout.item_spinner, null);
                    TextView spinText = itemView.findViewById(R.id.textLabel);
                    spinText.setText((model.getQuestion()));
                    Spinner spinner = itemView.findViewById(R.id.spinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, model.getOptions());
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

                    break;
                case "RadioButton":
                    itemView = inflater.inflate(R.layout.item_radiogroup, null);
                    CommonUtils.hideKeyboard((Activity) context);
                    TextView textLabelRadio = itemView.findViewById(R.id.textLabel);
                    textLabelRadio.setText(model.getQuestion());

                    MultiLineRadioGroup flowRadioGroup = itemView.findViewById(R.id.radioGroup);
                    for (String option : model.getOptions()) {
                        RadioButton radioButton = new RadioButton(context);
                        radioButton.setText(option);
                        flowRadioGroup.addButtons(radioButton);

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
                    itemView.setOnTouchListener((v, event) -> {
                        hideKeyboard(v);
                        return false;
                    });
                    break;
                case "CheckBox":
                    itemView = inflater.inflate(R.layout.item_checkbox_group, null);
                    TextView textLabelCheckBox = itemView.findViewById(R.id.textLabel);
                    textLabelCheckBox.setText(model.getQuestion());
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
            }

            if (iconUrl != null && !iconUrl.isEmpty()) {
                icon.setVisibility(View.VISIBLE);
                Glide.with(context).load(iconUrl).into(icon);
            } else {
                icon.setVisibility(View.GONE);
            }
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allQuestionAnswered = true;
                for (FormModel model : formList) {
                    if (!model.captureAnswerList() && allQuestionAnswered) {
                        allQuestionAnswered = false;
                    }
                }
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation() // Exclude fields without @Expose
                        .setPrettyPrinting() // Optional: Makes the JSON output readable
                        .create();

                String jsonString = gson.toJson(formList);
                if (!allQuestionAnswered) {
                    Toast.makeText(context, "Please fill all the details to continue", Toast.LENGTH_SHORT).show();
                } else {
                    if (context instanceof NewDashboardActivity && from.equalsIgnoreCase("Dashboard")) {
                        ((NewDashboardActivity) context).SaveBannerRegistration(registrationId, jsonString, redirectionModel,isRegistered);
                        dismiss();
                    } else if (context instanceof NewDashboardActivity && from.equalsIgnoreCase("Quiz")) {
                        ((NewDashboardActivity) context).ApiSaveQuizRegistration(registrationId, jsonString, intent, isQuizStarted);
                        APILogs.INSTANCE.activityTracker("A_PlayAndWin_Quizathon_RegistrationPopUp_Submit", context);
                        dismiss();
                    } else if (context instanceof PlayAndWinActivity) {
                        ((PlayAndWinActivity) context).SaveQuizRegistration(registrationId, jsonString, intent, isQuizStarted);
                        APILogs.INSTANCE.activityTracker("A_PlayAndWin_Quizathon_RegistrationPopUp_Submit", context);
                        dismiss();
                    } else if (context instanceof QuizathonViewAllActivity) {
                        ((QuizathonViewAllActivity) context).SaveQuizRegistration(registrationId, jsonString, intent, isQuizStarted);
                        APILogs.INSTANCE.activityTracker("A_PlayAndWin_Quizathon_RegistrationPopUp_Submit", context);
                        dismiss();
                    }else if (context instanceof QuizathonScoreActivity) {
                        ((QuizathonScoreActivity) context).SaveQuizRegistration(registrationId, jsonString, intent, isQuizStarted);
                        APILogs.INSTANCE.activityTracker("A_PlayAndWin_Quizathon_RegistrationPopUp_Submit", context);
                        dismiss();
                    }
                    else {
                        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from != null && from.equalsIgnoreCase("Quiz")) {
                    APILogs.INSTANCE.activityTracker("A_PlayAndWin_Quizathon_RegistrationPopUp_Close", context);
                }
                dismiss();
            }
        });
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
    @SerializedName("answer")
    @Expose
    private List<String> answerList = new ArrayList<>();

    public String getViewLength() {
        return viewLength;
    }

    public void setViewLength(String viewLength) {
        this.viewLength = viewLength;
    }

    @SerializedName("viewLength")
    @Expose
    private String viewLength;

    public FormModel(int questionId, String question, String viewType, String viewLength, JSONArray optionsJson) {
        this.questionId = questionId;
        this.question = question;
        this.viewType = viewType;
        this.options = new ArrayList<>();
        this.viewLength = viewLength;
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
