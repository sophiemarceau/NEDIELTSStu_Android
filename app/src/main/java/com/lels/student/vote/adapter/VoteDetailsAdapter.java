package com.lels.student.vote.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VoteDetailsAdapter extends BaseAdapter{
	
	private List<HashMap<String, Object>> mlist;
	private Context context;
	
	private int percent;
	//判断是否是自己投票
	private int ownVote;

	/**
	 * @param mlist
	 * @param context
	 */
	public VoteDetailsAdapter(List<HashMap<String, Object>> mlist,
			Context context) {
		super();
		this.mlist = mlist;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.isEmpty()?0:mlist.size();
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

	@SuppressLint("NewApi") @Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(context).inflate(R.layout.item_vote_details, null);
			holder = new ViewHolder();
			holder.item_txt_vote_detalis = (TextView) arg1.findViewById(R.id.item_txt_vote_detalis);
			holder.item_txt_vote_content = (TextView) arg1.findViewById(R.id.item_txt_content);
			holder.item_txt_vote_percent = (TextView) arg1.findViewById(R.id.item_txt_percent);
			arg1.setTag(holder);
		}else{
			holder = (ViewHolder) arg1.getTag();
		}
		
		System.out.println("mlist的数据 ====="+mlist);
			//设置百分比
		holder.item_txt_vote_percent.setText(mlist.get(arg0).get("voteNum").toString()+"%");
		//设置内容
		holder.item_txt_vote_content.setText(mlist.get(arg0).get("OptionDesc").toString());
		
		ownVote = Integer.parseInt(mlist.get(arg0).get("ownVote").toString());
		//1是0不是
		if(ownVote == 1){
			 holder.item_txt_vote_detalis.setBackground(context.getResources().getDrawable(R.drawable.own_toupiao));
		}else{
			 holder.item_txt_vote_detalis.setBackground(context.getResources().getDrawable(R.drawable.other_toupiao));
		}
		
		//获取屏幕的高和宽
		WindowManager wm = ((Activity) context).getWindowManager();
		    int screen_width = wm.getDefaultDisplay().getWidth();
		    int screen_heigth = wm.getDefaultDisplay().getHeight();
		    System.out.println("屏幕的高和宽====="+screen_heigth+"==="+screen_width);
		    percent = Integer.parseInt(mlist.get(arg0).get("voteNum").toString());
		    System.out.println("percent====="+percent);
		    LayoutParams lp =     holder.item_txt_vote_detalis.getLayoutParams();
			lp.width = (int) ((int) (screen_width*(percent*0.01)));
		    holder.item_txt_vote_detalis.setLayoutParams(lp);
		   
		
		return arg1;
	}
	
	class ViewHolder{
		private TextView item_txt_vote_detalis;
		private TextView item_txt_vote_content;
		private TextView item_txt_vote_percent;
	}

}
