package com.vzhuan.ads;

/**
 * Created by lscm on 2015/1/8.
 */
public class AdvertiseContext
{
    Advertise advertise;

    private AdvertiseContext setType(String type)
    {
        if ("DOMOB".equals(type))
        {
            advertise = new DomobAdvertise();
        }
        else if ("DIANRU".equals(type))
        {
            advertise = new DianruAdvertise();
        }
        return this;
    }

    private void openAd()
    {
        advertise.show();
    }
}
