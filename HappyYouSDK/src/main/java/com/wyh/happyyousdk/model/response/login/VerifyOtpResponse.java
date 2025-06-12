package com.wyh.happyyousdk.model.response.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class VerifyOtpResponse {

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

    @SerializedName("userkey")
    @Expose
    private Object userkey;

    @SerializedName("isGoogleFit")
    @Expose
    private Boolean isGoogleFit;



    public Object getUserkey() {
        return userkey;
    }

    public void setUserkey(Object userkey) {
        this.userkey = userkey;
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

    public Object getRewards() {
        return rewards;
    }

    public void setRewards(Object rewards) {
        this.rewards = rewards;
    }

    public Boolean getIsGoogleFit() {
        return isGoogleFit;
    }

    public class Data {
        @SerializedName("authToken")
        @Expose
        private String authToken;

        @SerializedName("clientDetails")
        @Expose
        private ClientDetails clientDetails;

        @SerializedName("crn")
        @Expose
        private String crn;

        @SerializedName("isRegistered")
        @Expose
        private Boolean isRegistered;

        @SerializedName("policyDetails")
        @Expose
        private PolicyDetails policyDetails;

        @SerializedName("userkey")
        @Expose
        private String userkey;

        @SerializedName("referralStatus")
        @Expose
        private String referralStatus;

        @SerializedName("authTokenIssuedOn")
        @Expose
        private String authTokenIssuedOn;

        @SerializedName("authTokenExpiresOn")
        @Expose
        private String authTokenExpiresOn;


        public Boolean getRegistered() {
            return isRegistered;
        }

        public void setRegistered(Boolean registered) {
            isRegistered = registered;
        }

        public String getReferralStatus() {
            return referralStatus;
        }

        public void setReferralStatus(String referralStatus) {
            this.referralStatus = referralStatus;
        }

        public Boolean getIsRegistered() {
            return isRegistered;
        }

        public void setIsRegistered(Boolean isRegistered) {
            this.isRegistered = isRegistered;
        }

        public String getAuthToken() {
            return authToken;
        }

        public void setAuthToken(String authToken) {
            this.authToken = authToken;
        }

        public ClientDetails getClientDetails() {
            return clientDetails;
        }

        public void setClientDetails(ClientDetails clientDetails) {
            this.clientDetails = clientDetails;
        }

        public String getCrn() {
            return crn;
        }

        public void setCrn(String crn) {
            this.crn = crn;
        }

        public PolicyDetails getPolicyDetails() {
            return policyDetails;
        }

        public void setPolicyDetails(PolicyDetails policyDetails) {
            this.policyDetails = policyDetails;
        }

        public Object getUserkey() {
            return userkey;
        }

        public void setUserkey(String userkey) {
            this.userkey = userkey;
        }

        public String getAuthTokenIssuedOn() {
            return authTokenIssuedOn;
        }

        public void setAuthTokenIssuedOn(String authTokenIssuedOn) {
            this.authTokenIssuedOn = authTokenIssuedOn;
        }

        public String getAuthTokenExpiresOn() {
            return authTokenExpiresOn;
        }

        public void setAuthTokenExpiresOn(String authTokenExpiresOn) {
            this.authTokenExpiresOn = authTokenExpiresOn;
        }

        public class PolicyDetails {

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
            private Object freevoucher;
            @SerializedName("enGTokens")
            @Expose
            private Object enGTokens;
            @SerializedName("rewards")
            @Expose
            private Object rewards;

            public PolicyDetails(String msg, Boolean success, List<Datum> data, Object freevoucher, Object enGTokens, Object rewards) {
                this.msg = msg;
                this.success = success;
                this.data = data;
                this.freevoucher = freevoucher;
                this.enGTokens = enGTokens;
                this.rewards = rewards;
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

            public class Datum {

                @SerializedName("NAME")
                @Expose
                private String name;
                @SerializedName("Email")
                @Expose
                private String email;
                @SerializedName("DOB")
                @Expose
                private String dob;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getDob() {
                    return dob;
                }

                public void setDob(String dob) {
                    this.dob = dob;
                }

            }


        }


        public class ClientDetails {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("otp")
            @Expose
            private Object otp;
            @SerializedName("expiresOn")
            @Expose
            private String expiresOn;
            @SerializedName("status")
            @Expose
            private Boolean status;
            @SerializedName("uuid")
            @Expose
            private String uuid;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("mobile")
            @Expose
            private String mobile;
            @SerializedName("dob")
            @Expose
            private String dob;
            @SerializedName("profilePicPath")
            @Expose
            private String profilePicPath;
            @SerializedName("gender")
            @Expose
            private String gender;
            @SerializedName("conversationId")
            @Expose
            private String conversationId;

            public String getCorporateRegistered() {
                return corporateRegistered;
            }

            public void setCorporateRegistered(String corporateRegistered) {
                this.corporateRegistered = corporateRegistered;
            }

            @SerializedName("corporateRegistered")
            @Expose
            private String corporateRegistered;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Object getOtp() {
                return otp;
            }

            public void setOtp(Object otp) {
                this.otp = otp;
            }

            public String getExpiresOn() {
                return expiresOn;
            }

            public void setExpiresOn(String expiresOn) {
                this.expiresOn = expiresOn;
            }

            public Boolean getStatus() {
                return status;
            }

            public void setStatus(Boolean status) {
                this.status = status;
            }

            public Object getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getDob() {
                return dob;
            }

            public void setDob(String dob) {
                this.dob = dob;
            }

            public String getProfilePicPath() {
                return profilePicPath;
            }

            public void setProfilePicPath(String profilePicPath) {
                this.profilePicPath = profilePicPath;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getConversationId() {
                return conversationId;
            }

            public void setConversationId(String conversationId) {
                this.conversationId = conversationId;
            }

        }

    }


}
