package picasso;

import android.graphics.*;
import com.squareup.picasso.Transformation;

public class CircleTransfrom implements Transformation
{
    public String key()
    {
        return null;
    }

    public Bitmap transform(Bitmap paramBitmap)
    {
        Bitmap localBitmap1;
        if (paramBitmap == null)
            return null;
        else
        {
            int i = paramBitmap.getWidth();
            int j = paramBitmap.getHeight();
            if ((i <= 0) || (j <= 0))
            {
                return null;
            }
            else
            {
                if (i < j)
                {
                    i = j;
                }
                int k = i / 2;
                Bitmap localBitmap2 = Bitmap.createBitmap(k * 2, k * 2, Bitmap.Config.ARGB_8888);
                Canvas localCanvas1 = new Canvas(localBitmap2);
                Paint localPaint = new Paint(1);
                localPaint.setColor(-65536);
                localCanvas1.drawCircle(k, k, k, localPaint);
                localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                localBitmap1 = Bitmap.createBitmap(k * 2, k * 2, Bitmap.Config.ARGB_8888);
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
