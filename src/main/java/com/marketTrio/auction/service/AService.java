package com.marketTrio.auction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketTrio.auction.domain.AuctionEntity;
import com.marketTrio.auction.repository.AListRepository;
import com.marketTrio.common.dao.JPAAuctionAndGroupBuyListDao;

@Service
public class AService {
	@Autowired
	private AListRepository aListRepository;
	@Autowired
	private JPAAuctionAndGroupBuyListDao jpaListDao;

	public List<AuctionEntity> getAPurchaseListByMemberId(String memberId) {
		List<Integer> purchaseListIds = jpaListDao.getAPostIdList(memberId);
		return aListRepository.findByAuctionPostIdIn(purchaseListIds);
	}

	public List<AuctionEntity> getASalesListByMemberId(String memberId) {
		return aListRepository.findByMember_Id(memberId);
	}
}
