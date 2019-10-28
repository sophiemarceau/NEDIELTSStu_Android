/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015-10-21 
 * 
 *******************************************************************************/
package com.lels.student.vote.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.history.InClass;
import com.lels.constants.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015-10-21
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class DialogVoteEndAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> mlist;
	private Context context;
	private int percent;
	private String ownVote;

	/**
	 * @param mlist
	 * @param context
	 */
	public DialogVoteEndAdapter(List<HashMap<String, Object>> mlist,
			Context context) {
		super();
		this.mlist = mlist;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.isEmpty() ? 0 : mlist.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mlist.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@SuppressLint("NewApi") @Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
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
		ownVote = mlist.get(arg0).get("ownVote").toString();
		if (ownVote.equals("1")) {
			holder.item_votedetails_txt_choose.setBackground(context.getResources().getDrawable(R.drawable.own_toupiao));

		}else{
			
			holder.item_votedetails_txt_choose.setBackground(context.getResources().getDrawable(R.drawable.other_toupiao));
		}
		holder.item_votecontent_txt.setText(mlist.get(arg0).get("OptionDesc").toString());
		//获取屏幕的高和宽
		WindowManager wm = ((Activity) context).getWindowManager();
		    int screen_width = wm.getDefaultDisplay().getWidth();
		    int screen_heigth = wm.getDefaultDisplay().getHeight();
		    System.out.println("屏幕的高和宽====="+screen_heigth+"==="+screen_width);
		    percent = Integer.parseInt(mlist.get(arg0).get("voteNum").toString());
			LayoutParams lp = 	holder.item_votedetails_txt_choose.getLayoutParams();
			lp.width = (int) ((int) (screen_width*(percent*0.01)));
			holder.item_votedetails_txt_choose.setLayoutParams(lp);
		holder.item_votedetails_txt_choose_rate.setText(mlist.get(arg0).get("voteNum").toString()+"%");

		return convertView;
	}

	class ViewHolder {
		private TextView item_votedetails_txt_choose,
				item_votedetails_txt_choose_rate,
		item_votecontent_txt;
	}
}
