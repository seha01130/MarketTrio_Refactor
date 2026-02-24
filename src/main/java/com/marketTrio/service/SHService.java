package com.marketTrio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marketTrio.controller.SHListCommand;
import com.marketTrio.controller.SHMyListCommand;
import com.marketTrio.dao.mybatis.MyBatisMemberDao;
import com.marketTrio.domain.Member;
import com.marketTrio.domain.ReplyEntity;
import com.marketTrio.domain.SecondHandEntity;
import com.marketTrio.repository.ReplyRepository;
import com.marketTrio.repository.SHListRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SHService {
    @Autowired
    private SHListRepository SHListRepository;
    @Autowired
    private ReviewService reviewService; // 주입
    @Autowired
	private ReplyRepository replyRepository;
    @Autowired
    private MyBatisMemberDao memberDao;

    //@Transactional(readOnly = true)
    @SuppressWarnings("null")
	public List<SHMyListCommand> getSHPurchaseListByMemberId(String memberId) {
    	List<SecondHandEntity> purchaseList = SHListRepository.findByBuyerId(memberId);
    	List<SHMyListCommand> purchaseListCommand = new ArrayList<>();
    	
    	for (SecondHandEntity s : purchaseList) {
    		SHMyListCommand c = new SHMyListCommand();
    		
    		c.setSHPostId(s.getSHPostId());
    		c.setImage(s.getImage());
    		c.setTitle(s.getTitle());
    		c.setCreateDate(s.getCreateDate());
    		c.setPrice(s.getPrice());
    		c.setShStatus(s.getSHStatus());
    		c.setBuyerId(s.getBuyerId());
    		c.setSellerId(s.getMember().getId());//seller의 Id 구하기. SecondHandEntity에 sellerId는 member의 fk이기떄문에 OneToOne으로 매핑되어있음. 
    		c.setReviewStatus(reviewService.hasReviewed(memberId, s.getSHPostId()) ? 1 : 0);
    		purchaseListCommand.add(c);
    	}
        return purchaseListCommand;
    }

    //@Transactional(readOnly = true)
    @SuppressWarnings("null")
	public List<SHMyListCommand> getSHSalesListByMemberId(String memberId) {
    	List<SecondHandEntity> salesList =  SHListRepository.findByMember_Id(memberId);
    	List<SHMyListCommand> salesListCommand = new ArrayList<>();
    	
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
    		salesListCommand.add(c);
    	}
        return salesListCommand;
    }
    
/////////////////////////////////////////////////////////////////////////
    
 // 게시물 수정을 위해 게시물을 가져오는데 쓰임
    public SecondHandEntity getSHPostByPostId(int SHPostId) {
    	//postId로 SecondHand 객체 가져와서 객체를 리턴. 중고거래 코드 사용하면 됨
		Optional<SecondHandEntity> sh = SHListRepository.findById(SHPostId);
		//    	SecondHandEntity shPost = sh.orElseThrow(() -> new IllegalArgumentException("Post does not exist!!!"));
		SecondHandEntity shPost = sh.get();

		return shPost;
    }
    
    
    
	//중고거래 list 가져오기 => list보기에 사용
	public List<SecondHandEntity> getAllSHList() {
		// 그냥 모든 리스트를 보는거니까 review의 여부는 받을 필요 없어서 SecondHandEntity꺼를 가져온건데 이게 맞을까요..?
		//findAll은 jpaRepository에서 기본으로 제공되는 메소드!!
		List<SecondHandEntity> salesList =  SHListRepository.findAllByOrderByCreateDateDesc();

		return salesList;
	}
	
	
	//중고거래 게시물 작성한거 db에 반영하는거!!
	//memberSession파일 넣기 => memberSession구해와서 현재 sellerId 가져오기?????????
	public SecondHandEntity insertSHPost(SHListCommand shCommand, String memberId, List<String> uploadedFileNames){
		SecondHandEntity shEntity = new SecondHandEntity();
		Date date = new Date();
		
		
//		shEntity.setSHPostId(shCommand.getSHPostId());
		shEntity.setBuyerId(null);
        shEntity.setImage(uploadedFileNames);
		shEntity.setTitle(shCommand.getTitle());
//		shEntity.setCreateDate(shCommand.getCreateDate());
		shEntity.setPrice(shCommand.getPrice());
		shEntity.setContent(shCommand.getContent());
		shEntity.setCreateDate(date);
		
        Member member = memberDao.getMember(memberId);
        shEntity.setMember(member);

		
		return SHListRepository.save(shEntity);
	}
	

	
	
	//중고거래 게시물 수정
	public SecondHandEntity updateSHPost(SHListCommand shCommand, int SHPostId, List<String> uploadedFileNames){
		Optional<SecondHandEntity> shOptionalPost = SHListRepository.findById(SHPostId);
		SecondHandEntity shPost = shOptionalPost.orElseThrow(() -> new IllegalArgumentException("Post does not exist!!!"));

//		SecondHandEntity shPost = new SecondHandEntity();
		
		shPost.setImage(uploadedFileNames);
		shPost.setTitle(shCommand.getTitle());
		shPost.setPrice(shCommand.getPrice());
		shPost.setContent(shCommand.getContent());

		return SHListRepository.save(shPost);
		
	} 
	


	//중고거래 게시물 삭제
	public void deleteSHPost(SecondHandEntity shPost, List<ReplyEntity> reEntity) { 
		
		replyRepository.deleteAll(reEntity);
//		shPost.setMember(null);
		SHListRepository.delete(shPost);
	}


	
	// 중고 거래 buyer 선택하는거
	@Transactional
	public void selectBuyer(String buyerId, int SHPostId) {
		
		SHListRepository.updateBuyer(buyerId, SHPostId);
		
		
	}
    
}
