package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.PurchaseLoader;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Purchase;
import com.alticast.viettelottcommons.resource.RentalPackageObject;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.util.TimeUtil;
import com.alticast.viettelottcommons.widget.FontCheckBox;

import retrofit2.Call;


public class SubcribedBasicDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    public static final String CLASS_NAME = SubcribedBasicDialog.class.getName();

    public static final String PARAM_PURCHASE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_PROGRAM = CLASS_NAME + ".PARAM_PURCHASED_DATE";

    private static final String TAG = SubcribedBasicDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    //TODO [확인] pre-payment
    private static final String FORMAT_DATE = "yyyy.MM.dd";

    private ProgressDialogFragment mProgressDialogFragment;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private boolean isStateChagnegd = false;
    private RentalPeriods rentalPackageObject = null;
    private Product mProduct = null;

    public void setSrc(RentalPeriods rentalPackageObject, Product product){
        this.rentalPackageObject = rentalPackageObject;
        this.mProduct = product;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_subscribed_basic);
        View v = dialog.getWindow().getDecorView();

        TextView txtPrice = (TextView) v.findViewById(R.id.txtPrice);
        TextView txtPeriod = (TextView) v.findViewById(R.id.txtPeriod);
        TextView available_date = (TextView) v.findViewById(R.id.available_date);

        txtPrice.setText(TextUtils.getNumberString(mProduct.getFpackageFinalPrice(rentalPackageObject, Product.CURRENCY_VND)) + " VNĐ");
        txtPeriod.setText("" + rentalPackageObject.getDurationString(getContext()));
//        available_date.setText("" + TextUtils.getDateString(rentalPackageObject.getSubscription_star(), FORMAT_DATE).toUpperCase());

        v.findViewById(R.id.stop_subscription_button).setOnClickListener(mOnClickListener);
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
