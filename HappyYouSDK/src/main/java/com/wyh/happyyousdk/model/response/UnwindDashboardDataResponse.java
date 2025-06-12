package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UnwindDashboardDataResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private Object enGTokens;
    @SerializedName("rewards")
    @Expose
    private Object rewards;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public Object getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(Object enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Object getRewards() {
        return rewards;
    }

    public void setRewards(Object rewards) {
        this.rewards = rewards;
    }

    public class Data {

        @SerializedName("imagePath")
        @Expose
        private List<String> imagePath;
        @SerializedName("message")
        @Expose
        private List<String> message;
        @SerializedName("joke")
        @Expose
        private List<String> joke;
        @SerializedName("riddle")
        @Expose
        private Riddle riddle;


        public List<String> getImagePath() {
            return imagePath;
        }

        public void setImagePath(List<String> imagePath) {
            this.imagePath = imagePath;
        }

        public List<String> getMessage() {
            return message;
        }

        public void setMessage(List<String> message) {
            this.message = message;
        }

        public List<String> getJoke() {
            return joke;
        }

        public void setJoke(List<String> joke) {
            this.joke = joke;
        }

        public Riddle getRiddle() {
            return riddle;
        }

        public void setRiddle(Riddle riddle) {
            this.riddle = riddle;
        }
    }

    public static class Riddle {
        @SerializedName("question")
        @Expose
        private String question;
        @SerializedName("hint1")
        @Expose
        private String hint1;
        @SerializedName("hint2")
        @Expose
        private String hint2;
        @SerializedName("option")
        @Expose
        private List<String> option;
        @SerializedName("answer")
        @Expose
        private String answer;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getHint1() {
            return hint1;
        }

        public void setHint1(String hint1) {
            this.hint1 = hint1;
        }

        public String getHint2() {
            return hint2;
        }

        public void setHint2(String hint2) {
            this.hint2 = hint2;
        }

        public List<String> getOption() {
            return option;
        }

        public void setOption(List<String> option) {
            this.option = option;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }


}