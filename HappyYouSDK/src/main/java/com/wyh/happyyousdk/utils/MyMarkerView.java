package com.wyh.happyyousdk.utils;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.wyh.happyyousdk.R;

import java.text.DecimalFormat;


public class MyMarkerView extends MarkerView {

    private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
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
            BarEntry barEntry = (BarEntry) e;
            float[] barValue = barEntry.getYVals();

            DecimalFormat format = new DecimalFormat("0.##");

            if (barEntry.isStacked()) {
                tvContent.setText(format.format(barEntry.getY()));
//                int value = (int) barValue[highlight.getStackIndex()];
//                tvContent.setText("" + Utils.formatNumber(value, 0, true, ','));
            } else {
                tvContent.setText(format.format(barEntry.getY()));
//                tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true, ','));
            }

            /*if (barEntry.isStacked()) {
                //Log.v("Entry", "IS Stack ");
                int value = (int) barValue[highlight.getStackIndex()];
                tvContent.setText("" + Utils.formatNumber(value, 0, true, ','));
            } else {
                //Log.v("Entry", "IS not Stack ");
                tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true, ','));
            }*/
        } catch (Exception exc) {
//            Log.v("Entry", "In Exception");
            if (e instanceof CandleEntry) {
                CandleEntry ce = (CandleEntry) e;
                tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true, ','));
            } else {
                tvContent.setText("" + Utils.formatNumber(e.getY(), 0, true, ','));
            }
        }

        //Add this
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}