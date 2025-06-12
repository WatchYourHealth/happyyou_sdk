package com.wyh.happyyousdk.model.response;

public class ChipsModel {
    String name;
    boolean isSelected;

    public ChipsModel(String course_name, boolean isSelected) {
        this.name = course_name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
