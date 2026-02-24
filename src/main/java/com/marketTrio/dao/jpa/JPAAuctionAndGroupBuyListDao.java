package com.marketTrio.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class JPAAuctionAndGroupBuyListDao {

	@PersistenceContext
    private EntityManager em;

	//내가 참여한 Auction게시글의Id를 리스트로 받아오기
	public List<Integer> getAPostIdList(String memberId) throws DataAccessException {
		TypedQuery<Integer> query = em.createQuery(
                "select a.auction.auctionPostId from AParticipantEntity a where a.member.id = ?1", Integer.class);
		query.setParameter(1,  memberId);
        return query.getResultList();
    }
	
	//내가 참여한 GroupBuy게시글의Id를 리스트로 받아오기
	public List<Integer> getGBPostIdList(String memberId) throws DataAccessException {
		TypedQuery<Integer> query = em.createQuery(
                "select b.gbEntity.GBPostId from GBParticipantEntity b where b.member.id = ?1", Integer.class);
		query.setParameter(1,  memberId);
        return query.getResultList();
    }
}