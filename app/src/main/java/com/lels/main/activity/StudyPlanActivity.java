package com.lels.main.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strudentlelts.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lels.constants.Constants;
import com.lels.costum_widget.CustomProgressDialog;
import com.lels.main.activity.adapter.MyremindTestAdapter;
import com.lels.main.activity.adapter.StudyPlanAdapter;
import com.lels.youke.activity.Yk_DbHelper;
import com.lelts.student.db.ClockInfo;
import com.lelts.student.db.ClockInfoWrapper;
import com.lelts.student.db.DbHelper;
import com.lelts.tool.ImageLoder;
import com.lelts.tool.ImageLoder.IntervalTimeTrace;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.student.calendar.CalendarView;

public class StudyPlanActivity extends Activity implements OnClickListener,
		IntervalTimeTrace {
	private CustomProgressDialog customDialog;
	private RelativeLayout studyOnLine_no_data;
	private PullToRefreshListView listview_studyOnline;
	private TextView studyOnLine_text;

	private static final String TAG = "StudyPlanActivity";
	private static final String TAG1 = "StudyPlanActivity11";

	// addmark for me
	private ImageButton imageview_addmark;
	private RelativeLayout relative_nullDate_plan;
	/** Called when the activity is first created. */
	/**
	 * gridview de adapter
	 **/
	private CalendarView calV = null;
	private GridView gridView = null;
	private TextView topText = null;

	private static int jumpMonth = 0;
	private static int jumpYear = 0;

	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";

	ImageButton previous;
	ImageButton next;
	LinearLayout main;
	private ImageButton img_back;

	SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");

	private ListView listview_studyplan;
	private String token;

	List<String> l_lessondata = new ArrayList<String>();
	List<HashMap<String, Object>> l_lessons = new ArrayList<HashMap<String, Object>>();
	HashMap<String, String> map_dataandlessons = new HashMap<String, String>();

	private HashMap<String, Object> map_num = new HashMap<String, Object>();
	private int month_up;
	private int month_down;

	private List<HashMap<String, Object>> list_plan;
	private List<ClockInfo> list_c = new ArrayList<ClockInfo>();

	private MyremindTestAdapter adapter_remind;
	private ListView listview_myself_studyplan;
	private String selected_data;

	// 当前的时间
	private String data_cccc;
	private boolean iscurrent = true;
	private static String tagusername;
	private static String tagyk;

	// set db info
	private List<ClockInfoWrapper> mlist = new ArrayList<ClockInfoWrapper>();

	public static DbHelper dbHelper;
	public static Yk_DbHelper yk_dbHelper;
	private int c_id = 0;

	private boolean isremind = true;
	private boolean isplan = true;

	/**
	 * get curren data
	 **/
	@SuppressLint("SimpleDateFormat")
	public StudyPlanActivity() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		currentDate = sdf.format(date);
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);

		data_cccc = currentDate.split("-")[3] + ":" + currentDate.split("-")[4];

		String month_cc = String.valueOf(month_c);
		String day_cc = String.valueOf(day_c);

		if (month_cc.length() == 1) {
			month_cc = "0" + month_cc;
		}

		if (day_cc.length() == 1) {
			day_cc = "0" + day_cc;
		}
		currentDate = String.valueOf(year_c) + "-" + month_cc + "-"
				+ String.valueOf(day_cc);
		Log.e(TAG1, "currentdata====" + currentDate);
		selected_data = currentDate;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studyplan);
		initView();
		getDataFromShare();

		if (tagusername.equals("游客") || tagyk.equals("1")) {
			yk_dbHelper = Yk_DbHelper.getInstance(this);
			// setdataisnull();
			Log.e(TAG1, "游客");
		} else {
			Log.e(TAG1, "用户");
			dbHelper = DbHelper.getInstance(this);
			getDataFromNet(currentDate);
		}
		/*
		 * adapter_remind = new MyremindTestAdapter(StudyPlanActivity.this,
		 * mlist); listview_myself_studyplan.setAdapter(adapter_remind);
		 */

		select();

	}

	// select
	// 查询数据库，是否有计划
	private void select() {
		if (tagusername.equals("游客") || tagyk.equals("1")) {
			list_c = yk_dbHelper.search(ClockInfo.class);
		} else {
			list_c = dbHelper.search(ClockInfo.class);
		}

		if (list_c != null) {
			/*
			 * adapter_remind.setUserEntities(list_c);
			 * adapter_remind.notifyDataSetChanged();
			 */
			if (list_c.size() > 0) {
				Log.e("TAG", "clock的 id===="
						+ list_c.get(list_c.size() - 1).getId());
				Log.e("TAG", list_c.get(list_c.size() - 1).getNote());
				c_id = list_c.get(list_c.size() - 1).getId();
			}
			mlist = new ArrayList<ClockInfoWrapper>();
			for (int i = 0; i < list_c.size(); i++) {
				if (list_c.get(i).getSelect_time()
						.equalsIgnoreCase(selected_data)) {
					ClockInfoWrapper wrapper = new ClockInfoWrapper();
					wrapper.info = list_c.get(i);
					mlist.add(wrapper);
				}
			}
			Comparator comparator = new MyPlanSortComparator();
			Collections.sort(mlist, comparator);
			findNearestStudentPlan(System.currentTimeMillis());
			adapter_remind = new MyremindTestAdapter(StudyPlanActivity.this,
					mlist);
			listview_myself_studyplan.setAdapter(adapter_remind);
			setListViewHeightBasedOnChildren(listview_myself_studyplan);

			if (mlist.size() == 0) {
				isremind = true;
				Log.e(TAG1, "select（） remind为空");
			} else {
				isremind = false;
				Log.e(TAG1, "select（）  remind不为空");
			}
		}
	}

	class MyPlanSortComparator implements Comparator<ClockInfoWrapper> {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");

		@Override
		public int compare(ClockInfoWrapper lhs, ClockInfoWrapper rhs) {
			long ci2 = 0;
			long ci1 = 0;
			try {
				ci2 = sdf.parse(rhs.info.getStarttime()).getTime() / 1000;
				ci1 = sdf.parse(lhs.info.getStarttime()).getTime() / 1000;
				System.out.println("list_sort : " + ci2 + ", " + ci1);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return (int) (ci1 - ci2);
		}

	}

	// 删除 根据条件 id等于一的我就把它删除了
	public static void delete(String clock_id) {
		System.out.println("String.valueOf(info.getId())====" + clock_id);
		boolean isTrue;
		if (tagusername.equals("游客") || tagyk.equals("1")) {
			isTrue = yk_dbHelper
					.deleteCriteria(ClockInfo.class, "id", clock_id);
		} else {
			isTrue = dbHelper.deleteCriteria(ClockInfo.class, "id", clock_id);
		}

		if (isTrue) {
			Log.e("tag", "删除成功");
		} else {
			Log.e("tag", "删除失败");
		}
	}

	public static void delete(ClockInfo cinfo) {
		System.out.println("String.valueOf(info.getId())====" + cinfo.getId());
		boolean isTrue;
		if (tagusername.equals("游客") || tagyk.equals("1")) {
			isTrue = yk_dbHelper.deleteCriteria(ClockInfo.class, "id",
					String.valueOf(cinfo.getId()));
		} else {
			isTrue = dbHelper.deleteCriteria(ClockInfo.class, "id",
					String.valueOf(cinfo.getId()));
		}
		if (isTrue) {
			Log.e("tag", "删除成功");
		} else {
			Log.e("tag", "删除失败");
		}
	}

	/**
	 * select by selectedtime for remind
	 **/
	private int selectFromSelectTime() {
		if (tagusername.equals("游客") || tagyk.equals("1")) {
			list_c = yk_dbHelper.search(ClockInfo.class);
		} else {
			list_c = dbHelper.search(ClockInfo.class);
		}
		if (list_c != null) {
			mlist = new ArrayList<ClockInfoWrapper>();

			for (int i = 0; i < list_c.size(); i++) {

				Log.e("TAG", "select====根据当前日期的全部clock==="
						+ list_c.get(i).getSelect_time());
				Log.e("TAG", "selected_data====选中的日期为===" + selected_data);
				if (list_c.get(i).getSelect_time()
						.equalsIgnoreCase(selected_data)) {
					Log.e(TAG, "加载个人计划信息");
					ClockInfoWrapper wrapper = new ClockInfoWrapper();
					wrapper.info = list_c.get(i);
					mlist.add(wrapper);
				}
			}
			Comparator comparator = new MyPlanSortComparator();
			Collections.sort(mlist, comparator);
			findNearestStudentPlan(System.currentTimeMillis());
			adapter_remind.setUserEntities(mlist);

			setListViewHeightBasedOnChildren(listview_myself_studyplan);

			if (mlist.size() == 0) {
				isremind = true;
				Log.e(TAG1, "selectfromselecttime()    remind为空");
			} else {
				isremind = false;
				Log.e(TAG1, "selectfromselecttime()    remind不为空");
			}
		}
		return mlist.size();
	}

	private void getDataFromShare() {
		SharedPreferences share = this.getSharedPreferences("userinfo",
				MODE_PRIVATE);
		token = share.getString("Token", "");
	}

	// 当天是否有课程
	private void getDataFromNet(String date_c) {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDENT_CALENDAR + "?dateParam="
				+ date_c;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);
		// params.addBodyParameter("dateParam", date_c);
		showCustomCircleProgressDialog(null, getString(R.string.common_Loading));
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		Log.e(TAG, url);
		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.e("StudyPlanActivity", "学习计划结果"
								+ responseInfo.result);
						try {
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							JSONArray array = new JSONArray(Data);
							map_dataandlessons = new HashMap<String, String>();
							l_lessondata.clear();
							if (array != null) {
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.optJSONObject(i);
									Iterator<String> it = obj.keys();
									while (it.hasNext()) {
										String s_d = (String) it.next();
										l_lessondata.add(s_d);
									}
									map_dataandlessons.put(l_lessondata.get(i),
											obj.getString(l_lessondata.get(i)));
								}

								// map_num = getdatadotnum(map_dataandlessons);
								Log.e("StudyPlanActivity", "map_num-----"
										+ map_num);
								gridView.setAdapter(null);

								map_num = getDataDotNum(map_dataandlessons);
								if (tagusername.equals("游客")
										|| tagyk.equals("1")) {
									map_num.clear();
								}
								calV = new CalendarView(StudyPlanActivity.this,
										jumpMonth, jumpYear, year_c, month_c,
										day_c, map_num);
								gridView.setAdapter(calV);
								// 设置当天的 课程信息
								listview_studyplan.setAdapter(null);
								list_plan = new ArrayList<HashMap<String, Object>>();
								list_plan = getListData(map_dataandlessons,
										currentDate);
								// 点击更新 listview 的 adapter
								// knowIfDataBackground(list_plan);
								if (list_plan.size() > 0) {
									isplan = false;
									// 是否显示背景图
									// knowIfDataBackground(list_plan);
									Log.e(TAG1,
											"getdatafromnet()     isplan不为空");
									findNearestClassPlan(System
											.currentTimeMillis());

									StudyPlanAdapter adapter = new StudyPlanAdapter(
											StudyPlanActivity.this, list_plan,
											data_cccc, true);
									listview_studyplan.setAdapter(adapter);
								} else {
									isplan = true;
									Log.e(TAG1, "getdatafromnet()     isplan为空");

								}
								setListViewHeightBasedOnChildren(listview_studyplan);
								// 设置 个人计划信息
								selectFromSelectTime();
								setDataIsNull();
							}
							closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							e.printStackTrace();
							closeCustomCircleProgressDialog();
						}
						/*
						 * setListViewHeightBasedOnChildren(listview_studyplan);
						 * // 设置 个人计划信息 selectFromSelectTime(); setDataIsNull();
						 */
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
						closeCustomCircleProgressDialog();

					}
				});
	}

	/**
	 * 是否显示 当天是否有课程
	 **/
	protected void setDataIsNull() {
		Log.e(TAG, "isplan=====" + isplan + "\n" + "isremind===" + isremind);
		if (tagusername.equals("游客") || tagyk.equals("1")) {
			listview_studyplan.setVisibility(View.GONE);
			if (isremind) {
				relative_nullDate_plan.setVisibility(View.VISIBLE);
				listview_myself_studyplan.setVisibility(View.GONE);
			} else {
				relative_nullDate_plan.setVisibility(View.GONE);
				listview_myself_studyplan.setVisibility(View.VISIBLE);
			}
		} else {
			if (isremind && isplan) {
				relative_nullDate_plan.setVisibility(View.VISIBLE);
				listview_studyplan.setVisibility(View.GONE);
				listview_myself_studyplan.setVisibility(View.GONE);
			} else {
				relative_nullDate_plan.setVisibility(View.GONE);
				listview_studyplan.setVisibility(View.VISIBLE);
				listview_myself_studyplan.setVisibility(View.VISIBLE);
			}
		}
	}

	private void initView() {
		SharedPreferences stushare = getSharedPreferences("stushare",
				MODE_PRIVATE);
		tagusername = stushare.getString("username", "");
		tagyk = stushare.getString("isVisitor", "");
		// listview_studyOnline = (PullToRefreshListView)
		// findViewById(R.id.listview_studyonline);
		// studyOnLine_no_data = (RelativeLayout)
		// findViewById(R.id.studyOnLine_no_data);
		studyOnLine_text = (TextView) findViewById(R.id.studyOnLine_text);
		// myself plan listview
		listview_myself_studyplan = (ListView) findViewById(R.id.listview_myself_studyplan);
		listview_studyplan = (ListView) findViewById(R.id.listview_studyplan);
		imageview_addmark = (ImageButton) findViewById(R.id.imageview_addmark);
		imageview_addmark.setOnClickListener(this);
		img_back = (ImageButton) findViewById(R.id.imageView_back);
		img_back.setOnClickListener(this);
		calV = new CalendarView(StudyPlanActivity.this, jumpMonth, jumpYear,
				year_c, month_c, day_c, map_num);
		gridView = (GridView) findViewById(R.id.gridView1);
		// 显示 是否有计划
		relative_nullDate_plan = (RelativeLayout) findViewById(R.id.relative_nulldate_plan);
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
		gridView.setBackgroundResource(R.color.white);
		gridView.setPadding(1, 1, 1, 1);
		// 閫変腑鏃ユ湡涔嬪悗 listview 瑕佸埛鏂版暟鎹�
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				Log.e(TAG,
						"calV.getDateByClickItem(position)========"
								+ calV.getDateByClickItem(position));
				String scheduleDay = calV.getDateByClickItem(position).split(
						"\\.")[0]; // 杩欎竴澶╃殑闃冲巻
				String titleYear = calV.getShowYear();
				String titleMonth = calV.getShowMonth();
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				if (position >= startPosition && position <= endPosition) {
					// Toast.makeText(
					// StudyPlanActivity.this,
					// "琚偣鍑荤殑鏃ユ湡 : " + titleYear + "骞�" + titleMonth + "鏈�"
					// + scheduleDay + "鏃�", Toast.LENGTH_LONG)
					// .show();
					// calV.setCurrentFlag(position);
					// calV.notifyDataSetChanged();
					selected_data = titleYear
							+ "-"
							+ String.format("%02d", Integer.valueOf(titleMonth))
							+ "-"
							+ String.format("%02d",
									Integer.valueOf(scheduleDay));
					Log.e(TAG, "" + "selected time====" + selected_data);
					selectFromSelectTime();
					listview_studyplan.setAdapter(null);
					list_plan = new ArrayList<HashMap<String, Object>>();
					list_plan = getListData(map_dataandlessons, selected_data);
					if (currentDate.equalsIgnoreCase(selected_data)) {
						iscurrent = true;
					} else {
						iscurrent = false;
					}
					// 鐐瑰嚮鏇存柊 listview 鐨� adapter
					if (list_plan.size() > 0) {
						isplan = false;
						// knowIfDataBackground(list_plan);

						findNearestClassPlan(System.currentTimeMillis());
						StudyPlanAdapter adapter = new StudyPlanAdapter(
								StudyPlanActivity.this, list_plan, data_cccc,
								iscurrent);
						listview_studyplan.setAdapter(adapter);

					} else {
						isplan = true;
					}
					setListViewHeightBasedOnChildren(listview_studyplan);
					calV.setCurrentFlag(position);
					calV.notifyDataSetChanged();
					System.out.println("isplan----"+isplan+"----isremind---"+isremind);
					setDataIsNull();
				} else if (position < startPosition) {
					getPreviousMonth();
				} else if (position > endPosition) {
					getNextMonth();
				} else {
					Toast.makeText(StudyPlanActivity.this, "No",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		gridView.setAdapter(calV);
		previous = (ImageButton) findViewById(R.id.previous);
		next = (ImageButton) findViewById(R.id.next);
		main = (LinearLayout) findViewById(R.id.main);
		topText = (TextView) findViewById(R.id.toptext);
		addTextToTopTextView(topText);
		previous.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getPreviousMonth();
			}
		});

		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getNextMonth();
			}
		});
	}

	// pre month
	private void getPreviousMonth() {
		Log.e(TAG, "getPreviousMonth()");
		Log.e(TAG, "上一个月的数据jumpMonth之前==" + jumpMonth);

		jumpMonth--;
		Log.e(TAG, "上一个月的数据jumpMonth之后==" + jumpMonth);
		// 获取当前的日期 （即此月的上一个月的日期）
		int day_s = day_c;
		int year_s = year_c + jumpYear;
		int month_s = month_c + jumpMonth;
		if (month_s > 0) {
			// //往下一个月跳转
			if (month_s % 12 == 0) {
				year_s = year_c + month_s / 12 - 1;
				month_s = 12;
			} else {
				year_s = year_c + month_s / 12;
				month_s = month_s % 12;
			}
		} else {
			// 往上一个月跳转
			year_s = year_c - 1 + month_s / 12;
			month_s = month_s % 12 + 12;
			if (month_s % 12 == 0) {
			}
		}
		// String data_s = String.valueOf(year_s) + "-"
		// + String.format("%02d", Integer.valueOf(month_s)) + "-"
		// + String.format("%02d", Integer.valueOf(day_s));
		String data_s = String.valueOf(year_s) + "-"
				+ String.format("%02d", Integer.valueOf(month_s)) + "-"
				+ String.format("%02d", Integer.valueOf(day_s));
		System.out
				.println("上个月的日期为===" + data_s + ", current = " + currentDate);
		if (!currentDate.equalsIgnoreCase(data_s)) {
			data_s = String.valueOf(year_s) + "-"
					+ String.format("%02d", Integer.valueOf(month_s)) + "-"
					+ String.format("%02d", 1);
		}
		selected_data = data_s;
		System.out.println("上个月的日期为=== selected_data " + selected_data);
		getDataFromNet(data_s);
		// calV = new CalendarView(getActivity(), getResources(), jumpMonth,
		// jumpYear, year_c, month_c, day_c,null);
		// gridView.setAdapter(calV);
		// addTextToTopTextView(topText);
		topText.setText(String.valueOf(year_s) + "年"
				+ String.format("%02d", Integer.valueOf(month_s)) + "月");
	}

	// next month
	private void getNextMonth() {
		Log.d(TAG, "getNextMonth()");
		Log.e(TAG, "下一个月的数据");

		jumpMonth++;

		Log.e(TAG, "下一个月的数据jumpMonth之后==" + jumpMonth);
		// 获取当前的日期 （即此月的上一个月的日期）
		int day_s = day_c;

		int year_s = year_c + jumpYear;
		int month_s = month_c + jumpMonth;

		if (month_s > 0) {
			// //往下一个月跳转
			if (month_s % 12 == 0) {
				year_s = year_c + month_s / 12 - 1;
				month_s = 12;
			} else {
				year_s = year_c + month_s / 12;
				month_s = month_s % 12;
			}
		} else {
			// 往上一个月跳转
			year_s = year_c - 1 + month_s / 12;
			month_s = month_s % 12 + 12;
			if (month_s % 12 == 0) {

			}
		}
		String data_s = String.valueOf(year_s) + "-"
				+ String.format("%02d", Integer.valueOf(month_s)) + "-"
				+ String.format("%02d", Integer.valueOf(day_s));

		Log.e(TAG, "下个月的日期为===" + data_s + ", current = " + currentDate);

		if (!currentDate.equalsIgnoreCase(data_s)) {
			data_s = String.valueOf(year_s) + "-"
					+ String.format("%02d", Integer.valueOf(month_s)) + "-"
					+ String.format("%02d", 1);
		}
		selected_data = data_s;
		Log.e(TAG, "下个月的日期为=== selected_data " + selected_data);
		getDataFromNet(data_s);

		topText.setText(String.valueOf(year_s) + "年"
				+ String.format("%02d", Integer.valueOf(month_s)) + "月");
	}

	private void addTextToTopTextView(TextView view) {
		String datestr = "";
		StringBuffer textDate = new StringBuffer();
		if (Integer.valueOf(calV.getShowMonth()) > 9) {
			datestr = calV.getShowYear() + "年" + calV.getShowMonth() + "月";
		} else {
			datestr = calV.getShowYear() + "年0" + calV.getShowMonth() + "月";
		}
		// try {
		// textDate.append(format2.format(format1.parse(datestr)));
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		view.setText(datestr);
		view.setTextColor(Color.WHITE);
		view.setTypeface(Typeface.DEFAULT_BOLD);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 添加个人计划
		case R.id.imageview_addmark:
			Intent intent = new Intent();
			intent.setClass(StudyPlanActivity.this,
					StudyPlanAddPlanActivity.class);

			Bundle b = new Bundle();
			b.putString("c_id", String.valueOf(c_id));
			Log.e(TAG, "wsf-------c_id=" + String.valueOf(c_id));
			intent.putExtras(b);
			startActivityForResult(intent, 11);
			break;
		case R.id.imageView_back:
			// IntentUtlis.sysStartActivity(StudyPlanActivity.this,
			// MainActivity.class);
			finish();
			break;
		default:
			break;
		}
	}

	// hashmap<riqi,dotnum>
	@SuppressWarnings("unused")
	private HashMap<String, Object> getDataDotNum(
			HashMap<String, String> dataandlessons) {

		HashMap<String, Object> map_num = new HashMap<String, Object>();

		List<HashMap<String, Object>> l_lesson = new ArrayList<HashMap<String, Object>>();
		int dot_ = 0;
		try {
			List<ClockInfo> list_c;
			if (tagusername.equals("游客") || tagyk.equals("1")) {
				list_c = yk_dbHelper.search(ClockInfo.class);
			} else {
				list_c = dbHelper.search(ClockInfo.class);
			}
			for (int i = 0; i < l_lessondata.size(); i++) {

				String s = dataandlessons.get(l_lessondata.get(i));
				String s_k = l_lessondata.get(i);

				Log.e(TAG, "有可乘的日期的" + s);
				Log.e(TAG, "l_lessondata.get(i)l_lessondata.get(i)======="
						+ s_k);

				if (s.equalsIgnoreCase("null")) {
					break;
				}

				JSONObject obj = new JSONObject(s);
				String lessons = obj.getString("lessons");
				JSONArray array = new JSONArray(lessons);
				dot_ = array.length();

				// 查询
				if (list_c != null) {
					if (list_c.size() > 0) {
						for (int j = 0; j < list_c.size(); j++) {
							if (list_c.get(j).getSelect_time()
									.equalsIgnoreCase(s_k)) {
								dot_++;
							} else {
								if (i == 0) {
									String selectTime = list_c.get(j)
											.getSelect_time();
									String year_month = selectTime
											.substring(
													0,
													selectTime
															.indexOf(
																	"-",
																	selectTime
																			.indexOf("-") + 1));
									System.out.println("dblist = " + selectTime
											+ ",currentDate = " + currentDate
											+ ",part = " + year_month
											+ ",result = "
											+ currentDate.indexOf(year_month));
									if (-1 != currentDate.indexOf(year_month)) {
										if (map_num.containsKey(selectTime)) {
											String nonNetData = map_num.get(
													selectTime).toString();
											int doc = Integer
													.parseInt(nonNetData.trim());
											doc++;
											map_num.put(selectTime,
													String.valueOf(doc));
										} else {
											map_num.put(selectTime,
													String.valueOf("1"));
										}
									}
								}
							}
						}
					}
				}
				map_num.put(l_lessondata.get(i), String.valueOf(dot_));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map_num;
	}

	/***
	 * param dataandlessons: calendar - select - data -listviewdata
	 * 
	 */
	private List<HashMap<String, Object>> getListData(
			HashMap<String, String> dataandlessons, String ri) {

		Log.e(TAG, "mou yitian de  riqi ==" + ri);
		List<HashMap<String, Object>> l_data = new ArrayList<HashMap<String, Object>>();
		if (!dataandlessons.containsKey(ri)) {
			return l_data;
		}
		try {
			String s = dataandlessons.get(ri);
			JSONObject obj = new JSONObject(s);
			String lessons = obj.getString("lessons");
			JSONArray array = new JSONArray(lessons);
			for (int j = 0; j < array.length(); j++) {
				// "id": 29905,
				// "sCode": "YA10113",
				// "SectEnd": 1435717800000,
				// "sNameBr": "鏈濋槼鍥借锤鏍″尯鐟炶禌鍟嗗姟妤�6灞俈IP3",
				// "sNameBc": "IELTS1瀵�1鐝紙鍩虹锛�",
				// "sAddress": "鍖椾含甯傛湞闃冲尯寤哄浗璺�128鍙蜂腑鑸伐涓氬ぇ鍘�",
				// "SectBegin": 1435708800000,
				// "nowLessonId": 29905,
				// "nLessonNo": 21

				JSONObject obj_l = array.optJSONObject(j);
				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("id", obj_l.get("id"));
				map.put("sCode", obj_l.get("sCode"));
				map.put("SectEnd", obj_l.get("SectEnd"));
				map.put("sNameBr", obj_l.get("sNameBr"));
				map.put("sNameBc", obj_l.get("sNameBc"));
				map.put("sAddress", obj_l.get("sAddress"));
				map.put("SectBegin", obj_l.get("SectBegin"));
				map.put("nowLessonId", obj_l.get("nowLessonId"));
				map.put("nLessonNo", obj_l.get("nLessonNo"));
				map.put("inTime", false);
				l_data.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return l_data;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		if (resultCode == 11) {
			/*
			 * Bundle b = data.getExtras(); ClockInfo infof = (ClockInfo)
			 * b.getSerializable("info"); add(infof);
			 */
			selectFromSelectTime();
		} else if (resultCode == 10) {
			select();
		}
		/*
		 * if (tagusername.equals("游客") || tagyk.equals("1")) {
		 * getDataFromNet(currentDate); } else {
		 */
		getDataFromNet(currentDate);
		// }
	}

	/**
	* 
	* */
	public void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			ViewGroup.LayoutParams params = listview_studyplan
					.getLayoutParams();
			params.height = 0;
			((MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除
			listview_studyplan.setLayoutParams(params);
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		((MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除
		listView.setLayoutParams(params);
	}

	// 显示自定义加载对话框
	public CustomProgressDialog showCustomCircleProgressDialog(String title,
			String msg) {
		if (customDialog != null) {
			try {
				customDialog.cancel();
				customDialog = null;
			} catch (Exception e) {
			}
		}

		customDialog = CustomProgressDialog.createDialog(this);
		// dialog.setIndeterminate(false);
		// dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		customDialog.setTitle(title);
		customDialog.setMessage(msg);

		try {
			customDialog.show();
		} catch (Exception e) {
		}
		customDialog.setCanceledOnTouchOutside(false);// 设置dilog点击屏幕空白处不消失
		return customDialog;
	}

	// 显示自定义加载对话框
	public CustomProgressDialog showCustomCircleProgressDialog(String title,
			String msg, boolean isCancelable) {
		if (customDialog != null) {
			try {
				customDialog.cancel();
				customDialog = null;
			} catch (Exception e) {
			}
		}

		customDialog = CustomProgressDialog.createDialog(this);
		// dialog.setIndeterminate(false);
		// dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		customDialog.setCancelable(isCancelable);// 是否可用用"返回键"取消
		customDialog.setTitle(title);
		customDialog.setMessage(msg);

		try {
			customDialog.show();
		} catch (Exception e) {
		}

		customDialog.setCanceledOnTouchOutside(false);// 设置dilog点击屏幕空白处不消失
		return customDialog;
	}

	// 关闭自定义加载对话框
	public void closeCustomCircleProgressDialog() {
		if (customDialog != null) {
			try {
				customDialog.cancel();
				customDialog = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		ImageLoder app = (ImageLoder) this.getApplication();
		if (null != app) {
			app.registerIntervalTrace(this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		ImageLoder app = (ImageLoder) this.getApplication();
		if (null != app) {
			app.unregisterIntervalTrace();
		}
	}

	@Override
	public void intervalFeedback(long currentInterval) {
		// TODO Auto-generated method stub
		/*
		 * if (null != mlist && !mlist.isEmpty()) {
		 * findNearestStudentPlan(currentInterval); if (null != adapter_remind)
		 * { runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run() { adapter_remind.setUserEntities(mlist);
		 * } }); } } if (null != list_plan && !list_plan.isEmpty()) {
		 * findNearestClassPlan(currentInterval); runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run() { StudyPlanAdapter adapter = new
		 * StudyPlanAdapter( StudyPlanActivity.this, list_plan, data_cccc,
		 * true); listview_studyplan.setAdapter(adapter); } }); }
		 */
	}

	private void findNearestStudentPlan(long currentInterval) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		int size = mlist.size();
		try {
			currentInterval = sdf.parse(sdf.format(new Date(currentInterval)))
					.getTime();
			/*
			 * int hasClass = -1; ClockInfoWrapper nearestInfo = null;
			 * List<ClockInfoWrapper> sames = new ArrayList<ClockInfoWrapper>();
			 */
			for (int i = size - 1; i >= 0; i--) {
				ClockInfoWrapper infoWrapper = mlist.get(i);
				String start = infoWrapper.info.getStarttime();
				String end = infoWrapper.info.getEndtime();
				long startTime = sdf.parse(start).getTime();
				long endTime = sdf.parse(end).getTime();
				if (currentInterval >= startTime && currentInterval <= endTime) {
					infoWrapper.isInPlan = true;
					// hasClass = i;
				}
				/*
				 * if (hasClass == -1) { if (currentInterval < startTime) { if
				 * (nearestInfo == null) { nearestInfo = infoWrapper; } else {
				 * // 如果相交，肯定当前时间不在二者之间 long nearStart =
				 * sdf.parse(nearestInfo.info.getStarttime()).getTime(); if
				 * (startTime < nearStart) { if (!sames.isEmpty()) {
				 * sames.clear(); } nearestInfo = infoWrapper; } else if
				 * (startTime == nearStart) { sames.add(infoWrapper); } } }
				 * 
				 * } else { nearestInfo = null; sames.clear(); }
				 */
			}
			/*
			 * if (null != nearestInfo) { nearestInfo.isInPlan = true; if
			 * (!sames.isEmpty()) { for (int j = 0; j < sames.size(); j++) {
			 * sames.get(j).isInPlan = true; } } }
			 */

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void findNearestClassPlan(long currentInterval) {
		Iterator<HashMap<String, Object>> iters = list_plan.iterator();
		// boolean hasClass = false;
		// HashMap<String, Object> nearestInfo = null;
		// List<HashMap<String, Object>> sames = new ArrayList<HashMap<String,
		// Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		try {
			currentInterval = sdf.parse(sdf.format(new Date(currentInterval)))
					.getTime();
			while (iters.hasNext()) {
				HashMap<String, Object> iter = iters.next();
				long startTime = Long.parseLong(iter.get("SectBegin")
						.toString());
				long endTime = Long.parseLong(iter.get("SectEnd").toString());

				if (currentInterval >= startTime && currentInterval <= endTime) {
					iter.put("inTime", true);
					// hasClass = true;
				}
				/*
				 * if (!hasClass) { if (currentInterval < startTime) { if
				 * (nearestInfo == null) { nearestInfo = iter; } else { //
				 * 如果相交，肯定当前时间不在二者之间 long nearStart =
				 * Long.parseLong(nearestInfo.get("SectBegin").toString()); if
				 * (startTime < nearStart) { sames.clear(); nearestInfo = iter;
				 * } else if (startTime == nearStart) { sames.add(iter); } } }
				 * 
				 * } else { nearestInfo = null; sames.clear(); }
				 */
			}
			/*
			 * if (null != nearestInfo) { nearestInfo.put("inTime", true); if
			 * (!sames.isEmpty()) { for (int j = 0; j < sames.size(); j++) {
			 * sames.get(j).put("inTime", true); } } }
			 */
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
