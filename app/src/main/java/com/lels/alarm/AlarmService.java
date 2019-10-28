package com.lels.alarm;

import java.util.List;

import com.lels.main.activity.StudyPlanAddPlanActivity;
import com.lelts.student.db.ClockInfo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {

	private AlarmManager alarmManager;
	private PendingIntent pendIntent;
	private long long_alarm_s;
	private int num;
	ClockInfo info = new ClockInfo();
	private AlarmSerReceiver receiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		receiver = new AlarmSerReceiver();
		IntentFilter filter = new IntentFilter("com.lels.alarm.del");
		this.registerReceiver(receiver, filter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flags = START_STICKY;
		try {
			if (intent.getBooleanExtra("svResult", false)) {
				// 多长时间后提醒
				long_alarm_s = intent.getLongExtra("long_alarm", 10);
				num = intent.getIntExtra("num", 0);
				System.out.println("long_alarm_s==== " + long_alarm_s+ " -----num" + num);
				info = (ClockInfo) intent.getSerializableExtra("info");
				intent.putExtra("info_title1", info.getTitle());
				intent.setClass(this, AlarmActivity.class);

				int alarmId = intent.getIntExtra("updateId", 0);
				System.out.println(" 闹铃的编号是：" + alarmId);
				pendIntent = PendingIntent.getActivity(getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + long_alarm_s * 1000 - num * 1000 ,
						pendIntent);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	
	class AlarmSerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String alarmId = intent.getStringExtra("delId");
			Intent my = new Intent();
			my.setClass(AlarmService.this, AlarmActivity.class);
			System.out.println(" 删除闹铃的编号是：" + alarmId);
			try {
				PendingIntent pendIntent = PendingIntent.getActivity(getApplicationContext(), Integer.parseInt(alarmId), my, PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.cancel(pendIntent);
			} catch (Exception e) {
				System.out.println(" 删除闹铃异常");
			}
			
		}
		
	}

	@Override
	public void onDestroy() {
		alarmManager.cancel(pendIntent);
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}
}