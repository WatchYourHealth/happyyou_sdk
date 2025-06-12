package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommunityGraphResponse {

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

    /**
     * No args constructor for use in serialization
     */
    public CommunityGraphResponse() {
    }

    /**
     * @param msg
     * @param freevoucher
     * @param data
     * @param success
     * @param enGTokens
     */
    public CommunityGraphResponse(String msg, Boolean success, Data data, Object freevoucher, Object enGTokens) {
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

        @SerializedName("st")
        @Expose
        private St st;
        @SerializedName("userDetails")
        @Expose
        private List<UserDetail> userDetails;

        /**
         * No args constructor for use in serialization
         */
        public Data() {
        }

        /**
         * @param st
         * @param userDetails
         */
        public Data(St st, List<UserDetail> userDetails) {
            super();
            this.st = st;
            this.userDetails = userDetails;
        }

        public St getSt() {
            return st;
        }

        public void setSt(St st) {
            this.st = st;
        }

        public List<UserDetail> getUserDetails() {
            return userDetails;
        }

        public void setUserDetails(List<UserDetail> userDetails) {
            this.userDetails = userDetails;
        }

        public class St {

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

            /**
             * No args constructor for use in serialization
             */
            public St() {
            }

            /**
             * @param periodType
             * @param dataPoints
             * @param periodIndex
             * @param activityType
             * @param uuid
             */
            public St(String uuid, String periodType, Integer periodIndex, String activityType, List<DataPoint> dataPoints) {
                super();
                this.uuid = uuid;
                this.periodType = periodType;
                this.periodIndex = periodIndex;
                this.activityType = activityType;
                this.dataPoints = dataPoints;
            }

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
                @SerializedName("point")
                @Expose
                private List<Point> point;

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
                public DataPoint(Integer rtId, String recordDate, List<Point> point) {
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

        public class UserDetail {

            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("name")
            @Expose
            private Object name;
            @SerializedName("mobileNo")
            @Expose
            private Object mobileNo;
            @SerializedName("hraConsent")
            @Expose
            private Boolean hraConsent;
            @SerializedName("vitalConsent")
            @Expose
            private Boolean vitalConsent;
            @SerializedName("activityConsent")
            @Expose
            private Boolean activityConsent;
            @SerializedName("isAdmin")
            @Expose
            private Boolean isAdmin;
            @SerializedName("isAccepted")
            @Expose
            private Boolean isAccepted;

            /**
             * No args constructor for use in serialization
             */
            public UserDetail() {
            }

            /**
             * @param hraConsent
             * @param activityConsent
             * @param isAccepted
             * @param name
             * @param mobileNo
             * @param isAdmin
             * @param userId
             * @param vitalConsent
             */
            public UserDetail(String userId, Object name, Object mobileNo, Boolean hraConsent, Boolean vitalConsent, Boolean activityConsent, Boolean isAdmin, Boolean isAccepted) {
                super();
                this.userId = userId;
                this.name = name;
                this.mobileNo = mobileNo;
                this.hraConsent = hraConsent;
                this.vitalConsent = vitalConsent;
                this.activityConsent = activityConsent;
                this.isAdmin = isAdmin;
                this.isAccepted = isAccepted;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public Object getName() {
                return name;
            }

            public void setName(Object name) {
                this.name = name;
            }

            public Object getMobileNo() {
                return mobileNo;
            }

            public void setMobileNo(Object mobileNo) {
                this.mobileNo = mobileNo;
            }

            public Boolean getHraConsent() {
                return hraConsent;
            }

            public void setHraConsent(Boolean hraConsent) {
                this.hraConsent = hraConsent;
            }

            public Boolean getVitalConsent() {
                return vitalConsent;
            }

            public void setVitalConsent(Boolean vitalConsent) {
                this.vitalConsent = vitalConsent;
            }

            public Boolean getActivityConsent() {
                return activityConsent;
            }

            public void setActivityConsent(Boolean activityConsent) {
                this.activityConsent = activityConsent;
            }

            public Boolean getIsAdmin() {
                return isAdmin;
            }

            public void setIsAdmin(Boolean isAdmin) {
                this.isAdmin = isAdmin;
            }

            public Boolean getIsAccepted() {
                return isAccepted;
            }

            public void setIsAccepted(Boolean isAccepted) {
                this.isAccepted = isAccepted;
            }

        }


    }
}



