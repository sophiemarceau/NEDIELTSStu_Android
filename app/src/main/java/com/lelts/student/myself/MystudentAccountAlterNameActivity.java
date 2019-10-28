package com.lelts.student.myself;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

public class MystudentAccountAlterNameActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "MystudentAccountAlterNameActivity";

	private ImageButton imageView_back;
	private EditText edittext_altered_name;
	private TextView textview_alter_ok;

	private String token;
	// 昵称【中文、字母、数字、符号 1-20】
	private String nike = "^[a-zA-Z0-9\u4e00-\u9fa5\u3000-\u301e\ufe10-\ufe19\ufe30-\ufe44\ufe50-\ufe6b\uff01-\uffee]{1,20}$";
	//匹配非表情符号的正则表达式
	 private final String reg ="^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){3,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";  
	private Pattern pattern = Pattern.compile(reg);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_myaccount_altername);
		init();
		getdatafromshare();
	}

	private void init() {
		imageView_back = (ImageButton) findViewById(R.id.imageView_back);
		edittext_altered_name = (EditText) findViewById(R.id.edittext_altered_name);
		textview_alter_ok = (TextView) findViewById(R.id.textview_alter_ok);

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
		//确认修改用户昵称
		case R.id.textview_alter_ok:
			String input = edittext_altered_name.getText().toString();
			if(input.length()==0){
			      Toast.makeText(MystudentAccountAlterNameActivity.this,
					        "用户昵称不能为空", Toast.LENGTH_SHORT).show();
			}else{
	
		     //正则匹配是否是表情符号
		     Matcher matcher = pattern.matcher(input.toString());
		     if(!matcher.matches()){
		      Toast.makeText(MystudentAccountAlterNameActivity.this,
		        "不能包含表情", Toast.LENGTH_SHORT).show();
		   }else{
			   Pattern pattern = Pattern.compile(nike);
				boolean tf = pattern.matcher(input).matches();
				if (tf == true) {
				    altername();
				}else{
					  Toast.makeText(MystudentAccountAlterNameActivity.this,
						        "昵称格式不正确，请重新输入！", Toast.LENGTH_SHORT).show();
				}
	
		  }
				}
			break;
		default:
			break;
		}
	}

	private void altername() {

		String name = edittext_altered_name.getText().toString();

		String url = new Constants().URL_MYSELF_NICKNAMECHANGE;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		params.addBodyParameter("newNickName", name);

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
							Log.d(TAG, "解析获取我的收藏列表" + responseInfo.result);

							JSONObject str = new JSONObject(responseInfo.result);
							
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							
							if (Result.equalsIgnoreCase("true")) {
								Toast.makeText(
										MystudentAccountAlterNameActivity.this,
										"修改成功", Toast.LENGTH_SHORT).show();
								finish();
							} else {
								Toast.makeText(
										MystudentAccountAlterNameActivity.this,
										"修改失败", Toast.LENGTH_SHORT).show();
							}
							
							if(Data != null){
								JSONObject obj = new JSONObject(Data);
							
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
