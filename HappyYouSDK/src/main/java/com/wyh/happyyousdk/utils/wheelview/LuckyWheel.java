package com.wyh.happyyousdk.utils.wheelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wyh.happyyousdk.R;

import java.util.List;

public class LuckyWheel extends FrameLayout implements View.OnTouchListener, OnRotationListener {
    private WheelView wheelView;
    private ImageView arrow;
    private int target = -1;
    private boolean isRotate = false;
    private OnRotationListener onRotationListener;


    public LuckyWheel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent();
        applyAttribute(attrs);
    }

    public LuckyWheel(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
        applyAttribute(attrs);
    }

    private void initComponent() {
        inflate(getContext(), R.layout.lucky_wheel_layout, this);
        setOnTouchListener(this);
        wheelView = findViewById(R.id.wv_main_wheel);
        wheelView.setOnRotationListener(this);
        arrow = findViewById(R.id.iv_arrow);
        onRotationListener = this;
    }


    public OnRotationListener getOnRotationListener() {
        return onRotationListener;
    }

    public void setOnRotationListener(OnRotationListener onRotationListener) {
        this.onRotationListener = onRotationListener;
    }

    public void addWheelItems(List<WheelItem> wheelItems) {
        wheelView.addWheelItems(wheelItems);
    }

    public void applyAttribute(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LuckyWheel, 0, 0);
        try {
            int backgroundColor = typedArray.getColor(R.styleable.LuckyWheel_background_color, Color.GREEN);
            int arrowImage = typedArray.getResourceId(R.styleable.LuckyWheel_arrow_image, R.drawable.arrow);
            int imagePadding = typedArray.getDimensionPixelSize(R.styleable.LuckyWheel_image_padding, 0);
            wheelView.setWheelBackgoundWheel(backgroundColor);
            wheelView.setItemsImagePadding(imagePadding);
            boolean isShowArrow = typedArray.getBoolean(R.styleable.LuckyWheel_showArrow, true);
            if (isShowArrow) {
                arrow.setImageResource(arrowImage);
            } else {
                arrow.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        typedArray.recycle();
    }

    public void setLuckyWheelReachTheTarget(OnLuckyWheelReachTheTarget onLuckyWheelReachTheTarget) {
        wheelView.setWheelListener(onLuckyWheelReachTheTarget);
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public void rotateWheelTo(int number) {
        isRotate = true;
        wheelView.resetRotationLocationToZeroAngle(number);
    }

    final int SWIPE_DISTANCE_THRESHOLD = 1000;
    float x1, x2, y1, y2, dx, dy;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (target < 0 || isRotate) {
            return false;
        }

        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                dx = x2 - x1;
                dy = y2 - y1;
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx < 0 && Math.abs(dx) > SWIPE_DISTANCE_THRESHOLD)
                        rotateWheelTo(target);
                } else {
                    if (dy > 0 && Math.abs(dy) > SWIPE_DISTANCE_THRESHOLD)
                        rotateWheelTo(target);
                }
                break;
            default:
                return true;
        }
        return true;
    }

    @Override
    public void onFinishRotation() {
        isRotate = false;
        try {
            if (onRotationListener != null) {
                onRotationListener.onFinishRotation();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        // Change the color of the target segment
        int newTargetColor = Color.TRANSPARENT; // Replace with your desired color
        //wheelView.setTargetItemColor(newTargetColor,target);
    }


}
