package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
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

public class ProgramPurchaseConfirmDialog extends DialogFragment {
    public static final String CLASS_NAME = ProgramPurchaseConfirmDialog.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_IS_ALTERNATIVE_PRODUCT = "PARAM_IS_ALTERNATIVE_PRODUCT";


    private static final String TAG = ProgramPurchaseConfirmDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_purchase_confirm_program);
        View v = dialog.getWindow().getDecorView();

        TextView productName = (TextView) v.findViewById(R.id.product_name);
        TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        TextView messageBundle = (TextView) v.findViewById(R.id.message_bundle);
        View pointIcon = v.findViewById(R.id.point_icon);
        TextView priceInfo = (TextView) v.findViewById(R.id.price_info);
        TextView hotLineView = (TextView) v.findViewById(R.id.hot_line);
        v.findViewById(R.id.purchase_button).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.cancel_button).setOnClickListener(mOnClickListener);

        LinearLayout layoutSubscriptionMsg =(LinearLayout)v.findViewById(R.id.layoutSubscriptionMsg);
        TextView txtDescriptionPackageMsg = (TextView)v.findViewById(R.id.txtDescriptionPackageMsg);

        Bundle arguments = getArguments();
        if (arguments != null) {
            Product product = arguments.getParcelable(Product.class.getName());
            Program program = arguments.getParcelable(Program.class.getName());
            RentalPeriods rentalPeriod = arguments.getParcelable(RentalPeriods.class.getName());
            boolean isPointUser = arguments.getBoolean(PARAM_IS_POINT_USER);
            boolean isAlternativeProduct = arguments.getBoolean(PARAM_IS_ALTERNATIVE_PRODUCT);

            int purchaseType = arguments.getInt(Product.TYPE_PURCHASE, -1);

            int pointPrice = product.getFinalPrice(Product.CURRENCY_PNT);
            int normalPrice = product.getFinalPrice(Product.CURRENCY_VND);

            int priceSuffixResId;
            int rentalPeriodNumber = 0;
            layoutSubscriptionMsg.setVisibility(View.GONE);
            txtDescriptionPackageMsg.setVisibility(View.GONE);

            if ("single".equals(product.getType())) {
                productName.setText(program != null ? program.getTitle(WindmillConfiguration.LANGUAGE) : product.getName());
                priceSuffixResId = R.string.purchase_vnd_hour;
                rentalPeriodNumber = product.getRental_period() / 3600;
            } else {
                productName.setText(product.getName());
                messageUser.setVisibility(View.GONE);
                if ("subscription".equals(product.getType())) {
                    if (rentalPeriod == null) {
                        priceSuffixResId = R.string.purchase_vnd_month;
                    } else {
                        priceSuffixResId = R.string.purchase_vnd_day;
                        rentalPeriodNumber = rentalPeriod.getPeriod();
//                        normalPrice = rentalPeriod.getPrice(Product.CURRENCY_VND);
                        normalPrice = product.getFinalPrice(Product.CURRENCY_VND);
                    }

                    layoutSubscriptionMsg.setVisibility(View.VISIBLE);
                    if(product.getDescription()!=null) {
                        txtDescriptionPackageMsg.setVisibility(View.VISIBLE);
                        txtDescriptionPackageMsg.setText(product.getName()+" : "+product.getDescription());
                    }
                } else {
                    priceSuffixResId = R.string.purchase_vnd_day;
                    rentalPeriodNumber = product.getRental_period() / 86400;
                }
            }

            if (isAlternativeProduct) {
                String message;
                if ("subscription".equals(product.getType())) {
                    message = getString(R.string.purchase_subcription_message_2);
                } else {
                    message = getString(R.string.purchase_package_message_2, product.getRental_period() / 86400);
                }
                messageBundle.setText(message);
                messageUser.setVisibility(View.GONE);
                pointIcon.setVisibility(View.GONE);
                priceInfo.setVisibility(View.GONE);
            } else {
                messageBundle.setVisibility(View.GONE);
                messageUser.setVisibility(View.GONE);
                messageUser.setText(isPointUser ? R.string.purchase_flexiplus_user_message : R.string.purchase_flexi_user_message);

                if (purchaseType == Product.TYPE_POINT) {
                    String priceString = pointPrice == 0 ? getString(R.string.lock_free) : getString(priceSuffixResId, TextUtils.getNumberString(pointPrice), rentalPeriodNumber);
                    String priceInfoString = getString(R.string.purchase_point) + "<font color=\"#ffcc05\">" + priceString + "</font>";
                    priceInfo.setText(Html.fromHtml(priceInfoString));
                } else if (purchaseType == Product.TYPE_PRICE) {
                    pointIcon.setVisibility(View.GONE);
                    String priceString = normalPrice == 0 ? getString(R.string.lock_free) : getString(priceSuffixResId, TextUtils.getNumberString(normalPrice), rentalPeriodNumber);                    String priceInfoString = getString(R.string.purchase_normal_price) + "<font color=\"#ffcc05\">" + priceString + "</font>";
                    priceInfo.setText(Html.fromHtml(priceInfoString));
                } else {
                    if (isPointUser && !(pointPrice < 0)) {
                        //messageUser.setText(R.string.purchase_flexiplus_user_message);
                        String priceString = pointPrice == 0 ? getString(R.string.lock_free) : getString(priceSuffixResId, TextUtils.getNumberString(pointPrice), rentalPeriodNumber);
                        String priceInfoString = getString(R.string.purchase_point) + "<font color=\"#ffcc05\">" + priceString + "</font>";
                        priceInfo.setText(Html.fromHtml(priceInfoString));
                    } else {
                        //messageUser.setText(R.string.purchase_flexi_user_message);
                        pointIcon.setVisibility(View.GONE);
                        String priceString = pointPrice == 0 ? getString(R.string.lock_free) : getString(priceSuffixResId, TextUtils.getNumberString(normalPrice), rentalPeriodNumber);
                        String priceInfoString = getString(R.string.purchase_normal_price) + "<font color=\"#ffcc05\">" + priceString + "</font>";
                        priceInfo.setText(Html.fromHtml(priceInfoString));
                    }
                }
            }

            hotLineView.setVisibility(View.GONE);

        }
        return dialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
