package com.marketTrio.member.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.marketTrio.member.domain.Member;
import com.marketTrio.member.service.MyInfoService;
import com.marketTrio.member.service.MyInfoValidator;

@Controller
@RequestMapping("/myPage")
@SessionAttributes({"memberCommand", "memberSession"})
public class MyInfoController {

	@Value("thyme/myPage/myInfoCheck")
	private String myInfoCheck;
	@Value("thyme/myPage/myInfoUpdate")
	private String myInfoUpdate;
	@Value("thyme/myPage/myInfo")
	private String myInfo;
	@Value("thyme/myPage/quit")
	private String quit;
	@Value("thyme/main")
	private String main;

	@Autowired
	private MyInfoService myInfoService;
	@Autowired
	private MyInfoValidator validator;

	@ModelAttribute("memberCommand")
	public MemberCommand formBacking(@ModelAttribute("memberSession") MemberSession memberSession,
			HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		MemberCommand memberCommand = (MemberCommand) session.getAttribute("memberCommand");
		if (memberCommand == null) {
			String memberId = memberSession.getMemberId();
			Member member = myInfoService.getMember(memberId);
			memberCommand = new MemberCommand(member);
		}
		return memberCommand;
	}

	@RequestMapping("/myInfo")
	public String showMyInfo(HttpServletRequest request, Model model, SessionStatus status) throws Exception {
		MemberCommand memberCommand = (MemberCommand) model.getAttribute("memberCommand");
		if (memberCommand == null) throw new RuntimeException("memberCommand is null");
		float dbRate = myInfoService.getRate(memberCommand.getMember().getId());
		if (dbRate != memberCommand.getMember().getRating()) {
			memberCommand.getMember().setRating(dbRate);
		}
		return myInfo;
	}

	@RequestMapping("/myInfoCheck")
	public String showMyInfoCheck() {
		return myInfoCheck;
	}

	@GetMapping("/myInfoUpdate")
	public String showForm(@RequestParam("password") String password,
			@ModelAttribute("memberCommand") MemberCommand memberCommand, Model model, HttpServletRequest requests) throws Exception {
		try {
			String memberId = memberCommand.getMember().getId();
			String dbPassword = myInfoService.getPassword(memberId);
			String dbProfilePicture = myInfoService.getProfilePicture(memberId);
			memberCommand.getMember().setProfilePicture(dbProfilePicture);
			float dbRate = myInfoService.getRate(memberCommand.getMember().getId());
			if (dbRate != memberCommand.getMember().getRating()) {
				memberCommand.getMember().setRating(dbRate);
			}
			if (password == null || password.length() < 1 || !dbPassword.equals(password)) {
				model.addAttribute("message", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
				return myInfoCheck;
			}
			return myInfoUpdate;
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "오류가 발생했습니다. 다시 시도해주세요.");
			return myInfoCheck;
		}
	}

	@PostMapping("/myInfoUpdate")
	public String onSubmit(@Valid @ModelAttribute MemberCommand memberCommand, BindingResult result,
			HttpServletRequest request, Model model, @RequestParam("files") MultipartFile[] files)
			throws IOException, ServletException, Exception {
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
		String memberId = memberCommand.getMember().getId();
		String firstUploadedFileName = !uploadedFileNames.isEmpty() ? uploadedFileNames.get(0) : null;
		if (firstUploadedFileName == null) {
			firstUploadedFileName = myInfoService.getProfilePicture(memberId);
		}
		memberCommand.getMember().setProfilePicture(firstUploadedFileName);

		validator.validate(memberCommand, result);
		if (result.hasErrors()) {
			return myInfoUpdate;
		}
		Member member = memberCommand.getMember();
		myInfoService.updateMember(member);
		request.removeAttribute("memberCommand");
		return "redirect:/myPage/myInfo";
	}

	@RequestMapping("/quit")
	public String quit() {
		return quit;
	}

	@PostMapping("/quit")
	public String quit(@RequestParam("password") String password, @ModelAttribute MemberCommand memberCommand,
			SessionStatus status, HttpServletRequest request, Model model) {
		try {
			String memberId = memberCommand.getMember().getId();
			String dbPassword = myInfoService.getPassword(memberId);
			if (password == null || password.length() < 1 || !dbPassword.equals(password)) {
				model.addAttribute("message", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
				return quit;
			}
			myInfoService.deleteMember(memberId);
			status.setComplete();
			return main;
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "오류가 발생했습니다. 다시 시도해주세요.");
			return quit;
		}
	}
}
