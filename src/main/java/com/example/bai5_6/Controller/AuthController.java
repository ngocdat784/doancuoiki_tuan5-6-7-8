package com.example.bai5_6.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    // xử lý đăng ký
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password) {

        accountService.register(username, password);

        // ✅ đăng ký xong quay lại login
        return "redirect:/login";
    }
}
