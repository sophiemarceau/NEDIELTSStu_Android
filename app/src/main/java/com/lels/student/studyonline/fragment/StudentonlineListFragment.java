package com.lels.student.studyonline.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.strudentlelts.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.student.studyonline.DataPublicclassDetailTestActivity;
import com.lels.student.studyonline.adapter.StudyOnlinepublicclassAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/***
 * 视频公开课
 */
public class StudentonlineListFragment extends Fragment {
	private static final String TAG = "StudentonlineListFragment";
	private RelativeLayout studyOnLine_no_data;
	private PullToRefreshListView listview_studyOnline;
	private View mView;
	private TextView studyOnLine_text;
	private String tagUsername;
	private List<HashMap<String, Object>> l_class;

	private String token = "";

	private int pageIndex = 1;
	private SharedPreferences stuShare;
	private StudyOnlinepublicclassAdapter adapter;

	// 记录筛选返回的数据
	private String nameCode = "0";
	private String roleId = "0";
	private String timeType = "0";
	private String beginDate = "";
	private String endDate = "";
	// 判断数据是否加载完成
	private boolean refresh_flag = false;

	private String changeBegindate;

	private String changeEnddate;

	public StudentonlineListFragment(String tagusername) {
		super();
		this.tagUsername = tagusername;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.list_fargment_studentonline_main,
				null);

		getDataFromShare();
		init();
		showDialog();
		if (tagUsername.equals("游客")) {
			getDataFromNetForScreenyk("", "0", nameCode, roleId, timeType,
					beginDate, endDate, String.valueOf(pageIndex));
		} else {
			if (stuShare.getString("isVistor", "").equals("1")) {
				getDataFromNetForScreenyk(token, "1", nameCode, roleId,
						timeType, beginDate, endDate, String.valueOf(pageIndex));
			} else {
				getDataFromNetForScreen(nameCode, roleId, timeType, beginDate,
						endDate, 1);
			}
		}
		return mView;
	}

	private void showDialog() {
		LodDialogClass.showCustomCircleProgressDialog(getActivity(), null,
				getString(R.string.xlistview_header_hint_loading));
	}

	/***
	 * 游客
	 * 
	 * @param vistorFlag
	 * @param nameCode
	 * @param roleId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @param pageIndex
	 */
	private void getDataFromNetForScreenyk(String token, String vistorFlag,
			String nameCode, String roleId, String timeType, String beginDate,
			String endDate, String pageIndex) {
		Datechange(beginDate, endDate);
		String url = Constants.URL_onlinestudyvistorVideoMaterials;
		HttpUtils util = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);
		params.addBodyParameter("vistorFlag", vistorFlag);
		params.addBodyParameter("nameCode", nameCode);
		params.addBodyParameter("roleId", roleId);
		params.addBodyParameter("timeType", timeType);
		params.addBodyParameter("beginDate", changeBegindate);
		params.addBodyParameter("endDate", changeEnddate);
		params.addBodyParameter("pageIndex", pageIndex);
		util.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {

				String result = arg0.result;
				JSONObject str;
				try {
					str = new JSONObject(result);
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");
					if (!Data.equalsIgnoreCase("null")) {
						l_class = new ArrayList<HashMap<String, Object>>();
						JSONArray array = new JSONArray(Data);
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.optJSONObject(i);
							HashMap<String, Object> map = new HashMap<String, Object>();
							// "createtime": "2015-08-19",
							// "name": "ZYX-测试视频",
							// "videoduration": null,
							// "MF_ID": null,
							// "Mate_ID": 305505,
							// "readcount": 79,
							// "videoThumbnail": null

							map.put("name", obj.getString("name"));
							map.put("readcount", obj.getString("readcount"));
							map.put("videoThumbnail",
									obj.getString("videoThumbnail"));
							map.put("Mate_ID", obj.getString("Mate_ID"));
							map.put("createtime", obj.getString("createtime"));
							map.put("videoduration",
									obj.getString("videoduration"));
							map.put("MF_ID", obj.getString("MF_ID"));
							map.put("TF_ID", obj.getString("TF_ID"));
							map.put("ST_ID", obj.getString("ST_ID"));

							l_class.add(map);
						}
						System.out.println("---ci---"+l_class);
						// 用于判断集合是否有数据，没有则显示没数据的图片
						if (l_class.size() != 0) {
							listview_studyOnline.setVisibility(View.VISIBLE);
							studyOnLine_no_data.setVisibility(View.GONE);
							adapter = new StudyOnlinepublicclassAdapter(
									getActivity(), l_class);
							listview_studyOnline.setAdapter(adapter);
						} else {
							listview_studyOnline.setVisibility(View.GONE);
							studyOnLine_no_data.setVisibility(View.VISIBLE);
						}
					}
					LodDialogClass.closeCustomCircleProgressDialog();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void addHttps(String mateId) {
		// TODO Auto-generated method stub
		String url = Constants.URL_Material_lookUpMaterials;
		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("mId", mateId);
		System.out.println(url+"----"+mateId);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("=-=-=-=-=-=-=-=-=-=");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				try {
					JSONObject obj = new JSONObject(result);
					String Result = obj.getString("Result");
					System.out.println("my----" + result);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private void init() {
		stuShare = getActivity().getSharedPreferences("",
				getActivity().MODE_PRIVATE);
		listview_studyOnline = (PullToRefreshListView) mView
				.findViewById(R.id.listview_studyonline);
		studyOnLine_no_data = (RelativeLayout) mView
				.findViewById(R.id.studyOnLine_no_data);
		studyOnLine_text = (TextView) mView.findViewById(R.id.studyOnLine_text);

		// studyOnLine_no_data = (RelativeLayout) mview
		// .findViewById(R.id.studyOnLine_no_data);

		listview_studyOnline.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position--;
				// 增加一次浏览次数
//				addreadcount(l_class.get(position).get("Mate_ID").toString());
				addHttps(l_class.get(position).get("Mate_ID").toString());
				if (!l_class.get(position).get("TF_ID").toString()
						.equalsIgnoreCase("null")) {
					taskfinish(l_class.get(position).get("ST_ID").toString(),
							"");
					Intent intent = new Intent();
					// intent.setClass(getActivity(),
					// DataPublicclassDetailActivity.class);
					intent.setClass(getActivity(),
							DataPublicclassDetailTestActivity.class);
					Bundle b = new Bundle();
					b.putString("Mate_ID", l_class.get(position).get("Mate_ID")
							.toString());
					b.putString("ST_ID", l_class.get(position).get("ST_ID")
							.toString());
					/*
					 * b.putString("MF_ID", l_class.get(position).get("MF_ID")
					 * .toString());
					 * 
					 * b.putString("name", l_class.get(position).get("name")
					 * .toString());
					 */

					b.putString("ST_ID", l_class.get(position).get("ST_ID")
							.toString());
					intent.putExtras(b);
					getActivity().startActivity(intent);
				}
			}
		});
	}

	private void getDataFromShare() {
		SharedPreferences share = getActivity().getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");
		Log.d(TAG, "获取的token数值为=====" + token);
	}

	/**
	 * 时间转换
	 * 
	 * @param beginDate1
	 * @param endDate1
	 */
	public void Datechange(String begindate, String enddate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date begin = df.parse(begindate);
			Date end = df.parse(enddate);
			changeBegindate = df.format(begin);
			changeEnddate = df.format(end);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	public void getDataFromNetForScreen(String nameCode, String roleId,
			String timeType, String beginDate, String endDate, int page) {

		// pageIndex =1;

		this.nameCode = nameCode;
		this.roleId = roleId;
		this.timeType = timeType;
		this.beginDate = beginDate;
		this.endDate = endDate;

		this.pageIndex = page;

		Log.d(TAG, "getdatafromnetforscreen()执行");
		if (beginDate.equals("")) {
			changeBegindate = "";
		} else if (endDate.equals("")) {
			changeEnddate = "";
		} else {
			Datechange(beginDate, endDate);
		}
		System.out.println(beginDate + "----" + endDate);
		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_PUBLICCLASS_DATE_SCREEN;
		// +"?nameCode="+nameCode+"&roleId="+roleId+"&timeType="+timeType+"&beginDate="+beginDate+"&endDate="+endDate+"&pageIndex="+pageIndex
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		// nameCode=[不限为0，KY:口语、YD:阅读、CH：词汇、XZ：写作、ZH：综合、TL：听力、YF：语法，举例:'KY']
		// roleId=[不限为0，1为集团2为教师]
		// timeType=[不限为0，如果起始日期有值也要传0，1为本周2为本月 3为本年]
		// beginDate=[开始日期]
		// endDate=[结束日期]

		params.addBodyParameter("nameCode", nameCode);
		params.addBodyParameter("roleId", roleId);
		params.addBodyParameter("timeType", timeType);
		params.addBodyParameter("beginDate", changeBegindate);
		params.addBodyParameter("endDate", changeEnddate);
		params.addBodyParameter("pageIndex", String.valueOf(pageIndex));
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("获取资料的数据======="
								+ responseInfo.result);
						
						LodDialogClass.closeCustomCircleProgressDialog();
						if (null==getActivity()) {
							return;
						}
						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							if (!Data.equalsIgnoreCase("null")) {
								// readcount = 浏览次数
								// videothumbnail = 缩略图网址
								// Mate_ID = 材料ID
								// createtime = 创建时间
								// videoduration = 视频时长（秒）

								// videoduration: 208,
								// createtime: 2015-04-23,
								// Mate_ID: 305302,
								// readcount: ,
								// videoThumbnail: http:
								// //2.img.bokecc.com/comimage/0500478A073885DA/2015-04-23/A37B45AB3EA0B1429C33DC5901307461-0.jpg,
								// name: 雅思官方听力评分标准[高清]
								if (pageIndex == 1) {
									l_class = new ArrayList<HashMap<String, Object>>();
								}
								JSONArray array = new JSONArray(Data);
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.optJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();
									// "createtime": "2015-08-19",
									// "name": "ZYX-测试视频",
									// "videoduration": null,
									// "MF_ID": null,
									// "Mate_ID": 305505,
									// "readcount": 79,
									// "videoThumbnail": null

									map.put("name", obj.getString("name"));
									map.put("readcount",
											obj.getString("readcount"));
									map.put("videoThumbnail",
											obj.getString("videoThumbnail"));
									map.put("Mate_ID", obj.getString("Mate_ID"));
									map.put("createtime",
											obj.getString("createtime"));
									map.put("videoduration",
											obj.getString("videoduration"));
									map.put("MF_ID", obj.getString("MF_ID"));
									map.put("TF_ID", obj.getString("TF_ID"));
									map.put("ST_ID", obj.getString("ST_ID"));
									l_class.add(map);
								}
								System.out.println("l_class.size()===="
										+ l_class.size() / pageIndex);
								if ((l_class.size() / pageIndex) < 10) {
									refresh_flag = true;
								} else {
									refresh_flag = false;
								}
								System.out.println("--------" + pageIndex
										+ "-------");
								if (pageIndex == 1) {
									knowIfDataBackground(l_class);
									adapter = new StudyOnlinepublicclassAdapter(
											getActivity(), l_class);
									listview_studyOnline.setAdapter(adapter);
								} else {
									adapter.notifyDataSetChanged();
								}
								refresh(listview_studyOnline, adapter);
							} else {
								knowIfDataBackground(l_class);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
						LodDialogClass.closeCustomCircleProgressDialog();
					}
				});
	}

	// 如果没有数据则显示一张背景图
	private void knowIfDataBackground(List<HashMap<String, Object>> list) {
		if (list.size() > 0) {
			listview_studyOnline.setVisibility(View.VISIBLE);
			studyOnLine_no_data.setVisibility(View.GONE);
		} else {
			listview_studyOnline.setVisibility(View.GONE);
			studyOnLine_no_data.setVisibility(View.VISIBLE);
			studyOnLine_text.setText("暂无雅思云课堂");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		showDialog();
		if (tagUsername.equals("游客")) {
			getDataFromNetForScreenyk("", "0", nameCode, roleId, timeType,
					beginDate, endDate, String.valueOf(pageIndex));
		} else {
			if (stuShare.getString("isVistor", "").equals("1")) {

				getDataFromNetForScreenyk(token, "1", nameCode, roleId,
						timeType, beginDate, endDate, String.valueOf(pageIndex));
			} else {
				getDataFromNetForScreen(nameCode, roleId, timeType, beginDate,
						endDate, 1);
			}
		}
	}

	/**
	 * 刷新监听
	 * 
	 * @param listview
	 * @param adapter
	 */
	public void refresh(final PullToRefreshListView listview,
			final StudyOnlinepublicclassAdapter adapter) {
		listview.setMode(Mode.BOTH);
		initIndicator();
		listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pageIndex = 1;
				if (tagUsername.equals("游客")) {
					getDataFromNetForScreenyk("", "0", nameCode, roleId,
							timeType, beginDate, endDate,
							String.valueOf(pageIndex));
				} else {
					if (stuShare.getString("isVistor", "").equals("1")) {

						getDataFromNetForScreenyk(token, "1", nameCode, roleId,
								timeType, beginDate, endDate,
								String.valueOf(pageIndex));
					} else {
						getDataFromNetForScreen(nameCode, roleId, timeType,
								beginDate, endDate, pageIndex);
					}
				}
				new FinishRefresh().execute();
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				if (tagUsername.equals("游客")) {
					getDataFromNetForScreenyk("", "0", nameCode, roleId,
							timeType, beginDate, endDate,
							String.valueOf(pageIndex));
				} else {
					if (stuShare.getString("isVistor", "").equals("1")) {
						getDataFromNetForScreenyk(token, "1", nameCode, roleId,
								timeType, beginDate, endDate,
								String.valueOf(pageIndex));
					} else {
						getDataFromNetForScreen(nameCode, roleId, timeType,
								beginDate, endDate, pageIndex);
					}
				}
				new FinishRefresh().execute();
				adapter.notifyDataSetChanged();
			}

		});
	}

	/**
	 * 一秒后取消刷新
	 * 
	 * @author Administrator
	 */
	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// adapter.notifyDataSetChanged();
			listview_studyOnline.onRefreshComplete();
		}
	}

	/**
	 * 自定义上拉刷新和下拉加载的显示文字
	 */
	private void initIndicator() {
		ILoadingLayout startLabels = listview_studyOnline
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("放开以加载");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = listview_studyOnline.getLoadingLayoutProxy(
				false, true);
		System.out.println("refresh_flag=====" + refresh_flag);
		if (refresh_flag == true) {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("全部加载完毕");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		} else {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("加载中。。。");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		}
	}

	/**
	 * 增加浏览次数
	 */
	private void addreadcount(String mateId) {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_ADDREADCOUNT
				+ "?pageIndex=";

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("mateId", mateId);
		HttpUtils http = new HttpUtils();
		 http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.d(TAG, "增加浏览次数==" + responseInfo.result.toString());
						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							// if (Result.equalsIgnoreCase("true")) {
							// Toast.makeText(
							// DataPublicclassDetailActivity.this,
							// "收藏成功", Toast.LENGTH_SHORT).show();
							// } else {
							// Toast.makeText(
							// DataPublicclassDetailActivity.this,
							// "失败", 2).show();
							// }
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
					}
				});
	}

	/**
	 * finish task
	 */
	private void taskfinish(String stId, String examInfoId) {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_TASK_FINISH + "?stId="
				+ stId + "&examInfoId=" + examInfoId;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.d(TAG,
								"finish task" + responseInfo.result.toString());
						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.d(TAG, error.toString());
					}
				});
	}
}
