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

public class ForgetPswSecondStepActivity extends Activity implements
		OnClickListener {

	private ImageButton imageview_back;
	private EditText edittext_code;
	private Button button_psw_next;

	private String registtype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpsw_secondstep);

		getdatafromintent();

		init();

	}

	private void getdatafromintent() {
		Bundle b = getIntent().getExtras();
		registtype = b.getString("registtype");
		Log.d("=======the second step", registtype);
	}

	private void init() {
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		edittext_code = (EditText) findViewById(R.id.edittext_code);
		button_psw_next = (Button) findViewById(R.id.button_psw_next);

		imageview_back.setOnClickListener(this);
		button_psw_next.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back:
			finish();
			break;
		case R.id.button_psw_next:
			String account = edittext_code.getText().toString();
			checkcode(account);
			break;
		default:
			break;
		}
	}

	/**
	 * 校验验证码
	 * */
	private void checkcode(final String str_test) {

		System.out.println("输入的数据为==" + str_test);

		String appId = AppConfig.U2AppIds; // 得到本应用appId
		String apiUrl = AppConfig.U2RootUrls + "/apis/usersv2.ashx";

		RequestParams params = new RequestParams();

		String method = "ResetPwdStep2VerifyCode"; // 方法名称，固定值
		final String user = registtype;
		String code = str_test;

		UUID uuid = UUID.randomUUID();
		Log.d("uuid========", uuid.toString());
		String guid = uuid.toString();
		String signText = (method + appId + guid + user + code + AppConfig.U2AppKeys)
				.toLowerCase();

		String sign = md5(signText).toUpperCase(); // 签名

		params.addBodyParameter("method", method);
		params.addBodyParameter("appid", appId);
		params.addBodyParameter("guid", guid);
		params.addBodyParameter("user", registtype);
		params.addBodyParameter("code", code);
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

								Toast.makeText(
										ForgetPswSecondStepActivity.this,
										Message, 2).show();
							} else {

								Intent intent = new Intent();
								intent.setClass(
										ForgetPswSecondStepActivity.this,
										ForgetPswThirdStepActivity.class);
								Bundle b = new Bundle();
								b.putString("registercode", str_test);
								b.putString("registtype", user);
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
