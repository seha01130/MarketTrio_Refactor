package com.marketTrio.controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.marketTrio.domain.ReviewEntity;
import com.marketTrio.service.MyInfoService;
import com.marketTrio.service.ReviewService;

@Controller
@RequestMapping("/review")
@SessionAttributes({"memberCommand", "memberSession"})
public class ReviewController {
	@Value("thyme/myPage/review")
	private String review;
	
	@Autowired
    private MyInfoService myInfoService;
	@Autowired
	private ReviewService reviewService;

	@GetMapping("/giveRate")
	public String giveRate(@RequestParam("SHPostId") int SHPostId,
							@RequestParam("receiverId") String receiverId,
							@RequestParam("senderId") String senderId,
	                       Model model) throws Exception {
	    // itemId, sellerId, buyerId를 review.html로 전달할 수 있음
		System.out.println("SHPostId: " + SHPostId);
		System.out.println("receiverId: " + receiverId);
		System.out.println("senderId: " + senderId);
		
		String receiverNickname = myInfoService.getNickname(receiverId);
		String senderNickname = myInfoService.getNickname(senderId);
		
		model.addAttribute("SHPostId", SHPostId);
	    model.addAttribute("receiverId", receiverId);
	    model.addAttribute("receiverNickname", receiverNickname);
	    model.addAttribute("senderId", senderId);
	    model.addAttribute("senderNickname", senderNickname);

	    return "thyme/myPage/review";
	}
	
	@PostMapping("/submitRating")
    public String submitRating(@ModelAttribute("memberSession") MemberSession memberSession,
    						   @RequestParam("receiverId") String receiverId,
    						   @RequestParam("senderId") String senderId,
                               @RequestParam("SHPostId") int SHPostId,
                               @RequestParam("rating") int rating) throws Exception {
        
		//review DB에 insert가 먼저 되어야 함. 
        ReviewEntity rvEntity = new ReviewEntity(SHPostId, senderId, receiverId, rating);
        reviewService.insertReview(rvEntity);
        
        //(원래의 별점에 갯수를 곱해주고 + 받은 별점) / 총 별점 갯수로 새로운 별점을 업데이트한다
        float originalRate = myInfoService.getRate(receiverId);
        int rateCount = reviewService.rateCount(receiverId);
        float newRate = (originalRate*(rateCount-1) + rating) / rateCount;
        
        //DecimalFormat을 사용하여 첫 번째 자릿수까지 반올림
        DecimalFormat df = new DecimalFormat("#.#");
        newRate = Float.parseFloat(df.format(newRate));
        
        //Member DB에 별점 update
        myInfoService.updateRate(receiverId, newRate);							
        
        String memberId = memberSession.getMemberId();
        //현재 포스트의 판매자Id 가져오기
        String postWriterId = myInfoService.getSellerIdFromSH(SHPostId);
        System.out.println("작성자 Id를 출력: " + postWriterId);
        if (memberId.equals(postWriterId)) {
        	return "redirect:/sales/mySalesList";
        } else {
        	return "redirect:/purchase/myPurchaseList";
        }
    }
}
