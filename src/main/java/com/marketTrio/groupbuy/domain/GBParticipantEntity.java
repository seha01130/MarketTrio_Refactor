package com.marketTrio.groupbuy.domain;

import javax.persistence.CascadeType;
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

import com.marketTrio.member.domain.Member;
import com.marketTrio.option.domain.OptionEntity;

@Entity
@Table(name = "GBParticipant")
public class GBParticipantEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gbPart_seq")
	@SequenceGenerator(name = "gbPart_seq", sequenceName = "SEQUENCE_GBPARTID", allocationSize = 1)
	private int gbPartId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBERID")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GBPostId")
	private GBEntity gbEntity;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "optionId")
	private OptionEntity myOption;
	private int myQuantity;

	public GBParticipantEntity() {
	}

	public GBParticipantEntity(int gbPartId, Member member, GBEntity gbPost, OptionEntity myOption, int myQuantity) {
		this.gbPartId = gbPartId;
		this.member = member;
		this.gbEntity = gbPost;
		this.myOption = myOption;
		this.myQuantity = myQuantity;
	}

	public int getGbPartId() { return gbPartId; }
	public void setGbPartId(int gbPartId) { this.gbPartId = gbPartId; }
	public GBEntity getGbPost() { return gbEntity; }
	public void setGbPost(GBEntity gbPost) { this.gbEntity = gbPost; }
	public Member getMember() { return member; }
	public void setMember(Member member) { this.member = member; }
	public OptionEntity getMyOption() { return myOption; }
	public void setMyOption(OptionEntity myOption) { this.myOption = myOption; }
	public int getMyQuantity() { return myQuantity; }
	public void setMyQuantity(int myQuantity) { this.myQuantity = myQuantity; }

	@Override
	public String toString() {
		return "GBParticipantEntity [gbPartId=" + gbPartId + ", member=" + member.getId() + ", gbPost=" + gbEntity.getGBPostId()
				+ ", myOption=" + (myOption != null ? myOption.getOptionId() : null) + ", myQuantity=" + myQuantity + "]";
	}
}
