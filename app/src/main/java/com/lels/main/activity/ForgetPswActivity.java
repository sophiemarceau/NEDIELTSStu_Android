package com.lels.main.activity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.constants.AppConfig;
import com.lelts.tool.IsPhoneorEmail;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgetPswActivity extends Activity implements OnClickListener {

	private ImageButton imageview_back;
	private EditText edittext_username;
	private Button button_psw_next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpsw);

		init();

	}

	private void init() {
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		edittext_username = (EditText) findViewById(R.id.edittext_username);
		button_psw_next = (Button) findViewById(R.id.button_psw_next);

		imageview_back.setOnClickListener(this);
		button_psw_next.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.button_psw_next:
			
			String account = edittext_username.getText().toString();
				checkemail(account);
			
//			Intent intent = new Intent();
//			intent.setClass(ForgetPswActivity.this,
//					ForgetPswtheSecondActivity.class);
//			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 校验邮箱，绑定邮箱 iphone、Android 学生App
	 * */

	/**
	 * 校验邮箱，绑定邮箱 iphone、Android 学生App
	 * */
	private void checkemail(final String str_test) {

		System.out.println("输入的数据为==" + str_test);

		String appId = AppConfig.U2AppIds; // 得到本应用appId
		String apiUrl = AppConfig.U2RootUrls + "/apis/usersv2.ashx";

		RequestParams params = new RequestParams();

		String method = "ResetPwdStep1SendCode"; // 方法名称，固定值
		String user = str_test;

		UUID uuid = UUID.randomUUID();
		Log.d("uuid========", uuid.toString());
		String guid = uuid.toString();
		String signText = (method + appId + guid + user + AppConfig.U2AppKeys)
				.toLowerCase();

		String sign = md5(signText).toUpperCase(); // 签名

		params.addBodyParameter("method", method);
		params.addBodyParameter("appid", appId);
		params.addBodyParameter("guid", guid);
		params.addBodyParameter("user", user);
		params.addBodyParameter("sign", sign);

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, apiUrl, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.d("ForgetPswActivity", "注册返回的信息为==="
								+ responseInfo.result);
						try {
							JSONObject str = new JSONObject(responseInfo.result);
							String Status = str.getString("Status");
							String Message = str.getString("Message");
							String Data = str.getString("Data");

							if (!Status.equalsIgnoreCase("1")) {

								Toast.makeText(ForgetPswActivity.this, Message,
										2).show();
							} else {

								Intent intent = new Intent();
								intent.setClass(ForgetPswActivity.this,
										ForgetPswSecondStepActivity.class);
								Bundle b = new Bundle();
								b.putString("registtype", str_test);
								intent.putExtras(b);
								startActivity(intent);
								finish();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure" + error.toString());
						System.out.println(msg.toString());

					}

				});

	}

	/**
	 * MD5 add secret
	 * **/
	public static String md5(String string) {

		byte[] hash;

		try {

			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));

		} catch (NoSuchAlgorithmException e) {

			throw new RuntimeException("Huh, MD5 should be supported?", e);

		} catch (UnsupportedEncodingException e) {

			throw new RuntimeException("Huh, UTF-8 should be supported?", e);

		}

		StringBuilder hex = new StringBuilder(hash.length * 2);

		for (byte b : hash) {

			if ((b & 0xFF) < 0x10)
				hex.append("0");

			hex.append(Integer.toHexString(b & 0xFF));

		}

		return hex.toString();

	}

}
