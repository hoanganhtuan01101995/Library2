package com.alticast.viettelottcommons.adapter;

import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class SimpleListAdapter<T> extends BaseAdapter {
	private LayoutInflater mInflater;
	private T[] mList;
	protected String query = null;

	public SimpleListAdapter(LayoutInflater inflater) {
		mInflater = inflater;
	}

	protected LayoutInflater getLayoutInflater() {
		return mInflater;
	}

	public void setList(T[] list) {
		mList = list;
		notifyDataSetChanged();
	}

	public void setList(T[] list, String query) {
		mList = list;
		this.query = query;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		try {
			return mList.length;
		} catch (NullPointerException e) {
			return 0;
		}
	}

	@Override
	public T getItem(int position) {
		try {
			return mList[position];
		} catch (NullPointerException e) {
			return null;
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public T[] getList() {
		return mList;
	}

}
