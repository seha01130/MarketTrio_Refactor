package com.marketTrio.reply.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketTrio.member.dao.MyBatisMemberDao;
import com.marketTrio.member.domain.Member;
import com.marketTrio.reply.controller.ReplyCommand;
import com.marketTrio.reply.domain.ReplyEntity;
import com.marketTrio.reply.repository.ReplyRepository;
import com.marketTrio.secondhand.domain.SecondHandEntity;

@Service
public class ReplyService {

	@Autowired
	private ReplyRepository replyRepository;
	@Autowired
	private MyBatisMemberDao memberDao;

	public List<ReplyEntity> getAllReply(int SHPostId) {
		return replyRepository.findBySHEntity_SHPostIdOrderByCreateDateAsc(SHPostId);
	}

	public List<ReplyEntity> getReplyBySHPostId(int shPostId) {
		return replyRepository.getBySHEntity_SHPostId(shPostId);
	}

	public ReplyEntity insertReply(ReplyCommand reCommand, int SHPostId, String memberId, SecondHandEntity shEntity) {
		ReplyEntity reEntity = new ReplyEntity();
		Member member = memberDao.getMember(memberId);
		reEntity.setMember(member);
		reEntity.setCreateDate(new Date());
		reEntity.setContent(reCommand.getContent());
		reEntity.setSHEntity(shEntity);
		return replyRepository.save(reEntity);
	}

	public void updateReply(ReplyCommand reCommand) {
		ReplyEntity reEntity = replyRepository.findById(reCommand.getReplyId())
				.orElseThrow(() -> new NoSuchElementException("Reply not found"));
		reEntity.setContent(reCommand.getContent());
		replyRepository.save(reEntity);
	}

	public void deleteReply(ReplyEntity reply) {
		replyRepository.delete(reply);
	}

	public List<Member> showBuyerMemberList(int SHPostId) {
		return replyRepository.findDistinctUserBySHPostId(SHPostId);
	}

	public ReplyEntity findReply(int replyId) {
		return replyRepository.findById(replyId)
				.orElseThrow(() -> new NoSuchElementException("Reply not found with id " + replyId));
	}
}
