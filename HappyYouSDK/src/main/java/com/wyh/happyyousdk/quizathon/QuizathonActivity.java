package com.wyh.happyyousdk.quizathon;

import static com.wyh.happyyousdk.utils.CommonUtils.convertListToString;
import static com.wyh.happyyousdk.utils.CommonUtils.getBaseUrlForAPI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.wyh.happyyousdk.APIEncryption.APILogs;

import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.databinding.ActivityQuizathonBinding;
import com.wyh.happyyousdk.model.request.quizathon.SaveFeedbackAnswerRequest;
import com.wyh.happyyousdk.model.request.quizathon.SaveQuizathonAnswerRequest;
import com.wyh.happyyousdk.model.response.QuestionModel;
import com.wyh.happyyousdk.model.response.playwin.QuizathonModel;
import com.wyh.happyyousdk.model.response.playwin.StreakModel;
import com.wyh.happyyousdk.model.response.quizathon.SaveQuizathonAnsResponse;
import com.wyh.happyyousdk.network.ApiClientWyh;
import com.wyh.happyyousdk.network.ApiInterfaceWyh;
import com.wyh.happyyousdk.quizathon.adapter.QuizathonOptionAdapter;
import com.wyh.happyyousdk.utils.Analytics;
import com.wyh.happyyousdk.utils.NewMediaPlayerManager;
import com.wyh.happyyousdk.utils.SharedPref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizathonActivity extends AppCompatActivity {

    NewMediaPlayerManager mediaPlayerManager;
    ActivityQuizathonBinding binding;
    boolean isAttempted = false;
    ProgressDialog progressDialog;
    ApiInterfaceWyh apiInterfaceWyh;
    Context context;
    QuizathonOptionAdapter adapter;
    int index, totalScore;
    String qId;
    String categoryName, category, comingFrom, rewardDate, type;
    ArrayList<QuestionModel> data;
    boolean isStamp = false;
    AlertDialog alertDialogBonusRewards, alertDialogStamp, alertDialogBonusStamp;
    ExoPlayer exoPlayer;
    boolean isRetake = false;
    String RetakeId = "0";
    QuizathonModel quizathonModel = null;
    String titleContent = "";

    public boolean isAttempted() {
        return isAttempted;
    }

    public void setAttempted(boolean attempted) {
        isAttempted = attempted;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isAttempted) {
                    binding.nextBtn.setVisibility(View.VISIBLE);
                    try {
                        if (index == (data.size() - 1)) {
                            binding.nextText.setText("Submit");
                            if (data.get(index).getQuestionType().equalsIgnoreCase("Single")) {
                                binding.nextBtn.setVisibility(View.GONE);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    // binding.nextBtn.setVisibility(View.GONE);
                }
            }
        }, 100);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quizathon);
        context = this;
        SharedPref.init(context);

        apiInterfaceWyh = ApiClientWyh.getClient(getBaseUrlForAPI(context)).create(ApiInterfaceWyh.class);
        progressDialog = new ProgressDialog(context, R.style.ProgressBarTheme);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        APILogs.INSTANCE.activityTracker("A_PlayAndWinDashboard_Quizathon_QA_View", context);

        index = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("data");
            comingFrom = extras.getString("comingFrom");
            categoryName = extras.getString("CategoryName");
            category = extras.getString("Category");
            qId = extras.getString("qId");
            rewardDate = extras.getString("rewardDate");
            type = extras.getString("type");
            if (comingFrom != null && comingFrom.equalsIgnoreCase("")) {
                comingFrom = "quiz";
            }
            try {
                isRetake = extras.getBoolean("IsRetake");
                RetakeId = extras.getString("RetakeId");
                quizathonModel = (QuizathonModel) getIntent().getSerializableExtra("quizModel");
            } catch (Exception ex) {
                quizathonModel = null;
            }
            binding.includeToolbar.tvBack.setText(category);
            try {
                final GsonBuilder gsonBuilder = new GsonBuilder();
                final Gson gson = gsonBuilder.create();
                data = gson.fromJson(value, new TypeToken<List<QuestionModel>>() {
                }.getType());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            quizathonModel = (QuizathonModel) getIntent().getSerializableExtra("quizModel");
        }

        mediaPlayerManager = NewMediaPlayerManager.getInstance();
        exoPlayer = new ExoPlayer.Builder(context).build();
        binding.exoPlayerQuiz.setPlayer(exoPlayer);
        setQuestion(-1);
        setToolBar();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
    }

    private void initView() {

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < data.size()) {
                    QuestionModel questionModel = data.get(index);

                    if (questionModel.getQuestionType().equalsIgnoreCase("Multi")) {

                        List<String> correctAnswer = new ArrayList<>();
                        if (questionModel.getAnswer() != null && questionModel.getAnswer().contains(",")) {
                            correctAnswer = Arrays.asList(questionModel.getAnswer().split(","));
                        } else {
                            correctAnswer.add(questionModel.getAnswer());
                        }
                        data.get(index).setAnswers(sortListsEqual(adapter.getAnswers()));
                        data.get(index).setUserAns(convertListToString(sortListsEqual(adapter.getAnswers())));
                        if (areListsEqual(correctAnswer, adapter.getAnswers())) {
                            totalScore++;
                        }
                        if (data.get(index).getUserAns() != null && !data.get(index).getUserAns().toString().isEmpty()) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //binding.nextBtn.setVisibility(View.GONE);
                                    nextQuestion();
                                }
                            }, 200);
                        } else {
                            Toast.makeText(context, "Please select at least one option", Toast.LENGTH_SHORT).show();
                        }
                    } else if (questionModel.getQuestionType().equalsIgnoreCase("EditText")) {
                        if (!checkStartOrEndSpaces(QuizathonActivity.this, binding.edTextAnswer.getText().toString()) && !binding.edTextAnswer.getText().toString().isEmpty()) {
                            List<String> correctAnswer = new ArrayList<>();
                            List<String> userAnswer = new ArrayList<>();
                            if (questionModel.getAnswer() != null && questionModel.getAnswer().contains(",")) {
                                correctAnswer = Arrays.asList(questionModel.getAnswer().split(","));
                            } else {
                                correctAnswer.add(questionModel.getAnswer());
                            }
                            userAnswer.add(binding.edTextAnswer.getText().toString().trim());

                            data.get(index).setAnswers(userAnswer);
                            data.get(index).setUserAns(convertListToString(userAnswer));
                            if (data.get(index).isExactMatch()) {
                                if (areListsEqual(correctAnswer, userAnswer)) {
                                    totalScore++;
                                }
                            } else {
                                if (data.get(index).getKeywords() != null && data.get(index).getKeywords().size() > 0) {
                                    if (areAllStringsPresent(data.get(index).getKeywords(), binding.edTextAnswer.getText().toString().trim())) {
                                        totalScore++;
                                    }
                                } else if (areListsEqual(correctAnswer, userAnswer)) {
                                    totalScore++;
                                }
                            }

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //  binding.nextBtn.setVisibility(View.GONE);
                                    nextQuestion();
                                }
                            }, 200);
                        } else {
                            //Toast.makeText(context, "Please select at one option", Toast.LENGTH_SHORT).show();
                        }
                    } else if (questionModel.getQuestionType().equalsIgnoreCase("TextArea")) {
                        if (!checkStartOrEndSpaces(QuizathonActivity.this, binding.edTextArea.getText().toString()) && !binding.edTextArea.getText().toString().isEmpty()) {
                            List<String> correctAnswer = new ArrayList<>();
                            List<String> userAnswer = new ArrayList<>();
                            if (questionModel.getAnswer() != null && questionModel.getAnswer().contains(",")) {
                                correctAnswer = Arrays.asList(questionModel.getAnswer().split(","));
                            } else {
                                correctAnswer.add(questionModel.getAnswer());
                            }
                            userAnswer.add(binding.edTextArea.getText().toString().trim());
                            data.get(index).setAnswers(userAnswer);
                            data.get(index).setUserAns(convertListToString(userAnswer));
                            if (data.get(index).isExactMatch()) {
                                if (areListsEqual(correctAnswer, userAnswer)) {
                                    totalScore++;
                                }
                            } else {

                                if (data.get(index).getKeywords() != null && data.get(index).getKeywords().size() > 0) {
                                    if (areAllStringsPresent(data.get(index).getKeywords(), binding.edTextArea.getText().toString().trim())) {
                                        totalScore++;
                                    }
                                } else if (areListsEqual(correctAnswer, userAnswer)) {
                                    totalScore++;
                                }

                            }

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // binding.nextBtn.setVisibility(View.GONE);
                                    nextQuestion();
                                }
                            }, 200);
                        } else {
                            //Toast.makeText(context, "Answer cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    } else if (questionModel.getQuestionType().equalsIgnoreCase("Single")) {

                        List<String> correctAnswer = new ArrayList<>();
                        if (questionModel.getAnswer() != null && questionModel.getAnswer().contains(",")) {
                            correctAnswer = Arrays.asList(questionModel.getAnswer().split(","));
                        } else {
                            correctAnswer.add(questionModel.getAnswer());
                        }
                        data.get(index).setAnswers(sortListsEqual(adapter.getAnswers()));
                        data.get(index).setUserAns(convertListToString(sortListsEqual(adapter.getAnswers())));
                        if (areListsEqual(correctAnswer, adapter.getAnswers())) {
                            totalScore++;
                        }
                        if (data.get(index).getUserAns() != null && !data.get(index).getUserAns().toString().isEmpty()) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //binding.nextBtn.setVisibility(View.GONE);
                                    nextQuestion();
                                }
                            }, 200);
                        } else {
                            Toast.makeText(context, "Please select at least one option", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        nextQuestion();
                    }

                }
            }
        });


        binding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevQuestion();
            }
        });
        setProgressBar(0);

        binding.seekQuizAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mediaPlayerManager != null && mediaPlayerManager.isPlaying()) {
                    mediaPlayerManager.pauseMediaPlayer();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayerManager != null) {
                    mediaPlayerManager.resumeMediaPlayer(seekBar.getProgress());
                    binding.ivPlayPause.setImageResource(R.drawable.ic_pause);
                }
            }
        });

        //mediaPlayerManager.prepareMediaPlayerFromUrl("https://file-examples.com/storage/fe1b07b09f67bcb9b96354c/2017/11/file_example_MP3_5MG.mp3");

        binding.ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerManager.isPlaying()) {
                    mediaPlayerManager.pauseMediaPlayer();
                    binding.ivPlayPause.setImageResource(R.drawable.ic_play);
                } else {
                    //mediaPlayerManager.startMediaPlayerFromUrl("https://file-examples.com/storage/fe1b07b09f67bcb9b96354c/2017/11/file_example_MP3_5MG.mp3");
                    mediaPlayerManager.startMediaPlayer();
                    binding.seekQuizAudio.setProgress(0); // Reset SeekBar
                    binding.ivPlayPause.setImageResource(R.drawable.ic_pause);
                }

            }
        });

        mediaPlayerManager.setPreparedListener(new NewMediaPlayerManager.PreparedListener() {
            @Override
            public void onPrepared(boolean isPrepared) {
                if (isPrepared) {
                    binding.rlImage.setVisibility(View.VISIBLE);
                    binding.tvSeekCurrentDuration.setText("00:00");

                    binding.tvSeekTotalDuration.setText("" + convertSecondsToMMSS(mediaPlayerManager.getDurationInSeconds()));
                    binding.seekQuizAudio.setMax(mediaPlayerManager.getDurationInSeconds());
                } else {
                    binding.rlImage.setVisibility(View.GONE);
                }
            }
        });


        mediaPlayerManager.setProgressListener(new NewMediaPlayerManager.PlaybackProgressListener() {
            @Override
            public void onProgress(int currentPosition) {
                binding.seekQuizAudio.setProgress((currentPosition / 1000));
                binding.tvSeekCurrentDuration.setText("" + convertSecondsToMMSS(mediaPlayerManager.getCurrentPositionInSeconds()));

            }
        });

        mediaPlayerManager.setOnMediaCompleteListener(() -> {
            binding.ivPlayPause.setImageResource(R.drawable.ic_play);
            mediaPlayerManager.resetMediaPlayer();
            binding.seekQuizAudio.setProgress(0);
            binding.tvSeekCurrentDuration.setText("00:00");
        });

    }

    private void setToolBar() {
        binding.includeToolbar.tvBack.setText(category);
        binding.includeToolbar.tvBack.setTextColor(getResources().getColor(R.color.white));
        binding.includeToolbar.ivBack.setColorFilter(getResources().getColor(R.color.white));
        binding.includeToolbar.ivMenu.setVisibility(View.GONE);
        binding.includeToolbar.llBack.setOnClickListener(v -> {
            onBackPressed();
        });

        setPrevBtn();

    }

    private void setQuestion(int ans) {
        if (data.get(index).getBgColor() != null) {
            binding.main.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(data.get(index).getBgColor())));
        }
        binding.tvQuestion.setText("Q." + (index + 1) + " " + data.get(index).getQuestion());
        String answer = data.get(index).getUserAns() != null ? data.get(index).getUserAns().toString() : "";
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayerManager != null && mediaPlayerManager.isPlaying()) {
                    mediaPlayerManager.stopMediaPlayer();
                    binding.seekQuizAudio.setProgress(0);
                    binding.ivPlayPause.setImageResource(R.drawable.ic_play);
                }
                if (exoPlayer != null && exoPlayer.isPlaying()) {
                    exoPlayer.stop();
                }
            }
        }, 1000);

        if (data.get(index).getContentType() != null) {
            if (data.get(index).getContentType().equalsIgnoreCase("Audio")) {
                binding.ivBearimage.setVisibility(View.GONE);
                binding.mcvAudioLayout.setVisibility(View.VISIBLE);
                binding.mcvVideo.setVisibility(View.GONE);
                binding.mcvQuizCard.setVisibility(View.GONE);
                mediaPlayerManager.prepareMediaPlayerFromUrl(data.get(index).getUrl());
//                if (BuildConfig.DEBUG) {
//                    mediaPlayerManager.prepareMediaPlayerFromUrl("https://file-examples.com/storage/fe47f62a2667e1ea098b647/2017/11/file_example_MP3_1MG.mp3");
//                } else {
//                    mediaPlayerManager.prepareMediaPlayerFromUrl(data.get(index).getUrl());
//                }
            } else if (data.get(index).getContentType().equalsIgnoreCase("Video")) {
                binding.ivBearimage.setVisibility(View.GONE);
                binding.mcvAudioLayout.setVisibility(View.GONE);
                binding.mcvVideo.setVisibility(View.VISIBLE);
                binding.mcvQuizCard.setVisibility(View.GONE);
                loadExoPlayer(data.get(index).getUrl());
            } else if (data.get(index).getContentType().equalsIgnoreCase("Image")) {
                binding.ivBearimage.setVisibility(View.GONE);
                binding.mcvAudioLayout.setVisibility(View.GONE);
                binding.mcvVideo.setVisibility(View.GONE);
                binding.mcvQuizCard.setVisibility(View.VISIBLE);
                Glide.with(this).load(data.get(index).getImage()).into(binding.ivImageQuestion);
            } else {
                binding.ivBearimage.setVisibility(View.VISIBLE);
                if (comingFrom.equalsIgnoreCase("feedback")) {
                    binding.ivBearimage.setImageDrawable(ContextCompat.getDrawable(QuizathonActivity.this, R.drawable.ic_bear_feedback_svg));
                }
                binding.mcvQuizCard.setVisibility(View.GONE);
                binding.mcvAudioLayout.setVisibility(View.GONE);
                binding.mcvQuizCard.setVisibility(View.GONE);
            }
        } else {
            if (comingFrom.equalsIgnoreCase("feedback")) {
                binding.ivBearimage.setImageDrawable(ContextCompat.getDrawable(QuizathonActivity.this, R.drawable.ic_bear_feedback_svg));
            }
            binding.ivBearimage.setVisibility(View.VISIBLE);
            binding.mcvAudioLayout.setVisibility(View.GONE);
            binding.mcvVideo.setVisibility(View.GONE);
            binding.mcvQuizCard.setVisibility(View.GONE);
        }
        if (data.get(index).getHints() != null && !data.get(index).getHints().isEmpty()) {
            binding.tvQuestionHint.setVisibility(View.VISIBLE);
            binding.tvQuestionHint.setText(data.get(index).getHints());
        } else {
            binding.tvQuestionHint.setVisibility(View.GONE);
        }
        if (data.get(index).getDisclaimer() != null && !data.get(index).getDisclaimer().isEmpty()) {
            binding.tvQuestionDisclaimer.setVisibility(View.VISIBLE);
            binding.tvQuestionDisclaimer.setText(data.get(index).getDisclaimer());
        } else {
            binding.tvQuestionDisclaimer.setVisibility(View.GONE);
        }

        isAttempted = false;
        if (data.get(index).getQuestionType() != null && data.get(index).getQuestionType().equalsIgnoreCase("Single") || data.get(index).getQuestionType().equalsIgnoreCase("Multi")) {
            binding.recyclerViewOption.setVisibility(View.VISIBLE);
            binding.edTextAnswer.setVisibility(View.GONE);
            binding.edTextArea.setVisibility(View.GONE);
            binding.tvTextCount.setVisibility(View.GONE);
            /*if (data.get(index).getQuestionType().equalsIgnoreCase("Multi")) {
                binding.tvDescription.setVisibility(View.VISIBLE);
                binding.tvDescription.setText("(Note: Select one or more options)");
                //binding.btnOptionSubmit.setVisibility(View.VISIBLE);
            } else {
                binding.tvDescription.setVisibility(View.GONE);
                binding.btnOptionSubmit.setVisibility(View.GONE);
            }*/
            adapter = new QuizathonOptionAdapter(context, data.get(index).getQuestionType(), data.get(index).options, data.get(index).answers, answer, new QuizathonOptionAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position, String name) {
                    if (index < data.size()) {
                        if (data.get(index).getUserAns() == null || data.get(index).getUserAns().equals("")) {
                            List<String> userAnswer = new ArrayList<>();
                            userAnswer.add(name);
                            data.get(index).setAnswers(userAnswer);
                            data.get(index).setUserAns(data.get(index).getOptions().get(position));
                            if (Objects.equals(data.get(index).getAnswer(), name)) {
                                totalScore++;
                            }

                        } else {
                            String prevAns = data.get(index).getUserAns().toString();
                            if (prevAns != name) {
                                List<String> userAnswer = new ArrayList<>();
                                userAnswer.add(name);
                                data.get(index).setAnswers(userAnswer);
                                data.get(index).setUserAns(data.get(index).getOptions().get(position));
                                if (Objects.equals(data.get(index).getAnswer(), name)) {
                                    totalScore++;
                                }
                            }
                        }
                        binding.recyclerViewOption.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // binding.nextBtn.setVisibility(View.GONE);
                                nextQuestion();
                            }
                        }, 200);
                    }
                }
            });
            binding.recyclerViewOption.setLayoutManager(new LinearLayoutManager(context));
            binding.recyclerViewOption.setAdapter(adapter);
        } else if (data.get(index).getQuestionType() != null && data.get(index).getQuestionType().equalsIgnoreCase("EditText")) {
            //binding.tvDescription.setVisibility(View.VISIBLE);
            //binding.tvDescription.setText("(Note:- Enter your answer here.)");
            //binding.btnOptionSubmit.setVisibility(View.VISIBLE);
            binding.recyclerViewOption.setVisibility(View.GONE);
            binding.edTextAnswer.setVisibility(View.VISIBLE);
            binding.tvTextCount.setVisibility(View.VISIBLE);
            binding.edTextArea.setVisibility(View.GONE);
            binding.edTextAnswer.requestFocus();
            if (comingFrom.equalsIgnoreCase("feedback")) {
                binding.edTextAnswer.setHint("Enter your Feedback");
            }
            int viewLength = 200;
            if (data.get(index).getUserAns() != null && !data.get(index).getUserAns().toString().isEmpty()) {
                binding.edTextAnswer.setText(data.get(index).getUserAns().toString());
            } else {
                binding.edTextAnswer.setText("");
            }
            if (data.get(index).getViewLength() != null && !data.get(index).getViewLength().isEmpty()) {
                int len = 200;
                try {
                    len = Integer.parseInt(data.get(index).getViewLength());

                } catch (Exception ex) {

                }
                viewLength = len;
                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(len);
                binding.edTextAnswer.setFilters(filterArray);
            }
            int finalViewLength = viewLength;
            int charLen = 0;
            if (binding.edTextAnswer.getText().toString() != null) {
                charLen = binding.edTextAnswer.getText().toString().length() > 0 ? binding.edTextAnswer.getText().toString().length() : 0;
            }
            binding.tvTextCount.setText(charLen + "/" + finalViewLength);
            binding.edTextAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    binding.tvTextCount.setText(charSequence.length() + "/" + finalViewLength);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        } else if (data.get(index).getQuestionType() != null && data.get(index).getQuestionType().equalsIgnoreCase("TextArea")) {
            int viewLength = 200;
            binding.recyclerViewOption.setVisibility(View.GONE);
            binding.edTextAnswer.setVisibility(View.GONE);
            binding.edTextArea.setVisibility(View.VISIBLE);

            if (data.get(index).getUserAns() != null && !data.get(index).getUserAns().toString().isEmpty()) {
                binding.edTextArea.setText(data.get(index).getUserAns().toString());
            } else {
                binding.edTextArea.setText("");
            }

            binding.tvTextCount.setVisibility(View.VISIBLE);
            binding.edTextArea.requestFocus();
            if (comingFrom.equalsIgnoreCase("feedback")) {
                binding.edTextArea.setHint("Enter your Feedback");
            }
            if (data.get(index).getViewLength() != null && !data.get(index).getViewLength().isEmpty()) {
                int len = 200;
                try {
                    len = Integer.parseInt(data.get(index).getViewLength());

                } catch (Exception ex) {

                }
                viewLength = len;
                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(len);
                binding.edTextArea.setFilters(filterArray);
            }
            int finalViewLength = viewLength;
            int charLen = 0;
            if (binding.edTextArea.getText().toString() != null) {
                charLen = binding.edTextArea.getText().toString().length() > 0 ? binding.edTextArea.getText().toString().length() : 0;
            }
            binding.tvTextCount.setText(charLen + "/" + finalViewLength);
            binding.edTextArea.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    binding.tvTextCount.setText(charSequence.length() + "/" + finalViewLength);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

    }

    public void nextQuestion() {
        index++;
        int ans = -1;
        if (index == (data.size() - 1)) {
            binding.nextText.setText("Submit");
            if (data.get(index).getQuestionType().equalsIgnoreCase("Single")) {
                binding.nextBtn.setVisibility(View.GONE);
            }
        }
        if (index <= (data.size() - 1)) {
            if (!comingFrom.equalsIgnoreCase("feedback")) {
                saveAllQuestionData();
            }
            if (data.get(index).getUserAns() != null && !data.get(index).getUserAns().equals("")) {
                //ans = (int) data.get(index).getUserAns();
                binding.nextBtn.setVisibility(View.VISIBLE);
            } else {
                //  binding.nextBtn.setVisibility(View.GONE);
            }
            setPrevBtn();
            setQuestion(ans);
            setProgressBar(index);
        } else {
            setProgressBar(index);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (comingFrom.equalsIgnoreCase("bannerQuiz")) {
                        saveQuestionData(true);
                    } else if (comingFrom.equalsIgnoreCase("Feedback")) {
                        saveQuestionData(true);
                    } else {
                        saveQuestionData(false);
                    }
                }
            }, 200);
        }

    }

    public boolean areAllStringsPresent(List<String> keywords, String input) {
        if (keywords == null || input == null || keywords.isEmpty()) {
            return false;
        }

        String trimmedInput = input.trim().toLowerCase(); // Trim and normalize case

        for (String keyword : keywords) {
            if (keyword == null || keyword.trim().isEmpty()) {
                continue; // Skip null or empty keywords
            }

            if (!trimmedInput.contains(keyword.trim().toLowerCase())) {
                return false; // If any keyword is not found
            }
        }

        return true;
    }

    public void prevQuestion() {
        binding.nextBtn.setVisibility(View.VISIBLE);
        index--;
        if (index > 0) {
            getPrevQuestion();
        } else {
            getPrevQuestion();
            setPrevBtn();
        }
    }

    private void getPrevQuestion() {
        binding.nextText.setText("Next");
        int ans = -1;
        if (data.get(index).getUserAns() != null) {
            try {
                ans = (int) data.get(index).getUserAns();
            } catch (Exception e) {

            }
        }
        setProgressBar(index);
        setQuestion(ans);
    }

    private void setPrevBtn() {
        if (index > 0) {
            binding.prevBtn.setVisibility(View.VISIBLE);
        } else {
            binding.prevBtn.setVisibility(View.INVISIBLE);
        }

        if (data != null && index == 0 && data.get(index).getUserAns() != null) {
            binding.nextBtn.setVisibility(View.VISIBLE);
        }

        if (index == 0 && data.get(index).getUserAns() == null) {
            // binding.nextBtn.setVisibility(View.GONE);
        }
    }

    private void setProgressBar(int value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.progressIndicator.setProgress(value, true);
        } else {
            binding.progressIndicator.setProgress(value);
        }
        binding.progressIndicator.setMax(data.size());
    }

    public void saveQuestionData(Boolean isFromDashboardBanner) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.show();
        totalScore = 0;
        for (QuestionModel q : data) {

            List<String> correctAnswer = new ArrayList<>();
            if (q.getAnswer() != null && q.getAnswer().contains(",")) {
                correctAnswer = Arrays.asList(q.getAnswer().split(","));
            } else {
                correctAnswer.add(q.getAnswer());
            }
            if (correctAnswer != null && q.getAnswers() != null && q.getAnswers().size() > 0) {
                if (q.isExactMatch() || !q.getQuestionType().equalsIgnoreCase("TextArea") && !q.getQuestionType().equalsIgnoreCase("EditText")) {
                    if (areListsEqual(correctAnswer, q.getAnswers())) {
                        totalScore++;
                    }
                } else {
                    if (q.getKeywords() != null && !q.getKeywords().isEmpty()) {
                        if (areAllStringsPresent(q.getKeywords(), q.getUserAns().toString().trim())) {
                            totalScore++;
                        }
                    } else if (areListsEqual(correctAnswer, q.getAnswers())) {
                        totalScore++;
                    }
                }
            }
        }

        Call<SaveQuizathonAnsResponse> call = null;
        if (comingFrom.equalsIgnoreCase("feedback")) {
            SaveFeedbackAnswerRequest saveFeedbackAnswerRequest = new SaveFeedbackAnswerRequest(new Gson().toJson(data), "Completed", qId);
            call = apiInterfaceWyh.SaveUserfeedback(SharedPref.getAuthToken(), saveFeedbackAnswerRequest);

        } else if (quizathonModel != null && quizathonModel.getQuizType() != null && quizathonModel.getQuizType().equalsIgnoreCase("Streak")) {
            SaveQuizathonAnswerRequest request = new SaveQuizathonAnswerRequest(qId, new Gson().toJson(data), "Completed", isRetake, RetakeId, String.valueOf(totalScore), String.valueOf(data.size()));
            call = apiInterfaceWyh.SaveStreakQuizAnswer(SharedPref.getAuthToken(), request);
        } else {
            SaveQuizathonAnswerRequest request = new SaveQuizathonAnswerRequest(qId, new Gson().toJson(data), "Completed", isRetake, RetakeId, String.valueOf(totalScore), String.valueOf(data.size()));
            call = apiInterfaceWyh.saveQuizathonAnswer(SharedPref.getAuthToken(), request);
            Log.d("Assign Response", new Gson().toJson(request));
        }


        call.enqueue(new Callback<SaveQuizathonAnsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SaveQuizathonAnsResponse> call, @NonNull Response<SaveQuizathonAnsResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                try {
                    if (response.body() != null && response.code() == 200) {
                        Log.d("Assign Response", new Gson().toJson(response.body()));
                        if (comingFrom != null && comingFrom.equalsIgnoreCase("feedback")) {
                            Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_save_answer_new_success));
                            Intent intent = new Intent(QuizathonActivity.this, QuizathonScoreActivity.class);
                            if (quizathonModel != null && quizathonModel.getQuizType() != null && quizathonModel.getQuizType().equalsIgnoreCase("Streak")) {
                                if (response.body().getData().getStreakModels() != null && !response.body().getData().getStreakModels().isEmpty()) {
                                    quizathonModel.setStreakModels(response.body().getData().getStreakModels());
                                }
                                intent = new Intent(QuizathonActivity.this, QuizathonTimeboundScoreActivity.class);
                            }
                            intent.putExtra("data", new Gson().toJson(data));
                            intent.putExtra("your_score", "" + totalScore);
                            intent.putExtra("total_score", "" + data.size());
                            intent.putExtra("CategoryName", categoryName);
                            intent.putExtra("Category", category);
                            intent.putExtra("qid", qId);
                            intent.putExtra("rewardDate", rewardDate);
                            intent.putExtra("comingFrom", comingFrom);
                            intent.putExtra("rewardText", "");
                            intent.putExtra("type", type);
                            intent.putExtra("withActivity", response.body().getData().getWithActivity());
                            intent.putExtra("quizdata", response.body().getData());
                            if (quizathonModel == null) {
                                quizathonModel = new QuizathonModel();
                            }
                            if (response.body().getData() != null && response.body().getData().getShowConsent() != null) {
                                quizathonModel.setShowConsent(response.body().getData().getShowConsent());
                            }

                            if (response.body().getData() != null) {
                                quizathonModel.setActivityImageUploaded(response.body().getData().getActivityImageUploaded());
                            }
                            if (response.body().getData() != null && response.body().getData().getImageStatus() != null) {
                                quizathonModel.setImageStatus(response.body().getData().getImageStatus());
                            }
                            if (response.body().getData() != null && response.body().getData().getRedirectionKey() != null) {
                                quizathonModel.setRedirectionKey(response.body().getData().getRedirectionKey());
                            }
                            if (response.body().getData() != null && response.body().getData().getActivityTransId() != null && !response.body().getData().getActivityTransId().isEmpty()) {
                                quizathonModel.setTransId(Integer.parseInt(response.body().getData().getActivityTransId()));
                            }
                            intent.putExtra("quizModel", quizathonModel);
                            if (response.body().getFeedbackRewardModel() != null) {
                                intent.putExtra("quizrewards", response.body().getFeedbackRewardModel());
                            } else if (response.body().getRewards() != null && response.body().getRewards().getReward() != null) {
                                intent.putExtra("rewards", response.body().getRewards().getReward());
                            } else {
                                intent.putExtra("rewards", "");
                            }

                            if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null) {
                                intent.putExtra("rewardsBonus", response.body().getRewards().getBonusRewards());
                            } else {
                                intent.putExtra("rewardsBonus", "");
                            }

                            if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                                intent.putExtra("stamps", response.body().getEnGTokens().getTokens());
                            } else {
                                intent.putExtra("stamps", "");
                            }

                            if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                                intent.putExtra("stampsBonus", response.body().getEnGTokens().getBonusTokens());
                            } else {
                                intent.putExtra("stampsBonus", "");
                            }

                            startActivity(intent);
                            Toast.makeText(QuizathonActivity.this, "Your feedback submitted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (comingFrom != null && !comingFrom.equalsIgnoreCase("feedback")) {
                            Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_save_answer_new_success));
                            Intent intent = new Intent(QuizathonActivity.this, QuizathonScoreActivity.class);
                            if (quizathonModel != null && quizathonModel.getQuizType() != null && quizathonModel.getQuizType().equalsIgnoreCase("Streak")) {
                                if (response.body().getData().getStreakModels() != null && !response.body().getData().getStreakModels().isEmpty()) {
                                    quizathonModel.setStreakModels(response.body().getData().getStreakModels());
                                }
                                intent = new Intent(QuizathonActivity.this, QuizathonTimeboundScoreActivity.class);
                            }
                            intent.putExtra("data", new Gson().toJson(data));
                            intent.putExtra("your_score", "" + totalScore);
                            intent.putExtra("total_score", "" + data.size());
                            intent.putExtra("CategoryName", categoryName);
                            intent.putExtra("Category", category);
                            intent.putExtra("qid", qId);
                            intent.putExtra("rewardDate", rewardDate);
                            //intent.putExtra("data_savequiz", response.body().getData().getMe);
                            intent.putExtra("comingFrom", comingFrom);
                            intent.putExtra("rewardText", "");
                            intent.putExtra("type", type);
                            intent.putExtra("quizdata", type);
                            //intent.putExtra("quizModel", quizathonModel);
                            if (comingFrom != null && !comingFrom.equalsIgnoreCase("feedback") && response.body().getData() != null) {
                                intent.putExtra("withActivity", response.body().getData().getWithActivity());
                                intent.putExtra("isRetakeAvailable", response.body().getData().getRetakeAvailable());
                                intent.putExtra("hasSufficientBalance", response.body().getData().getHasSufficientBalance());
                                intent.putExtra("retakePointstoBurn", response.body().getData().getRetakePointstoBurn());
                                intent.putExtra("quizdata", response.body().getData());
                                intent.putExtra("feedback", response.body().getFeedbackDetails());
                                intent.putExtra("showNextGameButton", response.body().getData().getShowNextGameButton());
                                intent.putExtra("nextGameRedirectTo", response.body().getData().getRedirectTo());
                                if (quizathonModel == null) {
                                    quizathonModel = new QuizathonModel();
                                }
                                if (response.body().getData() != null && response.body().getData().getShowConsent() != null) {
                                    quizathonModel.setShowConsent(response.body().getData().getShowConsent());
                                }
                                if (response.body().getData() != null && response.body().getData().getRewardName() != null) {
                                    quizathonModel.setRewardName(response.body().getData().getRewardName());
                                }
                                if (response.body().getData() != null && response.body().getData().getRewardIcon() != null) {
                                    quizathonModel.setRewardIcon(response.body().getData().getRewardIcon());
                                }
                                if (response.body().getData() != null && response.body().getData().getRewardDescription() != null) {
                                    quizathonModel.setRewardDescription(response.body().getData().getRewardDescription());
                                }
                                if (response.body().getData() != null && response.body().getData().getImageStatus() != null) {
                                    quizathonModel.setImageStatus(response.body().getData().getImageStatus());
                                }
                                if (response.body().getData() != null && response.body().getData().getRedirectionKey() != null) {
                                    quizathonModel.setRedirectionKey(response.body().getData().getRedirectionKey());
                                }
                                if (response.body().getData() != null) {
                                    quizathonModel.setActivityImageUploaded(response.body().getData().getActivityImageUploaded());
                                }
                                if (response.body().getData() != null && response.body().getData().getActivityTransId() != null && !response.body().getData().getActivityTransId().isEmpty()) {
                                    quizathonModel.setTransId(Integer.parseInt(response.body().getData().getActivityTransId()));
                                }
                            }
                            intent.putExtra("quizModel", quizathonModel);

                            if (response.body().getData().getDialog_model() != null) {
                                intent.putExtra("dialogModel", response.body().getData().getDialog_model());
                            }
                            if (response.body().getQuizathonRewardData() != null) {
                                intent.putExtra("quizrewards", response.body().getQuizathonRewardData());
                            } else if (response.body().getRewards() != null && response.body().getRewards().getReward() != null) {
                                intent.putExtra("rewards", response.body().getRewards().getReward());
                            } else {
                                intent.putExtra("rewards", "");
                            }
                            if (response.body().getRewards() != null && response.body().getRewards().getBonusRewards() != null) {
                                intent.putExtra("rewardsBonus", response.body().getRewards().getBonusRewards());
                            } else {
                                intent.putExtra("rewardsBonus", "");
                            }

                            if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getTokens() != null) {
                                intent.putExtra("stamps", response.body().getEnGTokens().getTokens());
                            } else {
                                intent.putExtra("stamps", "");
                            }

                            if (response.body().getEnGTokens() != null && response.body().getEnGTokens().getBonusTokens() != null) {
                                intent.putExtra("stampsBonus", response.body().getEnGTokens().getBonusTokens());
                            } else {
                                intent.putExtra("stampsBonus", "");
                            }

                            startActivity(intent);
                            finish();
                        } else {
                            Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_save_answer_new_failed));
                            Toast.makeText(context, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SaveQuizathonAnsResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_save_answer_new_failed));
                Toast.makeText(QuizathonActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveAllQuestionData() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
        Call<SaveQuizathonAnsResponse> call = null;
        if (quizathonModel != null && quizathonModel.getQuizType() != null && quizathonModel.getQuizType().equalsIgnoreCase("Streak")) {
            SaveQuizathonAnswerRequest request = new SaveQuizathonAnswerRequest(qId, new Gson().toJson(data), "InProgress", isRetake, RetakeId, String.valueOf(totalScore), String.valueOf(data.size()));
            call = apiInterfaceWyh.SaveStreakQuizAnswer(SharedPref.getAuthToken(), request);
        } else {
            SaveQuizathonAnswerRequest request = new SaveQuizathonAnswerRequest(qId, new Gson().toJson(data), "InProgress", isRetake, RetakeId, String.valueOf(totalScore), String.valueOf(data.size()));
            call = apiInterfaceWyh.saveQuizathonAnswer(SharedPref.getAuthToken(), request);

        }

        call.enqueue(new Callback<SaveQuizathonAnsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SaveQuizathonAnsResponse> call, @NonNull Response<SaveQuizathonAnsResponse> response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SaveQuizathonAnsResponse> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                Analytics.logEvent(context, context.getClass().getName(), getString(R.string.quiz_save_answer_new_failed));
                Toast.makeText(QuizathonActivity.this, getResources().getString(R.string.error_string), Toast.LENGTH_SHORT).show();
            }
        });
    }


//    public static boolean areListsEqual(List<String> list1, List<String> list2) {
//        // Sort both lists alphabetically
//        list1.replaceAll(String::trim);
//        list2.replaceAll(String::trim);
//        Collections.sort(list1);
//        Collections.sort(list2);
//
//        // Compare the sorted lists
//        return list1.equals(list2);
//    }

    public static boolean areListsEqual(List<String> list1, List<String> list2) {
        try {
            // Trim spaces and convert all elements to lowercase
            list1.replaceAll(s -> s.trim().toLowerCase());
            list2.replaceAll(s -> s.trim().toLowerCase());

            // Sort both lists
            Collections.sort(list1);
            Collections.sort(list2);

            // Compare the sorted lists
            return list1.equals(list2);
        } catch (Exception ex) {
            return false;
        }
    }

    public static List<String> sortListsEqual(List<String> list1) {
        // Sort both lists alphabetically
        list1.replaceAll(String::trim);
        Collections.sort(list1);

        // Compare the sorted lists
        return list1;
    }

    public void loadExoPlayer(String url) {
        MediaItem mediaItem = MediaItem.fromUri(url);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayerManager != null && mediaPlayerManager.isPlaying()) {
            mediaPlayerManager.stopMediaPlayer();
        }
        if (exoPlayer != null && exoPlayer.isPlaying()) {
            exoPlayer.stop();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayerManager != null && mediaPlayerManager.isPlaying()) {
            mediaPlayerManager.pauseMediaPlayer();
            binding.ivPlayPause.setImageResource(R.drawable.ic_play);
        }
        if (exoPlayer != null && exoPlayer.isPlaying()) {
            exoPlayer.pause();
        }
    }

    public static boolean checkStartOrEndSpaces(Context context, String input) {
        input = input.trim();
        if (input == null || input.trim().isEmpty()) {
            Toast.makeText(context, "Answer cannot be empty", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (input.startsWith(" ") || input.endsWith(" ")) {
            Toast.makeText(context, "Answer contains spaces at the start or end", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static String convertSecondsToMMSS(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}