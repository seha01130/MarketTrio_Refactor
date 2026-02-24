package com.marketTrio.auction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketTrio.auction.domain.AParticipantEntity;

public interface AParticipantRepository extends JpaRepository<AParticipantEntity, Integer> {
	AParticipantEntity findByAuction_AuctionPostIdAndMember_Id(int auctionId, String memberId);
	List<AParticipantEntity> findByAuction_AuctionPostId(int auctionPostId);
}
