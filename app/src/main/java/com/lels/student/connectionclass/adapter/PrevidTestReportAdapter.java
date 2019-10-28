/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年8月3日 
 * 
 *******************************************************************************/
package com.lels.student.connectionclass.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.strudentlelts.R;
import com.example.strudentlelts.R.color;
import com.history.Groups;
import com.history.HisGroups;
import com.history.HisVotes;
import com.history.History;
import com.history.StudentOwnHistoryExerciseReport;
import com.lelts.tool.CalculateListviewGrideview;
import com.lelts.tool.RoundProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年8月3日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class PrevidTestReportAdapter extends BaseAdapter {
	private Context Context;
	// private List<HashMap<String, Object>> mlist;
	private List<History> list;
	private int TYPE_1=0;
	private int TYPE_2=1;
	private int TYPE_3=2;
	private ViewHolder holder;
	private ViewHolder1 holder1;
	private ViewHolder2 holder2;
	private List<Groups> groupList;

	public PrevidTestReportAdapter(android.content.Context context,
			List<History> list) {
		super();
		Context = context;
		this.list = list;
	}

	/*
	 * public PrevidTestReportAdapter(android.content.Context context,
	 * List<HashMap<String, Object>> mlist) { super(); Context = context;
	 * this.mlist = mlist; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.isEmpty() ? 0 : list.size();
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int p = list.get(position).type;
		
		System.out.println("p----"+p);
		if (p == 0) {
			return TYPE_1;
		} else if (p == 1) {
			return TYPE_2;
		} else if (p==2){
			return TYPE_3;
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		holder = null;
		holder1 = null;
		holder2 = null;
		
		int type = getItemViewType(position);
//		int type = 2;
		

		if (convertView == null) {
			System.out.println(type);
			switch (type) {
			case 0:
				
				convertView = LayoutInflater.from(Context).inflate(
						R.layout.item_previdtest_report, null);
				holder = new ViewHolder();
				holder.txt_classnum = (TextView) convertView
						.findViewById(R.id.txt_num_provoid_report);
				holder.txt_paperName = (TextView) convertView
						.findViewById(R.id.txt_classescontent_provoid_report);
				holder.txt_CostTime = (TextView) convertView
						.findViewById(R.id.textView4);
				holder.bar_Accuracy = (RoundProgressBar) convertView
						.findViewById(R.id.roundProgressBar2_true_prevoid);
				holder.true_pro3 = (RoundProgressBar) convertView
						.findViewById(R.id.roundProgressBar3_true_prevoid);
				convertView.setTag(holder);

				break;
			case 1:
				holder1 = new ViewHolder1();
				convertView = LayoutInflater.from(Context).inflate(
						R.layout.item_glistview, null);
				holder1.tv = (TextView) convertView.findViewById(R.id.tv);
				holder1.textView = (TextView) convertView
						.findViewById(R.id.textView);
				holder1.tv_group = (TextView) convertView
						.findViewById(R.id.tv_group);
				holder1.tv_total = (TextView) convertView
						.findViewById(R.id.tv_total);
				holder1.gridview = (GridView) convertView
						.findViewById(R.id.gridView);
				convertView.setTag(holder1);
				break;
			case 2:
				holder2 = new ViewHolder2();
				convertView = LayoutInflater.from(Context).inflate(
						R.layout.hisvote, null);
				holder2.listview = (ListView) convertView.findViewById(R.id.listView);
				holder2.textView =  (TextView) convertView.findViewById(R.id.textView);
				holder2.edittext_contactus =  (EditText) convertView.findViewById(R.id.edittext_contactus);
				
				convertView.setTag(holder2);
				break;
			default:
				break;
			}

		} else {
			switch (type) {
			case 0:
				holder = (ViewHolder) convertView.getTag();
				break;
			case 1:
				holder1 = (ViewHolder1) convertView.getTag();
				break;
			case 2:
				holder2 = (ViewHolder2) convertView.getTag();
				break;

			default:
				break;
			}

		}

		// 设置数据
		switch (type) {
		case 0:
			
			StudentOwnHistoryExerciseReport report = (StudentOwnHistoryExerciseReport) list
					.get(position);
			// 答题
			holder.txt_classnum.setText(position + 1 + "");

			holder.txt_paperName.setText(report.getPaperName());
			holder.txt_CostTime.setText(report.getCostTime());
			holder.bar_Accuracy.setProgress(Integer.parseInt(report
					.getAccuracy().substring(0,
							report.getAccuracy().length() - 1)));
			holder.bar_Accuracy.setTextColor(color.describe_color);

			break;
		case 1:
		
			// 分组
			HisGroups hisgroups = (HisGroups) list.get(position);
			holder1.tv_group.setText("第"+hisgroups.getGroupNum()+"组    ("+hisgroups.getGroupCnt()+")");
			holder1.tv_total.setText("共"+hisgroups.getGroupCnt()+"组");
			holder1.textView.setText((position+1)+"");
			Group_gridAdapter adapter = new Group_gridAdapter(Context, hisgroups.getMyGroup());
			holder1.gridview.setAdapter(adapter);
			setListViewHeightBasedOnChildren1(holder1.gridview);
			
			break;
		case 2:
			// 投票
			HisVotes vote = (HisVotes) list.get(position);
			holder2.textView.setText((position+1)+"");
			holder2.edittext_contactus.setText(vote.getSubject());
			HisVotesAdapter madapter = new HisVotesAdapter(vote.getOpts(), Context);
			holder2.listview.setAdapter(madapter);
			CalculateListviewGrideview.setListViewHeightBasedOnChildren(holder2.listview);
			break;

		default:
			break;
		}

		return convertView;
	}

	class ViewHolder {
		TextView txt_paperName, txt_CostTime, txt_classnum;
		RoundProgressBar bar_Accuracy, true_pro3;
	}

	class ViewHolder1 {
			TextView tv, textView, tv_group, tv_total;
			GridView gridview;
	}

	class ViewHolder2 {
		ListView listview;
		TextView textView;
		EditText edittext_contactus;
	}
	//测量gridview
	public static void setListViewHeightBasedOnChildren1(GridView listView) {  
        // 获取listview的adapter  
           ListAdapter listAdapter = listView.getAdapter();  
           if (listAdapter == null) {  
               return;  
           }  
           // 固定列宽，有多少列  
           int col = 4;// listView.getNumColumns();  
           int totalHeight = 0;  
           // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，  
           // listAdapter.getCount()小于等于8时计算两次高度相加  
           for (int i = 0; i < listAdapter.getCount(); i += col) {  
            // 获取listview的每一个item  
               View listItem = listAdapter.getView(i, null, listView);  
               listItem.measure(0, 0);  
               // 获取item的高度和  
               totalHeight += listItem.getMeasuredHeight();  
           }  
      
           // 获取listview的布局参数  
           ViewGroup.LayoutParams params = listView.getLayoutParams();  
           // 设置高度  
           params.height = totalHeight;  
           // 设置margin  
           ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);  
           // 设置参数  
           listView.setLayoutParams(params);  
       }  
}