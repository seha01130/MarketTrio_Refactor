package com.marketTrio.controller;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class GBUpdateInfoCommand {
	private Integer id;
	private String productName;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date duration;
	private String content;
	
	public GBUpdateInfoCommand(Integer id, String productName, Date duration, String content) {
		super();
		this.id = id;
		this.productName = productName;
		this.duration = duration;
		this.content = content;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "GBUpdateInfoCommand [id=" + id + ", productName=" + productName + ", duration=" + duration
				+ ", content=" + content + "]";
	}
}
