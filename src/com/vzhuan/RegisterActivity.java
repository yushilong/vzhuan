package com.vzhuan;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import com.google.gson.Gson;
import com.vzhuan.api.HttpListener;
import com.vzhuan.api.MyHttpRequestor;
import com.vzhuan.mode.Code;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lscm on 2015/1/7.
 */
public class RegisterActivity extends BaseActivity
{
    EditText et_code;
    ImageView iv_code;
    TextView tv_code;
    Dialog mDialog;
    MyHttpRequestor getCodeRequest, registerRequest, checkAccessRequest;
    private String code;

    @Override public int doGetContentViewId()
    {
        return R.layout.register;
    }

    @Override public void doInitSubViews()
    {
        super.doInitSubViews();
        et_code = (EditText) findViewById(R.id.et_code);
    }

    @Override public void doInitDataes()
    {
        super.doInitDataes();
        //
        if (JPushInterface.isPushStopped(MainApplication.getInstance()))
        {
            JPushInterface.resumePush(MainApplication.getInstance());
        }
        Log.d(RegisterActivity.class.getSimpleName(), "Registration Id : " + JPushInterface.getRegistrationID(MainApplication.getInstance()));
        //
        getCodeRequest = new MyHttpRequestor().init(MainApplication.getInstance(), MyHttpRequestor.GET_METHOD, Constants.REGISTER_GETCODE, new HttpListener()
        {
            @Override public void onSuccess(String msg)
            {
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject(msg);
                    jsonObject = jsonObject.getJSONObject("entity");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                Code code = gson.fromJson(jsonObject.toString(), Code.class);
                ImageUtil.displayDefaultNotPeopleRoundImage(Constants.HOST + code.wxTwoDimensionCode, iv_code);
                tv_code.setText(code.wxAccount);
                ShareUtil.setString(RegisterActivity.this, ShareUtil.ShareKey.SHARE_TITLE, code.context);
                ShareUtil.setString(RegisterActivity.this, ShareUtil.ShareKey.SHARE_URL, code.referrer);
                ShareUtil.setString(RegisterActivity.this, ShareUtil.ShareKey.SHARE_DOWNLOAD, code.download);
            }

            @Override public void onFailure(int statusCode)
            {
            }
        });
        //
        registerRequest = new MyHttpRequestor().init(MainApplication.getInstance(), MyHttpRequestor.POST_METHOD, Constants.REGISTER, new HttpListener()
        {
            @Override public void onSuccess(String msg)
            {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                ShareUtil.setBoolean(RegisterActivity.this, ShareUtil.ShareKey.KEY_ISFIRST_OPEN, false);
                finish();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }

            @Override public void onFailure(int statusCode)
            {
            }
        });
        //
        checkAccessRequest = new MyHttpRequestor().init(MainApplication.getInstance(), MyHttpRequestor.POST_METHOD, Constants.REGISTER_CHECK_ACCESS, new HttpListener()
        {
            @Override public void onSuccess(String msg)
            {
                Map<String, Object> register = new HashMap<String, Object>();
                register.put("mac", getImei());
                register.put("token", JPushInterface.getRegistrationID(MainApplication.getInstance()));
                register.put("openUdid", getImei());
                register.put("access", code);
                registerRequest.setParam(register);
                registerRequest.start();
            }

            @Override public void onFailure(int statusCode)
            {
            }
        });
    }

    public void toRegister(View view)
    {
        code = et_code.getText().toString().trim();
        if (!TextUtils.isEmpty(code))
        {
            Map<String, Object> checkAddress = new HashMap<String, Object>();
            checkAddress.put("access", code);
            checkAccessRequest.setParam(checkAddress);
            checkAccessRequest.start();
        }
        else
        {
            et_code.requestFocus();
            et_code.setError("请填写邀请码");
        }
    }

    public void showMethod(View view)
    {
        if (mDialog == null)
            initDialog();
        getCodeRequest.start();
        if (!mDialog.isShowing())
            mDialog.show();
    }

    private void initDialog()
    {
        mDialog = new Dialog(this, R.style.MyDialog);
        View view = View.inflate(this, R.layout.dialog_getcode, null);
        iv_code = (ImageView) view.findViewById(R.id.iv_code);
        iv_code.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                Bitmap bitmap = ((BitmapDrawable) iv_code.getDrawable()).getBitmap();
                saveBitmap(bitmap);
            }
        });
        tv_code = (TextView) view.findViewById(R.id.tv_code);
        tv_code.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override public boolean onLongClick(View view)
            {
                ClipboardManager clipboardManager = (ClipboardManager) RegisterActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simpleText", tv_code.getText());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(RegisterActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mDialog.setContentView(view);
    }

    public void closeDialog(View view)
    {
        mDialog.dismiss();
    }

    /**
     * 保存方法
     */
    public void saveBitmap(Bitmap bm)
    {
        File fd = new File("/sdcard/vzhuan/");
        if (!fd.exists())
            fd.mkdirs();
        File f = new File("/sdcard/vzhuan/", new Date().getTime() + ".jpg");
        if (f.exists())
        {
            f.delete();
        }
        try
        {
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    public static String getImei()
    {
        return ((TelephonyManager) MainApplication.getInstance().getSystemService(TELEPHONY_SERVICE)).getDeviceId();
    }

    @Override protected void onDestroy()
    {
        super.onDestroy();
        getCodeRequest.releaseConnection();
        checkAccessRequest.releaseConnection();
        registerRequest.releaseConnection();
    }
}
