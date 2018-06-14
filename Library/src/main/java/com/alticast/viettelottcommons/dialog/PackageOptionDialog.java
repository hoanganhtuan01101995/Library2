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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.adapter.SimpleListAdapter;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.widget.ListViewMaxHeight;

/**
 * Created by user on 2016-05-17.
 */
public class PackageOptionDialog extends DialogFragment {
    private static final String TAG = PackageOptionDialog.class.getSimpleName();
    public static final String CLASS_NAME = PackageOptionDialog.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_PACKAGES = "PARAM_PACKAGES";

    private View.OnClickListener mOnClickListener = null;
    private AdapterView.OnItemClickListener mOnItemClickListener = null;

    private ListViewMaxHeight listPackageOption = null;
    private Adapter mAdapter;
    private  boolean isPointUser = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_package_option);
        dialog.setCanceledOnTouchOutside(false);

//        Window window = dialog.getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);


        View v = dialog.getWindow().getDecorView();

        TextView txtUserType = (TextView)v.findViewById(R.id.txtUserType);
        v.findViewById(R.id.cancel_button).setOnClickListener(mOnClickListener);

        listPackageOption = (ListViewMaxHeight)v.findViewById(R.id.listPackageOption);
        mAdapter =  new Adapter(getActivity().getLayoutInflater());
        listPackageOption.setAdapter(mAdapter);
        listPackageOption.setOnItemClickListener(mOnItemClickListener);
        Bundle arguments = getArguments();
        if (arguments != null) {
            isPointUser = arguments.getBoolean(PARAM_IS_POINT_USER);
            Product[] products = (Product [] )arguments.getParcelableArray(PARAM_PACKAGES);

            Logger.d(TAG, "products length " + (products != null ? products.length : "null"));
            mAdapter.setList(products);
            mAdapter.notifyDataSetChanged();

            if(AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
                txtUserType.setVisibility(View.VISIBLE);
                txtUserType.setText(isPointUser ? R.string.purchase_flexiplus_user_message : R.string.purchase_flexi_user_message);//TODO 페어링 user인 경우,
            }else{
                txtUserType.setVisibility(View.GONE);
            }

        }
        return dialog;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    public class Adapter extends SimpleListAdapter<Product> {
        public Adapter(LayoutInflater inflater) {
            super(inflater);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder holder;
            if (convertView == null) {
                v = getLayoutInflater().inflate(R.layout.item_package_option, parent, false);
                holder = new ViewHolder();
                holder.layoutPackageOptionBtn = (LinearLayout) v.findViewById(R.id.layoutPackageOptionBtn);
                holder.layoutPackagePrice = (LinearLayout)v.findViewById(R.id.layoutPackagePrice);
                holder.txtDescription = (TextView) v.findViewById(R.id.txtDescription);
                holder.txtPackageName = (TextView) v.findViewById(R.id.txtPackageName);
                holder.txtPrice = (TextView) v.findViewById(R.id.txtPrice);
                holder.layoutSinglePriceTitle = (LinearLayout)v.findViewById(R.id.layoutSinglePriceTitle);
                holder.txtPackagePriceTitle = (TextView)v.findViewById(R.id.txtPackagePriceTitle);
                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }

            Product product = getItem(position);
            if (product != null) {
                holder.txtPackageName.setText(product.getName());
                if(product.getShortDescription() == null || product.getShortDescription().equals("")){
                    holder.txtDescription.setVisibility(View.GONE);
                }else {
                    holder.txtDescription.setVisibility(View.VISIBLE);
                    holder.txtDescription.setText(product.getShortDescription());
                }

                if (product.isRentalPeriodProduct()) {
                    holder.layoutPackagePrice.setVisibility(View.GONE);
                }else {
                    holder.layoutPackagePrice.setVisibility(View.VISIBLE);
                    holder.txtPrice.setText(setPrice(product));
                    holder.layoutSinglePriceTitle.setVisibility(isPointUser ? View.VISIBLE : View.GONE);
                    holder.txtPackagePriceTitle.setVisibility(isPointUser ? View.GONE : View.VISIBLE);
                    holder.txtPackagePriceTitle.setText(product.isBundleProduct() ? R.string.bundlePrice : R.string.packagePrice);
                }
            }
            return v;
        }

    }

    private static class ViewHolder {
        LinearLayout layoutPackageOptionBtn = null;
        LinearLayout layoutPackagePrice = null;
        TextView txtDescription = null;
        TextView txtPackageName = null;
        TextView txtPrice = null;
        LinearLayout layoutSinglePriceTitle = null;
        TextView txtPackagePriceTitle = null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity != null && activity instanceof GlobalActivity) {
            ((GlobalActivity) activity).onDismisDialog();
        }

    }


    public String setPrice(Product product){
        int pointPrice = -1;
        int normalPrice = -1;
        if(product.isFpackage()){
            RentalPeriods rentalPeriod =product.getOneMonthRentalInfo();
            if(rentalPeriod != null)
//                normalPrice = (int)(rentalPeriod.getPrice(Product.CURRENCY_VND).getValue());
                normalPrice = product.getFpackageFinalPrice(rentalPeriod, Product.CURRENCY_VND);
        }else{
            pointPrice = product.getFinalPrice(Product.CURRENCY_PNT);
            normalPrice = product.getFinalPrice(Product.CURRENCY_VND);
        }

        String pointPriceString = pointPrice == 0 ? getString(R.string.lock_free) : getString(R.string.purchase_vnd_month, TextUtils.getNumberString(pointPrice));
        String normalPriceString = normalPrice == 0 ? getString(R.string.lock_free) : getString(R.string.purchase_vnd_month, TextUtils.getNumberString(normalPrice));
        if (isPointUser &&!(pointPrice< 0)) {//flexi+ user && pointPrice != 0
            Logger.d(TAG, "flexi+ user");
            //message3.setText(R.string.purchase_flexiplus_user_message);
            return pointPriceString;
        } else {//flexi user
            Logger.d(TAG, "flexi user");
            return normalPriceString;
        }
    }

}
