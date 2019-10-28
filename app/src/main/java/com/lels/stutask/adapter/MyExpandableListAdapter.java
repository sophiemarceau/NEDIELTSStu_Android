package com.lels.stutask.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.strudentlelts.R;
import com.lels.bean.ListPeople;
import com.lels.constants.Constants;
import com.lels.student.starttask.activity.Renwu_contentActivity;
import com.lels.student.studyonline.DataPublicclassDetailActivity;
import com.lels.student.studyonline.DataPublicclassDetailTestActivity;
import com.lels.student.studyonline.DataStudyDetailActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015-8-17
 * 作者:	 Mr_Wang
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人： 
 *    修改内容：
 * </pre>
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

	private List<ListPeople> list;
	private Context context;
	private int childPosition1;
	private Map<Integer, String> state;
	private SharedPreferences share;
	private HashMap<Integer, Integer> map_ty;

	public MyExpandableListAdapter(Context context, List<ListPeople> list,
			SharedPreferences share) {
		this.context = context;
		this.list = list;
		this.share = share;
		if (this.list == null) {
			return;
		}

		state = new HashMap<Integer, String>();
		map_ty = new HashMap<Integer, Integer>();
	}

	public void Mynotify(List<ListPeople> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.get(groupPosition).getList()
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		this.childPosition1 = childPosition;

		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.childview, null);
			holder.img = (ImageView) convertView
					.findViewById(R.id.task_renwuliebiao_icon);
			holder.iocimag = (ImageView) convertView
					.findViewById(R.id.task_num_icon);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.task_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String text = list.get(groupPosition).getList().get(childPosition)
				.getRefData().getName();
		holder.tv_title.setText(text);
		
		/**
		 * 上下线显示
		 */
		View taskUp = (View) convertView.findViewById(R.id.task_line_up);
		View taskDown = (View) convertView.findViewById(R.id.task_line_down);
		if (list.get(groupPosition).getList().size() > 1) {
			if (childPosition == 0) {
				taskUp.setVisibility(View.INVISIBLE);
				taskDown.setVisibility(View.VISIBLE);
			} else if (childPosition == list.get(groupPosition).getList()
					.size() - 1) {
				taskDown.setVisibility(View.INVISIBLE);
				taskUp.setVisibility(View.VISIBLE);
			} else {
				taskDown.setVisibility(View.VISIBLE);
				taskUp.setVisibility(View.VISIBLE);
			}
		} else {
			taskUp.setVisibility(View.INVISIBLE);
			taskDown.setVisibility(View.INVISIBLE);
		}
		/**
		 * 类型判断
		 */
		int ty = list.get(groupPosition).getList().get(childPosition)
				.getTaskType();
		map_ty.put(childPosition1, ty);// 将类型保存在map中；
		switch (map_ty.get(childPosition1)) {
		case 1:
			holder.iocimag.setImageResource(R.drawable.mokao);
			break;
		case 2:
			holder.iocimag.setImageResource(R.drawable.lianxi);
			break;
		case 3:
			holder.iocimag.setImageResource(R.drawable.ziliao);
			break;
		default:
			break;
		}

		if (childPosition1 == 0) {
			holder.iocimag.setVisibility(View.VISIBLE);
		} else {
			if (list.get(groupPosition).getList().get(childPosition)
					.getTaskType() == list.get(groupPosition).getList()
					.get(childPosition - 1).getTaskType()) {
				holder.iocimag.setVisibility(View.INVISIBLE);
			} else {
				holder.iocimag.setVisibility(View.VISIBLE);
			}
		}

		/**
		 * 判断完成状态
		 */
		if (list.get(groupPosition).getList().get(childPosition).getTF_ID() == null) {
			holder.img.setImageResource(R.drawable.morenhui);
			if (list.get(groupPosition).getList().get(childPosition).isChacked()==true) {
				holder.img.setImageResource(R.drawable.renwuliebiao_hong);
				System.out.println("----快快变红---");
			} else {
				holder.img.setImageResource(R.drawable.morenhui);
				System.out.println("----变---灰---");
			}
		} else {
			holder.img.setImageResource(R.drawable.renwuliebiao_hong);
		}
		
		return convertView;
	}

	
	class ViewHolder {
		ImageView img, iocimag;
		TextView tv_title;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return list == null ? 0 : list.get(groupPosition).getList().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list == null ? null : list.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolderparent holderparent;
		if (convertView == null) {
			holderparent = new ViewHolderparent();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.groupview, null);
			holderparent.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holderparent);
		} else {
			holderparent = (ViewHolderparent) convertView.getTag();
		}
		holderparent.text.setText(list.get(groupPosition).getDate());
		return convertView;
	}

	class ViewHolderparent {
		TextView text;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
