package com.github.tvbox.osc.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 保来屋定制：时钟式弧形进度条。
 * 从 12 点方向（-90°）顺时针扫描，表示加载进度。
 * 外层翠绿弧形 = 进度，内层深色弧形 = 轨道。
 * 中心放 logo + 百分比文字。
 */
public class BlwArcProgressView extends FrameLayout {

    private final Paint trackPaint;  // 轨道（深色）
    private final Paint progressPaint; // 进度（翠绿）
    private final Paint textPaint;    // 百分比文字
    private final RectF oval;
    private float progress = 0f;     // 0~100
    private int centerX, centerY;
    private int arcRadius;
    private int strokeWidth;

    public BlwArcProgressView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);

        // 轨道
        trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setColor(0x66FFFFFF);  // 半透明白色
        trackPaint.setStrokeWidth(8f);
        trackPaint.setStrokeCap(Paint.Cap.ROUND);

        // 进度
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setColor(0xFF009874);  // 翠绿
        progressPaint.setStrokeWidth(8f);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        // 文字
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(28f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);

        oval = new RectF();
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        // 弧形半径 = 短边一半减去描边宽度
        arcRadius = Math.min(w, h) / 2 - strokeWidth - 4;
        strokeWidth = 8;
        oval.set(centerX - arcRadius, centerY - arcRadius,
                 centerX + arcRadius, centerY + arcRadius);
    }

    /**
     * 设置进度（0-100）。传 -1 表示不确定进度（只显示旋转指示器）。
     */
    public void setProgress(float percent) {
        this.progress = Math.max(0f, Math.min(100f, percent));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 轨道（完整圆环）
        canvas.drawArc(oval, 0, 360, false, trackPaint);

        if (progress < 0) {
            // 不确定进度：旋转指示器（1/4 圆弧）
            // 简单画 90° 弧，旋转由动画驱动
            canvas.drawArc(oval, -90, 90, false, progressPaint);
            return;
        }

        // 确定进度：从 12 点（-90°）顺时针画 progress 度
        float sweepAngle = progress / 100f * 360f;
        canvas.drawArc(oval, -90f, sweepAngle, false, progressPaint);

        // 中心百分比文字
        if (progress > 0 && progress < 100) {
            String text = (int) progress + "%";
            canvas.drawText(text, centerX, centerY + 10, textPaint);
        } else if (progress >= 100) {
            canvas.drawText("✓", centerX, centerY + 10, textPaint);
        }
    }
}
