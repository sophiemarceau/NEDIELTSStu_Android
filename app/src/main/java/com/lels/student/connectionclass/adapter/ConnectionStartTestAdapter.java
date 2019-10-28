/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015��7��15�� 
 * 
 *******************************************************************************/
package com.lels.student.connectionclass.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.example.strudentlelts.R.color;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConnectionStartTestAdapter extends BaseAdapter {

	public static int setSelectedPosition;
	private List<HashMap<String, Object>> mlist;
	private Context context;
	private int selectPositon = -1;

	public ConnectionStartTestAdapter(List<HashMap<String, Object>> mlist,
			Context context) {
		super();
		this.mlist = mlist;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.isEmpty() ? 0 : mlist.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_connection_starttest, null);
			holder.txt_testnum = (TextView) convertView
					.findViewById(R.id.item_txt_testnum);
	
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		/*
		 * 设置item选中变红
		 */
		if (selectPositon == position) {
			holder.txt_testnum.setTextColor(Color.RED);
			holder.txt_testnum.setBackgroundResource(R.drawable.img_connection_starttest_background); 
		} else {
			holder.txt_testnum.setTextColor(Color.BLACK);
			holder.txt_testnum.setBackgroundColor(Color.parseColor("#efefef")); 
		}

		holder.txt_testnum.setText(mlist.get(position).get("num").toString());
		return convertView;
	}

	class ViewHolder {
		TextView txt_testnum;
		RelativeLayout left_listview;
	}


	public  void setSelectedPosition(int positon)
	{
		selectPositon=positon;
	}

}
