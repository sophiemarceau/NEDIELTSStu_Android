package com.lels.student.studyonline.adapter;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lels.student.studyonline.DataPublicclassDetailActivity;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class StudyOnlinepublicclassAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	Context mContext;
	List<HashMap<String, Object>> mlist;
	
	private BitmapUtils bitmaputil;

	public StudyOnlinepublicclassAdapter(Context context, List<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mlist = list;
		bitmaputil = new BitmapUtils(mContext);
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
					R.layout.item_data_maindata, null);

			viewholer = new ViewHolder();
			viewholer.imageview_data_item_type = (ImageView) convertView
					.findViewById(R.id.imageview_data_item_type);
			
			viewholer.textview_data_item_title = (TextView) convertView
					.findViewById(R.id.textview_data_item_title);
			viewholer.textview_data_item_time = (TextView) convertView
					.findViewById(R.id.textview_data_item_time);
			viewholer.textview_data_item_browsenum = (TextView) convertView
					.findViewById(R.id.textview_data_item_browsenum);
			viewholer.textview_create_time = (TextView) convertView
			.findViewById(R.id.textview_create_time);
			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}
		
		// name = 标题
		// readcount = 浏览次数
		// videothumbnail = 缩略图网址
		// mate_id = 材料ID
		// createtime = 创建时间
		// videoduration = 视频时长（秒）
		
//		System.out.println("mlist的长度为======="+mlist.size()+mlist.get(position).get("name"));
		

		if (mlist.size() > 0) {
			
//			if(mlist.get(position).containsKey("videoduration")){
				
			
//			}
			
//			if(mlist.get(position).containsKey("videoduration")){
				
			
//			}
			
			if(!mlist.get(position).get("videoduration").toString().equalsIgnoreCase("null")){
				String timelength = mlist.get(position).get("videoduration").toString();
				if(Integer.valueOf(timelength) < 60){
					viewholer.textview_data_item_time.setText(timelength+"秒");
				}else{
					int minutes = Integer.valueOf(timelength)/60;
					int seconds = Integer.valueOf(timelength)%60;
					viewholer.textview_data_item_time.setText(minutes+"分"+seconds+"秒");
				}
			}else{
				viewholer.textview_data_item_time.setText("0秒");
			}
			
			
			
			viewholer.textview_data_item_title.setText(mlist.get(position).get("name").toString());
			viewholer.textview_create_time.setText(mlist.get(position).get("createtime").toString());
			if(mlist.get(position).get("readcount").toString().equals("null")){
				viewholer.textview_data_item_browsenum.setText("0次");
			}else{
				System.out.println(mlist.get(position).get("readcount").toString()+"次");
				viewholer.textview_data_item_browsenum.setText(mlist.get(position).get("readcount").toString()+"次");
			}
//			System.out.println("获取的图片路径为===="+mlist.get(position).get("videoThumbnail").toString());
			if(!mlist.get(position).get("videoThumbnail").toString().equalsIgnoreCase("null")){
				bitmaputil.display(viewholer.imageview_data_item_type, mlist.get(position).get("videoThumbnail").toString());
			}else{
				viewholer.imageview_data_item_type.setImageResource(R.drawable.icon_studyonline_default);
			}
			
			
		}
		

//		convertView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(mContext, DataPublicclassDetailActivity.class);
//				Bundle b = new Bundle();
//				b.putString("mateId", mlist.get(position).get("Mate_ID").toString());
//				intent.putExtras(b);
//				mContext.startActivity(intent);
//			}
//		});
		
		
		return convertView;
	}

	class ViewHolder {
		ImageView imageview_data_item_type;
		TextView textview_data_item_title;
		TextView textview_data_item_time;
		TextView textview_data_item_browsenum;
		TextView textview_create_time;
	}

	
}
