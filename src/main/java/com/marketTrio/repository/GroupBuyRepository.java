package com.marketTrio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.marketTrio.domain.GBEntity;
import com.marketTrio.domain.GBParticipantEntity;

public interface GroupBuyRepository extends JpaRepository<GBEntity, Integer> {
	// CRUD는 여기서 정의 안 하고 Service에서 바로 쓰면 됨
//	
//	List<GBEntity> findAll();
//	
//	//ID로 특정 객체 찾기
//	GBPostEntity findByGBPostId(int GBPostId);
	List<GBParticipantEntity> findByGBPostId(int gbPostd);
}
