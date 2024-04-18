package com.example.demo.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.dto.PostDetailDto;
import com.example.demo.model.dto.PostDto;
import com.example.demo.model.dto.req.PostCreateReq;
import com.example.demo.model.dto.req.ReactionReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.entity.Customer;
import com.example.demo.model.entity.Post;
import com.example.demo.model.entity.Reaction;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ReactionRepository;
import com.example.demo.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostService {
	private PostRepository postRepo;
	private CustomerRepository customerRepository;
	private ReactionRepository reactionRepository;
	
	@Autowired
	public PostService(PostRepository postRepository, CustomerRepository customerRepository
			, ReactionRepository reactionRepository) {
		this.postRepo = postRepository;
		this.customerRepository = customerRepository;
		this.reactionRepository = reactionRepository;
	}
	
	// 2024-03-31 CRUD 생성
	// 게시글 작성
	// 본인 작성 게시글 목록
	
	
	// 게시글 작성
	@Transactional
	public CommonRes createPost(int cuscode, PostCreateReq postDto) {
		// customer 찾기
//		Customer customer = Customer.builder().cusCode(3).build();
		Customer customer = customerRepository.findById(cuscode).orElseThrow(() -> new NoSuchElementException("해당하는 사용자가 없습니다."));
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
	public CommonRes updatePost(int cuscode, int postid, PostCreateReq postDto) {
		
		Optional<Customer> customerOptional = customerRepository.findById(cuscode);
		CommonRes commonRes;
		if (customerOptional.isPresent()) {
			Post post = getPost(postid);
			if (cuscode == post.getCustomer().getCusCode()) {
				post.setPostTitle(postDto.getPostTitle());
				post.setPostContent(postDto.getPostContent());
				postRepo.saveAndFlush(post);
				commonRes = CommonRes.builder().code(200).msg("게시글 수정 완료되었습니다.").build();
			}
			else {
				commonRes = CommonRes.builder().code(300).msg("본인이 아니기 때문에 게시글 수정이 불가능합니다.").build();
			}	
		}
		else {
			commonRes = CommonRes.builder().code(400).msg("회원 정보가 존재하지 않습니다.").build();
		}
			
	
		return commonRes;
	}
	
	// 게시글 상세
	public PostDetailDto detailPost(int postId) {
		Post post = postRepo.findById(postId).orElseThrow(()->
		new IllegalArgumentException("해당 게시글이 없습니다."));
		post.setPostViews(post.getPostViews()+1);
		postRepo.saveAndFlush(post);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		PostDetailDto postDto = PostDetailDto.builder()
								.postTitle(post.getPostTitle())
								.postLike(post.getPostLike())
								.postDislike(post.getPostDislike())
								.postViews(post.getPostViews())
								.writer(post.getCustomer().getCusId())
								.postDate(sdf1.format(post.getPostDate()))
								.postId(post.getPostId())
								.postContent(post.getPostContent())
								.build();
		return postDto;
	}
	
	
	// 게시글 전체 목록
	@Transactional(readOnly = true)
	public Page<PostDetailDto> getPostList(int page){
		
		Pageable pageRequest = PageRequest.of(page, 5, Direction.DESC, "postDate");
//		Page<Post> posts = postRepo.findAllFetchJoin(pageable);
		List<Post> posts =  postRepo.findAllFetchJoin();
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		List<PostDetailDto> postList = new ArrayList<PostDetailDto>();
		for(Post post : posts) {
			PostDetailDto dto = PostDetailDto.builder()
								.postTitle(post.getPostTitle())
								.postLike(post.getPostLike())
								.postViews(post.getPostViews())
								.writer(post.getCustomer().getCusId())
								.postDate(sdf1.format(post.getPostDate()))
								.postId(post.getPostId())
								.build();
			postList.add(dto);
		}
		
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), postList.size());
		System.out.println("전체서비스" + start + "end" + end);
		Page<PostDetailDto> ListPage = new PageImpl<>(postList.subList(start, end), pageRequest, postList.size());

		return ListPage;
	}
	
	
	// 게시글 검색
	@Transactional(readOnly = true)
	public Page<PostDetailDto> getPostListByKeyword(int page, String keyword){
		Pageable pageRequest = PageRequest.of(page, 5, Direction.DESC, "postDate");
		List<Post> posts = postRepo.findAllByPostTitleContainingFetchJoin(keyword);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		List<PostDetailDto> postList = new ArrayList<PostDetailDto>();
		for(Post post : posts) {
			PostDetailDto dto = PostDetailDto.builder()
								.postTitle(post.getPostTitle())
								.postLike(post.getPostLike())
								.postViews(post.getPostViews())
								.writer(post.getCustomer().getCusId())
								.postDate(sdf1.format(post.getPostDate()))
								.postId(post.getPostId())
								.build();
			postList.add(dto);
		}
		
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), postList.size());
		System.out.println("검색서비스 start : " + start + " end : " + end + " 검색결과 : " + posts.size() + " page :" + page+" keyword : " + keyword);
		Page<PostDetailDto> ListPage = new PageImpl<>(postList.subList(start, end), pageRequest, postList.size());
		return ListPage;
	}
	
	
	// 본인 작성한 게시글 리스트
	@Transactional
	public List<Post> getMyPostList( Customer customer){
		
		return postRepo.findAllByPostCustomerFetchJoin(customer);
	}
	
	// 게시글 삭제
	@Transactional
	public CommonRes deletePost(int cuscode, int post_id) {
		
		Optional<Customer> customerOptional = customerRepository.findById(cuscode);
		CommonRes commonRes;
		if (customerOptional.isPresent()) {
			Post post = getPost(post_id);
			
			if (post.getCustomer().getCusCode() == cuscode) {
				postRepo.deleteById(post_id);
				commonRes = CommonRes.builder().code(200).msg("게시글 삭제가 완료되었습니다.").build();
			}
			else {
				commonRes = CommonRes.builder().code(300).msg("본인이 아니기 때문에 게시글 삭제가 불가능합니다.").build();
			}
		}
		else {
			commonRes = CommonRes.builder().code(400).msg("회원 정보가 존재하지 않습니다.").build();
		}
		
		return commonRes;
	}
	
	@Transactional
	public Post getPost(int postId) {
		return postRepo.findById(postId).orElseThrow(() -> new NoSuchElementException("해당하는 게시글이 없습니다."));
	}
	
	
	
	// Like, DisLike
	@Transactional
	public CommonRes doReaction(ReactionReq req, int cusCode, int postId) {
		// reactionId(PK), postId(FK), reaction(1 like, 0 dislike), cusCode(FK)
		Post post =  postRepo.findById(postId).orElseThrow(() -> new NoSuchElementException("해당하는 게시글이 없습니다."));
		Customer customer = customerRepository.findById(cusCode).orElseThrow(() -> new NoSuchElementException("해당하는 사용자가 없습니다."));
		Reaction reaction =   reactionRepository.findByPost_PostIdAndCustomer_CusCode(postId, cusCode);
		CommonRes commonRes = CommonRes.builder().code(100).msg("초기화").build();
		boolean isLike = false;
		// 1 like 0 dislike
		if (req.getReaction() == 1) {
			isLike = true;
		}
		// 1번 케이스 초기의 경우(무조건 생성)
		if (reaction == null) {
			reaction = Reaction.builder()
					.post(post)
					.customer(customer)
					.reaction(isLike)
					.build();
			reactionRepository.save(reaction);
			// True인 경우 좋아요
			if (isLike) {
				post.setPostLike(post.getPostLike()+1);
			}
			else {
				post.setPostDislike(post.getPostDislike() + 1);
			}
//			postRepo.save(post);
			commonRes = CommonRes.builder().code(200).msg("리액션 반영 완료").build();
		}
		// 이미 True(좋아요)
		else if (reaction.isReaction()){
			// 좋아요 2번은 삭제
			if (isLike) {
				reactionRepository.delete(reaction);
				post.setPostLike(post.getPostLike()-1);
				
				commonRes = CommonRes.builder().code(200).msg("리액션 좋아요 2번 클릭으로 인한 삭제 완료").build();
			}
			// 좋아요 -> 싫어요
			else {
				post.setPostLike(post.getPostLike()-1);
				post.setPostDislike(post.getPostDislike() + 1);
				
				reaction.setReaction(isLike);
				reactionRepository.save(reaction);
				commonRes = CommonRes.builder().code(200).msg("리액션 좋아요 후 싫어요 반영 완료").build();
			}
		}
		// 이미 False(싫어요)
		else if (! reaction.isReaction()) {
			if (isLike) {
				reaction.setReaction(isLike);
				reactionRepository.save(reaction);
				
				post.setPostLike(post.getPostLike() + 1);
				post.setPostDislike(post.getPostDislike() - 1);
				
				commonRes = CommonRes.builder().code(200).msg("리액션 싫어요 후 좋아요 반영 완료").build();
			}
			// 
			else {
				reactionRepository.delete(reaction);
				post.setPostDislike(post.getPostDislike() - 1);
				commonRes = CommonRes.builder().code(200).msg("리액션 싫어요 2번 클릭으로 인한 삭제 완료").build();
			}
		}
		postRepo.save(post);
		
		return commonRes;
	}
}
