package com.lelts.student.myself;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lels.student.connectionclass.adapter.ConnectionReportAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MystudentTargetAlterTestTypeActivity extends Activity implements
		OnClickListener {

	private ImageView imageView_back;
	private TextView textview_academic;
	private TextView textview_trainingclass;
	private String token;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_targetaltertesttype);
		init();
		getdatafromshare();
	}

	private void init() {
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		textview_academic = (TextView) findViewById(R.id.textview_academic);
		textview_trainingclass = (TextView) findViewById(R.id.textview_trainingclass);

		imageView_back.setOnClickListener(this);
		textview_academic.setOnClickListener(this);
		textview_trainingclass.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back:
			finish();
			break;
		case R.id.textview_academic:
			UpdateStudentSettingsKslb(1);
			Intent intent = new Intent();
			intent.setClass(MystudentTargetAlterTestTypeActivity.this,
					MystudentTargetActivity.class);
			Bundle b = new Bundle();
			b.putString("testtype", "学术类");
			intent.putExtras(b);
			setResult(0, intent);
			finish();
			break;
		case R.id.textview_trainingclass:
			UpdateStudentSettingsKslb(2);
			intent = new Intent();
			intent.setClass(MystudentTargetAlterTestTypeActivity.this,
					MystudentTargetActivity.class);
			b = new Bundle();
			b.putString("testtype", "培训类");
			intent.putExtras(b);
			setResult(0, intent);
			finish();
			break;
		default:
			break;
		}
	}
	
	//获得token
	private void getdatafromshare() {
		SharedPreferences share = this.getSharedPreferences("userinfo",
				MODE_PRIVATE);
		token = share.getString("Token", "");
		
	}
	
	/**
	 * 方法说明：更新我的目标中的考试类别
	 *
	 */
	private void UpdateStudentSettingsKslb(int type) {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);
		final HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, Constants.URL_MYSELF_TAEGET_MYSKSLB
				+ "?examType=" + type , params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				
				System.out.println("URL_MYSELF_TAEGET_MYSKSLB===================" + result);
			}
		});
	}

}
