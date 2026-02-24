package com.marketTrio.groupbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketTrio.groupbuy.domain.GBEntity;
import com.marketTrio.groupbuy.domain.GBParticipantEntity;

public interface GroupBuyPartRepository extends JpaRepository<GBParticipantEntity, Integer> {
	GBParticipantEntity findByGbEntity_GBPostIdAndMember_Id(int gbPostId, String memberId);
	List<GBParticipantEntity> findByGbEntity(GBEntity gbEntity);
	List<GBParticipantEntity> findByGbEntity_GBPostId(int gbPostId);
	List<GBParticipantEntity> findAllByMember_Id(String memberId);
}
