package com.vzhuan.ads;

import android.app.Activity;

/**
 * Created by lscm on 2015/1/8.
 */
public class AdvertiseContext {
    Advertise advertise;
    Activity activity;

    public AdvertiseContext setType(Activity activity, String type) {
        this.activity = activity;
        if ("AIM".equals(type)) {
            advertise = new AiMengAdvertise();
        } else if ("DIANLE".equals(type)) {
            advertise = new DianLeAdvertise();
        } else if ("WAPS".equals(type)) {
            advertise = new WapsAdvertise();
        } else if ("YOUMI".equals(type)) {
            advertise = new YoumiAdvertise();
        } else if ("BEIDUO".equals(type)) {
            advertise = new BeiDuoAdvertise();
        }else if ("DTN".equals(type)) {
            advertise = new DTNAdvertise();
        }else if ("DM".equals(type)) {
            advertise = new DAOWAdvertise();
        }else if ("MJF".equals(type)) {
            advertise = new MiJiAdvertise();
        }else if ("YGWX".equals(type)) {
            advertise = new YGWXAdvertise();
        }
        return this;
    }

    public void openAd() {
        if (advertise != null)
            advertise.show(activity);
    }
}
