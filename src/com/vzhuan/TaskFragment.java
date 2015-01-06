package com.vzhuan;

import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by lscm on 2015/1/5.
 */
public class TaskFragment extends BaseFragment
{
    private ListView mListView;
    private TaskAdapter mTaskAdapter;

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
    }

    @Override public void doInitDataes()
    {
        super.doInitDataes();
        for (int i = 0; i < 6; i++)
        {
            Task task = new Task();
            task.pic = "http://c.hiphotos.baidu.com/image/pic/item/bba1cd11728b47106bd83abcc0cec3fdfd0323cf.jpg";
            task.name = "this is task name " + i;
            task.desc = "this is task description";
            mTaskAdapter.list.add(task);
        }
        mTaskAdapter.notifyDataSetChanged();
    }
}
