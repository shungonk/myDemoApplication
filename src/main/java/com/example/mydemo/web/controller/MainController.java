package com.example.mydemo.web.controller;

import java.util.List;

import com.example.mydemo.web.model.Wallet;
import com.example.mydemo.web.service.WalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    WalletService walletService;
    
    @GetMapping(value="/home")
    public String home(Authentication authentication, Model model) {
        UserDetails user = (User) authentication.getPrincipal();
        List<Wallet> walletList = walletService.findByUsername(user.getUsername());
        model.addAttribute("walletList", walletList);
        return "home";
    }

    @GetMapping(value="/send")
    public String send(@RequestParam("name") String name, Authentication authentication, Model model) {
        UserDetails user = (User) authentication.getPrincipal();
        Wallet wallet = walletService.findByPrimaryKey(name, user.getUsername());
        model.addAttribute("wallet", wallet);
        return "send";
    }
}
