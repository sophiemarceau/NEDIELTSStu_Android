package com.lels.student.testpredictions.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.drocode.swithcer.predict.PreGuideGallery;
import com.drocode.swithcer.predict.PreImageAdapter;
import com.example.strudentlelts.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.main.activity.MainActivity;
import com.lels.student.studyonline.StudyADVDTEXTetailActivity;
import com.lels.student.studyonline.StudyADVDetailActivity;
import com.lels.student.studyonline.StudyOnlineActivity;
import com.lels.student.testpredictions.adapter.TestPreditAdapter;
import com.lelts.tool.PrintTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class TestPredictionsActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "";

	private PullToRefreshListView listview_testpredictions_main;
	private ImageButton imageview_back;

	private String token;

	private PrintTool print;

	private List<HashMap<String, Object>> l_map;

	private TestPreditAdapter adapter;

	private int index = 1;

	private String imgurl = Constants.URL_UserloadAdvertisements;

	private List<String> imgpath = new ArrayList<String>();

	private List<HashMap<String, Object>> l_images = new ArrayList<HashMap<String, Object>>();

	private String[] str_images;

	// 循环滚动 广告位设置
	public List<String> urls;
	public PreGuideGallery images_ga;
	private int positon = 0;
	private Thread timeThread = null;
	public boolean timeFlag = true;
	private boolean isExit = false;
	public ImageTimerTask timeTaks = null;
	Uri uri;
	Intent intent;
	int gallerypisition = 0;
	private boolean flag = false;
	private LinearLayout pointLinear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testpredictions_main);
		LodDialogClass.showCustomCircleProgressDialog(
				TestPredictionsActivity.this, null,
				getString(R.string.common_Loading));
		initadv();
		print = new PrintTool(TestPredictionsActivity.this);
		getdatafromshare();
		init();
		getimaghttp();
		getdatafromnet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		// setContentView(R.layout.activity_testpredictions_main);
		// initadv();
		// print = new PrintTool(TestPredictionsActivity.this);
		// getdatafromshare();
		// init();
		// getimaghttp();
		getdatafromnet();
		super.onRestart();
	}

	private void initadv() {
		timeTaks = new ImageTimerTask();
		autoGallery.scheduleAtFixedRate(timeTaks, 5000, 5000);
		timeThread = new Thread() {
			public void run() {
				while (!isExit) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synchronized (timeTaks) {
						if (!timeFlag) {
							timeTaks.timeCondition = true;
							timeTaks.notifyAll();
						}
					}
					timeFlag = true;
				}
			};
		};
		timeThread.start();
	}

	private void init() {

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		listview_testpredictions_main = (PullToRefreshListView) findViewById(R.id.listview_testpredictions_main);

		pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		// pointLinear.setBackgroundColor(Color.argb(200, 135, 135, 152));

		imageview_back.setOnClickListener(this);

		images_ga = (PreGuideGallery) findViewById(R.id.image_wall_gallery);
		images_ga.setImageActivity(this);


	}

	private void getimaghttp() {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);
		params.addBodyParameter("type", "1003");
		// final String imgurls =
		// "http://testielts2.staff.xdf.cn/upload_dev/userImage/1437708866579838.jpg";
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		// 设置默认请求的缓存时间
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(HttpMethod.POST, imgurl, params,
				new RequestCallBack<String>() {

					private String interval;

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println("onFailure");
						LodDialogClass.closeCustomCircleProgressDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String result = arg0.result;
						// System.out.println(result);
						Log.d(TAG, result);
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject objdata = obj.getJSONObject("Data");
							JSONArray arraylist = objdata.getJSONArray("list");
							l_images = new ArrayList<HashMap<String, Object>>();
							str_images = new String[arraylist.length()];
							if (arraylist.length() > 0) {
								for (int i = 0; i < arraylist.length(); i++) {

									// "list": [
									// {
									// "Picture": null,
									// "Sort": 1,
									// "Link": null,
									// "Interval": 300,
									// "Content": null,
									// "Title": null
									// },

									JSONObject obj_i = arraylist
											.optJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("Picture",
											obj_i.getString("Picture"));
									// map.put("Picture",
									// "http://testielts2.staff.xdf.cn/upload_dev/userImage/1437708866579838.jpg");
									map.put("Sort", obj_i.getString("Sort"));
									map.put("Link", obj_i.getString("Link"));
									map.put("Interval",
											obj_i.getString("Interval"));
									map.put("Content",
											obj_i.getString("Content"));
									map.put("Title", obj_i.getString("Title"));

									// if
									// (!obj_i.getString("Picture").toString()
									// .equalsIgnoreCase("null")) {
									// str_images[i] = new
									// Constants().URL_IMAGE_ADVERT+obj.getString("Picture")
									// .toString();
									str_images[i] = obj_i.getString("Picture")
											.toString();
									// } else {
									// str_images[i] =
									// "http://www.apkbus.com/template/devc/style/logo.png";
									// }

									l_images.add(map);
								}
								// 开始加载数据 添加适配器
								InitViewPager(l_images);
							} else {
								System.out.println("没有数据");
							}
					
							
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});

	}

	/**
	 * 内容的请求数据
	 * 
	 * @param index
	 */
	private void setlistview(int index) {
		if (index == 1) {
			adapter = new TestPreditAdapter(TestPredictionsActivity.this, l_map);
			listview_testpredictions_main.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		adapter.notifyDataSetInvalidated();
		refresh(listview_testpredictions_main, adapter);
		listview_testpredictions_main
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						arg2 = arg2 - 1;
						Intent intent = new Intent();
						intent.setClass(TestPredictionsActivity.this,
								TestPredictionsWebActivity.class);
						Bundle b = new Bundle();
						b.putString("Link", l_map.get(arg2).get("Link")
								.toString());
						b.putString("MF_ID", l_map.get(arg2).get("MF_ID")
								.toString());
						b.putString("ID", l_map.get(arg2).get("ID").toString());
						b.putString("Content", l_map.get(arg2).get("Content")
								.toString());
						b.putString("Title", l_map.get(arg2).get("Title")
								.toString());
						intent.putExtras(b);
						startActivity(intent);

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
			listview_testpredictions_main.onRefreshComplete();
		}
	}

	/**
	 * 刷新监听
	 * 
	 * @param listview
	 * @param adapter
	 */
	public void refresh(final PullToRefreshListView listview,
			final TestPreditAdapter adapter) {
		listview.setMode(Mode.BOTH);
		initIndicator();
		listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				index = 1;
				getdatafromnet();
				new FinishRefresh().execute();
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				index++;
				getdatafromnet();
				new FinishRefresh().execute();
				adapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * 自定义上拉刷新和下拉加载的显示文字
	 */
	private void initIndicator() {
		ILoadingLayout startLabels = listview_testpredictions_main
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("放开以加载");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = listview_testpredictions_main
				.getLoadingLayoutProxy(false, true);
		if (flag == true) {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("全部加载完成");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		} else {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("加载中。。。");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		}
	}

	private void getdatafromshare() {

		SharedPreferences share = this.getSharedPreferences("userinfo",
				MODE_PRIVATE);
		token = share.getString("Token", "");
		print.printforLog(TAG, token);

	}

	private void getdatafromnet() {

		Log.d(TAG, "解析获取我的预测" + "getdatafromnet()");

		String url = new Constants().URL_STUDYONLINE_TESTPREDICTION;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		params.addBodyParameter("pageIndex", String.valueOf(index));

		HttpUtils http = new HttpUtils();
		// 设置缓存5秒,0秒内直接返回上次成功请求的结果
		http.configCurrentHttpCacheExpiry(0);
		// 设置默认请求的缓存时间
		http.configDefaultHttpCacheExpiry(0);
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							Log.d(TAG, "解析获取我的收藏列表" + responseInfo.result);
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							if (index == 1) {
								l_map = new ArrayList<HashMap<String, Object>>();
							}
							if (Data != null) {
								JSONObject obj = new JSONObject(Data);
								// ID = 主键;
								//   Title = 名称;
								//   Link = 链接地址;
								//   CreateDate = 创建日期;
								//   IsPublish = 是否已经发布 0=未发布；1=已发布;
								//   OnTop = 是否置顶 0=不置顶；1=置顶;
								//   IsDelete = 是否已经删除 0=未删除；1=已删除;
								//   Content = 发布内容的富文本;
								//   isOpenForVisitor = 是否对游客公开 0=不公开 1=公开;

								// Link: http: //www.sina.com,
								// IsDelete: 0,
								// OnTop: 0,
								// ID: 12,
								// Content: ,
								// CreateDate: 06-172015,
								// Title: 标题1,
								// isOpenForVisitor: ,
								// IsPublish: 1

								String str_list = obj.getString("list");
								JSONArray array = new JSONArray(str_list);
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj_l = array.optJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("Link", obj_l.getString("Link"));
									map.put("IsDelete",
											obj_l.getString("IsDelete"));
									map.put("OnTop", obj_l.getString("OnTop"));
									map.put("ID", obj_l.getString("ID"));
									map.put("MF_ID", obj_l.getString("MF_ID"));
									map.put("Content",
											obj_l.getString("Content"));
									map.put("CreateDate",
											obj_l.getString("CreateDate"));
									map.put("Title", obj_l.getString("Title"));
									map.put("isOpenForVisitor",
											obj_l.getString("isOpenForVisitor"));
									map.put("IsPublish",
											obj_l.getString("IsPublish"));
									l_map.add(map);
								}
								if (l_map.size() / index < 10) {
									flag = true;
								} else {
									flag = false;
								}
								setlistview(index);
								LodDialogClass.closeCustomCircleProgressDialog();

							}
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.d(TAG, "请求失败原因" + error.toString());
						LodDialogClass.closeCustomCircleProgressDialog();
					}

				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 后退按钮
		case R.id.imageview_back:
			// Intent intent = new Intent();
			// intent.setClass(TestPredictionsActivity.this,
			// MainActivity.class);
			// startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	public void changePointView(int cur) {
		pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		View view = pointLinear.getChildAt(positon);
		View curView = pointLinear.getChildAt(cur);
		if (view != null && curView != null) {
			ImageView pointView = (ImageView) view;
			ImageView curPointView = (ImageView) curView;
			pointView.setBackgroundResource(R.drawable.feature_point);
			curPointView.setBackgroundResource(R.drawable.feature_point_cur);
			positon = cur;
		}
	}

	final Handler autoGalleryHandler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			switch (message.what) {
			case 1:
				images_ga.setSelection(message.getData().getInt("pos"));
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		timeFlag = false;
		// timeTaks.timeCondition = true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		timeTaks.timeCondition = false;
	}

	public class ImageTimerTask extends TimerTask {
		public volatile boolean timeCondition = true;

		// int gallerypisition = 0;
		public void run() {
			synchronized (this) {
				while (!timeCondition) {
					try {
						Thread.sleep(100);
						// wait();
					} catch (InterruptedException e) {
						Thread.interrupted();
					}
				}
			}
			try {
				gallerypisition = images_ga.getSelectedItemPosition() + 1;
				System.out.println(gallerypisition + "");
				Message msg = new Message();
				Bundle date = new Bundle();
				date.putInt("pos", gallerypisition);
				msg.setData(date);
				msg.what = 1;// ��Ϣ��ʶ
				autoGalleryHandler.sendMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	Timer autoGallery = new Timer();

	protected void InitViewPager(final List<HashMap<String, Object>> l_images2) {
		PreImageAdapter imageAdapter = new PreImageAdapter(this, str_images);
		images_ga.setAdapter(imageAdapter);

		for (int i = 0; i < l_images2.size(); i++) {
			ImageView pointView = new ImageView(this);
			if (i == 0) {
				pointView.setBackgroundResource(R.drawable.feature_point_cur);
			} else
				pointView.setBackgroundResource(R.drawable.feature_point);
			pointLinear.addView(pointView);
		}
		images_ga.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent();
				// intent.setClass(TestPredictionsActivity.this,
				// StudyADVDetailActivity.class);
				String ss = l_images2.get(position % l_images2.size())
						.get("Link").toString();
				System.out.println("Link.toString()===" + ss);

				if (!ss.equalsIgnoreCase("")) {
					intent.setClass(TestPredictionsActivity.this,
							StudyADVDetailActivity.class);
				} else {
					intent.setClass(TestPredictionsActivity.this,
							StudyADVDetailActivity.class);
				}

				Bundle b = new Bundle();
				// b.putString("url", l_images2.get(position % l_images2.size())
				// .get("Link").toString());
				// b.putString("Title", l_images2.get(position %
				// l_images2.size())
				// .get("Title").toString());
				// b.putString(
				// "Content",
				// l_images2.get(position % l_images2.size())
				// .get("Content").toString());
				// intent.putExtras(b);
				// startActivity(intent);
				b.putString("url", l_images2.get(position % l_images2.size())
						.get("Link").toString());
				b.putString("Title", l_images2.get(position % l_images2.size())
						.get("Title").toString());
				b.putString(
						"Content",
						l_images2.get(position % l_images2.size())
								.get("Content").toString());
				intent.putExtras(b);
				startActivity(intent);
			}
		});
	}

}
