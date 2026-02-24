package com.marketTrio.groupbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketTrio.groupbuy.domain.GBEntity;

public interface GBListRepository extends JpaRepository<GBEntity, Integer> {
	List<GBEntity> findByMember_Id(String memberId);
	List<GBEntity> findByGBPostIdIn(List<Integer> gbPostIds);
}
