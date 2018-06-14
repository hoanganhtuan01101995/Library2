package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.adapter.SimpleListAdapter;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;

public class NotViettelSubWarningDialog extends DialogFragment {
    private static final String TAG = NotViettelSubWarningDialog.class.getSimpleName();
    public static final String CLASS_NAME = NotViettelSubWarningDialog.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_PACKAGES = "PARAM_PACKAGES";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_not_viettel_sub_warning);
        View v = dialog.getWindow().getDecorView();
        v.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        return dialog;
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
