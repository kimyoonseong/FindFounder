package com.example.demo.repository;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Post;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
	Page<Post> findAll(Pageable pageable);
	
	Page<Post> findAllByTitleContaining(String keyword);
	
	Page<Post> findAllByCustomer(Customer customer);
}
