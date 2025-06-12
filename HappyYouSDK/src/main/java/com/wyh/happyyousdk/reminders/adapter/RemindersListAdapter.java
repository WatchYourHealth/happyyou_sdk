package com.wyh.happyyousdk.reminders.adapter;

import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.RemindersListAdapterBinding;
import com.wyh.happyyousdk.model.CommonSuccessResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.reminders.RemindersActivity;
import com.wyh.happyyousdk.model.request.DeleteReminderRequest;
import com.wyh.happyyousdk.model.request.UpdateReminderSettingRequest;
import com.wyh.happyyousdk.model.response.FetchRemindersByUUIDResponse;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemindersListAdapter extends RecyclerView.Adapter<RemindersListAdapter.MyViewHolder> {

    Context context;
    List<FetchRemindersByUUIDResponse.Datum> remindersList;

    public RemindersListAdapter(Context context, List<FetchRemindersByUUIDResponse.Datum> remindersList) {
        this.context = context;
        this.remindersList = remindersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RemindersListAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.reminders_list_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("Reminders", new Gson().toJson(remindersList));
        holder.binding.tvReminderName.setText(remindersList.get(position).getReminderTitle());
        String startTime = CommonUtils.formatDateFromString("HH:mm:ss", "h:mm aa", remindersList.get(position).getStartTime());
        String intervalTime = CommonUtils.formatDateFromString("HH:mm:ss", "h:mm aa", remindersList.get(position).getIntervalORFixedT());


        String reminderBy = (remindersList.get(position).getReminderBy()) != null ? remindersList.get(position).getReminderBy() : "";

        if (SharedPref.getAesUuid().equals(reminderBy)) {
            holder.binding.llMain.setBackgroundColor(context.getResources().getColor(R.color.transperent));
        } else {
            holder.binding.llMain.setBackgroundColor(context.getResources().getColor(R.color.blue));
        }

        if (remindersList.get(position).getIsPredefined()) {
            holder.binding.reminderSwitch.setVisibility(View.VISIBLE);
            boolean isSwitchOn;
            if (remindersList.get(position).getIsReminderOn().equals(0)) {
                isSwitchOn = false;
            } else {
                isSwitchOn = true;
            }
            holder.binding.reminderSwitch.setChecked(isSwitchOn);
            holder.binding.ivDeleteReminder.setVisibility(View.GONE);
        } else {
            holder.binding.reminderSwitch.setVisibility(View.GONE);
            holder.binding.ivDeleteReminder.setVisibility(View.VISIBLE);
        }

        if (remindersList.get(position).getIsRepetitive()) {
            holder.binding.tvReminderFrequency.setText("Daily");
        } else {
            holder.binding.tvReminderFrequency.setText("Today");
        }

        if (remindersList.get(position).getIsPredefined()) {
            if (remindersList.get(position).getReminderTitle().equalsIgnoreCase("water"))
                holder.binding.tvReminderTime.setText(startTime);
            else
                holder.binding.tvReminderTime.setText(intervalTime);
        } else {
            holder.binding.tvReminderTime.setText(intervalTime);
        }

        holder.binding.reminderSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            Log.d("Reminders", new Gson().toJson(remindersList.get(position)));
            updateReminderAPI(remindersList.get(position));
        });
        holder.binding.ivDeleteReminder.setOnClickListener(view -> {
            deleteReminderAPI(remindersList.get(position).getId());
        });


        if (holder.binding.reminderSwitch.getVisibility() == View.VISIBLE) {
            holder.binding.llMain.setBackgroundColor(context.getResources().getColor(R.color.transperent));
        }
    }

    private void deleteReminderAPI(Integer id) {
        ApiInterfaceWyh apiInterfaceWyh;
        ProgressDialog progressDialog;
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        DeleteReminderRequest deleteReminderRequest = new DeleteReminderRequest(SharedPref.getUuid(), id);
        Call<CommonSuccessResponse> call = apiInterfaceWyh.deleteReminder(SharedPref.getAuthToken(), deleteReminderRequest);
        Log.v("Url_Request_save", call.request().url() + "\n" + SharedPref.getAuthToken() + "\n" + new Gson().toJson(deleteReminderRequest));

        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.delete_reminder_success));
                    Toast.makeText(context, "Reminder Deleted Successfully", Toast.LENGTH_SHORT).show();
                    if (context instanceof RemindersActivity) {
                        ((RemindersActivity) context).getRemindersByUUID();
                    }
                   /* List<FetchRemindersByUUIDResponse.Datum> remindersList = response.body().getData();
                    if (remindersList.size() > 0) {
                        setRemindersRv(remindersList);
                    }*/
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.delete_reminder_failed));
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.delete_reminder_failed));
            }
        });
    }

    private void updateReminderAPI(FetchRemindersByUUIDResponse.Datum datum) {
        ApiInterfaceWyh apiInterfaceWyh;
        ProgressDialog progressDialog;
        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        int reminderOn;
        if (datum.getIsReminderOn().equals(1)) {
            reminderOn = 0;
        } else {
            reminderOn = 1;
        }
        UpdateReminderSettingRequest reminderRequest = new UpdateReminderSettingRequest(datum.getId(), datum.getActive(), datum.getAppointmentDate(), datum.getCommunityId(), datum.getEndTime(), datum.getIntervalORFixedT(), datum.getIsPredefined(), reminderOn, datum.getIsRepetitive(), SharedPref.getUuid(), datum.getReminderName(), datum.getReminderTitle(), datum.getStartTime(), SharedPref.getUuid(), datum.getWeekDays());
        Call<CommonSuccessResponse> call = apiInterfaceWyh.updateReminderAPI(SharedPref.getAuthToken(), Collections.singletonList(reminderRequest));
        Log.v("Url_Request_save", call.request().url() + "\n" + SharedPref.getAuthToken() + "\n" + new Gson().toJson(reminderRequest));

        call.enqueue(new Callback<CommonSuccessResponse>() {
            @Override
            public void onResponse(Call<CommonSuccessResponse> call, Response<CommonSuccessResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (response.code() == 200 && response.body() != null && response.body().isSuccess()) {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.update_reminder_settings_success));
                    Toast.makeText(context, "Reminder setting updated successfully", Toast.LENGTH_SHORT).show();
                    if (context instanceof RemindersActivity) {
                        ((RemindersActivity) context).getRemindersByUUID();
                    }
//                    finish();
                   /* List<FetchRemindersByUUIDResponse.Datum> remindersList = response.body().getData();
                    if (remindersList.size() > 0) {
                        setRemindersRv(remindersList);
                    }*/
                } else {
                    Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.update_reminder_settings_failed));
                    Toast.makeText(context, context.getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonSuccessResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), context.getString(R.string.update_reminder_settings_failed));
            }
        });
    }


    public interface CommunityListInterface {
        void setValues(List<Integer> values);
    }

    @Override
    public int getItemCount() {
        return remindersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RemindersListAdapterBinding binding;

        public MyViewHolder(@NonNull RemindersListAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
