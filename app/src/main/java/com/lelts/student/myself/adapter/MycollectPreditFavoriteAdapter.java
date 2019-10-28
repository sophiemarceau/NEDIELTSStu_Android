package com.lelts.student.myself.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.lelts.student.myself.MystudentCollectActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MycollectPreditFavoriteAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private MystudentCollectActivity host;
	Context mContext;
	List<HashMap<String, Object>> mlist;

	public MycollectPreditFavoriteAdapter(Context context,
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
					R.layout.item_myself_collect_predit, null);

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
			System.out.println("截取的年===================" + time_year
					+ "街区的月为========" + time_month);

			viewholer.textview_pre_year.setText(time_year);
			viewholer.textview_pre_date.setText(time_month);
			host = ((MystudentCollectActivity) mContext);
			convertView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					try {
						
						host.cancelProdictCollection(position);
					} catch (Exception e) {
					}
					return true;
				}
			});
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					host.onItemClickProdictCollection(position);
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		TextView textview_pre_date;
		TextView textview_pre_year;
		TextView textview_pre_title;
	}
	
	public void reset(List<HashMap<String, Object>> list) {
		this.mlist = list;
		notifyDataSetChanged();
	}

}
