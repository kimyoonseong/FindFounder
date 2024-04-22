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
import com.example.demo.model.dto.req.PostCreateReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.service.ConsultService;
import com.example.demo.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@Tag(name = "4. 컨설팅", description = "send to Flask and send from Flask ")
public class ConsultController {
	private ConsultService service;
	
	@Autowired
    public ConsultController(ConsultService service) {
        this.service = service;
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
	//2024-03-29 컨설팅 결과 
	@Operation(summary = "프론트 컨설팅 결과", description = "Consult result",
			parameters =  {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
	@GetMapping("/api/consultation")
	public ConsultDto showConsultation(@RequestHeader("X-AUTH-TOKEN") String jwtToken) {
		//return  "/api/consultation/"+consultId;
		
		// ConsultationService를 사용하여 consultId에 해당하는 데이터 가져오기
        ConsultDto dto = service.getConsultationById(jwtToken);
        return dto;
		
	
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

	@Operation(summary = "쿠폰구매", description = "user coupon개수 추가")
	    @PostMapping("/api/consultation/coupon/{count}")
	    public ResponseEntity<CommonRes> buyCoupon(@CookieValue(value = "Set-Cookie") String jwtToken,
	                                         @PathVariable int count
	                                         ) {
	        // 쿠키로부터 토큰을 가져오고 이를 사용하여 쿿ㄱ에 구매를 처리하는 로직
	        CommonRes res = service.buy(jwtToken, count);
	        System.out.println(jwtToken);
	        // 응답으로 토큰을 클라이언트에게 전달하기 위해 쿠키를 설정
	        //Cookie cookie = new Cookie("X-AUTH-TOKEN", jwtToken);
	        //cookie.setPath("/"); // 쿠키의 유효 경로 설정 (예시로 전체 경로로 설정)
	        //response.addCookie(cookie); // 응답에 쿠키 추가

	        return ResponseEntity.ok(res);
	    }
	
	 
}
