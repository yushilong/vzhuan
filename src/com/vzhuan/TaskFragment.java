package com.vzhuan;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.Gson;
import com.vzhuan.ads.AdvertiseContext;
import com.vzhuan.api.HttpListener;
import com.vzhuan.api.MyHttpRequestor;
import com.vzhuan.mode.Ads;
import com.vzhuan.viewpager.ViewPager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    MyHttpRequestor getAdsRequest;
    private Activity mActivity;

    @Override public int doGetContentViewId()
    {
        return R.layout.task;
    }

    @Override public void doInitSubViews(View containerView)
    {
        super.doInitSubViews(containerView);
        mActivity = getActivity();
        mListView = (ListView) containerView.findViewById(R.id.lv_task);
        mTaskAdapter = new TaskAdapter(getActivity(), new ArrayList<Ads>());
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final Ads ads = mTaskAdapter.list.get((int) id);
                new AdvertiseContext().setType(mActivity, ads.type).openAd();
            }
        });
    }

    @Override public void doInitDataes()
    {
        super.doInitDataes();
        //        for (int i = 0; i < 50; i++)
        //        {
        //            Task task = new Task();
        //            task.pic = "http://c.hiphotos.baidu.com/image/pic/item/bba1cd11728b47106bd83abcc0cec3fdfd0323cf.jpg";
        //            task.name = "this is task name " + i;
        //            task.desc = "this is task description";
        //            mTaskAdapter.list.add(task);
        //        }
        //
        getAdsRequest = new MyHttpRequestor().init(MyHttpRequestor.GET_METHOD, Constants.GET_ADS, new HttpListener()
        {
            @Override public void onSuccess(String msg)
            {
                JSONArray jsonArray = null;
                try
                {
                    jsonArray = new JSONObject(msg).optJSONArray("entity");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                int length = jsonArray.length();
                Gson gson = new Gson();
                for (int i = 0; i < length; i++)
                {
                    Ads ads = gson.fromJson(jsonArray.optJSONObject(i).toString(), Ads.class);
                    mTaskAdapter.list.add(ads);
                }
                mTaskAdapter.notifyDataSetChanged();
            }

            @Override public void onFailure(int statusCode)
            {
            }
        });
        getAdsRequest.start();
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
