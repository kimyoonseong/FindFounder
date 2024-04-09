package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction, Integer>{
	Reaction findByPost_PostIdAndCustomer_CusCode(int postId, int cusCode);
}
