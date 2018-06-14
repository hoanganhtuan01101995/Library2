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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.adapter.SimpleListAdapter;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPackageObject;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.widget.ListViewMaxHeight;
import com.alticast.viettelottcommons.widget.MyListView;

import java.util.ArrayList;

public class PackageOptionDialogPhase2 extends DialogFragment {
    private static final String TAG = PackageOptionDialogPhase2.class.getSimpleName();
    public static final String CLASS_NAME = PackageOptionDialogPhase2.class.getName();

    public static final String PARAM_IS_POINT_USER = "PARAM_IS_POINT_USER";
    public static final String PARAM_PACKAGES = "PARAM_PACKAGES";

    private View.OnClickListener mOnClickListener = null;
    private MyListView listUsedDevice = null;
    private Adapter mAdapter;
    private ArrayList<Product> products;
    private  boolean isPointUser = false;
    private int checkIndex = -1;
    Button btnConfirm;

    public void setSrc(boolean isPointUser, ArrayList<Product> products){
        this.products = products;
        this.isPointUser = isPointUser;
    }

    public Product getChooseProduct() {
        return mAdapter.getCheckedProduct();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_package_option_p2);
        View v = dialog.getWindow().getDecorView();
        btnConfirm =  (Button) v.findViewById(R.id.btnConfirm);

        listUsedDevice = (MyListView) v.findViewById(R.id.listDevices);

//        int maxItem = WindmillConfiguration.scrHeight < 1200 ? 4 : 5;
//
//        int limit = products.size() > maxItem ? maxItem : products.size();
//        listUsedDevice.setMaxHeight((int)(limit * 55 * WindmillConfiguration.scale));
        mAdapter = new Adapter(getActivity().getLayoutInflater());
        listUsedDevice.setAdapter(mAdapter);
        listUsedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.d(TAG, "called onItemClick()-position : " + position);
                if(checkIndex < 0) {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setAlpha(1f);
                }
                mAdapter.setCheckedIndex(position);


            }
        });




//        if(products.size() > 2) {
//            listUsedDevice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)WindmillConfiguration.convertDpToPixel(165f)));
//        } else {
//            listUsedDevice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)WindmillConfiguration.convertDpToPixel(110f)));
//        }

//        btnConfirm.setEnabled(false);
//        btnConfirm.setAlpha(0.3f);


        mAdapter.setList(products.toArray(new Product[products.size()]));
        mAdapter.notifyDataSetChanged();
        mAdapter.setCheckedIndex(0);

        btnConfirm.setOnClickListener(mOnClickListener);
        v.findViewById(R.id.btnCancel).setOnClickListener(mOnClickListener);


        return dialog;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

//    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
//        mOnItemClickListener = this;
//    }


    public class Adapter extends SimpleListAdapter<Product> {
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

            Product product = getItem(position);
            if (product != null) {
                holder.txtDeviceName.setText(product.getName());
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

        public Product getCheckedProduct() {
            if(checkIndex >= 0 && checkIndex < getCount()) {
                return getItem(checkIndex);
            }
            return null;
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
