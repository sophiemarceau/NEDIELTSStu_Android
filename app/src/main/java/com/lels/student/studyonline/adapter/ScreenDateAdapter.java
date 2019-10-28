package com.lels.student.studyonline.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScreenDateAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	Context mContext;
	List<HashMap<String, Object>> mlist;
	int position_selected;

	public ScreenDateAdapter(Context context,
			List<HashMap<String, Object>> list, int position) {
		this.mContext = context;
		this.mlist = list;
		this.position_selected = position;
	}

	public void updataforgridview(int aa) {
		this.position_selected = aa;
		notifyDataSetChanged();
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

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder viewholer;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_data_screendata, null);

			viewholer = new ViewHolder();
			viewholer.relative_screen_background = (RelativeLayout) convertView
					.findViewById(R.id.relative_screen_background);
			viewholer.textview_screen_name = (TextView) convertView
					.findViewById(R.id.textview_screen_name);

			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}

		viewholer.textview_screen_name.setText(mlist.get(position)
				.get("screenkey").toString());

		if (position_selected == -1) {
			System.out.println("-111111111111111");
			viewholer.relative_screen_background
					.setBackgroundResource(R.drawable.icon_screen_select);
		} else {
			if (position == position_selected) {
				System.out.println("111111111111111111");
				viewholer.relative_screen_background
						.setBackgroundResource(R.drawable.icon_screen_selected);
				viewholer.textview_screen_name.setTextColor(Color.WHITE);
			} else {
				System.out.println("2222222222222222222222");
				viewholer.relative_screen_background
						.setBackgroundResource(R.drawable.icon_screen_select);
			}

		}

		return convertView;
	}

	class ViewHolder {
		RelativeLayout relative_screen_background;
		TextView textview_screen_name;
	}

}
