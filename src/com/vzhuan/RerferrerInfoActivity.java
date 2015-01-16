package com.vzhuan;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.vzhuan.api.HttpListener;
import com.vzhuan.api.MyHttpRequestor;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lscm on 2015/1/14.
 */
public class RerferrerInfoActivity extends BaseActivity
{
    MyHttpRequestor submitReferRequest;
    ViewGroup inviteView, resultView;
    String invited;
    EditText et_id;
    long currentId;

    @Override public int doGetContentViewId()
    {
        return R.layout.referrer;
    }

    @Override public void doInitSubViews()
    {
        super.doInitSubViews();
        inviteView = (ViewGroup) findViewById(R.id.ll_invite);
        resultView = (ViewGroup) findViewById(R.id.rl_result);
        et_id = (EditText) findViewById(R.id.et_id);
    }

    @Override public void doInitDataes()
    {
        super.doInitDataes();
        invited = ShareUtil.getString(this, ShareUtil.ShareKey.UID, null);
        submitReferRequest = new MyHttpRequestor().init(MyHttpRequestor.POST_METHOD, Constants.SUBMIT_REFERRER, new HttpListener()
        {
            @Override public void onSuccess(String msg)
            {
                JSONObject resultObject = null;
                try
                {
                    resultObject = new JSONObject(msg);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                boolean isInvited = resultObject.optBoolean("entity");
                if (isInvited)
                {
                    inviteView.setVisibility(View.GONE);
                    resultView.setBackgroundResource(currentId == R.id.bt_2 ? R.drawable.get2yuan : R.drawable.get3yuan);
                    resultView.setVisibility(View.VISIBLE);
                }
            }

            @Override public void onFailure(int statusCode)
            {
            }
        });
    }

    public void threeYuan(View view)
    {
        currentId = view.getId();
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("invited", invited);
        String referId = et_id.getText().toString().trim();
        if (!TextUtils.isEmpty(referId))
            objectMap.put("referrer", referId);
        submitReferRequest.setParam(objectMap);
        submitReferRequest.start();
    }

    public void twoYuan(View view)
    {
        currentId = view.getId();
        submitReferRequest.start();
    }

    public void commit(View view)
    {
        finish();
        ShareUtil.setBoolean(this, ShareUtil.ShareKey.BONUSED, true);
        startActivity(new Intent(this, MainActivity.class));
    }
}
