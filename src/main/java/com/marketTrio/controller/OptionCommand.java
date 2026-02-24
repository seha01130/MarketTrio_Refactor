package com.marketTrio.controller;

import java.sql.Date;
import java.util.ArrayList;

import com.marketTrio.domain.OptionEntity;

public class OptionCommand {
	private String optionName;
	private int quantity;
	private int remainingQuantity;
	
	public OptionCommand(String optionName, int quantity, int remainingQuantity) {
		super();
		this.optionName = optionName;
		this.quantity = quantity;
		this.remainingQuantity = remainingQuantity;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getRemainingQuantity() {
		return remainingQuantity;
	}

	public void setRemainingQuantity(int remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}
}
