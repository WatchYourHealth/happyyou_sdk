package com.wyh.happyyousdk.play_and_win.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.wyh.happyyousdk.utils.CommonUtils.isDateExpired;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.wyh.happyyousdk.APIEncryption.APILogs;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.SDKConstants;
import com.wyh.happyyousdk.SpinWheel.Utilities.MessageInfoDialog;
import com.wyh.happyyousdk.SpinWheel.onRewardClick;
import com.wyh.happyyousdk.dashboard.NewDashboardActivity;
import com.wyh.happyyousdk.databinding.ItemPlaywinCardBinding;
import com.wyh.happyyousdk.databinding.ItemRewardCardBinding;
import com.wyh.happyyousdk.model.response.GetQuizQuestions;
import com.wyh.happyyousdk.model.response.QuestionModel;
import com.wyh.happyyousdk.model.response.playwin.QuizFeedbackModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonModel;
import com.wyh.happyyousdk.model.response.playwin.StreakModel;
import com.wyh.happyyousdk.play_and_win.PlayAndWinActivity;
import com.wyh.happyyousdk.quizathon.QuizathonViewAllActivity;
import com.wyh.happyyousdk.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PlayWinCardViewAdapter extends BaseAdapter {
    public List<QuizFeedbackModel> quizFeedbackModels = new ArrayList<>();
    public List<QuizathonModel> quizathonModel = new ArrayList<>();
    public List<GetQuizQuestions> questionModelList = new ArrayList<>();
    Context context;

    private String type;
    onRewardClick rewardClick;
    int newPos = 0;

    private int[] res = {
            R.color.blue,
            R.color.pink,
            R.color.light_orange,
    };

    HashMap<String, String> drawableHashMap = new HashMap<>();

    public PlayWinCardViewAdapter(List<QuizFeedbackModel> quizFeedbackModels, List<QuizathonModel> quizathonModel, List<GetQuizQuestions> questionModelList, Context context, String type, onRewardClick rewardClick) {
        this.quizFeedbackModels = quizFeedbackModels;
        this.quizathonModel = quizathonModel;
        this.questionModelList = questionModelList;
        this.context = context;
        this.type = type;
        this.rewardClick = rewardClick;

        drawableHashMap.put("Water".toLowerCase(), "water.png");
        drawableHashMap.put("Benefits Of Walking".toLowerCase(), "benefitsofwalking.png");
        drawableHashMap.put("Diabetes Mellitus".toLowerCase(), "diabetes_mellitus.png");
        drawableHashMap.put("Diabetic Foot".toLowerCase(), "diabeticfoot.png");
        drawableHashMap.put("Fruits".toLowerCase(), "fruits.png");
        drawableHashMap.put("Immunity Boosters".toLowerCase(), "immunity.png");
        drawableHashMap.put("Nutrition".toLowerCase(), "nutrition.png");
        drawableHashMap.put("Obesity".toLowerCase(), "obesity.png");
        drawableHashMap.put("Super Foods".toLowerCase(), "suparfood.png");
        drawableHashMap.put("Vegetables".toLowerCase(), "vegetables.png");
        drawableHashMap.put("Headache".toLowerCase(), "ic_headache.png");
    }

    @Override
    public int getCount() {
        if (quizFeedbackModels != null && quizFeedbackModels.size() > 0) {
            return quizFeedbackModels.size();
        }
        if (quizathonModel != null && quizathonModel.size() > 0) {
            return quizathonModel.size();
        } else if (questionModelList != null && questionModelList.size() > 0) {
            return questionModelList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return "";
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemPlaywinCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_playwin_card, parent, false);
        try {
            if (quizFeedbackModels != null && quizFeedbackModels.size() > 0) {
                String title = quizFeedbackModels.get(position).getFeedbackTitle();

                if (title != null && title.length() > 15) {
                    title = quizFeedbackModels.get(position).getFeedbackDesc().substring(0, 13) + "...";
                }
                String category = quizFeedbackModels.get(position).getFeedbackDesc();
                if (quizFeedbackModels.get(position).getFeedbackDesc() != null && quizFeedbackModels.get(position).getFeedbackDesc().length() > 13) {
                    category = category.substring(0, 13) + "...";
                }
                if (quizFeedbackModels.get(position).getIsFeedbackTaken()) {
                    binding.imChecked.setVisibility(VISIBLE);
                }
                binding.tvQuizTitle.setText(title);
                binding.tvQuizTitle.setTextSize(12);
                binding.tvQuizCategory.setText(category);

                binding.mcvQuizButton.setCardBackgroundColor(Color.parseColor(quizFeedbackModels.get(position).getFeedbackButtonColor()));
                binding.tvQuizButtonText.setText(quizFeedbackModels.get(position).getFeedbackButtonText());
                binding.rlPlayWinCard.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(quizFeedbackModels.get(position).getFeedbackCardBG())));
                if (quizFeedbackModels.get(position).getFeedbackLogo() != null && !quizFeedbackModels.get(position).getFeedbackLogo().isEmpty()) {
                    Glide.with(context).load(quizFeedbackModels.get(position).getFeedbackLogo()).into(binding.ivQuizLogo);
                }

            } else if (questionModelList != null && questionModelList.size() > 0) {
                String title = questionModelList.get(position).getCategoryName();


                if (title != null && title.length() > 15) {
                    title = questionModelList.get(position).getCategoryName().substring(0, 13) + "...";
                }
                String category = questionModelList.get(position).getCategory();
                if (questionModelList.get(position).getCategory() != null && questionModelList.get(position).getCategory().length() > 13) {
                    category = category.substring(0, 13) + "...";
                }

                String url = drawableHashMap.get(questionModelList.get(position).getCategoryName().toLowerCase());
                Glide.with(context)
                        .load(CommonUtils.getBaseUrlForAPI(context) + SDKConstants.endPointForImages + url)
                        .into(binding.ivQuizLogo);
                binding.tvQuizTitle.setText("Health and Lifestyle".toUpperCase());
                binding.tvQuizTitle.setTextSize(12);
                binding.tvQuizCategory.setText(title);
                binding.mcvQuizButton.setCardBackgroundColor(ContextCompat.getColor(context, R.color.score_green_dark));
                binding.tvQuizButtonText.setText("Start Now");


                binding.rlPlayWinCard.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, res[newPos])));
                if (newPos >= 2) {
                    newPos = 0;
                } else {
                    newPos++;
                }
            } else if (quizathonModel != null && quizathonModel.size() > 0) {
                String title = quizathonModel.get(position).getQuizTitle();

                if (quizathonModel.get(position).getQuizType() != null && !quizathonModel.get(position).getQuizType().isEmpty() &&
                        quizathonModel.get(position).getQuizType().equalsIgnoreCase("Streak")) {
                    binding.clStreak.setVisibility(VISIBLE);
                    if (quizathonModel.get(position).getStreakModels() != null && quizathonModel.get(position).getStreakModels().size() > 0) {
                        try {
                            List<StreakModel> attemptedList = quizathonModel.get(position).getStreakModels().stream().filter(streakModel -> streakModel.isQuizTaken()).collect(Collectors.toList());
                            int countAttempted = 0;
                            if (attemptedList != null && attemptedList.size() > 0) {
                                countAttempted = attemptedList.size();
                            }
                            binding.tvStreak.setText(countAttempted + "/" + quizathonModel.get(position).getStreakModels().size());
                        } catch (Exception e) {
                            binding.tvStreak.setText("0/" + quizathonModel.get(position).getStreakModels().size());
                        }
                    }

                } else {
                    binding.clStreak.setVisibility(GONE);
                }

                if (title != null && title.length() > 15) {
                    title = quizathonModel.get(position).getQuizTitle().substring(0, 13) + "...";
                }
                String category = quizathonModel.get(position).getQuizDesc();
                if (quizathonModel.get(position).getQuizDesc() != null && quizathonModel.get(position).getQuizDesc().length() > 13) {
                    category = category.substring(0, 13) + "...";
                }

                binding.tvQuizTitle.setText(title.toUpperCase());
                binding.tvQuizTitle.setTextSize(12);
                binding.tvQuizCategory.setText(category);
                if (!type.equalsIgnoreCase("spin") && !type.equalsIgnoreCase("others") && quizathonModel.get(position).getQuizButtonColor() != null) {
                    if (quizathonModel.get(position).getQuizButtonColor() != null && !quizathonModel.get(position).getQuizButtonColor().isEmpty()) {
                        binding.mcvQuizButton.setCardBackgroundColor(Color.parseColor(quizathonModel.get(position).getQuizButtonColor()));
                    }
                    binding.tvQuizButtonText.setText(quizathonModel.get(position).getQuizButtonText());
                    binding.rlPlayWinCard.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(quizathonModel.get(position).getQuizBgColor())));
                    Glide.with(context).load(quizathonModel.get(position).getQuizLogo()).into(binding.ivQuizLogo);
                } else if (type.equalsIgnoreCase("others")) {
                    binding.ivQuizLogo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_file_share));
                    binding.tvQuizButtonText.setText(quizathonModel.get(position).getQuizButtonText());
                }
                if (quizathonModel.get(position).isQuizCompleted() && !quizathonModel.get(position).getShowActivity()) {
                    binding.imChecked.setVisibility(VISIBLE);
                }
            }

            binding.rlPlayWinCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (quizathonModel != null && quizathonModel.size() > 0) {
                        if (!isDateExpired(quizathonModel.get(position).getQuizRegistrationDate())) {
                            rewardClick.onRewardClick(quizathonModel.get(position), type);
                        } else {
                            if (!quizathonModel.get(position).isRegistrationAllowed() && quizathonModel.get(position).getRegistrationPointBurn() >= 0) {
                                rewardClick.onRewardClick(quizathonModel.get(position), type);
                            } else {
                                if (quizathonModel.get(position).isRegistrationAllowed()) {
                                    rewardClick.onRewardClick(quizathonModel.get(position), type);
                                } else {
                                    if (quizathonModel.get(position).getIsUserRegistered()) {
                                        rewardClick.onRewardClick(quizathonModel.get(position), type);
                                    } else {
                                        MessageInfoDialog messageInfoDialog = new MessageInfoDialog(context, ContextCompat.getDrawable(context, R.drawable.ic_oops), "Oops!", ContextCompat.getString(context, R.string.quiz_reg_over));
                                        messageInfoDialog.show();
                                    }
                                }

                            }
                        }
                        try {
                            if (type.equalsIgnoreCase("Spin")) {
                                APILogs.INSTANCE.activityTracker("A_PlayAndWin_Spin_Start", context);
                            } else if (type.equalsIgnoreCase("Others")) {
                                APILogs.INSTANCE.activityTracker("A_PlayAndWin_Others_Start", context);
                            } else {
                                if (binding.tvQuizButtonText.getText() != null) {
                                    APILogs.INSTANCE.activityTracker("A_PlayAndWin_Quizathon_" + binding.tvQuizButtonText.getText().toString(), context);
                                }
                            }
                        } catch (Exception ex) {

                        }
                    } else if (questionModelList != null && questionModelList.size() > 0) {
                        rewardClick.onRewardClick(questionModelList.get(position), type);
                        try {
                            APILogs.INSTANCE.activityTracker("A_PlayAndWin_PlayNLearn_Start", context);

                        } catch (Exception ex) {
                        }
                    } else if (quizFeedbackModels != null && quizFeedbackModels.size() > 0) {
                        rewardClick.onRewardClick(quizFeedbackModels.get(position), type);
                        try {
                            if (binding.tvQuizButtonText.getText() != null) {
                                APILogs.INSTANCE.activityTracker("A_PlayAndWin_Feedback_" + binding.tvQuizButtonText.getText().toString(), context);
                            }
                        } catch (Exception ex) {

                        }
                    }


                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }


        return binding.getRoot();
    }
}
