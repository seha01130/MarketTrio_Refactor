package com.marketTrio.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.marketTrio.domain.OptionEntity;

public class GBInfoCommand {
	private String productName;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date duration;
	private int allQuantity;
	private int regularPrice;
	private double discountRate;
	private String content;
	private Date createDate;
	private String image;
	private List<OptionEntity> options;
	
	public GBInfoCommand() {
		
	}
	
	public GBInfoCommand(List<OptionEntity> options, String productName, Date duration, int allQuantity, int regularPrice,
			double discountRate, String content, Date createDate, String image) {
		super();
		this.productName = productName;
		this.duration = duration;
		this.allQuantity = allQuantity;
		this.regularPrice = regularPrice;
		this.discountRate = discountRate;
		this.content = content;
		this.createDate = createDate;
		this.image = image;
		this.options = options;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getDuration() {
		return duration;
	}

	public void setDuration(Date duration) {
		this.duration = duration;
	}

	public int getAllQuantity() {
		return allQuantity;
	}

	public void setAllQuantity(int allQuantity) {
		this.allQuantity = allQuantity;
	}

	public int getRegularPrice() {
		return regularPrice;
	}

	public void setRegularPrice(int regularPrice) {
		this.regularPrice = regularPrice;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<OptionEntity> getOptions() {
		return options;
	}

	public void setOptions(List<OptionEntity> options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return "GBInfoCommand [productName=" + productName + ", duration=" + duration + ", allQuantity=" + allQuantity
				+ ", regularPrice=" + regularPrice + ", discountRate=" + discountRate + ", content=" + content
				+ ", createDate=" + createDate + ", image=" + image + ", options=" + options + "]";
	}

}
