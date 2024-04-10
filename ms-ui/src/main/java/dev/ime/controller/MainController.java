package dev.ime.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class MainController {

	@GetMapping("/index")
	public String index() {
		return "index";
	}	

	@GetMapping("/success")
	public String successLogin() {
		return "success";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
}
