package com.lels.student.studyonline;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strudentlelts.R;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.main.activity.MainActivity;
import com.lels.student.connectionclass.activity.ConnetionStartTestActivity;
import com.lels.student.connectionclass.activity.StartListeningTestActivity;
import com.lelts.tool.IntentUtlis;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class DataStudyDetailActivity extends Activity implements OnClickListener {

	private static final String TAG = "DataStudyDetailActivity";
	private RelativeLayout goneVisiable;
	private ImageButton imageview_back;
	private TextView textview_data_collect;
	private TextView textview_data_type;// 资料类型
	private TextView textview_data_teacher_name;// 老师名字
	private TextView textview_data_createtime;// 创建 时间
	private TextView textview_data_looknum;// 浏览次数
	private SharedPreferences stuShare, stuInfo;
	private WebView webview_data_class_video;

	private Button btn_data_anster;

	private String mate_id;
	private String token;

	private String ST_ID;

	private String MF_ID;

	private String url_video = "";

	private String optType;

	private String tagUsername;

	private HashMap<String, Object> map_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studyonline_studydata_detail);
		init();
		LodDialogClass.showCustomCircleProgressDialog(DataStudyDetailActivity.this, null, getString(R.string.xlistview_header_hint_loading));

		getDataFromShare();
		if (tagUsername.equals("游客")) {
			textview_data_collect.setVisibility(View.GONE);
		}
		getDataFromIntent();
		getDataInfo();
		initView();
		getDataForIsCollect();
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
		LodDialogClass.showCustomCircleProgressDialog(DataStudyDetailActivity.this, null, getString(R.string.xlistview_header_hint_loading));

		getDataFromShare();
		if (tagUsername.equals("游客")) {
			textview_data_collect.setVisibility(View.GONE);
		}
		getDataFromIntent();
		getDataInfo();
		initView();
		getDataForIsCollect();
		
	}

	private void getDataForIsCollect() {
		Log.d(TAG, "getdatafromnet()执行");
		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_MEDIA_ISCOLLECT + "?type=0&id=" + mate_id;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		// params.addBodyParameter("mateId", mateId);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Log.d(TAG, "是否收藏的  数据结果为=========" + responseInfo.result);
				try {
					JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");
					// result = 0表示没有收藏，大于0表示已收藏;
					JSONObject obj = new JSONObject(Data);
					String result = obj.getString("result");
					if (result.equalsIgnoreCase("0")) {
						System.out.println("MF_ID为空，可以收藏");
						optType = "1";
						textview_data_collect.setText("收藏");
					} else {
						System.out.println("MF_ID不为空，可以取消收藏");
						optType = "0";
						textview_data_collect.setText("取消收藏");
					}
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

	private void initView() {
		System.out.println("获取到 url_video===" + url_video);
		imageview_back.setOnClickListener(this);
		textview_data_collect.setOnClickListener(this);
	}

	private void getDataFromIntent() {
		Bundle b = getIntent().getExtras();
		mate_id = b.getString("mate_id");
		ST_ID = b.getString("ST_ID");
		System.out.println("---" + mate_id);
		showView(mate_id);
	}

	// 获取url
	private void showView(String mate_id) {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		params.addBodyParameter("mateId", mate_id);
		String url = Constants.URL_HomegetMaterialsInfo;
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					System.out.println(arg0.result);
					JSONObject str = new JSONObject(arg0.result);
					JSONObject obj = str.getJSONObject("Data");
					url_video = obj.getString("Url");
					setWebview(webview_data_class_video, url_video);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void getDataFromShare() {
		SharedPreferences share = DataStudyDetailActivity.this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");
		Log.d(TAG, "获取的token数值为=====" + token);
	}

	@SuppressWarnings("static-access")
	private void init() {
		btn_data_anster = (Button) findViewById(R.id.btn_data_anster);
		SharedPreferences stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		tagUsername = stushare.getString("username", "");
		textview_data_collect = (TextView) findViewById(R.id.textview_data_collect);
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		textview_data_type = (TextView) findViewById(R.id.textview_data_type);
		textview_data_teacher_name = (TextView) findViewById(R.id.textview_data_teacher_name);
		textview_data_createtime = (TextView) findViewById(R.id.textview_data_createtime);
		webview_data_class_video = (WebView) findViewById(R.id.webview_data_class_video);
		btn_data_anster.setOnClickListener(this);
		stuInfo = getSharedPreferences("stuinfo", Context.MODE_PRIVATE);
		goneVisiable = (RelativeLayout) findViewById(R.id.goneorvisible);
	}

	private void setWebview(WebView wv, String url) {
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setPluginState(PluginState.ON);
		// wv.getSettings().setPluginsEnabled(true);//可以使用插件
		// wv.getSettings().setEnableSmoothTransition(true);
		wv.setEnabled(true);
		wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wv.getSettings().setAllowFileAccess(true);
		wv.getSettings().setDefaultTextEncodingName("UTF-8");
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setVisibility(View.VISIBLE);
		wv.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				LodDialogClass.closeCustomCircleProgressDialog();
			}
		});
		wv.loadUrl(url);
	}

	/**
	 * 收藏功能
	 */
	private void setCollectData() {

		Log.d(TAG, "getdatafromnet()执行");
		@SuppressWarnings("static-access")
		// String url = new Constants().URL_STUDYONLINE_COLLECT_DATA;
		String url = new Constants().URL_STUDYONLINE_COLLECT_DATA + "?mateId=" + mate_id + "&optType=" + optType+"&ST_ID="+ST_ID;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("mateId", mate_id);
		params.addBodyParameter("optType", optType);
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Log.d(TAG, "getdatafromnet()执行之接收数据成功");
				Log.d(TAG, "收藏资料的数据结果为=========" + responseInfo.result);
				try {
					JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");
					if (Result.equalsIgnoreCase("true")) {
						if (optType.equalsIgnoreCase("0")) {
							Toast.makeText(DataStudyDetailActivity.this, "取消收藏成功", 2).show();
							optType = "1";
							textview_data_collect.setText("收藏");
							LodDialogClass.closeCustomCircleProgressDialog();
						} else {
							Toast.makeText(DataStudyDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
							optType = "0";
							textview_data_collect.setText("取消收藏");
							LodDialogClass.closeCustomCircleProgressDialog();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.d(TAG, error.toString());
				LodDialogClass.closeCustomCircleProgressDialog();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.textview_data_collect:
			if (optType.endsWith("1")) {
				LodDialogClass.showCustomCircleProgressDialog(DataStudyDetailActivity.this, "", "收藏中...");
			}else{
				LodDialogClass.showCustomCircleProgressDialog(DataStudyDetailActivity.this, "", "取消收藏中...");
			}
			setCollectData();
			break;
		// 答题按钮
		case R.id.btn_data_anster:
			Intent intent = new Intent();
			intent.setClass(DataStudyDetailActivity.this, ConnetionStartTestActivity.class);
			Editor ed = stuInfo.edit();
			ed.putInt("testtype", 2);
			ed.putString("domainPFolder", map_info.get("domainPFolder").toString());
			ed.putString("P_ID", map_info.get("P_ID").toString());
			ed.putString("mateId", mate_id);
			ed.putString("ST_ID", ST_ID);
			System.out.println("ST_ID====" + ST_ID);
			// intent.putExtra("testtype", 2);
			// intent.putExtra("domainPFolder",map_info.get("domainPFolder").toString());
			// intent.putExtra("P_ID", map_info.get("P_ID").toString());
			// intent.putExtra("mateId", mateId);
			ed.commit();
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		webview_data_class_video.reload();
		Log.d(TAG, "onpause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		webview_data_class_video.stopLoading();
		Log.d(TAG, "onStop");
		super.onStop();
	}

	/**
	 * 任务-视频资料中试题库 iphone、Android 学生App
	 */
	private void getDataInfo() {
		Log.d(TAG, "getdatafromnet()执行");
		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_PAPERSINFOBUMID;
		// + "?mateId="+mateId
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		params.addBodyParameter("mId", mate_id);
		// params.addBodyParameter("pageIndex", pageIndex);
		// params.addBodyParameter("mateId", mateId);
		HttpUtils http = new HttpUtils();
		// http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				Log.d(TAG, "getdatainfo()执行之接收数据成功" + responseInfo.result);
				try {
					JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");

					JSONObject obj = new JSONObject(Data);
					String checkData = obj.getString("checkData");
					if (checkData.equalsIgnoreCase("0")) {
						goneVisiable.setVisibility(View.GONE);
						return;
					} else {
						goneVisiable.setVisibility(View.VISIBLE);
						// P_ID = 试卷主键;
						// PaperNumber = 试卷编号;
						// Name = 试卷名称;
						// Target = 试卷用途 1=模考 2=练习 3=测试;
						// Category = 试卷标签;

						// CreateTime = 创建时间;
						// SubjectiveIn = 是否包含主观题 0=不包含 1=包含;
						// RoleID = 创建人角色ID;
						// IsDelete = 是否已删除 0=未删除 1=已删除;
						// IsPublic = 是否已发布 0=未发布 1=已发布;
						// UpdateTime = 更新时间;
						// PaperState = 试卷状态 0=初始创建 2=发布成功等待打包 3=打包完成;
						// PaperVersion = 试卷版本号;

						// PaperZip = 打包后的文件名;
						// PaperFolder = 打包前试卷文件存储目录;
						// domainPFolder =;
						// domainPZip =;
						// ST_ID =任务ID;
						// CC_ID =课次ID;
						map_info = new HashMap<String, Object>();
						map_info.put("P_ID", obj.getString("P_ID"));
						map_info.put("PaperNumber", obj.getString("PaperNumber"));
						map_info.put("Name", obj.getString("Name"));
						map_info.put("Target", obj.getString("Target"));
						map_info.put("Category", obj.getString("Category"));
						map_info.put("CreateTime", obj.getString("CreateTime"));
						map_info.put("SubjectiveIn", obj.getString("SubjectiveIn"));
						map_info.put("RoleID", obj.getString("RoleID"));
						map_info.put("IsDelete", obj.getString("IsDelete"));
						map_info.put("IsPublic", obj.getString("IsPublic"));
						map_info.put("UpdateTime", obj.getString("UpdateTime"));
						map_info.put("PaperState", obj.getString("PaperState"));
						map_info.put("PaperVersion", obj.getString("PaperVersion"));
						map_info.put("PaperZip", obj.getString("PaperZip"));
						map_info.put("PaperFolder", obj.getString("PaperFolder"));
						map_info.put("domainPFolder", obj.getString("domainPFolder"));
						map_info.put("domainPZip", obj.getString("domainPZip"));
						// map.put("ST_ID", obj.getString("ST_ID"));
						// map.put("CC_ID", obj.getString("CC_ID"));

					}

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
}
