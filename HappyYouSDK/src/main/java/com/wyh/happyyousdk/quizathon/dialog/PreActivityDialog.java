package com.wyh.happyyousdk.quizathon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.DialogPreActivityBinding;
import com.wyh.happyyousdk.model.response.playwin.QuizathonModel;
import com.wyh.happyyousdk.model.response.quizathon.SaveQuizathonAnsModel;
import com.wyh.happyyousdk.quizathon.QuizathonScoreActivity;
import com.wyh.happyyousdk.quizathon.ReTakeQuizListener;

public class PreActivityDialog extends Dialog {
    DialogPreActivityBinding binding;
    SaveQuizathonAnsModel saveQuizathonAnsModel;
    QuizathonModel quizathonModel;
    ReTakeQuizListener reTakeQuizListener;

    public PreActivityDialog(@NonNull Context context, SaveQuizathonAnsModel saveQuizathonAnsModel,ReTakeQuizListener reTakeQuizListener) {
        super(context);
        this.saveQuizathonAnsModel = saveQuizathonAnsModel;
        this.reTakeQuizListener = reTakeQuizListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_pre_activity, null, false);
        setContentView(binding.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        if (saveQuizathonAnsModel.getHeader1() != null) {
            binding.headerOne.setText(saveQuizathonAnsModel.getHeader1());
        } else {
            binding.headerOne.setVisibility(View.GONE);
        }
        if (saveQuizathonAnsModel.getHeader2() != null) {
            binding.headerTwo.setText(saveQuizathonAnsModel.getHeader2());
        } else {
            binding.headerTwo.setVisibility(View.GONE);
        }
        if (saveQuizathonAnsModel.getRewardName() != null) {
            binding.tvrewardName.setText(saveQuizathonAnsModel.getRewardName());
        } else {
            binding.tvrewardName.setVisibility(View.GONE);
        }
        if (saveQuizathonAnsModel.getRewardDescription() != null) {
            binding.tvRewardDesc.setText(saveQuizathonAnsModel.getRewardDescription());
        } else {
            binding.tvRewardDesc.setVisibility(View.GONE);
        }
        if (saveQuizathonAnsModel.getButtonText() != null) {
            binding.btnText1.setText(saveQuizathonAnsModel.getButtonText());
        } else {
            binding.btnText1.setText("Claim");
            binding.btnText1.setVisibility(View.VISIBLE);
        }
        if(saveQuizathonAnsModel.getShowConsent() != null && saveQuizathonAnsModel.getShowConsent()){
            binding.btnText2.setText("Requiz");
        }else {
            binding.btnText2.setText("Close");
        }
        if (saveQuizathonAnsModel.getRewardIcon() != null) {
            Glide.with(context).load(saveQuizathonAnsModel.getRewardIcon()).into(binding.ivScratch);
        } else {
            binding.ivScratch.setVisibility(View.GONE);
        }
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        binding.btnText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (context instanceof QuizathonScoreActivity) {
                    if(saveQuizathonAnsModel.getShowConsent() != null && saveQuizathonAnsModel.getShowConsent()){
                        reTakeQuizListener.onClaimClicked();
                    }else {
                        ((QuizathonScoreActivity) context).showBottomSheet();
                    }
                }
            }
        });


        binding.btnText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(saveQuizathonAnsModel.getShowConsent() != null && saveQuizathonAnsModel.getShowConsent()){
                    reTakeQuizListener.onRetakeQuizClicked();
                }
            }
        });
    }
    public PreActivityDialog(@NonNull Context context, QuizathonModel quizathonModel, ReTakeQuizListener reTakeQuizListener) {
        super(context);
        this.quizathonModel = quizathonModel;
        this.reTakeQuizListener = reTakeQuizListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_pre_activity, null, false);
        setContentView(binding.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        if (quizathonModel.getHeader1() != null) {
            binding.headerOne.setText(quizathonModel.getHeader1());
        } else {
            binding.headerOne.setVisibility(View.GONE);
        }
        if (quizathonModel.getHeader2() != null) {
            binding.headerTwo.setText(quizathonModel.getHeader2());
        } else {
            binding.headerTwo.setVisibility(View.GONE);
        }
        if (quizathonModel.getRewardName() != null) {
            binding.tvrewardName.setText(quizathonModel.getRewardName());
        } else {
            binding.tvrewardName.setVisibility(View.GONE);
        }
        if (quizathonModel.getRewardDescription() != null) {
            binding.tvRewardDesc.setText(quizathonModel.getRewardDescription().replace("\\n", "\n"));
        } else {
            binding.tvRewardDesc.setVisibility(View.GONE);
        }
        if (quizathonModel.getButtonText() != null) {
            binding.btnText1.setText(quizathonModel.getButtonText());
        } else {
            binding.btnText1.setText("Claim");
            binding.btnText1.setVisibility(View.VISIBLE);
        }
        if(quizathonModel.getShowConsent() != null && quizathonModel.getShowConsent()){
            binding.btnText2.setText("Requiz");
        }else {
            binding.btnText2.setText("Close");
        }
        if (quizathonModel.getRewardIcon() != null) {
            Glide.with(context).load(quizathonModel.getRewardIcon()).into(binding.ivScratch);
        } else {
            binding.ivScratch.setVisibility(View.GONE);
        }

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        binding.btnText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (context instanceof QuizathonScoreActivity) {
                    if(quizathonModel.getShowConsent() != null && quizathonModel.getShowConsent()){
                        reTakeQuizListener.onClaimClicked();
                    }else {
                        ((QuizathonScoreActivity) context).showBottomSheet();
                    }
                }
            }
        });


        binding.btnText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(quizathonModel.getShowConsent() != null && quizathonModel.getShowConsent()){
                    reTakeQuizListener.onRetakeQuizClicked();
                }
            }
        });
    }
}
