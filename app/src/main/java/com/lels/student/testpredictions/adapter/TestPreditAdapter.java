package com.lels.student.testpredictions.adapter;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.lels.student.testpredictions.activity.TestPredictionsWebActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TestPreditAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	
	Context mContext;
	List<HashMap<String, Object>> mlist;

	public TestPreditAdapter(Context context,
			List<HashMap<String, Object>> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewholer;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_testprediction, null);

			viewholer = new ViewHolder();
			viewholer.textview_pre_date = (TextView) convertView
					.findViewById(R.id.textview_pre_date);
			viewholer.textview_pre_year = (TextView) convertView
					.findViewById(R.id.textview_pre_year);
			viewholer.textview_pre_title = (TextView) convertView
					.findViewById(R.id.textview_pre_title);

			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();
		}

		//  MF_ID = 收藏主键;
		//   ID = 预测主键;
		//   Title = 名称;
		//   Link = 链接地址;
		//   CreateDate = 创建日期;
		//   IsPublish = 是否已经发布 0=未发布；1=已发布;
		//   OnTop = 是否置顶 0=不置顶；1=置顶;
		//   IsDelete = 是否已经删除 0=未删除；1=已删除;
		//   Content = 发布内容的富文本;

		if (mlist.size() > 0) {
			viewholer.textview_pre_title.setText(mlist.get(position)
					.get("Title").toString());
			
			String time = mlist.get(position).get("CreateDate").toString();
			
			String time_year = time.substring(6);
			String time_month = time.substring(0, 5);
			System.out.println("截取的年==================="+time_year+"街区的月为========"+time_month);
			
			viewholer.textview_pre_year.setText(time_year);
			viewholer.textview_pre_date.setText(time_month);
		}
//		convertView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(mContext, TestPredictionsWebActivity.class);
//				Bundle b = new Bundle();
//				b.putString("Link", mlist.get(position).get("Link").toString());
//				b.putString("MF_ID", mlist.get(position).get("MF_ID").toString());
//				b.putString("ID", mlist.get(position).get("ID").toString());
//				b.putString("Content", mlist.get(position).get("Content").toString());
//				intent.putExtras(b);
//				mContext.startActivity(intent);
//				
//			}
//		});
		return convertView;
	}

	class ViewHolder {
		TextView textview_pre_date;
		TextView textview_pre_year;
		TextView textview_pre_title;
	}
	
	

}
