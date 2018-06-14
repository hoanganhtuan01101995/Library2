package com.alticast.viettelottcommons.fragment.vwallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.adapter.SimpleListAdapter;
import com.alticast.viettelottcommons.fragment.FragmentBase;
import com.alticast.viettelottcommons.resource.ChargeAmountObj;
import com.alticast.viettelottcommons.resource.WalletTopupMethod;

import java.util.ArrayList;

/**
 * Created by duyuno on 9/9/17.
 */
public class FragmentPhoneMain extends FragmentBase {

    public static FragmentPhoneMain newInstance() {
        FragmentPhoneMain fragmentFirst = new FragmentPhoneMain();
        return fragmentFirst;
    }

    private WalletTopupMethod walletTopupMethod;
    private boolean isTablet;

    public void setWalletTopupMethod(WalletTopupMethod walletTopupMethod, boolean isTablet) {
        this.walletTopupMethod = walletTopupMethod;
        this.isTablet = isTablet;
    }

    private ListView listAmountOption = null;
    private TextView txtPromotionDescription = null;
    private Adapter amountAdapter;
    private boolean isHasPromotion;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_phone_charge_option, container, false);

        initView();

        initData();

        return view;
    }

    public void initView() {
        txtPromotionDescription = (TextView) view.findViewById(R.id.txtPromotionDescription);

        txtPromotionDescription.setLineSpacing(1, 1.2f);

        listAmountOption = (ListView) view.findViewById(R.id.listAmountOption);
        amountAdapter = new Adapter(activity.getLayoutInflater());
        listAmountOption.setAdapter(amountAdapter);
        listAmountOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == amountAdapter.getCount() - 1) {
                    FragmentChargeManager.goToChargePhoneEnterAmount(activity, 0);
                } else {
                    FragmentChargeManager.goToChargePhoneGetOtp(activity, amountAdapter.getItem(position), 0);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.hideAllKeyboard();
    }

    public void initData() {
        ArrayList<ChargeAmountObj> listChargeAmount = genListChargeObj(walletTopupMethod);


        amountAdapter.setList(listChargeAmount.toArray(new ChargeAmountObj[listChargeAmount.size()]));
        amountAdapter.notifyDataSetChanged();
    }

    public ArrayList<ChargeAmountObj> genListChargeObj(WalletTopupMethod walletTopupMethod) {
        ArrayList<ChargeAmountObj> list = new ArrayList<>();

        if (walletTopupMethod == null || walletTopupMethod.getPredefined_amount() == null)
            return list;

//        Integer[] amouts = walletTopupMethod.getPredefined_amount().toArray(new Integer[walletTopupMethod.getPredefined_amount().size()]);

        if (walletTopupMethod.checkHasPromotion()) {
            isHasPromotion = true;
            txtPromotionDescription.setVisibility(View.VISIBLE);
            String proMessage = walletTopupMethod.getPromotion().getDescription(activity);
            proMessage += "\n";
//            if (FragmentChargeManager.isTablet) {
//                proMessage += "\n";
//            } else {
//                proMessage += ":";
//            }
            proMessage += " (" + walletTopupMethod.getPromotion().getStartDate() + " - " + walletTopupMethod.getPromotion().getEndDate() + ")";
            txtPromotionDescription.setText(proMessage);
        } else {
            txtPromotionDescription.setVisibility(View.GONE);
            isHasPromotion = false;
        }

        for (int i = 0, size = walletTopupMethod.getPredefined_amount().size(); i < size; i++) {
            ChargeAmountObj chargeAmountObj = new ChargeAmountObj();
            chargeAmountObj.setAmount(walletTopupMethod.getPredefined_amount().get(i));

            if (walletTopupMethod.getPromotion() != null && walletTopupMethod.getPromotion().getItems() != null && walletTopupMethod.getPromotion().getItems().length > i) {
                chargeAmountObj.setItem(walletTopupMethod.getPromotion().getItems());
//                chargeAmountObj.setItem(walletTopupMethod.getPromotion().getItems()[i]);
            }

            list.add(chargeAmountObj);
        }

        ChargeAmountObj chargeAmountObj = new ChargeAmountObj();
//        if(walletTopupMethod.getPromotion().getItems() != null && walletTopupMethod.getPromotion().getItems().length > list.size()) {
//            chargeAmountObj.setItem(walletTopupMethod.getPromotion().getItems()[list.size()]);
//        }

        list.add(chargeAmountObj);


        return list;
    }

    @Override
    public void onBackPress() {

    }

    private static class ViewHolder {
        TextView txtAmount;
        TextView txtBonus;
    }

    public class Adapter extends SimpleListAdapter<ChargeAmountObj> {
        public Adapter(LayoutInflater inflater) {
            super(inflater);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            ViewHolder holder;
            ChargeAmountObj chargeAmountObj = getItem(position);
            if (convertView == null) {
                v = getLayoutInflater().inflate(R.layout.item_charge_amount, parent, false);
                holder = new ViewHolder();
                holder.txtAmount = (TextView) v.findViewById(R.id.txtAmount);
                holder.txtBonus = (TextView) v.findViewById(R.id.txtBonus);
                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }


            if (chargeAmountObj != null) {
                holder.txtAmount.setText(chargeAmountObj.getAmountDisplay());
                if (chargeAmountObj.getItem() != null) {
                    holder.txtBonus.setVisibility(View.VISIBLE);
                    holder.txtBonus.setText(chargeAmountObj.getBonusDisplay(FragmentChargeManager.walletTopupMethod));

                } else {
                    holder.txtBonus.setVisibility(View.GONE);
                }
            }

            if (FragmentChargeManager.isTablet) {
                if (isHasPromotion) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.txtAmount.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    holder.txtAmount.setLayoutParams(layoutParams);
                } else {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.txtAmount.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    holder.txtAmount.setLayoutParams(layoutParams);
                }
            }

            return v;
        }

    }

}
