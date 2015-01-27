package com.vzhuan.ads;

import android.app.Activity;
import net.youmi.android.offers.OffersManager;

/**
 * Created by lscm on 2015/1/8.
 */
public class YoumiAdvertise implements Advertise {
    @Override
    public void show(Activity activity) {
        OffersManager.getInstance(activity).showOffersWall();
    }
}
