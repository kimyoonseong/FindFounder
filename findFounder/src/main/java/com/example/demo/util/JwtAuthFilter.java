package com.example.demo.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.config.CustomerDetailsService;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

//	 private final CustomerDetailsService customerDetailsService;

	    private  JwtUtil jwtUtil;
	   
	    public JwtAuthFilter(JwtUtil jwtUtil) {
	    	this.jwtUtil = jwtUtil;
	    }
	    
	    
	    @Override
	    protected void doFilterInternal(HttpServletRequest servletRequest,
	        HttpServletResponse servletResponse,
	        FilterChain filterChain) throws ServletException, IOException {
	        String token = jwtUtil.resolveToken(servletRequest);



	        if (token != null && jwtUtil.validateToken(token)) {
	        	System.out.println("################## 유효한 토큰입니다.");
	            Authentication authentication = jwtUtil.getAuthentication(token);
	            System.out.println("유효1");
	            SecurityContextHolder.getContext().setAuthentication(authentication);
	            System.out.println("유효2");

	        }

	        filterChain.doFilter(servletRequest, servletResponse);
	    }
	    
	    
//	    @Override
	    /**
	     * JWT 토큰 검증 필터 수행
	     */
//	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//	        String authorizationHeader = request.getHeader("Authorization");
//	        String token = jwtUtil.resolveToken(request);
//	        System.out.println("#################################################################################22222들어와요");
//	        //JWT가 헤더에 있는 경우
//	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//	            String token = authorizationHeader.substring(7);
//	            //JWT 유효성 검증
//	            if (jwtUtil.validateToken(token)) {
//	                String userId = jwtUtil.getCusId(token);
//
//	                //유저와 토큰 일치 시 userDetails 생성
//	                UserDetails userDetails = customerDetailsService.loadUserByUsername(userId);
//
//	                if (userDetails != null) {
//	                    //UserDetsils, Password, Role -> 접근권한 인증 Token 생성
//	                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//	                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//	                    //현재 Request의 Security Context에 접근권한 설정
//	                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//	                }
//	            }
//	        }
//
//	 private  JwtUtil jwtUtil;
// 
//	 
//	 public JwtAuthFilter(JwtUtil jwtUtil) {
//		 this.jwtUtil = jwtUtil;
//		 
//	 }
//	 
//	 
//	 
//	 @Override
//	    protected void doFilterInternal(HttpServletRequest servletRequest,
//	        HttpServletResponse servletResponse,
//	        FilterChain filterChain) throws ServletException, IOException {
//	        String token = jwtUtil.resolveToken(servletRequest);
//	        
//
//	        
//	        if (token != null && jwtUtil.validateToken(token)) {
//	            Authentication authentication = jwtUtil.getAuthentication(token);
//	            SecurityContextHolder.getContext().setAuthentication(authentication);
//	            
//>>>>>>> Stashed changes
//	        }
//
//	        filterChain.doFilter(servletRequest, servletResponse);
//	    }
//	    @Override
//	    /**
//	     * JWT 토큰 검증 필터 수행
//	     */
//	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//	        String authorizationHeader = request.getHeader("Authorization");
//
//	        //JWT가 헤더에 있는 경우
//	        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//	            String token = authorizationHeader.substring(7);
//	            //JWT 유효성 검증
//	            if (jwtUtil.validateToken(token)) {
//	                String userId = jwtUtil.getCusId(token);
//
//	                //유저와 토큰 일치 시 userDetails 생성
//	                UserDetails userDetails = customerDetailsService.loadUserByUsername(userId.toString());
//
//	                if (userDetails != null) {
//	                    //UserDetsils, Password, Role -> 접근권한 인증 Token 생성
//	                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//	                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//	                    //현재 Request의 Security Context에 접근권한 설정
//	                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//	                }
//	            }
//	        }
//
//	        filterChain.doFilter(request, response); // 다음 필터로 넘기기
//	    }
}
