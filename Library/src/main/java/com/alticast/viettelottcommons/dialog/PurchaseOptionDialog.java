package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.widget.ListViewMaxHeight;

import java.util.ArrayList;

public class PurchaseOptionDialog extends DialogFragment {

    public static final String SINGLE_PRODUCT = "SINGLE_PRODUCT";
    public static final String PACKAGE_PRODUCT = "PACKAGE_PRODUCT";

    public static final String CLASS_NAME = PurchaseOptionDialog.class.getName();
    private static final String TAG = PurchaseOptionDialog.class.getSimpleName();

    private Product singleProduct;
    private ArrayList<Product> listPackage;

    private ListView listPackagePeriod = null;
    private Adapter mAdapter;
    Button btnPurchase;
    private int checkIndex = -1;

    private OnClickListener mOnClickListener;
    public boolean isDismiss;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_purchase_option);
        dialog.setCanceledOnTouchOutside(false);
//        Window window = dialog.getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        View v = dialog.getWindow().getDecorView();

        Bundle arguments = getArguments();
        if (arguments != null) {

            singleProduct = arguments.getParcelable(SINGLE_PRODUCT);
            listPackage = arguments.getParcelableArrayList(PACKAGE_PRODUCT);

            btnPurchase = (Button) v.findViewById(R.id.btnPurchase);
            Button btnCancel = (Button) v.findViewById(R.id.btnCancel);

            listPackagePeriod = (ListView) v.findViewById(R.id.listPeriod);
            mAdapter = new Adapter(getActivity().getLayoutInflater());
            listPackagePeriod.setAdapter(mAdapter);
            listPackagePeriod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Logger.d(TAG, "called onItemClick()-position : " + position);

                    if(checkIndex < 0) {
                        btnPurchase.setEnabled(true);
                        btnPurchase.setAlpha(1f);
                    }
                    mAdapter.setCheckedIndex(position);
                }
            });
//            mAdapter.setCheckedIndex(0);

//            btnPurchase.setEnabled(false);
//            btnPurchase.setAlpha(0.3f);

            btnPurchase.setTag(singleProduct);
            btnPurchase.setOnClickListener(mOnClickListener);
            btnCancel.setOnClickListener(mOnClickListener);

            ArrayList<PurchaseOption> listPurchase = new ArrayList<>();
            listPurchase.add(new PurchaseOption(getActivity().getString(R.string.retailPurchase), singleProduct));
            listPurchase.add(new PurchaseOption(getActivity().getString(R.string.btnPackagePurchase), listPackage));

            mAdapter.setList(listPurchase.toArray(new PurchaseOption[listPurchase.size()]));
            mAdapter.setCheckedIndex(1);
            mAdapter.notifyDataSetChanged();
        }



        return dialog;
    }

    public Product getSingleProduct() {
        return singleProduct;
    }

    public void setSingleProduct(Product singleProduct) {
        this.singleProduct = singleProduct;
    }

    public ArrayList<Product> getListPackage() {
        return listPackage;
    }

    public void setListPackage(ArrayList<Product> listPackage) {
        this.listPackage = listPackage;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

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
                v = getLayoutInflater().inflate(R.layout.item_package_period_p2, parent, false);
                holder = new ViewHolder();
                holder.txtPurchaseOption = (TextView) v.findViewById(R.id.txtDeviceName);
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
