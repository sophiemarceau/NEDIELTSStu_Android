package com.lels.student.connectionclass.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.history.Groups;
import com.lels.constants.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class Group_gridAdapter extends BaseAdapter {
	private Context context;
	private List<Groups> list;
	
	public Group_gridAdapter(Context context, List<Groups> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.isEmpty() ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_groupdialog, null);
			holder.group_stuname = (TextView) convertView.findViewById(R.id.group_stuname);
			holder.classes_teacher_photo = (ImageView) convertView.findViewById(R.id.classes_teacher_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String ioc = list.get(position).getIconUrl();
		String name = list.get(position).getsName();
		holder.group_stuname.setText(name);
		if (ioc.length()>0||!ioc.equals("null")) {
			ImageLoader.getInstance().displayImage(Constants.URL_USERIMG+ioc, holder.classes_teacher_photo);
		}else{
			holder.classes_teacher_photo.setBackgroundResource(R.drawable.mor);
		}
		return convertView;
	}

	class ViewHolder {
		TextView group_stuname;
		ImageView classes_teacher_photo;

	}

}
