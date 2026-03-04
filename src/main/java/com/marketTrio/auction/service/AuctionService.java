package com.marketTrio.auction.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketTrio.auction.domain.AParticipantEntity;
import com.marketTrio.auction.domain.AuctionEntity;
import com.marketTrio.auction.dto.AuctionForm;
import com.marketTrio.auction.exception.AuctionNotFoundException;
import com.marketTrio.auction.repository.AParticipantRepository;
import com.marketTrio.auction.repository.AuctionRepository;
import com.marketTrio.member.dao.MyBatisMemberDao;
import com.marketTrio.member.domain.Member;

@Service
public class AuctionService {

	private static final Logger log = LoggerFactory.getLogger(AuctionService.class);
	private static final int AUCTION_STATUS_OPEN = 0;
	private static final int AUCTION_STATUS_CLOSED = 1;

	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private AParticipantRepository aParticipantRepository;
	@Autowired
	private TaskScheduler taskScheduler;
	@Autowired
	private MyBatisMemberDao memberDao;

	public List<AuctionEntity> getNotAvailableAuctionList() {
		return auctionRepository.findByAuctionStatusOrderByDeadlineAsc(AUCTION_STATUS_CLOSED);
	}

	@Transactional
	public AuctionEntity createAuction(AuctionForm postData, String memberId, List<String> uploadedFileNames) throws IllegalStateException, IOException {
		AuctionEntity auctionEntity = new AuctionEntity();
		auctionEntity.setName(postData.getName());
		auctionEntity.setStartPrice(postData.getStartPrice());
		auctionEntity.setMaxPrice(postData.getStartPrice());
		auctionEntity.setDetailInfo(postData.getDetailInfo());
		LocalDate deadlineDate = postData.getDeadline();
		Date deadlineDateTime = Date.from(
				deadlineDate.atTime(LocalTime.of(23, 59, 59))
						.atZone(ZoneId.systemDefault())
						.toInstant()
		);
		auctionEntity.setDeadline(deadlineDateTime);
		auctionEntity.setAuctionStatus(AUCTION_STATUS_OPEN);
		auctionEntity.setCreateDate(new Date());
		auctionEntity.setPictures(uploadedFileNames);
		Member member = memberDao.getMember(memberId);
		auctionEntity.setMember(member);
		auctionEntity.setLatitude(postData.getLatitude());
		auctionEntity.setLongitude(postData.getLongitude());
		return auctionRepository.save(auctionEntity);
	}

	public AuctionEntity getAuction(int auctionId) {
		return auctionRepository.findById(auctionId)
				.orElseThrow(() -> new AuctionNotFoundException(auctionId));
	}

	public AuctionEntity updateAPost(AuctionEntity auction) {
		return auctionRepository.save(auction);
	}

	@Transactional
	public void updateBidAmount(int auctionId, int bidAmount, String memberId) {
		AParticipantEntity participant = aParticipantRepository.findByAuction_AuctionPostIdAndMember_Id(auctionId, memberId);
		if (participant != null) {
			participant.setParticipatePrice(bidAmount);
			aParticipantRepository.save(participant);
		} else {
			AParticipantEntity newParticipant = new AParticipantEntity();
			Optional<AuctionEntity> auction = auctionRepository.findById(auctionId);
			if (auction.isEmpty()) throw new IllegalArgumentException("Auction not found for given auctionId");
			newParticipant.setMember(memberDao.getMember(memberId));
			newParticipant.setAuction(auction.get());
			newParticipant.setBidstatus(0);
			newParticipant.setParticipatePrice(bidAmount);
			aParticipantRepository.save(newParticipant);
		}
	}

	@Transactional
	public void cancelBid(int auctionId, String memberId) {
		AParticipantEntity participant = aParticipantRepository.findByAuction_AuctionPostIdAndMember_Id(auctionId, memberId);
		participant.setAuction(null);
		participant.setMember(null);
		aParticipantRepository.delete(participant);
	}

	public void scheduleAuctionClose(int auctionId, Date deadline) {
		Date now = new Date();
		Date triggerTime = deadline.before(now) ? now : deadline;

		taskScheduler.schedule(() -> {
			try {
				auctionRepository.findById(auctionId).ifPresent(a -> {
					if (a.getAuctionStatus() != AUCTION_STATUS_CLOSED) {
						a.setAuctionStatus(AUCTION_STATUS_CLOSED);
						auctionRepository.save(a);
					}
				});
			} catch (Exception e) {
				log.error("Failed to close auction {}", auctionId, e);
			}
		}, triggerTime);
	}

	public List<AuctionEntity> getAvailableAuctions() {
		return auctionRepository.findByAuctionStatusOrderByDeadlineAsc(AUCTION_STATUS_OPEN);
	}

	public List<AParticipantEntity> getParticipantsByAuctionId(int auctionId) {
		return aParticipantRepository.findByAuction_AuctionPostId(auctionId);
	}

	public AParticipantEntity getCurrentMaxParticipant(int auctionId) {
		return aParticipantRepository.findTopByAuction_AuctionPostIdOrderByParticipatePriceDesc(auctionId)
				.orElse(null);
	}

	public void delete(int auctionId) {
		auctionRepository.deleteById(auctionId);
	}
}
