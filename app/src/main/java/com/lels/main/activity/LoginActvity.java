package com.lels.main.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.costum.EditDelectText;
import com.example.strudentlelts.R;
import com.lels.bean.BooleanWife;
import com.lels.bean.ButtonControl;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lelts.tool.CodeUtil;
import com.lelts.tool.ImageLoder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

public class LoginActvity extends Activity implements OnClickListener {
	private Button mButton_login;
	private TextView textview_mLogin_zhuce, textview_mLogin_forget_password;

	// private Button relative_youke;
	private RelativeLayout relative_youke;
	private Context mContext;
	private Intent intent;
	private String username, userPass;
	private EditDelectText editText_username;
	private EditDelectText editText_password;
	private String device_token;
	private SharedPreferences stushare;
	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;
		ImageLoder loder = (ImageLoder) this.getApplication();
		loder.setCurIndex(5);
		initView();

		ExitApplication.getInstance().addActivitylistExit(this);
		checkAutoLogin();
	}

	private void initView() {
		mButton_login = (Button) findViewById(R.id.button_login);
		relative_youke = (RelativeLayout) findViewById(R.id.relative_youke);

		textview_mLogin_zhuce = (TextView) findViewById(R.id.textview_mLogin_zhuce);
		textview_mLogin_forget_password = (TextView) findViewById(R.id.textview_login_forget);

		editText_username = (EditDelectText) findViewById(R.id.editText_username);
		editText_password = (EditDelectText) findViewById(R.id.editText_password);

		stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		editor = stushare.edit();
		// 实现推送
		PushAgent mPushAgent = PushAgent.getInstance(mContext);
		mPushAgent.enable();

		if (mPushAgent.isEnabled()) {
			// Toast.makeText(context, "11111", 1).show();
		} else {
			// Toast.makeText(context, "55555", 1).show();
		}
		device_token = UmengRegistrar.getRegistrationId(mContext);
		System.out.println(device_token + "-------------ppppppp");
		
		 mButton_login.setOnClickListener(this);
		relative_youke.setOnClickListener(this);
		textview_mLogin_zhuce.setOnClickListener(this);
		textview_mLogin_forget_password.setOnClickListener(this);
	}
	
	private void checkAutoLogin() {
		System.out.println("登录local check: has user : " + stushare.contains("username") + ", has pass : " + stushare.contains("userPass"));
		if (stushare.contains("username")) {
			String user = stushare.getString("username", "");
			if (!user.equals("游客")) {
				System.out.println("登录local: username : " + user);
				editText_username.setText(user);
			}
			if (stushare.contains("userPass")) {
				String pass = stushare.getString("userPass", null);
				System.out.println("登录local: pass : " + pass);
				if (!"".equals(pass)) {
					LodDialogClass.showCustomCircleProgressDialog(
							LoginActvity.this, null, "登录中...");
					System.out.println("登录自动 : user : " + user + ", pass : " + pass);
					editText_password.setText(CodeUtil.Decode(pass));
					login(user, pass);
				}
			} else {
				editText_password.getEditableText().clear();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_login:
			if (BooleanWife.isNetworkAvailable(mContext)) {
				if (ButtonControl.isFastClick()) {
					return;
				} else {
					username = editText_username.getText().toString();
					userPass = editText_password.getText().toString();
					LodDialogClass.showCustomCircleProgressDialog(
							LoginActvity.this, null, "登录中...");
					String encodePass = CodeUtil.Encode(userPass);
					System.out.println("登录加密: pass : " + userPass + ", encode : " + encodePass);
					
					login(username, encodePass);
				}
			} else {
				Toast.makeText(mContext, "请连接网络", 0).show();
			}
			break;
		case R.id.relative_youke:

			editor.putString("username", "游客");
			editor.commit();
			intent = new Intent();
			intent.setClass(mContext, MainActivity.class);
			startActivity(intent);

			break;
		case R.id.textview_mLogin_zhuce:
			intent = new Intent();
			intent.putExtra("youke", "youke");
			intent.setClass(mContext, RegisterActivity.class);
			startActivity(intent);
			// mIntentUtils.sysStartActivity(mContext, RegisterActivity.class);
			break;
		case R.id.textview_login_forget:
			intent = new Intent();
			intent.putExtra("youke", "youke");
			intent.setClass(mContext, ForgetPswActivity.class);
			startActivity(intent);
			// mIntentUtils.sysStartActivity(mContext, RegisterActivity.class);
			break;

		default:
			break;
		}
	}

	public void login(final String name, final String pass) {

		String url = new Constants().URL_STUDENT_LOGIN;
		
	
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("u", name);
		params.addQueryStringParameter("p", pass);

		params.addQueryStringParameter("DeviceToken", device_token);
		params.addQueryStringParameter("DeviceTokenType", "Android");

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.configSoTimeout(1000*10);
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.d("LoginActvity", "登录返回的信息为==="
								+ responseInfo.result);
						try {
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							if (Result.equalsIgnoreCase("false")) {
								LodDialogClass.closeCustomCircleProgressDialog();
								Toast.makeText(LoginActvity.this, Infomation, 2)
										.show();
							} else {
								/*
								 * savetosharepreference(responseInfo.result);
								 * 
								 * // new 11.4 SharedPreferences sp_user =
								 * LoginActvity
								 * .this.getSharedPreferences("userinfo",
								 * MODE_PRIVATE); ImageLoder app = (ImageLoder)
								 * LoginActvity.this.getApplication();
								 * app.checkNewestUserInfo
								 * (sp_user.getString("Token", "")); // new 11.4
								 */

								editor.putString("username", name);
								editor.putString("userPass", pass);
								editor.commit();
								Intent intent = new Intent();
								intent.setClass(LoginActvity.this,
										MainActivity.class);
								Bundle b = new Bundle();
								intent.putExtra("youke", "youke");
								intent.putExtra("userInfo", responseInfo.result);
								intent.putExtras(b);
								startActivity(intent);
								LodDialogClass
										.closeCustomCircleProgressDialog();
								finish();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
						LodDialogClass.closeCustomCircleProgressDialog();
						if (error.getExceptionCode()==0) {
							Toast.makeText(mContext, "连接超时", 0).show();
						}
						// Intent intent = new Intent();
						// intent.setClass(LoginActvity.this,
						// MainActivity.class);
						// Bundle b = new Bundle();
						// intent.putExtra("youke", "youke");
						// intent.putExtras(b);
						// startActivity(intent);
						// finish();
					}

				});

	}

	/*
	 * private void savetosharepreference(String str) {
	 * 
	 * try { JSONObject obj = new JSONObject(str); // Result: true, //
	 * Infomation: , // Data: { String result = obj.getString("Result"); String
	 * infomation = obj.getString("Infomation"); String data =
	 * obj.getString("Data");
	 * 
	 * JSONObject obj_data = new JSONObject(data); String userInfo =
	 * obj_data.getString("UserInfo"); JSONObject obj_info = new
	 * JSONObject(userInfo);
	 * 
	 * // { // "UserInfo": { // "teacherCode": "", // "Email":
	 * "ielts_2_1@qq.com", // "NickName": "hongjing1", // "UID": 38, //
	 * "Password": "", // "Account": "xdf0050050206", // "RoleID": 3, //
	 * "IconUrl": "1435911946840445.JPG", // "Signature": null // }, // "Token":
	 * "Mzh8eGRmMDA1MDA1MDIwNnwxNDM2MTg1Mjk4MDkx" // }
	 * 
	 * String Email = obj_info.getString("Email"); String UID =
	 * obj_info.getString("UID"); String NickName =
	 * obj_info.getString("NickName"); String Password =
	 * obj_info.getString("Password"); String Account =
	 * obj_info.getString("Account"); String RoleID =
	 * obj_info.getString("RoleID"); String IconUrl =
	 * obj_info.getString("IconUrl"); String Signaure =
	 * obj_info.getString("Signature"); // 聊天室的token String chatToken =
	 * obj_info.getString("chatToken");
	 * 
	 * String Token = obj_data.getString("Token"); // getChatDataFromNet(Token);
	 * SharedPreferences share =
	 * LoginActvity.this.getSharedPreferences("userinfo", MODE_PRIVATE);
	 * SharedPreferences.Editor editor = share.edit();
	 * 
	 * editor.putString("Email", Email); editor.putString("UID", UID);
	 * editor.putString("NickName", NickName); editor.putString("Password",
	 * Password); editor.putString("Account", Account);
	 * editor.putString("RoleID", RoleID); editor.putString("IconUrl", IconUrl);
	 * editor.putString("Signaure", Signaure); editor.putString("Token", Token);
	 * // 聊天室的token editor.putString("chatToken", chatToken);
	 * 
	 * editor.commit();
	 * 
	 * // String test = share.getString("Token", ""); // Log.d("LoginActivity",
	 * "Token ======"+test);
	 * 
	 * } catch (JSONException e) { e.printStackTrace(); }
	 * 
	 * }
	 */

	// 网络解析聊天token数据
	// public void getChatDataFromNet(String token) {
	// String url = Constants.URL_ActiveClass_getChatToken;
	// RequestParams params = new RequestParams();
	// params.addHeader("Authentication", token);
	// params.addBodyParameter("checkUpdate", "true");
	// HttpUtils utils = new HttpUtils();
	// utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
	//
	// @Override
	// public void onFailure(HttpException arg0, String arg1) {
	// // TODO Auto-generated method stub
	// System.out.println("网络解析聊天token数据    ===  onFailure");
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> arg0) {
	// // TODO Auto-generated method stub
	// System.out.println("网络解析聊天token数据    ===  onSuccess");
	// String result = arg0.result;
	// System.out.println(" login的数据333444222wushuai  " + result);
	// try {
	// JSONObject str = new JSONObject(result);
	// // JSONObject Infomation = str.getJSONObject("Infomation");
	// // JSONObject Result = str.getJSONObject("Result");
	// // JSONObject data = str.getJSONObject("Data");
	// String Result = str.getString("Result");
	// String Infomation = str.getString("Infomation");
	// String data = str.getString("Data");
	// JSONObject obj_data = new JSONObject(data);
	// String chatToken = obj_data.getString("chatToken");
	// System.out.println("login的聊天数据 ？？？=" + chatToken);
	// SharedPreferences share =
	// LoginActvity.this.getSharedPreferences("userChatToken", MODE_PRIVATE);
	// Editor editor = share.edit();
	// editor.putString("chatToken", chatToken);
	// editor.commit();
	// // new 11.4
	// ImageLoder app = (ImageLoder) LoginActvity.this.getApplication();
	// app.connect(chatToken);
	// // new 11.4
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// });
	// }
	
	private boolean isOut;


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

}
