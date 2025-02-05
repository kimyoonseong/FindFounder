package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.ConsultDto;
import com.example.demo.model.dto.CustomerDto;
import com.example.demo.model.dto.LoginReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.entity.Consult;
import com.example.demo.model.entity.Customer;
import com.example.demo.service.AuthService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.MyPageService;
import com.example.demo.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "5. 마이페이지", description = "마이페이지 컨트롤러 ")
/*
MyPage Controller
1. showMyPage         : 마이페이지 조회
2. updateMyPage       : 회원정보 수정
3. consultHistory     : 컨설트 내역 조회
*/
public class MyPageController {

		@Autowired
	   private CustomerService cusService;
	   private  AuthService authService;
	   private MyPageService Myservice;
	   private  PasswordEncoder encoder;
	   private JwtUtil jwtUtil;
	  
	   
	   public MyPageController(CustomerService cusService, AuthService authService, 
			   PasswordEncoder encoder,MyPageService Myservice, JwtUtil jwtUtil) {
		   this.cusService=cusService;
		   this.authService=authService;
		   this.Myservice=Myservice;
			this.encoder = encoder;
			this.jwtUtil = jwtUtil;
	   }
	   // 회원코드는 JWT토큰 통해서 가져오기.
	   //2024-04-04
	   @Operation(summary = "마이페이지 조회", description = "회원정보")
	   @GetMapping("/mypage")
	   public CustomerDto showMyPage(@CookieValue(name = "Set-Cookie", required = false) String jwtToken) {
		   log.info("[Log] 마이페이지 조회 : {}", jwtUtil.getCusId(jwtToken));
		   CustomerDto dto = Myservice.getCusInfo(jwtToken);
		   return dto;
	   }
	   //2024-04-04
	   @Operation(summary="회원정보 수정",description="수정")
	   @PatchMapping("/mypage")
	   public ResponseEntity<CommonRes> updateMyPage(@CookieValue(name = "Set-Cookie", required = false) String jwtToken,
			   @RequestBody CustomerDto dto, 
			   @RequestParam(required = false) String nowPW) {
		   log.info("[Log] 회원정보 수정 : {}", jwtUtil.getCusId(jwtToken));

		   	if(nowPW!=null) {
		   		CommonRes res=Myservice.cusUpdate(jwtToken,dto,nowPW); // 현재 비번 친사람 -> 비번 바꾸겠다는사람
		   		return ResponseEntity.ok(res); 
		   	}
		   	else {
		   		CommonRes res=Myservice.cusUpdateExpw(jwtToken,dto);// 현재 비번 안친사람-> 비번 안바꾸겠다는 사람
		   		return ResponseEntity.ok(res); 
		   	}

	   }
	   
	   
	   //2024-04-08 컨설팅 내역 모델내용을 불러와야한
	   @Operation(summary="컨설팅 내역",description="히스토리 내역들 확인")
	   @GetMapping("/mypage/history")   
	   public List<Consult> consultHistory(@CookieValue(name = "Set-Cookie", required = false) String jwtToken) {
		   log.info("[Log] 컨설팅 내역 확인 : {}", jwtUtil.getCusId(jwtToken));
		   List<Consult> con=Myservice.getConsultHistory(jwtToken);
		   return con;
	   }
	   
}
