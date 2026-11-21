package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.util.BrandQRHelper;

import org.jetbrains.annotations.NotNull;

/** 保来屋定制：品牌二维码弹窗（关注公众号 / 加微信 / 访问网址）。 */
public class BrandDialog extends BaseDialog {

    public BrandDialog(@NonNull @NotNull Context context) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_brand, null);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        BrandQRHelper.fill(context, view, 300);
    }
}
