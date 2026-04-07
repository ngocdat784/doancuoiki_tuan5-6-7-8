package com.example.bai5_6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bai5_6.Service.AccountService;

@Controller
public class AuthController {

    @Autowired
    private AccountService accountService;

    // hiển thị form
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
public String doRegister(@RequestParam String username,
                         @RequestParam String password,
                         Model model) {
    try {
        accountService.register(username, password);
        return "redirect:/login";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "register";
    }
}
}
