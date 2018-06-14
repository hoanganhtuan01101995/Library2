package com.alticast.viettelottcommons.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.activity.App;
import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.adapter.SimpleListAdapter;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.PurchaseLoader;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPackageObject;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.resource.response.CheckCouponRes;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.widget.ListViewMaxHeight;
import com.alticast.viettelottcommons.widget.MyListView;

import java.util.ArrayList;

import retrofit2.Call;

public class PackagePeriodDialogPhase2 extends DialogFragment {
    private static final String TAG = PackagePeriodDialogPhase2.class.getSimpleName();
    public static final String CLASS_NAME = PackagePeriodDialogPhase2.class.getName();

    public static final String PARAM_PRODUCT = "PARAM_PRODUCT";
    public static final String DEVICE_ACCOUNT = "DEVICE_ACCOUNT";

    private boolean isNotCheckCouponMode = true;

    private View.OnClickListener mOnClickListener = null;
    private MyListView listUsedDevice = null;
    private Adapter mAdapter;
    private ArrayList<RentalPeriods> rentalPackageObjects;
    private RentalPeriods firstRental;
    private Product mProduct;
    private CheckCouponRes checkCouponRes;
    private Integer checkIndex;

    private EditText edtCoupon;
    private TextView txtCouponHint;
    private Button btnConfirm;
    ScrollView scrollView;

//    private DialogInterface.OnDismissListener mOnDismissListener;

    public void setSrc(RentalPeriods firstRental, Product product) {
        this.firstRental = firstRental;
//        this.rentalPackageObjects = product.getRental_periods();
        this.rentalPackageObjects = product.getListRentalsUpdate();
        this.mProduct = product;
        this.mProduct.sortRental();
    }

    public RentalPeriods getChoseRental() {
        return mAdapter.getItem(checkIndex);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        mProduct.setCheckCouponRes(null);

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_package_period_p2);
        View v = dialog.getWindow().getDecorView();

//        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//                int heightDiff = v.getRootView().getHeight() - v.getHeight();
//                Log.e("Keyboard", "heightDiff " + heightDiff);
//                if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
//                    Log.e("Keyboard", "show");
//                    scrollAll();
//                }
//            }
//        });

        scrollView = (ScrollView) v.findViewById(R.id.scrollView);

        TextView title = (TextView) v.findViewById(R.id.title);
        TextView txtDescription = (TextView) v.findViewById(R.id.txtDescription);
        TextView txtProductShortDescription = (TextView) v.findViewById(R.id.txtProductShortDescription);

        edtCoupon = (EditText) v.findViewById(R.id.edtCoupon);
        txtCouponHint = (TextView) v.findViewById(R.id.txtCouponHint);
        btnConfirm = (Button) v.findViewById(R.id.btnConfirm);

        edtCoupon.addTextChangedListener(new CouponTextWatcher(edtCoupon));

        if (mProduct != null) {
            title.setText(mProduct.getName());
        }
//        txtDescription.setText(mProduct.getDescription());
        if (mProduct != null && mProduct.getDescription() != null && !mProduct.getDescription().isEmpty()) {
            txtDescription.setText(mProduct.getDescription());
            txtDescription.setMovementMethod(new ScrollingMovementMethod());
        } else {
            txtDescription.setVisibility(View.GONE);
        }
        if (mProduct != null && mProduct.getShortDescription() != null && !mProduct.getShortDescription().isEmpty()) {
            txtProductShortDescription.setText(mProduct.getShortDescription());
        } else {
            txtProductShortDescription.setVisibility(View.GONE);
        }


        listUsedDevice = (MyListView) v.findViewById(R.id.listDevices);
//        int maxItem = WindmillConfiguration.scrHeight < 1200 ? 4 : 5;
//        int limit = rentalPackageObjects.size() > maxItem ? maxItem : rentalPackageObjects.size();
//        listUsedDevice.setMaxHeight((int)(limit * 55 * WindmillConfiguration.scale));


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

        mAdapter.setCheckedIndex(0);
        if (firstRental == null) {
            for (int i = 0, size = rentalPackageObjects.size(); i < size; i++) {
                if (rentalPackageObjects.get(i).getPeriod() == 30) {
                    mAdapter.setCheckedIndex(i);
                    break;
                }
            }

        } else {
            for (int i = 0, size = rentalPackageObjects.size(); i < size; i++) {
                if (firstRental.getPeriod() == rentalPackageObjects.get(i).getPeriod()) {
                    mAdapter.setCheckedIndex(i);
                    break;
                }
            }
        }

//        if (!WindmillConfiguration.isBigSmallPackageVersion) {
//            v.findViewById(R.id.txtCouponTitle).setVisibility(View.GONE);
//            v.findViewById(R.id.edtCoupon).setVisibility(View.GONE);
//            v.findViewById(R.id.dividerCoupon).setVisibility(View.GONE);
//            v.findViewById(R.id.txtCouponHint).setVisibility(View.GONE);
//        }

        txtCouponHint.setVisibility(View.GONE);

        if(!WindmillConfiguration.isEnableCoupon) {
            v.findViewById(R.id.txtCouponTitle).setVisibility(View.GONE);
            v.findViewById(R.id.edtCoupon).setVisibility(View.GONE);
            v.findViewById(R.id.dividerCoupon).setVisibility(View.GONE);

        } else {
            v.findViewById(R.id.txtCouponTitle).setVisibility(View.VISIBLE);
            v.findViewById(R.id.edtCoupon).setVisibility(View.VISIBLE);
            v.findViewById(R.id.dividerCoupon).setVisibility(View.VISIBLE);
//            v.findViewById(R.id.txtCouponHint).setVisibility(View.VISIBLE);
        }


        v.findViewById(R.id.btnConfirm).setOnClickListener(onClickListener);
        v.findViewById(R.id.btnCancel).setOnClickListener(onClickListener);

        edtCoupon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollAll();
            }
        });

        edtCoupon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    scrollAll();
                }

                return false;
            }
        });

        return dialog;
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//
//        // Checks whether a hardware keyboard is available
//        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
//            scrollAll();
//        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
//        }
//    }

    private void scrollAll() {
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }, 500);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

//        final View activityRootView = getDialog().getWindow().getDecorView().findViewById(android.R.id.content);
//        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
//                Log.e("Keyboard", "heightDiff " + heightDiff);
//                if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
//                    Log.e("Keyboard", "show");
//                    scrollAll();
//                }
//            }
//        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//
//            if(onClickListener != null) {
//                onClickListener.onClick(v);
//            }
//        }
//    };

    private class CouponTextWatcher implements TextWatcher {

        private View targetView = null;

        public CouponTextWatcher(View targetView) {
            super();
            this.targetView = targetView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int i = targetView.getId();
            if (i == R.id.edtCoupon) {

                if(s.length() == 0) {
                    btnConfirm.setEnabled(true);
                    btnConfirm.setAlpha(1.0f);
                    checkCouponRes = null;
                    mProduct.setCheckCouponRes(null);
                } else {
                    btnConfirm.setEnabled(false);
                    btnConfirm.setAlpha(0.5f);
                }

                if (s.length() == 12) {
                    if(isNotCheckCouponMode) {
                        edtCoupon.setEnabled(true);
                        btnConfirm.setEnabled(true);
                        btnConfirm.setAlpha(1.0f);
                    } else {
                        edtCoupon.setEnabled(false);
                        checkCoupon();
                    }




                } else if(s.length() > 12) {
                    txtCouponHint.setVisibility(View.VISIBLE);
                    txtCouponHint.setText(getString(R.string.period_coupon_invalid));
                } else {
                    txtCouponHint.setVisibility(View.GONE);
                }

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

    }

    public void checkCoupon() {
        final GlobalActivity globalActivity = (GlobalActivity) getActivity();
        globalActivity.showProgress(getChildFragmentManager());

        PurchaseLoader.getInstance().checkCoupon(mProduct.getId(), edtCoupon.getText().toString(), new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                globalActivity.hideProgress(getChildFragmentManager());

                checkCouponRes = (CheckCouponRes) obj;
                edtCoupon.setEnabled(true);
                btnConfirm.setEnabled(true);
                btnConfirm.setAlpha(1.0f);

                if(checkCouponRes.getDiscount_method().equals(CheckCouponRes.METHOD_RATE)) {
                    showDetailCoupon(String.format(getString(R.string.coupon_rate), "" + checkCouponRes.getDiscount_value() + "%"));
                } else if(checkCouponRes.getDiscount_method().equals(CheckCouponRes.METHOD_PRICE)) {
                    showDetailCoupon(String.format(getString(R.string.coupon_price), "" + TextUtils.getNumberString(checkCouponRes.getDiscount_value())));
                } else {
                    txtCouponHint.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                globalActivity.hideProgress(getChildFragmentManager());
                checkCouponRes = null;
                txtCouponHint.setVisibility(View.GONE);
                edtCoupon.setEnabled(true);
                mProduct.setCheckCouponRes(null);
//                globalActivity.showKeyBoard(edtCoupon);
                App.showAlertDialogNetwork(globalActivity, getChildFragmentManager(), null);
            }

            @Override
            public void onError(ApiError error) {
                globalActivity.hideProgress(getChildFragmentManager());
                checkCouponRes = null;
                edtCoupon.setEnabled(true);
                mProduct.setCheckCouponRes(null);
                globalActivity.showKeyBoard(edtCoupon);

                scrollAll();
//                showErrorCoupon(getString(R.string.period_coupon_invalid));
                showErrorCoupon(error.getError().getMessage());
            }
        });

    }

    public void showErrorCoupon(String errorString) {
        txtCouponHint.setVisibility(View.VISIBLE);
        txtCouponHint.setTextColor(getResources().getColor(R.color.red_error));
        txtCouponHint.setText(errorString);
    }
    public void showDetailCoupon(String message) {
        txtCouponHint.setVisibility(View.VISIBLE);
        txtCouponHint.setTextColor(getResources().getColor(R.color.wallet_blue));
        txtCouponHint.setText(message);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {

                if(isNotCheckCouponMode) {
                    checkCouponRes = new CheckCouponRes();
                    checkCouponRes.setCode(edtCoupon.getEditableText().toString());
                    checkCouponRes.setDiscount_method(CheckCouponRes.METHOD_PRICE);
                    mProduct.setCheckCouponRes(checkCouponRes);
                } else {
                    if(checkCouponRes != null) {
                        checkCouponRes.setCode(edtCoupon.getEditableText().toString().trim());
                        mProduct.setCheckCouponRes(checkCouponRes);
                    }
                }


                mOnClickListener.onClick(v);
            }
        }
    };

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
                String info = "" + rentalPackageObject.getPriceString(mProduct) + " VNĐ / " + rentalPackageObject.getDurationString(getContext());
                if (WindmillConfiguration.isBigSmallPackageVersion && rentalPackageObject.getMax_screen() > 0) {
                    info += " - " + rentalPackageObject.getMax_screen() + " màn hình";
                }
                holder.txtDeviceName.setText(info);
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
