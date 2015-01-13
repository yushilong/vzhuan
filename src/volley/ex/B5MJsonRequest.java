package volley.ex;

import com.vzhuan.api.ResponseResult;
import org.json.JSONObject;
import volley.AuthFailureError;
import volley.Request;
import volley.Response;
import volley.toolbox.JsonResponseRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class B5MJsonRequest extends JsonResponseRequest
{
    private Map<String, String> headers = new HashMap<String, String>();
    private Request.Priority priority = null;
    private JSONObject mRequestBody;
    public int statusCode;
    public String msg;

    public B5MJsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener<ResponseResult> listener, Response.ErrorListener errorListener)
    {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public B5MJsonRequest(int method, String url, Response.Listener<ResponseResult> listener, Response.ErrorListener errorListener)
    {
        super(method, url, listener, errorListener);
    }

    public B5MJsonRequest(String url, JSONObject jsonRequest, Response.Listener<ResponseResult> listener, Response.ErrorListener errorListener)
    {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override public Map<String, String> getHeaders() throws AuthFailureError
    {
        return headers;
    }

    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }

    @Override public byte[] getBody()
    {
        try
        {
            return mRequestBody == null ? null : mRequestBody.toString().getBytes(PROTOCOL_CHARSET);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setRequestBody(JSONObject mRequestBody)
    {
        this.mRequestBody = mRequestBody;
    }

    @Override public String getRequestBody() throws AuthFailureError
    {
        return mRequestBody + "";
    }

    public void setPriority(Priority priority)
    {
        this.priority = priority;
    }

    public Priority getPriority()
    {
        if (this.priority != null)
        {
            return priority;
        }
        else
        {
            return Priority.NORMAL;
        }
    }
}
