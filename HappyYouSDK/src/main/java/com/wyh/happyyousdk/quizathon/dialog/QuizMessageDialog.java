package com.wyh.happyyousdk.quizathon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.DialogQuizInfoBinding;
import com.wyh.happyyousdk.model.request.GetQuizathonDetailsRequest;
import com.wyh.happyyousdk.model.response.GetQuizathonDetailsResponse;
import com.wyh.happyyousdk.model.response.playwin.DialogModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonModel;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.play_and_win.PlayAndWinActivity;
import com.wyh.happyyousdk.quizathon.QuizathonActivity;
import com.wyh.happyyousdk.quizathon.QuizathonScoreActivity;
import com.wyh.happyyousdk.quizathon.QuizathonTimeboundScoreActivity;
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity;
import com.wyh.happyyousdk.quizathon.TimeBoundScoreActivity;
import com.wyh.happyyousdk.utils.CommonUtils;
import com.wyh.happyyousdk.utils.SharedPref;
import com.wyh.happyyousdk.utils.dialog.RegistrationDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizMessageDialog extends Dialog {
    DialogQuizInfoBinding binding;
    DialogModel dialogModel;
    Context context;
    ApiInterfaceWyh apiInterfaceWyh;

    public QuizMessageDialog(@NonNull Context context, DialogModel dialogModel, String rewardDate, ApiInterfaceWyh apiInterfaceWyh) {
        super(context);
        this.apiInterfaceWyh = apiInterfaceWyh;
        this.context = context;
        this.dialogModel = dialogModel;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_quiz_info, null, false);
        setContentView(binding.getRoot());


        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        if (dialogModel.getIcon() != null && !dialogModel.getIcon().isEmpty()) {
            binding.ivInfoIcon.setVisibility(View.VISIBLE);
            Glide.with(context).load(dialogModel.getIcon()).into(binding.ivInfoIcon);
        } else {
            binding.ivInfoIcon.setVisibility(View.GONE);
        }
        if (dialogModel.getTitle() != null && !dialogModel.getTitle().isEmpty()) {
            binding.lblTitle.setVisibility(View.VISIBLE);
            binding.lblTitle.setText(dialogModel.getTitle());
        } else {
            binding.lblTitle.setVisibility(View.GONE);
        }
        if (dialogModel.getMessage_top() != null && !dialogModel.getMessage_top().isEmpty()) {
            binding.tvMessageTop.setVisibility(View.VISIBLE);
            String formattedText = dialogModel.getMessage_top().replaceAll("\\\\n", "\n");
            binding.tvMessageTop.setText(formattedText);
        } else {
            binding.tvMessageTop.setVisibility(View.GONE);
        }

        if (dialogModel.getMessage_bottom() != null && !dialogModel.getMessage_bottom().isEmpty()) {
            binding.tvMessageBottom.setVisibility(View.VISIBLE);
            String formattedText = dialogModel.getMessage_bottom().replaceAll("\\\\n", "\n");
            binding.tvMessageBottom.setText(formattedText);
        } else {
            binding.tvMessageBottom.setVisibility(View.GONE);
        }
        binding.mcvExpiredHrs.setVisibility(View.GONE);
        if (dialogModel.getType() != null && dialogModel.getType().equalsIgnoreCase("TimerClaim")) {
            binding.mcvExpiredHrs.setVisibility(View.VISIBLE);
            if (rewardDate != null) {
                startCountdownToDate(rewardDate, binding.tvExpiredHours);
            }
        } else {
            binding.mcvExpiredHrs.setVisibility(View.GONE);
        }

        if (dialogModel.getButton_text() != null && !dialogModel.getButton_text().isEmpty()) {
            binding.mcvQuizButton.setVisibility(View.VISIBLE);
            binding.tvButtonText.setVisibility(View.VISIBLE);
            binding.tvButtonText.setText(dialogModel.getButton_text());
        } else {
            binding.mcvQuizButton.setVisibility(View.GONE);
            binding.tvButtonText.setVisibility(View.GONE);
        }

        if (dialogModel.getButton_text_optional() != null && !dialogModel.getButton_text_optional().isEmpty()) {
            binding.mcvQuizButtonOptional.setVisibility(View.VISIBLE);
            binding.tvButtonTextOptional.setVisibility(View.VISIBLE);
            binding.tvButtonTextOptional.setText(dialogModel.getButton_text_optional());
        } else {
            binding.mcvQuizButtonOptional.setVisibility(View.GONE);
            binding.tvButtonTextOptional.setVisibility(View.GONE);
        }

        binding.mcvQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PlayAndWinActivity.class));
                dismiss();
            }
        });

        binding.mcvQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.mcvQuizButtonOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (dialogModel.getRedirectionKey().contains("_")) {
                    String qid = "";
                    List<String> valList = Arrays.asList(dialogModel.getRedirectionKey().split("_"));
                    if (valList != null && valList.size() > 1) {
                        qid = valList.get(1);
                        nextButtonAction(Integer.parseInt(qid));
                    }

                } else if (dialogModel.getRedirectionKey().equalsIgnoreCase("play&win")) {
                    if (context instanceof PlayAndWinActivity) {

                    } else {
                        Intent intent = new Intent(context, PlayAndWinActivity.class);
                        context.startActivity(intent);
                    }

                } else if (dialogModel.getRedirectionKey().equalsIgnoreCase("quizathon")) {
                    if (context instanceof QuizathonViewAllActivity) {

                    } else {
                        Intent in = new Intent(context, QuizathonViewAllActivity.class);
                        in.putExtra("type", "quizathon");
                        in.putExtra("name", "Quiz");
                        context.startActivity(in);
                    }
                }
            }
        });

    }

    public QuizMessageDialog(@NonNull Context context, DialogModel dialogModel, ApiInterfaceWyh apiInterfaceWyh) {
        super(context);
        this.apiInterfaceWyh = apiInterfaceWyh;
        this.context = context;
        this.dialogModel = dialogModel;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_quiz_info, null, false);
        setContentView(binding.getRoot());


        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);


        if (dialogModel.getIcon() != null && !dialogModel.getIcon().isEmpty()) {
            binding.ivInfoIcon.setVisibility(View.VISIBLE);
            Glide.with(context).load(dialogModel.getIcon()).into(binding.ivInfoIcon);
        } else {
            if (dialogModel.getType().equalsIgnoreCase("NoReward")) {
                binding.ivInfoIcon.setVisibility(View.VISIBLE);
                Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_info_black)).into(binding.ivInfoIcon);
            } else {
                binding.ivInfoIcon.setVisibility(View.GONE);
            }
        }
        if (dialogModel.getTitle() != null && !dialogModel.getTitle().isEmpty()) {
            binding.lblTitle.setVisibility(View.VISIBLE);
            binding.lblTitle.setText(dialogModel.getTitle());
        } else {
            binding.lblTitle.setVisibility(View.GONE);
        }
        if (dialogModel.getMessage_top() != null && !dialogModel.getMessage_top().isEmpty()) {
            binding.tvMessageTop.setVisibility(View.VISIBLE);
            String formattedText = dialogModel.getMessage_top().replaceAll("\\\\n", "\n");
            binding.tvMessageTop.setText(formattedText);
        } else {
            binding.tvMessageTop.setVisibility(View.GONE);
        }

        if (dialogModel.getMessage_bottom() != null && !dialogModel.getMessage_bottom().isEmpty()) {
            binding.tvMessageBottom.setVisibility(View.VISIBLE);
            String formattedText = dialogModel.getMessage_bottom().replaceAll("\\\\n", "\n");
            binding.tvMessageBottom.setText(formattedText);
        } else {
            binding.tvMessageBottom.setVisibility(View.GONE);
        }
        binding.mcvExpiredHrs.setVisibility(View.GONE);
//        if(dialogModel.getType() != null && dialogModel.getType().equalsIgnoreCase("SaveRegistration")){
//            binding.mcvExpiredHrs.setVisibility(View.VISIBLE);
//        }else{
//            binding.mcvExpiredHrs.setVisibility(View.GONE);
//        }

        if (dialogModel.getButton_text() != null && !dialogModel.getButton_text().isEmpty()) {
            binding.mcvQuizButton.setVisibility(View.VISIBLE);
            binding.tvButtonText.setVisibility(View.VISIBLE);
            binding.tvButtonText.setText(dialogModel.getButton_text());
        } else {
            binding.tvButtonText.setVisibility(View.GONE);
        }

        if (dialogModel.getButton_text_optional() != null && !dialogModel.getButton_text_optional().isEmpty()) {
            binding.mcvQuizButtonOptional.setVisibility(View.VISIBLE);
            binding.tvButtonTextOptional.setVisibility(View.VISIBLE);
            binding.tvButtonTextOptional.setText(dialogModel.getButton_text_optional());
        } else {
            binding.mcvQuizButtonOptional.setVisibility(View.GONE);
            binding.tvButtonTextOptional.setVisibility(View.GONE);
        }
        binding.mcvQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.mcvQuizButtonOptional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (dialogModel.getRedirectionKey().contains("_")) {
                    String qid = "";
                    List<String> valList = Arrays.asList(dialogModel.getRedirectionKey().split("_"));
                    if (valList != null && valList.size() > 1) {
                        qid = valList.get(1);
                        nextButtonAction(Integer.parseInt(qid));
                    }

                } else if (dialogModel.getRedirectionKey().equalsIgnoreCase("play&win")) {
                    APILogs.INSTANCE.activityTracker("A_PlayAndWin_Quizathon_RegistrationDone_NotStarted_VA", context);
                    if (context instanceof PlayAndWinActivity) {

                    } else {
                        Intent intent = new Intent(context, PlayAndWinActivity.class);
                        context.startActivity(intent);
                    }

                } else if (dialogModel.getRedirectionKey().equalsIgnoreCase("quizathon")) {
                    APILogs.INSTANCE.activityTracker("A_PlayAndWin_Quizathon_RegistrationDone_NotStarted_PNW", context);
                    if (context instanceof QuizathonViewAllActivity) {

                    } else {
                        Intent in = new Intent(context, QuizathonViewAllActivity.class);
                        in.putExtra("type", "quizathon");
                        in.putExtra("name", "Quiz");
                        context.startActivity(in);
                    }
                }
            }
        });

    }


//    public void startCountdownToDate(String targetDate, TextView view) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
//
//        try {
//            // Parse the target date
//            Date endDate = dateFormat.parse(targetDate);
//            if (endDate == null) throw new ParseException("Invalid date", 0);
//
//            long targetTimeMillis = endDate.getTime();
//            long currentTimeMillis = System.currentTimeMillis();
//
//            // Calculate the countdown duration
//            long countdownDuration = targetTimeMillis - currentTimeMillis;
//
//            if (countdownDuration > 0) {
//                // Start the countdown timer
//                new CountDownTimer(countdownDuration, 1000) {
//
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                        long seconds = millisUntilFinished / 1000;
//                        long minutes = seconds / 60;
//                        long hours = minutes / 60;
//
//                        seconds %= 60;
//                        minutes %= 60;
//
//                        // Update the Button
//                        String timeRemaining = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
//                        view.setText(timeRemaining);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        // Update the Button when the countdown finishes
//                        view.setText("Expired!");
//                    }
//
//                }.start();
//            } else {
//                // If the target time has already passed
//                view.setText("Expired!");
//            }
//
//        } catch (ParseException e) {
//            // Handle parsing error
//            view.setText("Invalid date format");
//            e.printStackTrace();
//        }
//    }

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


    private void nextButtonAction(int quizID) {
        try {
            CommonUtils.showProgressDialige(context);
            GetQuizathonDetailsRequest request = new GetQuizathonDetailsRequest(quizID);
            apiInterfaceWyh.getQuizthonDetails(SharedPref.getAuthToken(), request).enqueue(new Callback<GetQuizathonDetailsResponse>() {
                @Override
                public void onResponse(Call<GetQuizathonDetailsResponse> call, Response<GetQuizathonDetailsResponse> response) {
                    try {
                        CommonUtils.dismissDialoge();
                        if (response.code() == 200 && response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            // QuizathonModel quizathonModel = (QuizathonModel) item;
                            QuizathonModel quizathonModel = response.body().getData();
                            if (quizathonModel.getAnswerJson() != null && !quizathonModel.getAnswerJson().isEmpty() && quizathonModel.isQuizCompleted()) {
                                Intent intent = new Intent(context, QuizathonScoreActivity.class);
                                if (quizathonModel.getQuizType() != null && quizathonModel.getQuizType().equalsIgnoreCase("Streak")) {
                                    intent = new Intent(context, QuizathonTimeboundScoreActivity.class);
                                }
                                intent.putExtra("data", quizathonModel.getAnswerJson());
                                intent.putExtra("your_score", quizathonModel.getUserScore());
                                intent.putExtra("total_score", quizathonModel.getTotalScore());
                                intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                                intent.putExtra("qid", quizathonModel.getQuizId());
                                intent.putExtra("comingFrom", "quiz");
                                intent.putExtra("rewardText", "");
                                intent.putExtra("withActivity", quizathonModel.getShowActivity());
                                intent.putExtra("quizdata", quizathonModel.getModel());
                                intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                                intent.putExtra("isRetakeAvailable", quizathonModel.getRetakeAvailable());
                                intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                                intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                                intent.putExtra("hasSufficientBalance", quizathonModel.isHasSufficientBalance());
                                intent.putExtra("retakePointstoBurn", quizathonModel.getRetakePointstoBurn());
                                intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                                intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());

                                context.startActivity(intent);
                            } else if (!quizathonModel.getIsUserRegistered() && quizathonModel.getRegistrationQuestions() != null && !quizathonModel.getRegistrationQuestions().isEmpty() && quizathonModel.getIsRegistrationRequired()) {
                                Intent intent = new Intent(context, QuizathonActivity.class);
                                intent.putExtra("data",
                                        (quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted())
                                                ? quizathonModel.getAnswerJson()
                                                : quizathonModel.getQuestionJson());
                                intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                                intent.putExtra("Category", quizathonModel.getQuizDesc());
                                intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                                intent.putExtra("comingFrom", "");
                                intent.putExtra("IsRetake", quizathonModel.getRetake());
                                intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                                intent.putExtra("qId", quizathonModel.getQuizId());
                                intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                                intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());

                                RegistrationDialog dialog = new RegistrationDialog(context, quizathonModel.getRegistrationQuestions(), quizathonModel.getRegTitle(), quizathonModel.getQuizId(), intent, quizathonModel.getQuizStarted(), quizathonModel.getQuizDesc());
                                if (quizathonModel.getRegIcon() != null && !quizathonModel.getRegIcon().isEmpty()) {
                                    dialog.setIconUrl(quizathonModel.getRegIcon());
                                }
                                dialog.show();
                            } else if (!quizathonModel.getIsRegistrationRequired()) {
                                Intent intent = new Intent(context, QuizathonActivity.class);
                                intent.putExtra("data",
                                        (quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted())
                                                ? quizathonModel.getAnswerJson()
                                                : quizathonModel.getQuestionJson());
                                intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                                intent.putExtra("Category", quizathonModel.getQuizDesc());
                                intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                                intent.putExtra("comingFrom", "");
                                intent.putExtra("IsRetake", quizathonModel.getRetake());
                                intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                                intent.putExtra("qId", quizathonModel.getQuizId());

                                context.startActivity(intent);
                                intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                                intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());

                            } else if (quizathonModel.getDialog_model() != null && quizathonModel.getDialog_model().getType() != null) {
                                QuizMessageDialog dialog = new QuizMessageDialog(context, quizathonModel.getDialog_model(), apiInterfaceWyh);
                                dialog.show();
                            } else {
                                Intent intent = new Intent(context, QuizathonActivity.class);
                                intent.putExtra("data", quizathonModel.getAnswerJson() != null && !quizathonModel.isQuizCompleted() ? quizathonModel.getAnswerJson() : quizathonModel.getQuestionJson());
                                intent.putExtra("CategoryName", quizathonModel.getQuizTitle());
                                intent.putExtra("Category", quizathonModel.getQuizDesc());
                                intent.putExtra("comingFrom", "");
                                intent.putExtra("IsRetake", quizathonModel.getRetake());
                                intent.putExtra("RetakeId", quizathonModel.getRetakeId());
                                intent.putExtra("rewardDate", quizathonModel.getRewardDate());
                                intent.putExtra("qId", quizathonModel.getQuizId());
                                intent.putExtra("showNextGameButton", quizathonModel.getShowNextGameButton());
                                intent.putExtra("nextGameRedirectTo", quizathonModel.getRedirectTo());
                                context.startActivity(intent);
                            }
                        } else {

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<GetQuizathonDetailsResponse> call, Throwable t) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
