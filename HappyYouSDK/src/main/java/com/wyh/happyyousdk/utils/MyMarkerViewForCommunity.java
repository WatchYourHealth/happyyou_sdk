package com.wyh.happyyousdk.utils;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.wyh.happyyousdk.R;
import com.wyh.happyyousdk.trends.MarkerDataClass;

import java.text.DecimalFormat;

public class MyMarkerViewForCommunity extends MarkerView {

    private TextView tvContent;

    public MyMarkerViewForCommunity(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

// callbacks everytime the MarkerView is redrawn, can be used to update the
// content (user-interface)

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //tvContent.setText("x: " + e.getX() + " , y: " + e.getY()); // set the entry-value as the display text
        try {
            DecimalFormat format = new DecimalFormat("0.##");
            BarEntry barEntry = (BarEntry) e;
//            barEntry.getData().
            float[] barValue = barEntry.getYVals();
            MarkerDataClass markerDataClass = (MarkerDataClass) barEntry.getData();
            int highLightIndex = highlight.getStackIndex();
            if (highLightIndex < 0) {
                highLightIndex = 0;
            }

            /*if (activityNames[highLightIndex] != 0) {
                //Log.v("Entry", "IS Stack ");
//                int value = (int) barValue[highlight.getStackIndex()];

                String activityName = new Gson().toJson(e.getData());*/
            if (markerDataClass.getPoints() != null && markerDataClass.getPoints().length > 0)
                tvContent.setText(format.format(markerDataClass.getPoints()[highLightIndex]) + ", " + markerDataClass.getUserName().get(highLightIndex));
            else
                tvContent.setText(format.format(markerDataClass.getPoint()) + ", " + markerDataClass.getName());
//            } else {
//                //Log.v("Entry", "IS not Stack ");
//                tvContent.setText("" + Utils.formatNumber(value, 0, true, ','));
//            }
        } catch (Exception exc) {
            //Log.v("Entry", "In Exception");
            if (e instanceof CandleEntry) {
                CandleEntry ce = (CandleEntry) e;
                tvContent.setText("" + ce.getData());
            } else {
                tvContent.setText("" + exc.getMessage());
            }
        }
        /*if (highlight.isStacked()) {
            Log.v("Entry","IS Stack ");
            BarEntry barEntry = (BarEntry) e;
            float[] barValue = barEntry.getYVals();
            if (barEntry.isStacked()) {
                int value = (int) barValue[highlight.getStackIndex()];
                tvContent.setText("" + Utils.formatNumber(value, 0, true));
            }
        } else {
            Log.v("Entry","IS not Stack ");
            if (e instanceof CandleEntry) {
                CandleEntry ce = (CandleEntry) e;
                tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
            } else {
                tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true));
            }
        }*/

        //Add this
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}


