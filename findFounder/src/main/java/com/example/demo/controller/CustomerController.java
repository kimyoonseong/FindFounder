package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.model.dto.CustomerDto;
import com.example.demo.model.dto.LoginReq;
import com.example.demo.model.entity.Customer;
import com.example.demo.service.AuthService;
import com.example.demo.service.CustomerService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class CustomerController {
   
   
   private CustomerService customerService;
   private  AuthService authService;
  
   public CustomerController(CustomerService customerService, AuthService authService) {
	   this.customerService = customerService;
	   this.authService = authService;
   }
   // 회원가입
   @PostMapping("/api/user")
   public ResponseEntity<?> join(@RequestBody @Valid CustomerDto request) throws Exception {
      customerService.join(request);
      return ResponseEntity.ok(200);
   }
   // 이미 가입되어있는 회원인지 확인(ID 중복체크)
   @PostMapping("/api/user/check")
   public ResponseEntity<?> check(@RequestBody @Valid CustomerDto requestDTO) throws Exception {
//   public ResponseEntity<?> check(@RequestBody CustomerDto requestDTO, Error error) throws Exception {
//	   System.out.println("집가고싶다11.");
      String testText = customerService.checkId(requestDTO.getCusId()); 
      return ResponseEntity.ok(200+" "+testText);
   }

   @PostMapping("login")
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
//   // 로그아웃
//   
//   // ID 찾기
//   
//   // 비밀번호 찾기
//   
//   // 비밀번호 재설정
//   

//   
   // 회원 탈퇴
//	2024-04-03 회원 탈퇴
	@GetMapping("/api/user/{cuscode}")
	public ResponseEntity<?> withdraw(@PathVariable int cuscode) throws Exception {
		customerService.withdraw(cuscode);
		return ResponseEntity.ok(200);
		
	}
	
}
