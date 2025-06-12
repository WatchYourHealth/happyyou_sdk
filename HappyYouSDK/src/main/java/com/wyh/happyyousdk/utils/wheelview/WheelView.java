package com.wyh.happyyousdk.utils.wheelview;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WheelView extends View {

    private RectF range = new RectF();
    private Paint archPaint, textPaint;
    private int padding, radius, center, mWheelBackground, mImagePadding;
    private List<WheelItem> mWheelItems = new ArrayList<>();
    private List<Integer> originalColors; // To store original colors of items
    private OnLuckyWheelReachTheTarget mOnLuckyWheelReachTheTarget;
    private OnRotationListener onRotationListener;
    private int targetIndex = -1; // Target index to track

    public WheelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponents();
    }

    private void initComponents() {
        // Paint for arcs
        archPaint = new Paint();
        archPaint.setAntiAlias(true);
        archPaint.setDither(true);

        // Paint for text
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setTextSize(30);
    }

    public void setWheelBackgoundWheel(int wheelBackground) {
        mWheelBackground = wheelBackground;
        invalidate();
    }

    public void setItemsImagePadding(int imagePadding) {
        mImagePadding = imagePadding;
        invalidate();
    }

    public void setWheelListener(OnLuckyWheelReachTheTarget onLuckyWheelReachTheTarget) {
        mOnLuckyWheelReachTheTarget = onLuckyWheelReachTheTarget;
    }

    public void addWheelItems(List<WheelItem> wheelItems) {
        mWheelItems = wheelItems;
        originalColors = new ArrayList<>();
        for (WheelItem item : mWheelItems) {
            originalColors.add(item.color); // Store the original colors
        }
        invalidate();
    }

    /**
     * Reset all wheel item colors to their original state.
     */
    public void resetItemColorsToOriginal() {
        if (originalColors != null && mWheelItems != null) {
            for (int i = 0; i < mWheelItems.size(); i++) {
                mWheelItems.get(i).color = originalColors.get(i);
            }
            invalidate(); // Redraw the wheel
        }
    }

    public void rotateWheelToTarget(int target) {
        resetItemColorsToOriginal(); // Reset colors before starting the spin

        targetIndex = target;
        float wheelItemCenter = 270 - getAngleOfIndexTarget(target) + (360 / mWheelItems.size()) / 2;
        int DEFAULT_ROTATION_TIME = 9000;

        animate()
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(DEFAULT_ROTATION_TIME)
                .rotation((360 * 15) + wheelItemCenter)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (targetIndex > 0) {
                            mWheelItems.get(targetIndex - 1).color = Color.TRANSPARENT; // Highlight the target
                        }
                        if (mOnLuckyWheelReachTheTarget != null) {
                            mOnLuckyWheelReachTheTarget.onReachTarget();
                        }
                        try {
                            if (onRotationListener != null) {
                                onRotationListener.onFinishRotation();
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        invalidate(); // Redraw the wheel
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .start();
    }

    public void resetRotationLocationToZeroAngle(final int target) {
        animate()
                .setDuration(0)
                .rotation(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rotateWheelToTarget(target);
                        clearAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .start();
    }

    private float getAngleOfIndexTarget(int target) {
        return (360 / mWheelItems.size()) * target;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWheelItems != null && mWheelItems.size() > 0) {
            drawWheelBackground(canvas);

            float tempAngle = 0;
            float sweepAngle = 360 / mWheelItems.size();

            for (int i = 0; i < mWheelItems.size(); i++) {
                archPaint.setColor(mWheelItems.get(i).color);
                canvas.drawArc(range, tempAngle, sweepAngle, true, archPaint);

                drawImage(canvas, tempAngle, mWheelItems.get(i).bitmap);
                drawText(canvas, tempAngle, sweepAngle, mWheelItems.get(i).text == null ? "" : mWheelItems.get(i).text);

                tempAngle += sweepAngle;
            }
        }
    }

    private void drawWheelBackground(Canvas canvas) {
        Paint backgroundPainter = new Paint();
        backgroundPainter.setAntiAlias(true);
        backgroundPainter.setDither(true);
        backgroundPainter.setColor(mWheelBackground);
        canvas.drawCircle(center, center, center, backgroundPainter);
    }

    private void drawImage(Canvas canvas, float tempAngle, Bitmap bitmap) {
        int imgWidth = (radius / mWheelItems.size()) - mImagePadding;
        float angle = (float) ((tempAngle + 360 / mWheelItems.size() / 2) * Math.PI / 180);

        int x = (int) (center + radius / 2 / 2 * Math.cos(angle));
        int y = (int) (center + radius / 2 / 2 * Math.sin(angle));

        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2);

        Matrix matrix = new Matrix();
        matrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
        matrix.postRotate(tempAngle + 120);
        matrix.postTranslate(rect.exactCenterX(), rect.exactCenterY());

        canvas.drawBitmap(bitmap, matrix, new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG));
        matrix.reset();
    }

    private void drawText(Canvas canvas, float tempAngle, float sweepAngle, String text) {
        Path path = new Path();
        path.addArc(range, tempAngle, sweepAngle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textPaint.setLetterSpacing(0.15f); // Adjust letter spacing
        }

        float textWidth = textPaint.measureText(text);
        int hOffset = (int) (radius * Math.PI / mWheelItems.size() / 2 - textWidth / 2);
        int vOffset = (radius / 2 / 5);
        canvas.drawTextOnPath(text, path, hOffset, vOffset, textPaint);
    }

//    private void drawText(Canvas canvas, float tempAngle, float sweepAngle, String text) {
//        Path path = new Path();
//        path.addArc(range, tempAngle, sweepAngle);
//
//        // Split the text into two parts if it contains a space
//        String[] parts = text.split(" ", 2); // Split into at most 2 parts
//
//        // Calculate the text width for the first part
//        float textWidth = textPaint.measureText(parts[0]);
//        int hOffset = (int) (radius * Math.PI / mWheelItems.size() / 2 - textWidth / 2);
//        int vOffset = (radius / 2 / 3) - 3;
//
//        // Draw the first part of the text
//        canvas.drawTextOnPath(parts[0], path, hOffset, vOffset, textPaint);
//
//        // If there is a second part, draw it below the first part
//        if (parts.length > 1) {
//            // Calculate the text width for the second part
//            float secondTextWidth = textPaint.measureText(parts[1]);
//            int secondHOffset = (int) (radius * Math.PI / mWheelItems.size() / 2 - secondTextWidth / 2);
//            float secondVOffset = vOffset + textPaint.getTextSize(); // Move down by the text size
//
//            // Draw the second part of the text
//            canvas.drawTextOnPath(parts[1], path, secondHOffset, secondVOffset, textPaint);
//        }
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        int DEFAULT_PADDING = 5;
        padding = getPaddingLeft() == 0 ? DEFAULT_PADDING : getPaddingLeft();
        radius = width - padding * 2;
        center = width / 2;
        range.set(padding, padding, padding + radius, padding + radius);

        setMeasuredDimension(width, width);
    }

    public void setOnRotationListener(OnRotationListener onRotationListener) {
        this.onRotationListener = onRotationListener;
    }
}
