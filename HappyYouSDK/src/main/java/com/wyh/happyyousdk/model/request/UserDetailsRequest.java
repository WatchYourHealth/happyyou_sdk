package com.wyh.happyyousdk.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailsRequest {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("gender")
    @Expose
    private String gender;

    public UserDetailsRequest(String name, String email, String dob, String gender) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
    }
}
