package com.marketTrio.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "APARTICIPANT")
public class AParticipantEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aPart_seq")
	@SequenceGenerator(name = "aPart_seq", sequenceName = "SEQUENCE_APARTID", allocationSize = 1)
	private int aPartId;
	
//	@OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MEMBERID")
//	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBERID")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUCTIONPOSTID")
	private AuctionEntity auction;

	@Column(nullable = false)
	private int bidstatus;

	@Column
	private int participatePrice;

	public int getParticipatePrice() {
		return participatePrice;
	}

	public void setParticipatePrice(int participatePrice) {
		this.participatePrice = participatePrice;
	}
	

	public int getaPartId() {
		return aPartId;
	}

	public void setaPartId(int aPartId) {
		this.aPartId = aPartId;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public AuctionEntity getAuction() {
		return auction;
	}

	public void setAuction(AuctionEntity auction) {
		this.auction = auction;
	}

	public int getBidstatus() {
		return bidstatus;
	}

	public void setBidstatus(int bidstatus) {
		this.bidstatus = bidstatus;
	}
}
