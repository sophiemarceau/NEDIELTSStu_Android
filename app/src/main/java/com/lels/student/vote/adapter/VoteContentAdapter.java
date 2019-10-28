package com.lels.student.vote.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.lelts.tool.CalculateListviewGrideview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VoteContentAdapter extends BaseAdapter{
	private List<HashMap<String, Object>> mlist;
	private Context context;
	
	

	/**
	 * @param mlist
	 * @param context
	 */
	public VoteContentAdapter(List<HashMap<String, Object>> mlist,
			Context context) {
		super();
		this.mlist = mlist;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.isEmpty()?0: mlist.size();
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (arg1==null) {
			arg1 = LayoutInflater.from(context).inflate(R.layout.item_vote_content, null);
			holder = new ViewHolder();
			holder.item_txt_vote_content = (TextView) arg1.findViewById(R.id.item_txt_vote_content);
			
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		
		holder.item_txt_vote_content.setText(mlist.get(arg0).get("OptionDesc").toString());

		return arg1;
	}
	
	class ViewHolder{
		TextView item_txt_vote_content;
	}

}
