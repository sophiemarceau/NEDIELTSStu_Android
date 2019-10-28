package com.lels.student.chatroom.bean;

import java.io.Serializable;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;

public class Chats implements Serializable{
/*
 * chats = 聊天室列表数据[{,
   ID = ChattingRoom表ID,
   ClassID = BS_Class表ID,
   className = 班级名称,
   ChattingGroup = 聊天室群组ID,
   ImgUrl = 群组头像，路径可直接访问,
   memberCnt = 群组成员人数,
   members = 群组成员详情[{,
 */
	
	public String ID;
	public String ClassID;
	public String className;
	public String ChattingGroup;
	public String ImgUrl;
	public String memberCnt;
	public List<Members> members;
	public String chatLastMessage1;
	public int chatCount;
	//public String recentSentUser;
	public SpannableStringBuilder drawable;
	@Override
	public String toString() {
		return "Chats [ID=" + ID + ", ClassID=" + ClassID + ", className=" + className + ", ChattingGroup="
				+ ChattingGroup + ", ImgUrl=" + ImgUrl + ", memberCnt=" + memberCnt + ", members="
				+ members.size() + ",chatLastMessage=" + chatLastMessage1 + "]";
	}
	public Chats(String iD, String classID, String className, String chattingGroup, String imgUrl, String memberCnt,
			List<Members> members) {
		super();
		ID = iD;
		ClassID = classID;
		this.className = className;
		ChattingGroup = chattingGroup;
		ImgUrl = imgUrl;
		this.memberCnt = memberCnt;
		this.members = members;
	}
	public Chats(String iD, String classID, String className, String chattingGroup, String imgUrl, String memberCnt) {
		super();
		ID = iD;
		ClassID = classID;
		this.className = className;
		ChattingGroup = chattingGroup;
		ImgUrl = imgUrl;
		this.memberCnt = memberCnt;
	}
	
	public void copy(Chats chat) {
		this.ID = chat.ID;
		this.ClassID = chat.ClassID;
		this.className = chat.className;
		this.ChattingGroup = chat.ChattingGroup;
		this.ImgUrl = chat.ImgUrl;
		this.memberCnt = chat.memberCnt;
		this.members = chat.members;
		this.chatLastMessage1 = chat.chatLastMessage1;
		this.chatCount = chat.chatCount;
		this.drawable = chat.drawable;
	}
}
