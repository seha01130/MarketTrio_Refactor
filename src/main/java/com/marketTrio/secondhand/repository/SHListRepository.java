package com.marketTrio.secondhand.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.marketTrio.secondhand.domain.SecondHandEntity;

public interface SHListRepository extends JpaRepository<SecondHandEntity, Integer> {
	List<SecondHandEntity> findByBuyerId(String memberId);
	List<SecondHandEntity> findByMember_Id(String memberId);
	List<SecondHandEntity> findAllByOrderByCreateDateDesc();

	@Modifying
	@Query("UPDATE SecondHandEntity s SET buyerId = :buyerId, s.SHStatus = 1 WHERE s.SHPostId = :SHPostId")
	void updateBuyer(@Param("buyerId") String buyerId, @Param("SHPostId") int SHPostId);
}
