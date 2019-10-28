package com.lelts.student.myself;

import java.util.regex.Pattern;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lels.main.activity.LoginActvity;
import com.lelts.tool.CodeUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class MystudentAccountAlterPasswordActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "MystudentAccountAlterPasswordActivity";

	private ImageView imageView_back;
	private EditText edittext_new_password;
	private EditText edittext_old_password;
	private EditText edittext_new_password_ok;

	private TextView textview_alter_ok;

	private String token;

	private String userId;
	// 密码【字母、数字、符号 6-15个字符】
	String pwd = "[a-zA-Z0-9\u3000-\u301e\ufe10-\ufe19\ufe30-\ufe44\ufe50-\ufe6b\uff01-\uffee]{6,15}$";

	private Editor editor;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_myaccount_alterpassword);
		init();
		getdatafromshare();
	}

	private void init() {
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		edittext_new_password = (EditText) findViewById(R.id.edittext_new_password);
		edittext_old_password = (EditText) findViewById(R.id.edittext_old_password);
		textview_alter_ok = (TextView) findViewById(R.id.textview_alter_ok);
		edittext_new_password_ok = (EditText) findViewById(R.id.edittext_new_password_ok);

		imageView_back.setOnClickListener(this);
		textview_alter_ok.setOnClickListener(this);

	}

	private void getdatafromshare() {
		SharedPreferences share = this.getSharedPreferences("userinfo",
				MODE_PRIVATE);
		SharedPreferences stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		editor = stushare.edit();
		token = share.getString("Token", "");

		userId = share.getString("Account", "");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back:
			finish();
			break;
		case R.id.textview_alter_ok:
			if (edittext_new_password.getText().length() <= 15
					&& edittext_new_password.getText().length() >= 6) {
				if (!edittext_new_password.getText().toString()
						.equalsIgnoreCase(null)
						&& !edittext_new_password_ok.getText().toString()
								.equalsIgnoreCase(null)
						&& edittext_new_password_ok
								.getText()
								.toString()
								.equalsIgnoreCase(
										edittext_new_password.getText()
												.toString())) {
					alterpass();
				} else {
					Toast.makeText(MystudentAccountAlterPasswordActivity.this,
							"请输入两次相同的密码！", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(MystudentAccountAlterPasswordActivity.this,
						"密码请设置为6-15个字符！", Toast.LENGTH_SHORT).show();
			}

			break;
		default:
			break;
		}
	}

	private void alterpass() {

		String oldpaw = edittext_old_password.getText().toString();
		String encodePass = CodeUtil.Encode(oldpaw); 
		String newpaw = edittext_new_password.getText().toString();
		String newencodePass = CodeUtil.Encode(newpaw); 
		String surenewpwd = edittext_new_password_ok.getText().toString();
		// String newpaw_o = eittext_new_password_ok.getText().toString();
		if (oldpaw.length() == 0 || newpaw.length() == 0
				|| surenewpwd.length() == 0) {
			Toast.makeText(MystudentAccountAlterPasswordActivity.this,
					"旧密码和新密码不能为空！", Toast.LENGTH_SHORT).show();
		} else {
			Pattern pattern = Pattern.compile(pwd);
			boolean tf = pattern.matcher(newpaw).matches();
			if (tf == true) {

				String url = new Constants().URL_MYSELF_PASSWORDCHANGE;
				System.out.println("userId=====" + userId + "===newpwd===="
						+ newpaw + "====oldpwd===" + oldpaw);
				RequestParams params = new RequestParams();
				params.addHeader("Authentication", token);

				params.addBodyParameter("userId", userId);
				params.addBodyParameter("newPass", newencodePass);
				params.addBodyParameter("oldPass", encodePass);

				HttpUtils http = new HttpUtils();
				http.configCurrentHttpCacheExpiry(1000 * 10);

				http.send(HttpRequest.HttpMethod.POST, url, params,
						new RequestCallBack<String>() {

							@Override
							public void onStart() {
								super.onStart();
							}

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								try {
									System.out.println("修改密码的解析数据==	"
											+ responseInfo.result);

									JSONObject str = new JSONObject(
											responseInfo.result);

									String Result = str.getString("Result");
									String Infomation = str
											.getString("Infomation");
									String Data = str.getString("Data");
									// JSONObject obj = new JSONObject(Data);

									if (Result.equalsIgnoreCase("true")) {
										editor.putString("userPass", "");
										editor.commit();
										Toast.makeText(
												MystudentAccountAlterPasswordActivity.this,
												"修改成功", Toast.LENGTH_SHORT)
												.show();
										Intent intent = new Intent();
										intent.setClass(
												MystudentAccountAlterPasswordActivity.this,
												LoginActvity.class);
										startActivity(intent);
										finish();
									} else {
										Toast.makeText(
												MystudentAccountAlterPasswordActivity.this,
												Infomation, Toast.LENGTH_SHORT)
												.show();
									}

								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								System.out.println("onFailure");

							}

						});
			} else {
				Toast.makeText(MystudentAccountAlterPasswordActivity.this,
						"新密码格式不正确！", Toast.LENGTH_SHORT).show();
			}

		}

	}

}
