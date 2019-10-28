package com.lelts.student.myself;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lelts.student.myself.adapter.PopGroupAdapter;
import com.lelts.student.myself.adapter.PopGroupListAdapter;
import com.lelts.tool.PrintTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.widget.time.JudgeDate;
import com.widget.time.ScreenInfo;
import com.widget.time.WheelMain;

public class MystudentTargetActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "MystudentTargetActivity";

	private ImageView imageView_back;
	private RelativeLayout relative_select_testtype;// 选择 考试类型
	private RelativeLayout relative_select_applytostudyabroad;// 留学申请日期
	private RelativeLayout relative_select_testtime;// 考试时间

	private TextView textview_data;
	private TextView textview_testtime;
	// falg 来判断是否可以点击修改 true可以修改
	private boolean flag = false;
	private int i;
	// 设置 时间选择 器的选择
	WheelMain wheelMain;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private String token;

	private PrintTool print;

	// 成绩 最近 显示 组件的 生成
	private TextView textview_target_rtotalscore, textview_target_rlistening,
			textview_target_rspeaking, textview_target_rreading,
			textview_target_rwriting;
	private TextView textview_target_mtotalscore, textview_target_mlistening,
			textview_target_mspeaking, textview_target_mreading,
			textview_target_mwriting;

	private TextView textview_target_examtype;
	private TextView textview_target_alerdata;

	private HashMap<String, Object> map_myresult = new HashMap<String, Object>();// my
																					// result
	private List<HashMap<String, Object>> l_map_testtime = new ArrayList<HashMap<String, Object>>();// test
																									// list
	private HashMap<String, Object> map_studyabroad_applytime;// study abroad
																// appley times
	// 设置 分数的 选择 group
	private List<String> groups;
	private List<String> l_test_type;
	// private List<HashMap<String, Object>> l_test_time;

	// 设置 popup
	// pop的声明
	private PopupWindow pop_class;
	private ListView lv_group;
	private View view;

	private PopupWindow pop_testtype;
	private PopupWindow pop_testtime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_mytarget);
		print = new PrintTool(MystudentTargetActivity.this);
		getdatafromshare();

		init();
		initpopupdata();
		// initdata();
		getdatafromnet();
	}

	private void initpopupdata() {
		// 分数 的 加载数据
		groups = new ArrayList<String>();

		groups.add("0.0");
		groups.add("1.0");
		groups.add("1.5");
		groups.add("2.0");
		groups.add("2.5");
		groups.add("3.0");
		groups.add("3.5");
		groups.add("4.0");
		groups.add("4.5");
		groups.add("5.0");
		groups.add("5.5");
		groups.add("6.0");
		groups.add("6.5");
		groups.add("7.0");
		groups.add("7.5");
		groups.add("8.0");
		groups.add("8.5");
		groups.add("9.0");

		l_test_type = new ArrayList<String>();
		l_test_type.add("学术类");
		l_test_type.add("培训类");

	}

	private void getdatafromshare() {
		SharedPreferences share = this.getSharedPreferences("userinfo",
				MODE_PRIVATE);
		token = share.getString("Token", "");
		print.printforLog(TAG, token);
	}

	private void init() {
		imageView_back = (ImageView) findViewById(R.id.imageView_back);

		// mu biao chengji
		textview_target_rtotalscore = (TextView) findViewById(R.id.textview_target_rtotalscore);// 当前总分
		textview_target_rlistening = (TextView) findViewById(R.id.textview_target_rlistening);// 听力
		textview_target_rspeaking = (TextView) findViewById(R.id.textview_target_rspeaking);
		textview_target_rreading = (TextView) findViewById(R.id.textview_target_rreading);
		textview_target_rwriting = (TextView) findViewById(R.id.textview_target_rwriting);
		// dangqian chengji
		textview_target_mtotalscore = (TextView) findViewById(R.id.textview_target_mtotalscore);
		textview_target_mlistening = (TextView) findViewById(R.id.textview_target_mlistening);
		textview_target_mspeaking = (TextView) findViewById(R.id.textview_target_mspeaking);
		textview_target_mreading = (TextView) findViewById(R.id.textview_target_mreading);
		textview_target_mwriting = (TextView) findViewById(R.id.textview_target_mwriting);
		// kaoshileixing
		textview_target_examtype = (TextView) findViewById(R.id.textview_target_examtype);
		// dianji xiugai
		textview_target_alerdata = (TextView) findViewById(R.id.textview_target_alerdata);

		relative_select_testtype = (RelativeLayout) findViewById(R.id.relative_select_testtype);
		relative_select_applytostudyabroad = (RelativeLayout) findViewById(R.id.relative_select_applytostudyabroad);
		relative_select_testtime = (RelativeLayout) findViewById(R.id.relative_select_testtime);

		textview_data = (TextView) findViewById(R.id.textview_data);
		textview_testtime = (TextView) findViewById(R.id.textview_testtime);

		imageView_back.setOnClickListener(this);

		relative_select_testtype.setOnClickListener(this);
		relative_select_applytostudyabroad.setOnClickListener(this);
		relative_select_testtime.setOnClickListener(this);

		textview_target_alerdata.setOnClickListener(this);

		// 修改我的目标成绩
		textview_target_rlistening.setOnClickListener(this);
		textview_target_rspeaking.setOnClickListener(this);
		textview_target_rreading.setOnClickListener(this);
		textview_target_rwriting.setOnClickListener(this);

		textview_target_examtype.setOnClickListener(this);

	}

	@SuppressWarnings("static-access")
	private void getdatafromnet() {

		String url = new Constants().URL_MYSELF_MYTARGETSPAGE;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(0);
		http.configDefaultHttpCacheExpiry(0);
		// http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							System.out.println("解析出我的目标----"
									+ responseInfo.result);
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							// String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							JSONObject obj = new JSONObject(Data);
							// 解析data数据 scoreAndTypeMap
							String scoreAndTypeMap = obj
									.getString("scoreAndTypeMap");// current
																	// result
																	// and old
																	// result
							String kssjList = obj.getString("kssjList");// test
																		// times
																		// list
							String lxsqMap = obj.getString("lxsqMap");// study
																		// abroad
																		// apply
																		// time

							JSONObject obj_score = new JSONObject(
									scoreAndTypeMap);
							if (!obj_score.equals(null)) {

								// "MyRead": 5.5,
								// "RWrite": 7.5,
								// "RRead": 7.5,
								// "ModifiedBy": null,

								// "RTotalScore": 7.5,
								// "ExamType": 1,
								// "MyWrite": 6.5,
								// "ModifyTime": null,

								// "MySpeak": 6,
								// "MyTotalScore": 6,
								// "RSpeak": 7.5,
								// "IsActive": null,

								// "RListen": 7.5,
								// "UID": 38,
								// "CreateTime": null,
								// "ID": 2,

								// "CreatedBy": null,
								// "MyListen": 6.5
								map_myresult.put("MyListen",
										obj_score.getString("MyListen"));
								map_myresult.put("MyRead",
										obj_score.getString("MyRead"));
								map_myresult.put("MySpeak",
										obj_score.getString("MySpeak"));
								map_myresult.put("MyWrite",
										obj_score.getString("MyWrite"));

								map_myresult.put("RWrite",
										obj_score.getString("RWrite"));
								map_myresult.put("RRead",
										obj_score.getString("RRead"));
								map_myresult.put("ModifiedBy",
										obj_score.getString("ModifiedBy"));
								map_myresult.put("RTotalScore",
										obj_score.getString("RTotalScore"));
								map_myresult.put("ExamType",
										obj_score.getString("ExamType"));

								map_myresult.put("ModifyTime",
										obj_score.getString("ModifyTime"));

								map_myresult.put("MyTotalScore",
										obj_score.getString("MyTotalScore"));
								map_myresult.put("RSpeak",
										obj_score.getString("RSpeak"));
								map_myresult.put("IsActive",
										obj_score.getString("IsActive"));

								map_myresult.put("RListen",
										obj_score.getString("RListen"));
								map_myresult.put("UID",
										obj_score.getString("UID"));
								map_myresult.put("CreateTime",
										obj_score.getString("CreateTime"));
								map_myresult.put("ID",
										obj_score.getString("ID"));
								map_myresult.put("CreatedBy",
										obj_score.getString("CreatedBy"));

							}

							// "Name": "考试",
							// "UID": 38,
							// "IsDelete": false,
							// "TDate_ID": 1058,
							// "UpdateTime": 1437474054000,
							// "TaskState": 0,
							// "DestDate": "2015-07-21",
							// "DateTypeID": 1,
							// "TaskIDs": "0"
							l_map_testtime = new ArrayList<HashMap<String, Object>>();
							JSONArray array_ksslist = new JSONArray(kssjList);
							if (array_ksslist.length() > 0) {
								for (int i = 0; i < array_ksslist.length(); i++) {
									JSONObject obj_k = array_ksslist
											.optJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("Name", obj_k.getString("Name"));
									map.put("UID", obj_k.getString("UID"));
									map.put("IsDelete",
											obj_k.getString("IsDelete"));
									map.put("TDate_ID",
											obj_k.getString("TDate_ID"));
									map.put("UpdateTime",
											obj_k.getString("UpdateTime"));
									map.put("TaskState",
											obj_k.getString("TaskState"));
									map.put("DestDate",
											obj_k.getString("DestDate"));
									map.put("DateTypeID",
											obj_k.getString("DateTypeID"));
									map.put("TaskIDs",
											obj_k.getString("TaskIDs"));
									l_map_testtime.add(map);
								}
							}

							// "Name": "留学申请",
							// "UID": 38,
							// "IsDelete": false,
							// "TDate_ID": 1056,

							// "UpdateTime": 1437559629000,
							// "TaskState": 0,
							// "DestDate": "2015-07-24",
							// "DateTypeID": 2,
							// "TaskIDs": "0"

							map_studyabroad_applytime = new HashMap<String, Object>();
							// lxsqMap json
							JSONObject obj_lxsq = new JSONObject(lxsqMap);
							if (obj_lxsq!=null&&obj_lxsq.length()!=0) {
								map_studyabroad_applytime.put("Name",
											obj_lxsq.getString("Name"));
								map_studyabroad_applytime.put("UID",
										obj_lxsq.getString("UID"));
								map_studyabroad_applytime.put("IsDelete",
										obj_lxsq.getString("IsDelete"));
								map_studyabroad_applytime.put("TDate_ID",
										obj_lxsq.getString("TDate_ID"));
								map_studyabroad_applytime.put("UpdateTime",
										obj_lxsq.getString("UpdateTime"));
								map_studyabroad_applytime.put("TaskState",
										obj_lxsq.getString("TaskState"));
								map_studyabroad_applytime.put("DestDate",
										obj_lxsq.getString("DestDate"));
								map_studyabroad_applytime.put("DateTypeID",
										obj_lxsq.getString("DateTypeID"));
								map_studyabroad_applytime.put("TaskIDs",
										obj_lxsq.getString("TaskIDs"));
								// 设置 留学申请 日期
								textview_data.setText(map_studyabroad_applytime.get("DestDate")
										.toString());
							}
							setdata();

							// map_myresult
							// l_map_testtime
							// map_studyabroad_applytime

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.d(TAG, error.toString());
					}

				});

	}

	protected void setdata() {

		// 保留一位小数
		DecimalFormat df = new DecimalFormat("0.0");
		// 目标成绩的设置
		if (map_myresult.get("RTotalScore").toString().equals("null")) {
			textview_target_mtotalscore.setText(0.0+"");
		}
		textview_target_mtotalscore.setText(df.format(Double
				.valueOf((map_myresult.get("RTotalScore").toString()))));// 总分

		textview_target_mlistening.setText(df.format(Double
				.valueOf((map_myresult.get("RListen").toString()))));
		textview_target_mspeaking.setText(df.format(Double
				.valueOf((map_myresult.get("RSpeak").toString()))));
		textview_target_mreading.setText(df.format(Double.valueOf((map_myresult
				.get("RRead").toString()))));
		textview_target_mwriting.setText(df.format(Double.valueOf((map_myresult
				.get("RWrite").toString()))));

		// 设置当前的成绩

		textview_target_rtotalscore.setText(df.format(Double
				.valueOf((map_myresult.get("MyTotalScore").toString()))));// 总分

		textview_target_rlistening.setText(df.format(Double
				.valueOf((map_myresult.get("MyListen").toString()))));
		textview_target_rspeaking.setText(df.format(Double
				.valueOf((map_myresult.get("MySpeak").toString()))));
		textview_target_rreading.setText(df.format(Double.valueOf((map_myresult
				.get("MyRead").toString()))));
		textview_target_rwriting.setText(df.format(Double.valueOf((map_myresult
				.get("MyWrite").toString()))));

		// 设置 考试类别
		System.out
				.println("考试类别====" + map_myresult.get("ExamType").toString());
		if (map_myresult.get("ExamType").toString().equals("1")) {
			textview_target_examtype.setText("学术类");
		} else {
			textview_target_examtype.setText("培训类");
		}

	
		// 设置 考试时间
		if (l_map_testtime.size() > 0) {
			textview_testtime.setText(l_map_testtime.get(0).get("DestDate")
					.toString());
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back:
			finish();
			break;
		case R.id.relative_select_testtype:// 选择 考试 类型
			// Toast.makeText(MystudentTargetActivity.this, "选择考试类型",
			// Toast.LENGTH_SHORT).show();
			break;
		// 留学申请日期
		case R.id.relative_select_applytostudyabroad: // study abroad data
														// seting
			LayoutInflater inflater = LayoutInflater
					.from(MystudentTargetActivity.this);
			final View timepickerview = inflater.inflate(R.layout.timepicker,
					null);
			ScreenInfo screenInfo = new ScreenInfo(MystudentTargetActivity.this);
			wheelMain = new WheelMain(timepickerview);
			wheelMain.screenheight = screenInfo.getHeight();
			String time = textview_data.getText().toString();
			Calendar calendar = Calendar.getInstance();
			if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
				try {
					calendar.setTime(dateFormat.parse(time));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			wheelMain.initDateTimePicker(year, month, day);
			new AlertDialog.Builder(MystudentTargetActivity.this)
					.setTitle("选择时间")
					.setView(timepickerview)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									textview_data.setText(wheelMain.getTime());

									// 设置 留学申请日期
									setstudyabroadtime("2", wheelMain.getTime());

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			break;
		// 设置考试时间
		case R.id.relative_select_testtime:
			LayoutInflater inflater1 = LayoutInflater
					.from(MystudentTargetActivity.this);
			final View timepickerview1 = inflater1.inflate(R.layout.timepicker,
					null);
			ScreenInfo screenInfo1 = new ScreenInfo(
					MystudentTargetActivity.this);
			wheelMain = new WheelMain(timepickerview1);
			wheelMain.screenheight = screenInfo1.getHeight();
			String time1 = textview_testtime.getText().toString();
			Calendar calendar1 = Calendar.getInstance();
			if (JudgeDate.isDate(time1, "yyyy-MM-dd")) {
				try {
					calendar1.setTime(dateFormat.parse(time1));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			int year1 = calendar1.get(Calendar.YEAR);
			int month1 = calendar1.get(Calendar.MONTH);
			int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
			wheelMain.initDateTimePicker(year1, month1, day1);
			new AlertDialog.Builder(MystudentTargetActivity.this)
					.setTitle("选择时间")
					.setView(timepickerview1)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									textview_testtime.setText(wheelMain
											.getTime());
									// 设置考试
									setstudyabroadtime("1", wheelMain.getTime());
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			// showpopfortesttime(textview_testtime, l_map_testtime);
			break;
		// 修改 目标成绩
		case R.id.textview_target_alerdata:

			if (i % 2 == 0) {
				textview_target_alerdata.setText("完成");
				flag = true;
			} else {

				flag = false;
				textview_target_alerdata.setText("修改");
				setupdatastudentsetting();
			}
			i++;
			break;
		// textview_target_rspeaking,
		// textview_target_rreading,textview_target_rwriting
		case R.id.textview_target_rlistening:
			if (flag) {
				showpop(textview_target_rlistening, groups);
			}

			break;
		case R.id.textview_target_rspeaking:
			if (flag) {

				showpop(textview_target_rspeaking, groups);
			}
			break;
		case R.id.textview_target_rreading:
			if (flag) {

				showpop(textview_target_rreading, groups);
			}
			break;
		case R.id.textview_target_rwriting:
			if (flag) {

				showpop(textview_target_rwriting, groups);
			}
			break;
		case R.id.textview_target_examtype:
			// showpop(textview_target_examtype, l_test_type);
			// showpopfortesttype(textview_target_examtype, l_test_type);
			// 跳转到下一个页面 进行选择
			Intent intent = new Intent();
			intent.setClass(MystudentTargetActivity.this,
					MystudentTargetAlterTestTypeActivity.class);
			startActivityForResult(intent, 0);

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		if (resultCode == 0) {

			Bundle b = data.getExtras();
			String ss = b.getString("testtype");
			textview_target_examtype.setText(ss);

		}

	}

	/**
	 * 学生端设置-我的目标-新增我设定的各种日期iphone、Android 学生App [Args]： dateTypeId=[类别(1考试
	 * 2留学申请)]  destDate=[设置的日期] get
	 * **/

	@SuppressWarnings("static-access")
	protected void setstudyabroadtime(String dateTypeId, String destdate) {

		Log.d(TAG, "出国留学时间" + "getdatafromnet()");

		String url = new Constants().URL_MYSELF_TAEGET_ADDTARGETDATE
				+ "?dateTypeId=" + dateTypeId + "&destDate=" + destdate;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.GET, url, params,
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
								Toast.makeText(MystudentTargetActivity.this,
										"设置成功", Toast.LENGTH_SHORT).show();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println(error.toString());

					}

				});
	}

	private void showpop(final TextView mtextView, final List<String> l_test) {
		if (pop_class == null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.pop_myself_testreport, null);
			lv_group = (ListView) view
					.findViewById(R.id.pop_listview_myself_result_class);

			PopGroupAdapter groupAdapter = new PopGroupAdapter(this, l_test);
			lv_group.setAdapter(groupAdapter);
			// 创建一个PopuWidow对象
			pop_class = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		// 使其聚集
		pop_class.setFocusable(true);
		// 设置允许在外点击消失
		pop_class.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		pop_class.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- pop_class.getWidth() / 2;
		Log.i("coder", "xPos:" + xPos);

		// pop_class.showAsDropDown(mtextView, xPos, 0);
		pop_class.showAsDropDown(mtextView);

		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

//				Toast.makeText(MystudentTargetActivity.this,
//						l_test.get(position).toString(), 1000).show();
				mtextView.setText(l_test.get(position).toString());

				if (pop_class != null) {
					pop_class.dismiss();
				}
			}
		});
	}

	/**
	 * 更改目标成绩 学生端设置-我的目标-更新学生设定的目标成绩iphone、Android 学生App [Auth]：token [URL]：
	 * /Home/UpdateStudentSettings [Method]：GET [Args]：  lisent=[听力分数]
	 *  speak=[口语分数]  read=[阅读分数]  write=[写作分数]
	 **/
	@SuppressWarnings("static-access")
	protected void setupdatastudentsetting() {

		// textview_target_rlistening,
		// textview_target_rspeaking, textview_target_rreading,
		// textview_target_rwriting;

		String listening = textview_target_rlistening.getText().toString();
		String speaking = textview_target_rspeaking.getText().toString();
		String reading = textview_target_rreading.getText().toString();
		String wtiting = textview_target_rwriting.getText().toString();

		String url = new Constants().URL_MYSELF_TARGET_UPDATESTUDENTSETTINGS
				+ "?lisent=" + listening + "&speak=" + speaking + "&read="
				+ reading + "&write=" + wtiting;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.GET, url, params,
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
								Toast.makeText(MystudentTargetActivity.this,
										"设置成功", Toast.LENGTH_SHORT).show();
								JSONObject obj = new JSONObject(Data);
								String floatTotalScore = obj
										.getString("floatTotalScore");
								// 保留一位小数
								DecimalFormat df = new DecimalFormat("0.0");
								textview_target_rtotalscore.setText(df.format(Double.valueOf(floatTotalScore)));// 总分
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println(error.toString());

					}

				});
	}

	// pop_testtype
	private void showpopfortesttype(final TextView mtextView,
			final List<String> l_test) {
		if (pop_testtype == null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.pop_myself_testreport, null);
			lv_group = (ListView) view
					.findViewById(R.id.pop_listview_myself_result_class);

			PopGroupAdapter groupAdapter = new PopGroupAdapter(this, l_test);
			lv_group.setAdapter(groupAdapter);
			// 创建一个PopuWidow对象
			pop_testtype = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		// 使其聚集
		pop_testtype.setFocusable(true);
		// 设置允许在外点击消失
		pop_testtype.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		pop_testtype.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- pop_testtype.getWidth() / 2;
		Log.i("coder", "xPos:" + xPos);

		// pop_class.showAsDropDown(mtextView, xPos, 0);
		pop_testtype.showAsDropDown(mtextView);

		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

//				Toast.makeText(MystudentTargetActivity.this,
//						l_test.get(position).toString(), 1000).show();
				mtextView.setText(l_test.get(position).toString());

				if (pop_testtype != null) {
					pop_testtype.dismiss();
				}
			}
		});
	}

	private void showpopfortesttime(final TextView mtextView,
			final List<HashMap<String, Object>> l_map_testtime) {
		if (pop_testtime == null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.pop_myself_testreport, null);
			lv_group = (ListView) view
					.findViewById(R.id.pop_listview_myself_result_class);

			PopGroupListAdapter groupAdapter = new PopGroupListAdapter(this,
					l_map_testtime);
			lv_group.setAdapter(groupAdapter);
			// 创建一个PopuWidow对象
			pop_testtime = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		// 使其聚集
		pop_testtime.setFocusable(true);
		// 设置允许在外点击消失
		pop_testtime.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		pop_testtime.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- pop_testtime.getWidth() / 2;
		Log.i("coder", "xPos:" + xPos);

		// pop_class.showAsDropDown(mtextView, xPos, 0);
		pop_testtime.showAsDropDown(mtextView);

		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

//				Toast.makeText(
//						MystudentTargetActivity.this,
//						l_map_testtime.get(position).get("DestDate").toString(),
//						1000).show();
				mtextView.setText(l_map_testtime.get(position).get("DestDate")
						.toString());

				if (pop_testtime != null) {
					pop_testtime.dismiss();
				}
				// 设置 留学申请日期
				setstudyabroadtime("1",
						l_map_testtime.get(position).get("DestDate").toString());
			}
		});
	}

}
