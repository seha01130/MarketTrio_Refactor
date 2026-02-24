package com.marketTrio.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketTrio.dao.mybatis.MyBatisMemberDao;
import com.marketTrio.domain.AParticipantEntity;
import com.marketTrio.domain.AuctionEntity;
import com.marketTrio.domain.AuctionForm;
import com.marketTrio.domain.Member;
import com.marketTrio.repository.AParticipantRepository;
import com.marketTrio.repository.AuctionRepository;

@Service
public class AuctionService {
	//spring jpa 이용 (auction 관련 질의)
	@Autowired
	private AuctionRepository auctionRepository;
	
	// (입찰 관련 질의)
	 @Autowired
	    private AParticipantRepository aParticipantRepository;

	 //경매마감에 대하여 스케쥴러 이용하기
	 @Autowired
	    private TaskScheduler taskScheduler;
	 
	//mybatis 이용
	@Autowired	
	private MyBatisMemberDao memberDao;
	
	public List<AuctionEntity> getNotAvailableAuctionList(){
	      return auctionRepository.findByAuctionStatusOrderByDeadlineAsc(1);
	   }

	public AuctionEntity createAuction(AuctionForm postData, String memberId,List<String> uploadedFileNames) throws IllegalStateException, IOException {
		AuctionEntity auctionEntity = new AuctionEntity();
		
		 auctionEntity.setName(postData.getName());
	        auctionEntity.setStartPrice(postData.getStartPrice());
	        auctionEntity.setMaxPrice(postData.getStartPrice()); //필드 초기화
	        auctionEntity.setDetailInfo(postData.getDetailInfo());
	        auctionEntity.setDeadline(postData.getDeadline());
	        auctionEntity.setAuctionStatus(0); //필드 초기화
	        auctionEntity.setCreateDate(new Date());
	        auctionEntity.setPictures(uploadedFileNames);
			Member member = memberDao.getMember(memberId);
			auctionEntity.setMember(member);
	        
			auctionEntity.setLatitude(postData.getLatitude());
			auctionEntity.setLongitude(postData.getLongitude());
			
			return auctionRepository.save(auctionEntity);
	}

	public AuctionEntity getAuction(int auctionId) {
		Optional<AuctionEntity> result = auctionRepository.findById(auctionId);
		if(result.isPresent()) return result.get();
		return null;
	}

	public AuctionEntity updateAPost(AuctionEntity auction) {
		return auctionRepository.save(auction);
	}
	
    @Transactional
	public void updateBidAmount(int auctionId, int bidAmount, String memberId) {
		AParticipantEntity participant = aParticipantRepository.findByAuction_AuctionPostIdAndMember_Id(auctionId, memberId);
		System.out.println(participant);
		 if (participant != null) {
	            participant.setParticipatePrice(bidAmount);
	            aParticipantRepository.save(participant);
	        } else {
	        	AParticipantEntity newparticipant = new AParticipantEntity();
	        	
	        	Optional<AuctionEntity> auction = auctionRepository.findById(auctionId);
	        	if (auction.isEmpty()) {
	                throw new IllegalArgumentException("Auction not found for given auctionId");
	        	}
	        	
	        	newparticipant.setMember(memberDao.getMember(memberId));
	        	newparticipant.setAuction(auction.get());
	        	newparticipant.setBidstatus(0);
	        	newparticipant.setParticipatePrice(bidAmount);
	        	

	            System.out.println("Saving new participant??! : " + newparticipant);
	            aParticipantRepository.save(newparticipant);
	        }
	}

    @Transactional
	public void cancelBid(int auctionId, String memberId) {
		AParticipantEntity participant = aParticipantRepository.findByAuction_AuctionPostIdAndMember_Id(auctionId, memberId);
		
		participant.setAuction(null);
		participant.setMember(null);
		
		aParticipantRepository.delete(participant);
		
	}
	
	 public void scheduleAuctionClose(AuctionEntity auction) {
		 Runnable updateTableRunner = new Runnable() { 
		     // anonymous class 정의
		     @Override
		     public void run() {   // 스케줄러에 의해 미래의 특정 시점(마감시각)에 실행될 작업을 정의    
		        Date curTime = new Date();
		        // 실행 시점의 현재 시각을 전달하여 그 시각 이전의 마감시각을 가진 event들의 상태를 변경함
		        Optional<AuctionEntity> auctionOptional = auctionRepository.findById(auction.getAuctionPostId());
		        if (auctionOptional.isPresent()) {
			        AuctionEntity changeAuction = auctionOptional.get();
			        changeAuction.setAuctionStatus(1);
			        auctionRepository.save(changeAuction);
			        System.out.println("updateTableRunner is executed at " + curTime);
			     }
		     }
		  };
		// closingTime설정
		  Date closingTime = auction.getDeadline();
		  taskScheduler.schedule(updateTableRunner, closingTime);
	 }

	 public List<AuctionEntity> getAvailableAuctions() {
	      return auctionRepository.findByAuctionStatusOrderByDeadlineAsc(0);
	   }
	
	
	 public List<AParticipantEntity> getParticipantsByAuctionId(int auctionId) {
	        return aParticipantRepository.findByAuction_AuctionPostId(auctionId);
	    }

	public void delete(int auctionId) {
		auctionRepository.deleteById(auctionId);
	}
	
	
//	@Autowired
//    private AuctionDao auctionDao;
//	
//	public AuctionEntity createAuction(AuctionForm postData) {
//		return auctionDao.createAuction(postData);
//	}
//
//	public AuctionEntity getAuction(int auctionId) {
//		// TODO Auto-generated method stub
//		return auctionDao.getAuction(auctionId);
//	}
//
//	public AuctionEntity modifyAuction(int auctionId, AuctionForm formData) {
//		// TODO Auto-generated method stub
//		return auctionDao.modifyAuction(auctionId, formData);
//	}
//	
//	public int deleteAuction(int auctionId) {
//		// TODO Auto-generated method stub
//		return auctionDao.deleteAuction(auctionId);
//	}
//
//	public Member getBidder(int auctionId) {
//		// TODO Auto-generated method stub
//		return auctionDao.getBidder(auctionId);
//	}
//
//	public int placeBid(int auctionId, int price) {
//		return auctionDao.placeBid(auctionId,price);
//	}
//  public List<Auction> getAuctionList() {
//      return auctionDao.getauctionList();
//  }
	
}
