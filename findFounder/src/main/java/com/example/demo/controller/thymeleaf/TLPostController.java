package com.example.demo.controller.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TLPostController {

	
	@GetMapping("/post-list")
	public String getTable() {
		return "postList";
	}
	
	@GetMapping("/post-create")
	public String getCreatePost() {
		return "postCreate";
	}
	
	@GetMapping("post-details")
	public String getPostDetail() {
		return "postDetail";
	}
}
 