package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
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
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.StringUtil;
import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.util.TimeUtil;
import com.alticast.viettelottcommons.widget.FontCheckBox;

import retrofit2.Call;


public class PurchaseDetailWalletDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    public static final String CLASS_NAME = PurchaseDetailWalletDialog.class.getName();

    public static final String PARAM_PURCHASE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_PROGRAM = CLASS_NAME + ".PARAM_PURCHASED_DATE";

    private static final String TAG = PurchaseDetailWalletDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;
    //TODO [확인] pre-payment
    private static final String FORMAT_DATE = "dd/MM/yyyy";

//    private FontButtonView stop_subscription_button = null;
    private ProgressDialogFragment mProgressDialogFragment;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private boolean isStateChagnegd = false;
    private Purchase mPurchase = null;
    private Product mProduct = null;
    private boolean isBasicPackage;
    private boolean isShowingDetail;

    public void setSrc(Purchase purchase,Product mProduct, boolean isBasicPackage, boolean isShowingDetail){
        this.mPurchase = purchase;
        this.mProduct = mProduct;
        this.isBasicPackage = isBasicPackage;
        this.isShowingDetail = isShowingDetail;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_purchase_detail_wallet);
        View v = dialog.getWindow().getDecorView();

        TextView title = (TextView) v.findViewById(R.id.title);
        TextView originalPrice = (TextView) v.findViewById(R.id.originalPrice);
        TextView duration = (TextView) v.findViewById(R.id.duration);
        TextView startTime = (TextView) v.findViewById(R.id.startTime);
        TextView status = (TextView) v.findViewById(R.id.status);
        TextView txtCancelGuide = (TextView) v.findViewById(R.id.txtCancelGuide);



        isStateChagnegd = false;
        mProgressDialogFragment = new ProgressDialogFragment();
        if (mPurchase != null) {

            title.setText(mPurchase.getProductName());

            if (mPurchase.getPaymentValue() == 0 ) {
                originalPrice.setText(getString(R.string.lock_free));
                (v.findViewById(R.id.layoutDuration)).setVisibility(View.GONE);
            } else {
                originalPrice.setText(TextUtils.getNumberString(mPurchase.getPaymentValue()) + " VNĐ");
                int rentalPeriod = TimeUtil.secToDay(mPurchase.getRentalPeriod());
                duration.setText(StringUtil.getPeriodTimeString(getContext(), rentalPeriod));
            }
            startTime.setText(TextUtils.getDateString(mPurchase.getPurchasedDate(), FORMAT_DATE).toUpperCase());

            int cclass = mPurchase.getCClass();

            boolean isCancelable = mPurchase.isCanCelable();

            if(isCancelable) {
                Button button = (Button) v.findViewById(R.id.btnCancelSubscription);
                if(mPurchase.isCanceled()) {
                    button.setText(getString(R.string.changeAutoRenewPurchaseDetail));
                    status.setText(getString(R.string.status_cancel));
                    txtCancelGuide.setVisibility(View.GONE);
                } else {
                    button.setText(getString(R.string.cancelSubscription));
                    status.setText(getString(R.string.status_subcribe));

                    txtCancelGuide.setVisibility(View.VISIBLE);

                    String guide1 = getContext().getString(R.string.cancelPackageGuide1);
                    String fullGuide = getContext().getString(R.string.cancelPackageGuide);

                    SpannableString ss = new SpannableString(fullGuide);
                    Drawable d = getResources().getDrawable(R.drawable.icon_menu);
                    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                    ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                    ss.setSpan(span, guide1.length() + 1, guide1.length() + "imagePlace".length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                    txtCancelGuide.setText(ss);

                    txtCancelGuide.setLineSpacing(1, 1.2f);
                }

                button.setOnClickListener(mOnClickListener);
            } else {
                txtCancelGuide.setVisibility(View.GONE);
                v.findViewById(R.id.btnCancelSubscription).setVisibility(View.GONE);
                v.findViewById(R.id.layoutStatus).setVisibility(View.GONE);
                if(mPurchase.getProduct().getType().equals(Product.TYPE_FPACKAGE)) {
                    originalPrice.setText(getString(R.string.lock_free));
                    (v.findViewById(R.id.layoutDuration)).setVisibility(View.GONE);
                }
            }

            v.findViewById(R.id.btnChangeChargePeriod).setVisibility(View.GONE);
//            if(mProduct != null && mProduct.isPurchaseable()
//                    && mProduct.getRental_periods() != null
//                    && mProduct.getRental_periods().size() > 1 && !isBasicPackage) {
//                v.findViewById(R.id.btnChangeChargePeriod).setVisibility(View.VISIBLE);
//                v.findViewById(R.id.btnChangeChargePeriod).setOnClickListener(mOnClickListener);
//            }

            if(isShowingDetail) {
                v.findViewById(R.id.btnChangeChargePeriod).setVisibility(View.GONE);
                v.findViewById(R.id.btnCancelSubscription).setVisibility(View.GONE);
            } else {
                txtCancelGuide.setVisibility(View.GONE);
            }

            v.findViewById(R.id.btnClose).setOnClickListener(mOnClickListener);
        }
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
