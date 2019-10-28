package com.lelts.student.myself;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.strudentlelts.R;
import com.lelts.tool.IntentUtlis;


public class MystudentMessageDetailActivity extends Activity implements OnClickListener{


	private static final String TAG = "MystudentMessageActivity";

	private Context mContext;
	private TextView textview_message_detail;
	private ImageView imageview_back;
	
	private String name;
	private String time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_mymessage_detail);
		getdatafromintent();
		init();

	}

	private void getdatafromintent() {
		Bundle b  = getIntent().getExtras();
		name = b.getString("Body");
		time = b.getString("CreateTime");
	}

	private void init() {
		mContext = this;
		textview_message_detail = (TextView) findViewById(R.id.textview_message_detail);
		
		textview_message_detail.setText(name);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
//			IntentUtlis.sysStartActivity(mContext, MystudentMessageActivity.class);
			finish();
			break;

		default:
			break;
		}
	}




}
