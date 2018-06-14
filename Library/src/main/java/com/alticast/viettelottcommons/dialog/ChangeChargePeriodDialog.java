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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.adapter.SimpleListAdapter;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;

public class ChangeChargePeriodDialog extends DialogFragment {
    private static final String TAG = ChangeChargePeriodDialog.class.getSimpleName();
    public static final String CLASS_NAME = ChangeChargePeriodDialog.class.getName();

    public static final String PARAM_PRODUCT = "PARAM_PRODUCT";
    public static final String DEVICE_ACCOUNT = "DEVICE_ACCOUNT";

    private View.OnClickListener mOnClickListener = null;
    private ListView listUsedDevice = null;
    private Adapter mAdapter;
    private ArrayList<RentalPeriods> rentalPackageObjects;
    private RentalPeriods firstRental;
    private Product mProduct;
    private Integer checkIndex;
//    private DialogInterface.OnDismissListener mOnDismissListener;

    public void setSrc(RentalPeriods firstRental, Product product){
        this.firstRental = firstRental;
        this.rentalPackageObjects = product.getRental_periods();
        this.mProduct = product;
    }

    public RentalPeriods getChoseRental() {
        return mAdapter.getItem(checkIndex);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_change_charge_period);
        View v = dialog.getWindow().getDecorView();

        TextView title = (TextView) v.findViewById(R.id.title);
        TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);


        title.setText(getString(R.string.changeChargePeriodTitle));
        txtDescription.setText(getString(R.string.changeChargePeriodDescription));



        listUsedDevice = (ListView) v.findViewById(R.id.listDevices);
        mAdapter = new Adapter(getActivity().getLayoutInflater());
        listUsedDevice.setAdapter(mAdapter);
        listUsedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.d(TAG, "called onItemClick()-position : " + position);
                mAdapter.setCheckedIndex(position);
            }
        });


        mAdapter.setList(rentalPackageObjects.toArray(new RentalPeriods[rentalPackageObjects.size()]));
        mAdapter.notifyDataSetChanged();

        if(firstRental == null) {
            mAdapter.setCheckedIndex(0);
        } else {
            for(int i = 0, size = rentalPackageObjects.size(); i < size; i++) {
                if(firstRental.getPriceValue() == rentalPackageObjects.get(i).getPriceValue()) {
                    mAdapter.setCheckedIndex(i);
                    break;
                }
            }
        }


        v.findViewById(R.id.btnConfirm).setOnClickListener(mOnClickListener);
        v.findViewById(R.id.btnCancel).setOnClickListener(mOnClickListener);


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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder holder;
            if (convertView == null) {
                v = getLayoutInflater().inflate(R.layout.item_package_period_p2, parent, false);
                holder = new ViewHolder();
                holder.txtDeviceName = (TextView) v.findViewById(R.id.txtDeviceName);
                holder.checkBox = (CheckBox) v.findViewById(R.id.checkId);
                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }

            RentalPeriods rentalPackageObject = getItem(position);
            if (rentalPackageObject != null) {
                holder.txtDeviceName.setText("" + rentalPackageObject.getPriceString(mProduct) + " VNĐ / " + rentalPackageObject.getDurationString(getContext()));
            }
            if (checkIndex == position) {
                v.setSelected(true);
                holder.checkBox.setChecked(true);
            } else {
                v.setSelected(false);
                holder.checkBox.setChecked(false);
            }
            return v;
        }

        public void setCheckedIndex(int position) {
            checkIndex = position;
            notifyDataSetChanged();
        }

    }

    private static class ViewHolder {
        TextView txtDeviceName;
        CheckBox checkBox;
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
