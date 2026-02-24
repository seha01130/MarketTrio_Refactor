package com.marketTrio.auction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketTrio.auction.domain.AuctionEntity;

public interface AuctionRepository extends JpaRepository<AuctionEntity, Integer> {
	List<AuctionEntity> findAll();
	List<AuctionEntity> findByAuctionStatus(int bidStatus);
	List<AuctionEntity> findByAuctionStatusOrderByDeadlineAsc(int i);
}
