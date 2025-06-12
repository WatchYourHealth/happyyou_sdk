package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultipleGraphDataResponse {

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

        @SerializedName("happyinsights")
        @Expose
        private Happyinsights happyinsights;
        @SerializedName("diagnosticGraph")
        @Expose
        private DiagnosticGraph diagnosticGraph;
        @SerializedName("diagnosticsType")
        @Expose
        private Object diagnosticsType;

        public Happyinsights getHappyinsights() {
            return happyinsights;
        }

        public void setHappyinsights(Happyinsights happyinsights) {
            this.happyinsights = happyinsights;
        }

        public DiagnosticGraph getDiagnosticGraph() {
            return diagnosticGraph;
        }

        public void setDiagnosticGraph(DiagnosticGraph diagnosticGraph) {
            this.diagnosticGraph = diagnosticGraph;
        }

        public Object getDiagnosticsType() {
            return diagnosticsType;
        }

        public void setDiagnosticsType(Object diagnosticsType) {
            this.diagnosticsType = diagnosticsType;
        }


        public class DiagnosticGraph {

            @SerializedName("recordDate")
            @Expose
            private List<RecordDate> recordDate;

            public List<RecordDate> getRecordDate() {
                return recordDate;
            }

            public void setRecordDate(List<RecordDate> recordDate) {
                this.recordDate = recordDate;
            }

            public class RecordDate {

                @SerializedName("graphdate")
                @Expose
                private String graphdate;
                @SerializedName("weeklyRange")
                @Expose
                private String weeklyRange;
                @SerializedName("point")
                @Expose
                private List<Point__1> point;


                public String getGraphdate() {
                    return graphdate;
                }

                public void setGraphdate(String graphdate) {
                    this.graphdate = graphdate;
                }

                public String getWeeklyRange() {
                    return weeklyRange;
                }

                public void setWeeklyRange(String weeklyRange) {
                    this.weeklyRange = weeklyRange;
                }

                public List<Point__1> getPoint() {
                    return point;
                }

                public void setPoint(List<Point__1> point) {
                    this.point = point;
                }

                public class Point__1 {

                    @SerializedName("point")
                    @Expose
                    private Double point;
                    @SerializedName("name")
                    @Expose
                    private String name;
                    @SerializedName("userId")
                    @Expose
                    private String userId;
                    @SerializedName("diffpoint")
                    @Expose
                    private Double diffpoint;

                    public Double getPoint() {
                        return point;
                    }

                    public void setPoint(Double point) {
                        this.point = point;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getUserId() {
                        return userId;
                    }

                    public void setUserId(String userId) {
                        this.userId = userId;
                    }

                    public Double getDiffpoint() {
                        return diffpoint;
                    }

                    public void setDiffpoint(Double diffpoint) {
                        this.diffpoint = diffpoint;
                    }

                }


            }

        }

        public class Happyinsights {

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
            private List<DataPoint> dataPoints;

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

            public class DataPoint {

                @SerializedName("rtId")
                @Expose
                private Integer rtId;
                @SerializedName("recordDate")
                @Expose
                private String recordDate;
                @SerializedName("weeklyRange")
                @Expose
                private String weeklyRange;
                @SerializedName("point")
                @Expose
                private List<Point> point;

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

                public List<Point> getPoint() {
                    return point;
                }

                public void setPoint(List<Point> point) {
                    this.point = point;
                }

                public class Point {

                    @SerializedName("point")
                    @Expose
                    private Double point;
                    @SerializedName("name")
                    @Expose
                    private String name;
                    @SerializedName("userId")
                    @Expose
                    private String userId;
                    @SerializedName("diffpoint")
                    @Expose
                    private Double diffpoint;

                    public Double getPoint() {
                        return point;
                    }

                    public void setPoint(Double point) {
                        this.point = point;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getUserId() {
                        return userId;
                    }

                    public void setUserId(String userId) {
                        this.userId = userId;
                    }

                    public Double getDiffpoint() {
                        return diffpoint;
                    }

                    public void setDiffpoint(Double diffpoint) {
                        this.diffpoint = diffpoint;
                    }

                }
            }
        }

    }
}
