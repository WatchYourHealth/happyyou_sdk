package com.wyh.happyyousdk.model;

public class PrimaryTribeSpinnerItem {
    String tribeName;
    boolean isPrimary;

    public PrimaryTribeSpinnerItem(String text, boolean isPrimary) {
        this.tribeName = text;
        this.isPrimary = isPrimary;
    }

    public String getTribeName() {
        return tribeName;
    }

    public void setTribeName(String tribeName) {
        this.tribeName = tribeName;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}
