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
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.strudentlelts.R;
import com.lels.bean.LodDialogClass;

public class StudyADVDetailActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "StudyADVDetailActivity";

	private ImageButton imageview_back;
	
	private TextView textview_adv_title;

	private WebView webview_data_class_video;

	private String url_video = "";
	private String Title;
	
	private String Content;
	//判断加载的 url还是html
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_studyonline_adv_detail);
		
		init();
		getdatafromintent();
	}

	private void getdatafromintent() {
		Bundle b = getIntent().getExtras();
		url_video = b.getString("url");
		Title = b.getString("Title");
		
		textview_adv_title.setText(Title);
		
		Content = b.getString("Content");
		textview_adv_title.setText(Title);
		//显示WebView url
		if(Content.equals("")||Content.length()==0){
			type =1;
			
		}else{
		//显示html	
			type=2;
		}
		
		setwebview();

	}


	@SuppressWarnings("static-access")
	private void init() {

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		
		textview_adv_title = (TextView) findViewById(R.id.textview_adv_title);

		webview_data_class_video = (WebView) findViewById(R.id.webview_data_class_video);

//		setwebview(webview_data_class_video, url_video);
	
		imageview_back.setOnClickListener(this);
		LodDialogClass.showCustomCircleProgressDialog(StudyADVDetailActivity.this, null, getString(R.string.common_Loading));
	}

//	private void setwebview(WebView wv, String url) {
//		wv.getSettings().setJavaScriptEnabled(true);
//		wv.getSettings().setPluginState(PluginState.ON);
//		wv.setEnabled(true);
//		wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//		wv.getSettings().setAllowFileAccess(true);
//		wv.getSettings().setDefaultTextEncodingName("UTF-8");
//		wv.getSettings().setLoadWithOverviewMode(true);
//		wv.getSettings().setUseWideViewPort(true);
//		wv.setVisibility(View.VISIBLE);
//		wv.loadUrl(url);
//	}
	@SuppressLint("SetJavaScriptEnabled")
	private void setwebview(){
		WebSettings settings = webview_data_class_video.getSettings();
		settings.setJavaScriptEnabled(true);
		webview_data_class_video.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				LodDialogClass.closeCustomCircleProgressDialog();
			}
	        public boolean shouldOverrideUrlLoading(WebView view, String url)
	                               { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
	                                       view.loadUrl(url);
	                                       return true;
	                               }
	        });
		webview_data_class_video.getSettings().setDefaultTextEncodingName("utf-8") ;
		// 加载需要显示的网页
		if(type ==1){
			
			//加载url
			webview_data_class_video.loadUrl(url_video);
		}else{
			//加载 html 解决乱码 
			webview_data_class_video.loadDataWithBaseURL(null, Content, "text/html", "utf-8", null);
			//webview_testprediction.loadData(Content, "text/html", "utf-8");
			
		}
		// 优先 使用缓存 
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// 不 使用缓存 
//		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		
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

	@Override
	protected void onPause() {
		webview_data_class_video.reload();
		Log.d(TAG, "onpause");
		super.onPause();
	}

	@Override
	protected void onStop() {

		webview_data_class_video.stopLoading();
		Log.d(TAG, "onStop");
		super.onStop();

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		webview_data_class_video.destroy();
	}
	
	//改写物理按键——返回的逻辑
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if(keyCode==KeyEvent.KEYCODE_BACK)
//        {
//            if(webview_data_class_video.canGoBack())
//            {
//            	webview_data_class_video.goBack();//返回上一页面
//                return true;
//            }
//            else
//            {
//                System.exit(0);//退出程序
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
