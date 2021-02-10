package com.example.mydemo.web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping(value="/home")
    public String home(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();


        model.addAttribute("useraccount", user);

        return "home";
    }
}
