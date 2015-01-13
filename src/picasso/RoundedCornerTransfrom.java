package picasso;

import android.graphics.*;
import com.squareup.picasso.Transformation;

public class RoundedCornerTransfrom implements Transformation
{
    private int mCornerSize;

    public RoundedCornerTransfrom(int paramInt)
    {
        this.mCornerSize = paramInt;
    }

    public String key()
    {
        return null;
    }

    public void setCornerSize(int paramInt)
    {
        this.mCornerSize = paramInt;
    }

    public Bitmap transform(Bitmap paramBitmap)
    {
        Bitmap localBitmap1;
        if (paramBitmap == null)
            localBitmap1 = null;
        else
        {
            int i = paramBitmap.getWidth();
            int j = paramBitmap.getHeight();
            if ((i <= 0) || (j <= 0))
            {
                localBitmap1 = null;
            }
            else
            {
                Bitmap localBitmap2 = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
                Canvas localCanvas1 = new Canvas(localBitmap2);
                Paint localPaint = new Paint(1);
                localPaint.setColor(-65536);
                localCanvas1.drawRoundRect(new RectF(0.0F, 0.0F, i, j), this.mCornerSize, this.mCornerSize, localPaint);
                localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                localBitmap1 = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
                Canvas localCanvas2 = new Canvas(localBitmap1);
                localCanvas2.drawBitmap(paramBitmap, 0.0F, 0.0F, null);
                localCanvas2.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint);
                paramBitmap.recycle();
                localBitmap2.recycle();
            }
        }
        return localBitmap1;
    }
}
