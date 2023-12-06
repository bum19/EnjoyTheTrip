package com.ssafy.web.member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.web.member.model.MemberDto;
import com.ssafy.web.member.model.service.MemberService;
import com.ssafy.web.member.model.service.OAuthService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class MemberController {

	private final Logger logger = LoggerFactory.getLogger(MemberController.class);

	private MemberService memberService;

	@Autowired
	OAuthService oAuthService;

	public MemberController(MemberService memberService) {
		super();
		this.memberService = memberService;
	}

	@GetMapping("/emailCheck/{email}")
	@ResponseBody
	public ResponseEntity<String> emailCheck(@PathVariable("email") String userId) throws Exception {
		logger.debug("emailCheck userid : {}", userId);
		int cnt = memberService.emailCheck(userId);
		return new ResponseEntity(cnt, HttpStatus.OK);
	}

	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody MemberDto memberDto) throws Exception {
		logger.debug("memberDto info : {}", memberDto);
		System.out.println(memberDto);
		memberService.joinMember(memberDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody MemberDto memberDto, HttpServletRequest request) {
		System.out.println("login gogogogo");
		logger.debug("login memberDto : {}", memberDto);

		// Referer가 일치하는지 확인
		String refererFromRequest = request.getHeader("Referer");
		if ("http://localhost:5173/".equals(refererFromRequest)) {
			try {
				boolean canLogin = memberService.loginCheck(memberDto.getEmail());
				if (canLogin) {
					memberDto = memberService.loginMember(memberDto);
					if (memberDto != null) {
						HttpSession session = request.getSession();
						session.setAttribute("userInfo", memberDto);
						return new ResponseEntity<MemberDto>(memberDto, HttpStatus.OK);
					} else {
						return new ResponseEntity<String>("아이디 또는 비밀번호 확인 후 다시 로그인하세요!", HttpStatus.BAD_REQUEST);
					}
				} else {
					return new ResponseEntity<String>("아이디가 잠겨있습니다.", HttpStatus.BAD_REQUEST);
				}
			} catch (Exception e) {
				return exceptionHandling(e);
			}
		} else {
			return new ResponseEntity<String>("잘못된 접근입니다.", HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null)
			session.invalidate();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 질문받아오기
	@GetMapping("/question")
	public ResponseEntity<?> getQuestion(@RequestParam String email) {
		System.out.println("email  : " + email);
		String question = memberService.getQuestion(email);
		System.out.println(question);
		if (question != null)
			return new ResponseEntity<String>(question, HttpStatus.OK);
		else
			return new ResponseEntity<String>(question, HttpStatus.NOT_FOUND);
	}

	// 비밀번호 재설정
	@PostMapping("/updatePW")
	public ResponseEntity<?> updatePW(@RequestBody MemberDto memberDto) {
		log.info("updatePW MemberDto - {}", memberDto);
		try {
			boolean isUpdateDone = memberService.updatePW(memberDto);
			if (isUpdateDone)
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			else
				return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	// 내정보가져오기
	@GetMapping("/myPage")
	public ResponseEntity<?> findMember(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		try {
			MemberDto memberDto = (MemberDto) session.getAttribute("userInfo");
			memberDto = memberService.getMember(memberDto.getEmail());
			MemberDto responseMemberDto = new MemberDto();
			responseMemberDto.setUserName(memberDto.getUserName());
			responseMemberDto.setQuestion(memberDto.getQuestion());
			responseMemberDto.setAnswer(memberDto.getAnswer());
			return new ResponseEntity<MemberDto>(responseMemberDto, HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	// 내정보수정
	@PostMapping("/update")
	public ResponseEntity<?> update(@RequestBody MemberDto memberDto, HttpServletRequest request) {
		log.info("updateMember MemberDto - {}", memberDto);
		HttpSession session = request.getSession(false);
		if (session == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		try {
			memberDto.setEmail(((MemberDto) session.getAttribute("userInfo")).getEmail());
			memberService.updateMember(memberDto);
			String userName = memberDto.getUserName();
			memberDto = (MemberDto) session.getAttribute("userInfo");
			memberDto.setUserName(userName);
			session.setAttribute("userInfo", memberDto);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	// 내정보삭제
	@GetMapping("/delete")
	public ResponseEntity<?> delete(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null)
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		try {
			memberService.deleteMember(((MemberDto) session.getAttribute("userInfo")).getEmail());
			session.invalidate();
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	private ResponseEntity<?> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("로그인 중 문제 발생!!!", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
