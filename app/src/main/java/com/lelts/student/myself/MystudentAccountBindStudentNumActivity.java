package com.lelts.student.myself;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class MystudentAccountBindStudentNumActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "MystudentAccountBindStudentNumActivity";

	private ImageView imageView_back;
	private EditText edittext_student_num;
	private EditText edittext_student_name;
	
	private Button textview_alter_ok;

	private String token;
	// 学员号【字母、数字 1-20】
	private String stunum = "^[a-zA-Z0-9]{1,20}$";

	private String num;

	private String name;

	private String nickName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_myaccount_bind_studynum);
		init();
		getdatafromshare();
	}

	private void init() {
		Intent intent = getIntent();
		nickName = intent.getStringExtra("NickName");
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		edittext_student_num = (EditText) findViewById(R.id.edittext_student_num);
		edittext_student_name = (EditText) findViewById(R.id.edittext_student_name);
		textview_alter_ok = (Button) findViewById(R.id.textview_alter_ok);

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
			
		//修改确定按钮
		case R.id.textview_alter_ok:
			num = edittext_student_num.getText().toString();
			name = edittext_student_name.getText().toString();
			if (name.equals(nickName)) {
				Pattern pattern = Pattern.compile(stunum);
				boolean tf = pattern.matcher(num).matches();
				if (tf == true) {
					alterpass();
				}else{
					Toast.makeText(
							MystudentAccountBindStudentNumActivity.this,
							"学员号格式不正确，请重新输入！", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(
						MystudentAccountBindStudentNumActivity.this,
						"学员名字输入有误，请重新输入！", Toast.LENGTH_SHORT).show();
			}
			
			break;
		default:
			break;
		}
	}

	private void alterpass() {

		

		String url = new Constants().URL_MYSELF_BINDSTUDENTCODE;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		params.addBodyParameter("stuCode", num);
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
							System.out.println("解析获取我的收藏列表" + responseInfo.result);

							JSONObject str = new JSONObject(responseInfo.result);
							
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");

							if (Result.equalsIgnoreCase("true")) {
								Toast.makeText(
										MystudentAccountBindStudentNumActivity.this,
										"绑定成功", Toast.LENGTH_SHORT).show();
								finish();
							} else {
								Toast.makeText(
										MystudentAccountBindStudentNumActivity.this,
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
