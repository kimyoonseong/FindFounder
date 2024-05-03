package com.example.demo.controller;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.CommentDetailDto;
import com.example.demo.model.dto.req.CommentCreateReq;
import com.example.demo.model.dto.req.CommentUpdateReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.entity.Comment;
import com.example.demo.service.CommentService;
import com.example.demo.util.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/comment")
@Tag(name = "3. 댓글", description = "댓글 관련 컨트롤러 ")
/*
Comment Controller
1. createComment : 댓글 작성
2. updateComment : 댓글 수정
3. getComments   : 게시글에 있는 전체 댓글 리스트
4. deleteComment : 댓글 삭제
*/
public class CommentController {
	
	private CommentService commentService;
	private JwtUtil jwtUtil;
	
	public CommentController(CommentService commentService, JwtUtil jwtUtil) {
		this.commentService = commentService;
		this.jwtUtil = jwtUtil;
	}
	
	
	// 댓글 작성
	@Operation(summary = "댓글 작성", description = "제목, 댓글 내용")
	@PostMapping()
	public ResponseEntity<CommonRes> createComment(@CookieValue(name = "Set-Cookie", required = false) String jwtToken
			, @RequestBody CommentCreateReq req ) {
		log.info("[Log] 댓글 작성 : {}", jwtUtil.getCusId(jwtToken));
		int cuscode = jwtUtil.getCusCode(jwtToken);
		CommonRes res =  commentService.createComment(cuscode, req);
		
		return ResponseEntity.ok(res);
	}
	
	
	// 댓글 수정
	@Operation(summary = "댓글 수정", description = "제목, 댓글 내용")
	@PatchMapping()
	public ResponseEntity<CommonRes> updateComment(@CookieValue(name = "Set-Cookie", required = false) String jwtToken, @RequestBody CommentUpdateReq req ) {
		log.info("[Log] 댓글 수정 : {}", jwtUtil.getCusId(jwtToken));
		int cuscode = jwtUtil.getCusCode(jwtToken);
		
		CommonRes res =  commentService.updateComment(cuscode, req);

		
		return ResponseEntity.ok(res);
	}
	
	
	// 댓글 리스트
	@Operation(summary = "댓글 리스트", description = "댓글 리스트")
	@GetMapping()
	public ResponseEntity<List<CommentDetailDto>> getComments(@RequestParam(required = false) int postid) {
		List<CommentDetailDto> comments =  commentService.findCommentList(postid);
		
		return ResponseEntity.ok(comments);
	}
	
	
	// 댓글 삭제
	@Operation(summary = "댓글 삭제", description = "댓글 삭제")
	@DeleteMapping("/{commentid}")
	public ResponseEntity<CommonRes> deleteComment(@CookieValue(name = "Set-Cookie", required = false) String jwtToken
			, @PathVariable int commentid) {
		log.info("[Log] 댓글 삭제 : {}", jwtUtil.getCusId(jwtToken));
		int cuscode = jwtUtil.getCusCode(jwtToken);
		CommonRes res =  commentService.deleteComment(cuscode, commentid);
		
		return ResponseEntity.ok(res);
	}
	
	
	

	

}