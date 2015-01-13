package com.vzhuan;

import com.vzhuan.mode.User;

/**
 * Created by lscm on 2015/1/13.
 */
public class UserManager
{
    private User user;
    private static UserManager _instance;
    private static Object mLock = new Object();

    public static UserManager getInstance()
    {
        synchronized (mLock)
        {
            if (_instance == null)
                _instance = new UserManager();
        }
        return _instance;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
