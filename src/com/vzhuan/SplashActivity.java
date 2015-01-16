package com.vzhuan;

import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import cn.jpush.android.api.JPushInterface;

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
//                if (!ShareUtil.getBoolean(SplashActivity.this, ShareUtil.ShareKey.BONUSED, false))
//                {
//                    startActivity(new Intent(SplashActivity.this, RerferrerInfoActivity.class));
//                }
//                else
//                {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                }
                finish();
            }

            @Override public void onAnimationRepeat(Animation animation)
            {
            }

            @Override public void onAnimationStart(Animation animation)
            {
            }
        });
    }

    @Override protected void onResume()
    {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override protected void onPause()
    {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
