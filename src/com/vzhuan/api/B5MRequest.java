package com.vzhuan.api;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.vzhuan.Constants;
import com.vzhuan.DataUtil;
import com.vzhuan.MainApplication;
import org.json.JSONException;
import org.json.JSONObject;
import volley.*;
import volley.ex.B5MJsonRequest;

import java.util.HashMap;
import java.util.Map;

public class B5MRequest
{
    public static String TAG = "Volley";
    /***
     * 请求返回内容处理接口
     */
    protected IB5MResponseListener listerner;
    /***
     * 请求URL
     */
    private String mRequestUrl;
    /***
     * 是否启用缓存,默认false
     */
    private boolean mCacheEnable = false;
    /**
     * 缓存过期时间
     */
    private long mCacheExpiredTime = 0;
    /***
     * 请求超时设置
     */
    private int mRetryTimeOut = 10000;
    /***
     * 请求重试策略
     */
    private RetryPolicy mRetryPolicy;
    /***
     * 是否启用进度条显示
     */
    private boolean mShowProcessEnable = true;
    /***
     * 网络请求参数
     */
    private JSONObject mRequestBody;
    /***
     * 自定以的进度条状态UI
     */
    private View mView;
    /**
     * 请求类型
     * post or get
     */
    private int mMethod = Request.Method.POST;
    /**
     * 标识是否需要强制刷新缓存内容
     *
     */
    private boolean mRefreshCacheNeeded = false;
    /**
     * 用户自定义Tag
     */
    private String reqTag;

    /***
     * 构造一个新的请求
     * @param method API function
     * @param listener
     */
    public B5MRequest(String method, IB5MResponseListener listener)
    {
        this.listerner = listener;
        mRequestUrl = Constants.HOST + method;
        init();
    }

    public B5MRequest(IB5MResponseListener listener)
    {
        this.listerner = listener;
        init();
    }

    /***
     * 构造一个新的显示网络加载view的请求
     * @param method API function
     * @param listener
     */
    public B5MRequest(String method, View view, IB5MResponseListener listener)
    {
        this(method, listener);
        this.mView = view;
    }

    /***
     * 初始化默认参数
     */
    private void init()
    {
        mRetryPolicy = new DefaultRetryPolicy(mRetryTimeOut, 0, 2.0f);
        mRequestBody = new JSONObject();
    }

    public B5MRequest setRequestTimeOut(int time)
    {
        mRetryTimeOut = time;
        mRetryPolicy = new DefaultRetryPolicy(mRetryTimeOut, 0, 2.0f);
        return this;
    }

    /***
     * 如果要显示进度条的话,设置一个ProcessBar  
     * @param show true/false
     * @return
     */
    public B5MRequest setShowProcessBar(View view, boolean show)
    {
        this.mView = view;
        this.mShowProcessEnable = show;
        return this;
    }

    /***
     * 设置请求重试次数，默认是0
     * @param count
     * @return
     */
    public B5MRequest setRetryTimes(int count)
    {
        mRetryPolicy = new DefaultRetryPolicy(mRetryTimeOut, count, 2.0f);
        return this;
    }

    /***
     * 设置包含接口名称的请求Url，
     * @param urlString
     * @return
     */
    public B5MRequest setRequestUrl(String urlString)
    {
        if (!TextUtils.isEmpty(urlString))
            mRequestUrl = urlString;
        return this;
    }

    public String getUrl()
    {
        return mRequestUrl;
    }

    public B5MRequest setReqTag(String tag)
    {
        this.reqTag = tag;
        return this;
    }

    public JSONObject getRequestBody()
    {
        return mRequestBody;
    }

    /***
     * 设置请求参数
     */
    public B5MRequest setRequestBody(JSONObject requestBody)
    {
        mRequestBody = requestBody;
        String currentUserId = null;
        try
        {
            mRequestBody.put("userId", currentUserId);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /***
     * 设置缓存过期时间,通过设置这个时间来启动是否使用缓存
     * @param time 日期格式为毫秒
     * @return
     */
    public B5MRequest setCacheExpiredTime(long time)
    {
        this.mCacheExpiredTime = System.currentTimeMillis() + time;
        return this;
    }

    /***
     * 设置请求的方式GET/POST
     * @param method
     * @return
     */
    public B5MRequest setRequestMethod(int method)
    {
        mMethod = method;
        return this;
    }

    /**
     * 设置是否需要强制刷新缓存内容
     * @param needed
     * @return
     */
    public B5MRequest setRefreshCacheNeeded(boolean needed)
    {
        mRefreshCacheNeeded = needed;
        return this;
    }

    public void setmShowProcessEnable(boolean mShowProcessEnable)
    {
        this.mShowProcessEnable = mShowProcessEnable;
    }

    /***
     * 开始启动请求
     * @throws java.text.ParseException
     */
    public B5MRequest start()
    {
        Log.i(TAG, "url--->" + mRequestUrl);
        if (!DataUtil.isNetworkAvailable(MainApplication.getInstance()))
        {
            onResponseError(new NetworkError());
            if (mCacheExpiredTime == 0L)
            {
                return this;
            }
        }
        if (mShowProcessEnable)
            setProgressVisible(true);
        B5MJsonRequest mRequest = new B5MJsonRequest(mMethod, mRequestUrl, new Response.Listener<ResponseResult>()
        {
            @Override public void onResponse(ResponseResult response)
            {
                Log.i(TAG, "response--->" + response);
                if (mShowProcessEnable)
                    setProgressVisible(false);
                /**
                 * 请求返回200，即表示请求成功
                 */
                if (response != null && response.statusCode == 200)
                {
                    ResponseEntry re = new ResponseEntry(response.responseResult);
                    onResponseEntry(re);
                }
                /**请求失败**/
                else
                {
                    onResponseError(new ServerError());
                }
            }
        }, new Response.ErrorListener()
        {
            @Override public void onErrorResponse(VolleyError error)
            {
                Log.i(TAG, "response--->" + error);
                onResponseError(error);
            }
        });
        /**
         * 设置请求重试策略
         */
        mRequest.setRetryPolicy(mRetryPolicy);
        /**
         * POST请求方式设置请求参数
         */
        if (mMethod == Request.Method.POST)
            mRequest.setRequestBody(setRequestBody(mRequestBody).mRequestBody);
        Log.i(TAG, "param--->" + mRequestBody);
        /**
         * 设置默认请求头参数
         */
        mRequest.setHeaders(getRequestHeaders());
        /**设置是否强制刷新缓存数据*/
        mRequest.setRefreshCacheNeeded(mRefreshCacheNeeded);
        /**过期时间大于0时才会进行缓存*/
        if (mCacheExpiredTime > 0)
        {
            mCacheEnable = true;
            mRequest.setCacheExpiredTime(mCacheExpiredTime);
        }
        else
        {
            mCacheEnable = false;
        }
        /**
         * 设置缓存启用/禁用
         */
        mRequest.setShouldCache(mCacheEnable);
        if (!TextUtils.isEmpty(reqTag))
            TAG = reqTag;
        B5MVolley.addToRequestQueue(mRequest, TAG);
        return this;
    }

    protected void onResponseEntry(ResponseEntry re)
    {
        if ("1".equals(re.s))//s:1错误2正常
        {
            if (listerner != null)
                listerner.onResponseError(new VolleyError(re.msg));
        }
        else
        {
            if (listerner != null)
                listerner.onResponse(re);
        }
    }

    protected void onResponseError(VolleyError error)
    {
        if (mShowProcessEnable)
            setProgressVisible(false);
        listerner.onResponseError(error);
    }

    /**
     * 设置状态Bar是否显示
     * @param visiblity
     */
    public void setProgressVisible(boolean visiblity)
    {
        if (mView != null)
            mView.setVisibility(visiblity ? View.VISIBLE : View.GONE);
    }

    /***
     * 获取请求头中的信息，包含设备信息，版本号等
     * @return
     */
    private Map<String, String> getRequestHeaders()
    {
        Map<String, String> params = new HashMap<String, String>();
        //        String t = String.valueOf(new Date().getTime());
        //        params = B5MHeader.getInstance().getHead(t);
        //        params.put(B5MHeader.SIGN, B5MHeader.getInstance().getSign(t));
        return params;
    }

    public enum ETResult
    {
        TJSONOBJECT, TJSONARRAY, ETResult, TEMPTY
    }
}
