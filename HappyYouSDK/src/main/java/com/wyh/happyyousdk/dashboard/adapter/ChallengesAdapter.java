package com.wyh.happyyousdk.dashboard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.ChallangesModule.Activities.ChallangesActivity;
import com.wyh.happyyousdk.ChallangesModule.Activities.NewTribeChallengeDashboard;
import com.wyh.happyyousdk.ChallangesModule.ClickInterface.ChallengeClick;
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Activities.EnrolledChallengeActivity;
import com.wyh.happyyousdk.ChallangesModule.Walkathon.Activities.WalkhatonDetailActivity;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.dashboard.DashboardHelper;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.DashboardChallengesListBinding;
import com.wyh.happyyousdk.model.response.challengesData;
import com.wyh.happyyousdk.utils.SharedPref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ViewHoler> {

    Context context;
    ArrayList<challengesData> challengesList;
    ChallengeClick click;

    public ChallengesAdapter(Context context, ArrayList<challengesData> challengesList, ChallengeClick click) {
        this.context = context;
        this.challengesList = challengesList;
        this.click = click;
    }

    @NonNull
    @Override
    public ChallengesAdapter.ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DashboardChallengesListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.dashboard_challenges_list,parent,false);
        return new ViewHoler(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengesAdapter.ViewHoler holder, @SuppressLint("RecyclerView") int position) {

        switch (position){
            case 0 :
                for (int i =0; i < 3; i++){
                    if(i == 0)  {
                        if(challengesList.get(i).getChallengePercentage() == 100){
                            holder.binding.startedTv1.setText("Completed");
                            holder.binding.challengeCompleteImg1.setVisibility(View.VISIBLE);
                        }else{
                            holder.binding.startedTv1.setText("Started");
                            holder.binding.challengeCompleteImg1.setVisibility(View.GONE);
                        }

                        if (challengesList.get(i).getChallengeType() != null && challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")){
                            holder.binding.infoIcon1.setVisibility(View.VISIBLE);
                        }else{
                            holder.binding.infoIcon1.setVisibility(View.GONE);

                        }

                        if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")) {
                            holder.binding.infoIcon1.setVisibility(View.VISIBLE);
                            holder.binding.challengeCompleteImg1.setVisibility(View.GONE);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = sdf.parse(challengesList.get(i).getChallengeStartDate());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            if (new Date().before(date)) {
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv1.setText(context.getString(R.string.enrolled));
                                } else {
                                    holder.binding.joinEnrollTv1.setText(context.getString(R.string.enroll));
                                }
                                SharedPref.putChallengeStarted(false);
                            } else {
                                SharedPref.putChallengeStarted(true);
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv1.setText("Join Now");
                                } else {
                                    holder.binding.joinEnrollTv1.setText("Join Now");

                                }
                            }

                            if(SharedPref.getChallengeStarted()){
                                if(challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(i).getIsStarted() == 1){
                                    if(challengesList.get(i).getChallengePercentage() == 100){
                                        holder.binding.challengeCompleteImg1.setVisibility(View.VISIBLE);
                                    }else{
                                        holder.binding.challengeCompleteImg1.setVisibility(View.GONE);

                                    }
                                    holder.binding.progressLayout1.setVisibility(View.VISIBLE);
                                    holder.binding.joinNowLayout1.setVisibility(View.GONE);
                                }else{
                                    holder.binding.progressLayout1.setVisibility(View.GONE);
                                    holder.binding.joinNowLayout1.setVisibility(View.VISIBLE);
                                }
                            }


                        }
                        else if(challengesList.get(i).getChallengeType().equalsIgnoreCase("Steps")){
                            if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                holder.binding.joinEnrollTv1.setText(context.getString(R.string.enrolled));
                            } else {
                                holder.binding.joinEnrollTv1.setText(context.getString(R.string.enroll));

                            }
                        }
                        else if (challengesList.get(i).getIsUserEnrolled().equals("1")) {
                            holder.binding.progressLayout1.setVisibility(View.VISIBLE);
                            holder.binding.joinNowLayout1.setVisibility(View.GONE);

                        } else {
                            holder.binding.progressLayout1.setVisibility(View.GONE);
                            holder.binding.joinNowLayout1.setVisibility(View.VISIBLE);
                            holder.binding.joinEnrollTv1.setText("Join now");
                        }

                        holder.binding.challangeNameTv1.setText(challengesList.get(i).getChallengeName());
                        holder.binding.challengeProgressBar1.setProgress(challengesList.get(i).getChallengePercentage());
                        holder.binding.challangePointsTv1.setText(challengesList.get(i).getChallengePercentage() + "%");
                        Glide.with(context).load(challengesList.get(i).getChallengeLogo()).into(holder.binding.challangeImage1);

                        holder.binding.rlChallenge1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (challengesList.get(0).getChallengeType().equalsIgnoreCase("Tribe")) {
                                    if(SharedPref.getChallengeStarted()){
                                        if(challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(0).getIsStarted() == 1){
                                            context.startActivity(new Intent(context, NewTribeChallengeDashboard.class)
                                                    .putExtra("challengeID",challengesList.get(0).getChallengeId()));
                                        }
                                    }
                                } else if(challengesList.get(0).getChallengeType().equalsIgnoreCase("Steps")){

                                    if(challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(0).getIsEventEnded() || challengesList.get(0).getIsWinnerAnnounced())){
                                        Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                    }else if(challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("0")){
                                        context.startActivity(new Intent(context, EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(0).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(0).getIsStarted()));
                                    } else if(challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(0).getIsStarted() == 0){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(0).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(0).getIsStarted()));
                                    } else if(challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(0).getIsStarted() == 1){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(0).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(0).getIsStarted()));
                                    } else if(challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(0).getIsStarted() == 1){
                                        context.startActivity(new Intent(context, WalkhatonDetailActivity.class)
                                                .putExtra("isWinnerAnnounced", challengesList.get(0).getIsWinnerAnnounced())
                                                .putExtra("isEventEnded", challengesList.get(0).getIsEventEnded()));

                                    }
                                }
                                else {
                                    click.onClickPerform(challengesList.get(0).getChallengeId(), position);
                                }
                            }
                        });



                        holder.binding.joinNowLayout1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(challengesList.get(0).getChallengeType().equalsIgnoreCase("steps")){

                                    if(challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(0).getIsEventEnded() || challengesList.get(0).getIsWinnerAnnounced())){
                                        Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                    }else if(challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(0).getIsStarted() == 0){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(0).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(0).getIsStarted()));
                                    } else if(challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(0).getIsStarted() == 0){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(0).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(0).getIsStarted()));
                                    } else if(challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(0).getIsStarted() == 1){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                 .putExtra("startDate",challengesList.get(0).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(0).getIsStarted()));

                                    } else if(challengesList.get(0).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(0).getIsStarted() == 1){
                                        context.startActivity(new Intent(context,WalkhatonDetailActivity.class)
                                                .putExtra("isWinnerAnnounced", challengesList.get(0).getIsWinnerAnnounced())
                                                .putExtra("isEventEnded", challengesList.get(0).getIsEventEnded()));
                                    }
                                }
                                else{
                                    click.joinNowClick(challengesList.get(0), challengesList.get(0).getChallengeId(), position, challengesList.get(0).getChallengeType(),0,
                                            NewDashboardHelper.binding.challengesRv);
                                }
                            }
                        });


                        holder.binding.infoIcon1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if ((challengesList.get(0).getChallengeType().equalsIgnoreCase("Tribe"))){
                                    new NewDashboardActivity().getChallengeApi(context,challengesList.get(0).getChallengeId()
                                    ,true,challengesList.get(0).getIsUserEnrolled(),challengesList.get(0).getIsStarted());
                                }
                            }
                        });
                    }
                    if(i == 1){
                        if(challengesList.size() > 1){
                            if(challengesList.get(i).getChallengePercentage() == 100){
                                holder.binding.startedTv2.setText("Completed");
                                holder.binding.challengeCompleteImg2.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.startedTv2.setText("Started");
                                holder.binding.challengeCompleteImg2.setVisibility(View.GONE);
                            }

                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")){
                                holder.binding.infoIcon2.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.infoIcon2.setVisibility(View.GONE);

                            }

                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")) {
                                holder.binding.infoIcon2.setVisibility(View.VISIBLE);
                                holder.binding.challengeCompleteImg2.setVisibility(View.GONE);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = sdf.parse(challengesList.get(i).getChallengeStartDate());
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                if (new Date().before(date)) {
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv2.setText(context.getString(R.string.enrolled));
                                    } else {
                                        holder.binding.joinEnrollTv2.setText(context.getString(R.string.enroll));
                                    }
                                    SharedPref.putChallengeStarted(false);
                                } else {
                                    SharedPref.putChallengeStarted(true);
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv2.setText("Join Now");
                                    } else {
                                        holder.binding.joinEnrollTv2.setText("Join Now");

                                    }
                                }

                                if(SharedPref.getChallengeStarted()){
                                    if(challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(i).getIsStarted() == 1){
                                        if(challengesList.get(i).getChallengePercentage() == 100){
                                            holder.binding.challengeCompleteImg2.setVisibility(View.VISIBLE);
                                        }else{
                                            holder.binding.challengeCompleteImg2.setVisibility(View.GONE);

                                        }

                                        holder.binding.progressLayout2.setVisibility(View.VISIBLE);
                                        holder.binding.joinNowLayout2.setVisibility(View.GONE);
                                    }else{
                                        holder.binding.progressLayout2.setVisibility(View.GONE);
                                        holder.binding.joinNowLayout2.setVisibility(View.VISIBLE);
                                    }
                                }


                            }
                            else if(challengesList.get(i).getChallengeType().equalsIgnoreCase("Steps")){
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv2.setText(context.getString(R.string.enrolled));
                                } else {
                                    holder.binding.joinEnrollTv2.setText(context.getString(R.string.enroll));

                                }
                            }
                            else if (challengesList.get(i).getIsUserEnrolled().equals("1")) {
                                holder.binding.progressLayout2.setVisibility(View.VISIBLE);
                                holder.binding.joinNowLayout2.setVisibility(View.GONE);

                            } else {
                                holder.binding.progressLayout2.setVisibility(View.GONE);
                                holder.binding.joinNowLayout2.setVisibility(View.VISIBLE);
                                holder.binding.joinEnrollTv2.setText("Join now");
                            }

                            holder.binding.challangeNameTv2.setText(challengesList.get(i).getChallengeName());
                            holder.binding.challengeProgressBar2.setProgress(challengesList.get(i).getChallengePercentage());
                            holder.binding.challangePointsTv2.setText(challengesList.get(i).getChallengePercentage() + "%");
                            Glide.with(context).load(challengesList.get(i).getChallengeLogo()).into(holder.binding.challangeImage2);

                            holder.binding.rlChallenges2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (challengesList.get(1).getChallengeType().equalsIgnoreCase("Tribe")) {
                                        if(SharedPref.getChallengeStarted()){
                                            if(challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("1")){
                                                //new ChallangesActivity().getChallengeDetails(context, challengesList.get(position).getChallengeId(), challengesList[position],recyclerView)
                                            }
                                        }
                                    } else if(challengesList.get(1).getChallengeType().equalsIgnoreCase("Steps")){


                                        if(challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(1).getIsEventEnded() || challengesList.get(1).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("0")){
                                            context.startActivity(new Intent(context, EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(1).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(1).getIsStarted()));
                                        } else if(challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(1).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(1).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(1).getIsStarted()));
                                        } else if(challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(1).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(1).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(1).getIsStarted()));
                                        } else if(challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(1).getIsStarted() == 1){


                                            context.startActivity(new Intent(context, WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(1).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(1).getIsEventEnded()));

                                        }
                                    }
                                    else {
                                        click.onClickPerform(challengesList.get(1).getChallengeId(), 1);
                                    }
                                }
                            });



                            holder.binding.joinNowLayout2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(challengesList.get(1).getChallengeType().equalsIgnoreCase("steps")){

                                        if(challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(1).getIsEventEnded() || challengesList.get(1).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(1).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(1).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(1).getIsStarted()));
                                        } else if(challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(1).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(1).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(1).getIsStarted()));
                                        } else if(challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(1).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(1).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(1).getIsStarted()));

                                        } else if(challengesList.get(1).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(1).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(1).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(1).getIsEventEnded()));
                                        }
                                    }
                                    else{
                                        //Join Now API
                                        click.joinNowClick(challengesList.get(1), challengesList.get(1).getChallengeId(), position, challengesList.get(1).getChallengeType(),0,
                                                NewDashboardHelper.binding.challengesRv);
                                    }
                                }
                            });


                            holder.binding.infoIcon2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if ((challengesList.get(1).getChallengeType().equalsIgnoreCase("Tribe"))){
                                        new NewDashboardActivity().getChallengeApi(context,challengesList.get(1).getChallengeId()
                                                ,true,challengesList.get(1).getIsUserEnrolled(),challengesList.get(1).getIsStarted());

                                    }
                                }
                            });
                        }
                        else{
                            holder.binding.rlChallenges2.setVisibility(View.GONE);
                            holder.binding.rlChallenges3.setVisibility(View.GONE);
                        }
                    }
                    if(i == 2){
                        if(challengesList.size() > 2){
                            if(challengesList.get(i).getChallengePercentage() == 100){
                                holder.binding.startedTv3.setText("Completed");
                                holder.binding.challengeCompleteImg3.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.startedTv3.setText("Started");
                                holder.binding.challengeCompleteImg3.setVisibility(View.GONE);
                            }

                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")){
                                holder.binding.infoIcon3.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.infoIcon3.setVisibility(View.GONE);

                            }

                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")) {
                                holder.binding.infoIcon3.setVisibility(View.VISIBLE);
                                holder.binding.challengeCompleteImg3.setVisibility(View.GONE);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = sdf.parse(challengesList.get(i).getChallengeStartDate());
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                if (new Date().before(date)) {
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv3.setText(context.getString(R.string.enrolled));
                                    } else {
                                        holder.binding.joinEnrollTv3.setText(context.getString(R.string.enroll));
                                    }
                                    SharedPref.putChallengeStarted(false);
                                } else {
                                    SharedPref.putChallengeStarted(true);
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv3.setText("Join Now");
                                    } else {
                                        holder.binding.joinEnrollTv3.setText("Join Now");

                                    }
                                }

                                if(SharedPref.getChallengeStarted()){
                                    if(challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(i).getIsStarted() == 1){
                                        if(challengesList.get(i).getChallengePercentage() == 100){
                                            holder.binding.challengeCompleteImg3.setVisibility(View.VISIBLE);
                                        }else{
                                            holder.binding.challengeCompleteImg3.setVisibility(View.GONE);

                                        }

                                        holder.binding.progressLayout3.setVisibility(View.VISIBLE);
                                        holder.binding.joinNowLayout3.setVisibility(View.GONE);
                                    }else{
                                        holder.binding.progressLayout3.setVisibility(View.GONE);
                                        holder.binding.joinNowLayout3.setVisibility(View.VISIBLE);
                                    }
                                }


                            }
                            else if(challengesList.get(i).getChallengeType().equalsIgnoreCase("Steps")){
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv3.setText(context.getString(R.string.enrolled));
                                } else {
                                    holder.binding.joinEnrollTv3.setText(context.getString(R.string.enroll));

                                }
                            }
                            else if (challengesList.get(i).getIsUserEnrolled().equals("1")) {
                                holder.binding.progressLayout3.setVisibility(View.VISIBLE);
                                holder.binding.joinNowLayout3.setVisibility(View.GONE);

                            } else {
                                holder.binding.progressLayout3.setVisibility(View.GONE);
                                holder.binding.joinNowLayout3.setVisibility(View.VISIBLE);
                                holder.binding.joinEnrollTv3.setText("Join now");
                            }

                            holder.binding.challangeNameTv3.setText(challengesList.get(i).getChallengeName());
                            holder.binding.challengeProgressBar3.setProgress(challengesList.get(i).getChallengePercentage());
                            holder.binding.challangePointsTv3.setText(challengesList.get(i).getChallengePercentage() + "%");
                            Glide.with(context).load(challengesList.get(i).getChallengeLogo()).into(holder.binding.challangeImage3);

                            holder.binding.rlChallenges3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (challengesList.get(2).getChallengeType().equalsIgnoreCase("Tribe")) {
                                        if(SharedPref.getChallengeStarted()){
                                            if(challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("1")){
                                                //new ChallangesActivity().getChallengeDetails(context, challengesList.get(position).getChallengeId(), challengesList[position],recyclerView)
                                            }
                                        }
                                    } else if(challengesList.get(2).getChallengeType().equalsIgnoreCase("Steps")){


                                        if(challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(2).getIsEventEnded() || challengesList.get(2).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("0")){
                                            context.startActivity(new Intent(context, EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(2).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(2).getIsStarted()));
                                        } else if(challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(2).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(2).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(2).getIsStarted()));
                                        } else if(challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(2).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(2).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(2).getIsStarted()));
                                        } else if(challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(2).getIsStarted() == 1){
                                            context.startActivity(new Intent(context, WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(2).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(2).getIsEventEnded()));

                                        }
                                    }
                                    else {
                                        click.onClickPerform(challengesList.get(2).getChallengeId(), 2);
                                    }
                                }
                            });



                            holder.binding.joinNowLayout3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(challengesList.get(2).getChallengeType().equalsIgnoreCase("steps")){

                                        if(challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(2).getIsEventEnded() || challengesList.get(2).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(2).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(2).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(2).getIsStarted()));
                                        } else if(challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(2).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(2).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(2).getIsStarted()));
                                        } else if(challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(2).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(2).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(2).getIsStarted()));

                                        } else if(challengesList.get(2).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(2).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(2).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(2).getIsEventEnded()));
                                        }
                                    }
                                    else{
                                        //Join Now API
                                        click.joinNowClick(challengesList.get(2), challengesList.get(2).getChallengeId(), position, challengesList.get(2).getChallengeType(),0,
                                                NewDashboardHelper.binding.challengesRv);
                                    }
                                }
                            });


                            holder.binding.infoIcon3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if ((challengesList.get(2).getChallengeType().equalsIgnoreCase("Tribe"))){
                                        new NewDashboardActivity().getChallengeApi(context,challengesList.get(2).getChallengeId()
                                                ,true,challengesList.get(2).getIsUserEnrolled(),challengesList.get(2).getIsStarted());
                                    }
                                }
                            });
                        }else{
                            holder.binding.rlChallenges3.setVisibility(View.GONE
                            );
                        }
                    }
                }
                break;

            case 1:
                for (int i =3; i < 6; i++){
                    if(i == 3)  {

                        if(challengesList.get(i).getChallengePercentage() == 100){
                            holder.binding.startedTv1.setText("Completed");
                            holder.binding.challengeCompleteImg1.setVisibility(View.VISIBLE);
                        }else{
                            holder.binding.startedTv1.setText("Started");
                            holder.binding.challengeCompleteImg1.setVisibility(View.GONE);
                        }

                        if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")){
                            holder.binding.infoIcon1.setVisibility(View.VISIBLE);
                        }else{
                            holder.binding.infoIcon1.setVisibility(View.GONE);

                        }

                        if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")) {
                            holder.binding.infoIcon1.setVisibility(View.VISIBLE);
                            holder.binding.challengeCompleteImg1.setVisibility(View.GONE);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = sdf.parse(challengesList.get(i).getChallengeStartDate());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            if (new Date().before(date)) {
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv1.setText(context.getString(R.string.enrolled));
                                } else {
                                    holder.binding.joinEnrollTv1.setText(context.getString(R.string.enroll));
                                }
                                SharedPref.putChallengeStarted(false);
                            } else {
                                SharedPref.putChallengeStarted(true);
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv1.setText("Join Now");
                                } else {
                                    holder.binding.joinEnrollTv1.setText("Join Now");

                                }
                            }

                            if(SharedPref.getChallengeStarted()){
                                if(challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(i).getIsStarted() == 1){
                                    if(challengesList.get(i).getChallengePercentage() == 100){
                                        holder.binding.challengeCompleteImg1.setVisibility(View.VISIBLE);
                                    }else{
                                        holder.binding.challengeCompleteImg1.setVisibility(View.GONE);

                                    }

                                    holder.binding.progressLayout1.setVisibility(View.VISIBLE);
                                    holder.binding.joinNowLayout1.setVisibility(View.GONE);
                                }else{
                                    holder.binding.progressLayout1.setVisibility(View.GONE);
                                    holder.binding.joinNowLayout1.setVisibility(View.VISIBLE);
                                }
                            }


                        }
                        else if(challengesList.get(i).getChallengeType().equalsIgnoreCase("Steps")){
                            if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                holder.binding.joinEnrollTv1.setText(context.getString(R.string.enrolled));
                            } else {
                                holder.binding.joinEnrollTv1.setText(context.getString(R.string.enroll));

                            }
                        }
                        else if (challengesList.get(i).getIsUserEnrolled().equals("1")) {
                            holder.binding.progressLayout1.setVisibility(View.VISIBLE);
                            holder.binding.joinNowLayout1.setVisibility(View.GONE);

                        } else {
                            holder.binding.progressLayout1.setVisibility(View.GONE);
                            holder.binding.joinNowLayout1.setVisibility(View.VISIBLE);
                            holder.binding.joinEnrollTv1.setText("Join now");
                        }

                        holder.binding.challangeNameTv1.setText(challengesList.get(i).getChallengeName());
                        holder.binding.challengeProgressBar1.setProgress(challengesList.get(i).getChallengePercentage());
                        holder.binding.challangePointsTv1.setText(challengesList.get(i).getChallengePercentage() + "%");
                        Glide.with(context).load(challengesList.get(i).getChallengeLogo()).into(holder.binding.challangeImage1);

                        holder.binding.rlChallenge1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (challengesList.get(1).getChallengeType().equalsIgnoreCase("Tribe")) {
                                    if(SharedPref.getChallengeStarted()){
                                        if(challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("1")){
                                            //new ChallangesActivity().getChallengeDetails(context, challengesList.get(position).getChallengeId(), challengesList[position],recyclerView)
                                        }
                                    }
                                } else if(challengesList.get(3).getChallengeType().equalsIgnoreCase("Steps")){


                                    if(challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(3).getIsEventEnded() || challengesList.get(3).getIsWinnerAnnounced())){
                                        Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                    }else if(challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("0")){
                                        context.startActivity(new Intent(context, EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(3).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(3).getIsStarted()));
                                    } else if(challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(3).getIsStarted() == 0){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(3).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(3).getIsStarted()));
                                    } else if(challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(3).getIsStarted() == 1){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(3).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(3).getIsStarted()));
                                    } else if(challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(3).getIsStarted() == 1){


                                        context.startActivity(new Intent(context, WalkhatonDetailActivity.class)
                                                .putExtra("isWinnerAnnounced", challengesList.get(3).getIsWinnerAnnounced())
                                                .putExtra("isEventEnded", challengesList.get(3).getIsEventEnded()));

                                    }
                                }
                                else {
                                    click.onClickPerform(challengesList.get(3).getChallengeId(), 3);
                                }
                            }
                        });



                        holder.binding.joinNowLayout1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(challengesList.get(3).getChallengeType().equalsIgnoreCase("steps")){

                                    if(challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(3).getIsEventEnded() || challengesList.get(3).getIsWinnerAnnounced())){
                                        Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                    }else if(challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(3).getIsStarted() == 0){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(3).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(3).getIsStarted()));
                                    } else if(challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(3).getIsStarted() == 0){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(3).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(3).getIsStarted()));
                                    } else if(challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(3).getIsStarted() == 1){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(3).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(3).getIsStarted()));

                                    } else if(challengesList.get(3).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(3).getIsStarted() == 1){
                                        context.startActivity(new Intent(context,WalkhatonDetailActivity.class)
                                                .putExtra("isWinnerAnnounced", challengesList.get(3).getIsWinnerAnnounced())
                                                .putExtra("isEventEnded", challengesList.get(3).getIsEventEnded()));
                                    }
                                }
                                else{
                                    //Join Now API
                                    click.joinNowClick(challengesList.get(3), challengesList.get(3).getChallengeId(), 3, challengesList.get(3).getChallengeType(),0,
                                            NewDashboardHelper.binding.challengesRv);                                }
                            }
                        });


                        holder.binding.infoIcon1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if ((challengesList.get(3).getChallengeType().equalsIgnoreCase("Tribe"))){
                                    new NewDashboardActivity().getChallengeApi(context,challengesList.get(3).getChallengeId()
                                            ,true,challengesList.get(3).getIsUserEnrolled(),challengesList.get(3).getIsStarted());
                                }
                            }
                        });
                    }
                    if(i == 4){
                        if(challengesList.size() > 4){
                            if(challengesList.get(i).getChallengePercentage() == 100){
                                holder.binding.startedTv2.setText("Completed");
                                holder.binding.challengeCompleteImg2.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.startedTv2.setText("Started");
                                holder.binding.challengeCompleteImg2.setVisibility(View.GONE);
                            }


                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")){
                                holder.binding.infoIcon2.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.infoIcon2.setVisibility(View.GONE);

                            }
                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")) {
                                holder.binding.infoIcon2.setVisibility(View.VISIBLE);
                                holder.binding.challengeCompleteImg2.setVisibility(View.GONE);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = sdf.parse(challengesList.get(i).getChallengeStartDate());
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                if (new Date().before(date)) {
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv2.setText(context.getString(R.string.enrolled));
                                    } else {
                                        holder.binding.joinEnrollTv2.setText(context.getString(R.string.enroll));
                                    }
                                    SharedPref.putChallengeStarted(false);
                                } else {
                                    SharedPref.putChallengeStarted(true);
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv2.setText("Join Now");
                                    } else {
                                        holder.binding.joinEnrollTv2.setText("Join Now");

                                    }
                                }

                                if(SharedPref.getChallengeStarted()){
                                    if(challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(i).getIsStarted() == 1){
                                        if(challengesList.get(i).getChallengePercentage() == 100){
                                            holder.binding.challengeCompleteImg2.setVisibility(View.VISIBLE);
                                        }else{
                                            holder.binding.challengeCompleteImg2.setVisibility(View.GONE);

                                        }

                                        holder.binding.progressLayout2.setVisibility(View.VISIBLE);
                                        holder.binding.joinNowLayout2.setVisibility(View.GONE);
                                    }else{
                                        holder.binding.progressLayout2.setVisibility(View.GONE);
                                        holder.binding.joinNowLayout2.setVisibility(View.VISIBLE);
                                    }
                                }


                            }
                            else if(challengesList.get(i).getChallengeType().equalsIgnoreCase("Steps")){
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv2.setText(context.getString(R.string.enrolled));
                                } else {
                                    holder.binding.joinEnrollTv2.setText(context.getString(R.string.enroll));

                                }
                            }
                            else if (challengesList.get(i).getIsUserEnrolled().equals("1")) {
                                holder.binding.progressLayout2.setVisibility(View.VISIBLE);
                                holder.binding.joinNowLayout2.setVisibility(View.GONE);

                            } else {
                                holder.binding.progressLayout2.setVisibility(View.GONE);
                                holder.binding.joinNowLayout2.setVisibility(View.VISIBLE);
                                holder.binding.joinEnrollTv2.setText("Join now");
                            }

                            holder.binding.challangeNameTv2.setText(challengesList.get(i).getChallengeName());
                            holder.binding.challengeProgressBar2.setProgress(challengesList.get(i).getChallengePercentage());
                            holder.binding.challangePointsTv2.setText(challengesList.get(i).getChallengePercentage() + "%");
                            Glide.with(context).load(challengesList.get(i).getChallengeLogo()).into(holder.binding.challangeImage2);

                            holder.binding.rlChallenges2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (challengesList.get(4).getChallengeType().equalsIgnoreCase("Tribe")) {
                                        if(SharedPref.getChallengeStarted()){
                                            if(challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("1")){
                                                //new ChallangesActivity().getChallengeDetails(context, challengesList.get(position).getChallengeId(), challengesList[position],recyclerView)
                                            }
                                        }
                                    } else if(challengesList.get(4).getChallengeType().equalsIgnoreCase("Steps")){


                                        if(challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(4).getIsEventEnded() || challengesList.get(4).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("0")){
                                            context.startActivity(new Intent(context, EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(4).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(4).getIsStarted()));
                                        } else if(challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(4).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(4).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(4).getIsStarted()));
                                        } else if(challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(4).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(4).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(4).getIsStarted()));
                                        } else if(challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(4).getIsStarted() == 1){


                                            context.startActivity(new Intent(context, WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(4).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(4).getIsEventEnded()));

                                        }
                                    }
                                    else {
                                        click.onClickPerform(challengesList.get(4).getChallengeId(), 4);
                                    }
                                }
                            });



                            holder.binding.joinNowLayout2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(challengesList.get(4).getChallengeType().equalsIgnoreCase("steps")){

                                        if(challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(4).getIsEventEnded() || challengesList.get(4).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(4).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(4).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(4).getIsStarted()));
                                        } else if(challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(4).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(4).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(4).getIsStarted()));
                                        } else if(challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(4).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(4).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(4).getIsStarted()));

                                        } else if(challengesList.get(4).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(4).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(4).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(4).getIsEventEnded()));
                                        }
                                    }
                                    else{
                                        //Join Now API
                                        click.joinNowClick(challengesList.get(4), challengesList.get(4).getChallengeId(), 4, challengesList.get(4).getChallengeType(),0,
                                                NewDashboardHelper.binding.challengesRv);
                                    }
                                }
                            });


                            holder.binding.infoIcon2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if ((challengesList.get(4).getChallengeType().equalsIgnoreCase("Tribe"))){
                                        new NewDashboardActivity().getChallengeApi(context,challengesList.get(4).getChallengeId()
                                                ,true,challengesList.get(4).getIsUserEnrolled(),challengesList.get(4).getIsStarted());
                                    }
                                }
                            });
                        }
                        else{
                            holder.binding.rlChallenges2.setVisibility(View.GONE);
                            holder.binding.rlChallenges3.setVisibility(View.GONE);
                        }
                    }
                    if(i == 5){
                        if(challengesList.size() > 5){
                            if(challengesList.get(i).getChallengePercentage() == 100){
                                holder.binding.startedTv3.setText("Completed");
                                holder.binding.challengeCompleteImg3.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.startedTv3.setText("Started");
                                holder.binding.challengeCompleteImg3.setVisibility(View.GONE);
                            }

                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")){
                                holder.binding.infoIcon3.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.infoIcon3.setVisibility(View.GONE);

                            }
                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")) {
                                holder.binding.infoIcon3.setVisibility(View.VISIBLE);
                                holder.binding.challengeCompleteImg3.setVisibility(View.GONE);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = sdf.parse(challengesList.get(i).getChallengeStartDate());
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                if (new Date().before(date)) {
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv3.setText(context.getString(R.string.enrolled));
                                    } else {
                                        holder.binding.joinEnrollTv3.setText(context.getString(R.string.enroll));
                                    }
                                    SharedPref.putChallengeStarted(false);
                                } else {
                                    SharedPref.putChallengeStarted(true);
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv3.setText("Join Now");
                                    } else {
                                        holder.binding.joinEnrollTv3.setText("Join Now");

                                    }
                                }

                                if(SharedPref.getChallengeStarted()){
                                    if(challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(i).getIsStarted() == 1){
                                        if(challengesList.get(i).getChallengePercentage() == 100){
                                            holder.binding.challengeCompleteImg3.setVisibility(View.VISIBLE);
                                        }else{
                                            holder.binding.challengeCompleteImg3.setVisibility(View.GONE);

                                        }

                                        holder.binding.progressLayout3.setVisibility(View.VISIBLE);
                                        holder.binding.joinNowLayout3.setVisibility(View.GONE);
                                    }else{
                                        holder.binding.progressLayout3.setVisibility(View.GONE);
                                        holder.binding.joinNowLayout3.setVisibility(View.VISIBLE);
                                    }
                                }


                            }
                            else if(challengesList.get(i).getChallengeType().equalsIgnoreCase("Steps")){
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv3.setText(context.getString(R.string.enrolled));
                                } else {
                                    holder.binding.joinEnrollTv3.setText(context.getString(R.string.enroll));

                                }
                            }
                            else if (challengesList.get(i).getIsUserEnrolled().equals("1")) {
                                holder.binding.progressLayout3.setVisibility(View.VISIBLE);
                                holder.binding.joinNowLayout3.setVisibility(View.GONE);

                            } else {
                                holder.binding.progressLayout3.setVisibility(View.GONE);
                                holder.binding.joinNowLayout3.setVisibility(View.VISIBLE);
                                holder.binding.joinEnrollTv3.setText("Join now");
                            }

                            holder.binding.challangeNameTv3.setText(challengesList.get(i).getChallengeName());
                            holder.binding.challengeProgressBar3.setProgress(challengesList.get(i).getChallengePercentage());
                            holder.binding.challangePointsTv3.setText(challengesList.get(i).getChallengePercentage() + "%");
                            Glide.with(context).load(challengesList.get(i).getChallengeLogo()).into(holder.binding.challangeImage3);

                            holder.binding.rlChallenges3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (challengesList.get(5).getChallengeType().equalsIgnoreCase("Tribe")) {
                                        if(SharedPref.getChallengeStarted()){
                                            if(challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("1")){
                                                //new ChallangesActivity().getChallengeDetails(context, challengesList.get(position).getChallengeId(), challengesList[position],recyclerView)
                                            }
                                        }
                                    } else if(challengesList.get(5).getChallengeType().equalsIgnoreCase("Steps")){


                                        if(challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(5).getIsEventEnded() || challengesList.get(5).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("0")){
                                            context.startActivity(new Intent(context, EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(5).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(5).getIsStarted()));
                                        } else if(challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(5).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(5).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(5).getIsStarted()));
                                        } else if(challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(5).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(5).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(5).getIsStarted()));
                                        } else if(challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(5).getIsStarted() == 1){
                                            context.startActivity(new Intent(context, WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(5).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(5).getIsEventEnded()));

                                        }
                                    }
                                    else {
                                        click.onClickPerform(challengesList.get(5).getChallengeId(), 5);
                                    }
                                }
                            });



                            holder.binding.joinNowLayout3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(challengesList.get(5).getChallengeType().equalsIgnoreCase("steps")){

                                        if(challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(5).getIsEventEnded() || challengesList.get(5).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(5).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(5).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(5).getIsStarted()));
                                        } else if(challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(5).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(5).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(5).getIsStarted()));
                                        } else if(challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(5).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(5).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(5).getIsStarted()));

                                        } else if(challengesList.get(5).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(5).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(5).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(5).getIsEventEnded()));
                                        }
                                    }
                                    else{
                                        //Join Now API
                                        click.joinNowClick(challengesList.get(5), challengesList.get(5).getChallengeId(), 5, challengesList.get(5).getChallengeType(),0,
                                                NewDashboardHelper.binding.challengesRv);
                                        //click.joinNowClick(challengesList[position], challengesList[position].ChallengeId, position, challengesList.get(position).ChallengeType,0, recyclerView)
                                    }
                                }
                            });


                            holder.binding.infoIcon3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if ((challengesList.get(position).getChallengeType().equalsIgnoreCase("Tribe"))){
                                        new NewDashboardActivity().getChallengeApi(context,challengesList.get(5).getChallengeId()
                                                ,true,challengesList.get(5).getIsUserEnrolled(),challengesList.get(5).getIsStarted());
                                    }
                                }
                            });
                        }else{
                            holder.binding.rlChallenges3.setVisibility(View.GONE
                            );
                        }
                    }
                }
                break;

            case 2:
                for (int i =6; i < 9; i++){
                    if(i == 6)  {
                        if(challengesList.get(i).getChallengePercentage() == 100){
                            holder.binding.startedTv1.setText("Completed");
                            holder.binding.challengeCompleteImg1.setVisibility(View.VISIBLE);
                        }else{
                            holder.binding.startedTv1.setText("Started");
                            holder.binding.challengeCompleteImg1.setVisibility(View.GONE);
                        }

                        if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")){
                            holder.binding.infoIcon1.setVisibility(View.VISIBLE);
                        }else{
                            holder.binding.infoIcon1.setVisibility(View.GONE);

                        }

                        if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")) {
                            holder.binding.infoIcon1.setVisibility(View.VISIBLE);
                            holder.binding.challengeCompleteImg1.setVisibility(View.GONE);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = sdf.parse(challengesList.get(i).getChallengeStartDate());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            if (new Date().before(date)) {
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv1.setText(context.getString(R.string.enrolled));
                                } else {
                                    holder.binding.joinEnrollTv1.setText(context.getString(R.string.enroll));
                                }
                                SharedPref.putChallengeStarted(false);
                            } else {
                                SharedPref.putChallengeStarted(true);
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv1.setText("Join Now");
                                } else {
                                    holder.binding.joinEnrollTv1.setText("Join Now");

                                }
                            }

                            if(SharedPref.getChallengeStarted()){
                                if(challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(i).getIsStarted() == 1){
                                    if(challengesList.get(i).getChallengePercentage() == 100){
                                        holder.binding.challengeCompleteImg1.setVisibility(View.VISIBLE);
                                    }else{
                                        holder.binding.challengeCompleteImg1.setVisibility(View.GONE);

                                    }

                                    holder.binding.progressLayout1.setVisibility(View.VISIBLE);
                                    holder.binding.joinNowLayout1.setVisibility(View.GONE);
                                }else{
                                    holder.binding.progressLayout1.setVisibility(View.GONE);
                                    holder.binding.joinNowLayout1.setVisibility(View.VISIBLE);
                                }
                            }


                        }
                        else if(challengesList.get(i).getChallengeType().equalsIgnoreCase("Steps")){
                            if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                holder.binding.joinEnrollTv1.setText(context.getString(R.string.enrolled));
                            } else {
                                holder.binding.joinEnrollTv1.setText(context.getString(R.string.enroll));

                            }
                        }
                        else if (challengesList.get(i).getIsUserEnrolled().equals("1")) {
                            holder.binding.progressLayout1.setVisibility(View.VISIBLE);
                            holder.binding.joinNowLayout1.setVisibility(View.GONE);

                        } else {
                            holder.binding.progressLayout1.setVisibility(View.GONE);
                            holder.binding.joinNowLayout1.setVisibility(View.VISIBLE);
                            holder.binding.joinEnrollTv1.setText("Join now");
                        }

                        holder.binding.challangeNameTv1.setText(challengesList.get(i).getChallengeName());
                        holder.binding.challengeProgressBar1.setProgress(challengesList.get(i).getChallengePercentage());
                        holder.binding.challangePointsTv1.setText(challengesList.get(i).getChallengePercentage() + "%");
                        Glide.with(context).load(challengesList.get(i).getChallengeLogo()).into(holder.binding.challangeImage1);

                        holder.binding.rlChallenge1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (challengesList.get(6).getChallengeType().equalsIgnoreCase("Tribe")) {
                                    if(SharedPref.getChallengeStarted()){
                                        if(challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("1")){
                                            //new ChallangesActivity().getChallengeDetails(context, challengesList.get(position).getChallengeId(), challengesList[position],recyclerView)
                                        }
                                    }
                                } else if(challengesList.get(6).getChallengeType().equalsIgnoreCase("Steps")){


                                    if(challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(6).getIsEventEnded() || challengesList.get(6).getIsWinnerAnnounced())){
                                        Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                    }else if(challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("0")){
                                        context.startActivity(new Intent(context, EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(6).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(6).getIsStarted()));
                                    } else if(challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(6).getIsStarted() == 0){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(6).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(6).getIsStarted()));
                                    } else if(challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(6).getIsStarted() == 1){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(6).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(6).getIsStarted()));
                                    } else if(challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(6).getIsStarted() == 1){

                                        context.startActivity(new Intent(context, WalkhatonDetailActivity.class)
                                                .putExtra("isWinnerAnnounced", challengesList.get(6).getIsWinnerAnnounced())
                                                .putExtra("isEventEnded", challengesList.get(6).getIsEventEnded()));

                                    }
                                }
                                else {
                                    click.onClickPerform(challengesList.get(6).getChallengeId(), 6);
                                }
                            }
                        });



                        holder.binding.joinNowLayout1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(challengesList.get(6).getChallengeType().equalsIgnoreCase("steps")){

                                    if(challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(6).getIsEventEnded() || challengesList.get(6).getIsWinnerAnnounced())){
                                        Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                    }else if(challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(6).getIsStarted() == 0){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(6).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(6).getIsStarted()));
                                    } else if(challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(6).getIsStarted() == 0){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(6).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(6).getIsStarted()));
                                    } else if(challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(6).getIsStarted() == 1){
                                        context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                .putExtra("startDate",challengesList.get(6).getChallengeStartDate())
                                                .putExtra("isStarted",challengesList.get(6).getIsStarted()));

                                    } else if(challengesList.get(6).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(6).getIsStarted() == 1){
                                        context.startActivity(new Intent(context,WalkhatonDetailActivity.class)
                                                .putExtra("isWinnerAnnounced", challengesList.get(6).getIsWinnerAnnounced())
                                                .putExtra("isEventEnded", challengesList.get(6).getIsEventEnded()));
                                    }
                                }
                                else{
                                    //Join Now API
                                    click.joinNowClick(challengesList.get(6), challengesList.get(6).getChallengeId(), 6, challengesList.get(6).getChallengeType(),0,
                                            NewDashboardHelper.binding.challengesRv);
                                    //click.joinNowClick(challengesList[position], challengesList[position].ChallengeId, position, challengesList.get(position).ChallengeType,0, recyclerView)
                                }
                            }
                        });


                        holder.binding.infoIcon1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if ((challengesList.get(6).getChallengeType().equalsIgnoreCase("Tribe"))){
                                    new NewDashboardActivity().getChallengeApi(context,challengesList.get(6).getChallengeId()
                                            ,true,challengesList.get(6).getIsUserEnrolled(),challengesList.get(6).getIsStarted());
                                }
                            }
                        });
                    }
                    if(i == 7){
                        if(challengesList.size() > 7){
                            if(challengesList.get(i).getChallengePercentage() == 100){
                                holder.binding.startedTv2.setText("Completed");
                                holder.binding.challengeCompleteImg2.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.startedTv2.setText("Started");
                                holder.binding.challengeCompleteImg2.setVisibility(View.GONE);
                            }

                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")){
                                holder.binding.infoIcon2.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.infoIcon2.setVisibility(View.GONE);

                            }
                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")) {
                                holder.binding.infoIcon2.setVisibility(View.VISIBLE);
                                holder.binding.challengeCompleteImg2.setVisibility(View.GONE);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = sdf.parse(challengesList.get(i).getChallengeStartDate());
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                if (new Date().before(date)) {
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv2.setText(context.getString(R.string.enrolled));
                                    } else {
                                        holder.binding.joinEnrollTv2.setText(context.getString(R.string.enroll));
                                    }
                                    SharedPref.putChallengeStarted(false);
                                } else {
                                    SharedPref.putChallengeStarted(true);
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv2.setText("Join Now");
                                    } else {
                                        holder.binding.joinEnrollTv2.setText("Join Now");

                                    }
                                }

                                if(SharedPref.getChallengeStarted()){
                                    if(challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(i).getIsStarted() == 1){
                                        if(challengesList.get(i).getChallengePercentage() == 100){
                                            holder.binding.challengeCompleteImg2.setVisibility(View.VISIBLE);
                                        }else{
                                            holder.binding.challengeCompleteImg2.setVisibility(View.GONE);

                                        }

                                        holder.binding.progressLayout2.setVisibility(View.VISIBLE);
                                        holder.binding.joinNowLayout2.setVisibility(View.GONE);
                                    }else{
                                        holder.binding.progressLayout2.setVisibility(View.GONE);
                                        holder.binding.joinNowLayout2.setVisibility(View.VISIBLE);
                                    }
                                }


                            }
                            else if(challengesList.get(i).getChallengeType().equalsIgnoreCase("Steps")){
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv2.setText(context.getString(R.string.enrolled));
                                } else {
                                    holder.binding.joinEnrollTv2.setText(context.getString(R.string.enroll));

                                }
                            }
                            else if (challengesList.get(i).getIsUserEnrolled().equals("1")) {
                                holder.binding.progressLayout2.setVisibility(View.VISIBLE);
                                holder.binding.joinNowLayout2.setVisibility(View.GONE);

                            } else {
                                holder.binding.progressLayout2.setVisibility(View.GONE);
                                holder.binding.joinNowLayout2.setVisibility(View.VISIBLE);
                                holder.binding.joinEnrollTv2.setText("Join now");
                            }

                            holder.binding.challangeNameTv2.setText(challengesList.get(i).getChallengeName());
                            holder.binding.challengeProgressBar2.setProgress(challengesList.get(i).getChallengePercentage());
                            holder.binding.challangePointsTv2.setText(challengesList.get(i).getChallengePercentage() + "%");
                            Glide.with(context).load(challengesList.get(i).getChallengeLogo()).into(holder.binding.challangeImage2);

                            holder.binding.rlChallenges2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (challengesList.get(7).getChallengeType().equalsIgnoreCase("Tribe")) {
                                        if(SharedPref.getChallengeStarted()){
                                            if(challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("1")){
                                                //new ChallangesActivity().getChallengeDetails(context, challengesList.get(position).getChallengeId(), challengesList[position],recyclerView)
                                            }
                                        }
                                    } else if(challengesList.get(7).getChallengeType().equalsIgnoreCase("Steps")){


                                        if(challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(7).getIsEventEnded() || challengesList.get(7).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("0")){
                                            context.startActivity(new Intent(context, EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(7).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(7).getIsStarted()));
                                        } else if(challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(7).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(7).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(7).getIsStarted()));
                                        } else if(challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(7).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(7).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(7).getIsStarted()));
                                        } else if(challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(7).getIsStarted() == 1){


                                            context.startActivity(new Intent(context, WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(7).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(7).getIsEventEnded()));

                                        }
                                    }
                                    else {
                                        click.onClickPerform(challengesList.get(7).getChallengeId(), 7);
                                    }
                                }
                            });



                            holder.binding.joinNowLayout2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(challengesList.get(7).getChallengeType().equalsIgnoreCase("steps")){

                                        if(challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(7).getIsEventEnded() || challengesList.get(7).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(7).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(7).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(7).getIsStarted()));
                                        } else if(challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(7).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(7).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(7).getIsStarted()));
                                        } else if(challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(7).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(7).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(7).getIsStarted()));

                                        } else if(challengesList.get(7).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(7).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(7).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(7).getIsEventEnded()));
                                        }
                                    }
                                    else{
                                        //Join Now API
                                        click.joinNowClick(challengesList.get(7), challengesList.get(7).getChallengeId(), 7, challengesList.get(7).getChallengeType(),0,
                                                NewDashboardHelper.binding.challengesRv);
                                        //click.joinNowClick(challengesList[position], challengesList[position].ChallengeId, position, challengesList.get(position).ChallengeType,0, recyclerView)
                                    }
                                }
                            });


                            holder.binding.infoIcon2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if ((challengesList.get(7).getChallengeType().equalsIgnoreCase("Tribe"))){
                                        new NewDashboardActivity().getChallengeApi(context,challengesList.get(7).getChallengeId()
                                                ,true,challengesList.get(7).getIsUserEnrolled(),challengesList.get(7).getIsStarted());
                                    }
                                }
                            });
                        }
                        else{
                            holder.binding.rlChallenges2.setVisibility(View.GONE);
                            holder.binding.rlChallenges3.setVisibility(View.GONE);
                        }
                    }
                    if(i == 8){
                        if(challengesList.size() > 8){
                            if(challengesList.get(i).getChallengePercentage() == 100){
                                holder.binding.startedTv3.setText("Completed");
                                holder.binding.challengeCompleteImg3.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.startedTv3.setText("Started");
                                holder.binding.challengeCompleteImg3.setVisibility(View.GONE);
                            }

                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")){
                                holder.binding.infoIcon3.setVisibility(View.VISIBLE);
                            }else{
                                holder.binding.infoIcon3.setVisibility(View.GONE);

                            }

                            if (challengesList.get(i).getChallengeType().equalsIgnoreCase("Tribe")) {
                                holder.binding.infoIcon3.setVisibility(View.VISIBLE);
                                holder.binding.challengeCompleteImg3.setVisibility(View.GONE);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = null;
                                try {
                                    date = sdf.parse(challengesList.get(i).getChallengeStartDate());
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                if (new Date().before(date)) {
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv3.setText(context.getString(R.string.enrolled));
                                    } else {
                                        holder.binding.joinEnrollTv3.setText(context.getString(R.string.enroll));
                                    }
                                    SharedPref.putChallengeStarted(false);
                                } else {
                                    SharedPref.putChallengeStarted(true);
                                    if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                        holder.binding.joinEnrollTv3.setText("Join Now");
                                    } else {
                                        holder.binding.joinEnrollTv3.setText("Join Now");

                                    }
                                }

                                if(SharedPref.getChallengeStarted()){
                                    if(challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(i).getIsStarted() == 1){
                                        if(challengesList.get(i).getChallengePercentage() == 100){
                                            holder.binding.challengeCompleteImg3.setVisibility(View.VISIBLE);
                                        }else{
                                            holder.binding.challengeCompleteImg3.setVisibility(View.GONE);

                                        }

                                        holder.binding.progressLayout3.setVisibility(View.VISIBLE);
                                        holder.binding.joinNowLayout3.setVisibility(View.GONE);
                                    }else{
                                        holder.binding.progressLayout3.setVisibility(View.GONE);
                                        holder.binding.joinNowLayout3.setVisibility(View.VISIBLE);
                                    }
                                }


                            }
                            else if(challengesList.get(i).getChallengeType().equalsIgnoreCase("Steps")){
                                if (challengesList.get(i).getIsUserEnrolled().equalsIgnoreCase("1")) {
                                    holder.binding.joinEnrollTv3.setText(context.getString(R.string.enrolled));
                                } else {
                                    holder.binding.joinEnrollTv3.setText(context.getString(R.string.enroll));

                                }
                            }
                            else if (challengesList.get(i).getIsUserEnrolled().equals("1")) {
                                holder.binding.progressLayout3.setVisibility(View.VISIBLE);
                                holder.binding.joinNowLayout3.setVisibility(View.GONE);

                            } else {
                                holder.binding.progressLayout3.setVisibility(View.GONE);
                                holder.binding.joinNowLayout3.setVisibility(View.VISIBLE);
                                holder.binding.joinEnrollTv3.setText("Join now");
                            }

                            holder.binding.challangeNameTv3.setText(challengesList.get(i).getChallengeName());
                            holder.binding.challengeProgressBar3.setProgress(challengesList.get(i).getChallengePercentage());
                            holder.binding.challangePointsTv3.setText(challengesList.get(i).getChallengePercentage() + "%");
                            Glide.with(context).load(challengesList.get(i).getChallengeLogo()).into(holder.binding.challangeImage3);

                            holder.binding.rlChallenges3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (challengesList.get(8).getChallengeType().equalsIgnoreCase("Tribe")) {
                                        if(SharedPref.getChallengeStarted()){
                                            if(challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("1")){
                                                //new ChallangesActivity().getChallengeDetails(context, challengesList.get(position).getChallengeId(), challengesList[position],recyclerView)
                                            }
                                        }
                                    } else if(challengesList.get(8).getChallengeType().equalsIgnoreCase("Steps")){


                                        if(challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(8).getIsEventEnded() || challengesList.get(8).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("0")){
                                            context.startActivity(new Intent(context, EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(8).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(8).getIsStarted()));
                                        } else if(challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(8).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(8).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(8).getIsStarted()));
                                        } else if(challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(8).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(8).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(8).getIsStarted()));
                                        } else if(challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(8).getIsStarted() == 1){
                                            context.startActivity(new Intent(context, WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(8).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(8).getIsEventEnded()));

                                        }
                                    }
                                    else {
                                        click.onClickPerform(challengesList.get(8).getChallengeId(), 8);
                                    }
                                }
                            });



                            holder.binding.joinNowLayout3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(challengesList.get(8).getChallengeType().equalsIgnoreCase("steps")){

                                        if(challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("0") && (challengesList.get(8).getIsEventEnded() || challengesList.get(8).getIsWinnerAnnounced())){
                                            Toast.makeText(context, "Event is already ended.", Toast.LENGTH_LONG).show();
                                        }else if(challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(8).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(8).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(8).getIsStarted()));
                                        } else if(challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(8).getIsStarted() == 0){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(8).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(8).getIsStarted()));
                                        } else if(challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("0") && challengesList.get(8).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,EnrolledChallengeActivity.class)
                                                    .putExtra("startDate",challengesList.get(8).getChallengeStartDate())
                                                    .putExtra("isStarted",challengesList.get(8).getIsStarted()));

                                        } else if(challengesList.get(8).getIsUserEnrolled().equalsIgnoreCase("1") && challengesList.get(8).getIsStarted() == 1){
                                            context.startActivity(new Intent(context,WalkhatonDetailActivity.class)
                                                    .putExtra("isWinnerAnnounced", challengesList.get(8).getIsWinnerAnnounced())
                                                    .putExtra("isEventEnded", challengesList.get(8).getIsEventEnded()));
                                        }
                                    }
                                    else{
                                        //Join Now API
                                        click.joinNowClick(challengesList.get(8), challengesList.get(8).getChallengeId(), 8, challengesList.get(8).getChallengeType(),0,
                                                NewDashboardHelper.binding.challengesRv);
                                        //click.joinNowClick(challengesList[position], challengesList[position].ChallengeId, position, challengesList.get(position).ChallengeType,0, recyclerView)
                                    }
                                }
                            });


                            holder.binding.infoIcon3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if ((challengesList.get(8).getChallengeType().equalsIgnoreCase("Tribe"))){
                                        new NewDashboardActivity().getChallengeApi(context,challengesList.get(8).getChallengeId()
                                                ,true,challengesList.get(8).getIsUserEnrolled(),challengesList.get(8).getIsStarted());
                                    }
                                }
                            });
                        }else{
                            holder.binding.rlChallenges3.setVisibility(View.GONE
                            );
                        }
                    }
                }
                break;

            case 3:
                holder.binding.rlChallenge1.setVisibility(View.GONE);
                holder.binding.rlChallenges2.setVisibility(View.GONE);
                holder.binding.rlChallenges3.setVisibility(View.GONE);
                break;

        }

    }

    @Override
    public int getItemCount() {
        if (challengesList.size() > 2) {
            return (int) Math.ceil(Double.parseDouble(String.valueOf(challengesList.size())) / 3.0);
        } else {
            return 1;
        }
    }

    public class ViewHoler extends RecyclerView.ViewHolder {

        DashboardChallengesListBinding binding;

        public ViewHoler(@NonNull DashboardChallengesListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
