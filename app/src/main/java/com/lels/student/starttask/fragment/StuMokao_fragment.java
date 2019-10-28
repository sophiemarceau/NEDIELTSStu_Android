package com.lels.student.starttask.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.webkit.WebSettings.ZoomDensity;

@SuppressLint({ "SetJavaScriptEnabled", "InflateParams" })
public class StuMokao_fragment extends Fragment {
	private WebView webview;
	private SharedPreferences share;
	private RelativeLayout rewu_mok;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initview();
		getHttpsArgs();

	}

	// 判断是否有模考
	private void getHttpsArgs() {
		// TODO Auto-generated method stub
		String path = Constants.URL_Task_getLastExam;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onfailure");
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("[][[][]--"+result);
				try {
					JSONObject obj = new JSONObject(result);
					String Infomation = obj.getString("Infomation");
					String Result = obj.getString("Result");
					if (Result.equals("true")) {
						JSONObject Data = obj.getJSONObject("Data");
						String checkData = Data.getString("checkData");
						
						if (checkData.equals("1")) {
							String UID = Data.getString("UID");
							String ST_ID = Data.getString("ST_ID");
							String PaperID = Data.getString("PaperID");
							getWebview(UID,ST_ID,PaperID);
						} else if (checkData.equals("0")){
							webview.setVisibility(View.GONE);
							rewu_mok.setVisibility(View.VISIBLE);
						}
						LodDialogClass.closeCustomCircleProgressDialog();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void getWebview(String uId,String stId,String pId) {
		String url = Constants.URL_V;
		System.out.println("11122---." + url + "/report/examReport?stId=" + stId+"&pId=" + pId + "&uId=" + uId);
		
		// 启用支持javascript
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// WebView加载web资源
		webview.loadUrl(url + "/report/examReport?stId=" + stId + "&pId=" + pId + "&uId=" + uId);
		// 得到WebSettings对象，设置支持JavaScript的参数
		webview.getSettings().setJavaScriptEnabled(true);
		// 设置可以支持缩放
		webview.getSettings().setSupportZoom(true);
		// 设置默认缩放尺寸是FAR
		webview.getSettings().setDefaultZoom(ZoomDensity.FAR);
		// 设置出现缩放工具
		webview.getSettings().setBuiltInZoomControls(true);

		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
	}

	private void initview() {
		webview = (WebView) getActivity().findViewById(R.id.frag_stumokao_webview);
		rewu_mok = (RelativeLayout) getActivity().findViewById(R.id.rewu_mok);
		share = getActivity().getSharedPreferences("userinfo", getActivity().MODE_PRIVATE);
		System.out.println(share.getString("Token", ""));
		LodDialogClass.showCustomCircleProgressDialog(getActivity(), null, getString(R.string.common_Loading));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_stumokao, null);
	}

}
