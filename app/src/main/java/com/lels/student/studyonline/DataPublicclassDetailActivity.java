package com.lels.student.studyonline;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lels.student.connectionclass.activity.ConnetionStartTestActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class DataPublicclassDetailActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "DataPublicclassDetailActivity";

	private String token;
	// 列表传过来的数据
	private String mateId;
	
	private String MF_ID;
	private String class_name;
	
	private String optType="1";
	private ImageButton imageview_back;
	private TextView textview_data_collect;
	private TextView textview_video_type;// 类型
	private TextView textview_data_type;// 资料类型
	private TextView textview_data_teacher_name;// 老师名字
	private TextView textview_data_createtime;// 创建 时间
	private TextView textview_data_looknum;// 浏览次数

	private String url_video = "";

	private WebView webview_public_class_video;

	private Button btn_is_answer;
	// 格式化时间
	private static SimpleDateFormat sf = null;
	private SharedPreferences stushare,stuinfo;

	private String tagusername;
	
	private HashMap<String, Object> map_info;

	private TextView textview_class_video_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studyonline_publicclass_detail);

		init();

		getdatafromshare();
		getdatafromintent();
		if (tagusername.equals("游客")) {
			textview_data_collect.setVisibility(View.GONE);
			btn_is_answer.setVisibility(View.GONE);
		} else {
			getdatainfo();
		}
		getdatafrovideo();

//		initview();
	}

//	private void initview() {
//		url_video = new Constants().URL_STUDYONLINE_STUDY_PUBLICCLASS_DETAIL_VIDEO
//				+ mateId;
//		System.out.println("webview的地址====="+webview_public_class_video);
//		setwebview(webview_public_class_video, url_video);
//
//		/*// 设置数据
//		textview_class_video_name.setText("名称 "+class_name);*/
//	}

	private void getdatafromintent() {
		Bundle b = getIntent().getExtras();
		mateId = b.getString("Mate_ID");
		MF_ID = b.getString("MF_ID");
		class_name = b.getString("name");

		url_video = new Constants().URL_STUDYONLINE_STUDY_PUBLICCLASS_DETAIL_VIDEO
				+ mateId;
		System.out.println("url_video的路径====" + url_video);
		setwebview(webview_public_class_video, url_video);

		// 设置数据
		textview_class_video_name.setText(class_name);

		System.out.println("MF_ID===" + MF_ID);
		System.out.println("mateId===资料ID===" + mateId);
		if (MF_ID.equalsIgnoreCase("null")) {
			System.out.println("MF_ID为空，可以收藏");
			optType = "1";
			textview_data_collect.setText("收藏");
		} else {
			System.out.println("MF_ID不为空，可以取消收藏");
			optType = "0";
			textview_data_collect.setText("取消收藏");
		}

	}

	private void getdatafrovideo() {

		Log.d(TAG, "getdatafromnet()执行");

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_LOOKVIDEOINFO;

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

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

						Log.d(TAG, "video detail 数据结果为========="
								+ responseInfo.result);

						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							// "Name": "阅读",
							// "sName": null,
							// "ReadCount": 116,
							// "CreateTime": "2015-04-14",
							// "FileType": "FLV"
							if (Result.equalsIgnoreCase("false")) {
								return;
							} else {

								JSONObject obj = new JSONObject(Data);
								String Name = obj.getString("Name");
								String sName = obj.getString("sName");
								String ReadCount = obj.getString("ReadCount");
								String CreateTime = obj.getString("CreateTime");
								String FileType = obj.getString("FileType");
								// 设置 各个字段的属性
								textview_data_createtime.setText(CreateTime);
								textview_data_type.setText(FileType);
								textview_data_teacher_name.setText(sName);
								
								if(Name.equalsIgnoreCase("null")){
									
								}else{
									textview_video_type.setText(Name);
								}
							
								textview_data_looknum.setText(ReadCount);

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
	

	private void init() {
		stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		tagusername = stushare.getString("username", "");
		textview_data_collect = (TextView) findViewById(R.id.textview_data_collect);
		btn_is_answer = (Button) findViewById(R.id.btn_is_answer);

		textview_class_video_name = (TextView) findViewById(R.id.textview_class_video_name);
		textview_video_type = (TextView) findViewById(R.id.textview_video_type);

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		textview_data_type = (TextView) findViewById(R.id.textview_data_type);
		textview_data_teacher_name = (TextView) findViewById(R.id.textview_data_teacher_name);
		textview_data_createtime = (TextView) findViewById(R.id.textview_data_createtime);

		textview_data_looknum = (TextView) findViewById(R.id.textview_data_looknum);

		webview_public_class_video = (WebView) findViewById(R.id.webview_public_class_video);

		imageview_back.setOnClickListener(this);
		textview_data_collect.setOnClickListener(this);
		btn_is_answer.setOnClickListener(this);
		stuinfo = getSharedPreferences("stuinfo", Context.MODE_PRIVATE);

	}

	private void setwebview(WebView wv, String url) {
		wv.getSettings().setJavaScriptEnabled(true);
		/*wv.getSettings().setPluginState(PluginState.ON);
		// wv.getSettings().setPluginsEnabled(true);//可以使用插件
		// wv.getSettings().setEnableSmoothTransition(true);
		wv.setEnabled(true);
		wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wv.getSettings().setAllowFileAccess(true);
		wv.getSettings().setDefaultTextEncodingName("UTF-8");
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setVisibility(View.VISIBLE);*/
		wv.loadUrl(url);
		wv.setWebViewClient(new WebViewClient(){

		      @Override
		      public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        // TODO Auto-generated method stub
		        view.loadUrl(url);
		        return true;
		      }
		    });
	}

	private void getdatafromshare() {
		SharedPreferences share = DataPublicclassDetailActivity.this
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");

		Log.d(TAG, "获取的token数值为=====" + token);

	}

	/**
	 * 收藏功能
	 * */
	private void setcollectdata() {

		Log.d(TAG, "getdatafromnet()执行");

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_COLLECT_DATA + "?mateId="
				+ mateId + "&optType=" + optType;

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// params.addBodyParameter("pageIndex", pageIndex);
		// params.addBodyParameter("mateId", mateId);

		HttpUtils http = new HttpUtils();
//		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

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
									Toast.makeText(
											DataPublicclassDetailActivity.this,
											"取消收藏成功", 2).show();
									optType = "1";
									textview_data_collect.setText("收藏");
								} else {
									Toast.makeText(
											DataPublicclassDetailActivity.this,
											"收藏成功", Toast.LENGTH_SHORT).show();
									optType = "0";
									textview_data_collect.setText("取消收藏");
								}

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

	/**
	 * 任务-视频资料中试题库 iphone、Android 学生App
	 * */
	private void getdatainfo() {

		Log.d(TAG, "getdatafromnet()执行");

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_PAPERSINFOBUMID;
		// + "?mateId="+mateId

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		params.addBodyParameter("mId", mateId);

		// params.addBodyParameter("pageIndex", pageIndex);
		// params.addBodyParameter("mateId", mateId);

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

						Log.d(TAG, "getdatainfo()执行之接收数据成功"
								+ responseInfo.result);

						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
							System.out.println("请求的数据===="+str);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							JSONObject obj = new JSONObject(Data);
							String checkData = obj.getString("checkData");
							if (checkData.equalsIgnoreCase("0")) {
								btn_is_answer.setVisibility(View.INVISIBLE);
								return;
							} else {
								btn_is_answer.setVisibility(View.VISIBLE);
								// P_ID = 试卷主键;
								//   PaperNumber = 试卷编号;
								//   Name = 试卷名称;
								//   Target = 试卷用途 1=模考 2=练习 3=测试;
								//   Category = 试卷标签;

								//   CreateTime = 创建时间;
								//   SubjectiveIn = 是否包含主观题 0=不包含 1=包含;
								//   RoleID = 创建人角色ID;
								//   IsDelete = 是否已删除 0=未删除 1=已删除;
								//   IsPublic = 是否已发布 0=未发布 1=已发布;
								//   UpdateTime = 更新时间;
								//   PaperState = 试卷状态 0=初始创建 2=发布成功等待打包 3=打包完成;
								//   PaperVersion = 试卷版本号;

								//   PaperZip = 打包后的文件名;
								//   PaperFolder = 打包前试卷文件存储目录;
								//   domainPFolder =;
								//   domainPZip =;
								//   ST_ID =任务ID;
								//   CC_ID =课次ID;
								map_info = new HashMap<String, Object>();
								map_info.put("P_ID", obj.getString("P_ID"));
								map_info.put("PaperNumber",
										obj.getString("PaperNumber"));
								map_info.put("Name", obj.getString("Name"));
								map_info.put("Target", obj.getString("Target"));
								map_info.put("Category", obj.getString("Category"));
								map_info.put("CreateTime",
										obj.getString("CreateTime"));
								map_info.put("SubjectiveIn",
										obj.getString("SubjectiveIn"));
								map_info.put("RoleID", obj.getString("RoleID"));
								map_info.put("IsDelete", obj.getString("IsDelete"));
								map_info.put("IsPublic", obj.getString("IsPublic"));
								map_info.put("UpdateTime",
										obj.getString("UpdateTime"));
								map_info.put("PaperState",
										obj.getString("PaperState"));
								map_info.put("PaperVersion",
										obj.getString("PaperVersion"));
								map_info.put("PaperZip", obj.getString("PaperZip"));
								map_info.put("PaperFolder",
										obj.getString("PaperFolder"));
								map_info.put("domainPFolder",
										obj.getString("domainPFolder"));
								map_info.put("domainPZip",
										obj.getString("domainPZip"));
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.textview_data_collect:// 收藏功能
			setcollectdata();
			break;
			//答题按钮
		case R.id.btn_is_answer:
			Intent intent = new Intent();
			
			intent.setClass(DataPublicclassDetailActivity.this,
					ConnetionStartTestActivity.class);
			Editor ed = stuinfo.edit();
			ed.putInt("testtype", 2);
			ed.putString("domainPFolder", map_info.get("domainPFolder").toString());
			ed.putString("P_ID", map_info.get("P_ID").toString());
			ed.putString("mateId", mateId);
//			intent.putExtra("testtype", 2);
//			intent.putExtra("domainPFolder",map_info.get("domainPFolder").toString());
//			intent.putExtra("P_ID", map_info.get("P_ID").toString());
//			intent.putExtra("mateId", mateId);
			ed.commit();
			 startActivity(intent);
			 finish();
			break;
		default:
			break;
		}
	}
	@Override  
	protected void onPause ()  
	{  
	    webview_public_class_video.reload ();  
	    super.onPause ();  
	}  
}
