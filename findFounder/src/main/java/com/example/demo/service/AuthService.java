package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.CustomerInfoDto;
import com.example.demo.model.dto.LoginReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.dto.res.LoginRes;
import com.example.demo.model.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.util.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
	
	private  JwtUtil jwtUtil;
	private  CustomerRepository customerRepository;
	private  PasswordEncoder encoder;
	
	
	@Autowired
	public AuthService(JwtUtil jwtUtil, CustomerRepository customerRepository,  PasswordEncoder encoder) {
		this.jwtUtil = jwtUtil;
		this.customerRepository = customerRepository;
		this.encoder = encoder;
	}
	
	
	
    @Transactional
    public LoginRes login(LoginReq dto) {
        String cusId = dto.getCusId();
        String password = dto.getCusPw();
        Customer customer = customerRepository.findByCusId(cusId).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));
        if(customer == null) {
        	System.out.println("살려줘 유저가 없어요");
//            throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
        }

        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
        if(!encoder.matches(password, customer.getCusPw())) {
        	System.out.println("살려줘 비밀번호 안맞아요");
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        
        CustomerInfoDto info = CustomerInfoDto.builder().cusId(customer.getCusId()).role("USER").cusPw(customer.getCusPw()).
        		cusCode(customer.getCusCode()). build();

        String accessToken = jwtUtil.createToken(info);
        LoginRes loginRes = LoginRes.builder().code(200).msg("로그인 완료되었습니다.").token(accessToken).build();
        return loginRes;
    }
    
//    @Transactional
//    public void logout(String encryptedRefreshToken, String accessToken) {
//    	this.verifiedRefreshToken(encryptedRefreshToken);
//    	String refreshToken = aes128Config.decryptAes(encryptedRefreshToken);
//        Claims claims = jwtUtil.parseClaims(refreshToken);
//        String email = claims.getSubject();
//        String redisRefreshToken = redisService.getValues(email);
//        if (redisService.checkExistsValue(redisRefreshToken)) {
//            redisService.deleteValues(email);
//
//            // 로그아웃 시 Access Token Redis 저장 ( key = Access Token / value = "logout" )
//            long accessTokenExpirationMillis = JwtUtil.getAccessTokenExpirationMillis();
//            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenExpirationMillis));
//        } 
//    }
//
//
//
//	private void verifiedRefreshToken(String encryptedRefreshToken) {
//		if (encryptedRefreshToken == null) {
//            throw new BusinessLogicException(ExceptionCode.HEADER_REFRESH_TOKEN_NOT_EXISTS);
//        }
//	}
//
//	  private Customer findMemberByEmail(String email) {
//	        return CustomerRepository.findByEmail(email)
//	                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
//	  }
}
