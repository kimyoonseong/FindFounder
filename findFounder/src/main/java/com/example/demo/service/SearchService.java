package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.repository.ConsultRepository;
import com.example.demo.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SearchService {
	private ConsultRepository consultRepo;
	private CustomerRepository customerRepo;
	//데이터를 JSON 객체로 변환하기 위해서 사용
    private final ObjectMapper objectMapper;
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Autowired
	public SearchService(ConsultRepository consultRepo, CustomerRepository customerRepo) {
	    
		this.objectMapper = new ObjectMapper();
		this.consultRepo = consultRepo;
	    this.customerRepo = customerRepo;
	}
  //2024-04-15 flask에서 해당 카테고리 업종 가져오기. 
  	 public String bringIndustry(String category) {
  		 	RestTemplate restTemplate = new RestTemplate();
  		 	String url = "http://15.165.229.232:5000/call_industry";
  		 	
  		 	HttpHeaders headers = new HttpHeaders();
  		    headers.setContentType(MediaType.APPLICATION_JSON);
  		 	HttpEntity<String> entity = new HttpEntity<>(category, headers);
  		 	String response = restTemplate.postForObject(url, entity, String.class);
  			return response;
  		}
	public String bringRegion(String category) {
		RestTemplate restTemplate = new RestTemplate();
		 	String url = "http://15.165.229.232:5000/call_region";
		 	
		 	HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		 	HttpEntity<String> entity = new HttpEntity<>(category, headers);
		 	String response = restTemplate.postForObject(url, entity, String.class);
			return response;
	}
}
