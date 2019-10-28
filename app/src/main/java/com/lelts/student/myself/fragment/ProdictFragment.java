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
import com.lels.student.testpredictions.activity.TestPredictionsWebActivity;
import com.lelts.student.myself.adapter.MycollectPreditFavoriteAdapter;
import com.lelts.student.myself.adapter.MycollectPublicclassAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
/**
 * myself collect
 * prodict
 * **/
public class ProdictFragment extends Fragment {

	private static final String TAG = "PublicclassFragment";

	private View mview;
	private PullToRefreshListView listview_myself_collect;

	private String token;
	private MycollectPreditFavoriteAdapter adapter;
	private int pageIndex = 1;
	
	private List<HashMap<String, Object>> l_collect_predict = new ArrayList<HashMap<String,Object>>();
	//判断是否有数据的布局
	private RelativeLayout nulldata;
	private TextView txtnull;
	//判断数据是否全部加载完成
	private boolean fresh = false;
	//取消收藏的一些参数
	private  String ID,optType;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mview = inflater.inflate(R.layout.fargment_myself_mycollect, null);
		Log.d(TAG, "ProdictFragment");
		initview();
		getdatafromshare();
		getdatafromnet();
		return mview;
	}

	private void getdatafromshare() {

		SharedPreferences share = getActivity().getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");

		Log.d(TAG, "获取的token数值为=====" + token);

	}

	private void initview() {
		listview_myself_collect = (PullToRefreshListView) mview
				.findViewById(R.id.listview_myself_collect);
		nulldata = (RelativeLayout) mview.findViewById(R.id.relative_warn_nulldata_publicclass);
		txtnull = (TextView) mview.findViewById(R.id.txt_warn_nulldata_class_main);
		//点击事件
//		listview_myself_collect.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				position = position - 1;
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), TestPredictionsWebActivity.class);
//				Bundle b = new Bundle();
//				b.putString("Link", l_collect_predict.get(position).get("Link").toString());
//				b.putString("MF_ID", l_collect_predict.get(position).get("MF_ID").toString());
//				b.putString("ID", l_collect_predict.get(position).get("ID").toString());
//				b.putString("Content", l_collect_predict.get(position).get("Content").toString());
//				b.putString("Title", l_collect_predict.get(position).get("Title").toString());
//				intent.putExtras(b);
//				getActivity().startActivity(intent);				
//			}
//		});
		
	}
	
	/**
	 * listview的点击事件
	 */
	public void onitemclick(int position) {
		// TODO Auto-generated method stub
//		position = position - 1;
		Intent intent = new Intent();
		intent.setClass(getActivity(), TestPredictionsWebActivity.class);
		Bundle b = new Bundle();
		b.putString("Link", l_collect_predict.get(position).get("Link").toString());
		b.putString("MF_ID", l_collect_predict.get(position).get("MF_ID").toString());
		b.putString("ID", l_collect_predict.get(position).get("ID").toString());
		b.putString("Content", l_collect_predict.get(position).get("Content").toString());
		b.putString("Title", l_collect_predict.get(position).get("Title").toString());
		intent.putExtras(b);
		getActivity().startActivity(intent);				
	}

	private void getdatafromnet() {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_MYSELF_MYCOLLECT_PREDICT
				+ "?pageIndex=" + pageIndex;// 公开课资料

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// pageIndex
		// params.addBodyParameter("pageIndex", "1");

		HttpUtils http = new HttpUtils();
	//	http.configCurrentHttpCacheExpiry(1000 * 10);
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

						Log.d("DataDFm", "获取资料的数据" + responseInfo.result);

						try {

							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							if (!Data.equalsIgnoreCase("null")) {
								//  MF_ID = 收藏主键;
								//   ID = 预测主键;
								//   Title = 名称;
								//   Link = 链接地址;
								//   CreateDate = 创建日期;
								//   IsPublish = 是否已经发布 0=未发布；1=已发布;
								//   OnTop = 是否置顶 0=不置顶；1=置顶;
								//   IsDelete = 是否已经删除 0=未删除；1=已删除;
								//   Content = 发布内容的富文本;
								JSONArray array = new JSONArray(Data);
								if (pageIndex==1) {
									
									l_collect_predict = new ArrayList<HashMap<String, Object>>();
								}

								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.optJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("MF_ID", obj.getString("MF_ID"));
									map.put("ID", obj.getString("ID"));
									map.put("Title", obj.getString("Title"));
									map.put("Link", obj.getString("Link"));
									map.put("CreateDate",
											obj.getString("CreateDate"));
									map.put("IsPublish",
											obj.getString("IsPublish"));
									map.put("OnTop", obj.getString("OnTop"));
									map.put("IsDelete",
											obj.getString("IsDelete"));
									map.put("Content", obj.getString("Content"));
									l_collect_predict.add(map);
								}
								if ((l_collect_predict.size()/pageIndex) < 10) {
									fresh = true;
								}else{
									fresh = false;
								}

								if (l_collect_predict.size() > 0) {
									//有数据
									listview_myself_collect.setVisibility(View.VISIBLE);
									nulldata.setVisibility(View.GONE);
									
								}else{
									//无数据
									listview_myself_collect.setVisibility(View.GONE);
									nulldata.setVisibility(View.VISIBLE);
									txtnull.setText("暂无预测");
									listview_myself_collect.setAdapter(null);
								}
								if (pageIndex == 1) {
									System.out.println(l_collect_predict.toString());
									adapter = new MycollectPreditFavoriteAdapter(
											getActivity(),
											l_collect_predict);
									listview_myself_collect
											.setAdapter(adapter);
								} else {
									adapter.notifyDataSetChanged();
								}
								refresh(listview_myself_collect, adapter);
							}else{
								listview_myself_collect.setAdapter(null);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.d(TAG, "http get error =====" + error.toString());
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
			final MycollectPreditFavoriteAdapter adapter) {
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
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "public class onresume!!");
		getdatafromnet();
	}
	
	public void cancelCollection(int position) {
		try {
			// loading and cancel from net , do follow method when onsuccess. notice that onUiThread
			ID = l_collect_predict.get(position).get("ID").toString();
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
	
		System.out.println("ID====="+ID+"=====optType===="+optType);
		@SuppressWarnings("static-access")
		String url = new Constants().URL_PREDICT_COLLECT_DATA;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("pId", ID);
		params.addBodyParameter("optType", optType);
		System.out.println("pId========" + ID + "=====optType===" + optType);
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
												l_collect_predict.remove(position);
												if (l_collect_predict.size()>0) {
													//有数据
													listview_myself_collect.setVisibility(View.VISIBLE);
													nulldata.setVisibility(View.GONE);
												}else{
													//无数据
													listview_myself_collect.setVisibility(View.GONE);
													nulldata.setVisibility(View.VISIBLE);
													txtnull.setText("暂无课堂");
												}
												adapter.reset(l_collect_predict);
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