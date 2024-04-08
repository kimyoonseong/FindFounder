package com.example.demo.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.example.demo.config.CustomerDetailsService;
import com.example.demo.model.dto.CustomerInfoDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
	
	
//    private final UserDetailsService userDetailsService; // Spring Security 에서 제공하는 서비스 레이어
    private  Key key;
    private final CustomerDetailsService customerDetailsService;
    
    
    @Value("${jwt.secret}")
    private String secretKey = "secretKey";
    private  long tokenValidMillisecond = 1000L * 60 * 60; // 1시간 토큰 유효

    /**
     * SecretKey 에 대해 인코딩 수행
     * 예제 13.11
     */
    @PostConstruct
    protected void init() {
       
//        System.out.println(secretKey);
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidMillisecond = tokenValidMillisecond;
        System.out.println(secretKey);
        
    }
//<<<<<<< Updated upstream
//    
//    /**
//     * Access Token 생성
//     * @param member
//     * @return Access Token String
//     */
//    public String createAccessToken(CustomerInfoDto customer) {
//        return createToken(customer, tokenValidMillisecond);
//=======

    // 예제 13.12
    // JWT 토큰 생성
    public String createToken(CustomerInfoDto customer) {
        
        Claims claims = Jwts.claims().setSubject(customer.getCusId());
        claims.put("roles", "USER");
        claims.put("cusCode", customer.getCusCode());

        Date now = new Date();
        String token = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
            .signWith(key, SignatureAlgorithm.HS256) // 암호화 알고리즘, secret 값 세팅
            .compact();

        return token;
//>>>>>>> Stashed changes
    }

    // 예제 13.13
    // JWT 토큰으로 인증 정보 조회
    public Authentication getAuthentication(String token) {
        
        UserDetails userDetails = customerDetailsService.loadUserByUsername(this.getCusId(token));
        
        return new UsernamePasswordAuthenticationToken(userDetails, "",
            userDetails.getAuthorities());
    }

    // 예제 13.14
    // JWT 토큰에서 회원 구별 정보 추출
    public String getCusId(String token) {
        
        String info = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    
        return info;
    }
    
    public int getCusCode(String token) {
    	int cusCode = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("cusCode",Integer.class);
    	return cusCode;
    }

    // 예제 13.15
    /**
     * HTTP Request Header 에 설정된 토큰 값을 가져옴
     *
     * @param request Http Request Header
     * @return String type Token 값
     */
    public String resolveToken(HttpServletRequest request) {
        
        return request.getHeader("X-AUTH-TOKEN");
    }

    // 예제 13.16
    // JWT 토큰의 유효성 + 만료일 체크
    public boolean validateToken(String token) {
        
        try {
//<<<<<<< Updated upstream
        	System.out.println("#################################################################################validateToken들어와요");
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println("#################################################################################validateToken들어와요22");
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }


    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
//    public Claims parseClaims(String accessToken) {
//        try {
//            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//=======
//            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            
//            return false;
//>>>>>>> Stashed changes
//        }
//    }
	
	
	
	
	
	
	
//	
//    private final Key key;
//    private final long accessTokenExpTime;
//
//    public JwtUtil(
//            @Value("${jwt.secret}") String secretKey,
//            @Value("${jwt.expiration_time}") long accessTokenExpTime
//    ) {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//        this.accessTokenExpTime = accessTokenExpTime;
//    }
//
//    /**
//     * Access Token 생성
//     * @param member
//     * @return Access Token String
//     */
//    public String createAccessToken(CustomerInfoDto customer) {
//        return createToken(customer, accessTokenExpTime);
//    }
//
//
//    /**
//     * JWT 생성
//     * @param member
//     * @param expireTime
//     * @return JWT String
//     */
//    private String createToken(CustomerInfoDto customer, long expireTime) {
//        Claims claims = Jwts.claims();
//        claims.put("customerId", customer.getCusId());
////        claims.put("email", customer.getEmail());
//        claims.put("role", customer.getRole());
//
//        ZonedDateTime now = ZonedDateTime.now();
//        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);
//
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(Date.from(now.toInstant()))
//                .setExpiration(Date.from(tokenValidity.toInstant()))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//
//    /**
//     * Token에서 User ID 추출
//     * @param token
//     * @return User ID
//     */
//    public Long getUserId(String token) {
//        return parseClaims(token).get("memberId", Long.class);
//    }
//
//
//    /**
//     * JWT 검증
//     * @param token
//     * @return IsValidate
//     */
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            log.info("Invalid JWT Token", e);
//        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT Token", e);
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT Token", e);
//        } catch (IllegalArgumentException e) {
//            log.info("JWT claims string is empty.", e);
//        }
//        return false;
//    }
//
//
//    /**
//     * JWT Claims 추출
//     * @param accessToken
//     * @return JWT Claims
//     */
//    public Claims parseClaims(String accessToken) {
//        try {
//            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }


}
