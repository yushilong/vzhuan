package com.vzhuan.ads;

import android.app.Activity;

/**
 * Created by lscm on 2015/1/8.
 */
public class AdvertiseContext
{
    Advertise advertise;
    Activity activity;

    public AdvertiseContext setType(Activity activity, String type)
    {
        this.activity = activity;
        if ("AIM".equals(type))
        {
            advertise = new AiMengAdvertise();
        }
        else if ("DIANLE".equals(type))
        {
            advertise = new DianLeAdvertise();
        }
        else if ("WAPS".equals(type))
        {
            advertise = new WapsAdvertise();
        }
        else if ("YOUMI".equals(type))
        {
            advertise = new YoumiAdvertise();
        }
        return this;
    }

    public void openAd()
    {
        if (advertise != null)
            advertise.show(activity);
    }
}
