package com.wyh.happyyousdk.happyMarket.Model;

public class HappyMartListModel {

    Integer happyMartImg;
    String happyMartTile;

    public HappyMartListModel(Integer happyMartImg, String happyMartTile) {
        this.happyMartImg = happyMartImg;
        this.happyMartTile = happyMartTile;
    }


    public Integer getHappyMartImg() {
        return happyMartImg;
    }

    public void setHappyMartImg(Integer happyMartImg) {
        this.happyMartImg = happyMartImg;
    }

    public String getHappyMartTile() {
        return happyMartTile;
    }

    public void setHappyMartTile(String happyMartTile) {
        this.happyMartTile = happyMartTile;
    }
}
