package com.marketTrio.auction.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.marketTrio.auction.domain.AParticipantEntity;
import com.marketTrio.auction.domain.AuctionEntity;
import com.marketTrio.auction.service.AuctionService;

@Controller
@SessionAttributes("memberSession")
public class ViewAuctionListController {

	private AuctionService auctionService;

	@Autowired
	public void setAuction(AuctionService auction) {
		this.auctionService = auction;
	}

	@RequestMapping("/auction/list")
	public String viewList(Model model,
			@RequestParam(name = "showOnlyAvailable", required = false, defaultValue = "false") boolean showOnlyAvailable) {
		List<AuctionEntity> auctionList;

		if (showOnlyAvailable) {
			List<AuctionEntity> auctionListForAvail = auctionService.getAvailableAuctions();
			model.addAttribute("auctionList", auctionListForAvail);

			List<Integer> listOfCurrentMaxPrice = new ArrayList<>();
			for (AuctionEntity auction : auctionListForAvail) {
				if (auction.getParticipants() == null || auction.getParticipants().isEmpty()) {
					listOfCurrentMaxPrice.add(auction.getStartPrice());
				} else {
					List<AParticipantEntity> participants = new ArrayList<>(auction.getParticipants());
					participants.sort(Comparator.comparingInt(AParticipantEntity::getParticipatePrice));
					listOfCurrentMaxPrice.add(participants.get(participants.size() - 1).getParticipatePrice());
				}
			}
			model.addAttribute("listOfCurrentMaxPrice", listOfCurrentMaxPrice);
			model.addAttribute("showOnlyAvailable", showOnlyAvailable);
			return "thyme/auction/auctionList";
		} else {
			List<AuctionEntity> auctionListForAvail = auctionService.getAvailableAuctions();
			auctionList = auctionService.getNotAvailableAuctionList();
			auctionListForAvail.addAll(auctionList);
			model.addAttribute("auctionList", auctionListForAvail);

			List<Integer> listOfCurrentMaxPrice = new ArrayList<>();
			for (AuctionEntity auction : auctionListForAvail) {
				if (auction.getParticipants() == null || auction.getParticipants().isEmpty()) {
					listOfCurrentMaxPrice.add(auction.getStartPrice());
				} else {
					List<AParticipantEntity> participants = new ArrayList<>(auction.getParticipants());
					participants.sort(Comparator.comparingInt(AParticipantEntity::getParticipatePrice));
					listOfCurrentMaxPrice.add(participants.get(participants.size() - 1).getParticipatePrice());
				}
			}
			model.addAttribute("listOfCurrentMaxPrice", listOfCurrentMaxPrice);
			model.addAttribute("showOnlyAvailable", showOnlyAvailable);
			return "thyme/auction/auctionList";
		}
	}
}
