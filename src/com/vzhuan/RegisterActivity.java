package com.vzhuan;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.vzhuan.api.B5MRequest;
import com.vzhuan.api.IB5MResponseListenerImpl;
import com.vzhuan.api.ResponseEntry;
import com.vzhuan.mode.Code;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by lscm on 2015/1/7.
 */
public class RegisterActivity extends BaseActivity
{
    EditText et_code;
    ImageView iv_code;
    TextView tv_code;
    Dialog mDialog;
    B5MRequest getCodeRequest, registerRequest, checkAccessRequest;

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
        getCodeRequest = new B5MRequest(Constants.REGISTER_GETCODE, findViewById(R.id.progressBar), new IB5MResponseListenerImpl()
        {
            @Override public void onResponse(ResponseEntry entry)
            {
                super.onResponse(entry);
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject(entry.result);
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
            }
        });
        //
        registerRequest = new B5MRequest(Constants.REGISTER, findViewById(R.id.progressBar), new IB5MResponseListenerImpl()
        {
            @Override public void onResponse(ResponseEntry entry)
            {
                super.onResponse(entry);
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                ShareUtil.setBoolean(RegisterActivity.this, ShareUtil.ShareKey.KEY_ISFIRST_OPEN, false);
                finish();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
        //
        checkAccessRequest = new B5MRequest(Constants.REGISTER_CHECK_ACCESS, findViewById(R.id.progressBar), new IB5MResponseListenerImpl()
        {
            @Override public void onResponse(ResponseEntry entry)
            {
                super.onResponse(entry);
                JSONObject jsonObject = new JSONObject();
                registerRequest.setRequestBody(jsonObject).start();
            }
        });
    }

    public void toRegister(View view)
    {
        String code = et_code.getText().toString().trim();
        if (!TextUtils.isEmpty(code))
        {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));//for test
            finish();
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

    /** 保存方法 */
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
}
