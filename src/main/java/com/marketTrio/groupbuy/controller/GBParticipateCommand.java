package com.marketTrio.groupbuy.controller;

import java.io.Serializable;

import javax.validation.constraints.Min;

@SuppressWarnings("serial")
public class GBParticipateCommand implements Serializable {
	private String member;
	private int GBPost;
	private int optionId;
	@Min(value = 0, message = "옵션 수량은 음수가 될 수 없습니다.")
	private int quantity;

	public GBParticipateCommand() {
	}

	public GBParticipateCommand(String member, int gbPost, int option, int quantity) {
		this.member = member;
		this.GBPost = gbPost;
		this.optionId = option;
		this.quantity = quantity;
	}

	public int getGBPostId() { return GBPost; }
	public void setGBPostId(int gBPost) { GBPost = gBPost; }
	public String getMember() { return member; }
	public void setMember(String member) { this.member = member; }
	public int getOptionId() { return optionId; }
	public void setOptionId(int optionId) { this.optionId = optionId; }
	public int getQuantity() { return quantity; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
}
