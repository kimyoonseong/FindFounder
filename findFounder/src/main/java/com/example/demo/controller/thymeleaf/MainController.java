package com.example.demo.controller.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	
	@GetMapping("/test")
    public String home() {
        return "test";  
    }
	
	@GetMapping("/nav")
	public String home2() {
        return "nav";  
    }
	
	@GetMapping("/test2")
	public String test2() {
		return "test2";
	}
	
	@GetMapping("/")
	public String test3() {
		return "index";
	}
}
