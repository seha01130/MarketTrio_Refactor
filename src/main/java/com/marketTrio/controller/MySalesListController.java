package com.marketTrio.controller;

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
	
	//내가 판매한 리스트 보여주기
	@RequestMapping("/mySalesList")
	public ModelAndView handleRequest(
		@ModelAttribute("memberSession") MemberSession memberSession) throws Exception {
		String memberId = memberSession.getMemberId();
		System.out.println("아이디 잘 받아옴?: " + memberId);
		 
		ModelAndView modelAndView = new ModelAndView(mySalesList);
	    modelAndView.addObject("SHSalesList", sHService.getSHSalesListByMemberId(memberId));
	    modelAndView.addObject("ASalesList", aService.getASalesListByMemberId(memberId));
	    modelAndView.addObject("GBSalesList", gBService.getGBSalesListByMemberId(memberId));
	    return modelAndView;
	}
}
