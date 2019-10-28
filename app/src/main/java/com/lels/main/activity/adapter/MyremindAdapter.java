package com.lels.main.activity.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyremindAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	Context mContext;
	List<HashMap<String, Object>> mlist;

	public MyremindAdapter(Context context, List<HashMap<String, Object>> list,String aa) {
		this.mContext = context;
		this.mlist = list;

	}

	@Override
	public int getCount() {

		if (mlist.size() > 0) {
			return mlist.size();
		} else {
			return 0;
		}

	}

	@Override
	public Object getItem(int arg0) {
		if (mlist.size() > 0) {
			return mlist.size();
		} else {
			return 0;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewholer;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_studyplan_myplan_test, null);

			viewholer = new ViewHolder();
			viewholer.textview_remind_time = (TextView) convertView
					.findViewById(R.id.textview_remind_start_time);
			viewholer.textview_remind_title = (TextView) convertView
					.findViewById(R.id.textview_remind_title);
			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}

		if (mlist.size() > 0) {
			viewholer.textview_remind_time.setText(mlist.get(position)
					.get("time").toString());
			viewholer.textview_remind_title.setText(mlist.get(position)
					.get("title").toString());
		}
		return convertView;
	}

	class ViewHolder {
		TextView textview_remind_time;
		TextView textview_remind_title;
	}

}
