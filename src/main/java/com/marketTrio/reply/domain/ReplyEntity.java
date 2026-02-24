package com.marketTrio.reply.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.marketTrio.member.domain.Member;
import com.marketTrio.secondhand.domain.SecondHandEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "Reply")
public class ReplyEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reply_seq")
	@SequenceGenerator(name = "reply_seq", sequenceName = "SEQUENCE_REPLYID", allocationSize = 1)
	private int replyId;
	@Column(nullable = false)
	private Date createDate;
	@Column(nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId", referencedColumnName = "id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHPostId", referencedColumnName = "SHPostId", nullable = false)
	private SecondHandEntity SHEntity;

	public ReplyEntity() {
	}

	public ReplyEntity(int replyId, SecondHandEntity SHEntity, Member member, Date createDate, String content) {
		this.replyId = replyId;
		this.SHEntity = SHEntity;
		this.member = member;
		this.createDate = createDate;
		this.content = content;
	}

	public int getReplyId() { return replyId; }
	public void setReplyId(int replyId) { this.replyId = replyId; }
	public SecondHandEntity getSHEntity() { return SHEntity; }
	public void setSHEntity(SecondHandEntity sHEntity) { SHEntity = sHEntity; }
	public Member getMember() { return member; }
	public void setMember(Member member) { this.member = member; }
	public Date getCreateDate() { return createDate; }
	public void setCreateDate(Date createDate) { this.createDate = createDate; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
}
