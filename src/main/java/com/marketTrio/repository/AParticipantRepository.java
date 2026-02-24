package com.marketTrio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketTrio.domain.AParticipantEntity;

public interface AParticipantRepository extends JpaRepository<AParticipantEntity, Integer> {
	// auctionRepository에 auctionId, bidAmount, memberId를 받아 해당 auctionId의 AParticipantEntity 중 memberId가 주어진 memberId와 일치하는 열의 participantPrice를 bidAmount로 변경
	 AParticipantEntity findByAuction_AuctionPostIdAndMember_Id(int auctionId, String memberId);
	 
	 List<AParticipantEntity> findByAuction_AuctionPostId(int auctionPostId);

}
