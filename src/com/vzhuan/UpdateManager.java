package com.vzhuan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.NotificationManager;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

public class UpdateManager {
    private Context ctx;
    private Boolean canceled;
    public String downUrl;
    public static final String UPDATE_SAVENAME = "vzhuan.apk";
    private static final int UPDATE_CHECKCOMPLETED = 1;
    private static final int UPDATE_DOWNLOADING = 2;
    private static final int UPDATE_DOWNLOAD_ERROR = 3;
    private static final int UPDATE_DOWNLOAD_COMPLETED = 4;
    private static final int UPDATE_DOWNLOAD_CANCELED = 5;
    private static final String savefolder = "/sdcard/vzhuan/";
    private int NOTIFICATION_ID = 110;
    private NotificationManager notificationManager;
    private boolean isShowProgress;
    private NotificationCompat.Builder builder;
    private int lastProgress;

    public UpdateManager(Context context, String downUrl, boolean isShowProgress) {
        ctx = context;
        this.downUrl = downUrl;
        canceled = false;
        this.isShowProgress = isShowProgress;
    }

    public void installAPK() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(savefolder, UPDATE_SAVENAME)),
                "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void downloadPackage() {
        if (isShowProgress)
            initNotification();
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(downUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    File fd = new File(savefolder);
                    if (!fd.exists())
                        fd.mkdirs();
                    File f = new File(savefolder, UPDATE_SAVENAME);
                    if (f.exists()) {
                        f.delete();
                    }
                    f.createNewFile();
                    FileOutputStream fos = new FileOutputStream(f);
                    int count = 0;
                    byte buf[] = new byte[512];
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        int progress = (int) (((float) count / length) * 100);
                        if ((progress - lastProgress) > 4) {
                            lastProgress = progress;
                            updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOADING, progress));
                        }
                        if (numread <= 0) {
                            updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_COMPLETED);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!canceled);
                    if (canceled) {
                        updateHandler.sendEmptyMessage(UPDATE_DOWNLOAD_CANCELED);
                    }
                    fos.close();
                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOAD_ERROR, e.getMessage()));
                } catch (IOException e) {
                    e.printStackTrace();
                    updateHandler.sendMessage(updateHandler.obtainMessage(UPDATE_DOWNLOAD_ERROR, e.getMessage()));
                }
            }
        }.start();
    }

    private void initNotification() {
        notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(ctx);
        builder.setTicker(ctx.getString(R.string.download_start));
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentTitle(ctx.getString(R.string.app_name));
        builder.setContentText("进度：0%");
        builder.setAutoCancel(false);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    Handler updateHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("eeee", "msg.what=" + msg.what + "");
            switch (msg.what) {
                case UPDATE_CHECKCOMPLETED:
                    break;
                case UPDATE_DOWNLOADING:
                    if (isShowProgress) {
                        Log.e("eeee", "progress=" + msg.obj + "");
                        builder.setContentText("进度：" + msg.obj + "%");
                        notificationManager.notify(NOTIFICATION_ID, builder.build());
                    }
                    break;
                case UPDATE_DOWNLOAD_ERROR:
                    if (isShowProgress) {
                        builder.setAutoCancel(true);
                        builder.setContentText("下载出错！");
                        notificationManager.notify(NOTIFICATION_ID, builder.build());
                    }
                    break;
                case UPDATE_DOWNLOAD_COMPLETED:
                    if (isShowProgress)
                        cancelNotification();
                    installAPK();
                    break;
                case UPDATE_DOWNLOAD_CANCELED:
                default:
                    break;
            }
        }
    };

    public void cancelNotification() {
        if (notificationManager != null)
            notificationManager.cancelAll();
    }
}