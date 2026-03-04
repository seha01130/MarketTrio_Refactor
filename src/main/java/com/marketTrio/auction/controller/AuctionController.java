package com.marketTrio.auction.controller;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;
import javax.validation.Valid;

import com.marketTrio.auction.domain.AParticipantEntity;
import com.marketTrio.auction.domain.AuctionEntity;
import com.marketTrio.auction.dto.AuctionForm;
import com.marketTrio.auction.service.AuctionService;
import com.marketTrio.common.service.FileStorageService;
import com.marketTrio.member.controller.MemberSession;

@Controller
@SessionAttributes("memberSession")
public class AuctionController {

	private final AuctionService auctionService;
	private final FileStorageService fileStorageService;

	@Autowired
	public AuctionController(AuctionService auctionService, FileStorageService fileStorageService) {
		this.auctionService = auctionService;
		this.fileStorageService = fileStorageService;
	}

	@GetMapping("/auction/create")
	public String showCreateForm(Model model) {
		model.addAttribute("auctionForm", new AuctionForm());
		return "thyme/auction/createAuction";
	}

	@PostMapping("/auction/create")
	public String register(@ModelAttribute("auctionForm") @Valid AuctionForm postData,
			BindingResult bindingResult,
			@RequestParam("files") MultipartFile[] files,
			@ModelAttribute("memberSession") MemberSession memberSession,
			Model model) throws IOException, ServletException {
		String memberId = memberSession.getMemberId();

		if (bindingResult.hasErrors()) {
			return "thyme/auction/createAuction";
		}

		List<String> uploadedFileNames = fileStorageService.storeFiles(files);

		AuctionEntity postAuction = auctionService.createAuction(postData, memberId, uploadedFileNames);
		model.addAttribute("postAuction", postAuction);
		model.addAttribute("isSeller", memberId.equals(postAuction.getMember().getId()));
		auctionService.scheduleAuctionClose(postAuction.getAuctionPostId(), postAuction.getDeadline());
		return "redirect:/auction/" + postAuction.getAuctionPostId() + "/detail";
	}

	@GetMapping("/auction/{auctionId}/detail")
	public String viewAuction(HttpServletRequest request, @PathVariable("auctionId") int auctionId,
			@ModelAttribute("memberSession") MemberSession memberSession, Model model) {
		String memberId = memberSession.getMemberId();
		boolean isCurrentMaxParticipant = false;
		AParticipantEntity currentMaxParticipant = null;

		AuctionEntity auction = auctionService.getAuction(auctionId);
		model.addAttribute("postAuction", auction);

		List<AParticipantEntity> participants = auction.getParticipants();
		if (participants == null || participants.isEmpty()) {
			model.addAttribute("currentMaxParticipant", null);
		} else {
			participants = auctionService.getParticipantsByAuctionId(auctionId);
			Set<String> memberIds = new HashSet<>();
			participants.removeIf(p -> !memberIds.add(p.getMember().getId()));
			participants.sort(Comparator.comparingInt(AParticipantEntity::getParticipatePrice));
			currentMaxParticipant = participants.get(participants.size() - 1);
			if (memberId.equals(currentMaxParticipant.getMember().getId())) isCurrentMaxParticipant = true;
			model.addAttribute("isCurrentMaxParticipant", isCurrentMaxParticipant);
			model.addAttribute("currentMaxParticipant", currentMaxParticipant);
		}

		model.addAttribute("isSeller", memberId.equals(auction.getMember().getId()));
		return "thyme/auction/auctionDetail";
	}
}
