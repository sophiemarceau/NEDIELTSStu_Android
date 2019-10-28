package com.lelts.student.myself;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.strudentlelts.R;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.main.activity.MyRegisterBindStudentNumActivity;
import com.lels.main.activity.RegisterActivity;
import com.lelts.tool.IntentUtlis;
import com.lelts.tool.PrintTool;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MySelfActivity extends Activity implements OnClickListener {

	private static final String TAG = "MySelfActivity";

	private ImageView imageview_back;

	private RelativeLayout relative_mytarget;
	private RelativeLayout relative_mycollect;

	private RelativeLayout relative_mymessage;
	private RelativeLayout relative_mynum;

	private RelativeLayout relative_mykaoshitest;
	private RelativeLayout relative_myaboutus;

	private String token;

	private PrintTool print;

	// IconUrl = 用户图像;
	//   NickName = 昵称;
	//   UID = 用户ID;
	//   Signature = 个性签名;
	//   DateDiff = 雅思考试时间与当日时间差;
	//   FinishTaskCount = 今日完成任务总数;
	//   DownMaterialsCount = 预览资料总数;
	//   StudyTimeCount = 今日学习时间;

	private String IconUrl;
	private String NickName;
	private String UID;
	private String Signature;
	private String DateDiff;
	private String FinishTaskCount;
	private String DownMaterialsCount;
	private String StudyTimeCount;

	private ImageView userphoto;// 用户头像
	private TextView textview_user_name;// 用户昵称

	private LinearLayout relative_isyasitest;// 是否有雅思考试
	private TextView textview_test_yasi_time;// 雅思考试时间

	private LinearLayout relative_ispersonsign;// 是否显示个性签名
	private TextView textview_personalsignature;// 个性签名
	private ImageView img_back;
	private ImageView imageview_myself_background;

	private BitmapUtils bitmaputils;
	private SharedPreferences stushare;

	private String isVistor;

	private AlertDialog alertDialog;

	private String tagusername;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself);

		print = new PrintTool(MySelfActivity.this);
		bitmaputils = new BitmapUtils(MySelfActivity.this);

		getdatafromshare();

		initview();
		if (tagusername.equals("游客")) {
			putykcontext();
		} else {
			LodDialogClass.showCustomCircleProgressDialog(MySelfActivity.this,
					null, getString(R.string.common_Loading));
			getdatafromnet();
		}
		ExitApplication.getInstance().addActivitylistExit(this);
	}

	private void getdatafromshare() {
		stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		tagusername = stushare.getString("username", "");
		isVistor = stushare.getString("isVistor", "");
		SharedPreferences share = this.getSharedPreferences("userinfo",
				MODE_PRIVATE);
		token = share.getString("Token", "");
		print.printforLog(TAG, token);
	}

	private void initview() {

		imageview_back = (ImageView) findViewById(R.id.imageview_back);

		userphoto = (ImageView) findViewById(R.id.userphoto);
		textview_user_name = (TextView) findViewById(R.id.textview_user_name);
		textview_personalsignature = (TextView) findViewById(R.id.textview_personalsignature);// 个性签名

		relative_isyasitest = (LinearLayout) findViewById(R.id.relative_isyasitest);// 是否有雅思考试
		textview_test_yasi_time = (TextView) findViewById(R.id.textview_test_yasi_time);// 考试时间

		relative_ispersonsign = (LinearLayout) findViewById(R.id.relative_ispersonsign);

		relative_mytarget = (RelativeLayout) findViewById(R.id.relative_mytarget);
		relative_mycollect = (RelativeLayout) findViewById(R.id.relative_mycollect);

		relative_mymessage = (RelativeLayout) findViewById(R.id.relative_mymessage);
		relative_mynum = (RelativeLayout) findViewById(R.id.relative_mynum);

		relative_mykaoshitest = (RelativeLayout) findViewById(R.id.relative_mykaoshitest);
		relative_myaboutus = (RelativeLayout) findViewById(R.id.relative_myaboutus);

		img_back = (ImageView) findViewById(R.id.imageview_back);
		// 设置背景
		imageview_myself_background = (ImageView) findViewById(R.id.imageview_myself_background);

		imageview_back.setOnClickListener(this);
		relative_mytarget.setOnClickListener(this);
		relative_mycollect.setOnClickListener(this);

		relative_mymessage.setOnClickListener(this);
		relative_mynum.setOnClickListener(this);

		relative_mykaoshitest.setOnClickListener(this);
		relative_myaboutus.setOnClickListener(this);

		img_back.setOnClickListener(this);
		
	}

	private void putykcontext() {

		textview_user_name.setText("用户昵称");
		textview_personalsignature.setText("这家伙很懒，还没有个人签名");
	}

	private void setviewdata() {
		textview_user_name.setText(NickName);

		print.printforLog(TAG, "解析的数据DateDiff为===============" + DateDiff);
		int integer_datediff = Integer.valueOf(DateDiff);
		if (integer_datediff >= 0) {

			relative_isyasitest.setVisibility(View.VISIBLE);
			relative_ispersonsign.setVisibility(View.GONE);
			textview_test_yasi_time.setText(DateDiff);

		} else if (Signature.equalsIgnoreCase("null")
				|| Signature.equalsIgnoreCase("")) {

			relative_isyasitest.setVisibility(View.GONE);
			relative_ispersonsign.setVisibility(View.VISIBLE);
			textview_personalsignature.setText("这个家伙很懒，还没有个性签名");

		} else {

			relative_isyasitest.setVisibility(View.GONE);
			relative_ispersonsign.setVisibility(View.VISIBLE);
			textview_personalsignature.setText(Signature);
			textview_personalsignature.setMaxEms(15);
			textview_personalsignature.setSingleLine(true);
			textview_personalsignature.setEllipsize(TruncateAt.END);
		}

		print.printforLog(TAG, "开始家在网络图片");
		// //家在网络图片
		// bitmaputils.display(userphoto, IconUrl);
		try {

			if (IconUrl == null || IconUrl.equals("")) {
			} else {
				IconUrl = new Constants().URL_USERIMG + IconUrl;
				ImageLoader.getInstance().displayImage(IconUrl, userphoto);

				bitmaputils.display(imageview_myself_background, IconUrl);

				imageview_myself_background.setAlpha(0.6f);

			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (tagusername.equals("游客")) {
			putykcontext();
		} else {

			getdatafromnet();
		}
	}

	private void getdatafromnet() {
		Log.d(TAG, "解析获取我的收藏列表" + "getdatafromnet()");

		String url = new Constants().URL_MYSELFINFO;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

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
						try {
							Log.d(TAG, "解析获取我的收藏列表" + responseInfo.result);
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							JSONObject obj = new JSONObject(Data);
							// IconUrl = 用户图像;
							//   NickName = 昵称;
							//   UID = 用户ID;
							//   Signature = 个性签名;
							//   DateDiff = 雅思考试时间与当日时间差;
							//   FinishTaskCount = 今日完成任务总数;
							//   DownMaterialsCount = 预览资料总数;
							//   StudyTimeCount = 今日学习时间;
							IconUrl = obj.getString("IconUrl");
							NickName = obj.getString("NickName");
							UID = obj.getString("UID");
							Signature = obj.getString("Signature");
							DateDiff = obj.getString("DateDiff");
							FinishTaskCount = obj.getString("FinishTaskCount");
							DownMaterialsCount = obj
									.getString("DownMaterialsCount");
							StudyTimeCount = obj.getString("StudyTimeCount");

							setviewdata();
							LodDialogClass.closeCustomCircleProgressDialog();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.relative_mytarget:// 目标
			if (tagusername.equals("游客")) {
				getDialog(R.layout.login_alertdialog);
			} else {
				IntentUtlis.sysStartActivity(MySelfActivity.this,
						MystudentTargetActivity.class);
			}
			break;
		case R.id.relative_mycollect:// 收藏
			if (tagusername.equals("游客")) {
				getDialog(R.layout.login_alertdialog);
			} else {
				IntentUtlis.sysStartActivity(MySelfActivity.this,
						MystudentCollectActivity.class);
			}
			break;
		case R.id.relative_mymessage:// 消息
			if (tagusername.equals("游客")) {
				getDialog(R.layout.login_alertdialog);
			} else {

				IntentUtlis.sysStartActivity(MySelfActivity.this,
						MystudentMessageTestActivity.class);
			}

			break;
		case R.id.relative_mynum:// 账号管理
			if (tagusername.equals("游客")) {
				getDialog(R.layout.login_alertdialog);
			} else {
				IntentUtlis.sysStartActivity(MySelfActivity.this,
						MystudentAccountManagermentActivity.class);
			}
			break;
		case R.id.relative_mykaoshitest:// 考试测试
			if (tagusername.equals("游客")) {
				getDialog(R.layout.login_alertdialog);
			} else {

				if (isVistor.equals("1")) {
					getDialog(R.layout.yk_alertdialog);
				} else {
					IntentUtlis.sysStartActivity(MySelfActivity.this,
							MystudentTestReportActivity.class);
				}
			}

			break;
		case R.id.relative_myaboutus:// 关于我们
			IntentUtlis.sysStartActivity(MySelfActivity.this,
					MystudentAboutUsActivity.class);
			break;
		// case R.id.imageview_back:
		// IntentUtlis.sysStartActivity(MySelfActivity.this,
		// MainActivity.class);
		// finish();
		// break;
		case R.id.imageView_x:
			alertDialog.dismiss();
			break;
		case R.id.dialog_btn:
			if (tagusername.equals("游客")) {
				IntentUtlis.sysStartActivity(MySelfActivity.this,
						RegisterActivity.class);
			} else {
				IntentUtlis.sysStartActivity(MySelfActivity.this,
						MyRegisterBindStudentNumActivity.class);
			}
			break;
		default:
			break;
		}
	}
	/**
	 * 创建游客dialog
	 */
	private void getDialog(int resulse) {
		/*
		 * String a = getintent(); if(a==null){
		 * 
		 * }else{ if (a.equals("游客")) {
		 */
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View myLoginView = layoutInflater.inflate(resulse, null);
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setView(myLoginView, 30, 0, 30, 30);
		alertDialog.setView(myLoginView, 0, 0, 0, 30);
		alertDialog.show();
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setCancelable(false);
		ImageView image = (ImageView) myLoginView
				.findViewById(R.id.imageView_x);
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		Button btn = (Button) myLoginView.findViewById(R.id.dialog_btn);
		btn.setOnClickListener(this);
	}

}
