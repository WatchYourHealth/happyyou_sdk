package com.wyh.happyyousdk.model.response.trends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class FetchGraphResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;

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
    public class Data {

        @SerializedName("uuid")
        @Expose
        private String uuid;
        @SerializedName("periodType")
        @Expose
        private String periodType;
        @SerializedName("periodIndex")
        @Expose
        private Integer periodIndex;
        @SerializedName("activityType")
        @Expose
        private String activityType;
        @SerializedName("dataPoints")
        @Expose
        private List<DataPoint> dataPoints = null;
        @SerializedName("dateRange")
        @Expose
        private DateRange dateRange;
        @SerializedName("highestpoint")
        @Expose
        private Highestpoint highestpoint;
        @SerializedName("avgDataPoints")
        @Expose
        private Integer avgDataPoints;
        @SerializedName("goal")
        @Expose
        private Integer goal;
        @SerializedName("todayPoint")
        @Expose
        private Integer todayPoint;

        @SerializedName("waterIntakeGoal")
        @Expose
        private String waterIntakeGoal;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getPeriodType() {
            return periodType;
        }

        public void setPeriodType(String periodType) {
            this.periodType = periodType;
        }

        public Integer getPeriodIndex() {
            return periodIndex;
        }

        public void setPeriodIndex(Integer periodIndex) {
            this.periodIndex = periodIndex;
        }

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public List<DataPoint> getDataPoints() {
            return dataPoints;
        }

        public void setDataPoints(List<DataPoint> dataPoints) {
            this.dataPoints = dataPoints;
        }

        public DateRange getDateRange() {
            return dateRange;
        }

        public void setDateRange(DateRange dateRange) {
            this.dateRange = dateRange;
        }

        public Highestpoint getHighestpoint() {
            return highestpoint;
        }

        public void setHighestpoint(Highestpoint highestpoint) {
            this.highestpoint = highestpoint;
        }

        public Integer getAvgDataPoints() {
            return avgDataPoints;
        }

        public void setAvgDataPoints(Integer avgDataPoints) {
            this.avgDataPoints = avgDataPoints;
        }

        public Integer getGoal() {
            return goal;
        }

        public void setGoal(Integer goal) {
            this.goal = goal;
        }
        public Integer getTodayPoint() {
            return todayPoint;
        }

        public void setTodayPoint(Integer todayPoint) {
            this.todayPoint = todayPoint;
        }

        public String getWaterIntakeGoal() {
            return waterIntakeGoal;
        }
    }

    public class DataPoint {

        @SerializedName("rtId")
        @Expose
        private Integer rtId;
        @SerializedName("recordDate")
        @Expose
        private String recordDate;
        @SerializedName("point")
        @Expose
        private Double point;
        @SerializedName("weeklyRange")
        @Expose
        private String weeklyRange;

        public Integer getRtId() {
            return rtId;
        }

        public void setRtId(Integer rtId) {
            this.rtId = rtId;
        }

        public String getRecordDate() {
            return recordDate;
        }

        public void setRecordDate(String recordDate) {
            this.recordDate = recordDate;
        }

        public String getWeeklyRange() {
            return weeklyRange;
        }

        public void setWeeklyRange(String weeklyRange) {
            this.weeklyRange = weeklyRange;
        }

        public Double getPoint() {
            return point;
        }

        public void setPoint(Double point) {
            this.point = point;
        }

    }

    public class DateRange {

        @SerializedName("startDate")
        @Expose
        private String startDate;
        @SerializedName("endDate")
        @Expose
        private String endDate;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

    }
    public class Highestpoint {

        @SerializedName("timestamp")
        @Expose
        private String timestamp;
        @SerializedName("points")
        @Expose
        private Integer points;

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public Integer getPoints() {
            return points;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }

    }

}

