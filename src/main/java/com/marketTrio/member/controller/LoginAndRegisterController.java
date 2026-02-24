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

@Controller
@RequestMapping("/loginAndRegister")
@SessionAttributes({"memberCommand", "memberSession"})
public class LoginAndRegisterController {

	@Value("thyme/loginAndRegister/login")
	private String login;
	@Value("thyme/main")
	private String main;
	@Value("thyme/loginAndRegister/register")
	private String register;

	@Autowired
	private MyInfoService myInfoService;

	@GetMapping("/register.do")
	public String register(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		MemberCommand memberCommand = (MemberCommand) session.getAttribute("memberCommand");
		if (memberCommand == null) {
			model.addAttribute("memberCommand", new MemberCommand());
		}
		return register;
	}

	@PostMapping("/register.do")
	public String register(HttpServletRequest request, SessionStatus sessionStatus, HttpSession session,
			@Valid @ModelAttribute MemberCommand memberCommand,
			BindingResult bindingResult, Model model,
			@RequestParam("files") MultipartFile[] files) throws IOException, ServletException, Exception {

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
		String firstUploadedFileName = !uploadedFileNames.isEmpty() ? uploadedFileNames.get(0) : null;
		memberCommand.getMember().setProfilePicture(firstUploadedFileName);

		if (bindingResult.hasErrors()) return register;
		if (myInfoService.isIdExist(memberCommand.getUserId())) {
			model.addAttribute("IdErrorMessage", "The ID already exists. Please choose a different ID.");
			return register;
		}
		if (!memberCommand.getPassword().equals(memberCommand.getRepeatedPassword())) {
			model.addAttribute("Pwdmessage", "Passwords do not match");
			return register;
		}
		Member member = memberCommand.getMember();
		myInfoService.insertMember(member);
		sessionStatus.setComplete();
		session.invalidate();
		return login;
	}

	@RequestMapping("/main")
	public String goMain() {
		return main;
	}

	@GetMapping("/login.do")
	public String login() {
		return login;
	}

	@PostMapping("/login.do")
	public String login(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam("id") String id,
			@RequestParam("password") String password) throws Exception {
		Member member = myInfoService.getMember(id, password);
		if (member == null) {
			model.addAttribute("message", "Invalid username or password.  Signon failed.");
			return login;
		} else {
			MemberSession memberSession = new MemberSession(id);
			model.addAttribute("memberSession", memberSession);
			return main;
		}
	}

	@RequestMapping("/logout.do")
	public String logout(SessionStatus sessionStatus, HttpSession session) {
		sessionStatus.setComplete();
		session.invalidate();
		return "redirect:/loginAndRegister/main";
	}
}
