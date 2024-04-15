package com.example.demo.controller.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TLPostController {

	
	@GetMapping("/table")
	public String getTable() {
		return "tables";
	}
	
	@GetMapping("/post/create")
	public String getCreatePost() {
		return "createPost";
	}
}
 