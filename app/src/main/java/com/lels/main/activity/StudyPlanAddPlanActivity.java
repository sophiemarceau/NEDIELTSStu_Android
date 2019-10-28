package com.lels.main.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

import com.example.strudentlelts.R;
import com.lels.alarm.AlarmReceiver;
import com.lels.alarm.AlarmService;
import com.lels.youke.activity.Yk_DbHelper;
import com.lelts.student.db.ClockInfo;
import com.lelts.student.db.DbHelper;
import com.lelts.student.myself.adapter.PopGroupAdapter;
import com.widget.time.JudgeDate;
import com.widget.time.ScreenInfo;
import com.widget.time.WheelMain;

@SuppressLint("SimpleDateFormat")
public class StudyPlanAddPlanActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "StudyPlanAddPlanActivity";
	public static StudyPlanAddPlanActivity instance;

	private int pre;
	private ImageButton imageview_back;
	private Button button_addmark_complete, button_delmark_complete;
	private EditText edittext_plan_title;

	private TextView textview_plan_starttime;
	private TextView textview_plan_endtime;
	private TextView textview_plan_repeat;
	private TextView textview_plan_sound;
	private TextView textview_plan_remind;

	private EditText edittext_plan_note;

	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private int hour_c = 0;
	private int minute_c = 0;
	private String currentDate = "";
	private String currentDate_type = "";

	private WheelMain wheelMain;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private View view;
	private ListView lv_group;
	// private ListView lv_voice;
	// private ListView lv_remind;
	// setting popupwindow
	private PopupWindow pop_repeat;
	private PopupWindow pop_voice;
	private PopupWindow pop_remind;

	private String tagusername, tagyk;

	private List<String> l_repeat = new ArrayList<String>();
	private List<String> l_voice = new ArrayList<String>();
	private List<String> l_remind = new ArrayList<String>();
	// 设置闹钟的参数
	private Calendar calendar;
	// 获取时间 的格式 yyyy-MM-dd-HH-mm
	private String date1 = "";
	private String date2 = "";

	public static long long_alarm;
	public static List<HashMap<String, Object>> mlist_clock = new ArrayList<HashMap<String, Object>>();
	private List<ClockInfo> clockEntities = new ArrayList<ClockInfo>();
	private String c_id;

	private Intent intents;

	// private ClockInfo info = new ClockInfo();
	public StudyPlanAddPlanActivity() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		currentDate = sdf.format(date);
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		hour_c = Integer.parseInt(currentDate.split("-")[3]);
		minute_c = Integer.parseInt(currentDate.split("-")[4]);
		// System.out.println("得到当前的日期为==" + currentDate);
		date1 = currentDate;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		currentDate_type = formatter.format(curDate);
		// System.out.println("得到当前的日期为==" + currentDate_type);
		date2 = currentDate_type;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studyplan_addplan);

		getDateFromIntent();
		instance = this;
		init();

		// EventBus.getDefault().register(this,"StartService");

		initData();
	}

	public void StartService(com.lels.eventbus.StartService start) {
		if (intents != null) {
			stopService(intents);
		}
	}

	// private void getDataFromIntent() {
	// info = (ClockInfo) getIntent().getExtras().getSerializable("delete");
	// c_id = (String) getIntent().getExtras().getString("c_id");
	// System.out.println("传递过来的 info id ====" + info.getId());
	// System.out.println("传递过来的 info id ====" + info.getNote());
	// }
	// private void initData1() {
	//
	// edittext_plan_title.setText(info.getTitle());
	//
	// textview_plan_starttime.setText(setdatafrom(info.getStarttime()));
	// textview_plan_endtime.setText(setdatafrom(info.getEndtime()));
	//
	// textview_plan_repeat.setText(info.getRepeat());
	// textview_plan_sound.setText(info.getSound());
	// textview_plan_remind.setText(info.getRemind());
	// edittext_plan_note.setText(info.getNote());
	//
	// }
	// private String setdatafrom(String datas) {
	//
	// String[] s = datas.split("-");
	// String dataf = s[0] + "-" + s[1] + "-" + s[2] + " " + s[3] + ":" + s[4];
	// return dataf;
	// }
	private void getDateFromIntent() {
		c_id = getIntent().getExtras().getString("c_id");
	}

	private void initData() {

		// 永不、每天、每周
		l_repeat.add("永不");
		l_repeat.add("每天");
		l_repeat.add("每周");
		l_voice.add("有");
		l_voice.add("无");

		// 无、提前10分钟、提前30分钟、提前1小时、提前1天
		l_remind.add("无");
		l_remind.add("提前10分钟");
		l_remind.add("提前30分钟");
		l_remind.add("提前1小时");
		l_remind.add("提前1天");

	}

	private void init() {
		SharedPreferences stushare = getSharedPreferences("stushare",
				MODE_PRIVATE);
		tagusername = stushare.getString("username", "");
		tagyk = stushare.getString("isVisitor", "");
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		button_addmark_complete = (Button) findViewById(R.id.button_addmark_complete);
		button_delmark_complete = (Button) findViewById(R.id.button_delmark_complete);
		edittext_plan_title = (EditText) findViewById(R.id.edittext_plan_title);

		textview_plan_starttime = (TextView) findViewById(R.id.textview_plan_starttime);
		textview_plan_endtime = (TextView) findViewById(R.id.textview_plan_endtime);
		textview_plan_repeat = (TextView) findViewById(R.id.textview_plan_repeat);
		textview_plan_sound = (TextView) findViewById(R.id.textview_plan_sound);
		textview_plan_remind = (TextView) findViewById(R.id.textview_plan_remind);
		edittext_plan_note = (EditText) findViewById(R.id.edittext_plan_note);

		imageview_back.setOnClickListener(this);
		button_addmark_complete.setOnClickListener(this);

		textview_plan_starttime.setOnClickListener(this);
		textview_plan_endtime.setOnClickListener(this);

		textview_plan_repeat.setOnClickListener(this);
		textview_plan_sound.setOnClickListener(this);
		textview_plan_remind.setOnClickListener(this);
		edittext_plan_note.setOnClickListener(this);

		edittext_plan_title.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (temp.length() > 25) {
					Toast.makeText(StudyPlanAddPlanActivity.this, "最多只能输入25个字",
							Toast.LENGTH_LONG).show();
					;
					CharSequence cs = s.subSequence(0, 25);
					edittext_plan_title.setText(cs);
					edittext_plan_title.setSelection(25);
				}
			}
		});

		edittext_plan_note.addTextChangedListener(new TextWatcher() {

			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (temp.length() > 100) {
					Toast.makeText(StudyPlanAddPlanActivity.this,
							"最多只能输入100个字", Toast.LENGTH_LONG).show();
					;
					CharSequence cs = s.subSequence(0, 100);
					edittext_plan_note.setText(cs);
					edittext_plan_note.setSelection(100);
				}
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	public void getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		currentDate = sdf.format(date);
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		hour_c = Integer.parseInt(currentDate.split("-")[3]);
		minute_c = Integer.parseInt(currentDate.split("-")[4]);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		currentDate_type = formatter.format(curDate);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		// 点击添加计划 完成按钮
		case R.id.button_addmark_complete:
			if (edittext_plan_title.getText().toString().equalsIgnoreCase("")) {
				Toast.makeText(StudyPlanAddPlanActivity.this, "请填写标题",
						Toast.LENGTH_SHORT).show();
				break;
			}
			if (textview_plan_starttime.getText().toString()
					.equalsIgnoreCase("无")) {
				Toast.makeText(StudyPlanAddPlanActivity.this, "请选择开始时间",
						Toast.LENGTH_SHORT).show();
				break;
			}
			if (textview_plan_endtime.getText().toString()
					.equalsIgnoreCase("无")) {
				Toast.makeText(StudyPlanAddPlanActivity.this, "请选择结束时间",
						Toast.LENGTH_SHORT).show();
				break;
			}

			String plan_starttime = textview_plan_starttime.getText()
					.toString();
			String plan_endtime = textview_plan_endtime.getText().toString();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				long startTime = sdf.parse(plan_starttime).getTime();
				long endTime = sdf.parse(plan_endtime).getTime();
				if (startTime >= endTime) {
					Toast.makeText(StudyPlanAddPlanActivity.this,
							"结束时间要大于开始时间", Toast.LENGTH_LONG).show();
					break;
				}
			} catch (java.text.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setClockInfo();
			break;
		case R.id.textview_plan_starttime:
			LayoutInflater inflater = LayoutInflater
					.from(StudyPlanAddPlanActivity.this);
			final View timepickerview = inflater.inflate(R.layout.timepicker,
					null);
			ScreenInfo screenInfo = new ScreenInfo(
					StudyPlanAddPlanActivity.this);
			wheelMain = new WheelMain(timepickerview, true);
			wheelMain.screenheight = screenInfo.getHeight();
			String time = textview_plan_starttime.getText().toString();
			Calendar calendar = Calendar.getInstance();
			if (JudgeDate.isDate(time, "yyyy-MM-dd-HH-mm")) {
				try {
					calendar.setTime(dateFormat.parse(time));
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
			}
			int year = calendar.get(Calendar.YEAR);
			int month = formattoint(calendar.get(Calendar.MONTH));
			int day = formattoint(calendar.get(Calendar.DAY_OF_MONTH));

			int hour = formattoint(calendar.get(Calendar.HOUR_OF_DAY));
			int minute = formattoint(calendar.get(Calendar.MINUTE));

			int hour_s = Integer.valueOf(format(hour));
			int minute_s = Integer.valueOf(format(minute));

			System.out.println("获取的时间为==" + format(hour) + "转换的时间为=" + hour_s);
			System.out.println("获取的时间为==" + format(minute) + "转换的时间为="
					+ minute_s);

			// date1 =
			// String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
			// System.out.println(year+"=="+month+"=="+day);

			wheelMain.initDateTimePicker(year, month, day, hour_s, minute_s);
			wheelMain.showYear(false).showMonth(false).showDay(false)
					.showHourWay(true);
			new AlertDialog.Builder(StudyPlanAddPlanActivity.this)
					.setTitle("选择时间")
					.setView(timepickerview)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									textview_plan_starttime
											.setText(setDataFrom(wheelMain
													.getTime()));
									date1 = wheelMain.getTime();
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
		case R.id.textview_plan_endtime:
			LayoutInflater inflater1 = LayoutInflater
					.from(StudyPlanAddPlanActivity.this);
			final View timepickerview1 = inflater1.inflate(R.layout.timepicker,
					null);
			ScreenInfo screenInfo1 = new ScreenInfo(
					StudyPlanAddPlanActivity.this);
			wheelMain = new WheelMain(timepickerview1, true);
			wheelMain.screenheight = screenInfo1.getHeight();
			String time1 = textview_plan_endtime.getText().toString();
			Calendar calendar1 = Calendar.getInstance();
			if (JudgeDate.isDate(time1, "yyyy-MM-dd-HH-mm")) {
				try {
					calendar1.setTime(dateFormat.parse(time1));
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
			}
			int year1 = calendar1.get(Calendar.YEAR);
			int month1 = formattoint(calendar1.get(Calendar.MONTH));
			int day1 = formattoint(calendar1.get(Calendar.DAY_OF_MONTH));
			int hour1 = formattoint(calendar1.get(Calendar.HOUR_OF_DAY));
			int minute1 = formattoint(calendar1.get(Calendar.MINUTE));

			wheelMain.initDateTimePicker(year1, month1, day1, hour1, minute1);
			wheelMain.showYear(false).showMonth(false).showDay(false)
					.showHourWay(true);
			new AlertDialog.Builder(StudyPlanAddPlanActivity.this)
					.setTitle("选择时间")
					.setView(timepickerview1)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									textview_plan_endtime
											.setText(setDataFrom(wheelMain
													.getTime()));
									date2 = wheelMain.getTime();
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
		case R.id.textview_plan_repeat:
			showPopForRepeat(textview_plan_repeat, l_repeat);
			break;
		case R.id.textview_plan_sound:
			showPopForVoice(textview_plan_sound, l_voice);
			break;
		case R.id.textview_plan_remind:
			showPopForRemind(textview_plan_remind, l_remind);
			break;
		default:
			break;
		}
	}

	// 添加计划成功,并且启动闹铃
	private void setClockInfo() {
		ClockInfo info = setClockdData();

		Log.e(TAG, "info--------------->" + info);
		// System.out.println("是否有声音===" + info.getSound());
		// Log.d("跳转要传递的数据为====", info.getNote());
		getCurrentDate();
		if (date1.equalsIgnoreCase("")) {
			date1 = currentDate;
		}
		getCurrentDate();
		long_alarm = getGapDays(currentDate, date1);
		// System.out.println("studyplanaddplanactivity 中  设置的时间为====" +
		// long_alarm);

		// before set alarm we save db
		boolean svResult;
		if (tagusername.equals("游客") || tagyk.equals("1")) {
			svResult = Yk_DbHelper.getInstance(this).save(info);
		} else {
			svResult = DbHelper.getInstance(this).save(info);
		}
		if (svResult) {
			List<ClockInfo> list;
			if (tagusername.equals("游客") || tagyk.equals("1")) {
				list = Yk_DbHelper.getInstance(this).search(
						ClockInfo.class);
			} else {
				list = DbHelper.getInstance(this).search(
						ClockInfo.class);
			}
			if (!list.isEmpty()) {
				info = list.get(list.size() - 1);
				System.out.println("svResult is OK id is " + info.getTitle()
						+ ", " + info.getId());
			}
		}
		intents = new Intent();
		intents.setClass(StudyPlanAddPlanActivity.this, AlarmService.class);
		intents.putExtra("long_alarm", long_alarm);
		intents.putExtra("info", info);
		intents.putExtra("num", pre);
		intents.putExtra("info_title", info.getTitle());
		intents.putExtra("svResult", svResult);
		intents.putExtra("updateId", info.getId());
		startService(intents);
		// 秒 转换成 天 时 分
		// long day_t = long_alarm / 60 / 60 / 24;
		// long hour_t = long_alarm / 60 / 60 % 24;
		// long minute_t = long_alarm % 60;
		// Toast.makeText(StudyPlanAddPlanActivity.this, "服务已启动,闹钟将开启",
		// Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.setClass(StudyPlanAddPlanActivity.this, StudyPlanActivity.class);
		Bundle b = new Bundle();
		b.putSerializable("info", info);
		b.putString("info_title", info.getTitle());
		// clockEntities.get()
		System.out.println("........info_title-----" + info.getTitle());
		intent.putExtras(b);
		setResult(11, intent);
		finish();
	}

	// pop_for repeat
	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	private void showPopForRepeat(final TextView mtextView,
			final List<String> l_test) {
		if (pop_repeat == null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.pop_myself_testreport, null);
			lv_group = (ListView) view
					.findViewById(R.id.pop_listview_myself_result_class);
			PopGroupAdapter groupAdapter = new PopGroupAdapter(this, l_test);
			lv_group.setAdapter(groupAdapter);
			// 创建一个PopuWidow对象
			pop_repeat = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		// 使其聚集
		pop_repeat.setFocusable(true);
		// 设置允许在外点击消失
		pop_repeat.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		pop_repeat.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		@SuppressWarnings("deprecation")
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- pop_repeat.getWidth() / 2;
		Log.i("coder", "xPos:" + xPos);
		// pop_class.showAsDropDown(mtextView, xPos, 0);
		pop_repeat.showAsDropDown(mtextView);
		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				mtextView.setText(l_test.get(position).toString());
				if (pop_repeat != null) {
					pop_repeat.dismiss();
				}
			}
		});
	}

	// pop_for repeat
	@SuppressWarnings("deprecation")
	private void showPopForVoice(final TextView mtextView,
			final List<String> l_test) {
		if (pop_voice == null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.pop_myself_testreport, null);
			lv_group = (ListView) view
					.findViewById(R.id.pop_listview_myself_result_class);
			PopGroupAdapter groupAdapter = new PopGroupAdapter(this, l_test);
			lv_group.setAdapter(groupAdapter);
			// 创建一个PopuWidow对象
			pop_voice = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		// 使其聚集
		pop_voice.setFocusable(true);
		// 设置允许在外点击消失
		pop_voice.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		pop_voice.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- pop_voice.getWidth() / 2;
		Log.i("coder", "xPos:" + xPos);
		// pop_class.showAsDropDown(mtextView, xPos, 0);
		pop_voice.showAsDropDown(mtextView);
		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				mtextView.setText(l_test.get(position).toString());
				if (pop_voice != null) {
					pop_voice.dismiss();
				}
			}
		});
	}

	// pop_for repeat
	@SuppressWarnings("deprecation")
	private void showPopForRemind(final TextView mtextView,
			final List<String> l_test) {
		if (pop_remind == null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater
					.inflate(R.layout.pop_studyplan_addplan_remind, null);
			lv_group = (ListView) view
					.findViewById(R.id.pop_listview_myself_result_class);
			PopGroupAdapter groupAdapter = new PopGroupAdapter(this, l_test);
			lv_group.setAdapter(groupAdapter);
			// 创建一个PopuWidow对象
			pop_remind = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		// 使其聚集
		pop_remind.setFocusable(true);
		// 设置允许在外点击消失
		pop_remind.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		pop_remind.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- pop_remind.getWidth() / 2;
		Log.i("coder", "xPos:" + xPos);
		pop_remind.showAsDropDown(mtextView);
		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				mtextView.setText(l_test.get(position).toString());
				String remind1 = l_test.get(position).toString();
				int num = 0;
				if (remind1.equals("无")) {

				} else if (remind1.equals("提前10分钟")) {
					num = Integer.parseInt(remind1.substring(2, 4));
					System.out.println("=======num==========" + num);
				} else if (remind1.equals("提前30分钟")) {
					num = Integer.parseInt(remind1.substring(2, 4));
					System.out.println("=======num==========" + num);
				} else if (remind1.equals("提前1小时")) {
					num = Integer.parseInt(remind1.substring(2, 3)) * 60;
					System.out.println("=======num==========" + num);
				} else if (remind1.equals("提前1天")) {
					num = Integer.parseInt(remind1.substring(2, 3)) * 60 * 24;
					System.out.println("=======num==========" + num);
				}
				pre = num * 60;
				if (pop_remind != null) {
					pop_remind.dismiss();
				}
			}
		});
	}

	// set alarm
	@SuppressWarnings("unused")
	private void setAlarm() {
		calendar = Calendar.getInstance();
		/* 建立Intent和PendingIntent，来调用目标组件 */
		Intent intent = new Intent();
		intent.setClass(StudyPlanAddPlanActivity.this, AlarmReceiver.class);
		PendingIntent pendingintent = PendingIntent.getBroadcast(
				StudyPlanAddPlanActivity.this, 0, intent, 0);
		AlarmManager am;
		/* 获取管理的实例 */
		am = (AlarmManager) getSystemService(ALARM_SERVICE);
		// 设置闹钟
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				pendingintent);
		// 设置周期闹钟
		am.setRepeating(AlarmManager.RTC_WAKEUP,
				SystemClock.currentThreadTimeMillis() + 10 * 1000,
				24 * 60 * 60 * 1000, pendingintent);
		String tempS = "设置的闹铃时间为" + format(4) + ":" + format(18);
		System.out.println(tempS);
	}

	/* 格式化字符串(7:3->07:03) */
	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}

	/* 格式化字符串(7:3->07:03) */
	private int formattoint(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return Integer.valueOf(s);
	}

	/**
	 * 获得给定两个日期相差度天数 create date:2009-6-23 author:Administrator
	 * 
	 * @param date1
	 *            参数格式:2009-06-21 开始时间为=2015-07-28 18:16结束时间为=========2015-07-28
	 *            18:16 currentDate
	 * @param date2
	 */

	public static long getGapDays(String date1, String date2) {

		System.out.println("获取当前的日期为===" + date1 + "---" + date2);
		String[] d1 = date1.split("-");
		String[] d2 = date2.split("-");
		Calendar c = Calendar.getInstance();

		long currentSec = System.currentTimeMillis() / 1000 % 60;
		c.set(Integer.parseInt(d1[0]), Integer.parseInt(d1[1]),
				Integer.parseInt(d1[2]), Integer.parseInt(d1[3]),
				Integer.parseInt(d1[4]), (int) currentSec);
		long l1 = c.getTimeInMillis();
		c.set(Integer.parseInt(d2[0]), Integer.parseInt(d2[1]),
				Integer.parseInt(d2[2]), Integer.parseInt(d2[3]),
				Integer.parseInt(d2[4]), 0);
		long l2 = c.getTimeInMillis();
		// long poor = (Math.abs(l1 - l2) / (24 * 60 * 60 * 1000));
		long poor = Math.abs(l1 - l2) / 1000;
		System.out.println("相隔的时间为===" + poor + "秒");
		return poor;
	}

	// set clock date
	private ClockInfo setClockdData() {
		String title = edittext_plan_title.getText().toString();

		String start_time = date1;
		String end_time = date2;

		String repeat = textview_plan_repeat.getText().toString();
		String sound = textview_plan_sound.getText().toString();
		String remind = textview_plan_remind.getText().toString();
		String note = edittext_plan_note.getText().toString();

		ClockInfo info = new ClockInfo();
		info.setId(Integer.valueOf(c_id));
		info.setTitle(title);
		info.setStarttime(start_time);
		info.setEndtime(end_time);
		info.setRepeat(repeat);
		info.setSound(sound);
		info.setRemind(remind);
		info.setNote(note);
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("title", title);
		// map.put("start_time", start_time);
		// map.put("end_time", end_time);
		// map.put("repeat", repeat);
		// map.put("sound", sound);
		// map.put("remind", remind);
		// map.put("note", note);

		System.out.println("要分割的数据为=================" + start_time);

		String[] ss = start_time.split(" ");
		String time_ss = ss[0];

		String select_t = String.valueOf(time_ss.split("-")[0])
				+ "-"
				+ String.valueOf(format(Integer.valueOf(time_ss.split("-")[1])))
				+ "-"
				+ String.valueOf(format(Integer.valueOf(time_ss.split("-")[2])));

		System.out.println("---123456---"
				+ Integer.valueOf(time_ss.split("-")[4]));

		info.setSelect_time(select_t);
		clockEntities.add(info);
		System.out.println("----总数据---" + info);
		System.out.println(info.getEndtime());
		return info;
	}

	private String setDataFrom(String datas) {
		String[] s = datas.split("-");
		System.out.println("---111111----" + s[4]);
		if (Integer.parseInt(s[4]) < 10) {
			String dataf = s[0] + "-" + s[1] + "-" + s[2] + " " + s[3] + ":"
					+ "0" + s[4];
			return dataf;
		} else {
			String dataf = s[0] + "-" + s[1] + "-" + s[2] + " " + s[3] + ":"
					+ s[4];
			return dataf;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 取消注册
		EventBus.getDefault().unregister(this);
	}
}
