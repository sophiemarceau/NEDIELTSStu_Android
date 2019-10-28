package com.lels.student.chatroom.bean;

public class Members {
	/*
	 * members = 群组成员详情[{, MemberIndex = 顺序号, RoleID = 角色，2为老师，3为学生,
	 * MemberAccount = 成员帐号, MemberName = 成员姓名, IconUrl = 头像，可直接访问 Email = 邮箱,
	 * ChatToken = 聊天室Token, ChattingRoomID = ChattingRoom表ID, }]
	 */
	public String MemberIndex;
	public String RoleID;
	public String MemberAccount;
	public String MemberName;
	public String IconUrl;
	public String Email;
	public String ChatToken;
	public String ChattingRoomID;

	public String getMemberIndex() {
		return MemberIndex;
	}

	public void setMemberIndex(String memberIndex) {
		MemberIndex = memberIndex;
	}

	public String getRoleID() {
		return RoleID;
	}

	public void setRoleID(String roleID) {
		RoleID = roleID;
	}

	public String getMemberAccount() {
		return MemberAccount;
	}

	public void setMemberAccount(String memberAccount) {
		MemberAccount = memberAccount;
	}

	public String getMemberName() {
		return MemberName;
	}

	public void setMemberName(String memberName) {
		MemberName = memberName;
	}

	public String getIconUrl() {
		return IconUrl;
	}

	public void setIconUrl(String iconUrl) {
		IconUrl = iconUrl;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getChatToken() {
		return ChatToken;
	}

	public void setChatToken(String chatToken) {
		ChatToken = chatToken;
	}

	public String getChattingRoomID() {
		return ChattingRoomID;
	}

	public void setChattingRoomID(String chattingRoomID) {
		ChattingRoomID = chattingRoomID;
	}

	@Override
	public String toString() {
		return "Members [MemberIndex=" + MemberIndex + ", RoleID=" + RoleID + ", MemberAccount=" + MemberAccount
				+ ", MemberName=" + MemberName + ", IconUrl=" + IconUrl + ", Email=" + Email + ", ChatToken="
				+ ChatToken + ", ChattingRoomID=" + ChattingRoomID + "]";
	}
	
	public Members() {
		
	}
	
	public Members(String memberIndex, String roleID, String memberAccount, String memberName, String iconUrl,
			String email, String chatToken, String chattingRoomID) {
		super();
		MemberIndex = memberIndex;
		RoleID = roleID;
		MemberAccount = memberAccount;
		MemberName = memberName;
		IconUrl = iconUrl;
		Email = email;
		ChatToken = chatToken;
		ChattingRoomID = chattingRoomID;
	}
}
