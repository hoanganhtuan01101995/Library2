package com.alticast.viettelottcommons.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.alticast.viettelottcommons.activity.GlobalActivity;
import com.alticast.viettelottcommons.dialog.PhoneLoginFragmentPhase2;

/**
 * Created by duyuno on 9/9/17.
 */
public class FragmentBase extends Fragment {

    public GlobalActivity activity;

    public void setEnableButtonWithDim(Button button, boolean enable) {
        button.setEnabled(enable);
        if (enable) {
            button.setAlpha(1);
        } else {
            button.setAlpha(PhoneLoginFragmentPhase2.ALPHA_DIM);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (GlobalActivity) activity;
    }

    public void onBackPress() {

    }
}
