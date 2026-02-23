package com.marketTrio.controller;

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

import com.marketTrio.domain.Member;
import com.marketTrio.service.MyInfoService;
import com.marketTrio.service.MyInfoValidator;

@Controller
@RequestMapping("/myPage")
@SessionAttributes({ "memberCommand", "memberSession"})
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

	public void setValidator(MyInfoValidator validator) {
		this.validator = validator;
	}

//	@ModelAttribute 어노테이션이 있는 메소드가 호출될 때 memberCommand 객체가 모델에 자동으로 추가된다는 것입니다. 그리고 이 객체는 세션에도 저장됩니다.
	@ModelAttribute("memberCommand")
	public MemberCommand formBacking(@ModelAttribute("memberSession") MemberSession memberSession,
			HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession(); // HttpSession을 통해 세션에 저장된 객체를 직접 가져옴
		MemberCommand memberCommand = (MemberCommand) session.getAttribute("memberCommand");
//	    System.out.println("멤버세션객체 만들어졌는지: " + memberCommand.getMember().getNickname()); -> Error
		// Cannot invoke "com.marketTrio.controller.MemberCommand.getMember()" because
		// "memberCommand" is null

		if (memberCommand == null) {
			String memberId = memberSession.getMemberId(); // 이 memberSession은 login할때 넣어줌
			System.out.println("멤버세션객체 만들어졌는지: " + memberId);
//			String memberId = "seha";   //위에꺼 주석풀면 이거 주석처리해주기
			Member member = myInfoService.getMember(memberId); // 현재 사용자의 member객체 구해서
			System.out.println("Member ID: " + member.getId());
			memberCommand = new MemberCommand(member); // memberCommand 객체내의 필드로 member객체를 넣고
		}
		return memberCommand; // session에 command객체를 저장
	}

	// 내 정보 보러 감
	@RequestMapping("/myInfo")
	public String showMyInfo(HttpServletRequest request, Model model, SessionStatus status) throws Exception {
//		formBacking에서 MemberCommand 객체를 session에 저장했으니 아래 로직 필요 없음
//		MemberSession memberSession = (MemberSession) request.getSession().getAttribute("memberId");
//		Member member = marketTrio.getMember(memberSession.getMemberId());
//		model.addAttribute("member", member);

//		status.setComplete();  //오류났을시 dirty read 막기위해서.

		MemberCommand memberCommand = (MemberCommand) model.getAttribute("memberCommand");
		if (memberCommand == null) {
			throw new RuntimeException("memberCommand is null");
		}

//		System.out.println("프로필사진 잘 불러오나: " + memberCommand.getMember().getProfilePicture());

		float dbRate = myInfoService.getRate(memberCommand.getMember().getId());
		System.out.println("DB에서 가져온 별점: " + dbRate);
		if (dbRate != memberCommand.getMember().getRating()) {
			memberCommand.getMember().setRating(dbRate);
		}
		System.out.println("나의 현재 별점 출력: " + memberCommand.getMember().getRating());
		return myInfo; // myInfo에서는 session에서 memberCommand 꺼내서 사용하면 됨
	}

	// 내 정보에서 정보수정 버튼 눌러서 myInfoCheck화면에서 비번 확인하는 창으로 가는거
	@RequestMapping("/myInfoCheck")
	public String showMyInfoCheck() {
		return myInfoCheck;
	}

	// 비번입력하고 확인버튼 누르면 정보 수정폼에 정보 보여주는 로직
//	@ModelAttribute("memberCommand")
	@GetMapping("/myInfoUpdate")
	public String showForm(@RequestParam("password") String password,
			@ModelAttribute("memberCommand") MemberCommand memberCommand, Model model, HttpServletRequest requests)
			throws Exception {

		try {
			String memberId = memberCommand.getMember().getId();
			String dbPassword = myInfoService.getPassword(memberId);
			
			//수정폼 들어가도 나의 원래 프로필사진이 보여지고 선택되도록.
			String dbProfilePicture = myInfoService.getProfilePicture(memberId);
			System.out.println("프로필사진 문자열 출력: " + dbProfilePicture);
			memberCommand.getMember().setProfilePicture(dbProfilePicture);
			System.out.println("프로필사진 커맨드에 저장: " + memberCommand.getMember().getProfilePicture());
			//
			
			float dbRate = myInfoService.getRate(memberCommand.getMember().getId());
			System.out.println("DB에서 가져온 별점: " + dbRate);
			if (dbRate != memberCommand.getMember().getRating()) {
				memberCommand.getMember().setRating(dbRate);
			}
			System.out.println("나의 현재 별점 출력: " + memberCommand.getMember().getRating());
			

			if (password == null || password.length() < 1 || !dbPassword.equals(password)) {
				model.addAttribute("message", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
				return myInfoCheck;
			}
			else {
				return myInfoUpdate;
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "오류가 발생했습니다. 다시 시도해주세요.");
			return myInfoCheck;
		}
	}

	// 정보수정 다 하고 수정완료 버튼 누르는 로직
	@PostMapping("/myInfoUpdate")
	public String onSubmit(@Valid @ModelAttribute MemberCommand memberCommand, BindingResult result,
			HttpServletRequest request, Model model, @RequestParam("files") MultipartFile[] files)
			throws IOException, ServletException, Exception {

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
		
		String memberId = memberCommand.getMember().getId();
		String firstUploadedFileName = !uploadedFileNames.isEmpty() ? uploadedFileNames.getFirst() : null;
		if (firstUploadedFileName==null) {
			firstUploadedFileName = myInfoService.getProfilePicture(memberId);
		}
		memberCommand.getMember().setProfilePicture(firstUploadedFileName);	
		System.out.println("사진이 command에 잘 저장됨?: " + firstUploadedFileName);
		////////////////////이미지처리////////////////////////////////////////////////////////////////////////////
		validator.validate(memberCommand, result);

		if (result.hasErrors()) {
			return myInfoUpdate;
		} else {
			Member member = memberCommand.getMember();
			System.out.println("커맨드 내용 출력(내가 입력한 폼의 비밀번호): " + member.getNickname());
			myInfoService.updateMember(member);
			request.removeAttribute("memberCommand");
			return "redirect:/myPage/myInfo";
			/*
			 * 정보 수정이 완료되면 myInfo 페이지로 리다이렉션 리다이렉션된 myInfo.jsp로 요청이 이동하면 showMyInfo 메소드가
			 * 실행됨. 그러면 @ModelAttribute("memberCommand") 어노테이션이 있는 formBacking 메소드가 실행되어 새로운
			 * memberCommand 객체가 생성되어 세션에 저장됨. 세션이 비워지더라도 새로운 memberCommand 객체가 생성되어 사용
			 * 가능하므로, 데이터의 유실 없이 페이지를 표시할 수 있음.
			 */
		}
	}

	// 회원탈퇴 화면으로 감
	@RequestMapping("/quit")
	public String quit() {
		return quit;
	}

	@PostMapping("/quit")
	public String quit(@RequestParam("password") String password, @ModelAttribute MemberCommand memberCommand,
			SessionStatus status, HttpServletRequest request, Model model) {
		try {
			System.out.println("입력받은 비번: " + password);
			String memberId = memberCommand.getMember().getId();
			String dbPassword = myInfoService.getPassword(memberId);

			if (password == null || password.length() < 1 || !dbPassword.equals(password)) {
				model.addAttribute("message", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
				return quit; // 다시 회원 탈퇴 화면으로 이동
			} else {
				myInfoService.deleteMember(memberId);
				status.setComplete();
				return main;
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "오류가 발생했습니다. 다시 시도해주세요.");
			return quit;
		}
	}
}
