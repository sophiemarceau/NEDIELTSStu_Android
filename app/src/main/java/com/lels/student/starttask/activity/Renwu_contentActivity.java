package com.lels.student.starttask.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lelts.tool.UtiltyHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
public class Renwu_contentActivity extends Activity implements OnClickListener {
	
	@SuppressWarnings("unused")
	private TextView Name,Time,type,nature,CC_ID,C_ID,count;
	private String stId,pId;
	private SharedPreferences share;
	private String url = Constants.URL_HomegetPapersInfo;
	private ImageButton back_img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_renwu_content);
		
		getintent();
		intview();
		gethttps();
	}
	/**
	 * 网络请求
	 */
	private void gethttps() {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));// 添加保密的东西
		params.addBodyParameter("stId", stId);
		params.addBodyParameter("pId", pId);
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				System.out.println(arg0.toString());
				LodDialogClass.closeCustomCircleProgressDialog();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				System.out.println("<<<<"+result);
				
/*			Data":{"Name":"模考试卷091501",
				"questionNum":12,--数量
				"OpenDate":"2015-09-15",--时间
				"classType":{"sName":"IELTS全科班"},--班型
				"TaskType":1,--任务类型 1:模考; 2:练习; 3:资料;
				"TaskProperty":1,--任务性质 -1=预习；0=随堂；1=复习
				"lessonNo":null}}--课次;*/

				try {
					JSONObject obj = new JSONObject(result);
					JSONObject objdata = obj.getJSONObject("Data");
					//Name,Time,type,nature,CC_ID,C_ID;
					Name.setText(objdata.getString("Name"));
					Time.setText(objdata.getString("OpenDate"));
					C_ID.setText(objdata.getString("questionNum"));
					String TaskProperty = objdata.getString("TaskProperty");
					if(TaskProperty.equals("-1")){
						count.setText("预习");	
					}else if(TaskProperty.equals("0")){
						count.setText("随堂");
					}else{
						count.setText("复习");
					}
					String tasktype = objdata.getString("TaskType");
					if(tasktype.equals("1")){
						type.setText("模拟考试");	
					}else if(tasktype.equals("2")){
						type.setText("练习");
					}else{
						type.setText("资料");
					}
					JSONObject classType = objdata.getJSONObject("classType");
					nature.setText(classType.getString("sName"));
					JSONObject objlessonNo = objdata.getJSONObject("lessonNo");
					if (UtiltyHelper.isEmpty(objlessonNo)) {
						CC_ID.setText("");
					}else{
						CC_ID.setText(objlessonNo.getString("nLessonNo"));
					}
					LodDialogClass.closeCustomCircleProgressDialog();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 获取传递值
	 */
	private void getintent() {
		Intent intent = getIntent();
		stId = intent.getStringExtra("ST_ID");
		pId = intent.getStringExtra("P_ID");
		System.out.println("stid？？？？"+stId+"????pId????"+pId);
	}
	/**
	 * 初始化组件
	 */
	private void intview() {
		Name = (TextView) findViewById(R.id.stu_taskcontent_name);
		Time = (TextView) findViewById(R.id.stu_taskcontent_time);
		nature = (TextView) findViewById(R.id.stu_taskcontent_nature);
		type = (TextView) findViewById(R.id.stu_taskcontent_type);
		CC_ID = (TextView) findViewById(R.id.stu_taskcontent_classid);
		C_ID = (TextView) findViewById(R.id.stu_taskcontent_class);
		count = (TextView) findViewById(R.id.stu_taskcontent_count);
		back_img = (ImageButton) findViewById(R.id.stu_taskcontent_back_img);
		back_img.setOnClickListener(this);
		
		share =getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		LodDialogClass.showCustomCircleProgressDialog(Renwu_contentActivity.this, null, getString(R.string.common_Loading));
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stu_taskcontent_back_img:
			finish();
			break;

		default:
			break;
		}
	}

}
