package com.lels.main.activity.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.strudentlelts.R;
import com.lels.main.activity.MyselfRemindDeatilActivity;
import com.lels.main.activity.StudyPlanAddPlanActivity;
import com.lelts.student.db.ClockInfo;
import com.lelts.student.db.ClockInfoWrapper;

public class MyremindTestAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	Context mContext;
	// List<HashMap<String, Object>> mlist;
	List<ClockInfoWrapper> mlist;

	public MyremindTestAdapter(Context context, List<ClockInfoWrapper> list_c) {
		this.mContext = context;
		this.mlist = list_c;

	}

	public void setUserEntities(List<ClockInfoWrapper> list_c) {
		this.mlist = list_c;
		this.notifyDataSetChanged();
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewholer;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_studyplan_myplan_test, null);

			viewholer = new ViewHolder();
			viewholer.textview_remind_start_time = (TextView) convertView.findViewById(R.id.textview_remind_start_time);
			viewholer.textview_remind_title = (TextView) convertView.findViewById(R.id.textview_remind_title);
			viewholer.textview_remind_end_time = (TextView) convertView.findViewById(R.id.textview_remind_end_time);
			viewholer.textview_plan_content = (TextView) convertView.findViewById(R.id.textview_plan_content);
			viewholer.view_redline = (View) convertView.findViewById(R.id.view_redline);

			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHolder) convertView.getTag();
		}

		if (mlist.size() > 0) {
			String startime = mlist.get(position).info.getStarttime();
			String endtime = mlist.get(position).info.getEndtime();

			viewholer.textview_remind_start_time.setText(getStrTime(startime));
			viewholer.textview_remind_title.setText(mlist.get(position).info.getTitle());
			viewholer.textview_remind_end_time.setText(getStrTime(endtime));
			viewholer.textview_plan_content.setText(mlist.get(position).info.getNote());
			if (mlist.get(position).isInPlan) {
				viewholer.view_redline.setVisibility(View.VISIBLE);
			} else {
				viewholer.view_redline.setVisibility(View.INVISIBLE);
			}
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, MyselfRemindDeatilActivity.class);
				// intent.setClass(mContext, StudyPlanAddPlanActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("delete", mlist.get(position).info);
				b.putString("c_id", String.valueOf(mlist.get(position).info.getId()));
				b.putInt("posi", position);
				intent.putExtras(b);
				// mContext.startActivity(intent);
				System.out.println("mlist.get(position).getId() ===========" + mlist.get(position).info.getId());
				((Activity) mContext).startActivityForResult(intent, 10);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView textview_remind_start_time;
		TextView textview_remind_title;
		TextView textview_remind_end_time;
		TextView textview_plan_content;
		View view_redline;
	}

	// 将时间戳转为字符串
	// public static String getStrTime(String cc_time) {
	// String re_StrTime = null;
	// String re_re = null;
	//
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//
	// // 例如：cc_time=1291778220
	// long lcc_time = Long.valueOf(cc_time);
	//
	// re_StrTime = sdf.format(new Date(lcc_time));
	// System.out.println("re_re的长度为========================"
	// + re_StrTime.length());
	// re_re = re_StrTime.substring(11, 16);
	// System.out.println("re_re===============" + re_re);
	// return re_re;
	//
	// }
	// 2015-8-28-2-2
	public static String getStrTime(String cc_time) {
		String re_re = null;
		String str[] = cc_time.split("-");

		String minute = str[3];
		String secound = str[4];
		if (minute.length() == 1) {
			minute = "0" + minute;
		}
		if (secound.length() == 1) {
			secound = "0" + secound;
		}
		re_re = minute + ":" + secound;
		return re_re;
	}
}
