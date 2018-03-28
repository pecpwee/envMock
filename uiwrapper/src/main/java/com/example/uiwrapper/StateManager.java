package com.example.uiwrapper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import com.pecpwee.lib.envmock.utils.LogUtils;

import java.util.HashMap;

/**
 * Created by pw on 2018/3/23.
 */

public class StateManager {
    private static final String TAG = "StateManager ";
    private Activity currentActivity = null;
    private View floatingView;
    ViewGroup.MarginLayoutParams floatingViewParams;
    MainPannelFragment mainFragment;
    HashMap<Activity, FloatView> hashMap;


    public void addFloatingWindow(Activity activity) {

        this.currentActivity = activity;

        ViewGroup contentParentView = activity.findViewById(android.R.id.content);
        for (int i = 0; i < contentParentView.getChildCount(); i++) {
            if (contentParentView.getChildAt(i) == floatingView) {
                // do anything here
                return;
            }
        }
        final View view = LayoutInflater.from(activity).inflate(R.layout.view_floating_icon, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMainWindow();
            }
        });
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                floatingViewParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            }
        });
        floatingView = view;


        LogUtils.d("addFloatingWindow");
        if (floatingViewParams != null) {
            activity.addContentView(floatingView, floatingViewParams);
        } else {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.height = 200;
            params.width = 200;
            activity.addContentView(floatingView, params);
        }

    }

    public void removeFloatingWindow(Activity activity) {
        ((ViewGroup) floatingView.getParent()).removeView(floatingView);
        onActivityStop(activity);
    }

    public void showMainWindow() {
        if (mainFragment == null) {
            mainFragment = new MainPannelFragment();
        }
        mainFragment.showFragment(currentActivity);
    }

    public void onActivityStop(Activity activity) {
        if (currentActivity == activity) {
            if (mainFragment != null) {
                mainFragment.destroy(activity);
            }
            currentActivity = null;
        }
    }
}
