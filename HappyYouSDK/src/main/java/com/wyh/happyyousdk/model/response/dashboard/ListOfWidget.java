package com.wyh.happyyousdk.model.response.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListOfWidget {
    private String name;
    private Double count;
    private Integer goal;
    private Integer protein;
    private Integer carbs;
    private Integer fat;
    private Integer burnedCalorie;

    public ListOfWidget(String name, Double count, Integer goal, Integer protein, Integer carbs, Integer fat, Integer burnedCalorie) {
        this.name = name;
        this.count = count;
        this.goal = goal;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.burnedCalorie = burnedCalorie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getBurnedCalorie() {
        return burnedCalorie;
    }

    public void setBurnedCalorie(Integer burnedCalorie) {
        this.burnedCalorie = burnedCalorie;
    }
}
