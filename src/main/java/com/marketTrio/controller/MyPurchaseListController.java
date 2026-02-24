package com.marketTrio.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.marketTrio.dao.mybatis.MyBatisMemberDao;
import com.marketTrio.domain.Member;
import com.marketTrio.domain.ReviewEntity;
import com.marketTrio.domain.SecondHandEntity;
import com.marketTrio.service.AService;
import com.marketTrio.service.GBService;
import com.marketTrio.service.ReviewService;
import com.marketTrio.service.SHService;

@Controller
@SessionAttributes("memberSession")
@RequestMapping("/purchase")
public class MyPurchaseListController {
	@Value("thyme/myPage/myPurchaseList")
	private String myPurchaseList;

	@Autowired	
	private SHService sHService;
	@Autowired	
	private AService aService;
	@Autowired	
	private GBService gBService;
	
	//내가 구매한 리스트 보여주기
	@RequestMapping("/myPurchaseList")
	public ModelAndView handleRequest(
		@ModelAttribute("memberSession") MemberSession memberSession) throws Exception {
		String memberId = memberSession.getMemberId();
		 
		ModelAndView modelAndView = new ModelAndView(myPurchaseList);
	    modelAndView.addObject("SHPurchaseList", sHService.getSHPurchaseListByMemberId(memberId));
	    modelAndView.addObject("APurchaseList", aService.getAPurchaseListByMemberId(memberId));
	    modelAndView.addObject("GBPurchaseList", gBService.getGBPurchaseListByMemberId(memberId));
	    return modelAndView;
	}
}
