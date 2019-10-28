package com.lels.student.studyonline.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.strudentlelts.R;
import com.lels.student.studyonline.DataStudyDetailActivity;
import com.lidroid.xutils.BitmapUtils;

public class StudyOnlineStudydataAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	Context mContext;
	List<HashMap<String, Object>> mlist;

	private BitmapUtils bitmaputil;
	private String filetype;
	public StudyOnlineStudydataAdapter(Context context,
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
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewholer;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_data_studydata, null);

			viewholer = new ViewHolder();
			viewholer.imageview_studydata_item_type = (ImageView) convertView
					.findViewById(R.id.imageview_studydata_item_type);

			viewholer.textview_studydata_item_teachername = (TextView) convertView
					.findViewById(R.id.textview_studydata_item_teachername);
			viewholer.textview_studydata_item_title = (TextView) convertView
					.findViewById(R.id.textview_studydata_item_title);
			viewholer.textview_studydata_item_time = (TextView) convertView
					.findViewById(R.id.textview_studydata_item_time);
			viewholer.textview_studydate_type = (TextView) convertView
					.findViewById(R.id.textview_studydate_type);

			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}

		// filetype = 文件类型（doc等）
		// uid = 用户ID
		// teachername = 教师姓名
		// name = 标题
		// Mate_ID = 材料ID
		// type = 分类标签（听力等）
		// createtime = 创建日期
		// url = 点击打开网址

		if (mlist.size() > 0) {

			viewholer.textview_studydata_item_title.setText(mlist.get(position)
					.get("name").toString());

			viewholer.textview_studydata_item_time.setText(mlist.get(position)
					.get("createtime").toString());
			if (mlist.get(position).get("type").toString().equals("null")) {
				viewholer.textview_studydate_type.setText("");
			} else {
				viewholer.textview_studydate_type.setText(mlist.get(position)
						.get("type").toString());
			}
			if (mlist.get(position).get("teachername").toString().equals("null")) {
				viewholer.textview_studydata_item_teachername.setText("");
			} else {
				viewholer.textview_studydata_item_teachername.setText(mlist
						.get(position).get("teachername").toString());
			}
			
			
			filetype = mlist.get(position).get("filetype").toString();

			System.out.println("filetype 文件类型 === " + filetype);
			if (filetype.equals("doc") || filetype.equals("docx")) {
				viewholer.imageview_studydata_item_type
						.setBackgroundResource(R.drawable.word);
			} else if (filetype.equals("xls") || filetype.equals("xlsx")) {
				viewholer.imageview_studydata_item_type
						.setBackgroundResource(R.drawable.excel);
			} else if (filetype.equals("ppt") || filetype.equals("pptx")) {
				viewholer.imageview_studydata_item_type
						.setBackgroundResource(R.drawable.ppt);
			} else if (filetype.equals("PDF") || filetype.equals("pdf")
					|| filetype.equals("Pdf")) {
				viewholer.imageview_studydata_item_type
						.setBackgroundResource(R.drawable.pdf);
			} else if (filetype.equals("mp4") || filetype.equals("mp4")
					|| filetype.equals("Mp4") || filetype.equals("flv")
					|| filetype.equals("Flv") || filetype.equals("FLV")
					|| filetype.equals("wmv") || filetype.equals("Wmv")
					|| filetype.equals("WMV")) {
				viewholer.imageview_studydata_item_type
				.setBackgroundResource(R.drawable.video);
			}

			// bitmaputil.display(viewholer.imageview_studydata_item_type,
			// mlist.get(position).get("videoThumbnail").toString());
		}

		// convertView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent();
		// intent.setClass(mContext, DataStudyDetailActivity.class);
		// mContext.startActivity(intent);
		// }
		// });

		return convertView;
	}

	class ViewHolder {
		ImageView imageview_studydata_item_type; // 图片类型

		TextView textview_studydata_item_title;// 标题
		TextView textview_studydata_item_teachername;// 老师名字
		TextView textview_studydata_item_time;// 时间
		TextView textview_studydate_type;// 文字类型
	}

}
