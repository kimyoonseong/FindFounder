package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.CustomerInfoDto;
import com.example.demo.model.dto.LoginReq;
import com.example.demo.model.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.util.JwtUtil;

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
    public String login(LoginReq dto) {
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
        
        CustomerInfoDto info = CustomerInfoDto.builder().cusId(customer.getCusId()).role("USER").cusPw(customer.getCusPw()).build();

        String accessToken = jwtUtil.createAccessToken(info);
        return accessToken;
    }

}
