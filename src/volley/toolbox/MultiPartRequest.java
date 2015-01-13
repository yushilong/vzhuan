package volley.toolbox;

import org.apache.http.HttpEntity;
import org.json.JSONObject;
import volley.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/***
 * 扩展请求，实现文件上传功能
 * @author lscm
 *
 */
public class MultiPartRequest extends Request<String>
{
    private MultipartRequestParams mParams = null;
    private HttpEntity httpEntity = null;
    private Response.Listener<String> mListener;

    public MultiPartRequest(int method, String url, JSONObject jsonRequest, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(method, url, errorListener);
    }

    public MultiPartRequest(String url, MultipartRequestParams params, JSONObject jsonRequest, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        this(Method.POST, url, jsonRequest, listener, errorListener);
        this.mParams = params;
        this.mListener = listener;
    }

    @Override public byte[] getBody() throws AuthFailureError
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (mParams != null)
        {
            httpEntity = mParams.getEntity();
            try
            {
                httpEntity.writeTo(baos);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    @Override protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(response));
        }
    }

    @Override public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();
        if (null == headers || headers.equals(Collections.emptyMap()))
        {
            headers = new HashMap<String, String>();
        }
        return headers;
    }

    @Override public String getBodyContentType()
    {
        return httpEntity.getContentType().getValue();
    }

    @Override protected void deliverResponse(String response)
    {
        mListener.onResponse(response);
    }
}