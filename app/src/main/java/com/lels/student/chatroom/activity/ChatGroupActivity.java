package com.lels.student.chatroom.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lels.student.chatroom.bean.ChatMeta;
import com.lels.student.chatroom.bean.Chats;
import com.lels.student.chatroom.bean.Data;
import com.lels.student.chatroom.bean.Members;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.proguard.h;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatGroupActivity extends Activity {

	private GridView grid_view;
	private SharedPreferences shares;
	private List<Chats> mListChats;
	private List<Members> mListMembers;
	private ImageButton image_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_group);
		shares = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		grid_view = (GridView) findViewById(R.id.grid_chat_group);
		grid_view.setSelector(new ColorDrawable(Color.TRANSPARENT));
		initBack();
		//getDataFromNet();
		
		
		SharedPreferences sharedPreferences = getSharedPreferences("chatroom_name",
				Context.MODE_WORLD_READABLE);
		String members = sharedPreferences.getString("groupInfo", null);
		List<Members> mListMembers = new ArrayList<Members>();
		if (null != members) {
			String[] strs = members.split(";");
			if (strs.length > 0) {
				for (int i = 0; i < strs.length; i++) {
					String member = strs[i];
					String[] info = member.split(",");
					String url = info[0];
					String name = info[1];
					Members item = new Members();
					item.IconUrl = url;
					item.MemberName = name;
					mListMembers.add(item);
				}
			}
		}
		System.out.println(mListMembers.toString());
		ChatAdapter adapter = new ChatAdapter(ChatGroupActivity.this, mListMembers);
		grid_view.setAdapter(adapter);
	}

	private void initBack() {
		// TODO Auto-generated method stub
		image_back = (ImageButton) findViewById(R.id.imgview_back);
		image_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

/*	// 网络解析
	private void getDataFromNet() {
		// TODO Auto-generated method stub
		String url = new Constants().URL_ActiveClass_loadChatList;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", shares.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("ChatGroupActivity  =====  onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				System.out.println("ChatGroupActivity  =====  onSuccess");
				String result = arg0.result;
				parse(result);
			}
		});
	}

	private Object parse(String result) {
		
		try {
			JSONObject str = new JSONObject(result);
			String Infomation = str.getString("Infomation");
			Boolean Result = str.getBoolean("Result");
			System.out.println("json result outside : Infomation : " + Infomation + ",Result : " + Result);

			JSONObject dataJson = str.getJSONObject("Data");
			int chatSize = dataJson.getInt("chatSize");
			JSONArray chatsArray = dataJson.getJSONArray("chats");
			System.out.println("json result data : chatSize : " + chatSize);

			mListMembers = new ArrayList<Members>();
			for (int i = 0; i < chatsArray.length(); i++) {
				JSONObject obj_chat = chatsArray.getJSONObject(i);
				String ImgUrl = obj_chat.getString("ImgUrl");
				String memberCnt = obj_chat.getString("memberCnt");
				String ID = obj_chat.getString("ID");
				String ChattingGroup = obj_chat.getString("ChattingGroup");
				String className = obj_chat.getString("className");
				String ClassID = obj_chat.getString("ClassID");
				mListChats = new ArrayList<Chats>();
				mListChats.add(new Chats(ID, ClassID, className, ChattingGroup, ImgUrl, memberCnt));
				
				JSONArray membersArray = obj_chat.getJSONArray("members");
				for (int j = 0; j < membersArray.length(); j++) {
					JSONObject obj_member = membersArray.getJSONObject(j);

					String Email = obj_member.getString("Email");
					String ChattingRoomID = obj_member.getString("ChattingRoomID");
					String ChatToken = obj_member.getString("ChatToken");
					String RoleID = obj_member.getString("RoleID");
					String IconUrl = obj_member.getString("IconUrl");
					String MemberAccount = obj_member.getString("MemberAccount");
					String MemberIndex = obj_member.getString("MemberIndex");
					String MemberName = obj_member.getString("MemberName");
					Members mem = new Members(MemberIndex, RoleID, MemberAccount, MemberName, IconUrl, Email, ChatToken,
							ChattingRoomID);
					mListMembers.add(mem);
				}
			}
			System.out.println("学员列表数据  === " + mListMembers);
			ChatAdapter adapter = new ChatAdapter(ChatGroupActivity.this, mListMembers);
			grid_view.setAdapter(adapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}*/

	private class ChatAdapter extends BaseAdapter {

		private List<Members> member;
		private LayoutInflater inflater;

		public ChatAdapter(Context cxt, List<Members> member) {
			this.inflater = LayoutInflater.from(cxt);
			this.member = member;
		}

		@Override
		public int getCount() {
			return this.member.size();
		}

		@Override
		public Object getItem(int position) {
			return this.member.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChatHolder holder = null;
			if (null == convertView) {
				convertView = inflater.inflate(R.layout.chat_group_item, null);
				holder = new ChatHolder();
				holder.group_name = (TextView) convertView.findViewById(R.id.group_name);
				holder.group_img_icon = (ImageView) convertView.findViewById(R.id.group_img_icon);
				convertView.setTag(holder);
			} else {
				holder = (ChatHolder) convertView.getTag();
			}
			holder.group_name.setText(member.get(position).getMemberName());
			// 图片路径
			String imagepath = member.get(position).getIconUrl();
			if (imagepath.length()>0&&!"".equals(imagepath)&&!"null".equals(imagepath)) {
				ImageLoader.getInstance().displayImage(imagepath, holder.group_img_icon);
			}else{
				holder.group_img_icon.setImageResource(R.drawable.mor);
			}
			
			return convertView;
		}

		private class ChatHolder {
			public ImageView group_img_icon;
			public TextView group_name;
		}
	}
}
