package com.lels.student.testpredictions.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strudentlelts.R;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.student.studyonline.DataStudyDetailActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class TestPredictionsWebActivity extends Activity implements OnClickListener {

	private static final String TAG = "TestPredictionsWebActivity";

	private ImageButton imageview_back;
	private WebView webview_testPrediction;
	private String url;
	private String MF_ID;
	private String ID;
	private String content;
	private String optType = "1";
	private String token;
	// 判断加载的 url还是html
	private int type;
	private TextView textview_collect_predict;
	// 标题
	private TextView txtTitle;
	private String Title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testpredictions_url);
		init();
		getDataFromIntent();
		getDataFromShare();
		initView(type);
	}

	private void init() {
		textview_collect_predict = (TextView) findViewById(R.id.textview_collect_predict);
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		webview_testPrediction = (WebView) findViewById(R.id.webview_testprediction);
		txtTitle = (TextView) findViewById(R.id.textView1);
		LodDialogClass.showCustomCircleProgressDialog(TestPredictionsWebActivity.this, null, getString(R.string.common_Loading));
	}

	private void getDataFromIntent() {
		Bundle b = getIntent().getExtras();
		url = b.getString("Link");
		MF_ID = b.getString("MF_ID");
		ID = b.getString("ID");
		content = b.getString("Content");
		Title = b.getString("Title");
		System.out.println("传过来的标题==="+Title);
		
		txtTitle.setText(Title);
		System.out.println("content文本内容===="+content);
		//显示WebView url
				if(content.equals("")||content.length()==0){
					type =1;
				}else{
				//显示html	
					type=2;
				}
		txtTitle.setText(Title);
		System.out.println("content文本内容====" + content);
		// 显示WebView url
		if (content.equals("") || content.length() == 0) {
			type = 1;
		} else {
			// 显示html
			type = 2;
		}
		System.out.println("传递过来的url为====" + url);
		System.out.println("MF_ID====" + MF_ID);

		if (MF_ID.equalsIgnoreCase("null")) {
			System.out.println("MF_ID为空,可以收藏 ");
			optType = "1";
			textview_collect_predict.setText("收藏");
		} else {
			System.out.println("MF_ID不为空 ");
			optType = "0";
			textview_collect_predict.setText("取消收藏");
		}
		System.out.println("optType===========" + optType);
	}

	private void getDataFromShare() {
		SharedPreferences share = TestPredictionsWebActivity.this.getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		token = share.getString("Token", "");
		Log.d(TAG, "获取的token数值为=====" + token);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView(int type) {

		WebSettings webSettings = webview_testPrediction.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		
		webview_testPrediction.getSettings().setDefaultTextEncodingName("utf-8");
		// 加载需要显示的网页
		if (type == 1) {
			// 加载url
			webview_testPrediction.loadUrl(url);
		} else {
			// 加载 html 解决乱码
			webview_testPrediction.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
			// webview_testprediction.loadData(Content, "text/html", "utf-8");
		}
		// 设置Web视图
		webview_testPrediction.setWebViewClient(new webViewClient());
		imageview_back.setOnClickListener(this);
		textview_collect_predict.setOnClickListener(this);
	}

	// Web视图
	private class webViewClient extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			LodDialogClass.closeCustomCircleProgressDialog();
		}
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview_testPrediction.canGoBack()) {
			webview_testPrediction.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		finish();// 结束退出程序
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			// Intent intent = new
			// Intent(TestPredictionsWebActivity.this,TestPredictionsActivity.class);
			// startActivity(intent);
			finish();
			break;
		case R.id.textview_collect_predict:
			setcollectdata();
			break;
		default:
			break;
		}
	}

	/**
	 * 收藏功能
	 */
	private void setcollectdata() {

		Log.d(TAG, "getdatafromnet()执行");
		@SuppressWarnings("static-access")
		String url = new Constants().URL_PREDICT_COLLECT_DATA;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西
		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("pId", ID);
		params.addBodyParameter("optType", optType);
		System.out.println("pId========" + ID + "=====optType===" + optType);
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Log.d(TAG, "getdatafromnet()执行之接收数据成功");
				Log.d(TAG, "预测资料的数据结果为=========" + responseInfo.result);
				try {
					JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");

					if (optType.equalsIgnoreCase("1")) {
						Toast.makeText(TestPredictionsWebActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
						optType = "0";
						textview_collect_predict.setText("取消收藏");
					} else {
						Toast.makeText(TestPredictionsWebActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
						optType = "1";
						textview_collect_predict.setText("收藏");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("onFailure");
				Log.d(TAG, "请求失败原因" + error.toString());
			}
		});
	}
}
