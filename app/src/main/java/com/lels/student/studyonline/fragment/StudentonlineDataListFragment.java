package com.lels.student.studyonline.fragment;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import android.text.format.DateFormat;
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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lels.constants.Constants;
import com.lels.student.studyonline.DataStudyDetailActivity;
import com.lels.student.studyonline.adapter.StudyOnlineStudydataAdapter;
import com.lelts.tool.UtiltyHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.message.proguard.ar;

/**
 * 学习资料
 */
public class StudentonlineDataListFragment extends Fragment {

	private static final String TAG = "StudentonlineDataListFragment";
	private RelativeLayout studyOnLine_no_data;
	private PullToRefreshListView listview_studyOnline;
	private View mView;
	private TextView studyOnLine_text;
	private StudyOnlineStudydataAdapter adapter;
	private List<HashMap<String, Object>> l_class;
	private String token;
	private int pageIndex = 1;
	private String tagUsername;
	private SharedPreferences stuShare;

	private String nameCode = "0";
	private String fileType = "0";
	private String timeType = "0";
	private String beginDate = "";
	private String endDate = "";
	private String changeBeginDate;
	private String changeEndDate;

	// 判断数据是否加载完成
	private boolean refresh_flag = false;

	public StudentonlineDataListFragment(String tagusername) {
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
		if (tagUsername.equals("游客")) {
			getDataFromNetyk("", "0", nameCode, fileType, timeType, beginDate,
					endDate);
		} else {
			if (stuShare.getString("isVistor", "").equals("1")) {
				getDataFromNetyk(token, "1", nameCode, fileType, timeType,
						beginDate, endDate);
			} else {
				// getdatafromnet();
				getDataFromNetForScreen(nameCode, fileType, timeType,
						beginDate, endDate, 1);
			}
		}
		return mView;
	}

	private void getDataFromNetyk(String token, String vistorFlag,
			String nameCode, String fileType, String timeType,
			String beginDate, String endDate) {
		if (beginDate.equals("") && endDate.equals("")) {

		} else {
			Datechange(beginDate, endDate);
		}
		String url = Constants.URL_onlinestudyvistorMaterials;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);
		params.addBodyParameter("vistorFlag", vistorFlag);
		params.addBodyParameter("nameCode", nameCode);
		params.addBodyParameter("fileType", fileType);
		params.addBodyParameter("timeType", timeType);
		params.addBodyParameter("beginDate", changeBeginDate);
		params.addBodyParameter("endDate", changeEndDate);
		params.addBodyParameter("pageIndex", String.valueOf(pageIndex));
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (null == getActivity()) {
					return;
				}

				JSONObject str;
				try {
					str = new JSONObject(arg0.result);
					System.out.println("----123321123321----" + str);
					System.out.println(str);
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");
					if (pageIndex == 1) {
						l_class = new ArrayList<HashMap<String, Object>>();
					}
					if (!Data.equalsIgnoreCase("null")) {
						JSONArray array = new JSONArray(Data);
						for (int i = 0; i < array.length(); i++) {
							// filetype = 文件类型（doc等）
							// uid = 用户ID
							// teachername = 教师姓名
							// name = 标题
							// Mate_ID = 材料ID
							// type = 分类标签（听力等）
							// createtime = 创建日期
							// url = 点击打开网址

							// "uid": 7416,
							// "createtime": "2015-04-21",
							// "mate_id": 305290,
							// "name": "26",
							// "filetype": "docx",
							// "type": null,
							// "url":
							// "http://yunku.gokuai.com/file/cuu5egi2#",
							// "teachername": null
							// TF_ID

							JSONObject obj = array.optJSONObject(i);
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("filetype", obj.getString("filetype"));
							map.put("uid", obj.getString("uid"));
							map.put("teachername", obj.getString("teachername"));
							map.put("name", obj.getString("name"));
							map.put("mate_id", obj.getString("mate_id"));
							map.put("type", obj.getString("type"));
							map.put("createtime", obj.getString("createtime"));
							map.put("url", obj.getString("url"));
							map.put("MF_ID", obj.getString("MF_ID"));
							map.put("TF_ID", obj.getString("TF_ID"));
							map.put("ST_ID", obj.getString("ST_ID"));
							l_class.add(map);
						}

						Collections.sort(l_class,
								new Comparator<HashMap<String, Object>>() {

									@Override
									public int compare(
											HashMap<String, Object> lhs,
											HashMap<String, Object> rhs) {
										Date date1 = DateUtil.stringToDate(lhs
												.get("createtime").toString());
										Date date2 = DateUtil.stringToDate(rhs
												.get("createtime").toString());
										if (date1.before(date2)) {
											return 1;
										}
										return -1;
									}
								});
						Log.d(TAG, "getdatafromnet()执行之开始填充listview");
						if (pageIndex == 1) {
							adapter = new StudyOnlineStudydataAdapter(
									getActivity(), l_class);
							listview_studyOnline.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						refresh(listview_studyOnline, adapter);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 刷新监听
	 * 
	 * @param listview
	 * @param adapter
	 */
	public void refresh(final PullToRefreshListView listview,
			final StudyOnlineStudydataAdapter adapter) {
		listview.setMode(Mode.BOTH);
		initIndicator();
		listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pageIndex = 1;
				if (tagUsername.equals("游客")) {
					getDataFromNetyk("", "0", nameCode, fileType, timeType,
							beginDate, endDate);
				} else {
					if (stuShare.getString("isVistor", "").equals("1")) {
						getDataFromNetyk(token, "1", nameCode, fileType,
								timeType, beginDate, endDate);
					} else {
						// getdatafromnet();
						getDataFromNetForScreen(nameCode, fileType, timeType,
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
					getDataFromNetyk("", "0", nameCode, fileType, timeType,
							beginDate, endDate);
				} else {
					if (stuShare.getString("isVistor", "").equals("1")) {
						getDataFromNetyk(token, "1", nameCode, fileType,
								timeType, beginDate, endDate);
					} else {
						// getdatafromnet();
						getDataFromNetForScreen(nameCode, fileType, timeType,
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
	 *
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

	private void addHttps(String mateId) {
		// TODO Auto-generated method stub
		String url = Constants.URL_Material_lookUpMaterials;
		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("mId", mateId);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("==-=-=-=-=-=-=-=-=-=-2");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				try {
					JSONObject obj = new JSONObject(result);
					String Result = obj.getString("Result");
					System.out.println(result);
					System.out.println("mmmmm---" + Result);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private void init() {
		stuShare = getActivity().getSharedPreferences("stushare",
				getActivity().MODE_PRIVATE);
		listview_studyOnline = (PullToRefreshListView) mView
				.findViewById(R.id.listview_studyonline);
		studyOnLine_no_data = (RelativeLayout) mView
				.findViewById(R.id.studyOnLine_no_data);
		studyOnLine_text = (TextView) mView.findViewById(R.id.studyOnLine_text);

		listview_studyOnline.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position--;
				// 增加一次浏览次数
				// addReadCount(l_class.get(position).get("mate_id").toString());
				addHttps(l_class.get(position).get("mate_id").toString());
				if (l_class.get(position).get("TF_ID").toString()
						.equalsIgnoreCase("null")) {
					taskFinish(l_class.get(position).get("ST_ID").toString(),
							"");
				}
				Intent intent = new Intent();
				intent.setClass(getActivity(), DataStudyDetailActivity.class);
				Bundle b = new Bundle();
				b.putString("url", l_class.get(position).get("url").toString());
				b.putString("mate_id", l_class.get(position).get("mate_id")
						.toString());
				b.putString("ST_ID", l_class.get(position).get("ST_ID")
						.toString());
				/*
				 * b.putString("MF_ID", l_class.get(position).get("MF_ID")
				 * .toString()); b.putString("url",
				 * l_class.get(position).get("url").toString());
				 */
				System.out.println("ST_ID======="
						+ l_class.get(position).get("ST_ID").toString());
				intent.putExtras(b);
				getActivity().startActivity(intent);
			}
		});
	}

	private void getDataFromShare() {
		SharedPreferences share = getActivity().getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");
		Log.d(TAG, "获取的token数值为=====" + token);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (tagUsername.equals("游客")) {
			getDataFromNetyk("", "0", nameCode, fileType, timeType, beginDate,
					endDate);
		} else {
			if (stuShare.getString("isVistor", "").equals("1")) {
				getDataFromNetyk(token, "1", nameCode, fileType, timeType,
						beginDate, endDate);
			} else {
				// getdatafromnet();
				getDataFromNetForScreen(nameCode, fileType, timeType,
						beginDate, endDate, 1);
			}
		}
	}

	/**
	 * 时间转换
	 * 
	 * @param beginDate1
	 * @param endDate1
	 */
	public void Datechange(String beginDate1, String endDate1) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date begin = df.parse(beginDate1);
			Date end = df.parse(endDate1);
			changeBeginDate = df.format(begin);
			changeEndDate = df.format(end);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	public void getDataFromNetForScreen(String nameCode1, String fileType1,
			String timeType1, String beginDate1, String endDate1, int page) {
		/*
		 * // pageIndex = 1;
		 * 
		 * this.nameCode = nameCode1; this.fileType = fileType1; this.timeType =
		 * timeType1; this.beginDate = beginDate1; this.endDate = endDate1;
		 * 
		 * this.pageIndex = page;
		 */
		this.nameCode = nameCode1;
		this.fileType = fileType1;
		this.timeType = timeType1;
		this.beginDate = beginDate1;
		this.endDate = endDate1;
		this.pageIndex = page;
		if (beginDate1.equals("")) {
			changeBeginDate = "";
		} else if (endDate1.equals("")) {
			changeEndDate = "";
		} else {
			Datechange(beginDate1, endDate1);
		}
		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_STUDY_DATE_SCREEN;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西

		// nameCode=[不限为0，KY:口语、YD:阅读、CH：词汇、XZ：写作、ZH：综合、TL：听力、YF：语法，举例:'KY']
		// roleId=[不限为0，1为集团2为教师]
		// timeType=[不限为0，如果起始日期有值也要传0，1为本周2为本月 3为本年]
		// beginDate=[开始日期]
		// endDate=[结束日期]

		params.addBodyParameter("nameCode", nameCode1);
		params.addBodyParameter("fileType", fileType1);
		params.addBodyParameter("timeType", timeType1);
		params.addBodyParameter("beginDate", changeBeginDate);
		params.addBodyParameter("endDate", changeEndDate);
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

						if (null == getActivity()) {
							return;
						}

						Log.d(TAG, "获取资料的数据=======" + responseInfo.result);
						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							if (!UtiltyHelper.isEmpty(Data)) {
								if (Data.length() == 0) {
									System.out.println("11111");
								} else {
									System.out.println("22222");
								}
								// name = 标题e
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
									map.put("filetype",
											obj.getString("filetype"));
									map.put("uid", obj.getString("uid"));
									map.put("teachername",
											obj.getString("teachername"));
									map.put("name", obj.getString("name"));
									map.put("mate_id", obj.getString("mate_id"));
									map.put("type", obj.getString("type"));
									map.put("createtime",
											obj.getString("createtime"));
									map.put("url", obj.getString("url"));
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
								Collections
										.sort(l_class,
												new Comparator<HashMap<String, Object>>() {

													@Override
													public int compare(
															HashMap<String, Object> lhs,
															HashMap<String, Object> rhs) {
														Date date1 = DateUtil
																.stringToDate(lhs
																		.get("createtime")
																		.toString());
														Date date2 = DateUtil
																.stringToDate(rhs
																		.get("createtime")
																		.toString());
														if (date1.before(date2)) {
															return 1;
														}
														return -1;
													}
												});
								if (pageIndex == 1) {
									knowIfDataBackground(l_class);
									adapter = new StudyOnlineStudydataAdapter(
											getActivity(), l_class);
									listview_studyOnline.setAdapter(adapter);
								} else {
									adapter.notifyDataSetChanged();
								}
								refresh(listview_studyOnline, adapter);
							} else {
								// 如果没有数据责显示一张背景图
								knowIfDataBackground(l_class);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println(error.toString());
					}
				});
	}

	// 如果没有数据则显示一张背景图
	private void knowIfDataBackground(List<HashMap<String, Object>> list) {
		if (list.size() > 0) {
			listview_studyOnline.setVisibility(View.VISIBLE);
			studyOnLine_no_data.setVisibility(View.GONE);
			// studyOnLine_text.setText("暂无资料");
		} else {
			listview_studyOnline.setVisibility(View.GONE);
			studyOnLine_no_data.setVisibility(View.VISIBLE);
			studyOnLine_text.setText("暂无资料");
		}
	}

	/**
	 * 增加浏览次数
	 */
	private void addReadCount(String mateId) {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_ADDREADCOUNT;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);
		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("mateId", mateId);
		HttpUtils http = new HttpUtils();
		// http.configCurrentHttpCacheExpiry(1000 * 10);
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

	public static class DateUtil {
		public static Date stringToDate(String dateString) {
			ParsePosition position = new ParsePosition(0);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date dateValue = simpleDateFormat.parse(dateString, position);
			return dateValue;
		}
	}

	/**
	 * finish task
	 */
	private void taskFinish(String stId, String examInfoId) {

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
