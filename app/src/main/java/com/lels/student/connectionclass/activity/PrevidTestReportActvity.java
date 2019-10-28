/*******************************************************************************
+
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月23日 
 * 
 *******************************************************************************/
package com.lels.student.connectionclass.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.history.ExerciseReport;
import com.history.Groups;
import com.history.HisGroups;
import com.history.HisVotes;
import com.history.History;
import com.history.InClass;
import com.history.OutClass;
import com.history.StudentOwnHistoryExerciseReport;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.student.connectionclass.adapter.PrevidTestReportAdapter;
import com.lels.student.main.fragment.User_interact_classActivity;
import com.lelts.tool.RoundProgressBar;
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
import android.support.v7.internal.widget.ActivityChooserModel.HistoricalRecord;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年7月23日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class PrevidTestReportActvity extends Activity implements
		OnClickListener {
	private Context context;
	private ImageButton img_back;
	private SharedPreferences share;
	private Intent intent;
	private RoundProgressBar true_pro2, true_pro3, time_pro3;
	private int progress;
	private String className, nLessonNo, exIds, ccId;
	private String paperName, Accuracy, CostTime;
	private ListView mlistview;
	private PrevidTestReportAdapter madapter;
	private List<History> Historylist;
	private TextView txt_classname, txt_nlesson;

	// "Data": {
	//   "className": "班级名称",
	//   "nLessonNo": 课次号,
	//   "exerciseReport": [
	//    {
	//     "paperName": "试卷名称",
	//     "Accuracy": "正确率",
	//     "CostTime": "答题时间"
	//    }
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_previod_test_report);
		initview();
		findStudentOwnHistoryExerciseReport();

	}

	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		txt_classname = (TextView) findViewById(R.id.txt_classname_provoid_report);
		txt_nlesson = (TextView) findViewById(R.id.txt_classcode_provoid_report);
		img_back = (ImageButton) findViewById(R.id.img_back_prevoid_report);
		img_back.setOnClickListener(this);
		true_pro2 = (RoundProgressBar) findViewById(R.id.roundProgressBar2_true_prevoid);
		true_pro3 = (RoundProgressBar) findViewById(R.id.roundProgressBar3_true_prevoid);
		time_pro3 = (RoundProgressBar) findViewById(R.id.roundProgressBar3_time_prevoid);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		Intent getinfo = getIntent();
		ccId = getinfo.getStringExtra("CC_ID");
		className = getinfo.getStringExtra("className");
		nLessonNo = getinfo.getStringExtra("nLessonNo");
		exIds = getinfo.getStringExtra("exIds");
		mlistview = (ListView) findViewById(R.id.listview_prevoid_report);
		Historylist = new ArrayList<History>();
		LodDialogClass.showCustomCircleProgressDialog(context, null, getString(R.string.common_Loading));
	}

	// /**
	// * 方法说明：初始化数据
	// *
	// */
	// private void initdata() {
	// // TODO Auto-generated method stub
	//
	// }

	/**
	 * 方法说明：获取学员自己的历史随堂练习列表
	 *
	 */
	private void findStudentOwnHistoryExerciseReport() {
		// TODO Auto-generated method stub
		System.out.println("参数==============》" + className + "," + nLessonNo
				+ "," + exIds);
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("token==========" + share.getString("Token", ""));
		utils.send(HttpMethod.GET,
				Constants.URL_findStudentOwnHistoryExerciseReport + "?ccId="
						+ ccId + "&className=" + className + "&nLessonNo="
						+ nLessonNo + "&exIds=" + exIds, params,
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

						/*
						 * try { InputStreamReader inputReader = new
						 * InputStreamReader(
						 * getResources().getAssets().open("Word"));
						 * BufferedReader bufReader = new
						 * BufferedReader(inputReader); String line = ""; result
						 * = ""; while ((line = bufReader.readLine()) != null)
						 * result += line; System.out.println("json===>" +
						 * result); } catch (IOException e) {
						 * System.out.println("json error===>" +
						 * e.getMessage()); e.printStackTrace(); }
						 */

						txt_classname.setText(className);
						txt_nlesson.setText("第" + nLessonNo + "课");
						// String result = arg0.result;
						System.out.println(result);
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject data = obj.getJSONObject("Data");
							// 答题
							if (data.has("studentOwnHistoryExerciseReport")) {
								JSONObject studentOwnHistoryExerciseReport = data
										.getJSONObject("studentOwnHistoryExerciseReport");
								if (studentOwnHistoryExerciseReport.has("exerciseReport")) {
									JSONArray exerciseReport = studentOwnHistoryExerciseReport
											.getJSONArray("exerciseReport");
									for (int i = 0; i < exerciseReport.length(); i++) {
										JSONObject info = exerciseReport
												.getJSONObject(i);
										CostTime = info.getString("CostTime");
										paperName = info.getString("paperName");
										Accuracy = info.getString("Accuracy");
										StudentOwnHistoryExerciseReport stuPort = new StudentOwnHistoryExerciseReport(
												paperName, Accuracy, CostTime);
										Historylist.add(stuPort);
									}
								}
								
								System.out.println("1"+Historylist.toString());
							}
							// 投票
							if (data.has("hisVotes")) {

								JSONObject hisVotes = data
										.getJSONObject("hisVotes");
								String hisVoteSize = hisVotes
										.getString("hisVoteSize");//
								if (Integer.valueOf(hisVoteSize) > 0) {
									JSONArray hisVotess = hisVotes
											.getJSONArray("hisVotes");
									for (int i = 0; i < hisVotess.length(); i++) {
										JSONObject outObj = hisVotess
												.getJSONObject(i);
										String ID = outObj.getString("ID");
										String ActiveClassID = outObj
												.getString("ActiveClassID");
										String Subject = outObj
												.getString("Subject");
										JSONArray opts = outObj
												.getJSONArray("opts");
										List<InClass> inList = new ArrayList<InClass>();
										for (int j = 0; j < opts.length(); j++) {
											JSONObject inObj = opts
													.getJSONObject(j);
											String OptionNum = inObj
													.getString("OptionNum");// 投票选项号,
											String OptionDesc = inObj
													.getString("OptionDesc");// 投票选项描述,
											String voteNum = inObj
													.getString("voteNum");// 投票率,
											String ownVote = inObj
													.getString("ownVote");//
											String finishVote = inObj
													.getString("finishVote");// 投票是否结束,true是，false否,
											inList.add(new InClass(OptionNum,
													OptionDesc, voteNum,
													ownVote, finishVote));
										}

										Historylist.add(new HisVotes(ID,
												ActiveClassID, Subject,
												inList));
										System.out.println("2"+Historylist.toString());
									}
								}
							}

							// 分组
							if (data.has("hisGroups")) {

								JSONObject hisGroups = data
										.getJSONObject("hisGroups");
								String remind = hisGroups.getString("remind");//
								if (remind.equals("1")) {
									String sName = hisGroups.getString("sName");// 班级名称,
									String nLessonNo = hisGroups
											.getString("nLessonNo");// 正在上课课次,
									String GroupCnt = hisGroups
											.getString("GroupCnt");// 分组数量,
									String GroupNum = hisGroups
											.getString("GroupNum");//
									JSONArray myGroup = hisGroups
											.getJSONArray("myGroup");
									List<Groups> group = new ArrayList<Groups>();
									for (int i = 0; i < myGroup.length(); i++) {
										HashMap<String, Object> map = new HashMap<String, Object>();
										JSONObject groupObj = myGroup
												.getJSONObject(i);
										String IconUrl = groupObj
												.getString("IconUrl");// 学生图像,
										String stuName = groupObj
												.getString("sName");//
										group.add(new Groups(IconUrl, stuName));
									}
									HisGroups hisgroup = new HisGroups(sName,
											nLessonNo, GroupCnt, GroupNum,group);
									Historylist.add(hisgroup);
									
								}
							}
							System.out.println("3"+Historylist.toString());
							madapter = new PrevidTestReportAdapter(context,
									Historylist);
							mlistview.setAdapter(madapter);
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out
								.println("findStudentOwnHistoryExerciseReport_result==================="
										+ result);
					}
				});
	}

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) { // pre-condition return; }

			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}

			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 各个事件的点击监听
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 返回键
		case R.id.img_back_prevoid_report:
			// intent = new Intent(PrevidTestReportActvity.this,
			// User_interact_classActivity.class);
			// startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

}
