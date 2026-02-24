package com.marketTrio.groupbuy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marketTrio.groupbuy.domain.GBEntity;

public interface GroupBuyRepository extends JpaRepository<GBEntity, Integer> {
}
