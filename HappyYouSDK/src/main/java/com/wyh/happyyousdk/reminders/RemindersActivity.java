package com.wyh.happyyousdk.reminders;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
;
import com.wyh.happyyousdk.R;

import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ActivityRemindersBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.model.request.UuidRequest;
import com.wyh.happyyousdk.model.request.encrDecr.EncryptionRequest;
import com.wyh.happyyousdk.model.response.encrDecr.EncryptionData;
import com.wyh.happyyousdk.model.response.encrDecr.EncryptionResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.reminders.adapter.RemindersListAdapter;
import com.wyh.happyyousdk.model.request.AddReminderRequest;
import com.wyh.happyyousdk.model.response.FetchRemindersByUUIDResponse;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemindersActivity extends AppCompatActivity {
    ActivityRemindersBinding binding;
    ApiInterfaceWyh apiInterfaceWyh;
    ProgressDialog progressDialog;
    ArrayAdapter<String> frequencySpinnerAdapter;
    ArrayAdapter<String> reminderNameSpinnerAdapter;
    String reminderType, reminderFrequency;
    Context context;
    private static final int INTERVAL = 15;
    private static final DecimalFormat FORMATTER = new DecimalFormat("00");

    private TimePicker picker; // set in onCreate
    private NumberPicker minutePicker;
    String selectedDate;
    boolean isRepetitive;
    String minutes = "";
    List<String> weekDays = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reminders);
        context = this;

        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        setMinutePicker();
        getRemindersByUUID();
        setFrequencyAdapter();
        setReminderTypeAdapter();

        JSONObject customObj = new JSONObject();
        try {
            customObj.put("PAGE_ID", "Reminders");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        


        binding.includeToolbar.tvBack.setText("Reminders");
        binding.includeToolbar.llBack.setOnClickListener(view -> finish());

        binding.ivHome.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        if (binding.timePicker.getMinute() == 1) {
            minutes = "15";
        } else if (binding.timePicker.getMinute() == 2) {
            minutes = "30";
        } else if (binding.timePicker.getMinute() == 3) {
            minutes = "45";
        } else if (binding.timePicker.getMinute() == 4) {
            minutes = "00";
        }
        selectedDate = formattedDate + " " + String.valueOf(binding.timePicker.getCurrentHour()) + ":" + minutes + ":00";

        binding.timePicker.setOnTimeChangedListener((timePicker, hourOfDay, minute) -> {
            if (minute == 1) {
                minutes = "15";
            } else if (minute == 2) {
                minutes = "30";
            } else if (minute == 3) {
                minutes = "45";
            } else if (minute == 0) {
                minutes = "00";
            }
            selectedDate = formattedDate + " " + String.valueOf(hourOfDay) + ":" + minutes + ":00";
        });

        binding.btnCancelReminder.setOnClickListener(view -> {
            finish();
        });


      /*  if (binding.cbMon.isChecked()) {
            weekDays.add("Monday");
        } else {
            for (int i = 0; i < weekDays.size(); i++) {
                if (weekDays.get(i).contains("Monday")) {
                    weekDays.remove(i);
                }
            }
        }
        if (binding.cbTues.isChecked()) {
            weekDays.add("Tuesday");
        } else {
            for (int i = 0; i < weekDays.size(); i++) {
                if (weekDays.get(i).contains("Tuesday")) {
                    weekDays.remove(i);
                }
            }
        }
        if (binding.cbWed.isChecked()) {
            weekDays.add("Wednesday");
        } else {
            for (int i = 0; i < weekDays.size(); i++) {
                if (weekDays.get(i).contains("Wednesday")) {
                    weekDays.remove(i);
                }
            }
        }
        if (binding.cbThur.isChecked()) {
            weekDays.add("Thursday");
        } else {
            for (int i = 0; i < weekDays.size(); i++) {
                if (weekDays.get(i).contains("Thursday")) {
                    weekDays.remove(i);
                }
            }
        }
        if (binding.cbFri.isChecked()) {
            weekDays.add("Friday");
        } else {
            for (int i = 0; i < weekDays.size(); i++) {
                if (weekDays.get(i).contains("Friday")) {
                    weekDays.remove(i);
                }
            }
        }
        if (binding.cbSat.isChecked()) {
            weekDays.add("Saturday");
        } else {
            for (int i = 0; i < weekDays.size(); i++) {
                if (weekDays.get(i).contains("Saturday")) {
                    weekDays.remove(i);
                }
            }
        }
        if (binding.cbSun.isChecked()) {
            weekDays.add("Sunday");
        } else {
            for (int i = 0; i < weekDays.size(); i++) {
                if (weekDays.get(i).contains("Sunday")) {
                    weekDays.remove(i);
                }
            }
        }*/

        binding.btnAddReminder.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(reminderFrequency) && reminderFrequency.equals("Once")){
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String currentDate = dtf.format(now).split(":")[0]+":"+dtf.format(now).split(":")[1]+":00";
                Log.d("date ", selectedDate);
                Log.d("date ", currentDate);
                int hour1, hour2, min1, min2;
                LocalTime time1, time2;

                hour1 = Integer.parseInt(selectedDate.split(" ")[1].split(":")[0]);
                min1 = Integer.parseInt(selectedDate.split(" ")[1].split(":")[1]);
                hour2 = Integer.parseInt(currentDate.split(" ")[1].split(":")[0]);
                min2 = Integer.parseInt(currentDate.split(" ")[1].split(":")[1]);

                time1 = LocalTime.of(hour1, min1, 0);
                time2 = LocalTime.of(hour2, min2, 0);

                Log.d("date ", time1+"");
                Log.d("date ", time2+"");

                int timeCheck = time1.compareTo(time2);
                Log.d("date ", timeCheck+"");
                if(timeCheck < 0 || timeCheck == 0){
                    Toast.makeText(context, "Please Select correct reminder time", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (TextUtils.isEmpty(binding.etReminderName.getText())) {
                Toast.makeText(context, "Please enter Reminder name", Toast.LENGTH_SHORT).show();
                binding.etReminderName.requestFocus();
                CommonUtils.showKeyboard(this);
                return;
            }
            if (TextUtils.isEmpty(reminderFrequency)) {
                Toast.makeText(context, "Please select Reminder Frequency", Toast.LENGTH_SHORT).show();
                binding.spinnerReminderFrequency.requestFocus();
                return;
//                CommonUtils.showKeyboard(this);
            }

            if (isRepetitive && weekDays.size() == 0) {
                Toast.makeText(context, "Please select Days", Toast.LENGTH_SHORT).show();
                return;
//                CommonUtils.showKeyboard(this);
            }
            if (TextUtils.isEmpty(reminderType)) {
                Toast.makeText(context, "Please select Reminder Type", Toast.LENGTH_SHORT).show();
                binding.spinnerRemindersName.requestFocus();
//                CommonUtils.showKeyboard(this);
                return;
            }

            if (!TextUtils.isEmpty(binding.etReminderName.getText()) && !TextUtils.isEmpty(reminderFrequency) && !TextUtils.isEmpty(reminderType)) {
                String str = "";
                String day = LocalDate.now().getDayOfWeek().name();
                if (isRepetitive) {
                    str = String.join(",", weekDays);
                } else {
                    str = day;
                }
                addReminderAPI(str);
            }

        });

    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        int id = view.getId();
        if (id == R.id.cb_mon) {
            if (checked)
                weekDays.add("Monday");
            else
                weekDays.remove("Monday");
        } else if (id == R.id.cb_tues) {
            if (checked)
                weekDays.add("Tuesday");
            else
                weekDays.remove("Tuesday");
        } else if (id == R.id.cb_wed) {
            if (checked)
                weekDays.add("Wednesday");
            else
                weekDays.remove("Wednesday");
        } else if (id == R.id.cb_thur) {
            if (checked)
                weekDays.add("Thursday");
            else
                weekDays.remove("Thursday");
        } else if (id == R.id.cb_fri) {
            if (checked)
                weekDays.add("Friday");
            else
                weekDays.remove("Friday");
        } else if (id == R.id.cb_sat) {
            if (checked)
                weekDays.add("Saturday");
            else
                weekDays.remove("Saturday");
        } else if (id == R.id.cb_sun) {
            if (checked)
                weekDays.add("Sunday");
            else
                weekDays.remove("Sunday");
        }
    }

    private void addReminderAPI(String days) {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        AddReminderRequest reminderRequest = new AddReminderRequest(SharedPref.getUuid(), selectedDate, selectedDate, reminderType, selectedDate, isRepetitive, 1, 1, selectedDate, false, binding.etReminderName.getText().toString() , days, SharedPref.getUuid(), 0);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.setReminderAPI(SharedPref.getAuthToken(), reminderRequest);
        Log.v("Url_Request_save", call.request().url() + "\n" + SharedPref.getAuthToken() + "\n" + new Gson().toJson(reminderRequest));

        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {

                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.create_reminder_success));
                    Toast.makeText(RemindersActivity.this, "Reminder Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    CommonUtils.hideKeyboard(RemindersActivity.this);
                   /* List<FetchRemindersByUUIDResponse.Datum> remindersList = response.body().getData();
                    if (remindersList.size() > 0) {
                        setRemindersRv(remindersList);
                    }*/
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.create_reminder_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.create_reminder_failed));
            }
        });
    }

    private void setReminderTypeAdapter() {
        String[] frequency = new String[]{"Select Reminder Type", "Medicines", "Others"};
        reminderNameSpinnerAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, frequency);
        reminderNameSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerRemindersName.setAdapter(reminderNameSpinnerAdapter);
        binding.spinnerRemindersName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Select Reminder Type")) {
                    reminderType = "";
                } else {
                    reminderType = adapterView.getItemAtPosition(i).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setFrequencyAdapter() {
        String[] frequency = new String[]{"Select Reminder Frequency", "Repeat", "Once"};
        frequencySpinnerAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, frequency);
        frequencySpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        binding.spinnerReminderFrequency.setAdapter(frequencySpinnerAdapter);
        binding.spinnerReminderFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("Select Reminder Frequency")) {
                    reminderFrequency = "";
                    binding.cbDays.setVisibility(View.GONE);
                } else {
                    reminderFrequency = adapterView.getItemAtPosition(i).toString();
                    isRepetitive = reminderFrequency.equals("Repeat");
                    if (isRepetitive) {
                        binding.cbDays.setVisibility(View.VISIBLE);
                    } else {
                        binding.cbDays.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setMinutePicker() {
        int numValues = 60 / INTERVAL;
        String[] displayedValues = new String[numValues];
        for (int i = 0; i < numValues; i++) {
            displayedValues[i] = FORMATTER.format(i * INTERVAL);
        }

        View minute = binding.timePicker.findViewById(Resources.getSystem().getIdentifier("minute", "id", "android"));
//        View amPmId = binding.timePicker.findViewById(Resources.getSystem().getIdentifier("ampm_layout", "id", "android"));
//        if (amPmId != null) {
//            amPmId.setVisibility(View.GONE);
//        }
        if ((minute != null) && (minute instanceof NumberPicker)) {
            minutePicker = (NumberPicker) minute;
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(numValues - 1);
            minutePicker.setDisplayedValues(displayedValues);
        }
    }

    public int getMinute() {
        if (minutePicker != null) {
            return (minutePicker.getValue() * 2);
        } else {
            return picker.getCurrentMinute();
        }
    }


    public void getRemindersByUUID() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        UuidRequest uuidRequest = new UuidRequest(SharedPref.getUuid());
        Call<FetchRemindersByUUIDResponse> call = apiInterfaceWyh.getRemindersByUUID(SharedPref.getAuthToken(), uuidRequest);
        Log.v("Url_Request_save", call.request().url() + "\n" + SharedPref.getAuthToken());

        call.enqueue(new Callback<FetchRemindersByUUIDResponse>() {
            @Override
            public void onResponse(Call<FetchRemindersByUUIDResponse> call, Response<FetchRemindersByUUIDResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_reminder_by_uuid_success));

                    List<FetchRemindersByUUIDResponse.Datum> remindersList = response.body().getData();
                    Log.d("reminder", new Gson().toJson(response.body()));
                    if (remindersList.size() > 0) {
                        setRemindersRv(remindersList);
                    }
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_reminder_by_uuid_failed));
                    Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchRemindersByUUIDResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.fetch_reminder_by_uuid_failed));
            }
        });
    }

    private void setRemindersRv(List<FetchRemindersByUUIDResponse.Datum> remindersList) {
        RemindersListAdapter remindersListAdapter = new RemindersListAdapter(context, remindersList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.rvReminders.setLayoutManager(linearLayoutManager);
        binding.rvReminders.setAdapter(remindersListAdapter);
        binding.rvReminders.setHasFixedSize(true);
    }
}