package com.marketTrio.controller;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MemberSession implements Serializable {
//	private Member member;
//
//	public MemberSession(Member member) {
//		this.member = member;
//	}
//
//	public Member getMember() {
//		return member;
//	}
	
	private String memberId;
	
	public MemberSession (String memberId) {
		this.memberId = memberId;
	}
	
	public String getMemberId() {
		return memberId;
	}
}
