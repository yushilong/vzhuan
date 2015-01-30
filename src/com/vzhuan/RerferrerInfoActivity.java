package com.vzhuan;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class RerferrerInfoActivity extends BaseActivity {
    MyHttpRequestor submitReferRequest;
    ViewGroup inviteView, resultView;
    String invited;
    EditText et_id;
    long currentId;

    @Override
    public int doGetContentViewId() {
        return R.layout.referrer;
    }

    @Override
    public void doInitSubViews() {
        super.doInitSubViews();
        inviteView = (ViewGroup) findViewById(R.id.ll_invite);
        resultView = (ViewGroup) findViewById(R.id.rl_result);
        et_id = (EditText) findViewById(R.id.et_id);
    }

    @Override
    public void doInitDataes() {
        super.doInitDataes();
        invited = ShareUtil.getString(this, ShareUtil.ShareKey.UID, null);
        submitReferRequest = new MyHttpRequestor().init(MyHttpRequestor.POST_METHOD, Constants.SUBMIT_REFERRER, new HttpListener() {
            @Override
            public void onSuccess(String msg) {
                JSONObject resultObject = null;
                try {
                    resultObject = new JSONObject(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                boolean isInvited = resultObject.optBoolean("entity");
                if (isInvited) {
                    //
                    showResultDialog();
                }
            }

            @Override
            public void onFailure(int statusCode, String emsg) {
                finish();
                startActivity(new Intent(RerferrerInfoActivity.this, MainActivity.class));
            }
        });
    }

    private void showResultDialog() {
        View view = View.inflate(this, R.layout.rerferresult, null);
        view.setBackgroundResource(currentId == R.id.bt_2 ? R.drawable.get2yuan : R.drawable.get3yuan);
        Button bt_commit = (Button) view.findViewById(R.id.bt_commit);
        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit(v);
            }
        });
        Dialog mDialog = new Dialog(RerferrerInfoActivity.this, R.style.MyDialog);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = getDisplayMetrics().widthPixels * 3 / 4;
        layoutParams.height = getDisplayMetrics().heightPixels * 2 / 3;
        mDialog.setContentView(view, layoutParams);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public void threeYuan(View view) {
        currentId = view.getId();
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("invited", invited);
        String referId = et_id.getText().toString().trim();
        if (!TextUtils.isEmpty(referId))
            objectMap.put("referrer", referId);
        submitReferRequest.setParam(objectMap);
        submitReferRequest.start();
    }

    public void twoYuan(View view) {
        currentId = view.getId();
        submitReferRequest.start();
    }

    public void commit(View view) {
        finish();
        ShareUtil.setBoolean(this, ShareUtil.ShareKey.BONUSED, true);
        startActivity(new Intent(this, MainActivity.class));
    }

    public DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
}
