package com.alticast.viettelottcommons.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class EventEditText extends EditText {
	private OnBackPressListener _listener;

	public EventEditText(Context context) {
		super(context);
	}

	public EventEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EventEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (_listener != null) {
			_listener.onBackPress(this, keyCode, event);
		}

		return super.onKeyPreIme(keyCode, event);
	}
	
	

	@Override
	public void onEditorAction(int actionCode) {
		// TODO Auto-generated method stub
		super.onEditorAction(actionCode);
		if (_listener != null) {
			_listener.onEditTextAction(this,actionCode);
		}
		
	}

	public void setKeyListener(OnBackPressListener $listener) {
		_listener = $listener;
	}

	public interface OnBackPressListener {
		public void onBackPress(EditText view, int keyCode, KeyEvent event);
		public void onEditTextAction(EditText view, int actionCode);
	}

}
