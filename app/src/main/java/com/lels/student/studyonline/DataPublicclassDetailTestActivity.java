package com.lels.student.studyonline;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strudentlelts.R;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.student.connectionclass.activity.ConnetionStartTestActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class DataPublicclassDetailTestActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "DataPublicclassDetailActivity";

	private String token;
	// 列表传过来的数据
	private String mateId;
	private String optType;
	private String ST_ID;

	private ImageButton imageview_back;
	private TextView textview_data_collect;
	private TextView textview_class_video_name;// ship品名称
	private TextView textview_video_type;// 类型
	private TextView textview_data_type;// 资料类型
	private TextView textview_data_teacher_name;// 老师名字
	private TextView textview_data_createtime;// 创建 时间
	private TextView textview_data_looknum;// 浏览次数

	private Button btn_is_answer;
	// 格式化时间B
	private static SimpleDateFormat sf = null;
	private SharedPreferences stushare,stuinfo;

	private String tagusername;
	
	private HashMap<String, Object> map_info;
	
	private SurfaceView surfaceView;
	private RelativeLayout rl_controller_play, rl_alter;
	private SeekBar skbProgress;
	private Player player;
	private LodDialogClass lodclass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studyonline_publicclass_detail_test);

		init();
		LodDialogClass.showCustomCircleProgressDialog(DataPublicclassDetailTestActivity.this, null, getString(R.string.xlistview_header_hint_loading));
		getdatafromshare();
		getdatafromintent();
		if (tagusername.equals("游客")) {
			textview_data_collect.setVisibility(View.GONE);
			btn_is_answer.setVisibility(View.GONE);
		} else {
			getdatainfo();
		}
		getdatafrovideo();
		
		getdataforiscollect();
		
		getdataforMediainfo();

	}


	private void getdataforMediainfo() {


		Log.d(TAG, "getdatafromnet()执行");

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_MEDIA_LOOKUPVIDEO;

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		params.addBodyParameter("mId", mateId);

		HttpUtils http = new HttpUtils();

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
							JSONObject Data = str.getJSONObject("Data");
							String VideoUrl = Data.getString("videoUrl");
							//setwebview(webview_public_class_video, VideoUrl);
							//loadVideo(VideoUrl);
							player.playUrl(VideoUrl);
							System.out.println("-----------p"+VideoUrl);
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


	private void getdataforiscollect() {


		Log.d(TAG, "getdatafromnet()执行");

		@SuppressWarnings("static-access")
		
		String url = new Constants().URL_STUDYONLINE_MEDIA_ISCOLLECT+"?type=0&id="+mateId;

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

//		params.addBodyParameter("mateId", mateId);

		HttpUtils http = new HttpUtils();

		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.d(TAG, "是否收藏的  数据结果为========="
								+ responseInfo.result);

						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
//							result = 0表示没有收藏，大于0表示已收藏;
							JSONObject obj = new JSONObject(Data);
							String result = obj.getString("result");
							

//							if (MF_ID.equalsIgnoreCase("null")) {
//								System.out.println("MF_ID为空，可以收藏");
//								optType = "1";
//								textview_data_collect.setText("收藏");
//							} else {
//								System.out.println("MF_ID不为空，可以取消收藏");
//								optType = "0";
//								textview_data_collect.setText("取消收藏");
//							}

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


	private void getdatafromintent() {
		Bundle b = getIntent().getExtras();
		mateId = b.getString("Mate_ID");
		ST_ID = b.getString("ST_ID");

//		System.out.println("MF_ID===" + MF_ID);
		System.out.println("mateId===视频资料ID===" + mateId);
		System.out.println("ST_ID========视频资料====="+ST_ID);
//		if (MF_ID.equalsIgnoreCase("null")) {
//			System.out.println("MF_ID为空，可以收藏");
//			optType = "1";
//			textview_data_collect.setText("收藏");
//		} else {
//			System.out.println("MF_ID不为空，可以取消收藏");
//			optType = "0";
//			textview_data_collect.setText("取消收藏");
//		}

	}

	private void getdatafrovideo() {

		Log.d(TAG, "getdatafromnet()执行");

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_LOOKVIDEOINFO;

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		params.addBodyParameter("mateId", mateId);

		HttpUtils http = new HttpUtils();

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
							
//							JSONArray array = new JSONArray(Data);
							
//							materialsName
							// "Name": "阅读",
							// "sName": null,
							// "ReadCount": 116,
							// "CreateTime": "2015-04-14",
							// "FileType": "FLV"
							if (Result.equalsIgnoreCase("false")) {
								return;
							} else {

								JSONObject obj = new JSONObject(Data);
								String materialsName = obj.getString("materialsName");
								String Name = obj.getString("Name");
								String sName = obj.getString("sName");
								String ReadCount = obj.getString("ReadCount");
								String CreateTime = obj.getString("CreateTime");
								String FileType = obj.getString("FileType");
								
								// 设置 各个字段的属性
								textview_class_video_name.setText("名称 "+materialsName);
								textview_data_createtime.setText(CreateTime);
//								textview_data_type.setText(FileType);
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
						LodDialogClass.closeCustomCircleProgressDialog();
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

		imageview_back.setOnClickListener(this);
		textview_data_collect.setOnClickListener(this);
		btn_is_answer.setOnClickListener(this);
		stuinfo = getSharedPreferences("stuinfo", Context.MODE_PRIVATE);
		surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);

		ImageView btn_play_pause = (ImageView) this.findViewById(R.id.btn_play_pause);
		//btn_play_pause.setOnClickListener(new ClickEvent());
		//btn_alterscreen = (ImageButton) this.findViewById(R.id.btn_alterscreen);
		//btn_alterscreen.setOnClickListener(new ClickEvent());
		
		rl_controller_play = (RelativeLayout) this.findViewById(R.id.rl_controller_play);
		rl_controller_play.setOnClickListener(new ClickEvent());
		rl_alter = (RelativeLayout) this.findViewById(R.id.rl_alter);
		rl_alter.setOnClickListener(new ClickEvent());
		
		skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		player = new Player(this, surfaceView, skbProgress, btn_play_pause,lodclass);
	}
	
	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			if (arg0 == rl_controller_play) {
				System.out.println("url_video alter : " + player.isPlaying());
				if (player.isPlaying()) {
					//btn_play_pause.setBackgroundResource(R.drawable.play);
					player.pause();
				} else {
					//btn_play_pause.setBackgroundResource(R.drawable.pause);
					player.start();
				}
				
			} else if (arg0 == rl_alter) {
				FrameLayout fl_1 = (FrameLayout) findViewById(R.id.fl_1);
				LinearLayout ll_info = (LinearLayout) findViewById(R.id.ll_info);
				ImageView btn_alterscreen = (ImageView) findViewById(R.id.btn_alterscreen);
				RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					ll_info.setVisibility(View.VISIBLE);
					layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_2);
					layoutParams.height = (int) getResources().getDimension(R.dimen.video_height);
					fl_1.setLayoutParams(layoutParams);
					btn_alterscreen.setBackgroundResource(R.drawable.alterscreen);
				} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		            fl_1.setLayoutParams(layoutParams);
		            ll_info.setVisibility(View.GONE);
		            btn_alterscreen.setBackgroundResource(R.drawable.alterscreenin);
				}
				/*FrameLayout fl_1 = (FrameLayout) findViewById(R.id.fl_1);
				LinearLayout ll_info = (LinearLayout) findViewById(R.id.ll_info);
				ImageView btn_alterscreen = (ImageView) findViewById(R.id.btn_alterscreen);
				RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				System.out.println("url_video alter : " + fl_1.getLayoutParams().height);
				if (fl_1.getLayoutParams().height < 0) {
					// to normal
					ll_info.setVisibility(View.VISIBLE);
					layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_2);
					layoutParams.height = (int) getResources().getDimension(R.dimen.video_height);
					fl_1.setLayoutParams(layoutParams);
				} else {
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		            fl_1.setLayoutParams(layoutParams);
		            ll_info.setVisibility(View.GONE);
				}*/
			}

		}
	}

	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
			this.progress = progress * player.mediaPlayer.getDuration()
					/ seekBar.getMax();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
			player.mediaPlayer.seekTo(progress);
		}
	}
	

	private void getdatafromshare() {
		SharedPreferences share = DataPublicclassDetailTestActivity.this
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
											DataPublicclassDetailTestActivity.this,
											"取消收藏成功", 2).show();
									optType = "1";
									textview_data_collect.setText("收藏");
									LodDialogClass.closeCustomCircleProgressDialog();
								} else {
									Toast.makeText(
											DataPublicclassDetailTestActivity.this,
											"收藏成功", Toast.LENGTH_SHORT).show();
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
						System.out.println("onFailure");
						LodDialogClass.closeCustomCircleProgressDialog();
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
			if (null != player) {
				player.finish();
			}
			finish();
			break;
		case R.id.textview_data_collect:// 收藏功能
			if (optType.endsWith("1")) {
				LodDialogClass.showCustomCircleProgressDialog(DataPublicclassDetailTestActivity.this, "", "收藏中...");
			}else{
				LodDialogClass.showCustomCircleProgressDialog(DataPublicclassDetailTestActivity.this, "", "取消收藏中...");
			}
	
			setcollectdata();
			break;
			//答题按钮
		case R.id.btn_is_answer:
			Intent intent = new Intent();
			
			intent.setClass(DataPublicclassDetailTestActivity.this,
					ConnetionStartTestActivity.class);
			Editor ed = stuinfo.edit();
			ed.putInt("testtype", 2);
			ed.putString("domainPFolder", map_info.get("domainPFolder").toString());
			ed.putString("P_ID", map_info.get("P_ID").toString());
			ed.putString("mateId", mateId);
			ed.putString("ST_ID", ST_ID);
//			intent.putExtra("testtype", 2);
//			intent.putExtra("domainPFolder",map_info.get("domainPFolder").toString());
//			intent.putExtra("P_ID", map_info.get("P_ID").toString());
//			intent.putExtra("mateId", mateId);
			ed.commit();
			 startActivity(intent);
			break;
		default:
			break;
		}
	}
	@Override  
	protected void onPause ()  
	{  
	   // webview_public_class_video.reload ();  
	    super.onPause ();  
	}  
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				FrameLayout fl_1 = (FrameLayout) findViewById(R.id.fl_1);
				LinearLayout ll_info = (LinearLayout) findViewById(R.id.ll_info);
				ImageView btn_alterscreen = (ImageView) findViewById(R.id.btn_alterscreen);
				RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				ll_info.setVisibility(View.VISIBLE);
				layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_2);
				layoutParams.height = (int) getResources().getDimension(R.dimen.video_height);
				fl_1.setLayoutParams(layoutParams);
				btn_alterscreen.setBackgroundResource(R.drawable.alterscreen);
				return true;
			}
			if (null != player) {
				player.finish();
			}
			finish();
		}
		return false;
	}
	
}
