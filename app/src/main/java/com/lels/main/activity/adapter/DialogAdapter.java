package com.lels.main.activity.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lelts.tool.ImageLoder;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogAdapter extends BaseAdapter {
	private Context mContext;
	private List<HashMap<String, Object>> mList;
	
	public DialogAdapter(Context mContext, List<HashMap<String, Object>> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
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
		ViewHolder holder = null;
		if (convertView==null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_groupdialog, null);
			holder.stuImg = (ImageView) convertView.findViewById(R.id.classes_teacher_photo);
			holder.stuTv = (TextView) convertView.findViewById(R.id.group_stuname);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		    holder.stuTv.setText(mList.get(position).get("sName").toString());
		   
		    if (!mList.get(position).get("IconUrl").toString().equals("")||!mList.get(position).get("IconUrl").toString().equals("null")) {
		    	ImageLoader.getInstance().displayImage(Constants.URL_USERIMG+mList.get(position).get("IconUrl").toString(),holder.stuImg);
		    	System.out.println("11111--"+Constants.URL_USERIMG+mList.get(position).get("IconUrl").toString());
		    }else{
		    	System.out.println("00000--"+Constants.URL_USERIMG+mList.get(position).get("IconUrl").toString());
		    }
		return convertView;
	}
class  ViewHolder{
	ImageView stuImg;
	TextView stuTv;
}
}
