package com.lels.student.starttask.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.example.strudentlelts.R;
import com.lels.bean.Learn;
import com.lels.bean.ListPeople;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.student.connectionclass.activity.StartListeningTestActivity;
import com.lels.student.starttask.activity.Renwu_contentActivity;
import com.lels.student.studyonline.DataPublicclassDetailTestActivity;
import com.lels.student.studyonline.DataStudyDetailActivity;
import com.lels.stutask.adapter.MyExpandableListAdapter;
import com.lels.stutask.adapter.PinnedHeaderExpandableListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StuTask_fragment extends Fragment implements
		ExpandableListView.OnChildClickListener,
		ExpandableListView.OnGroupClickListener {
	private SharedPreferences share;
	private SharedPreferences stuInfo;
	// 原路径
	private String Path = Constants.URL_ + "/Home/MyTaskList";
	private PinnedHeaderExpandableListView mExpandableListview;
	private List<Learn> listday;
	private List<Learn> listday1;
	private List<Learn> listday2;
	private List<Learn> listday3;
	private List<ListPeople> list;
	private List<Learn> zllist11;
	private List<Learn> zllist21;
	private List<Learn> zllist31;
	private List<Learn> zllist1;
	private List<Learn> zllist2;
	private List<Learn> zllist3;
	private List<Learn> zllist12;
	private List<Learn> zllist22;
	private List<Learn> zllist32;
	private List<Learn> zllist13;
	private List<Learn> zllist23;
	private List<Learn> zllist33;
	//判断 资料是否能答题   checkDoEx =是否可以答题，1可以，0不可以，只有Target为2练习时，才会有该字段;
	private String checkDoEx;
	//试卷的路径
	private String domainPFolder;
	private RelativeLayout relative_warn_nulldata_clastudy;
	private String ST_ID;
	private String P_ID;
	private MyExpandableListAdapter myExpandableListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.frag_stutask, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		 LodDialogClass.showCustomCircleProgressDialog(getActivity(), null, getString(R.string.xlistview_header_hint_loading));
	}
	/**
	 * 初始化组件
	 */
	private void initView() {
		relative_warn_nulldata_clastudy = (RelativeLayout) getActivity().findViewById(R.id.relative_warn_nulldata_clastudy);
		mExpandableListview = (PinnedHeaderExpandableListView) getActivity()
				.findViewById(R.id.expandableListview);

		httpRequest();

		// mExpandableListview.setOnHeaderUpdateListener(this);
		mExpandableListview.setOnChildClickListener(this);
		mExpandableListview.setOnGroupClickListener(this);
		stuInfo =getActivity().getSharedPreferences("stuinfo", Context.MODE_PRIVATE);
		LodDialogClass.showCustomCircleProgressDialog(getActivity(), null, getString(R.string.xlistview_header_hint_loading));
	}



	/**
	 * 请求数据
	 */
	private void httpRequest() {
		share = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
//		httpUtils.configSoTimeout(5000);
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		System.out.println("TOKEN==="+ share.getString("Token", ""));
		httpUtils.send(HttpMethod.GET, Path, params,
				new RequestCallBack<String>() {

					private Learn learnday1;
					private Learn learnday2;
					private Learn learnday3;
					private Learn learnday11;
					private Learn learnday12;
					private Learn learnday13;
					private Learn learnday21;
					private Learn learnday22;
					private Learn learnday23;
					private Learn learnday31;
					private Learn learnday32;
					private Learn learnday33;

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LodDialogClass.closeCustomCircleProgressDialog();
						
						/*if (arg0.getExceptionCode()==504) {
							Toast.makeText(getActivity(), "连接超时", 0).show();
						}*/
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> response) {
						if (null==getActivity()) {
							return;
						}
						listday = new ArrayList<Learn>();
						listday1 = new ArrayList<Learn>();
						listday2 = new ArrayList<Learn>();
						listday3 = new ArrayList<Learn>();
						list = new ArrayList<ListPeople>();

						String json = response.result.toString();
						
					System.out.println("任务数据的返回结果===="+json);
						JSONObject jsonObject;
						try {

							jsonObject = new JSONObject(json);
							JSONObject dataJsonObject = jsonObject
									.optJSONObject("Data");

							/**
							 * 今天
							 */
							JSONArray day = dataJsonObject
									.optJSONArray("today");
							zllist1 = new ArrayList<Learn>();
							zllist2 = new ArrayList<Learn>();
							zllist3 = new ArrayList<Learn>();
							if (day.length() > 0) {

								for (int i = 0; i < day.length(); i++) {

									JSONObject obj = day.getJSONObject(i);
									switch (Integer.valueOf(obj
											.getString("TaskType"))) {
									//模考
									case 1:
										learnday1 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist1.add(learnday1);
										break;
										//练习
									case 2:
										learnday2 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist2.add(learnday2);
										break;
										//资料
									case 3:
										learnday3 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist3.add(learnday3);
										break;
									default:
										break;
									}

								}

								listday.addAll(zllist1);
								listday.addAll(zllist2);
								listday.addAll(zllist3);
								ListPeople listPeople1 = new ListPeople("今天",
										listday);
								list.add(listPeople1);
							}
							/**
							 * 昨天
							 */
							JSONArray day1 = dataJsonObject
									.optJSONArray("day1");
							zllist11 = new ArrayList<Learn>();
							zllist21 = new ArrayList<Learn>();
							zllist31 = new ArrayList<Learn>();
							if (day1.length() > 0) {

								for (int i = 0; i < day1.length(); i++) {

									JSONObject obj = day1.getJSONObject(i);
									switch (Integer.valueOf(obj
											.getString("TaskType"))) {
									case 1:
										learnday11 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist11.add(learnday11);
										break;
									case 2:
										learnday12 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist21.add(learnday12);
										break;
									case 3:
										learnday13 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist31.add(learnday13);
										break;
									default:
										break;
									}

								}
								listday1.addAll(zllist11);
								listday1.addAll(zllist21);
								listday1.addAll(zllist31);

								ListPeople listPeople2 = new ListPeople("昨天",
										listday1);
								list.add(listPeople2);

							}
							/**
							 * 前天
							 */
							JSONArray day2 = dataJsonObject
									.optJSONArray("day2");
							zllist12 = new ArrayList<Learn>();
							zllist22 = new ArrayList<Learn>();
							zllist32 = new ArrayList<Learn>();
							if (day2.length() > 0) {

								for (int i = 0; i < day2.length(); i++) {

									JSONObject obj = day2.getJSONObject(i);

									switch (Integer.valueOf(obj
											.getString("TaskType"))) {
									case 1:
										learnday21 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist12.add(learnday21);
										break;
									case 2:
										learnday22 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist22.add(learnday22);
										break;
									case 3:
										learnday23 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist32.add(learnday23);
										break;
									default:
										break;
									}
								}

								listday2.addAll(zllist12);
								listday2.addAll(zllist22);
								listday2.addAll(zllist32);

								ListPeople listPeople3 = new ListPeople("前天",
										listday2);
								list.add(listPeople3);

							}
							/**
							 * 3天前
							 */
							JSONArray day3 = dataJsonObject
									.optJSONArray("day3");

							zllist13 = new ArrayList<Learn>();
							zllist23 = new ArrayList<Learn>();
							zllist33 = new ArrayList<Learn>();
							if (day3.length() > 0) {

								for (int i = 0; i < day3.length(); i++) {
									JSONObject obj = day3.getJSONObject(i);

									switch (Integer.valueOf(obj
											.getString("TaskType"))) {
									case 1:
										learnday31 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist13.add(learnday31);
										break;
									case 2:
										learnday32 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist23.add(learnday32);
										break;
									case 3:
										learnday33 = JSON.parseObject(
												obj.toString(), Learn.class);
										zllist33.add(learnday33);
										break;
									default:
										break;
									}

								}

								listday3.addAll(zllist13);
								listday3.addAll(zllist23);
								listday3.addAll(zllist33);
								ListPeople listPeople4 = new ListPeople("三天前",
										listday3);
								list.add(listPeople4);

							}
							// 数据为空则提示，listview 消失
							if (list.size()> 0) {
								mExpandableListview.setVisibility(View.VISIBLE);
								relative_warn_nulldata_clastudy
										.setVisibility(View.GONE);
								myExpandableListAdapter = new MyExpandableListAdapter(
										getActivity(), list, share);

								mExpandableListview
										.setAdapter(myExpandableListAdapter);
							} else {
								mExpandableListview.setVisibility(View.GONE);
								relative_warn_nulldata_clastudy
										.setVisibility(View.VISIBLE);
							}
							
							/**
							 * 默认展开
							 */
							int groupCount = myExpandableListAdapter
									.getGroupCount();

							for (int i = 0; i < groupCount; i++) {

								mExpandableListview.expandGroup(i);

							}
							
							LodDialogClass.closeCustomCircleProgressDialog();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});

	}
	@Override
	public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
			long arg3) {
		return false;
	}
	private void addHttps(String mateId) {
		// TODO Auto-generated method stub
		String url = Constants.URL_Material_lookUpMaterials;
		RequestParams params = new RequestParams();

		params.addHeader("Authentication",share.getString("Token", ""));// 添加保密的东西

		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("mId", mateId);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				try {
					JSONObject obj = new JSONObject(result);
					String Result = obj.getString("Result");
					System.out.println(result);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		//判断任务的类型  1:模考; 2:练习; 3:资料;     练习特殊判断，判断是否能答题
		int tp = list.get(groupPosition).getList().get(childPosition)
				.getTaskType();
		int StorePoint = list.get(groupPosition).getList().get(childPosition)
				.getStorePoint();
		String data = list.get(groupPosition).getDate();
		String st_id = list.get(groupPosition).getList().get(childPosition)
				.getST_ID();
		
		
		
		System.out.println("任务的类型是============"+tp);
		//模考
		if (tp == 1) {
			Intent intent = new Intent();
			intent.putExtra("ST_ID",
					list.get(groupPosition).getList().get(childPosition)
							.getST_ID());
			intent.putExtra("P_ID",
					list.get(groupPosition).getList().get(childPosition)
							.getRefData().getP_ID());
			intent.setClass(getActivity(), Renwu_contentActivity.class);
			getActivity().startActivity(intent);

		//练习
		}else if( tp == 2){
			checkDoEx = list.get(groupPosition).getList().get(childPosition).getRefData().getCheckDoEx();
			domainPFolder =  list.get(groupPosition).getList().get(childPosition).getRefData().getDomainPFolder();
			ST_ID = list.get(groupPosition).getList().get(childPosition).getST_ID();
			P_ID  = list.get(groupPosition).getList().get(childPosition).getRefData().getP_ID();
			System.out.println("返回资料是否能答题=="+checkDoEx);
			System.out.println("返回资料路径=="+domainPFolder);
			System.out.println("ST_ID===="+ST_ID+"====P_ID===="+P_ID);
			String name = list.get(groupPosition).getList().get(childPosition).getRefData().getName();
			System.out.println("练习题的题目名称===="+name);
			
			//9.30
			if(checkDoEx!=null){
				//0是不可以答题
				
				if(checkDoEx.equals("1")){
					//写入数据答题数据
					Editor ed = stuInfo.edit();
					ed.putInt("testtype", 3);
					ed.putString("exercise", name);
					ed.putString("domainPFolder",domainPFolder);
					ed.putString("P_ID", P_ID);
//					ed.putString("mateId", mate_id);
					ed.putString("ST_ID", ST_ID);
//					System.out.println("ST_ID====" + ST_ID);
					// intent.putExtra("testtype", 2);
					// intent.putExtra("domainPFolder",map_info.get("domainPFolder").toString());
					// intent.putExtra("P_ID", map_info.get("P_ID").toString());
					// intent.putExtra("mateId", mateId);
					ed.commit();
					Intent intent = new Intent();
//					intent.putExtra("ST_ID",
//							list.get(groupPosition).getList().get(childPosition)
//									.getST_ID());
//					intent.putExtra("P_ID",
//							list.get(groupPosition).getList().get(childPosition)
//									.getRefData().getP_ID());
					intent.setClass(getActivity(), StartListeningTestActivity.class);
					getActivity().startActivity(intent);
//				getActivity().finish();
				}else{
					//0是不可以答题，进入详情页面
					Intent intent = new Intent();
					intent.putExtra("ST_ID",
							list.get(groupPosition).getList().get(childPosition)
									.getST_ID());
					intent.putExtra("P_ID",
							list.get(groupPosition).getList().get(childPosition)
									.getRefData().getP_ID());
					intent.setClass(getActivity(), Renwu_contentActivity.class);
					getActivity().startActivity(intent);
				}
			}else{
				Intent intent = new Intent();
				intent.putExtra("ST_ID",
						list.get(groupPosition).getList().get(childPosition)
								.getST_ID());
				intent.putExtra("P_ID",
						list.get(groupPosition).getList().get(childPosition)
								.getRefData().getP_ID());
				intent.setClass(getActivity(), Renwu_contentActivity.class);
				getActivity().startActivity(intent);
			}
//			Intent intent = new Intent();
//			intent.putExtra("ST_ID",
//					list.get(groupPosition).getList().get(childPosition)
//							.getST_ID());
//			intent.putExtra("P_ID",
//					list.get(groupPosition).getList().get(childPosition)
//							.getRefData().getP_ID());
//			intent.setClass(getActivity(), Renwu_contentActivity.class);
//			getActivity().startActivity(intent);
		}
		//资料
			else if (tp == 3)
		{
			addHttps(list.get(groupPosition).getList().get(childPosition).getRefID());
			Intent intent = new Intent();
			Bundle b = new Bundle();

			System.out.println("list.size-----"
					+ list.get(groupPosition).getList().size() + "---"
					+ childPosition);

			if (StorePoint == 2) {// 视频
				b.putString("Mate_ID",
						list.get(groupPosition).getList().get(childPosition)
								.getRefID());
				b.putString("ST_ID",
						list.get(groupPosition).getList().get(childPosition)
								.getST_ID());
				intent.setClass(getActivity(),
						DataPublicclassDetailTestActivity.class);
				intent.putExtras(b);
				getActivity().startActivity(intent);
			} else if (StorePoint == 1) {// 文档
				b.putString("mate_id",
						list.get(groupPosition).getList().get(childPosition)
								.getRefID());
				b.putString("ST_ID",
						list.get(groupPosition).getList().get(childPosition)
								.getST_ID());
				intent.setClass(getActivity(), DataStudyDetailActivity.class);
				intent.putExtras(b);
				getActivity().startActivity(intent);
			}
			 getfinishHttp(st_id, "0");
			if (data.equals("三天前")) {
				list.get(groupPosition).getList().remove(childPosition);
				System.out.println("----快快删除---");
			}
			else{
				Learn learn = (Learn) myExpandableListAdapter.getChild(
						groupPosition, childPosition);
				learn.setChacked(true);
				System.out.println("----dian  ji   le----");
			}
			myExpandableListAdapter.notifyDataSetChanged();
		}

		return false;
	}
	/**
	 * 任务完成状态
	 * 
	 * @param stid
	 * @param examinfoid
	 */
	private void getfinishHttp(String stid, String examinfoid) {
		String url = Constants.URL_ + "/Task/InsertTaskFinish?stId=" + stid
				+ "&examInfoId=" + examinfoid;
		System.out.println("url----" + url);
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils finishutil = new HttpUtils();
		finishutil.send(HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String result = arg0.result;
						System.out.println("你妹的"+result);
					}
				});
	}

}
