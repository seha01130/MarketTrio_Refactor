package com.marketTrio.auction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketTrio.auction.domain.AuctionEntity;

public interface AListRepository extends JpaRepository<AuctionEntity, Integer> {
	List<AuctionEntity> findByMember_Id(String memberId);
	List<AuctionEntity> findByAuctionPostIdIn(List<Integer> auctionPostIds);
}
