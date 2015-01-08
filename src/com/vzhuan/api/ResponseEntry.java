package com.vzhuan.api;

import android.text.TextUtils;
import com.vzhuan.JsonHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/***
 * ResponseEntry 
 * if response status code is 200,return this object,
 * or otherwise,deal it on default way.
 * @author lscm
 *
 */
public class ResponseEntry
{
    /***
     * 请求返回内容的类型
     * JSON object/JSON array/empty
     * 默认为空       TEMPTY
     */
    public B5MRequest.ETResult type = B5MRequest.ETResult.TEMPTY;
    /***
     * 返回内容字符串
     */
    public String result;
    /**
     * 返回成功码，可能没有,默认最大
     */
    public String s;
    /**
     * 请求返回错误码,可能没有,默认最大
     */
    public String ec;
    /**
     * 错误信息的提示内容,可能为空,默认为null;
     */
    public String msg;
    public boolean first = true;
    public boolean firstPage;
    public boolean last;
    public boolean lastPage;
    public int number;
    public int numberOfElements;
    public int size;
    public int totalElements;
    public int totalPages;

    public ResponseEntry()
    {
    }

    public ResponseEntry(String response)
    {
        if (!TextUtils.isEmpty(response))
            parase(response);
    }

    /***
     * 转化返回内容
     * @param str
     */
    public void parase(String str)
    {
        if (TextUtils.isEmpty(str))
            return;
        try
        {
            switch (JsonHelper.getJSONType(str))
            {
                case JSON_TYPE_OBJECT:
                    JSONObject json = new JSONObject(str);
                    parserHead(json);
                    type = B5MRequest.ETResult.TJSONOBJECT;
                    result = json.toString();
                    break;
                case JSON_TYPE_ARRAY:
                    JSONArray jsonArray = new JSONArray(str);
                    type = B5MRequest.ETResult.TJSONARRAY;
                    result = jsonArray.toString();
                    break;
                case JSON_TYPE_ERROR:
                    break;
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /***
     * 解析是否包含错误异常信息;
     * @param json
     * @throws org.json.JSONException
     */
    public void parserHead(JSONObject json) throws JSONException
    {
        this.s = json.optString("s");
        this.ec = json.optString("ec");
        this.msg = json.optString("emsg");
        this.first = json.optBoolean("first", true);
        this.firstPage = json.optBoolean("firstPage");
        this.last = json.optBoolean("last");
        this.lastPage = json.optBoolean("lastPage");
        this.number = json.optInt("number");
        this.numberOfElements = json.optInt("numberOfElements");
        this.size = json.optInt("size");
        this.totalElements = json.optInt("totalElements");
        this.totalPages = json.optInt("totalPages");
    }
}
