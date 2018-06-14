package com.alticast.viettelottcommons.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alticast.viettelottcommons.GlobalKey;
import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.def.PlatformFileds;
import com.alticast.viettelottcommons.dialog.MessageDialog;
import com.alticast.viettelottcommons.dialog.PhoneLoginFragment;
import com.alticast.viettelottcommons.loader.FrontEndLoader;
import com.alticast.viettelottcommons.loader.PlatformLoader;
import com.alticast.viettelottcommons.loader.ProgramLoader;
import com.alticast.viettelottcommons.manager.ChannelManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.MyContentManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.util.Logger;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;

import static android.R.attr.id;

/**
 * Created by mc.kim on 8/24/2016.
 */
public class App extends Application {

    public static final String TAG = App.class.getSimpleName();
    private static boolean mIsAlertShown;

    public enum AppStatus {
        BACKGROUND,                // app is background
        RETURNED_TO_FOREGROUND,    // app returned to foreground(or first launch)
        FOREGROUND;                // app is foreground
    }

    protected AppStatus mAppStatus = null;


    public AppStatus getAppStatus() {
        return mAppStatus;
    }

    // check if app is return foreground
    public boolean isReturnedForground() {
        return mAppStatus.ordinal() == AppStatus.RETURNED_TO_FOREGROUND.ordinal();
    }

    public static Toast getToast(Context context, String title, String message, boolean isAlert) {

        Context appContext = context.getApplicationContext();
        Toast toast = new Toast(appContext);
        View v;
        if (isAlert) {
            v = View.inflate(appContext, R.layout.toast_message_alert, null);
        } else {
            v = View.inflate(appContext, R.layout.toast_message, null);
        }
        if (title != null) {
            Logger.print(TAG, "title : " + title);
            TextView titleView = (TextView) v.findViewById(R.id.title);
            titleView.setText(title);
            titleView.setVisibility(View.VISIBLE);
        }
        if (message != null) {
            Logger.print(TAG, "message : " + message);
            TextView messageView = (TextView) v.findViewById(R.id.message);
            messageView.setText(message);
            messageView.setVisibility(View.VISIBLE);
        }

        toast.setView(v);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setMargin(0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    public static void showToast(Context context, String title, ApiError error, boolean isAlert) {
        Context appContext = context.getApplicationContext();
        Toast toast = new Toast(appContext);
        View v;
        if (isAlert) {
            v = View.inflate(appContext, R.layout.toast_message_alert, null);
        } else {
            v = View.inflate(appContext, R.layout.toast_message, null);
        }
        if (title != null) {
            Logger.print(TAG, "title : " + title);
            TextView titleView = (TextView) v.findViewById(R.id.title);
            titleView.setText(title);
            titleView.setVisibility(View.VISIBLE);
        }
        if (error != null && error.getError() != null && ! error.getError().getMessage().isEmpty()) {
//            Logger.print(TAG, "message : " + message);
            TextView messageView = (TextView) v.findViewById(R.id.message);
            messageView.setText(error.getError().getMessage());
            messageView.setVisibility(View.VISIBLE);
        }

        toast.setView(v);
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        toast.setMargin(0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

//    public static String getErrorMeassage(ApiError error) {
//        error = checkAndChangeError(error);
//        String message;
//        if (error != null) {
//            String errorCode = error.getError().getCode();
//            if (errorCode != null && errorCode.length() != 0) {
//                message = String.format("%s\n(ID:%s)", error.getError().getMessage(), errorCode);
//            } else {
//                message = String.format("%s", error.getError().getMessage());
//            }
//        } else {
//            message = "Error";
//        }
//        return message;
//    }

    public static void showAlertDialog(Context context, FragmentManager fm, ApiError error) {
        try {
            if (isInvalidToken(error)) {
                processInvalidToken(context, error, fm, null);
                return;
            }

            error = checkAndChangeError(error);

            showAlertDialog(context, fm, error, null);
        } catch (Exception e) {
            if (context != null && context instanceof GlobalActivity) {
                ((GlobalActivity) context).sendLog("Exception", e.getMessage());
            }
        }
    }

    public static void showDialog(Context context, FragmentManager fm, ApiError error, final DialogInterface.OnDismissListener onDismissListener) {
        try {
            final MessageDialog dialog = new MessageDialog();
            Bundle args = new Bundle();

            if (isInvalidToken(error)) {
                processInvalidToken(context, error, fm, onDismissListener);
                return;
            }

            error = checkAndChangeError(error);

            String message;
            if (error != null) {
                String errorCode = error.getError().getCode();
                if (errorCode != null && errorCode.length() > 0) {
                    message = String.format("%s\n(ID:%s)", error.getError().getMessage(), errorCode);
                } else {
                    message = error.getError().getMessage();
                }
//            App app = (App) context.getApplicationContext();
            } else {
                message = "Error";
            }

            args.putString(MessageDialog.PARAM_TITLE, context.getString(R.string.notice));
            args.putString(MessageDialog.PARAM_MESSAGE, message);
            args.putString(MessageDialog.PARAM_BUTTON_1, context.getString(R.string.ok));
            dialog.setArguments(args);
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Logger.print(TAG, "showAlertDialog onDismiss");
                    if (onDismissListener != null)
                        onDismissListener.onDismiss(dialog);
                }
            });
            Logger.print(TAG, "showAlertDialog show");
            dialog.show(fm, MessageDialog.CLASS_NAME);
        } catch (Exception e) {
            if (context != null && context instanceof GlobalActivity) {
                ((GlobalActivity) context).sendLog("Exception", e.getMessage());
            }
        }
    }

    public static void showAlertDialogNetwork(Context context, FragmentManager fm, final DialogInterface.OnDismissListener onDismissListener) {
        try {
            final MessageDialog dialog = new MessageDialog();
            Bundle args = new Bundle();
            if (context == null) return;
            String message = context.getResources().getString(R.string.error_h1001);

            args.putString(MessageDialog.PARAM_TITLE, context.getString(R.string.notice));
            args.putString(MessageDialog.PARAM_MESSAGE, message);
            args.putString(MessageDialog.PARAM_BUTTON_1, context.getString(R.string.ok));
            dialog.setArguments(args);
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (onDismissListener != null)
                        onDismissListener.onDismiss(dialog);

                    mIsAlertShown = false;
                }
            });
            if (!fm.isDestroyed() && !mIsAlertShown)
                dialog.show(fm, MessageDialog.CLASS_NAME);
            mIsAlertShown = true;
        } catch (Exception e) {
            if (context != null && context instanceof GlobalActivity) {
                ((GlobalActivity) context).sendLog("Exception", e.getMessage());
            }
        }
    }

    public static void showAlertNoContent(Context context, FragmentManager fm, final DialogInterface.OnDismissListener onDismissListener) {
        try {
            final MessageDialog dialog = new MessageDialog();
            Bundle args = new Bundle();

            String message = context.getResources().getString(R.string.empty_message_vod_list);

            args.putString(MessageDialog.PARAM_TITLE, context.getString(R.string.notice));
            args.putString(MessageDialog.PARAM_MESSAGE, message);
            args.putString(MessageDialog.PARAM_BUTTON_1, context.getString(R.string.ok));
            dialog.setArguments(args);
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (onDismissListener != null)
                        onDismissListener.onDismiss(dialog);


                    mIsAlertShown = false;
                }
            });
            if (!fm.isDestroyed())
                dialog.show(fm, MessageDialog.CLASS_NAME);
            mIsAlertShown = true;
        } catch (Exception e) {
            if (context != null && context instanceof GlobalActivity) {
                ((GlobalActivity) context).sendLog("Exception", e.getMessage());
            }
        }
    }

    public static String getErrorMeassage(ApiError error) {
        error = checkAndChangeError(error);
        String message;
        if (error != null) {
            String errorCode = error.getError().getCode();
            if (errorCode != null && errorCode.length() != 0) {
                message = String.format("%s\n(ID:%s)", error.getError().getMessage(), errorCode);
            } else {
                message = String.format("%s", error.getError().getMessage());
            }
        } else {
            message = "Error";
        }
        return message;
    }

    public static void showAlertDialog(Context context, FragmentManager fm, ApiError error, final DialogInterface.OnDismissListener onDismissListener) {
        try {
            Logger.print(TAG, "showAlertDialog mIsAlertShown:" + mIsAlertShown);
            Logger.print(TAG, "showAlertDialog error: " + error);

            if (isInvalidToken(error)) {
                processInvalidToken(context, error, fm, onDismissListener);
                return;
            }

            error = checkAndChangeError(error);

//        showAlertDialog(context, fm, error.getError().getCode(), error.getError().getMessage(), onDismissListener);

            if (mIsAlertShown || error == null || error.getError() == null || "C0202".equals(error.getError().getCode())) {
                if (onDismissListener != null)
                    onDismissListener.onDismiss(null);
                return;
            }
            Logger.print(TAG, "showAlertDialog error.getErrorCode(): " + error.getError().getCode());
            final MessageDialog dialog = new MessageDialog();
            Bundle args = new Bundle();

            String message;
            if (error != null) {
                String errorCode = error.getError().getCode();
                if (errorCode != null && errorCode.length() != 0) {
                    message = String.format("%s\n(ID:%s)", error.getError().getMessage(), errorCode);
                } else {
                    message = String.format("%s", error.getError().getMessage());
                }
//            App app = (App) context.getApplicationContext();
            } else {
                message = "Error";
            }

            args.putString(MessageDialog.PARAM_TITLE, context.getString(R.string.notice));
            args.putString(MessageDialog.PARAM_MESSAGE, message);
            args.putString(MessageDialog.PARAM_BUTTON_1, context.getString(R.string.ok));
            dialog.setArguments(args);
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (onDismissListener != null)
                        onDismissListener.onDismiss(dialog);


                    mIsAlertShown = false;
                }
            });
            if (!fm.isDestroyed())
                dialog.show(fm, MessageDialog.CLASS_NAME);
            mIsAlertShown = true;
        } catch (Exception e) {
            if (context != null && context instanceof GlobalActivity) {
                ((GlobalActivity) context).sendLog("Exception", e.getMessage());
            }
        }
    }

    public static void showAlertDialog(Context context, FragmentManager fm, String code, String message, final DialogInterface.OnDismissListener onDismissListener) {
        try {
            Logger.print(TAG, "showAlertDialog mIsAlertShown:" + mIsAlertShown);
            Logger.print(TAG, "showAlertDialog error: " + message);

            if (mIsAlertShown || (code != null && "C0202".equals(code))) {
                if (onDismissListener != null)
                    onDismissListener.onDismiss(null);
                return;
            }
            Logger.print(TAG, "showAlertDialog error.getErrorCode(): " + code);
            final MessageDialog dialog = new MessageDialog();
            Bundle args = new Bundle();

            if (message != null && message.length() != 0) {
                if (code != null && code.length() != 0) {
                    message = String.format("%s\n(ID:%s)", message, code);
                } else {
                    message = String.format("%s", message);
                }
//            App app = (App) context.getApplicationContext();
            } else {
                message = "Error";
            }

            args.putString(MessageDialog.PARAM_TITLE, context.getString(R.string.notice));
            args.putString(MessageDialog.PARAM_MESSAGE, message);
            args.putString(MessageDialog.PARAM_BUTTON_1, context.getString(R.string.ok));
            dialog.setArguments(args);
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (onDismissListener != null)
                        onDismissListener.onDismiss(dialog);


                    mIsAlertShown = false;
                }
            });
            if (!fm.isDestroyed())
                dialog.show(fm, MessageDialog.CLASS_NAME);
            mIsAlertShown = true;
        } catch (Exception e) {
            if (context != null && context instanceof GlobalActivity) {
                ((GlobalActivity) context).sendLog("Exception", e.getMessage());
            }
        }
    }

    public static void showAlertMessageDialog(Context context, FragmentManager fm, String title, String message, final DialogInterface.OnDismissListener onDismissListener) {
        try {
            Logger.print(TAG, "showAlertDialog mIsAlertShown:" + mIsAlertShown);
            Logger.print(TAG, "showAlertDialog error: " + message);

            final MessageDialog dialog = new MessageDialog();
            Bundle args = new Bundle();

            args.putString(MessageDialog.PARAM_TITLE, title);
            args.putString(MessageDialog.PARAM_MESSAGE, message);
            args.putString(MessageDialog.PARAM_BUTTON_1, context.getString(R.string.ok));
            dialog.setArguments(args);
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (onDismissListener != null)
                        onDismissListener.onDismiss(dialog);


                    mIsAlertShown = false;
                }
            });
            if (!fm.isDestroyed())
                dialog.show(fm, MessageDialog.CLASS_NAME);
            mIsAlertShown = true;
        } catch (Exception e) {
            if (context != null && context instanceof GlobalActivity) {
                ((GlobalActivity) context).sendLog("Exception", e.getMessage());
            }
        }
    }

    public static ApiError checkAndChangeError(ApiError error) {

        if (error == null) {
            return null;
        }

        if (error.getError() == null) {
            return null;
        }

        String code = error.getError().getCode();

        if(code != null && code.contains("C99")) {
            error.getError().setMessage("Quý khách vui lòng kiểm tra lại kết nối mạng và thử lại!");
            return error;
        }

        String message = error.getError().getMessage();
        if (message != null && message.contains("***")) {
            message = message.replaceAll(Pattern.quote("***"), PlatformLoader.getInstance().getPlatFormValue(PlatformFileds.REGIONAL_PHONE));
            error.getError().setMessage(message);
        }

        return error;

    }

    public static boolean isInvalidToken(ApiError error) {
        if (error == null || error.getError() == null) {
            return false;
        }
        return error.getError().getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED && error.getError().getCode().equals("C0202");
    }

    public static void processInvalidToken(final Context context, ApiError error, final FragmentManager fm, final DialogInterface.OnDismissListener onDismissListener) {

        if (context == null) {
            return;
        }

        final GlobalActivity globalActivity = (GlobalActivity) context;
        globalActivity.sendLog("Invalidtoken", "ID: " + HandheldAuthorization.getInstance().getCurrentId() + ", Error=" + error);
        if (HandheldAuthorization.getInstance().getBoolean(ServiceGenerator.ERROR_CODE_1024)) {
            globalActivity.showProgress();
            FrontEndLoader.getInstance().requestLogout(new WindmillCallback() {
                @Override
                public void onSuccess(Object obj) {
                    globalActivity.hideProgress();
                    HandheldAuthorization.getInstance().logOut();
                    MyContentManager.getInstance().clearData();
                    ChannelManager.getInstance().clearData();
                    ProgramLoader.getInstance().clearData();
                    globalActivity.checkAndRemoveOverlayFragment();
                    Intent intent = new Intent(GlobalKey.MainActivityKey.REFRESH_DATA);
                    intent.putExtra(GlobalKey.MainActivityKey.IS_CHANNEL_ACTIVITY, true);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    ((GlobalActivity) context).showLimitNoticeDialog();
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    globalActivity.hideProgress();
                    showAlertDialogNetwork(globalActivity, globalActivity.getSupportFragmentManager(), null);
                }

                @Override
                public void onError(ApiError error) {
                    globalActivity.hideProgress();
                    showAlertDialog(globalActivity, globalActivity.getSupportFragmentManager(), error, null);
                }
            });
            HandheldAuthorization.getInstance().putBoolean(ServiceGenerator.ERROR_CODE_1024, false);

        } else if (error.getError().getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED && !error.getError().getCode().equals("F0102")) {
            globalActivity.showProgress();
            FrontEndLoader.getInstance().requestLogout(new WindmillCallback() {
                @Override
                public void onSuccess(Object obj) {
                    globalActivity.hideProgress();
                    HandheldAuthorization.getInstance().logOut();
                    MyContentManager.getInstance().clearData();
                    ChannelManager.getInstance().clearData();
                    ProgramLoader.getInstance().clearData();
                    globalActivity.checkAndRemoveOverlayFragment();
                    Intent intent = new Intent(GlobalKey.MainActivityKey.REFRESH_DATA);
                    intent.putExtra(GlobalKey.MainActivityKey.IS_CHANNEL_ACTIVITY_AUTHOR, true);
                    intent.putExtra(GlobalKey.MainActivityKey.ACCESS_TOKEN_FAIL, true);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    ((GlobalActivity) context).checkAutoLogin(context);
                    globalActivity.hideProgress();
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    globalActivity.hideProgress();
                    showAlertDialogNetwork(globalActivity, globalActivity.getSupportFragmentManager(), null);
                }

                @Override
                public void onError(ApiError error) {
                    globalActivity.hideProgress();
                    showAlertDialog(globalActivity, globalActivity.getSupportFragmentManager(), error, null);
                }
            });
        }

    }

    public static boolean isAlertShown() {
        return mIsAlertShown;
    }
}
