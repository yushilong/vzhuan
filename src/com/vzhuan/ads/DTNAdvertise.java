package com.vzhuan.ads;

import android.app.Activity;
import com.datouniao.AdPublisher.AppConfig;
import com.datouniao.AdPublisher.AppConnect;
import com.vzhuan.Constants;

/**
 * Created by yushilong on 2015/3/24.
 */
public class DTNAdvertise implements Advertise {
    @Override
    public void show(Activity activity) {
        AppConfig appConfig = new AppConfig();
        appConfig.setAppID("c65e9440-0ca7-49fc-affd-46c3609c68fd");
        appConfig.setSecretKey("ylssasxbjasv");
        appConfig.setCtx(activity);
        appConfig.setClientUserID(Constants.getDid());
        AppConnect.getInstance(appConfig).ShowAdsOffers();
    }
}
