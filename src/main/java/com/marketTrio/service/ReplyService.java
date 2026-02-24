package com.marketTrio.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketTrio.controller.ReplyCommand;
import com.marketTrio.dao.mybatis.MyBatisMemberDao;
import com.marketTrio.domain.Member;
import com.marketTrio.domain.ReplyEntity;
import com.marketTrio.domain.SecondHandEntity;
import com.marketTrio.repository.ReplyRepository;
import java.util.NoSuchElementException;

@Service
public class ReplyService {

	@Autowired
	private ReplyRepository replyRepository;
    @Autowired
    private MyBatisMemberDao memberDao;

	
	//중고거래 게시물 댓글 list 가져오기 => 게시물 상세보기 창에서 댓글 목록 보여주기 위해 사용
	public List<ReplyEntity> getAllReply(int SHPostId) {
		// 그냥 모든 리스트를 보는거니까 review의 여부는 받을 필요 없어서 SecondHandEntity꺼를 가져온건데 이게 맞을까요..?
		//findAll은 jpaRepository에서 기본으로 제공되는 메소드!!
		List<ReplyEntity> replies =  replyRepository.findBySHEntity_SHPostIdOrderByCreateDateAsc(SHPostId);

		return replies;
	}

	public List<ReplyEntity> getReplyBySHPostId(int shPostId) {
		
		List<ReplyEntity> reEntity = replyRepository.getBySHEntity_SHPostId(shPostId);
		
		return reEntity;
	}
	

	//댓글 작성
	public ReplyEntity insertReply(ReplyCommand reCommand, int SHPostId, String memberId, SecondHandEntity shEntity) {
		ReplyEntity reEntity = new ReplyEntity();
		Date date = new Date();
		
		Member member = memberDao.getMember(memberId);
		reEntity.setMember(member);
		
		reEntity.setCreateDate(date);
		reEntity.setContent(reCommand.getContent());
		reEntity.setSHEntity(shEntity);
		
		return replyRepository.save(reEntity);
	}
	
	//댓글 수정
	public void updateReply(ReplyCommand reCommand) {
		ReplyEntity reEntity = new ReplyEntity();

		reEntity.setReplyId(reCommand.getReplyId());
		reEntity.getMember().setId(reCommand.getMemberId());
		reEntity.setCreateDate(reCommand.getCreateDate());
		reEntity.setContent(reCommand.getContent());

		replyRepository.save(reEntity);
	}


	//댓글 삭제
	public void deleteReply(ReplyEntity reply) {
		replyRepository.delete(reply);
	}
	
	
	// 중고 거래 선택 대상 List 가져오기 => 댓글 작성한 사람들의 memberId로 List 가져오기!
	public List<Member> showBuyerMemberList(int SHPostId) {
		return replyRepository.findDistinctUserBySHPostId(SHPostId);
	}
	
	
	// 중고 거래 게시물 댓글 => createDate 순서로 List로 받아와서 주르륵 출력할거임
	public void showReply(int SHPostId) {
		List<ReplyEntity> replyList;
		
		replyList = replyRepository.findBySHEntity_SHPostIdOrderByCreateDateAsc(SHPostId);
	}
	


	public ReplyEntity findReply(int replyId) {
	    return replyRepository.findById(replyId)
	        .orElseThrow(() -> new NoSuchElementException("Reply not found with id " + replyId));
	}
	
}
