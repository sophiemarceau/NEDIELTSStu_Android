/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015��7��17�� 
 * 
 *******************************************************************************/ 
package com.lels.student.connectionclass.adapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.lelts.students.bean.StuAnswerInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ConnectionSavetestAdapter extends BaseAdapter{

	private Context context;
	private List<StuAnswerInfo> mlist;
	private String answer;
	private StringBuffer appendsb;
	public ConnectionSavetestAdapter(Context context,
			List<StuAnswerInfo> mlist) {
		super();
		this.context = context;
		this.mlist = mlist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.isEmpty()?0:mlist.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_connection_savetest, null);
			holder.txt_num = (TextView) convertView.findViewById(R.id.item_txt_num_connection_savetest);
			holder.txt_answer = (TextView) convertView.findViewById(R.id.item_txt_answer_connection_savetest);
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		// 格式化字符串  AB,B,NULL
		if(mlist.get(position).getAnswer().toString() != null&&!mlist.isEmpty()&&mlist.size()!=0&&!mlist.get(position).getAnswer().toString().equals("")){
			String getlast=mlist.get(position).getAnswer().toString();
			//判断字符串是否包含分号  ；
			if(getlast.indexOf("`")!=-1){
				appendsb = new StringBuffer();
				//包含多个答案
				String[] getmore = getlast.split("`");
				System.out.println("getmore======"+getmore.toString());
//				List list = Arrays.asList(arr);
				for (int i = 0; i < getmore.length; i++) {
					String[] getone=getmore[i].split("\\|");
//					System.out.println("getone======"+getone[i]);
						String getg=getone[1];
						System.out.println("获取多个答案==="+getg);
						if(getg.equals("")||getg.equals(null)){
							appendsb.append("");
						}else{
							answer = getg;
							appendsb.append(answer).append(" ; "); 
							holder.txt_answer.setText(appendsb.substring(0,appendsb.length()-2));
						}
						
						System.out.println("moreanswer............"+appendsb);
						
				}
//				if(appendsb.equals("NULL")){
//					holder.txt_answer.setText("");
//				}else{
//					
//					
//				}
				
			}else{
				//只有一个答案
				String[] getone=getlast.split("\\|");
				System.out.println("getone======"+getone);
					String getg=getone[1];
					String dd=getg.replace("[", "");
					answer = dd;
					System.out.println("answer............"+answer);
					if(answer.equals("NULL")){
						holder.txt_answer.setText("");
					}else{
						
						holder.txt_answer.setText(answer);
					}
			}
		
		}else{
			holder.txt_answer.setText("");
		}
		holder.txt_num.setText("第"+mlist.get(position).getCode()+"题 : ");
		
		return convertView;
	}
	
	class ViewHolder{
		private TextView txt_num,txt_answer;
	}

}
