package com.vzhuan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.vzhuan.viewpager.FragmentPagerAdapter;
import com.vzhuan.viewpager.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lscm on 2015/1/5.
 */
public class MainActivity extends BaseActivity
{
    private ViewPager mViewPager;
    private List<Fragment> fragments;

    @Override public int doGetContentViewId()
    {
        return R.layout.main;
    }

    @Override public void doInitDataes()
    {
        super.doInitDataes();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        fragments = new ArrayList<Fragment>();
        fragments.add(0, new HomeFragment());
        fragments.add(1, new TaskFragment());
        ShareFragment shareFragment = new ShareFragment();
        shareFragment.setViewPager(mViewPager);
        fragments.add(2, shareFragment);
        mViewPager.setAdapter(new VerticalPager(getSupportFragmentManager()));
    }

    class VerticalPager extends FragmentPagerAdapter
    {
        public VerticalPager(FragmentManager fm)
        {
            super(fm);
        }

        @Override public Fragment getItem(int i)
        {
            return fragments.get(i);
        }

        @Override public int getCount()
        {
            return fragments.size();
        }
    }

    @Override protected void onDestroy()
    {
        super.onDestroy();
        AppManagerStack.getInstance().AppExit(this);
    }
}
