package com.example.demo.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Post;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
//	List<Post> findAll(int postId);
	
	List<Post> findAllByPostTitleContaining(String keyword);
	
	List<Post> findAllByCustomer(Customer customer);
}
