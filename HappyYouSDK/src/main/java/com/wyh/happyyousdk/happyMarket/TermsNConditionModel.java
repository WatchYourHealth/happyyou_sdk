package com.wyh.happyyousdk.happyMarket;

public class TermsNConditionModel {

    String termsName;
    String count;


    public TermsNConditionModel(String termsName, String count) {
        this.termsName = termsName;
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public String getTermsName() {
        return termsName;
    }

    public void setTermsName(String termsName) {
        this.termsName = termsName;
    }
}
