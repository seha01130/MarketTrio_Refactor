package com.marketTrio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.marketTrio.domain.SecondHandEntity;

@Repository
public interface SHListRepository extends JpaRepository<SecondHandEntity, Integer> {

	List<SecondHandEntity> findByBuyerId(String memberId);  //내가 구매한 리스트 가져올떄
	   
    List<SecondHandEntity> findByMember_Id(String memberId); //내가 판매한 리스트 가져올떄
    
    List<SecondHandEntity> findAllByOrderByCreateDateDesc();

    @Modifying
	@Query("UPDATE SecondHandEntity s SET buyerId = :buyerId, s.SHStatus = 1 WHERE s.SHPostId = :SHPostId")
	void updateBuyer(@Param("buyerId") String buyerId, @Param("SHPostId") int SHPostId);
}
