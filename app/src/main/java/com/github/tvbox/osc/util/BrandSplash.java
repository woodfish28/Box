package com.github.tvbox.osc.util;

import android.app.Activity;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.tvbox.osc.R;

/**
 * 保来屋定制：启动品牌蒙层 · 电影制片厂风格。
 * 两个版本：
 *   show(activity, true)  → 5.8s 完整版（光点→logo→打字机slogan→品牌名→域名→淡出）
 *   show(activity, false) → 3.5s 精简版（logo缩放→品牌名→淡出）
 *
 * 直接在 Activity decorView 上叠加全屏 View，不占焦点、不拦返回键、不碰主界面逻辑。
 */
public final class BrandSplash {
    private BrandSplash() {}

    private static boolean shown = false;
    private static final Handler H = new Handler(Looper.getMainLooper());
    private static final String SLOGAN = "用专业为家撑伞";

    /**
     * @param full true=5.8s 完整版，false=3.5s 精简版
     */
    public static void show(final Activity activity, boolean full) {
        if (activity == null || shown || activity.isFinishing()) return;
        shown = true;

        final View splash = activity.getLayoutInflater()
                .inflate(R.layout.blw_splash_view, null);
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        ((android.view.ViewGroup) activity.getWindow().getDecorView()).addView(splash, lp);

        final View logoBox  = splash.findViewById(R.id.blw_splash_logo_box);
        final View logo     = splash.findViewById(R.id.blw_splash_logo);
        final BlwClockRevealLogo reveal = (BlwClockRevealLogo) splash.findViewById(R.id.blw_splash_reveal);
        final View glow     = splash.findViewById(R.id.blw_splash_glow);
        final ImageView slogan = (ImageView) splash.findViewById(R.id.blw_splash_slogan);
        final TextView brand  = splash.findViewById(R.id.blw_splash_brand);
        final TextView url    = splash.findViewById(R.id.blw_splash_url);
        final ImageView bg    = (ImageView) splash.findViewById(R.id.blw_splash_bg);

        // 片头 logo 用 3D 水晶版（时钟揭示 + slogan 均为水晶质感）
        if (reveal != null) reveal.setLogoRes(R.drawable.blw_logo_crystal);

        // 精简版隐藏打字机 slogan 和光点
        if (!full) {
            if (slogan != null) slogan.setVisibility(View.GONE);
            if (glow != null)   glow.setVisibility(View.GONE);
        }

        // ===== 整体淡入（450ms） =====
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(450);
        fadeIn.setFillAfter(true);
        splash.startAnimation(fadeIn);

        // ===== 0.2s → 影院背景缓慢浮现（1.6s，制造"电影开场黑场亮起"的质感） =====
        if (bg != null) {
            bg.setVisibility(View.VISIBLE);
            AlphaAnimation bgIn = new AlphaAnimation(0f, 1f);
            bgIn.setDuration(1600);
            bgIn.setFillAfter(true);
            bg.startAnimation(bgIn);
        }

        if (full) {
            // ===== 5.8s 完整版时序 =====
            // 0.0s → 光点出现（脉动 800ms）
            if (glow != null) {
                glow.setVisibility(View.VISIBLE);
                AlphaAnimation glowPulse = new AlphaAnimation(0f, 1f);
                glowPulse.setDuration(600);
                glowPulse.setFillAfter(true);
                glow.startAnimation(glowPulse);
            }

            // 0.8s → logo 从光点中扩展（缩放 0.3→1.0，600ms）+ 同时启动时钟指针揭示（1.2s 扫满 360°）
            H.postDelayed(new Runnable() {
                @Override public void run() {
                    if (glow != null) glow.setVisibility(View.GONE);
                    if (logoBox != null) logoBox.setVisibility(View.VISIBLE);
                    ScaleAnimation scale = new ScaleAnimation(0.3f, 1f, 0.3f, 1f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scale.setDuration(600);
                    scale.setInterpolator(new DecelerateInterpolator());
                    scale.setFillBefore(true);
                    scale.setFillAfter(true);
                    logoBox.startAnimation(scale);
                    if (logo != null) logo.setVisibility(View.INVISIBLE); // 改用 reveal 层重绘 logo，避免双绘
                    startClockReveal(reveal, 1200);
                }
            }, 800);

            // 2.0s → 水晶 slogan 字标浮现（玻璃字淡入 + 轻微上浮，900ms）
            H.postDelayed(new Runnable() {
                @Override public void run() {
                    if (slogan != null) {
                        slogan.setVisibility(View.VISIBLE);
                        slogan.setAlpha(0f);
                        slogan.setTranslationY(24f);
                        slogan.animate().alpha(1f).translationY(0f).setDuration(900)
                                .setInterpolator(new android.view.animation.DecelerateInterpolator()).start();
                    }
                }
            }, 2000);

            // 3.5s → 品牌名淡入（600ms）
            H.postDelayed(new Runnable() {
                @Override public void run() {
                    if (brand != null) {
                        AlphaAnimation a = new AlphaAnimation(0f, 1f);
                        a.setDuration(600);
                        a.setFillAfter(true);
                        brand.startAnimation(a);
                    }
                }
            }, 3500);

            // 4.2s → 域名极淡滑入（400ms）
            H.postDelayed(new Runnable() {
                @Override public void run() {
                    if (url != null) {
                        AlphaAnimation a = new AlphaAnimation(0f, 0.6f);
                        a.setDuration(400);
                        a.setFillAfter(true);
                        url.startAnimation(a);
                    }
                }
            }, 4200);

            // 5.0s → 整体淡出（800ms）
            H.postDelayed(new Runnable() {
                @Override public void run() {
                    fadeOutAndRemove(splash, 800);
                }
            }, 5000);

        } else {
            // ===== 3.5s 精简版时序 =====
            // 0.5s → logo 缩放显现（500ms）+ 启动时钟指针揭示（1.0s 扫满 360°）
            H.postDelayed(new Runnable() {
                @Override public void run() {
                    if (logoBox != null) logoBox.setVisibility(View.VISIBLE);
                    ScaleAnimation scale = new ScaleAnimation(0.3f, 1f, 0.3f, 1f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scale.setDuration(500);
                    scale.setInterpolator(new DecelerateInterpolator());
                    scale.setFillBefore(true);
                    scale.setFillAfter(true);
                    logoBox.startAnimation(scale);
                    if (logo != null) logo.setVisibility(View.INVISIBLE); // 改用 reveal 层重绘 logo，避免双绘
                    startClockReveal(reveal, 1000);
                }
            }, 500);

            // 1.8s → 品牌名淡入（600ms）
            H.postDelayed(new Runnable() {
                @Override public void run() {
                    if (brand != null) {
                        AlphaAnimation a = new AlphaAnimation(0f, 1f);
                        a.setDuration(600);
                        a.setFillAfter(true);
                        brand.startAnimation(a);
                    }
                }
            }, 1800);

            // 2.5s → 整体淡出（700ms）
            H.postDelayed(new Runnable() {
                @Override public void run() {
                    fadeOutAndRemove(splash, 700);
                }
            }, 2500);
        }
    }

    /** 时钟指针揭示：指针从 12 点顺时针扫 0→360°，扫过处 logo 由 50% 透明变不透明。
     *  动画结束后定格为完全不透明。reveal 自身已重绘 logo，静态 logo ImageView 由调用方隐藏。 */
    private static void startClockReveal(final BlwClockRevealLogo reveal, long durationMs) {
        if (reveal == null) return;
        ValueAnimator va = ValueAnimator.ofFloat(0f, 360f);
        va.setDuration(durationMs);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator a) {
                reveal.setSweep((float) a.getAnimatedValue());
            }
        });
        va.start();
        H.postDelayed(new Runnable() {
            @Override public void run() {
                reveal.finishReveal();
            }
        }, durationMs + 50);
    }

    /** 打字机效果：逐字显示 */
    private static void typeWriter(final TextView tv, final String text, final int msPerChar) {
        final int len = text.length();
        tv.setText(""); // 初始空白
        // 用 Handler 逐字推进
        final int[] pos = {0};
        H.post(new Runnable() {
            @Override public void run() {
                pos[0]++;
                if (pos[0] <= len) {
                    tv.setText(text.substring(0, pos[0]));
                    H.postDelayed(this, msPerChar);
                }
            }
        });
    }

    private static void fadeOutAndRemove(final View splash, long duration) {
        AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
        fadeOut.setDuration(duration);
        fadeOut.setFillAfter(true);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation a) {}
            @Override public void onAnimationRepeat(Animation a) {}
            @Override public void onAnimationEnd(Animation a) {
                try {
                    ((android.view.ViewGroup) splash.getParent()).removeView(splash);
                } catch (Exception ignored) {}
            }
        });
        splash.startAnimation(fadeOut);
    }
}
