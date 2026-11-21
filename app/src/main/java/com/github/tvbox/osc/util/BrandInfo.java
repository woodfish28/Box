package com.github.tvbox.osc.util;

import com.github.tvbox.osc.R;

/**
 * 保来屋定制：品牌信息集中配置（唯一改点）。
 *
 * 图片默认走打包进 APK 的本地资源（drawable-nodpi），电视端不依赖网络、秒开。
 * 如果以后想换图又不重编译 APK：把新图传到自有域名（如 https://tvbox.woodfish.shop/qr/wechat.png），
 * 填到对应 QR_*_URL（非空即优先走网络），重新编一次即可；之后换图只换服务器上的图。
 */
public final class BrandInfo {
    private BrandInfo() {}

    // ===== 文案 =====
    public static final String TITLE = "扫码关注 · 保来屋";
    /** 品牌 slogan（老余 2026-11-21 拍板） */
    public static final String SLOGAN = "保来屋 · 用专业为家撑伞";
    public static final String SUBTITLE = "保险医生余仁哲 · 您身边的保险管家";
    public static final String WEBSITE = "https://baolaiwu.cn";
    public static final String TIP = "电视端订阅已内置，无需手动配置";

    // ===== 图片（打包进 APK，本地资源）=====
    public static final int LOGO_RES = R.drawable.blw_logo;
    /** 微信个人码（猫图艺术二维码，已去水印） */
    public static final int QR_WECHAT_RES = R.drawable.blw_qr_wechat;
    /** 保来屋公众号码 */
    public static final int QR_GZH_RES = R.drawable.blw_qr_gzh;

    public static final String QR_WECHAT_LABEL = "加微信 · 余仁哲";
    public static final String QR_GZH_LABEL = "关注公众号 · 保来屋";

    // ===== 可选：换图不走重编译的通道（留空=用上面的本地资源）=====
    public static final String QR_WECHAT_URL = "";
    public static final String QR_GZH_URL = "";
}
