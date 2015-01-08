
package com.vzhuan;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import com.squareup.picasso.Transformation;
import picasso.CircleTransfrom;
import picasso.RoundedCornerTransfrom;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class BitmapUtils
{
    // 图片质量压缩
    public static Bitmap compressImage(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100)
        { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        return bitmap;
    }

    // 图片按比例大小压缩
    public static Bitmap compBitmap(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024)
        {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h)
        {// 如果宽度大的话根据宽度固定大小缩放
            be = newOpts.outWidth / h;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.outWidth = w;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    // 图片按比例大小压缩
    public static Bitmap compBitmap(Bitmap image , int size)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024)
        {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int outHeight = (size * h) / w; // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h)
        {// 如果宽度大的话根据宽度固定大小缩放
            be = newOpts.outWidth / h;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, size, outHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

    public static Bitmap getResizedBitmap2(Bitmap bitmap , int newWidth)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int quality = 100;
        while (baos.toByteArray().length / 1024 > 30)
        { // 循环判断如果压缩后图片是否大于30kb,大于继续压缩
            quality -= 10;// 每次都减少10
            if (quality <= 0)
            {
                break;
            }
            baos.reset();// 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 这里压缩为quality%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        // bitmap = BitmapFactory.decodeStream(inputStream);
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        int w = options.outWidth;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        float be = 1.0f;// be=1表示不缩放
        if (w > newWidth)
        {// 如果宽度大的话根据宽度固定大小缩放
            be = (float) (w / newWidth);
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) be;// 设置缩放比例
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length, options);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= newWidth)
        {
            return bitmap;
        }
        else
        {
            float scaleWidth = ((float) newWidth) / width;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            return bitmap;
        }
    }

    //图片缩放
    public static Bitmap scaleDownBitmap(Bitmap photo , int newHeight)
    {
        final float densityMultiplier = B5MDisplayHelper.getDensity();
        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));
        photo = Bitmap.createScaledBitmap(photo, w, h, true);
        return photo;
    }

    public static Bitmap getResizedBitmap(Bitmap bm , int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap bitmap = compBitmap(bm);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }

    public static Bitmap getCompressBmp(Bitmap bmpOrg)
    {
        if (bmpOrg != null)
        {
            int src_width = bmpOrg.getWidth();
            int src_height = bmpOrg.getHeight();
            float height = (float) bmpOrg.getHeight();
            float bmpHeight = (float) src_width / height;
            Matrix matrix = new Matrix();
            matrix.postScale(1, bmpHeight);
            Bitmap resizeBmp = Bitmap.createBitmap(bmpOrg, 0, 0, src_width, src_height, matrix, true);
            return resizeBmp;
        }
        return bmpOrg;
    }

    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        int w = drawable.getIntrinsicWidth(), h = drawable.getIntrinsicHeight();
        if (w <= 0 || h <= 0)
        {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap convertDrawable2Bitmap(Drawable drawable)
    {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    public static Bitmap getBitmapFromResources(Context act , int resId)
    {
        Resources res = act.getResources();
        return BitmapFactory.decodeResource(res, resId);
    }

    public InputStream Drawable2InputStream(Context act , int resId)
    {
        Drawable d = act.getResources().getDrawable(resId);
        Bitmap bitmap = drawableToBitmap(d);
        return Bitmap2InputStream(bitmap, 50);
    }

    public InputStream Bitmap2InputStream(Bitmap bm , int quality)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    public Bitmap drawable2Bitmap(Drawable drawable)
    {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /** 
    * 将多个Bitmap合并成一个图片。 
    *  
    * @param columns 将多个图合成多少列
    * @param bitmaps 要合成的图片
    * @return 
    */
    public static Bitmap combineBitmaps(int columns , Bitmap... bitmaps)
    {
        if (columns <= 0 || bitmaps == null || bitmaps.length == 0)
        {
            throw new IllegalArgumentException("Wrong parameters: columns must > 0 and bitmaps.length must > 0.");
        }
        int maxWidthPerImage = 0;
        int maxHeightPerImage = 0;
        for (Bitmap b : bitmaps)
        {
            maxWidthPerImage = maxWidthPerImage > b.getWidth() ? maxWidthPerImage : b.getWidth();
            maxHeightPerImage = maxHeightPerImage > b.getHeight() ? maxHeightPerImage : b.getHeight();
        }
        int rows = 0;
        if (columns >= bitmaps.length)
        {
            rows = 1;
            columns = bitmaps.length;
        }
        else
        {
            rows = bitmaps.length % columns == 0 ? bitmaps.length / columns : bitmaps.length / columns + 1;
        }
        Bitmap newBitmap = Bitmap.createBitmap(columns * maxWidthPerImage, rows * maxHeightPerImage, Config.RGB_565);
        for (int x = 0; x < rows; x++)
        {
            for (int y = 0; y < columns; y++)
            {
                int index = x * columns + y;
                if (index >= bitmaps.length)
                    break;
                newBitmap = mixtureBitmap(newBitmap, bitmaps[index], new PointF(y * maxWidthPerImage, x * maxHeightPerImage));
            }
        }
        return newBitmap;
    }

    public static Bitmap mixtureBitmap(Bitmap first , Bitmap second , PointF fromPoint)
    {
        if (first == null || second == null || fromPoint == null)
        {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(), first.getHeight(), Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return newBitmap;
    }

    public static Transformation get(int paramInt)
    {
        Transformation localTransformation = null;
        if (paramInt >= 0)
        {
            localTransformation = get(Effection.values()[paramInt]);
        }
        return localTransformation;
    }

    public static Transformation get(Effection paramEffection)
    {
        Transformation localObject = null;
        if (paramEffection == Effection.CIRCLE)
            localObject = new CircleTransfrom();
        else if (paramEffection == Effection.ROUND_CORNER)
            localObject = new RoundedCornerTransfrom(10);
        return localObject;
    }

    public static Transformation get(String paramString)
    {
        return get(Effection.valueOf(paramString));
    }

    public enum Effection {
        CIRCLE("CIRCLE"), ROUND_CORNER("ROUND_CORNER");
        private final String effect;

        private Effection(String paramString)
        {
            this.effect = paramString;
        }

        public boolean equals(Effection paramEffection)
        {
            return this.effect.equals(paramEffection.effect);
        }
    }

    public static byte[] bmpToByteArray(final Bitmap bmp , final boolean needRecycle)
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle)
        {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try
        {
            output.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
