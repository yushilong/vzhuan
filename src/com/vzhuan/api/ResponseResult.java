package com.vzhuan.api;

import android.text.TextUtils;
import com.vzhuan.JsonHelper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ResponseResult
 * @author unknown
 *
 * modify log: 2014/10/24 jdd
 *
 */
public class ResponseResult
{
    /**
     * HTTP请求响应码
     */
    public int statusCode;
    /**
     * 返回数据内容
     */
    public String responseResult;
    /**
     * 帮5买接口返回内容数据可能存在的错误码
     */
    public String s;

    public ResponseResult(int statusCode, String responseResult)
    {
        super();
        this.statusCode = statusCode;
        this.responseResult = responseResult;
        if (!TextUtils.isEmpty(responseResult))
        {
            try
            {
                switch (JsonHelper.getJSONType(responseResult))
                {
                    case JSON_TYPE_OBJECT:
                        JSONObject json = new JSONObject(responseResult);
                        this.s = json.optString("s");
                        break;
                    case JSON_TYPE_ARRAY:
                    case JSON_TYPE_ERROR:
                        break;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
