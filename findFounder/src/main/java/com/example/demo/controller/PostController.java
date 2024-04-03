package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.PostDto;
import com.example.demo.model.dto.req.PostCreateReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.dto.res.PostListRes;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/api/board")
public class PostController {

	private PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	// 회원코드는 JWT토큰 통해서 가져오기.
	@Operation(summary = "게시글 작성", description = "제목, 내용, 회원코드, 조회수는 0")
	@PostMapping()
	public ResponseEntity<CommonRes> createPost(@RequestBody PostCreateReq req ) {
		CommonRes res =  postService.createPost(req);
		
		return ResponseEntity.ok(res);
	}
	
	// 회원코드는 JWT토큰 통해서 가져오기.
	@Operation(summary = "게시글 수정", description = "제목, 내용, 회원코드, 조회수, 게시글ID")
	@PostMapping("/{postid}")
	public ResponseEntity<CommonRes> updatePost(@PathVariable int postid, @RequestBody PostCreateReq req ) {
		CommonRes res =  postService.updatePost(postid, req);
		
		return ResponseEntity.ok(res);
	}
	
	// 회원코드는 JWT토큰 통해서 가져오기.
	@Operation(summary = "게시글 전체 조회", description = "게시글 전체 조회")
	@GetMapping()
	public ResponseEntity<PostListRes> getPosts() {
		PostListRes res = PostListRes.builder().posts(postService.getPostList()).build();
		
		return ResponseEntity.ok(res);
	}
	
	@Operation(summary = "게시글 키워드 조회", description = "게시글 키워드 조회")
	@GetMapping("search/{keyword}")
	public ResponseEntity<PostListRes> getPostsByKeyword(@PathVariable String keyword) {
		PostListRes res = PostListRes.builder().posts(postService.getPostListByKeyword(keyword)).build();
		
		return ResponseEntity.ok(res);
	}
	
	@Operation(summary = "게시글 삭제", description = "게시글 삭제")
	@DeleteMapping("/{postid}")
	public ResponseEntity<CommonRes> deletePostById(@PathVariable int postid) {
		CommonRes res = postService.deletePost(postid);
		
		return ResponseEntity.ok(res);
	}
	
	@Operation(summary = "게시글 상세", description = "게시글 상세조회")
	@GetMapping("/{postid}")
	public ResponseEntity<PostDto> getPostDetailById(@PathVariable int postid) {
		PostDto postDto = postService.detailPost(postid);
		
		return ResponseEntity.ok(postDto);
	}
	
	
	
}
