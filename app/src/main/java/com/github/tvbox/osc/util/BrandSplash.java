package com.github.tvbox.osc.util;

import android.app.Activity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.github.tvbox.osc.R;

/**
 * 保来屋定制：启动品牌蒙层。
 * 直接在 Activity 根视图上叠加一个全屏 View，淡入→停留→淡出后自动移除。
 * 不占焦点、不拦返回键、不碰主界面逻辑。
 */
public final class BrandSplash {
    private BrandSplash() {}

    /** 首次安装才显示，避免每次开应用都等 2.5 秒 */
    private static boolean shown = false;

    public static void show(final Activity activity) {
        if (activity == null || shown || activity.isFinishing()) return;
        shown = true;

        final View splash = activity.getLayoutInflater().inflate(R.layout.blw_splash_view, null);
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        // 加到 decorView 最上层，盖住一切
        activity.getWindow().getDecorView().addView(splash, lp);

        View card = splash.findViewById(R.id.blw_splash_card);
        if (card != null) {
            ScaleAnimation scale = new ScaleAnimation(0.88f, 1f, 0.88f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scale.setDuration(500);
            scale.setInterpolator(new DecelerateInterpolator());
            scale.setFillBefore(true);
            card.startAnimation(scale);
        }

        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(450);
        fadeIn.setFillAfter(true);
        splash.startAnimation(fadeIn);

        splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
                fadeOut.setDuration(500);
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
        }, 2200);
    }
}
