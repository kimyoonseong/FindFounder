package com.example.demo.controller;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@Tag(name = "1. 사용자", description = "회원관련 컨트롤러 ")
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
      CommonRes res = customerService.join(request);
      return ResponseEntity.ok(res);
   }
   
   
   // 이미 가입되어있는 회원인지 확인(ID 중복체크)
//   @PostMapping("/api/user/check")
//   @Operation(summary = "ID 중복체크", description = "ID 중복체크")
//   public ResponseEntity<CommonRes> check(@RequestBody @Valid CustomerJoinDto request) throws Exception {
////   public ResponseEntity<?> check(@RequestBody CustomerDto requestDTO, Error error) throws Exception {
////	   System.out.println("집가고싶다11.");
//      return ResponseEntity.ok(customerService.checkId(request.getCusId()));
//   }

   @PostMapping("login")
   @Operation(summary = "로그인", description = "로그인")
   public ResponseEntity<LoginRes> getMemberProfile(
		   @RequestBody LoginReq dto
) {
       LoginRes res = authService.login(dto);
//       return ResponseEntity.status(HttpStatus.OK).body(token);
       return ResponseEntity.ok(res);
   }
   

//   // 2024-04-04 ID 찾기 - 이메일 인증
   	 // /api/user?email=
   @PostMapping("/api/user/dispatch")
   @Operation(summary = "이메일보내기", description = "이메일보내기")
   public ResponseEntity<CommonRes> sendEmail(@RequestBody String email) throws Exception{
	   
	   
	   CommonRes res = emailService.sendEmail(email);
	   
	   return ResponseEntity.ok(res);
   }

   
   
//   // 2024-04-04 비밀번호 재설정
//   /api/user/pw/{cuscode}?
   @PostMapping("/api/user/pw/{cusid}")
   @Operation(summary = "비밀번호 수정", description = "비밀번호 수정")
   public ResponseEntity<CommonRes> updatePw(@PathVariable String cusid, CustomerUpdatePwReq req){
	   CommonRes res;
	   if (customerService.updatePw(cusid, req).equals("비밀번호 수정 완료")) {
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
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
	public ResponseEntity<CommonRes> withdraw(@RequestHeader("X-AUTH-TOKEN") String jwtToken) throws Exception {
		
		int cusCode = jwtUtil.getCusCode(jwtToken);
		
		CommonRes res = customerService.withdraw(cusCode);
		return ResponseEntity.ok(res);
		
	}
	
	// 2024-04-04 비밀번호 찾기 질문
	///api/user?userid=&question=&answer=
	@GetMapping("/api/user")
	@Operation(summary = "비밀번호 찾기 질문", description = "비밀번호 찾기 질문")
	public ResponseEntity<CommonRes> findPw(CustomerFindPwReq req){
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
	
//  // 2024-04-04 로그아웃
	// /api/user/logout
//	@PostMapping("/api/user/logout")
//	public ResponseEntity<?> logout(@Valid @RequestBody LoginReq request) {
//		String token = authService.logout(request);
//	    return ResponseEntity.status(HttpStatus.OK).body(token);
//		
//	}
	
}
