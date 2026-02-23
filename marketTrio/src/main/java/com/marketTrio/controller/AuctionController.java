package com.marketTrio.controller;

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

import com.marketTrio.domain.AParticipantEntity;
import com.marketTrio.domain.AuctionEntity;
import com.marketTrio.domain.AuctionForm;
import com.marketTrio.service.AuctionService;

@Controller
@SessionAttributes("memberSession")
public class AuctionController {

    private AuctionService auctionService;

    @Autowired
    public void setAuction(AuctionService auction) {
        this.auctionService = auction;
    }

    // 경매글 작성 get요청
    @GetMapping("/auction/create")
    public String showCreateForm(Model model) {
        model.addAttribute("auctionForm", new AuctionForm());
        return "thyme/auction/createAuction";
    }

    // 경매글 작성
    @PostMapping("/auction/create")
    public String register(AuctionForm postData,
				    		Model model, HttpServletRequest request,
				    		@RequestParam("files") MultipartFile[] files,
				    		@ModelAttribute("memberSession") MemberSession memberSession) throws IOException, ServletException {
    	
//        String memberId = "dami"; // 예제에서는 고정값, 실제 구현에서는 세션 등에서 가져와야 함
    	String memberId = memberSession.getMemberId();
        String fileDir = "C:/absolute/path/to/upload/";
        
        boolean hasError = false;
        
        if(postData.getName().isEmpty()) {
            model.addAttribute("NoTitleMsg", "제목을 입력하세요");
            hasError = true;
        }
        
        if(postData.getStartPrice()==0) {
            model.addAttribute("NoPriceMsg", "가격을 입력하세요");
            hasError = true;
        }
        
        if(postData.getDetailInfo().isEmpty()) {
            model.addAttribute("NoDetailMsg", "상세설명을 입력해주세요");
            hasError = true;
        }
        
        if(postData.getDeadline() == null) {
            model.addAttribute("NoDeadMsg", "마감 날짜를 입력해주세요");
            hasError = true;
        }
        if (hasError) {
        	model.addAttribute("auctionForm", postData);
            return "thyme/auction/createAuction";
        }

        // 디렉토리가 존재하지 않으면 생성
        File directory = new File(fileDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 여러 파일 업로드 처리
        List<String> uploadedFileNames = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                String uploadedFileName = file.getOriginalFilename();
                System.out.println("Processing file: " + uploadedFileName); // 추가된 디버그 메시지
                
                if (uploadedFileName != null && !uploadedFileName.isEmpty()) {
                    file.transferTo(new File(fileDir + uploadedFileName));
                    uploadedFileNames.add(uploadedFileName);
                }
            }
        }

        System.out.println("Uploaded File Names in Controller: " + uploadedFileNames);
        System.out.println("postData 확인: " + postData.getName() + postData.getStartPrice());

        AuctionEntity postAuction = auctionService.createAuction(postData, memberId, uploadedFileNames);
        model.addAttribute("postAuction", postAuction);

        boolean isSeller = memberId.equals(postAuction.getMember().getId());
        model.addAttribute("isSeller", isSeller);

        auctionService.scheduleAuctionClose(postAuction);
        
        return "redirect:/auction/" + postAuction.getAuctionPostId() + "/detail";
    }

    // 디테일 페이지
    @GetMapping("/auction/{auctionId}/detail")
    public String viewAuction(HttpServletRequest request, @PathVariable("auctionId") int auctionId, 
    							@ModelAttribute("memberSession") MemberSession memberSession, Model model) {
//        String memberId = "dami";
    	String memberId = memberSession.getMemberId();
    	
    	System.out.println("세션에서 멤버 아이디: " + memberId);
    	
        boolean isCurrentMaxParticipant = false;
        AParticipantEntity currentMaxParticipant = null;

        AuctionEntity auction = auctionService.getAuction(auctionId);
        model.addAttribute("postAuction", auction);

        //참여자가 없을 경우
        if (auction.getParticipants().size() == 0) {
            model.addAttribute("currentMaxParticipant", null);
        } else { //참여자가 있을 경우
        	List<AParticipantEntity> participants = auctionService.getParticipantsByAuctionId(auctionId);
        	System.out.println(participants.getFirst().getaPartId());

        	 // 중복 제거를 위해 Set을 사용
            Set<String> memberIds = new HashSet<>();
            participants.removeIf(participant -> !memberIds.add(participant.getMember().getId()));

        	participants.sort(Comparator.comparingInt(AParticipantEntity::getParticipatePrice));
        	currentMaxParticipant = participants.getLast();
        	
        	System.out.println(currentMaxParticipant.getMember().getNickname() + "ㅇㄹㄴㅇㄹㄴㅇㄹㄴㅇㄹ");
        	
        	if (memberId.equals(currentMaxParticipant.getMember().getId())){
            	isCurrentMaxParticipant = true; //사용자가 최대입찰가인지
            }
        	
            model.addAttribute("isCurrentMaxParticipant", isCurrentMaxParticipant);
            model.addAttribute("currentMaxParticipant", currentMaxParticipant);            
        	}
        
        System.out.println("@!!!!!!!!!!!!" + auction.getMember().getId());
        System.out.println("Auction Latitude: " + auction.getLatitude());
        System.out.println("Auction Longitude: " + auction.getLongitude());
        //판매자인지 구매자인지
        boolean isSeller = memberId.equals(auction.getMember().getId());
        model.addAttribute("isSeller", isSeller);

        return "thyme/auction/auctionDetail";
    }
}
