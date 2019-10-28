package com.lels.student.chatroom.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;

import java.util.Locale;

import com.example.strudentlelts.R;
import com.lels.bean.ExitApplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationActivity extends FragmentActivity implements OnClickListener{

	/**
	 * 目标 Id
	 */
	private String mTargetId;

	/**
	 * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
	 */
	private String mTargetIds;
	private String className;

	/**
	 * 会话类型
	 */
	private Conversation.ConversationType mConversationType;
	//回退键
	private ImageView chatroom_group;
	private ImageButton image_back;
	private TextView chatName;
	private SharedPreferences share;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.conversation);
		ExitApplication.getInstance().addActivitylistExit(this);
		chatName = (TextView) findViewById(R.id.chatroom_name);
		share = getSharedPreferences("chatroom_name", Context.MODE_PRIVATE);
		className = share.getString("className", "抱歉！！");
		initAttr();
		Intent intent = getIntent();
		getIntentDate(intent);
		chatName.setText(className);
	}

	private void initAttr() {
		// TODO Auto-generated method stub
		image_back = (ImageButton) findViewById(R.id.imgview_back);
		chatroom_group = (ImageView) findViewById(R.id.chatroom_group);
		image_back.setOnClickListener(this);
		chatroom_group.setOnClickListener(this);
	}

	/**
	 * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
	 */
	private void getIntentDate(Intent intent) {

		mTargetId = intent.getData().getQueryParameter("targetId");
		mTargetIds = intent.getData().getQueryParameter("targetIds");
		System.out.println("聊天室的mTargetId是：  = " + mTargetId);
		// intent.getData().getLastPathSegment();//获得当前会话类型
		mConversationType = Conversation.ConversationType.valueOf(intent
				.getData().getLastPathSegment()
				.toUpperCase(Locale.getDefault()));

		enterFragment(mConversationType, mTargetId);
	}

	/**
	 * 加载会话页面 ConversationFragment
	 *
	 * @param mConversationType
	 *            会话类型
	 * @param mTargetId
	 *            目标 Id
	 */
	private void enterFragment(Conversation.ConversationType mConversationType,
			String mTargetId) {

		ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager()
				.findFragmentById(R.id.conversation);

		Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName)
				.buildUpon().appendPath("conversation")
				.appendPath(mConversationType.getName().toLowerCase())
				.appendQueryParameter("targetId", mTargetId).build();

		fragment.setUri(uri);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgview_back:
			finish();
			break;
		case R.id.chatroom_group:
			Intent intent2 = new Intent(ConversationActivity.this, ChatGroupActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (!TextUtils.isEmpty(mTargetId)) {
			RongIM.getInstance().getRongIMClient().clearMessagesUnreadStatus(Conversation.ConversationType.GROUP, mTargetId);
		}
	}
}
