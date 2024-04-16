package com.example.demo.controller.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TLCustomerController {
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	
	@GetMapping("/login")
    public String login() {
        return "login";  
    }
	
	@GetMapping("/withdrawl")
	public String withdrawl() {
		return "withdrawl";
	}
	
	@GetMapping("/email")
    public String email() {
        return "email";  
    }
	
	@GetMapping("/password")
	public String password() {
		return "password";
	}
	
	@GetMapping("/password_reset")
	public String password_reset() {
		return "password_reset";
	}
	
	@GetMapping("/mypage_viewer")
	public String mypage_view() {
		return "mypage_viewer";
	}
	
	@GetMapping("/mypage_editor")
	public String mypage_edit() {
		return "mypage_editor";
	}
	
	@GetMapping("/consulting_history")
	public String consulting_history() {
		return "consulting_history";
	}

}
