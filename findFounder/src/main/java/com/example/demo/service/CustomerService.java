package com.example.demo.service;

import java.util.Optional;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.boot.web.servlet.server.Session.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.CustomerDto;
import com.example.demo.model.entity.Customer;
import com.example.demo.repository.CustomerRepository;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true) // 읽기 전용 메서드
@Service
public class CustomerService {
   private final CustomerRepository customerRepository;
   private final PasswordEncoder passwordEncoder;
   private final AuthenticationManager authenticationManager;
   
   public void checkId(String id) {
      Optional<Customer> customers = customerRepository.findById(id);
      if(customers.isPresent()) {
         throw new Exception("이미 존재하는 id입니다. : " + id);
      }
      
   }
   
  
   
   @Transactional
   public void join(CustomerDto requestDTO) {
      checkId(requestDTO.getCus_id());
      String encodedPassword = passwordEncoder.encode(requestDTO.getCus_pw());
      requestDTO.setCus_pw(encodedPassword);
      try {
         customerRepository.save(requestDTO.toEntity());
      }catch (Exception e) {
         throw new Exception(e.getMessage());
      }
   }
   
   @Transactional
   public String login(CustomerDto requestDTO) {
      
      try {
         UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
            = new UsernamePasswordAuthenticationToken(requestDTO.getCus_id(), requestDTO.getCus_pw());
         // 사용자의 id와 패스워드를 포함한 인증 토큰을 생성함
         Authentication authentication = authenticationManager.authenticate(
            usernamePasswordAuthenticationToken   
         );
         CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
         
         return JwtTokenProvider.create(customUserDetails.getUser());
      }catch (Exception e) {
         throw new Exception("인증되지 않음.");
      }
   }
   



   
   
}

