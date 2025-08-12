package com.wyh.happyyousdk.SpinWheel.Utilities;


import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.SpinWheel.Activities.spinwheelreward.RewardsListActivity;
import com.wyh.happyyousdk.SpinWheel.Activities.spinwheelreward.SpinWheelRewardsActivity;
import com.wyh.happyyousdk.dashboard.helper.NewDashboardHelper;
import com.wyh.happyyousdk.databinding.DialogRewardsInfoBinding;
import com.wyh.happyyousdk.model.response.postloginreward.RewardItem;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.dialog.PostSpinDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RewardInfoBottomSheet extends BottomSheetDialogFragment {
    RewardItem reward;
    DialogRewardsInfoBinding binding;
    int[] rewardcolors = new int[]{0xFFC6B416, 0xFFE0CC15, 0xFFE7EB24};
    int[] expiredcolors = new int[]{0xFFFF9F80, 0xFFD10B28};
    onRewardDialogClick onRewardDialogClick;
    Context context;
    String rewardStatus = "";
    boolean isFromRewardList = false;
    boolean isFromQuiz = true;
    String comingFrom = "Quiz";

    String messageNote = "";

    public String getMessageNote() {
        return messageNote;
    }

    public void setMessageNote(String messageNote) {
        this.messageNote = messageNote;
    }

    public String getComingFrom() {
        return comingFrom;
    }

    public void setComingFrom(String comingFrom) {
        if (comingFrom.equalsIgnoreCase(""))
            comingFrom = "Quiz";
        this.comingFrom = comingFrom;
    }

    public boolean isFromRewardList() {
        return isFromRewardList;
    }

    public void setFromRewardList(boolean fromRewardList) {
        isFromRewardList = fromRewardList;
    }

    public RewardInfoBottomSheet(Context context, RewardItem reward, String rewardStatus, onRewardDialogClick onRewardDialogClick) {
        setCancelable(true);
        this.context = context;
        this.reward = reward;
        this.rewardStatus = rewardStatus;
        this.onRewardDialogClick = onRewardDialogClick;

        if (context instanceof SpinWheelRewardsActivity || context instanceof RewardsListActivity) {
            isFromQuiz = false;
        }
        setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set the BottomSheetDialog to expanded state
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (dialog != null) {
            View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_rewards_info, container, false);
        if (!isFromRewardList) {
            Glide.with(context).load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + "reward_celebration.gif")
                    .into(binding.ivCelebration);
            binding.ivCelebration.setVisibility(View.VISIBLE);
            startRewardCelebrationHandler();
        }

        if (reward != null && reward.getRewardType() != null && !reward.getRewardType().equalsIgnoreCase("Badge") && !reward.getRewardType().equalsIgnoreCase("Voucher")) {
            binding.tvYouHaveWon.setText("You Have Won");
        } else {
            binding.tvYouHaveWon.setText("You Have Won A");
        }

        if (messageNote != null && !messageNote.isEmpty() && messageNote.equalsIgnoreCase("top")) {
            binding.tvYouHaveWon.setText("You're Eligible To Win");
        }

        binding.tvVouchername.setText(reward.getRewardName());
        binding.tvVoucherWinMessage.setText(reward.getRewardDescription());
        try {
            if (reward.getRewardType() != null) {
                if (rewardStatus.equals("Redeemed") || rewardStatus.equals("Completed") || rewardStatus.equals("RewardRedeemed")) {
                    binding.tvExpiredHours.setText("Completed");
                } else if (rewardStatus.equals("Expired") || rewardStatus.equals("RewardExpired")) {
                    binding.tvExpiredHours.setText("Expired");
                } else {
                    startCountdownToDate(reward.getExpireOn(), binding.tvExpiredHours);
                }
                binding.tvActivity.setText(reward.getActivity());
                binding.tvWhatToDo.setText(reward.getWhatToDo());
                binding.tvHowToDo.setText(reward.getHowToDo());
                binding.tvWhyToDo.setText(reward.getWhyToDo());
                applyGradientText(binding.tvVouchername, rewardcolors);
                applyGradientText(binding.tvExpiredHours, expiredcolors);


                if (rewardStatus.equalsIgnoreCase("Redeemed") || rewardStatus.equals("Completed") || rewardStatus.equals("RewardRedeemed")) {
                    binding.btnStartevent.setVisibility(View.GONE);
                    binding.btnCompleteevent.setVisibility(View.GONE);
                    binding.btnSubmitEvent.setVisibility(View.GONE);
                    binding.btnViewReward.setVisibility(View.VISIBLE);
                    binding.btnCloseevent.setVisibility(View.VISIBLE);
                    binding.btnSubmitEvent.setVisibility(View.GONE);
                    binding.edWriteSomethingJournal.setVisibility(View.GONE);
                    binding.tvJournalupload.setVisibility(View.GONE);
                } else if (rewardStatus.equalsIgnoreCase("Expired") || rewardStatus.equals("RewardExpired")) {
                    binding.btnStartevent.setVisibility(View.GONE);
                    binding.btnCompleteevent.setVisibility(View.GONE);
                    binding.btnSubmitEvent.setVisibility(View.GONE);
                    binding.btnViewReward.setVisibility(View.GONE);
                    binding.btnCloseevent.setVisibility(View.VISIBLE);
                    binding.btnSubmitEvent.setVisibility(View.GONE);
                    binding.edWriteSomethingJournal.setVisibility(View.GONE);
                    binding.tvJournalupload.setVisibility(View.GONE);
                } else if (reward.getIsStarted()) {
                    binding.btnStartevent.setVisibility(View.GONE);
                    binding.btnViewReward.setVisibility(View.GONE);
                    binding.btnCloseevent.setVisibility(View.VISIBLE);
                    if (reward.getRedirectionKey().equalsIgnoreCase("journalUpload") || reward.getRedirectionKey().equalsIgnoreCase("activityimageupload")) {
                        binding.edWriteSomethingJournal.setVisibility(View.VISIBLE);
                        binding.tvJournalupload.setVisibility(View.VISIBLE);
                        binding.btnCompleteevent.setVisibility(View.GONE);
                        binding.btnSubmitEvent.setVisibility(View.VISIBLE);
                    } else {
                        binding.btnCompleteevent.setVisibility(View.VISIBLE);
                        binding.btnSubmitEvent.setVisibility(View.GONE);
                        binding.edWriteSomethingJournal.setVisibility(View.GONE);
                        binding.tvJournalupload.setVisibility(View.GONE);
                    }

                } else {
                    binding.btnViewReward.setVisibility(View.GONE);
                    binding.btnStartevent.setVisibility(View.VISIBLE);
                    binding.btnCloseevent.setVisibility(View.VISIBLE);
                    binding.btnCompleteevent.setVisibility(View.GONE);
                    binding.edWriteSomethingJournal.setVisibility(View.GONE);
                    binding.tvJournalupload.setVisibility(View.GONE);
                }
            } else {
                applyGradientText(binding.tvVouchername, rewardcolors);
                binding.llActivityUi.setVisibility(View.GONE);
                binding.tvCongratulations.setText("Never Mind");
                binding.tvYouHaveWon.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        binding.btnViewReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromQuiz) {
                    APILogs.INSTANCE.activityTracker("A_PlayAndWinQuizathon_ActivityPopUp_ViewRewards", context);
                }
                PostSpinDialog.INSTANCE.getRewardRedirections(context, reward.getRewardType());
            }
        });

        binding.btnSubmitEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((reward.getRedirectionKey().equalsIgnoreCase("journalUpload") || reward.getRedirectionKey().equalsIgnoreCase("activityimageupload")) && binding.edWriteSomethingJournal.getText().toString().length() < 15) {
                    Toast.makeText(context, "Please enter more than 15 character.", Toast.LENGTH_SHORT).show();
                } else {
                    if (reward.getRedirectionKey().equalsIgnoreCase("journalUpload")) {
                        APILogs.INSTANCE.activityTracker("A_PlayAndWinQuizathon_ActivityPopUp_JournalUpload", context);
                    } else if (reward.getRedirectionKey().equalsIgnoreCase("activityimageupload")) {
                        APILogs.INSTANCE.activityTracker("A_PlayAndWinQuizathon_ActivityPopUpRedirection_activityimageupload", context);
                    }

                    dismiss();
                    NewDashboardHelper.Companion.setActivityName("" + reward.getRedirectionKey());
                    NewDashboardHelper.Companion.setTrasactionId("" + reward.getActivityTransId());
                    if (comingFrom != null && !comingFrom.equalsIgnoreCase("Quiz")) {
                        NewDashboardHelper.Companion.setFeatureName("Feedback");
                    } else {
                        NewDashboardHelper.Companion.setFeatureName("Quiz");
                    }
                    onRewardDialogClick.onSubmit(binding.edWriteSomethingJournal.getText().toString());

                }
            }
        });


        binding.btnStartevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewDashboardHelper.Companion.setActivityName("" + reward.getRedirectionKey());
                NewDashboardHelper.Companion.setTrasactionId("" + reward.getActivityTransId());
                if (comingFrom != null && !comingFrom.equalsIgnoreCase("Quiz")) {
                    NewDashboardHelper.Companion.setFeatureName("Feedback");
                } else {
                    NewDashboardHelper.Companion.setFeatureName("Quiz");
                }
                if (isFromQuiz) {
                    APILogs.INSTANCE.activityTracker("A_PlayAndWinQuizathon_ActivityPopUp_Start", context);
                } else if (isFromRewardList) {
                    APILogs.INSTANCE.activityTracker("A_SPIN_MYACTIVITIES_ACTIVITYPOPUP_START", context);
                } else {
                    APILogs.INSTANCE.activityTracker("A_SPINDASHBOARD_ACTIVITYPOPUP_START", context);
                }


                callToAnalytics(reward.getRedirectionKey());
                if (reward.getRedirectionKey() != null && reward.getRedirectionKey().equalsIgnoreCase("journalUpload") || reward.getRedirectionKey().equalsIgnoreCase("activityimageupload")) {
                    onRewardDialogClick.onStartClick();
                    binding.btnStartevent.setVisibility(View.GONE);
                    binding.tvJournalupload.setVisibility(View.VISIBLE);
                    binding.edWriteSomethingJournal.setVisibility(View.VISIBLE);
                    binding.btnSubmitEvent.setVisibility(View.VISIBLE);
                } else {
                    dismiss();
                    onRewardDialogClick.onStartClick();

                }
            }
        });
        binding.btnCompleteevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewDashboardHelper.Companion.setActivityName("" + reward.getRedirectionKey());
                NewDashboardHelper.Companion.setTrasactionId("" + reward.getActivityTransId());
                if (comingFrom != null && !comingFrom.equalsIgnoreCase("Quiz")) {
                    NewDashboardHelper.Companion.setFeatureName("Feedback");
                } else {
                    NewDashboardHelper.Companion.setFeatureName("Quiz");
                }
                if (isFromRewardList) {
                    APILogs.INSTANCE.activityTracker("A_SPIN_MYACTIVITIES_ACTIVITYPOPUP_COMPLETE", context);
                } else {
                    APILogs.INSTANCE.activityTracker("A_SPINDASHBOARD_ACTIVITYPOPUP_COMPLETE", context);
                }
                dismiss();
                onRewardDialogClick.onCompleteClick();

            }
        });

        binding.btnCloseevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromQuiz) {
                    APILogs.INSTANCE.activityTracker("A_PlayAndWinQuizathon_ActivityPopUp_Close", context);
                } else if (isFromRewardList) {
                    APILogs.INSTANCE.activityTracker("A_SPIN_MYACTIVITIES_ACTIVITYPOPUP_CLOSE", context);
                } else {
                    APILogs.INSTANCE.activityTracker("A_SPINDASHBOARD_ACTIVITYPOPUP_CLOSE", context);
                }
                dismiss();
                onRewardDialogClick.onCloseClick();
            }
        });

        return binding.getRoot();

    }

    private void applyGradientText(TextView view, int[] colors) {
        TextPaint paint = view.getPaint();
        float width = paint.measureText(view.getText().toString()); // Dynamic width based on text
        Shader textShader = new LinearGradient(0, 0, width, view.getTextSize(),
                colors, null, Shader.TileMode.REPEAT);
        paint.setShader(textShader);
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
                // Start the countdown timer
                new CountDownTimer(countdownDuration, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        long seconds = millisUntilFinished / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;

                        seconds %= 60;
                        minutes %= 60;

                        // Update the Button
                        String timeRemaining = String.format(Locale.getDefault(), "Expires in %02dh %02dm %02ds", hours, minutes, seconds);
                        view.setText(timeRemaining);
                    }

                    @Override
                    public void onFinish() {
                        // Update the Button when the countdown finishes
                        view.setText("Expired!");
                    }

                }.start();
            } else {
                // If the target time has already passed
                view.setText("Expired!");
            }

        } catch (ParseException e) {
            // Handle parsing error
            view.setText("Invalid date format");
            e.printStackTrace();
        }
    }

    void callToAnalytics(String activitytype) {
        if (activitytype != null) {
            String message = "A_SPINDASHBOARD_";
            if (isFromRewardList) {
                message = "A_SPIN_MYACTIVITIES_";
            }
            if (activitytype.equalsIgnoreCase("articles")) {
                message += "ACTIVITYPOPUPREDIRECTION_ARTICLES";
            } else if (activitytype.equalsIgnoreCase("facescan")) {
                message += "ACTIVITYPOPUPREDIRECTION_FACESCAN";
            } else if (activitytype.equalsIgnoreCase("Know your wellbeing")) {
                message += "ACTIVITYPOPUPREDIRECTION_KYW";
            } else if (activitytype.equalsIgnoreCase("meditation")) {
                message += "ACTIVITYPOPUPREDIRECTION_MEDITATION";
            } else if (activitytype.equalsIgnoreCase("water")) {
                message += "ACTIVITYPOPUPREDIRECTION_WATER";
            } else if (activitytype.equalsIgnoreCase("calories burned")) {
                message += "ACTIVITYPOPUPREDIRECTION_CALORIESBURNED";
            } else if (activitytype.equalsIgnoreCase("calories consumed")) {
                message += "ACTIVITYPOPUPREDIRECTION_CALORIESCONSUMED";
            } else if (activitytype.equalsIgnoreCase("video")) {
                message += "ACTIVITYPOPUPREDIRECTION_VIDEO";
            } else if (activitytype.equalsIgnoreCase("journalUpload")) {
                message += "ACTIVITYPOPUP_JOURNALUPLOAD";
            } else if (activitytype.equalsIgnoreCase("activityimageupload")) {
                message += "ACTIVITYPOPUP_IMAGEUPLOAD";
            } else if (activitytype.equalsIgnoreCase("quiz")) {
                message += "ACTIVITYPOPUPREDIRECTION_QUIZ";
            } else if (activitytype.equalsIgnoreCase("webinar")) {
                message += "ACTIVITYPOPUPREDIRECTION_WEBINAR";
            } else if (activitytype.equalsIgnoreCase("challenges")) {
                message += "ACTIVITYPOPUPREDIRECTION_CHALLENGES";
            } else if (activitytype.equalsIgnoreCase("quiz")) {
                message += "ACTIVITYPOPUPREDIRECTION_QUIZ";
            } else if (activitytype.equalsIgnoreCase("feedback")) {
                message += "ACTIVITYPOPUPREDIRECTION_FEEDBACK";
            }

            APILogs.INSTANCE.activityTracker(message, context);
        }
    }

    void startRewardCelebrationHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.ivCelebration.setVisibility(View.GONE);
            }
        }, 2000);
    }
}
