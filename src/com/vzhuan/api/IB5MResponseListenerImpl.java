package com.vzhuan.api;

import android.text.TextUtils;
import android.widget.Toast;
import com.vzhuan.MainApplication;
import volley.*;

public class IB5MResponseListenerImpl implements IB5MResponseListener
{
    public static final String ERROR_AUTH = "认证失败";
    public static final String ERROR_PARSE = "解析错误";
    public static final String ERROR_NET = "网络错误";
    public static final String ERROR_TIMEOUT = "数据超时";

    @Override public void onResponse(ResponseEntry entry)
    {
        // TODO Auto-generated method stub
    }

    @Override public void onResponseError(VolleyError error)
    {
        // TODO Auto-generated method stub
        String errId = null;
        if (error instanceof AuthFailureError)
        {
            errId = ERROR_AUTH;
        }
        else if (error instanceof ParseError)
        {
            errId = ERROR_PARSE;
        }
        else if (error instanceof NoConnectionError)
        {
            errId = ERROR_NET;
        }
        else if (error instanceof TimeoutError)
        {
            errId = ERROR_TIMEOUT;
        }
        else if (error instanceof NetworkError)
        {
            errId = ERROR_NET;
        }
        if (!TextUtils.isEmpty(errId))
            Toast.makeText(MainApplication.getInstance(), errId, Toast.LENGTH_SHORT).show();
    }
}
