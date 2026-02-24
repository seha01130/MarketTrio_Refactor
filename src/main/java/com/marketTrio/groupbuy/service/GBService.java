package com.marketTrio.groupbuy.service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketTrio.common.dao.JPAAuctionAndGroupBuyListDao;
import com.marketTrio.groupbuy.controller.GBInfoCommand;
import com.marketTrio.groupbuy.controller.GBParticipateCommand;
import com.marketTrio.groupbuy.controller.GBUpdateInfoCommand;
import com.marketTrio.groupbuy.domain.GBEntity;
import com.marketTrio.groupbuy.domain.GBParticipantEntity;
import com.marketTrio.groupbuy.repository.GBListRepository;
import com.marketTrio.groupbuy.repository.GroupBuyPartRepository;
import com.marketTrio.groupbuy.repository.GroupBuyRepository;
import com.marketTrio.member.dao.MyBatisMemberDao;
import com.marketTrio.member.domain.Member;
import com.marketTrio.option.domain.OptionEntity;
import com.marketTrio.option.repository.OptionRepository;

@Service
public class GBService {

	@Autowired
	private GBListRepository gbListRepository;
	@Autowired
	private JPAAuctionAndGroupBuyListDao jpaListDao;
	@Autowired
	private GroupBuyRepository gbRepository;
	@Autowired
	private GroupBuyPartRepository gbPartRepository;
	@Autowired
	private OptionRepository optionRepository;
	@Autowired
	private MyBatisMemberDao memberDao;

	public List<GBEntity> getGBPurchaseListByMemberId(String memberId) {
		List<Integer> purchaseListIds = jpaListDao.getGBPostIdList(memberId);
		return gbListRepository.findByGBPostIdIn(purchaseListIds);
	}

	public List<GBEntity> getGBSalesListByMemberId(String memberId) {
		return gbListRepository.findByMember_Id(memberId);
	}

	public List<GBEntity> getGBPostList() {
		return gbRepository.findAll();
	}

	public GBEntity getGBPost(int gbPostId) {
		return gbRepository.findById(gbPostId).orElseThrow();
	}

	@Transactional
	public GBEntity createGBPost(GBInfoCommand gbInfo, String memberId, List<String> uploadedFileNames) throws IllegalStateException, IOException {
		GBEntity gbEntity = new GBEntity();
		Member member = memberDao.getMember(memberId);
		gbEntity.setProductName(gbInfo.getProductName());
		gbEntity.setAllQuantity(gbInfo.getAllQuantity());
		gbEntity.setRegularPrice(gbInfo.getRegularPrice());
		gbEntity.setDiscountRate(gbInfo.getDiscountRate());
		gbEntity.setContent(gbInfo.getContent());
		gbEntity.setDuration(gbInfo.getDuration());
		gbEntity.setCreateDate(gbInfo.getCreateDate());
		gbEntity.setMember(member);
		gbEntity.setPictures(uploadedFileNames);
		return gbRepository.save(gbEntity);
	}

	@Transactional
	public void deleteGBPost(GBEntity gbPost, List<OptionEntity> optList, List<GBParticipantEntity> gbpList) {
		gbPartRepository.deleteAll(gbpList);
		optionRepository.deleteAll(optList);
		gbRepository.delete(gbPost);
	}

	@Transactional
	public GBEntity getUpdateGBPost(int gbPostId) {
		return gbRepository.findById(gbPostId).orElse(null);
	}

	@Transactional
	public GBEntity updateGBPost(GBUpdateInfoCommand gbUpdateInfo, List<String> uploadedFileNames) throws IllegalStateException, IOException {
		Optional<GBEntity> optionalPost = gbRepository.findById(gbUpdateInfo.getId());
		if (optionalPost.isPresent()) {
			GBEntity gbPost = optionalPost.get();
			gbPost.setProductName(gbUpdateInfo.getProductName());
			gbPost.setDuration(gbUpdateInfo.getDuration());
			gbPost.setContent(gbUpdateInfo.getContent());
			gbPost.setPictures(uploadedFileNames);
			calculateDday(gbPost);
			return gbRepository.save(gbPost);
		}
		return null;
	}

	public OptionEntity createOption(OptionEntity option) {
		OptionEntity optEntity = new OptionEntity();
		optEntity.setOptionName(option.getOptionName());
		optEntity.setQuantity(option.getQuantity());
		optEntity.setGBEntity(option.getGBEntity());
		optEntity.setRemainingQuantity(option.getQuantity());
		return optionRepository.save(optEntity);
	}

	public List<OptionEntity> findOptionList(int gbPostId) {
		GBEntity gbEntity = gbRepository.findById(gbPostId).orElseThrow();
		return optionRepository.findByGbEntity(gbEntity);
	}

	public GBParticipantEntity getGBPart(int gbPostid, String memberId) {
		return gbPartRepository.findByGbEntity_GBPostIdAndMember_Id(gbPostid, memberId);
	}

	public List<GBParticipantEntity> findGBPaticipantsList(int gbPostId) {
		GBEntity gbEntity = gbRepository.findById(gbPostId).orElseThrow();
		return gbPartRepository.findByGbEntity(gbEntity);
	}

	@Transactional
	public void participate(GBParticipateCommand gbPartCommand) {
		OptionEntity o = optionRepository.findById(gbPartCommand.getOptionId()).orElseThrow();
		if (o.getRemainingQuantity() < gbPartCommand.getQuantity()) {
			throw new IllegalArgumentException("잔여 수량보다 선택한 수량이 더 많아 선택할 수 없습니다.");
		}
		GBEntity gbPost = gbRepository.findById(gbPartCommand.getGBPostId()).orElseThrow();
		Member member = memberDao.getMember(gbPartCommand.getMember());

		GBParticipantEntity gbp = new GBParticipantEntity();
		gbp.setGbPost(gbPost);
		gbp.setMember(member);
		gbp.setMyOption(o);
		gbp.setMyQuantity(gbPartCommand.getQuantity());
		gbPartRepository.save(gbp);

		o.setRemainingQuantity(o.getRemainingQuantity() - gbPartCommand.getQuantity());
		optionRepository.save(o);
	}

	@Transactional
	public void participateCancel(int gbPostId, String memberId) {
		GBParticipantEntity gbPart = gbPartRepository.findByGbEntity_GBPostIdAndMember_Id(gbPostId, memberId);
		OptionEntity option = gbPart.getMyOption();
		option.setRemainingQuantity(option.getRemainingQuantity() + gbPart.getMyQuantity());
		optionRepository.save(option);
		gbPart.setMyOption(null);
		gbPartRepository.delete(gbPart);
	}

	public String calculateDday(GBEntity gbPost) {
		List<OptionEntity> optionList = optionRepository.findByGbEntity(gbPost);
		int optStatus = 1;
		for (OptionEntity o : optionList) {
			if (o.getRemainingQuantity() != 0) {
				optStatus = 0;
				break;
			}
		}
		if (optStatus == 1) return "마감";

		int status = gbPost.getGBStatus();
		Instant now = Instant.now();
		Date durationDate = gbPost.getDuration();
		Instant deadline = durationDate.toInstant();
		long daysRemaining = ChronoUnit.DAYS.between(now, deadline);

		if (status == 0) {
			if (daysRemaining < 0) {
				gbPost.setGBStatus(1);
				gbRepository.save(gbPost);
			}
			if (optStatus == 1) {
				gbPost.setGBStatus(1);
				gbRepository.save(gbPost);
			}
		} else {
			if (daysRemaining > 0) {
				gbPost.setGBStatus(0);
				gbRepository.save(gbPost);
			}
		}

		if (gbPost.getGBStatus() == 1) return "마감";
		return String.format("D-%d", daysRemaining);
	}
}
