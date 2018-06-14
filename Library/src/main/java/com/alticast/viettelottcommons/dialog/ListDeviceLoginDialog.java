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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.adapter.SimpleListAdapter;
import com.alticast.viettelottcommons.resource.MyDeviceAccount;
import com.alticast.viettelottcommons.resource.RegistedDevice;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;

public class ListDeviceLoginDialog extends DialogFragment {
    private static final String TAG = ListDeviceLoginDialog.class.getSimpleName();
    public static final String CLASS_NAME = ListDeviceLoginDialog.class.getName();

    public static final String PARAM_PRODUCT = "PARAM_PRODUCT";
    public static final String DEVICE_ACCOUNT = "DEVICE_ACCOUNT";

    private View.OnClickListener mOnClickListener = null;
    private ListView listUsedDevice = null;
    private Adapter mAdapter;
    private RegistedDevice registedDevice;
    private ArrayList<Integer> mListCheckIndex = new ArrayList<>();
    private String deviceAccountID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_list_device);
        View v = dialog.getWindow().getDecorView();

        Bundle arguments = getArguments();
        if (arguments != null) {
            registedDevice = arguments.getParcelable(PARAM_PRODUCT);
            deviceAccountID = arguments.getString(DEVICE_ACCOUNT);

            final Button btnConfirm = (Button) v.findViewById(R.id.btnConfirm);
            Button btnCancel = (Button) v.findViewById(R.id.btnCancel);
            btnConfirm.setEnabled(false);
            btnConfirm.setAlpha(0.3f);

            ArrayList<MyDeviceAccount.Devices> devices = registedDevice.getDevices();
            if (devices != null && devices.size() > 0) {
                for (MyDeviceAccount.Devices dev : devices) {
                    if (dev.getId().equalsIgnoreCase(deviceAccountID)) {
                        devices.remove(dev);
                        break;
                    }
                }
            }
            listUsedDevice = (ListView) v.findViewById(R.id.listDevices);
            mAdapter = new Adapter(getActivity().getLayoutInflater());
            listUsedDevice.setAdapter(mAdapter);
            listUsedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Logger.d(TAG, "called onItemClick()-position : " + position);
                    mAdapter.setCheckedIndex(position);
                    if (mAdapter.getCheckedIndex().size() == 0) {
                        btnConfirm.setEnabled(false);
                        btnConfirm.setAlpha(0.3f);
                    } else {
                        btnConfirm.setEnabled(true);
                        btnConfirm.setAlpha(1f);
                    }
                }
            });

            btnConfirm.setOnClickListener(mOnClickListener);
            btnCancel.setOnClickListener(mOnClickListener);

            mAdapter.setList(devices.toArray(new MyDeviceAccount.Devices[devices.size()]));
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

    public ArrayList<MyDeviceAccount.Devices> getSelectedDevice() {
        ArrayList<MyDeviceAccount.Devices> list = new ArrayList<>();
        for (int i = 0; i < mListCheckIndex.size(); i++) {
            list.add(mAdapter.getItem(mListCheckIndex.get(i)));
        }
        return list;
    }

    public class Adapter extends SimpleListAdapter<MyDeviceAccount.Devices> {
        public Adapter(LayoutInflater inflater) {
            super(inflater);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder holder;
            if (convertView == null) {
                v = getLayoutInflater().inflate(R.layout.item_device_list_logout, parent, false);
                holder = new ViewHolder();
                holder.txtDeviceName = (TextView) v.findViewById(R.id.txtDeviceName);
                holder.imvDevice = (ImageView) v.findViewById(R.id.imv_device_type);
                holder.checkBox = (CheckBox) v.findViewById(R.id.checkId);
                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }

            MyDeviceAccount.Devices devices = getItem(position);
            if (devices != null) {
                if (devices.getModel_name() == null || devices.getModel_name().equalsIgnoreCase("")) {
                    holder.txtDeviceName.setText(devices.getModel_no());
                } else
                    holder.txtDeviceName.setText(devices.getModel_name());
                if (devices.getModel().contains("PAD")) {
                    holder.imvDevice.setImageResource(R.drawable.icon_pad);
                } else if (devices.getModel().contains("PHONE")) {
                    holder.imvDevice.setImageResource(R.drawable.icon_phonenumber);
                }
            }
            if (mListCheckIndex.contains(position)) {
                v.setSelected(true);
                holder.checkBox.setChecked(true);
            } else {
                v.setSelected(false);
                holder.checkBox.setChecked(false);
            }
            return v;
        }

        public void setCheckedIndex(int position) {
            if (mListCheckIndex.contains(position)) {
                mListCheckIndex.remove((Integer) position);
            } else {
                mListCheckIndex.add((Integer) position);
            }
            notifyDataSetChanged();
        }

        public ArrayList<Integer> getCheckedIndex() {
            return mListCheckIndex;
        }

    }

    private static class ViewHolder {
        TextView txtDeviceName;
        ImageView imvDevice;
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
