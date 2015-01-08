package com.vzhuan;

import android.view.View;
import com.vzhuan.viewpager.ViewPager;

/**
 * Created by lscm on 2015/1/5.
 */
public class HomeFragment extends BaseFragment
{
    ViewPager viewPager;

    @Override public int doGetContentViewId()
    {
        return R.layout.home;
    }

    @Override public void doInitSubViews(View containerView)
    {
        super.doInitSubViews(containerView);
        containerView.findViewById(R.id.iv_share).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                viewPager.setCurrentItem(2, true);
            }
        });
        containerView.findViewById(R.id.bt_startcoin).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                viewPager.setCurrentItem(1, true);
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
