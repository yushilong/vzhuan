package volley.toolbox;

import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SelectorImageView extends ImageView implements OnClickListener
{
    @Override protected void drawableStateChanged()
    {
        super.drawableStateChanged();
        invalidate();
    }

    public SelectorImageView(Context context)
    {
        super(context);
    }

    public SelectorImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public SelectorImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setOnClickListener(this);
    }

    public void changeLight(int brightness)
    {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[] { 1 , 0 , 0 , 0 , brightness , 0 , 1 , 0 , 0 , brightness ,// 改变亮度
            0 , 0 , 1 , 0 , brightness , 0 , 0 , 0 , 1 , 0 });
        colorFilter = new ColorMatrixColorFilter(cMatrix);
        invalidate();
    }

    @Override public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_CANCEL:
                changeLight(0);
                break;
            case MotionEvent.ACTION_UP:
                changeLight(0);
                break;
            case MotionEvent.ACTION_DOWN:
                changeLight(-50);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private Bitmap image;

    @Override protected void onDraw(Canvas canvas)
    {
        Drawable drawable = getBackground();
        if (drawable != null)
        {
            image = ((BitmapDrawable) drawable).getBitmap();
        }
        if (image == null || image.isRecycled())
        {
            return;
        }
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

    @Override public void onClick(View v)
    {
        // TODO Auto-generated method stub
    }
}
