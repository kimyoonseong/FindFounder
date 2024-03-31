package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.PostRepository;
import com.example.demo.service.PostService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/board")
public class PostController {

	private PostService postService;
	
	@ApiOperation(value = "게시글 작성", notes = "게시글을 작성한다.")
	@PostMapping()
	public 
}
