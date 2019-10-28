package com.lels.main.activity;

import com.example.strudentlelts.R;
import com.lels.alarm.AlarmService;
import com.lelts.student.db.ClockInfo;
import com.lelts.student.db.DbHelper;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

public class MyselfRemindDeatilActivity extends Activity implements OnClickListener {

	private ImageButton imageview_back;
	private Button button_delMark_details;
	private TextView edittext_plan_title;
	private TextView textview_plan_starttime;
	private TextView textview_plan_repeat;
	private TextView textview_plan_sound;
	private TextView textview_plan_remind;

	private TextView edittext_plan_note;
	// private View view;

	private TextView textview_plan_endtime;

	private ClockInfo info = new ClockInfo();
	private String c_id;
	public DbHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_remind_details);
		getDataFromIntent();
		init();
		initData();
	}

	private void getDataFromIntent() {
		info = (ClockInfo) getIntent().getExtras().getSerializable("delete");
		c_id = (String) getIntent().getExtras().getString("c_id");
//		System.out.println("传递过来的 info id ====" + info.getId());
//		System.out.println("传递过来的 info id ====" + info.getNote());
	}

	private void init() {
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		button_delMark_details = (Button) findViewById(R.id.button_delmark_details);
		edittext_plan_title = (TextView) findViewById(R.id.edittext_plan_title);

		textview_plan_starttime = (TextView) findViewById(R.id.textview_plan_starttime);
		textview_plan_repeat = (TextView) findViewById(R.id.textview_plan_repeat);
		textview_plan_sound = (TextView) findViewById(R.id.textview_plan_sound);
		textview_plan_remind = (TextView) findViewById(R.id.textview_plan_remind);
		edittext_plan_note = (TextView) findViewById(R.id.edittext_plan_note);

		textview_plan_endtime = (TextView) findViewById(R.id.textview_plan_endtime);

		imageview_back.setOnClickListener(this);
		button_delMark_details.setOnClickListener(this);
	}

	private void initData() {

		edittext_plan_title.setText(info.getTitle());

		textview_plan_starttime.setText(setdatafrom(info.getStarttime()));
		textview_plan_endtime.setText(setdatafrom(info.getEndtime()));

		textview_plan_repeat.setText(info.getRepeat());
		textview_plan_sound.setText(info.getSound());
		textview_plan_remind.setText(info.getRemind());
		edittext_plan_note.setText(info.getNote());
	}

	private String setdatafrom(String datas) {

		String[] s = datas.split("-");
		String dataf = s[0] + "-" + s[1] + "-" + s[2] + " " + s[3] + ":" + s[4];
		return dataf;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.button_delmark_details:
			// dbHelper = .dbHelper;
			Log.d("TAG", String.valueOf(info.getId()));
			StudyPlanActivity.delete(c_id);
			System.out.println("删除闹铃时获取的id号：" + c_id);
			//进行发送
//			com.lels.eventbus.StartService startService = new com.lels.eventbus.StartService();
//			EventBus.getDefault().post(startService);
//			AlarmService s = new AlarmService();
//			s.delAlarm();
//			Toast.makeText(MyselfRemindDeatilActivity.this, "该闹钟已关闭", Toast.LENGTH_SHORT).show();
			Intent delIntent = new Intent("com.lels.alarm.del");
			delIntent.putExtra("delId", c_id);
			this.sendBroadcast(delIntent);
			System.out.println("==========该闹钟已关闭");
			Intent intent = new Intent();
			intent.setClass(MyselfRemindDeatilActivity.this, StudyPlanActivity.class);
			setResult(10, intent);
			finish();
			break;
		default:
			break;
		}
	}
}
