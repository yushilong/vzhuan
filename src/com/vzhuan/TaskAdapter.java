package com.vzhuan;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vzhuan.mode.Ads;

import java.util.List;

/**
 * Created by lscm on 2015/1/6.
 */
public class TaskAdapter extends ReplenishAdapter<Ads>
{
    public TaskAdapter(Activity activity, List<Ads> mList)
    {
        super(activity, mList);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent, ViewHolder viewHolder)
    {
        ImageView iv_pic = viewHolder.obtainView(convertView, R.id.iv_pic);
        TextView tv_name = viewHolder.obtainView(convertView, R.id.tv_name);
        TextView tv_desc = viewHolder.obtainView(convertView, R.id.tv_desc);
        Ads task = list.get(position);
        ImageUtil.displayRoundImage(Constants.HOST + task.icon, iv_pic, 180, R.drawable.avatar);
        tv_name.setText(task.label);
        tv_desc.setText(task.info);
        return convertView;
    }

    @Override public int itemLayoutRes()
    {
        return R.layout.task_item;
    }
}
