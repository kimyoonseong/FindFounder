package com.example.demo.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.boot.web.servlet.server.Session.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import com.example.demo.model.dto.ConsultDto;
import com.example.demo.model.dto.CustomerDto;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.entity.Consult;
import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Question;
import com.example.demo.repository.ConsultRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor    

@Service
public class MyPageService {
	   private  CustomerRepository customerRepo;
	   private  ConsultRepository consultRepo;
	   private  AuthenticationManager authenticationManager;
	   private  PasswordEncoder encoder;
	   private  QuestionRepository questionRepo;
	   @Value("${jwt.secret}")
	    private String secretKey;
	   
		@Autowired
		public MyPageService( CustomerRepository customerRepo,  ConsultRepository consultRepo,
				AuthenticationManager authenticationManager, PasswordEncoder encoder
				, QuestionRepository questionRepo) {
		    this.customerRepo = customerRepo;
		    this.consultRepo= consultRepo;
			this.authenticationManager = authenticationManager;
			this.encoder = encoder;
			 if (encoder == null) {
		            throw new IllegalStateException("PasswordEncoder가 주입되지 않았습니다.");
		        }
			 this.questionRepo = questionRepo;
		    }		
		public CustomerDto getCusInfo(String jwtToken) {
			 // JWT 토큰을 해독하여 클레임을 추출
	        Claims claims = Jwts.parser()
	                .setSigningKey(secretKey)
	                .parseClaimsJws(jwtToken)
	                .getBody();
	        
	        // 클레임에서 cusId 값을 가져옴
	        Integer cusCode = (Integer) claims.get("cusCode");

		 // 가져온 cusCode를 사용하여 사용자의 정보를 조회
		    Optional<Customer> customer = customerRepo.findById(cusCode);

			// TODO Auto-generated method stub
			//Optional<Customer> customer = customerRepo.findById(6);
			if(customer.isPresent()) {
				Customer cus=customer.get();
				CustomerDto dto= cus.toDto();
				return dto;
			}
			else{
				throw new NotFoundException("Consultation not found with id: " );
			}
			
		}
		public CommonRes cusUpdate(String jwtToken ,CustomerDto dto,String nowPW) {
			 // JWT 토큰을 해독하여 클레임을 추출
	        Claims claims = Jwts.parser()
	                .setSigningKey(secretKey)
	                .parseClaimsJws(jwtToken)
	                .getBody();
	        
	        // 클레임에서 cusId 값을 가져옴
	        Integer cusCode = (Integer) claims.get("cusCode");
			// TODO Auto-generated method stub
			Optional<Customer> customer = customerRepo.findById(cusCode);//없을수도 있기 때문에 optional
			Optional<Question> question = questionRepo.findById(dto.getCusQuestionId());
		    Question que = question.get();
			if(customer.isPresent()) {
				Customer cus=customer.get();
				
				if(encoder.matches(nowPW, cus.getCusPw())) {//인코딩된 비번과 일치한다면.
					//System.out.println("---------디버깅--------------------------");
					if (dto.getCusEmail() != null) cus.setCusEmail(dto.getCusEmail());
			        if (dto.getCusName() != null) cus.setCusName(dto.getCusName());
			
			        if (dto.getCusPw() != null) {
			            String encodedNewPassword = encoder.encode(dto.getCusPw());
			            cus.setCusPw(encodedNewPassword);//인코딩된 비번으로 변경.
			        }
			        if (dto.getCusPwAnswer() != null) cus.setCusPwAnswer(dto.getCusPwAnswer());
			        if (dto.getCusQuestionId() != null) cus.setQuestion(que);
			        customerRepo.saveAndFlush(cus);
					CommonRes commonRes = CommonRes.builder().code(200).msg("회원정보 수정 완료").build();
					return commonRes;
				}else {
				    // 현재 비밀번호가 일치하지 않는 경우
				    CommonRes commonRes = CommonRes.builder().code(400).msg("현재 비밀번호가 일치하지 않습니다.").build();
				    return commonRes;
				}
			}
			else{
				CommonRes commonRes = CommonRes.builder().code(200).msg("회원이 존재하지 않습니다.").build();
				return commonRes;
			}
		
		}
	 
		public CommonRes cusUpdateExpw(String jwtToken,CustomerDto dto) {
			 // JWT 토큰을 해독하여 클레임을 추출
	        Claims claims = Jwts.parser()
	                .setSigningKey(secretKey)
	                .parseClaimsJws(jwtToken)
	                .getBody();
	        
	        // 클레임에서 cusId 값을 가져옴
	        Integer cusCode = (Integer) claims.get("cusCode");
	        Optional<Question> question = questionRepo.findById(dto.getCusQuestionId());
	        Question que = question.get();
			// TODO Auto-generated method stub
			Optional<Customer> customer = customerRepo.findById(cusCode);//없을수도 있기 때문에 optional
			if(customer.isPresent()) {
				Customer cus=customer.get();
				
				if (dto.getCusEmail() != null) cus.setCusEmail(dto.getCusEmail());
		        if (dto.getCusName() != null) cus.setCusName(dto.getCusName());
		        if (dto.getCusQuestionId() != null) cus.setQuestion(que);
		        if (dto.getCusPwAnswer() != null) cus.setCusPwAnswer(dto.getCusPwAnswer());
		        customerRepo.saveAndFlush(cus);
				CommonRes commonRes = CommonRes.builder().code(200).msg("회원정보 수정 완료").build();
				return commonRes;
				
				
			}
			else{
				CommonRes commonRes = CommonRes.builder().code(200).msg("회원이 존재하지 않습니다.").build();
				return commonRes;
			}
		}
		//2024-04-08해당 cuscode의 consult 내역들.
		public List<Consult> getConsultHistory(String jwtToken) {
			 // JWT 토큰을 해독하여 클레임을 추출
	        Claims claims = Jwts.parser()
	                .setSigningKey(secretKey)
	                .parseClaimsJws(jwtToken)
	                .getBody();
	        
	        // 클레임에서 cusId 값을 가져옴
	        Integer cusCode = (Integer) claims.get("cusCode");
			// TODO Auto-generated method stub
			//Optional<Customer> customer = customerRepo.findById(cusCode);//없을수도 있기 때문에 optional
	        System.out.println(cusCode);
			List<Consult> consultation= consultRepo.findByCusCode(cusCode);
			// TODO Auto-generated method stub
			if (!consultation.isEmpty()) {
			    // 리스트가 비어있지 않은 경우
			    // 처리할 로직 작성
			    return consultation;
			}
			else{
				throw new NotFoundException("Consultation not found with id: " );
			}
		}
    
}
