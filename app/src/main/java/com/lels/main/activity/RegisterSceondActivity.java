package com.lels.main.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lelts.student.myself.MystudentAccountBindStudentNumActivity;
import com.lelts.tool.CodeUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class RegisterSceondActivity extends Activity implements OnClickListener {

	private ImageButton imageview_back;
	private EditText edittext_regirst_name, edittext_regirst_phone,
			edittext_regirst_email, edittext_regirst_band_student_num,
			edittext_regirst_pwd, edittext_regirst_pwd_more;
	private Button button_zhuce_ok;
	SharedPreferences share; 
	Editor editor;

	private String registerFlag;
	private String registertype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_secondtype);
		getdatafromintent();
		System.out.println("RegisterSceondActivity");
		init();
	}

	private void getdatafromintent() {
		Bundle b = getIntent().getExtras();
		registerFlag = b.getString("registerFlag");
		registertype = b.getString("registtype");
		Log.d("registersceond", registerFlag + " -------- " + registertype);
	}

	private void init() {
		share = getSharedPreferences("userinfo", MODE_PRIVATE);
		editor = share.edit();
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		button_zhuce_ok = (Button) findViewById(R.id.button_zhuce_ok);
		edittext_regirst_name = (EditText) findViewById(R.id.edittext_regirst_name);// name
		edittext_regirst_phone = (EditText) findViewById(R.id.edittext_regirst_phone);// phone
		edittext_regirst_email = (EditText) findViewById(R.id.edittext_regirst_email);// email
		edittext_regirst_band_student_num = (EditText) findViewById(R.id.edittext_regirst_band_student_num);// checknum
		edittext_regirst_pwd = (EditText) findViewById(R.id.edittext_regirst_pwd);// password
		edittext_regirst_pwd_more = (EditText) findViewById(R.id.edittext_regirst_pwd_more);// password
																							// more
		if (registerFlag.equalsIgnoreCase("1")) {
			edittext_regirst_phone.setText(registertype);
		} else {
			edittext_regirst_email.setText(registertype);
		}

		imageview_back.setOnClickListener(this);
		button_zhuce_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_zhuce_ok:

			String name = edittext_regirst_name.getText().toString();
			String phone = edittext_regirst_phone.getText().toString();
			String email = edittext_regirst_email.getText().toString();
			String checknum = edittext_regirst_band_student_num.getText()
					.toString();
			String pwd = edittext_regirst_pwd.getText().toString();
			//对密码进行加密
			String encodePass = CodeUtil.Encode(pwd);
			String pwd_s = edittext_regirst_pwd_more.getText().toString();
			System.out.println("手机的长度为" + phone.length());
			System.out.println("是否为11位" + (phone.length() == 11));
			System.out
					.println("是否第一位十一 ========" + phone.subSequence(0, 1) != "1");
			
			if ((phone.length() == 11) && phone.subSequence(0, 1) .equals("1")) {
				register(registerFlag, name, phone, email, checknum, encodePass, pwd_s);
				/*editor.putString("username", name);
				editor.putString("userPass", encodePass);
				editor.commit();*/
				System.out.println("registerFlag--"+registerFlag+"---name---"+name+"---phone---"+phone
						+"---email---"+email+"---checknum---"+checknum+"---pwd---"+pwd+"---pwd_s---"+pwd_s);
			} else {
				Toast.makeText(RegisterSceondActivity.this, "请输入11位手机号",
						Toast.LENGTH_SHORT).show();
				return;
			}

			break;
		case R.id.imageview_back:
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * register student account registerFlag=[注册标识，1手机2邮箱，不可为空]
	 *  phone=[手机号码，不可为空]  email=[邮箱，不可为空]  pwd=[密码，不可为空]  smsCode=[验证码，不可为空]
	 *  nickName=[昵称，不可为空]
	 * 
	 * **/
	@SuppressWarnings("unused")
	private void register(String tag, final String name, String phone, String email,
			String sign, final String pass, String pass_s) {

		String url = new Constants().URL_STUDENT_REGISTER;
System.out.println("tag--"+tag+"phone--"+phone+"email--"+email+"pass--"+pass+"sign--"+sign+"name--"+name);
		RequestParams params = new RequestParams();

		params.addQueryStringParameter("registerFlag", tag);
		params.addQueryStringParameter("phone", phone);

		params.addQueryStringParameter("email", email);
		params.addQueryStringParameter("pwd", pass);
		params.addQueryStringParameter("smsCode", sign);
		params.addQueryStringParameter("nickName", name);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				Log.d("RegisterSceondActivity", "注册完成之后返回的信息为==="
						+ responseInfo.result);
				try {
					JSONObject str = new JSONObject(responseInfo.result);
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");

					if (Result.equalsIgnoreCase("false")) {
						Toast.makeText(RegisterSceondActivity.this, Infomation,
								Toast.LENGTH_SHORT).show();

						// Intent intent = new Intent();
						// intent.setClass(RegisterSceondActivity.this,
						// MyRegisterBindStudentNumActivity.class);
						// startActivity(intent);
						//
						// finish();

					} else {
						editor.putString("username", name);
						editor.putString("userPass", pass);
						editor.commit();
						Toast.makeText(RegisterSceondActivity.this, "恭喜你，注册成功",
								Toast.LENGTH_SHORT).show();

						Intent intent = new Intent();
						intent.setClass(RegisterSceondActivity.this,
								MyRegisterBindStudentNumActivity.class);
						startActivity(intent);

						finish();
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
