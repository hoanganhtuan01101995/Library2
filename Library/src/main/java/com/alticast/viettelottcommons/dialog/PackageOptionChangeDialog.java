package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPackageObject;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;


public class PackageOptionChangeDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    public static final String CLASS_NAME = PackageOptionChangeDialog.class.getName();

    public static final String PARAM_PURCHASE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_PROGRAM = CLASS_NAME + ".PARAM_PURCHASED_DATE";

    private static final String TAG = PackageOptionChangeDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    //TODO [확인] pre-payment
    private static final String FORMAT_DATE = "yyyy.MM.dd";

    private ProgressDialogFragment mProgressDialogFragment;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private boolean isStateChagnegd = false;
    private RentalPeriods rentalCurrentObject = null;
    private RentalPeriods rentalChangeObject = null;
    private Product mProduct = null;

    public void setSrc(RentalPeriods current, RentalPeriods change, Product product){
        this.rentalCurrentObject = current;
        this.rentalChangeObject = change;
        this.mProduct = product;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_subcription_wallet);
        View v = dialog.getWindow().getDecorView();

        LinearLayout layoutCurrentOption = (LinearLayout) v.findViewById(R.id.layoutCurrentOption);
        LinearLayout layoutAfterOption = (LinearLayout) v.findViewById(R.id.layoutAfterOption);

        ((TextView)layoutCurrentOption.findViewById(R.id.txtRenewalTime)).setText(getText(R.string.current));
        ((TextView)layoutAfterOption.findViewById(R.id.txtRenewalTime)).setText(getText(R.string.afterChange));

        TextView txtPriceCurrent = (TextView) layoutCurrentOption.findViewById(R.id.txtPrice);
        TextView txtDurationCurrent = (TextView) layoutCurrentOption.findViewById(R.id.txtDuration);
        txtPriceCurrent.setText(rentalCurrentObject.getPriceString(mProduct));
        txtDurationCurrent.setText(rentalCurrentObject.getDurationString(getContext()));

        TextView txtPriceChange = (TextView) layoutAfterOption.findViewById(R.id.txtPrice);
        TextView txtDurationChange = (TextView) layoutAfterOption.findViewById(R.id.txtDuration);
        txtPriceChange.setText(rentalChangeObject.getPriceString(mProduct));
        txtDurationChange.setText(rentalChangeObject.getDurationString(getContext()));

        layoutAfterOption.findViewById(R.id.layoutContain).setBackgroundResource(R.drawable.drawable_border_wallet_press);
        layoutAfterOption.findViewById(R.id.bottomBg).setBackgroundResource(R.drawable.drawable_border_bottom_wallet_press);
        ((TextView)layoutAfterOption.findViewById(R.id.txtPrice)).setTextColor(getContext().getResources().getColor(R.color.white));
        ((TextView)layoutAfterOption.findViewById(R.id.txtDuration)).setTextColor(getContext().getResources().getColor(R.color.white));
        ((TextView)layoutAfterOption.findViewById(R.id.txtRenewalTime)).setTextColor(getContext().getResources().getColor(R.color.viettelBlue));

        v.findViewById(R.id.change_subscription_button).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.close_button).setOnClickListener(mOnClickListener);

        isStateChagnegd = false;
        mProgressDialogFragment = new ProgressDialogFragment();
        return dialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //TODO AUTO RENEWAL FUNCTION
        mProgressDialogFragment.show(getChildFragmentManager(), ProgressDialogFragment.CLASS_NAME);
    }

//    @Override
//    public void onResult(BaseLoader loader) {
//        mProgressDialogFragment.dismiss();
//        if (loader.getData() == null) {
//
//            checkAutoRenewal.setOnCheckedChangeListener(null);
//            checkAutoRenewal.setChecked(!checkAutoRenewal.isChecked());
//            checkAutoRenewal.setOnCheckedChangeListener(this);
//            showMessageDialog(getString(R.string.notice), loader.getError().getMessage());
//
//            Logger.d("AutomaticRenewalLoader", "result : " + false);
//        } else {
//
//            isStateChagnegd = true;
//            boolean result = ((AutomaticRenewalLoader) loader).getData();
//            Logger.d("AutomaticRenewalLoader", "result : " + result);
//        }
//
//
//    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }


//    private void showMessageDialog(String title, String message) {
//        final MessageDialog dialog = new MessageDialog();
//        Bundle args = new Bundle();
//        args.putString(MessageDialog.PARAM_TITLE, title);
//        args.putString(MessageDialog.PARAM_MESSAGE, message);
//        args.putString(MessageDialog.PARAM_BUTTON_1, getString(R.string.ok));
//        dialog.setArguments(args);
//        dialog.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show(getChildFragmentManager(), MessageDialog.CLASS_NAME);
//    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null && isStateChagnegd) mOnDismissListener.onDismiss(dialog);
    }
}
