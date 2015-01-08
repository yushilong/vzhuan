package com.vzhuan;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lscm on 2015/1/5.
 */
public class GuideActivity extends BaseActivity
{
    @Override public int doGetContentViewId()
    {
        return R.layout.guide;
    }

    @Override public void doInitDataes()
    {
        super.doInitDataes();
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        final List<View> views = new ArrayList<View>();
        LayoutInflater layoutInflater = getLayoutInflater();
        for (int i = 0; i < 4; i++)
        {
            ViewGroup view = (ViewGroup) layoutInflater.inflate(R.layout.guideitem, null);
            views.add(view);
            int imgResId = 0;
            switch (i)
            {
                case 0:
                    imgResId = R.drawable.guide1;
                    break;
                case 1:
                    imgResId = R.drawable.guide2;
                    break;
                case 2:
                    imgResId = R.drawable.guide3;
                    break;
                case 3:
                    imgResId = R.drawable.guide4;
                    break;
            }
            ((ImageView) view.findViewById(R.id.imageView)).setImageResource(imgResId);
        }
        PagerAdapter mPagerAdapter = new PagerAdapter()
        {
            @Override public int getCount()
            {
                return views.size();
            }

            @Override public boolean isViewFromObject(View view, Object o)
            {
                return view == o;
            }

            @Override public void destroyItem(ViewGroup container, int position, Object object)
            {
                container.removeView(views.get(position));
            }

            @Override public Object instantiateItem(ViewGroup container, int position)
            {
                View view = views.get(position);
                container.addView(view);
                if (position == views.size() - 1)
                {
                    view.findViewById(R.id.iv_go).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.iv_go).setOnClickListener(new View.OnClickListener()
                    {
                        @Override public void onClick(View view)
                        {
                            finish();
                            startActivity(new Intent(GuideActivity.this, RegisterActivity.class));
                        }
                    });
                }
                else
                {
                    view.findViewById(R.id.iv_go).setVisibility(View.GONE);
                }
                return view;
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
    }
}
