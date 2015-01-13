package com.vzhuan;

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
    }

    @Override public void doInitDataes()
    {
        super.doInitDataes();
        //
        userInfoRequest = new MyHttpRequestor().init(MainApplication.getInstance(), MyHttpRequestor.POST_METHOD, Constants.USERINFO, new HttpListener()
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
        TextView tv_status = (TextView) containerView.findViewById(R.id.tv_userStatus);
        TextView tv_name = (TextView) containerView.findViewById(R.id.tv_name);
        TextView tv_score = (TextView) containerView.findViewById(R.id.tv_score);
        ImageUtil.displayRoundImage(mUser.thumb, iv_avatar, 180, R.drawable.avatar);
        if ("ENABLE".equals(mUser.us))
            tv_status.setText("用户非法");
        tv_name.setText(mUser.name);
        tv_score.setText("￥:" + mUser.score);
    }

    private String getDid()
    {
        String primary_token = "8mStOqOeKdqhcjwpG6AAcHrSdiN5FjOzp";
        String preMd5 = primary_token + RegisterActivity.getImei() + primary_token;
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
