package com.marketTrio.common.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class JPAAuctionAndGroupBuyListDao {

	@PersistenceContext
	private EntityManager em;

	public List<Integer> getAPostIdList(String memberId) throws DataAccessException {
		TypedQuery<Integer> query = em.createQuery(
				"select a.auction.auctionPostId from AParticipantEntity a where a.member.id = ?1", Integer.class);
		query.setParameter(1, memberId);
		return query.getResultList();
	}

	public List<Integer> getGBPostIdList(String memberId) throws DataAccessException {
		TypedQuery<Integer> query = em.createQuery(
				"select b.gbPost.GBPostId from GBParticipantEntity b where b.member.id = ?1", Integer.class);
		query.setParameter(1, memberId);
		return query.getResultList();
	}
}
