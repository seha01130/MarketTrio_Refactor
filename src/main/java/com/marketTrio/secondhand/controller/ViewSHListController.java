package com.marketTrio.secondhand.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.servlet.ModelAndView;

import com.marketTrio.member.controller.MemberSession;
import com.marketTrio.member.domain.Member;
import com.marketTrio.reply.controller.ReplyCommand;
import com.marketTrio.reply.domain.ReplyEntity;
import com.marketTrio.reply.service.ReplyService;
import com.marketTrio.secondhand.domain.SecondHandEntity;
import com.marketTrio.secondhand.service.SHService;

@Controller
@SessionAttributes("memberSession")
@RequestMapping("/shPost")
public class ViewSHListController {

	@Autowired
	private SHService sHService;
	@Autowired
	private ReplyService replyService;

	@Value("thyme/SecondHand/secondHandWrite")
	private String shForm;
	@Value("thyme/SecondHand/secondHandEdit")
	private String shEdit;

	@RequestMapping("/list")
	public ModelAndView showList() {
		ModelAndView modelAndView = new ModelAndView("thyme/SecondHand/secondHandList");
		modelAndView.addObject("SHPostList", sHService.getAllSHList());
		return modelAndView;
	}

	@GetMapping("/detail/{SHPostId}")
	public String getSHPost(@PathVariable("SHPostId") int SHPostId, Model model,
			@ModelAttribute("memberSession") MemberSession memberSession) {
		String loginUserId = memberSession.getMemberId();
		SecondHandEntity shPost = sHService.getSHPostByPostId(SHPostId);
		List<ReplyEntity> replyList = replyService.getAllReply(SHPostId);
		List<Member> buyerList = replyService.showBuyerMemberList(SHPostId);

		model.addAttribute("replyCommand", new ReplyCommand());
		model.addAttribute("replyList", replyList);
		model.addAttribute("shPost", shPost);
		model.addAttribute("buyerList", buyerList);
		model.addAttribute("loginUser", loginUserId);

		Iterator<Member> iterator = buyerList.iterator();
		while (iterator.hasNext()) {
			Member item = iterator.next();
			if (item.getId().equals(shPost.getMember().getId())) {
				iterator.remove();
			}
		}

		if (loginUserId.equals(shPost.getMember().getId())) {
			return "thyme/SecondHand/SHSellerDetail";
		} else {
			return "thyme/SecondHand/SHBuyerDetail";
		}
	}

	@PostMapping("/detail/{SHPostId}")
	public String readSHPost(@RequestParam(name = "formType") String formType,
			@RequestParam(name = "buyerId", required = false) String buyerId,
			@RequestParam(name = "replyId", required = false) Integer replyId,
			@ModelAttribute("shPost") SecondHandEntity shPost, @PathVariable("SHPostId") int SHPostId,
			ReplyCommand replyCommand, Model model, HttpServletRequest request,
			@ModelAttribute("memberSession") MemberSession memberSession) throws IOException, ServletException {
		String loginUserId = memberSession.getMemberId();

		if ("selectBuyer".equals(formType)) {
			sHService.selectBuyer(buyerId, SHPostId);
		} else if ("selectComment".equals(formType)) {
			replyService.insertReply(replyCommand, SHPostId, loginUserId, shPost);
		} else if ("deleteComment".equals(formType) && replyId != null) {
			ReplyEntity re = replyService.findReply(replyId);
			re.setSHEntity(null);
			re.setMember(null);
			replyService.deleteReply(re);
		}
		return "redirect:/shPost/detail/" + SHPostId;
	}

	@GetMapping("/write")
	public String showForm(Model model) {
		model.addAttribute("shCommand", new SHListCommand());
		return shForm;
	}

	@PostMapping("/write")
	public String writeSHPost(@Valid @ModelAttribute SHListCommand shCommand, Model model, HttpServletRequest request,
			@RequestParam("files") MultipartFile[] files, @ModelAttribute("memberSession") MemberSession memberSession)
			throws IOException, ServletException {
		String loginUserId = memberSession.getMemberId();
		String fileDir = "C:/absolute/path/to/upload/";

		boolean hasError = false;
		if (shCommand.getTitle() == null || shCommand.getTitle().isEmpty()) {
			model.addAttribute("NoTitleMsg", "Insert Title");
			hasError = true;
		}
		if (shCommand.getPrice() == 0) {
			model.addAttribute("NoPriceMsg", "Insert Price");
			hasError = true;
		}
		if (shCommand.getContent() == null || shCommand.getContent().isEmpty()) {
			model.addAttribute("NoContentMsg", "Insert Content");
			hasError = true;
		}
		if (hasError) {
			model.addAttribute("shCommand", shCommand);
			return shForm;
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

		SecondHandEntity shPost = sHService.insertSHPost(shCommand, loginUserId, uploadedFileNames);
		return "redirect:/shPost/detail/" + shPost.getSHPostId();
	}

	@RequestMapping("/delete")
	public String deleteSHPost(@RequestParam("shPostId") int shPostId) {
		SecondHandEntity shPost = sHService.getSHPostByPostId(shPostId);
		List<ReplyEntity> reEntity = replyService.getReplyBySHPostId(shPostId);
		sHService.deleteSHPost(shPost, reEntity);
		return "redirect:/shPost/list/";
	}

	@GetMapping("/update/{SHPostId}")
	public String updateForm(@PathVariable("SHPostId") int SHPostId, Model model) {
		SecondHandEntity shPost = sHService.getSHPostByPostId(SHPostId);
		SHListCommand shCommand = new SHListCommand();
		shCommand.setTitle(shPost.getTitle());
		shCommand.setPrice(shPost.getPrice());
		shCommand.setContent(shPost.getContent());
		shCommand.setImage(shPost.getImage());
		model.addAttribute("shPost", shPost);
		model.addAttribute("shCommand", shCommand);
		return shEdit;
	}

	@PostMapping("/update/{SHPostId}")
	public String updateSHPost(@PathVariable("SHPostId") int SHPostId, SHListCommand shCommand, Model model,
			HttpServletRequest request, @RequestParam("files") MultipartFile[] files)
			throws IOException, ServletException {
		String fileDir = "C:/absolute/path/to/upload/";
		SecondHandEntity sEntity = sHService.getSHPostByPostId(SHPostId);
		List<String> uploadedFileNames = sEntity.getImage() != null ? new ArrayList<>(sEntity.getImage()) : new ArrayList<>();

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

		SecondHandEntity shPost = sHService.updateSHPost(shCommand, SHPostId, uploadedFileNames);
		return "redirect:/shPost/detail/" + shPost.getSHPostId();
	}
}
