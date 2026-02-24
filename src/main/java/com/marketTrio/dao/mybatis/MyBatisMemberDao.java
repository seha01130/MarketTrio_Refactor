package com.marketTrio.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.marketTrio.dao.mybatis.mapper.MemberMapper;
import com.marketTrio.domain.Member;


@Repository
public class MyBatisMemberDao {
	@Autowired
    private SqlSession sqlSession;
	@Autowired
	private MemberMapper memberMapper;
	
	public Member getMember(String id) throws DataAccessException {
		return memberMapper.getMemberById(id);
	}
	
	public String getSellerIdFromSH(int postId) throws DataAccessException {
		return memberMapper.getSellerIdFromSH(postId);
	}
	
	public String getProfilePicture(String memberId) throws DataAccessException {
		return memberMapper.getProfilePicture(memberId);
	}

	public Member getMember(String id, String password) throws DataAccessException {
		return memberMapper.getMemberByIdAndPassword(id, password);
	}
	
	public boolean isIdExist(String id) throws DataAccessException {
		return memberMapper.isIdExist(id);
	}
	public String getPassword(String id) throws DataAccessException {
		return memberMapper.getPassword(id);
	}
	
	public String getNickname(String id) throws DataAccessException {
		return memberMapper.getNicknameById(id);
	}
	
	public float getRate(String id) throws DataAccessException {
		return memberMapper.getRateById(id);
	}

    public void updateRate(String sellerId, float newRate) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("newRate", newRate);
        parameterMap.put("id", sellerId);

        sqlSession.update("updateRateById", parameterMap);
    }

	public void insertMember(Member member) throws DataAccessException {
		memberMapper.insertMember(member);
	}

	@Transactional
	public void updateMember(Member member) throws DataAccessException {
		memberMapper.updateMember(member);
	}
	
	public void deleteMember(String id) throws DataAccessException {
		memberMapper.deleteMember(id);
	}
}

