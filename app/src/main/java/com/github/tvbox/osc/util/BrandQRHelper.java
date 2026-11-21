package com.github.tvbox.osc.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.tvbox.osc.R;

/** 保来屋定制：把 BrandInfo 内容填充到含品牌控件的布局（AboutDialog / BrandDialog 复用）。 */
public final class BrandQRHelper {
    private BrandQRHelper() {}

    public static void fill(Context context, View root) {
        setText(root, R.id.tvBrandTitle, BrandInfo.TITLE);
        setText(root, R.id.tvBrandSlogan, BrandInfo.SLOGAN);
        setText(root, R.id.tvBrandSubtitle, BrandInfo.SUBTITLE);
        setText(root, R.id.tvBrandWebsite, BrandInfo.WEBSITE);
        setText(root, R.id.tvBrandTip, BrandInfo.TIP);
        setText(root, R.id.tvQrWechatLabel, BrandInfo.QR_WECHAT_LABEL);
        setText(root, R.id.tvQrGzhLabel, BrandInfo.QR_GZH_LABEL);

        setLogo(root, R.id.ivBrandLogo);

        setQr(context, root, R.id.ivBrandQR,
                BrandInfo.QR_WECHAT_RES, BrandInfo.QR_WECHAT_URL);
        setQr(context, root, R.id.ivBrandQR2,
                BrandInfo.QR_GZH_RES, BrandInfo.QR_GZH_URL);
    }

    private static void setText(View root, int id, String text) {
        TextView tv = root.findViewById(id);
        if (tv != null) tv.setText(TextUtils.isEmpty(text) ? "" : text);
    }

    private static void setLogo(View root, int id) {
        ImageView iv = root.findViewById(id);
        if (iv != null) iv.setImageResource(BrandInfo.LOGO_RES);
    }

    private static void setQr(Context context, View root, int id, int localRes, String url) {
        ImageView iv = root.findViewById(id);
        if (iv == null) return;
        if (!TextUtils.isEmpty(url)) {
            // 网络图：换图只换服务器，不重编译；禁缓存保证换图立即生效
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(localRes)
                    .error(localRes)
                    .into(iv);
        } else {
            iv.setImageResource(localRes);
        }
    }
}
