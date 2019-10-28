/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015-8-17 
 * 
 *******************************************************************************/
package com.lels.bean;
/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015-8-17
 * 作者:	 Mr_Wang
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人： 
 *    修改内容：
 * </pre>
 */
public class RefData {
	private int AssignCount;
	private long AssignDate;
	private int Category;
	private long CreateTime;
	private boolean IsDelete;
	private boolean IsPublic;
	private String Name;
	private String P_ID;
	private String PaperFolder;
	private String PaperNumber;
	private String PaperZip;
	private int PaperState;
	private int PaperVersion;
	private int RoleId;
	private int SubjectiveIn;
	private int Target;
	private int TaskType;
	private int Type;
	private int UID;
	private long UpdateTime;
	private String domainPZip;
	//判断是否能答题
		private String checkDoEx;
		//试卷的路径
		private String domainPFolder;
		
	public RefData() {
	}
	

	/**
	 * @return the checkDoEx
	 */
	public String getCheckDoEx() {
		return checkDoEx;
	}


	/**
	 * @param checkDoEx the checkDoEx to set
	 */
	public void setCheckDoEx(String checkDoEx) {
		this.checkDoEx = checkDoEx;
	}


	public RefData(int assignCount, int assignDate, int category,
			int createTime, boolean isDelete, boolean isPublic, String name,
			String p_ID, String paperFolder, String paperNumber, String paperZip,
			int paperState, int paperVersion, int roleId, int subjectiveIn,
			int target, int taskType, int type, int uID, int updateTime,
			String domainPFolder, String domainPZip) {
		super();
		AssignCount = assignCount;
		AssignDate = assignDate;
		Category = category;
		CreateTime = createTime;
		IsDelete = isDelete;
		IsPublic = isPublic;
		Name = name;
		P_ID = p_ID;
		PaperFolder = paperFolder;
		PaperNumber = paperNumber;
		PaperZip = paperZip;
		PaperState = paperState;
		PaperVersion = paperVersion;
		RoleId = roleId;
		SubjectiveIn = subjectiveIn;
		Target = target;
		TaskType = taskType;
		Type = type;
		UID = uID;
		UpdateTime = updateTime;
		this.domainPFolder = domainPFolder;
		this.domainPZip = domainPZip;
	}

	public int getAssignCount() {
		return AssignCount;
	}

	public void setAssignCount(int assignCount) {
		AssignCount = assignCount;
	}

	public long getAssignDate() {
		return AssignDate;
	}

	public void setAssignDate(long assignDate) {
		AssignDate = assignDate;
	}

	public int getCategory() {
		return Category;
	}

	public void setCategory(int category) {
		Category = category;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public boolean isIsDelete() {
		return IsDelete;
	}

	public void setIsDelete(boolean isDelete) {
		IsDelete = isDelete;
	}

	public boolean isIsPublic() {
		return IsPublic;
	}

	public void setIsPublic(boolean isPublic) {
		IsPublic = isPublic;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getP_ID() {
		return P_ID;
	}

	public void setP_ID(String p_ID) {
		P_ID = p_ID;
	}

	public String getPaperFolder() {
		return PaperFolder;
	}

	public void setPaperFolder(String paperFolder) {
		PaperFolder = paperFolder;
	}

	public String getPaperNumber() {
		return PaperNumber;
	}

	public void setPaperNumber(String paperNumber) {
		PaperNumber = paperNumber;
	}

	public String getPaperZip() {
		return PaperZip;
	}

	public void setPaperZip(String paperZip) {
		PaperZip = paperZip;
	}

	public int getPaperState() {
		return PaperState;
	}

	public void setPaperState(int paperState) {
		PaperState = paperState;
	}

	public int getPaperVersion() {
		return PaperVersion;
	}

	public void setPaperVersion(int paperVersion) {
		PaperVersion = paperVersion;
	}

	public int getRoleId() {
		return RoleId;
	}

	public void setRoleId(int roleId) {
		RoleId = roleId;
	}

	public int getSubjectiveIn() {
		return SubjectiveIn;
	}

	public void setSubjectiveIn(int subjectiveIn) {
		SubjectiveIn = subjectiveIn;
	}

	public int getTarget() {
		return Target;
	}

	public void setTarget(int target) {
		Target = target;
	}

	public int getTaskType() {
		return TaskType;
	}

	public void setTaskType(int taskType) {
		TaskType = taskType;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public int getUID() {
		return UID;
	}

	public void setUID(int uID) {
		UID = uID;
	}

	public long getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(long updateTime) {
		UpdateTime = updateTime;
	}

	public String getDomainPFolder() {
		return domainPFolder;
	}

	public void setDomainPFolder(String domainPFolder) {
		this.domainPFolder = domainPFolder;
	}

	public String getDomainPZip() {
		return domainPZip;
	}

	public void setDomainPZip(String domainPZip) {
		this.domainPZip = domainPZip;
	}

	@Override
	public String toString() {
		return "RefData [AssignCount=" + AssignCount + ", AssignDate="
				+ AssignDate + ", Category=" + Category + ", CreateTime="
				+ CreateTime + ", IsDelete=" + IsDelete + ", IsPublic="
				+ IsPublic + ", Name=" + Name + ", P_ID=" + P_ID
				+ ", PaperFolder=" + PaperFolder + ", PaperNumber="
				+ PaperNumber + ", PaperZip=" + PaperZip + ", PaperState="
				+ PaperState + ", PaperVersion=" + PaperVersion + ", RoleId="
				+ RoleId + ", SubjectiveIn=" + SubjectiveIn + ", Target="
				+ Target + ", TaskType=" + TaskType + ", Type=" + Type
				+ ", UID=" + UID + ", UpdateTime=" + UpdateTime
				+ ", domainPFolder=" + domainPFolder + ", domainPZip="
				+ domainPZip + "]";
	}
}
