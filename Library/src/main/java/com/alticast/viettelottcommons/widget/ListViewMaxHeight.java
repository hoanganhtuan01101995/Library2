package com.alticast.viettelottcommons.widget;

/**
 * Created by mc.kim on 12/23/2015.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.ListView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.util.Logger;


public class ListViewMaxHeight extends ListView {

    private int maxHeight;

    public ListViewMaxHeight(Context context) {
        this(context, null);
    }

    public ListViewMaxHeight(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListViewMaxHeight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ListViewMaxHeight);
            maxHeight = a.getDimensionPixelSize(R.styleable.ListViewMaxHeight_maxHeight, Integer.MAX_VALUE);
            a.recycle();
        } else {
            maxHeight = Integer.MAX_VALUE;
        }
    }


    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        Logger.print(this, "onMeasure  measuredHeight   : " + measuredHeight);
        Logger.print(this, "onMeasure  maxHeight    : " + maxHeight);

        Logger.print(this, "onMeasure b heightMeasureSpec  : " + maxHeight);
        if (maxHeight > 0 && maxHeight < measuredHeight) {
            int measureMode = MeasureSpec.getMode(heightMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, measureMode);
        }
        Logger.print(this, "onMeasure a heightMeasureSpec  : " + maxHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            ViewParent p = getParent();
            if (p != null) {
                p.requestDisallowInterceptTouchEvent(true);
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }
}