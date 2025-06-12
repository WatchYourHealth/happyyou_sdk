package com.wyh.happyyousdk.model.response.rewards;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;

import java.util.List;

public class PendingActivitiesResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("freevoucher")
    @Expose
    private String freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;

    public PendingActivitiesResponse() {
    }

    public PendingActivitiesResponse(String msg, Boolean success, List<Datum> data, String freevoucher, EnGTokensModel enGTokens) {
        super();
        this.msg = msg;
        this.success = success;
        this.data = data;
        this.freevoucher = freevoucher;
        this.enGTokens = enGTokens;
    }

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(String freevoucher) {
        this.freevoucher = freevoucher;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }

    public class Datum {

        @SerializedName("EventId")
        @Expose
        private Integer eventId;
        @SerializedName("EventName")
        @Expose
        private String eventName;
        @SerializedName("EventToken")
        @Expose
        private Integer eventToken;
        @SerializedName("EventType")
        @Expose
        private String eventType;
        @SerializedName("IsStarted")
        @Expose
        private Integer isStarted;
        @SerializedName("IsCompleted")
        @Expose
        private Integer isCompleted;
        @SerializedName("EventStartDate")
        @Expose
        private String eventStartDate;
        @SerializedName("EventEndDate")
        @Expose
        private String eventEndDate;
        @SerializedName("EventLogo")
        @Expose
        private String eventLogo;
        @SerializedName("EventDescription")
        @Expose
        private String eventDescription;
        @SerializedName("whatTo")
        @Expose
        private String whatTo;
        @SerializedName("howTo")
        @Expose
        private String howTo;
        @SerializedName("whyTo")
        @Expose
        private String whyTo;
        @SerializedName("ProgressPercentage")
        @Expose
        private Integer progressPercentage;

        @SerializedName("positionColor")
        @Expose
        private Integer positionColor;

        public Integer getEventId() {
            return eventId;
        }

        public void setEventId(Integer eventId) {
            this.eventId = eventId;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public Integer getEventToken() {
            return eventToken;
        }

        public void setEventToken(Integer eventToken) {
            this.eventToken = eventToken;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Integer getIsStarted() {
            return isStarted;
        }

        public void setIsStarted(Integer isStarted) {
            this.isStarted = isStarted;
        }

        public String getEventStartDate() {
            return eventStartDate;
        }

        public void setEventStartDate(String eventStartDate) {
            this.eventStartDate = eventStartDate;
        }

        public String getEventEndDate() {
            return eventEndDate;
        }

        public void setEventEndDate(String eventEndDate) {
            this.eventEndDate = eventEndDate;
        }

        public String getEventLogo() {
            return eventLogo;
        }

        public void setEventLogo(String eventLogo) {
            this.eventLogo = eventLogo;
        }

        public String getEventDescription() {
            return eventDescription;
        }

        public void setEventDescription(String eventDescription) {
            this.eventDescription = eventDescription;
        }

        public Integer getIsCompleted() {
            return isCompleted;
        }

        public void setIsCompleted(Integer isCompleted) {
            this.isCompleted = isCompleted;
        }

        public String getWhatTo() {
            return whatTo;
        }

        public void setWhatTo(String whatTo) {
            this.whatTo = whatTo;
        }

        public String getHowTo() {
            return howTo;
        }

        public void setHowTo(String howTo) {
            this.howTo = howTo;
        }

        public String getWhyTo() {
            return whyTo;
        }

        public void setWhyTo(String whyTo) {
            this.whyTo = whyTo;
        }

        public Integer getProgressPercentage() {
            return progressPercentage;
        }

        public void setProgressPercentage(Integer progressPercentage) {
            this.progressPercentage = progressPercentage;
        }
        public Integer getPositionColor() {
            return positionColor;
        }

        public void setPositionColor(Integer positionColor) {
            this.positionColor = positionColor;
        }
    }

}
