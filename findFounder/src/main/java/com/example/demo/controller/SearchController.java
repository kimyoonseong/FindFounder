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
import com.example.demo.service.SearchService;
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
@Tag(name = "5. 검색", description = "send to Flask and send from Flask ")
public class SearchController {
	private SearchService service;
	
	@Autowired
    public SearchController(SearchService service) {
        this.service = service;
    }
	
	//2024-04-15 해당 카테고리 상권 가지고오기
		@Operation(summary="Call Industry", description="업종 return")
		@PostMapping("/api/industry")
		public String Bring(@CookieValue(value = "Set-Cookie") String jwtToken,@RequestParam String category) {
			return service.bringIndustry(category);
		}
//2024-04-15 해당 카테고리 상권 가지고오기
		@Operation(summary="Call Region", description="행정동 return")
		@PostMapping("/api/region")
		public String Region(@CookieValue(value = "Set-Cookie") String jwtToken,@RequestParam String category) {
			return service.bringRegion(category);
		}

}
