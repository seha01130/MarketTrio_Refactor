package com.marketTrio.controller;

import java.util.Date;

public class ReplyCommand {
	
	private int ReplyId;
	private String memberId;
    private Date createDate;
    private String content;//그에 따른 후기작성하기 버튼을 보여줘야함.
    
    public ReplyCommand() {
		super();
	}
    

	public ReplyCommand(int replyId, String memberId, Date createDate, String content) {
		super();
		ReplyId = replyId;
		this.memberId = memberId;
		this.createDate = createDate;
		this.content = content;
	}


	public int getReplyId() {
		return ReplyId;
	}

	public void setReplyId(int replyId) {
		ReplyId = replyId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
    

}
