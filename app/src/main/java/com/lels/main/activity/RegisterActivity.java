package com.lels.main.activity;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.strudentlelts.R;
import com.lels.constants.AppConfig;
import com.lelts.tool.IsPhoneorEmail;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class RegisterActivity extends Activity implements OnClickListener {

	private ImageButton imageview_back;
	private EditText edittext_regirst_name;
	private CheckBox register_checkBox;
	private Button button_zhuce_next;
	private TextView textview_note;

//	private boolean Isagree = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		init();
	}

	private void init() {
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		edittext_regirst_name = (EditText) findViewById(R.id.edittext_regirst_name);
		register_checkBox = (CheckBox) findViewById(R.id.register_checkBox);
		button_zhuce_next = (Button) findViewById(R.id.button_zhuce_next);
		textview_note = (TextView) findViewById(R.id.textview_note);

		imageview_back.setOnClickListener(this);
		textview_note.setOnClickListener(this);
		button_zhuce_next.setOnClickListener(this);
		// checkbox
		register_checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_zhuce_next:

			String account = edittext_regirst_name.getText().toString();

			if (register_checkBox.isChecked()) {
				if (IsPhoneorEmail.isMobile(account)) {
					System.out.println("输入的是手机号");
					checkphone(account);
				} else if (IsPhoneorEmail.isEmail(account)) {
					System.out.println("输入的是邮箱");
					checkemail(account);
				} else {
					Toast.makeText(RegisterActivity.this, "输入正确的手机号码或邮箱",
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(RegisterActivity.this, "请阅读并同意雅思互动学习平台服务条款",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.imageview_back:
			finish();
			break;
		case R.id.textview_note:
			Intent intent = new Intent();
			intent.setClass(RegisterActivity.this,
					RegisterDocumentActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/**
	 * 校验邮箱，绑定邮箱 iphone、Android 学生App
	 * */
	private void checkphone(final String str_pp) {

		System.out.println("输入的数据为==" + str_pp);

		String appId = AppConfig.U2AppIds; // 得到本应用appId
		String apiUrl = AppConfig.U2RootUrls + "/apis/usersv2.ashx";
		// String apiUrl = "http://testu2.staff.xdf.cn/apis/usersv2.ashx";

		// String dic = new Dictionary<string, string>();
		RequestParams params = new RequestParams();

		String method = "SendSmsCode"; // 方法名称，固定值
		String mobile = str_pp;
		// String isNewMobile = IsNewMobileTextBox.Text.Trim().ToInt();
		// //0已注册过通行证的旧手机、1未注册过的新手机，新手机要求不能与已绑定通行证的手机重复
		String isNewMobile = "1";
		String signText = (method + appId + mobile + AppConfig.U2AppKeys)
				.toLowerCase();

		String sign = md5(signText).toUpperCase(); // 签名

		params.addBodyParameter("method", method);
		params.addBodyParameter("appid", appId);
		params.addBodyParameter("mobile", mobile);
		params.addBodyParameter("isNewMobile", isNewMobile);
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

						Log.d("RegisterActivity", "注册返回的信息为==="
								+ responseInfo.result);
						try {
							JSONObject str = new JSONObject(responseInfo.result);
							String Status = str.getString("Status");
							String Message = str.getString("Message");
							String Data = str.getString("Data");

							if (!Status.equalsIgnoreCase("1")) {

								Toast.makeText(RegisterActivity.this, Message,
										2).show();
							} else {

								Intent intent = new Intent();
								intent.setClass(RegisterActivity.this,
										RegisterSceondActivity.class);
								Bundle b = new Bundle();
								b.putString("registerFlag", "1");
								b.putString("registtype", str_pp);
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
	 * 校验邮箱，绑定邮箱 iphone、Android 学生App
	 * */
	private void checkemail(final String str_email) {

		System.out.println("输入的数据为==" + str_email);

		String appId = AppConfig.U2AppIds; // 得到本应用appId
		String apiUrl = AppConfig.U2RootUrls + "/apis/usersv2.ashx";

		RequestParams params = new RequestParams();

		String method = "SendEmailCodeV5"; // 方法名称，固定值
		String email = str_email;

		UUID uuid = UUID.randomUUID();
		Log.d("uuid========", uuid.toString());
		String guid = uuid.toString();
		String signText = (method + appId + guid + email + AppConfig.U2AppKeys)
				.toLowerCase();

		String sign = md5(signText).toUpperCase(); // 签名

		params.addBodyParameter("method", method);
		params.addBodyParameter("appid", appId);
		params.addBodyParameter("email", email);
		params.addBodyParameter("guid", guid);
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

						Log.d("RegisterActivity", "注册返回的信息为==="
								+ responseInfo.result);
						try {
							JSONObject str = new JSONObject(responseInfo.result);
							String Status = str.getString("Status");
							String Message = str.getString("Message");
							String Data = str.getString("Data");

							if (!Status.equalsIgnoreCase("1")) {

								Toast.makeText(RegisterActivity.this, Message,
										2).show();
							} else {

								Intent intent = new Intent();
								intent.setClass(RegisterActivity.this,
										RegisterSceondActivity.class);
								Bundle b = new Bundle();
								b.putString("registerFlag", "2");
								b.putString("registtype", str_email);
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
