package com.wyh.happyyousdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
public class PolicyListResp {
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isPoolingReq() {
        return isPoolingReq;
    }

    public void setPoolingReq(boolean poolingReq) {
        isPoolingReq = poolingReq;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Object getFeedbackDetails() {
        return feedbackDetails;
    }

    public void setFeedbackDetails(Object feedbackDetails) {
        this.feedbackDetails = feedbackDetails;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public EnGTokens getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokens enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Rewards getRewards() {
        return rewards;
    }

    public void setRewards(Rewards rewards) {
        this.rewards = rewards;
    }

    public Object getUserkey() {
        return userkey;
    }

    public void setUserkey(Object userkey) {
        this.userkey = userkey;
    }

    public boolean isGoogleFit() {
        return isGoogleFit;
    }

    public void setGoogleFit(boolean googleFit) {
        isGoogleFit = googleFit;
    }

    public Object getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(Object userDetail) {
        this.userDetail = userDetail;
    }

    public Object getSpinTheWheelRewardsDetail() {
        return spinTheWheelRewardsDetail;
    }

    public void setSpinTheWheelRewardsDetail(Object spinTheWheelRewardsDetail) {
        this.spinTheWheelRewardsDetail = spinTheWheelRewardsDetail;
    }

    @SerializedName("msg")
    private String msg;

    @SerializedName("success")
    private boolean success;

    @SerializedName("isPoolingReq")
    private boolean isPoolingReq;

    @SerializedName("data")
    private Data data;

    @SerializedName("feedbackDetails")
    private Object feedbackDetails;

    @SerializedName("freevoucher")
    private Object freevoucher;

    @SerializedName("enGTokens")
    private EnGTokens enGTokens;

    @SerializedName("rewards")
    private Rewards rewards;

    @SerializedName("userkey")
    private Object userkey;

    @SerializedName("isGoogleFit")
    private boolean isGoogleFit;

    @SerializedName("userDetail")
    private Object userDetail;

    @SerializedName("spinTheWheelRewardsDetail")
    private Object spinTheWheelRewardsDetail;

    // Getters and Setters

    public static class Data {
        public Policy getPolicy() {
            return policy;
        }

        public void setPolicy(Policy policy) {
            this.policy = policy;
        }

        @SerializedName("policy")
        private Policy policy;

        // Getters and Setters
    }

    public static class Policy {
        public List<Content> getContent() {
            return content;
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }

        @SerializedName("paymentrenewalurl")
        private String paymentrenewalurl;

        public String getPaymentrenewalurl() {
            return paymentrenewalurl;
        }

        public void setPaymentrenewalurl(String paymentrenewalurl) {
            this.paymentrenewalurl = paymentrenewalurl;
        }

        @SerializedName("autodebiturl")
        private String autodebiturl;

        public String getAutodebiturl() {
            return autodebiturl;
        }

        public void setAutodebiturl(String autodebiturl) {
            this.autodebiturl = autodebiturl;
        }

        public Pageable getPageable() {
            return pageable;
        }

        public void setPageable(Pageable pageable) {
            this.pageable = pageable;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public Sort getSort() {
            return sort;
        }

        public void setSort(Sort sort) {
            this.sort = sort;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        @SerializedName("content")
        private List<Content> content;

        @SerializedName("pageable")
        private Pageable pageable;

        @SerializedName("totalPages")
        private int totalPages;

        @SerializedName("totalElements")
        private int totalElements;

        @SerializedName("last")
        private boolean last;

        @SerializedName("first")
        private boolean first;

        @SerializedName("sort")
        private Sort sort;

        @SerializedName("numberOfElements")
        private int numberOfElements;

        @SerializedName("size")
        private int size;

        @SerializedName("number")
        private int number;

        @SerializedName("empty")
        private boolean empty;
    }

    public static class Content {
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

        public String getSumAssured() {
            return sumAssured;
        }

        public void setSumAssured(String sumAssured) {
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

        public String getPremiumAmount() {
            return premiumAmount;
        }

        public void setPremiumAmount(String premiumAmount) {
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
        private String sumAssured;

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
        private String premiumAmount;

        @SerializedName("purchaseDate")
        private String purchaseDate;

        @SerializedName("clientId")
        private String clientId;

        @SerializedName("premiumStatusCode")
        private String premiumStatusCode;

        @SerializedName("isFullyPaid")
        private String isFullyPaid;

        public String getPolicyTerm() {
            return policyTerm;
        }

        public void setPolicyTerm(String policyTerm) {
            this.policyTerm = policyTerm;
        }

        @SerializedName("policyTerm")
        private String policyTerm;

        public int getPremiumTerm() {
            return premiumTerm;
        }

        public void setPremiumTerm(int premiumTerm) {
            this.premiumTerm = premiumTerm;
        }

        @SerializedName("premiumTerm")
        private int premiumTerm;

        @SerializedName("ecsStatus")
        private String ecsStatus;

        public String getEcsStatus() {
            return ecsStatus;
        }

        public void setEcsStatus(String ecsStatus) {
            this.ecsStatus = ecsStatus;
        }

        @SerializedName("policyMasterStatus")
        public String policyMasterStatus;

        public String getPolicyMasterStatus() {
            return policyMasterStatus;
        }

        public void setPolicyMasterStatus(String policyMasterStatus) {
            this.policyMasterStatus = policyMasterStatus;
        }

        @SerializedName("mobileNumber")
        public String mobileNumber;

        @SerializedName("clientAddress")
        public String clientAddress;

        public String getEnddate() {
            return enddate;
        }

        public void setEnddate(String enddate) {
            this.enddate = enddate;
        }

        @SerializedName("endDate")
        public String enddate;

        public String getInsurencecover() {
            return insurencecover;
        }

        public void setInsurencecover(String insurencecover) {
            this.insurencecover = insurencecover;
        }

        @SerializedName("insuranceCover")
        public String insurencecover;

        public String getClientAddress() {
            return clientAddress;
        }

        public void setClientAddress(String clientAddress) {
            this.clientAddress = clientAddress;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }





        // Include more fields as required along with their Getters and Setters
    }

    public static class Pageable {
        @SerializedName("sort")
        private Sort sort;

        @SerializedName("pageSize")
        private int pageSize;

        @SerializedName("pageNumber")
        private int pageNumber;

        @SerializedName("offset")
        private int offset;

        @SerializedName("paged")
        private boolean paged;

        public Sort getSort() {
            return sort;
        }

        public void setSort(Sort sort) {
            this.sort = sort;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public boolean isPaged() {
            return paged;
        }

        public void setPaged(boolean paged) {
            this.paged = paged;
        }

        public boolean isUnpaged() {
            return unpaged;
        }

        public void setUnpaged(boolean unpaged) {
            this.unpaged = unpaged;
        }

        @SerializedName("unpaged")
        private boolean unpaged;

        // Getters and Setters
    }

    public static class Sort {
        @SerializedName("sorted")
        private boolean sorted;

        @SerializedName("unsorted")
        private boolean unsorted;

        public boolean isSorted() {
            return sorted;
        }

        public void setSorted(boolean sorted) {
            this.sorted = sorted;
        }

        public boolean isUnsorted() {
            return unsorted;
        }

        public void setUnsorted(boolean unsorted) {
            this.unsorted = unsorted;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        @SerializedName("empty")
        private boolean empty;

        // Getters and Setters
    }

    public static class EnGTokens {
        @SerializedName("tokens")
        private Object tokens;

        public Object getTokens() {
            return tokens;
        }

        public void setTokens(Object tokens) {
            this.tokens = tokens;
        }

        public Object getBonusTokens() {
            return bonusTokens;
        }

        public void setBonusTokens(Object bonusTokens) {
            this.bonusTokens = bonusTokens;
        }

        @SerializedName("bonusTokens")
        private Object bonusTokens;

        // Getters and Setters
    }

    public static class Rewards {
        public Object getReward() {
            return reward;
        }

        public void setReward(Object reward) {
            this.reward = reward;
        }

        public Object getBonusRewards() {
            return bonusRewards;
        }

        public void setBonusRewards(Object bonusRewards) {
            this.bonusRewards = bonusRewards;
        }

        @SerializedName("reward")
        private Object reward;

        @SerializedName("bonusRewards")
        private Object bonusRewards;

        // Getters and Setters
    }
}
