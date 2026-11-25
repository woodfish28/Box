package com.github.tvbox.osc.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.View;

import com.github.tvbox.osc.R;

/**
 * 保来屋定制：片头 logo 时钟揭示动效（全 API 兼容，不依赖 SweepLineMaskFilter）。
 *
 * 两层叠加：
 *  - 底层 logo：整体 50% 透明（半透明占位，未扫过部分呈 50% 灰态）。
 *  - 顶层 logo：不透明，用 Canvas 扇形裁剪只绘制"指针已扫过的扇形区"，
 *    指针从 12 点（0°）顺时针扫 0→360°，扫过的部分由 50% 透明 → 不透明揭示。
 *
 * 指针角度由 {@link #setSweep(float)} 驱动（0~360，12 点=0，顺时针），
 * 外部 ValueAnimator 在 ~1.2s 内从 0 动画到 360。
 */
public class BlwClockRevealLogo extends View {

    private final Paint basePaint;    // 底层 50% 透明 logo
    private final Paint revealPaint;   // 顶层不透明 logo
    private final Paint pointerPaint;  // 时钟指针（细长翠绿）
    private final Path fanPath;        // 扇形揭示裁剪区
    private Bitmap logo;
    private float sweep = 0f;          // 当前指针角度 0~360（12 点=0，顺时针）

    private final int sizePx;
    private int cx, cy;

    public BlwClockRevealLogo(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 180dp 与片头 logo_box 一致
        sizePx = (int) (180 * context.getResources().getDisplayMetrics().density);
        basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basePaint.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
        basePaint.setAlpha(128); // 50% 透明

        revealPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        revealPaint.setAlpha(255);

        pointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointerPaint.setColor(0xFF009874);
        pointerPaint.setStrokeWidth(6f);
        pointerPaint.setStrokeCap(Paint.Cap.ROUND);

        fanPath = new Path();

        try {
            logo = android.graphics.BitmapFactory.decodeResource(context.getResources(), R.drawable.blw_logo);
            if (logo != null) {
                logo = Bitmap.createScaledBitmap(logo, sizePx, sizePx, true);
            }
        } catch (Throwable t) {
            logo = null;
        }
        setWillNotDraw(false);
    }

    /** 设置指针角度（0~360，12 点=0，顺时针）。*/
    public void setSweep(float degrees) {
        this.sweep = Math.max(0f, Math.min(360f, degrees));
        invalidate();
    }

    /** 动画结束定格：扫满 360°，logo 完全不透明。*/
    public void finishReveal() {
        this.sweep = 360f;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        cx = getWidth() / 2;
        cy = getHeight() / 2;
        int left = cx - sizePx / 2;
        int top = cy - sizePx / 2;
        if (logo != null) {
            // 底层：50% 透明占位（整图）
            canvas.drawBitmap(logo, left, top, basePaint);

            // 顶层：不透明 + 扇形裁剪揭示
            if (sweep > 0f) {
                float radius = sizePx / 2f + 2;
                // 12 点方向 = -90°（屏幕坐标）。从 12 点顺时针扫 sweep 度。
                float startAngle = -90f;
                float sweepAngle = sweep; // 0~360
                fanPath.reset();
                fanPath.moveTo(cx, cy);
                // 用 arc 闭合形成扇形（顺时针 sweep，start 在 12 点=-90°）
                fanPath.arcTo(cx - radius, cy - radius, cx + radius, cy + radius,
                        startAngle, sweepAngle, false);
                fanPath.close();
                canvas.save();
                canvas.clipPath(fanPath);
                canvas.drawBitmap(logo, left, top, revealPaint);
                canvas.restore();
            }
        }
        // 时钟指针：从中心指向"12 点起顺时针 sweep"方向
        if (sweep < 360f) {
            double rad = Math.toRadians(sweep - 90);
            int len = (int) (sizePx * 0.46);
            int px = cx + (int) (Math.cos(rad) * len);
            int py = cy + (int) (Math.sin(rad) * len);
            canvas.drawLine(cx, cy, px, py, pointerPaint);
            canvas.drawCircle(cx, cy, 10f, pointerPaint);
        }
    }
}
