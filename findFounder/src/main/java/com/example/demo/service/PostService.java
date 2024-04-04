package com.example.demo.service;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.PostDto;
import com.example.demo.model.dto.req.PostCreateReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Post;
import com.example.demo.repository.PostRepository;


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
	public CommonRes createPost(PostCreateReq postDto) {
		// customer 찾기
		Customer customer = Customer.builder().cusCode(3).build();
		Post post = Post.builder()
					.postTitle(postDto.getPostTitle())
					.postContent(postDto.getPostContent())
					.postViews(0)
					.customer(customer)
					.build();
		postRepo.saveAndFlush(post);
		CommonRes commonRes = CommonRes.builder().code(200).msg("게시글 작성 완료").build();
		return commonRes;
		
	}
	
	// 게시글 수정
	@Transactional
	public CommonRes updatePost(int postid, PostCreateReq postDto) {
		Post post = postRepo.findById(postid).orElseThrow(()->
		new IllegalArgumentException("해당 게시글이 없습니다."));
		post.setPostTitle(postDto.getPostTitle());
		post.setPostContent(postDto.getPostContent());
		postRepo.saveAndFlush(post);
		CommonRes commonRes = CommonRes.builder().code(200).msg("게시글 수정 완료").build();
		return commonRes;
	}
	
	// 게시글 상세
	public PostDto detailPost(int postId) {
		Post post = postRepo.findById(postId).orElseThrow(()->
		new IllegalArgumentException("해당 게시글이 없습니다."));
		post.setPostViews(post.getPostViews()+1);
		postRepo.saveAndFlush(post);
		PostDto postDto = post.toDto();
		return postDto;
	}
	
	
	// 게시글 전체 목록
	@Transactional(readOnly = true)
	public List<Post> getPostList(){
		List<Post> posts =  postRepo.findAllFetchJoin();
		
		return posts;
	}
	
	
	// 게시글 검색
	@Transactional
	public List<Post> getPostListByKeyword(String keyword){
		return postRepo.findAllByPostTitleContainingFetchJoin(keyword);
	}
	
	
	// 본인 작성한 게시글 리스트
	@Transactional
	public List<Post> getMyPostList( Customer customer){
		return postRepo.findAllByPostCustomerFetchJoin(customer);
	}
	
	// 게시글 삭제
	@Transactional
	public CommonRes deletePost(int post_id) {
		
		postRepo.deleteById(post_id);
		CommonRes commonRes = CommonRes.builder().code(200).msg("게시글 삭제 완료").build();
		return commonRes;
	}
	
	@Transactional
	public Post getPost(int postId) {
		return postRepo.findById(postId).orElseThrow(() -> new NoSuchElementException("해당하는 게시글이 없습니다."));
	}
	
	
	
	
}
