
package com.vzhuan.api;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import com.vzhuan.Constants;
import volley.Cache;
import volley.Network;
import volley.Request;
import volley.RequestQueue;
import volley.toolbox.*;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

public class B5MVolley
{
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    public static final String TAG = "VolleyPatterns";

    private B5MVolley()
    {
        // no instances
    }

    public static void init(Context context)
    {
        mRequestQueue = newRequestQueue(context);
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int cacheSize = manager.getMemoryClass() / 8;
        mImageLoader = new ImageLoader(mRequestQueue, new B5MVolley().new BitmapLruCache(cacheSize));
    }

    public static RequestQueue getRequestQueue()
    {
        if (mRequestQueue != null)
        {
            return mRequestQueue;
        }
        else
        {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static ImageLoader getImageLoader()
    {
        if (mImageLoader != null)
        {
            return mImageLoader;
        }
        else
        {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    public static <T> void addToRequestQueue(Request<T> req , String tag)
    {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public static <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public static void cancelPendingRequests(Object tag)
    {
        if (mRequestQueue != null)
        {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static void clear(Context ctx)
    {
        mRequestQueue.cancelAll(ctx);
        mImageLoader = null;
        mRequestQueue = null;
    }

    public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache
    {
        private static final String TAG = "BitmapLruCache";
        private BitmapSoftRefCache softRefCache;

        public BitmapLruCache(int maxSize)
        {
            super(maxSize);
            softRefCache = new BitmapSoftRefCache();
        }

        @Override
        protected int sizeOf(String key , Bitmap value)
        {
            return value.getRowBytes() * value.getHeight() / 1024;
        }

        @Override
        protected void entryRemoved(boolean evicted , String key , Bitmap oldValue , Bitmap newValue)
        {
            if (evicted)
            {
                //                LogUtil.i(TAG, "空间已满，缓存图片被挤出:" + key);
                // 将被挤出的bitmap对象，添加至软引用BitmapSoftRefCache
                softRefCache.putBitmap(key, oldValue);
            }
        }

        /**
         * 得到缓存对象
         */
        @Override
        public Bitmap getBitmap(String url)
        {
            Bitmap bitmap = get(url);
            // 如果bitmap为null，尝试从软引用缓存中查找
            if (bitmap == null)
            {
                bitmap = softRefCache.getBitmap(url);
            }
            else
            {
            }
            return bitmap;
        }

        /**
         * 添加缓存对象
         */
        @Override
        public void putBitmap(String url , Bitmap bitmap)
        {
            put(url, bitmap);
        }
    }

    /**
    * 软引用缓存管理类
    */
    public class BitmapSoftRefCache implements ImageLoader.ImageCache
    {
        private static final String TAG = "BitmapSoftRefCache";
        private LinkedHashMap<String, SoftReference<Bitmap>> map;

        public BitmapSoftRefCache()
        {
            map = new LinkedHashMap<String, SoftReference<Bitmap>>();
        }

        /**
         * 从软引用集合中得到Bitmap对象
         */
        @Override
        public Bitmap getBitmap(String url)
        {
            Bitmap bitmap = null;
            SoftReference<Bitmap> softRef = map.get(url);
            if (softRef != null)
            {
                bitmap = softRef.get();
                if (bitmap == null)
                {
                    map.remove(url); //从map中移除
                    if (Constants.DEBUG)
                    {
                    }
                }
                else
                {
                    if (Constants.DEBUG)
                    {
                    }
                }
            }
            return bitmap;
        }

        /**
         * 从软引用集合中添加bitmap对象
         */
        @Override
        public void putBitmap(String url , Bitmap bitmap)
        {
            SoftReference<Bitmap> softRef = new SoftReference<Bitmap>(bitmap);
            map.put(url, softRef);
        }
    }
    private static final String DEFAULT_CACHE_DIR = "volleycache";

    private static RequestQueue newRequestQueue(Context context)
    {
        B5MFileHelper.checkDir(DEFAULT_CACHE_DIR);
        File rootCache = new File(B5MFileHelper.getAppFilePath(DEFAULT_CACHE_DIR));
        HttpStack stack = new HurlStack();
        Network network = new BasicNetwork(stack);
        DiskBasedCache diskBasedCache = new DiskBasedCache(rootCache, getMaxCacheSizeInBytes());
        RequestQueue queue = new RequestQueue(diskBasedCache, network);
        queue.start();
        return queue;
    }

    public static Cache getRequestCache()
    {
        if (mRequestQueue != null)
        {
            return mRequestQueue.getCache();
        }
        else
        {
            throw new IllegalStateException("DiskBasedCache not initialized");
        }
    }

    private static int getMaxCacheSizeInBytes()
    {
        if (B5MFileHelper.isExistSDCard())
        {
            return 20 * 1024 * 1024;
        }
        else
        {
            return 8 * 1024 * 1024;
        }
    }
}
