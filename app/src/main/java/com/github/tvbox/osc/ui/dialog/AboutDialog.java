package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.util.BrandQRHelper;

import org.jetbrains.annotations.NotNull;

public class AboutDialog extends BaseDialog {

    public AboutDialog(@NonNull @NotNull Context context) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_about, null);
        setContentView(view);
        // 保来屋定制：免责声明上方填品牌二维码区
        BrandQRHelper.fill(context, view);
    }
}