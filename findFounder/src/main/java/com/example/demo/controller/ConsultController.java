package com.example.demo.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.ConsultDto;
import com.example.demo.service.ConsultService;


@RestController
public class ConsultController {
	private ConsultService service;
	
	
	//2024-03-29 컨설팅 답변       저장 -> 결과화면 
	@PostMapping("/api/consultation")
    public String createConsultation(@ModelAttribute ConsultDto dto) {
		try {
			ConsultDto result=service.consult(dto);
			return "/api/consultation/"+result.getCunsult_id();
		} catch (RuntimeException e) {			
			return "예외발생!!!!!!!!!!!!!";
		}
	}
	//2024-03-29 컨설팅 결과 
	@GetMapping("/api/consultation/{consultId}")
	public String showConsultation(@PathVariable Integer consultId) {
		//return  "/api/consultation/"+consultId;
		return "컨설팅 결과 창입니다";
	}
	
	//2024-03-29 상권 통계 -> flask에 지역, 업종 값넘겨줘야함.
	@GetMapping("/api/analysis")
	public String showAnalysis( ) {//Model model 이건 프론트에 보내굎을때.
	
		return "해당 결과 화면 보여주기";
	}
	//2024-03-29 상권 통계 검색 
	@PostMapping("/api/analysis")
	public String Search (@RequestParam String region, @RequestParam String industry) {
		//-> 서비스에서 외부 api로 flask서버에 넘겨야하나?
		return "/api/analysis";
	}
	
	
}
