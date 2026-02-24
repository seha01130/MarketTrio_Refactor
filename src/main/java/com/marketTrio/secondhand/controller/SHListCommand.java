package com.marketTrio.secondhand.controller;

import java.util.List;

public class SHListCommand {
	private List<String> image;
	private String title;
	private int price;
	private String content;

	public SHListCommand() {
	}

	public SHListCommand(List<String> image, String title, int price, String content) {
		this.image = image;
		this.title = title;
		this.price = price;
		this.content = content;
	}

	public List<String> getImage() { return image; }
	public void setImage(List<String> image) { this.image = image; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public int getPrice() { return price; }
	public void setPrice(int price) { this.price = price; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
}
