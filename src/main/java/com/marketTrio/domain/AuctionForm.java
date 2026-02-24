package com.marketTrio.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
public class AuctionForm implements Serializable{
    private String name;
    private int startPrice;
    private String detailInfo;
    private Double latitude;
    private Double longitude;
    

    public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deadline;
    private String picture;
    
    public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getStartPrice() {
        return startPrice;
    }
    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }
    public String getDetailInfo() {
        return detailInfo;
    }
    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }
    public Date getDeadline() {
        return deadline;
    }
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
    
    
}
