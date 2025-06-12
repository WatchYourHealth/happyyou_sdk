package com.wyh.happyyousdk.model.response.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GetDailyProgramResponse1 {

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
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("UUID")
        @Expose
        private String uuid;
        @SerializedName("ProgramID")
        @Expose
        private String programID;
        @SerializedName("ProgramList")
        @Expose
        private List<Program> programList = null;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getProgramID() {
            return programID;
        }

        public void setProgramID(String programID) {
            this.programID = programID;
        }

        public List<Program> getProgramList() {
            return programList;
        }

        public void setProgramList(List<Program> programList) {
            this.programList = programList;
        }
        public class Program {

            @SerializedName("TrackedOn")
            @Expose
            private String trackedOn;
            @SerializedName("ProgramName")
            @Expose
            private String programName;
            @SerializedName("ProgramValue")
            @Expose
            private String programValue;
            @SerializedName("Status")
            @Expose
            private String status;

            public String getTrackedOn() {
                return trackedOn;
            }

            public void setTrackedOn(String trackedOn) {
                this.trackedOn = trackedOn;
            }

            public String getProgramName() {
                return programName;
            }

            public void setProgramName(String programName) {
                this.programName = programName;
            }

            public String getProgramValue() {
                return programValue;
            }

            public void setProgramValue(String programValue) {
                this.programValue = programValue;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

        }


    }


}



