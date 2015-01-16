package com.vzhuan.ads;

import android.app.Activity;
import com.esas.cned.Eecx;
import com.vzhuan.MainApplication;

/**
 * Created by lscm on 2015/1/8.
 */
public class AiMengAdvertise implements Advertise
{
    @Override public void show(Activity activity)
    {
        Eecx eecx = Eecx.getInstance(activity, "bbd42ed55f7c59de4fd0109a9a14d327");
        eecx.start(MainApplication.getInstance());
//        Aww.getInstance(activity).show(activity, "bbd42ed55f7c59de4fd0109a9a14d327");
//        Aww.getInstance(activity).e(activity, "bbd42ed55f7c59de4fd0109a9a14d327");
//        Aww.getInstance(MainApplication.getInstance()).show(MainApplication.getInstance(),"bbd42ed55f7c59de4fd0109a9a14d327");
    }
}
