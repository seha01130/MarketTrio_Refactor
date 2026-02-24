package com.marketTrio.dao.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.marketTrio.domain.Member;


@Mapper
public interface MemberMapper {
	Member getMemberById(String id);
	
	String getSellerIdFromSH(int postId);
	
	String getProfilePicture(String memberId);

	Member getMemberByIdAndPassword(String id, String password);
	
	boolean isIdExist(String id);
	
	String getPassword(String id);  //정보수정할때 비밀번호 입력받아서 비교할때 사용함
	
	String getNicknameById(String id);
	
	float getRateById(String id);
	
	void updateRateById(String id, float newRate);

	List<String> getNicknameList();
	  
	void insertMember(Member member);

	void updateMember(Member member);
	
	void deleteMember(String id);
}
