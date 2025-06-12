package com.wyh.happyyousdk.model.response.qc.productDescription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wyh.happyyousdk.model.response.qc.allProducts.ProductImages;

import java.util.List;

public class ProductDetailsResponse {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("kycEnabled")
    @Expose
    private String kycEnabled;
    @SerializedName("additionalForm")
    @Expose
    private Object additionalForm;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("schedulingEnabled")
    @Expose
    private boolean schedulingEnabled;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("images")
    @Expose
    private ProductImages images;
    @SerializedName("tnc")
    @Expose
    private Tnc tnc;
    @SerializedName("categories")
    @Expose
    private List<String> categories;
    @SerializedName("customThemesAvailable")
    @Expose
    private boolean customThemesAvailable;
    @SerializedName("reloadCardNumber")
    @Expose
    private boolean reloadCardNumber;
    @SerializedName("expiry")
    @Expose
    private String expiry;
    @SerializedName("brandName")
    @Expose
    private String brandName;
    @SerializedName("etaMessage")
    @Expose
    private String etaMessage;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("payout")
    @Expose
    private Payout payout;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getKycEnabled() {
        return kycEnabled;
    }

    public void setKycEnabled(String kycEnabled) {
        this.kycEnabled = kycEnabled;
    }

    public Object getAdditionalForm() {
        return additionalForm;
    }

    public void setAdditionalForm(Object additionalForm) {
        this.additionalForm = additionalForm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSchedulingEnabled() {
        return schedulingEnabled;
    }

    public void setSchedulingEnabled(boolean schedulingEnabled) {
        this.schedulingEnabled = schedulingEnabled;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ProductImages getImages() {
        return images;
    }

    public Payout getPayout() {
        return payout;
    }

    public void setPayout(Payout payout) {
        this.payout = payout;
    }

    public void setImages(ProductImages images) {
        this.images = images;
    }

    public Tnc getTnc() {
        return tnc;
    }

    public void setTnc(Tnc tnc) {
        this.tnc = tnc;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public boolean isCustomThemesAvailable() {
        return customThemesAvailable;
    }

    public void setCustomThemesAvailable(boolean customThemesAvailable) {
        this.customThemesAvailable = customThemesAvailable;
    }

    public boolean isReloadCardNumber() {
        return reloadCardNumber;
    }

    public void setReloadCardNumber(boolean reloadCardNumber) {
        this.reloadCardNumber = reloadCardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getEtaMessage() {
        return etaMessage;
    }

    public void setEtaMessage(String etaMessage) {
        this.etaMessage = etaMessage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
