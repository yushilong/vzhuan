package com.vzhuan;

import android.app.Application;
import android.content.Context;
import cn.jpush.android.api.JPushInterface;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.testin.cloud.tesaclo;

/**
 * Created by lscm on 2015/1/6.
 */
public class MainApplication extends Application {
    private static Application _instance;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
        _instance = this;
        initJPush();
        tesaclo.onCreate(this);
    }

    private void initJPush() {
        JPushInterface.setDebugMode(Constants.DEBUG);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }

    public static Application getInstance() {
        return _instance;
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config;
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        builder.memoryCacheExtraOptions(360, 500);
        //        builder.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024));
        builder.threadPriority(Thread.NORM_PRIORITY - 2);
        builder.denyCacheImageMultipleSizesInMemory();
        builder.discCacheFileNameGenerator(new Md5FileNameGenerator());
        builder.tasksProcessingOrder(QueueProcessingType.LIFO);
        builder.writeDebugLogs(); // Remove for release app
        config = builder.build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
