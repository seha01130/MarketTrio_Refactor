package com.marketTrio.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ReviewMyBatis implements Serializable {
	private String reviewId;
	private String SHPostId;
	private String senderId;
	private String receiverId;
	private float rating;
	
	public ReviewMyBatis() {
		super();
	}
	public ReviewMyBatis(String sHPostId, String senderId, String receiverId) {
		super();
		this.SHPostId = sHPostId;
		this.senderId = senderId;
		this.receiverId = receiverId;
	}
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public String getSHPostId() {
		return SHPostId;
	}
	public void setSHPostId(String sHPostId) {
		SHPostId = sHPostId;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
}
