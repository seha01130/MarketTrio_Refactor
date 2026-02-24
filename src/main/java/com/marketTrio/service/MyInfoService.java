package com.marketTrio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketTrio.dao.mybatis.MyBatisMemberDao;
import com.marketTrio.domain.Member;
import com.marketTrio.controller.MemberCommand;

@Service
public class MyInfoService {

    @Autowired
    private MyBatisMemberDao memberDao;
    
    public Member getMember(String memberId) throws Exception {
    	Member member = memberDao.getMember(memberId);
    	return member;
    }
    
    public String getSellerIdFromSH(int postId) throws Exception {
    	return memberDao.getSellerIdFromSH(postId);
    }
    
    public String getProfilePicture(String memberId) throws Exception {
    	return memberDao.getProfilePicture(memberId);
    }

    public Member getMember(String id, String password) throws Exception {
    	return memberDao.getMember(id, password);
    }
    
    public boolean isIdExist(String id) throws Exception {
    	return memberDao.isIdExist(id);
    }
    public MemberCommand getMemberCommand(String memberId) throws Exception {
        Member member = memberDao.getMember(memberId);
        return new MemberCommand(member);
    }
    
    public float getRate(String memberId) throws Exception {
    	return memberDao.getRate(memberId);
    }
    
    public void updateRate(String memberId, float newRate) throws Exception {
    	memberDao.updateRate(memberId, newRate);
    }
    
    public String getNickname(String memberId) throws Exception {
    	return memberDao.getNickname(memberId);
    }

    public String getPassword(String memberId) throws Exception {
        return memberDao.getPassword(memberId);
    }

    public void insertMember(Member member) throws Exception{
    	memberDao.insertMember(member);
    }
    public void updateMember(Member member) throws Exception {
        memberDao.updateMember(member);
    }

    public void deleteMember(String memberId) throws Exception {
        memberDao.deleteMember(memberId);
    }
}
