package com.wyh.happyyousdk.dashboard.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.HomeCardsAdapterBinding;
import com.wyh.happyyousdk.model.response.dashboard.RemindersDetails;
import com.wyh.happyyousdk.model.response.getAnalysis.GetAnalysisResponse;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.Calendar;

public class HomeCardsAdapter extends RecyclerView.Adapter<HomeCardsAdapter.MyViewHolder> {

    Context context;
    GetAnalysisResponse getAnalysisResponse;
    ClickInterface clickInterface;
    MyViewHolder holder;
    boolean isMorning, isAfterNoon, isEvening;
    String redirect1, redirect2, redirect3;

    public HomeCardsAdapter(Context context, ClickInterface clickInterface) {
        this.context = context;
        this.clickInterface = clickInterface;

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        if (timeOfDay >= 6 && timeOfDay < 12) {
            isMorning = true;
            isEvening = false;
            isAfterNoon = false;
            redirect1 = "Reminder";
            redirect2 = "Riddle";
            redirect3 = "Goals";
        } else if (timeOfDay >= 12 && timeOfDay < 17) {
            isAfterNoon = true;
            isMorning = false;
            isEvening = false;
            redirect1 = "Affirmation";
            redirect2 = "Tribe";
            redirect3 = "Winnings";
        } else {
            isAfterNoon = false;
            isMorning = false;
            isEvening = true;
            redirect1 = "Reminder";
            redirect2 = "HealthHacks";
            redirect3 = "Lifestyle";
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeCardsAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.home_cards_adapter, parent, false);
        return new MyViewHolder(binding);
    }

    public void updateWinningsProgress(int progress) {
        if (holder != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.progressGoal3.setProgress(progress, true);
            } else {
                holder.binding.progressGoal3.setProgress(progress);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        this.holder = holder;
        if (isMorning) {
            getAnalysisResponse = new Gson().fromJson(SharedPref.getHRAAnalysis(), GetAnalysisResponse.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.binding.progressGoal2.setProgress(Integer.parseInt(SharedPref.getTodaySteps()), true);
            } else
                holder.binding.progressGoal2.setProgress(Integer.parseInt(SharedPref.getTodaySteps()));

            RemindersDetails remindersDetails = new Gson().fromJson(SharedPref.getUserLatestReminder(), RemindersDetails.class);

            if (remindersDetails != null && remindersDetails.getReminderName() != null) {
                String time = CommonUtils.formatDateFromString("HH:mm", "hh:mm a", remindersDetails.getReminderTime());
                holder.binding.tvGoalDesc1.setText("You have " + remindersDetails.getReminderName().toLowerCase() + " reminder at " + time);
            } else {
                holder.binding.tvGoalDesc1.setText("Please set reminders");
            }

            int stepGoals = SharedPref.getStepsGoals();

            holder.binding.tvStepGoal.setText(stepGoals + "");
        } else if (isAfterNoon) {
            holder.binding.ivGoalIcon1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_affirmation_smiley));
            holder.binding.tvGoal1.setText("Affirmation");
            holder.binding.tvGoalDesc1.setText("Read today’s quote?");

            holder.binding.ivGoalIconRiddle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tribe));
            holder.binding.tvGoalRiddle.setText("Tribe");
            holder.binding.tvGoalDescRiddle.setText("Check what your tribes are upto");

            holder.binding.rlGoals.setVisibility(View.GONE);
            holder.binding.rlWinnings.setVisibility(View.VISIBLE);
        } else {
            RemindersDetails remindersDetails = new Gson().fromJson(SharedPref.getUserLatestReminder(), RemindersDetails.class);
            holder.binding.tvGoal1.setText("Reminder");
            holder.binding.ivGoalIcon1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bell));
            if (remindersDetails != null && remindersDetails.getReminderName() != null) {
                String time = CommonUtils.formatDateFromString("HH:mm", "hh:mm a", remindersDetails.getReminderTime());
                holder.binding.tvGoalDesc1.setText("You have " + remindersDetails.getReminderName().toLowerCase() + " reminder at " + time);
            } else {
                holder.binding.tvGoalDesc1.setText("Please set reminders");
            }

            holder.binding.ivGoalIconRiddle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_quick_read));
            holder.binding.tvGoalRiddle.setText("Health Hacks");
            holder.binding.tvGoalDescRiddle.setText("Know how you can ace your health");

            holder.binding.rlGoals.setVisibility(View.GONE);
            holder.binding.rlWinnings.setVisibility(View.VISIBLE);
            holder.binding.progressGoal3.setVisibility(View.GONE);

            holder.binding.tvGoal3.setText("Lifestyle");
            holder.binding.tvGoalDesc3.setText("Track your way of living");
            holder.binding.ivGoalIcon3.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_foot_prints));
        }


        holder.binding.rlMain1.setOnClickListener(view -> {
            clickInterface.onItemClicked(position, redirect1);
        });

        holder.binding.rlMain2.setOnClickListener(view -> {
            clickInterface.onItemClicked(position, redirect2);
        });

        holder.binding.rlMain3.setOnClickListener(view -> {
            clickInterface.onItemClicked(position, redirect3);
        });
    }

    public interface ClickInterface {
        void onItemClicked(int index, String tag);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        HomeCardsAdapterBinding binding;

        public MyViewHolder(HomeCardsAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
