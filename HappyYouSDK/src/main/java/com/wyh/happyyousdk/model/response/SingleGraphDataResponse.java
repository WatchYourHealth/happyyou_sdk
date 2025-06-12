package com.wyh.happyyousdk.model.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SingleGraphDataResponse {

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

            public DiagnosticGraph(List<RecordDate> recordDate) {
                super();
                this.recordDate = recordDate;
            }

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
                @SerializedName("point")
                @Expose
                private List<Point> point;


                public String getGraphdate() {
                    return graphdate;
                }

                public void setGraphdate(String graphdate) {
                    this.graphdate = graphdate;
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
                    private Integer point;
                    @SerializedName("name")
                    @Expose
                    private String name;

                    /**
                     * No args constructor for use in serialization
                     */
                    public Point() {
                    }

                    /**
                     * @param name
                     * @param point
                     */
                    public Point(Integer point, String name) {
                        super();
                        this.point = point;
                        this.name = name;
                    }

                    public Integer getPoint() {
                        return point;
                    }

                    public void setPoint(Integer point) {
                        this.point = point;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                }


            }


        }

        public class Happyinsights {

            @SerializedName("uuid")
            @Expose
            private Object uuid;
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
            @SerializedName("dateRange")
            @Expose
            private DateRange dateRange;
            @SerializedName("highestpoint")
            @Expose
            private Highestpoint highestpoint;
            @SerializedName("avgDataPoints")
            @Expose
            private Integer avgDataPoints;
            @SerializedName("aboutText")
            @Expose
            private Object aboutText;


            public Object getUuid() {
                return uuid;
            }

            public void setUuid(Object uuid) {
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

            public Object getAboutText() {
                return aboutText;
            }

            public void setAboutText(Object aboutText) {
                this.aboutText = aboutText;
            }

            public class DateRange {

                @SerializedName("startDate")
                @Expose
                private String startDate;
                @SerializedName("endDate")
                @Expose
                private String endDate;

                /**
                 * No args constructor for use in serialization
                 */
                public DateRange() {
                }

                /**
                 * @param endDate
                 * @param startDate
                 */
                public DateRange(String startDate, String endDate) {
                    super();
                    this.startDate = startDate;
                    this.endDate = endDate;
                }

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

            public class DataPoint {

                @SerializedName("rtId")
                @Expose
                private Integer rtId;
                @SerializedName("recordDate")
                @Expose
                private String recordDate;
                @SerializedName("point")
                @Expose
                private Integer point;

                /**
                 * No args constructor for use in serialization
                 */
                public DataPoint() {
                }

                /**
                 * @param rtId
                 * @param recordDate
                 * @param point
                 */
                public DataPoint(Integer rtId, String recordDate, Integer point) {
                    super();
                    this.rtId = rtId;
                    this.recordDate = recordDate;
                    this.point = point;
                }

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

                public Integer getPoint() {
                    return point;
                }

                public void setPoint(Integer point) {
                    this.point = point;
                }

            }


        }

    }


}
