package com.wyh.happyyousdk.Goals.Adapters;

import static com.wyh.happyyousdk.utils.Constants.CALORIE;
import static com.wyh.happyyousdk.utils.Constants.CALORIE_BURNED;
import static com.wyh.happyyousdk.utils.Constants.CALORIE_CONSUME;
import static com.wyh.happyyousdk.utils.Constants.MEDITATION;
import static com.wyh.happyyousdk.utils.Constants.SLEEP;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.WATER;
import static com.wyh.happyyousdk.utils.Constants.WEIGHT;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyh.happyyousdk.Goals.Utils.Master;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.diary.NewDiaryActivity;
import com.wyh.happyyousdk.model.response.HistoryData;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;

public class DeleteGaolItemAdapter extends RecyclerView.Adapter<DeleteGaolItemAdapter.ViewHolder> {

    Context context;
    ArrayList<HistoryData> list;

    String comingFrom;


    public DeleteGaolItemAdapter(Context context, ArrayList<HistoryData> list, String comingFrom) {
        this.context = context;
        this.list = list;
        this.comingFrom = comingFrom;
    }

    @NonNull
    @Override
    public DeleteGaolItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.individual_goal_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteGaolItemAdapter.ViewHolder holder, int position) {
        String daysOrDay = "day";
        String glasseOrglasses = "glass";
        String hrOrHrs = "hr";
        String stepOrSteps = "step";
        String calOrCals = "cal";

        holder.deleteGoal1.setVisibility(View.GONE);
        holder.deleteGoal2.setVisibility(View.GONE);
        holder.deleteGoal3.setVisibility(View.GONE);



        switch (position) {
            case 0 :
                for (int i=0; i < 3; i++){
                    if(list.size() > i){
                        if(list.get(i).getNoOfDays() > 1){
                            daysOrDay = "days";
                        }else{
                            daysOrDay = "day";
                        }
                        if(list.get(i).getGoalTarget() > 1){
                            glasseOrglasses = "glasses";
                            hrOrHrs = "hrs";
                            stepOrSteps = "steps";
                            calOrCals = "cals";
                        }else{
                            glasseOrglasses = "glass";
                            hrOrHrs = "hr";
                            stepOrSteps = "step";
                            calOrCals = "cal";
                        }
                    }
                    if(i == 0){
                        if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                            holder.tvComName1.setText("Drink"+" "+list.get(i).getGoalTarget()+" "+glasseOrglasses+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                            holder.tvComName1.setText("Sleep"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                            holder.tvComName1.setText("Walk"+" "+list.get(i).getGoalTarget()+" "+stepOrSteps+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                            holder.tvComName1.setText("Meditate"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                            holder.tvComName1.setText("Burn"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_calories_burnt_white));

                        }else if (list.get(i).getGoalName().replace("\n","").equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME().replace("\n",""))){
                            holder.tvComName1.setText("Consume"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                            double currentWeight = 0.0;
                            if(SharedPref.getWeight().equalsIgnoreCase("")){
                                 currentWeight = 0.0;
                            }else{
                                 currentWeight = Double.valueOf(SharedPref.getWeight());

                            }
                            double weightToLoseOrGain = currentWeight - list.get(i).getGoalTarget();
                            String desc = "";
                            if(weightToLoseOrGain < 0){
                                desc = "Gain"+" "+ -((int) weightToLoseOrGain)+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                            }else if(weightToLoseOrGain > 0){
                                desc = "Lose"+" "+ weightToLoseOrGain+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                            }else {
                                desc = "Maintain"+" "+list.get(i).getGoalTarget()+" "+"kg"+" for "+list.get(i).getNoOfDays() + " "+daysOrDay;
                            }


                            holder.tvComName1.setText(desc);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                            holder.tvComName1.setText(list.get(i).getGoalDescription());
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));
                        }

                        holder.goalProgressBar.setProgress((int) list.get(i).getProgressPercentage());
                        holder.goalPointsTv.setText(String.valueOf(list.get(i).getProgressPercentage()));
                        if(comingFrom.equalsIgnoreCase("completed")){
                            holder.goalcompletecheckimg.setVisibility(View.VISIBLE);
                        }else{
                            holder.goalcompletecheckimg.setVisibility(View.GONE);
                        }

                        holder.rlGoal1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                redirectGoal(list.get(0).getGoalName(),list.get(0).getGoalDescription());
                            }
                        });
                    }

                    if(i == 1){
                        if(list.size() > 1){
                            if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                                holder.tvComName2.setText("Drink"+" "+list.get(i).getGoalTarget()+" "+glasseOrglasses+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                                holder.tvComName2.setText("Sleep"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                                holder.tvComName2.setText("Walk"+" "+list.get(i).getGoalTarget()+" "+stepOrSteps+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                                holder.tvComName2.setText("Meditate"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                                holder.tvComName2.setText("Burn"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_calories_burnt_white));

                            }else if (list.get(i).getGoalName().replace("\n","").equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME().replace("\n",""))){
                                holder.tvComName2.setText("Consume"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                                double currentWeight = 0.0;
                                if(SharedPref.getWeight().equalsIgnoreCase("")){
                                    currentWeight = 0.0;
                                }else{
                                    currentWeight = Double.valueOf(SharedPref.getWeight());

                                }
                                double weightToLoseOrGain = currentWeight - list.get(i).getGoalTarget();
                                String desc = "";
                                if(weightToLoseOrGain < 0){
                                    desc = "Gain"+" "+ -((int) weightToLoseOrGain)+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else if(weightToLoseOrGain > 0){
                                    desc = "Lose"+" "+ weightToLoseOrGain+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else {
                                    desc = "Maintain"+" "+list.get(i).getGoalTarget()+" "+"kg"+" for "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }

                                holder.tvComName2.setText(desc);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                                holder.tvComName2.setText(list.get(i).getGoalDescription());
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));

                            }

                            holder.goalProgressBar2.setProgress((int) list.get(i).getProgressPercentage());
                            holder.goalPointsTv2.setText(String.valueOf(list.get(i).getProgressPercentage()));
                            if(comingFrom.equalsIgnoreCase("completed")){
                                holder.goalcompletecheckimg2.setVisibility(View.VISIBLE);
                            }else{
                                holder.goalcompletecheckimg2.setVisibility(View.GONE);
                            }

                            holder.rlGoal2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    redirectGoal(list.get(1).getGoalName(),list.get(1).getGoalDescription());

                                }
                            });
                        }else{
                            holder.rlGoal2.setVisibility(View.GONE);
                            holder.rlGoal3.setVisibility(View.GONE);
                        }
                    }

                    if(i == 2) {
                        if(list.size() > 2){
                            if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                                holder.tvComName3.setText("Drink"+" "+list.get(i).getGoalTarget()+" "+glasseOrglasses+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                                holder.tvComName3.setText("Sleep"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                                holder.tvComName3.setText("Walk"+" "+list.get(i).getGoalTarget()+" "+stepOrSteps+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                                holder.tvComName3.setText("Meditate"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                                holder.tvComName3.setText("Burn"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_calories_burnt_white));

                            }else if (list.get(i).getGoalName().replace("\n","").equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME().replace("\n",""))){
                                holder.tvComName3.setText("Consume"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                                double currentWeight = 0.0;
                                if(SharedPref.getWeight().equalsIgnoreCase("")){
                                    currentWeight = 0.0;
                                }else{
                                    currentWeight = Double.valueOf(SharedPref.getWeight());

                                }                                double weightToLoseOrGain = currentWeight - list.get(i).getGoalTarget();
                                String desc = "";
                                if(weightToLoseOrGain < 0){
                                    desc = "Gain"+" "+ -((int) weightToLoseOrGain)+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else if(weightToLoseOrGain > 0){
                                    desc = "Lose"+" "+ weightToLoseOrGain+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else {
                                    desc = "Maintain"+" "+list.get(i).getGoalTarget()+" "+"kg"+" for "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }

                                                            holder.tvComName3.setText(desc);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                                holder.tvComName3.setText(list.get(i).getGoalDescription());
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));

                            }

                            holder.goalProgressBar3.setProgress((int) list.get(i).getProgressPercentage());
                            holder.goalPointsTv3.setText(String.valueOf(list.get(i).getProgressPercentage()));
                            if(comingFrom.equalsIgnoreCase("completed")){
                                holder.goalcompletecheckimg3.setVisibility(View.VISIBLE);
                            }else{
                                holder.goalcompletecheckimg3.setVisibility(View.GONE);
                            }

                            holder.rlGoal3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    redirectGoal(list.get(2).getGoalName(),list.get(2).getGoalDescription());

                                }
                            });
                        }else{
                            holder.rlGoal3.setVisibility(View.GONE);
                        }
                    }

                }
                break;

            case 1 :
                for (int i =3; i < 6; i++){
                    if(list.size() > i){
                        if(list.get(i).getNoOfDays() > 1){
                            daysOrDay = "days";
                        }else{
                            daysOrDay = "day";
                        }
                        if(list.get(i).getGoalTarget() > 1){
                            glasseOrglasses = "glasses";
                            hrOrHrs = "hrs";
                            stepOrSteps = "steps";
                            calOrCals = "cals";
                        }else{
                            glasseOrglasses = "glass";
                            hrOrHrs = "hr";
                            stepOrSteps = "step";
                            calOrCals = "cal";
                        }
                    }

                    if(i == 3){
                        if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                            holder.tvComName1.setText("Drink"+" "+list.get(i).getGoalTarget()+" "+glasseOrglasses+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                            holder.tvComName1.setText("Sleep"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                            holder.tvComName1.setText("Walk"+" "+list.get(i).getGoalTarget()+" "+stepOrSteps+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                            holder.tvComName1.setText("Meditate"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                            holder.tvComName1.setText("Burn"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_calories_burnt_white));

                        }else if (list.get(i).getGoalName().replace("\n","").equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME().replace("\n",""))){
                            holder.tvComName1.setText("Consume"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                            double currentWeight = 0.0;
                            if(SharedPref.getWeight().equalsIgnoreCase("")){
                                currentWeight = 0.0;
                            }else{
                                currentWeight = Double.valueOf(SharedPref.getWeight());

                            }                            double weightToLoseOrGain = currentWeight - list.get(i).getGoalTarget();
                            String desc = "";
                            if(weightToLoseOrGain < 0){
                                desc = "Gain"+" "+ -((int) weightToLoseOrGain)+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                            }else if(weightToLoseOrGain > 0){
                                desc = "Lose"+" "+ weightToLoseOrGain+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                            }else {
                                desc = "Maintain"+" "+list.get(i).getGoalTarget()+" "+"kg"+" for "+list.get(i).getNoOfDays() + " "+daysOrDay;
                            }

                            holder.tvComName1.setText(desc);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                            holder.tvComName1.setText(list.get(i).getGoalDescription());
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));
                        }

                        holder.goalProgressBar.setProgress((int) list.get(i).getProgressPercentage());
                        holder.goalPointsTv.setText(String.valueOf(list.get(i).getProgressPercentage()));
                        if(comingFrom.equalsIgnoreCase("completed")){
                            holder.goalcompletecheckimg.setVisibility(View.VISIBLE);
                        }else{
                            holder.goalcompletecheckimg.setVisibility(View.GONE);
                        }


                        holder.rlGoal1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                redirectGoal(list.get(3).getGoalName(),list.get(3).getGoalDescription());
                            }
                        });
                    }

                    if(i == 4){
                        if(list.size() > 4){
                            if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                                holder.tvComName2.setText("Drink"+" "+list.get(i).getGoalTarget()+" "+glasseOrglasses+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                                holder.tvComName2.setText("Sleep"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                                holder.tvComName2.setText("Walk"+" "+list.get(i).getGoalTarget()+" "+stepOrSteps+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                                holder.tvComName2.setText("Meditate"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                                holder.tvComName2.setText("Burn"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_calories_burnt_white));

                            }else if (list.get(i).getGoalName().replace("\n","").equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME().replace("\n",""))){
                                holder.tvComName2.setText("Consume"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                                double currentWeight = 0.0;
                                if(SharedPref.getWeight().equalsIgnoreCase("")){
                                    currentWeight = 0.0;
                                }else{
                                    currentWeight = Double.valueOf(SharedPref.getWeight());

                                }                                double weightToLoseOrGain = currentWeight - list.get(i).getGoalTarget();
                                String desc = "";
                                if(weightToLoseOrGain < 0){
                                    desc = "Gain"+" "+ -((int) weightToLoseOrGain)+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else if(weightToLoseOrGain > 0){
                                    desc = "Lose"+" "+ weightToLoseOrGain+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else {
                                    desc = "Maintain"+" "+list.get(i).getGoalTarget()+" "+"kg"+" for "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }

                                holder.tvComName2.setText(desc);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                                holder.tvComName2.setText(list.get(i).getGoalDescription());
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));

                            }

                            holder.goalProgressBar2.setProgress((int) list.get(i).getProgressPercentage());
                            holder.goalPointsTv2.setText(String.valueOf(list.get(i).getProgressPercentage()));
                            if(comingFrom.equalsIgnoreCase("completed")){
                                holder.goalcompletecheckimg2.setVisibility(View.VISIBLE);
                            }else{
                                holder.goalcompletecheckimg2.setVisibility(View.GONE);
                            }


                            holder.rlGoal2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    redirectGoal(list.get(4).getGoalName(),list.get(4).getGoalDescription());

                                }
                            });
                        }else{
                            holder.rlGoal2.setVisibility(View.GONE);
                            holder.rlGoal3.setVisibility(View.GONE);
                        }
                    }

                    if(i == 5){
                        if (list.size() > 5){
                            if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                                holder.tvComName3.setText("Drink"+" "+list.get(i).getGoalTarget()+" "+glasseOrglasses+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                                holder.tvComName3.setText("Sleep"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                                holder.tvComName3.setText("Walk"+" "+list.get(i).getGoalTarget()+" "+stepOrSteps+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                                holder.tvComName3.setText("Meditate"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                                holder.tvComName3.setText("Burn"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_calories_burnt_white));

                            }else if (list.get(i).getGoalName().replace("\n","").equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME().replace("\n",""))){
                                holder.tvComName3.setText("Consume"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                                double currentWeight = 0.0;
                                if(SharedPref.getWeight().equalsIgnoreCase("")){
                                    currentWeight = 0.0;
                                }else{
                                    currentWeight = Double.valueOf(SharedPref.getWeight());

                                }                                double weightToLoseOrGain = currentWeight - list.get(i).getGoalTarget();
                                String desc = "";
                                if(weightToLoseOrGain < 0){
                                    desc = "Gain"+" "+ -((int) weightToLoseOrGain)+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else if(weightToLoseOrGain > 0){
                                    desc = "Lose"+" "+ weightToLoseOrGain+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else {
                                    desc = "Maintain"+" "+list.get(i).getGoalTarget()+" "+"kg"+" for "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }

                                holder.tvComName3.setText(desc);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                                holder.tvComName3.setText(list.get(i).getGoalDescription());
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));

                            }

                            holder.goalProgressBar3.setProgress((int) list.get(i).getProgressPercentage());
                            holder.goalPointsTv3.setText(String.valueOf(list.get(i).getProgressPercentage()));
                            if(comingFrom.equalsIgnoreCase("completed")){
                                holder.goalcompletecheckimg3.setVisibility(View.VISIBLE);
                            }else{
                                holder.goalcompletecheckimg3.setVisibility(View.GONE);
                            }

                            holder.rlGoal3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    redirectGoal(list.get(5).getGoalName(),list.get(5).getGoalDescription());

                                }
                            });
                        }else{
                            holder.rlGoal3.setVisibility(View.GONE);
                        }
                    }
                }
                break;

            case 2 :
                for (int i = 6; i < 9; i ++){
                    if(list.size() > i){
                        if(list.get(i).getNoOfDays() > 1){
                            daysOrDay = "days";
                        }else{
                            daysOrDay = "day";
                        }
                        if(list.get(i).getGoalTarget() > 1){
                            glasseOrglasses = "glasses";
                            hrOrHrs = "hrs";
                            stepOrSteps = "steps";
                            calOrCals = "cals";
                        }else{
                            glasseOrglasses = "glass";
                            hrOrHrs = "hr";
                            stepOrSteps = "step";
                            calOrCals = "cal";
                        }
                    }

                    if(i == 6){
                        if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                            holder.tvComName1.setText("Drink"+" "+list.get(i).getGoalTarget()+" "+glasseOrglasses+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                            holder.tvComName1.setText("Sleep"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                            holder.tvComName1.setText("Walk"+" "+list.get(i).getGoalTarget()+" "+stepOrSteps+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                            holder.tvComName1.setText("Meditate"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                            holder.tvComName1.setText("Burn"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_calories_burnt_white));

                        }else if (list.get(i).getGoalName().replace("\n","").equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME().replace("\n",""))){
                            holder.tvComName1.setText("Consume"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                            double currentWeight = 0.0;
                            if(SharedPref.getWeight().equalsIgnoreCase("")){
                                currentWeight = 0.0;
                            }else{
                                currentWeight = Double.valueOf(SharedPref.getWeight());

                            }                            double weightToLoseOrGain = currentWeight - list.get(i).getGoalTarget();
                            String desc = "";
                            if(weightToLoseOrGain < 0){
                                desc = "Gain"+" "+ -((int) weightToLoseOrGain)+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                            }else if(weightToLoseOrGain > 0){
                                desc = "Lose"+" "+ weightToLoseOrGain+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                            }else {
                                desc = "Maintain"+" "+list.get(i).getGoalTarget()+" "+"kg"+" for "+list.get(i).getNoOfDays() + " "+daysOrDay;
                            }

                            holder.tvComName1.setText(desc);
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));


                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                            holder.tvComName1.setText(list.get(i).getGoalDescription());
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));
                        }

                        holder.goalProgressBar.setProgress((int) list.get(i).getProgressPercentage());
                        holder.goalPointsTv.setText(String.valueOf(list.get(i).getProgressPercentage()));
                        if(comingFrom.equalsIgnoreCase("completed")){
                            holder.goalcompletecheckimg.setVisibility(View.VISIBLE);
                        }else{
                            holder.goalcompletecheckimg.setVisibility(View.GONE);
                        }

                        holder.rlGoal1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                redirectGoal(list.get(6).getGoalName(),list.get(6).getGoalDescription());
                            }
                        });
                    }

                    if(i == 7){
                        if(list.size() > 7){
                            if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                                holder.tvComName2.setText("Drink"+" "+list.get(i).getGoalTarget()+" "+glasseOrglasses+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                                holder.tvComName2.setText("Sleep"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                                holder.tvComName2.setText("Walk"+" "+list.get(i).getGoalTarget()+" "+stepOrSteps+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                                holder.tvComName2.setText("Meditate"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                                holder.tvComName2.setText("Burn"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_calories_burnt_white));

                            }else if (list.get(i).getGoalName().replace("\n","").equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME().replace("\n",""))){
                                holder.tvComName2.setText("Consume"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                                double currentWeight = 0.0;
                                if(SharedPref.getWeight().equalsIgnoreCase("")){
                                    currentWeight = 0.0;
                                }else{
                                    currentWeight = Double.valueOf(SharedPref.getWeight());

                                }                                double weightToLoseOrGain = currentWeight - list.get(i).getGoalTarget();
                                String desc = "";
                                if(weightToLoseOrGain < 0){
                                    desc = "Gain"+" "+ -((int) weightToLoseOrGain)+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else if(weightToLoseOrGain > 0){
                                    desc = "Lose"+" "+ weightToLoseOrGain+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else {
                                    desc = "Maintain"+" "+list.get(i).getGoalTarget()+" "+"kg"+" for "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }

                                holder.tvComName2.setText(desc);
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                                holder.tvComName2.setText(list.get(i).getGoalDescription());
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));

                            }

                            holder.goalProgressBar2.setProgress((int) list.get(i).getProgressPercentage());
                            holder.goalPointsTv2.setText(String.valueOf(list.get(i).getProgressPercentage()));
                            if(comingFrom.equalsIgnoreCase("completed")){
                                holder.goalcompletecheckimg2.setVisibility(View.VISIBLE);
                            }else{
                                holder.goalcompletecheckimg2.setVisibility(View.GONE);
                            }


                            holder.rlGoal2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    redirectGoal(list.get(7).getGoalName(),list.get(7).getGoalDescription());

                                }
                            });
                        }else{
                            holder.rlGoal2.setVisibility(View.GONE);
                            holder.rlGoal3.setVisibility(View.GONE);
                        }
                    }

                    if(i == 8){
                        if (list.size() > 8){
                            if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                                holder.tvComName3.setText("Drink"+" "+list.get(i).getGoalTarget()+" "+glasseOrglasses+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                                holder.tvComName3.setText("Sleep"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                                holder.tvComName3.setText("Walk"+" "+list.get(i).getGoalTarget()+" "+stepOrSteps+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                                holder.tvComName3.setText("Meditate"+" "+list.get(i).getGoalTarget()+" "+hrOrHrs+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                                holder.tvComName3.setText("Burn"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_calories_burnt_white));

                            }else if (list.get(i).getGoalName().replace("\n","").equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME().replace("\n",""))){
                                holder.tvComName3.setText("Consume"+" "+list.get(i).getGoalTarget()+" "+calOrCals+" in "+list.get(i).getNoOfDays() + " "+daysOrDay);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                                double currentWeight = 0.0;
                                if(SharedPref.getWeight().equalsIgnoreCase("")){
                                    currentWeight = 0.0;
                                }else{
                                    currentWeight = Double.valueOf(SharedPref.getWeight());

                                }                                double weightToLoseOrGain = currentWeight - list.get(i).getGoalTarget();
                                String desc = "";
                                if(weightToLoseOrGain < 0){
                                    desc = "Gain"+" "+ -((int) weightToLoseOrGain)+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else if(weightToLoseOrGain > 0){
                                    desc = "Lose"+" "+ weightToLoseOrGain+" "+"kg"+" in "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }else {
                                    desc = "Maintain"+" "+list.get(i).getGoalTarget()+" "+"kg"+" for "+list.get(i).getNoOfDays() + " "+daysOrDay;
                                }

                                holder.tvComName3.setText(desc);
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                                holder.tvComName3.setText(list.get(i).getGoalDescription());
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));

                            }

                            holder.goalProgressBar3.setProgress((int) list.get(i).getProgressPercentage());
                            holder.goalPointsTv3.setText(String.valueOf(list.get(i).getProgressPercentage()));
                            if(comingFrom.equalsIgnoreCase("completed")){
                                holder.goalcompletecheckimg3.setVisibility(View.VISIBLE);
                            }else{
                                holder.goalcompletecheckimg3.setVisibility(View.GONE);
                            }

                            holder.rlGoal3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    redirectGoal(list.get(8).getGoalName(),list.get(8).getGoalDescription());

                                }
                            });
                        }else{
                            holder.rlGoal3.setVisibility(View.GONE);
                        }
                    }
                }
                break;

            case 3:
                holder.rlGoal1.setVisibility(View.GONE);
                holder.rlGoal2.setVisibility(View.GONE);
                holder.rlGoal3.setVisibility(View.GONE);
                break;


        }

    }

    @Override
    public int getItemCount() {
        if (list.size() > 2) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(list.size())) / 3.0);
        } else {
            return 1;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvComName1,tvComName2,tvComName3;
        ImageView goal_history_img1,goal_history_img2,goal_history_img3,deleteGoal1,deleteGoal2,deleteGoal3,iv_add_goal,iv_add_goal2,iv_add_goal3,goalcompletecheckimg,goalcompletecheckimg2,goalcompletecheckimg3;
        RelativeLayout rlGoal1,rlGoal2,rlGoal3;
        ProgressBar goalProgressBar,goalProgressBar2,goalProgressBar3;
        TextView goalPointsTv,goalPointsTv2,goalPointsTv3;
        RelativeLayout progressLayout,progressLayout2,progressLayout3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvComName1 = itemView.findViewById(R.id.tvComName1);
            tvComName2 = itemView.findViewById(R.id.tvComName2);
            tvComName3 = itemView.findViewById(R.id.tvComName3);
            deleteGoal1 = itemView.findViewById(R.id.deleteGoal1);
            deleteGoal2 = itemView.findViewById(R.id.deleteGoal2);
            deleteGoal3 = itemView.findViewById(R.id.deleteGoal3);
            goal_history_img1 = itemView.findViewById(R.id.goal_history_img1);
            goal_history_img2 = itemView.findViewById(R.id.goal_history_img2);
            goal_history_img3 = itemView.findViewById(R.id.goal_history_img3);
            rlGoal1 = itemView.findViewById(R.id.rlGoal1);
            rlGoal2 = itemView.findViewById(R.id.rlGoal2);
            rlGoal3 = itemView.findViewById(R.id.rlGoal3);
            iv_add_goal = itemView.findViewById(R.id.iv_add_goal);
            iv_add_goal2 = itemView.findViewById(R.id.iv_add_goal2);
            iv_add_goal3 = itemView.findViewById(R.id.iv_add_goal3);
            goalProgressBar = itemView.findViewById(R.id.goal_progress_bar);
            goalProgressBar2 = itemView.findViewById(R.id.goal_progress_bar2);
            goalProgressBar3 = itemView.findViewById(R.id.goal_progress_bar3);
            goalPointsTv = itemView.findViewById(R.id.goal_points_tv);
            goalPointsTv2 = itemView.findViewById(R.id.goal_points_tv2);
            goalPointsTv3 = itemView.findViewById(R.id.goal_points_tv3);
            progressLayout = itemView.findViewById(R.id.progress_layout);
            progressLayout2 = itemView.findViewById(R.id.progress_layout2);
            progressLayout3 = itemView.findViewById(R.id.progress_layout3);
            goalcompletecheckimg = itemView.findViewById(R.id.goal_complete_check_img);
            goalcompletecheckimg2 = itemView.findViewById(R.id.goal_complete_check_img2);
            goalcompletecheckimg3 = itemView.findViewById(R.id.goal_complete_check_img3);
        }
    }

    private void redirectGoal(String goalName,String goalDesc){
        try{

            if(comingFrom.equalsIgnoreCase("pending")){
                switch (goalName.replace("\n","")) {
                    case "Water" :
                        context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",WATER));
                        break;

                    case "Sleep" :
                        context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",SLEEP));
                        break;

                    case "Steps" :
                        context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",STEPS));
                        break;

                    case "Calories Burn" :
                        context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",CALORIE_BURNED));
                        break;

                    case "Calories Consume" :
                        context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",CALORIE_CONSUME));
                        break;

                    case "Weight" :
                        context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",WEIGHT));
                        break;

                    case "Meditation":
                        context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",MEDITATION));
                        break;

                    case "Other":
                        if(goalDesc.contains("Winning")){
                            context.startActivity(new Intent(context, RewardsActivity.class));
                        }else{
                            context.startActivity(new Intent(context, NewDiaryActivity.class));
                        }
                        break;


                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
