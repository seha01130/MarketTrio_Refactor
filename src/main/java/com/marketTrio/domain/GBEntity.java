package com.marketTrio.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name="GroupBuy")
public class GBEntity implements Serializable{
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gbpost_seq")
    @SequenceGenerator(name = "gbpost_seq", sequenceName = "SEQUENCE_GBPOSTID", allocationSize = 1)
	private int GBPostId;
    
	@ManyToOne //공구 글 : 작성자, 단방향 N:1
	@JoinColumn(name="memberId")
	private Member member;
	
//	@OneToMany(cascade = CascadeType.ALL) //공구 글 : 옵션, 단방향 1:n
//	@JoinColumn(name = "GBPostId") // 외래 키 설정
//	private List<OptionEntity> options;
	
//	@OneToMany(cascade = CascadeType.ALL)	  // 공구 글 : 참여자, 단방향 1:n
//	@JoinColumn(name = "GBPostId") // 외래 키 설정
//	private List<GBParticipantEntity> participants;
	
	@OneToMany(mappedBy = "gbEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GBParticipantEntity> participants = new ArrayList<>();
	
	private String productName;
	private Date duration;
	private int allQuantity;
	private int regularPrice;
//	@Transient	// 판매가는 db 저장 X
//	private String salePrice;
	private double discountRate;
	private String content;
	private Date createDate;
	@ElementCollection
	@CollectionTable(name = "GROUPBUY_PICTURES", joinColumns = @JoinColumn(name = "GBPOSTID"))
	@Column(name = "PICTURE", nullable = false)
	private List<String> pictures;
	private int GBStatus;
	
	public GBEntity() {
	}
	
	public GBEntity(int gbPostId, Member member, String productName, Date duration, int allQuantity,
			int regularPrice, double discountRate, String content, Date createDate, String image, int gbStatus) {
		super();
		GBPostId = gbPostId;
		this.member = member;
		this.productName = productName;
		this.duration = duration;
		this.allQuantity = allQuantity;
		this.regularPrice = regularPrice;
		this.discountRate = discountRate;
		this.content = content;
		this.createDate = createDate;
		this.GBStatus = gbStatus;
	}

	public int getGBPostId() {
		return GBPostId;
	}
	
	public void setGBPostId(int gbPostId) {
		GBPostId = gbPostId;
	}
	
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
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
	public void setRegularPrice(int d) {
		this.regularPrice = d;
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
	public List<GBParticipantEntity> getParticipants() {
		return participants;
	}

	public void setParticipants(List<GBParticipantEntity> participants) {
		this.participants = participants;
	}

	public List<String> getPictures() {
		return pictures;
	}

	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}

	public int getGBStatus() {
		return GBStatus;
	}
	public void setGBStatus(int gBStatus) {
		GBStatus = gBStatus;
	}
//	public String getSalePrice() {
//		return salePrice;
//	}
//	public void setSalePrice(String salePrice) {
//		this.salePrice = salePrice;
//	}
}
