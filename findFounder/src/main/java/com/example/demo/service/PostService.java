package com.example.demo.service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.PostDto;
import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Post;
import com.example.demo.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostService {
	private PostRepository postRepo;
	
	
	@Autowired
	public PostService(PostRepository postRepository) {
		this.postRepo = postRepository;
	}
	
	// 2024-03-31 CRUD 생성
	// 게시글 작성
	// 본인 작성 게시글 목록
	
	
	// 게시글 작성
	@Transactional
	public void createPost(PostDto postDto, Customer customer) {
		Post post = new Post();
		
		post = postDto.toEntity();
		post.setCustomer(customer);
		
		postRepo.saveAndFlush(post);
		
		
	}
	
	// 게시글 수정
	public void updatePost(PostDto postDto, Customer customer) {
		Post post = new Post();
		post = postDto.toEntity();
		
		post.setCustomer(customer);
		
		postRepo.saveAndFlush(post);
	}
	
	// 게시글 상세
	public Optional<Post> detailPost(int post_id) {
		return postRepo.findById(post_id);
	}
	
	
	// 게시글 전체 목록
	public Page<Post> getPostList(Pageable pageable){
	
		return postRepo.findAll(pageable);
	}
	
	
	// 게시글 검색
	public Page<Post> getPostListByKeyword(Pageable pageable, String keyword){
		return postRepo.findAllByTitleContaining(keyword);
	}
	
	
	// 본인 작성한 게시글 리스트
	public Page<Post> getMyPostList(Pageable pageable, Customer customer){
		return postRepo.findAllByCustomer(customer);
	}
	
	// 게시글 삭제
	public void deletePost(int post_id) {
		
		postRepo.deleteById(post_id);
	}
	
	
	
	
}
