package com.quang.app.JavaWeb_cdquang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

	@GetMapping("/")
    public String home() {
        return "redirect:/user/home.html"; 
    }
	
	@GetMapping("/admin")
    public String admin() {
        return "redirect:/admin/home.html"; 
    }
	
	@GetMapping("/admin/")
    public String adminFlash() {
        return "redirect:/admin/home.html"; 
    }
}
