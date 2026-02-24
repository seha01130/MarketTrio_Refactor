package com.marketTrio.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name="Opt")
@Embeddable
public class OptionEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "option_seq")
	@SequenceGenerator(name = "option_seq", sequenceName = "SEQUENCE_OPTIONID", allocationSize = 1)
	private int optionId;
	
	@ManyToOne
	@JoinColumn(name="GBPostId")
	private GBEntity gbEntity;
	
	private String optionName;
	private int quantity;
	private int remainingQuantity;
	
	public OptionEntity() {
	}
		
	public OptionEntity(int optionId, String optionName, int quantity, int remainingQuantity) {
		super();
		this.optionId = optionId;
		this.optionName = optionName;
		this.quantity = quantity;
		this.remainingQuantity = remainingQuantity;
	}

	public int getOptionId() {
		return optionId;
	}
	public void setOptionId(int optionId) {
		this.optionId = optionId;
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

	public GBEntity getGBEntity() {
		return gbEntity;
	}

	public void setGBEntity(GBEntity gbEntity) {
		this.gbEntity = gbEntity;
	}

	@Override
	public String toString() {
		return "OptionEntity [optionId=" + optionId + ", optionName=" + optionName + ", quantity=" + quantity
				+ ", remainingQuantity=" + remainingQuantity + "]";
	}
	
	
}
