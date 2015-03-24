package com.vzhuan.ads;

import android.app.Activity;
import com.datouniao.AdPublisher.AppConnect;

/**
 * Created by yushilong on 2015/3/24.
 */
public class DTNAdvertise implements Advertise {
    @Override
    public void show(Activity activity) {
        AppConnect.getInstance(activity).ShowAdsOffers();
    }
}
