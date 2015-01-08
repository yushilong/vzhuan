package com.vzhuan;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import com.vzhuan.mode.Task;
import com.vzhuan.viewpager.ViewPager;

import java.util.ArrayList;

/**
 * Created by lscm on 2015/1/5.
 */
public class TaskFragment extends BaseFragment
{
    private ListView mListView;
    private TaskAdapter mTaskAdapter;
    GestureDetector mGestureDetector;
    ViewPager viewPager;

    @Override public int doGetContentViewId()
    {
        return R.layout.task;
    }

    @Override public void doInitSubViews(View containerView)
    {
        super.doInitSubViews(containerView);
        mListView = (ListView) containerView.findViewById(R.id.lv_task);
        mTaskAdapter = new TaskAdapter(getActivity(), new ArrayList<Task>());
        mListView.setAdapter(mTaskAdapter);
        mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener()
        {
            @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
            {
                if (e1.getY() - e2.getY() > 120 && mListView.getLastVisiblePosition() == mTaskAdapter.list.size() - 1)
                {//向上
                    viewPager.setCurrentItem(2, true);
                }
                else if (e1.getY() - e2.getY() <= 120 && mListView.getFirstVisiblePosition() == 0)
                {
                    viewPager.setCurrentItem(0, true);
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override public boolean onTouch(View view, MotionEvent motionEvent)
            {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
    }

    @Override public void doInitDataes()
    {
        super.doInitDataes();
        for (int i = 0; i < 50; i++)
        {
            Task task = new Task();
            task.pic = "http://c.hiphotos.baidu.com/image/pic/item/bba1cd11728b47106bd83abcc0cec3fdfd0323cf.jpg";
            task.name = "this is task name " + i;
            task.desc = "this is task description";
            mTaskAdapter.list.add(task);
        }
        mTaskAdapter.notifyDataSetChanged();
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
