package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.TimeManager;
import com.alticast.viettelottcommons.util.Logger;


public class WarningLimitDialog extends DialogFragment implements OnCheckedChangeListener {
	public static final String CLASS_NAME = WarningLimitDialog.class.getName();

	public static final String PARAM_MESSAGE = CLASS_NAME + ".PARAM_MESSAGE";
	private final String priceMark = "#";

	private static final String TAG = WarningLimitDialog.class.getSimpleName();

	private OnClickListener mOnClickListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Logger.d(TAG, "onCreateDialog");

		Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_warning_limit);
		View v = dialog.getWindow().getDecorView();
		
		boolean state = HandheldAuthorization.getInstance().getWaringState(TimeManager.getInstance().getMonth());
		Bundle arguments = getArguments();
		if (arguments != null) {
			String message = arguments.getString(PARAM_MESSAGE);
			TextView msg = (TextView) v.findViewById(R.id.message);

			initLimitMsg(msg, message);
			v.findViewById(R.id.btnPurchase).setOnClickListener(mOnClickListener);
			v.findViewById(R.id.btnCancel).setOnClickListener(mOnClickListener);

			CheckBox againCheck = (CheckBox) v.findViewById(R.id.again_checkbox);
			againCheck.setOnCheckedChangeListener(this);
			againCheck.setChecked(state);
		}
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

	public void setOnClickListener(OnClickListener onClickListener) {
		mOnClickListener = onClickListener;
	}

	private void initLimitMsg(TextView view, String msg) {
		String viewMsg = getString(R.string.warningDes);

		viewMsg = viewMsg.replace(priceMark, msg);

		view.setText(viewMsg);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		HandheldAuthorization.getInstance().setWarningState(isChecked, TimeManager.getInstance().getMonth());
	}
}
