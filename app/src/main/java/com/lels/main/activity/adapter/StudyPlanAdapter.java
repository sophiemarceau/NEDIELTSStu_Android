package com.lels.main.activity.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;

import android.content.Context;
import android.provider.ContactsContract.Contacts.Data;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StudyPlanAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	Context mContext;
	List<HashMap<String, Object>> mlist;
	private String data_hourandmin;

	private boolean isccc;

	public StudyPlanAdapter(Context context, List<HashMap<String, Object>> list, String data_s, boolean iscurrent) {
		this.mContext = context;
		this.mlist = list;
		this.data_hourandmin = data_s;
		this.isccc = iscurrent;
	}

	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mlist.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_studyplan_main, null);

			holder = new ViewHolder();
			holder.textview_plan_starttime = (TextView) convertView.findViewById(R.id.textview_plan_starttime);
			holder.textview_plan_endtime = (TextView) convertView.findViewById(R.id.textview_plan_endtime);
			holder.textview_plan_title = (TextView) convertView.findViewById(R.id.textview_plan_title);
			holder.textview_plan_class_num = (TextView) convertView.findViewById(R.id.textview_plan_class_num);
			holder.textview_plan_address = (TextView) convertView.findViewById(R.id.textview_plan_address);
			// viewholer.imageview_plan_join = (ImageView) convertView
			// .findViewById(R.id.imageview_plan_join);
			holder.view_redline = (View) convertView.findViewById(R.id.view_redline);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (mlist.size() > 0) {

			// SimpleDateFormat simple = new SimpleDateFormat(
			// "yyyy-MM-dd hh:mm:ss");

			String begintime = mlist.get(position).get("SectBegin").toString();
			String endtime = mlist.get(position).get("SectEnd").toString();
			// if (position == 0) {
			// viewholer.view_redline.setVisibility(View.VISIBLE);
			// } else {
			// viewholer.view_redline.setVisibility(View.INVISIBLE);
			// }
			String begin = getStrTime(begintime);
			String end = getStrTime(endtime);
			if (isccc) {
				// if(ismore(begin,data_hourandmin,end)){
				// viewholer.view_redline.setVisibility(View.VISIBLE);
				// }else{
				// viewholer.view_redline.setVisibility(View.INVISIBLE);
				// }
				/*if (position == 0) {
					holder.view_redline.setVisibility(View.VISIBLE);
				} else {
					holder.view_redline.setVisibility(View.INVISIBLE);
				}*/
				if ((Boolean)mlist.get(position).get("inTime")) {
					holder.view_redline.setVisibility(View.VISIBLE);
				} else {
					holder.view_redline.setVisibility(View.INVISIBLE);
				}
			}
			holder.textview_plan_starttime.setText(getStrTime(begintime));
			holder.textview_plan_endtime.setText(getStrTime(endtime));

			holder.textview_plan_title.setText(mlist.get(position).get("sNameBc").toString());
			// viewholer.textview_plan_class_num.setText(mlist.get(position)
			// .get("sCode").toString());
			holder.textview_plan_class_num.setText("第" + mlist.get(position).get("nLessonNo").toString() + "次课");
			holder.textview_plan_address.setText(
					mlist.get(position).get("sAddress").toString() + mlist.get(position).get("sNameBr").toString());
		}
		return convertView;
	}

	class ViewHolder {
		TextView textview_plan_starttime;// 开始时间
		TextView textview_plan_endtime;// 结束时间
		TextView textview_plan_title;// 标题
		TextView textview_plan_class_num;// 课次号
		TextView textview_plan_address;// 地址
		// ImageView imageview_plan_join;
		View view_redline;
	}

	// 将时间戳转为字符串
	public static String getStrTime(String cc_time) {
		String re_StrTime = null;
		String re_re = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);

		re_StrTime = sdf.format(new Date(lcc_time));
		System.out.println("re_re的长度为========================" + re_StrTime.length());
		re_re = re_StrTime.substring(11, 16);
		System.out.println("re_re===============" + re_re);
		return re_re;
	}

	private boolean ismore(String s, String z, String e) {

		int s_h = Integer.valueOf(s.split(":")[0]);
		int s_m = Integer.valueOf(s.split(":")[1]);

		int z_h = Integer.valueOf(z.split(":")[0]);
		int z_m = Integer.valueOf(z.split(":")[1]);

		int e_h = Integer.valueOf(e.split(":")[0]);
		int e_m = Integer.valueOf(e.split(":")[1]);

		if (z_h > s_h && z_h < e_h) {
			return true;
		} else if (z_h == s_h && z_h == e_h && z_m >= s_m && z_m <= e_m) {
			return true;
		} else if (z_h < e_h && z_m <= e_m) {
			return true;
		} else {
			return false;
		}
	}
}
