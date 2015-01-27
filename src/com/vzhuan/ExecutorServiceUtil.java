package com.vzhuan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lscm on 2015/1/13.
 */
public class ExecutorServiceUtil {
    private static ExecutorService _instance;
    public static final Object mObject = new Object();

    public static ExecutorService getInstance() {
        synchronized (mObject) {
            if (_instance == null)
                _instance = Executors.newFixedThreadPool(5);
        }
        return _instance;
    }
}
