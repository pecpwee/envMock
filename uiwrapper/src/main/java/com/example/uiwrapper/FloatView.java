package com.example.uiwrapper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by pw on 2018/3/23.
 */

public class FloatView extends ImageView {

    private float mLastX;
    private float mLastY;
    private Boolean mIsDrag;
    private int mTouchSlop;


    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop() << 1;
        this.setAlpha(0.8F);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsDrag = null;
                mLastX = event.getRawX();
                mLastY = event.getRawY();
                this.setAlpha(0.4F);

                break;
            case MotionEvent.ACTION_MOVE:
                float curX = event.getRawX();
                float curY = event.getRawY();
                if (mIsDrag == null) {
                    if (Math.pow(curX - mLastX, 2) + Math.pow(curY - mLastY, 2) > mTouchSlop) {
                        mIsDrag = true;
                        mLastX = curX;
                        mLastY = curY;
                        break;
                    }
                } else if (mIsDrag) {
                    ViewGroup.LayoutParams params = getLayoutParams();
                    if (!(params instanceof ViewGroup.MarginLayoutParams)) {
                        return super.onTouchEvent(event);
                    }
                    ViewGroup.MarginLayoutParams marginLayoutParams = ((ViewGroup.MarginLayoutParams) params);
                    marginLayoutParams.leftMargin += (curX - mLastX);
                    marginLayoutParams.topMargin += (curY - mLastY);
                    requestLayout();
                    mLastX = curX;
                    mLastY = curY;
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsDrag == null) {
                    performClick();
                }
                moveIfNeeded();
                this.setAlpha(0.8F);

                break;
        }

        return true;
    }

    private void moveIfNeeded() {//保持在屏幕内
        ViewGroup.LayoutParams params = getLayoutParams();
        if (!(params instanceof ViewGroup.MarginLayoutParams)) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = ((ViewGroup.MarginLayoutParams) params);
        if (marginLayoutParams.leftMargin < 0) {
            marginLayoutParams.leftMargin = 0;
        }
        if (marginLayoutParams.topMargin < 0) {
            marginLayoutParams.topMargin = 0;
        }
        if (marginLayoutParams.leftMargin + getWidth() > ((View) getParent()).getWidth()) {
            marginLayoutParams.leftMargin = ((View) getParent()).getWidth() - getWidth();
        }
        if (marginLayoutParams.topMargin + getHeight() > ((View) getParent()).getHeight()) {
            marginLayoutParams.topMargin = ((View) getParent()).getHeight() - getHeight();
        }
        requestLayout();
    }



}
