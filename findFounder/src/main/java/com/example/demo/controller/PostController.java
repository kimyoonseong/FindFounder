package com.example.demo.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.example.demo.model.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.CustomerService;
import com.example.demo.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;



@RestController
@RequestMapping("/api/board")
@Tag(name = "2. 게시글", description = "게시글 컨트롤러 ")
public class PostController {

	private PostService postService;
	private CustomerRepository customerRepository;
	
	public PostController(PostService postService, CustomerRepository customerRepository) {
		this.postService = postService;
		this.customerRepository = customerRepository;
	}
	
	// 회원코드는 JWT토큰 통해서 가져오기.

	@Operation(summary = "게시글 작성", description = "제목, 내용, 회원코드, 조회수는 0"
			,
            parameters =  {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })

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
	@Operation(summary = "게시글 전체 조회", description = "게시글 전체 조회",
            parameters =  {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
	@GetMapping()
	public ResponseEntity<PostListRes> getPosts(Authentication authentication) {
		System.out.println("#################################################################################11111들어와요");
		System.out.println(authentication.getName());
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
	
	@Operation(summary = "내 게시글 조회", description = "내 게시글 조회")
	@GetMapping("/mypost")
	public ResponseEntity<PostListRes> getMyPostDetailById() {
		// jwt에서 cuscode 가져오기
		
		Customer customer = customerRepository.findById(3).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));
		PostListRes res  = PostListRes.builder().posts(postService.getMyPostList(customer)).build();
		
		return ResponseEntity.ok(res);
	}
	
	
	
	
}
