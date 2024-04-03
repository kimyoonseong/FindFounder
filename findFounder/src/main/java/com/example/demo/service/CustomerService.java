package com.example.demo.service;

import java.util.Optional;

//import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.boot.web.servlet.server.Session.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.CustomerDto;
import com.example.demo.model.dto.req.CustomerFindPwReq;
import com.example.demo.model.entity.Customer;
import com.example.demo.repository.ConsultRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.QuestionRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor    
@Transactional(readOnly = true) // 읽기 전용 메서드
@Service
public class CustomerService {
   private final CustomerRepository customerRepository;
   private final PasswordEncoder passwordEncoder;
   private final AuthenticationManager authenticationManager;
   private final ConsultRepository consultRepository;
   private final QuestionRepository questionRepository;
   
   public String checkId(String cusId) throws Exception {
	   System.out.println("집가고싶다.");
	      Customer customer = customerRepository.findByCusId(cusId).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));
//	      if(customer.getCusId().equals(cusId)) {
//	         throw new Exception("이미 존재하는 id입니다. : " + cusId);
//	      }
	      return "있어요";
	      
   }
	   
   
  
   
   @Transactional
   public void join(CustomerDto requestDTO) throws Exception {
      checkId(requestDTO.getCusId());
      String encodedPassword = passwordEncoder.encode(requestDTO.getCusPw());
      requestDTO.setCusPw(encodedPassword);
      try {
         customerRepository.save(requestDTO.toEntity());
      }catch (Exception e) {
         throw new Exception(e.getMessage());
      }
   }

   @Transactional
   public String withdraw(int cuscode) {
	   // comment 삭제
	   
	   // post 삭제
	   
	   // consult 삭제
	   consultRepository.deleteAllByCustomer_CusCode(cuscode);
	   // 회원 삭제
	   customerRepository.deleteById(cuscode);
	   return "회원 탈퇴 완료";
   }
   
   @Transactional
   public String findCusPw(CustomerFindPwReq req1) {
	   
	   // req : id, questionid, answer
	   // id가 실제로 존재하는 사용자인지 체크 customer
	   Customer customer = customerRepository.findByCusId(req1.getCusId()).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));
	   // customer q, answer 가져와서

	   // req question, answer가 일치하는지
	   if (customer.getQuestion().getCusQuestionId() == req1.getCusQuestionId()) {
		   if (customer.getCusPwAnswer().equals(req1.getCusPwAnswer()) ) {
			   return "비밀번호 수정 가능";
		   }
		   
	   }
	   return "비밀번호 수정 불가능";
	   
	   //일치할 때 return 문자열, 일치하지 않을 떄 문장열 return
	   
   }
//   
//   @Transactional
//   public String login(CustomerDto requestDTO) {
//      
//      try {
//         UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
//            = new UsernamePasswordAuthenticationToken(requestDTO.getCus_id(), requestDTO.getCus_pw());
//         // 사용자의 id와 패스워드를 포함한 인증 토큰을 생성함
//         Authentication authentication = authenticationManager.authenticate(
//            usernamePasswordAuthenticationToken   
//         );
//         CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
//         
//         return JwtTokenProvider.create(customUserDetails.getUser());
//      }catch (Exception e) {
//         throw new Exception("인증되지 않음.");
//      }
//   }
//   
//
//
//
//   
//   
}


