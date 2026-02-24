package com.marketTrio.dao.mybatis.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.marketTrio.controller.MemberSession;
import com.marketTrio.domain.AuctionEntity;
import com.marketTrio.domain.AuctionForm;
import com.marketTrio.domain.Member;

@Mapper
public interface AuctionMapper {

	public List<AuctionEntity> getauctionList();

	public AuctionEntity createAuction(AuctionForm postData);
	
	public AuctionEntity getAuction(int auctionId);

	public AuctionEntity modifyAuction(int auctionId, AuctionForm formData);

	public int deleteAuction(int auctionId);
	
	public int findParticipant(MemberSession memberSession);

	public Member getBidder(int auctionId);

	public int placeBid(int auctionId, int price);

}
