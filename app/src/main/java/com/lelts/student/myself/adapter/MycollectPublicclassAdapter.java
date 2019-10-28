package com.lelts.student.myself.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.lelts.student.myself.MystudentCollectActivity;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MycollectPublicclassAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private SimpleDateFormat sf = null;
	Context mContext;
	List<HashMap<String, Object>> mlist;
	private MystudentCollectActivity host;
	private BitmapUtils bitmaputil;
	private int countsecond,minute,second;
	public MycollectPublicclassAdapter(Context context,
			List<HashMap<String, Object>> list) {
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
					R.layout.item_myself_collect_publicclass, null);

			viewholer = new ViewHolder();
			viewholer.textview_message_title = (TextView) convertView
					.findViewById(R.id.textview_data_item_title);
			viewholer.imageview_data_item_type = (ImageView) convertView
					.findViewById(R.id.imageview_data_item_type);
			viewholer.textview_data_item_time = (TextView) convertView
					.findViewById(R.id.textview_data_item_time);
			viewholer.textview_data_item_browsenum = (TextView) convertView
					.findViewById(R.id.textview_data_item_browsenum);
			viewholer.textview_data_item_createtime = (TextView) convertView
					.findViewById(R.id.textview_data_item_createtime);

			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}

		if (mlist.size() > 0) {
			viewholer.textview_message_title.setText(mlist.get(position)
					.get("Name").toString());

			String url = mlist.get(position).get("VideoThumbNail").toString();
			System.out.println("VideoThumbNail是否有数=============" + url);
			if (!url.equalsIgnoreCase("null")) {
				System.out.println("VideoThumbNail有数据 显示图片=============");
				bitmaputil.display(viewholer.imageview_data_item_type, url);
			}
			//设置浏览次数
			viewholer.textview_data_item_browsenum.setText(mlist.get(position)
					.get("ReadCount").toString()+"次");
			/*将字符串转为时间戳*/
			
			sf = new SimpleDateFormat("yyyy-MM-dd");
			long time = Long.valueOf(mlist.get(position).get("CreateTime")
					.toString());
			String nowtime = sf.format(new Date(time));
			System.out.println("nowtime========>"+nowtime);
			//设置时间
			viewholer.textview_data_item_createtime.setText(nowtime);
			//设置分钟
			if(mlist.get(position).get("VideoDuration").toString().equals("null")){
				viewholer.textview_data_item_time.setText("0秒");
			}else{
			countsecond = Integer.parseInt(mlist.get(position).get("VideoDuration").toString());
			minute = countsecond/60;
			second = countsecond%60;
			viewholer.textview_data_item_time.setText(minute+"分"+second+"秒");
			}
			host = ((MystudentCollectActivity) mContext);
			convertView.setOnLongClickListener(new OnLongClickListener() {
				
			

				@Override
				public boolean onLongClick(View v) {
					try {
						
						host.cancelPublicCollection(position);
					} catch (Exception e) {
					}
					return true;
				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					host.onItemClickPublicCollection(position);
				}
			});
			
			
		}
		return convertView;
	}

	class ViewHolder {
		TextView textview_message_title;
		ImageView imageview_data_item_type;
		TextView textview_data_item_time;
		TextView textview_data_item_browsenum;
		TextView textview_data_item_createtime;

	}
	
	public void reset(List<HashMap<String, Object>> list) {
		this.mlist = list;
		notifyDataSetChanged();
	}

}
