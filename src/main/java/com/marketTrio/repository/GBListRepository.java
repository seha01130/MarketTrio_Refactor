package com.marketTrio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marketTrio.domain.AuctionEntity;
import com.marketTrio.domain.GBEntity;
import com.marketTrio.domain.SecondHandEntity;

@Repository
public interface GBListRepository extends JpaRepository<GBEntity, Integer> {
	
    List<GBEntity> findByMemberId(String memberId); //내가 판 GBEntity 리스트 가져오기
    
	List<GBEntity> findByGBPostIdIn(List<Integer> gbPostIds); //내가 판 GBEntity 리스트 가져오기
}
