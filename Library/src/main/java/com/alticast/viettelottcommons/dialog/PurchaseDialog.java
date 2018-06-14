package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;


public class PurchaseDialog extends DialogFragment {
    public static final String CLASS_NAME = PurchaseDialog.class.getName();

    public static final String PARAM_TITLE = CLASS_NAME + ".PARAM_TITLE";
    public static final String PARAM_MESSAGE = CLASS_NAME + ".PARAM_MESSAGE";
    public static final String PARAM_AVAILABLE_DATE = CLASS_NAME + ".PARAM_AVAILABLE_DATE";
    public static final String PARAM_CASH_PRICE = CLASS_NAME + ".PARAM_CASH_PRICE";
    public static final String PARAM_POINT_PRICE = CLASS_NAME + ".PARAM_POINT_PRICE";
    public static final String PARAM_IS_POINT_USER = CLASS_NAME +".PARAM_IS_POINT_USER";

    private static final String TAG = PurchaseDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_purchase);
        View v = dialog.getWindow().getDecorView();

        Bundle arguments = getArguments();
        if (arguments != null) {
            String title = arguments.getString(PARAM_TITLE);
            String message = arguments.getString(PARAM_MESSAGE);
            long availableDate = arguments.getLong(PARAM_AVAILABLE_DATE, -1);
            int cashPrice = arguments.getInt(PARAM_CASH_PRICE);
            int pointPrice = arguments.getInt(PARAM_POINT_PRICE);


            RentalPeriods rentalPeriod = arguments.getParcelable(RentalPeriods.class.getName());

            boolean isPointUser= arguments.getBoolean(PARAM_IS_POINT_USER);

            if (message != null) {
                v.findViewById(R.id.message).setVisibility(View.VISIBLE);
                ((TextView) v.findViewById(R.id.message)).setText(message);
            }
            if (availableDate > -1) {
                v.findViewById(R.id.available_date_views).setVisibility(View.VISIBLE);
                ((TextView) v.findViewById(R.id.available_date)).setText(TextUtils.getDateString(availableDate, "EEE dd/MM").toUpperCase());
            }

//            Bundle platform = ((App) getActivity().getApplication()).getPlatform();
//            boolean cashEnabled = platform != null
//                    && platform.containsKey("flexi.vod.purchase")
//                    && !"false".equals(platform.getString("flexi.vod.purchase"));

            ((TextView) v.findViewById(R.id.title)).setText(title);

            //#####################################################################
            View btnPurchasePrice = v.findViewById(R.id.purchase_cash_button);
            View btnPurchasePoint = v.findViewById(R.id.purchase_point_button);
            btnPurchasePrice.setOnClickListener(mOnClickListener);
            btnPurchasePoint.setOnClickListener(mOnClickListener);

            Logger.d(TAG,"isPointUser : "+isPointUser +" , pointPrice : "+pointPrice+" ,cashPrice :"+cashPrice);
            if(isPointUser && pointPrice != 0){
                btnPurchasePrice.setVisibility(View.GONE);
                btnPurchasePoint.setVisibility(View.VISIBLE);
                ((TextView) v.findViewById(R.id.price_point)).setText(TextUtils.getNumberString(Math.max(0, pointPrice)));
                v.findViewById(R.id.purchase_point_button).setEnabled(pointPrice >= 0);
            }else{
                btnPurchasePrice.setVisibility(View.VISIBLE);
                btnPurchasePoint.setVisibility(View.GONE);

                if(rentalPeriod == null) {
                    ((TextView) v.findViewById(R.id.price_cash)).setText(TextUtils.getNumberString(Math.max(0, cashPrice)));
                }else{
                    ((TextView) v.findViewById(R.id.price_cash)).setText(TextUtils.getNumberString(Math.max(0, rentalPeriod.getPrice(Product.CURRENCY_VND).getValue())));
                }
                v.findViewById(R.id.purchase_cash_button).setEnabled(cashPrice >= 0);
            }
            //#####################################################################
            v.findViewById(R.id.cancel_button).setOnClickListener(mOnClickListener);
        }
        return dialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
