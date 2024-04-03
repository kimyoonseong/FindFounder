package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.webjars.NotFoundException;

import com.example.demo.model.dto.ConsultDto;
import com.example.demo.model.entity.Consult;
import com.example.demo.model.entity.Customer;
import com.example.demo.repository.ConsultRepository;
import com.example.demo.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class ConsultService {
	private ConsultRepository consultRepo;
	private CustomerRepository customerRepo;
	//데이터를 JSON 객체로 변환하기 위해서 사용
    private final ObjectMapper objectMapper;
    
	@Autowired
	public ConsultService(ConsultRepository consultRepo, CustomerRepository customerRepo) {
	    
		this.objectMapper = new ObjectMapper();
		this.consultRepo = consultRepo;
	    this.customerRepo = customerRepo;
	}
	//2024-03-29 고객 답변 db에 저장
	public ConsultDto consult(ConsultDto dto) {
		Consult consult=dto.toEntity();
		// 고객의 보유 쿠폰 갯수 확인
		
		//Customer customer = customerRepo.findByCusCode(consult.getCustomer().getCusCode());
		//		if (customer != null && customer.getCusCupons() >= 1) {
		//            // 쿠폰 갯수가 1개 이상이면 상담 정보를 저장
		//            consultRepo.save(consult);
		//            
		//            // 고객의 컨설팅 참여 여부를 true로 변경
		//            if(!customer.getCusIsConsult()) customer.setCusIsConsult(true);
		//            customerRepo.save(customer); // 변경된 내용을 저장
		//        } else {
		//            throw new RuntimeException("고객의 보유 쿠폰 갯수가 1개 미만입니다.");
		//        }
		
		consult = consultRepo.save(consult); // Consult 엔티티를 DB에 저장하고 저장된 엔티티를 반환
		System.out.println(consult.toString());
        // 저장된 Consult 엔티티의 id를 ConsultDto에 설정
        //dto.setId(consult.getId());
        
        // 필요하다면 저장된 엔티티의 다른 정보를 ConsultDto에 설정
		return dto;
	}
	
//	public ConsultDto getConsultationById(Integer consultId) {
//        // 데이터베이스 또는 다른 서비스에서 데이터를 가져와서 ConsultDto 객체 생성 후 반환
//        ConsultDto dto = new ConsultDto();
//      
//		return dto;
//	}
	 public ConsultDto getConsultationById(Integer consultId) {
		 // 데이터베이스에서 consultId에 해당하는 데이터를 가져오기
		    Optional<Consult> consultationOptional = consultRepo.findById(consultId);
		    
		    // ConsultationEntity가 존재하는지 확인
		    if (consultationOptional.isPresent()) {
		    
		        Consult consultationEntity = consultationOptional.get();
		        ConsultDto dto = consultationEntity.toDto();
		       // System.out.println(dto.toString());
		        return dto;
		    } else {
		        throw new NotFoundException("Consultation not found with id: " + consultId);
		    }
	    }
	 @Transactional
	    public String sendToFlask(String jsonDto) throws JsonProcessingException {
	        RestTemplate restTemplate = new RestTemplate();

	        // 헤더를 JSON으로 설정함
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        // Flask 서버로 전송할 데이터와 헤더를 가진 HttpEntity 객체 생성
	        //jsonDto를 JSON 형식으로 변환하여 HTTP 요청의 본문으로 보냄. 이를 통해 서버에 JSON 형식의 데이터를 전달할 수 있습니다.
	        HttpEntity<String> entity = new HttpEntity<>(jsonDto, headers);

	        // 실제 Flask 서버와 연결하기 위한 URL
	        String url = "http://127.0.0.1:5000/receive_string";

	        // Flask 서버로 데이터를 전송하고 받은 응답 값을 반환
	        return restTemplate.postForObject(url, entity, String.class);
	        //return "실행됨";
	    }
	 @Transactional
	public String searchStatic(String region, String industry) {
		// TODO Auto-generated method stub
		 	RestTemplate restTemplate = new RestTemplate();
		 	String url = "http://127.0.0.1:5000/receive_string";
		 	
		 // 헤더를 JSON으로 설정함
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        String SearchString="{\"preferIndustry\": \"" + industry + "\", \"preferLoc\": \"" + region + "\"}";
	        HttpEntity<String> entity = new HttpEntity<>(SearchString, headers);
	     // REST 호출
	        String response = restTemplate.postForObject(url, entity, String.class);

	        // 응답 반환
	        return response;
	}

	  
}


