package com.lels.student.connectionclass.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.history.InClass;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HisVotesAdapter extends BaseAdapter {
	private List<InClass> mlist;
	private Context context;
	private int percent;
	private String ownVote;
	
	
	public HisVotesAdapter(List<InClass> mlist, Context context) {
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_dialog_vote_end, null);
			holder = new ViewHolder();
			holder.item_votedetails_txt_choose = (TextView) convertView.findViewById(R.id.item_votedetails_txt_choose);
			holder.item_votedetails_txt_choose_rate = (TextView) convertView.findViewById(R.id.item_votedetails_txt_choose_rate);
			holder.item_votecontent_txt  = (TextView) convertView.findViewById(R.id.item_votecontent_txt);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		ownVote = mlist.get(position).getOwnVote();
		if (ownVote.equals("1")) {
			holder.item_votedetails_txt_choose.setBackgroundResource(R.drawable.own_toupiao);

		}else{
			
			holder.item_votedetails_txt_choose.setBackgroundResource(R.drawable.other_toupiao);
		}
		holder.item_votecontent_txt.setText(mlist.get(position).getOptionDesc());
		//获取屏幕的高和宽
		WindowManager wm = ((Activity) context).getWindowManager();
		    int screen_width = wm.getDefaultDisplay().getWidth();
		    int screen_heigth = wm.getDefaultDisplay().getHeight();
		    System.out.println("屏幕的高和宽====="+screen_heigth+"==="+screen_width);
		    percent = Integer.parseInt(mlist.get(position).getVoteNum());
			LayoutParams lp = 	holder.item_votedetails_txt_choose.getLayoutParams();
			lp.width = (int) ((int) (screen_width*(percent*0.01)));
			holder.item_votedetails_txt_choose.setLayoutParams(lp);
		holder.item_votedetails_txt_choose_rate.setText(mlist.get(position).getVoteNum()+"%");

		return convertView;
	}
	class ViewHolder {
		private TextView item_votedetails_txt_choose,
				item_votedetails_txt_choose_rate,
		item_votecontent_txt;
	}
}
