package com.lels.main.activity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.constants.AppConfig;
import com.lelts.tool.CodeUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgetPswThirdStepActivity extends Activity implements
		OnClickListener {

	private ImageButton imageview_back;
	private EditText edittext_new_pwd, edittext_pwd_more;
	private Button button_psw_ok;

	private String registercode;
	private String registtype;

	private SharedPreferences stushare;
	private Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpsw_nexttype);

		getdatafromintent();

		init();

	}

	private void getdatafromintent() {
		Bundle b = getIntent().getExtras();
		registercode = b.getString("registercode");
		registtype = b.getString("registtype");

		Log.d("getdatais    ", registercode + "   " + registtype);
	}

	private void init() {

		stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		editor = stushare.edit();
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		edittext_new_pwd = (EditText) findViewById(R.id.edittext_new_pwd);
		edittext_pwd_more = (EditText) findViewById(R.id.edittext_pwd_more);
		button_psw_ok = (Button) findViewById(R.id.button_psw_ok);

		imageview_back.setOnClickListener(this);
		button_psw_ok.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back:
			finish();
			break;
		case R.id.button_psw_ok:

			String str_test = edittext_new_pwd.getText().toString();
			//加密----
			String encodePass = CodeUtil.Encode(str_test); 
			String str_more = edittext_pwd_more.getText().toString();
			if (!str_test.equalsIgnoreCase(str_more)) {
				Toast.makeText(ForgetPswThirdStepActivity.this,
						"两次输入密码不一样，请重新输入", Toast.LENGTH_SHORT).show();
			} else {
				alterpsw(encodePass);
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 校验验证码
	 * */
	private void alterpsw(final String str_test) {

		System.out.println("输入的数据为==" + str_test);

		String appId = AppConfig.U2AppIds; // 得到本应用appId
		String apiUrl = AppConfig.U2RootUrls + "/apis/usersv2.ashx";

		RequestParams params = new RequestParams();

		String method = "ResetPwdStep3SetNewPwd"; // 方法名称，固定值
		String user = registtype;
		String code = registercode;

		 String newPwd = str_test;

		UUID uuid = UUID.randomUUID();
		Log.d("uuid========", uuid.toString());
		String guid = uuid.toString();
		String signText = (method + appId + guid + user + code + newPwd + AppConfig.U2AppKeys)
				.toLowerCase();

		String sign = md5(signText).toUpperCase(); // 签名

		params.addBodyParameter("method", method);
		params.addBodyParameter("appid", appId);
		params.addBodyParameter("guid", guid);
		params.addBodyParameter("user", user);
		params.addBodyParameter("code", code);
		params.addBodyParameter("newPwd", newPwd);
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

						Log.d("ForgetPswThirdStepActivity", "重置密码返回==="
								+ responseInfo.result);
						try {
							JSONObject str = new JSONObject(responseInfo.result);
							String Status = str.getString("Status");
							String Message = str.getString("Message");
							String Data = str.getString("Data");

							if (!Status.equalsIgnoreCase("1")) {

								Toast.makeText(ForgetPswThirdStepActivity.this,
										Message, Toast.LENGTH_SHORT).show();
							} else {
								/*editor.putString("userPass", str_test);
								editor.commit();*/
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
