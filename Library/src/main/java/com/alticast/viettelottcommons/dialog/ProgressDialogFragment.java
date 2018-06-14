package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alticast.viettelottcommons.R;


public class ProgressDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String CLASS_NAME = ProgressDialogFragment.class.getName();
    private static final String TAG = ProgressDialogFragment.class.getSimpleName();

    private View mProgressView = null;
    private boolean isCancelled;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(getActivity() == null || getActivity().isFinishing()) {
            return null;
        }

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_progress);


        View v = dialog.getWindow().getDecorView();
        mProgressView = v.findViewById(android.R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);

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
//        showImmersive(manager);
        setCancelable(false);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        int result = super.show(transaction, tag);
//        showImmersive(getFragmentManager());
        setCancelable(false);
        return result;
    }

//    private void showImmersive(FragmentManager manager) {
//        // It is necessary to call executePendingTransactions() on the FragmentManager
//        // before hiding the navigation bar, because otherwise getWindow() would raise a
//        // NullPointerException since the window was not yet created.
//        if (Build.VERSION.SDK_INT >= 24) {
//
//        } else {
//
//        }
//        manager.executePendingTransactions();
//
//        // Copy flags from the activity, assuming it's fullscreen.
//        // It is important to do this after show() was called. If we would do this in onCreateDialog(),
//        // we would get a requestFeature() error.
//        if(getDialog() == null) return;
//        getDialog().getWindow().getDecorView().setSystemUiVisibility(
//                getActivity().getWindow().getDecorView().getSystemUiVisibility()
//        );
//
//        // Make the dialogs window focusable again
//        getDialog().getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//        );
//    }

    public boolean isShowing() {
        if (getDialog() != null) {
            return getDialog().isShowing();
        } else {
            return false;
        }
    }

//    @Override
//    public void show(FragmentManager manager, String tag) {
//        super.show(manager, tag);
//        setCancelable(false);
//    }

    @Override
    public void dismiss() {
        if(getDialog() != null && getDialog().isShowing()) {
            super.dismiss();
        }
    }

    public boolean isCancelled() {
        return isCancelable();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        isCancelled = true;
    }

}
