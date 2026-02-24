package com.marketTrio.controller;

import java.util.Date;
import java.util.List;

public class SHMyListCommand {
    private int SHPostId;
//    private String image;
    private List<String> image;
    private String title;
    private Date createDate;
    private int price;
    
    private int shStatus;
    
    private String buyerId;
    private String sellerId;
    
    private int reviewStatus;  //이게 secondHandEntity에는 없는데 command에서는 필요함. 리스트에 보여줄때 후기 작성 여부를 판별해서
    							//그에 따른 후기작성하기 버튼을 보여줘야함.
    
    public SHMyListCommand() {
		super();
	}

	public SHMyListCommand(int sHPostId, List<String> image, String title, Date createDate, int price, int shStatus, String buyerId, String sellerId, int reviewStatus) {
		super();
		this.SHPostId = sHPostId;
		this.image = image;
		this.title = title;
		this.createDate = createDate;
		this.price = price;
		this.shStatus = shStatus;
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		this.reviewStatus = reviewStatus;
	}

	public int getSHPostId() {
		return SHPostId;
	}
	public void setSHPostId(int sHPostId) {
		this.SHPostId = sHPostId;
	}
//	public String getImage() {
//		return image;
//	}
//	public void setImage(String image) {
//		this.image = image;
//	}
	public List<String> getImage() {
		return image;
	}
	public void setImage(List<String> image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getShStatus() {
		return shStatus;
	}

	public void setShStatus(int shStatus) {
		this.shStatus = shStatus;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public int getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(int reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
}

