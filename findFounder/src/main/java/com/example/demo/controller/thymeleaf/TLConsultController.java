package com.example.demo.controller.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TLConsultController {
	@GetMapping("/buytoken")
    public String home() {
        return "buy-token";  
    }
	@GetMapping("/testlogin")
    public String login() {
        return "login";  
    }
	@GetMapping("/consult")
    public String consult() {
        return "consult";  
    }
	@GetMapping("/result")
	public String result() {
		return "result";
	}
	@GetMapping("/result_more")
	public String resultMore() {
		return "result_more";
	}
}
