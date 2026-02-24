package com.marketTrio.secondhand.controller;

import java.util.Date;
import java.util.List;

public class SHMyListCommand {
	private int SHPostId;
	private List<String> image;
	private String title;
	private Date createDate;
	private int price;
	private int shStatus;
	private String buyerId;
	private String sellerId;
	private int reviewStatus;

	public SHMyListCommand() {
	}

	public int getSHPostId() { return SHPostId; }
	public void setSHPostId(int sHPostId) { SHPostId = sHPostId; }
	public List<String> getImage() { return image; }
	public void setImage(List<String> image) { this.image = image; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public Date getCreateDate() { return createDate; }
	public void setCreateDate(Date createDate) { this.createDate = createDate; }
	public int getPrice() { return price; }
	public void setPrice(int price) { this.price = price; }
	public int getShStatus() { return shStatus; }
	public void setShStatus(int shStatus) { this.shStatus = shStatus; }
	public String getBuyerId() { return buyerId; }
	public void setBuyerId(String buyerId) { this.buyerId = buyerId; }
	public String getSellerId() { return sellerId; }
	public void setSellerId(String sellerId) { this.sellerId = sellerId; }
	public int getReviewStatus() { return reviewStatus; }
	public void setReviewStatus(int reviewStatus) { this.reviewStatus = reviewStatus; }
}
