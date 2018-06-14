package com.alticast.viettelottcommons.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class GroupieCustomSwitch extends View {
    private String TAG = "ToggleButton";

    // the bitmap of toggle on
    private Bitmap backgroudBitmap = null;

    // the bitmap of toggle flip
    private Bitmap slidingBitmap = null;

    // whether is button if is Sliding
    private boolean isSliding = false;

    // the previous state of the button
    private boolean previousState = false;

    private Paint mPaint = new Paint();

    private Matrix mMatrix = new Matrix();

    private OnToggleStateChangedListener mOnToggleStateChangedListener = null;
    private SwitchTouchListener mOnTouchListener = null;

    // current X-Location which touched
    private float touchXLocation = 0;

    // the slidingBitmap inner margin the ToggleButton
    private float marginLeft = 0;

    private boolean isChecked = false;

    public GroupieCustomSwitch(Context context) {
        super(context);
    }

    public GroupieCustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupieCustomSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageResource(int backgroudResID, int flipResID) {
        backgroudBitmap = BitmapFactory.decodeResource(getResources(), backgroudResID);
        slidingBitmap = BitmapFactory.decodeResource(getResources(), flipResID);
    }

    public void setOnSwitchTouchListener(SwitchTouchListener onTouchListener) {
        this.mOnTouchListener = onTouchListener;

    }

    public void setInitState(boolean isOn) {
        previousState = isOn;
//		invalidate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(backgroudBitmap, mMatrix, mPaint);
        if (isSliding) {// if sliding
            // to avoid slidingBitmap sliding out of the ToggleButton
            if (touchXLocation >= backgroudBitmap.getWidth() - slidingBitmap.getWidth() / 2
                    || touchXLocation <= slidingBitmap.getWidth() / 2) {

                if (touchXLocation >= backgroudBitmap.getWidth() - slidingBitmap.getWidth() / 2) {
                    marginLeft = backgroudBitmap.getWidth() - slidingBitmap.getWidth();
                } else {
                    marginLeft = 0;
                }
            } else {
                marginLeft = touchXLocation - slidingBitmap.getWidth() / 2;
            }
            canvas.drawBitmap(slidingBitmap, marginLeft, 0, mPaint);
        } else {
            if (previousState == true) {// on
                canvas.drawBitmap(slidingBitmap, backgroudBitmap.getWidth() - slidingBitmap.getWidth(), 0, mPaint);
            } else {
                canvas.drawBitmap(slidingBitmap, 0, 0, mPaint);
            }
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (0 <= event.getX() && event.getX() <= backgroudBitmap.getWidth() && 0 <= event.getY()
                        && event.getY() <= backgroudBitmap.getHeight()) {
                    touchXLocation = event.getX();
                    if (mOnTouchListener != null) {
                        mOnTouchListener.onTouchDown();
                    }
                    isSliding = true;
                } else {
                    isSliding = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isSliding) {// to avoid change the state out of the toggle
                    touchXLocation = event.getX();
                }
                break;
            case MotionEvent.ACTION_UP:
                isSliding = false;
                if (touchXLocation > backgroudBitmap.getWidth() / 2) {// on
                    // if previous state is off
                    if (previousState == false) {
                        this.isChecked = true;
                        mOnToggleStateChangedListener.changed(true, this);
                        previousState = true;
                    }
                } else if (touchXLocation < backgroudBitmap.getWidth() / 2) {// off
                    // if previous state if on
                    if (previousState == true) {
                        this.isChecked = false;
                        mOnToggleStateChangedListener.changed(false, this);
                        previousState = false;
                    }
                }
                if (mOnTouchListener != null) {
                    mOnTouchListener.onTouchUp();
                }
                break;
        }
        invalidate();
        return true;
    }

    public void setChecked(boolean isCheck) {
        this.isChecked = isCheck;
        setInitState(isCheck);
        if (mOnToggleStateChangedListener != null)
            mOnToggleStateChangedListener.changed(isCheck, this);
    }

    public void setCheckedNoResponse(boolean isCheck) {
        this.isChecked = isCheck;
        setInitState(isCheck);
    }

    /**
     * The Listener of this ToggleButton
     */
    public interface OnToggleStateChangedListener {
        void changed(boolean isOn, View view);
    }

    /**
     * set the Listener for the ToggleButton
     */
    public void setOnStateChangedListener(OnToggleStateChangedListener mOnToggleStateChangedListener) {
        this.mOnToggleStateChangedListener = mOnToggleStateChangedListener;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public interface SwitchTouchListener {
        public void onTouchUp();

        public void onTouchDown();
    }

}