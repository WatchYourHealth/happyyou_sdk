package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.CommonSuccessResponse;

import java.util.List;

public class FunZoneHistoryResponse extends CommonSuccessResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("affirmations")
        @Expose
        private List<String> affirmations;
        @SerializedName("jokes")
        @Expose
        private List<String> jokes;
        @SerializedName("riddles")
        @Expose
        private List<Riddle> riddles;
        @SerializedName("thoughOfTheDay")
        @Expose
        private List<String> thoughOfTheDay;

        public List<String> getAffirmations() {
            return affirmations;
        }

        public void setAffirmations(List<String> affirmations) {
            this.affirmations = affirmations;
        }

        public List<String> getJokes() {
            return jokes;
        }

        public void setJokes(List<String> jokes) {
            this.jokes = jokes;
        }

        public List<Riddle> getRiddles() {
            return riddles;
        }

        public void setRiddles(List<Riddle> riddles) {
            this.riddles = riddles;
        }

        public List<String> getThoughOfTheDay() {
            return thoughOfTheDay;
        }

        public void setThoughOfTheDay(List<String> thoughOfTheDay) {
            this.thoughOfTheDay = thoughOfTheDay;
        }

    }


    public static class Riddle {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("question")
        @Expose
        private String question;
        @SerializedName("hint1")
        @Expose
        private String hint1;
        @SerializedName("hint2")
        @Expose
        private String hint2;
        @SerializedName("answer")
        @Expose
        private String answer;
        @SerializedName("userAnswer")
        @Expose
        private String userAnswer;

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

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }
    }
}
