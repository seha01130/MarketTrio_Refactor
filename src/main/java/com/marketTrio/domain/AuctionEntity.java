package com.marketTrio.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@Table(name="Auction")
public class AuctionEntity {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auction_seq")
	 @SequenceGenerator(name = "auction_seq", sequenceName = "SEQUENCE_APOSTID", allocationSize = 1)
	 private int auctionPostId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private int startPrice;

	@Column(nullable = false)
	private int maxPrice;

	@Column(nullable = false)
	private String detailInfo;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(nullable = false)
	private Date deadline;
	
	 @ElementCollection
	    @CollectionTable(name = "AUCTION_PICTURES", joinColumns = @JoinColumn(name = "AUCTION_ID"))
	    @Column(name = "PICTURE", nullable = false)
	    private List<String> pictures;

	@Column(nullable = false)
	private int auctionStatus;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date createDate;
	
	@OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AParticipantEntity> participants;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SELLERID")
	private Member member;

	 public List<String> getPictures() {
	        return pictures;
	    }

	 @Column(nullable = true)
	    private Double latitude;

	    @Column(nullable = true)
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
	 
	    public void setPictures(List<String> pictures) {
	        this.pictures = pictures;
	    }

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public int getAuctionPostId() {
		return auctionPostId;
	}

	public void setAuctionPostId(int auctionPostId) {
		this.auctionPostId = auctionPostId;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
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

	public int getAuctionStatus() {
		return auctionStatus;
	}

	public void setAuctionStatus(int auctionStatus) {
		this.auctionStatus = auctionStatus;
	}

	public List<AParticipantEntity> getParticipants() {
		return participants;
	}

	public void setParticipants(List<AParticipantEntity> participants) {
		this.participants = participants;
	}
}
