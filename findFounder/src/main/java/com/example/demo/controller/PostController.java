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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.PostDto;
import com.example.demo.model.dto.req.PostCreateReq;
import com.example.demo.model.dto.req.ReactionReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.dto.res.PostListRes;
import com.example.demo.model.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.service.CustomerService;
import com.example.demo.service.PostService;
import com.example.demo.util.JwtUtil;

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
	private JwtUtil jwtUtil;
	
	public PostController(PostService postService, CustomerRepository customerRepository, JwtUtil jwtUtil) {
		this.postService = postService;
		this.customerRepository = customerRepository;
		this.jwtUtil = jwtUtil;
	}
	
	// 회원코드는 JWT토큰 통해서 가져오기.

	@Operation(summary = "게시글 작성", description = "제목, 내용, 회원코드, 조회수는 0"
			,
            parameters =  {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })

	@PostMapping()
	public ResponseEntity<CommonRes> createPost(@RequestHeader("X-AUTH-TOKEN") String jwtToken, @RequestBody PostCreateReq req ) {
		
		int cusCode = jwtUtil.getCusCode(jwtToken);
		CommonRes res =  postService.createPost(cusCode, req);
		
		
		return ResponseEntity.ok(res);
	}
	
	// 회원코드는 JWT토큰 통해서 가져오기.
		@Operation(summary = "게시글 수정", description = "제목, 내용, 회원코드, 조회수, 게시글ID"
				,
	            parameters =  {
	                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
	            })
		@PostMapping("/{postid}")
		public ResponseEntity<CommonRes> updatePost(@RequestHeader("X-AUTH-TOKEN") String jwtToken, @PathVariable int postid, @RequestBody PostCreateReq req ) {
			
			int cusCode = jwtUtil.getCusCode(jwtToken);
			CommonRes res =  postService.updatePost(cusCode, postid, req);
			
			return ResponseEntity.ok(res);
		}
	
	// 회원코드는 JWT토큰 통해서 가져오기.
	@Operation(summary = "게시글 전체 조회", description = "게시글 전체 조회",
            parameters =  {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
	@GetMapping()
	public ResponseEntity<PostListRes> getPosts(Authentication authentication) {

		PostListRes res = PostListRes.builder().posts(postService.getPostList()).build();
		
		return ResponseEntity.ok(res);
	}
	
	@Operation(summary = "게시글 키워드 조회", description = "게시글 키워드 조회"
			,
            parameters =  {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
	@GetMapping("search/{keyword}")
	public ResponseEntity<PostListRes> getPostsByKeyword(@PathVariable String keyword) {
		PostListRes res = PostListRes.builder().posts(postService.getPostListByKeyword(keyword)).build();
		
		return ResponseEntity.ok(res);
	}
	
	@Operation(summary = "게시글 삭제", description = "게시글 삭제"
			,
            parameters =  {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
	@DeleteMapping("/{postid}")
	public ResponseEntity<CommonRes> deletePostById(@RequestHeader("X-AUTH-TOKEN") String jwtToken, @PathVariable int postid) {
		
		int cusCode = jwtUtil.getCusCode(jwtToken);
		
		CommonRes res = postService.deletePost(cusCode,postid);
		
		return ResponseEntity.ok(res);
	}
	
	@Operation(summary = "게시글 상세", description = "게시글 상세조회"
			,
            parameters =  {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
	@GetMapping("/{postid}")
	public ResponseEntity<PostDto> getPostDetailById(@PathVariable int postid) {
		PostDto postDto = postService.detailPost(postid);
		
		return ResponseEntity.ok(postDto);
	}
	
	@Operation(summary = "내 게시글 조회", description = "내 게시글 조회"
			,
            parameters =  {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
	@GetMapping("/mypost")
	public ResponseEntity<PostListRes> getMyPostDetailById(@RequestHeader("X-AUTH-TOKEN") String jwtToken) {
		// jwt에서 cuscode 가져오기
		int cusCode = jwtUtil.getCusCode(jwtToken);
		Customer customer = customerRepository.findById(cusCode).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));
		PostListRes res  = PostListRes.builder().posts(postService.getMyPostList(customer)).build();
		
		
		return ResponseEntity.ok(res);
	}
	
	
	@Operation(summary = "리액션 추가", description = "리액션 True : 좋아요, False : 싫어요"
			,
            parameters =  {
                    @Parameter(name = "X-AUTH-TOKEN", description = "JWT Token", required = true, in = HEADER)
            })
	@PostMapping("reaction/{postid}")
	public ResponseEntity<CommonRes> updateReaction(@PathVariable int postid,
			@RequestHeader("X-AUTH-TOKEN") String jwtToken, 
			@RequestBody ReactionReq req ) {
		
		
		int cuscode = jwtUtil.getCusCode(jwtToken);
		
		CommonRes res =  postService.doReaction(req, cuscode, postid);
		
		return ResponseEntity.ok(res);
	}
	
	
	
	
	
}
