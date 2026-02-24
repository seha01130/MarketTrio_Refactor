package com.marketTrio.domain;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name="SecondHand")
public class SecondHandEntity implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secondhand_seq")
	@SequenceGenerator(name = "secondhand_seq", sequenceName = "SEQUENCE_SHPOSTID", allocationSize = 1)
	private int SHPostId;
	private String buyerId;
	//    private String sellerId;
	@Column(nullable = false)
	private int SHStatus;
	@Column(nullable = false)
	private Date createDate;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private int price;

	@ElementCollection
	@CollectionTable(name = "SHPOST_PICTURES", joinColumns = @JoinColumn(name = "SHPOSTID"))
	@Column(name = "IMAGE", nullable = false)
	private List<String> image;


	@Column(nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sellerId", referencedColumnName="id", nullable = false)
	private Member member;

//	@OneToOne (cascade = CascadeType.ALL)
//	@PrimaryKeyJoinColumn(name="SHPostId")
//	private ReviewEntity review;

	public SecondHandEntity() {	
		super();
	}

	public SecondHandEntity(int sHPostId, String buyerId, Member member, int sHStatus, Date createDate, String title,
			int price, List<String> image, String content) {
		super();
		SHPostId = sHPostId;
		this.buyerId = buyerId;
		this.member = member;
		SHStatus = sHStatus;
		this.createDate = createDate;
		this.title = title;
		this.price = price;
		this.image = image;
		this.content = content;
	}

	public int getSHPostId() {
		return SHPostId;
	}

	public void setSHPostId(int sHPostId) {
		SHPostId = sHPostId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public int getSHStatus() {
		return SHStatus;
	}

	public void setSHStatus(int sHStatus) {
		SHStatus = sHStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public List<String> getImage() {
		return image;
	}

	public void setImage(List<String> image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}