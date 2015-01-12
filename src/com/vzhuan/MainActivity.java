package com.vzhuan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.Toast;
import cn.sharesdk.demo.Laiwang;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.vzhuan.api.B5MRequest;
import com.vzhuan.api.IB5MResponseListenerImpl;
import com.vzhuan.api.ResponseEntry;
import com.vzhuan.baidupush.Utils;
import com.vzhuan.viewpager.FragmentPagerAdapter;
import com.vzhuan.viewpager.ViewPager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lscm on 2015/1/5.
 */
public class MainActivity extends BaseActivity {
    public static String TEST_IMAGE;
    public static String TEST_IMAGE_URL;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private B5MRequest userInfoRequest;

    @Override
    public int doGetContentViewId() {
        return R.layout.main;
    }

    @Override
    public void doInitDataes() {
        super.doInitDataes();
        ShareSDK.initSDK(this);
        ShareSDK.registerPlatform(Laiwang.class);
        ShareSDK.setConnTimeout(20000);
        ShareSDK.setReadTimeout(20000);
        //
        initBaiduPush();
        //
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
        //
        userInfoRequest = new B5MRequest(Constants.USERINFO, new IB5MResponseListenerImpl() {
            @Override
            public void onResponse(ResponseEntry entry) {
                super.onResponse(entry);
            }
        });
        JSONObject userInfoObject = new JSONObject();
        try {
            userInfoObject.put("did", "123456");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userInfoRequest.setRequestBody(userInfoObject);
        userInfoRequest.start();
    }

    private void initBaiduPush() {
        if (!Utils.hasBind(getApplicationContext())) {
            // 请将AndroidManifest.xml 104行处 api_key 字段值修改为自己的 api_key 方可使用 ！！
            // ATTENTION：You need to modify the value of api_key to your own at row 104 in AndroidManifest.xml to use this Demo !!
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Constants.BAIDU_PUSH_APIKEY);
            // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
            //            PushManager.enableLbs(getApplicationContext());
        }
        if (PushManager.isPushEnabled(getApplicationContext())) {
            PushManager.resumeWork(getApplicationContext());
        }
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
        super.onDestroy();
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
}
