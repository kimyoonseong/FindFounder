package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.example.demo.model.dto.ConsultDto;
import com.example.demo.model.dto.req.PostCreateReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.service.ConsultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@Tag(name = "컨설팅", description = "send to Flask and send from Flask ")
public class ConsultController {
	private ConsultService service;
	
	@Autowired
    public ConsultController(ConsultService service) {
        this.service = service;
    }
	
	
	//2024-03-29 컨설팅 응답저장
	@Operation(summary = "Consult Save", description = "컨설팅 응답 저장")
	@PostMapping("/api/consultation")
    public ResponseEntity<CommonRes> createConsultation(@RequestBody ConsultDto dto) {
	
			CommonRes res=service.consult(dto);
			//ConsultDto result=service.consult(dto);
			//System.out.println(dto.toString());
			return ResponseEntity.ok(res);
			
	
	}
	//2024-03-29 컨설팅 결과 
	@Operation(summary = "Consult result", description = "프론트 컨설팅 결과")
	@GetMapping("/api/consultation/{consultId}")
	public ConsultDto showConsultation(@PathVariable Integer consultId) {
		//return  "/api/consultation/"+consultId;
		
		// ConsultationService를 사용하여 consultId에 해당하는 데이터 가져오기
        ConsultDto dto = service.getConsultationById(consultId);
        return dto;
		
	
	}
	
	//2024-03-29 상권 통계 
	//flask-> springboot
	@Operation(summary = "analysis", description = "프론트 통계 검색결과")
	@GetMapping("/api/analysis")
	public String showAnalysis( ) {//Model model 이건 프론트에 보내굎을때.
	
		return "해당 결과 화면 보여주기";
	}
	
	//2024-03-29 상권 통계 검색 
	@Operation(summary = "Search", description = "검색값 flask전송")
	@PostMapping("/api/analysis")
	public String Search (@RequestParam String region, @RequestParam String industry) {
	
		return service.searchStatic(region,industry);
	}
	
	//2024 04 02 flask로 json dto보내는 메소드
	@Operation(summary = "FlaskTest", description = "Flask 전송 테스트")
	 @PostMapping("/flask")
	    public String sendToFlask(@RequestBody ConsultDto dto) throws JsonProcessingException {
		 
		 	ObjectMapper objectMapper = new ObjectMapper();
		    String jsonDto = objectMapper.writeValueAsString(dto);
		    System.out.println(jsonDto);
		   
		    return service.sendToFlask(jsonDto);
	    }
	 //2024 04 03 쿠폰 구매
	 @Operation(summary = "쿠폰구매", description = "user coupon개수 추가")
	 @PostMapping("/api/consultation/coupon/{count}")
	 public ResponseEntity<CommonRes> buyCoupon(@PathVariable int count) {
		 CommonRes res=service.buy(count);
		 return ResponseEntity.ok(res);
		 
	 }
	
	 
}
