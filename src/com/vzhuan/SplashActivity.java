package com.vzhuan;

import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class SplashActivity extends BaseActivity
{
    @Override public int doGetContentViewId()
    {
        return R.layout.splash;
    }

    @Override public void doInitDataes()
    {
        super.doInitDataes();
        boolean isfirstOpen = ShareUtil.getBoolean(this, ShareUtil.ShareKey.KEY_ISFIRST_OPEN, true);
        if (isfirstOpen)
        {
            startActivity(new Intent(this, GuideActivity.class));
            finish();
        }
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(3000);
        findViewById(android.R.id.content).startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationEnd(Animation arg0)
            {
                finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }

            @Override public void onAnimationRepeat(Animation animation)
            {
            }

            @Override public void onAnimationStart(Animation animation)
            {
            }
        });
    }
}
