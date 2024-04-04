package com.example.demo.service;

import java.util.NoSuchElementException;
import java.util.Optional;

//import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.boot.web.servlet.server.Session.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.dto.CustomerDto;
import com.example.demo.model.dto.req.CustomerFindPwReq;
import com.example.demo.model.dto.req.CustomerUpdatePwReq;
import com.example.demo.model.dto.req.PostCreateReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Post;
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
	   
	      Customer customer = findCustomer(cusId);
//	      if(customer.getCusId().equals(cusId)) {
//	         throw new Exception("이미 존재하는 id입니다. : " + cusId);
//	      }
	      return "있어요";
	      
   }
	   
   
  
   
   @Transactional
   public void join(CustomerDto requestDTO) throws Exception {
//      checkId(requestDTO.getCusId());
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
   public String findCusPw(CustomerFindPwReq req) {
	   
	   // req1 : id, questionid, answer
	   // id가 실제로 존재하는 사용자인지 체크 customer
	   Customer customer = findCustomer(req.getCusId());
	  // customer q, answer 가져와서
	  // req1 question, answer가 일치하는지
	  //일치할 때 return 문자열, 일치하지 않을 떄 문장열 return
	   if (customer.getQuestion().getCusQuestionId() == req.getCusQuestionId()) {
		   if (customer.getCusPwAnswer().equals(req.getCusPwAnswer()) ) {
			   return "비밀번호 수정 가능";
		   }
		   
	   }
	   return "비밀번호 수정 불가능";
   }
   
//	public CommonRes updatePost(int postid, PostCreateReq postDto) {
//		Post post = postRepo.findById(postid).orElseThrow(()->
//		new IllegalArgumentException("해당 게시글이 없습니다."));
//		post.setPostTitle(postDto.getPostTitle());
//		post.setPostContent(postDto.getPostContent());
//		postRepo.saveAndFlush(post);
//		CommonRes commonRes = CommonRes.builder().code(200).msg("게시글 수정 완료").build();
//		return commonRes;
//	}
//	
   public Customer findCustomer(int cusCode) {
	   Customer customer = customerRepository.findById(cusCode).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 없습니다."));
	   return customer;
   }
   
   public Customer findCustomer(String cusId) {
	   Customer customer = customerRepository.findByCusId(cusId).orElseThrow(() -> new NoSuchElementException("해당하는 유저가 없습니다."));
	   return customer;
   }
   
   
   @Transactional
   public String updatePw(String cusId, CustomerUpdatePwReq req) {
	   // 비밀번호 찾기에서 비밀번호 수정 가능이 뜨면
	   // 입력받았던 아이디에 일치하는 회원을 찾아서 
	   // 비밀번호 변경 가능하게 해줌.
	 
	   Customer customer = findCustomer(cusId);
	   String encodedPassword = passwordEncoder.encode(customer.getCusPw());
	   customer.setCusPw(encodedPassword);
	   customerRepository.save(customer);
	 
	   return  "비밀번호 수정 완료";
	   
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


