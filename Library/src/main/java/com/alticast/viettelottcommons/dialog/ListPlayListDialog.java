package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.manager.MyContentManager;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;

public class ListPlayListDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = ListPlayListDialog.class.getSimpleName();
    public static final String CLASS_NAME = ListPlayListDialog.class.getName();

    private View.OnClickListener mOnClickListener = null;
    private View viewGeneral, viewKid, viewMusic, viewClip;
    private ArrayList<View> listView = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_list_playlist);
        View v = dialog.getWindow().getDecorView();

        viewGeneral = v.findViewById(R.id.general);
        viewKid = v.findViewById(R.id.kid);
        viewMusic = v.findViewById(R.id.music);
        viewClip = v.findViewById(R.id.clips);
        viewGeneral.setOnClickListener(this);
        viewKid.setOnClickListener(this);
        viewMusic.setOnClickListener(this);
        viewClip.setOnClickListener(this);
        listView.add(viewGeneral);
        listView.add(viewKid);
        listView.add(viewClip);
        listView.add(viewMusic);

        viewGeneral.setSelected(true);
        View checkBox = ((ViewGroup) viewGeneral).getChildAt(0);
        if (checkBox instanceof CheckBox) {
            ((CheckBox) checkBox).setChecked(true);
        }
        MyContentManager.getInstance().myContentType = MyContentManager.GROUP_OTHERS;

        Button btnConfirm = (Button) v.findViewById(R.id.btnConfirm);
        Button btnCancel = (Button) v.findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(mOnClickListener);
        btnCancel.setOnClickListener(mOnClickListener);
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
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        int result = super.show(transaction, tag);
//        showImmersive(getFragmentManager());
        return result;
    }

//    private void showImmersive(FragmentManager manager) {
//        // It is necessary to call executePendingTransactions() on the FragmentManager
//        // before hiding the navigation bar, because otherwise getWindow() would raise a
//        // NullPointerException since the window was not yet created.
//        manager.executePendingTransactions();
//
//        // Copy flags from the activity, assuming it's fullscreen.
//        // It is important to do this after show() was called. If we would do this in onCreateDialog(),
//        // we would get a requestFeature() error.
//        getDialog().getWindow().getDecorView().setSystemUiVisibility(
//                getActivity().getWindow().getDecorView().getSystemUiVisibility()
//        );
//
//        // Make the dialogs window focusable again
//        getDialog().getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//        );
//    }

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.general) {
            MyContentManager.getInstance().myContentType = MyContentManager.GROUP_OTHERS;
        } else if (v.getId() == R.id.music) {
            MyContentManager.getInstance().myContentType = MyContentManager.GROUP_MUSIC;
        } else if (v.getId() == R.id.kid) {
            MyContentManager.getInstance().myContentType = MyContentManager.GROUP_KIDS;
        } else if (v.getId() == R.id.clips) {
            MyContentManager.getInstance().myContentType = MyContentManager.GROUP_CLIP;
        }
        updateSelectedLayout(v.getId());
    }

    private void updateSelectedLayout(int id) {
        for (View view : listView) {
            if (view.getId() != id) {
                view.setSelected(false);
                View checkBox = ((ViewGroup) view).getChildAt(0);
                if (checkBox instanceof CheckBox) {
                    ((CheckBox) checkBox).setChecked(false);
                }
            } else {
                view.setSelected(true);
                View checkBox = ((ViewGroup) view).getChildAt(0);
                if (checkBox instanceof CheckBox) {
                    ((CheckBox) checkBox).setChecked(true);
                }
            }
        }
    }
}
