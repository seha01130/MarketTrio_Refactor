package com.marketTrio.secondhand.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketTrio.member.dao.MyBatisMemberDao;
import com.marketTrio.member.domain.Member;
import com.marketTrio.reply.domain.ReplyEntity;
import com.marketTrio.reply.repository.ReplyRepository;
import com.marketTrio.review.service.ReviewService;
import com.marketTrio.secondhand.controller.SHListCommand;
import com.marketTrio.secondhand.controller.SHMyListCommand;
import com.marketTrio.secondhand.domain.SecondHandEntity;
import com.marketTrio.secondhand.repository.SHListRepository;

@Service
public class SHService {

	@Autowired
	private SHListRepository shListRepository;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReplyRepository replyRepository;
	@Autowired
	private MyBatisMemberDao memberDao;

	public List<SHMyListCommand> getSHPurchaseListByMemberId(String memberId) {
		List<SecondHandEntity> purchaseList = shListRepository.findByBuyerId(memberId);
		List<SHMyListCommand> result = new ArrayList<>();
		for (SecondHandEntity s : purchaseList) {
			SHMyListCommand c = new SHMyListCommand();
			c.setSHPostId(s.getSHPostId());
			c.setImage(s.getImage());
			c.setTitle(s.getTitle());
			c.setCreateDate(s.getCreateDate());
			c.setPrice(s.getPrice());
			c.setShStatus(s.getSHStatus());
			c.setBuyerId(s.getBuyerId());
			c.setSellerId(s.getMember().getId());
			c.setReviewStatus(reviewService.hasReviewed(memberId, s.getSHPostId()) ? 1 : 0);
			result.add(c);
		}
		return result;
	}

	public List<SHMyListCommand> getSHSalesListByMemberId(String memberId) {
		List<SecondHandEntity> salesList = shListRepository.findByMember_Id(memberId);
		List<SHMyListCommand> result = new ArrayList<>();
		for (SecondHandEntity s : salesList) {
			SHMyListCommand c = new SHMyListCommand();
			c.setSHPostId(s.getSHPostId());
			c.setImage(s.getImage());
			c.setTitle(s.getTitle());
			c.setCreateDate(s.getCreateDate());
			c.setPrice(s.getPrice());
			c.setShStatus(s.getSHStatus());
			c.setBuyerId(s.getBuyerId());
			c.setSellerId(s.getMember().getId());
			c.setReviewStatus(reviewService.hasReviewed(memberId, s.getSHPostId()) ? 1 : 0);
			result.add(c);
		}
		return result;
	}

	public SecondHandEntity getSHPostByPostId(int SHPostId) {
		return shListRepository.findById(SHPostId).orElseThrow();
	}

	public List<SecondHandEntity> getAllSHList() {
		return shListRepository.findAllByOrderByCreateDateDesc();
	}

	public SecondHandEntity insertSHPost(SHListCommand shCommand, String memberId, List<String> uploadedFileNames) {
		SecondHandEntity shEntity = new SecondHandEntity();
		shEntity.setBuyerId(null);
		shEntity.setImage(uploadedFileNames);
		shEntity.setTitle(shCommand.getTitle());
		shEntity.setPrice(shCommand.getPrice());
		shEntity.setContent(shCommand.getContent());
		shEntity.setCreateDate(new Date());
		Member member = memberDao.getMember(memberId);
		shEntity.setMember(member);
		shEntity.setSHStatus(0);
		return shListRepository.save(shEntity);
	}

	public SecondHandEntity updateSHPost(SHListCommand shCommand, int SHPostId, List<String> uploadedFileNames) {
		SecondHandEntity shPost = shListRepository.findById(SHPostId).orElseThrow(() -> new IllegalArgumentException("Post does not exist!!!"));
		shPost.setImage(uploadedFileNames);
		shPost.setTitle(shCommand.getTitle());
		shPost.setPrice(shCommand.getPrice());
		shPost.setContent(shCommand.getContent());
		return shListRepository.save(shPost);
	}

	public void deleteSHPost(SecondHandEntity shPost, List<ReplyEntity> reEntity) {
		replyRepository.deleteAll(reEntity);
		shListRepository.delete(shPost);
	}

	@Transactional
	public void selectBuyer(String buyerId, int SHPostId) {
		shListRepository.updateBuyer(buyerId, SHPostId);
	}
}
