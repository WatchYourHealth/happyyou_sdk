package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EnrolbyAadhaarResponse {

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isPoolingReq() {
        return isPoolingReq;
    }

    public Data getData() {
        return data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public EnGTokens getEnGTokens() {
        return enGTokens;
    }

    public Rewards getRewards() {
        return rewards;
    }

    public Object getUserkey() {
        return userkey;
    }

    public boolean isGoogleFit() {
        return isGoogleFit;
    }
    @SerializedName("msg")
    @Expose
    public String msg;

    @SerializedName("success")
    @Expose
    public boolean success;

    @SerializedName("isPoolingReq")
    @Expose
    public boolean isPoolingReq;
    @SerializedName("data")
    @Expose
    public Data data;
    @SerializedName("freevoucher")
    @Expose
    public Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    public EnGTokens enGTokens;
    @SerializedName("rewards")
    @Expose
    public Rewards rewards;
    @SerializedName("userkey")
    @Expose
    public Object userkey;
    @SerializedName("isGoogleFit")
    @Expose
    public boolean isGoogleFit;

    public class AbhaProfile{
        public String getFirstName() {
            return firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getDob() {
            return dob;
        }

        public String getGender() {
            return gender;
        }

        public String getPhoto() {
            return photo;
        }

        public Object getMobile() {
            return mobile;
        }

        public String getEmail() {
            return email;
        }

        public ArrayList<String> getPhrAddress() {
            return phrAddress;
        }

        public String getAddress() {
            return address;
        }

        public String getPinCode() {
            return pinCode;
        }

        public String getStateName() {
            return stateName;
        }

        public String getDistrictName() {
            return districtName;
        }

        public String getAbhaNumber() {
            return abhaNumber;
        }

        public String getAbhaStatus() {
            return abhaStatus;
        }

        public String firstName;
        public String middleName;
        public String lastName;
        public String dob;
        public String gender;
        public String photo;
        public Object mobile;
        public String email;
        public ArrayList<String> phrAddress;
        public String address;
        public String pinCode;
        public String stateName;
        public String districtName;
        public String abhaNumber;
        public String abhaStatus;
    }

    public class Data{
        public String getMessage() {
            return message;
        }

        public String getTxnId() {
            return txnId;
        }

        public Tokens getTokens() {
            return tokens;
        }

        public AbhaProfile getAbhaProfile() {
            return abhaProfile;
        }

        public boolean isNew() {
            return isNew;
        }

        public String message;
        public String txnId;
        public Tokens tokens;
        public AbhaProfile abhaProfile;
        public boolean isNew;

        public Error getError() {
            return error;
        }
        @SerializedName("error")
        @Expose
        public Error error;
    }
    public static class Error{
        public String getMessage() {
            return message;
        }
        @SerializedName("message")
        @Expose
        public String message;
    }
    public class EnGTokens{
        public Object tokens;
        public Object bonusTokens;
    }

    public class Rewards{
        public Object reward;
        public Object bonusRewards;
    }
    public class Tokens{
        public String getToken() {
            return token;
        }

        public String token;
    }

}

