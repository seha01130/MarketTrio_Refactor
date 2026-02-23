package com.marketTrio.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.marketTrio.domain.AParticipantEntity;
import com.marketTrio.domain.AuctionEntity;
import com.marketTrio.service.AuctionService;


@Controller
@SessionAttributes("memberSession")
//@SessionAttributes("auctionList")
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
                if (auction.getParticipants().isEmpty()) { // 참여자가 없는 경우
                    listOfCurrentMaxPrice.add(auction.getStartPrice());
                } else { // 참여자가 있는 경우
                    List<AParticipantEntity> participants = auction.getParticipants();
                    participants.sort(Comparator.comparingInt(AParticipantEntity::getParticipatePrice));
                    listOfCurrentMaxPrice.add(participants.getLast().getParticipatePrice());
                }
                
                model.addAttribute("listOfCurrentMaxPrice", listOfCurrentMaxPrice);
            }
            model.addAttribute("showOnlyAvailable", showOnlyAvailable);
            
            return "thyme/auction/auctionList";
        } else {
           List<AuctionEntity> auctionListForAvail = auctionService.getAvailableAuctions();
            auctionList = auctionService.getNotAvailableAuctionList();
            auctionListForAvail.addAll(auctionList);
            model.addAttribute("auctionList", auctionListForAvail);
            
            List<Integer> listOfCurrentMaxPrice = new ArrayList<>();
            for (AuctionEntity auction : auctionListForAvail) {
                if (auction.getParticipants().isEmpty()) { // 참여자가 없는 경우
                    listOfCurrentMaxPrice.add(auction.getStartPrice());
                } else { // 참여자가 있는 경우
                    List<AParticipantEntity> participants = auction.getParticipants();
                    participants.sort(Comparator.comparingInt(AParticipantEntity::getParticipatePrice));
                    listOfCurrentMaxPrice.add(participants.getLast().getParticipatePrice());
                }
            }
            
            model.addAttribute("listOfCurrentMaxPrice", listOfCurrentMaxPrice);
            
            model.addAttribute("showOnlyAvailable", showOnlyAvailable);
            
            return "thyme/auction/auctionList";   
        }
       
//        PagedListHolder<AuctionEntity> auctionListPage = new PagedListHolder<AuctionEntity>(auctionList);
//        auctionListPage.setPageSize(4); //페이지당 4개의 auction 설정       
        
    }

    

//    @RequestMapping("/auction/list2")
//    public String viewList2(
//    		@RequestParam("pageName") String page,
//    		@RequestParam(name = "showOnlyAvailable", required = false, defaultValue = "false") boolean showOnlyAvailable,
//    		BindingResult result,
//    		Model model) {
//    	
//    	List<AuctionEntity> auctionList;
//
//        if (showOnlyAvailable) {
//            auctionList = auctionService.getAvailableAuctions();
//        } else {
//            auctionList = auctionService.getAuctionList();
//        }
//    	
//        PagedListHolder<AuctionEntity> auctionListPage = new PagedListHolder<>(auctionList);
//        auctionListPage.setPageSize(4);
//
//        if ("next".equals(page)) {
//            auctionListPage.nextPage();
//        } else if ("previous".equals(page)) {
//            auctionListPage.previousPage();
//        }
//
//        model.addAttribute("auctionList", auctionListPage);
//        model.addAttribute("showOnlyAvailable", showOnlyAvailable);
//
//        
//        List<Integer> listOfCurrentMaxPrice = new ArrayList<>();
//        for (AuctionEntity auction : auctionListPage.getPageList()) {
//            if (auction.getParticipants().isEmpty()) { // 참여자가 없는 경우
//                listOfCurrentMaxPrice.add(auction.getStartPrice());
//            } else { // 참여자가 있는 경우
//                List<AParticipantEntity> participants = auction.getParticipants();
//                participants.sort(Comparator.comparingInt(AParticipantEntity::getParticipatePrice));
//                listOfCurrentMaxPrice.add(participants.get(participants.size() - 1).getParticipatePrice());
//            }
//        }
//        model.addAttribute("listOfCurrentMaxPrice", listOfCurrentMaxPrice);
//
//        
//       return "thyme/auctionList";
//    }
    

}
