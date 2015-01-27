package com.vzhuan;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by lscm on 2015/1/5.
 */
public class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManagerStack.getInstance().addActivity(this);
        setContentView(doGetContentViewId());
        doInitSubViews();
        doInitDataes();
    }

    public void doInitDataes() {
    }

    public void doInitSubViews() {
    }

    public int doGetContentViewId() {
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManagerStack.getInstance().finishActivity(this);
    }
}
