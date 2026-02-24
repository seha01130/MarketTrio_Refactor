package com.marketTrio.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.marketTrio.auction.service.AService;
import com.marketTrio.groupbuy.service.GBService;
import com.marketTrio.secondhand.service.SHService;

@Controller
@SessionAttributes("memberSession")
@RequestMapping("/sales")
public class MySalesListController {

	@Value("thyme/myPage/mySalesList")
	private String mySalesList;

	@Autowired
	private SHService sHService;
	@Autowired
	private AService aService;
	@Autowired
	private GBService gBService;

	@RequestMapping("/mySalesList")
	public ModelAndView handleRequest(@ModelAttribute("memberSession") MemberSession memberSession) throws Exception {
		String memberId = memberSession.getMemberId();
		ModelAndView modelAndView = new ModelAndView(mySalesList);
		modelAndView.addObject("SHSalesList", sHService.getSHSalesListByMemberId(memberId));
		modelAndView.addObject("ASalesList", aService.getASalesListByMemberId(memberId));
		modelAndView.addObject("GBSalesList", gBService.getGBSalesListByMemberId(memberId));
		return modelAndView;
	}
}
