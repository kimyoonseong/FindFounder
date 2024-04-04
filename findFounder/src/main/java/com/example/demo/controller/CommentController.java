package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.req.CommentCreateReq;
import com.example.demo.model.dto.req.CommentUpdateReq;
import com.example.demo.model.dto.res.CommonRes;
import com.example.demo.model.entity.Comment;
import com.example.demo.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
	
	private CommentService commentService;
	
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	
	// 회원코드는 JWT토큰 통해서 가져오기.
	@Operation(summary = "댓글 작성", description = "제목, 댓글 내용")
	@PostMapping()
	public ResponseEntity<CommonRes> createComment(@RequestBody CommentCreateReq req ) {
		CommonRes res =  commentService.createComment(req);
		
		return ResponseEntity.ok(res);
	}
	
	@Operation(summary = "댓글 수정", description = "제목, 댓글 내용")
	@PatchMapping()
	public ResponseEntity<CommonRes> updateComment(@RequestBody CommentUpdateReq req ) {
		CommonRes res =  commentService.updateComment(req);
		
		return ResponseEntity.ok(res);
	}
	
	@Operation(summary = "댓글 리스트", description = "댓글 리스트")
	@GetMapping("/{postid}")
	public ResponseEntity<List<Comment>> getComments(@PathVariable int postid) {
		List<Comment> comments =  commentService.findCommentList(postid);
		
		return ResponseEntity.ok(comments);
	}
	
	@Operation(summary = "댓글 삭제", description = "댓글 삭제")
	@DeleteMapping("/{commentid}")
	public ResponseEntity<CommonRes> deleteComment(@PathVariable int commentid) {
		CommonRes res =  commentService.deleteComment(commentid);
		
		return ResponseEntity.ok(res);
	}
	
	
	

	

}
