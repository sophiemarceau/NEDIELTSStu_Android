package com.lels.main.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.main.activity.adapter.DialogAdapter;
import com.lels.main.activity.adapter.StudentClassAdapter;
import com.lels.student.connectionclass.activity.ConnetionStartTestActivity;
import com.lels.student.vote.activity.BaseDialogActivity;
import com.lels.student.vote.activity.VoteContentActivity;
import com.lelts.tool.UtiltyHelper;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StundentClassroom extends BaseDialogActivity implements OnClickListener {
	private AlertDialog alertDialog, vote_dialog,class_alertDialog,groupdialog;
	private SharedPreferences share, stuinfo;
	private Context context;
	private StudentClassAdapter madapter;
	private GridView mgridview;
	private String code, ccId, className, nLessonNo, paperName, hasresult,
			PaperID, activeClassPaperInfoId, PaperSubmitCountdown;
	private TextView txt_sClassCode, txt_className, txt_teacherName,
			txt_linenum, txt_waring;
	private TextView waring_txtclassName, waring_txtnLessonNo,
			waring_txtpaperName;
	private TextView btn_nLessonNo;
	private List<HashMap<String, Object>> mlist;
	private Button btn_sure;
	private ImageView img_close;
	private Timer stutimer, testtimer, clsassstatetimer, votetimer,votestatetimer;
	private ImageButton img_back;
	private Intent intent;
	private String[] answer;
	private int state;// 判断是否下课
	private TextView txt_teachercontent;
	// 判断是否是第一次请求数据. true 表示第一次
	private boolean flag = true;
	// 新加数据 班型业务主键ID,班级业务 主键ID,教室业务主键ID
	private String sClassTypeId, sClassId, sTeacherId;
	// 投票
	private TextView vote_txtnLessonNo, vote_txtclassName;
	private Button vote_btn_sure, vote_btn_cancle;
	private ImageView vote_img_close;
	private String activeClassId;
	private String path;
	// 判断是否有投票
	private String checkVote;
	private String sName;
	private String NLessonNo;
	private String Subject;
	private String voteId;
	private String str_voting;
	// 判断是否参加了投票
	private String isVote;
	//判断是否老师结束了
	private boolean endtest = false;
	//判断是否投票是否结束了
	private boolean flag_vote = false;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_classroom);
		initview();
		getteacherinfo();
		getstudentinfo();
		stustate();
		clsassstate();
		 votedetail();
//		 votestate();
		 ExerciseStatus();
		ExitApplication.getInstance().addActivity(this);
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		getteacherinfo();
		getstudentinfo();
		gettestInfo();
		getGroupInfo();
		votedetail();
		super.onRestart();
	}

	/**
	 * 初始化控件
	 * 
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		mgridview = (GridView) findViewById(R.id.gridview_student_classroom);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		stuinfo = getSharedPreferences("stuinfo", Context.MODE_PRIVATE);
		txt_sClassCode = (TextView) findViewById(R.id.txt_num_student_classroom);
		txt_className = (TextView) findViewById(R.id.txt_name_student_classroom);
		txt_teacherName = (TextView) findViewById(R.id.txt_teachername_student_classroom);
		btn_nLessonNo = (TextView) findViewById(R.id.btn_classroom_student_classroom);
		// img_teacherIconUrl = (ImageView)
		// findViewById(R.id.img_classroom_student_classroom);
		txt_linenum = (TextView) findViewById(R.id.txt_studentcount_student_classroom);
		img_back = (ImageButton) findViewById(R.id.btn_back_ClassRoom_conncetion);
		img_back.setOnClickListener(this);
		txt_teachercontent = (TextView) findViewById(R.id.txt_count_student_classroom);
		activeClassId = share.getString("activeClassId", "");
		LodDialogClass.showCustomCircleProgressDialog(context, null, getString(R.string.common_Loading));
	}

	/**
	 * 获取自己组内信息 iphone、Android 学生App
	 */
	private void getGroupInfo() {
		// TODO Auto-generated method stub
		String url = Constants.URL_ActiveClass_loadMyGroup;
		activeClassId = share.getString("activeClassId", "");

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("分组" + result);
				try {

					/*
					 * remind = 1：已经提示过分组，0：未提示过分组，-1：没有分组,   sName = 班级名称,
					 *   nLessonNo = 正在上课课次,   GroupCnt = 分组数量,   GroupNum =
					 * 所在组号,   myGroup = 在线的学生列表信息[{IconUrl = 学生图像,    sName =
					 * 学生姓名,   }]
					 */

					JSONObject obj = new JSONObject(result);
					JSONObject data = obj.getJSONObject("Data");
					String remind = data.getString("remind");
					String sName = data.getString("sName");
					String nLessonNo = data.getString("nLessonNo");
					String GroupCnt = data.getString("GroupCnt");
					String GroupNum = data.getString("GroupNum");
					JSONArray myGroup = data.getJSONArray("myGroup");

					System.out.println("remind----" + remind);
					if (remind.equals("-1")) {
						System.out.println("分11111111111");
					} else if (remind.equals("1")) {
						System.out.println("分222222222222");
					} else if (remind.equals("0")) {
						List<HashMap<String, Object>> maplist = new ArrayList<HashMap<String, Object>>();
						for (int i = 0; i < myGroup.length(); i++) {
							HashMap<String, Object> map = new HashMap<String, Object>();
							JSONObject stuObj = myGroup.getJSONObject(i);
							map.put("IconUrl", stuObj.getString("IconUrl"));
							map.put("sName", stuObj.getString("sName"));
							maplist.add(map);
						}
						System.out.println("分33333333333333");
						showGroupDialog(sName, nLessonNo, GroupCnt, GroupNum,
								maplist);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 网络获取老师的信息
	 * 
	 */
	private void getteacherinfo() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		// code = intent.getStringExtra("code");
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("token==========" + share.getString("Token", ""));
		System.out.println("code==================="
				+ stuinfo.getString("code", ""));
		utils.send(HttpMethod.GET, Constants.URL_StudentGetStudentOnLine
				+ "?passCode=" + stuinfo.getString("code", ""), params,
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
							JSONObject obj1 = obj.getJSONObject("Data");
							JSONObject info = obj1
									.getJSONObject("lessonAndTeacherInfo");

							ccId = info.getString("ccId");
							txt_sClassCode.setText(info.getString("sClassCode"));
							txt_className.setText(info.getString("className"));
							txt_teacherName.setText(info
									.getString("teacherName"));
							// 设置超过就用....
							txt_teachercontent
									.setEllipsize(TextUtils.TruncateAt
											.valueOf("END"));
							if (UtiltyHelper.isEmpty(info.getString("Intro"))
									|| info.getString("Intro").equals("null")) {
								txt_teachercontent.setText("");
							} else {

								txt_teachercontent.setText(info
										.getString("Intro"));
							}
							btn_nLessonNo.setText("第"
									+ info.getString("nLessonNo") + "课");

							// 新加数据
							sClassTypeId = info.getString("sClassTypeId");
							sClassId = info.getString("sClassId");
							sTeacherId = info.getString("sTeacherId");

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out.println("tescher_result==================="
								+ result);
					}
				});

	}

	/**
	 * 网络获取学生列表的信息
	 * 
	 */
	private void getstudentinfo() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("token==========" + share.getString("Token", ""));
		System.out.println("code==================="
				+ stuinfo.getString("code", ""));
		utils.send(HttpMethod.GET,
				Constants.URL_TeacherOrStudentGetStudentOnLine + "?passCode="
						+ stuinfo.getString("code", "") + "&roleId=3", params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						LodDialogClass.closeCustomCircleProgressDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						System.out.println("student_result==================="
								+ result);
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject obj1 = obj.getJSONObject("Data");

							txt_linenum.setText("学员列表("
									+ obj1.getString("studentOnLineNum") + "/"
									+ obj1.getString("classStudentTotalNum")
									+ ")");
							JSONArray array = obj1.getJSONArray("studentList");
							mlist = new ArrayList<HashMap<String, Object>>();
							for (int i = 0; i < array.length(); i++) {
								JSONObject data = array.getJSONObject(i);
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("studentname",
										data.getString("studentname"));
								map.put("iconUrl", data.getString("iconUrl"));
								map.put("studentloginstatus",
										data.getString("studentloginstatus"));

								mlist.add(map);
							}
							if (flag == true) {
								madapter = new StudentClassAdapter(context,
										mlist);
								mgridview.setAdapter(madapter);
								flag = false;
							} else {
								madapter.setdatachanges(mlist);
								madapter.notifyDataSetChanged();
							}
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

	}

	/**
	 * 方法说明：3秒循环请求数据,看老师是否下课
	 * 
	 */
	private void clsassstate() {
		// TODO Auto-generated method stub
		clsassstatetimer = new Timer();
		TimerTask clsassstatetimertask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ActiveClassStatus();
			}
		};
		clsassstatetimer.schedule(clsassstatetimertask, 3000, 3000);

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
								clsassstatetimer.cancel();

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

							}else{
								endtest =false;
								alertDialog.dismiss();
								SharedPreferences share = getSharedPreferences("stuinfo",
										MODE_PRIVATE);
								SharedPreferences.Editor editor = share.edit();
								editor.putString("ccId", ccId);
								editor.putString("paperId", PaperID);
								editor.putString("PaperSubmitCountdown", PaperSubmitCountdown);
								editor.putString("activeClassPaperInfoId", activeClassPaperInfoId);
								editor.putInt("testtype", 1);
								editor.commit();
								intent = new Intent(StundentClassroom.this,
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

	/**
	 * 方法说明：心跳 学生端，获取随堂练习的课堂状态 Data = 当前随堂练习的课堂状态 0未上课 1上课中 2已下课(教师点了下课);
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
				+ "?passCode=" + stuinfo.getString("code", ""), params,
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
							state = obj.getInt("Data");
							// 已经下课
							if (state == 2) {
								class_ShowDiglog();
								if (clsassstatetimer != null) {

									clsassstatetimer.cancel();
								}
							} else {
								gettestInfo();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out
								.println("ActiveClassStatus_result==================="
										+ result);
					}
				});

	}

	/**
	 * 方法说明：学生端检测是否有投票iphone、Android 学生App 3秒一轮询
	 * 
	 */
	private void checkHasVote() {
		// TODO Auto-generated method stub
		// [Args]：
		//  activeClassId=[互动课堂ID，不可为空]
		// [Return]：
		//  Infomation = 提示信息;
		//  Result = 返回结果true/false;
		//  Data = {
		//   checkVote = 0没有投票，1有投票,
		//   isVote = 是否已投过票，0没有投，大于0以投，   voting = 有投票，只有checkVote为1时存在{,
		//    voteId = 投票定义ID,
		//    sName = 班级名称,
		//    NLessonNo = 当前课次号,
		//    Subject = 投票描述,
		//    voteOpts = 投票选项[{,
		//     OptionNum = 投票选项号,
		//     OptionDesc = 投票选项描述,
		//    }]
		//   }
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		System.out.println("activeClassId===" + activeClassId);
		HttpUtils util = new HttpUtils();
		util.configCurrentHttpCacheExpiry(0);
		util.configDefaultHttpCacheExpiry(0);
		path = Constants.URL_checkHasVote;
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("轮询是否有投票----" + result);
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject data = obj.getJSONObject("Data");
					checkVote = data.getString("checkVote");
					System.out.println("是否有投票=====checkVote" + checkVote
							+ "=====isVote=====" + isVote);
					isVote = data.getString("isVote");
					if (checkVote.equals("1") && isVote.equals("0")) {

						JSONObject voting = data.getJSONObject("voting");
						str_voting = data.getJSONObject("voting").toString();
						sName = voting.getString("sName");
						NLessonNo = voting.getString("NLessonNo");
						Subject = voting.getString("Subject");
						voteId = voting.getString("voteId");
						Editor ed = share.edit();
						ed.putString("voteId", voteId);
						ed.commit();
						JSONArray array1 = voting.getJSONArray("voteOpts");
						for (int i = 0; i < array1.length(); i++) {
							JSONObject voteOpts = array1.getJSONObject(i);
							String OptionNum = voteOpts.getString("OptionNum");
							String OptionDesc = voteOpts
									.getString("OptionDesc");
						}
						showVoteDialog();
						votetimer.cancel();

					} else{
						System.out.println("没有投票");
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 方法说明：学生端弃权投票iphone、Android 学生App
	 * 
	 */
	private void waiverVote() {
		// TODO Auto-generated method stub
		// [Args]：
		//  activeClassId=[互动课堂ID，不可为空]
		//  voteId=[投票定义ID，不可为空]
		// [Return]：
		//  Infomation = 提示信息;
		//  Result = 返回结果true/false;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		params.addBodyParameter("voteId", voteId);
		System.out.println("activeClassId===" + activeClassId
				+ "=====voteId====" + voteId);
		HttpUtils util = new HttpUtils();
		path = Constants.URL_waiverVote;
		util.configCurrentHttpCacheExpiry(0);
		util.configDefaultHttpCacheExpiry(0);
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("放弃投票的结果----" + result);

			}
		});

	}
	
	
	/**
	 * 方法说明：学生端心跳检测是否结束投票iphone、Android 学生App 3秒一轮询
	 * 
	 */
	private void loadMyVote() {
		// TODO Auto-generated method stub
		// [Args]：
		//  activeClassId=[互动课堂ID，不可为空]
		// [Return]：
		//  Infomation = 提示信息;
		//  Result = 返回结果true/false;
		//  Data = {
		//   flag = 标识，0表示未结束投票，1表示结束投票，根据标识来判断下面数据是否存在,
		//   voteResult = 投票结果，只有flag为1时存在[{,
		//    OptionNum = 投票选项号,
		//    OptionDesc = 投票选项描述,
		//    voteNum = 投票率,
		//    finishVote = 投票是否结束,true是，false否,
		//   }]
		//  }

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		params.addBodyParameter("voteId", voteId);
		System.out.println("activeClassId===" + activeClassId
				+ "=====voteId===" + voteId);
		HttpUtils util = new HttpUtils();
		util.configCurrentHttpCacheExpiry(0);
		util.configDefaultHttpCacheExpiry(0);
		path = Constants.URL_loadMyVote;
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("是否老师停止投票----" + result);
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject data = obj.getJSONObject("Data");
					String juestvotes = data.getString("flag");
					if (juestvotes.equals("1")) {
						// 老师结束了投票
						flag_vote = true;
						System.out
								.println("voteResult===结束的结果===" );
						Toast.makeText(context, "投票已经结束，请关闭提示框", Toast.LENGTH_SHORT).show();

					} else {
						flag_vote = false;
						// 正在投票
						System.out.println("正在投票  ======");
						if (votetimer != null) {
							votetimer.cancel();
						}
						votetimer = null;

						if (stutimer != null) {
							stutimer.cancel();// 程序退出时cancel timer
						}
						stutimer = null;
						if (testtimer != null) {
							testtimer.cancel();
						}
						testtimer = null;
						if (clsassstatetimer != null) {
							clsassstatetimer.cancel();
						}
						clsassstatetimer = null;
						vote_dialog.dismiss();
						vote_dialog = null;
						intent = new Intent(context, VoteContentActivity.class);
						intent.putExtra("str_voting", str_voting);
						startActivity(intent);
						finish();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
			alertDialog = new AlertDialog.Builder(StundentClassroom.this)
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
	 * 方法说明：弹出下课Dialog
	 * 
	 */
	private void class_ShowDiglog() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View myLoginView = layoutInflater.inflate(
				R.layout.waring_end_classroom, null);
		if (class_alertDialog == null) {
			class_alertDialog = new AlertDialog.Builder(StundentClassroom.this)
					.create();
			class_alertDialog.setView(myLoginView, 0, 0, 0, 30);
			class_alertDialog.getWindow().setGravity(Gravity.CENTER);
			class_alertDialog.setCanceledOnTouchOutside(false);
			class_alertDialog.setCancelable(false);
			class_alertDialog.show();
		}

		// class_alertDialog = new AlertDialog.Builder(this).create();
		// class_alertDialog.setView(myLoginView, 0, 0, 0, 30);
		// class_alertDialog.show();
		// class_alertDialog.getWindow().setGravity(Gravity.CENTER);
		// class_alertDialog.setCanceledOnTouchOutside(false);
		// class_alertDialog.setCancelable(false);
		txt_waring = (TextView) myLoginView
				.findViewById(R.id.waring_end_classroom_txt_testname);
		txt_waring.setText("老师已下课，请点击确定返回互动课堂首页！");
		btn_sure = (Button) myLoginView
				.findViewById(R.id.waring_end_classroom_btn_sure);
		// img_close = (ImageButton) myLoginView
		// .findViewById(R.id.waing_end_classroom_btn_close);
		btn_sure.setOnClickListener(this);
		// img_close.setOnClickListener(this);
	}

	/**
	 * 方法说明：弹出 老师发起投票 Dialog
	 * 
	 */
	private void showVoteDialog() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View myLoginView = layoutInflater.inflate(R.layout.diglog_vote_detail,
				null);

		if (vote_dialog == null) {

			vote_dialog = new AlertDialog.Builder(StundentClassroom.this)
					.create();
			vote_dialog.setView(myLoginView, 0, 0, 0, 30);
			vote_dialog.getWindow().setGravity(Gravity.CENTER);
			vote_dialog.setCanceledOnTouchOutside(false);
			vote_dialog.setCancelable(false);
			vote_dialog.show();
		} else {
			System.out.println("投票dialog不为空   不弹出dialog");
		}

		vote_btn_sure = (Button) myLoginView
				.findViewById(R.id.vote_connection_btn_sure);
		vote_btn_cancle = (Button) myLoginView
				.findViewById(R.id.vote_connection_btn_cancle);
		vote_img_close = (ImageView) myLoginView
				.findViewById(R.id.vote_connection_btn_close);
		vote_txtnLessonNo = (TextView) myLoginView
				.findViewById(R.id.vote_connection_txt_classnum);
		vote_txtclassName = (TextView) myLoginView
				.findViewById(R.id.vote_connection_txt_calssname);
		vote_txtclassName.setText(sName);
		vote_txtnLessonNo.setText("第" + NLessonNo + "课");
		vote_btn_sure.setOnClickListener(this);
		vote_btn_cancle.setOnClickListener(this);
		vote_img_close.setOnClickListener(this);
		// img_close.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View) 点击事件
	 * 的监听
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 开始答题-
		case R.id.waring_connection_btn_sure:
			
			ActiveClassExerciseStatus();
//			System.out.println("endtest =======是否结束答题===="+endtest);
//			if (endtest == true) {
//				Toast.makeText(context, "答题已经结束，请关闭提示框", Toast.LENGTH_SHORT).show();
//			}else{
//				alertDialog.dismiss();
//				SharedPreferences share = getSharedPreferences("stuinfo",
//						MODE_PRIVATE);
//				SharedPreferences.Editor editor = share.edit();
//				editor.putString("ccId", ccId);
//				editor.putString("paperId", PaperID);
//				editor.putString("PaperSubmitCountdown", PaperSubmitCountdown);
//				editor.putString("activeClassPaperInfoId", activeClassPaperInfoId);
//				editor.putInt("testtype", 1);
//				editor.commit();
//				intent = new Intent(StundentClassroom.this,
//						ConnetionStartTestActivity.class);
//				// type 1 ,老师推题
//				System.out.println("PaperSubmitCountdown====="
//						+ PaperSubmitCountdown);
//				startActivity(intent);
//				finish();
//			}
			
			break;
		// 关闭Diaglog
		case R.id.waing_connection_btn_close:
			alertDialog.dismiss();
			alertDialog = null;
			clsassstate();
			// alertDialog.show();
			// clsassstate();
			// timer();
			break;
		case R.id.btn_back_ClassRoom_conncetion:
			finish();
			break;
		case R.id.waring_end_classroom_btn_sure:
			// intent = new
			// Intent(StundentClassroom.this,User_interact_classActivity.class);
			// startActivity(intent);
			finish();
			break;

		// 确定 跳转到投票的详情页面
		case R.id.vote_connection_btn_sure:
		
			loadMyVote();
			
			break;
		// 点击投票 弃权
		case R.id.vote_connection_btn_cancle:
			vote_dialog.dismiss();
			waiverVote();
			vote_dialog = null;
			break;
		// 点击投票 小X图片
		case R.id.vote_connection_btn_close:
			vote_dialog.dismiss();
			vote_dialog = null;
			votedetail();
			
			break;

		// 分组确认按钮
		case R.id.waing_choose_classes_btn_sure:
			groupdialog.dismiss();
			groupdialog=null;
			break;
		default:
			break;
		}
	}

	/**
	 * 方法说明：3秒 判断是否老师发起了投票
	 * 
	 */
	private void votedetail() {
		// TODO Auto-generated method stub
		votetimer = new Timer();
		TimerTask stutask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				checkHasVote();
			}
		};
		votetimer.schedule(stutask, 3000, 3000);
	}

	 /**
	 * 3秒循环判断老师是否结束了
	 *
	 */
	 private void ExerciseStatus() {
	
	 testtimer = new Timer();
	 TimerTask testtask = new TimerTask() {
	
	 @Override
	 public void run() {
	 // TODO Auto-generated method stub
		 	
//		 ActiveClassExerciseStatus();
	
	 	}
	 };
	 
	 testtimer.schedule(testtask, 3000, 3000);
	 
	 }

	/**
	 * 方法说明：3秒获取学生状态
	 * 
	 */
	private void stustate() {
		// TODO Auto-generated method stub
		stutimer = new Timer();
		TimerTask stutask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getstudentinfo();
				 getGroupInfo();
			}
		};
		stutimer.schedule(stutask, 3000, 3000);
	}
	
//	/**
//	 * 方法说明：3秒获取投票的状态
//	 * 
//	 */
//	private void votestate() {
//		// TODO Auto-generated method stub
//		votestatetimer = new Timer();
//		TimerTask stutask = new TimerTask() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				
//				loadMyVote();
//			}
//		};
//		votestatetimer.schedule(stutask, 3000, 3000);
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (votestatetimer != null) {
			votestatetimer.cancel();
			votestatetimer = null;
		}
		if (testtimer != null) {
			testtimer.cancel();
			testtimer = null;
		}

		if (stutimer != null) {
			stutimer.cancel();// 程序退出时cancel timer
			stutimer = null;
		}

		if (testtimer != null) {
			testtimer.cancel();
			testtimer = null;
		}
		if (clsassstatetimer != null) {
			clsassstatetimer.cancel();
			clsassstatetimer = null;
		}
		System.out.println("页面停止==========");
		super.onStop();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		if (votestatetimer != null) {
			votestatetimer.cancel();
			votestatetimer = null;
		}
		if (testtimer != null) {
			testtimer.cancel();
			testtimer = null;
		}

		if (votetimer != null) {
			votetimer.cancel();
		}
		votetimer = null;

		if (votetimer != null) {
			votetimer.cancel();
		}
		votetimer = null;

		if (stutimer != null) {
			stutimer.cancel();// 程序退出时cancel timer
		}
		stutimer = null;
		if (testtimer != null) {
			testtimer.cancel();
		}
		testtimer = null;
		if (clsassstatetimer != null) {
			clsassstatetimer.cancel();
		}
		clsassstatetimer = null;

		System.out.println("页面销毁==============");
		super.onDestroy();
	}

	// 学生端分组弹窗
	public void showGroupDialog(String sName, String nLessonNo,
			String GroupCnt, String GroupNum,
			List<HashMap<String, Object>> maplist) {
		System.out.println("进入--" + maplist);
		View v = LayoutInflater.from(context).inflate(
				R.layout.stu_group_dialog, null);
		if (groupdialog == null) {
			groupdialog = new AlertDialog.Builder(context).create();
			groupdialog.setView(v, 0, 0, 0, 30);
			groupdialog.getWindow().setGravity(Gravity.CENTER);
			groupdialog.setCanceledOnTouchOutside(false);
			groupdialog.setCancelable(false);
			groupdialog.show();
		}
		GridView gridview = (GridView) v
				.findViewById(R.id.waring_gridview_choose_classes);
		DialogAdapter logadapter = new DialogAdapter(context, maplist);
		gridview.setAdapter(logadapter);
		TextView txt_tishi1 = (TextView) v.findViewById(R.id.txt_tishi1);
		txt_tishi1.setText(sName + "  第" + nLessonNo + "课");

		TextView txt_tishi2 = (TextView) v.findViewById(R.id.txt_tishi2);
		txt_tishi2.setText("课上分组练习分组结果：第" + GroupNum + "组(" + maplist.size()
				+ ") 共" + GroupCnt + "组");
		Button waing_choose_classes_btn_sure = (Button) v
				.findViewById(R.id.waing_choose_classes_btn_sure);
		waing_choose_classes_btn_sure.setOnClickListener(this);

	}

}
