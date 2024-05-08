package com.example.demo.controller;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.model.dto.CustomerDto;
import com.example.demo.model.dto.CustomerJoinDto;
import com.example.demo.model.dto.LoginReq;
import com.example.demo.model.dto.req.CustomerFindPwReq;

import com.example.demo.model.dto.req.CustomerUpdatePwReq;
import com.example.demo.model.dto.req.PostCreateReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.dto.res.EmailRes;
import com.example.demo.model.dto.res.LoginRes;
import com.example.demo.model.entity.Customer;
import com.example.demo.service.AuthService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.EmailService;
import com.example.demo.util.JwtUtil;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RestController
@Tag(name = "1. 사용자", description = "회원관련 컨트롤러 ")
/*
Customer Controller
1. join               : 회원가입
2. getMemberProfile   : 로그인
3. sendEmail          : ID 찾기(이메일 인증)
4. updatePw           : 비밀번호 재설정
5. withdraw           : 회원탈퇴
6. findPw             : 비밀번호 찾기 질문
7. logout             : 로그아웃
*/
public class CustomerController {
   
   
   private CustomerService customerService;
   private  AuthService authService;
   private EmailService emailService;
   private JwtUtil jwtUtil;
  
   public CustomerController(CustomerService customerService, AuthService authService, EmailService emailService, JwtUtil jwtUtil) {
	   this.customerService = customerService;
	   this.authService = authService;
	   this.emailService = emailService;
	   this.jwtUtil = jwtUtil;
   }
   // 회원가입
   @PostMapping("/api/user")
   @Operation(summary = "회원가입", description = "회원가입")
   public ResponseEntity<CommonRes> join(@RequestBody @Valid CustomerJoinDto request) throws Exception {
	   log.info("[Log] 회원가입 : {}", request.getCusId());
	 
      CommonRes res = customerService.join(request);
      return ResponseEntity.ok(res);
   }
   
   // 로그인
   @PostMapping("login")
   @Operation(summary = "로그인", description = "로그인")
   public ResponseEntity<LoginRes> getMemberProfile(
		   @RequestBody LoginReq dto, HttpServletResponse response
		   
		   ) {		
	   log.info("[Log] 로그인 : {}", dto.getCusId());
       LoginRes res = authService.login(dto);
       String token = res.getToken();

       
       Cookie cookie = new Cookie("Set-Cookie", token);
	   cookie.setHttpOnly(false); // JavaScript를 통한 접근 방지
       cookie.setPath("/"); // 전체 경로에 대해 쿠키 유효
       cookie.setMaxAge(60*60*60);
       // 쿠키의 보안 설정 (HTTPS 환경에서만 쿠키를 전송하도록 설정)
       // 개발 환경이 아닌 경우에만 Secure 플래그를 활성화해야 할 수 있습니다.
       cookie.setSecure(false);
       
       // 응답에 쿠키 추가
       response.addCookie(cookie);

       return ResponseEntity.ok(res);
   }

   
//   // 2024-04-04 ID 찾기 - 이메일 인증
   	 // /api/user?email=
   @PostMapping("/api/user/dispatch")
   @Operation(summary = "이메일보내기", description = "이메일보내기")
   public ResponseEntity<CommonRes> sendEmail(@RequestBody EmailRes emailDto) throws Exception{
	   
	   
	   System.out.println(emailDto.toString());
	   CommonRes res = emailService.sendEmail(emailDto.getEmail());
	   log.info("[Log] 아이디 찾기 : {}", emailDto.getEmail());
	   return ResponseEntity.ok(res);
   }

   
   
//   // 2024-04-04 비밀번호 재설정
//   /api/user/pw/{cuscode}?
   @PostMapping("/api/user/pw")
   @Operation(summary = "비밀번호 수정", description = "비밀번호 수정")
   public ResponseEntity<CommonRes> updatePw(@RequestBody CustomerUpdatePwReq req){
	   
	   log.info("[Log] 비밀번호 재설정 : {}", req.getCusId());
	   System.out.println(req.getCusId() + " ##" + req.getCusPw());
	   CommonRes res;
	   if (customerService.updatePw( req).equals("비밀번호 수정 완료")) {
		   res = CommonRes.builder().code(200).msg("비밀번호 수정이 완료되었습니다.").build();
		   return ResponseEntity.ok(res);
	   }
	   else {
		   res = CommonRes.builder().code(300).msg("비밀번호 수정이 불가능합니다.").build();
	   }
	   return ResponseEntity.ok(res);
   }

//   
   // 회원 탈퇴
//	2024-04-03 회원 탈퇴
	@DeleteMapping("/api/user")
	@Operation(summary = "회원 탈퇴", description = "회원 탈퇴",
			parameters =  {
//                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
                    
            })
	public ResponseEntity<CommonRes> withdraw(HttpServletRequest request, HttpServletResponse response,
				  @CookieValue(name = "Set-Cookie", required = false) String jwtToken) throws Exception {
		
		log.info("[Log] 회원 탈퇴 : {}", jwtUtil.getCusId(jwtToken));
		System.out.println("@@@@@@@@@@@@@@@@" + jwtToken);
		int cusCode = jwtUtil.getCusCode(jwtToken);
		
		CommonRes res = customerService.withdraw(cusCode);
		
		Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Set-Cookie")) {
                	System.out.println("쿠키찾음");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
		return ResponseEntity.ok(res);
		
	}
	
	// 2024-04-04 비밀번호 찾기 질문
	///api/user?userid=&question=&answer=
	@GetMapping("/api/user")
	@Operation(summary = "비밀번호 찾기 질문", description = "비밀번호 찾기 질문")
	public ResponseEntity<CommonRes> findPw(CustomerFindPwReq req){
		log.info("[Log] 비밀번호 찾기 질문, 답 : {}", req.getCusId());
		CommonRes res;
		// 서비스에 있는 만든 함수 불러서  req를 인자로 넘겨주고
		if (customerService.findCusPw(req).equals("비밀번호 수정 가능")) {
			res = CommonRes.builder().code(200).msg("비밀번호 수정 가능합니다.").build();
		}
		else {
			res = CommonRes.builder().code(300).msg("비밀번호 수정 불가능합니다.").build();
	
		}
		return ResponseEntity.ok(res);
	}

	
	// 2024-04-14 로그아웃


	    @PostMapping("/api/user/logout")
	    @Operation(summary = "로그아웃", description = "로그아웃")
	    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
	        // 쿠키 만료 시간을 0으로 설정하여 쿠키를 제거합니다.
	    	
	        Cookie[] cookies = request.getCookies();

	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if (cookie.getName().equals("Set-Cookie")) {
	                	System.out.println("쿠키찾음");
	                	log.info("[Log] 로그아웃 : {}", jwtUtil.getCusId(cookie.getValue()));
	                    cookie.setMaxAge(0);
	                    cookie.setPath("/");
	                    response.addCookie(cookie);
	                }
	                
	            }
	        }
	        return ResponseEntity.ok("로그아웃되었습니다.");
	    }

	
}