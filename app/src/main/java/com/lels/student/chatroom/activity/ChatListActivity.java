package com.lels.student.chatroom.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.UserInfoProvider;
import io.rong.imkit.model.Emoji;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OperationCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.strudentlelts.R;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.main.activity.MainActivity;
import com.lels.student.chatroom.bean.ChatMeta;
import com.lels.student.chatroom.bean.Chats;
import com.lels.student.chatroom.bean.Data;
import com.lels.student.chatroom.bean.LastMessage;
import com.lels.student.chatroom.bean.Members;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChatListActivity extends Activity implements UserInfoProvider , RongIM.ConversationBehaviorListener{
	private ListView lv_chat;
	private SharedPreferences share;
	List<Members> mListMembers = new ArrayList<Members>();
	List<Chats> mListChats = null;
	List<LastMessage> mListLast = null;
	String chatContent;
	private ImageView chatroom_img_unread;
	private ImageButton image_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_list);
		
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		chatroom_img_unread = (ImageView) findViewById(R.id.chatroom_img_unread);
		this.lv_chat = (ListView) findViewById(R.id.lv_chat);
		initBack();
		init();
		// 网络解析的数据
		getChatListDataNet();
		RongIM.setUserInfoProvider(this, false);
		registerEvent();
		RongIM.setConversationBehaviorListener(this);
		
		LodDialogClass.showCustomCircleProgressDialog(ChatListActivity.this, "", getString(R.string.xlistview_header_hint_loading));
		ExitApplication.getInstance().addActivitylistExit(this);
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

	private ChatMeta parse(String result) {
		// chat meta
		ChatMeta meta = new ChatMeta();
		try {
			JSONObject str = new JSONObject(result);
			String Infomation = str.getString("Infomation");
			Boolean Result = str.getBoolean("Result");
			System.out.println("json result outside : Infomation : " + Infomation + ",Result : " + Result);
			// chat meta
			// ChatMeta meta = new ChatMeta();
			meta.infomation = Infomation;
			meta.result = Result;
			// chat meta

			JSONObject dataJson = str.getJSONObject("Data");
			int chatSize = dataJson.getInt("chatSize");
			JSONArray chatsArray = dataJson.getJSONArray("chats");
			System.out.println("json result data : chatSize : " + chatSize);

			List<Chats> mListChats = new ArrayList<Chats>();
			// chat data
			Data data = new Data();
			data.chatSize = chatSize;
			data.chats = mListChats;
			// chat data
			meta.data = data;

			for (int i = 0; i < chatsArray.length(); i++) {
				JSONObject obj_chat = chatsArray.getJSONObject(i);

				String ImgUrl = obj_chat.getString("ImgUrl");
				String memberCnt = obj_chat.getString("memberCnt");
				String ID = obj_chat.getString("ID");
				String ChattingGroup = obj_chat.getString("ChattingGroup");
				String className = obj_chat.getString("className");
				// String ClassID = obj_chat.getString("ClassID");

				List<Members> mListMembers = new ArrayList<Members>();
				// chat obj
				Chats chat = new Chats(ID, "", className, ChattingGroup, ImgUrl, memberCnt);
				chat.members = mListMembers;
				// chat obj
				mListChats.add(chat);
				System.out.println("json result chat : ImgUrl" + ImgUrl + ",memberCnt : " + memberCnt + ",ID : " + ID
						+ ",ChattingGroup : " + ChattingGroup + ",className : " + className);
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

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return meta;
	}

	private void listenGroup() {
		int totalcount = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.GROUP);
		System.out.println("totalcount before : " + totalcount);
		chatroom_img_unread.setVisibility(totalcount > 0 ? View.VISIBLE : View.GONE);
		if (null != ChatListActivity.this.meta) {
			List<Chats> chats = ChatListActivity.this.meta.data.chats;
			for (int i = 0; i < chats.size(); i++) {
				// 加入group
				boolean isFeedBack = false;
				if (i == chats.size() - 1) {
					isFeedBack = true;
				}
				connectGroup(chats.get(i), isFeedBack);
			}
		}
	}

	private void updateChat(final Chats chat) {
		ChatListActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ChatAdapter adapter = (ChatAdapter) lv_chat.getAdapter();
				adapter.updateChat(chat);
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void connectGroup(final Chats chat, final boolean isFeedBack) {
		RongIM.getInstance().getRongIMClient().joinGroup(chat.ChattingGroup, chat.className, new OperationCallback() {

			@Override
			public void onSuccess() {
				int count = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.GROUP,
						chat.ChattingGroup);
				List<Message> msg = RongIM.getInstance().getRongIMClient()
						.getLatestMessages(Conversation.ConversationType.GROUP, chat.ChattingGroup, 1);
				chat.chatCount = count;
				if (null != msg && !msg.isEmpty()) {
					MessageContent mc = msg.get(0).getContent();
					System.out.println("消息的user last : " + mc.getClass());
					List<Members> members = chat.members;
					String userName = "";
					if (null != msg.get(0).getSenderUserId()) {
						for (int j = 0; j < members.size(); j++) {
							if (msg.get(0).getSenderUserId().equals(members.get(j).getMemberAccount())) {
								userName = members.get(j).getMemberName();
								break;
							}
						}
					}
					if (mc instanceof TextMessage) {
						SpannableStringBuilder ssb = ensure(userName + ":" + ((TextMessage) mc).getContent());
						if (ssb != null) {
							chat.drawable = ssb;
							//chat.chatLastMessage = "";
						} else {
							chat.drawable = null;
							//chat.chatLastMessage = ((TextMessage) mc).getContent();
						}
						System.out.println("message call back last : " + count + "; msg : " + chat.drawable );
					} else if (mc instanceof ImageMessage) {// 图片消息
						chat.drawable = new SpannableStringBuilder(userName + ":" + "[图片]");
						//chat.chatLastMessage = "[图片]";
					} else if (mc instanceof VoiceMessage) {// 语音消息
						chat.drawable = new SpannableStringBuilder(userName + ":" + "[语音]");
						//chat.chatLastMessage =  "[语音]";
					}
					//chat.recentSentUser = msg.get(0).getSenderUserId();
				}
				if (0 != chat.chatCount || null != chat.drawable) {
					System.out.println("message call back last : " + count + "; msg : " + chat.drawable);
					updateChat(chat);
				}
				if (isFeedBack) {
					List<Chats> chats = ChatListActivity.this.meta.data.chats;
					registerOnReceivedMessage(chats);
				}
			}

			@Override
			public void onError(ErrorCode arg0) {
				if (isFeedBack) {
					List<Chats> chats = ChatListActivity.this.meta.data.chats;
					registerOnReceivedMessage(chats);
				}
			}
		});
	}
	
	HashMap<Integer, Emoji> sEmojiMap = new HashMap<Integer, Emoji>();
	private void init() {
		int[] codes = getResources().getIntArray(R.array.rc_emoji_code);
        TypedArray array = getResources().obtainTypedArray(R.array.rc_emoji_res);

        if (codes.length != array.length()) {
            throw new RuntimeException("Emoji resource init fail.");
        }

        int i = -1;
        while (++i < codes.length) {
            Emoji emoji = new Emoji(codes[i], array.getResourceId(i, -1));

            sEmojiMap.put(codes[i], emoji);
        }
	}
	
	private SpannableStringBuilder ensure(String input) {

        if (input == null) {
            return null;
        }
        // extract the single chars that will be operated on
        final char[] chars = input.toCharArray();
        List<Integer> list = new ArrayList<Integer>();
        SpannableStringBuilder ssb = new SpannableStringBuilder(input);
        int codePoint;
        boolean isSurrogatePair;
        for (int i = 0; i < chars.length; i++) {
        	
            if (Character.isHighSurrogate(chars[i])) {
                continue;
            } else if (Character.isLowSurrogate(chars[i])) {
                if (i > 0 && Character.isSurrogatePair(chars[i - 1], chars[i])) {
                    codePoint = Character.toCodePoint(chars[i - 1], chars[i]);
                    isSurrogatePair = true;
                } else {
                    continue;
                }
            } else {
                codePoint = (int) chars[i];
                isSurrogatePair = false;
            }
            if (sEmojiMap.containsKey(codePoint)) {
            	System.out.println("codePoint: " +codePoint + ",index : " + i + ",chars.length : " + chars.length);
            	list.add(codePoint);
            	ssb.setSpan(new ImageSpan(getBg(codePoint), ImageSpan.ALIGN_BASELINE), isSurrogatePair ? i - 1 : i, i + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }
        return ssb;

    }
	
	private Drawable getBg(int codePoint) {

		Drawable mDrawable = getResources().getDrawable(sEmojiMap.get(codePoint).getRes());
		DisplayMetrics dm = new DisplayMetrics(); 
        int width = mDrawable.getIntrinsicWidth() - (int) (4 * dm.density);
        int height = mDrawable.getIntrinsicHeight() - (int) (4 * dm.density);
        mDrawable.setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
        return mDrawable;
    }

	// 网上获取聊天室列表信息
	private ChatMeta meta;

	public void getChatListDataNet() {
		String url = new Constants().URL_ActiveClass_loadChatList;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("网上获取聊天室列表信息    ===   onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				System.out.println("网上获取聊天室列表信息    ===  onSuccess");
				String result = arg0.result;
				ChatListActivity.this.meta = parse(result);
				if (null != ChatListActivity.this.meta) {
					ChatAdapter adapter = new ChatAdapter(ChatListActivity.this, meta);
					lv_chat.setAdapter(adapter);
					listenGroup();
					LodDialogClass.closeCustomCircleProgressDialog();
				}
			}
		});
	}

	private void registerOnReceivedMessage(final List<Chats> chats) {
		RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {

			@Override
			public boolean onReceived(Message message, int arg1) {
				final int totalcount = RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.GROUP);
				System.out.println("totalcount : " + totalcount);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						chatroom_img_unread.setVisibility(totalcount > 0 ? View.VISIBLE : View.GONE);
					}
				});
				for (int i = 0; i < chats.size(); i++) {
					Chats chat = chats.get(i);
					if (message.getTargetId().equals(chats.get(i).ChattingGroup)) {
						int count = RongIM.getInstance().getRongIMClient()
								.getUnreadCount(Conversation.ConversationType.GROUP, chat.ChattingGroup);
						chat.chatCount = count;
						MessageContent mc = message.getContent();
						System.out.println("消息的user" + mc.getClass());
//						List<Members> members = chat.members;
//						String userName = "";
//						for (int j = 0; j < members.size(); j++) {
//							System.out.println("username : " + members.toString());
//							if (members.get(j).getMemberAccount().equals(message.getSenderUserId())) {
//								System.out.println("username = " + members.get(j).getMemberAccount());
//								userName = members.get(j).getMemberName();
//								break;
//							}
//						}
						//chat.recentSentUser = message.getSenderUserId();
						List<Members> members = chat.members;
						String userName = "";
						if (null != message.getSenderUserId()) {
							for (int j = 0; j < members.size(); j++) {
								if (message.getSenderUserId().equals(members.get(j).getMemberAccount())) {
									userName = members.get(j).getMemberName();
									break;
								}
							}
						}
						//文字内容
						if (mc instanceof TextMessage) {
							SpannableStringBuilder ssb = ensure(userName + ":" + ((TextMessage) mc).getContent());
							if (ssb != null) {
								chat.drawable = ssb;
								//chat.chatLastMessage = "";
							} else {
								chat.drawable = null;
								//chat.chatLastMessage = ((TextMessage) mc).getContent();
							}
						} else if (mc instanceof ImageMessage) {// 图片消息
							chat.drawable = new SpannableStringBuilder(userName + ":" + "[图片]");
							//chat.chatLastMessage = "[图片]";
						} else if (mc instanceof VoiceMessage) {// 语音消息
							chat.drawable = new SpannableStringBuilder(userName + ":" + "[语音]");
							//chat.chatLastMessage =  "[语音]";
						}

						updateChat(chat);
						break;
					}
				}
				return true;
			}
		});
	}

	private void registerEvent() {

		this.lv_chat.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ChatAdapter adapter = (ChatAdapter) lv_chat.getAdapter();
				Chats chat = (Chats) adapter.getItem(position);
				// new
				String className = chat.className;
				SharedPreferences sharedPreferences = ChatListActivity.this.getSharedPreferences("chatroom_name",
						Context.MODE_WORLD_READABLE);
				Editor editor = sharedPreferences.edit();
				editor.putString("className", className);
				
				Object item = adapter.getItem(position);
				if (item instanceof Chats) {
					Chats itemChat = (Chats)item;
					List<Members> members = itemChat.members;
					StringBuilder sb = new StringBuilder();
					int size = members.size();
					for (int i = 0; i < size; i++) {
						Members member = members.get(i);
						sb.append(member.IconUrl).append(",").append(member.MemberName);
						if (i != size - 1) {
							sb.append(";");
						}
					}
					editor.putString("groupInfo", sb.toString());
				}
				
				editor.commit();
				// new
				RongIM.getInstance().startConversation(ChatListActivity.this, Conversation.ConversationType.GROUP,
						chat.ChattingGroup, chat.className);
			}
		});
	}

	private class ChatAdapter extends BaseAdapter {
		private List<Chats> items;
		private LayoutInflater inflater;

		private ChatMeta meta;

		public ChatAdapter(Context cxt, ChatMeta meta) {
			this.inflater = LayoutInflater.from(cxt);
			this.meta = meta;
			this.items = this.meta.data.chats;
		}

		public void updateChat(Chats chat) {
			System.out.println("message call back filter ================== > begin");
			for (int i = 0; i < this.items.size(); i++) {
				if (this.items.get(i).ChattingGroup.equals(chat.ChattingGroup)) {
					System.out.println("message call back filter == >" + chat.toString());
					this.items.get(i).copy(chat);
					System.out.println("message call back filter == > done===");
					break;
				}
			}
		}

		@Override
		public int getCount() {
			return this.items.size();
		}

		@Override
		public Object getItem(int position) {
			return this.items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ChatHolder holder = null;
			if (null == convertView) {
				convertView = inflater.inflate(R.layout.chatlist_item, null);
				holder = new ChatHolder();
				holder.chat_content = (TextView) convertView.findViewById(R.id.chat_content);
				holder.chat_title = (TextView) convertView.findViewById(R.id.chat_title);
				holder.chat_unNum_qipao = (TextView) convertView.findViewById(R.id.chat_unRead_qipao);
				holder.chatimg_icon = (ImageView) convertView.findViewById(R.id.chatimg_icon);
				convertView.setTag(holder);
			} else {
				holder = (ChatHolder) convertView.getTag();
			}
			Chats chat = this.items.get(position);
			holder.chat_title.setText(chat.className + "（" + chat.memberCnt  + "人）");
			//System.out.println("my chat last message = " + chat.chatLastMessage);
			
			/*List<Members> members = chat.members;
			String userName = "";
			if (null != chat.recentSentUser) {
				for (int j = 0; j < members.size(); j++) {
					System.out.println("username = " + members.get(j).getMemberAccount());
					if (chat.recentSentUser.equals(members.get(j).getMemberAccount())) {
						userName = members.get(j).getMemberName();
						break;
					}
				}
			}*/
			
			/*if (!TextUtils.isEmpty(chat.chatLastMessage)) {
				holder.chat_content.setText(userName + ":" + chat.chatLastMessage);
			} else {
				holder.chat_content.setText("");
			}*/
			
			if (chat.chatCount <= 0) {
				holder.chat_unNum_qipao.setVisibility(View.INVISIBLE);
				holder.chat_unNum_qipao.setText("");
			} else {
				holder.chat_unNum_qipao.setVisibility(View.VISIBLE);
				holder.chat_unNum_qipao.setText(String.valueOf(chat.chatCount));
			}
			if (null != chat.drawable) {
				/*holder.iv_portri.setVisibility(View.VISIBLE);
				holder.iv_portri.setBackground(chat.drawable);*/
				holder.chat_content.setText(chat.drawable);
			} else {
				//holder.iv_portri.setVisibility(View.GONE);
				holder.chat_content.setText("");
			}
			// 图片路径
			ImageLoader.getInstance().displayImage(this.items.get(position).ImgUrl, holder.chatimg_icon);
			return convertView;
		}

		private class ChatHolder {
			public ImageView chatimg_icon;
			public TextView chat_title, chat_content, chat_unNum_qipao;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		listenGroup();
	}

	@Override
	public UserInfo getUserInfo(String userId) {
		System.out.println("chat member info userId = " + userId);
		if (meta != null) {
			List<Chats> chats = meta.data.chats;
			for (int i = 0; i < chats.size(); i++) {
				Chats chat = chats.get(i);
				List<Members> members = chat.members;
				for (int j = 0; j < members.size(); j++) {
					Members member = members.get(j);
					if (userId.equals(member.MemberAccount)) {
						System.out.println("chat member info = " + member.toString());
						return new UserInfo(userId, member.MemberName,
								TextUtils.isEmpty(member.IconUrl) ? null : Uri.parse(member.IconUrl));
					}
				}
			}
		}
		return null;
	}



	@Override
	public boolean onMessageClick(Context arg0, View arg1, final Message message) {
		// TODO Auto-generated method stub
		if (message.getContent() instanceof ImageMessage) {
			System.out.println("on message click...");
			runOnUiThread(new Runnable() {
				public void run() {
					ImageMessage imageMessage = (ImageMessage) message.getContent();
		            Intent intent = new Intent(ChatListActivity.this, ChatPhotoActivity.class);

		            intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
		            if (imageMessage.getThumUri() != null)
		                intent.putExtra("thumbnail", imageMessage.getThumUri());

		            startActivity(intent);
				}
			});
		}
		return false;
		
	}

	@Override
	public boolean onMessageLinkClick(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onMessageLongClick(Context arg0, View arg1, Message arg2) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onUserPortraitClick(Context arg0, ConversationType arg1, UserInfo arg2) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean onUserPortraitLongClick(Context arg0, ConversationType arg1, UserInfo arg2) {
		// TODO Auto-generated method stub
		return false;
	}
}
