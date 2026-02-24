package com.marketTrio.auction.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.marketTrio.auction.domain.AuctionEntity;
import com.marketTrio.auction.service.AuctionService;

@Controller
@SessionAttributes({"auction", "memberSession"})
public class AuctionModifyController {

	private AuctionService auctionService;

	@Autowired
	public void setAuction(AuctionService auction) {
		this.auctionService = auction;
	}

	@ModelAttribute
	public AuctionEntity formBacking(HttpServletRequest request) {
		return new AuctionEntity();
	}

	@RequestMapping("/delete/{auctionId}")
	public String deleteAuction(@PathVariable("auctionId") int auctionId, Model model) {
		auctionService.delete(auctionId);
		return "redirect:/auction/list";
	}

	@GetMapping("/auction/{auctionId}/form")
	public String updateAuction(@PathVariable("auctionId") int auctionId, Model model) {
		AuctionEntity auction = auctionService.getAuction(auctionId);
		model.addAttribute("auction", auction);
		return "thyme/auction/auctionModify";
	}

	@PostMapping("/auction/{auctionId}/form")
	public String saveUpdated(
			@ModelAttribute("auction") AuctionEntity auction,
			AUpdateCommand aupdateInfo,
			@PathVariable("auctionId") int auctionId,
			Model model,
			@RequestParam("files") MultipartFile[] files) throws IllegalStateException, IOException {

		auction.setName(aupdateInfo.getName());
		auction.setDetailInfo(aupdateInfo.getDetailInfo());

		String fileDir = "C:/absolute/path/to/upload/";
		List<String> uploadedFileNames = auction.getPictures() != null ? new ArrayList<>(auction.getPictures()) : new ArrayList<>();

		if (files != null && files.length > 0) {
			File directory = new File(fileDir);
			if (!directory.exists()) directory.mkdirs();
			for (MultipartFile file : files) {
				String name = file.getOriginalFilename();
				if (name != null && !name.isEmpty()) {
					file.transferTo(new File(fileDir + name));
					uploadedFileNames.add(name);
				}
			}
		}

		auction.setPictures(uploadedFileNames);
		AuctionEntity updatedAuction = auctionService.updateAPost(auction);
		model.addAttribute("auction", updatedAuction);
		return "redirect:/auction/" + auctionId + "/detail";
	}
}
