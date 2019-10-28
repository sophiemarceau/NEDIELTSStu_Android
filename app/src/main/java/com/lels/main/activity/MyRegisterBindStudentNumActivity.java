package com.lels.main.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class MyRegisterBindStudentNumActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "MystudentAccountBindStudentNumActivity";

	private ImageButton imageView_back;
	private EditText edittext_student_num;
	private EditText edittext_student_name;

	private Button textview_alter_ok;

	private String token;

	private TextView textview_jump;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_bind_studynum);
		init();
		getdatafromshare();
	}

	private void init() {
		imageView_back = (ImageButton) findViewById(R.id.imageView_back);
		edittext_student_num = (EditText) findViewById(R.id.edittext_student_num);
		edittext_student_name = (EditText) findViewById(R.id.edittext_student_name);
		textview_alter_ok = (Button) findViewById(R.id.textview_alter_ok);

		textview_jump = (TextView) findViewById(R.id.textview_jump);

		textview_jump.setOnClickListener(this);
		imageView_back.setOnClickListener(this);
		textview_alter_ok.setOnClickListener(this);

	}

	private void getdatafromshare() {
		SharedPreferences share = this.getSharedPreferences("userinfo",
				MODE_PRIVATE);
		token = share.getString("Token", "");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back:
			finish();
			break;

		// 修改确定按钮
		case R.id.textview_alter_ok:

			alterpass();
			break;
		case R.id.textview_jump:

			finish();

			break;
		default:
			break;
		}
	}

	private void alterpass() {

		String num = edittext_student_num.getText().toString();
		String name = edittext_student_name.getText().toString();

		String url = new Constants().URL_MYSELF_BINDSTUDENTCODE;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		params.addBodyParameter("sStudentId", num);
		params.addBodyParameter("stuName", name);

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
							Log.d(TAG, "解析获取我的studentnum bind"
									+ responseInfo.result);

							JSONObject str = new JSONObject(responseInfo.result);

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");

							if (Result.equalsIgnoreCase("true")) {
								Toast.makeText(
										MyRegisterBindStudentNumActivity.this,
										"绑定成功", Toast.LENGTH_SHORT).show();
								finish();
							} else {
								Toast.makeText(
										MyRegisterBindStudentNumActivity.this,
										Infomation, Toast.LENGTH_SHORT).show();
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
