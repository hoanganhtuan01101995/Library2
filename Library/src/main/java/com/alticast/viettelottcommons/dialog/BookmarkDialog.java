package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;
import com.squareup.picasso.Picasso;

import java.util.Formatter;
import java.util.Locale;

public class BookmarkDialog extends DialogFragment {
    private static final String TAG = BookmarkDialog.class.getSimpleName();
    public static final String CLASS_NAME = BookmarkDialog.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_PACKAGES = "PARAM_PACKAGES";

    private View.OnClickListener mOnClickListener = null;

    public void setmOnNoActionListener(View.OnClickListener mOnNoActionListener) {
        this.mOnNoActionListener = mOnNoActionListener;
    }

    private View.OnClickListener mOnNoActionListener = null;
    private String title;
    private int seekPoint;
    private int duration;
    private String lastTime;
    private String logoUrl;
    private CountDownTimer mCountdownTimer;

    private void timerAutoPlayNextEpisode() {

        mCountdownTimer.start();
    }

    public void removeCoundownTimer() {
        if (mCountdownTimer != null) {
            mCountdownTimer.cancel();
        }
    }

    public void setSrc(String title, int seekPoint, int duration, String lastTime, String logoUrl) {
        this.title = title;
        this.seekPoint = seekPoint;
        this.duration = duration;
//        this.lastTime = lastTime;

        getLastTimeString(seekPoint / 1000);

        this.logoUrl = logoUrl;
    }

    public void getLastTimeString(long elapsedSeconds) {

        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        if (elapsedSeconds >= 3600) {
            hours = elapsedSeconds / 3600;
            elapsedSeconds -= hours * 3600;
        }
        if (elapsedSeconds >= 60) {
            minutes = elapsedSeconds / 60;
            elapsedSeconds -= minutes * 60;
        }
        seconds = elapsedSeconds;

        StringBuilder sb = new StringBuilder(8);
        sb.append(hours > 10 ? hours : "0" + hours).append(":");
        sb.append(minutes > 10 ? minutes : "0" + minutes).append(":");
        sb.append(seconds > 10 ? seconds : "0" + seconds);
        lastTime = sb.toString();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();

        if (WindmillConfiguration.type.equals("phone")) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                attributes.height = WindmillConfiguration.scrWidth - 80;
                attributes.width = attributes.height * 320 / 325;
            } else {
                attributes.width = WindmillConfiguration.scrWidth - 80;
                attributes.height = attributes.height * 325 / 320;
            }
        } else {
            attributes.width = WindmillConfiguration.scrWidth - 80;
            attributes.height = attributes.height * 325 / 320;
        }

        attributes.gravity = Gravity.CENTER;
        attributes.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        dialog.getWindow().setAttributes(attributes);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_bookmark_layout, container, false);
        final ScrollView scrollView = (ScrollView) v.findViewById(R.id.scrollView);
        TextView txtTitle = (TextView) v.findViewById(R.id.title);
        TextView lastTimeWatched = (TextView) v.findViewById(R.id.lastTimeWatched);
        ImageView logo = (ImageView) v.findViewById(R.id.logo);
        ProgressBar seekBar = (ProgressBar) v.findViewById(R.id.custom_seek_bar);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for gingerbread and newer versions
            seekBar.setProgressTintList(ColorStateList.valueOf(0xFF01E6EB));
            seekBar.setProgressBackgroundTintList(ColorStateList.valueOf(0xFFFFFFFF));
        }
        seekBar.setScaleY(0.33f);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        seekBar.setMax(duration);
        seekBar.setProgress(seekPoint);

        txtTitle.setText(title);
        lastTimeWatched.setText(String.format(getString(R.string.lastTimeWatchTitle), (lastTime != null && !lastTime.isEmpty()) ? lastTime : "N/A"));

        if (logoUrl != null && !logoUrl.isEmpty()) {
            Picasso picasso = Picasso.with(getActivity());
            picasso.load(logoUrl).placeholder(R.drawable.bg_grey).into(logo);
        }

        v.findViewById(R.id.btnStartOver).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.btnContinue).setOnClickListener(mOnClickListener);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

        mCountdownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                Logger.d(TAG, " coundown onTick " + millisUntilFinished);
            }

            public void onFinish() {
                Logger.d(TAG, " coundown finish");
                dismiss();
                mOnNoActionListener.onClick(null);
            }
        };
        timerAutoPlayNextEpisode();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeCoundownTimer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeCoundownTimer();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity != null && activity instanceof GlobalActivity) {
            ((GlobalActivity) activity).onDismisDialog();
        }
    }
}
