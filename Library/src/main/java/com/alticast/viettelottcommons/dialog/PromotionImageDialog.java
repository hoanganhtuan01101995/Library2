package com.alticast.viettelottcommons.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.resource.Product;
import com.alticast.viettelottcommons.resource.RentalPeriods;
import com.alticast.viettelottcommons.util.Logger;
import com.alticast.viettelottcommons.util.TextUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class PromotionImageDialog extends DialogFragment {
    public static final String CLASS_NAME = PromotionImageDialog.class.getName();

    public static final String PARAM_CONTENT_URL = "PARAM_CONTENT_URL";

    private static final String TAG = PromotionImageDialog.class.getSimpleName();

    private OnClickListener mOnClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    View v;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Logger.d(TAG, "onCreateDialog");

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_promotion_image);

        v = dialog.getWindow().getDecorView();
        ImageView imvPromotion = (ImageView) v.findViewById(R.id.imvPromotion);
        View viewClose = v.findViewById(R.id.imvBack);
        View viewShadow = v.findViewById(R.id.viewShadow);
        viewShadow.setVisibility(View.INVISIBLE);

        Bundle arguments = getArguments();
        Picasso picasso = Picasso.with(getActivity());
        BannerTarget target = new BannerTarget(imvPromotion);
        if (arguments != null) {
            String contentUrl = arguments.getString(PARAM_CONTENT_URL) + "?width=" + WindmillConfiguration.scrWidth + "&height=" + WindmillConfiguration.scrHeight;
            picasso.load(contentUrl).placeholder(R.drawable.bg_grey).into(target);
        }
        viewClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return dialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    private class BannerTarget implements Target {
        private ImageView posterBanner = null;

        public BannerTarget(ImageView posterBanner) {
            super();
            this.posterBanner = posterBanner;
        }

        @Override
        public void onBitmapFailed(Drawable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onBitmapLoaded(Bitmap arg0, Picasso.LoadedFrom arg1) {
            // TODO Auto-generated method stub
            posterBanner.setImageBitmap(arg0);
            View viewShadow = v.findViewById(R.id.viewShadow);

            ViewGroup.LayoutParams params = viewShadow.getLayoutParams();
            params.width = arg0.getWidth();
            viewShadow.requestLayout();
            viewShadow.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPrepareLoad(Drawable arg0) {
            // TODO Auto-generated method stub
            Logger.print(this, "ChannelList onPrepareLoad");
        }

    }
}
