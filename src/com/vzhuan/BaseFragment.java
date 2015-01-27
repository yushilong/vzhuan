package com.vzhuan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lscm on 2015/1/5.
 */
public class BaseFragment extends Fragment {
    public View containerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (containerView == null) {
            if (doGetContentView() != null) {
                containerView = doGetContentView();
            } else {
                containerView = inflater.inflate(doGetContentViewId(), container, false);
            }
            doInitSubViews(containerView);
            doInitDataes();
        }
        ViewGroup parent = (ViewGroup) containerView.getParent();
        if (parent != null) {
            parent.removeView(containerView);
        }
        return containerView;
    }

    public void doInitDataes() {
    }

    public void doInitSubViews(View containerView) {
    }

    public View doGetContentView() {
        return null;
    }

    public int doGetContentViewId() {
        return 0;
    }
}
