package com.history;

public class InClass {
	private String OptionNum;// = 投票选项号,
	private String OptionDesc;// 投票选项描述
	private String voteNum;// 投票率,
	private String ownVote;//是否是自己投的票，1是0不是,
	private String finishVote;//投票是否结束,true是，false否,
	
	public InClass() {
		super();
	}
	public InClass(String optionNum, String optionDesc, String voteNum,
			String ownVote, String finishVote) {
		super();
		OptionNum = optionNum;
		OptionDesc = optionDesc;
		this.voteNum = voteNum;
		this.ownVote = ownVote;
		this.finishVote = finishVote;
	}
	public String getOptionNum() {
		return OptionNum;
	}
	public void setOptionNum(String optionNum) {
		OptionNum = optionNum;
	}
	public String getOptionDesc() {
		return OptionDesc;
	}
	public void setOptionDesc(String optionDesc) {
		OptionDesc = optionDesc;
	}
	public String getVoteNum() {
		return voteNum;
	}
	public void setVoteNum(String voteNum) {
		this.voteNum = voteNum;
	}
	public String getOwnVote() {
		return ownVote;
	}
	public void setOwnVote(String ownVote) {
		this.ownVote = ownVote;
	}
	public String getFinishVote() {
		return finishVote;
	}
	public void setFinishVote(String finishVote) {
		this.finishVote = finishVote;
	}

}
