package com.marketTrio.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.marketTrio.member.domain.Member;

@Mapper
public interface MemberMapper {
	Member getMemberById(String id);
	String getSellerIdFromSH(int postId);
	String getProfilePicture(String memberId);
	Member getMemberByIdAndPassword(String id, String password);
	boolean isIdExist(String id);
	String getPassword(String id);
	String getNicknameById(String id);
	float getRateById(String id);
	void updateRateById(String id, float newRate);
	List<String> getNicknameList();
	void insertMember(Member member);
	void updateMember(Member member);
	void deleteMember(String id);
}
