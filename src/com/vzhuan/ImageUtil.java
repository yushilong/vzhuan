package com.vzhuan;

import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lscm on 2015/1/6.
 */
public class ImageUtil {
    public static int DEFAULT_IMG_SIZE = 500;
    public static int DEFAULT_IMG_ROUND = 10;
    public static int DEFAULT_LOAD_ICON_PEOPLE = R.drawable.avatar;
    public static int DEFAULT_LOAD_ICON_NOTPEOPLE = R.drawable.avatar;

    public static DisplayImageOptions.Builder getBaseDefaultBuilder() {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading(true);
        return builder;
    }

    private static int getDefaultPeopleImgResId() {
        return DEFAULT_LOAD_ICON_PEOPLE;
    }

    private static int getDefaultNotPeopleImgResId() {
        return DEFAULT_LOAD_ICON_NOTPEOPLE;
    }

    public static BitmapDisplayer getDefaultRoundBitmapDisplayer() {
        BitmapDisplayer bitmapDisplayer = new RoundedBitmapDisplayer(DEFAULT_IMG_ROUND);
        return bitmapDisplayer;
    }

    public static BitmapDisplayer getDefaultRoundBitmapDisplayer(int round) {
        BitmapDisplayer bitmapDisplayer = new RoundedBitmapDisplayer(round);
        return bitmapDisplayer;
    }

    public static DisplayImageOptions getDefaultNotRoundImgOptions(int resId) {
        DisplayImageOptions.Builder builder = getBaseDefaultBuilder();
        builder.showImageForEmptyUri(resId);
        builder.showImageOnFail(resId);
        builder.showImageOnLoading(resId);
        builder.cacheInMemory(true);
        builder.cacheOnDisc(true);
        DisplayImageOptions displayImageOptions = builder.build();
        return displayImageOptions;
    }

    public static DisplayImageOptions getDefaultRoundImgOptions(int resId) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageForEmptyUri(resId);
        builder.showImageOnFail(resId);
        builder.showImageOnLoading(resId);
        builder.cacheInMemory(true);
        builder.cacheOnDisc(true);
        builder.considerExifParams(true);
        builder.displayer(getDefaultRoundBitmapDisplayer());
        DisplayImageOptions displayImageOptions = builder.build();
        return displayImageOptions;
    }

    public static DisplayImageOptions getDefaultRoundImgOptions(int resId, int round) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageForEmptyUri(resId);
        builder.showImageOnFail(resId);
        builder.showImageOnLoading(resId);
        builder.cacheInMemory(true);
        builder.cacheOnDisc(true);
        builder.considerExifParams(true);
        builder.displayer(getDefaultRoundBitmapDisplayer(round));
        DisplayImageOptions displayImageOptions = builder.build();
        return displayImageOptions;
    }

    public static String getImagePath(String imgUrl, int size) {
        if (imgUrl != null) {
            return imgUrl.startsWith("http://img.b5m.com/") ? imgUrl + "/" + (size == 0 ? DEFAULT_IMG_SIZE : size) : imgUrl;
        }
        return "";
    }

    /**
     * 使用默认的图片尺寸
     *
     * @param imgUrl
     * @param imageView
     * @param displayImageOptions
     */
    public static void displayImage(String imgUrl, ImageView imageView, DisplayImageOptions displayImageOptions) {
        ImageLoader.getInstance().displayImage(getImagePath(imgUrl, DEFAULT_IMG_SIZE), imageView, displayImageOptions, new AnimateFirstDisplayListener());
    }

    /**
     * 自定义图片的尺寸</br>
     * 0是默认尺寸
     *
     * @param imgUrl
     * @param imageView
     * @param displayImageOptions
     */
    public static void displayImage(String imgUrl, int cdnImgSize, ImageView imageView, DisplayImageOptions displayImageOptions) {
        ImageLoader.getInstance().displayImage(getImagePath(imgUrl, cdnImgSize), imageView, displayImageOptions, new AnimateFirstDisplayListener());
    }

    /**
     * 使用自定义的ImageLoadingListener
     *
     * @param imgUrl
     * @param imageView
     * @param displayImageOptions
     * @param animateFirstDisplayListener
     */
    public static void displayImage(String imgUrl, ImageView imageView, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener) {
        ImageLoader.getInstance().displayImage(getImagePath(imgUrl, DEFAULT_IMG_SIZE), imageView, displayImageOptions, imageLoadingListener);
    }

    public static void displayImage(String imgUrl, ImageView imageView, ImageLoadingListener imageLoadingListener, boolean isRound) {
        if (isRound) {
            if (imageLoadingListener == null) {
                displayImage(imgUrl, imageView, getDefaultRoundImgOptions(getDefaultPeopleImgResId()));
            } else {
                displayImage(imgUrl, imageView, getDefaultRoundImgOptions(getDefaultPeopleImgResId()), imageLoadingListener);
            }
        } else {
            if (imageLoadingListener == null) {
                displayImage(imgUrl, imageView, getDefaultNotRoundImgOptions(getDefaultPeopleImgResId()));
            } else {
                displayImage(imgUrl, imageView, getDefaultNotRoundImgOptions(getDefaultPeopleImgResId()), imageLoadingListener);
            }
        }
    }

    /**
     * 显示人物的圆角图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void displayDefaultPeopleRoundImage(String imgUrl, ImageView imageView) {
        displayImage(imgUrl, imageView, getDefaultRoundImgOptions(getDefaultPeopleImgResId()));
    }

    /**
     * 显示人物的非圆角图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void displayDefaultPeopleNotRoundImage(String imgUrl, ImageView imageView) {
        displayImage(imgUrl, imageView, getDefaultNotRoundImgOptions(getDefaultPeopleImgResId()));
    }

    /**
     * 显示非人物的圆角图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void displayDefaultNotPeopleRoundImage(String imgUrl, ImageView imageView) {
        displayImage(imgUrl, imageView, getDefaultRoundImgOptions(getDefaultNotPeopleImgResId()));
    }

    /**
     * 显示非人物的非圆角图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void displayDefaultNotPeopleNotRoundImage(String imgUrl, ImageView imageView) {
        displayImage(imgUrl, imageView, getDefaultNotRoundImgOptions(getDefaultNotPeopleImgResId()));
    }

    /**
     * 显示非人物的非圆角图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void displayDiyNotPeopleNotRoundImage(String imgUrl, ImageView imageView, int ImgResId) {
        displayImage(imgUrl, imageView, getDefaultNotRoundImgOptions(ImgResId));
    }

    /**
     * 显示非人物的非圆角图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void displayDiyNotPeopleNotRoundImage(String imgUrl, ImageView imageView, int ImgResId, int size) {
        ImageLoader.getInstance().displayImage(getImagePath(imgUrl, size), imageView, getDefaultNotRoundImgOptions(ImgResId), new AnimateFirstDisplayListener());
    }

    /**
     * 自定义图片的尺寸</br>
     * 0是默认尺寸
     *
     * @param imgUrl
     * @param imageView
     */
    public static void displayDiyNotPeopleNotRoundSizeImage(String imgUrl, int cdnImgSize, ImageView imageView) {
        displayImage(imgUrl, cdnImgSize, imageView, getDefaultNotRoundImgOptions(getDefaultNotPeopleImgResId()));
    }

    /**
     * 显示非人物的圆角带圈圈样式图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void displayCircleTransfromRoundImage(String imgUrl, final ImageView imageView, int round, int imgResId) {
        ImageLoader.getInstance().displayImage(getImagePath(imgUrl, DEFAULT_IMG_SIZE), imageView, getDefaultRoundImgOptions(imgResId, round), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                int i = loadedImage.getWidth();
                int j = loadedImage.getHeight();
                if (i < j) {
                    i = j;
                }
                int k = i / 2;
                Bitmap localBitmap = Bitmap.createBitmap(k * 2, k * 2, Bitmap.Config.ARGB_8888);
                Canvas localCanvas1 = new Canvas(localBitmap);
                Paint localPaint = new Paint(1);
                localPaint.setColor(-65536);
                localCanvas1.drawCircle(k, k, k, localPaint);
                localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                Bitmap canvasBitmap = Bitmap.createBitmap(k * 2, k * 2, Bitmap.Config.ARGB_8888);
                Canvas localCanvas2 = new Canvas(localBitmap);
                localCanvas2.drawBitmap(canvasBitmap, 0.0F, 0.0F, null);
                localCanvas2.drawBitmap(localBitmap, 0.0F, 0.0F, localPaint);
                imageView.setImageBitmap(canvasBitmap);
                localBitmap.recycle();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                // TODO Auto-generated method stub
            }
        });
    }

    /**
     * 显示自定义角度的圆角图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void displayRoundImage(String imgUrl, ImageView imageView, int round, int imgResId) {
        displayImage(imgUrl, imageView, getDefaultRoundImgOptions(imgResId, round));
    }

    public static Bitmap getBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void newSelector(String imgUrl, final ImageView imageView) {
        ImageLoader.getInstance().displayImage(imgUrl, imageView, getDefaultRoundImgOptions(R.drawable.avatar), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                int width = loadedImage.getWidth();
                int height = loadedImage.getHeight();
                BitmapDrawable disabled = new BitmapDrawable(loadedImage);
                Bitmap pressBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                StateListDrawable bg = new StateListDrawable();
                Canvas canvas = new Canvas(pressBitmap);
                Paint paint = new Paint();
                paint.setAlpha(126);
                canvas.drawBitmap(loadedImage, 0, 0, paint);
                BitmapDrawable normal = new BitmapDrawable(MainApplication.getInstance().getResources(), pressBitmap);
                bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, disabled);
                bg.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_enabled}, disabled);
                bg.addState(new int[]{android.R.attr.state_enabled}, normal);
                bg.addState(new int[]{}, normal);
                imageView.setImageDrawable(bg);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                // TODO Auto-generated method stub
            }
        });
    }

    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
