package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardRequest {
    @SerializedName("TimeOfTheDay")
    @Expose
    private TimeOfTheDay timeOfTheDay;
    @SerializedName("FetchWeather")
    @Expose
    private FetchWeather fetchWeather;

    public DashboardRequest(TimeOfTheDay timeOfTheDay, FetchWeather fetchWeather) {
        this.timeOfTheDay = timeOfTheDay;
        this.fetchWeather = fetchWeather;
    }

    public TimeOfTheDay getTimeOfTheDay() {
        return timeOfTheDay;
    }

    public void setTimeOfTheDay(TimeOfTheDay timeOfTheDay) {
        this.timeOfTheDay = timeOfTheDay;
    }

    public FetchWeather getFetchWeather() {
        return fetchWeather;
    }

    public void setFetchWeather(FetchWeather fetchWeather) {
        this.fetchWeather = fetchWeather;
    }

    public static class FetchWeather {

        @SerializedName("City")
        @Expose
        private String city;

        public FetchWeather(String city) {
            this.city = city;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

    }

    public static class TimeOfTheDay {

        @SerializedName("timeOfDay")
        @Expose
        private String timeOfDay;

        public TimeOfTheDay(String timeOfDay) {
            this.timeOfDay = timeOfDay;
        }

        public String getTimeOfDay() {
            return timeOfDay;
        }

        public void setTimeOfDay(String timeOfDay) {
            this.timeOfDay = timeOfDay;
        }

    }
}

