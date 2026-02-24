package com.marketTrio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marketTrio.domain.AuctionEntity;
import com.marketTrio.domain.SecondHandEntity;

@Repository
public interface AListRepository extends JpaRepository<AuctionEntity, Integer> {
	
    List<AuctionEntity> findByMember_Id(String memberId); //내가 판 AuctionEntity 리스트 가져오기

	List<AuctionEntity> findByAuctionPostIdIn(List<Integer> auctionPostIds); //내가 산 AuctionEntity 리스트 가져오기
}
