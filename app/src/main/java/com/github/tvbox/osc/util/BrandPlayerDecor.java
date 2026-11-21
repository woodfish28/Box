package com.github.tvbox.osc.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tvbox.osc.R;

/**
 * 保来屋定制：播放器品牌装饰（缓冲 logo 百分比 + 暂停品牌卡片）。
 * 由 BaseController 在 initView 里挂到自身（FrameLayout），一处覆盖点播+直播。
 */
public class BrandPlayerDecor {

    private final View bufferView;
    private final ImageView bufferLogo;
    private final TextView bufferPct;
    private final AlphaAnimation breathe;

    private final View pauseCard;

    public BrandPlayerDecor(final FrameLayout controller) {
        Context ctx = controller.getContext();

        // ===== 缓冲视图（居中）=====
        bufferView = View.inflate(ctx, R.layout.blw_buffer_view, null);
        FrameLayout.LayoutParams bp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        bp.gravity = Gravity.CENTER;
        bufferLogo = bufferView.findViewById(R.id.blw_buffer_logo);
        bufferPct = bufferView.findViewById(R.id.blw_buffer_pct);
        controller.addView(bufferView, bp);

        // logo 呼吸动画（无真实进度时用）
        breathe = new AlphaAnimation(0.45f, 1f);
        breathe.setDuration(900);
        breathe.setRepeatMode(Animation.REVERSE);
        breathe.setRepeatCount(Animation.INFINITE);

        // ===== 暂停品牌卡片（居中偏下，不挡画面主体）=====
        pauseCard = View.inflate(ctx, R.layout.blw_pause_card, null);
        FrameLayout.LayoutParams pp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        pp.gravity = Gravity.CENTER;
        controller.addView(pauseCard, pp);
    }

    /** 缓冲中：有真实进度(1-94)显示百分比，否则 logo 呼吸 + "加载中" */
    public void showBuffer(int percent) {
        hidePause();
        bufferView.setVisibility(View.VISIBLE);
        if (percent > 0 && percent < 95) {
            bufferLogo.clearAnimation();
            bufferPct.setText(percent + "%");
        } else {
            bufferPct.setText("加载中…");
            if (bufferLogo.getAnimation() != breathe) bufferLogo.startAnimation(breathe);
        }
    }

    public void hideBuffer() {
        bufferView.setVisibility(View.GONE);
        bufferLogo.clearAnimation();
    }

    /** 暂停：显示居中品牌卡片 */
    public void showPause() {
        hideBuffer();
        pauseCard.setVisibility(View.VISIBLE);
    }

    public void hidePause() {
        pauseCard.setVisibility(View.GONE);
    }
}
