package com.marketTrio.auction.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import com.marketTrio.auction.domain.AParticipantEntity;
import com.marketTrio.auction.domain.AuctionEntity;
import com.marketTrio.auction.domain.AuctionForm;
import com.marketTrio.auction.service.AuctionService;
import com.marketTrio.member.controller.MemberSession;

@Controller
@SessionAttributes("memberSession")
public class AuctionController {

	private AuctionService auctionService;

	@Autowired
	public void setAuction(AuctionService auction) {
		this.auctionService = auction;
	}

	@GetMapping("/auction/create")
	public String showCreateForm(Model model) {
		model.addAttribute("auctionForm", new AuctionForm());
		return "thyme/auction/createAuction";
	}

	@PostMapping("/auction/create")
	public String register(AuctionForm postData, Model model, HttpServletRequest request,
			@RequestParam("files") MultipartFile[] files,
			@ModelAttribute("memberSession") MemberSession memberSession) throws IOException, ServletException {
		String memberId = memberSession.getMemberId();
		String fileDir = "C:/absolute/path/to/upload/";

		boolean hasError = false;
		if (postData.getName().isEmpty()) {
			model.addAttribute("NoTitleMsg", "제목을 입력하세요");
			hasError = true;
		}
		if (postData.getStartPrice() == 0) {
			model.addAttribute("NoPriceMsg", "가격을 입력하세요");
			hasError = true;
		}
		if (postData.getDetailInfo().isEmpty()) {
			model.addAttribute("NoDetailMsg", "상세설명을 입력해주세요");
			hasError = true;
		}
		if (postData.getDeadline() == null) {
			model.addAttribute("NoDeadMsg", "마감 날짜를 입력해주세요");
			hasError = true;
		}
		if (hasError) {
			model.addAttribute("auctionForm", postData);
			return "thyme/auction/createAuction";
		}

		File directory = new File(fileDir);
		if (!directory.exists()) directory.mkdirs();

		List<String> uploadedFileNames = new ArrayList<>();
		if (files != null && files.length > 0) {
			for (MultipartFile file : files) {
				String name = file.getOriginalFilename();
				if (name != null && !name.isEmpty()) {
					file.transferTo(new File(fileDir + name));
					uploadedFileNames.add(name);
				}
			}
		}

		AuctionEntity postAuction = auctionService.createAuction(postData, memberId, uploadedFileNames);
		model.addAttribute("postAuction", postAuction);
		model.addAttribute("isSeller", memberId.equals(postAuction.getMember().getId()));
		auctionService.scheduleAuctionClose(postAuction);
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
