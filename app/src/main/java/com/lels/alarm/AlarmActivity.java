package com.lels.alarm;

import com.example.strudentlelts.R;
import com.lelts.student.db.ClockInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

public class AlarmActivity extends Activity {

	MediaPlayer alarmMusic;
	WakeLock mWakelock;
	Vibrator mVibrator;
	public static AlarmActivity instance = null;

	private ClockInfo info;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance = this;

		info = (ClockInfo) getIntent().getSerializableExtra("info");
		String info_title = getIntent().getStringExtra("info_title1");

//		System.out.println("33333知道6666info_title====" + info_title);
//		System.out.println("是否有声音=======" + info.getSound());
//		System.out.println("wsf=======" + info);

		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		// 获取震动
//		mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

		// 加载指定音乐，并为之创建MediaPlayer对象
		alarmMusic = MediaPlayer.create(this, R.raw.fallbackring);
		alarmMusic.setLooping(true);
//		mVibrator.vibrate(alarmMusic.getDuration());

		if (info.getSound().toString().trim().equals("无")) {
			System.out.println("无声音++++" + info.getSound().toString().trim());
		} else {
			// 播放音乐
			System.out.println("有声音+++" + info.getSound().toString().trim());
			alarmMusic.start();
		}
		// // 播放音乐
		// alarmMusic.start();

		// 创建一个对话框
		new AlertDialog.Builder(AlarmActivity.this).setTitle("个人提醒")
				.setMessage(info.getTitle())
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// 停止音乐
						alarmMusic.stop();
						// 停止震动
//						mVibrator.cancel();
						// 结束该Activity
						AlarmActivity.this.finish();
					}
				}).show();
	}	
}
