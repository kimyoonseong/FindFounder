package com.example.demo.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.demo.repository.ConsultRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor    

@Service
public class MyPageService {
	   private  CustomerRepository customerRepo;

	   private  AuthenticationManager authenticationManager;
	   private  PasswordEncoder encoder;
	   
		@Autowired
		public MyPageService( CustomerRepository customerRepo,
				AuthenticationManager authenticationManager, PasswordEncoder encoder) {
		    this.customerRepo = customerRepo;

			this.authenticationManager = authenticationManager;
			this.encoder = encoder;
			 if (encoder == null) {
		            throw new IllegalStateException("PasswordEncoder가 주입되지 않았습니다.");
		        }
		    }		
		public CustomerDto getCusInfo() {
			// TODO Auto-generated method stub
			Optional<Customer> customer = customerRepo.findById(3);
			if(customer.isPresent()) {
				Customer cus=customer.get();
				CustomerDto dto= cus.toDto();
				return dto;
			}
			else{
				throw new NotFoundException("Consultation not found with id: " + 3);
			}
			
		}
		public CommonRes cusUpdate(CustomerDto dto,String nowPW) {
			// TODO Auto-generated method stub
			Optional<Customer> customer = customerRepo.findById(7);//없을수도 있기 때문에 optional
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
	 
		public CommonRes cusUpdateExpw(CustomerDto dto) {
			Optional<Customer> customer = customerRepo.findById(6);//없을수도 있기 때문에 optional
			if(customer.isPresent()) {
				Customer cus=customer.get();
				
				if (dto.getCusEmail() != null) cus.setCusEmail(dto.getCusEmail());
		        if (dto.getCusName() != null) cus.setCusName(dto.getCusName());
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
    
}
