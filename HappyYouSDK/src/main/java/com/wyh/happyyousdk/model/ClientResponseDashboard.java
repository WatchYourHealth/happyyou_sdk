package com.wyh.happyyousdk.model;

import com.google.gson.annotations.SerializedName;

public class ClientResponseDashboard {

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getPremiumDueDate() {
        return premiumDueDate;
    }

    public void setPremiumDueDate(String premiumDueDate) {
        this.premiumDueDate = premiumDueDate;
    }

    public double getSumAssured() {
        return sumAssured;
    }

    public void setSumAssured(double sumAssured) {
        this.sumAssured = sumAssured;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    public void setPolicyStatus(String policyStatus) {
        this.policyStatus = policyStatus;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getPremiumPaid() {
        return premiumPaid;
    }

    public void setPremiumPaid(double premiumPaid) {
        this.premiumPaid = premiumPaid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public Object getBenifitAmount() {
        return benifitAmount;
    }

    public void setBenifitAmount(Object benifitAmount) {
        this.benifitAmount = benifitAmount;
    }

    public double getAccumulatedAmount() {
        return accumulatedAmount;
    }

    public void setAccumulatedAmount(double accumulatedAmount) {
        this.accumulatedAmount = accumulatedAmount;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getProposerName() {
        return proposerName;
    }

    public void setProposerName(String proposerName) {
        this.proposerName = proposerName;
    }

    public String getInsuredDob() {
        return insuredDob;
    }

    public void setInsuredDob(String insuredDob) {
        this.insuredDob = insuredDob;
    }

    public String getProposerDob() {
        return proposerDob;
    }

    public void setProposerDob(String proposerDob) {
        this.proposerDob = proposerDob;
    }

    public double getWealthCreated() {
        return wealthCreated;
    }

    public void setWealthCreated(double wealthCreated) {
        this.wealthCreated = wealthCreated;
    }

    public double getPremiumWithoutTax() {
        return premiumWithoutTax;
    }

    public void setPremiumWithoutTax(double premiumWithoutTax) {
        this.premiumWithoutTax = premiumWithoutTax;
    }

    public String getPremiumCycle() {
        return premiumCycle;
    }

    public void setPremiumCycle(String premiumCycle) {
        this.premiumCycle = premiumCycle;
    }

    public String getTermEndDate() {
        return termEndDate;
    }

    public void setTermEndDate(String termEndDate) {
        this.termEndDate = termEndDate;
    }

    public String getPremiumStatus() {
        return premiumStatus;
    }

    public void setPremiumStatus(String premiumStatus) {
        this.premiumStatus = premiumStatus;
    }

    public double getTotalPremium() {
        return totalPremium;
    }

    public void setTotalPremium(double totalPremium) {
        this.totalPremium = totalPremium;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPremiumStatusCode() {
        return premiumStatusCode;
    }

    public void setPremiumStatusCode(String premiumStatusCode) {
        this.premiumStatusCode = premiumStatusCode;
    }

    public String getIsFullyPaid() {
        return isFullyPaid;
    }

    public void setIsFullyPaid(String isFullyPaid) {
        this.isFullyPaid = isFullyPaid;
    }

    @SerializedName("performance")
    private String performance;

    @SerializedName("premiumDueDate")
    private String premiumDueDate;

    @SerializedName("sumAssured")
    private double sumAssured;

    @SerializedName("policyStatus")
    private String policyStatus;

    @SerializedName("productId")
    private String productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("income")
    private double income;

    @SerializedName("premiumPaid")
    private double premiumPaid;

    @SerializedName("category")
    private String category;

    @SerializedName("policyNo")
    private String policyNo;

    @SerializedName("benifitAmount")
    private Object benifitAmount;

    @SerializedName("accumulatedAmount")
    private double accumulatedAmount;

    @SerializedName("insuredName")
    private String insuredName;

    @SerializedName("proposerName")
    private String proposerName;

    @SerializedName("insuredDob")
    private String insuredDob;

    @SerializedName("proposerDob")
    private String proposerDob;

    @SerializedName("wealthCreated")
    private double wealthCreated;

    @SerializedName("premiumWithoutTax")
    private double premiumWithoutTax;

    @SerializedName("premiumCycle")
    private String premiumCycle;

    @SerializedName("termEndDate")
    private String termEndDate;

    @SerializedName("premiumStatus")
    private String premiumStatus;

    @SerializedName("totalPremium")
    private double totalPremium;

    @SerializedName("premiumAmount")
    private double premiumAmount;

    @SerializedName("purchaseDate")
    private String purchaseDate;

    @SerializedName("clientId")
    private String clientId;

    @SerializedName("premiumStatusCode")
    private String premiumStatusCode;

    @SerializedName("isFullyPaid")
    private String isFullyPaid;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @SerializedName("endDate")
    private String endDate;
}
