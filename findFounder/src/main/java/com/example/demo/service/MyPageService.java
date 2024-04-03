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
import com.example.demo.model.entity.Customer;
import com.example.demo.repository.ConsultRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PostRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor    
@Transactional(readOnly = true) // 읽기 전용 메서드
@Service
public class MyPageService {
	   private final CustomerRepository customerRepository;
	   private final PasswordEncoder passwordEncoder;
	   private final AuthenticationManager authenticationManager;
	   private final ConsultRepository consultRepository;
	   
}
