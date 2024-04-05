package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.model.dto.CustomerDto;
import com.example.demo.model.dto.LoginReq;
import com.example.demo.model.dto.req.CustomerFindPwReq;
import com.example.demo.model.dto.req.CustomerUpdatePwReq;
import com.example.demo.model.entity.Customer;
import com.example.demo.service.AuthService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.EmailService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@Tag(name = "1. 사용자", description = "회원관련 컨트롤러 ")
public class CustomerController {
   
   
   private CustomerService customerService;
   private  AuthService authService;
   private EmailService emailService;
  
   public CustomerController(CustomerService customerService, AuthService authService, EmailService emailService) {
	   this.customerService = customerService;
	   this.authService = authService;
	   this.emailService = emailService;
   }
   // 회원가입
   @PostMapping("/api/user")
   @Operation(summary = "회원가입", description = "회원가입")
   public ResponseEntity<?> join(@RequestBody @Valid CustomerDto request) throws Exception {
      customerService.join(request);
      return ResponseEntity.ok(200);
   }
   // 이미 가입되어있는 회원인지 확인(ID 중복체크)
   @PostMapping("/api/user/check")
   @Operation(summary = "ID 중복체크", description = "ID 중복체크")
   public ResponseEntity<?> check(@RequestBody @Valid CustomerDto requestDTO) throws Exception {
//   public ResponseEntity<?> check(@RequestBody CustomerDto requestDTO, Error error) throws Exception {
//	   System.out.println("집가고싶다11.");
      String testText = customerService.checkId(requestDTO.getCusId()); 
      return ResponseEntity.ok(200+" "+testText);
   }

   @PostMapping("login")
   @Operation(summary = "로그인", description = "로그인")
   public ResponseEntity<String> getMemberProfile(
           @Valid @RequestBody LoginReq request
   ) {
       String token = authService.login(request);
       return ResponseEntity.status(HttpStatus.OK).body(token);
   }
   
//   2024-04-02 로그인
//   @PostMapping(value="/login")
//   public ResponseEntity<?> signIn(@RequestBody CustomerDto request, HttpServletResponse response) {
//      String jwt = customerService.login(request);
//      // "Bearer " 접두사 제거
//      jwt = jwt.replace(JwtTokenProvider.TOKEN_PREFIX, "");
//      
//      // 쿠키 설정
//      Cookie cookie = new Cookie("jwtToken", jwt);
//      cookie.setHttpOnly(true);
//      cookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
//      response.addCookie(cookie);
//      
//      return ResponseEntity.ok().header(JwtTokenProvider.HEADER, jwt)
//            .body(200);
//	   ----------------------------------------------------
//	   로그인
//	   String customer = customerService.login(request);
	   
//	   return ResponseEntity.ok(200 + " " + customer);
	   
//   }
//   
//   // 2024-04-04 ID 찾기 - 이메일 인증
   	 // /api/user?email=
   @PostMapping("/api/user/dispatch")
   @Operation(summary = "이메일보내기", description = "이메일보내기")
   public ResponseEntity<?> sendEmail(@RequestBody String email) throws Exception{
	   
	   String content = "내용을 입력하세요";
	   emailService.sendEmail(email, content);
	   
	   return ResponseEntity.ok(200);
   }

   
   
//   // 2024-04-04 비밀번호 재설정
//   /api/user/pw/{cuscode}?
   @PostMapping("/api/user/pw/{cusid}")
   @Operation(summary = "비밀번호 수정", description = "비밀번호 수정")
   public ResponseEntity<?> updatePw(@PathVariable String cusid, CustomerUpdatePwReq req){
	   if (customerService.updatePw(cusid, req).equals("비밀번호 수정 완료")) {
		   return ResponseEntity.ok(200);
	   }
	   return ResponseEntity.ok(300);
   }

//   
   // 회원 탈퇴
//	2024-04-03 회원 탈퇴
	@DeleteMapping("/api/user/{cuscode}")
	@Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
	public ResponseEntity<?> withdraw(@PathVariable int cuscode) throws Exception {
		customerService.withdraw(cuscode);
		return ResponseEntity.ok(200);
		
	}
	
	// 2024-04-04 비밀번호 찾기
	///api/user?userid=&question=&answer=
	@GetMapping("/api/user")
	@Operation(summary = "비밀번호 찾기 질문", description = "비밀번호 찾기 질문")
	public ResponseEntity<?> findPw(CustomerFindPwReq req){

		// 서비스에 있는 만든 함수 불러서  req를 인자로 넘겨주고
		if (customerService.findCusPw(req).equals("비밀번호 수정 가능")) {
			System.out.println("비밀번호 수정 가능합니다.");
			return ResponseEntity.ok(200);
		}
		return ResponseEntity.ok(300);
	}
	
//  // 2024-04-04 로그아웃
	// /api/user/logout
//	@PostMapping("/api/user/logout")
//	public ResponseEntity<?> logout(@Valid @RequestBody LoginReq request) {
//		String token = authService.logout(request);
//	    return ResponseEntity.status(HttpStatus.OK).body(token);
//		
//	}
	
}
