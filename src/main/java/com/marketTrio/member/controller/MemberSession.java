package com.marketTrio.member.controller;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MemberSession implements Serializable {
	private String memberId;

	public MemberSession(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberId() {
		return memberId;
	}
}
