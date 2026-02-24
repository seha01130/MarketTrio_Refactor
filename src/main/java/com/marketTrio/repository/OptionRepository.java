package com.marketTrio.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketTrio.domain.GBEntity;
import com.marketTrio.domain.OptionEntity;

public interface OptionRepository extends JpaRepository<OptionEntity, Integer> {
	//gbPostId와 현재 보는 글의 gbPostId와 같은 옵션들을 
	List<OptionEntity> findByGbEntity(GBEntity gbEntity);
}