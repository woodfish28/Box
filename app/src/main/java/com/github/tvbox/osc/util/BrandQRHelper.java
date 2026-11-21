package com.github.tvbox.osc.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.tvbox.osc.R;
import com.github.tvbox.osc.ui.tv.QRCodeGen;

import me.jessyan.autosize.utils.AutoSizeUtils;

/** 保来屋定制：把 BrandInfo 内容填充到含品牌二维码控件的布局里（AboutDialog / BrandDialog 复用）。 */
public final class BrandQRHelper {
    private BrandQRHelper() {}

    /** qrSizeMm：二维码边长（mm），与布局里 ImageView 尺寸保持一致。 */
    public static void fill(Context context, View root, int qrSizeMm) {
        TextView tvTitle = root.findViewById(R.id.tvBrandTitle);
        ImageView ivQR = root.findViewById(R.id.ivBrandQR);
        TextView tvSubtitle = root.findViewById(R.id.tvBrandSubtitle);
        TextView tvWebsite = root.findViewById(R.id.tvBrandWebsite);
        TextView tvTip = root.findViewById(R.id.tvBrandTip);

        if (tvTitle != null) tvTitle.setText(BrandInfo.TITLE);
        if (tvSubtitle != null) tvSubtitle.setText(BrandInfo.SUBTITLE);
        if (tvWebsite != null) {
            tvWebsite.setText(TextUtils.isEmpty(BrandInfo.WEBSITE) ? "" : BrandInfo.WEBSITE);
            tvWebsite.setVisibility(TextUtils.isEmpty(BrandInfo.WEBSITE) ? View.GONE : View.VISIBLE);
        }
        if (tvTip != null) {
            tvTip.setText(TextUtils.isEmpty(BrandInfo.TIP) ? "" : BrandInfo.TIP);
            tvTip.setVisibility(TextUtils.isEmpty(BrandInfo.TIP) ? View.GONE : View.VISIBLE);
        }
        if (ivQR == null) return;

        int px = AutoSizeUtils.mm2px(context, qrSizeMm);
        if (!TextUtils.isEmpty(BrandInfo.QR_IMAGE_URL)) {
            Glide.with(context)
                    .load(BrandInfo.QR_IMAGE_URL)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(ivQR);
        } else {
            String text = TextUtils.isEmpty(BrandInfo.QR_TEXT) ? BrandInfo.WEBSITE : BrandInfo.QR_TEXT;
            ivQR.setImageBitmap(QRCodeGen.generateBitmap(text, px, px));
        }
    }
}
