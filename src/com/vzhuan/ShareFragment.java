package com.vzhuan;

import android.view.View;
import android.widget.ImageView;
import com.vzhuan.viewpager.ViewPager;

/**
 * Created by lscm on 2015/1/5.
 */
public class ShareFragment extends BaseFragment
{
    ViewPager viewPager;

    @Override public int doGetContentViewId()
    {
        return R.layout.share;
    }

    @Override public void doInitSubViews(View containerView)
    {
        super.doInitSubViews(containerView);
        ImageView iv_rocket = (ImageView) containerView.findViewById(R.id.iv_rocket);
        iv_rocket.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                if (viewPager != null)
                    viewPager.setCurrentItem(0, true);
            }
        });
    }

    public ViewPager getViewPager()
    {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager)
    {
        this.viewPager = viewPager;
    }
}
