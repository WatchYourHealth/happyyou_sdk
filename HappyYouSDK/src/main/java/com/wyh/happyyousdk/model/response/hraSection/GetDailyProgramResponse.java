package com.wyh.happyyousdk.model.response.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDailyProgramResponse {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StatusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("Data")
    @Expose
    private List<Datum> data = null;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("UUID")
        @Expose
        private String uuid;
        //        @SerializedName("ProgramID")
//        @Expose
//        private String programID;
        @SerializedName("TrackedOn")
        @Expose
        private String trackedOn;
        @SerializedName("QuitSmoking")
        @Expose
        private Object quitSmoking;
        @SerializedName("ReducedAlcohol")
        @Expose
        private Object reducedAlcohol;
        @SerializedName("DoYoga")
        @Expose
        private Object doYoga;
        @SerializedName("GoGymDaily")
        @Expose
        private Object goGymDaily;
        @SerializedName("ReadDaily")
        @Expose
        private Object readDaily;
        @SerializedName("GoBedEarly")
        @Expose
        private Object goBedEarly;
        @SerializedName("BeOccupied")
        @Expose
        private Object beOccupied;
        @SerializedName("ToDoList")
        @Expose
        private Object toDoList;
        @SerializedName("WriteGoodThings")
        @Expose
        private Object writeGoodThings;
        @SerializedName("WakeUpEarly")
        @Expose
        private Object wakeUpEarly;

        @SerializedName("Status")
        @Expose
        private String status;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

//        public String getProgramID() {
//            return programID;
//        }
//
//        public void setProgramID(String programID) {
//            this.programID = programID;
//        }

        public String getTrackedOn() {
            return trackedOn;
        }

        public void setTrackedOn(String trackedOn) {
            this.trackedOn = trackedOn;
        }

        public Object getQuitSmoking() {
            return quitSmoking;
        }

        public void setQuitSmoking(Object quitSmoking) {
            this.quitSmoking = quitSmoking;
        }

        public Object getReducedAlcohol() {
            return reducedAlcohol;
        }

        public void setReducedAlcohol(Object reducedAlcohol) {
            this.reducedAlcohol = reducedAlcohol;
        }

        public Object getDoYoga() {
            return doYoga;
        }

        public void setDoYoga(Object doYoga) {
            this.doYoga = doYoga;
        }

        public Object getGoGymDaily() {
            return goGymDaily;
        }

        public void setGoGymDaily(Object goGymDaily) {
            this.goGymDaily = goGymDaily;
        }

        public Object getReadDaily() {
            return readDaily;
        }

        public void setReadDaily(Object readDaily) {
            this.readDaily = readDaily;
        }

        public Object getGoBedEarly() {
            return goBedEarly;
        }

        public void setGoBedEarly(Object goBedEarly) {
            this.goBedEarly = goBedEarly;
        }

        public Object getBeOccupied() {
            return beOccupied;
        }

        public void setBeOccupied(Object beOccupied) {
            this.beOccupied = beOccupied;
        }

        public Object getToDoList() {
            return toDoList;
        }

        public void setToDoList(Object toDoList) {
            this.toDoList = toDoList;
        }

        public Object getWriteGoodThings() {
            return writeGoodThings;
        }

        public void setWriteGoodThings(Object writeGoodThings) {
            this.writeGoodThings = writeGoodThings;
        }

        public Object getWakeUpEarly() {
            return wakeUpEarly;
        }

        public void setWakeUpEarly(Object wakeUpEarly) {
            this.wakeUpEarly = wakeUpEarly;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

}

