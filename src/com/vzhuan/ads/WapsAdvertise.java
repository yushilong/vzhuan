package com.vzhuan.ads;

import android.app.Activity;
import cn.waps.AppConnect;

/**
 * Created by lscm on 2015/1/8.
 */
public class WapsAdvertise implements Advertise {
    @Override
    public void show(Activity activity) {
        AppConnect.getInstance(activity).showAppOffers(activity);
    }
}
