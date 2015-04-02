package com.vzhuan;

import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.Gson;
//import com.miji.MijiConnect;
//import com.miji.MijiNotifier;
//import com.miji.MijiSpendPointsNotifier;
import com.vzhuan.ads.AdvertiseContext;
import com.vzhuan.api.HttpListener;
import com.vzhuan.api.MyHttpRequestor;
import com.vzhuan.mode.Ads;
import com.vzhuan.viewpager.ViewPager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lscm on 2015/1/5.
 */
public class TaskFragment extends BaseFragment {
    private ListView mListView;
    private TaskAdapter mTaskAdapter;
    GestureDetector mGestureDetector;
    ViewPager viewPager;
    MyHttpRequestor getAdsRequest,updatePointRequest;
    private Activity mActivity;
    private boolean isFirstLogin = true;
    private Timer mTimer;
    private TimerTask mTimeTask;
    private boolean isSendEnd = true;

    @Override
    public int doGetContentViewId() {
        return R.layout.task;
    }

    @Override
    public void doInitSubViews(View containerView) {
        super.doInitSubViews(containerView);
        mActivity = getActivity();
        mListView = (ListView) containerView.findViewById(R.id.lv_task);
        mTaskAdapter = new TaskAdapter(getActivity(), new ArrayList<Ads>());
        mListView.setAdapter(mTaskAdapter);
        mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getY() - e2.getY() > 120 && mListView.getLastVisiblePosition() == mTaskAdapter.list.size() - 1) {//向上
                    viewPager.setCurrentItem(2, true);
                } else if (e1.getY() - e2.getY() <= 120 && mListView.getFirstVisiblePosition() == 0) {
                    viewPager.setCurrentItem(0, true);
                }
                return true;
            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Ads ads = mTaskAdapter.list.get((int) id);
                new AdvertiseContext().setType(mActivity, ads.type).openAd();
            }
        });
    }

    @Override
    public void doInitDataes() {
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
        getAdsRequest = new MyHttpRequestor().init(MyHttpRequestor.GET_METHOD, Constants.GET_ADS, new HttpListener() {
            @Override
            public void onSuccess(String msg) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONObject(msg).optJSONArray("entity");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int length = jsonArray.length();
                Gson gson = new Gson();
                mTaskAdapter.list.clear();
                for (int i = 0; i < length; i++) {
                    Ads ads = gson.fromJson(jsonArray.optJSONObject(i).toString(), Ads.class);
                    mTaskAdapter.list.add(ads);
                }
                mTaskAdapter.notifyDataSetChanged();
                ShareUtil.setString(getActivity(), ShareUtil.ShareKey.TIME_TASK, System.currentTimeMillis() + "");
                //init timer
                mTimer = new Timer();
                mTimeTask = new TimerTask() {
                    @Override
                    public void run() {
//                        updatePointToSever();
                    }
                };
                mTimer.schedule(mTimeTask, 0, 1 * 60 * 1000);
            }

            @Override
            public void onFailure(int statusCode, String emsg) {
            }
        });
    }

//    private void updatePointToSever() {
//        if (!isSendEnd)
//            return;
//        isSendEnd = false;
//        MijiConnect.getInstance().getPoints(new MijiNotifier() {
//            @Override
//            public void getUpdatePoints(String s, int i) {
//                Log.i("HTTP", "积分数--->" + i);
//                if (i > 0) {
//                    MijiConnect.getInstance().spendPoints(i, new MijiSpendPointsNotifier() {
//                        @Override
//                        public void getSpendPointsResponse(String s, int i) {
//                            //调wuyimin端接口
//                            //
//                            String updateUrl = Constants.UPDATE_POINT+"&oid="+s+"&uid="+Constants.getDid()+"&p="+i;
//                            updatePointRequest = new MyHttpRequestor().init(MyHttpRequestor.GET_METHOD, null, new HttpListener() {
//                                @Override
//                                public void onSuccess(String msg) {
//                                    isSendEnd = true;
//                                }
//
//                                @Override
//                                public void onFailure(int statusCode, String emsg) {
//                                    isSendEnd = true;
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void getSpendPointsResponseFailed(String s) {
//                            isSendEnd = true;
//                        }
//                    });
//                }else {
//                    isSendEnd = true;
//                }
//            }
//
//            @Override
//            public void getUpdatePointsFailed(String s) {
//                isSendEnd = true;
//            }
//        });
//    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLogin) {
            getAdsRequest.start();
            isFirstLogin = false;
        } else {
            String timeStr = ShareUtil.getString(getActivity(), ShareUtil.ShareKey.TIME_TASK, "0");
            long time = Long.valueOf(timeStr);
            long currentTime = System.currentTimeMillis();
            if (Math.abs(currentTime - time) >= Constants.time_refresh) {
                getAdsRequest.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mTimer!=null)
            mTimer.cancel();
        super.onDestroy();
    }
}
