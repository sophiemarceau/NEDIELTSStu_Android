package com.lels.student.starttask.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lelts.tool.RoundProgressBar;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StuStart_fragment extends Fragment {
	private RoundProgressBar progerss_class, progress_task, progress_mokao,
			progress_start;
//	private Handler handler,handler1;
	private SharedPreferences share;
	private String url = Constants.URL_HomeCompleteRate;
	private int imk;
	private int ixx;
	private int ilx;
	private int ikc;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initview();
		getHttp();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		
	}
	private void getHttp() {
		share = getActivity().getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));// 添加保密的东西

		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				System.out.println(result);
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject obj1 = obj.getJSONObject("Data");
					String kstime = obj1.getString("ksTime");
					String mk = obj1.getString("mk");
					String xx = obj1.getString("xx");
					String lx = obj1.getString("lx");
					String kc = obj1.getString("kc");
					imk = Integer.parseInt(mk);
					ixx = Integer.parseInt(xx);
					ilx = Integer.parseInt(lx);
					ikc = Integer.parseInt(kc);
					System.out.println(imk+","+ixx+","+ilx+","+ikc);
					progress_mokao.setProgress(imk);
					progress_start.setProgress(ixx);
					progress_task.setProgress(ilx);
					progerss_class.setProgress(ikc);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}


	private void initview() {
		progerss_class = (RoundProgressBar) getActivity().findViewById(
				R.id.RoundProgressBar_class_choose);
		progress_task = (RoundProgressBar) getActivity().findViewById(
				R.id.RoundProgressBar_task_choose);

		progress_mokao = (RoundProgressBar) getActivity().findViewById(
				R.id.RoundProgressBar_mokao_choose);

		progress_start = (RoundProgressBar) getActivity().findViewById(
				R.id.RoundProgressBar_start_choose);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_stustart, null);
	}

}
