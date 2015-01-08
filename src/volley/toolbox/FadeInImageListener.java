
package volley.toolbox;

import android.content.Context;
import android.widget.ImageView;
import com.vzhuan.R;
import volley.VolleyError;

import java.lang.ref.WeakReference;

public class FadeInImageListener implements ImageLoader.ImageListener
{
    WeakReference<ImageView> mImageView;
    Context mContext;

    public FadeInImageListener(ImageView image, Context context)
    {
        mImageView = new WeakReference<ImageView>(image);
        mContext = context;
    }

    @Override
    public void onErrorResponse(VolleyError arg0)
    {
        if (mImageView.get() != null)
        {
            mImageView.get().setImageResource(R.drawable.avatar);
        }
    }

    @Override
    public void onResponse(ImageLoader.ImageContainer response , boolean arg1)
    {
        if (mImageView.get() != null)
        {
            ImageView image = mImageView.get();
            if (response.getBitmap() != null)
            {
                //                image.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
                image.setImageBitmap(response.getBitmap());
            }
            else
            {
                image.setImageResource(R.drawable.avatar);
            }
        }
    }
}
