package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
	public CommonRes createComment(int cuscode, CommentCreateReq req) {
//		Customer customer = customerRepository.findById(req.getCusCode()).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));
		Customer customer = customerRepository.findById(cuscode).orElseThrow(() -> new UsernameNotFoundException("해당하는 사용자가 없습니다."));
		Post post = postService.getPost(req.getPostId());
		Comment comment = Comment.builder()
							.commentContent(req.getCommentContent())
							.post(post)
							.customer(customer)
							.build();
		commentRepository.saveAndFlush(comment);
		CommonRes commonRes = CommonRes.builder().code(200).msg("댓글 등록 완료되었습니다.").build();
		return commonRes;
	}
	
	// Update Comment
	@Transactional
	public CommonRes updateComment(int cuscode, CommentUpdateReq req) {
		Optional<Customer> customerOptional = customerRepository.findById(cuscode);
		
		CommonRes commonRes; 
		if (customerOptional.isPresent()) {

			Comment comment = findComment(req.getCommentId());

			if (comment.getCustomer().getCusCode() == cuscode) {
				comment.setCommentContent(req.getCommentContent());
		
				commentRepository.save(comment);
		
				commonRes = CommonRes.builder().code(200).msg("댓글 수정 완료되었습니다.").build();
				
			}
			else {
				commonRes = CommonRes.builder().code(300).msg("본인이 아니기 때문에 댓글 수정이 불가능합니다.").build();
			}
			
		}
		else {
			commonRes = CommonRes.builder().code(400).msg("회원 정보가 존재하지 않습니다.").build();
		}
		
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
	public CommonRes deleteComment(int cuscode, int commentId) {
		Optional<Customer> customerOptional = customerRepository.findById(cuscode);
		
		CommonRes commonRes; 
		if (customerOptional.isPresent()) {
			Comment comment = findComment(commentId);
			if (comment.getCustomer().getCusCode() == cuscode) {
				commentRepository.deleteById(commentId);
				commonRes = CommonRes.builder().code(200).msg("댓글 삭제 완료되었습니다.").build();
			}
			else {
				commonRes = CommonRes.builder().code(300).msg("본인이 아니기 때문에 댓글 삭제가 불가능합니다.").build();
			}
		} 
		else {
			commonRes = CommonRes.builder().code(400).msg("회원 정보가 존재하지 않습니다.").build();
		}
		
		return commonRes;
			
	}
			
		
}

