package com.wyh.happyyousdk.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
public class CustomChipView extends ViewGroup {
    private int horizontalSpacing = 16;
    private int verticalSpacing = 16;
    private int lineMaxWidth;
    private List<List<View>> lines = new ArrayList<>();
    public CustomChipView(Context context) {
        super(context);
        init();
    }
    public CustomChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CustomChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        lineMaxWidth = getResources().getDisplayMetrics().widthPixels - getPaddingLeft() - getPaddingRight();
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childTop = getPaddingTop();
        for (List<View> line : lines) {
            int childLeft = getPaddingLeft();
            int maxHeightInLine = 0;
            for (View child : line) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                childLeft += childWidth + horizontalSpacing;
                maxHeightInLine = Math.max(maxHeightInLine, childHeight);
            }
            childTop += maxHeightInLine + verticalSpacing;
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = getPaddingTop() + getPaddingBottom();
        int lineMaxHeight = 0;
        int lineMaxWidth = width - getPaddingLeft() - getPaddingRight();
        lines.clear();
        List<View> currentLine = new ArrayList<>();
        lines.add(currentLine);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (currentLine.size() > 0 && currentLine.get(currentLine.size() - 1).getRight() + childWidth + horizontalSpacing > lineMaxWidth) {
                currentLine = new ArrayList<>();
                lines.add(currentLine);
                height += lineMaxHeight + verticalSpacing;
                lineMaxHeight = 0;
            }
            currentLine.add(child);
            lineMaxHeight = Math.max(lineMaxHeight, childHeight);
        }
        height += lineMaxHeight;
        setMeasuredDimension(width, height);
    }
    public void addViewToNextLine(View view) {
        List<View> currentLine = lines.get(lines.size() - 1);
        if (!currentLine.isEmpty()) {
            // Add spacing
            View lastView = currentLine.get(currentLine.size() - 1);
            int spacing = horizontalSpacing;
            int width = lastView.getWidth() + spacing;
            if (getPaddingLeft() + width + view.getMeasuredWidth() + getPaddingRight() > lineMaxWidth) {
                // Start a new line if adding this view exceeds the line width
                currentLine = new ArrayList<>();
                lines.add(currentLine);
            }
        }
        currentLine.add(view);
        addView(view);
        requestLayout();
    }
    public void removeAllViewsFromChips() {
        removeAllViews();
        lines.clear();
    }
}