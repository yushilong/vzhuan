package com.vzhuan;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import cn.jpush.android.api.JPushInterface;
import com.vzhuan.api.HttpListener;
import com.vzhuan.api.MyHttpRequestor;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class SplashActivity extends BaseActivity {
    private MyHttpRequestor alreadySubmitRequest, checkUpdateRequest;

    @Override
    public int doGetContentViewId() {
        return R.layout.splash;
    }

    @Override
    public void doInitDataes() {
        super.doInitDataes();
        //update app
        checkUpdate();
        //
        boolean isfirstOpen = ShareUtil.getBoolean(this, ShareUtil.ShareKey.KEY_ISFIRST_OPEN, true);
        if (isfirstOpen) {
            startActivity(new Intent(this, GuideActivity.class));
            finish();
        }
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(3000);
        findViewById(android.R.id.content).startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                if (!ShareUtil.isExist(SplashActivity.this, ShareUtil.ShareKey.BONUSED)) {
                    alreadySubmitRequest.start();
                } else {
                    if (!ShareUtil.getBoolean(SplashActivity.this, ShareUtil.ShareKey.BONUSED, false)) {
                        startActivity(new Intent(SplashActivity.this, RerferrerInfoActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
        //
        String alreadyUrl = Constants.ALREADY_SUBMIT_REFERRRERINFO + "?invited=" + ShareUtil.getString(SplashActivity.this, ShareUtil.ShareKey.UID, null);
        alreadySubmitRequest = new MyHttpRequestor().init(MyHttpRequestor.GET_METHOD, alreadyUrl, new HttpListener() {
            @Override
            public void onSuccess(String msg) {
                JSONObject resultObject = null;
                try {
                    resultObject = new JSONObject(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                boolean isInvited = resultObject.optBoolean("entity");
                if (!isInvited) {
                    startActivity(new Intent(SplashActivity.this, RerferrerInfoActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }

            @Override
            public void onFailure(int statusCode, String emsg) {
            }
        });
    }

    private void checkUpdate() {
        if (checkUpdateRequest == null) {
            checkUpdateRequest = new MyHttpRequestor().init(MyHttpRequestor.GET_METHOD, Constants.CHECK_UPDATE, new HttpListener() {
                @Override
                public void onSuccess(String msg) {
                    JSONObject resultObject = null;
                    try {
                        resultObject = new JSONObject(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int versionCode = resultObject.optInt("version");
                    int policy = resultObject.optInt("policy");
                    String downUrl = resultObject.optString("downloadUrl");
                    if (isNeedUpdate(versionCode)) {
                        if (policy==1){//need update
                            if (DataUtil.isWifi(SplashActivity.this)){
                                downloadFile(downUrl,false);
                            }else {
                                showUpdateDialog(downUrl);
                            }
                        }else {
                            showUpdateDialog(downUrl);
                        }

                    }
                }

                @Override
                public void onFailure(int statusCode, String emsg) {

                }
            });
        }
        checkUpdateRequest.start();
    }

    private void showUpdateDialog(String downUrl) {
    }

    private void downloadFile(String downUrl,boolean isShowProgress) {
        DownloadManager downloadmanager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downUrl));
        String foler  = "vzhuan";
        File file = Environment.getExternalStoragePublicDirectory(foler);
        if (!file.exists()||!file.isDirectory())
            file.mkdirs();
        request.setDestinationInExternalPublicDir(foler,"vzhuan.apk");
    }

    private boolean isNeedUpdate(int versionCode) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            int localVersionCode = info.versionCode;
            if (localVersionCode < versionCode)
                return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
