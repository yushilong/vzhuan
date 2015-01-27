package com.vzhuan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.demo.ShareContentCustomizeDemo;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import com.vzhuan.viewpager.ViewPager;

/**
 * Created by lscm on 2015/1/5.
 */
public class ShareFragment extends BaseFragment {
    ViewPager viewPager;
    TextView tv_id;
    WebView tv_content;
    GestureDetector mGestureDetector;

    @Override
    public int doGetContentViewId() {
        return R.layout.share;
    }

    @Override
    public void doInitSubViews(View containerView) {
        super.doInitSubViews(containerView);
        containerView.findViewById(R.id.bt_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare(false, null, false);
            }
        });
        tv_id = (TextView) containerView.findViewById(R.id.tv_id);
        tv_content = (WebView) containerView.findViewById(R.id.tv_content);
        mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getY() - e2.getY() <= 120 && (tv_content.getScrollY() == 0)) {//向下
                    viewPager.setCurrentItem(1, true);
                }
                return true;
            }
        });
        tv_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
    }

    @Override
    public void doInitDataes() {
        super.doInitDataes();
        if (UserManager.getInstance().getUser() == null)
            return;
        tv_id.setText("邀请码:" + UserManager.getInstance().getUser().id);
        tv_content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        tv_content.loadUrl(Constants.HOST + ShareUtil.getString(getActivity(), ShareUtil.ShareKey.SHARE_REFERRER, "") + "?did=" + Constants.getDid());
        //        tv_content.loadUrl("http://www.baidu.com");
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    /**
     * ShareSDK集成方法有两种</br>
     * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
     * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
     * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
     * 或者看网络集成文档 http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
     * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
     * <p/>
     * <p/>
     * 平台配置信息有三种方式：
     * 1、在我们后台配置各个微博平台的key
     * 2、在代码中配置各个微博平台的key，http://mob.com/androidDoc/cn/sharesdk/framework/ShareSDK.html
     * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
     */
    private void showShare(boolean silent, String platform, boolean captureView) {
        Context context = getActivity();
        final OnekeyShare oks = new OnekeyShare();
        oks.setNotification(R.drawable.ic_launcher, context.getString(R.string.app_name));
        //oks.setAddress("12345678901");
        oks.setTitle(context.getString(R.string.evenote_title));
        oks.setTitleUrl(Constants.HOST);
        String customText = ShareUtil.getString(getActivity(), ShareUtil.ShareKey.SHARE_CONTEXT, "v赚");
        if (customText != null) {
            oks.setText(customText);
        } else {
            oks.setText(context.getString(R.string.share_content));
        }
        //        oks.setImagePath(CustomShareFieldsPage.getString("imagePath", MainActivity.TEST_IMAGE));
        //        oks.setImageUrl(CustomShareFieldsPage.getString("imageUrl", MainActivity.TEST_IMAGE_URL));
        //        //	oks.setImageArray(new String[]{MainActivity.TEST_IMAGE, MainActivity.TEST_IMAGE_URL});
        //        oks.setUrl(CustomShareFieldsPage.getString("url", "http://www.mob.com"));
        //        oks.setFilePath(CustomShareFieldsPage.getString("filePath", MainActivity.TEST_IMAGE));
        //        oks.setComment(CustomShareFieldsPage.getString("comment", context.getString(R.string.share)));
        //        oks.setSite(CustomShareFieldsPage.getString("site", context.getString(R.string.app_name)));
        //        oks.setSiteUrl(CustomShareFieldsPage.getString("siteUrl", "http://mob.com"));
        //        oks.setVenueName(CustomShareFieldsPage.getString("venueName", "ShareSDK"));
        //        oks.setVenueDescription(CustomShareFieldsPage.getString("venueDescription", "This is a beautiful place!"));
        //        oks.setLatitude(23.056081f);
        //        oks.setLongitude(113.385708f);
        //        oks.setSilent(silent);
        //        oks.setShareFromQQAuthSupport(shareFromQQLogin);
        String theme = null;
        if (OnekeyShareTheme.SKYBLUE.toString().toLowerCase().equals(theme)) {
            oks.setTheme(OnekeyShareTheme.SKYBLUE);
        } else {
            oks.setTheme(OnekeyShareTheme.CLASSIC);
        }
        if (platform != null) {
            oks.setPlatform(platform);
        }
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        //        if (!CustomShareFieldsPage.getBoolean("enableSSO", true))
        oks.disableSSOWhenAuthorize();
        // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
        //oks.setCallback(new OneKeyShareCallback());
        // 去自定义不同平台的字段内容
        oks.setShareContentCustomizeCallback((ShareContentCustomizeCallback) new ShareContentCustomizeDemo());
        // 去除注释，演示在九宫格设置自定义的图标
        Bitmap enableLogo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        Bitmap disableLogo = BitmapFactory.decodeResource(getResources(), R.drawable.sharesdk_unchecked);
        String label = getResources().getString(R.string.app_name);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                String text = "Customer Logo -- ShareSDK " + ShareSDK.getSDKVersionName();
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        };
        oks.setCustomerLogo(enableLogo, disableLogo, label, listener);
        // 去除注释，则快捷分享九宫格中将隐藏新浪微博和腾讯微博
        //		oks.addHiddenPlatform(SinaWeibo.NAME);
        //		oks.addHiddenPlatform(TencentWeibo.NAME);
        // 为EditPage设置一个背景的View
        //        oks.setEditPageBackground(getPage());
        //设置kakaoTalk分享链接时，点击分享信息时，如果应用不存在，跳转到应用的下载地址
        oks.setInstallUrl(ShareUtil.getString(getActivity(), ShareUtil.ShareKey.SHARE_DOWNLOAD, Constants.HOST));
        //设置kakaoTalk分享链接时，点击分享信息时，如果应用存在，打开相应的app
        oks.setExecuteUrl("kakaoTalkTest://starActivity");
        oks.show(context);
    }
}
