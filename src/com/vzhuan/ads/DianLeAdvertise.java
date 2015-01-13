package com.vzhuan.ads;

import android.app.Activity;
import com.dlnetwork.Dianle;

/**
 * Created by lscm on 2015/1/8.
 */
public class DianLeAdvertise implements Advertise
{
    @Override public void show(Activity activity)
    {
        Dianle.showOffers(activity);
    }
}
