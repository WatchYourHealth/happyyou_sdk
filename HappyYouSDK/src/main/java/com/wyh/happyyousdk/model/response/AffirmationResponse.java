package com.wyh.happyyousdk.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AffirmationResponse {

    @SerializedName("affirmation")
    @Expose
    private String affirmation;

    /**
     * No args constructor for use in serialization
     *
     */
    public AffirmationResponse() {
    }

    /**
     *
     * @param affirmation
     */
    public AffirmationResponse(String affirmation) {
        super();
        this.affirmation = affirmation;
    }

    public String getAffirmation() {
        return affirmation;
    }

    public void setAffirmation(String affirmation) {
        this.affirmation = affirmation;
    }

}
