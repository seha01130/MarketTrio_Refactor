package com.marketTrio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketTrio.controller.SHMyListCommand;
import com.marketTrio.domain.AuctionEntity;
import com.marketTrio.domain.SecondHandEntity;
import com.marketTrio.repository.AListRepository;
import com.marketTrio.dao.jpa.JPAAuctionAndGroupBuyListDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AService {
    @Autowired
    private AListRepository AListRepository;
    @Autowired
    private JPAAuctionAndGroupBuyListDao jpaListDao;

    //@Transactional(readOnly = true)
    public List<AuctionEntity> getAPurchaseListByMemberId(String memberId) {
    	List<Integer> purchaseListIds  = jpaListDao.getAPostIdList(memberId); //내가 참여한 옥션의 게시글ID의 List를 가져오기
    	
//    	// String 타입의 게시글 ID 리스트를 Integer 타입으로 변환
//        List<Integer> purchaseListIds = purchaseListString.stream()
//                                                           .map(Integer::parseInt)
//                                                           .collect(Collectors.toList());
        
        // ID 목록을 사용하여 AuctionEntity 객체 목록을 조회
        List<AuctionEntity> purchaseList = AListRepository.findByAuctionPostIdIn(purchaseListIds);
    	
        return purchaseList;
    }

    //@Transactional(readOnly = true)
    public List<AuctionEntity> getASalesListByMemberId(String memberId) {
    	List<AuctionEntity> salesList =  AListRepository.findByMember_Id(memberId);
        return salesList;
    }
    //////////////////////여기까지가 수연 사용 - myInfo에서 List 가져오는부분 //////////////////////////////
}
