package com.marketTrio.groupbuy.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.marketTrio.groupbuy.domain.GBEntity;
import com.marketTrio.groupbuy.domain.GBParticipantEntity;
import com.marketTrio.groupbuy.service.GBService;
import com.marketTrio.member.controller.MemberSession;
import com.marketTrio.option.domain.OptionEntity;

@Controller
@RequestMapping("/groupBuy")
@SessionAttributes("memberSession")
public class GBController {

	@Autowired
	private GBService gbService;

	@RequestMapping("/list")
	public ModelAndView getGBPostList() {
		ModelAndView mv = new ModelAndView("thyme/groupBuy/groupBuyList");
		mv.addObject("gbPostList", gbService.getGBPostList());
		return mv;
	}

	@RequestMapping("/detail/{GBPostId}")
	public ModelAndView getGBPost(
			@ModelAttribute("memberSession") MemberSession memberSession,
			@PathVariable("GBPostId") int GBPostId) {
		String loginUserId = memberSession.getMemberId();

		GBEntity gbPost = gbService.getGBPost(GBPostId);
		String gbPostAuthor = gbPost.getMember().getId();
		String dday = gbService.calculateDday(gbPost);
		double sp = gbPost.getRegularPrice() * (gbPost.getDiscountRate() / 100);
		int salePrice = gbPost.getRegularPrice() - (int) Math.floor(sp);
		List<OptionEntity> opList = gbService.findOptionList(GBPostId);
		List<GBParticipantEntity> gbpList = gbService.findGBPaticipantsList(GBPostId);

		ModelAndView mv;
		if (loginUserId.equals(gbPostAuthor)) {
			mv = new ModelAndView("thyme/groupBuy/gbSellerDetail");
		} else {
			GBParticipantEntity gbPart = gbService.getGBPart(GBPostId, loginUserId);
			if (gbPart == null) {
				mv = new ModelAndView("thyme/groupBuy/gbNoneBuyerDetail");
				mv.addObject("gbpCommand", new GBParticipateCommand());
			} else {
				mv = new ModelAndView("thyme/groupBuy/gbBuyerDetail");
			}
		}

		mv.addObject("gbPost", gbPost);
		mv.addObject("dday", dday);
		mv.addObject("salePrice", salePrice);
		mv.addObject("optionList", opList);
		mv.addObject("gbpList", gbpList);
		return mv;
	}

	@GetMapping("/write")
	public ModelAndView writeform() {
		ModelAndView mv = new ModelAndView("thyme/groupBuy/groupBuyWrite");
		mv.addObject("gbInfoCommand", new GBInfoCommand());
		return mv;
	}

	@PostMapping("/writed")
	public String writeGBPost(
			@ModelAttribute("gbInfoCommand") GBInfoCommand gbInfo,
			@ModelAttribute("memberSession") MemberSession memberSession,
			@RequestParam("files") MultipartFile[] files,
			RedirectAttributes redirectAttributes) throws IOException, ServletException {
		String loginUserId = memberSession.getMemberId();
		boolean hasError = false;

		if (gbInfo.getDuration() != null && gbInfo.getDuration().before(new Date())) {
			redirectAttributes.addFlashAttribute("MinusDate", "현재 시각보다 마감일이 늦는 글을 작성할 수 없습니다.");
			hasError = true;
		}
		if (gbInfo.getRegularPrice() <= 0 && gbInfo.getDiscountRate() <= 0 && gbInfo.getAllQuantity() <= 0) {
			redirectAttributes.addFlashAttribute("Minus", "정가, 할인율, 총 수량은 0 이하의 값이 될 수 없습니다.");
			hasError = true;
		}
		if (gbInfo.getOptions() == null) {
			redirectAttributes.addFlashAttribute("MinusOption", "1개 이상의 옵션을 생성해야합니다.");
			hasError = true;
		}
		if (hasError) return "redirect:/groupBuy/write";

		String fileDir = "C:/absolute/path/to/upload/";
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

		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			gbInfo.setCreateDate(dateFormat.parse(dateFormat.format(now)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		GBEntity createdPost = gbService.createGBPost(gbInfo, loginUserId, uploadedFileNames);
		int postId = createdPost.getGBPostId();

		for (OptionEntity option : gbInfo.getOptions()) {
			option.setGBEntity(createdPost);
			gbService.createOption(option);
		}
		return "redirect:/groupBuy/detail/" + postId;
	}

	@RequestMapping("/delete")
	public String deleteGBPost(@RequestParam("gbPostId") int gbPostId) {
		GBEntity gbPost = gbService.getGBPost(gbPostId);
		List<OptionEntity> optList = gbService.findOptionList(gbPostId);
		List<GBParticipantEntity> gbpList = gbService.findGBPaticipantsList(gbPostId);
		gbService.deleteGBPost(gbPost, optList, gbpList);
		return "redirect:/groupBuy/list";
	}

	@GetMapping("/update/{GBPostId}")
	public ModelAndView Updateform(@PathVariable("GBPostId") int GBPostId) {
		ModelAndView mv = new ModelAndView("thyme/groupBuy/groupBuyCorrection");
		GBEntity gbPost = gbService.getGBPost(GBPostId);
		GBUpdateInfoCommand gbUpdateInfoCommand = new GBUpdateInfoCommand(gbPost.getGBPostId(), gbPost.getProductName(), gbPost.getDuration(), gbPost.getContent());
		mv.addObject("gbPost", gbPost);
		mv.addObject("gbUpdateInfoCommand", gbUpdateInfoCommand);
		return mv;
	}

	@PostMapping("/updated/{GBPostId}")
	public String updateGBPost(
			@ModelAttribute("gbUpdateInfoCommand") GBUpdateInfoCommand gbUpdateInfo,
			@PathVariable("GBPostId") int GBPostId,
			@RequestParam("files") MultipartFile[] files,
			RedirectAttributes redirectAttributes) throws IOException, ServletException {
		GBEntity gbPost = gbService.getGBPost(GBPostId);
		if (gbUpdateInfo.getDuration() != null && gbUpdateInfo.getDuration().before(new Date())) {
			redirectAttributes.addFlashAttribute("MinusDate", "현재 시각보다 마감일이 늦는 글로 수정할 수 없습니다.");
			return "redirect:/groupBuy/update/" + GBPostId;
		}

		List<String> uploadedFileNames = gbPost.getPictures() != null ? new ArrayList<>(gbPost.getPictures()) : new ArrayList<>();
		if (files != null && files.length > 0) {
			File directory = new File("C:/absolute/path/to/upload/");
			if (!directory.exists()) directory.mkdirs();
			for (MultipartFile file : files) {
				String name = file.getOriginalFilename();
				if (name != null && !name.isEmpty()) {
					file.transferTo(new File(directory.getPath() + "/" + name));
					uploadedFileNames.add(name);
				}
			}
		}
		gbService.updateGBPost(gbUpdateInfo, uploadedFileNames);
		return "redirect:/groupBuy/detail/" + GBPostId;
	}

	@PostMapping("/participate/{GBPostId}")
	public ModelAndView participateGB(
			@ModelAttribute("gbpCommand") GBParticipateCommand gbParticipate,
			@PathVariable("GBPostId") int GBPostId,
			@ModelAttribute("memberSession") MemberSession memberSession,
			RedirectAttributes redirectAttributes) {
		String loginUserId = memberSession.getMemberId();
		if (gbParticipate.getQuantity() <= 0) {
			redirectAttributes.addFlashAttribute("MinusQuantity", "수량은 0 이하가 될 수 없습니다.");
			ModelAndView mv = new ModelAndView("redirect:/groupBuy/detail/" + GBPostId);
			return mv;
		}
		gbParticipate.setMember(loginUserId);
		gbService.participate(gbParticipate);
		return new ModelAndView("redirect:/groupBuy/detail/" + GBPostId);
	}

	@RequestMapping("/participateCancel/{GBPostId}")
	public String participateCancelGB(
			@ModelAttribute("memberSession") MemberSession memberSession,
			@PathVariable("GBPostId") int GBPostId) {
		String loginUserId = memberSession.getMemberId();
		gbService.participateCancel(GBPostId, loginUserId);
		return "redirect:/groupBuy/detail/" + GBPostId;
	}
}
