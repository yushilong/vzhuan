package com.vzhuan.ads;

import android.app.Activity;
import com.bb.dd.BeiduoPlatform;

/**
 * Created by yushilong on 2015/3/24.
 */
public class BeiDuoAdvertise implements Advertise {
    @Override
    public void show(Activity activity) {
        BeiduoPlatform.showOfferWall();
    }
}
