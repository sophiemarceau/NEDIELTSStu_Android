package com.lels.student.vote.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lels.main.activity.StundentClassroom;
import com.lels.student.vote.adapter.DialogVoteEndAdapter;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VoteDetailActivity extends BaseDialogActivity implements OnClickListener{
	private Context context;
	private SharedPreferences share, stuinfo;
	private ImageButton btn_back_ClassRoom_conncetion;
	private Button vote_btn_endvote;
	private ListView mlistview;
	private VoteDetailsAdapter madapter;
	
	private String voteResult;
	private String path;
	private TextView votedetails_txt_content;
	private String Subject;
	//参数
	private String 	activeClassId;
	private String    voteId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_vote_detail);
		
		initview();
		collectStuVotes();
		
		show_votedialog();
	}
	/**
	 * 
	 * 初始化控件
	 * 
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		stuinfo = getSharedPreferences("stuinfo", Context.MODE_PRIVATE);
		activeClassId = share.getString("activeClassId", "");
		voteId = share.getString("voteId", "");
		btn_back_ClassRoom_conncetion  = (ImageButton) findViewById(R.id.btn_back_ClassRoom_conncetion);
		btn_back_ClassRoom_conncetion.setOnClickListener(this);
		vote_btn_endvote = (Button) findViewById(R.id.vote_btn_endvote);
		vote_btn_endvote.setOnClickListener(this);
		mlistview = (ListView) findViewById(R.id.listview_vote_details);
		votedetails_txt_content  = (TextView) findViewById(R.id.votedetails_txt_content);
		Intent getdata = getIntent();
		if (getdata!=null) {
//			voteResult = getdata.getStringExtra("voteResult");
			Subject = getdata.getStringExtra("Subject");
		}
		System.out.println("Subject======"+Subject);
		votedetails_txt_content.setText(Subject);
		
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
				// TODO Auto-generated method stub
				List<HashMap<String, Object>> mlist = new ArrayList<HashMap<String,Object>>();

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
						mlist.add(map);
					}
					madapter = new VoteDetailsAdapter(mlist, context);
					mlistview.setAdapter(madapter);
					CalculateListviewGrideview
							.setListViewHeightBasedOnChildren(mlistview);

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
		//返回键
		case R.id.btn_back_ClassRoom_conncetion:
			IntentUtlis.sysStartActivity(context, StundentClassroom.class);
			finish();
			break;
			//查看投票的结果
		case R.id.vote_btn_endvote:
			collectStuVotes();
			break;

		default:
			break;
		}
	}

}
