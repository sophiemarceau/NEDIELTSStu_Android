/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015��7��16�� 
 * 
 *******************************************************************************/
package com.lels.student.connectionclass.activity;

import io.rong.imkit.widget.LoadingDialogFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.bean.ButtonControl;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.main.activity.StundentClassroom;
import com.lels.student.connectionclass.adapter.ConnectionSavetestAdapter;
import com.lelts.student.db.SqliteDao;
import com.lelts.students.bean.StuAnswerInfo;
import com.lelts.tool.OperationFileHelper;
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

public class ConnectionSaveAnswerActivity extends Activity implements
		OnClickListener {
	private Context context;
	private ListView mlistview;
	private List<StuAnswerInfo> mlist;
	private ConnectionSavetestAdapter madapter;
	private ImageButton img_back;
	private TextView txt_commit;
	private Intent intent;
	private int chooseposition;
	private SharedPreferences share, stuinfo;
	private SqliteDao dao;
	private long endtime, jointime;
	private int n;
	private String myanswer;
	private StringBuffer answer;
	private String data;
	private AlertDialog alertDialog;
	private TextView txt_waring;
	private Button btn_sure;
	private Timer end_test_time;
	private String paperId;
	// 试卷的练习
	private int testtype;
	//判断是否是听力答题  1是听力
	private int state;
	private int Data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection_saveanswer);
		initview();

		switch (testtype) {
		// 老师推题
		case 1:
			jointime = stuinfo.getInt("jointime", -1);
			getdata();
			timer();
			break;
		// 资料答题
		case 2:
			jointime = stuinfo.getInt("jointime", -1);
			System.out.println("jointime===" + jointime);
			getdata();
//			getendtime();
			break;
		case 3:
			jointime = stuinfo.getInt("jointime", -1);
			System.out.println("jointime===" + jointime);
			getdata();
//			getendtime();
			break;

		default:
			break;
		}
		ExitApplication.getInstance().addActivity(this);
	}

	/**
	 * 初始化视图
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		dao = new SqliteDao();
		context = this;
		mlistview = (ListView) findViewById(R.id.listview_connection_savetest);
		mlist = new ArrayList<StuAnswerInfo>();
		img_back = (ImageButton) findViewById(R.id.btn_back_conncetion_savetest);
		img_back.setOnClickListener(this);
		txt_commit = (TextView) findViewById(R.id.txt_commit_conncetion_savetest);
		txt_commit.setOnClickListener(this);
		intent = new Intent();
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		stuinfo = getSharedPreferences("stuinfo", Context.MODE_PRIVATE);
		Intent gettime = getIntent();
		testtype = stuinfo.getInt("testtype", -1);
		state = gettime.getIntExtra("state", 0);
	}

	/**
	 * 初始化数据
	 *
	 */
	private void getdata() {
		// TODO Auto-generated method stub

		mlist = dao.GetallAnswer(context);
		System.out.println("mlistanswer============>" + mlist);
		madapter = new ConnectionSavetestAdapter(context, mlist);
		mlistview.setAdapter(madapter);
		mlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(state == 1){
					//听力练习试卷
//					intent.putExtra("testtype", testtype);
//					intent.putExtra("chooseposition", 0);
					chooseposition = position;
					intent.putExtra("testtype", testtype);
					intent.putExtra("chooseposition", chooseposition);
					intent.putExtra("flag", false);
					intent.setClass(ConnectionSaveAnswerActivity.this,
							StartListeningTestActivity.class);
					startActivity(intent);
					finish();
					}
				else{
				
				chooseposition = position;
				intent.putExtra("testtype", testtype);
				intent.putExtra("chooseposition", chooseposition);
				intent.putExtra("flagintent", false);
				intent.setClass(ConnectionSaveAnswerActivity.this,
						ConnetionStartTestActivity.class);
				startActivity(intent);
				finish();
					}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View) 事件的点击监听
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 返回
		case R.id.btn_back_conncetion_savetest:
			// intent.putExtra("chooseposition", chooseposition);
			// intent.putExtra("testtype", testtype);
			// intent.setClass(ConnectionSaveAnswerActivity.this,ConnetionStartTestActivity.class);
			// startActivity(intent);
			// setResult(10, intent);
			if(state == 1){
				//听力练习试卷
//				intent.putExtra("testtype", testtype);
//				intent.putExtra("chooseposition", 0);
				intent.setClass(ConnectionSaveAnswerActivity.this,
						StartListeningTestActivity.class);
				intent.putExtra("flag", false);
				startActivity(intent);

				finish();
			}else{
				
				intent.putExtra("testtype", testtype);
				intent.putExtra("chooseposition", 0);
				intent.setClass(ConnectionSaveAnswerActivity.this,
						ConnetionStartTestActivity.class);
				startActivity(intent);

				finish();
				
			}
	
			break;
		// 连接 提交试卷
		case R.id.txt_commit_conncetion_savetest:
			// 老师推题
			LodDialogClass.showCustomCircleProgressDialog(context, "", "正在提交试卷");
			if (ButtonControl.isFastClick()) {
				return;
			}else{
				
				if (testtype == 1) {
					getendtime();
					OperationFileHelper.RecursionDeleteFile(new File("/sdcard/httpcomponents-client-4.2.5-src.zip"));
					OperationFileHelper.RecursionDeleteFile(new File("/sdcard/folderPath"));
					dao.deleteDatabase(context);
					createExamInfoId();
				} else if(testtype==3){
					
					EvaluateThePaper();
					getendtime();
					dao.deleteDatabase(context);
				}else{
					
					getendtime();
					dao.deleteDatabase(context);
					EvaluateTheMaterialsExercisePaper();
				}
			}
		

			break;
		// 点击 Diaglog 的确定按钮 ，提交试卷
		case R.id.waring_end_classroom_btn_sure:

			getendtime();
			dao.deleteDatabase(context);
			createExamInfoId();
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 方法说明：弹出Dialog
	 *
	 */
	private void ShowDiglog() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater
				.from(ConnectionSaveAnswerActivity.this);
		View myLoginView = layoutInflater.inflate(
				R.layout.waring_end_classroom, null);
		alertDialog = new AlertDialog.Builder(ConnectionSaveAnswerActivity.this)
				.create();
		alertDialog.setView(myLoginView, 0, 0, 0, 30);
		alertDialog.show();
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setCancelable(false);
		txt_waring = (TextView) myLoginView
				.findViewById(R.id.waring_end_classroom_txt_testname);
		if (Data == 3) {
			txt_waring.setText("已达到百分比，请提交试卷！");
		}
		btn_sure = (Button) myLoginView
				.findViewById(R.id.waring_end_classroom_btn_sure);
		// img_close = (ImageButton) myLoginView
		// .findViewById(R.id.waing_end_classroom_btn_close);
		btn_sure.setOnClickListener(this);
		// img_close.setOnClickListener(this);
	}

	/**
	 * 方法说明：心跳 获取教师结束当前练习的指令，如状态为结束，学生端强制提交当前随堂练习试卷的作答  Data = 当前随堂练习试卷的推送状态
	 * 0未推送 1已推送 2已完成(教师点了结束);
	 *
	 */
	private void ActiveClassExerciseStatus() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(HttpMethod.GET, Constants.URL_ActiveClassExerciseStatus
				+ "?ccId=" + stuinfo.getString("ccId", "") + "&paperId="
				+ stuinfo.getString("paperId", ""), params,
				new RequestCallBack<String>() {

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
							Data = obj.getInt("Data");
							System.out.println("data...." + Data);
							if (Data == 2) {
								ShowDiglog();
								if (alertDialog.isShowing()) {
									end_test_time.cancel();
								}
							}else if (Data == 3) {
								ShowDiglog();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out
								.println("ActiveClassExerciseStatus_result==================="
										+ result);

					}
				});
	}

	/**
	 * 方法说明：老师推题时候，提交试卷时，先生成examInfoId，并上传答案
	 *
	 */
	private void createExamInfoId() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		final HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, Constants.URL_createExamInfoId + "?paperId="
				+ stuinfo.getString("paperId", "") + "&TaskType=2"
				+ "&costTime=" + (n + ""), params,
				new RequestCallBack<String>() {

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
							data = obj.getString("Data");

							StringBuffer sb = new StringBuffer();
							for (int i = 0; i < mlist.size(); i++) {
								answer = sb.append(mlist.get(i).getAnswer()
										+ "`");
							}
							myanswer = answer.deleteCharAt(sb.length() - 1)
									.toString();
							System.out.println("paperId====="
									+ stuinfo.getString("paperId", "")
									+ "========examInfoId======" + data
									+ "====answer==========" + myanswer
									+ "===ccId==="
									+ stuinfo.getString("ccId", ""));
							//							[Auth]：token
							//							[URL]：/PaperInfo/EvaluateTheActiveClassExercisePaper
							//							[Method]：POST
							//							[Args]：
							//							 paperId=[考试试卷的主键ID]&
							//							 examInfoId=[examInfoId]&
							//							 studentAnswers=[学生答案]&
							//							 ccId=[课次ID]
							//							 costTime=[答题时间]&
							//							 clientType=[直接传空串]&
							//							[Return]：
							//							 Infomation = 提示信息;
							//							 Result = 返回结果true/false;
							//							 Data = 无返回数据;
							RequestParams params = new RequestParams();
							params.addBodyParameter("paperId",
									stuinfo.getString("paperId", ""));
							params.addBodyParameter("examInfoId", data);
							params.addBodyParameter("studentAnswers", myanswer);
							params.addBodyParameter("ccId",
									stuinfo.getString("ccId", ""));
							params.addBodyParameter("costTime",(n + ""));
							params.addBodyParameter("clientType","");
							params.addHeader("Authentication",
									share.getString("Token", ""));
							utils.send(
									HttpMethod.POST,
									Constants.URL_EvaluateTheActiveClassExercisePaper,
									params, new RequestCallBack<String>() {

										@Override
										public void onFailure(
												HttpException arg0, String arg1) {
											// TODO Auto-generated method stub
										}

										@Override
										public void onSuccess(
												ResponseInfo<String> arg0) {
											// TODO Auto-generated method stub
											String result = arg0.result;
											System.out
													.println("test_result==================="
															+ result);
											// intent.setClass(ConnectionSaveAnswerActivity.this,ConnectionReportActivity.class);
											// startActivity(intent);
											//关闭加载dialog
											LodDialogClass.closeCustomCircleProgressDialog();
											intent.putExtra("examInfoId", data);
											intent.setClass(
													ConnectionSaveAnswerActivity.this,
													ConnectionReportActivity.class);
											startActivity(intent);
											finish();
											System.out
													.println("data===============+++++"
															+ data);
										}
									});

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out.println("example_result==================="
								+ result);
					}
				});

	}

	/**
	 * 方法说明：资料答题时候，提交试卷时，先生成examInfoId，并上传答案
	 *
	 */
	private void EvaluateTheMaterialsExercisePaper() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		final HttpUtils utils = new HttpUtils();
		System.out.println("资料答题时间：" + n);
		utils.send(HttpMethod.GET, Constants.URL_createExamInfoId + "?paperId="
				+ stuinfo.getString("P_ID", "") + "&TaskType=2" + "&costTime="
				+ (n + ""), params, new RequestCallBack<String>() {

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
					data = obj.getString("Data");
					String ST_ID = 	stuinfo.getString("ST_ID", "");
					if (ST_ID.equals("null")) {
						ST_ID = "0";
					}
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < mlist.size(); i++) {
						answer = sb.append(mlist.get(i).getAnswer() + "`");
					}
					myanswer = answer.deleteCharAt(sb.length() - 1).toString();
					System.out.println("P_ID====="
							+ stuinfo.getString("P_ID", "")
							+ "========examInfoId======" + data
							+ "====answer==========" + myanswer + "===mateId==="
							+ stuinfo.getString("mateId", "")+"====ST_ID====="+ST_ID);
				//					Auth]：token
				//					[URL]：/PaperInfo/EvaluateTheMaterialsExercisePaper
				//					[Method]：POST
				//					[Args]：
				//					 paperId=[考试试卷的主键ID]&
				//					 examInfoId=[examInfoId]&
				//					 studentAnswers=[学生答案]&
				//					 costTime=[答题时间]&
				//					 stId=[试卷所属的资料的任务ID]
				//					 clientType=[直接传空串]&
				//					[Return]：
				//					 Infomation = 提示信息;
				//					 Result = 返回结果true/false;
				//					 Data = 无返回数据;
					RequestParams params = new RequestParams();
					params.addBodyParameter("paperId",
							stuinfo.getString("P_ID", ""));
					params.addBodyParameter("examInfoId", data);
					params.addBodyParameter("studentAnswers", myanswer);
					params.addBodyParameter("costTime",(n + ""));
				
					params.addBodyParameter("stId",
							ST_ID);
					params.addBodyParameter("clientType","");
					params.addHeader("Authentication",
							share.getString("Token", ""));
					utils.send(HttpMethod.POST,
							Constants.URL_EvaluateTheMaterialsExercisePaper,
							params, new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									// TODO Auto-generated method stub
									String result = arg0.result;
									System.out
											.println("test_result==================="
													+ result);
									// intent.setClass(ConnectionSaveAnswerActivity.this,ConnectionReportActivity.class);
									// startActivity(intent);
									//关闭加载dialog
									LodDialogClass.closeCustomCircleProgressDialog();
									intent.putExtra("examInfoId", data);
									intent.setClass(
											ConnectionSaveAnswerActivity.this,
											ConnectionReportActivity.class);
									startActivity(intent);
									finish();
									System.out
											.println("data===============+++++"
													+ data);
								}
							});

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out
						.println("example_result===================" + result);
			}
		});

	}
	
	/**
	 * 方法说明：提交试卷(判卷) - 正规判卷，客观题和写作题 ipad、iphone、Android 学生App
	 *
	 */
	private void EvaluateThePaper() {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		final HttpUtils utils = new HttpUtils();
		System.out.println("资料答题时间：" + n);
		utils.send(HttpMethod.GET, Constants.URL_createExamInfoId + "?paperId="
				+ stuinfo.getString("P_ID", "") + "&TaskType=2" + "&costTime="
				+ (n + ""), params, new RequestCallBack<String>() {

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
					data = obj.getString("Data");
					String ST_ID = 	stuinfo.getString("ST_ID", "");
					if (ST_ID.equals("null")||ST_ID.equals("")||ST_ID.equals(null)||ST_ID.isEmpty()) {
						ST_ID = "0";
					}
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < mlist.size(); i++) {
						answer = sb.append(mlist.get(i).getAnswer() + "`");
					}
					myanswer = answer.deleteCharAt(sb.length() - 1).toString();
					System.out.println("P_ID====="
							+ stuinfo.getString("P_ID", "")
							+ "========examInfoId======" + data
							+ "====answer==========" + myanswer + "===mateId==="
							+ stuinfo.getString("mateId", "")+"====ST_ID====="+ST_ID);
					
					// TODO Auto-generated method stub
					// TODO Auto-generated method stub
					//		[Auth]：token
					//		[URL]：/PaperInfo/EvaluateThePaper
					//		[Method]：POST
					//		[Args]：
					//		 paperId=[考试试卷的主键ID]&
					//		 stId=[该次考试所属的任务ID]&
					//		 studentAnswers=[学生的答案]&
					//		 examInfoId=[examInfo表主键ID]
					//		 costTime=[答题时间]&
					//		 clientType=[直接传空串]&
					//		[Return]：
					//		 Infomation = 提示信息;
					//		 Result = 返回结果true/false;
					//		 Data = 生成的examInfoId;
					RequestParams params = new RequestParams();
					params.addBodyParameter("paperId",
							stuinfo.getString("P_ID", ""));
					params.addBodyParameter("stId",ST_ID);
					params.addBodyParameter("studentAnswers", myanswer);
					params.addBodyParameter("examInfoId", data);
					params.addBodyParameter("costTime",(n + ""));
					params.addBodyParameter("clientType","");
					params.addHeader("Authentication",
							share.getString("Token", ""));
					utils.send(HttpMethod.POST,
							Constants.URL_EvaluateThePaper,
							params, new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									// TODO Auto-generated method stub
									String result = arg0.result;
									System.out
											.println("test_result==================="
													+ result);
									// intent.setClass(ConnectionSaveAnswerActivity.this,ConnectionReportActivity.class);
									// startActivity(intent);
									//关闭加载dialog
									LodDialogClass.closeCustomCircleProgressDialog();
									intent.putExtra("examInfoId", data);
									intent.setClass(
											ConnectionSaveAnswerActivity.this,
											ConnectionReportActivity.class);
									startActivity(intent);
									finish();
									System.out
											.println("data===============+++++"
													+ data);
								}
							});

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out
						.println("example_result===================" + result);
			}
		});

	}
	
	

	/**
	 * 方法说明：结束时间
	 *
	 */
	private void getendtime() {
		// TODO Auto-generated method stub
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		long Hours = curDate.getHours();
		long Seconds = curDate.getSeconds();
		long Minutes = curDate.getMinutes();
		endtime = Hours * 3600 + Minutes * 60 + Seconds;
		System.out.println("endtime======"+endtime);
		System.out.println("jointime======="+jointime);
		System.out.println("Join===========" + str);
		System.out.println("curDate.getTime();" + curDate.getTime());
		System.out.println("	curDate.getSeconds();" + curDate.getSeconds());
		System.out.println("	curDate.getHours();" + curDate.getHours());
		System.out.println("	curDate.getMinutes();" + curDate.getMinutes());
		n = (int) ((endtime) - (jointime));
		System.out.println("时间差=====" + n);
	}

	/**
	 * 方法说明：每3秒 请求看是否下课
	 *
	 */
	private void timer() {
		end_test_time = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ActiveClassExerciseStatus();
				getendtime();
			}
		};
		end_test_time.schedule(task, 3000, 3000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// 停止定时器
		if (testtype == 1) {

			end_test_time.cancel();
		}
		super.onDestroy();
	}

}
