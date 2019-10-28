package com.lels.main.activity.adapter;

import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.strudentlelts.R;

@SuppressWarnings("deprecation")
public class GalleryAdapter extends BaseAdapter {
	Context mContext;
	private int selectItem;
//	private String[] str = { "在线学习", "聊天室", "考前预测", , "学习计划" };
	private String[] str = { "任务动态", "聊天室", "考前预测", "在线学习", "学习计划" };
	private int drawable1[] ;
	private int screenWidth ;
    private int screenHeight ; 

	public GalleryAdapter(Context mContext, int[] drawable1,int screenWidth,int screenHeight) {
		super();
		this.mContext = mContext;
		this.drawable1 = drawable1;
		this.screenWidth = screenWidth/3;
		this.screenHeight = screenHeight/3;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setSelectItem(int selectItem) {

		if (this.selectItem != selectItem) {
			this.selectItem = selectItem;
			notifyDataSetChanged();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_garrley_star, null);
			holder.pic = (ImageView) convertView
					.findViewById(R.id.gallery_item_img);
			holder.text = (TextView) convertView
					.findViewById(R.id.gallery_item_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.pic.setImageResource(drawable1[position%drawable1.length]);
		holder.text.setText(str[position%str.length]);
		if (position < 0) {
			position = position + drawable1.length;
		}
		
		if (selectItem == position) {
			Animation animation = AnimationUtils.loadAnimation(mContext,
					R.anim.my_scale_action);
			convertView.startAnimation(animation);
			
		} else {
			 
			convertView.setLayoutParams(new Gallery.LayoutParams(screenWidth, screenHeight));

		}
		return convertView;
	}

	class ViewHolder {
		private ImageView pic;
		private TextView text;
	}
}