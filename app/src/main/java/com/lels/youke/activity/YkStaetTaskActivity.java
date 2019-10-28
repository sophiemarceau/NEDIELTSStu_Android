package com.lels.youke.activity;

import com.example.strudentlelts.R;
import com.lels.main.activity.MainActivity;
import com.lelts.tool.IntentUtlis;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class YkStaetTaskActivity extends Activity {
	private ImageButton image_back;
	private TextView tv_total;
	private int total,t,thread_t;
	private Handler handler;
	private SharedPreferences stushare;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yktask);
		initview();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				int pro = (Integer) msg.obj;
				tv_total.setText(pro + "");
			};
		};
		thread.start();
		
		
		image_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					finish();
			}
		});
	}
	private void initview() {
		stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		String b = stushare.getString("isVistor", "");
		if (b.equals("1")) {
			total = stushare.getInt("DownMaterialsCount", 0);	
		}else{
			total = 0;
		}
		
		image_back = (ImageButton) findViewById(R.id.yk_task_back_img);
		tv_total = (TextView) findViewById(R.id.tv_score);
		double tv_totals = Math.random();// 0-9随机数
		if (total == 1) {
			t = (int)(tv_totals * (20 - 10) + 10);
		} else if (total == 2) {
			t = (int) (tv_totals * (40 - 21) + 21);
		} else if (total >= 3 && total <= 5) {
			t = (int) (tv_totals * (60 - 41) + 41);
		} else if (total > 5 && total <= 10) {
			t = (int) (tv_totals * (80 - 61) + 61);
		} else if (total > 10) {
			t = (int) (tv_totals * (99 - 81) + 81);
		}else{
			t=(int) (tv_totals * (9 - 0) + 0);
		}
	
		
	}
	Thread thread = new Thread(){
		public void run() {
			while (thread_t<t) {
				thread_t+=2;
				Message mss = new Message();
				mss.obj = thread_t;
				handler.handleMessage(mss);
			}
		};
	};
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
