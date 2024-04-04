package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
	
	void deleteAllByCustomer_CusCode(int cusCode);
	
	List<Comment> findAllByPost_PostId(int postId);
}
