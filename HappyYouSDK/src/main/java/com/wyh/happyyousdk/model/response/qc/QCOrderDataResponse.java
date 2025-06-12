package com.wyh.happyyousdk.model.response.qc;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QCOrderDataResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("refno")
    @Expose
    private String refno;
    @SerializedName("cancel")
    @Expose
    private Cancel cancel;
    @SerializedName("currency")
    @Expose
    private Currency currency;
    @SerializedName("payments")
    @Expose
    private List<Payment> payments;
    @SerializedName("cards")
    @Expose
    private List<Card> cards;
    @SerializedName("products")
    @Expose
    private Products products;
    @SerializedName("additionalTxnFields")
    @Expose
    private List<Object> additionalTxnFields;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public Cancel getCancel() {
        return cancel;
    }

    public void setCancel(Cancel cancel) {
        this.cancel = cancel;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public List<Object> getAdditionalTxnFields() {
        return additionalTxnFields;
    }

    public void setAdditionalTxnFields(List<Object> additionalTxnFields) {
        this.additionalTxnFields = additionalTxnFields;
    }

    public class Cancel {

        @SerializedName("allowed")
        @Expose
        private Boolean allowed;
        @SerializedName("allowedWithIn")
        @Expose
        private Integer allowedWithIn;

        public Boolean getAllowed() {
            return allowed;
        }

        public void setAllowed(Boolean allowed) {
            this.allowed = allowed;
        }

        public Integer getAllowedWithIn() {
            return allowedWithIn;
        }

        public void setAllowedWithIn(Integer allowedWithIn) {
            this.allowedWithIn = allowedWithIn;
        }

    }

    public class Card {

        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("productName")
        @Expose
        private String productName;
        @SerializedName("labels")
        @Expose
        private Labels labels;
        @SerializedName("cardNumber")
        @Expose
        private String cardNumber;
        @SerializedName("cardPin")
        @Expose
        private String cardPin;
        @SerializedName("activationCode")
        @Expose
        private Object activationCode;
        @SerializedName("barcode")
        @Expose
        private Object barcode;
        @SerializedName("activationUrl")
        @Expose
        private Object activationUrl;
        @SerializedName("formats")
        @Expose
        private List<Object> formats;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("validity")
        @Expose
        private String validity;
        @SerializedName("issuanceDate")
        @Expose
        private String issuanceDate;
        @SerializedName("cardId")
        @Expose
        private Integer cardId;
        @SerializedName("recipientDetails")
        @Expose
        private RecipientDetails recipientDetails;
        @SerializedName("theme")
        @Expose
        private String theme;

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Labels getLabels() {
            return labels;
        }

        public void setLabels(Labels labels) {
            this.labels = labels;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getCardPin() {
            return cardPin;
        }

        public void setCardPin(String cardPin) {
            this.cardPin = cardPin;
        }

        public Object getActivationCode() {
            return activationCode;
        }

        public void setActivationCode(Object activationCode) {
            this.activationCode = activationCode;
        }

        public Object getBarcode() {
            return barcode;
        }

        public void setBarcode(Object barcode) {
            this.barcode = barcode;
        }

        public Object getActivationUrl() {
            return activationUrl;
        }

        public void setActivationUrl(Object activationUrl) {
            this.activationUrl = activationUrl;
        }

        public List<Object> getFormats() {
            return formats;
        }

        public void setFormats(List<Object> formats) {
            this.formats = formats;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getValidity() {
            return validity;
        }

        public void setValidity(String validity) {
            this.validity = validity;
        }

        public String getIssuanceDate() {
            return issuanceDate;
        }

        public void setIssuanceDate(String issuanceDate) {
            this.issuanceDate = issuanceDate;
        }

        public Integer getCardId() {
            return cardId;
        }

        public void setCardId(Integer cardId) {
            this.cardId = cardId;
        }

        public RecipientDetails getRecipientDetails() {
            return recipientDetails;
        }

        public void setRecipientDetails(RecipientDetails recipientDetails) {
            this.recipientDetails = recipientDetails;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

    }

    public class Currency {

        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("numericCode")
        @Expose
        private String numericCode;
        @SerializedName("symbol")
        @Expose
        private String symbol;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getNumericCode() {
            return numericCode;
        }

        public void setNumericCode(String numericCode) {
            this.numericCode = numericCode;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

    }

    public class Delivery {

        @SerializedName("mode")
        @Expose
        private String mode;
        @SerializedName("status")
        @Expose
        private Status status;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

    }

    public class Egcgbfk001 {

        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("balanceEnquiryInstruction")
        @Expose
        private Object balanceEnquiryInstruction;
        @SerializedName("specialInstruction")
        @Expose
        private String specialInstruction;
        @SerializedName("images")
        @Expose
        private Images images;
        @SerializedName("cardBehaviour")
        @Expose
        private String cardBehaviour;

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getBalanceEnquiryInstruction() {
            return balanceEnquiryInstruction;
        }

        public void setBalanceEnquiryInstruction(Object balanceEnquiryInstruction) {
            this.balanceEnquiryInstruction = balanceEnquiryInstruction;
        }

        public String getSpecialInstruction() {
            return specialInstruction;
        }

        public void setSpecialInstruction(String specialInstruction) {
            this.specialInstruction = specialInstruction;
        }

        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;
        }

        public String getCardBehaviour() {
            return cardBehaviour;
        }

        public void setCardBehaviour(String cardBehaviour) {
            this.cardBehaviour = cardBehaviour;
        }

    }

    public class Email {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("reason")
        @Expose
        private String reason;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

    }

    public class Images {

        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("base")
        @Expose
        private String base;
        @SerializedName("small")
        @Expose
        private String small;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

    }

    public class Labels {

        @SerializedName("cardNumber")
        @Expose
        private String cardNumber;
        @SerializedName("cardPin")
        @Expose
        private String cardPin;
        @SerializedName("activationCode")
        @Expose
        private String activationCode;
        @SerializedName("validity")
        @Expose
        private String validity;

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }

        public String getCardPin() {
            return cardPin;
        }

        public void setCardPin(String cardPin) {
            this.cardPin = cardPin;
        }

        public String getActivationCode() {
            return activationCode;
        }

        public void setActivationCode(String activationCode) {
            this.activationCode = activationCode;
        }

        public String getValidity() {
            return validity;
        }

        public void setValidity(String validity) {
            this.validity = validity;
        }

    }

    public class Payment {

        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("balance")
        @Expose
        private String balance;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

    }

    public class Products {

        @SerializedName("EGCGBFK001")
        @Expose
        private Egcgbfk001 egcgbfk001;

        public Egcgbfk001 getEgcgbfk001() {
            return egcgbfk001;
        }

        public void setEgcgbfk001(Egcgbfk001 egcgbfk001) {
            this.egcgbfk001 = egcgbfk001;
        }

    }

    public class RecipientDetails {

        @SerializedName("salutation")
        @Expose
        private Object salutation;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobileNumber")
        @Expose
        private String mobileNumber;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("failureReason")
        @Expose
        private String failureReason;
        @SerializedName("delivery")
        @Expose
        private Delivery delivery;

        public Object getSalutation() {
            return salutation;
        }

        public void setSalutation(Object salutation) {
            this.salutation = salutation;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFailureReason() {
            return failureReason;
        }

        public void setFailureReason(String failureReason) {
            this.failureReason = failureReason;
        }

        public Delivery getDelivery() {
            return delivery;
        }

        public void setDelivery(Delivery delivery) {
            this.delivery = delivery;
        }

    }

    public class Sms {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("reason")
        @Expose
        private String reason;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

    }

    public class Status {

        @SerializedName("sms")
        @Expose
        private Sms sms;
        @SerializedName("email")
        @Expose
        private Email email;

        public Sms getSms() {
            return sms;
        }

        public void setSms(Sms sms) {
            this.sms = sms;
        }

        public Email getEmail() {
            return email;
        }

        public void setEmail(Email email) {
            this.email = email;
        }

    }
}
