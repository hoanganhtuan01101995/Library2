package com.alticast.viettelottcommons.playback;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import com.alticast.media.AltiPlayer;
import com.alticast.media.AltiPlayerListener;
import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;


/**
 * Created by mc.kim on 8/16/2016.
 */
public class AltiVideoView extends SurfaceView implements MediaController.MediaPlayerControl {
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;


    private final int SUCCESS_SETDATASRC = 0;

    private final String HOST_HLS = "hls";


    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;


    private SurfaceHolder mSurfaceHolder = null;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mSurfaceWidth;
    private int mSurfaceHeight;


    private int mSeekWhenPrepared;  // recording the seek position while preparing
    private boolean mCanPause;
    private boolean mCanSeekBack;
    private boolean mCanSeekForward;

    private Uri mUri;
    private OnErrorListener mOnErrorListener;
    private OnPrepareListener mOnPrepareListener;
    private OnCompleteListener mOnCompleteListener;
    private int mAudioSession;
    private String timeOut = String.valueOf(54);

    private int iframe_resId = -1;

    private ViewHandler viewHandler = new ViewHandler();
    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mSurfaceHolder = holder;
            Logger.print(this, "surfaceCreated  ");
            if (mCurrentState == STATE_PAUSED) {
                AltiPlayer.setVideoSurface(holder.getSurface());
                viewHandler.sendEmptyMessage(ViewHandler.RESUME);
            } else {
                openVideo();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mSurfaceWidth = width;
            mSurfaceHeight = height;

            boolean isValidState = (mTargetState == STATE_PLAYING);
            boolean hasValidSize = (mVideoWidth == width && mVideoHeight == height);


            Logger.print(this, "surfaceChanged  isValidState : " + isValidState);
            Logger.print(this, "surfaceChanged  hasValidSize : " + hasValidSize);
            Logger.print(this, "surfaceChanged  width : " + width + " ,height : " + height);
            if (isValidState && hasValidSize) {

            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Logger.print(this, "surfaceDestroyed  ");
            mSurfaceHolder = null;
            if (getVideoType() == AltiPlayer.TYPE_CHANNEL) {
                release(true);
            } else {
                pause();
            }
        }
    };


    private void changeSurfaceSize(int width, int height) {
        float scaleWidth = 1f;
        float scaleHeight = 1f;

        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();


        float videoRatio = (float) width / height;
        float viewRatio = (float) viewWidth / viewHeight;
        if (viewRatio > videoRatio) {
            scaleWidth = videoRatio / viewRatio;
        } else {
            scaleHeight = viewRatio / videoRatio;
        }

        int scaledWidth = (int) (scaleWidth * width);
        int scaledHeight = (int) (scaleHeight * height);
        Logger.print(this, "onVideoSizeChanged scaledWidth : " + scaledWidth);
        Logger.print(this, "onVideoSizeChanged scaledHeight : " + scaledHeight);

        if (scaledWidth != 0 && scaledHeight != 0) {
            Message msg = viewHandler.obtainMessage();
            msg.what = ViewHandler.SURFACE_SET_FIXED_SIZE;
            msg.arg1 = scaledWidth;
            msg.arg2 = scaledHeight;
            viewHandler.sendMessage(msg);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            Logger.print(this, "onConfigurationChanged ORIENTATION_LANDSCAPE");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Logger.print(this, "onConfigurationChanged ORIENTATION_PORTRAIT");

        }

        changeSurfaceSize(getMeasuredWidth(), getMeasuredHeight());
    }

    public AltiVideoView(Context context) {
        super(context);
        initVideoView();
    }

    public AltiVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            Logger.print(this, "attrs");
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AltiVideoView);
            iframe_resId = a.getResourceId(R.styleable.AltiVideoView_iframe, -1);
            a.recycle();
        } else {
            iframe_resId = -1;
        }
        initVideoView();
    }

    public AltiVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        if (attrs != null) {
            Logger.print(this, "attrs");
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AltiVideoView);
            iframe_resId = a.getResourceId(R.styleable.AltiVideoView_iframe, -1);
            a.recycle();
        } else {
            iframe_resId = -1;
        }

        initVideoView();
    }


    private void initVideoView() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        getHolder().addCallback(mSHCallback);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        mCurrentState = STATE_IDLE;
        mTargetState = STATE_IDLE;

    }

    public void setIframe_resId(int iframe_resId) {
        this.iframe_resId = iframe_resId;
    }

    public void resume() {
        Logger.print(this, "onResume() isInPlaybackState() : " + isInPlaybackState());
        if (isInPlaybackState()) {

            mCurrentState = STATE_PLAYING;

            viewHandler.sendEmptyMessage(ViewHandler.RESUME);
        }
        mTargetState = STATE_PLAYING;
    }

    @Override
    public void start() {
        Logger.print(this, "call start");
        if (isInPlaybackState()) {
            viewHandler.sendEmptyMessage(ViewHandler.START);

            mCurrentState = STATE_PLAYING;
        }
        mTargetState = STATE_PLAYING;
    }


    private int getVideoType() {
        if (mUri == null) {
            return -1;
        }

        String host = mUri.getScheme();
        int type;
        if (host.equals(HOST_HLS)) {
            type = AltiPlayer.TYPE_CHANNEL;
        } else {
            type = AltiPlayer.TYPE_VOD;
        }
        return type;
    }

    @Override
    public void pause() {
        if (isInPlaybackState()) {
            Message msg = viewHandler.obtainMessage(ViewHandler.PAUSE);
            viewHandler.sendMessage(msg);
            mCurrentState = STATE_PAUSED;

        }
        mTargetState = STATE_PAUSED;

    }

    @Override
    public int getDuration() {
        if (isInPlaybackState()) {
            return AltiPlayer.getDuration();
        }

        return -1;
    }


    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnPrepareListener(OnPrepareListener onPrepareListener) {
        this.mOnPrepareListener = onPrepareListener;
    }

    public void setOnCompleteListener(OnCompleteListener nCompleteListener) {
        this.mOnCompleteListener = nCompleteListener;
    }

    @Override
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return AltiPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void setDataResource(Uri uri, int startOffset) {
        if (getVideoType() == AltiPlayer.TYPE_VOD) {
            mUri = new Uri.Builder()
                    .scheme("hlsvod")
                    .encodedAuthority(uri.toString())
                    .appendQueryParameter("content_id", "0")
                    .appendQueryParameter("start_file_offset", String.valueOf(startOffset))
                    .appendQueryParameter("duration", "100000")
                    .appendQueryParameter("settlement_type", "1")
                    .appendQueryParameter("ad", "0")
                    .appendQueryParameter("user_pin", "0")
                    .appendQueryParameter("payment_type", "0")
                    .appendQueryParameter("hls_max_bw", "0")
                    .appendQueryParameter("hls_default_bw", "0")
//                    .appendQueryParameter("hls_max_bw", "-1")
//                    .appendQueryParameter("hls_default_bw", "3000000")
                    .appendQueryParameter("srcdata_timeout", timeOut)
                    .appendQueryParameter("video_start_frame_count", Config.VIDEO_START_FRAME_COUNT).
                            build();
        } else {
            //live
            mUri = new Uri.Builder()
                    .scheme("hls")
                    .encodedAuthority(uri.toString())
                    .appendQueryParameter("content_id", "0")
                    .appendQueryParameter("start_file_offset", "0")
                    .appendQueryParameter("duration", "0")
                    .appendQueryParameter("settlement_type", "1")
                    .appendQueryParameter("ad", "0")
                    .appendQueryParameter("user_pin", "0")
                    .appendQueryParameter("payment_type", "0")
                    .appendQueryParameter("hls_max_bw", "0")
                    .appendQueryParameter("hls_default_bw", "0")
//                    .appendQueryParameter("hls_max_bw", "-1")
//                    .appendQueryParameter("hls_default_bw", "3000000")
                    .appendQueryParameter("srcdata_timeout", timeOut)
                    .appendQueryParameter("video_start_frame_count", Config.VIDEO_START_FRAME_COUNT).build();
        }
        setVideoURI(mUri);
    }

    @Override
    public void seekTo(int pos) {
        if (isInPlaybackState()) {

            Message msg = viewHandler.obtainMessage();
            msg.what = ViewHandler.SEEK_TO;
            msg.arg1 = pos;
            viewHandler.sendMessage(msg);

            mSeekWhenPrepared = 0;
        } else {
            mSeekWhenPrepared = pos;
        }
    }

    public void seekForward(int milis) {
        int currentTime = getCurrentPosition();
        if (currentTime + milis < getDuration()) {
            seekTo(currentTime + milis);
        } else {
            seekTo(getDuration());
        }
    }

    public void seekBack(int milis) {
        int currentTime = getCurrentPosition();
        if (currentTime - milis < 0) {
            seekTo(0);
        } else {
            seekTo(currentTime - milis);
        }
    }

    @Override
    public boolean isPlaying() {
        return isInPlaybackState() && AltiPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return -1;
    }

    @Override
    public boolean canPause() {
        return mCanPause;
    }

    @Override
    public boolean canSeekBackward() {
        return mCanSeekBack;
    }

    @Override
    public boolean canSeekForward() {
        return mCanSeekForward;
    }

    @Override
    public int getAudioSessionId() {
        return -1;
    }

    private boolean isInPlaybackState() {
        Logger.print(this, "mCurrentState : " + mCurrentState);

        return (
                mCurrentState != STATE_ERROR &&
                        mCurrentState != STATE_IDLE &&
                        mCurrentState != STATE_PREPARING && mCurrentState != STATE_PLAYBACK_COMPLETED);
    }

    private void openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously

        release(false);
        Logger.print(this, "openVideo");

        Message msg = viewHandler.obtainMessage(ViewHandler.SET_DATA_SRC);
        msg.obj = mUri.toString();
        viewHandler.sendMessage(msg);
    }


    public void release(boolean cleartargetstate) {
        if (isInPlaybackState()) {
            Message msg = viewHandler.obtainMessage();
            msg.what = ViewHandler.RELEASE;
            viewHandler.sendMessage(msg);
        } else if (iframe_resId != -1) {

            this.setBackground(getResources().getDrawable(iframe_resId));
        }


        mCurrentState = STATE_IDLE;
        if (cleartargetstate) {
            mTargetState = STATE_IDLE;
        }
        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(null);

    }

    public void setVideoURI(Uri uri) {
        mUri = uri;
        mode_list = false;
        mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    private ArrayList<Uri> mUriList = null;
    private int idx = 0;
    private boolean mode_list = false;

    public void setVideoURIList(ArrayList<Uri> uriList) {
        this.mUriList = uriList;
        mode_list = true;
        mUri = mUriList.get(idx);
        mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void showNextVod() {
        idx++;
        if (idx < mUriList.size()) {
            mode_list = true;
            mUri = mUriList.get(idx);
            mSeekWhenPrepared = 0;
            openVideo();
            requestLayout();
            invalidate();

        } else {
            release(true);
            mCurrentState = STATE_PLAYBACK_COMPLETED;
            mTargetState = STATE_PLAYBACK_COMPLETED;

            if (mOnCompleteListener != null) {
                mOnCompleteListener.onComplete();
            }
        }

    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec) {
        return getDefaultSize(desiredSize, measureSpec);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("@@@@", "onMeasure(" + MeasureSpec.toString(widthMeasureSpec) + ", "
                + MeasureSpec.toString(heightMeasureSpec) + ")");

        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
        if (mVideoWidth > 0 && mVideoHeight > 0) {

            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize;
                height = heightSpecSize;

                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * mVideoHeight / mVideoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * mVideoWidth / mVideoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = mVideoWidth;
                height = mVideoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * mVideoWidth / mVideoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * mVideoHeight / mVideoWidth;
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }
        setMeasuredDimension(width, height);
    }

    public boolean isPrepared() {
        return mCurrentState == STATE_PREPARED;
    }


    AltiPlayerListener mAltiPlayerListener = new AltiPlayerListener() {
        @Override
        public void onComplete() {
            if (mode_list) {

                showNextVod();

            } else {
                mCurrentState = STATE_PLAYBACK_COMPLETED;
                mTargetState = STATE_PLAYBACK_COMPLETED;

                if (mOnCompleteListener != null) {
                    mOnCompleteListener.onComplete();
                }
            }


        }

        @Override
        public void onBufferingUpdate(int percent) {
            //not used
        }

        @Override
        public void onVideoSizeChanged(int width, int height) {
            mVideoWidth = width;
            mVideoHeight = height;
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                changeSurfaceSize(mVideoWidth, mVideoHeight);
            }

        }

        @Override
        public void onError(int why, int extra) {
            Message msg = viewHandler.obtainMessage(ViewHandler.PAUSE);
            viewHandler.sendMessage(msg);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;

            if (mOnErrorListener != null) {
                mOnErrorListener.onError(why, extra);
            }
        }


        @Override
        public void onPrepare(int type) {
            mCurrentState = STATE_PREPARED;
            mCanPause = mCanSeekBack = mCanSeekForward = type == AltiPlayer.TYPE_VOD;

            mVideoWidth = AltiPlayer.getVideoWidth();
            mVideoHeight = AltiPlayer.getVideoHeight();

            Logger.print(this, "mVideoWidth : " + mVideoWidth);
            Logger.print(this, "mVideoHeight : " + mVideoHeight);
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                changeSurfaceSize(mVideoWidth, mVideoHeight);
            }

            int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
            if (getVideoType() == AltiPlayer.TYPE_VOD && seekToPosition != 0) {
                seekTo(seekToPosition);
                resume();
            } else {
                if (mOnPrepareListener != null) {
                    mOnPrepareListener.onPrepare();
                }
            }
        }

        @Override
        public void onVideoRenderingStarted(int width, int height) {
            viewHandler.sendEmptyMessage(ViewHandler.RENDERING_START);
        }
    };

    protected class ViewHandler extends Handler {

        public static final int ON = 0;
        public static final int OFF = 1;
        public static final int SURFACE_VISIBLE = 2;
        public static final int SURFACE_INVISIBLE = 3;
        public static final int SURFACE_SET_VISIBLE = 4;
        public static final int SURFACE_SET_INVISIBLE = 5;
        public static final int SURFACE_SET_FIXED_SIZE = 6;
        public static final int SEEK_TO = 7;
        public static final int RELEASE = 8;
        public static final int PAUSE = 9;
        public static final int START = 10;
        public static final int SET_DATA_SRC = 11;
        public static final int RESUME = 12;
        public static final int RENDERING_START = 13;


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Logger.print(this, "ViewHandler - What: " + msg.what);
            switch (msg.what) {
                case ON:
                    setKeepScreenOn(true);
                    break;

                case OFF:
                    setKeepScreenOn(false);

                    break;

                case SURFACE_SET_FIXED_SIZE:
                    getHolder().setFixedSize(msg.arg1, msg.arg2);
                    break;

                case SEEK_TO:
                    Logger.print(this, "SEEK_TO: ViewHandler time : " + msg.arg1);
                    AltiPlayer.seekTo(msg.arg1, false);
                    break;

                case RELEASE:
                    setBackground(true);
                    setKeepScreenOn(false);
                    AltiPlayer.release();
                    break;

                case PAUSE:
                    setKeepScreenOn(false);
                    AltiPlayer.pause();
                    break;
                case START:

                    AltiPlayer.start(getVideoType());
                    setKeepScreenOn(true);
                    break;

                case RENDERING_START:
                    setBackground(false);

                    break;


                case SET_DATA_SRC:

                    new StreamReadyTask((String) msg.obj).execute();
                    break;

                case RESUME:
                    setKeepScreenOn(true);
                    AltiPlayer.resume();
                    break;
                default:

                    break;
            }
        }
    }

    private void setBackground(boolean clear) {
        if (iframe_resId == -1) {
            return;
        }

        if (clear)
            this.setBackground(getResources().getDrawable(iframe_resId));
        else
            this.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public class StreamReadyTask extends AsyncTask<Void, Void, Integer> {
        private String mUrl = null;

        public StreamReadyTask(String param) {
            this.mUrl = param;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... vs) {
            Logger.print(this, "mUrl : " + mUrl);
            return AltiPlayer.setDataSource(mUrl);
        }

        @Override
        protected void onPostExecute(Integer obj) {
            super.onPostExecute(obj);
            int result = obj;
            if (result != SUCCESS_SETDATASRC) {
                if (mOnErrorListener != null) {
                    mOnErrorListener.onError(result, result);
                }
                return;
            }
            AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            AltiPlayer.setAltiPlayerListener(mAltiPlayerListener);
            AltiPlayer.setVideoSurface(mSurfaceHolder.getSurface());
            mCurrentState = STATE_PREPARING;
            mAltiPlayerListener.onPrepare(getVideoType());
        }
    }
}
