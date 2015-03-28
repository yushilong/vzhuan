package com.vzhuan.ads;

import android.app.Activity;
import com.testin.cloud.tesaclo;

/**
 * Created by lscm on 2015/1/8.
 */
public class YoumiAdvertise implements Advertise {
    @Override
    public void show(Activity activity) {
        tesaclo.getInstance(activity).webolp();
    }
}