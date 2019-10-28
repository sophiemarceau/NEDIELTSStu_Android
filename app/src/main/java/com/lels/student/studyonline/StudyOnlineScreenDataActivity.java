package com.lels.student.studyonline;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.example.strudentlelts.R;
import com.lels.student.studyonline.adapter.ScreenDateAdapter;
import com.lelts.tool.PrintTool;
import com.widget.time.JudgeDate;
import com.widget.time.ScreenInfo;
import com.widget.time.WheelMain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class StudyOnlineScreenDataActivity extends Activity implements OnClickListener {

	private static final String TAG = "StudyOnlineScreenDataActivity";

	private ImageButton imageview_back;
	private TextView textview_screen_ok;
	private String token = "";
	private GridView gridview_screen_date;
	private GridView gridview_screen_date_source;
	private GridView gridview_screen_date_time;

	private ScreenDateAdapter adapter, adapter1, adapter2, adapter3;
	private List<HashMap<String, Object>> l_map, l_map2, l_map3;

	private int position_format_selected = 0;
	private int position_source_selected = 0;
	private int position_time_selected = -1;

	private String[] str = { "不限", "口语", "阅读", "词汇", "写作", "综合", "听力", "语法" };

	private String[] str2 = { "不限", "PPT", "PDF", "Word", "Xlsx" };

	private String[] str3 = { "本周", "本月", "本年" };

	private String[] str_code = { "0", "KY", "YD", "CH", "XZ", "ZH", "TL", "YF" };
	private String[] str2_code = { "0", "3", "4", "1", "2" };
	private String[] str3_code = { "0", "1", "2", "3" };

	// beginDate=[开始日期]
	// endDate=[结束日期]

	private String nameCode1, fileType1, timeType1;

	private String beginDate, endDate;

	private TextView edittext_starttime;
	private TextView edittext_endtime;

	private PrintTool print;

	// 设置 时间选择 器的选择
	WheelMain wheelMain;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studyonline_screen_studydate_test);

		getDataFromShare();

		print = new PrintTool(StudyOnlineScreenDataActivity.this);
		getDataDromIntent();
		initView();
		initData();
		initGridView0();
		initGridView2();
		initGridView3();

		initPosition();
	}

	private void getDataFromShare() {
		SharedPreferences share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");
		Log.d(TAG, "获取的token数值为=====" + token);

	}

	private void getDataDromIntent() {
		Bundle b = getIntent().getExtras();
		nameCode1 = b.getString("fileType1");
		fileType1 = b.getString("roleId1");
		timeType1 = b.getString("uploadYear1");

		beginDate = b.getString("beginDate");
		endDate = b.getString("endDate");

		for (int i = 0; i < str_code.length; i++) {
			if (nameCode1.equalsIgnoreCase(str_code[i])) {
				position_format_selected = i;
			}
		}

		for (int i = 0; i < str2_code.length; i++) {
			if (fileType1.equalsIgnoreCase(str2_code[i])) {
				position_source_selected = i;
			}
		}

		timeType1 = String.valueOf(Integer.valueOf(Integer.valueOf(timeType1) - 1));

		for (int i = 0; i < str3_code.length; i++) {
			if (timeType1.equalsIgnoreCase(str3_code[i])) {
				position_time_selected = i;
			}
		}

		System.out.println("nameCode==" + nameCode1);
		System.out.println("nameCode==" + fileType1);
		System.out.println("nameCode==" + timeType1);

	}

	private void initPosition() {
		// adapter 1 2
		// edittext_starttime.

		adapter.updataforgridview(position_format_selected);
		adapter1.updataforgridview(position_source_selected);
		if (position_time_selected != 0) {
			adapter2.updataforgridview(position_time_selected);
		}

		/*edittext_starttime.setText(beginDate);
		edittext_endtime.setText(endDate);*/

	}

	private void initView() {

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		textview_screen_ok = (TextView) findViewById(R.id.textview_screen_ok);

		gridview_screen_date = (GridView) findViewById(R.id.gridview_screen_date);
		gridview_screen_date_source = (GridView) findViewById(R.id.gridview_screen_date_source);
		gridview_screen_date_time = (GridView) findViewById(R.id.gridview_screen_date_time);

		edittext_starttime = (TextView) findViewById(R.id.edittext_starttime);
		edittext_endtime = (TextView) findViewById(R.id.edittext_endtime);

		Calendar calendar = Calendar.getInstance();
		// edittext_starttime.setText(calendar.get(Calendar.YEAR) + "-"
		// + (calendar.get(Calendar.MONTH) + 1) + "-"
		// + calendar.get(Calendar.DAY_OF_MONTH) + "");
		//
		// edittext_endtime.setText(calendar.get(Calendar.YEAR) + "-"
		// + (calendar.get(Calendar.MONTH) + 1) + "-"
		// + calendar.get(Calendar.DAY_OF_MONTH) + "");

		edittext_starttime.setOnClickListener(this);
		edittext_endtime.setOnClickListener(this);

		imageview_back.setOnClickListener(this);
		textview_screen_ok.setOnClickListener(this);
	}

	private void initData() {

		l_map = new ArrayList<HashMap<String, Object>>();

		// String[] str = { "不限", "WORD", "excel", "PPT", "PDF", "视频资料" };

		for (int i = 0; i < str.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("screenkey", str[i]);
			l_map.add(map);
		}

		l_map2 = new ArrayList<HashMap<String, Object>>();

		// String[] str2 = { "不限","集团", "个人" };

		for (int i = 0; i < str2.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("screenkey", str2[i]);
			l_map2.add(map);
		}

		l_map3 = new ArrayList<HashMap<String, Object>>();

		// String[] str3 = { "不限","2016", "2015","2014","2013" };

		for (int i = 0; i < str3.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("screenkey", str3[i]);
			l_map3.add(map);
		}

	}

	private void initGridView0() {

		adapter = new ScreenDateAdapter(StudyOnlineScreenDataActivity.this, l_map, position_format_selected);
		gridview_screen_date.setAdapter(adapter);
		gridview_screen_date.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				position_format_selected = position;
				RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.relative_screen_background);
				// relative.setBackground(getResources().getDrawable(
				// R.drawable.icon_screen_selected));
				relative.setBackgroundResource(R.drawable.icon_screen_selected);

				print.printforLog(TAG, "onitemclick" + position);
				adapter = new ScreenDateAdapter(StudyOnlineScreenDataActivity.this, l_map, position_format_selected);
				gridview_screen_date.setAdapter(adapter);

			}
		});

	}

	private void initGridView2() {
		adapter1 = new ScreenDateAdapter(StudyOnlineScreenDataActivity.this, l_map2, position_source_selected);
		gridview_screen_date_source.setAdapter(adapter1);

		gridview_screen_date_source.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				position_source_selected = position;
				RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.relative_screen_background);
				// relative.setBackground(getResources().getDrawable(
				// R.drawable.icon_screen_selected));
				relative.setBackgroundResource(R.drawable.icon_screen_selected);

				print.printforLog(TAG, "onitemclick" + position);

				adapter1 = new ScreenDateAdapter(StudyOnlineScreenDataActivity.this, l_map2, position_source_selected);
				gridview_screen_date_source.setAdapter(adapter1);

			}
		});

	}

	private void initGridView3() {
		adapter2 = new ScreenDateAdapter(StudyOnlineScreenDataActivity.this, l_map3, position_time_selected);
		gridview_screen_date_time.setAdapter(adapter2);

		gridview_screen_date_time.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				edittext_starttime.setText("");
				edittext_endtime.setText("");
				position_time_selected = position;
				RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.relative_screen_background);
				relative.setBackgroundResource(R.drawable.icon_screen_selected);

				print.printforLog(TAG, "onitemclick" + position);

				adapter2 = new ScreenDateAdapter(StudyOnlineScreenDataActivity.this, l_map3, position_time_selected);
				gridview_screen_date_time.setAdapter(adapter2);

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.textview_screen_ok:
			
			
			
			
			Intent intent = new Intent();
			// beginDate=[开始日期]
			// endDate=[结束日期]
			beginDate = edittext_starttime.getText().toString();
			endDate = edittext_endtime.getText().toString();

			intent.setClass(StudyOnlineScreenDataActivity.this, StudyOnlineActivity.class);
			Bundle b = new Bundle();

			b.putString("fileType1", str_code[position_format_selected]);
			b.putString("roleId1", str2_code[position_source_selected]);

			b.putString("uploadYear1", str3_code[position_time_selected + 1]);

			b.putString("beginDate1", beginDate);
			b.putString("endDate1", endDate);

			intent.putExtras(b);

			// System.out.println(str_code[position_format_selected] + " "
			// + str2_code[position_source_selected] + " "
			// + str3_code[position_time_selected]);

			setResult(11, intent);
			finish();
			break;
		case R.id.edittext_starttime:
			position_time_selected=-1;
			adapter2 = new ScreenDateAdapter(StudyOnlineScreenDataActivity.this, l_map3, position_time_selected);
			gridview_screen_date_time.setAdapter(adapter2);
			System.out.println("点击了开始时间");
			
			LayoutInflater inflater = LayoutInflater.from(StudyOnlineScreenDataActivity.this);
			
			final View timepickerview = inflater.inflate(R.layout.timepicker, null);
			ScreenInfo screenInfo = new ScreenInfo(StudyOnlineScreenDataActivity.this);
			wheelMain = new WheelMain(timepickerview);
			wheelMain.screenheight = screenInfo.getHeight();
			String time = edittext_starttime.getText().toString();
			System.out.println("字符串时间----" + time);

			Calendar calendar = Calendar.getInstance();
			if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
				try {
					calendar.setTime(dateFormat.parse(time));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			wheelMain.initDateTimePicker(year, month, day);
			new AlertDialog.Builder(StudyOnlineScreenDataActivity.this).setTitle("选择时间").setView(timepickerview)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							edittext_starttime.setText(wheelMain.getTime());
							
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();

			break;
		case R.id.edittext_endtime:
			
			LayoutInflater inflater1 = LayoutInflater.from(StudyOnlineScreenDataActivity.this);
			final View timepickerview1 = inflater1.inflate(R.layout.timepicker, null);
			ScreenInfo screenInfo1 = new ScreenInfo(StudyOnlineScreenDataActivity.this);
			wheelMain = new WheelMain(timepickerview1);
			wheelMain.screenheight = screenInfo1.getHeight();
			String time1 = edittext_starttime.getText().toString();
			Calendar calendar1 = Calendar.getInstance();
			if (JudgeDate.isDate(time1, "yyyy-MM-dd")) {
				try {
					calendar1.setTime(dateFormat.parse(time1));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int year1 = calendar1.get(Calendar.YEAR);
			int month1 = calendar1.get(Calendar.MONTH);
			int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
			wheelMain.initDateTimePicker(year1, month1, day1);
			new AlertDialog.Builder(StudyOnlineScreenDataActivity.this).setTitle("选择时间").setView(timepickerview1)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							edittext_endtime.setText(wheelMain.getTime());
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
			break;
		default:
			break;
		}
	}
}
