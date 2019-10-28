package com.lelts.student.myself.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.lelts.student.myself.MystudentCollectActivity;
import com.lelts.student.myself.adapter.MycollectPublicclassAdapter.ViewHolder;
import com.lelts.student.myself.fragment.DataFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MycollectDataAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	Context mContext;
	List<HashMap<String, Object>> mlist;

	private String teachername;// 老师姓名

	private String type;// 老师职位
	private String filetype;// 文件类型

	private MystudentCollectActivity host;

	public MycollectDataAdapter(Context context,
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

	@SuppressWarnings("null")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewholer;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_myself_collect_data, null);

			viewholer = new ViewHolder();

			viewholer.textview_data_item_datatitle = (TextView) convertView
					.findViewById(R.id.textview_data_item_datatitle);
			viewholer.imageview_data_item_type = (ImageView) convertView
					.findViewById(R.id.imageview_data_item_type);
			viewholer.textview_data_item_teachername = (TextView) convertView
					.findViewById(R.id.textview_data_item_teachername);
			viewholer.textview_data_item_type = (TextView) convertView
					.findViewById(R.id.textview_data_item_type);
			viewholer.textview_data_item_createtime = (TextView) convertView
					.findViewById(R.id.textview_data_item_createtime);

			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}

		//  MF_ID = 收藏主键;
		//    mate_id = 资料表主键;
		//    name = 资料名称;
		//    url = 资料查看地址;
		//    filetype = 资料类型，文件后缀名;
		//    uid = 创建人ID;
		//    createtime = 创建时间;
		//    type = 类型，[口语，听力，写作等];
		//    teachername = 教师名;

		if (mlist.size() > 0) {
			viewholer.textview_data_item_datatitle.setText(mlist.get(position)
					.get("name").toString());
			teachername = mlist.get(position).get("teachername").toString();
			type = mlist.get(position).get("type").toString();

			if (teachername.equals("null") || teachername.equals("")) {
				viewholer.textview_data_item_teachername.setText("");
			} else {
				viewholer.textview_data_item_teachername.setText(mlist
						.get(position).get("teachername").toString());

			}
			if (type.equals("null") || type.equals("")) {
				viewholer.textview_data_item_type.setText("");
			} else {
				viewholer.textview_data_item_type.setText(mlist.get(position)
						.get("type").toString());
			}

			viewholer.textview_data_item_createtime.setText(mlist.get(position)
					.get("createtime").toString());
			filetype = mlist.get(position).get("filetype").toString();

			System.out.println("filetype 文件类型 === " + filetype);
			if (filetype.equals("doc") || filetype.equals("docx")) {
				viewholer.imageview_data_item_type
						.setBackgroundResource(R.drawable.word);
			} else if (filetype.equals("xls") || filetype.equals("xlsx")) {
				viewholer.imageview_data_item_type
						.setBackgroundResource(R.drawable.excel);
			} else if (filetype.equals("ppt") || filetype.equals("pptx")) {
				viewholer.imageview_data_item_type
						.setBackgroundResource(R.drawable.ppt);
			} else if (filetype.equals("PDF") || filetype.equals("pdf")
					|| filetype.equals("Pdf")) {
				viewholer.imageview_data_item_type
						.setBackgroundResource(R.drawable.pdf);
			} else if (filetype.equals("mp4") || filetype.equals("mp4")
					|| filetype.equals("Mp4") || filetype.equals("flv")
					|| filetype.equals("Flv") || filetype.equals("FLV")
					|| filetype.equals("wmv") || filetype.equals("Wmv")
					|| filetype.equals("WMV")) {
				viewholer.imageview_data_item_type
				.setBackgroundResource(R.drawable.video);
			}
			host = ((MystudentCollectActivity) mContext);
			convertView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					try {
				
						host.cancelDataCollection(position);
					} catch (Exception e) {
					}
					return true;
				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					host.onItemClickDataCollection(position);
				}
			});
			

		}
		return convertView;
	}

	class ViewHolder {
		TextView textview_data_item_datatitle;
		TextView textview_data_item_teachername;
		TextView textview_data_item_type;
		TextView textview_data_item_createtime;
		// 图标
		ImageView imageview_data_item_type;
	}
	
	public void reset(List<HashMap<String, Object>> list) {
		this.mlist = list;
		notifyDataSetChanged();
	}

}
