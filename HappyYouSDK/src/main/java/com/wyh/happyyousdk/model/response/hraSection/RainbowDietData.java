package com.wyh.happyyousdk.model.response.hraSection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RainbowDietData {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("ColorID")
    @Expose
    private Integer colorID;
    @SerializedName("FoodName")
    @Expose
    private String foodName;
    @SerializedName("FoodLink")
    @Expose
    private String foodLink;
    @SerializedName("FoodType")
    @Expose
    private String foodType;
    @SerializedName("ImageURL")
    @Expose
    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getColorID() {
        return colorID;
    }

    public void setColorID(Integer colorID) {
        this.colorID = colorID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodLink() {
        return foodLink;
    }

    public void setFoodLink(String foodLink) {
        this.foodLink = foodLink;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}
