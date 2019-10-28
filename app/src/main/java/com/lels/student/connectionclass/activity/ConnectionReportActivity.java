/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015��7��17�� 
 * 
 *******************************************************************************/ 
package com.lels.student.connectionclass.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.example.strudentlelts.R.color;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.main.activity.StundentClassroom;
import com.lels.student.connectionclass.adapter.ConnectionReportAdapter;
import com.lels.student.main.fragment.User_interact_classActivity;
import com.lels.student.starttask.activity.StaetTaskActivity;
import com.lels.student.studyonline.StudyOnlineActivity;
import com.lelts.student.db.SqliteDao;
import com.lelts.tool.RoundProgressBar;
import com.lelts.tool.RoundProgressBarforreport;
import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ConnectionReportActivity extends Activity implements OnClickListener{
	private Context context;
	private ListView mlistview;
	private List<HashMap<String, Object>> mlist;
	private ConnectionReportAdapter madapter;
	private ImageButton img_back;
	private TextView txt_time;
	private SharedPreferences share,stuinfo;
	private Intent intent;
	private int progress;
	private String exId;
	private String CostTime,Accuracy,QNumber,AnswerContent,QSValue,RightCount,ScoreCount;
	private String CompletionRate = null;
	private SqliteDao dao ;
	private int state;//判断是否下课
	private AlertDialog class_alertDialog,alertDialog;
	private Timer class_end;
	private TextView txt_waring;
	private Button btn_sure;
	//判断试卷的 类型， 1 老师推题 ，2 资料答题
	private int testtype;
//	"CostTime": "答题时间",
//	  "Accuracy": "正确率",
//	  "studentOwnExamAnswerList": 错题集[{
//	   "QNumber": "试题编号"    "AnswerContent": "学生答案",
//	   "QSValue": "正确答案(当RightCount-ScoreCount<0，显示正确答案)",
//	   "RightCount": 正确的得分点,
//	   "ScoreCount": 总得分点
	//完成率
	private TextView txt_completion;
	private int CompletionRate_pross;
	//回放答题
	private TextView txt_playback;
	private RoundProgressBarforreport trueorfalse_pro2, trueorfalse_pro3,complete_pro2, complete_pro3;
	//ccid
	private String code, ccId, className, nLessonNo, paperName, hasresult,
	PaperID, activeClassPaperInfoId, PaperSubmitCountdown;
	private ImageView img_close;
	private TextView waring_txtclassName, waring_txtnLessonNo,
	waring_txtpaperName;
	//判断是否老师结束了
	private boolean endtest = false;
	private int Data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection_report);
		initview();
		
		switch (testtype) {
		//老师推题
		case 1:
			findWholeSubmitModeStudentExerciseReport();
			break;
		//资料答题
		case 2:
			findMaterialsExerciseReport();
			break;
		//听力答题
		case 3:
			findMaterialsExerciseReport();
			break;
		default:
			break;
		}
		
		ExitApplication.getInstance().addActivity(this);
	}
	
	/**
	 * 初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		intent = new Intent();
		mlistview = (ListView) findViewById(R.id.listview_conncetion_report);
		mlist = new ArrayList<HashMap<String,Object>>();
		img_back = (ImageButton) findViewById(R.id.img_back_conncetion_report);
		img_back.setOnClickListener(this);
		txt_time = (TextView) findViewById(R.id.txt_time_conncetion_report);
		trueorfalse_pro2 = (RoundProgressBarforreport) findViewById(R.id.roundProgressBar2_trueorfalse);
		trueorfalse_pro3 = (RoundProgressBarforreport) findViewById(R.id.roundProgressBar3_trueorfalse);
		
		complete_pro3 = (RoundProgressBarforreport) findViewById(R.id.roundProgressBar3_complete);
		complete_pro2 = (RoundProgressBarforreport) findViewById(R.id.roundProgressBar2_complete);
		txt_completion = (TextView) findViewById(R.id.textView3);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		stuinfo = getSharedPreferences("stuinfo", Context.MODE_PRIVATE);
		ccId = stuinfo.getString("ccId", "");
		
		Intent getexId = getIntent();
		exId = getexId.getStringExtra("examInfoId");
		System.out.println("exId============"+exId);
		dao = new SqliteDao();
		dao.deleteDatabase(context);
		testtype = stuinfo.getInt("testtype", -1);
		
		txt_playback = (TextView) findViewById(R.id.txt_playback);
		txt_playback.setOnClickListener(this);
		LodDialogClass.showCustomCircleProgressDialog(context, "", "正在获取成绩...");
		
		if(testtype == 3){
			txt_playback.setVisibility(View.VISIBLE);
			txt_time.setVisibility(View.INVISIBLE);
			txt_completion.setText("完成率");
			
		}else if(testtype == 2){
			txt_time.setVisibility(View.INVISIBLE);
			txt_playback.setVisibility(View.GONE);
			txt_completion.setText("完成率");
		}else{
			complete_pro3.setProgress(100);
			complete_pro2.setVisibility(View.INVISIBLE);
			txt_completion.setText("计时器");
		}
	}
	
	
	/**
	 * 初始化probar
o	 *
	 */
	private void initprobar() {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (progress <= Integer.parseInt(Accuracy.substring(0,Accuracy.length()-1))) {
					System.out.println(progress);
					trueorfalse_pro2.setProgress(progress);
					trueorfalse_pro2.setTextColor(color.describe_color);
					trueorfalse_pro3.setTextColor(color.describe_color);
					trueorfalse_pro3.setProgress(progress);
					progress += 1;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("CompletionRate 完成率===="+CompletionRate);
								while (CompletionRate_pross <= Integer.parseInt(CompletionRate.substring(0,CompletionRate.length()-1))) {
					System.out.println("CompletionRate_pross===="+CompletionRate_pross);
					complete_pro2.setProgress(CompletionRate_pross);
					complete_pro2.setTextColor(color.describe_color);
					complete_pro3.setProgress(100);
					complete_pro3.setTextColor(color.describe_color);
					CompletionRate_pross += 1;
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				}
			
		}).start();
	}
	/*
	 * 加载老师推题时候的进度条
	 * 
	 */
	private void initpro_StudentExerciseReport() {
		// TODO Auto-generated method stub

			new Thread(new Runnable() {

				@Override
				public void run() {
					while (progress <= Integer.parseInt(Accuracy.substring(0,Accuracy.length()-1))) {
						System.out.println(progress);
						trueorfalse_pro2.setProgress(progress);
						trueorfalse_pro2.setTextColor(color.describe_color);
						trueorfalse_pro3.setTextColor(color.describe_color);
						trueorfalse_pro3.setProgress(progress);
						complete_pro3.setProgress(100);
						progress += 1;
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}).start();

	}
	
	/**
	 * 方法说明：老师推题的试卷 ，提交后，课堂练习报告
	 *
	 */
	private void findWholeSubmitModeStudentExerciseReport() {
		// TODO Auto-generated method stub
	
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		final HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, Constants.URL_findStudentOwnExerciseReport
				+ "?exId=" + exId, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject data = obj.getJSONObject("Data");
					CostTime = data.getString("CostTime");
					Accuracy = data.getString("Accuracy");
					txt_time.setText(CostTime);
				
					initpro_StudentExerciseReport();
					System.out.println("CostTime"+CostTime+"===Accuracy"+Accuracy);
					JSONArray studentOwnExamAnswerList = data.getJSONArray("studentOwnExamAnswerList");
					
					for (int i = 0; i < studentOwnExamAnswerList.length(); i++) {
						JSONObject answer_info = studentOwnExamAnswerList.getJSONObject(i);
						HashMap<String, Object> map = new HashMap<String, Object>();
						QNumber = answer_info.getString("QNumber");
						AnswerContent = answer_info.getString("AnswerContent");
				//		 "QSValue": "正确答案(当RightCount-ScoreCount<0，显示正确答案)",
						QSValue = answer_info.getString("QSValue");
						RightCount = answer_info.getString("RightCount");
						ScoreCount = answer_info.getString("ScoreCount");
						map.put("QNumber", QNumber);
						map.put("AnswerContent", AnswerContent);
						map.put("QSValue", QSValue);
						map.put("RightCount", RightCount);
						map.put("ScoreCount", ScoreCount);
						mlist.add(map);
					}
					madapter = new ConnectionReportAdapter(context, mlist);
					mlistview.setAdapter(madapter);
					LodDialogClass.closeCustomCircleProgressDialog();
					timer();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("findStudentOwnExerciseReport_result===================" + result);
			}
		});
		
	}
	
	
	/**
	 * 方法说明：资料答题的试卷，提交后，资料答题报告
	 *
	 */
	private void findMaterialsExerciseReport() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		final HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, Constants.URL_findMaterialsExerciseReport
				+ "?exId=" + exId, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				LodDialogClass.closeCustomCircleProgressDialog();
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject data = obj.getJSONObject("Data");
					CostTime = data.getString("CostTime");
					Accuracy = data.getString("Accuracy");
					CompletionRate = data.getString("CompletionRate");
					initprobar();
					System.out.println("CostTime"+CostTime+"===Accuracy"+Accuracy);
					JSONArray studentOwnExamAnswerList = data.getJSONArray("studentExamAnswerList");
					
					for (int i = 0; i < studentOwnExamAnswerList.length(); i++) {
						JSONObject answer_info = studentOwnExamAnswerList.getJSONObject(i);
						HashMap<String, Object> map = new HashMap<String, Object>();
						QNumber = answer_info.getString("QNumber");
						AnswerContent = answer_info.getString("AnswerContent");
				//		 "QSValue": "正确答案(当RightCount-ScoreCount<0，显示正确答案)",
						QSValue = answer_info.getString("QSValue");
						RightCount = answer_info.getString("RightCount");
						ScoreCount = answer_info.getString("ScoreCount");
						map.put("QNumber", QNumber);
						map.put("AnswerContent", AnswerContent);
						map.put("QSValue", QSValue);
						map.put("RightCount", RightCount);
						map.put("ScoreCount", ScoreCount);
						mlist.add(map);
					}
					madapter = new ConnectionReportAdapter(context, mlist);
					mlistview.setAdapter(madapter);
					LodDialogClass.closeCustomCircleProgressDialog();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("findStudentOwnExerciseReport_result===================" + result);
			}
		});
		
	}
	
	
	/**
	 * 方法说明：每隔3秒 网络请求， 查看是否老师已经发题了。
	 * 
	 */
	private void gettestInfo() {
		// TODO Auto-generated method stub
		System.out.println("ccId==================" + ccId);
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(HttpMethod.GET,
				Constants.URL_FindPushStatusActiveClassPaperInfo + "?ccId="
						+ ccId, params, new RequestCallBack<String>() {

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
							hasresult = obj.getString("Result");
							if (hasresult.equals("true")) {
								JSONObject obj1 = obj.getJSONObject("Data");
								className = obj1.getString("className");
								nLessonNo = obj1.getString("nLessonNo");
								paperName = obj1.getString("paperName");
								PaperID = obj1.getString("PaperID");
								PaperSubmitCountdown = obj1
										.getString("PaperSubmitCountdown");
								activeClassPaperInfoId = obj1
										.getString("activeClassPaperInfoId");
								ShowChooseClassDiglog();

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out.println("tst_result==================="
								+ result);
					}
				});

	}
	
	
	/**
	 * 方法说明：弹出是否答题的Dialog
	 * 
	 */
	private void ShowChooseClassDiglog() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View myLoginView = layoutInflater.inflate(R.layout.waring_classroom,
				null);
		if (alertDialog == null) {
			alertDialog = new AlertDialog.Builder(ConnectionReportActivity.this)
					.create();
			alertDialog.setView(myLoginView, 0, 0, 0, 30);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setCancelable(false);
			alertDialog.show();
		}

		// alertDialog = new AlertDialog.Builder(context).create();
		// alertDialog.setView(myLoginView, 0, 0, 0, 30);
		// alertDialog.getWindow().setGravity(Gravity.CENTER);
		// alertDialog.setCanceledOnTouchOutside(false);
		// alertDialog.setCancelable(false);
		// alertDialog.show();

		btn_sure = (Button) myLoginView
				.findViewById(R.id.waring_connection_btn_sure);
		img_close = (ImageView) myLoginView
				.findViewById(R.id.waing_connection_btn_close);
		waring_txtnLessonNo = (TextView) myLoginView
				.findViewById(R.id.waring_connection_txt_classnum);
		waring_txtclassName = (TextView) myLoginView
				.findViewById(R.id.waring_connection_txt_calssname);
		waring_txtpaperName = (TextView) myLoginView
				.findViewById(R.id.waring_connection_txt_testname);
		waring_txtclassName.setText(className);
		waring_txtpaperName.setText(paperName);
		waring_txtnLessonNo.setText("第" + nLessonNo + "课");
		btn_sure.setOnClickListener(this);
		img_close.setOnClickListener(this);
	}
	
	
	/**
	 * 方法说明：心跳 学生端，获取随堂练习的课堂状态
	 *Data = 当前随堂练习的课堂状态 0未上课 1上课中 2已下课(教师点了下课);
	 *
	 */
	private void ActiveClassStatus() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(HttpMethod.GET, Constants.URL_ActiveClassStatus
				+ "?passCode=" +stuinfo.getString("code", "") , params, new RequestCallBack<String>() {

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
					state = obj.getInt("Data");
					System.out.println("课堂状态===state==="+state);
					//已经下课
					if(state==2){
						class_ShowDiglog();
						class_end.cancel();
					}else{
						gettestInfo();
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("ConnectionReportActivity_ActiveClassStatus_result===================" + result);
			}
		});

	}
	
	
	
	/**
	 * 方法说明：弹出下课Dialog
	 *
	 */
	private void class_ShowDiglog() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View myLoginView = layoutInflater.inflate(
				R.layout.waring_end_classroom, null);
		class_alertDialog = new AlertDialog.Builder(ConnectionReportActivity.this).create();
		class_alertDialog.setView(myLoginView, 0, 0, 0, 30);
		class_alertDialog.show();
		class_alertDialog.getWindow().setGravity(Gravity.CENTER);
		class_alertDialog.setCanceledOnTouchOutside(false);
		class_alertDialog.setCancelable(false);
		txt_waring = (TextView) myLoginView.findViewById(R.id.waring_end_classroom_txt_testname);
		txt_waring.setText("老师已下课，请点击确定返回互动课堂首页！");
		btn_sure = (Button) myLoginView
				.findViewById(R.id.waring_end_classroom_btn_sure);
	//	img_close = (ImageButton) myLoginView
//				.findViewById(R.id.waing_end_classroom_btn_close);
		btn_sure.setOnClickListener(this);
	//	img_close.setOnClickListener(this);
	}
	
	
	/**
	 * 方法说明：心跳 获取教师结束当前练习的指令，如状态为结束，学生端强制提交当前随堂练习试卷的作答
	 * Data = 当前随堂练习试卷的推送状态 0未推送 1已推送 2已完成(教师点了结束);
	 *
	 */
	private void ActiveClassExerciseStatus() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		System.out.println("ccId==="+ccId+"====paperId===="+PaperID);
	    HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(
				HttpMethod.GET,
				Constants.URL_ActiveClassExerciseStatus
						+ "?ccId="
						+ ccId
						+ "&paperId=" + PaperID,
				params, new RequestCallBack<String>() {

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
							int Data = obj.getInt("Data");
							System.out.println("data...."+Data);
							if(Data ==2 ){
								endtest =true;
								
								Toast.makeText(context, "答题已经结束，请关闭提示框", Toast.LENGTH_SHORT).show();
								
							}else if (Data ==1){
								endtest =false;
								if (alertDialog!=null) {
									alertDialog.dismiss();
								}
								SharedPreferences share = getSharedPreferences("stuinfo",
										MODE_PRIVATE);
								SharedPreferences.Editor editor = share.edit();
								editor.putString("ccId", ccId);
								editor.putString("paperId", PaperID);
								editor.putString("PaperSubmitCountdown", PaperSubmitCountdown);
								editor.putString("activeClassPaperInfoId", activeClassPaperInfoId);
								editor.putInt("testtype", 1);
								editor.commit();
								intent = new Intent(ConnectionReportActivity.this,
										ConnetionStartTestActivity.class);
								// type 1 ,老师推题
								System.out.println("PaperSubmitCountdown====="
										+ PaperSubmitCountdown);
								startActivity(intent);
								finish();
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out.println("ActiveClassExerciseStatus_result==================="
								+ result);
						
					}
				});
	}

	

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//返回
		case R.id.img_back_conncetion_report:
			switch (testtype) {
			//老师推题
			case 1:
				intent.setClass(ConnectionReportActivity.this, StundentClassroom.class);
				startActivity(intent);
				finish();
				break;
			//资料答题
			case 2:
//				intent.setClass(ConnectionReportActivity.this, StudyOnlineActivity.class);
//				startActivity(intent);
				finish();
				break;
			case 3:
				intent.setClass(ConnectionReportActivity.this, StaetTaskActivity.class);
				startActivity(intent);
				finish();
				break;
			default:
				break;
			}
			
			break;
			//回放按钮
		case R.id.txt_playback:
			intent = new Intent(context, PlayBackTestActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.waring_end_classroom_btn_sure:
			intent.setClass(ConnectionReportActivity.this, User_interact_classActivity.class);
			startActivity(intent);
			finish();
			break;
			
			// 开始答题-
		case R.id.waring_connection_btn_sure:
			
			ActiveClassExerciseStatus();
			// 关闭Diaglog
		case R.id.waing_connection_btn_close:
			alertDialog.dismiss();
			alertDialog = null;
			break;

		default:
			break;
		}
	}
	

	/**
	 * 3秒循环请求数据 老师是否下课
	 *
	 */
	private void timer() {  
		
		class_end = new Timer();
		TimerTask stutask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ActiveClassStatus();
			}
		
		};
		class_end.schedule(stutask, 3000,3000);

	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		停止计时器
		if (class_end!=null) {
			
			class_end.cancel();
		}
		class_end  = null;
		super.onDestroy();
		
		
	}

}
