package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.util.JwtAuthFilter;
import com.example.demo.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@RequiredArgsConstructor
@Configuration
//@EnableWebSecurity(debug = true)
public class SecurityConfig {
	
	private final JwtUtil jwtUtil;
	private static final String[] AUTH_WHITELIST = {
             "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html", "/api/v1/auth/**",  "/login", "/api/user", "/api/user/dispatch"
            , "/api/user/check", "/api/user/pw/**", "/login.html", "/css/**", "/js/**", "/index.html", "/assets/**","/email.html"
            , "/board.html",  "/tables.html", "/register.html", "/withdrawl.html", "/password.html","/","/favicon.ico","/seoul_municipalities_geo_simple.json"
            , "/password_reset.html", "/api/**", "/static/**", "/test.html", "/nav.html", "/templates/**", "/**", "/images/**", "/loan.html"
    };
	@Autowired
	public SecurityConfig(JwtUtil jwtUtil) {
	    this.jwtUtil = jwtUtil;
	}
//	public SecurityConfig( JwtUtil jwtUtil) {
//		this.jwtUtil = jwtUtil;
//	}
	
	@Bean // 스프링 빈으로 등록
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		
		return authenticationConfiguration.getAuthenticationManager();
	}

//	@Bean
//	public WebSecurityCustomizer webSecurityCustomizer() {
//	    return (web) -> web.ignoring().requestMatchers("swagger-ui.html");
//	}
	
	 @Bean
	    public InMemoryUserDetailsManager userDetailsService() {
	        UserDetails user = User.withDefaultPasswordEncoder()
	            .username("user1")
	            .password("password")
	            .roles("USER")
	            .build();
	        return new InMemoryUserDetailsManager(user);
	    }
	 
	
	@Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        http
	            // CSRF 보호 기능을 비활성화
	        	.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
	                SessionCreationPolicy.STATELESS))
	            .csrf().disable()
	            .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
	            .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        //@PreAuthrization을 사용할 것이기 때문에 모든 경로에 대한 인증처리는 Pass
//                        .anyRequest().permitAll()
                        .anyRequest().hasRole("USER")
                        );

	        return http.build();
	    }
	
//
//	public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity>{
//		
//		@Override
//		public void configure(HttpSecurity httpSecurity) throws Exception {
//			AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
//			
//			httpSecurity.addFilter(new JwtAuthenticationFilter(authenticationManager));
//			
//			super.configure(httpSecurity);
//		}	
//	}
//	
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		
//		// 1. CSRF 해제(서버에 인증정보 저장하지 않기 때문)
//		http.csrf((csrfConfig) -> csrfConfig.disable()) ;
//		
//		// 2. iframe 거부 설정
//		http.headers((headerConfig) -> headerConfig.frameOptions(frameOptionsConfig ->
//		frameOptionsConfig.sameOrigin()));
//		
//		// 3. cors 재설정
//		http.cors((corConfig) -> corConfig.configurationSource(configurationSource()));
		
		// 4.jSessionId 사용 거부
		
		
		// 5. form 로긴 해제 (UsernamePasswordAuthenticationFilter 비활성화) (폼 로그인 비활성화)
		
		
		
		// 6. 로그인 인증창이 뜨지 않게 비활성화(기본 인증 비활성화)
		
		
		// 7. 커스텀 필터 적용 (시큐리티 필터 교환) 
		
		
		
		// 8. 인증 실패 처리
		
		
		
		// 9. 권한 실패 처리
		
		
		
		// 10. 인증, 권한 필터 설정 - 경로에 대한 인증 설정
		
		
		// 11. 로그아웃 관련 설정
//	}
	
	
	
	
}

