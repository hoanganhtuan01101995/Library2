package com.alticast.viettelottcommons.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

/**
 * Created by duyuno on 8/29/17.
 */
public class RoundedTransformation implements Transformation {
    private int mWidth, mHeight;
    private int mCornerRadius = 0;

    public RoundedTransformation(int mWidth, int mHeight) {
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        mCornerRadius = 5;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (source == null) return null;
        int width = Math.min(source.getWidth(), mWidth);
        int height = Math.min(source.getHeight(), mHeight);

        Bitmap squaredBitmap = Bitmap.createBitmap(source, 0, 0, width, height);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, width, height);
        canvas.drawRoundRect(new RectF(rect), mCornerRadius, mCornerRadius, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "bitmapBorder(" +
                "mWidth=" + this.mWidth + ", " +
                "mHeight=" + this.mHeight + ", " +
                "cornerRadius=" + this.mCornerRadius + ", " +
                ")";
    }

}
