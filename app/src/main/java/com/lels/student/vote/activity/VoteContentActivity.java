package com.lels.student.vote.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lels.main.activity.StundentClassroom;
import com.lels.student.vote.adapter.DialogVoteEndAdapter;
import com.lels.student.vote.adapter.VoteContentAdapter;
import com.lels.student.vote.adapter.VoteDetailsAdapter;
import com.lelts.tool.CalculateListviewGrideview;
import com.lelts.tool.IntentUtlis;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VoteContentActivity extends BaseDialogActivity implements OnClickListener {
	private Context context;
	private ImageButton btn_back_ClassRoom_conncetion;
	private SharedPreferences share, stuinfo;
	private Intent intent;
	// 投票的详情
	private String str_voting;
	private TextView votedetails_txt_content;
	private ListView mlistview;
	private List<HashMap<String, Object>> mlist;
	private VoteContentAdapter madapter;

	private String Subject;
	private String voteId;
	private String OptionNum;
	private String OptionDesc;
	// 参与投票
	private String path;
	private String activeClassId;
	private String optNum;
	// 是否停止投票 0表示未结束投票，1表示结束投票，根据标识来判断下面数据是否存在,
	private String flag;
	private Timer endvote_timer;

	private String voteResult;
	private List<HashMap<String, Object>> mdialog_list;
	private DialogVoteEndAdapter mdialog_adapter;
	private AlertDialog  alertDialog;
	private ListView mdialog_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vote_content);

		initview();
		getdata();
//		has_endvote();
		show_votedialog();
	}

	/**
	 * 初始化控件
	 * 
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		stuinfo = getSharedPreferences("stuinfo", Context.MODE_PRIVATE);
		activeClassId = share.getString("activeClassId", "");
		Intent getdata = getIntent();
		if (getdata != null) {
			str_voting = getdata.getStringExtra("str_voting");
		}
		System.out.println("str_voting 的投票详情数据====" + str_voting);
		votedetails_txt_content = (TextView) findViewById(R.id.votedetails_txt_content);
		mlistview = (ListView) findViewById(R.id.listview_vote_content);

		btn_back_ClassRoom_conncetion = (ImageButton) findViewById(R.id.btn_back_ClassRoom_conncetion);
		btn_back_ClassRoom_conncetion.setOnClickListener(this);
	}

	/**
	 * 
	 * 
	 * 解析获得到得数据
	 * 
	 * 
	 */
	private void getdata() {
		// TODO Auto-generated method stub
		mlist = new ArrayList<HashMap<String, Object>>();
		try {
			JSONObject obj = new JSONObject(str_voting);
			Subject = obj.getString("Subject");
			voteId = obj.getString("voteId");
			JSONArray array = obj.getJSONArray("voteOpts");
			for (int i = 0; i < array.length(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				JSONObject voteOpts = array.getJSONObject(i);
				OptionNum = voteOpts.getString("OptionNum");
				OptionDesc = voteOpts.getString("OptionDesc");
				map.put("OptionNum", OptionNum);
				map.put("OptionDesc", OptionDesc);
				mlist.add(map);
			}
			votedetails_txt_content.setText(Subject);
			madapter = new VoteContentAdapter(mlist, context);
			mlistview.setAdapter(madapter);
			CalculateListviewGrideview
					.setListViewHeightBasedOnChildren(mlistview);
			mlistview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					optNum = mlist.get(arg2).get("OptionNum").toString();
					joinVote();

				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 方法说明：弹出 结束投票的 Dialog
	 * 
	 * 
	 */

	private void ShowDiglogDevelop() {

		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(VoteContentActivity.this);
		View myLoginView = layoutInflater.inflate(R.layout.dialog_vote_end,null);
		
		if (alertDialog == null) {
			alertDialog = new AlertDialog.Builder(VoteContentActivity.this)
					.create();
			alertDialog.setView(myLoginView, 0, 0, 0, 30);
			alertDialog.getWindow().setGravity(Gravity.CENTER);
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setCancelable(false);
			alertDialog.show();
		}
		ImageButton btn_close = (ImageButton) myLoginView.findViewById(R.id.waing_end_btn_close);
		btn_close.setOnClickListener(this);
		Button waing_end_btn_sure = (Button) myLoginView.findViewById(R.id.waing_end_btn_sure);
		waing_end_btn_sure.setOnClickListener(this);
		mdialog_listview = (ListView) myLoginView.findViewById(R.id.mlistview_dialog_vote_end);
		mdialog_adapter = new DialogVoteEndAdapter(mdialog_list, context);
		mdialog_listview.setAdapter(mdialog_adapter);

	}

	/**
	 * 方法说明：学生端参与投票iphone、Android 学生App
	 * 
	 */
	private void joinVote() {
		// TODO Auto-generated method stub
		// [Args]：
		//  activeClassId=[互动课堂ID，不可为空]
		//  voteId=[投票定义ID，不可为空]
		//  optNum=[投票选项号，不可为空]
		// [Return]：
		//  Infomation = 提示信息;
		//  Result = 返回结果true/false;
		//  Data = {
		//   voteResult = 投票选项统计[{,
		//    OptionNum = 投票选项号,
		//    OptionDesc = 投票选项描述,
		//    voteNum = 投票率,
		//    ownVote = 是否是自己投的票，1是0不是,
		//    finishVote = 投票是否结束,true是，false否,
		//   }]
		//  }

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		params.addBodyParameter("voteId", voteId);
		params.addBodyParameter("optNum", optNum);
		System.out.println("activeClassId===" + activeClassId
				+ "=====voteId===" + voteId + "=====optNum===" + optNum);
		HttpUtils util = new HttpUtils();
		path = Constants.URL_joinVote;
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("学生参与投票的结果----" + result);

				intent = new Intent(context, VoteDetailActivity.class);
				intent.putExtra("voteResult", voteResult);
				intent.putExtra("Subject", Subject);
				startActivity(intent);
				finish();
				// try {
				// JSONObject obj = new JSONObject(result);
				// JSONObject data = obj.getJSONObject("Data");
				// JSONArray array = data.getJSONArray("voteResult");
				// for (int i = 0; i < array.length(); i++) {
				// JSONObject voteResult = array.getJSONObject(i);
				// HashMap<String, Object> map = new HashMap<String, Object>();
				// String OptionNum = voteResult.getString("OptionNum");
				// String finishVote = voteResult.getString("finishVote");
				// String voteNum = voteResult.getString("voteNum");
				// String OptionDesc = voteResult.getString("OptionDesc");
				// map.put("OptionNum", OptionNum);
				// map.put("finishVote", finishVote);
				// map.put("voteNum", voteNum);
				// map.put("OptionDesc", OptionDesc);
				// mlist.add(map);
				// }
				// ShowDiglogDevelop();
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		});

	}

	/**
	 * 方法说明：学生端心跳检测是否结束投票iphone、Android 学生App 3秒一轮询
	 * 
	 */
	private void loadMyVote() {
		// TODO Auto-generated method stub
		// [Args]：
		//  activeClassId=[互动课堂ID，不可为空]
		// [Return]：
		//  Infomation = 提示信息;
		//  Result = 返回结果true/false;
		//  Data = {
		//   flag = 标识，0表示未结束投票，1表示结束投票，根据标识来判断下面数据是否存在,
		//   voteResult = 投票结果，只有flag为1时存在[{,
		//    OptionNum = 投票选项号,
		//    OptionDesc = 投票选项描述,
		//    voteNum = 投票率,
		//    finishVote = 投票是否结束,true是，false否,
		//   }]
		//  }

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		System.out.println("activeClassId===" + activeClassId);
		HttpUtils util = new HttpUtils();
		path = Constants.URL_loadMyVote;
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("是否老师停止投票----" + result);
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject data = obj.getJSONObject("Data");
					flag = data.getString("flag");
					if (flag.equals("1")) {
						// 老师结束了投票
						voteResult = data.getJSONArray("voteResult").toString();
						System.out
								.println("voteResult===结束的结果===" + voteResult);
						collectStuVotes();
						
					} else {
						// 正在投票
						System.out.println("正在投票  ======");
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}



	/**
	 * 方法说明：学生端弃权投票iphone、Android 学生App
	 * 
	 */
	private void waiverVote() {
		// TODO Auto-generated method stub
		// [Args]：
		//  activeClassId=[互动课堂ID，不可为空]
		//  voteId=[投票定义ID，不可为空]
		// [Return]：
		//  Infomation = 提示信息;
		//  Result = 返回结果true/false;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		params.addBodyParameter("voteId", voteId);
		System.out.println("activeClassId===" + activeClassId
				+ "=====voteId====" + voteId);
		HttpUtils util = new HttpUtils();
		path = Constants.URL_waiverVote;
		util.configCurrentHttpCacheExpiry(0);
		util.configDefaultHttpCacheExpiry(0);
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				System.out.println("放弃投票的结果----" + result);
				intent = new Intent(context, VoteDetailActivity.class);
				intent.putExtra("voteResult", voteResult);
				intent.putExtra("Subject", Subject);
				startActivity(intent);
				finish();

			}
		});

	}
	
	/**
	 * 方法说明：学生端心跳汇总当前投票数据
	 * 
	 */
	private void collectStuVotes() {
		// TODO Auto-generated method stub
		// [Args]：
		//  activeClassId=[互动课堂ID，不可为空]
		//  voteId=[投票定义ID，不可为空]
		// [Return]：
		//  Infomation = 提示信息;
		//  Result = 返回结果true/false;
		//  Data = [{
		//   OptionNum = 投票选项号,
		//   OptionDesc = 投票选项描述,
		//   voteNum = 投票率,
		//   finishVote = 投票是否结束,true是，false否,
		//        sownVote = 是否是自己投的票
		//  }]
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		params.addBodyParameter("voteId", voteId);
		System.out.println("activeClassId===" + activeClassId
				+ "=====voteId===" + voteId);
		HttpUtils util = new HttpUtils();
		path = Constants.URL_collectStuVotes;
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				mdialog_list = new ArrayList<HashMap<String,Object>>();

				String result = arg0.result;
				System.out.println("投票结果----" + result);
				try {
					JSONObject obj = new JSONObject(result);
					JSONArray array = obj.getJSONArray("Data");
					for (int i = 0; i < array.length(); i++) {
						JSONObject Data = array.getJSONObject(i);
						HashMap<String, Object> map = new HashMap<String, Object>();
						String OptionNum = Data.getString("OptionNum");
						String voteNum = Data.getString("voteNum");
						String OptionDesc = Data.getString("OptionDesc");
						String ownVote = Data.getString("ownVote");
						map.put("OptionNum", OptionNum);
						map.put("voteNum", voteNum);
						map.put("OptionDesc", OptionDesc);
						map.put("ownVote", ownVote);
						mdialog_list.add(map);
					}
					
					ShowDiglogDevelop();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 确定老师已经结束投票
		case R.id.waring_end_vote_btn_sure:

			// 弃权
			waiverVote();

			break;

		// 返回键
		case R.id.btn_back_ClassRoom_conncetion:
			IntentUtlis.sysStartActivity(context, StundentClassroom.class);
			finish();
			
		case R.id.waing_end_btn_sure:
			IntentUtlis.sysStartActivity(context, StundentClassroom.class);
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 方法说明：3秒 判断是否老师是否结束投票
	 * 
	 */
	private void has_endvote() {
		// TODO Auto-generated method stub
		endvote_timer = new Timer();
		TimerTask stutask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				loadMyVote();

			}
		};
		endvote_timer.schedule(stutask, 3000, 3000);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		// 停止计时器
		if (endvote_timer != null) {
			endvote_timer.cancel();
		}
		endvote_timer = null;
		super.onStop();
	}

}
