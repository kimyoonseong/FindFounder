package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.req.CommentCreateReq;
import com.example.demo.model.dto.req.CommentUpdateReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.entity.Comment;
import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Post;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	private CommentRepository commentRepository;
	private CustomerRepository customerRepository;
	private PostService postService;
	
	@Autowired
	public CommentService(CommentRepository commentRepository, CustomerRepository customerRepository, PostService postService) {
		this.commentRepository = commentRepository;
		this.customerRepository = customerRepository;
		this.postService = postService;
	}
	
	/*
	 * commentId
	 * commentContent
	 */
	@Transactional
	public Comment findComment(int commentId) {
		return commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("해당하는 댓글이 없습니다."));
	}
	
	
	// Create Comment
	@Transactional
	public CommonRes createComment(CommentCreateReq req) {
		Customer customer = customerRepository.findById(req.getCusCode()).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));
		Post post = postService.getPost(req.getPostId());
		Comment comment = Comment.builder()
							.commentContent(req.getCommentContent())
							.post(post)
							.customer(customer)
							.build();
		commentRepository.saveAndFlush(comment);
		CommonRes commonRes = CommonRes.builder().code(200).msg("댓글 등록 완료").build();
		return commonRes;
	}
	
	// Update Comment
	@Transactional
	public CommonRes updateComment(CommentUpdateReq req) {
		Comment comment = findComment(req.getCommentId());
		comment.setCommentContent(req.getCommentContent());
		
		commentRepository.save(comment);
		
		CommonRes commonRes = CommonRes.builder().code(200).msg("댓글 수정 완료").build();
		return commonRes;
	}
	
	// detail CommentList
	@Transactional
	public List<Comment> findCommentList(int postId){
		List<Comment> commentList = new ArrayList<>();
		commentList = commentRepository.findAllByPost_PostId(postId);
		
		return commentList;
	}
	
	// delete Comment
	@Transactional
	public CommonRes deleteComment(int commentId) {
		commentRepository.deleteById(commentId);
		CommonRes commonRes = CommonRes.builder().code(200).msg("댓글 삭제 완료").build();
		return commonRes;
	}
	
}
