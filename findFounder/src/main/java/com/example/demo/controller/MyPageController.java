package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.ConsultDto;
import com.example.demo.model.dto.CustomerDto;
import com.example.demo.model.dto.LoginReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.entity.Customer;
import com.example.demo.service.AuthService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.MyPageService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class MyPageController {

		@Autowired
	   private CustomerService cusService;
	   private  AuthService authService;
	   private MyPageService Myservice;
	   private  PasswordEncoder encoder;
	   public MyPageController(CustomerService cusService, AuthService authService, 
			   PasswordEncoder encoder,MyPageService Myservice) {
		   this.cusService=cusService;
		   this.authService=authService;
		   this.Myservice=Myservice;
			this.encoder = encoder;
	   }
	   // 회원코드는 JWT토큰 통해서 가져오기.
	   //2024-04-04
	   @Operation(summary = "마이페이지 조회", description = "회원정보")
	   @GetMapping("/mypage")
	   public CustomerDto showMyPage() {
		   CustomerDto dto = Myservice.getCusInfo();
		   return dto;
	   }
	   //2024-04-04
	   @Operation(summary="회원정보 수정",description="수정")
	   @PatchMapping("/mypage")
	   public ResponseEntity<CommonRes> updateMyPage(@RequestBody CustomerDto dto, 
			   @RequestParam(required = false) String nowPW) {
		   	if(nowPW!=null) {
		   		CommonRes res=Myservice.cusUpdate(dto,nowPW); // 현재 비번 친사람 -> 비번 바꾸겠다는사람
		   		return ResponseEntity.ok(res); 
		   	}
		   	else {
		   		CommonRes res=Myservice.cusUpdateExpw(dto);// 현재 비번 안친사람-> 비번 안바꾸겠다는 사람
		   		return ResponseEntity.ok(res); 
		   	}

	   }
}
