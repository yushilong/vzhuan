package com.vzhuan;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vzhuan.mode.Task;

import java.util.List;

/**
 * Created by lscm on 2015/1/6.
 */
public class TaskAdapter extends ReplenishAdapter<Task>
{
    public TaskAdapter(Activity activity, List<Task> mList)
    {
        super(activity, mList);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent, ViewHolder viewHolder)
    {
        ImageView iv_pic = viewHolder.obtainView(convertView, R.id.iv_pic);
        TextView tv_name = viewHolder.obtainView(convertView, R.id.tv_name);
        TextView tv_desc = viewHolder.obtainView(convertView, R.id.tv_desc);
        Task task = list.get(position);
        ImageUtil.displayRoundImage(task.pic, iv_pic, 180, R.drawable.avatar);
        tv_name.setText(task.name);
        tv_desc.setText(task.desc);
        return convertView;
    }

    @Override public int itemLayoutRes()
    {
        return R.layout.task_item;
    }
}
