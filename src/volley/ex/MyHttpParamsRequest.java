package volley.ex;

import com.vzhuan.api.ResponseResult;
import org.json.JSONObject;
import volley.AuthFailureError;
import volley.NetworkResponse;
import volley.Request;
import volley.Response;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yushilong on 15-1-12.
 */
public class MyHttpParamsRequest extends HttpParamsRequest<ResponseResult> {

    private Map<String, String> headers = new HashMap<String, String>();
    private Request.Priority priority = null;
    public int statusCode;
    public String msg;

    public MyHttpParamsRequest(int method, String url, String requestBody, Response.Listener<ResponseResult> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    public MyHttpParamsRequest(int method, String url, Response.Listener<ResponseResult> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        return headers;
    }

    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }

    @Override
    protected Response<ResponseResult> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    public byte[] getBody()
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

    @Override
    public String getRequestBody() throws AuthFailureError
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
