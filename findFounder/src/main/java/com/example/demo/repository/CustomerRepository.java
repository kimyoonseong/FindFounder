package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	Optional<Customer> findByCusId(String cusId);
	
	Customer findBYCusName(String cusName);

	void deleteByCusCode(int cuscode);

//	Optional<Customer> findById(String id);
	
}
