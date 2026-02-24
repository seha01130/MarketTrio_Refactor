package com.marketTrio.reply.controller;

import java.util.Date;

public class ReplyCommand {
	private int replyId;
	private String memberId;
	private Date createDate;
	private String content;

	public ReplyCommand() {
	}

	public ReplyCommand(int replyId, String memberId, Date createDate, String content) {
		this.replyId = replyId;
		this.memberId = memberId;
		this.createDate = createDate;
		this.content = content;
	}

	public int getReplyId() { return replyId; }
	public void setReplyId(int replyId) { this.replyId = replyId; }
	public String getMemberId() { return memberId; }
	public void setMemberId(String memberId) { this.memberId = memberId; }
	public Date getCreateDate() { return createDate; }
	public void setCreateDate(Date createDate) { this.createDate = createDate; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
}
