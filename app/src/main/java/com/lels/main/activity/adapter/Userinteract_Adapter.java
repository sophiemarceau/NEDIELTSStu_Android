package com.lels.main.activity.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.strudentlelts.R;
import com.lelts.students.bean.User_interact_info;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Userinteract_Adapter extends BaseAdapter {
	private List<User_interact_info> mList;
	private Context mContext;
	private static SimpleDateFormat sf = null;

	public Userinteract_Adapter(List<User_interact_info> mList, Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.user_interact_item, null);
			holder.classname = (TextView) convertView
					.findViewById(R.id.txt_user_interact_name);
			holder.calsscount = (TextView) convertView
					.findViewById(R.id.txt_user_interact_class_count);
			holder.classtime = (TextView) convertView
					.findViewById(R.id.txt_user_interact_text_time);
			holder.textcount = (TextView) convertView
					.findViewById(R.id.txt_user_interact_count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		/*将字符串转为时间戳*/
		
//		sf = new SimpleDateFormat("yyyy-MM-dd");
//		long time = Long.valueOf(mList.get(position).getClasstime());
//		String nowtime = sf.format(new Date(time));
//		System.out.println("nowtime========>"+nowtime);
		System.out.println("============"+mList.get(position).getClassname());
		holder.classname.setText(mList.get(position).getClassname());
		holder.calsscount.setText("第"+mList.get(position).getClasscount()+"课");
		holder.classtime.setText(mList.get(position).getClasstime());
		holder.textcount.setText(mList.get(position).getTextcount()+"");
		return convertView;
	}
	
	
		
	class ViewHolder {
		TextView classname, calsscount, classtime, textcount;
	}
}
