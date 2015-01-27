package com.vzhuan;

import android.content.Context;
import android.util.DisplayMetrics;

public class B5MDisplayHelper {
    public static DisplayMetrics metrics;

    /**
     * App启动时初始化
     */
    public static void init(Context context) {
        metrics = context.getResources().getDisplayMetrics();
    }

    /**
     * [简要描述]: Display Density
     * [详细描述]:
     */
    public static float getDensity() {
        return metrics.density;
    }

    /**
     * 屏幕密度DPI
     */
    public static int getDensityDpi() {
        return metrics.densityDpi;
    }

    /**
     * [简要描述]: 获取屏幕宽度（像素值）
     * [详细描述]:
     */
    public static int getScreenWidth() {
        return metrics.widthPixels;
    }

    /**
     * [简要描述]: 获取屏幕宽度（Dip值）
     */
    public static float getWidthDip() {
        return (getScreenWidth() / getDensity());
    }

    /**
     * [简要描述]:   获取屏幕高度（像素值）
     * [详细描述]:
     */
    public static int getScreenHeight() {
        return metrics.heightPixels;
    }

    /**
     * [简要描述]:   获取屏幕高度（Dip值）
     * [详细描述]:
     */
    public static float getHeightDip() {
        return (getScreenHeight() / getDensity());
    }

    /**
     * [简要描述]: 获取Dip值 [Int]
     */
    public static int getIntDip(float dpValue) {
        return (int) (metrics.density * dpValue + 0.5f);
    }

    /**
     * [简要描述]: 获取Dip值 [Float]
     */
    public float getFloatDip(int i) {
        return (metrics.density * i);
    }

    public static int getIntPx(float pxValue) {
        return (int) (pxValue / metrics.density + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dipTopx(float dpValue) {
        final float scale = getDensity();
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxTodip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> localClass = Class.forName("com.android.internal.R$dimen");
            Object localObject = localClass.newInstance();
            int j = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
            int k = context.getResources().getDimensionPixelSize(j);
            return k;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return 0;
    }

    public static int[] getScreenCenter() {
        return new int[]{getScreenWidth() / 2, getScreenHeight() / 2};
    }
}
