package com.wyh.happyyousdk.quizathon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.ActivityClaimReclaimList;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.dashboard.SearchActivity;
import com.wyh.happyyousdk.databinding.DialogClaimReclaimBinding;
import com.wyh.happyyousdk.model.response.playwin.ClaimReClaimRewardModel;
import com.wyh.happyyousdk.quizathon.QuizClaimActivityRewardList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClaimReClaimDialog extends Dialog {
    DialogClaimReclaimBinding binding;

    public ClaimReClaimDialog(@NonNull Context context) {
        super(context);
    }

    public ClaimReClaimDialog(@NonNull Context context, String TransId, String BurnValue, String title, String icon, String message, boolean isClaim, ClaimReClaimRewardModel claimReClaimRewardModel, onRewardClick rewardClick) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_claim_reclaim, null, false);
        setContentView(binding.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        if (isClaim) {
            binding.tvButtonClaim.setText("Claim");
        } else {
            binding.tvButtonClaim.setText("Reclaim");
        }
        if (message != null && !message.isEmpty()) {
            message = message.replaceAll("\\\\n", "\n");
        }
        binding.tvButtonClose.setText("Close");
        binding.lblRetakeTitle.setText(title);
        binding.tvTitle.setText(claimReClaimRewardModel.getQuizDesc());
        binding.tvRetakeMessageTop.setText(message != null ? message : "");
        Glide.with(context).load(icon).into(binding.ivRetakeQuiz);

        if (!isClaim) {
            if (BurnValue != null && !BurnValue.isEmpty() && !BurnValue.equalsIgnoreCase("0")) {
                binding.tvRetakeBurnValue.setVisibility(View.VISIBLE);
                binding.tvRetakeBurnValue.setText(BurnValue + " Points");
            } else {
                binding.tvRetakeBurnValue.setVisibility(View.GONE);
            }
        }

        if (claimReClaimRewardModel.getExpireOn() != null && !claimReClaimRewardModel.getExpireOn().isEmpty()) {
            binding.mcvExpiredHrs.setVisibility(View.VISIBLE);
            startCountdownToDate(claimReClaimRewardModel.getExpireOn(), binding.tvExpiredHours);
        } else {
            binding.mcvExpiredHrs.setVisibility(View.GONE);
        }

        binding.mcvRewardClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (context instanceof NewDashboardActivity) {
                    if (claimReClaimRewardModel.isShowActivity()) {
                        rewardClick.onRewardClick(claimReClaimRewardModel, "Activity");
                    } else {
                        ((NewDashboardActivity) context).ClaimRewardById(TransId, isClaim, !isClaim, BurnValue, claimReClaimRewardModel);
                    }
                } else if (context instanceof QuizClaimActivityRewardList) {
                    if (claimReClaimRewardModel.isShowActivity()) {
                        rewardClick.onRewardClick(claimReClaimRewardModel, "Activity");
                    } else {
                        ((QuizClaimActivityRewardList) context).ClaimRewardById(TransId, isClaim, !isClaim, BurnValue, claimReClaimRewardModel);
                    }
                } else if (context instanceof ActivityClaimReclaimList) {
                    if (claimReClaimRewardModel.isShowActivity()) {
                        rewardClick.onRewardClick(claimReClaimRewardModel, "Activity");
                    } else {
                        ((ActivityClaimReclaimList) context).ClaimRewardById(TransId, isClaim, !isClaim, BurnValue, claimReClaimRewardModel);
                    }
                }

            }
        });
        binding.mcvRewardClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (context instanceof NewDashboardActivity) {
                            ((NewDashboardActivity) context).removeClaimDialog(claimReClaimRewardModel);
                        }
                    }
                }, 200);

            }
        });

    }

    public void startCountdownToDate(String targetDate, TextView view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        try {
            // Parse the target date
            Date endDate = dateFormat.parse(targetDate);
            if (endDate == null) throw new ParseException("Invalid date", 0);

            long targetTimeMillis = endDate.getTime();
            long currentTimeMillis = System.currentTimeMillis();

            // Calculate the countdown duration
            long countdownDuration = targetTimeMillis - currentTimeMillis;

            if (countdownDuration > 0) {
                new CountDownTimer(countdownDuration, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        long totalSeconds = millisUntilFinished / 1000;
                        long days = totalSeconds / (24 * 60 * 60);
                        long hours = (totalSeconds / 3600) % 24;
                        long minutes = (totalSeconds / 60) % 60;
                        long seconds = totalSeconds % 60;

                        String timeRemaining;

//                        if (days > 2) {
//                            timeRemaining = String.format(Locale.getDefault(), "%dD %02d:%02d:%02d", days, hours, minutes, seconds);
//                        } else {
//                            long totalHours = totalSeconds / 3600;
//                            timeRemaining = String.format(Locale.getDefault(), "%02d:%02d:%02d", totalHours, minutes, seconds);
//                        }
                        if (days > 2) {
                            // Example: 12d 5h 35m 16s
                            timeRemaining = String.format(
                                    Locale.getDefault(),
                                    "%dd %dh %dm %ds",
                                    days, hours, minutes, seconds
                            );
                        } else {
                            long totalHours = totalSeconds / 3600;
                            // Example: 53h 35m 16s
                            timeRemaining = String.format(
                                    Locale.getDefault(),
                                    "%dh %dm %ds",
                                    totalHours, minutes, seconds
                            );
                        }

                        view.setText(timeRemaining);
                    }

                    @Override
                    public void onFinish() {
                        view.setText("Expired!");
                    }

                }.start();
            } else {
                view.setText("Expired!");
            }

        } catch (ParseException e) {
            view.setText("Invalid date format");
            e.printStackTrace();
        }
    }
}
