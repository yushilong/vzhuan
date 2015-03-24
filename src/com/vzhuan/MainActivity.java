package com.vzhuan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import cn.aow.android.DAOW;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.waps.AppConnect;
import com.bb.dd.BeiduoPlatform;
import com.datouniao.AdPublisher.AppConfig;
import com.dlnetwork.Dianle;
import com.miji.MijiConnect;
import com.testin.cloud.tesaclo;
import com.vzhuan.eventbus.EventBus;
import com.vzhuan.eventbus.EventNames;
import com.vzhuan.viewpager.FragmentPagerAdapter;
import com.vzhuan.viewpager.ViewPager;
import com.winad.android.offers.AdManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lscm on 2015/1/5.
 */
public class MainActivity extends BaseActivity implements EventBus.SubscriberChangeListener {
    public static String TEST_IMAGE;
    public static String TEST_IMAGE_URL;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private ImageView iv_rocket;

    @Override
    public int doGetContentViewId() {
        return R.layout.main;
    }

    @Override
    public void doInitSubViews() {
        super.doInitSubViews();
        iv_rocket = (ImageView) findViewById(R.id.iv_rocket);
        iv_rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_rocket.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.rocket));
                if (mViewPager != null)
                    mViewPager.setCurrentItem(0, true);
            }
        });
    }

    @Override
    public void doInitDataes() {
        super.doInitDataes();
        ShareSDK.initSDK(this);
//        ShareSDK.registerPlatform(Laiwang.class);
        ShareSDK.setConnTimeout(20000);
        ShareSDK.setReadTimeout(20000);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        fragments = new ArrayList<Fragment>();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setViewPager(mViewPager);
        fragments.add(0, homeFragment);
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setViewPager(mViewPager);
        fragments.add(1, taskFragment);
        ShareFragment shareFragment = new ShareFragment();
        shareFragment.setViewPager(mViewPager);
        fragments.add(2, shareFragment);
        mViewPager.setAdapter(new VerticalPager(getSupportFragmentManager()));
        //ads config
        initAds();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                iv_rocket.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //        startActivity(new Intent(this,RerferrerInfoActivity.class));
    }

    private void initAds() {
        //dianle
        Dianle.initGoogleContext(this, "c96e880f25579db0645a73dddd6fc1b0");
        Dianle.setCurrentUserID(this, Constants.getDid());
        //appoffer
        AppConnect.getInstance("e962f6e8021a4d4db681772ac9783dae", "waps", this);
        AppConnect.getInstance(this).getConfig(Constants.getDid());
        //youmi
        tesaclo.getInstance(this).weaklp();
        //aimeng
        //beiduo
        BeiduoPlatform.setAppId(this,"13535","14c46f9e7471112");
        BeiduoPlatform.setUserId(Constants.getDid());
        //大头鸟
        AppConfig appConfig = new AppConfig();
        appConfig.setAppID("c65e9440-0ca7-49fc-affd-46c3609c68fd");
        appConfig.setSecretKey("ylssasxbjasv");
        //米积分
        MijiConnect.requestConnect(this,"d27a7cb7e7a6a8929a87767bc74e5d4e","google");
        //赢告无限
        AdManager.setAPPID(this,"FE62770D3B6D0B8F72D7F94EB7F5F2B8504D51AC");
        AdManager.setUserID(this,Constants.getDid());
        //多盟
        DAOW.getInstance(this).init(this,"appid","userId");
    }

    @Override
    public void onSubscriberDataChanged(Object notificationName, Object notificateContent) {
        findViewById(R.id.ll_logining).setVisibility(View.GONE);
    }

    class VerticalPager extends FragmentPagerAdapter {
        public VerticalPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    protected void onDestroy() {
        // 回收积分墙资源
        tesaclo.getInstance(this).webclp();
        super.onDestroy();
        EventBus.getInstance().unSubscribe(EventNames.LOGIN_SUCCESS, this);
        AppManagerStack.getInstance().AppExit(this);
    }

    /**
     * 将action转换为String
     */
    public static String actionToString(int action) {
        switch (action) {
            case Platform.ACTION_AUTHORIZING:
                return "ACTION_AUTHORIZING";
            case Platform.ACTION_GETTING_FRIEND_LIST:
                return "ACTION_GETTING_FRIEND_LIST";
            case Platform.ACTION_FOLLOWING_USER:
                return "ACTION_FOLLOWING_USER";
            case Platform.ACTION_SENDING_DIRECT_MESSAGE:
                return "ACTION_SENDING_DIRECT_MESSAGE";
            case Platform.ACTION_TIMELINE:
                return "ACTION_TIMELINE";
            case Platform.ACTION_USER_INFOR:
                return "ACTION_USER_INFOR";
            case Platform.ACTION_SHARE:
                return "ACTION_SHARE";
            default: {
                return "UNKNOWN";
            }
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return true;
            } else {
                AppManagerStack.getInstance().AppExit(MainActivity.this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getInstance().subscribe(EventNames.LOGIN_SUCCESS, this);
    }
}
