package com.example.FinalProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {


// Show login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; 
    }

    // Handle login form submission
    @PostMapping("/login")
    public String processLogin(@RequestParam String user,
                               @RequestParam String password,
                               Model model) {
        // For testing: accept any username/password
        model.addAttribute("username", user);
        return "home"; 
    }

    // Show register page
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    // Handle register form submission
    @PostMapping("/register")
    public String processRegister(@RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  Model model) {
        // For testing: just echo back the name
        model.addAttribute("username", name);
        return "home";
    }

    @GetMapping("/logout")
public String logout() {
    
    return "redirect:/login";
}

}
