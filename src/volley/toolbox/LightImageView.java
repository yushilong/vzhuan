package volley.toolbox;

import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.vzhuan.BitmapUtils;

public class LightImageView extends NetworkImageView
{
    @Override protected void drawableStateChanged()
    {
        super.drawableStateChanged();
        invalidate();
    }

    public LightImageView(Context context)
    {
        super(context);
    }

    public LightImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LightImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        setOnTouchListener(onTouchListener);
    }

    public void changeLight(ImageView imageView, int brightness)
    {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[] { 1 , 0 , 0 , 0 , brightness , 0 , 1 , 0 , 0 , brightness ,// 改变亮度
            0 , 0 , 1 , 0 , brightness , 0 , 0 , 0 , 1 , 0 });
        colorFilter = new ColorMatrixColorFilter(cMatrix);
        invalidate();
    }

    public View.OnTouchListener onTouchListener = new View.OnTouchListener()
    {
        @Override public boolean onTouch(View view, MotionEvent event)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_CANCEL:
                    changeLight((ImageView) view, 0);
                    break;
                case MotionEvent.ACTION_UP:
                    changeLight((ImageView) view, 0);
                    break;
                case MotionEvent.ACTION_DOWN:
                    changeLight((ImageView) view, -50);
                    break;
                default:
                    break;
            }
            return false;
        }
    };
    private Bitmap image;

    @Override protected void onDraw(Canvas canvas)
    {
        Drawable drawable = getDrawable();
        if (drawable != null)
        {
            image = BitmapUtils.drawableToBitmap(drawable);
        }
        if (image == null || image.isRecycled())
        {
            return;
        }
        //头像的宽高  
        Paint paint = new Paint();
        Bitmap bitmap = createFramedPhoto();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    ColorFilter colorFilter = null;

    private Bitmap createFramedPhoto()
    {
        Rect dst = new Rect(0, 0, getWidth(), getHeight());
        Bitmap output = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawRect(new RectF(0, 0, getWidth(), getHeight()), paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        //绘制图片的时候，给画笔加一个灰色的蒙层  
        if (colorFilter != null)
        {
            paint.setColorFilter(colorFilter);
        }
        canvas.drawBitmap(image, null, dst, paint);
        return output;
    }

    //图片剪切  
    public static Bitmap cutBitmap(Bitmap mBitmap, Rect r, Bitmap.Config config)
    {
        int width = r.width();
        int height = r.height();
        Bitmap croppedImage = Bitmap.createBitmap(width, height, config);
        Canvas cvs = new Canvas(croppedImage);
        Rect dr = new Rect(0, 0, width, height);
        cvs.drawBitmap(mBitmap, r, dr, null);
        return croppedImage;
    }
}
