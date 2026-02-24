package com.marketTrio.option.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketTrio.groupbuy.domain.GBEntity;
import com.marketTrio.option.domain.OptionEntity;

public interface OptionRepository extends JpaRepository<OptionEntity, Integer> {
	List<OptionEntity> findByGbEntity(GBEntity gbEntity);
}
