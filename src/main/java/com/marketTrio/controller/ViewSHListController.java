package com.marketTrio.controller;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.marketTrio.dao.mybatis.MyBatisMemberDao;
import com.marketTrio.domain.Member;
import com.marketTrio.domain.ReplyEntity;
import com.marketTrio.domain.SecondHandEntity;
import com.marketTrio.service.ReplyService;
import com.marketTrio.service.SHService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Controller
@SessionAttributes("memberSession")
@RequestMapping("/shPost")
public class ViewSHListController {

	@Autowired
	private SHService sHService;
	@Autowired
	private ReplyService replyService;
	@Autowired
	private MyBatisMemberDao memberDao;

	// 주소들 연결 편하게 하기 위한 Value 지정~
	@Value("thyme/SecondHand/secondHandWrite")
	private String shForm;
	@Value("thyme/SecondHand/secondHandEdit")
	private String shEdit;

	// 중고거래 리스트 보여주기 => 페이징 처리를 어떻게...? 이거 생각해봐야함
	@RequestMapping("/list")
	public ModelAndView showList() {
		ModelAndView modelAndView = new ModelAndView("thyme/SecondHand/secondHandList"); // shPostList.jsp로 감
		modelAndView.addObject("SHPostList", sHService.getAllSHList());
		return modelAndView;
	}

	// 댓글 작성 get요청(게시물 상세보기 페이지 GET 요청)
	// 게시물 상세 보기
	@GetMapping("/detail/{SHPostId}")
	public String getSHPost(@PathVariable("SHPostId") int SHPostId, Model model,
			@ModelAttribute("memberSession") MemberSession memberSession) {

		// 지금은 로그인 기능 구현 전이므로 id를 seha로 두고 해보기
//      String loginUserId = "dami";
		String loginUserId = memberSession.getMemberId();

		SecondHandEntity shPost = sHService.getSHPostByPostId(SHPostId);
		List<ReplyEntity> replyList = replyService.getAllReply(SHPostId);
		List<Member> buyerList = replyService.showBuyerMemberList(SHPostId);

		// @requstParam

		// 비어있는 command add함
		model.addAttribute("replyCommand", new ReplyCommand());
		model.addAttribute("replyList", replyList);
		model.addAttribute("shPost", shPost);
		model.addAttribute("buyerList", buyerList);
		model.addAttribute("loginUser", loginUserId);

		/*
		 * buyermember 리스트를 댓글 단 사람으로 가져옴 근데 게시물 작성자가 댓글을 다는 것이 가능하므로 buyerList에서 작성자는
		 * 삭제해줘야함! => 리스트를 순회하면서 글 작성자일 경우 삭제
		 */

		Iterator<Member> iterator = buyerList.iterator();
		while (iterator.hasNext()) {
			Member item = iterator.next();
			if (item == shPost.getMember()) {
				iterator.remove();
			}
		}

		// 작성자와 아닌사람 화면 구분해주기
		if (loginUserId.equals(shPost.getMember().getId())) {
			// 작성자면 SHSellerDetail로 이동
			return "thyme/SecondHand/SHSellerDetail";
		} else {
			// 작성자 아니면 SHBuyerDetail로 이동
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
//      String loginUserId = "dami";

		ReplyEntity rEntity = new ReplyEntity();

//      System.out.println("여기까진 오나?");
//      System.out.println(formType);
//      System.out.println(replyId);

		if ("selectBuyer".equals(formType)) {
			// buyerForm 처리 로직
			sHService.selectBuyer(buyerId, SHPostId);
		} else if ("selectComment".equals(formType)) {
			// commentForm 처리 로직
			System.out.println("커맨트 처리");
			replyService.insertReply(replyCommand, SHPostId, loginUserId, shPost);
		} else if ("deleteComment".equals(formType)) {

			System.out.println(replyId);

			ReplyEntity re = replyService.findReply(replyId);

			re.setSHEntity(null);
			re.setMember(null);

			replyService.deleteReply(re);
		}

		model.addAttribute("reply", rEntity);
		model.addAttribute("buyerId", buyerId);

		return "redirect:/shPost/detail/" + SHPostId;
	}

	// 중고거래 게시물 작성 get요청
	@GetMapping("/write")
	public String showForm(Model model) {
		// 비어있는 command add함
		model.addAttribute("shCommand", new SHListCommand());
		return shForm;
	}

	// 중고거래글 작성
	@PostMapping("/write")
	public String writeSHPost(@Valid @ModelAttribute SHListCommand shCommand, Model model, HttpServletRequest request,
			@RequestParam("files") MultipartFile[] files, @ModelAttribute("memberSession") MemberSession memberSession)
			throws IOException, ServletException {
//      String loginUserId = "dami";
		String loginUserId = memberSession.getMemberId();

		String fileDir = "C:/absolute/path/to/upload/";

		boolean hasError = false;

		if (shCommand.getTitle().isEmpty()) {
			model.addAttribute("NoTitleMsg", "Insert Title");
			hasError = true;
		}

		if (shCommand.getPrice() == 0) {
			model.addAttribute("NoPriceMsg", "Insert Price");
			hasError = true;
		}

		if (shCommand.getContent().isEmpty()) {
			model.addAttribute("NoContentMsg", "Insert Content");
			hasError = true;
		}

		if (hasError) {
			model.addAttribute("shCommand", shCommand); // 폼 객체를 다시 모델에 추가
			return shForm; // 오류가 있으면 폼 페이지로 돌아감
		}

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

		if (shCommand.getTitle() == null) {
			model.addAttribute("NoTitleMsg", "Insert Title");
			return shForm;
		}

		if (shCommand.getPrice() == 0) {
			model.addAttribute("NoPriceMsg", "Insert Price");
			return shForm;
		}

		if (shCommand.getContent() == null) {
			model.addAttribute("NoContentMsg", "Insert Content");
			return shForm;
		}

		SecondHandEntity shPost = sHService.insertSHPost(shCommand, loginUserId, uploadedFileNames);
		shPost.setSHStatus(0);

		model.addAttribute("shPost", shPost);

		return "redirect:/shPost/detail/" + shPost.getSHPostId();
	}

	// 중고 거래 게시글 삭제
	@RequestMapping("/delete")
	public String deleteSHPost(@RequestParam("shPostId") int shPostId) {
		SecondHandEntity shPost = sHService.getSHPostByPostId(shPostId);
		List<ReplyEntity> reEntity = replyService.getReplyBySHPostId(shPostId);

		sHService.deleteSHPost(shPost, reEntity);

		return "redirect:/shPost/list/";
	}

	// // 공동구매 글 수정 - GET 방식
	// @GetMapping("/update")
	// public ModelAndView Updateform(int gbPostId) {
	// ModelAndView mv = new ModelAndView("groupBuyCorrection");
	// //groupBuyCorrection.jsp로 이동
	// GBPostEntity gbPost = gbService.getGBPost(gbPostId);
	// mv.addObject("gbPost", gbPost);
	//
	// return mv;
	// }
	//
	// // 공동구매 글 수정 - POST 방식
	// @PostMapping("/detail/{GBPostId}")
	// public GBPostEntity updateGBPost(
	// @RequestBody GBUpdateInfoCommand gbUpdateInfo,
	// @PathVariable("GBPostId") int GBPostId) {
	// GBPostEntity updatedPost = gbService.updateGBPost(gbUpdateInfo);
	//
	// return updatedPost;
	// }

	// 중고거래 게시글 수정
	// 작성자 여부 판단은 상세보기 코드에서 처리후 ui 차이를 둘 것이기 때문에 여기서는 따로 판별 코드 작성 X
	@GetMapping("/update/{SHPostId}")
	public String updateForm(@PathVariable("SHPostId") int SHPostId, Model model) {
		// model.addAttribute("shCommand", new SHListCommand());

		SecondHandEntity shPost = sHService.getSHPostByPostId(SHPostId);
		SHListCommand shCommand = new SHListCommand();

		shCommand.setTitle(shPost.getTitle());
		shCommand.setPrice(shPost.getPrice());
		shCommand.setContent(shPost.getContent());
		shCommand.setImage(shPost.getImage());

		System.out.println(shCommand.getImage());
		model.addAttribute("shPost", shPost);
		model.addAttribute("shCommand", shCommand);

		return shEdit;
	}

	@PostMapping("/update/{SHPostId}")
	public String updateSHPost(@PathVariable("SHPostId") int SHPostId, SHListCommand shCommand, Model model,
			HttpServletRequest request, @RequestParam("files") MultipartFile[] files)
			throws IOException, ServletException {
		// MemberSession memberSession =
		// (MemberSession)request.getAttribute("memberId");
		// String memberId = memberSession.getMemberId();
		// String loginUserId = "dami";

		String fileDir = "C:/absolute/path/to/upload/";

		SecondHandEntity sEntity = sHService.getSHPostByPostId(SHPostId);

		// 여러 파일 업로드 처리
		List<String> uploadedFileNames = sEntity.getImage();

		System.out.println(uploadedFileNames);

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

		// command에 수정한 값들 담은 상태임!! 그걸 Entity에 넣어주는 과정
		SecondHandEntity shPost = sHService.updateSHPost(shCommand, SHPostId, uploadedFileNames);
		model.addAttribute("shPost", shPost);

		return "redirect:/shPost/detail/" + shPost.getSHPostId();
	}

	// 댓글 수정

	// 댓글 삭제

	// 거래자 선택

	// 중고거래의 status 변경

}
