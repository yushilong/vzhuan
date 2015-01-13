package volley.ex;

import volley.*;
import java.io.UnsupportedEncodingException;

/**
 * Created by yushilong on 15-1-12.
 */
abstract public class HttpParamsRequest<T> extends Request<T> {
    /** Charset for request. */
    protected static final String PROTOCOL_CHARSET = "utf-8";
    /** Content type for request. */
//    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);
    private Response.Listener<T> mListener;
    private String mRequestBody;

//    public HttpParamsRequest(String url, String requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener)
//    {
//        this(Method.DEPRECATED_GET_OR_POST, url, requestBody, listener, errorListener);
//    }

    public HttpParamsRequest(int method, String url, String requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener)
    {
        super(method, url, errorListener);
        mListener = listener;
        mRequestBody = requestBody;
    }

    public HttpParamsRequest(int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener)
    {
        super(method, url, errorListener);
        mListener = listener;
    }

    @Override
    protected void deliverResponse(T response)
    {
        mListener.onResponse(response);
    }

    @Override
    abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

    /**
     * @deprecated Use {@link #getBodyContentType()}.
     */
    @Override
    public String getPostBodyContentType()
    {
        return getBodyContentType();
    }

    /**
     * @deprecated Use {@link #getBody()}.
     */
    @Override
    public byte[] getPostBody()
    {
        return getBody();
    }

//    @Override
//    public String getBodyContentType()
//    {
//        return PROTOCOL_CONTENT_TYPE;
//    }

    @Override
    public byte[] getBody()
    {
        try
        {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        }
        catch (UnsupportedEncodingException uee)
        {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }
}
