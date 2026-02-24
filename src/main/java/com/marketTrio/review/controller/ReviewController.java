package com.marketTrio.review.controller;

import java.text.DecimalFormat;

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

import com.marketTrio.member.controller.MemberSession;
import com.marketTrio.member.service.MyInfoService;
import com.marketTrio.review.domain.ReviewEntity;
import com.marketTrio.review.service.ReviewService;

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
		ReviewEntity rvEntity = new ReviewEntity(SHPostId, senderId, receiverId, rating);
		reviewService.insertReview(rvEntity);

		float originalRate = myInfoService.getRate(receiverId);
		int rateCount = reviewService.rateCount(receiverId);
		float newRate = (originalRate * (rateCount - 1) + rating) / rateCount;
		DecimalFormat df = new DecimalFormat("#.#");
		newRate = Float.parseFloat(df.format(newRate));
		myInfoService.updateRate(receiverId, newRate);

		String memberId = memberSession.getMemberId();
		String postWriterId = myInfoService.getSellerIdFromSH(SHPostId);
		if (memberId.equals(postWriterId)) {
			return "redirect:/sales/mySalesList";
		} else {
			return "redirect:/purchase/myPurchaseList";
		}
	}
}
