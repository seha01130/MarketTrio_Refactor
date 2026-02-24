package com.marketTrio.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="Review")
public class ReviewEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "review_seq")
    @SequenceGenerator(name = "review_seq", sequenceName = "SEQUENCE_REVIEWID", allocationSize = 1)
    private int reviewId;
    private int SHPostId;
    private String senderId;
    private String receiverId;
    private float rating;
    
	public ReviewEntity() {
		super();
	}

	public ReviewEntity(int SHPostId, String senderId, String receiverId, float rating) {
		this.SHPostId = SHPostId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.rating = rating;
    }

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public int getSHPostId() {
		return SHPostId;
	}

	public void setSHPostId(int sHPostId) {
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