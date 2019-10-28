package com.lels.student.chatroom.activity;

import com.example.strudentlelts.R;
import com.lels.main.activity.LoginActvity;
import com.lelts.tool.ImageLoder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class KickOffDialog extends Activity implements OnClickListener{
	private SharedPreferences stushare;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.ry_kickoff);
		View confirm = findViewById(R.id.ry_kickoff_confirm);
		confirm.setOnClickListener(this);
		stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		Editor editor = stushare.edit();
		editor.putString("userPass", "");
		editor.commit();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		Toast.makeText(this, "click", Toast.LENGTH_LONG).show();
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*if (KeyEvent.KEYCODE_BACK == event.getAction()) {
			return true;
		}*/
		return true;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.ry_kickoff_confirm:
//				Toast.makeText(this, "right", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(KickOffDialog.this, LoginActvity.class);
				startActivity(intent);
				ImageLoder app = (ImageLoder) getApplication();
				app.finishAllDelay();
				finish();
				break;
			default:
				break;
		}
	}
}
