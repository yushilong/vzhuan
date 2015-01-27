package com.vzhuan.api;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.vzhuan.Constants;
import com.vzhuan.ExecutorServiceUtil;
import com.vzhuan.MainApplication;
import com.vzhuan.R;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lscm on 2015/1/13.
 */
public class MyHttpRequestor {
    public static final byte GET_METHOD = 0x00;
    public static final byte POST_METHOD = 0x01;
    public static final byte PUT_METHOD = 0x02;
    public static final byte PATCH_METHOD = 0x03;
    public static final byte DELETE_METHOD = 0x04;
    public static final byte HEAD_METHOD = 0x05;
    public static final byte OPTIONS_METHOD = 0x06;
    public static final byte TRACE_METHOD = 0x07;
    public static final String UTF_8 = "UTF-8";
    public static final String DESC = "descend";
    public static final String ASC = "ascend";
    public final static int TIMEOUT_CONNECTION = 20000;// 连接超时时间
    public final static int TIMEOUT_SOCKET = 20000;// socket超时
    private String url;
    // 网络请求agent
    private static String appUserAgent;
    // 请求client
    private HttpClient _httpClient;
    // 请求方法类型
    private byte _methodType;
    // 请求方法
    private HttpMethod _method;
    //private String _method = "GET"; // 默认用GET方式请求
    private Map<String, Object> _data = new HashMap<String, Object>();// 请求表单参数
    private Application mContext;
    private HttpListener mHttpListener;
    private String TAG = "HTTP";
    private Handler mHandler;
    private String responseBodyString;

    private enum METHOD {
        GET, PUT, POST, PATCH, DELETE, HEAD, OPTIONS, TRACE;

        public static String prettyValues() {
            METHOD[] methods = METHOD.values();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < methods.length; i++) {
                METHOD method = methods[i];
                builder.append(method.toString());
                if (i != methods.length - 1) {
                    builder.append(", ");
                }
            }
            return builder.toString();
        }
    }

    /**
     * 获得一个http连接
     *
     * @return
     */
    private static HttpClient getHttpClient() {
        HttpClient httpClient = new HttpClient();
        // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
        //        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        // 设置 连接超时时间
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
        // 设置 读数据超时时间
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
        // 设置 字符集
        httpClient.getParams().setContentCharset(UTF_8);
        return httpClient;
    }

    private static HttpMethod getMethod(byte methodType, String url, String userAgent) {
        HttpMethod httpMethod = null;
        switch (methodType) {
            case GET_METHOD:
                httpMethod = new GetMethod(url);
                break;
            case POST_METHOD:
                httpMethod = new PostMethod(url);
                break;
            case PUT_METHOD:
                httpMethod = new PutMethod(url);
                break;
            case PATCH_METHOD:
                //httpMethod = new
                break;
            case DELETE_METHOD:
                httpMethod = new DeleteMethod(url);
                break;
            case HEAD_METHOD:
                httpMethod = new HeadMethod(url);
                break;
            case OPTIONS_METHOD:
                httpMethod = new OptionsMethod(url);
                break;
            case TRACE_METHOD:
                httpMethod = new TraceMethod(url);
                break;
            default:
                break;
        }
        if (null == httpMethod) {
            // 抛出一个不支持的请求方法
            throw new IllegalArgumentException("Invalid HTTP Method: UnKonwn" + ". Must be one of " + METHOD.prettyValues());
        }
        // 设置 请求超时时间
        httpMethod.getParams().setSoTimeout(TIMEOUT_SOCKET);
        //        httpMethod.setRequestHeader("Host", URLs.HOST);
        //        httpMethod.setRequestHeader("Accept-Encoding", "gzip,deflate,sdch");
        //        httpMethod.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
        //        httpMethod.setRequestHeader("Connection", "Keep-Alive");
//                httpMethod.setRequestHeader(HTTP.USER_AGENT, userAgent);
        httpMethod.setRequestHeader("Client-Agent", getClientAgent());
        return httpMethod;
    }

    private static String getClientAgent() {
        return "Android " + android.os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;
    }

    public MyHttpRequestor init(byte methodType, String url, HttpListener httpListener) {
        _methodType = methodType;
        _httpClient = getHttpClient();
        this.mContext = MainApplication.getInstance();
        this.url = Constants.HOST + url;
        //        String urser_agent = appContext != null ? getUserAgent(appContext) : "";
        String urser_agent = "";
        _method = getMethod(methodType, this.url, urser_agent);
        this.mHttpListener = httpListener;
        this.mHandler = new Handler();
        return this;
    }
    //    /**
    //     * 获得UserAgent
    //     * @param appContext
    //     * @return
    //     */
    //    private static String getUserAgent(Context appContext)
    //    {
    //        if (appUserAgent == null || appUserAgent == "")
    //        {
    //            StringBuilder ua = new StringBuilder("Git@OSC.NET");
    //            ua.append('/' + appContext.getPackageInfo().versionName + '_' + appContext.getPackageInfo().versionCode);//App版本
    //            ua.append("/Android");//手机系统平台
    //            ua.append("/" + android.os.Build.VERSION.RELEASE);//手机系统版本
    //            ua.append("/" + android.os.Build.MODEL); //手机型号
    //            ua.append("/" + appContext.getAppId());//客户端唯一标识
    //            appUserAgent = ua.toString();
    //        }
    //        return appUserAgent;
    //    }

    public void start() {
        if (!isNetworkConnected()) {
            Toast.makeText(mContext, mContext.getString(R.string.network_not_connected), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, "url--->" + url);
        // 设置请求参数
        if (hasOutput()) {
            submitData();
        }
        responseBodyString = null;
        //        GZIPInputStream gis = null;
        ExecutorServiceUtil.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final int statusCode = _httpClient.executeMethod(_method);
                    if (statusCode != HttpStatus.SC_OK) {
                        //                uploadErrorToServer(mContext, url, statusCode, getMethod(_methodType) + "  " + _method.getResponseBodyAsString() + "  " + getUserAgent(mContext) + "  " + getJsonString(_data));
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                AppException.http(statusCode).makeToast(mContext);
                                if (mHttpListener != null)
                                    mHttpListener.onFailure(statusCode, null);
                            }
                        });
                    } else {
                        responseBodyString = _method.getResponseBodyAsString();
                        if (mHttpListener != null)
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject resultObject = null;
                                    try {
                                        resultObject = new JSONObject(responseBodyString);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if ("2".equals(resultObject.optString("s"))) {
                                        mHttpListener.onSuccess(resultObject.toString());
                                    } else {
                                        String emsg = resultObject.optString("emsg");
                                        int ec = Integer.valueOf(resultObject.optString("ec"));
                                        mHttpListener.onFailure(ec, emsg);
                                        Toast.makeText(mContext, emsg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    }
                } catch (final HttpException e) {
                    // 发生致命的异常，可能是协议不对或者返回的内容有问题
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            AppException.http(e).makeToast(mContext);
                        }
                    });
                } catch (final IOException e) {
                    // 发生网络异常
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            AppException.network(e).makeToast(mContext);
                        }
                    });
                } finally {
                    // 释放连接
                    //                    releaseConnection();
                }
                Log.i(TAG, "response-->" + responseBodyString);
            }
        });
    }

    public void releaseConnection() {
        // 释放连接
        if (_method != null) {
            _method.releaseConnection();
        }
        if (_httpClient != null) {
            _httpClient = null;
        }
    }

    /**
     * 判断是否需要设置表单参数
     *
     * @param methodType
     * @return
     */
    private boolean hasOutput() {
        return _methodType == POST_METHOD || _methodType == PUT_METHOD;
    }

    private String getMethod(byte method) {
        String res = "";
        switch (method) {
            case GET_METHOD:
                res = "GET";
                break;
            case POST_METHOD:
                res = "POST";
                break;
            case PUT_METHOD:
                res = "PUT";
                break;
            case PATCH_METHOD:
                res = "PATCH";
                break;
            case DELETE_METHOD:
                res = "DELETE";
                break;
            case HEAD_METHOD:
                res = "HEAD";
                break;
            case OPTIONS_METHOD:
                res = "OPTIONS";
                break;
            case TRACE_METHOD:
                res = "TRACE";
                break;
        }
        return res;
    }

    /**
     * 表单参数处理
     */
    private void submitData() {
        _method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        if (_method instanceof PostMethod) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (_data != null) {
                for (String name : _data.keySet()) {
                    if (((PostMethod) _method).getParameter(name) != null)
                        ((PostMethod) _method).removeParameter(name);
                    Object value = _data.get(name);
                    Log.i(TAG, name + "=" + value);
                    NameValuePair nvp = new NameValuePair(name, String.valueOf(value));
                    nvps.add(nvp);
                }
            }
            for (NameValuePair nameValuePair : nvps) {
                ((PostMethod) _method).addParameter(nameValuePair);
            }
        }
        if (_method instanceof PutMethod) {
            Part parts[] = new Part[_data.size()];
            int i = 0;
            for (String name : _data.keySet()) {
                Object value = _data.get(name);
                parts[i++] = new StringPart(name, String.valueOf(value));
            }
            ((PutMethod) _method).setRequestEntity(new MultipartRequestEntity(parts, _method.getParams()));
        }
    }

    public Map<String, Object> getParam() {
        return _data;
    }

    public void setParam(Map<String, Object> _data) {
        this._data = _data;
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
