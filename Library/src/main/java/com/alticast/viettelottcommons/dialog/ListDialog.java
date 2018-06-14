package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.adapter.SimpleListAdapter;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;

public class ListDialog extends DialogFragment {
    public static final String CLASS_NAME = ListDialog.class.getName();

    public static final String PARAM_ITEMS = CLASS_NAME + ".PARAM_ITEMS";
    public static final String PARAM_SELECTED_INDEX = CLASS_NAME + ".PARAM_SELECTED_INDEX";

    private static final String TAG = ListDialog.class.getSimpleName();

    private OnItemClickListener mOnClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_list);
        View v = dialog.getWindow().getDecorView();
        ListView listView = (ListView) v.findViewById(android.R.id.list);

        listView.setOnItemClickListener(mOnClickListener);
        Adapter adapter = new Adapter(getActivity().getLayoutInflater());
        listView.setAdapter(adapter);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String[] stringArray = arguments.getStringArray(PARAM_ITEMS);
            if (stringArray == null) {
                ArrayList<String> stringArrayList = arguments.getStringArrayList(PARAM_ITEMS);
                if (stringArrayList != null) {
                    stringArray = stringArrayList.toArray(new String[stringArrayList.size()]);
                }
            }
            adapter.setList(stringArray);

            int selectedIndex = arguments.getInt(PARAM_SELECTED_INDEX);
            if (selectedIndex >= 0) {
                listView.setSelection(selectedIndex);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listView.setItemChecked(selectedIndex, true);
            }
        }
        return dialog;
    }

    public void setOnItemClickListener(OnItemClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }


    private class Adapter extends SimpleListAdapter<String> {

        public Adapter(LayoutInflater inflater) {
            super(inflater);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_list_dialog, parent, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position));
            return convertView;
        }

    }
}
