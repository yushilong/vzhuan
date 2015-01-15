package com.vzhuan;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.vzhuan.api.HttpListener;
import com.vzhuan.api.MyHttpRequestor;
import com.vzhuan.mode.User;
import com.vzhuan.viewpager.ViewPager;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lscm on 2015/1/5.
 */
public class HomeFragment extends BaseFragment
{
    ViewPager viewPager;
    private MyHttpRequestor userInfoRequest;
    private User mUser;

    @Override public int doGetContentViewId()
    {
        return R.layout.home;
    }

    @Override public void doInitSubViews(View containerView)
    {
        super.doInitSubViews(containerView);
        containerView.findViewById(R.id.iv_share).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                viewPager.setCurrentItem(2, true);
            }
        });
        containerView.findViewById(R.id.bt_startcoin).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                viewPager.setCurrentItem(1, true);
            }
        });
        containerView.findViewById(R.id.iv_weixin).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                startWeixin();
            }
        });
        setViewPagerScroller();
    }

    private void setViewPagerScroller()
    {
        try
        {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getActivity());
            // scroller.setFixedDuration(5000);
            mScroller.set(viewPager, scroller);
        }
        catch (NoSuchFieldException e)
        {
        }
        catch (IllegalArgumentException e)
        {
        }
        catch (IllegalAccessException e)
        {
        }
    }

    private void startWeixin()
    {
        try
        {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override public void doInitDataes()
    {
        super.doInitDataes();
        //
        userInfoRequest = new MyHttpRequestor().init(MyHttpRequestor.POST_METHOD, Constants.USERINFO, new HttpListener()
        {
            @Override public void onSuccess(String msg)
            {
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject(msg).optJSONObject("entity");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                mUser = new Gson().fromJson(jsonObject.toString(), User.class);
                UserManager.getInstance().setUser(mUser);
                initUserInfo();
            }

            @Override public void onFailure(int statusCode)
            {
            }
        });
        Map<String, Object> userInfo = new HashMap<String, Object>();
        userInfo.put("did", getDid());
        userInfoRequest.setParam(userInfo);
        userInfoRequest.start();
    }

    private void initUserInfo()
    {
        ImageView iv_avatar = (ImageView) containerView.findViewById(R.id.iv_avatar);
        ImageView iv_duigou = (ImageView) containerView.findViewById(R.id.iv_duigou);
        TextView tv_status = (TextView) containerView.findViewById(R.id.tv_status);
        TextView tv_name = (TextView) containerView.findViewById(R.id.tv_name);
        TextView tv_score = (TextView) containerView.findViewById(R.id.tv_score);
        ImageUtil.displayRoundImage(mUser.thumb, iv_avatar, 180, R.drawable.avatar);
        iv_duigou.setImageResource("ENABLE".equals(mUser.ds) ? R.drawable.duigou : R.drawable.cha);
        tv_status.setText("设备非法");
        tv_status.setVisibility("ENABLE".equals(mUser.us) ? View.VISIBLE : View.GONE);
        tv_name.setText(mUser.name);
        tv_score.setText("￥:" + mUser.score);
    }

    private String getDid()
    {
        String preMd5 = Constants.primary_token_did + Constants.getImei(getActivity()) + Constants.primary_token_did;
        return MD5.getMessageDigest(preMd5.getBytes());
    }

    @Override public void onDestroy()
    {
        super.onDestroy();
        userInfoRequest.releaseConnection();
    }

    public ViewPager getViewPager()
    {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager)
    {
        this.viewPager = viewPager;
    }
}
