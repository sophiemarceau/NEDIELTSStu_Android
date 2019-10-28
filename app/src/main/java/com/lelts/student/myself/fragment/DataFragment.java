package com.lelts.student.myself.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.strudentlelts.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.student.studyonline.DataStudyDetailActivity;
import com.lelts.student.myself.adapter.MycollectDataAdapter;
import com.lelts.student.myself.adapter.MycollectPublicclassAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class DataFragment extends Fragment {

	private static final String TAG = "PublicclassFragment";

	private String token;

	private View mview;
	private PullToRefreshListView listview_myself_collect;
	
	private int pageIndex = 1;
	private MycollectDataAdapter adapter;
	private List<HashMap<String, Object>> l_collect_data = new ArrayList<HashMap<String,Object>>();
	//无数据提示
	private RelativeLayout nulldata;
	private TextView txtnull;
	//判断数据是否全部加载完成
	private boolean fresh = false;
	//取消收藏的一些参数
	private  String mateId,optType,ST_ID;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mview = inflater.inflate(R.layout.fargment_myself_mycollect, null);
		Log.d(TAG, "DataFragment");

		initview();
		getdatafromshare();
		getdatafromnet();

		return mview;
	}

	private void initview() {
		listview_myself_collect = (PullToRefreshListView) mview
				.findViewById(R.id.listview_myself_collect);
		nulldata = (RelativeLayout)mview.findViewById(R.id.relative_warn_nulldata_publicclass);
		txtnull = (TextView) mview.findViewById(R.id.txt_warn_nulldata_class_main);
//		//listview 点击事件
//		listview_myself_collect.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				position = position-1;
//				// 增加一次浏览次数
////				addreadcount(l_collect_data.get(position).get("mate_id").toString());
//
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), DataStudyDetailActivity.class);
//				Bundle b = new Bundle();
//				b.putString("mate_id", l_collect_data.get(position).get("mate_id")
//						.toString());
//				b.putString("MF_ID", l_collect_data.get(position).get("MF_ID")
//						.toString());
//				b.putString("url", l_collect_data.get(position).get("url")
//						.toString());
//				
//				b.putString("ST_ID", l_collect_data.get(position).get("ST_ID")
//						.toString());
//				intent.putExtras(b);
//
//				getActivity().startActivity(intent);
//				
//			}
//		});
	}
	
	/**
	 * 点击事件
	 */
	public void onitemclick(int position) {
		// TODO Auto-generated method stub
		System.out.println("触发点击事件======");
		
//		position = position-1;
		// 增加一次浏览次数
//		addreadcount(l_collect_data.get(position).get("mate_id").toString());
		addHttps(l_collect_data.get(position).get("mate_id")
				.toString());
		Intent intent = new Intent();
		intent.setClass(getActivity(), DataStudyDetailActivity.class);
		Bundle b = new Bundle();
		b.putString("mate_id", l_collect_data.get(position).get("mate_id")
				.toString());
		b.putString("MF_ID", l_collect_data.get(position).get("MF_ID")
				.toString());
		b.putString("url", l_collect_data.get(position).get("url")
				.toString());
		
		b.putString("ST_ID", l_collect_data.get(position).get("ST_ID")
				.toString());
		intent.putExtras(b);

		getActivity().startActivity(intent);
		
	}

	private void getdatafromshare() {

		SharedPreferences share = getActivity().getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");

		Log.d(TAG, "获取的token数值为=====" + token);

	}

	private void getdatafromnet() {

		String url = Constants.URL_MYSELF_MYCOLLECT_DATA + "?pageIndex="
				+ pageIndex;// 公开课资料

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// pageIndex
		// params.addBodyParameter("pageIndex", "1");

		HttpUtils http = new HttpUtils();
		
		http.configCurrentHttpCacheExpiry(0);
		http.configDefaultHttpCacheExpiry(0);
		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						System.out.println("获取资料的数据" + responseInfo.result);

						try {

							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							
							if(!Data.equalsIgnoreCase(null)){
								JSONObject obj_d = new JSONObject(Data);
							String str_list = obj_d.getString("list");
							
							JSONArray array = new JSONArray(str_list);

								//  MF_ID = 收藏主键;
								//    mate_id = 资料表主键;
								//    name = 资料名称;
								//    url = 资料查看地址;
								//    filetype = 资料类型，文件后缀名;
								//    uid = 创建人ID;
								//    createtime = 创建时间;
								//    type = 类型，[口语，听力，写作等];
								//    teachername = 教师名;

								//  filetype = 文件类型（doc等）
								//   uid = 用户ID
								//   RoleID = 角色ID，1集团2个人
								//   name = 标题
								//   mate_id = 材料ID
								//   type = 分类标签（听力等）
								//   createtime = 创建日期
								//   url = 点击打开网址
								//   StorePoint = 存储点 1:够快网盘; 2:CC视频
								//   ReadCount = 被查看次数
								//   VideoDuration = 视频长度（分钟）
								//   VideoThumbNail = 视频截图
//								JSONArray array = new JSONArray(Data);
							if (pageIndex==1) {
								
								l_collect_data = new ArrayList<HashMap<String, Object>>();
							}

								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.optJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();

									map.put("MF_ID", obj.getString("MF_ID"));
									map.put("mate_id", obj.getString("mate_id"));
									map.put("name", obj.getString("name"));
									map.put("url", obj.getString("url"));
									map.put("filetype",
											obj.getString("filetype"));
									map.put("uid", obj.getString("uid"));
									map.put("createtime",
											obj.getString("createtime"));
									map.put("type", obj.getString("type"));
									map.put("teachername",
											obj.getString("teachername"));
									
									//新加
									map.put("ST_ID", obj.getString("ST_ID"));

									l_collect_data.add(map);
								}
								if ((l_collect_data.size()/pageIndex) < 10) {
									fresh = true;
								}else{
									fresh = false;
								}
								if (l_collect_data.size()>0) {
									//有数据
									listview_myself_collect.setVisibility(View.VISIBLE);
									nulldata.setVisibility(View.GONE);
								}else{
									//无数据
									listview_myself_collect.setVisibility(View.GONE);
									nulldata.setVisibility(View.VISIBLE);
									txtnull.setText("暂无资料");;
								}
									
									
									if (pageIndex==1) {
										
									  adapter = new MycollectDataAdapter(
												getActivity(), l_collect_data);
										listview_myself_collect.setAdapter(adapter);
									}else{
										adapter.notifyDataSetChanged();
									}
								}else{
									
									listview_myself_collect.setAdapter(null);
								}
								refresh(listview_myself_collect, adapter);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
						Log.d(TAG, "xutil请求失败原因" + error.toString());
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
			final MycollectDataAdapter adapter) {
		listview.setMode(Mode.BOTH);
		initIndicator();
		listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pageIndex = 1;
				getdatafromnet();
				new FinishRefresh().execute();
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				getdatafromnet();
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
			listview_myself_collect.onRefreshComplete();
		}
	}

	/**
	 * 自定义上拉刷新和下拉加载的显示文字
	 */
	private void initIndicator() {
		ILoadingLayout startLabels = listview_myself_collect
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("放开以加载");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = listview_myself_collect.getLoadingLayoutProxy(false, true);
		System.out.println("fresh====="+fresh);
		if (fresh == true) {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("全部加载完毕");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		} else {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("加载中。。。");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		}
	}
	//增加首页浏览总数
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
	/**
	 * 增加浏览次数
	 * */
	private void addreadcount(String mateId) {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_ADDREADCOUNT;

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
						// System.out.println("onFailure");
						Log.d(TAG, error.toString());

					}

				});

	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "public class onresume!!");
		getdatafromnet();
	}
	
	public void cancelCollection(int position) {
		try {
			// loading and cancel from net , do follow method when onsuccess. notice that onUiThread
			mateId = l_collect_data.get(position).get("mate_id").toString();
			ST_ID = l_collect_data.get(position).get("ST_ID").toString();
			optType = "0";
			LodDialogClass.showCustomCircleProgressDialog(getActivity(), "", "正在取消...");
			setcollectdata(position);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 收藏功能
	 * */
	private void setcollectdata(final int position) {
		
	
		System.out.println("mateId====="+mateId+"=====optType===="+optType+"===ST_ID===="+ST_ID);
		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_COLLECT_DATA + "?mateId="
				+ mateId + "&optType=" + optType+"&ST_ID="+ST_ID;

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// params.addBodyParameter("pageIndex", pageIndex);
		// params.addBodyParameter("mateId", mateId);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Activity con = getActivity();
						Log.d(TAG, "getdatafromnet()执行之接收数据成功");

						Log.d(TAG, "收藏资料的数据结果为=========" + responseInfo.result);

						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							if (Result.equalsIgnoreCase("true")) {
								Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
								if (con!=null) {
									con.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											if (null != adapter) {
												l_collect_data.remove(position);
												if (l_collect_data.size()>0) {
													//有数据
													listview_myself_collect.setVisibility(View.VISIBLE);
													nulldata.setVisibility(View.GONE);
												}else{
													//无数据
													listview_myself_collect.setVisibility(View.GONE);
													nulldata.setVisibility(View.VISIBLE);
													txtnull.setText("暂无课堂");
												}
												adapter.reset(l_collect_data);
												LodDialogClass.closeCustomCircleProgressDialog();
											}

										}
									});
									
								}else{
									LodDialogClass.closeCustomCircleProgressDialog();
								}
							}else{
								Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
								LodDialogClass.closeCustomCircleProgressDialog();
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


}