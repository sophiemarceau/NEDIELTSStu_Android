
package com.lels.main.activity.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lelts.students.bean.StudentClassroom_info;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class StudentClassAdapter extends BaseAdapter {
	private Context context;
	private List<HashMap<String, Object>> mlist;

	public StudentClassAdapter(Context context,
			List<HashMap<String, Object>> mlist) {
		super();
		this.context = context;
		this.mlist = mlist;
	}
	//刷新list的值
	public void setdatachanges(List<HashMap<String, Object>> mlist
			) {
		this.mlist = mlist;
		this.notifyDataSetChanged();
	}
	

	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.isEmpty() ? 0 : mlist.size();
	}

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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_student_classroom, null);
			holder = new ViewHolder();
			holder.student_name = (TextView) convertView.findViewById(R.id.txt_name_Stuclassroom_coonection);
			holder.student_img = (ImageView) convertView.findViewById(R.id.img_Stuclassroom_coonection);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if (mlist.get(position).get("iconUrl").toString().length()>0&&!"null".equals(mlist.get(position).get("iconUrl").toString())) {
			ImageLoader.getInstance().displayImage(Constants.URL_USERIMG+mlist.get(position).get("iconUrl").toString(),holder.student_img);
			
		}else{
			holder.student_img.setBackgroundResource(R.drawable.mor);
		}
		if(mlist.get(position).get("studentloginstatus").equals("1")){
			holder.student_img.setAlpha(1.0f);
			holder.student_name.setTextColor(Color.BLACK);
		}else{
			holder.student_img.setAlpha(0.5f);
			holder.student_name.setTextColor(context.getResources().getColor(R.color.gray));
		}
		holder.student_name.setText(mlist.get(position).get("studentname").toString());
		return convertView;
	}

}

class ViewHolder {
	TextView student_name;
	ImageView student_img;
}
