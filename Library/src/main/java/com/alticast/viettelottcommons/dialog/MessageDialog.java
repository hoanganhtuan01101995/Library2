package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.util.Logger;

public class MessageDialog extends DialogFragment {
    public static final String CLASS_NAME = MessageDialog.class.getName();

    public static final String PARAM_TITLE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_SUBTITLE = CLASS_NAME + ".PARAM_SUBTITLE";
    public static final String PARAM_MESSAGE = CLASS_NAME + ".PARAM_MESSAGE";
    public static final String PARAM_COLOR_MESSAGE = CLASS_NAME + ".PARAM_COLOR_MESSAGE";
    public static final String PARAM_MESSAGE_NAME = CLASS_NAME + ".PARAM_MESSAGE_NAME";
    public static final String PARAM_MESSAGE_EMPHASIS = CLASS_NAME + ".PARAM_MESSAGE_EMPHASIS";
    public static final String PARAM_MESSAGE_COMMENT = CLASS_NAME + ".PARAM_MESSAGE_COMMENT";
    public static final String PARAM_BUTTON_1 = CLASS_NAME + ".PARAM_BUTTON_1";
    public static final String PARAM_BUTTON_2 = CLASS_NAME + ".PARAM_BUTTON_2";
    public static final String PARAM_BUTTON_3 = CLASS_NAME + ".PARAM_BUTTON_3";
    public static final String PARAM_CANCEL_ON_TOUCH_OUTSIDE = CLASS_NAME + ".PARAM_CANCEL_ON_TOUCH_OUTSIDE";

    public static final String PARAM_FACEBOOK_TITLE = CLASS_NAME + ".PARAM_FACEBOOK_TITLE";
    public static final String PARAM_FACEBOOK_MESSAGE = CLASS_NAME + ".PARAM_FACEBOOK_MESSAGE";
    public static final String PARAM_FACEBOOK_MESSAGE2 = CLASS_NAME + ".PARAM_FACEBOOK_MESSAGE2";

    private static final String TAG = MessageDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    private OnDismissListener mOnDismissListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        View v = dialog.getWindow().getDecorView();

        Bundle arguments = getArguments();
        if (arguments != null) {
            String title = arguments.getString(PARAM_TITLE);
            String subtitle = arguments.getString(PARAM_SUBTITLE);
            String message = arguments.getString(PARAM_MESSAGE);
            //################################################
            String colorMessage = arguments.getString(PARAM_COLOR_MESSAGE);
            //################################################
            String messageName = arguments.getString(PARAM_MESSAGE_NAME);
            String messageEmphasis = arguments.getString(PARAM_MESSAGE_EMPHASIS);
            String messageComment = arguments.getString(PARAM_MESSAGE_COMMENT);
            String button1Text = arguments.getString(PARAM_BUTTON_1);
            String button2Text = arguments.getString(PARAM_BUTTON_2);
            String button3Text = arguments.getString(PARAM_BUTTON_3);

            String facebookTitle = arguments.getString(PARAM_FACEBOOK_TITLE);
            String facebookMessage = arguments.getString(PARAM_FACEBOOK_MESSAGE);
            String facebookMessage2 = arguments.getString(PARAM_FACEBOOK_MESSAGE2);

            Button button1 = (Button) v.findViewById(R.id.button1);
            Button button2 = (Button) v.findViewById(R.id.button2);
            Button button3 = (Button) v.findViewById(R.id.button3);
            button1.setOnClickListener(mOnClickListener);
            button2.setOnClickListener(mOnClickListener);
            button3.setOnClickListener(mOnClickListener);

            boolean cancelOnTouchOutside = arguments.getBoolean(PARAM_CANCEL_ON_TOUCH_OUTSIDE, true);
            dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);

            if (facebookTitle != null) {
                TextView facebookTitleView = (TextView) v.findViewById(R.id.facebookTitle);
                TextView facebookMsgView = (TextView) v.findViewById(R.id.facebookMessage);
                TextView facebookMsg2View = (TextView) v.findViewById(R.id.facebookMessage2);

                facebookTitleView.setText(facebookTitle);
                facebookMsgView.setText(facebookMessage);
                facebookMsgView.setText(facebookMessage2);

                facebookTitleView.setVisibility(View.VISIBLE);
                facebookMsgView.setVisibility(View.VISIBLE);
                facebookMsg2View.setVisibility(View.VISIBLE);
            } else {
                if (title == null) {
                    v.findViewById(R.id.title_group).setVisibility(View.GONE);
                } else {
                    if (title.length() < 24) {
                        TextView titleView = (TextView) v.findViewById(R.id.title);
                        titleView.setVisibility(View.VISIBLE);
                        titleView.setText(title);
                    } else {
                        TextView longTitleView = (TextView) v.findViewById(R.id.long_title);
                        longTitleView.setVisibility(View.VISIBLE);
                        longTitleView.setText(title);
                    }
                }
                TextView subtitleView = (TextView) v.findViewById(R.id.subtitle);
                TextView messageEmphasisView = (TextView) v.findViewById(R.id.message_emphasis);
                TextView messageNameView = (TextView) v.findViewById(R.id.message_name);
                TextView messageCommentView = (TextView) v.findViewById(R.id.message_comment);

                if (message != null) {
                    Logger.print(TAG, "message: " + message);
                    message = message.replace("\n", "<br />");
                    Logger.print(TAG, "message: " + message);
                    ((TextView) v.findViewById(R.id.message)).setText(Html.fromHtml(message));
                } else if (colorMessage != null) {
                    Logger.print(TAG, "colorMessage : " + colorMessage);
                    ((TextView) v.findViewById(R.id.colorMessage)).setText(colorMessage);
                } else {
                    v.findViewById(R.id.message).setVisibility(View.GONE);
                    v.findViewById(R.id.button_space).setVisibility(View.GONE);
                }

                if (subtitle != null) {
                    subtitleView.setText(subtitle);
                    subtitleView.setVisibility(View.VISIBLE);
                }
                if (messageEmphasis != null) {
                    messageEmphasisView.setText(Html.fromHtml(messageEmphasis));
                    messageEmphasisView.setVisibility(View.VISIBLE);
                }
                if (messageName != null) {
                    messageNameView.setText(Html.fromHtml(messageName));
                    messageNameView.setVisibility(View.VISIBLE);
                }
                if (messageComment != null) {
                    messageCommentView.setText(Html.fromHtml(messageComment));
                    messageCommentView.setVisibility(View.VISIBLE);
                }
            }

            if (button1Text != null) {
                button1.setText(button1Text);
                button1.setVisibility(View.VISIBLE);
            }

            if (button2Text != null) {
                button2.setText(button2Text);
                button2.setVisibility(View.VISIBLE);
            }

            if (button3Text != null) {
                button3.setText(button3Text);
                button3.setVisibility(View.VISIBLE);
            }
        }
        return dialog;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        // Make the dialog non-focusable before showing it
        dialog.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        showImmersive(manager);
    }

    public void showDialog(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        int result = super.show(transaction, tag);
        showImmersive(getFragmentManager());
        return result;
    }

    private void showImmersive(FragmentManager manager) {
        // It is necessary to call executePendingTransactions() on the FragmentManager
        // before hiding the navigation bar, because otherwise getWindow() would raise a
        // NullPointerException since the window was not yet created.
        manager.executePendingTransactions();

        // Copy flags from the activity, assuming it's fullscreen.
        // It is important to do this after show() was called. If we would do this in onCreateDialog(),
        // we would get a requestFeature() error.
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                getActivity().getWindow().getDecorView().getSystemUiVisibility()
        );

        // Make the dialogs window focusable again
        getDialog().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        );
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) mOnDismissListener.onDismiss(dialog);

        final Activity activity = getActivity();
        if (activity != null && activity instanceof GlobalActivity) {
            ((GlobalActivity) activity).onDismisDialog();
        }
    }
}
