/*
 *  @(#)Resources.java 1.0 2012.09.16
 *  Copyright (c) 2010 Alticast Corp.
 *  All rights reserved. http://www.alticast.com/
 *
 *  This software is the confidential and proprietary information of
 *  Alticast Corp. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Alticast.
 */

package com.alticast.viettelottcommons.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alticast.viettelottcommons.R;

import java.util.HashMap;


/**
 * <code>Resources</code> Resource(Color, Font, Key, Image) 관련된 사항들을 처리함.
 * 
 * @since 2012.09.16
 * @version $Revision: 1.1 $ $Date: 2015/08/31 23:23:17 $
 * @author tklee
 */
public final class Rs {
	/** The context. */
	private static Context context = null;

	/** The base timer delay. */
	public static int baseTimerDelay = 200;
	public static final long TIME_LOAD_DELAY = 1000L;


	private static HashMap<String, Typeface> fontTypeMap = new HashMap<String, Typeface>();

	/** The normal face. */
	public static Typeface normalFace = null;

	public static int dimenMenuLeftMargin = 0;
	public static int intSmartPhoneLimit = 0;
	public static int[] dimensMenuPos = new int[5];
	public static String strErrCommunication = null;
	public static int cMenuName = -1;
	public static Drawable drawNewIcon = null;

	/**
	 * 생성자.
	 */
	private Rs() {
	}

	/**
	 * Inits the.
	 * 
	 * @param context
	 *            the context
	 */
	public static void init(Context context) {
		if (context == null) {
			return;
		}
		Rs.context = context;


		String[] fontIds = getResStringArrays(R.array.fontIds);
		String[] fontFiles = getResStringArrays(R.array.fontFiles);
		for (int i = 0; i < fontIds.length; i++) {
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/"+fontFiles[i]);
			
			if (typeface == null) {
				typeface = Typeface.DEFAULT;
			}
			fontTypeMap.put(fontIds[i], typeface);
		}

		/*
		 * baseTimerDelay = getResInteger(R.integer.baseTimerDelay); // TODO 빼야
		 * 함. normalFace =
		 * Typeface.createFromAsset(context.getAssets(),"fonts/YGO540.ttf"); //
		 * dimensMenuPos = getResIntegerArrays(R.array.menuFocutMarginArray);
		 * int[] posResId = { R.dimen.menu1Margin, R.dimen.menu2Margin,
		 * R.dimen.menu3Margin, R.dimen.menu4Margin, R.dimen.menu5Margin }; for
		 * (int i = 0; i < posResId.length; i++) { dimensMenuPos[i] =
		 * getResDimension(posResId[i]); } strErrCommunication =
		 * getResString(R.string.errCommunication); cMenuName =
		 * getResColor(R.color.menuName); drawNewIcon =
		 * getResDrawable(R.drawable.menu_icon_new);
		 */
	}

	/**
	 * Gets the resource color.
	 * 
	 * @param resId
	 *            the resource id
	 * @return the resource color
	 */
	public static int getResColor(int resId) {
		return context.getResources().getColor(resId);
	}

	/**
	 * Gets the res drawable.
	 * 
	 * @param resId
	 *            the res id
	 * @return the res drawable
	 */
	public static Drawable getResDrawable(int resId) {
		Drawable draw = context.getResources().getDrawable(resId);
		draw.setBounds(0, 0, draw.getIntrinsicWidth(), draw.getIntrinsicHeight());
		return draw;
	}

	/**
	 * Gets the res dimension.
	 * 
	 * @param resId
	 *            the res id
	 * @return the res dimension
	 */
	private static int getResDimension(int resId) {
		int dimen = context.getResources().getDimensionPixelSize(resId);
		Logger.print(Rs.class, "getResDimension()-dimen : " + dimen);
		return dimen;
	}

	/**
	 * Gets the res integer.
	 * 
	 * @param resId
	 *            the res id
	 * @return the res integer
	 */
	public static int getResInteger(int resId) {
		int resInt = context.getResources().getInteger(resId);
		Logger.print(Rs.class, "getResInteger()-resInt : " + resInt);
		return resInt;
	}

	/**
	 * Gets the res integer arrays.
	 * 
	 * @param resId
	 *            the res id
	 * @return the res integer arrays
	 */
	public static int[] getResIntegerArrays(int resId) {
		int[] resInts = context.getResources().getIntArray(resId);
		Logger.print(Rs.class, "getResInteger()-resInts : " + resInts);
		return resInts;
	}

	/**
	 * Gets the res string.
	 * 
	 * @param resId
	 *            the res id
	 * @return the res string
	 */
	public static String getResString(int resId) {
		String resString = context.getResources().getString(resId);
		Logger.print(Rs.class, "getResString()-resString : " + resString);
		return resString;
	}

	public static String[] getResStringArrays(int resId) {
		String[] resStrs = context.getResources().getStringArray(resId);
		Logger.print(Rs.class, "getResStringArrays()-resInts : " + resStrs);
		return resStrs;
	}

	/**
	 * Sets the normal face.
	 * 
	 * @param view
	 *            the new normal face
	 */
	public static void setNormalFace(TextView view) {
		view.setTypeface(normalFace);
	}

	/**
	 * Sets the bold face.
	 * 
	 * @param view
	 *            the new bold face
	 */
	public static void setBoldFace(TextView view) {
		// view.setTypeface(boldFace);
	}

	/**
	 * Sets the digit face.
	 * 
	 * @param view
	 *            the new digit face
	 */
	public static void setDigitFace(TextView view) {
		// view.setTypeface(digitFace);
	}

	/**
	 * Sets the typefaces.
	 * 
	 * @param typeface
	 *            the typeface
	 * @param activity
	 *            the activity
	 * @param viewIds
	 *            the view ids
	 */
	public static void setTypefaces(Typeface typeface, Activity activity, int[] viewIds) {
		for (int i = 0; viewIds != null && i < viewIds.length; i++) {
			TextView textview = (TextView) activity.findViewById(viewIds[i]);
			textview.setTypeface(typeface);
		}
	}

	/**
	 * Sets the typefaces.
	 * 
	 * @param typeface
	 *            the typeface
	 * @param view
	 *            the view
	 * @param viewIds
	 *            the view ids
	 */
	public static void setTypefaces(Typeface typeface, View view, int[] viewIds) {
		for (int i = 0; viewIds != null && i < viewIds.length; i++) {
			TextView textview = (TextView) view.findViewById(viewIds[i]);
			textview.setTypeface(typeface);
		}
	}

	public static Typeface getFont(String fontId) {
		Typeface typeface = (Typeface) fontTypeMap.get(fontId);
		if (typeface == null) {
			typeface = Typeface.DEFAULT;
		}
		return typeface;
	}

	/**
	 * ImagePool class 생성.
	 * 
	 */
	public static void createImagePools() {

	}

	/**
	 * Image Pool flush.
	 */
	public static void dispose() {
		Logger.print(Class.class, "called Rs.dispose()");
	}

	/**
	 * Sets the location.
	 * 
	 * @param v
	 *            the v
	 * @param leftMargin
	 *            the left margin
	 * @param topMargin
	 *            the top margin
	 */
	public static void setLocation(View v, float leftMargin, float topMargin) {
		Logger.print(Rs.class, "setLocation()-leftMargin : " + leftMargin + ", topMargin : " + topMargin);
		// int leftPixel = (int) (leftDp * density);
		// int topPixel = (int) (topDp * density);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins((int) leftMargin, (int) topMargin, 0, 0);
		v.setLayoutParams(params);
	}
}
