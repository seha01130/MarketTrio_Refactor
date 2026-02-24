package com.marketTrio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketTrio.domain.AuctionEntity;
import com.marketTrio.domain.AuctionForm;

public interface AuctionRepository extends JpaRepository<AuctionEntity, Integer> {
	 List<AuctionEntity> findAll();

	List<AuctionEntity> findByAuctionStatus(int bidStatus);

	List<AuctionEntity> findByAuctionStatusOrderByDeadlineAsc(int i);
}
