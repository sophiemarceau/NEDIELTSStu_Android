package com.lels.student.studyonline;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.strudentlelts.R;

public class StudyADVDTEXTetailActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "StudyADVDTEXTetailActivity";

	private ImageButton imageview_back;
	
	private TextView textview_adv_title;
	
	private WebView web_textview_content;

	private String url_video = "";
	private String Title;
	private String Content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studyonline_adv_text_detail);
		
		init();
		getdatafromintent();
	}

	private void getdatafromintent() {
		Bundle b = getIntent().getExtras();
		url_video = b.getString("url");
		Title = b.getString("Title");
		Content = b.getString("Content");
		
		textview_adv_title.setText(Title);
		
		web_textview_content.loadUrl(Content);

	}


	@SuppressWarnings("static-access")
	private void init() {

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		
		textview_adv_title = (TextView) findViewById(R.id.textview_adv_title);
		
		web_textview_content = (WebView) findViewById(R.id.textview_content);
		

	
		imageview_back.setOnClickListener(this);

	}


	/**
	 * 收藏功能
	 * */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		default:
			break;
		}
	}



}
