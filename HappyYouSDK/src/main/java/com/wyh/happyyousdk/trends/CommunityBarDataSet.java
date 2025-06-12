package com.wyh.happyyousdk.trends;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.wyh.happyyousdk.R;

import java.util.ArrayList;
import java.util.List;

public class CommunityBarDataSet extends BarDataSet {

    int[] colors = new int[]{R.color.blue_cyan, R.color.blue, R.color.orange, R.color.happy_dark_grey,
            R.color.btn_blue, R.color.dark_pink, R.color.light_blue, R.color.light_orange,
            R.color.purple_500, R.color.kotakDarkBrown};

    List<BarEntry> barData;
    List<BarNameData> barNameData = new ArrayList<>();

    public CommunityBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
        this.barData = yVals;
        for (int i = 0; i < barData.size(); i++){
            MarkerDataClass markerDataClass = (MarkerDataClass) barData.get(i).getData();
        }
    }

    @Override
    public int getColor() {
        return super.getColor();
    }
}
