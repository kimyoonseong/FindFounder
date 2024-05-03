package com.example.demo.controller;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.demo.model.dto.ConsultDto;
import com.example.demo.model.dto.CustomerDto;
import com.example.demo.model.dto.req.PostCreateReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.service.ConsultService;
import com.example.demo.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "4. 컨설팅", description = "send to Flask and send from Flask ")
/*
Consult Controller
1. createConsultation : 컨설팅 설문 답변 저장
2. showConsultation   : 컨설팅 결과 조회
3. Search             : 단순 지역 통계 조회
4. sendToFlask        : Flask와 DTO 통신
5. buyCoupon          : 컨설팅 쿠폰 구매
6. checkCoupons       : 컨설팅 쿠폰 개수 조회
*/
public class ConsultController {
	private ConsultService service;
	private JwtUtil jwtUtil;
	
	@Autowired
    public ConsultController(ConsultService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }
	
	
	//2024-03-29 컨설팅 응답저장 
	//2024-04-08 jwt인증완료
	@Operation(summary = "컨설팅 응답 저장", description = "Consult Save")
	@PostMapping("/api/consultation")
    public ResponseEntity<CommonRes> createConsultation(@CookieValue(value = "Set-Cookie") String jwtToken,
    		@RequestBody ConsultDto dto) {
			CommonRes res=service.consult(jwtToken,dto);
			return ResponseEntity.ok(res);
			
	
	}

	//2024 04-23 컨설트 결과 프론트와 통신
	@Operation(summary = "프론트 컨설팅 결과", description = "Consult result")
	@PostMapping("/api/consultation/result")
	public String showConsultation(@CookieValue(value = "Set-Cookie") String jwtToken) throws JsonProcessingException {
		
        return service.getConsultationById(jwtToken);
		
	}
	
	//2024-03-29 상권 통계 검색 
	@Operation(summary = "통계 검색 ", description = "검색값 flask전송")
	@GetMapping("/api/analysis")
	public String Search (@CookieValue(value = "Set-Cookie") String jwtToken,@RequestParam String region, @RequestParam String industry) {
	
		return service.searchStatic(region,industry);
	}
	
	//2024 04 02 flask로 json dto보내는 메소드
	@Operation(summary = "FlaskTest", description = "Flask 전송 테스트",
			parameters =  {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
	 @PostMapping("/flask")
	    public String sendToFlask(@RequestBody ConsultDto dto) throws JsonProcessingException {
		 
		 	ObjectMapper objectMapper = new ObjectMapper();
		    String jsonDto = objectMapper.writeValueAsString(dto);
		    System.out.println(jsonDto);
		   
		    return service.sendToFlask(jsonDto);
	    }
	
	// 컨설팅 쿠폰 구매
	@Operation(summary = "쿠폰구매", description = "user coupon개수 추가")
	    @PostMapping("/api/consultation/coupon/{count}")
	    public ResponseEntity<CommonRes> buyCoupon(@CookieValue(value = "Set-Cookie") String jwtToken,
	                                         @PathVariable int count
	                                         ) {
	        // 쿠키로부터 토큰을 가져오고 이를 사용하여 쿿ㄱ에 구매를 처리하는 로직
	        CommonRes res = service.buy(jwtToken, count);
	        System.out.println(jwtToken);

	        return ResponseEntity.ok(res);
	    }
	
	// 컨설팅 쿠폰 개수 체크
	@Operation(summary = "쿠폰 수 체크", description = "user coupon개수 체크")
	@GetMapping("/api/consultation/check-coupons")
    public ResponseEntity<String> checkCoupons(@CookieValue(value = "Set-Cookie") String jwtToken) {
		   int currentCoupons = service.checkCoupons(jwtToken);
		   return ResponseEntity.ok("{\"couponCount\":" + currentCoupons + "}");
}
}
