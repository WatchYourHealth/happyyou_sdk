package com.wyh.happyyousdk.Goals.Adapters;

import static com.wyh.happyyousdk.utils.Constants.CALORIE;
import static com.wyh.happyyousdk.utils.Constants.MEDITATION;
import static com.wyh.happyyousdk.utils.Constants.SLEEP;
import static com.wyh.happyyousdk.utils.Constants.STEPS;
import static com.wyh.happyyousdk.utils.Constants.WATER;
import static com.wyh.happyyousdk.utils.Constants.WEIGHT;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
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
import com.wyh.happyyousdk.model.response.RecomendedGoalsData;
import com.wyh.happyyousdk.rewards.RewardsActivity;
import com.wyh.happyyousdk.trends.TrendsActivity;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;

public class RecommendedGoalAdapter extends RecyclerView.Adapter<RecommendedGoalAdapter.ViewHolder> {

    Context context;
    ArrayList<RecomendedGoalsData> list;

    public RecommendedGoalAdapter(Context context, ArrayList<RecomendedGoalsData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecommendedGoalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.individual_goal_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedGoalAdapter.ViewHolder holder, int position) {

        holder.deleteGoal1.setVisibility(View.GONE);
        holder.deleteGoal2.setVisibility(View.GONE);
        holder.deleteGoal3.setVisibility(View.GONE);
        holder.progressLayout.setVisibility(View.GONE);
        holder.progressLayout2.setVisibility(View.GONE);
        holder.progressLayout3.setVisibility(View.GONE);


        switch (position) {
            case 0 :
                for(int i=0; i < 3 ; i++){
                    if(i == 0){
                        if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_water));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));
                        }
                        holder.tvComName1.setText(list.get(i).getGoalText());
                        holder.rlGoal1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                redirectGoal(list.get(0).getGoalName());
                            }
                        });
                    }

                    if(i == 1){
                        if(list.size() > 1){
                            if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));

                            }
                            holder.tvComName2.setText(list.get(i).getGoalText());

                            holder.rlGoal2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    redirectGoal(list.get(1).getGoalName());

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
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));

                            }
                            holder.tvComName3.setText(list.get(i).getGoalText());

                            holder.rlGoal3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    redirectGoal(list.get(2).getGoalName());

                                }
                            });
                        }else{
                            holder.rlGoal3.setVisibility(View.GONE);
                        }
                    }
                }
                break;

            case 1 :
                for(int i=3; i < 6 ; i++){
                    if(i == 3){
                        if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_water));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                        }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                            holder.goal_history_img1.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));
                        }
                        holder.tvComName1.setText(list.get(i).getGoalText());

                        holder.rlGoal1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                redirectGoal(list.get(3).getGoalName());
                            }
                        });
                    }

                    if(i == 4){
                        if(list.size() > 4){
                            if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                                holder.goal_history_img2.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));

                            }
                            holder.tvComName2.setText(list.get(i).getGoalText());

                            holder.rlGoal2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    redirectGoal(list.get(4).getGoalName());

                                }
                            });
                        }else{
                            holder.rlGoal2.setVisibility(View.GONE);
                            holder.rlGoal3.setVisibility(View.GONE);
                        }
                    }

                    if(i == 5) {
                        if(list.size() > 5){
                            if(list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWATER_GOAL())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_water));


                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSLEEP_GOAL())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_sleep_star));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getSTEPS_GOAL())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_steps));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getMEDITATION())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_zen_zone));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_BURN())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getCALORIES_CONSUME())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_cal_burned));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getWEIGHT())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_ideal_weight));

                            }else if (list.get(i).getGoalName().equalsIgnoreCase(Master.INSTANCE.getOTHERS())){
                                holder.goal_history_img3.setImageDrawable(context.getDrawable(R.drawable.ic_other_document));

                            }
                            holder.tvComName3.setText(list.get(i).getGoalText());

                            holder.rlGoal3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    redirectGoal(list.get(5).getGoalName());

                                }
                            });
                        }else{
                            holder.rlGoal3.setVisibility(View.GONE);
                        }
                    }
                }
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
        ImageView goal_history_img1,goal_history_img2,goal_history_img3,deleteGoal1,deleteGoal2,deleteGoal3,iv_add_goal,iv_add_goal2,iv_add_goal3;
        RelativeLayout rlGoal1,rlGoal2,rlGoal3;
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
            progressLayout = itemView.findViewById(R.id.progress_layout);
            progressLayout2 = itemView.findViewById(R.id.progress_layout2);
            progressLayout3 = itemView.findViewById(R.id.progress_layout3);
        }

    }

    private void redirectGoal(String goalName){
        try{

            switch (goalName) {
                case "Water" :
                    context.startActivity(new Intent(context, TrendsActivity.class).putExtra("activityType",WATER));
                    break;

                case "Sleep" :
                    context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",SLEEP));
                    break;

                case "Steps" :
                    context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",STEPS));
                    break;

                case "Calories Burn" :

                case "Calories \\n\" + \"Consume" :
                    context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",CALORIE));
                    break;

                case "Weight" :
                    context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",WEIGHT));
                    break;

                case "Meditation":
                    context.startActivity(new Intent(context,TrendsActivity.class).putExtra("activityType",MEDITATION));
                    break;




            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
