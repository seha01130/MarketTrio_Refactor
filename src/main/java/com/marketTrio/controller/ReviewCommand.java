package com.marketTrio.controller;

import java.io.Serializable;

import com.marketTrio.domain.ReviewEntity;

@SuppressWarnings("serial")
public class ReviewCommand implements Serializable {
	private ReviewEntity review;
	private String senderNickname;
	private String receiverNickname;

	public ReviewCommand() {}
	
	public ReviewCommand(ReviewEntity review, String senderNickname, String receiverNickname) {
		this.review = review;
		this.senderNickname = senderNickname;
		this.receiverNickname = receiverNickname;
	}

	public ReviewEntity getReview() {
		return review;
	}
	public void setReview(ReviewEntity review) {
		this.review = review;
	}
	public String getSenderNickname() {
		return senderNickname;
	}
	public void setSenderNickname(String senderNickname) {
		this.senderNickname = senderNickname;
	}
	public String getReceiverNickname() {
		return receiverNickname;
	}
	public void setReceiverNickname(String receiverNickname) {
		this.receiverNickname = receiverNickname;
	} 
}
