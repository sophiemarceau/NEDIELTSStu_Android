package com.lels.student.connectionclass.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.student.connectionclass.adapter.PlayBackTestAdapter;
import com.lels.student.starttask.activity.StaetTaskActivity;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PlayBackTestActivity extends Activity implements OnClickListener {

	private Context context;
	private ImageButton img_back_playback_test;
	private ListView listview_playback_test;
	private SharedPreferences share, stushare;
	private Button btn_again_test;
	private List<HashMap<String , Object>> mlist;
	private PlayBackTestAdapter madapter;
	private TextView txt_exercise_name;
	private String exercise_name;
	private Intent intent;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playback_test);
		initview();
		LodDialogClass.showCustomCircleProgressDialog(context, "", getString(R.string.xlistview_header_hint_loading));
		loadStudentHisExercise();
	}

	/**
	 * 初始化控件
	 * 
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		img_back_playback_test = (ImageButton) findViewById(R.id.img_back_playback_test);
		img_back_playback_test.setOnClickListener(this);
		listview_playback_test = (ListView) findViewById(R.id.listview_playback_test);
		btn_again_test = (Button) findViewById(R.id.btn_again_test);
		btn_again_test.setOnClickListener(this);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		stushare = getSharedPreferences("stuinfo", Context.MODE_PRIVATE);
		ExitApplication.getInstance().addActivity(this);
		
		//设置练习题的名称
		txt_exercise_name = (TextView)findViewById(R.id.exercise_name);
		exercise_name = stushare.getString("exercise", "");
		txt_exercise_name.setText(exercise_name);
		
	}

	/**
	 * 
	 * 获取历史练习信息
	 * 
	 */
	private void loadStudentHisExercise() {
		// TODO Auto-generated method stub
		//		[Auth]：token
		//		[URL]：/ActiveClass/loadStudentHisExercise
		//		[Method]：GET
		//		[Args]：
		//		[Return]：
		//		 Infomation = 提示信息;
		//		 Result = 返回结果true/false;
		//		 Data = {
		//		  exerciseCnt = 历史练习数量，可用于判断是否取详细信息,
		//		  hisExercises = 历史练习数据[{,
		//		   EX_ID = ExamInfo表ID,
		//		   doExTime = 练习时间,
		//		   wrongCnt = 错题数,
		//		   totalCnt = 总数,
		//		   rightRate = 正确率,
		//		  }]
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		System.out.println("pId=====" + stushare.getString("P_ID", ""));
		utils.send(HttpMethod.GET, Constants.URL_loadStudentHisExercise
				+ "?pId=" + stushare.getString("P_ID", ""), params,
				new RequestCallBack<String>() {

					private HashMap< String, Object> map;
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						try {
							JSONObject obj = new JSONObject(result);
							String  Result = obj.getString("Result");
							if (Result.equals("true")) {
								JSONObject Data = obj.getJSONObject("Data");
								String exerciseCnt = Data.getString("exerciseCnt");
								if(!exerciseCnt.equals("0")){
									JSONArray array = Data.getJSONArray("hisExercises");
									mlist = new ArrayList<HashMap<String,Object>>();
									for (int i = 0; i < array.length(); i++) {
										JSONObject hisExercises = array.getJSONObject(i);
										map = new  HashMap<String, Object>();
										String doExTime = hisExercises.getString("doExTime");
										String wrongCnt = hisExercises.getString("wrongCnt");
										String totalCnt = hisExercises.getString("totalCnt");
										String rightRate = hisExercises.getString("rightRate");
										map.put("doExTime", doExTime);
										map.put("wrongCnt", wrongCnt);
										map.put("totalCnt", totalCnt);
										map.put("rightRate", rightRate);
										mlist.add(map);
									}
									
									madapter = new PlayBackTestAdapter(mlist, context);
									listview_playback_test.setAdapter(madapter);
									//关闭加载dialog
									LodDialogClass.closeCustomCircleProgressDialog();
								}
								
							}else{
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out
								.println("loadStudentHisExercise历史的信息==================="
										+ result);

					}
				});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.btn_again_test:

			intent = new Intent(context,
					StartListeningTestActivity.class);
			startActivity(intent);
			finish();
			break;

		case R.id.img_back_playback_test:
			intent = new Intent(context,
					StaetTaskActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}
}
