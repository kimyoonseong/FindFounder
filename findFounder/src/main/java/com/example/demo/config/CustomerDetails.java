package com.example.demo.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.demo.model.dto.CustomerInfoDto;
import com.example.demo.model.entity.Customer;
import com.example.demo.service.CustomerService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomerDetails implements UserDetails  {
//	 private final CustomerService customerService;
	private  final CustomerInfoDto customerInfoDto;

//	    @Autowired
//	    public CustomerDetails(CustomerService customerService) {
//	        this.customerService = customerService;
//	    }

//	    @Override
//	    public UserDetails loadUserByUsername(String insertedUserId) throws UsernameNotFoundException {
//	        Customer findOne = customerService.findCustomerByCusId(insertedUserId);
////	        Member member = findOne.orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다 ㅠ"));
//
//	        return User.builder()
//	                .username(findOne.getCusId())
//	                .password(findOne.getCusPw())
//	                .roles("User")
//	                .build();
//	    }

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_" + customerInfoDto.getRole().toString());


        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

	 @Override
	    public String getPassword() {
	        return customerInfoDto.getCusPw();
	    }

	    @Override
	    public String getUsername() {
	        return customerInfoDto.getCusId().toString();
	    }


	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }
	
}
