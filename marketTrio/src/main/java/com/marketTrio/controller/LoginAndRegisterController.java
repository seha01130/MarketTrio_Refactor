package com.marketTrio.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.support.PagedListHolder;
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
import org.springframework.web.servlet.ModelAndView;

import com.marketTrio.controller.MemberSession;
import com.marketTrio.dao.mybatis.MyBatisMemberDao;
import com.marketTrio.domain.Member;
import com.marketTrio.service.MyInfoService;

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
	
/////////////////////////////////////////////////////////////////회원가입////////////////////////////////////////////////////////////////////
	
	@GetMapping("/register.do")
	public String register(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(); // HttpSession을 통해 세션에 저장된 객체를 직접 가져옴
		MemberCommand memberCommand = (MemberCommand) session.getAttribute("memberCommand");
//		System.out.println("현재 memberCommand의 이름값: " + memberCommand.getName());
		if (memberCommand == null) {
			model.addAttribute("memberCommand", new MemberCommand());
//			System.out.println("현재 memberCommand의 이름값은 새롭기때문에 null이어야함: " + memberCommand.getName());
		}
		return register;
	}
	
	@PostMapping("/register.do")
	public String register(HttpServletRequest request,SessionStatus sessionStatus, HttpSession session,
							@Valid @ModelAttribute MemberCommand memberCommand,
							BindingResult bindingResult, Model model,
							@RequestParam("files") MultipartFile[] files)  throws IOException, ServletException, Exception {
		
		
		////////////////////이미지처리////////////////////////////////////////////////////////////////////////////
		String fileDir = "C:/absolute/path/to/upload/";

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
        // 첫 번째 파일 이름을 사용
        String firstUploadedFileName = !uploadedFileNames.isEmpty() ? uploadedFileNames.getFirst() : null;
        memberCommand.getMember().setProfilePicture(firstUploadedFileName);
        
        System.out.println("사진이 command에 잘 저장됨?: " + firstUploadedFileName);
        ////////////////////////////이미지처리 끝//////////////////////////////////////////////////////////////////////////
		
		System.out.println("커맨드 클래스 잘 갖고오니?: " + memberCommand.getName());
		if (bindingResult.hasErrors()) {
            return register;
        }
		
		//입력받은 id가 이미 존재하는지 확인하는 코드
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
	
/////////////////////////////////////////////////////////////////로그인////////////////////////////////////////////////////////////////////
	
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
								@RequestParam("password") String password
								/*@ModelAttribute("memberCommand") MemberCommand memberCommand*/) throws Exception {
		// 예를 들어, 로그인 폼에서 가져온 사용자 ID를 사용하여 MemberSession 객체를 생성합니다.
		Member member = myInfoService.getMember(id, password);
		if (member == null) {  //없으면
			model.addAttribute("message", "Invalid username or password.  Signon failed.");
            return login;
//			return new ModelAndView("Error", "message", "Invalid username or password.  Signon failed.");
		} else {  //있으면
			MemberSession memberSession = new MemberSession(id); //MemberSession 객체 만들어짐
//			session.setAttribute("memberSession", memberSession); //이거 없어도 됨. 위에 @SessionAttributes 했으니까 아래의 코드만으로도 충분. 함께 저장됨.
			model.addAttribute("memberSession", memberSession);
			
			
			//로그인하면 배너에 프로필사진을 보여주기 위해 로그인시 바로 memberCommand 생성해서 정보 담아주기
//			session = request.getSession(); // HttpSession을 통해 세션에 저장된 객체를 직접 가져옴
//			MemberCommand memberCommand = (MemberCommand) session.getAttribute("memberCommand");
			
//			if (memberCommand == null) {
//				String memberId = memberSession.getMemberId(); // 이 memberSession은 login할때 넣어줌
//				System.out.println("멤버세션객체 만들어졌는지: " + memberId);
//				member = myInfoService.getMember(memberId); // 현재 사용자의 member객체 구해서
//				System.out.println("Member ID: " + member.getId());
//				memberCommand = new MemberCommand(member); // memberCommand 객체내의 필드로 member객체를 넣고
//				model.addAttribute("memberCommand", memberCommand);
//			}
			//
			
			
			
			return main;
		}
	}
	
/////////////////////////////////////////////////////////////////로그아웃////////////////////////////////////////////////////////////////////
	
	@RequestMapping("/logout.do")
	public String logout(SessionStatus sessionStatus, HttpSession session) {
		sessionStatus.setComplete();
		session.invalidate();
		return "redirect:/loginAndRegister/main";
	}
}
