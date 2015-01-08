package com.vzhuan;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lscm on 2015/1/5.
 */
public class ShareUtil
{
    private static final String SHARE_NAME = "vzhuan";

    private static SharedPreferences getDefault(Context context)
    {
        return context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean getBoolean(Context context, String shareKey, boolean def)
    {
        return getDefault(context).getBoolean(shareKey, def);
    }

    public static void setBoolean(Context context, String shareKey, boolean value)
    {
        getDefault(context).edit().putBoolean(shareKey, value).commit();
    }

    public static String getString(Context context, String shareKey, String def)
    {
        return getDefault(context).getString(shareKey, def);
    }

    public static void setString(Context context, String shareKey, String value)
    {
        getDefault(context).edit().putString(shareKey, value).commit();
    }

    public final static class ShareKey
    {
        public static final String KEY_ISFIRST_OPEN = "KEY_ISFIRST_OPEN";
        public static final String KEY_USER_ID = "KEY_USER_ID";
        public static final String KEY_CHANNEL_ID = "KEY_CHANNEL_ID";
    }
}
