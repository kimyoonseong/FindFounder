package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.model.dto.CustomerDto;
import com.example.demo.service.CustomerService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class CustomerController {
   
   @Autowired
   private CustomerService customerService;
   // 회원가입
   @PostMapping("/api/user")
   public ResponseEntity<?> join(@RequestBody @Valid CustomerDto request, Error error) throws Exception {
      customerService.join(request);
      return ResponseEntity.ok(200);
   }
   // 이미 가입되어있는 회원인지 확인(ID 중복체크)
   @PostMapping("/api/user/check")
   public ResponseEntity<?> check(@RequestBody @Valid CustomerDto requestDTO) throws Exception {
//   public ResponseEntity<?> check(@RequestBody CustomerDto requestDTO, Error error) throws Exception {
	   System.out.println("집가고싶다11.");
      String testText = customerService.checkId(requestDTO.getCusId());
      
      return ResponseEntity.ok(200+" "+testText);
   }
}
   
//   // 로그인
//   @PostMapping(value="/login")
//   public ResponseEntity<?> signIn(@RequestBody CustomerDto request, HttpServletResponse response, Error error) {
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
//            .body(ApiUtils.success(null));
//   // 로그아웃
//   
//   // ID 찾기
//   
//   // 비밀번호 찾기
//   
//   // 비밀번호 재설정
//   
//   // 회원정보 수정
//   
//   // 회원정보 상세
//   
//   // 회원 탈퇴
//}
//