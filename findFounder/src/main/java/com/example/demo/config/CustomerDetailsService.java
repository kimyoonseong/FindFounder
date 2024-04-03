package com.example.demo.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.CustomerInfoDto;
import com.example.demo.model.entity.Customer;
import com.example.demo.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerDetailsService implements UserDetailsService  {
	 private final CustomerRepository customerRepository;


    @Override
    public UserDetails loadUserByUsername(String cusId) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByCusId(cusId)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));

        CustomerInfoDto dto = CustomerInfoDto.builder().cusId(customer.getCusId()).role("USER").cusPw(customer.getCusPw()).build();

        return new CustomerDetails(dto);
    }
}
