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
public class Learn {
	private String CC_ID;//
	private String C_ID;//
	private String TF_ID;
	private String OpenDate;//
	private RefData RefData;//
	private String RefID;//
	private String ST_ID;//
	private String Status;//
	private int StorePoint;//
	private int TaskType;//
	private String UID;//
	
	
	

	private boolean isChacked;


	public boolean isChacked() {
		return isChacked;
	}

	public void setChacked(boolean isChacked) {
		this.isChacked = isChacked;
	}

	public Learn() {
	}

	public String getCC_ID() {
		return CC_ID;
	}

	public void setCC_ID(String cC_ID) {
		CC_ID = cC_ID;
	}

	public String getC_ID() {
		return C_ID;
	}

	public void setC_ID(String c_ID) {
		C_ID = c_ID;
	}

	public String getTF_ID() {
		return TF_ID;
	}

	public void setTF_ID(String tF_ID) {
		TF_ID = tF_ID;
	}

	public String getOpenDate() {
		return OpenDate;
	}

	public void setOpenDate(String openDate) {
		OpenDate = openDate;
	}

	public RefData getRefData() {
		return RefData;
	}

	public void setRefData(RefData refData) {
		RefData = refData;
	}

	public String getRefID() {
		return RefID;
	}

	public void setRefID(String refID) {
		RefID = refID;
	}

	public String getST_ID() {
		return ST_ID;
	}

	public void setST_ID(String sT_ID) {
		ST_ID = sT_ID;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public int getStorePoint() {
		return StorePoint;
	}

	public void setStorePoint(int storePoint) {
		StorePoint = storePoint;
	}

	public int getTaskType() {
		return TaskType;
	}

	public void setTaskType(int taskType) {
		TaskType = taskType;
	}

	public String getUID() {
		return UID;
	}

	public void setUID(String uID) {
		UID = uID;
	}

	public Learn(String cC_ID, String c_ID, String openDate,
			com.lels.bean.RefData refData, String refID, String sT_ID,
			String status, int storePoint, int taskType, String uID) {
		super();
		CC_ID = cC_ID;
		C_ID = c_ID;
		OpenDate = openDate;
		RefData = refData;
		RefID = refID;
		ST_ID = sT_ID;
		Status = status;
		StorePoint = storePoint;
		TaskType = taskType;
		UID = uID;
	}

}
