package com.vzhuan.api;

/**
 * Created by lscm on 2015/1/13.
 */
public interface HttpListener
{
    void onSuccess(String msg);

    void onFailure(int statusCode);
}
