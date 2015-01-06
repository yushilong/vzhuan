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

    static class ShareKey
    {
        public static String KEY_ISFIRST_OPEN;
    }
}
