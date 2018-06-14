package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;

public class ProgramPurchaseDialog extends DialogFragment {
    public static final String CLASS_NAME = ProgramPurchaseDialog.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";


    private static final String TAG = ProgramPurchaseDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_purchase_simple);
        View v = dialog.getWindow().getDecorView();

        TextView titleView = (TextView) v.findViewById(R.id.title);
        TextView message1 = (TextView) v.findViewById(R.id.message_1);
        TextView message2 = (TextView) v.findViewById(R.id.message_2);
        TextView message3 = (TextView) v.findViewById(R.id.message_3);
        TextView purchasePointDetail = (TextView) v.findViewById(R.id.purchase_point_detail);
        TextView purchaseNormalDetail = (TextView) v.findViewById(R.id.purchase_normal_detail);
        TextView hotLineView = (TextView) v.findViewById(R.id.hot_line);
        v.findViewById(R.id.cancel_button).setOnClickListener(mOnClickListener);
        View purchasePointButton = v.findViewById(R.id.purchase_point_button);
        View purchaseNormalButton = v.findViewById(R.id.purchase_normal_button);
        purchasePointButton.setOnClickListener(mOnClickListener);
        purchaseNormalButton.setOnClickListener(mOnClickListener);

        //###########################################################################
        LinearLayout layoutDescriptionPackage = (LinearLayout) v.findViewById(R.id.layoutDescriptionPackage);
        // TextView txtDescriptionPackageTitle = (TextView)v.findViewById(R.id.txtDescriptionPackageTitle);
        TextView txtDescriptionPackageMsg = (TextView) v.findViewById(R.id.txtDescriptionPackageMsg);

        LinearLayout layoutSinglePriceTitle = (LinearLayout) v.findViewById(R.id.layoutSinglePriceTitle);
        TextView txtPackagePriceTitle = (TextView) v.findViewById(R.id.txtPackagePriceTitle);
        //###########################################################################

        Bundle arguments = getArguments();
        if (arguments != null) {
            Product product = arguments.getParcelable(Product.class.getName());
            Program program = arguments.getParcelable(Program.class.getName());
            boolean isPointUser = arguments.getBoolean(PARAM_IS_POINT_USER);
            RentalPeriods rentalPeriod = arguments.getParcelable(RentalPeriods.class.getName());
            Logger.d(TAG, "onCreateDialog isPointUser:" + isPointUser);
            int pointPrice = product.getFinalPrice(Product.CURRENCY_PNT);
            int normalPrice = product.getFinalPrice(Product.CURRENCY_VND);
            int detailResId;
            int rentalPeriodNumber = 0;
            layoutDescriptionPackage.setVisibility(View.GONE);

            txtPackagePriceTitle.setVisibility(View.GONE);
            layoutSinglePriceTitle.setVisibility(View.VISIBLE);
            if ("subscription".equals(product.getType())) {
                Logger.d(TAG, "onCreateDialog product type subscription");
                titleView.setText(product.getName());
                message1.setText(R.string.purchase_subcription_message_1);
                message2.setText(R.string.purchase_subcription_message_2);

                if (rentalPeriod == null) {
                    detailResId = R.string.purchase_vnd_month;
                } else {
                    detailResId = R.string.purchase_vnd_day;
                    rentalPeriodNumber = rentalPeriod.getPeriod();
//                    normalPrice = rentalPeriod.getPrice(Product.CURRENCY_VND);
                    normalPrice = product.getFinalPrice(Product.CURRENCY_VND);
                }
                //TODO [문구수정] 16.01.12 ################################################################
                if (product.getDescription() != null) {
                    layoutDescriptionPackage.setVisibility(View.VISIBLE);
                    txtDescriptionPackageMsg.setText(product.getName() + " : " + product.getDescription());
                }
                txtPackagePriceTitle.setVisibility(View.VISIBLE);
                layoutSinglePriceTitle.setVisibility(View.GONE);
                //######################################################################
            } else if ("package".equals(product.getType())) {
                Logger.d(TAG, "onCreateDialog product type package");
                rentalPeriodNumber = product.getRental_period() / 86400;
                titleView.setText(product.getName());
                message1.setText(getString(R.string.purchase_package_message_1, rentalPeriodNumber));
                message2.setText(getString(R.string.purchase_package_message_2, rentalPeriodNumber));
                detailResId = R.string.purchase_vnd_day;

            } else {//single
                Logger.d(TAG, "onCreateDialog product type other");
                rentalPeriodNumber = product.getRental_period() / 3600;
                titleView.setText(program.getTitle(WindmillConfiguration.LANGUAGE));
                message1.setText(getString(R.string.purchase_single_message, rentalPeriodNumber));
                message2.setVisibility(View.GONE);
                detailResId = R.string.purchase_vnd_hour;
            }
            String pointPriceString = pointPrice == 0 ? getString(R.string.lock_free) : getString(detailResId, TextUtils.getNumberString(pointPrice), rentalPeriodNumber);
            String normalPriceString = normalPrice == 0 ? getString(R.string.lock_free) : getString(detailResId, TextUtils.getNumberString(normalPrice), rentalPeriodNumber);
            purchasePointDetail.setText(pointPriceString);
            purchaseNormalDetail.setText(normalPriceString);
            message3.setText(isPointUser ? R.string.purchase_flexiplus_user_message : R.string.purchase_flexi_user_message);
            if (isPointUser && !(pointPrice < 0)) {//flexi+ user && pointPrice != 0
                Logger.d(TAG, "flexi+ user");
                //message3.setText(R.string.purchase_flexiplus_user_message);
                purchaseNormalButton.setEnabled(false);
                //#################################################
                purchasePointButton.setVisibility(View.VISIBLE);
                purchaseNormalButton.setVisibility(View.GONE);
                //#################################################
            } else {//flexi user
                Logger.d(TAG, "flexi user");
                //message3.setText(R.string.purchase_flexi_user_message);
                //#################################################
                purchasePointButton.setVisibility(View.GONE);
                purchaseNormalButton.setVisibility(View.VISIBLE);
                //#################################################
            }
            hotLineView.setVisibility(View.GONE);
        }
        return dialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
