package com.wyh.happyyousdk.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.EnGTokensModel;
import com.wyh.happyyousdk.model.response.FeedbackResponse;

import java.util.List;

public class MyOrderResponse extends FeedbackResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Data> data;
    @SerializedName("freevoucher")
    @Expose
    private Object freevoucher;
    @SerializedName("enGTokens")
    @Expose
    private EnGTokensModel enGTokens;
    @SerializedName("rewards")
    @Expose
    private Object rewards;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Object getFreevoucher() {
        return freevoucher;
    }

    public void setFreevoucher(Object freevoucher) {
        this.freevoucher = freevoucher;
    }

    public EnGTokensModel getEnGTokens() {
        return enGTokens;
    }

    public void setEnGTokens(EnGTokensModel enGTokens) {
        this.enGTokens = enGTokens;
    }

    public Object getRewards() {
        return rewards;
    }

    public void setRewards(Object rewards) {
        this.rewards = rewards;
    }

    public static class Data {

        @SerializedName("Service")
        @Expose
        private String service;
        @SerializedName("Vendor")
        @Expose
        private String vendor;
        @SerializedName("Date")
        @Expose
        private String date;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("Reporturl")
        @Expose
        private String reportUrl;
        @SerializedName("InvoiceDocument")
        @Expose
        private String invoiceDocument;
        @SerializedName("OrderID")
        @Expose
        private String orderID;
        @SerializedName("DateofAppointment")
        @Expose
        private String dateOfAppointment;
        @SerializedName("CustomerName")
        @Expose
        private String customerName;

        @SerializedName("vendorlogo")
        @Expose
        private String vendorlogo;


        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getReportUrl() {
            return reportUrl;
        }

        public void setReportUrl(String reportUrl) {
            this.reportUrl = reportUrl;
        }

        public String getInvoiceDocument() {
            return invoiceDocument;
        }

        public void setInvoiceDocument(String invoiceDocument) {
            this.invoiceDocument = invoiceDocument;
        }

        public String getOrderID() {
            return orderID;
        }

        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }

        public String getDateOfAppointment() {
            return dateOfAppointment;
        }

        public void setDateOfAppointment(String dateOfAppointment) {
            this.dateOfAppointment = dateOfAppointment;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getVendorlogo() {
            return vendorlogo;
        }

        public void setVendorlogo(String vendorlogo) {
            this.vendorlogo = vendorlogo;
        }
    }

}