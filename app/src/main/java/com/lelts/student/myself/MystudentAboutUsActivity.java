package com.lelts.student.myself;

import com.example.strudentlelts.R;
import com.lelts.tool.IntentUtlis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MystudentAboutUsActivity extends Activity implements OnClickListener{
	
	private ImageButton img_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_myaboutus);
		initview();
	}
	
	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		img_back = (ImageButton) findViewById(R.id.imageview_back);
		img_back.setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnCli ckListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//返回键
		case R.id.imageview_back:
			finish();
			break;

		default:
			break;
		}
	}
	
	

}
