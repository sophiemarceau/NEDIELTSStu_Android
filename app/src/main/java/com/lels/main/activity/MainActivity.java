package com.lels.main.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.main.activity.adapter.GalleryAdapter;
import com.lels.student.chatroom.activity.ChatListActivity;
import com.lels.student.chatroom.activity.ChatVisitorActivity;
import com.lels.student.main.fragment.User_interact_classActivity;
import com.lels.student.starttask.activity.StaetTaskActivity;
import com.lels.student.studyonline.StudyOnlineActivity;
import com.lels.student.testpredictions.activity.TestPredictionsActivity;
import com.lels.youke.activity.YkStaetTaskActivity;
import com.lelts.student.myself.MySelfActivity;
import com.lelts.students.bean.UserInfo;
import com.lelts.tool.ImageLoder;
import com.lelts.tool.IntentUtlis;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import io.rong.imkit.RongIM;
import io.rong.imlib.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

public class MainActivity extends Activity implements OnClickListener,
		OnItemSelectedListener {

	private ImageView interact_class, layoutimg;
	@SuppressWarnings("deprecation")
	private Gallery gallery;
	private GalleryAdapter adapter;
	private Context mContext;
	private String iconUrl;
	private String token;
	private String signature;
	private String nickName;
	private String dateDiff;
	private boolean flag = false;

	private LinearLayout stu_linear, yk_linear;
	/*
	 * private int drawables[] = new int[] { R.drawable.kaoqian,
	 * R.drawable.startplay, R.drawable.onlinestart, R.drawable.starttesk,
	 * R.drawable.liaotian };
	 */
	private int drawables[] = { R.drawable.select_gallery_rwdt,
			R.drawable.select_gallery_lt, R.drawable.select_gallery_kqyc,
			R.drawable.select_gallery_zxxx, R.drawable.select_gallery_play };
	// private List<View> viewList;

	/**
	 * 界面属性
	 */

	private RelativeLayout relative_to_myself;

	private TextView homepage_stu_name, homepage_stu_data,
			homepage_stu_finished, homepage_stu_starttotal,
			homepage_stu_starttime;
	private ImageView homepage_user_photo;
	private SharedPreferences stushare, shareToken;
	private String Path = Constants.URL_MYSELFINFO;
	private String Path_userimg = Constants.URL_USERIMG;
	private Editor editor;
	private String a, b;
	private TextView tv_qm;
	private TextView tv_day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		initViewid();
		a = stushare.getString("username", "");
		if (a.equals("游客")) {
			// getDialog(R.layout.login_alertdialog);
			putykcontext();
		} else {
			// login repair
			Intent fromLogin = getIntent();
			String loginReponse = fromLogin.getStringExtra("userInfo");
			final UserInfo user = parseUserJson(loginReponse);
			if (null != user) {
				getHttpuser(user.Token);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						savetosharepreference(user);
					}
				}, 100);
				ImageLoder app = (ImageLoder) getApplication();
				app.checkNewestUserInfo(user.Token);
				app.traceUser(user.Token);
			}
		}
		ExitApplication.getInstance().addActivitylistExit(this);
	}

	/*
	 * private void blur(Bitmap bkg, View view) {
	 * 
	 * RenderScript rs = RenderScript.create(MainActivity.this); Allocation
	 * overlayAlloc = Allocation.createFromBitmap(rs, bkg); ScriptIntrinsicBlur
	 * blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
	 * blur.setInput(overlayAlloc); blur.setRadius(radius);
	 * blur.forEach(overlayAlloc); overlayAlloc.copyTo(bkg);
	 * view.setBackground(new BitmapDrawable(getResources(), bkg));
	 * rs.destroy(); }
	 */
	private UserInfo parseUserJson(String str) {
		UserInfo user = null;
		try {
			JSONObject obj = new JSONObject(str);
			String data = obj.getString("Data");

			JSONObject obj_data = new JSONObject(data);
			String userInfo = obj_data.getString("UserInfo");
			JSONObject obj_info = new JSONObject(userInfo);

			user = new UserInfo();
			user.Email = obj_info.getString("Email");
			user.UID = obj_info.getString("UID");
			user.NickName = obj_info.getString("NickName");
			user.Password = obj_info.getString("Password");
			user.Account = obj_info.getString("Account");
			user.RoleID = obj_info.getString("RoleID");
			user.IconUrl = obj_info.getString("IconUrl");
			user.Signaure = obj_info.getString("Signature");
			user.chatToken = obj_info.getString("chatToken");
			user.Token = obj_data.getString("Token");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if (a.equals("游客")) {
			// getDialog(R.layout.login_alertdialog);
			putykcontext();
		} else {
			share = getSharedPreferences("userinfo", MODE_PRIVATE);
			token = share.getString("Token", "");
			System.out.println("执行onrestart 方法========");
			getdatafromnet();
			getHttpuser(token);
		}
	}

	/**
	 * 获取学生的个人信息
	 * 
	 */
	private void getdatafromnet() {

		String url = new Constants().URL_MYSELFINFO;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							System.out.println("解析获取学生信息===="
									+ responseInfo.result);
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
							// IconUrl = obj.getString("IconUrl");
							nickName = obj.getString("NickName");
							// UID = obj.getString("UID");
							signature = obj.getString("Signature");
							dateDiff = obj.getString("DateDiff");
							// FinishTaskCount =
							// obj.getString("FinishTaskCount");
							// DownMaterialsCount = obj
							// .getString("DownMaterialsCount");
							// StudyTimeCount = obj.getString("StudyTimeCount");

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

	/**
	 * 设置信息
	 * 
	 */
	private void setviewdata() {
		homepage_stu_name.setText(nickName);
		int diff = Integer.valueOf(dateDiff);
		if (diff >= 0) {
			System.out.println("diff考试时间====" + diff);
			tv_qm.setText("距离雅思考试还有");
			homepage_stu_data.setVisibility(View.VISIBLE);
			tv_day.setVisibility(View.VISIBLE);
			homepage_stu_data.setTextSize(22);
			homepage_stu_data.setText(dateDiff);

		} else if (signature.equalsIgnoreCase("null")
				|| signature.equalsIgnoreCase("")) {
			tv_qm.setText("这家伙很懒，还没有个人签名");
			homepage_stu_data.setVisibility(View.GONE);
			tv_day.setVisibility(View.GONE);

		} else {

			tv_qm.setText(signature);
			tv_qm.setMaxEms(15);
			tv_qm.setSingleLine(true);
			tv_qm.setEllipsize(TruncateAt.END);
			homepage_stu_data.setVisibility(View.GONE);
			tv_day.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void savetosharepreference(UserInfo user) {

		share = getSharedPreferences("userinfo", MODE_PRIVATE);
		SharedPreferences.Editor editor = share.edit();

		editor.putString("Email", user.Email);
		editor.putString("UID", user.UID);
		editor.putString("NickName", user.NickName);
		editor.putString("Password", user.Password);
		editor.putString("Account", user.Account);
		editor.putString("RoleID", user.RoleID);
		editor.putString("IconUrl", user.IconUrl);
		editor.putString("Signaure", user.Signaure);
		editor.putString("Token", user.Token);
		editor.putString("chatToken", user.chatToken);
		editor.commit();

	}

	/**
	 * 网络请求07-15 07:33:17.233: I/System.out(19641):
	 * >>>>>>>>Mzh8eGRmMDA1MDA1MDIwNnwxNDM2OTQ1NDM3NTA1-----------
	 */

	private void getHttpuser(String token) {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		System.out.println(">>>>>>>>" + token + "-----------");
		HttpUtils utils = new HttpUtils();
		final BitmapUtils bitutil = new BitmapUtils(mContext);
		utils.configCurrentHttpCacheExpiry(0);
		utils.configHttpCacheSize(0);
		utils.send(HttpMethod.POST, Path, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@SuppressWarnings("deprecation")
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String result = arg0.result;
						System.out.println(">>>>>>" + result + "-----");
						/*
						 * TextView homepage_stu_name, homepage_stu_data,
						 * homepage_stu_finished, homepage_stu_starttotal,
						 * homepage_stu_starttime;
						 */
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject obj1 = obj.getJSONObject("Data");
							String isVistor = obj1.getString("isVistor");

							editor.putString("isVistor", isVistor);

							nickName = obj1.getString("NickName");
							System.out.println("nickname=" + nickName);
							homepage_stu_name.setText(nickName);

							dateDiff = obj1.getString("DateDiff");
							int diff = Integer.valueOf(dateDiff);

							signature = obj1.getString("Signature");

							if (diff >= 0) {
								System.out.println("diff考试时间====" + diff);
								tv_qm.setText("距离雅思考试还有");
								homepage_stu_data.setVisibility(View.VISIBLE);
								tv_day.setVisibility(View.VISIBLE);
								homepage_stu_data.setTextSize(22);
								homepage_stu_data.setText(dateDiff);

							} else if (signature.equalsIgnoreCase("null")
									|| signature.equalsIgnoreCase("")) {
								tv_qm.setText("这家伙很懒，还没有个人签名");
								homepage_stu_data.setVisibility(View.GONE);
								tv_day.setVisibility(View.GONE);

							} else {

								tv_qm.setText(signature);
								tv_qm.setMaxEms(15);
								tv_qm.setSingleLine(true);
								tv_qm.setEllipsize(TruncateAt.END);
								homepage_stu_data.setVisibility(View.GONE);
								tv_day.setVisibility(View.GONE);
							}

							String UID = obj1.getString("UID");// 用户ID;

							String DownMaterialsCount = obj1
									.getString("DownMaterialsCount");// 预览资料总数;

							homepage_stu_starttotal.setText(DownMaterialsCount);

							String StudyTimeCount = obj1
									.getString("StudyTimeCount");// 今日学习时间;
							homepage_stu_starttime.setText(StudyTimeCount);
							iconUrl = obj1.getString("IconUrl");// 用户图像;
							if (iconUrl == null || iconUrl.equals("")) {

							} else {
								bitutil.display(layoutimg, Path_userimg
										+ iconUrl);
								layoutimg.setAlpha(0.6f);
								ImageLoader.getInstance()// 头像加载
										.displayImage(Path_userimg + iconUrl,
												homepage_user_photo);
							}
							// ImageView imgv = new ImageView(mContext);
							String FinishTaskCount = obj1
									.getString("FinishTaskCount");// 今日完成任务总数;
							homepage_stu_finished.setText(FinishTaskCount);

							editor.putInt("FinishTaskCount",
									Integer.valueOf(FinishTaskCount));
							editor.putInt("DownMaterialsCount",
									Integer.valueOf(DownMaterialsCount));
							editor.putInt("DateDiff", diff);
							editor.putString("NickName", nickName);
							editor.putString("iconUrl", iconUrl);
							editor.putString("signature", signature);
							editor.commit();
							System.out.println(stushare.getInt(
									"DownMaterialsCount", 0));
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
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
		// alertDialog.setView(myLoginView, 0, 0, 0, 30);
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

	private void putykcontext() {
		homepage_stu_name.setText("用户昵称");
		tv_qm.setText("这家伙很懒，还没有个人签名");
		homepage_stu_data.setVisibility(View.GONE);
		tv_day.setVisibility(View.GONE);
		yk_linear.setVisibility(View.VISIBLE);
		stu_linear.setVisibility(View.GONE);
	}

	private String getintent() {
		Intent mIntent = getIntent();
		String a = (String) mIntent.getCharSequenceExtra("youke");
		return a;
	}

	/*
	 * 初始化组件
	 */
	@SuppressWarnings("deprecation")
	private void initViewid() {
		/*
		 * TextView homepage_stu_name, homepage_stu_data, homepage_stu_finished,
		 * homepage_stu_starttotal, homepage_stu_starttime;
		 */
		relative_to_myself = (RelativeLayout) findViewById(R.id.relative_to_myself);
		stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		shareToken = getSharedPreferences("userChatToken", Context.MODE_PRIVATE);
		editor = stushare.edit();
		SharedPreferences share = this.getSharedPreferences("userinfo",
				MODE_PRIVATE);

		tv_qm = (TextView) findViewById(R.id.textView2);
		tv_day = (TextView) findViewById(R.id.textView4);

		homepage_stu_name = (TextView) findViewById(R.id.homepage_stu_name);// 名字
		homepage_stu_data = (TextView) findViewById(R.id.homepage_stu_data);// 剩余时间
		homepage_stu_finished = (TextView) findViewById(R.id.homepage_stu_finished);// 完成量
		homepage_stu_starttotal = (TextView) findViewById(R.id.homepage_stu_starttotal);// 学习总数
		homepage_stu_starttime = (TextView) findViewById(R.id.homepage_stu_starttime);// 学习时间
		homepage_user_photo = (ImageView) findViewById(R.id.homepage_user_photo);// 学生头像
		layoutimg = (ImageView) findViewById(R.id.homepage_stu_all);
		homepage_user_photo.setOnClickListener(this);
		relative_to_myself.setOnClickListener(this);

		gallery = (Gallery) findViewById(R.id.gallery);

		// 方法1 Android获得屏幕的宽和高
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();

		stu_linear = (LinearLayout) findViewById(R.id.stu_linear);
		yk_linear = (LinearLayout) findViewById(R.id.yk_linear);
		yk_linear.setOnClickListener(this);

		interact_class = (ImageView) findViewById(R.id.home_interact_img);
		interact_class.setOnClickListener(this);
		adapter = new GalleryAdapter(mContext, drawables, screenWidth,
				screenHeight);
		gallery.setAdapter(adapter);
		gallery.setSpacing(80);
		gallery.setSelection(adapter.getCount() / 2);
		gallery.setOnItemSelectedListener(this);
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				switch (position % drawables.length) {
				case 2:// 考前预测
					if (a.equals("游客")) {
						getDialog(R.layout.login_alertdialog);
					} else {
						IntentUtlis.sysStartActivity(mContext,
								TestPredictionsActivity.class);
					}
					break;
				case 4:// 学习计划
					IntentUtlis.sysStartActivity(mContext,
							StudyPlanActivity.class);
					break;
				case 0:// 任务动态
					b = stushare.getString("isVistor", "");
					if (a.equals("游客") || b.equals("1")) {
						IntentUtlis.sysStartActivity(mContext,
								YkStaetTaskActivity.class);
					} else {
						IntentUtlis.sysStartActivity(mContext,
								StaetTaskActivity.class);
					}
					// IntentUtlis.sysStartActivity(mContext,
					// StudyOnlineActivity.class);
					break;
				case 3:// 在线学习
					IntentUtlis.sysStartActivity(mContext,
							StudyOnlineActivity.class);

					break;
				case 1:
					// getLtdialog();// 聊天
					System.out.println("1------1-------1");
					b = stushare.getString("isVistor", "");
					if (a.equals("游客") || b.equals("1")) {
						Intent intent = new Intent(mContext,
								ChatVisitorActivity.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent(mContext,
								ChatListActivity.class);
						startActivity(intent);
					}
					break;
				}
			}
		});
	}

	/*
	 * 事件监听
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.home_interact_img:
			if (a.equals("游客")) {
				getDialog(R.layout.login_alertdialog);
			} else {
				b = stushare.getString("isVistor", "");
				if (b.equals("1")) {
					getDialog(R.layout.yk_alertdialog);
				} else {
					Intent intent = new Intent();
					intent.putExtra("stu_imgurl", iconUrl);
					intent.setClass(mContext, User_interact_classActivity.class);
					startActivity(intent);
				}
			}
			break;
		case R.id.homepage_user_photo:
			IntentUtlis.sysStartActivity(mContext, MySelfActivity.class);
			break;
		case R.id.dialog_btn:
			if (a.equals("游客")) {
				IntentUtlis.sysStartActivity(mContext, RegisterActivity.class);
			} else {
				IntentUtlis.sysStartActivity(mContext,
						MyRegisterBindStudentNumActivity.class);
			}
			break;
		case R.id.yk_linear:
			IntentUtlis.sysStartActivity(mContext, RegisterActivity.class);
		default:
			break;
		}
	}

	private boolean isOut;
	private SharedPreferences share;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isOut) {
				return super.onKeyDown(keyCode, event);
			} else {
				isOut = true;
				Toast.makeText(mContext, "再按一次返回键退出", Toast.LENGTH_LONG).show();
				//
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						isOut = false;
					}
				};
				timer.schedule(task, 3000); // 调试设置为5s
				return true;

			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		adapter.setSelectItem(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}
