package com.vzhuan.api;

import volley.VolleyError;

/***
 * 提供一个接口，用于处理Http请求的返回内容,只需处理成功返回的情况
 * @author lscm
 */
public interface IB5MResponseListener
{
    /***
     * 需要提供实现的处理请求返回内容的接口 
     * @param entry
     */
    public void onResponse(ResponseEntry entry);

    /**
     * 接口调用失败，包括服务器链接错误/网络错误/接口调用返回数据错误等情况
     */
    public void onResponseError(VolleyError error);
}
