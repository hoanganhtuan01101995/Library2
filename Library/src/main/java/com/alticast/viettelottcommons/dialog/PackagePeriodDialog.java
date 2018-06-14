package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
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
import com.alticast.viettelottcommons.resource.MyDeviceAccount;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;

import java.util.ArrayList;

/**
 * Created by user on 2016-07-01.
 */
public class PackagePeriodDialog extends DialogFragment {
    private static final String TAG = PackagePeriodDialog.class.getSimpleName();
    public static final String CLASS_NAME = PackageOptionDialog.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_PRODUCT = "PARAM_PRODUCT";

    private View.OnClickListener mOnClickListener = null;
    //  private AdapterView.OnItemClickListener mOnItemClickListener = null;

//    private ListViewMaxHeight listPackagePeriod = null;
    private ListView listPackagePeriod = null;
    private Adapter mAdapter;
    private boolean isPointUser = false;
    private Product product;

    private CheckBox checkAutoRenewal = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);
//
//        Window window = dialog.getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        dialog.setContentView(R.layout.dialog_package_period);
        View v = dialog.getWindow().getDecorView();

        Bundle arguments = getArguments();
        if (arguments != null) {
            isPointUser = arguments.getBoolean(PARAM_IS_POINT_USER);
            product = (Product) arguments.getParcelable(PARAM_PRODUCT);

            TextView title = (TextView) v.findViewById(R.id.title);
            TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);
            Button btnPurchase = (Button) v.findViewById(R.id.btnPurchase);
            Button btnCancel = (Button) v.findViewById(R.id.btnCancel);
            checkAutoRenewal = (CheckBox) v.findViewById(R.id.checkAutoRenewal);

            title.setText(product.getName());
            txtDescription.setText(product.getDescription());
            txtDescription.setMovementMethod(new ScrollingMovementMethod());

            ArrayList<RentalPeriods> rentalPeriods = product.getRental_periods();
//            rentalPeriods.addAll(product.getRental_periods());
//            rentalPeriods.addAll(product.getRental_periods());
//            rentalPeriods.addAll(product.getRental_periods());

            Logger.d(TAG, "rentalPeriods length " + (rentalPeriods != null ? rentalPeriods.size() : "null"));
            listPackagePeriod = (ListView) v.findViewById(R.id.listPeriod);
            mAdapter = new Adapter(getActivity().getLayoutInflater());
            listPackagePeriod.setAdapter(mAdapter);
            listPackagePeriod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Logger.d(TAG, "called onItemClick()-position : " + position);
                    mAdapter.setCheckedIndex(position);
                }
            });
            mAdapter.setCheckedIndex(0);

            btnPurchase.setOnClickListener(mOnClickListener);
            btnCancel.setOnClickListener(mOnClickListener);

            mAdapter.setList(rentalPeriods.toArray(new RentalPeriods[rentalPeriods.size()]));
            mAdapter.notifyDataSetChanged();
        }


        return dialog;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

//    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
//        mOnItemClickListener = this;
//    }

    public class Adapter extends SimpleListAdapter<RentalPeriods> {
        public Adapter(LayoutInflater inflater) {
            super(inflater);
        }

        private int mCheckedIndex = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder holder;
            if (convertView == null) {
                v = getLayoutInflater().inflate(R.layout.item_package_period, parent, false);
                holder = new ViewHolder();
                holder.txtRentalPeriod = (TextView) v.findViewById(R.id.txtRentalPeriod);
                holder.txtPrice = (TextView) v.findViewById(R.id.txtPrice);
                holder.checkBox = (CheckBox) v.findViewById(R.id.checkId);
                holder.txtUnit = (TextView) v.findViewById(R.id.txtUnit);
                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }

            RentalPeriods rentalPeriod = getItem(position);
            if (rentalPeriod != null) {
//                if (rentalPeriod.getPeriod() == 1) {
//                    holder.txtRentalPeriod.setText(getString(R.string.periodOndDay));
//                    holder.txtUnit.setText(getString(R.string.pay_unit_day));
//                } else {
//                    holder.txtRentalPeriod.setText(getString(R.string.periodDays, rentalPeriod.getPeriod()));
//                    holder.txtUnit.setText(getString(R.string.pay_unit_days, rentalPeriod.getPeriod()));
//                }

                switch (rentalPeriod.getPeriod()) {
                    case 0:
                        holder.txtRentalPeriod.setVisibility(View.GONE);
                        break;
                    case 1:
                        holder.txtRentalPeriod.setText(getString(R.string.buyDayPackage));
                        break;
                    case 7:
                        holder.txtRentalPeriod.setText(getString(R.string.buyWeekPackage));
                        break;
                    case 30:
                        holder.txtRentalPeriod.setText(getString(R.string.buyMonthlyPackage));
                        break;
                    default:
                        int month = rentalPeriod.getPeriod() / 30;
                        if(month == 0) {
                            holder.txtRentalPeriod.setText(getString(R.string.buyPackage) + " " + rentalPeriod + " " + getString(R.string.justDay));
                        }else {
                            holder.txtRentalPeriod.setText(getString(R.string.buyPackage) + " " + month + " " + getString(R.string.justMothn));
                        }

                }

//                String price = TextUtils.getNumberString(rentalPeriod.getPrice(Product.CURRENCY_VND).getValue());//getString(R.string.priceOnlyOneDay, TextUtils.getNumberString(rentalPeriod.getPrice(Product.CURRENCY_VND)));
                String price = TextUtils.getNumberString(product.getFpackageFinalPrice(rentalPeriod, Product.CURRENCY_VND));//getString(R.string.priceOnlyOneDay, TextUtils.getNumberString(rentalPeriod.getPrice(Product.CURRENCY_VND)));
                Logger.d(TAG, "price  : " + price);
                holder.txtPrice.setText(price);
            }

            Logger.d(TAG, "position : " + position + " ,mCheckedIndex : " + mCheckedIndex);
            if (position == mCheckedIndex) {
                v.setSelected(true);
                holder.checkBox.setChecked(true);
            } else {
                v.setSelected(false);
                holder.checkBox.setChecked(false);
            }
            return v;
        }

        public void setCheckedIndex(int position) {
            mCheckedIndex = position;
            notifyDataSetChanged();
        }

        public int getCheckedIndex() {
            Logger.d(TAG, "called getCheckedIndex() - mCheckedIndex : " + mCheckedIndex);
            return mCheckedIndex;
        }

    }

    private static class ViewHolder {
        TextView txtRentalPeriod = null;
        TextView txtPrice = null;
        CheckBox checkBox = null;
        TextView txtUnit = null;
    }


    public String setPrice(Product product) {
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
        if (isPointUser && !(pointPrice < 0)) {//flexi+ user && pointPrice != 0
            Logger.d(TAG, "flexi+ user");
            //message3.setText(R.string.purchase_flexiplus_user_message);
            return pointPriceString;
        } else {//flexi user
            Logger.d(TAG, "flexi user");
            return normalPriceString;
        }
    }

    public RentalPeriods getSelectedRentalPeriod() {
        return mAdapter.getItem(mAdapter.getCheckedIndex());
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity != null && activity instanceof GlobalActivity) {
            ((GlobalActivity) activity).onDismisDialog();
        }

    }

    public boolean selectedAutoRenewal() {
        Logger.d(TAG, "called selectedAutoRenewal() - isChecked :" + checkAutoRenewal.isChecked());
//        return !checkAutoRenewal.isChecked();
        return true;
    }
}
