/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015��7��17�� 
 * 
 *******************************************************************************/
package com.lels.student.connectionclass.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConnectionReportAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, Object>> mlist;

	public ConnectionReportAdapter(Context context,
			List<HashMap<String, Object>> mlist) {
		super();
		this.context = context;
		this.mlist = mlist;
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
					R.layout.item_connection_report, null);
			holder.txt_num = (TextView) convertView
					.findViewById(R.id.item_txt_num_connection_report);
			holder.txt_answer = (TextView) convertView
					.findViewById(R.id.item_txt_answer_connection_report);
			holder.txt_trueanswer = (TextView) convertView
					.findViewById(R.id.item_txt_trueanswer_connection_report);
			holder.txt_title = (TextView) convertView
					.findViewById(R.id.textView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String getlast = mlist.get(position).get("AnswerContent").toString();
		String answer = getlast.replace("[", "");
		System.out.println("answer======" + answer);
		if (answer.endsWith("null") || answer == null) {
			answer = "";
		}
		holder.txt_answer.setText(answer);
		holder.txt_num.setText("第"
				+ mlist.get(position).get("QNumber").toString() + "题 ");

		if (Integer.parseInt(mlist.get(position).get("RightCount").toString())
				- Integer.parseInt(mlist.get(position).get("ScoreCount")
						.toString()) < 0) {
			// 错误
			holder.txt_trueanswer.setVisibility(View.VISIBLE);
			holder.txt_title.setVisibility(View.VISIBLE);
			holder.txt_num.setTextColor(convertView.getResources().getColor(
					R.color.red_default));
			holder.txt_answer.setTextColor(convertView.getResources().getColor(
					R.color.red_default));
			holder.txt_trueanswer.setText(mlist.get(position).get("QSValue")
					.toString());

		} else {
			// 正确
			holder.txt_num.setTextColor(Color.BLACK);
			holder.txt_answer.setTextColor(Color.BLACK);
			holder.txt_trueanswer.setVisibility(View.GONE);
			holder.txt_title.setVisibility(View.GONE);
		}

		return convertView;
	}

	class ViewHolder {
		TextView txt_num, txt_answer, txt_trueanswer, txt_title;
	}

}
