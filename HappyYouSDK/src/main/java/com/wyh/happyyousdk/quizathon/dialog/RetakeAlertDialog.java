package com.wyh.happyyousdk.quizathon.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.DialogRetakeAlertBinding;
import com.wyh.happyyousdk.quiz.adapter.QuizOptionAdapter;
import com.wyh.happyyousdk.quizathon.QuizathonScoreActivity;

public class RetakeAlertDialog extends Dialog {

    DialogRetakeAlertBinding binding;

    public RetakeAlertDialog(@NonNull Context context, int retakePointstoBurn, Drawable drawable, String title, String message, QuizOptionAdapter.OnItemClickListener dialogInterface) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_retake_alert, null, false);
        setContentView(binding.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        Glide.with(context).load(drawable).into(binding.ivRetakeQuiz);

        binding.lblRetakeTitle.setText(title);
        binding.tvRetakeMessage.setText(message.replace("\\n","\n"));
        if (context instanceof QuizathonScoreActivity) {
            if (retakePointstoBurn == 0) {
                binding.tvButtonText.setText("Take Requiz");
            } else {
                binding.tvButtonText.setText("Retake the quiz with " + retakePointstoBurn + " HY Points");
            }
        } else {
            if (retakePointstoBurn > 0) {
                if (title.equalsIgnoreCase("Quiz"))
                {
                    binding.tvButtonText.setText("Play using " + retakePointstoBurn + " HY Points");
                }
                else {
                    binding.tvButtonText.setText("Register using " + retakePointstoBurn + " HY Points");
                }
            }
        }

        binding.ivRetakeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dialogInterface.onClick(0, "Close");
            }
        });
        binding.mcvQuizretake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dialogInterface.onClick(0, "Retake");
            }
        });

    }

    public RetakeAlertDialog(@NonNull Context context, int retakePointstoBurn, String drawable, String title, String message, QuizOptionAdapter.OnItemClickListener dialogInterface) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_retake_alert, null, false);
        setContentView(binding.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);

        Glide.with(context).load(drawable).into(binding.ivRetakeQuiz);

        binding.lblRetakeTitle.setText(title);
        binding.tvRetakeMessage.setText(message.replace("\\n","\n"));
        if (context instanceof QuizathonScoreActivity) {
            if (retakePointstoBurn == 0) {
                binding.tvButtonText.setText("Take Requiz");
            } else {
                binding.tvButtonText.setText("Retake the with " + retakePointstoBurn + " HY Points");
            }
        } else {
            if (retakePointstoBurn > 0) {
                if (title.equalsIgnoreCase("Quiz"))
                {
                    binding.tvButtonText.setText("Play using " + retakePointstoBurn + " HY Points");
                }
                else {
                    binding.tvButtonText.setText("Register using " + retakePointstoBurn + " HY Points");
                }

            }
            else {
                binding.tvButtonText.setText("Close");
            }
        }

        binding.ivRetakeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dialogInterface.onClick(0, "Close");
            }
        });
        binding.mcvQuizretake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (binding.tvButtonText.getText().equals("Close"))
                {
                    dialogInterface.onClick(0, "Close");
                }
                else {
                    dialogInterface.onClick(0, "Retake");
                }

            }
        });

    }
}
