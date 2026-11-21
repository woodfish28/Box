package com.github.tvbox.osc.util;

/**
 * 保来屋定制：品牌信息集中配置。
 *
 * 只改这里即可，不用碰布局和其他代码：
 *  - 公众号二维码图片地址（网络 URL，换图不用重编译 APK）
 *  - 公众号名称 / 微信号 / 网址
 *
 * 二维码显示策略：
 *  1) QR_IMAGE_URL 非空 → Glide 直接加载该图片（适合公众号/个人微信二维码图片）
 *  2) QR_IMAGE_URL 为空 → 用 QRCodeGen 从 QR_TEXT 现场生成（适合纯文字/网址/微信号）
 */
public final class BrandInfo {
    private BrandInfo() {}

    // ===== 在这里填你的内容 =====

    /** 公众号二维码图片 URL（放到自有域名下，如 https://tvbox.woodfish.shop/qr/gzh.png）。
     *  留空则用下方 QR_TEXT 现场生成二维码。 */
    public static final String QR_IMAGE_URL = "";

    /** 无二维码图片时，用这串文字生成二维码。可填网址、微信号、或公众号文章链接。 */
    public static final String QR_TEXT = "https://tvbox.woodfish.shop/";

    /** 展示给电视用户看的标题，如「扫码关注公众号」 */
    public static final String TITLE = "扫码关注 · 保来屋";

    /** 二维码下方说明文字（公众号名 / 微信号 / 用途） */
    public static final String SUBTITLE = "保来屋经纪 · 保险医生余仁哲";

    /** 网址行（可留空） */
    public static final String WEBSITE = "tvbox.woodfish.shop";

    /** 免责/提示行（可留空） */
    public static final String TIP = "电视端订阅地址已内置，无需手动输入";
}
