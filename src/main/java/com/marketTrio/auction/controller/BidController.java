package com.marketTrio.auction.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.marketTrio.auction.domain.AParticipantEntity;
import com.marketTrio.auction.domain.AuctionEntity;
import com.marketTrio.auction.service.AuctionService;
import com.marketTrio.member.controller.MemberSession;

@Controller
@SessionAttributes("memberSession")
public class BidController {

	private AuctionService auctionService;

	@Autowired
	public void setAuction(AuctionService auction) {
		this.auctionService = auction;
	}

	@PostMapping("/auction/{auctionId}/bid")
	public String placeBid(@ModelAttribute("memberSession") MemberSession memberSession,
			@PathVariable("auctionId") int auctionId, @RequestParam int bidAmount, Model model, HttpServletRequest request) {
		String memberId = memberSession.getMemberId();
		boolean isCurrentMaxParticipant = false;

		AuctionEntity auction = auctionService.getAuction(auctionId);
		List<AParticipantEntity> participants = auction.getParticipants();
		model.addAttribute("postAuction", auction);

		boolean isSeller = memberId.equals(auction.getMember().getId());
		model.addAttribute("isSeller", isSeller);

		if (participants != null && participants.size() >= 1) {
			AParticipantEntity currentMaxParticipant = participants.get(participants.size() - 1);
			model.addAttribute("currentMaxParticipant", currentMaxParticipant);

			if (memberId.equals(currentMaxParticipant.getMember().getId())) {
				isCurrentMaxParticipant = true;
				model.addAttribute("isCurrentMaxParticipant", isCurrentMaxParticipant);
			}

			if (bidAmount <= currentMaxParticipant.getParticipatePrice()) {
				model.addAttribute("message", "현재 입찰가보다 낮습니다. 다시 입력해주세요.");
				return "thyme/auction/auctionDetail";
			}
		} else {
			if (bidAmount <= auction.getStartPrice()) {
				model.addAttribute("message", "현재 입찰가보다 낮습니다. 다시 입력해주세요.");
				return "thyme/auction/auctionDetail";
			}
			model.addAttribute("currentMaxParticipant", null);
			model.addAttribute("isCurrentMaxParticipant", false);
		}

		auctionService.updateBidAmount(auctionId, bidAmount, memberId);
		return "redirect:/auction/" + auctionId + "/detail";
	}

	@RequestMapping("/auctions/{auctionId}/bidCancel")
	public String cancelBid(@ModelAttribute("memberSession") MemberSession memberSession,
			@PathVariable("auctionId") int auctionId, HttpServletRequest request) {
		String memberId = memberSession.getMemberId();
		auctionService.cancelBid(auctionId, memberId);
		return "redirect:/auction/" + auctionId + "/detail";
	}
}
