package volley.toolbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ScanImageView extends NetworkImageView
{
    private Bitmap currentBitmap;
    private ImageChangeListener imageChangeListener;
    private boolean scaleToWidth = false;

    public ScanImageView(Context context)
    {
        super(context);
        init();
    }

    public ScanImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public ScanImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        this.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    public void recycle()
    {
        setImageBitmap(null);
        if ((this.currentBitmap == null) || (this.currentBitmap.isRecycled()))
            return;
        this.currentBitmap.recycle();
        this.currentBitmap = null;
    }

    @Override public void setImageBitmap(Bitmap bm)
    {
        currentBitmap = bm;
        super.setImageBitmap(currentBitmap);
        if (imageChangeListener != null)
            imageChangeListener.changed((currentBitmap == null));
    }

    @Override public void setImageDrawable(Drawable d)
    {
        super.setImageDrawable(d);
        if (imageChangeListener != null)
            imageChangeListener.changed((d == null));
    }

    @Override public void setImageResource(int id)
    {
        super.setImageResource(id);
    }

    public interface ImageChangeListener
    {
        // a callback for when a change has been made to this imageView
        void changed(boolean isEmpty);
    }

    public ImageChangeListener getImageChangeListener()
    {
        return imageChangeListener;
    }

    public void setImageChangeListener(ImageChangeListener imageChangeListener)
    {
        this.imageChangeListener = imageChangeListener;
    }

    private int imageWidth;
    private int imageHeight;

    public void setImageWidth(int w)
    {
        imageWidth = w;
    }

    public void setImageHeight(int h)
    {
        imageHeight = h;
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == View.MeasureSpec.EXACTLY || widthMode == View.MeasureSpec.AT_MOST)
        {
            scaleToWidth = true;
        }
        else if (heightMode == View.MeasureSpec.EXACTLY || heightMode == View.MeasureSpec.AT_MOST)
        {
            scaleToWidth = false;
        }
        else
            throw new IllegalStateException("width or height needs to be set to match_parent or a specific dimension");
        if (imageWidth == 0)
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        else
        {
            if (scaleToWidth)
            {
                int iw = imageWidth;
                int ih = imageHeight;
                int heightC = width * ih / iw;
                if (height > 0)
                    if (heightC > height)
                    {
                        heightC = height;
                        width = heightC * iw / ih;
                    }
                this.setScaleType(ImageView.ScaleType.CENTER_CROP);
                setMeasuredDimension(width, heightC);
            }
            else
            {
                int marg = 0;
                if (getParent() != null)
                {
                    if (getParent().getParent() != null)
                    {
                        marg += ((RelativeLayout) getParent().getParent()).getPaddingTop();
                        marg += ((RelativeLayout) getParent().getParent()).getPaddingBottom();
                    }
                }
                int iw = imageWidth;
                int ih = imageHeight;
                width = height * iw / ih;
                height -= marg;
                setMeasuredDimension(width, height);
            }
        }
    }
}
