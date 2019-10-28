package com.lels.student.connectionclass.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.lels.student.connectionclass.adapter.ConnectionStartTestAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PlayBackTestAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> mlist;
	private Context context;

	/**
	 * @param mlist
	 * @param context
	 */
	public PlayBackTestAdapter(List<HashMap<String, Object>> mlist,
			Context context) {
		super();
		this.mlist = mlist;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.isEmpty() ? 0 : mlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_playback_test, null);
			holder = new ViewHolder();
			holder.txt_year_playback_test = (TextView) convertView.findViewById(R.id.txt_year_playback_test);
			holder.txt_month_playback_test = (TextView) convertView.findViewById(R.id.txt_month_playback_test);
			holder.txt_correct_playback_test = (TextView) convertView.findViewById(R.id.txt_correct_playback_test);
			holder.txt_wrong_num_playback_test = (TextView) convertView.findViewById(R.id.txt_wrong_num_playback_test);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();

		}
			//        "rightRate": "25%",
			//        "totalCnt": 4,
			//        "wrongCnt": 3,
			//        "EX_ID": 3284,
			//        "doExTime": "2015 11-02"
		String totalCnt = mlist.get(position).get("totalCnt").toString();
		String wrongCnt = mlist.get(position).get("wrongCnt").toString();
		String rightRate =  mlist.get(position).get("rightRate").toString();
		holder.txt_correct_playback_test.setText(rightRate);
		holder.txt_wrong_num_playback_test.setText(wrongCnt+"/"+totalCnt);
		
		String doExTime =  mlist.get(position).get("doExTime").toString();
		String []  newdata  = doExTime.split("-");
//		String[] split = doExTime.split("\\s+");
//		String year = split[0];
//		String month = split[1];
		System.out.println("新日期==doExTime==="+doExTime+newdata[0]+newdata[1]+newdata[2]);
		holder.txt_year_playback_test.setText(newdata[0]);
		holder.txt_month_playback_test.setText(newdata[1]+"/"+newdata[2]);
		

		
		return convertView;
	}

	class ViewHolder {
		private TextView txt_year_playback_test, txt_month_playback_test,
				txt_correct_playback_test, txt_wrong_num_playback_test;
	}

}
