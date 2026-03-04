package com.marketTrio.auction.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.FutureOrPresent;

@SuppressWarnings("serial")
public class AuctionForm implements Serializable {
	@NotBlank(message = "제목을 입력하세요")
	private String name;

	@Min(value = 1, message = "가격을 입력하세요")
	private int startPrice;

	@NotBlank(message = "상세설명을 입력해주세요")
	private String detailInfo;

	private Double latitude;
	private Double longitude;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "마감 날짜를 입력해주세요")
	@FutureOrPresent(message = "마감 날짜는 현재 시각 이후여야 합니다")
	private Date deadline;
	private String picture;

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
