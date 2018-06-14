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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.adapter.SimpleListAdapter;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;

public class PurchaseOptionDialogPhase2 extends DialogFragment {
    private static final String TAG = PurchaseOptionDialogPhase2.class.getSimpleName();
    public static final String CLASS_NAME = PurchaseOptionDialogPhase2.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_PACKAGES = "PARAM_PACKAGES";

    private View.OnClickListener mOnClickListener = null;
    private ListView listUsedDevice = null;
    private Adapter mAdapter;
    private ArrayList<Product> listPackage;
    private Product singleProduct;
    public void setSrc(Product singleProduct, ArrayList<Product> products){
        this.singleProduct = singleProduct;
        this.listPackage = products;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_purchase_option_p2);
        View v = dialog.getWindow().getDecorView();

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

        ArrayList<PurchaseOption> listPurchase = new ArrayList<>();
        listPurchase.add(new PurchaseOption(getActivity().getString(R.string.retailPurchase), singleProduct));
        listPurchase.add(new PurchaseOption(getActivity().getString(R.string.btnPackagePurchase), listPackage));

        mAdapter.setList(listPurchase.toArray(new PurchaseOption[listPurchase.size()]));
        mAdapter.notifyDataSetChanged();
        mAdapter.setCheckedIndex(0);

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

    private static class ViewHolder {
        TextView txtPurchaseOption = null;
        CheckBox checkBox = null;
    }

    public class Adapter extends SimpleListAdapter<PurchaseOption> {
        public Adapter(LayoutInflater inflater) {
            super(inflater);
        }

        private int mCheckedIndex = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder holder;
            if (convertView == null) {
                v = getLayoutInflater().inflate(R.layout.item_purchase_option, parent, false);
                holder = new ViewHolder();
                holder.txtPurchaseOption = (TextView) v.findViewById(R.id.txtPurchaseOption);
                holder.checkBox = (CheckBox) v.findViewById(R.id.checkId);
                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }

            PurchaseOption purchaseOption = getItem(position);
            if (purchaseOption != null) {
                holder.txtPurchaseOption.setText(purchaseOption.getName());
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

    public PurchaseOption getSelectedPurchaseOption() {
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
}
